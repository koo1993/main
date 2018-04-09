package seedu.ptman.model;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.ptman.commons.util.DateUtil.getWeekFromDate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.ptman.commons.core.ComponentManager;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.model.OutletDataChangedEvent;
import seedu.ptman.commons.events.model.PartTimeManagerChangedEvent;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
import seedu.ptman.model.shift.exceptions.ShiftNotFoundException;
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
    private HashMap<OutletInformation, Password> tempAdminPasswordMap;

    /**
     * Initializes a ModelManager with the given partTimeManager and userPrefs.
     */
    public ModelManager(ReadOnlyPartTimeManager partTimeManager, UserPrefs userPrefs,
                        OutletInformation outletInformation) {
        super();
        requireAllNonNull(partTimeManager, userPrefs, outletInformation);

        logger.fine("Initializing with PTMan: " + partTimeManager + " and user prefs " + userPrefs);

        this.partTimeManager = new PartTimeManager(partTimeManager);
        try {
            this.partTimeManager.updateOutlet(outletInformation);
        } catch (NoOutletInformationFieldChangeException e) {
            logger.warning("Outlet data should not be empty.");
        }
        filteredEmployees = new FilteredList<>(this.partTimeManager.getEmployeeList());
        filteredShifts = new FilteredList<>(this.partTimeManager.getShiftList());

        // Only display shifts in the current week
        updateFilteredShiftList(shift ->
                getWeekFromDate(shift.getDate().getLocalDate()) == getWeekFromDate(LocalDate.now()));

        tempPasswordMap = new HashMap<>();
        tempAdminPasswordMap = new HashMap<>();
    }

    public ModelManager() {
        this(new PartTimeManager(), new UserPrefs(), new OutletInformation());
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
        raise(new OutletDataChangedEvent(partTimeManager.getOutletInformation()));
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

    //@@author koo1993
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
        indicatePartTimeManagerChanged();
    }

    @Override
    public void storeResetPassword(Employee employee, Password tempPassword) {
        tempPasswordMap.put(employee, tempPassword);
    }

    @Override
    public void storeResetPassword(OutletInformation outlet, Password tempPassword) {
        tempAdminPasswordMap.put(outlet, tempPassword);
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
    public boolean isCorrectTempPwd(OutletInformation outlet, Password tempPassword) {
        if (!tempAdminPasswordMap.containsKey(outlet)) {
            return false;
        } else {
            return tempAdminPasswordMap.get(outlet).equals(tempPassword);
        }
    }

    //@@author shanwpf
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
    public void updateShift(Shift target, Shift editedShift) throws ShiftNotFoundException, DuplicateShiftException {
        partTimeManager.updateShift(target, editedShift);
        indicatePartTimeManagerChanged();
    }

    @Override
    public void updateFilteredShiftList(Predicate<Shift> predicate) {
        requireNonNull(predicate);
        filteredShifts.setPredicate(predicate);
    }

    @Override
    public void updateEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireAllNonNull(target, editedEmployee);

        partTimeManager.updateEmployee(target, editedEmployee);
        indicatePartTimeManagerChanged();
    }

    //@@author SunBangjie
    @Override
    public void updateOutlet(OutletInformation editedOutlet) throws NoOutletInformationFieldChangeException {
        partTimeManager.updateOutlet(editedOutlet);
        indicatePartTimeManagerChanged();
    }

    @Override
    public String getOutletInformationMessage() {
        return partTimeManager.getOutletInformationMessage();
    }

    @Override
    public OutletInformation getOutletInformation() {
        return partTimeManager.getOutletInformation();
    }

    @Override
    public void encryptLocalStorage() {
        partTimeManager.encryptLocalStorage();
        indicatePartTimeManagerChanged();
    }

    @Override
    public void decryptLocalStorage() {
        partTimeManager.decryptLocalStorage();
        indicatePartTimeManagerChanged();
    }
    //@@author

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
                && filteredEmployees.equals(other.filteredEmployees)
                && filteredShifts.equals(other.filteredShifts);
    }

}
