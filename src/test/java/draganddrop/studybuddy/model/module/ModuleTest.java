package draganddrop.studybuddy.model.module;

import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.SampleModules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ModuleTest {
    Module sample1 = SampleModules.getSampleModule()[0];
    Module sample2 = SampleModules.getSampleModule()[1];
    Module sample3 = SampleModules.getSampleModule()[2];

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

    @Test
    public void createModuleWithNoNumber_throwsModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new Module("ABBA"));
    }

    /*@Test
    public void filterTaskFromTaskListTest() {
        Module emptyMod = new EmptyModule();
        emptyMod.filterAndSetInternalTaskList(SampleModules.getSampleTask());
        for(int i = 0; i < emptyMod.getInternalTaskList().size(); i++) {
            assertTrue(emptyMod.getInternalTaskList().get(i), SampleModules.getExpectedOutcomeForFilterFunction().get(i));
        }

    }*/
}
