package seedu.ptman.logic.commands;

import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;

import seedu.ptman.logic.commands.exceptions.CommandException;

//@@author SunBangjie
/**
 * Encrypts local storage files.
 */
public class EncryptDataCommand extends Command {

    public static final String COMMAND_WORD = "encrypt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Encrypts local storage files.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_ENCRYPT_SUCCESS = "Local files successfully encrypted.";
    public static final String MESSAGE_ENCRYPT_FAILURE = "Local files have already been encrypted.";

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        if (model.getOutletInformation().getEncryptionMode()) {
            throw new CommandException(MESSAGE_ENCRYPT_FAILURE);
        }
        model.encryptLocalStorage();
        return new CommandResult(MESSAGE_ENCRYPT_SUCCESS);
    }
}
