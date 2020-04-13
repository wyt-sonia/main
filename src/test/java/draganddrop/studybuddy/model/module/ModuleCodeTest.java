package draganddrop.studybuddy.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.testutil.Assert;

public class ModuleCodeTest {
    private String sample1 = "CS1000";
    private String sample1Clone = "CS1000";
    private String sample1SmallCase = "cs1000";
    private String noPrefix = "2222";
    private String longPrefix = "FFFF11";
    private String longNumbers = "CS1111111111";
    private String noNumbers = "CS";
    private String sample2 = "LSM1303A";
    private String postfix = "CS0000X";
    private String noPostfix = "CS0000";
    private String longPostfix = "CS0000XX";

    @Test
    public void testSampleWith3LetterPrefix() throws ModuleException {
        new ModuleCode(sample2);
    }

    @Test
    public void postFixOptional() throws ModuleException {
        new ModuleCode(postfix);
        new ModuleCode(noPostfix);
    }

    @Test
    public void testEqualsFunctionOnString() throws ModuleException {
        assertEquals(sample1, new ModuleCode(sample1).toString());
    }

    @Test
    public void moduleCodePostfixTooLongThrowModuleCodeException() {
        Assert.assertThrows(ModuleException.class, () -> new ModuleCode(longPostfix));
    }

    @Test
    public void isSameModuleCode() throws ModuleException {
        assertEquals(new ModuleCode(sample1), new ModuleCode(sample1Clone));
    }

    @Test
    public void isSameModuleCodeSmallCase() throws ModuleException {
        assertEquals(new ModuleCode(sample1), new ModuleCode(sample1SmallCase));
    }

    @Test
    public void moduleCodeNoPrefixThrowModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new ModuleCode(noPrefix));
    }

    @Test
    public void moduleCodeLongPrefixThrowModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new ModuleCode(longPrefix));
    }

    @Test
    public void moduleCodeLongCodeThrowModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new ModuleCode(longNumbers));
    }

    @Test
    public void moduleCodeNoNumThrowModuleCodeException() {
        Assert.assertThrows(ModuleException.class, ()-> new ModuleCode(noNumbers));
    }
}
