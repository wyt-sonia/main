package draganddrop.studybuddy.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.testutil.TypicalTasks;


public class JsonAdaptedTaskTest {

    /*private static String INVALID_DATE_TIME = "";
    private static String VALID_NAME = "";
    private static String VALID_TYPE = "";
    private static String VALID_DATE_TIME = "";
    private static double VALID_WEIGHT = 0.0;
    private static double VALID_TIME_COST = 0.0;
    private static String VALID_DESCRIPTION = "";
    private static String VALID_STATUS = "";
    private static String VALID_MODULE = "";
    private static String VALID_CREATION_TIME = "";
    private static String VALID_FINISH_TIME = "";

    @BeforeEach
    public void setUp_returns_void() throws Exception {
        INVALID_DATE_TIME = "12/12/12";
        VALID_NAME = TypicalTasks.getSampleTasks()[1].getTaskName();
        VALID_TYPE = TypicalTasks.getSampleTasks()[1].getTaskType().toString();
        VALID_DATE_TIME = TypicalTasks.getSampleTasks()[1].getTimeString();
        VALID_WEIGHT = TypicalTasks.getSampleTasks()[1].getWeight();
        VALID_TIME_COST = TypicalTasks.getSampleTasks()[1].getEstimatedTimeCost();
        VALID_DESCRIPTION = TypicalTasks.getSampleTasks()[1].getTaskDescription();
        VALID_STATUS = TypicalTasks.getSampleTasks()[1].getTaskStatus().toString();
        VALID_MODULE = TypicalTasks.getSampleTasks()[1].getModule().toString();
        VALID_CREATION_TIME = TypicalTasks.getSampleTasks()[1]
                .getCreationDateTime().toString();
        try {
            VALID_FINISH_TIME = TypicalTasks.getSampleTasks()[1]
                    .getFinishDateTime().toString();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }*/

    @Test
    public void toModelType_validTaskDetails_returnsTask() throws Exception {
        JsonAdaptedTask task = new JsonAdaptedTask(TypicalTasks.getSampleTasks()[1]);
        assertEquals(TypicalTasks.getSampleTasks()[1], task.toModelType());
    }

    /*
    @Test
    public void toModelType_invalidDate_throwsInteractiveCommandException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_NAME, VALID_TYPE, INVALID_DATE_TIME, VALID_FINISH_TIME,
                        VALID_DESCRIPTION, VALID_MODULE, VALID_WEIGHT, VALID_TIME_COST, VALID_STATUS,
                        VALID_CREATION_TIME);
        String expectedMessage = "dataTimeFormatError";
        //"Invalid date time format, please follow the format below:\n"
        //+ "Event: HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy\n"
        //+ "e.g. 12:00 01/01/2020-14:00 01/01/2020\n"
        //+ "Rest : HH:mm dd/MM/yyyy   e.g. 12:00 01/01/2020\n";
        assertThrows(InteractiveCommandException.class, expectedMessage, task::toModelType);
    }*/
}
