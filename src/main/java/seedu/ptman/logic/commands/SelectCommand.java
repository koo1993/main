package seedu.ptman.logic.commands;

import java.util.List;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.core.Messages;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.events.ui.JumpToListRequestEvent;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.employee.Employee;

/**
 * Selects a employee identified using it's last displayed index from PTMan.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";
    public static final String COMMAND_ALIAS = "sel";

    public static final String COMMAND_FORMAT = "EMPLOYEE_INDEX";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the employee identified by the index number used in the last employee listing and highlights"
            + "his/her shifts in the timetable.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + " (must be a positive integer)"
            + "\nExample: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_EMPLOYEE_SUCCESS = "Selected Employee: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Employee> lastShownList = model.getFilteredEmployeeList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex, true));
        return new CommandResult(String.format(MESSAGE_SELECT_EMPLOYEE_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
