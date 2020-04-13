package draganddrop.studybuddy.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.TestModules;

public class ModuleTest {
    private Module sample1 = TestModules.getSampleModule()[0];
    private Module sample2 = TestModules.getSampleModule()[1];
    private Module sample3 = TestModules.getSampleModule()[2];

    @Test
    public void equals() {
        //different name but same code -> true
        assertEquals(sample1, sample2);
        //same name but different code -> false
        assertNotEquals(sample1, sample3);
    }

    @Test
    public void changeModuleNameSucceed() {
        sample1.setModuleName("New Name");
        assertEquals(sample1.getModuleName(), "New Name");
    }

    @Test
    public void changeModuleCodeSucceed() throws ModuleException {
        sample1.setModuleCode("CC1111");
        assertEquals(sample1.getModuleCode(), new ModuleCode("CC1111"));
    }

    @Test
    public void createModuleWithNoPrefixThrowsModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new Module("2312312"));
    }

    @Test
    public void createModuleWithNoNumberThrowsModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new Module("CS"));
    }

    @Test
    public void createModuleExceedPrefixLengthThrowsModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new Module("CSSS1000"));
    }

    @Test
    public void createModuleExceedLengthThrowsModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new Module("CS100000000Z"));
    }

    @Test
    public void testFilterTaskList() {

    }

}
