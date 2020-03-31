package draganddrop.studybuddy.model.module;

import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.testutil.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModuleCodeTest {
    String sample1 = "CS1000";
    String sample1Clone = "CS1000";
    String sample1SmallCase = "cs1000";
    String noPrefix = "2222";
    String longPrefix = "FFFF11";
    String longNumbers = "CS1111111111";
    String noNumbers = "CS";
    String sample2 = "LSM1303A";
    String postfix = "CS0000X";
    String noPostfix = "CS0000";
    String longPostfix = "CS0000XX";

    @Test
    public void testSampleWith3LetterPrefix() throws ModuleCodeException {
        new ModuleCode(sample2);
    }

    @Test
    public void postFixOptional() throws ModuleCodeException {
        new ModuleCode(postfix);
        new ModuleCode(noPostfix);
    }

    @Test
    public void moduleCodePostfixTooLong_throwModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, () -> new ModuleCode(longPostfix));
    }

    @Test
    public void isSameModuleCode() throws ModuleCodeException {
        assertEquals(new ModuleCode(sample1), new ModuleCode(sample1Clone));
    }

    @Test
    public void isSameModuleCode_smallCase() throws ModuleCodeException {
        assertEquals(new ModuleCode(sample1), new ModuleCode(sample1SmallCase));
    }

    @Test
    public void moduleCodeNoPrefix_throwModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new ModuleCode(noPrefix));
    }

    @Test
    public void moduleCodeLongPrefix_throwModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new ModuleCode(longPrefix));
    }

    @Test
    public void moduleCodeLongCode_throwModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new ModuleCode(longNumbers));
    }

    @Test
    public void moduleCodeNoNum_throwModuleCodeException() {
        Assert.assertThrows(ModuleCodeException.class, ()-> new ModuleCode(noNumbers));
    }
}
