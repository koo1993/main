package seedu.ptman.model;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.ptman.commons.core.ComponentManager;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.model.PartTimeManagerChangedEvent;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;
import seedu.ptman.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final PartTimeManager partTimeManager;
    private final FilteredList<Employee> filteredEmployees;
    private final FilteredList<Shift> filteredShifts;
    private HashMap<Employee, Password> tempPasswordMap;

    /**
     * Initializes a ModelManager with the given partTimeManager and userPrefs.
     */
    public ModelManager(ReadOnlyPartTimeManager partTimeManager, UserPrefs userPrefs) {
        super();
        requireAllNonNull(partTimeManager, userPrefs);

        logger.fine("Initializing with address book: " + partTimeManager + " and user prefs " + userPrefs);

        this.partTimeManager = new PartTimeManager(partTimeManager);
        filteredEmployees = new FilteredList<>(this.partTimeManager.getEmployeeList());
        filteredShifts = new FilteredList<>(this.partTimeManager.getShiftList());
        tempPasswordMap = new HashMap<>();
    }

    public ModelManager() {
        this(new PartTimeManager(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyPartTimeManager newData) {
        partTimeManager.resetData(newData);
        indicatePartTimeManagerChanged();
    }

    @Override
    public ReadOnlyPartTimeManager getPartTimeManager() {
        return partTimeManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicatePartTimeManagerChanged() {
        raise(new PartTimeManagerChangedEvent(partTimeManager));
    }

    @Override
    public synchronized void deleteEmployee(Employee target) throws EmployeeNotFoundException {
        partTimeManager.removeEmployee(target);
        indicatePartTimeManagerChanged();
    }

    @Override
    public synchronized void addEmployee(Employee employee) throws DuplicateEmployeeException {
        partTimeManager.addEmployee(employee);
        updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        indicatePartTimeManagerChanged();
    }

    @Override
    public boolean isAdminMode() {
        return partTimeManager.isAdminMode();
    }

    @Override
    public synchronized boolean setTrueAdminMode(Password password) {
        requireNonNull(password);
        if (!partTimeManager.isAdminPassword(password)) {
            return false;
        }
        partTimeManager.setAdminMode(partTimeManager.isAdminPassword(password));
        return true;
    }

    @Override
    public synchronized void setFalseAdminMode() {
        partTimeManager.setAdminMode(false);
    }

    @Override
    public boolean isAdminPassword(Password password) {
        return partTimeManager.isAdminPassword(password);
    }

    @Override
    public void setAdminPassword(Password password) {
        partTimeManager.setAdminPassword(password);
    }

    @Override
    public void storeResetPassword(Employee employee, Password tempPassword) {
        tempPasswordMap.put(employee, tempPassword);
    }

    @Override
    public boolean isCorrectTempPwd(Employee employee, Password tempPassword) {
        if (!tempPasswordMap.containsKey(employee)) {
            return false;
        } else {
            return tempPasswordMap.get(employee).equals(tempPassword);
        }
    }

    @Override
    public void addShift(Shift shift) throws DuplicateShiftException {
        partTimeManager.addShift(shift);
        indicatePartTimeManagerChanged();
    }

    @Override
    public ObservableList<Shift> getFilteredShiftList() {
        SortedList<Shift> sortedShiftList = new SortedList<>(filteredShifts, Shift::compareTo);
        return FXCollections.unmodifiableObservableList(sortedShiftList);
    }

    @Override
    public void deleteShift(Shift target) throws ShiftNotFoundException {
        partTimeManager.removeShift(target);
        indicatePartTimeManagerChanged();
    }

    @Override
    public void updateEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireAllNonNull(target, editedEmployee);

        partTimeManager.updateEmployee(target, editedEmployee);
        indicatePartTimeManagerChanged();
    }

    @Override
    public void updateOutlet(OutletInformation editedOutlet) throws NoOutletInformationFieldChangeException {
        partTimeManager.updateOutlet(editedOutlet);
        indicatePartTimeManagerChanged();
    }

    @Override
    public String getOutletInformationMessage() {
        return partTimeManager.getOutletInformationMessage();
    }

    //=========== Filtered Employee List Accessors =============================================================
    @Override
    public void deleteTagFromAllEmployee(Tag tag) {
        partTimeManager.removeTagFromAllEmployees(tag);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Employee} backed by the internal list of
     * {@code partTimeManager}
     */
    @Override
    public ObservableList<Employee> getFilteredEmployeeList() {
        return FXCollections.unmodifiableObservableList(filteredEmployees);
    }

    @Override
    public void updateFilteredEmployeeList(Predicate<Employee> predicate) {
        requireNonNull(predicate);
        filteredEmployees.setPredicate(predicate);
    }

    @Override
    public OutletInformation getOutletInformation() {
        return partTimeManager.getOutletInformation();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return partTimeManager.equals(other.partTimeManager)
                && filteredEmployees.equals(other.filteredEmployees);
    }

}
