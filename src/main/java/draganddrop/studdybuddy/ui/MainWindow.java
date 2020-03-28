package draganddrop.studdybuddy.ui;

import java.util.logging.Logger;

import draganddrop.studdybuddy.commons.core.GuiSettings;
import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.logic.Logic;
import draganddrop.studdybuddy.logic.commands.CommandResult;
import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.logic.parser.exceptions.ParseException;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePrompt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private TaskListPanel taskListPanel;
    private TaskSummaryPanel taskSummaryPanel;
    private DueSoonListPanel dueSoonListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private ModuleListPanel moduleListPanel;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private HBox taskListHolder;

    @FXML
    private StackPane taskListPanelPlaceholder;

    @FXML
    private StackPane dueSoonListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane timeLeftbarPlaceholder;

    @FXML
    private StackPane taskSummaryHolder;

    @FXML
    private StackPane modulePaneHolder;


    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        taskListPanel = new TaskListPanel(logic.getFilteredTaskList());
        taskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());

        taskSummaryPanel = new TaskSummaryPanel(logic.getFilteredTaskList(),
            logic.getFilteredArchivedTaskList());
        taskSummaryHolder.getChildren().add(taskSummaryPanel.getRoot());
        taskSummaryHolder.setVisible(false);
        taskSummaryHolder.setManaged(false);

        moduleListPanel = new ModuleListPanel(logic.getFilteredModuleList(), logic.getFilteredTaskList());
        modulePaneHolder.getChildren().add(moduleListPanel.getRoot());
        modulePaneHolder.setVisible(false);
        modulePaneHolder.setManaged(false);

        dueSoonListPanel = new DueSoonListPanel(logic.getFilteredDueSoonTaskList());
        dueSoonListPanelPlaceholder.getChildren().add(dueSoonListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getStudyBuddyFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    /**
     * Shows the task summaries.
     */
    @FXML
    public void handleShowTaskSummary() {
        toggleAllHoldersInvisible();
        setTaskSummaryHolderView(true);
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
            (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Shows all task list in taskListHolder.
     */
    @FXML
    private void handleShowAllTasks() {
        toggleAllHoldersInvisible();
        setTaskListHolderView(true);
        taskListPanel = new TaskListPanel(logic.getFilteredTaskList());
        taskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());
        handleDueSoonTasks();
    }

    /**
     * handles task to be shown in TaskListHolder.
     */
    @FXML
    private void handleDueSoonTasks() {
        toggleAllHoldersInvisible();
        setTaskListHolderView(true);
        dueSoonListPanel = new DueSoonListPanel(logic.getFilteredDueSoonTaskList());
        dueSoonListPanelPlaceholder.getChildren().add(dueSoonListPanel.getRoot());
    }

    /**
     * handles archived task to be shown in TaskListHolder.
     */
    @FXML
    private void handleShowArchivedTasks() {
        toggleAllHoldersInvisible();
        setTaskListHolderView(true);
        TaskListPanel archiveListPanel = new TaskListPanel(logic.getFilteredArchivedTaskList());
        taskListPanelPlaceholder.getChildren().add(archiveListPanel.getRoot());
        handleDueSoonTasks();
    }

    /**
     * handles modules to be displayed in ModulesTabHolder.
     */
    @FXML
    private void handleShowModules() {
        toggleAllHoldersInvisible();
        setModTabView(true);
    }

    /**
     * handles calendar to be shown in TaskListHolder.
     */
    @FXML
    private void handleShowCalendar() {
        toggleAllHoldersInvisible();
        setTaskListHolderView(true);
        CalendarBox calendar = new CalendarBox(logic.getFilteredTaskList(), dueSoonListPanelPlaceholder);
        taskListPanelPlaceholder.getChildren().add(calendar.getRoot());
    }

    public TaskListPanel getPersonListPanel() {
        return taskListPanel;
    }

    public DueSoonListPanel getDueSoonListPanel() {
        return dueSoonListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    private CommandResult executeCommand(InteractivePrompt currentInteractivePrompt, String commandText)
        throws CommandException, ParseException {

        currentInteractivePrompt.setLogic(logic);
        String reply = currentInteractivePrompt.interact(commandText);
        resultDisplay.setFeedbackToUser(reply);
        if (currentInteractivePrompt.isQuit()) {
            handleExit();
        }

        if (currentInteractivePrompt.isEndOfCommand()) {
            return new CommandResult("Set current interactive to null", false, false);
        }
        return null;
    }

    /**
     * Toggles according to values assigned. Can add more if there are more windows required for display.
     * @param val1 toggles TaskListHolder
     * @param val2 toggles TaskSummaryHolder
     * @param val3 toggles ModuleTabHolder
     */
    private void customToggleHolders(boolean val1, boolean val2, boolean val3) {
        setTaskListHolderView(val1);
        setTaskSummaryHolderView(val2);
        setModTabView(val3);
    }

    private void toggleAllHoldersInvisible() {
        customToggleHolders(false, false, false);
    }


    private void setTaskSummaryHolderView(boolean val) {
        taskSummaryHolder.setVisible(val);
        taskSummaryHolder.setManaged(val);
    }

    private void setTaskListHolderView(boolean val) {
        taskListHolder.setVisible(val);
        taskListHolder.setManaged(val);
    }

    private void setModTabView(boolean val) {
        modulePaneHolder.setVisible(val);
        modulePaneHolder.setManaged(val);
    }


    /*
    private void toggleHolder() {
        if (!taskListHolder.isManaged()) {
            //toggleTaskSummaryHolder();
            //toggleTaskListHolder();
            //toggleModuleTabs();
        }
    }

    private void toggleModuleTabs() {
        if (modulePaneHolder.isManaged()) {
            modulePaneHolder.setVisible(false);
            modulePaneHolder.setManaged(false);
        } else {
            modulePaneHolder.setVisible(true);
            modulePaneHolder.setManaged(true);
        }
    }

    private void toggleTaskSummaryHolder() {
        if (taskSummaryHolder.isManaged()) {
            taskSummaryHolder.setVisible(false);
            taskSummaryHolder.setManaged(false);
        } else {
            taskSummaryHolder.setManaged(true);
            taskSummaryHolder.setVisible(true);
        }
    }

    private void toggleTaskListHolder() {
        if (taskListHolder.isManaged()) {
            taskListHolder.setVisible(false);
            taskListHolder.setManaged(false);
        } else {
            taskListHolder.setManaged(true);
            taskListHolder.setVisible(true);
        }
    }
    */
}
