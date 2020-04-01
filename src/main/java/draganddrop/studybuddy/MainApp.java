package draganddrop.studybuddy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.Config;
import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.commons.core.Version;
import draganddrop.studybuddy.commons.exceptions.DataConversionException;
import draganddrop.studybuddy.commons.util.ConfigUtil;
import draganddrop.studybuddy.commons.util.StringUtil;
import draganddrop.studybuddy.logic.Logic;
import draganddrop.studybuddy.logic.LogicManager;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.ReadOnlyUserPrefs;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.util.SampleDataUtil;
import draganddrop.studybuddy.storage.JsonStudyBuddyStorage;
import draganddrop.studybuddy.storage.JsonUserPrefsStorage;
import draganddrop.studybuddy.storage.Storage;
import draganddrop.studybuddy.storage.StorageManager;
import draganddrop.studybuddy.storage.StudyBuddyStorage;
import draganddrop.studybuddy.storage.UserPrefsStorage;
import draganddrop.studybuddy.ui.Ui;
import draganddrop.studybuddy.ui.UiManager;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    final public static Version VERSION = new Version(1, 2, 1, true);

    final private static Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing StudyBuddy ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        StudyBuddyStorage studyBuddyStorage = new JsonStudyBuddyStorage(userPrefs.getStudyBuddyFilePath());
        storage = new StorageManager(studyBuddyStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting StudyBuddy " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Study Buddy ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s study buddy and {@code userPrefs}. <br>
     * The data from the sample study buddy will be used instead if {@code storage}'s study buddy is not found,
     * or an empty study buddy will be used instead if errors occur when reading {@code storage}'s study buddy.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyStudyBuddy> studyBuddyOptional;
        ReadOnlyStudyBuddy initialData;
        try {
            studyBuddyOptional = storage.readStudyBuddy();
            if (!studyBuddyOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample StudyBuddy");
            }
            initialData = studyBuddyOptional.orElseGet(SampleDataUtil::getSampleStudyBuddy);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty StudyBuddy");
            initialData = new StudyBuddy();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty StudyBuddy");
            initialData = new StudyBuddy();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty StudyBuddy");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }
}
