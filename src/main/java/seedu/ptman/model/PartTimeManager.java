package seedu.ptman.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.UniqueEmployeeList;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.Name;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.UniqueShiftList;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;
import seedu.ptman.model.tag.Tag;
import seedu.ptman.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class PartTimeManager implements ReadOnlyPartTimeManager {

    private final UniqueEmployeeList employees;
    private final UniqueShiftList shifts;
    private final UniqueTagList tags;
    private final Password password;
    private boolean isAdminMode;
    private final OutletInformation outlet;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        employees = new UniqueEmployeeList();
        shifts = new UniqueShiftList();
        tags = new UniqueTagList();
        password = new Password();
        isAdminMode = false;
        outlet = new OutletInformation(new Name("asd"), new Password(""), new OperatingHours("11:00-14:00"));
    }

    public PartTimeManager() {}

    /**
     * Creates an PartTimeManager using the Employees and Tags in the {@code toBeCopied}
     */
    public PartTimeManager(ReadOnlyPartTimeManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// authorization operations
    public boolean isAdminMode() {
        return this.isAdminMode;
    }

    /**
     * Check if given password is of outlet's
     * @param password
     * @return true if password is the same
     */
    public boolean isAdminPassword(Password password) {
        return this.password.equals(password);
    }

    //// list overwrite operations

    public void setEmployees(List<Employee> employees) throws DuplicateEmployeeException {
        this.employees.setEmployees(employees);
    }

    public void setShifts(List<Shift> shifts) throws DuplicateShiftException {
        this.shifts.setShifts(shifts);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code PartTimeManager} with {@code newData}.
     */
    public void resetData(ReadOnlyPartTimeManager newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Employee> syncedEmployeeList = newData.getEmployeeList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());

        List<Shift> syncedShiftList = newData.getShiftList();
        try {
            setEmployees(syncedEmployeeList);
            setShifts(syncedShiftList);
        } catch (DuplicateEmployeeException e) {
            throw new AssertionError("PartTimeManagers should not have duplicate employees");
        } catch (DuplicateShiftException e) {
            throw new AssertionError("PartTimeManagers should not have duplicate shifts");
        }
    }

    //// employee-level operations

    /**
     * Adds an employee to the address book.
     * Also checks the new employee's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the employee to point to those in {@link #tags}.
     *
     * @throws DuplicateEmployeeException if an equivalent employee already exists.
     */
    public void addEmployee(Employee p) throws DuplicateEmployeeException {
        Employee employee = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any employee
        // in the employee list.
        employees.add(employee);
    }

    /**
     * Replaces the given employee {@code target} in the list with {@code editedEmployee}.
     * {@code PartTimeManager}'s tag list will be updated with the tags of {@code editedEmployee}.
     *
     * @throws DuplicateEmployeeException if updating the employee's details causes the employee to be equivalent to
     *      another existing employee in the list.
     * @throws EmployeeNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Employee)
     */
    public void updateEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireNonNull(editedEmployee);

        Employee syncedEditedEmployee = syncWithMasterTagList(editedEmployee);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any employee
        // in the employee list.
        employees.setEmployee(target, syncedEditedEmployee);
    }

    /**
     *  Updates the master tag list to include tags in {@code employee} that are not in the list.
     *  @return a copy of this {@code employee} such that every tag in this employee points to a Tag
     *  object in the master list.
     */
    private Employee syncWithMasterTagList(Employee employee) {
        final UniqueTagList employeeTags = new UniqueTagList(employee.getTags());
        tags.mergeFrom(employeeTags);

        // Create map with values = tag object references in the master list
        // used for checking employee tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of employee tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        employeeTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Employee(
                employee.getName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getSalary(),
                employee.getPassword(),
                correctTagReferences
        );
    }

    /**
     * Removes {@code key} from this {@code PartTimeManager}.
     * @throws EmployeeNotFoundException if the {@code key} is not in this {@code PartTimeManager}.
     */
    public boolean removeEmployee(Employee key) throws EmployeeNotFoundException {
        if (employees.remove(key)) {
            removeUnusedTag();
            return true;
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    public boolean removeShift(Shift key) throws ShiftNotFoundException {
        return shifts.remove(key);
    }

    /**
     * Adds a shift to the address book.
     *
     * @throws DuplicateShiftException if a equivalent shift already exists.
     */
    public void addShift(Shift p) throws DuplicateShiftException {
        outlet.addShift(p);
        shifts.add(p);
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// util methods
    public void setAdminMode(boolean isAdmin) {
        isAdminMode = isAdmin;
    }


    /**
     * Remove tag from Employee if the tag exist in Employee.
     * @param tag
     * @param employee
     */
    private void removeTagFromEmployee(Tag tag, Employee employee) {
        Set<Tag> newTags = new HashSet<>(employee.getTags());

        if (!newTags.contains(tag)) {
            return;
        } else {
            newTags.remove(tag);
        }

        Employee newEmployee = new Employee(employee.getName(), employee.getPhone(), employee.getEmail(),
                employee.getAddress(), employee.getSalary(), employee.getPassword(), newTags);

        try {
            updateEmployee(employee, newEmployee);
        } catch (DuplicateEmployeeException dpe) {
            throw new AssertionError("updating employee should not result in duplicated employee");
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("updating employee should always be able to find the employee you are editing");
        }

    }

    /**
     * remove tag that is unused in addressbook
     */
    private void removeUnusedTag() {
        HashSet newSet = new HashSet();
        for (Employee employee:employees) {
            for (Tag tag: employee.getTags()) {
                if (!newSet.contains(tag)) {
                    newSet.add(tag);
                }
            }
        }
        tags.setTags(newSet);
    }
    /**
     * Remove tags from everyone in the address book
     * @param tag
     */
    public void removeTagFromAllEmployees(Tag tag) {
        for (Employee employee: employees) {
            removeTagFromEmployee(tag, employee);
        }
        removeUnusedTag();
    }

    @Override
    public String toString() {
        return employees.asObservableList().size() + " employees, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Employee> getEmployeeList() {
        return employees.asObservableList();
    }

    @Override
    public ObservableList<Shift> getShiftList() {
        return shifts.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PartTimeManager // instanceof handles nulls
                && this.employees.equals(((PartTimeManager) other).employees)
                && this.password.equals(((PartTimeManager) other).password)
                && this.tags.equalsOrderInsensitive(((PartTimeManager) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(employees, tags);
    }
}
