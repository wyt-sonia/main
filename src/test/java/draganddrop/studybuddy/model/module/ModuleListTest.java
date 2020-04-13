package draganddrop.studybuddy.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.testutil.Assert;
import draganddrop.studybuddy.testutil.TestModules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Represents a ModuleListTest.
 */
public class ModuleListTest {
    @Test
    public void modListDuplicateModsThrowModuleCodeException() {
        ModuleList moduleList = new ModuleList();
        Assert.assertThrows(ModuleException.class, ()-> {
            for (Module sampleMod: TestModules.getSampleModule()) {
                moduleList.add(sampleMod);
            }
        });
    }

    @Test
    public void lastModOnListIsEmptyModule() throws ModuleException {
        ModuleList moduleList = new ModuleList();
        moduleList.add(new EmptyModule());
        moduleList.add(new Module("A", "CS2000"));
        moduleList.add(new Module("B", "CS2001"));
        assertEquals(new EmptyModule(), moduleList.get(moduleList.getSize() - 1));
        moduleList.setModuleList(List.of());
        moduleList.add(new Module("B", "CS2001"));
        moduleList.add(new Module("A", "CS2000"));
        moduleList.add(new EmptyModule());
        assertEquals(new EmptyModule(), moduleList.get(moduleList.getSize() - 1));
    }

    @Test
    public void containsModInListReturnTrue() throws ModuleException {
        ModuleList moduleList = new ModuleList();
        Module sample1 = TestModules.getSampleModule()[0];
        moduleList.add(sample1);
        assertTrue(moduleList.isContains(sample1));
    }

    @Test
    public void containsModNotInListReturnFalse() {
        ModuleList moduleList = new ModuleList();
        Module sample1 = TestModules.getSampleModule()[0];
        assertFalse(moduleList.isContains(sample1));
    }

    @Test
    public void filterTaskListWorks() {
        Module testModule = TestModules.getSampleModule()[3];
        ObservableList<Task> tasksToBeFiltered = FXCollections.observableArrayList(TestModules.getSampleTask());
        ObservableList<Task> expectedTasks = TestModules.getSampleTask()
                .filtered(x -> x.getModule().equals(testModule));
        testModule.filterAndSetInternalTaskList(tasksToBeFiltered);
        assertEquals(expectedTasks, testModule.getInternalTaskList());
        testModule.getInternalTaskList().forEach(x -> {
            assertTrue(x.getModule().equals(testModule));
        });
    }

}
