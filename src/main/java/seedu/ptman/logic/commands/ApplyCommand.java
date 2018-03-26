package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.List;
import java.util.Objects;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.Model;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;
import seedu.ptman.model.outlet.exceptions.ShiftNotFoundException;

/**
 * Registers an employee to a shift identified using their last displayed index from PTMan.
 */
public class ApplyCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "apply";
    public static final String COMMAND_ALIAS = "ap";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Applies an employee for the shift identified by the index number.\n"
            + "Parameters: EMPLOYEE_INDEX (must be a positive integer) "
            + "SHIFT_INDEX "
            + PREFIX_PASSWORD + "PASSWORD\n"
            + "Example: " + COMMAND_WORD + " 1 1 " + PREFIX_PASSWORD + "hunter2";

    public static final String MESSAGE_APPLY_SHIFT_SUCCESS = "Employee %1$s applied for shift %2$s";
    public static final String MESSAGE_DUPLICATE_EMPLOYEE = "Employee is already in the shift";

    private final Index employeeIndex;
    private final Index shiftIndex;
    private final Password password;

    private Employee applicant;
    private Shift shiftToApply;
    private Shift editedShift;

    public ApplyCommand(Index employeeIndex, Index shiftIndex, Password password) {
        this.password = password;
        this.employeeIndex = employeeIndex;
        this.shiftIndex = shiftIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(applicant);

        if (!applicant.isCorrectPassword(password)) {
            throw new InvalidPasswordException();
        }

        try {
            model.updateShift(shiftToApply, editedShift);
        } catch (ShiftNotFoundException e) {
            throw new AssertionError("Shift not found");
        } catch (DuplicateShiftException e) {
            throw new AssertionError("Duplicate shift");
        }

        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
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
