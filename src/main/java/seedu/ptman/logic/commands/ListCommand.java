package seedu.ptman.logic.commands;

import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

/**
 * Lists all employees in PTMan to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";

    public static final String MESSAGE_SUCCESS = "Listed all employees";


    @Override
    public CommandResult execute() {
        model.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
