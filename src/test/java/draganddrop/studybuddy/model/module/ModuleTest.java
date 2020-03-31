package draganddrop.studybuddy.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.SampleModules;

public class ModuleTest {
    private Module sample1 = SampleModules.getSampleModule()[0];
    private Module sample2 = SampleModules.getSampleModule()[1];
    private Module sample3 = SampleModules.getSampleModule()[2];

    @Test
    public void equals() {
        //different name but same code -> true
        assertEquals(sample1, sample2);
        //same name but different code -> false
        assertNotEquals(sample1, sample3);
    }

    @Test
    public void createModuleWithNoPrefix_throwsModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new Module("2312312Z"));
    }

    /*@Test
    public void createModuleWithNoNumber_throwsModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new Module("AA"));
    }*/

    /*@Test
    public void filterTaskFromTaskListTest() {
        Module emptyMod = new EmptyModule();
        emptyMod.filterAndSetInternalTaskList(SampleModules.getSampleTask());
        ObservableList<?> list = SampleModules.getExpectedOutcomeForFilterFunction();
        for (int i = 0; i < emptyMod.getInternalTaskList().size(); i++) {
            assertEquals(emptyMod.getInternalTaskList().get(i),
                    SampleModules.getExpectedOutcomeForFilterFunction().get(i));
        }

    }*/
}
