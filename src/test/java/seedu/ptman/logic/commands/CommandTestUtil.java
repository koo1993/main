package seedu.ptman.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_CAPACITY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OPERATING_HOURS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_CONTACT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_END;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_START;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Model;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.NameContainsKeywordsPredicate;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.testutil.EditEmployeeDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_SALARY_AMY = "0";
    public static final String VALID_SALARY_BOB = "10";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_OUTLET_NAME = "ValidOutlet";
    public static final String VALID_OPERATING_HOURS = "0900-2200";
    public static final String VALID_OUTLET_CONTACT = "912345678";
    public static final String VALID_OUTLET_EMAIL = "ValidOutlet@gmail.com";
    public static final String DEFAULT1_HASH = "wkqTFuX6NX3hucWqn2ZxB24cRo73LssRq7IDOk6Zx00=";
    public static final String DEFAULT_PASSWORD = "DEFAULT1";

    public static final String VALID_DATE_12MAR = "12-03-18";
    public static final String VALID_DAY_13MAR = "13-03-18";
    public static final String VALID_TIME_START_10AM = "1000";
    public static final String VALID_TIME_START_12PM = "1200";
    public static final String VALID_TIME_END_8PM = "2000";
    public static final String VALID_TIME_END_10PM = "2200";
    public static final String VALID_CAPACITY_1 = "1";
    public static final String VALID_CAPACITY_2 = "2";

    public static final String DATE_DESC_12MAR = " " + PREFIX_DATE + VALID_DATE_12MAR;
    public static final String DATE_DESC_13MAR = " " + PREFIX_DATE + VALID_DAY_13MAR;
    public static final String TIME_START_DESC_10AM = " " + PREFIX_TIME_START + VALID_TIME_START_10AM;
    public static final String TIME_START_DESC_12PM = " " + PREFIX_TIME_START + VALID_TIME_START_12PM;
    public static final String TIME_END_DESC_8PM = " " + PREFIX_TIME_END + VALID_TIME_END_8PM;
    public static final String TIME_END_DESC_10PM = " " + PREFIX_TIME_END + VALID_TIME_END_10PM;
    public static final String CAPACITY_DESC_1 = " " + PREFIX_CAPACITY + VALID_CAPACITY_1;
    public static final String CAPACITY_DESC_2 = " " + PREFIX_CAPACITY + VALID_CAPACITY_2;

    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "12-3-18"; // month not prefixed with '0'
    public static final String INVALID_TIME_START_DESC =
            " " + PREFIX_TIME_START + "9am"; // time should be in 24-hour format
    public static final String INVALID_TIME_END_DESC = " " + PREFIX_TIME_END + "23:00"; // time should not include ':'
    public static final String INVALID_CAPACITY_DESC = " " + PREFIX_CAPACITY + "3!"; // '!' not allowed in capacity

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String SALARY_DESC_AMY = " " + PREFIX_SALARY + VALID_SALARY_AMY;
    public static final String SALARY_DESC_BOB = " " + PREFIX_SALARY + VALID_SALARY_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String OUTLET_NAME_DESC = " " + PREFIX_OUTLET_NAME + VALID_OUTLET_NAME;
    public static final String OPERATING_HOURS_DESC = " " + PREFIX_OPERATING_HOURS + VALID_OPERATING_HOURS;
    public static final String OUTLET_CONTACT_DESC = " " + PREFIX_OUTLET_CONTACT + VALID_OUTLET_CONTACT;
    public static final String OUTLET_EMAIL_DESC = " " + PREFIX_OUTLET_EMAIL + VALID_OUTLET_EMAIL;
    public static final String DEFAULT_DESC_ADMINPASSWORD = " " + PREFIX_PASSWORD + DEFAULT_PASSWORD;

    public static final String ADMINPASSWORD_DESC_DEFAULT = " " + PREFIX_PASSWORD + DEFAULT_PASSWORD;
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_SALARY_DESC = " " + PREFIX_SALARY + "-1"; // negative not allowed for Salary
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_OUTLET_NAME_DESC = " " + PREFIX_OUTLET_NAME + "Invalid@Outlet";
    public static final String INVALID_OPERATING_HOURS_DESC = " " + PREFIX_OPERATING_HOURS + "09:00/22:00";
    public static final String INVALID_OUTLET_CONTACT_DESC = " " + PREFIX_OUTLET_CONTACT + "91234567@";
    public static final String INVALID_OUTLET_EMAIL_DESC = " " + PREFIX_OUTLET_EMAIL + "Invalid!gmail";
    public static final String INVALID_DESC_ADMINPASSWORD = " " + PREFIX_PASSWORD + "wrongpassword";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditEmployeeDescriptor DESC_AMY;
    public static final EditCommand.EditEmployeeDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withSalary(VALID_SALARY_AMY).withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditEmployeeDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withSalary(VALID_SALARY_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - PTMan and the filtered employee list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        PartTimeManager expectedPartTimeManager = new PartTimeManager(actualModel.getPartTimeManager());
        List<Employee> expectedFilteredList = new ArrayList<>(actualModel.getFilteredEmployeeList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedPartTimeManager, actualModel.getPartTimeManager());
            assertEquals(expectedFilteredList, actualModel.getFilteredEmployeeList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the employee at the given {@code targetIndex} in the
     * {@code model}'s ptman book.
     */
    public static void showEmployeeAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEmployeeList().size());

        Employee employee = model.getFilteredEmployeeList().get(targetIndex.getZeroBased());
        final String[] splitName = employee.getName().fullName.split("\\s+");
        model.updateFilteredEmployeeList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredEmployeeList().size());
    }

    /**
     * Deletes the first employee in {@code model}'s filtered list from {@code model}'s ptman book.
     */
    public static void deleteFirstEmployee(Model model) {
        Employee firstEmployee = model.getFilteredEmployeeList().get(0);
        try {
            model.deleteEmployee(firstEmployee);
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("Employee in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
