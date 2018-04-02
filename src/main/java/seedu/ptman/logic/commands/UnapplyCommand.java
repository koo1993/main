package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.commands.exceptions.InvalidPasswordException;
import seedu.ptman.logic.commands.exceptions.MissingPasswordException;
import seedu.ptman.model.Model;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.EmployeeNotFoundException;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
import seedu.ptman.model.shift.exceptions.ShiftNotFoundException;

//@@author shanwpf
/**
 * Registers an employee to a shift identified using their last displayed index from PTMan.
 */
public class UnapplyCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "unapply";
    public static final String COMMAND_ALIAS = "uap";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes an employee from the shift identified by the index number.\n"
            + "Parameters: EMPLOYEE_INDEX (must be a positive integer) "
            + "SHIFT_INDEX "
            + PREFIX_PASSWORD + "PASSWORD\n"
            + "Example: " + COMMAND_WORD + " 1 1 " + PREFIX_PASSWORD + "hunter2";

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

        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
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
