package seedu.address.model.module;

import static java.util.Objects.requireNonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/**
 * packages task into various modules that the student have. Each module has list of task, taken from the main TaskList.
 * Identified by ModuleCode instead of ModuleName to minimise errors.
 */
public class Module {
    private ObservableList<Task> internalTaskList = FXCollections.observableArrayList();
    private String moduleName;
    private ModuleCode moduleCode;

    /**
     * Constructs a Module. Will check for any duplicates in the moduleList.
     *
     * @param moduleName
     * @param fullModuleCode
     * @throws ModuleCodeException
     */
    public Module(String moduleName, String fullModuleCode) {
        this.moduleName = moduleName;
        try {
            this.moduleCode = new ModuleCode(fullModuleCode);
        } catch (ModuleCodeException e) {
            System.out.println("ModuleCode is invalid!");
        }
    }

    /**
     * Same as previous constructor, but a module without a name.
     *
     * @param fullModuleCode
     * @throws ModuleCodeException
     */
    public Module(String fullModuleCode) throws ModuleCodeException {
        this.moduleName = "";
        this.moduleCode = new ModuleCode(fullModuleCode);
    }

    public Module() {
        this.moduleName = "";
        try {
            this.moduleCode = new ModuleCode("O0000O");
        } catch (ModuleCodeException e) {
            System.out.println("from Module(). ModuleCode is invalid!");
        }
    }

    /**
     * returns whether a Task is inside the internalList of this module.
     *
     * @param toCheck
     * @return
     */
    public boolean contains(Task toCheck) {
        requireNonNull(toCheck);
        return internalTaskList.stream().anyMatch(toCheck::isSameTask);
    }

    /**
     * Adds a task.
     *
     * @param toAdd
     * @throws DuplicateTaskException
     */
    public void add(Task toAdd) throws DuplicateTaskException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalTaskList.add(toAdd);
    }

    /**
     * Removes a task from the module list.
     *
     * @param toRemove
     * @throws TaskNotFoundException
     */
    public void remove(Task toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        if (contains(toRemove)) {
            internalTaskList.remove(toRemove);
        } else {
            throw new TaskNotFoundException();
        }
    }

    /**
     * Compares moduleCode instead of moduleName.
     *
     * @param other object, possibly a module
     * @return true or false.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Module) {
            ModuleCode otherModuleCode = ((Module) other).moduleCode;
            return this.moduleCode.equals(otherModuleCode) ? true : false;
        } else {
            return false;
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ModuleCode getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) throws ModuleCodeException {
        this.moduleCode = new ModuleCode(moduleCode);
    }

    public ObservableList<Task> getInternalTaskList() {
        return internalTaskList;
    }

    public void setInternalTaskList(ObservableList<Task> internalTaskList) {
        this.internalTaskList = internalTaskList;
    }

    public void filterAndSetInternalTaskList(ObservableList<Task> externalTaskList) {
        ObservableList<Task> collectedTasks = externalTaskList.filtered(x-> x.getModule().equals(this));
        this.internalTaskList = collectedTasks;
    }
    @Override
    public String toString() {
        return getModuleCode().toString();
    }

}
