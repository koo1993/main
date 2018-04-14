# shanwpf
###### \java\seedu\ptman\commons\util\DateUtilTest.java
``` java
public class DateUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getWeekFromDate_validDate_returnsWeekNumber() {
        // 14th Jan 2018 is in the 2nd week of 2018
        assertEquals(DateUtil.getWeekFromDate(LocalDate.of(2018, 1, 14)), 2);

        // 15th Jan 2018 is in the 3rd week of 2018
        assertEquals(DateUtil.getWeekFromDate(LocalDate.of(2018, 1, 15)), 3);
    }

    @Test
    public void getWeekFromDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getWeekFromDate(null);
    }

    @Test
    public void getMondayOfDate_validDate_returnsMondayDate() {
        // Sunday 8th April 2018 returns Monday 2nd April 2018
        assertEquals(DateUtil.getMondayOfDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 2));

        // Monday 9th April 2018 returns Monday 9th April 2018
        assertEquals(DateUtil.getMondayOfDate(LocalDate.of(2018, 4, 9)), LocalDate.of(2018, 4, 9));
    }

    @Test
    public void getMondayOfDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getMondayOfDate(null);
    }

```
###### \java\seedu\ptman\logic\commands\AddShiftCommandIntegrationTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code AddShiftCommand}.
 */
public class AddShiftCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPartTimeManagerWithShifts(), new UserPrefs(), new OutletInformation());
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_newShift_success() throws Exception {
        Shift validShift = new ShiftBuilder().withDate(LocalDate.now()).build();

        Model expectedModel =
                new ModelManager(getTypicalPartTimeManagerWithShifts(), new UserPrefs(), new OutletInformation());
        expectedModel.setTrueAdminMode(new Password());
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.addShift(validShift);

        assertCommandSuccess(prepareCommand(validShift, model), model,
                String.format(AddShiftCommand.MESSAGE_SUCCESS, validShift), expectedModel);
    }

    @Test
    public void execute_duplicateShift_throwsCommandException() throws DuplicateShiftException {
        Shift shift = new ShiftBuilder().withDate(LocalDate.now()).build();
        model.addShift(shift);
        assertCommandFailure(prepareCommand(shift, model), model, AddShiftCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_invalidShiftDate_throwsCommandException() {
        Shift shift = new ShiftBuilder().withDate("01-01-10").build();
        assertCommandFailure(prepareCommand(shift, model), model, AddShiftCommand.MESSAGE_DATE_OVER);
    }

    @Test
    public void execute_startTimeAfterEndTime_throwsCommandException() {
        Shift shift = new ShiftBuilder().withStartTime("1000").withEndTime("0800").build();
        assertCommandFailure(prepareCommand(shift, model), model, AddShiftCommand.MESSAGE_INVALID_TIME);
    }

    /**
     * Generates a new {@code AddShiftCommand} which upon execution, adds {@code shift} into the {@code model}.
     */
    private AddShiftCommand prepareCommand(Shift shift, Model model) {
        AddShiftCommand command = new AddShiftCommand(shift);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\ptman\logic\commands\AddShiftCommandTest.java
``` java
public class AddShiftCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullShift_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddShiftCommand(null);
    }

    @Test
    public void execute_shiftAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingShiftAdded modelStub = new ModelStubAcceptingShiftAdded();
        modelStub.setTrueAdminMode(new Password());

        Shift validShift = new ShiftBuilder().withDate(LocalDate.now()).build();
        CommandResult commandResult = getAddShiftCommandForShift(validShift, modelStub).execute();

        assertEquals(String.format(AddShiftCommand.MESSAGE_SUCCESS, validShift), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validShift), modelStub.shiftsAdded);
    }

    @Test
    public void execute_duplicateShift_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateShiftException();
        modelStub.setTrueAdminMode(new Password());
        Shift validShift = new ShiftBuilder().withDate(LocalDate.now()).build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddShiftCommand.MESSAGE_DUPLICATE_SHIFT);

        getAddShiftCommandForShift(validShift, modelStub).execute();
    }

    @Test
    public void equals() {
        AddShiftCommand addMondayAmCommand = new AddShiftCommand(SHIFT_MONDAY_AM);
        AddShiftCommand addMondayPmCommand = new AddShiftCommand(SHIFT_MONDAY_PM);

        // same object -> returns true
        assertTrue(addMondayAmCommand.equals(addMondayAmCommand));

        // same values -> returns true
        AddShiftCommand addMondayAmCommandCopy = new AddShiftCommand(SHIFT_MONDAY_AM);
        assertTrue(addMondayAmCommand.equals(addMondayAmCommandCopy));

        // different types -> returns false
        assertFalse(addMondayAmCommand.equals(1));

        // null -> returns false
        assertFalse(addMondayAmCommand.equals(null));

        // different employee -> returns false
        assertFalse(addMondayAmCommand.equals(addMondayPmCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given employee.
     */
    private AddShiftCommand getAddShiftCommandForShift(Shift shift, Model model) {
        AddShiftCommand command = new AddShiftCommand(shift);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addEmployee(Employee employee) throws DuplicateEmployeeException {
            fail("This method should not be called.");
        }

        @Override
        public boolean isAdminPassword(Password password) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void setAdminPassword(Password password) {
            fail("This method should not be called.");
        }


        @Override
        public void addShift(Shift shift) throws DuplicateShiftException {
            fail("This method should not be called.");
        }
        @Override
        public boolean isAdminMode() {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public boolean setTrueAdminMode(Password password) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void setFalseAdminMode() {
            fail("This method should not be called.");
        }

        @Override
        public void storeResetPassword(Employee employee, Password tempPassword) {
            fail("This method should not be called.");
        }

        @Override
        public void storeResetPassword(OutletInformation outlet, Password tempPassword) {
            fail("This method should not be called.");
        }

        @Override
        public boolean isCorrectTempPwd(OutletInformation outlet, Password tempPassword) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public boolean isCorrectTempPwd(Employee employee, Password tempPassword) {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public void deleteTagFromAllEmployee(Tag tag) {
            fail("This method should not be called");
        }

        @Override
        public void resetData(ReadOnlyPartTimeManager newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyPartTimeManager getPartTimeManager() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteEmployee(Employee target) throws EmployeeNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateEmployee(Employee target, Employee editedEmployee)
                throws DuplicateEmployeeException {
            fail("This method should not be called.");
        }

        @Override
        public void updateOutlet(OutletInformation outlet) throws NoOutletInformationFieldChangeException {
            fail("This method should not be called.");
        }

        @Override
        public String getEncryptionModeMessage() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public boolean getEncryptionMode() {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public ObservableList<Employee> getFilteredEmployeeList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Shift> getFilteredShiftList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public OutletInformation getOutletInformation() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredEmployeeList(Predicate<Employee> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void setFilteredShiftListToWeek(LocalDate date) {
            fail("This method should not be called.");
        }

        @Override
        public void deleteShift(Shift shiftToDelete) throws ShiftNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateShift(Shift shiftToApply, Shift editedShift)
                throws ShiftNotFoundException, DuplicateShiftException {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredShiftList(Predicate<Shift> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void encryptLocalStorage() {
            fail("This method should not be called.");
        }

        @Override
        public void decryptLocalStorage() {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateEmployeeException when trying to add an employee.
     */
    private class ModelStubThrowingDuplicateShiftException extends ModelStub {
        @Override
        public void addShift(Shift shift) throws DuplicateShiftException {
            throw new DuplicateShiftException();
        }

        @Override
        public boolean isAdminMode() {
            return true;
        }

        @Override
        public boolean setTrueAdminMode(Password password) {
            requireNonNull(password);
            return true;
        }

        @Override
        public ReadOnlyPartTimeManager getPartTimeManager() {
            return new PartTimeManager();
        }
    }

    /**
     * A Model stub that always accept the employee being added.
     */
    private class ModelStubAcceptingShiftAdded extends ModelStub {
        final ArrayList<Shift> shiftsAdded = new ArrayList<>();

        @Override
        public void addShift(Shift shift) throws DuplicateShiftException {
            requireNonNull(shift);
            shiftsAdded.add(shift);
        }

        @Override
        public boolean setTrueAdminMode(Password password) {
            requireNonNull(password);
            return true;
        }

        @Override
        public boolean isAdminMode() {
            return true;
        }

        @Override
        public ReadOnlyPartTimeManager getPartTimeManager() {
            return new PartTimeManager();
        }
    }

}
```
###### \java\seedu\ptman\logic\commands\ApplyCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for ApplyCommand.
 */
public class ApplyCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(new PartTimeManager(getTypicalPartTimeManagerWithShifts()), new UserPrefs(),
            new OutletInformation());

    @Before
    public void setMode_adminMode() {
        model.setTrueAdminMode(new Password());
    }

    @Before
    public void showAllShifts() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
    }

    @Test
    public void execute_userModeNoPassword_throwsMissingPasswordException() throws Exception {
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        Model model = prepareModel(false, shift, employee);

        ApplyCommand applyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(MissingPasswordException.class);
        applyCommand.execute();
    }

    @Test
    public void execute_userModeIncorrectPassword_throwsInvalidPasswordException() throws Exception {
        Employee employee = new EmployeeBuilder().withName("Present").withPassword("incorrect").build();
        Shift shift = new ShiftBuilder().build();
        Model model = prepareModel(false, shift, employee);

        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(InvalidPasswordException.class);
        applyCommand.execute();
    }

    @Test
    public void execute_adminModeEmployeeNotInShift_success() throws Exception {
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Employee expectedEmployee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        Shift expectedShift = new ShiftBuilder().build();
        expectedShift.addEmployee(expectedEmployee);

        Model model = prepareModel(true, shift, employee);
        Model expectedModel = prepareModel(true, expectedShift, expectedEmployee);

        ApplyCommand applyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        String expectedMessage =
                String.format(ApplyCommand.MESSAGE_APPLY_SHIFT_SUCCESS, expectedEmployee.getName(), 1);

        assertCommandSuccess(applyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_shiftFull_throwsCommandException()
            throws ShiftFullException, DuplicateEmployeeException {
        Employee employee1 = new EmployeeBuilder().withName("first").build();
        Employee employee2 = new EmployeeBuilder().withName("second").build();
        Shift shift = new ShiftBuilder().withCapacity("1").build();
        shift.addEmployee(employee1);
        Model model = prepareModel(false, shift, employee1, employee2);

        String expectedMessage = String.format(MESSAGE_SHIFT_FULL, INDEX_FIRST_SHIFT.getOneBased());
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertCommandFailure(applyCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicateEmployee_throwsCommandException()
            throws ShiftFullException, DuplicateEmployeeException {
        Employee employee1 = new EmployeeBuilder().withName("first").build();
        Shift shift = new ShiftBuilder().withCapacity("2").build();
        shift.addEmployee(employee1);
        Model model = prepareModel(false, shift, employee1);

        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertCommandFailure(applyCommand, model, MESSAGE_DUPLICATE_EMPLOYEE);
    }

    @Test
    public void execute_shiftOver_throwsCommandException() {
        Shift shift = new ShiftBuilder().withDate("01-01-10").withCapacity("3").build();
        Employee employee = new EmployeeBuilder().build();
        Model model = prepareModel(false, shift, employee);

        String expectedMessage = String.format(MESSAGE_SHIFT_OVER, INDEX_FIRST_SHIFT.getOneBased());
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertCommandFailure(applyCommand, model, expectedMessage);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(applyCommand.equals(applyCommand));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        ApplyCommand applyCommand1 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommand2 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(applyCommand1.equals(applyCommand2));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommand.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommand.equals(null));
    }

    @Test
    public void equals_differentShifts_returnsFalse() {
        ApplyCommand applyCommandFirst = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommandSecond = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        assertFalse(applyCommandFirst.equals(applyCommandSecond));
    }

    @Test
    public void equals_differentEmployees_returnsFalse() {
        ApplyCommand applyCommandFirst = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        ApplyCommand applyCommandSecond = prepareCommandWithPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(applyCommandFirst.equals(applyCommandSecond));
    }

    @Test
    public void execute_shiftIndexOutOfRange_throwsCommandException() {
        ApplyCommand applyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        Assert.assertThrows(CommandException.class, applyCommand::execute);
    }

    @Test
    public void execute_employeeIndexOutOfRange_throwsCommandException() {
        ApplyCommand applyCommand = prepareCommandWithPassword(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        Assert.assertThrows(CommandException.class, applyCommand::execute);
    }

    @Test
    public void execute_incorrectPassword_throwsInvalidPasswordException() {
        model.setFalseAdminMode();
        ApplyCommand applyCommand = new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT,
                Optional.of(new Password("wrongPassword")));
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        Assert.assertThrows(InvalidPasswordException.class, applyCommand::execute);
    }

    @After
    public void reset() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
    }

    /**
     * Returns an {@code ApplyCommand} with parameters {@code employeeIndex} and {@code shiftIndex}
     * and a valid employee password
     */
    private ApplyCommand prepareCommandWithPassword(Index employeeIndex, Index shiftIndex, Model model) {
        ApplyCommand applyCommand = new ApplyCommand(employeeIndex, shiftIndex, Optional.of(new Password()));
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return applyCommand;
    }

    /**
     * Returns an {@code ApplyCommand} with parameters {@code employeeIndex} and {@code shiftIndex}
     * without a password
     */
    private ApplyCommand prepareCommandWithoutPassword(Index employeeIndex, Index shiftIndex, Model model) {
        ApplyCommand applyCommand = new ApplyCommand(employeeIndex, shiftIndex, Optional.empty());
        applyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return applyCommand;
    }

    /**
     * Returns a {@Code Model} with parameters {@code shift} and {@code employees}.
     */
    private Model prepareModel(boolean isAdmin, Shift shift, Employee... employees) {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        if (isAdmin) {
            model.setTrueAdminMode(new Password());
        } else {
            model.setFalseAdminMode();
        }
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);

        try {
            model.addShift(shift);
            for (final Employee employee: employees) {
                model.addEmployee(employee);
            }
        } catch (DuplicateShiftException e) {
            throw new AssertionError("Shift should not be a duplicate");
        } catch (DuplicateEmployeeException e) {
            throw new AssertionError("Employees should not be duplicates");
        }
        return model;
    }
}
```
###### \java\seedu\ptman\logic\commands\DeleteShiftCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteShiftCommand}.
 */
public class DeleteShiftCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManagerWithShifts(), new UserPrefs(),
            new OutletInformation());

    @Before
    public void setupAdminMode() {
        model.setTrueAdminMode(new Password());
    }

    @Before
    public void showAllShifts() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        DeleteShiftCommand deleteShiftCommand = prepareCommand(INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(DeleteShiftCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        ModelManager expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(),
                new OutletInformation());
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.deleteShift(shiftToDelete);

        assertCommandSuccess(deleteShiftCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        DeleteShiftCommand deleteShiftCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteShiftCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        DeleteShiftCommand deleteShiftCommand = prepareCommand(INDEX_FIRST_SHIFT);

        String expectedMessage = String.format(DeleteShiftCommand.MESSAGE_DELETE_SHIFT_SUCCESS, shiftToDelete);

        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.deleteShift(shiftToDelete);
        assertNotEquals(shiftToDelete, expectedModel.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased()));

        assertCommandSuccess(deleteShiftCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Index outOfBoundIndex = INDEX_OUT_OF_BOUNDS_SHIFT;

        DeleteShiftCommand deleteShiftCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteShiftCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Shift shiftToDelete = model.getFilteredShiftList().get(INDEX_FIRST_SHIFT.getZeroBased());
        DeleteShiftCommand deleteShiftCommand = prepareCommand(INDEX_FIRST_SHIFT);
        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);

        // delete -> first employee deleted
        deleteShiftCommand.execute();
        undoRedoStack.push(deleteShiftCommand);

        // undo -> reverts parttimemanager back to previous state and filtered employee list to show all employees
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first employee deleted again
        expectedModel.deleteShift(shiftToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);
        DeleteShiftCommand deleteShiftCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCommand not pushed into undoRedoStack
        assertCommandFailure(deleteShiftCommand, model, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() throws Exception {
        DeleteShiftCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_SHIFT);
        DeleteShiftCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_SHIFT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteShiftCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_SHIFT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteShiftCommand prepareCommand(Index index) {
        DeleteShiftCommand deleteShiftCommand = new DeleteShiftCommand(index);
        deleteShiftCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteShiftCommand;
    }
}
```
###### \java\seedu\ptman\logic\commands\UnapplyCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for UnapplyCommand.
 */
public class UnapplyCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(new PartTimeManager(getTypicalPartTimeManagerWithShifts()), new UserPrefs(),
            new OutletInformation());

    @Before
    public void setMode_adminMode() {
        model.setTrueAdminMode(new Password());
    }

    @Before
    public void showAllShifts() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
    }

    @Test
    public void execute_employeeNotInShift_throwsCommandException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        model.setTrueAdminMode(new Password());
        Employee employee = new EmployeeBuilder().withName("Absent").build();
        Shift shift = new ShiftBuilder().build();
        model.addEmployee(employee);
        model.addShift(shift);
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        assertCommandFailure(unapplyCommand, model, UnapplyCommand.MESSAGE_EMPLOYEE_NOT_FOUND);
    }

    @Test
    public void execute_adminModeEmployeeInShift_success() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        Model expectedModel = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        model.setTrueAdminMode(new Password());
        expectedModel.setTrueAdminMode(new Password());
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Employee expectedEmployee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        Shift expectedShift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        expectedModel.addShift(expectedShift);
        expectedModel.addEmployee(expectedEmployee);
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        String expectedMessage =
                String.format(UnapplyCommand.MESSAGE_UNAPPLY_SHIFT_SUCCESS, expectedEmployee.getName(), 1);

        assertCommandSuccess(unapplyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_userModeEmployeeInShift_success() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        Model expectedModel = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.setFalseAdminMode();
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Employee expectedEmployee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        Shift expectedShift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        expectedModel.addShift(expectedShift);
        expectedModel.addEmployee(expectedEmployee);
        UnapplyCommand unapplyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);

        String expectedMessage =
                String.format(UnapplyCommand.MESSAGE_UNAPPLY_SHIFT_SUCCESS, expectedEmployee.getName(), 1);

        assertCommandSuccess(unapplyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_userModeNoPassword_throwsMissingPasswordException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").build();
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(MissingPasswordException.class);
        unapplyCommand.execute();
    }

    @Test
    public void execute_userModeIncorrectPassword_throwsInvalidPasswordException() throws Exception {
        Model model = new ModelManager(new PartTimeManager(), new UserPrefs(), new OutletInformation());
        model.setFalseAdminMode();
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        Employee employee = new EmployeeBuilder().withName("Present").withPassword("incorrect").build();
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(employee);
        model.addEmployee(employee);
        model.addShift(shift);
        UnapplyCommand unapplyCommand = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        thrown.expect(InvalidPasswordException.class);
        unapplyCommand.execute();
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(unapplyCommand.equals(unapplyCommand));
        assertTrue(unapplyCommandPw.equals(unapplyCommandPw));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        UnapplyCommand unapplyCommand1 = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommand2 = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw1 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw2 = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertTrue(unapplyCommand1.equals(unapplyCommand2));
        assertTrue(unapplyCommandPw1.equals(unapplyCommandPw2));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(unapplyCommand.equals(1));
        assertFalse(unapplyCommandPw.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(unapplyCommand.equals(null));
        assertFalse(unapplyCommandPw.equals(null));
    }

    @Test
    public void equals_differentShifts_returnsFalse() {
        UnapplyCommand unapplyCommandFirst =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandSecond =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        UnapplyCommand unapplyCommandPwFirst =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPwSecond =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_SECOND_SHIFT, model);
        assertFalse(unapplyCommandFirst.equals(unapplyCommandSecond));
        assertFalse(unapplyCommandPwFirst.equals(unapplyCommandPwSecond));
    }

    @Test
    public void equals_differentEmployees_returnsFalse() {
        UnapplyCommand unapplyCommandFirst =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandSecond =
                prepareCommandWithoutPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPwFirst =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPwSecond =
                prepareCommandWithPassword(INDEX_SECOND_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertFalse(unapplyCommandFirst.equals(unapplyCommandSecond));
        assertFalse(unapplyCommandPwFirst.equals(unapplyCommandPwSecond));
    }

    @Test
    public void execute_shiftIndexOutOfRange_throwsCommandException() {
        UnapplyCommand unapplyCommand =
                prepareCommandWithoutPassword(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        UnapplyCommand unapplyCommandPw =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, Index.fromOneBased(99), model);
        Assert.assertThrows(CommandException.class, unapplyCommand::execute);
        Assert.assertThrows(CommandException.class, unapplyCommandPw::execute);
    }

    @Test
    public void execute_employeeIndexOutOfRange_throwsCommandException() {
        UnapplyCommand unapplyCommand = prepareCommandWithoutPassword(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommandPw = prepareCommandWithPassword(Index.fromOneBased(99), INDEX_FIRST_SHIFT, model);
        Assert.assertThrows(CommandException.class, unapplyCommand::execute);
        Assert.assertThrows(CommandException.class, unapplyCommandPw::execute);
    }

    @Test
    public void execute_incorrectPassword_throwsInvalidPasswordException() {
        model.setFalseAdminMode();
        UnapplyCommand unapplyCommand = new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT,
                Optional.of(new Password("wrongPassword")));
        unapplyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        Assert.assertThrows(InvalidPasswordException.class, unapplyCommand::execute);
    }

    @Test
    public void hashCode_sameValues_returnsSameHashCode() {
        UnapplyCommand unapplyCommand1 =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        UnapplyCommand unapplyCommand2 =
                prepareCommandWithPassword(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, model);
        assertEquals(unapplyCommand1.hashCode(), unapplyCommand2.hashCode());
    }

    @After
    public void reset() {
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
    }

    /**
     * Returns an {@code UnapplyCommand} with parameters {@code employeeIndex} and {@code shiftIndex}
     * and a valid employee password
     */
    private UnapplyCommand prepareCommandWithPassword(Index employeeIndex, Index shiftIndex, Model model) {
        UnapplyCommand unapplyCommand = new UnapplyCommand(employeeIndex, shiftIndex, Optional.of(new Password()));
        unapplyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return unapplyCommand;
    }

    /**
     * Returns an {@code UnapplyCommand} with parameters {@code employeeIndex} and {@code shiftIndex}
     * without a password
     */
    private UnapplyCommand prepareCommandWithoutPassword(Index employeeIndex, Index shiftIndex, Model model) {
        UnapplyCommand unapplyCommand = new UnapplyCommand(employeeIndex, shiftIndex, Optional.empty());
        unapplyCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return unapplyCommand;
    }
}
```
###### \java\seedu\ptman\logic\parser\AddShiftCommandParserTest.java
``` java
public class AddShiftCommandParserTest {
    private AddShiftCommandParser parser = new AddShiftCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Shift expectedShift = new ShiftBuilder().withDate(VALID_DATE_12MAR).withStartTime(VALID_TIME_START_10AM)
                .withEndTime(VALID_TIME_END_8PM).withCapacity(VALID_CAPACITY_1).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple dates - last day accepted
        assertParseSuccess(parser, DATE_DESC_13MAR + DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple start times - last start time accepted
        assertParseSuccess(parser, DATE_DESC_12MAR + TIME_START_DESC_12PM + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple end times - last end time accepted
        assertParseSuccess(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM +  TIME_END_DESC_10PM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple capacities - last capacity accepted
        assertParseSuccess(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM +  TIME_END_DESC_8PM
                + CAPACITY_DESC_2 + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE);

        // missing date prefix
        assertParseFailure(parser,  VALID_DATE_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, expectedMessage);

        // missing start time prefix
        assertParseFailure(parser,  DATE_DESC_12MAR + VALID_TIME_START_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, expectedMessage);
        // missing end time prefix
        assertParseFailure(parser,  DATE_DESC_12MAR + TIME_START_DESC_10AM + VALID_TIME_END_8PM
                + CAPACITY_DESC_1, expectedMessage);
        // missing capacity prefix
        assertParseFailure(parser,  DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + VALID_CAPACITY_1, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_DATE_12MAR + VALID_TIME_START_10AM + VALID_TIME_END_8PM + VALID_CAPACITY_1,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid date
        assertParseFailure(parser, INVALID_DATE_DESC + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, Date.MESSAGE_DATE_CONSTRAINTS);

        // invalid start time
        assertParseFailure(parser, DATE_DESC_12MAR + INVALID_TIME_START_DESC + TIME_END_DESC_8PM
                        + CAPACITY_DESC_1, Time.MESSAGE_TIME_CONSTRAINTS);

        // invalid end time
        assertParseFailure(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM + INVALID_TIME_END_DESC
                        + CAPACITY_DESC_1, Time.MESSAGE_TIME_CONSTRAINTS);

        // invalid capacity
        assertParseFailure(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                        + INVALID_CAPACITY_DESC, Capacity.MESSAGE_CAPACITY_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM + INVALID_TIME_END_DESC
                        + INVALID_CAPACITY_DESC, Time.MESSAGE_TIME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                        + INVALID_CAPACITY_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\ptman\logic\parser\ApplyCommandParserTest.java
``` java
public class ApplyCommandParserTest {

    private ApplyCommandParser parser = new ApplyCommandParser();

    @Test
    public void parse_validArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1 pw/DEFAULT1",
                new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())));
    }

    @Test
    public void parse_noPassword_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1", new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.empty()));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a b pw/DEFAULT1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ApplyCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\ptman\logic\parser\DeleteShiftCommandParserTest.java
``` java
public class DeleteShiftCommandParserTest {

    private DeleteShiftCommandParser parser = new DeleteShiftCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteShiftCommand() {
        assertParseSuccess(parser, "1", new DeleteShiftCommand(INDEX_FIRST_SHIFT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteShiftCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\ptman\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseTime_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTime((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTime((Optional<String>) null));
    }

    @Test
    public void parseTime_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseTime(INVALID_TIME));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseTime(Optional.of(INVALID_TIME)));
    }

    @Test
    public void parseTime_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseTime(Optional.empty()).isPresent());
    }

    @Test
    public void parseTime_validValueWithoutWhitespace_returnsTime() throws Exception {
        Time expectedTime = new Time(VALID_TIME);
        assertEquals(expectedTime, ParserUtil.parseTime(VALID_TIME));
        assertEquals(Optional.of(expectedTime), ParserUtil.parseTime(Optional.of(VALID_TIME)));
    }

    @Test
    public void parseTime_validValueWithWhitespace_returnsTrimmedTime() throws Exception {
        String timeWithWhitespace = WHITESPACE + VALID_TIME + WHITESPACE;
        Time expectedTime = new Time(VALID_TIME);
        assertEquals(expectedTime, ParserUtil.parseTime(timeWithWhitespace));
        assertEquals(Optional.of(expectedTime), ParserUtil.parseTime(Optional.of(timeWithWhitespace)));
    }

    @Test
    public void parseCapacity_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseCapacity((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseCapacity((Optional<String>) null));
    }

    @Test
    public void parseCapacity_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseCapacity(INVALID_CAPACITY));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseCapacity(Optional.of(INVALID_CAPACITY)));
    }

    @Test
    public void parseCapacity_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseCapacity(Optional.empty()).isPresent());
    }

    @Test
    public void parseCapacity_validValueWithoutWhitespace_returnsCapacity() throws Exception {
        Capacity expectedCapacity = new Capacity(VALID_CAPACITY);
        assertEquals(expectedCapacity, ParserUtil.parseCapacity(VALID_CAPACITY));
        assertEquals(Optional.of(expectedCapacity), ParserUtil.parseCapacity(Optional.of(VALID_CAPACITY)));
    }

    @Test
    public void parseCapacity_validValueWithWhitespace_returnsTrimmedCapacity() throws Exception {
        String capacityWithWhitespace = WHITESPACE + VALID_CAPACITY + WHITESPACE;
        Capacity expectedCapacity = new Capacity(VALID_CAPACITY);
        assertEquals(expectedCapacity, ParserUtil.parseCapacity(capacityWithWhitespace));
        assertEquals(Optional.of(expectedCapacity), ParserUtil.parseCapacity(Optional.of(capacityWithWhitespace)));
    }

    @Test
    public void parseSecondIndex_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseSecondIndex("1 a"));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseSecondIndex("1 -1"));
    }
```
###### \java\seedu\ptman\logic\parser\PartTimeManagerParserTest.java
``` java
    @Test
    public void parseCommand_addShift() throws Exception {
        Shift shift = new ShiftBuilder().build();
        AddShiftCommand command = (AddShiftCommand) parser.parseCommand(ShiftUtil.getAddShiftCommand(shift));
        assertEquals(new AddShiftCommand(shift), command);
    }

    @Test
    public void parseCommand_addShiftAlias() throws Exception {
        Shift shift = new ShiftBuilder().build();
        AddShiftCommand command = (AddShiftCommand) parser.parseCommand(ShiftUtil.getAliasedAddShiftCommand(shift));
        assertEquals(new AddShiftCommand(shift), command);
    }

    @Test
    public void parseCommand_apply() throws Exception {
        ApplyCommand command = (ApplyCommand) parser.parseCommand(
                ApplyCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + " " + INDEX_FIRST_SHIFT.getOneBased() + " " + PREFIX_PASSWORD + "DEFAULT1");
        assertEquals(new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())), command);
    }

    @Test
    public void parseCommand_applyAlias() throws Exception {
        ApplyCommand command = (ApplyCommand) parser.parseCommand(
                ApplyCommand.COMMAND_ALIAS + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + " " + INDEX_FIRST_SHIFT.getOneBased() + " " + PREFIX_PASSWORD + "DEFAULT1");
        assertEquals(new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())), command);
    }

    @Test
    public void parseCommand_deleteShift() throws Exception {
        DeleteShiftCommand command = (DeleteShiftCommand) parser.parseCommand(
                DeleteShiftCommand.COMMAND_WORD + " " + INDEX_FIRST_SHIFT.getOneBased());
        assertEquals(new DeleteShiftCommand(INDEX_FIRST_EMPLOYEE), command);
    }

    @Test
    public void parseCommand_deleteShiftAlias() throws Exception {
        DeleteShiftCommand command = (DeleteShiftCommand) parser.parseCommand(
                DeleteShiftCommand.COMMAND_ALIAS + " " + INDEX_FIRST_SHIFT.getOneBased());
        assertEquals(new DeleteShiftCommand(INDEX_FIRST_EMPLOYEE), command);
    }

    @Test
    public void parseCommand_unapply() throws Exception {
        UnapplyCommand command = (UnapplyCommand) parser.parseCommand(
                UnapplyCommand.COMMAND_WORD + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + " " + INDEX_FIRST_SHIFT.getOneBased() + " " + PREFIX_PASSWORD + "DEFAULT1");
        assertEquals(new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())), command);
    }

    @Test
    public void parseCommand_unapplyAlias() throws Exception {
        UnapplyCommand command = (UnapplyCommand) parser.parseCommand(
                UnapplyCommand.COMMAND_ALIAS + " " + INDEX_FIRST_EMPLOYEE.getOneBased()
                        + " " + INDEX_FIRST_SHIFT.getOneBased() + " " + PREFIX_PASSWORD + "DEFAULT1");
        assertEquals(new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())), command);
    }
```
###### \java\seedu\ptman\logic\parser\UnapplyCommandParserTest.java
``` java
public class UnapplyCommandParserTest {

    private UnapplyCommandParser parser = new UnapplyCommandParser();

    @Test
    public void parse_userModeValidArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1 pw/DEFAULT1",
                new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())));
    }

    @Test
    public void parse_adminModeValidArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1",
                new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.empty()));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a b pw/DEFAULT1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnapplyCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\ptman\model\PartTimeManagerTest.java
``` java
    @Test
    public void resetData_withDuplicateShifts_throwsAssertionError() {
        List<Employee> newEmployees = Arrays.asList(ALICE);
        List<Shift> newShifts = Arrays.asList(SHIFT_MONDAY_AM, SHIFT_MONDAY_AM);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        OutletInformation outlet = new OutletInformation();
        PartTimeManagerStub newData = new PartTimeManagerStub(newEmployees, newTags, newShifts, outlet);

        thrown.expect(AssertionError.class);
        partTimeManager.resetData(newData);
    }
```
###### \java\seedu\ptman\model\shift\CapacityTest.java
``` java
public class CapacityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Capacity(null));
    }

    @Test
    public void constructor_invalidCapacity_throwsIllegalArgumentException() {
        String invalidCapacity = "w";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Capacity(invalidCapacity));
    }

    @Test
    public void isValidCapacity_nullCapacity_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Capacity.isValidCapacity(null));
    }

    @Test
    public void isValidCapacity_invalidCapacity_returnsFalse() {
        assertFalse(Capacity.isValidCapacity("")); // empty string
        assertFalse(Capacity.isValidCapacity(" ")); // spaces only
        assertFalse(Capacity.isValidCapacity("^")); // only non-numeric characters
        assertFalse(Capacity.isValidCapacity("3*")); // contains non-numeric characters
        assertFalse(Capacity.isValidCapacity("-3")); // only negative numeric string
        assertFalse(Capacity.isValidCapacity("0")); // only zero
    }

    @Test
    public void isValidCapacity_validCapacity_returnsTrue() {
        assertTrue(Capacity.isValidCapacity("4")); // positive integer only
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Capacity test = new Capacity("5");
        assertEquals(test.toString(), "5");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Capacity test = new Capacity("5");
        Capacity other = new Capacity("5");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Capacity test = new Capacity("5");
        Capacity other = new Capacity("3");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameValue_returnsTrue() {
        Capacity test = new Capacity("4");
        assertEquals(test.hashCode(), new Integer(4).hashCode());
    }
}
```
###### \java\seedu\ptman\model\shift\DateTest.java
``` java
public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Date((String) null));
        Assert.assertThrows(NullPointerException.class, () -> new Date((LocalDate) null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        String invalidDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Date(invalidDate));
    }

    @Test
    public void isValidDate_nullDate_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Date.isValidDate(null));
    }

    @Test
    public void isValidDate_invalidDate_returnsFalse() {
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only
        assertFalse(Date.isValidDate("^")); // only non-alphanumeric characters
        assertFalse(Date.isValidDate("11-11")); // invalid date
        assertFalse(Date.isValidDate("12-13-18")); // invalid Date
    }

    @Test
    public void isValidDate_validDate_returnsTrue() {
        assertTrue(Date.isValidDate("12-12-18"));
        assertTrue(Date.isValidDate("01-01-19"));
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Date date = new Date("10-10-18");
        assertEquals(date.toString(), "10-10-18");
    }

    @Test
    public void equals_null_returnsFalse() {
        Date test = new Date("10-10-18");
        assertFalse(test.equals(null));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Date test = new Date("10-10-18");
        Date other = new Date("10-10-18");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Date test = new Date("12-10-10");
        Date other = new Date("11-01-11");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameDate_sameHashCode() {
        Date test = new Date("11-11-18");
        Date test1 = new Date("11-11-18");
        assertEquals(test.hashCode(), test1.hashCode());
    }

    @Test
    public void hashCode_differentDate_differentHashCode() {
        Date test = new Date("11-11-18");
        Date test1 = new Date("11-12-18");
        assertNotEquals(test.hashCode(), test1.hashCode());
    }
}
```
###### \java\seedu\ptman\model\shift\ShiftTest.java
``` java
public class ShiftTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(null, null, null, null)
        );
        Assert.assertThrows(NullPointerException.class, () ->
                new Shift(new Date("19-03-18"), new Time("1000"), null, null)
        );
    }

    @Test
    public void setEmployees() throws DuplicateEmployeeException, ShiftFullException {
        Shift shift = new ShiftBuilder().build();
        shift.addEmployee(ALICE);
        shift.addEmployee(BOB);
        Shift other = new ShiftBuilder().build();
        other.setEmployees(shift);
        assertTrue(other.contains(ALICE));
        assertTrue(other.contains(BOB));
    }

    @Test
    public void isFull_shiftFull_returnsTrue() throws ShiftFullException, DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().withCapacity("1").build();
        shift.addEmployee(ALICE);
        assertTrue(shift.isFull());
    }

    @Test
    public void isFull_shiftNotFull_returnsFalse() throws ShiftFullException, DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().withCapacity("2").build();
        shift.addEmployee(ALICE);
        assertFalse(shift.isFull());
    }

    @Test
    public void addEmployee_shiftFull_throwsShiftFullException() throws ShiftFullException, DuplicateEmployeeException {
        Shift shift = new ShiftBuilder().withCapacity("1").build();
        shift.addEmployee(ALICE);
        thrown.expect(ShiftFullException.class);
        shift.addEmployee(BOB);
    }

    @Test
    public void equals_sameShift_returnsTrue() throws DuplicateEmployeeException, ShiftFullException {
        Shift shift1 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        shift1.addEmployee(ALICE);
        shift2.addEmployee(ALICE);
        assertEquals(shift1, shift2);
    }

    @Test
    public void hashCode_sameShift_sameHashCode() {
        Shift shift1 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        assertEquals(shift1.hashCode(), shift2.hashCode());
    }

    @Test
    public void hashCode_differentShift_differentHashCode() {
        Shift shift1 = new ShiftBuilder().withDate("12-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        Shift shift2 = new ShiftBuilder().withDate("19-03-18")
                .withCapacity("4")
                .withStartTime("1200")
                .withEndTime("1600").build();
        assertNotEquals(shift1.hashCode(), shift2.hashCode());
    }

}
```
###### \java\seedu\ptman\model\shift\TimeTest.java
``` java
public class TimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Time(null));
    }

    @Test
    public void constructor_invalidTime_throwsIllegalArgumentException() {
        String invalidTime = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Time(invalidTime));
    }

    @Test
    public void isValidTime_nullTime_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Time.isValidTime(null));
    }

    @Test
    public void isValidTime_invalidTime_returnsFalse() {
        assertFalse(Time.isValidTime("")); // empty string
        assertFalse(Time.isValidTime(" ")); // spaces only
        assertFalse(Time.isValidTime("^")); // only non-alphanumeric characters
        assertFalse(Time.isValidTime("11:11")); // contains non-alphanumeric characters
        assertFalse(Time.isValidTime("2500")); // contains invalid time
    }

    @Test
    public void isValidTime_validTime_returnsTrue() {
        assertTrue(Time.isValidTime("0000"));
        assertTrue(Time.isValidTime("2359"));
    }

    @Test
    public void toString_sameValue_returnsTrue() {
        Time day = new Time("1000");
        assertEquals(day.toString(), "1000");
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Time test = new Time("1000");
        Time other = new Time("1000");
        assertTrue(test.equals(other));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Time test = new Time("1200");
        Time other = new Time("2300");
        assertFalse(test.equals(other));
    }

    @Test
    public void hashCode_sameTime_returnsTrue() {
        Time test = new Time("1100");
        assertEquals(test.hashCode(), LocalTime.of(11, 0).hashCode());
    }
}
```
###### \java\seedu\ptman\model\shift\UniqueShiftListTest.java
``` java
public class UniqueShiftListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void setShift_shiftDoesNotExist_throwsShiftNotFoundException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        assertThrows(ShiftNotFoundException.class, () -> {
            uniqueShiftList.setShift(SHIFT_MONDAY_AM, SHIFT_MONDAY_PM);
        });
    }

    @Test
    public void setShift_editedShiftExists_throwsDuplicateShiftException()
            throws DuplicateShiftException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        uniqueShiftList.add(SHIFT_MONDAY_AM);
        uniqueShiftList.add(SHIFT_MONDAY_PM);
        assertThrows(DuplicateShiftException.class, () -> {
            uniqueShiftList.setShift(SHIFT_MONDAY_AM, SHIFT_MONDAY_PM);
        });
    }

    @Test
    public void setShift_validShifts_shiftReplaced()
            throws DuplicateShiftException, ShiftNotFoundException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        uniqueShiftList.add(SHIFT_MONDAY_AM);
        uniqueShiftList.add(SHIFT_MONDAY_PM);
        uniqueShiftList.setShift(SHIFT_MONDAY_AM, SHIFT_TUESDAY_AM);
        assertFalse(uniqueShiftList.contains(SHIFT_MONDAY_AM));
        assertTrue(uniqueShiftList.contains(SHIFT_TUESDAY_AM));
    }

    @Test
    public void setShifts_validShifts_shiftsReplaced() throws DuplicateShiftException {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        List<Shift> shiftList = new ArrayList<>();
        shiftList.add(SHIFT_MONDAY_AM);
        shiftList.add(SHIFT_MONDAY_PM);
        shiftList.add(SHIFT_TUESDAY_AM);
        uniqueShiftList.setShifts(shiftList);
        assertTrue(uniqueShiftList.contains(SHIFT_MONDAY_AM));
        assertTrue(uniqueShiftList.contains(SHIFT_MONDAY_PM));
        assertTrue(uniqueShiftList.contains(SHIFT_TUESDAY_AM));
    }

    @Test
    public void equals_sameUniqueShiftLists_returnsTrue() throws DuplicateShiftException {
        UniqueShiftList uniqueShiftList1 = new UniqueShiftList();
        UniqueShiftList uniqueShiftList2 = new UniqueShiftList();
        uniqueShiftList1.add(SHIFT_MONDAY_AM);
        uniqueShiftList2.add(SHIFT_MONDAY_AM);
        assertTrue(uniqueShiftList1.equals(uniqueShiftList2));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueShiftList.asObservableList().remove(0);
    }

    @Test
    public void remove_shiftDoesNotExist_throwsShiftNotFoundException() {
        UniqueShiftList uniqueShiftList = new UniqueShiftList();
        assertThrows(ShiftNotFoundException.class, () -> uniqueShiftList.remove(SHIFT_MONDAY_AM));
    }
}
```
###### \java\seedu\ptman\storage\XmlAdaptedShiftTest.java
``` java
public class XmlAdaptedShiftTest {
    private static final String INVALID_DATE = "1-1-18";
    private static final String INVALID_START_TIME = "asd";
    private static final String INVALID_END_TIME = "2500";
    private static final String INVALID_CAPACITY = "two";

    private static final String VALID_DATE = "01-01-18";
    private static final String VALID_CAPACITY = SHIFT_THURSDAY_AM.getCapacity().toString();
    private static final String VALID_START_TIME = SHIFT_THURSDAY_AM.getStartTime().toString();
    private static final String VALID_END_TIME = SHIFT_THURSDAY_AM.getEndTime().toString();

    private static final List<XmlAdaptedEmployee> VALID_EMPLOYEES =
            SHIFT_THURSDAY_AM.getEmployeeList().stream()
            .map(XmlAdaptedEmployee::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validShiftDetails_returnsShift() throws Exception {
        XmlAdaptedShift shift = new XmlAdaptedShift(SHIFT_THURSDAY_AM);
        assertEquals(SHIFT_THURSDAY_AM, shift.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(INVALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(null, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidStartTime_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, INVALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Time.MESSAGE_TIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, null, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Time.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidEndTime_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, INVALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Time.MESSAGE_TIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, null, VALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Time.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidCapacity_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, INVALID_CAPACITY,
                        VALID_EMPLOYEES);

        String expectedMessage = Capacity.MESSAGE_CAPACITY_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, null,
                        VALID_EMPLOYEES);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT_SHIFT, Capacity.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, shift::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedEmployee> invalidEmployees = new ArrayList<>();
        invalidEmployees.add(new XmlAdaptedEmployee(null, null, null, null, null, null, null));
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, null,
                        invalidEmployees);

        Assert.assertThrows(IllegalValueException.class, shift::toModelType);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertTrue(shift.equals(shift));
    }

    @Test
    public void equals_sameShift_returnsTrue() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        XmlAdaptedShift shift1 =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertTrue(shift.equals(shift1));
    }

    @Test
    public void equals_differentShift_returnsTrue() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        XmlAdaptedShift shift1 =
                new XmlAdaptedShift("02-14-18", VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertFalse(shift.equals(shift1));
    }

    @Test
    public void equals_null_returnsFalse() {
        XmlAdaptedShift shift =
                new XmlAdaptedShift(VALID_DATE, VALID_START_TIME, VALID_END_TIME, VALID_CAPACITY,
                        VALID_EMPLOYEES);
        assertFalse(shift.equals(null));
    }
}
```
###### \java\seedu\ptman\testutil\ShiftBuilder.java
``` java
/**
 * A utility class to help with building Shift objects.
 */
public class ShiftBuilder {

    public static final LocalDate DEFAULT_DATE = LocalDate.now().plusDays(1);
    public static final String DEFAULT_TIME_START = "0900";
    public static final String DEFAULT_TIME_END = "1600";
    public static final String DEFAULT_CAPACITY = "5";

    private Date date;
    private Time startTime;
    private Time endTime;
    private Capacity capacity;
    private Set<Employee> employees;

    public ShiftBuilder() {
        date = new Date(DEFAULT_DATE);
        startTime = new Time(DEFAULT_TIME_START);
        endTime = new Time(DEFAULT_TIME_END);
        capacity = new Capacity(DEFAULT_CAPACITY);
        employees = new HashSet<>();
    }

    /**
     * Sets the {@code Date} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withDate(LocalDate date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the start {@code Time} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withStartTime(String startTime) {
        this.startTime = new Time(startTime);
        return this;
    }

    /**
     * Sets the end {@code Time} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withEndTime(String endTime) {
        this.endTime = new Time(endTime);
        return this;
    }

    /**
     * Sets the {@code Capacity} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withCapacity(String capacity) {
        this.capacity = new Capacity(capacity);
        return this;
    }

    /**
     * Sets the {@code Employee} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withEmployees(Employee... employees) {
        this.employees.addAll(Arrays.asList(employees));
        return this;
    }

    /**
     * Returns the {@code Shift}
     */
    public Shift build() {
        return new Shift(date, startTime, endTime, capacity, employees);
    }

}
```
###### \java\seedu\ptman\testutil\ShiftUtil.java
``` java
/**
 * A utility class for Shift.
 */
public class ShiftUtil {
    /**
     * Returns an addshift command string for adding the {@code shift}.
     */
    public static String getAddShiftCommand(Shift shift) {
        return AddShiftCommand.COMMAND_WORD + " " + getShiftDetails(shift);
    }

    /**
     * Returns an aliased addshift command string for adding the {@code shift}.
     */
    public static String getAliasedAddShiftCommand(Shift shift) {
        return AddShiftCommand.COMMAND_ALIAS + " " + getShiftDetails(shift);
    }

    /**
     * Returns the part of command string for the given {@code shift}'s details.
     */
    public static String getShiftDetails(Shift shift) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DATE + shift.getDate().toString() + " ");
        sb.append(PREFIX_TIME_START + shift.getStartTime().toString() + " ");
        sb.append(PREFIX_TIME_END + shift.getEndTime().toString() + " ");
        sb.append(PREFIX_CAPACITY + shift.getCapacity().toString() + " ");
        return sb.toString();
    }
}
```
###### \java\seedu\ptman\testutil\TypicalShifts.java
``` java
/**
 * A utility class containing a list of {@code Shift} objects to be used in tests.
 */
public class TypicalShifts {

    public static final Shift SHIFT_MONDAY_AM = new ShiftBuilder().withDate("19-03-18")
            .withStartTime("0800")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift SHIFT_MONDAY_PM = new ShiftBuilder().withDate("19-03-18")
            .withStartTime("1300")
            .withEndTime("2200")
            .withCapacity("4").build();
    public static final Shift SHIFT_TUESDAY_AM = new ShiftBuilder().withDate("20-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift SHIFT_TUESDAY_PM = new ShiftBuilder().withDate("20-03-18")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift SHIFT_SUNDAY_AM = new ShiftBuilder().withDate("25-03-18")
            .withStartTime("1000")
            .withEndTime("1300")
            .withCapacity("4").build();
    public static final Shift SHIFT_SUNDAY_PM = new ShiftBuilder().withDate("25-03-18")
            .withStartTime("1300")
            .withEndTime("1700")
            .withCapacity("4").build();
    public static final Shift SHIFT_WEDNESDAY_AM = new ShiftBuilder().withDate("21-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5").build();
    public static final Shift SHIFT_WEDNESDAY_PM = new ShiftBuilder().withDate("21-03-18")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3").build();
    public static final Shift SHIFT_THURSDAY_AM = new ShiftBuilder().withDate("22-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("5")
            .withEmployees(new EmployeeBuilder().build()).build();
    public static final Shift SHIFT_THURSDAY_PM = new ShiftBuilder().withDate("22-03-18")
            .withStartTime("1200")
            .withEndTime("2200")
            .withCapacity("3")
            .withEmployees(ALICE, BENSON, CARL).build();

    public static final Shift SHIFT_RUNNING_OUT = new ShiftBuilder().withDate("22-03-18")
            .withStartTime("0900")
            .withEndTime("1200")
            .withCapacity("1").build();

    private TypicalShifts() {} // prevents instantiation

    public static PartTimeManager getTypicalPartTimeManagerWithShifts() {
        PartTimeManager ptman = new PartTimeManager();
        for (Employee employee : getTypicalEmployees()) {
            try {
                ptman.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Shift shift : getTypicalShifts()) {
            try {
                ptman.addShift(shift);
            } catch (DuplicateShiftException e) {
                throw new AssertionError("not possible");
            }
        }
        return ptman;
    }

    public static List<Shift> getTypicalShifts() {
        return new ArrayList<>(Arrays.asList(SHIFT_MONDAY_AM, SHIFT_MONDAY_PM, SHIFT_TUESDAY_AM, SHIFT_TUESDAY_PM,
                SHIFT_WEDNESDAY_AM, SHIFT_WEDNESDAY_PM, SHIFT_THURSDAY_PM, SHIFT_SUNDAY_AM, SHIFT_SUNDAY_PM));
    }
}
```
