package seedu.ptman.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;
import seedu.ptman.model.tag.Tag;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Employee> PREDICATE_SHOW_ALL_EMPLOYEES = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyPartTimeManager newData);

    /** Returns the PartTimeManager */
    ReadOnlyPartTimeManager getPartTimeManager();

    /** Deletes the given employee. */
    void deleteEmployee(Employee target) throws EmployeeNotFoundException;

    /** Adds the given employee */
    void addEmployee(Employee employee, String password) throws DuplicateEmployeeException, InvalidPasswordException;

    /**
     * Delete tag from all employees
     */
    void deleteTagFromAllEmployee(Tag tag);
    /**
     * Replaces the given employee {@code target} with {@code editedEmployee}.
     *
     * @throws DuplicateEmployeeException if updating the employee's details causes the employee to be equivalent to
     *      another existing employee in the list.
     * @throws EmployeeNotFoundException if {@code target} could not be found in the list.
     */
    void updateEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException;

    /** Returns an unmodifiable view of the filtered employee list */
    ObservableList<Employee> getFilteredEmployeeList();

    /**
     * Updates the filter of the filtered employee list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEmployeeList(Predicate<Employee> predicate);

}
