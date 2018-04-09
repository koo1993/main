# shanwpf
###### \java\seedu\ptman\commons\util\DateUtil.java
``` java
/**
 * Utility methods for handling dates
 */
public class DateUtil {
    /**
     * Returns the week number for {@code date} from the start of the year
     */
    public static int getWeekFromDate(LocalDate date) {
        requireNonNull(date);
        TemporalField woy = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();
        return date.get(woy);
    }

    /**
     * Given a {@code date}, returns the date of the week's Monday
     */
    public static LocalDate getMondayOfDate(LocalDate date) {
        requireNonNull(date);
        int week = getWeekFromDate(date);
        // We use Locale.FRANCE because it sets the first day of the week
        // to be Monday.
        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        return LocalDate.now()
                .withYear(date.getYear())
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 1);
    }
}
```
###### \java\seedu\ptman\logic\commands\AddShiftCommand.java
``` java
/**
 * Adds a shift to PTMan.
 */
public class AddShiftCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addshift";
    public static final String COMMAND_ALIAS = "as";

    public static final String COMMAND_FORMAT = PREFIX_DATE + "DATE (in dd-mm-yy format) "
            + PREFIX_TIME_START + "START_TIME "
            + PREFIX_TIME_END + "END_TIME "
            + PREFIX_CAPACITY + "CAPACITY ";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a shift. "
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_DATE + "12-03-18 "
            + PREFIX_TIME_START + "0900 "
            + PREFIX_TIME_END + "1600 "
            + PREFIX_CAPACITY + "5 ";

    public static final String MESSAGE_SUCCESS = "New shift added: %1$s";
    public static final String MESSAGE_DUPLICATE_SHIFT = "This shift already exists in PTMan";

    private final Shift toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Shift}
     */
    public AddShiftCommand(Shift shift) {
        requireNonNull(shift);
        toAdd = shift;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        try {
            model.addShift(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateShiftException e) {
            throw new CommandException(MESSAGE_DUPLICATE_SHIFT);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddShiftCommand // instanceof handles nulls
                && toAdd.equals(((AddShiftCommand) other).toAdd));
    }
}
```
###### \java\seedu\ptman\logic\commands\ApplyCommand.java
``` java
/**
 * Registers an employee to a shift identified using their last displayed index from PTMan.
 */
public class ApplyCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "apply";
    public static final String COMMAND_ALIAS = "ap";

    public static final String COMMAND_FORMAT = "EMPLOYEE_INDEX (must be a positive integer) "
            + "SHIFT_INDEX "
            + "[" + PREFIX_PASSWORD + "PASSWORD]";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Applies an employee for the shift identified by the index number.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " 1 1 " + PREFIX_PASSWORD + "hunter2";

    public static final String MESSAGE_APPLY_SHIFT_SUCCESS = "Employee %1$s applied for shift %2$s";
    public static final String MESSAGE_DUPLICATE_EMPLOYEE = "Employee is already in the shift";
    public static final String MESSAGE_SHIFT_FULL = "Shift %1$s is full";

    private final Index employeeIndex;
    private final Index shiftIndex;
    private final Optional<Password> optionalPassword;

    private Employee applicant;
    private Shift shiftToApply;
    private Shift editedShift;

    public ApplyCommand(Index employeeIndex, Index shiftIndex, Optional<Password> optionalPassword) {
        this.optionalPassword = optionalPassword;
        this.employeeIndex = employeeIndex;
        this.shiftIndex = shiftIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(applicant);

        // Check if password is present when not in admin mode
        if (!model.isAdminMode()) {
            if (!optionalPassword.isPresent()) {
                throw new MissingPasswordException();
            }
            if (!applicant.isCorrectPassword(optionalPassword.get())) {
                throw new InvalidPasswordException();
            }
        }

        try {
            model.updateShift(shiftToApply, editedShift);
        } catch (ShiftNotFoundException e) {
            throw new AssertionError("Shift not found");
        } catch (DuplicateShiftException e) {
            throw new AssertionError("Duplicate shift");
        }

        return new CommandResult(String.format(MESSAGE_APPLY_SHIFT_SUCCESS,
                applicant.getName(), shiftIndex.getOneBased()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Employee> lastShownList = model.getFilteredEmployeeList();
        List<Shift> shiftList = model.getFilteredShiftList();

        if (employeeIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }
        if (shiftIndex.getZeroBased() >= shiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        applicant = lastShownList.get(employeeIndex.getZeroBased());
        shiftToApply = shiftList.get(shiftIndex.getZeroBased());
        editedShift = new Shift(shiftToApply);
        try {
            editedShift.addEmployee(applicant);
        } catch (DuplicateEmployeeException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EMPLOYEE);
        } catch (ShiftFullException e) {
            throw new CommandException(String.format(MESSAGE_SHIFT_FULL, shiftIndex.getOneBased()));
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplyCommand that = (ApplyCommand) o;
        return Objects.equals(employeeIndex, that.employeeIndex)
                && Objects.equals(shiftIndex, that.shiftIndex)
                && Objects.equals(applicant, that.applicant)
                && Objects.equals(shiftToApply, that.shiftToApply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeIndex, shiftIndex, applicant, shiftToApply);
    }
}
```
###### \java\seedu\ptman\logic\commands\DeleteShiftCommand.java
``` java
/**
 * Deletes a shift identified using it's last displayed index in the timetable.
 */
public class DeleteShiftCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteshift";
    public static final String COMMAND_ALIAS = "ds";

    public static final String COMMAND_FORMAT = "SHIFT_INDEX";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the shift identified by the index number used in the timetable.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + " (must be a positive integer)"
            + "\nExample: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_SHIFT_SUCCESS = "Deleted Shift: %1$s";

    private final Index targetIndex;

    private Shift shiftToDelete;

    public DeleteShiftCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(shiftToDelete);

        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }

        try {
            model.deleteShift(shiftToDelete);
        } catch (ShiftNotFoundException pnfe) {
            throw new AssertionError("The target shift cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Shift> lastShownList = model.getFilteredShiftList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        shiftToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteShiftCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteShiftCommand) other).targetIndex) // state check
                && Objects.equals(this.shiftToDelete, ((DeleteShiftCommand) other).shiftToDelete));
    }
}
```
###### \java\seedu\ptman\logic\commands\UnapplyCommand.java
``` java
/**
 * Registers an employee to a shift identified using their last displayed index from PTMan.
 */
public class UnapplyCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "unapply";
    public static final String COMMAND_ALIAS = "uap";

    public static final String COMMAND_FORMAT = "EMPLOYEE_INDEX "
            + "SHIFT_INDEX "
            + "[" + PREFIX_PASSWORD + "PASSWORD]";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes an employee from the shift identified by the index number.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " 1 1 " + PREFIX_PASSWORD + "hunter2";

    public static final String MESSAGE_UNAPPLY_SHIFT_SUCCESS = "Employee %1$s removed from shift %2$s";
    public static final String MESSAGE_EMPLOYEE_NOT_FOUND = "Employee is not in the shift.";

    private final Index employeeIndex;
    private final Index shiftIndex;
    private final Optional<Password> optionalPassword;

    private Employee applicant;
    private Shift shiftToUnapply;
    private Shift editedShift;

    public UnapplyCommand(Index employeeIndex, Index shiftIndex, Optional<Password> optionalPassword) {
        this.optionalPassword = optionalPassword;
        this.employeeIndex = employeeIndex;
        this.shiftIndex = shiftIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(applicant);

        // Check if password is present when not in admin mode
        if (!model.isAdminMode()) {
            if (!optionalPassword.isPresent()) {
                throw new MissingPasswordException();
            }
            if (!applicant.isCorrectPassword(optionalPassword.get())) {
                throw new InvalidPasswordException();
            }
        }

        try {
            model.updateShift(shiftToUnapply, editedShift);
        } catch (ShiftNotFoundException e) {
            throw new AssertionError("Shift not found");
        } catch (DuplicateShiftException e) {
            throw new AssertionError("Duplicate shift");
        }

        return new CommandResult(String.format(MESSAGE_UNAPPLY_SHIFT_SUCCESS,
                applicant.getName(), shiftIndex.getOneBased()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Employee> lastShownList = model.getFilteredEmployeeList();
        List<Shift> shiftList = model.getFilteredShiftList();

        if (employeeIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }
        if (shiftIndex.getZeroBased() >= shiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        applicant = lastShownList.get(employeeIndex.getZeroBased());
        shiftToUnapply = shiftList.get(shiftIndex.getZeroBased());
        editedShift = new Shift(shiftToUnapply);
        try {
            editedShift.removeEmployee(applicant);
        } catch (EmployeeNotFoundException e) {
            throw new CommandException(MESSAGE_EMPLOYEE_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnapplyCommand that = (UnapplyCommand) o;
        return Objects.equals(employeeIndex, that.employeeIndex)
                && Objects.equals(shiftIndex, that.shiftIndex)
                && Objects.equals(applicant, that.applicant)
                && Objects.equals(shiftToUnapply, that.shiftToUnapply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeIndex, shiftIndex, applicant, shiftToUnapply, optionalPassword);
    }
}
```
###### \java\seedu\ptman\logic\parser\AddShiftCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddShiftCommand object
 */
public class AddShiftCommandParser implements Parser<AddShiftCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddShiftCommand
     * and returns an AddShiftCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddShiftCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME_START,
                        PREFIX_TIME_END, PREFIX_CAPACITY);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_TIME_START,
                PREFIX_TIME_END, PREFIX_CAPACITY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE));
        }

        try {
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).get();
            Time startTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME_START)).get();
            Time endTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME_END)).get();
            Capacity capacity = ParserUtil.parseCapacity(argMultimap.getValue(PREFIX_CAPACITY)).get();

            Shift shift = new Shift(date, startTime, endTime, capacity);

            return new AddShiftCommand(shift);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
```
###### \java\seedu\ptman\logic\parser\ApplyCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ApplyCommand object
 */
public class ApplyCommandParser implements Parser<ApplyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ApplyCommand
     * and returns an ApplyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ApplyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);
        try {
            Optional<Password> password = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD));
            Index employeeIndex = ParserUtil.parseFirstIndex(clearPasswordFromCommand(args));
            Index shiftIndex = ParserUtil.parseSecondIndex(clearPasswordFromCommand(args));
            return new ApplyCommand(employeeIndex, shiftIndex, password);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ApplyCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\ptman\logic\parser\DeleteShiftCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteShiftCommand object
 */
public class DeleteShiftCommandParser implements Parser<DeleteShiftCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteShiftCommand
     * and returns an DeleteShiftCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteShiftCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(clearPasswordFromCommand(args));
            return new DeleteShiftCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteShiftCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\ptman\logic\parser\UnapplyCommandParser.java
``` java
/**
 * Parses input arguments and creates a new UnapplyCommand object
 */
public class UnapplyCommandParser implements Parser<UnapplyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnapplyCommand
     * and returns an UnapplyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnapplyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);
        try {
            Optional<Password> password = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD));
            Index employeeIndex = ParserUtil.parseFirstIndex(ParserUtil.clearPasswordFromCommand(args));
            Index shiftIndex = ParserUtil.parseSecondIndex(ParserUtil.clearPasswordFromCommand(args));
            return new UnapplyCommand(employeeIndex, shiftIndex, password);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnapplyCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\ptman\model\ModelManager.java
``` java
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

```
###### \java\seedu\ptman\model\PartTimeManager.java
``` java
    public void setShifts(List<Shift> shifts) throws DuplicateShiftException {
        this.shifts.setShifts(shifts);
    }
```
###### \java\seedu\ptman\model\PartTimeManager.java
``` java
    /**
     * Removes {@code key} from all shifts
     * @throws EmployeeNotFoundException if the {@code key} is not found
     */
    private void removeEmployeeFromAllShifts(Employee key) throws EmployeeNotFoundException {
        for (Shift shift : shifts) {
            if (shift.containsEmployee(key)) {
                Shift copy = new Shift(shift);
                try {
                    copy.removeEmployee(key);
                    shifts.setShift(shift, copy);
                } catch (DuplicateShiftException e) {
                    throw new AssertionError("shifts should never be duplicates");
                } catch (ShiftNotFoundException e) {
                    throw new AssertionError("shift should always exist");
                }
            }
        }
    }

    /**
     * Removes {@code key} from this {@code PartTimeManager}.
     * @throws ShiftNotFoundException if the {@code key} is not in this {@code PartTimeManager}
     */
    public boolean removeShift(Shift key) throws ShiftNotFoundException {
        return shifts.remove(key);
    }

    /**
     * Adds a shift to PTMan.
     * @throws DuplicateShiftException if a equivalent shift already exists.
     */
    public void addShift(Shift p) throws DuplicateShiftException {
        shifts.add(p);
    }

    /**
     * Replaces the given shift {@code target} in the list with {@code editedShift}.
     *
     * @throws DuplicateShiftException if updating the shift's details causes the shift to be equivalent to
     *      another existing shift in the list.
     * @throws ShiftNotFoundException if {@code target} could not be found in the list.
     */
    public void updateShift(Shift target, Shift editedShift) throws ShiftNotFoundException, DuplicateShiftException {
        shifts.setShift(target, editedShift);
    }
```
###### \java\seedu\ptman\model\PartTimeManager.java
``` java
    @Override
    public ObservableList<Shift> getShiftList() {
        return shifts.asObservableList();
    }
```
###### \java\seedu\ptman\model\shift\Capacity.java
``` java
/**
 * Represents a shift's capacity
 * Guarantees: immutable; is valid as declared in {@link #isValidCapacity(String)}
 */
public class Capacity {

    public static final String MESSAGE_CAPACITY_CONSTRAINTS = "Capacity should be a positive integer.";
    public static final String CAPACITY_VALIDATION_REGEX = "^[1-9]\\d*$";

    public final int capacity;

    public Capacity(String capacity) {
        requireNonNull(capacity);
        checkArgument(isValidCapacity(capacity), MESSAGE_CAPACITY_CONSTRAINTS);
        this.capacity = Integer.parseInt(capacity);
    }

    public static Boolean isValidCapacity(String test) {
        return test.matches(CAPACITY_VALIDATION_REGEX);
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return String.valueOf(capacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Capacity capacity1 = (Capacity) o;
        return Objects.equals(capacity, capacity1.capacity);
    }

    @Override
    public int hashCode() {
        return new Integer(capacity).hashCode();
    }
}
```
###### \java\seedu\ptman\model\shift\Date.java
``` java
/**
 * Represents a shift's date
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String STRING_DATE_PATTERN = "dd-MM-yy";
    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be in dd-mm-yy format";

    public final LocalDate date;

    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern(STRING_DATE_PATTERN));
    }

    /**
     * Returns true if a given string is a valid date.
     * @param test
     * @return
     */
    public static Boolean isValidDate(String test) {
        try {
            LocalDate.parse(test, DateTimeFormatter.ofPattern(STRING_DATE_PATTERN));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern(STRING_DATE_PATTERN));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Date date1 = (Date) o;
        return Objects.equals(date, date1.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    public int compareTo(Date startDate) {
        return date.compareTo(startDate.getLocalDate());
    }

    public LocalDate getLocalDate() {
        return date;
    }
}
```
###### \java\seedu\ptman\model\shift\exceptions\DuplicateShiftException.java
``` java
/**
 * Signals that the operation will result in duplicate Shift objects.
 */
public class DuplicateShiftException extends DuplicateDataException {
    public DuplicateShiftException() {
        super("Operation would result in duplicate employees");
    }
}
```
###### \java\seedu\ptman\model\shift\exceptions\ShiftFullException.java
``` java
/**
 * Signals that the operation is attempting to add an employee to a full shift
 */
public class ShiftFullException extends Exception {}
```
###### \java\seedu\ptman\model\shift\exceptions\ShiftNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified shift.
 */
public class ShiftNotFoundException extends Exception {}
```
###### \java\seedu\ptman\model\shift\Shift.java
``` java
/**
 * Represents a shift that employees can work in.
 */
public class Shift {
    public static final String MESSAGE_SHIFT_CONSTRAINTS = "Start time should be after the end time.";
    private Time startTime;
    private Time endTime;
    private Date date;
    private UniqueEmployeeList uniqueEmployeeList;
    private Capacity capacity;

    public Shift(Date date, Time startTime, Time endTime, Capacity capacity) {
        requireAllNonNull(startTime, endTime, capacity);
        checkArgument(endTime.isAfter(startTime), MESSAGE_SHIFT_CONSTRAINTS);
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.uniqueEmployeeList = new UniqueEmployeeList();
    }

    public Shift(Shift shift) {
        this.date = shift.getDate();
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.capacity = shift.getCapacity();
        this.uniqueEmployeeList = new UniqueEmployeeList();
        setEmployees(shift);
    }

    public Shift(Date date, Time startTime, Time endTime, Capacity capacity, Set<Employee> employees) {
        requireAllNonNull(date, startTime, endTime, capacity, employees);
        checkArgument(endTime.isAfter(startTime), MESSAGE_SHIFT_CONSTRAINTS);
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.date = date;
        this.uniqueEmployeeList = new UniqueEmployeeList(employees);
    }

    protected boolean contains(Employee employee) {
        return uniqueEmployeeList.contains(employee);
    }

    /**
     * Adds an employee to this shift
     * @throws DuplicateEmployeeException
     * @throws ShiftFullException
     */
    public void addEmployee(Employee employee) throws DuplicateEmployeeException, ShiftFullException {
        if (this.isFull()) {
            throw new ShiftFullException();
        }
        uniqueEmployeeList.add(employee);
    }

    /**
     * Removes an employee from this shift
     * @throws EmployeeNotFoundException
     */
    public void removeEmployee(Employee employee) throws EmployeeNotFoundException {
        uniqueEmployeeList.remove(employee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shift shift = (Shift) o;
        return startTime.equals(shift.startTime)
                && endTime.equals(shift.endTime)
                && date.equals(shift.date)
                && uniqueEmployeeList.equals(shift.uniqueEmployeeList)
                && capacity.equals(shift.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, date, uniqueEmployeeList, capacity);
    }

    public ObservableList<Employee> getEmployeeList() {
        return uniqueEmployeeList.asObservableList();
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public int getSlotsLeft() {
        int numEmployees = Iterables.size(uniqueEmployeeList);
        return capacity.getCapacity() - numEmployees;
    }

    public boolean isFull() {
        return getEmployeeList().size() >= this.capacity.getCapacity();
    }

    /**
     * Compares this shift to another.
     * Returns a negative integer if the argument is an earlier shift,
     * 0 if the shifts are equal, or a positive integer if the argument is a later shift.
     */
    public int compareTo(Shift other) {
        if (date.equals(other.getDate())) {
            return startTime.compareTo(other.getStartTime());
        } else if (date.compareTo(other.getDate()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    public UniqueEmployeeList getUniqueEmployeeList() {
        return uniqueEmployeeList;
    }

    public void setEmployees(Shift shift) {
        for (final Employee employee : shift.getEmployeeList()) {
            try {
                uniqueEmployeeList.add(employee);
            } catch (DuplicateEmployeeException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        return sb.append("Date: ")
                .append(date)
                .append(" Start time: ")
                .append(startTime)
                .append(" End time: ")
                .append(endTime)
                .append(" Capacity: ")
                .append(capacity).toString();
    }

    public Date getDate() {
        return date;
    }

    public boolean containsEmployee(Employee key) {
        return uniqueEmployeeList.contains(key);
    }

    public Set<Employee> getEmployees() {
        return Collections.unmodifiableSet(uniqueEmployeeList.toSet());
    }
}
```
###### \java\seedu\ptman\model\shift\Time.java
``` java
/**
 * Represents a shift's start or end time.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_TIME_CONSTRAINTS = "Time should be in 24-hour format.";

    public final LocalTime time;

    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_TIME_CONSTRAINTS);
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));
    }

    /**
     * Returns true if a given string is a valid time.
     * @param test
     * @return
     */
    public static Boolean isValidTime(String test) {
        try {
            LocalTime.parse(test, DateTimeFormatter.ofPattern("HHmm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isAfter(Time t) {
        return time.isAfter(t.time);
    }

    @Override
    public String toString() {
        return time.toString().replace(":", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Time time1 = (Time) o;
        return Objects.equals(time, time1.time);
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }

    public int compareTo(Time startTime) {
        return time.compareTo(startTime.getLocalTime());
    }

    public LocalTime getLocalTime() {
        return time;
    }
}
```
###### \java\seedu\ptman\model\shift\UniqueShiftList.java
``` java
/**
 * A list of shifts that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Shift#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueShiftList implements Iterable<Shift> {

    private final ObservableList<Shift> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent shift as the given argument.
     */
    public boolean contains(Shift toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a shift to the list.
     *
     * @throws DuplicateShiftException if the shift to add is a duplicate of an existing shift in the list.
     */
    public void add(Shift toAdd) throws DuplicateShiftException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateShiftException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the shift {@code target} in the list with {@code editedShift}.
     *
     * @throws DuplicateShiftException if the replacement is equivalent to another existing shift in the list.
     * @throws ShiftNotFoundException if {@code target} could not be found in the list.
     */
    public void setShift(Shift target, Shift editedShift)
            throws DuplicateShiftException, ShiftNotFoundException {
        requireNonNull(editedShift);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ShiftNotFoundException();
        }

        if (!target.equals(editedShift) && internalList.contains(editedShift)) {
            throw new DuplicateShiftException();
        }

        internalList.set(index, editedShift);
    }

    /**
     * Removes the equivalent shift from the list.
     *
     * @throws ShiftNotFoundException if no such shift could be found in the list.
     */
    public boolean remove(Shift toRemove) throws ShiftNotFoundException {
        requireNonNull(toRemove);
        final boolean shiftFoundAndDeleted = internalList.remove(toRemove);
        if (!shiftFoundAndDeleted) {
            throw new ShiftNotFoundException();
        }
        return shiftFoundAndDeleted;
    }

    public void setShifts(UniqueShiftList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setShifts(List<Shift> shifts) throws DuplicateShiftException {
        requireAllNonNull(shifts);
        final UniqueShiftList replacement = new UniqueShiftList();
        for (final Shift shift : shifts) {
            replacement.add(shift);
        }
        setShifts(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Shift> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Shift> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueShiftList // instanceof handles nulls
                        && this.internalList.equals(((UniqueShiftList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\ptman\storage\XmlAdaptedShift.java
``` java
/**
 * JAXB-friendly version of the Shift.
 */
public class XmlAdaptedShift {

    public static final String MISSING_FIELD_MESSAGE_FORMAT_SHIFT = "Shifts's %s field is missing!";

    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String startTime;
    @XmlElement(required = true)
    private String endTime;
    @XmlElement(required = true)
    private String capacity;

    @XmlElement
    private List<XmlAdaptedEmployee> employees = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedShift.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedShift() {}

    /**
     * Constructs an {@code XmlAdaptedShift} with the given shift details.
     */
    public XmlAdaptedShift(String date, String startTime, String endTime,
                           String capacity, List<XmlAdaptedEmployee> employees) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;

        if (employees != null) {
            this.employees = new ArrayList<>(employees);
        }
    }

    /**
     * Converts a given Shift into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedShift
     */
    public XmlAdaptedShift(Shift source) {
        date = source.getDate().toString();
        startTime = source.getStartTime().toString();
        endTime = source.getEndTime().toString();
        capacity = source.getCapacity().toString();

        employees = new ArrayList<>();
        for (Employee employee : source.getEmployeeList()) {
            employees.add(new XmlAdaptedEmployee(employee));
        }
    }

    /**
     * Decrypts date
     * @return
     * @throws IllegalValueException
     */
    private Date setDate() throws IllegalValueException {
        if (this.date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.date)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        return new Date(this.date);
    }

    /**
     * Decryptes time
     * @param time
     * @return
     * @throws IllegalValueException
     */
    private Time setTime(String time) throws IllegalValueException {
        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(time)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        return new Time(time);
    }

    /**
     * Decryptes capacity
     * @return
     * @throws IllegalValueException
     */
    private Capacity setCapacity() throws IllegalValueException {
        if (this.capacity == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT,
                    Capacity.class.getSimpleName()));
        }
        if (!Capacity.isValidCapacity(this.capacity)) {
            throw new IllegalValueException(Capacity.MESSAGE_CAPACITY_CONSTRAINTS);
        }
        return new Capacity(this.capacity);
    }

    /**
     * Converts this jaxb-friendly adapted shift object into the model's Shift object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted shift
     */
    public Shift toModelType() throws IllegalValueException {
        final List<Employee> employees = new ArrayList<>();
        for (XmlAdaptedEmployee employee : this.employees) {
            employees.add(employee.toModelType());
        }

        final Date date = setDate();
        final Time startTime = setTime(this.startTime);
        final Time endTime = setTime(this.endTime);
        final Capacity capacity = setCapacity();

        return new Shift(date, startTime, endTime, capacity, new HashSet<>(employees));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedShift)) {
            return false;
        }

        XmlAdaptedShift otherShift = (XmlAdaptedShift) other;
        return Objects.equals(date, otherShift.date)
                && Objects.equals(startTime, otherShift.startTime)
                && Objects.equals(endTime, otherShift.endTime)
                && Objects.equals(capacity, otherShift.capacity)
                && employees.equals(otherShift.employees);
    }
}
```
