package seedu.ptman.logic.commands;

import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;

import seedu.ptman.logic.commands.exceptions.CommandException;

//@@author SunBangjie
/**
 * Decrypts local storage files.
 */
public class DecryptDataCommand extends Command {

    public static final String COMMAND_WORD = "decrypt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Decrypts local storage files.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DECRYPT_SUCCESS = "Local files successfully decrypted.";
    public static final String MESSAGE_DECRYPT_FAILURE = "Local files have already been decrypted.";

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        if (!model.getOutletInformation().getEncryptionMode()) {
            throw new CommandException(MESSAGE_DECRYPT_FAILURE);
        }
        model.decryptLocalStorage();
        return new CommandResult(MESSAGE_DECRYPT_SUCCESS);
    }
}
