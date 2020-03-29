package draganddrop.studybuddy.model.module;

import static draganddrop.studybuddy.logic.commands.CommandTestUtil.VALID_DESCRIPTION_ONE;
import static draganddrop.studybuddy.logic.commands.CommandTestUtil.VALID_TASK_NAME_ONE;

import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.SampleModules;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import draganddrop.studybuddy.model.module.Module;

import draganddrop.studybuddy.model.util.SampleDataUtil;
import org.junit.jupiter.api.Test;

public class ModuleTest {
    Module sample1 = SampleModules.getSampleModule()[0];
    Module sample2 = SampleModules.getSampleModule()[1];
    Module sample3 = SampleModules.getSampleModule()[2];

    @Test
    public void equals() {
        //different name but same code -> true
        assertTrue(sample1.equals(sample2));
        //same name but different code -> false
        assertFalse(sample1.equals(sample3));
    }

    @Test
    public void createModuleWithNoPrefix_throwsModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new Module("2312312Z"));
    }

    @Test
    public void createModuleWithNoNumber_throwsModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new Module("ABBA"));
    }
}
