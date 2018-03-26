package seedu.ptman.logic.commands;

import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OPERATING_HOURS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_CONTACT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_NAME;

import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.events.ui.OutletInformationChangedEvent;
import seedu.ptman.commons.events.ui.OutletNameChangedEvent;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletEmail;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.OutletName;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;

/**
 * Edits the details of outlet in the ptman.
 */
public class EditOutletCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editoutlet";
    public static final String COMMAND_ALIAS = "eo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the outlet in admin "
            + "mode. Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "[" + PREFIX_OUTLET_NAME + "OUTLETNAME] "
            + "[" + PREFIX_OPERATING_HOURS + "OPERATINGHOURS] "
            + "[" + PREFIX_OUTLET_CONTACT + "CONTACT] "
            + "[" + PREFIX_OUTLET_EMAIL + "EMAIL] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OUTLET_NAME + "AwesomeOutlet "
            + PREFIX_OPERATING_HOURS + "09:00-22:00 "
            + PREFIX_OUTLET_CONTACT + "91234567 "
            + PREFIX_OUTLET_EMAIL + "AwesomeOutlet@gmail.com";

    public static final String MESSAGE_EDIT_OUTLET_SUCCESS = "Outlet Information Edited.";
    public static final String MESSAGE_EDIT_OUTLET_FAILURE = "At least one field must be specified.\n"
            + MESSAGE_USAGE;

    private final OutletName name;
    private final OperatingHours operatingHours;
    private final OutletContact outletContact;
    private final OutletEmail outletEmail;

    /**
     * Constructor of EditOutletCommand
     */
    public EditOutletCommand(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                             OutletEmail outletEmail) {
        this.name = name;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        try {
            OutletInformation editedOutlet = new OutletInformation(model.getOutletInformation());
            editedOutlet.setOutletInformation(name, operatingHours, outletContact, outletEmail);
            model.updateOutlet(editedOutlet);
            EventsCenter.getInstance().post(new OutletInformationChangedEvent(
                    editedOutlet.getOperatingHours().toString(),
                    editedOutlet.getOutletContact().toString(),
                    editedOutlet.getOutletEmail().toString()));
            EventsCenter.getInstance().post(new OutletNameChangedEvent(editedOutlet.getName().toString()));
        } catch (NoOutletInformationFieldChangeException e) {
            throw new CommandException(MESSAGE_EDIT_OUTLET_FAILURE);
        }
        return new CommandResult(MESSAGE_EDIT_OUTLET_SUCCESS);
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
        return outletContact.equals(e.outletContact)
                && name.equals(e.name)
                && operatingHours.equals(e.operatingHours)
                && outletEmail.equals(e.outletEmail);
    }
}
