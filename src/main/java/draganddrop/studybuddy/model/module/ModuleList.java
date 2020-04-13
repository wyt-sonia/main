package draganddrop.studybuddy.model.module;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * List of Modules. Checks for duplicates. Will be stored in Json.
 */
public class ModuleList {
    private ObservableList<Module> internalList = FXCollections.observableArrayList();

    /**
     * To be executed every time whenever a module is added.
     *
     * @param module
     * @return if there is a duplicate
     */
    public boolean isContains(Module module) {
        final Module moduleForPredicate = module;
        return internalList.contains(moduleForPredicate);

    }

    /**
     * To be executed every time whenever a module is added.
     *
     * @param string moduleName
     * @return if there is a duplicate name.
     */
    public boolean isContainsDuplicateName(String string) {
        FilteredList<Module> filteredList = internalList.filtered((x) -> x.getModuleName()
                .toLowerCase().equals(string.toLowerCase()));
        return !filteredList.isEmpty();
    }

    public ObservableList<Module> getInternalList() {
        return internalList;
    }

    /**
     * checks for duplicate modules/names first, then add into the moduleList.
     * Moves Empty module to the back of the internalList.
     * @param module to be added to the ModuleList
     */
    public void add(Module module) throws ModuleException {
        if (this.isContains(module)) {
            throw new ModuleException("Duplicate modules");
        } else if (this.isContainsDuplicateName(module.getModuleName())) {
            throw new ModuleException("Duplicate module name");
        } else {
            internalList.add(module);
            shiftEmptyModBack();
        }
    }

    /**
     * relocates Empty Module to the back of the internalList if it exist.
     */
    private void shiftEmptyModBack() {
        //check last mod for Empty Module
        if (internalList.get(internalList.size() - 1).equals(new EmptyModule())) {
            //Do nothing
        } else { //if it is not empty
            if (internalList.contains(new EmptyModule())) {
                int emptyIndex = internalList.indexOf(new EmptyModule());
                swap(emptyIndex, emptyIndex + 1);
            }
        }
    }

    /**
     * swaps 2 elements in the internalList.
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        Module firstMod = internalList.get(i);
        Module secondMod = internalList.get(j);
        internalList.set(i, secondMod);
        internalList.set(j, firstMod);
    }

    /**
     * Removes module from the list.
     *
     * @param module
     * @throws ModuleException
     */
    public void remove(Module module) throws ModuleException {
        if (this.isContains(module)) {
            internalList.remove(module);
        } else {
            throw new ModuleException("Module does not exist");
        }
    }

    /**
     * Changes moduleName of a certain mod. Checks for existence of origin mod & duplicity of new name.
     * @param oldModule
     * @param newModule
     * @throws ModuleException
     */
    public void changeModuleName(Module oldModule, Module newModule) throws ModuleException {
        if (this.isContains(oldModule) && !this.isContainsDuplicateName(newModule.getModuleName())) {
            int oldIndex = internalList.indexOf(oldModule);
            internalList.set(oldIndex, newModule);
        } else {
            if (!this.isContains(oldModule)) {
                throw new ModuleException("Module does not exist");
            }
            if (this.isContainsDuplicateName(newModule.getModuleName())) {
                throw new ModuleException("A duplicate module name already exist.");
            }
        }
    }

    /**
     * Changes moduleCode of a certain mod. Checks for existence of origin mod & duplicity of new code.
     * @param oldModule
     * @param newModule
     * @throws ModuleException
     */
    public void changeModuleCode(Module oldModule, Module newModule) throws ModuleException {
        if (this.isContains(newModule)) {
            throw new ModuleException("new module code is a duplicate!");
        } else if (this.isContains(oldModule)) {
            if (!newModule.equals(new EmptyModule())) {
                newModule.setInternalTaskList(oldModule.getInternalTaskList());
            } else {
                newModule.setInternalTaskList(getEmptyModule().getInternalTaskList());
                newModule.addAll(oldModule.getInternalTaskList());
            }

            int index = this.indexOf(oldModule);
            internalList.set(index, newModule);
        } else {
            throw new ModuleException("module does not exist");
        }
    }

    /**
     * Retrieves a module with the original module name inside the moduleList.
     *
     * @param moduleCode
     * @return
     * @throws ModuleException
     */

    public Module get(String moduleCode) throws ModuleException {
        Module moduleToFind = new Module(moduleCode);
        if (this.isContains(moduleToFind)) {
            int index = this.internalList.indexOf(moduleToFind);
            return this.internalList.get(index);
        } else {
            throw new ModuleException("Module Not Found");
        }
    }

    public Module get(int index) {
        return this.internalList.get(index);
    }

    public Module getEmptyModule() {
        return this.internalList.get(internalList.size() - 1);
    }

    public void setModuleList(List<Module> replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement);
    }

    public int indexOf(Module module) {
        return internalList.indexOf(module);
    }

    public int getSize() {
        return internalList.size();
    }
}
