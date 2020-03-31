package draganddrop.studybuddy.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.module.exceptions.ModuleCodeException;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.SampleModules;

/**
 * Pending
 */
public class ModuleListTest {
    private ModuleList moduleList = new ModuleList();
    private Module sample1 = SampleModules.getSampleModule()[0];
    private Module sample2 = SampleModules.getSampleModule()[2];

    @Test
    public void modListDuplicateMods_throwModuleCodeException() {
        ModuleList moduleList = new ModuleList();
        Assert.assertThrows(ModuleCodeException.class, ()-> {
            for (Module sampleMod: SampleModules.getSampleModule()) {
                moduleList.add(sampleMod);
            }
        });
    }

    @Test
    public void lastModOnListIsEmptyModule() throws ModuleCodeException {
        moduleList.add(new EmptyModule());
        moduleList.add(sample1);
        moduleList.add(sample2);
        assertEquals(new EmptyModule(), moduleList.get(moduleList.getSize() - 1));
        moduleList.setModuleList(List.of());
        moduleList.add(sample1);
        moduleList.add(sample2);
        moduleList.add(new EmptyModule());
        assertEquals(new EmptyModule(), moduleList.get(moduleList.getSize() - 1));
    }

    @Test
    public void containsModInList_returnTrue() throws ModuleCodeException {
        Module sample1 = SampleModules.getSampleModule()[0];
        moduleList.add(sample1);
        assertTrue(moduleList.contains(sample1));
    }

    @Test
    public void containsModNotInList_returnFalse() throws ModuleCodeException {
        Module sample1 = SampleModules.getSampleModule()[0];
        assertFalse(moduleList.contains(sample1));
    }
}
