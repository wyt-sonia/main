package draganddrop.studdybuddy.model.module;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studdybuddy.model.module.exceptions.ModuleCodeException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * List of Modules. Checks for duplicates. Will be stored in Json.
 */
public class ModuleList {
    private ObservableList<Module> internalList = FXCollections.observableArrayList();

    public ModuleList() {

    }

    /**
     * To be executed every time whenever a module is added.
     *
     * @param module
     * @return if there is a duplicate
     */
    public boolean contains(Module module) {
        final Module moduleForPredicate = module;
        FilteredList<Module> filteredList = internalList.filtered((x) -> x.equals(moduleForPredicate));
        return filteredList.isEmpty() ? false : true;

    }

    public ObservableList<Module> getInternalList() {
        return internalList;
    }

    /**
     * checks for duplicate modules first, then add into the moduleList.
     * Moves Empty module to the back of the internalList.
     * @param module to be added to the ModuleList
     */
    public void add(Module module) throws ModuleCodeException {
        if (this.contains(module)) {
            throw new ModuleCodeException("Duplicate modules");
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
     * @throws ModuleCodeException
     */
    public void remove(Module module) throws ModuleCodeException {
        if (this.contains(module)) {
            internalList.remove(module);
        } else {
            throw new ModuleCodeException("Module does not exist");
        }
    }

    /**
     * Retrieves a module with the original module name inside the moduleList.
     *
     * @param moduleCode
     * @return
     * @throws ModuleCodeException
     */

    public Module get(String moduleCode) throws ModuleCodeException {
        Module moduleToFind = new Module(moduleCode);
        if (this.contains(moduleToFind)) {
            int index = this.internalList.indexOf(moduleToFind);
            return this.internalList.get(index);
        } else {
            throw new ModuleCodeException("Module Not Found");
        }
    }

    public void setModuleList(List<Module> replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement);
    }

    public int indexOf(Module module) {
        return internalList.indexOf(module);
    }
}
