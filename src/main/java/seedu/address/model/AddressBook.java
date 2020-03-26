package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleList;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskType;
import seedu.address.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTaskList archivedTasks;
    private final UniqueTaskList dueSoonTasks;
    private final UniqueTaskList tasks;
    private final ModuleList moduleList;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tasks = new UniqueTaskList();
        archivedTasks = new UniqueTaskList();
        dueSoonTasks = new UniqueTaskList();
        moduleList = new ModuleList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the task list.
     * The task identity of {@code editedTask} must not be the same as another existing task in the task list.
     */
    public void setTasks(Task target, Task editedTask) {
        requireNonNull(editedTask);
        tasks.setTask(target, editedTask);
    }

    /**
     * Sort tasks by the given {@code keyword}.
     */
    public void sortTasks(String keyword) {
        tasks.sortTasks(keyword);
    }

    /**
     * Sort tasks by the given {@code keyword}.
     */
    public void sortDueSoonTasks() {
        dueSoonTasks.sortTasks("deadline / task start date");
    }

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task list.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }


    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the task list.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
        if (this.getDueSoonList().contains(key)) {
            removeDueSoonTask(key);
        }
        sortDueSoonTasks();
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the task list.
     */
    public void removeDueSoonTask(Task key) {
        dueSoonTasks.remove(key);
    }

    public void setArchivedTasks(List<Task> aTasks) {
        this.archivedTasks.setTasks(aTasks);
    }

    public void setDueSoonTasks(List<Task> aTasks) {
        this.dueSoonTasks.setTasks(aTasks);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setArchivedTasks(newData.getArchivedList());
        setDueSoonTasks(newData.getDueSoonList());
        setTasks(newData.getTaskList());
        setModuleList(newData.getModuleList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Adds an archived person to the address book.
     *
     * @param p must not already exist in the address book.
     */
    public void addArchivedTask(Task p) {
        archivedTasks.add(p);
    }

    /**
     * Adds a due soon task to the dueSoonTasks list.
     *
     * @param p must not already exist in the address book.
     */
    public void addDueSoonTask(Task p) {
        if (p.isDueSoon()) {
            dueSoonTasks.add(p);
            sortDueSoonTasks();
        }
    }

    /**
     * Adds a module to the ModuleList.
     *
     * @param module
     */
    public void addModule(Module module) {
        try {
            moduleList.add(module);
        } catch (ModuleCodeException ex) {
            System.out.println("AddModule Failed, from addressBook.addModule()");
        }
    }

    /**
     * Adds a task to the task list.
     * The task must not already exist in the task list.
     */
    public void addTask(Task t) {
        tasks.add(t);
        if (t.isDueSoon()) {
            addDueSoonTask(t);
        }
    }

    public void completeTask(Task target) {
        tasks.completeTask(target);
    }

    public void setTaskName(Task target, String newTaskName) {
        tasks.setTaskName(target, newTaskName);
    }

    public void setTaskType(Task target, TaskType newTaskType) {
        tasks.setTaskType(target, newTaskType);
    }

    public void setTaskDateTime(Task target, LocalDateTime[] newDateTimes) {
        tasks.setTaskDateTime(target, newDateTimes);
    }

    public void setModuleInTask(Task target, Module module) throws ModuleCodeException {
        if (moduleList.contains(module)) {
            tasks.setTaskMod(target, module);
        } else {
            throw new ModuleCodeException("Module does not exist in addressBook!");
        }
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    //// util methods

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getArchivedList() {
        return archivedTasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getDueSoonList() {
        return dueSoonTasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Module> getModuleList() {
        return moduleList.getInternalList();
    }

    public void setModuleList(List<Module> modules) {
        this.moduleList.setModuleList(modules);
    }

    public boolean hasModule(Module module) {
        return moduleList.contains(module);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddressBook // instanceof handles nulls
            && persons.equals(((AddressBook) other).persons));
    }

}
