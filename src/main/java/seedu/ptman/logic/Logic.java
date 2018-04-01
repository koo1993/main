package seedu.ptman.logic;

import javafx.collections.ObservableList;
import seedu.ptman.logic.commands.CommandResult;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of employees */
    ObservableList<Employee> getFilteredEmployeeList();

    /** Returns an unmodifiable sorted view of the filtered shifts */
    ObservableList<Shift> getFilteredShiftList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();

    OutletInformation getOutletInformation();

    /** Returns if the user is currently in admin mode */
    boolean isAdminMode();
}
