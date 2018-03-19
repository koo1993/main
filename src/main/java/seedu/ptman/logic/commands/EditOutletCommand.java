package seedu.ptman.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_MASTER_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OPERATING_HOURS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_NAME;

import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletName;

/**
 * Edits the details of outlet in the ptman.
 */
public class EditOutletCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editoutlet";
    public static final String COMMAND_ALIAS = "eo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the outlet "
            + "verified by the master password. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "[" + PREFIX_MASTER_PASSWORD + "MASSTERPASSWORD] "
            + "[" + PREFIX_OUTLET_NAME + "OUTLETNAME] "
            + "[" + PREFIX_OPERATING_HOURS + "OPERATINGHOURS] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MASTER_PASSWORD + "DEFAULT1 "
            + PREFIX_OUTLET_NAME + "AwesomeOutlet "
            + PREFIX_OPERATING_HOURS + "09:00-22:00";

    public static final String MESSAGE_EDIT_OUTLET_SUCCESS = "Outlet Information Edited.";

    private final Password masterPassword;
    private final OutletName name;
    private final OperatingHours operatingHours;

    /**
     * Constructor of EditOutletCommand
     */
    public EditOutletCommand(Password masterPassword, OutletName name, OperatingHours operatingHours) {
        requireNonNull(masterPassword);
        this.masterPassword = masterPassword;
        this.name = name;
        this.operatingHours = operatingHours;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        model.updateOutlet(name, operatingHours);
        return new CommandResult(String.format(MESSAGE_EDIT_OUTLET_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditOutletCommand)) {
            return false;
        }

        // state check
        EditOutletCommand e = (EditOutletCommand) other;
        return masterPassword.equals(e.masterPassword)
                && name.equals(e.name)
                && operatingHours.equals(e.operatingHours);
    }
}
