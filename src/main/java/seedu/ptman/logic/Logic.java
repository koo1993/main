package seedu.ptman.logic;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import seedu.ptman.logic.commands.CommandResult;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.shift.Shift;

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

    /** Returns the current date used to update the filtered shift list */
    LocalDate getCurrentDisplayedDate();

    /** Sets filteredShiftList to display the next week */
    void setFilteredShiftListToNextWeek();

    /** Sets filteredShiftList to display the previous week */
    void setFilteredShiftListToPrevWeek();

    /** Sets filteredShiftList to display the current week */
    void setFilteredShiftListToCurrentWeek();

    /** Sets filteredShiftList to display the wee of the given date */
    void setFilteredShiftListToCustomWeek(LocalDate date);

    /** Returns if the user is currently in admin mode */
    boolean isAdminMode();
}
