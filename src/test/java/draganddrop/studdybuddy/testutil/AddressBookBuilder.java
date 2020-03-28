package draganddrop.studdybuddy.testutil;

import draganddrop.studdybuddy.model.AddressBook;
import draganddrop.studdybuddy.model.task.Task;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 * {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withTask(Task task) {
        addressBook.addTask(task);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
