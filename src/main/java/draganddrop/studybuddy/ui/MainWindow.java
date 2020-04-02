package draganddrop.studybuddy.ui;

import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.Logic;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.ui.box.CalendarBox;
import draganddrop.studybuddy.ui.box.CommandBox;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.add.CreateModuleInteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.edit.EditModInteractivePrompt;
import draganddrop.studybuddy.ui.panel.DueSoonListPanel;
import draganddrop.studybuddy.ui.panel.ModuleListPanel;
import draganddrop.studybuddy.ui.panel.TaskListPanel;
import draganddrop.studybuddy.ui.panel.TaskSummaryPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {
    private static final String ALL_TASK = "All Tasks";
    private static final String TITLE = "Study Buddy";
    private static final String DUE_SOON_TASK = "Tasks Due Soon";
    private static final String MODULE = "Modules";
    private static final String ARCHIVED_TASK = "Archived Task";
    private static final String CALENDAR = "Calendar";
    private static final String TASK_SUMMARY = "Task Summary";
    private static final String PRODUCTIVITY_PAGE = "Your Productivity";

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

    private CommandBox commandBox;

    private ProductivityPage productivityPage;

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

    @FXML
    private StackPane productivityPageHolder;

    @FXML
    private Label taskListPanelTitle;

    @FXML
    private Pane taskListPanelTitleHolder;

    @FXML
    private Label dueListPanelTitle;

    @FXML
    private Pane dueListPanelTitleHolder;

    @FXML
    private Label mainTitle;

    @FXML
    private Pane mainTitleHolder;


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
            logic.getFilteredArchivedTaskList(), logic.getFilteredModuleList(),
            dueSoonListPanelPlaceholder, dueListPanelTitle);
        taskSummaryHolder.getChildren().add(taskSummaryPanel.getRoot());
        taskSummaryHolder.setVisible(false);
        taskSummaryHolder.setManaged(false);

        //Productivity page
        productivityPage = new ProductivityPage(logic.getFilteredTaskList());
        productivityPageHolder.getChildren().add(productivityPage.getRoot());
        productivityPageHolder.setVisible(false);
        productivityPageHolder.setManaged(false);

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

        commandBox = new CommandBox(this::executeCommand);
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
        toggleTaskSummaryHolderView(true);
        toggleTaskListHolderView(false, true);
        taskSummaryPanel.renderSelectedListPanel();

        toggleAllTitle(false);
        toggleDueSoonTaskListPanelTitleView(true);
        toggleMainTitleView(true);
        setMainTitleText(TASK_SUMMARY);
    }

    void show() {
        primaryStage.show();
    }


    /**
     * handles showing the productivity page
     */
    @FXML
    private void handleShowProductivity() {
        toggleAllHoldersInvisible();
        toggleProductivityHolderView(true);

        toggleAllTitle(false);
        toggleMainTitleView(true);
        setMainTitleText(PRODUCTIVITY_PAGE);
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
        toggleTaskListHolderView(true, true);

        toggleAllTitle(true);
        setMainTitleText(TITLE);
        setTaskListTitleText(ALL_TASK);
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
        toggleTaskListHolderView(true, true);

        toggleAllTitle(true);
        setMainTitleText(TITLE);
        dueListPanelTitle.setText(DUE_SOON_TASK);
        dueListPanelTitle.getStyleClass().clear();
        dueListPanelTitle.getStyleClass().add("sub_header");
        dueSoonListPanel = new DueSoonListPanel(logic.getFilteredDueSoonTaskList());
        dueSoonListPanelPlaceholder.getChildren().add(dueSoonListPanel.getRoot());
    }

    /**
     * handles archived task to be shown in TaskListHolder.
     */
    @FXML
    private void handleShowArchivedTasks() {
        toggleAllHoldersInvisible();
        toggleTaskListHolderView(true, true);

        toggleAllTitle(true);
        setMainTitleText(TITLE);
        setTaskListTitleText(ARCHIVED_TASK);
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
        toggleModTabView(true);
        toggleProductivityHolderView(false);

        toggleAllTitle(false);
        toggleMainTitleView(true);
        setMainTitleText(MODULE);
    }

    /**
     * on clicked: leads to createModuleInteractivePrompt.
     */
    @FXML
    private void handleCreateMod() {
        handleShowModules();
        commandBox.run(new CreateModuleInteractivePrompt());
        commandBox.handleCommandEntered();

    }

    /**
     * on clicked: leads to EditModInteractivePrompt.
     */
    @FXML
    private void handleEditMod() {
        handleShowModules();
        commandBox.run(new EditModInteractivePrompt());
        commandBox.handleCommandEntered();
    }


    /**
     * handles calendar to be shown in TaskListHolder.
     */
    @FXML
    private void handleShowCalendar() {
        toggleAllHoldersInvisible();
        toggleTaskListHolderView(true, true);

        toggleAllTitle(true);
        toggleTaskListPanelTitleView(false);
        setMainTitleText(CALENDAR);

        CalendarBox calendar = new CalendarBox(logic.getFilteredTaskList(), dueSoonListPanelPlaceholder,
            dueListPanelTitle);
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
    private CommandResult executeCommand(InteractivePrompt currentInteractivePrompt, String commandText) {
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
     * Toggles list panels according to values assigned. Can add more if there are more windows required for display.
     *
     * @param val1 toggles TaskListHolder
     * @param val2 toggles TaskSummaryHolder
     * @param val3 toggles ModuleTabHolder
     */
    private void customToggleHolders(boolean val1, boolean val2, boolean val3, boolean val4) {
        toggleTaskListHolderView(val1, val1);
        toggleTaskSummaryHolderView(val2);
        toggleModTabView(val3);
        toggleProductivityHolderView(val4);
    }


    /**
     * Toggles title according to values assigned. Can add more if there are more windows required for display.
     *
     * @param val1 toggles MainTitle
     * @param val2 toggles TaskList(Left side)
     */
    private void customToggleTitle(boolean val1, boolean val2/*, boolean val3*/) {
        toggleMainTitleView(val1);
        toggleTaskListPanelTitleView(val2);
        //setDueSoonTaskListPanelTitleView(val3);
    }

    private void toggleAllTitle(boolean val) {
        customToggleTitle(val, val);
    }

    private void toggleAllHoldersInvisible() {
        customToggleHolders(false, false, false, false);
    }

    private void toggleTaskSummaryHolderView(boolean val) {
        taskSummaryHolder.setVisible(val);
        taskSummaryHolder.setManaged(val);
        setPaneView(taskSummaryHolder, val);
    }

    /**
     * Toggles the visibility of taskListHolder.
     *
     * @param isAllTaskListShow
     * @param isDueSoonShow
     */
    private void toggleTaskListHolderView(boolean isAllTaskListShow, boolean isDueSoonShow) {
        setPaneView(taskListPanelPlaceholder, isAllTaskListShow);
        setPaneView(taskListPanelTitleHolder, isAllTaskListShow);
        setPaneView(dueSoonListPanelPlaceholder, isDueSoonShow);
        setPaneView(dueListPanelTitleHolder, isDueSoonShow);
        if (isAllTaskListShow || isDueSoonShow) {
            taskListHolder.setManaged(true);
            taskListHolder.setVisible(true);
        } else {
            taskListHolder.setManaged(false);
            taskListHolder.setVisible(false);
        }
    }

    private void toggleModTabView(boolean val) {
        setPaneView(modulePaneHolder, val);
    }

    private void toggleProductivityHolderView(boolean val) {
        setPaneView(productivityPageHolder, val);
    }


    private void toggleMainTitleView(boolean val) {
        setPaneView(mainTitleHolder, val);
    }

    private void toggleTaskListPanelTitleView(boolean val) {
        setPaneView(taskListPanelTitleHolder, val);
    }

    private void toggleDueSoonTaskListPanelTitleView(boolean val) {
        setPaneView(dueListPanelTitleHolder, val);
    }

    private void setTaskListTitleText(String text) {
        taskListPanelTitle.setText(text);
    }

    private void setMainTitleText(String text) {
        mainTitle.setText(text);
    }

    private void setPaneView(Pane pane, boolean val) {
        pane.setVisible(val);
        pane.setManaged(val);
    }
}
