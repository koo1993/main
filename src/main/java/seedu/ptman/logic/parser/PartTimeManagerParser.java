package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.ptman.logic.commands.AddCommand;
import seedu.ptman.logic.commands.AddShiftCommand;
import seedu.ptman.logic.commands.AnnouncementCommand;
import seedu.ptman.logic.commands.ChangeMasterPasswordCommand;
import seedu.ptman.logic.commands.ChangePasswordCommand;
import seedu.ptman.logic.commands.ClearCommand;
import seedu.ptman.logic.commands.Command;
import seedu.ptman.logic.commands.DeleteCommand;
import seedu.ptman.logic.commands.DeleteShiftCommand;
import seedu.ptman.logic.commands.EditCommand;
import seedu.ptman.logic.commands.EditOutletCommand;
import seedu.ptman.logic.commands.ExitCommand;
import seedu.ptman.logic.commands.FindCommand;
import seedu.ptman.logic.commands.HelpCommand;
import seedu.ptman.logic.commands.HistoryCommand;
import seedu.ptman.logic.commands.ListCommand;
import seedu.ptman.logic.commands.LogInAdminCommand;
import seedu.ptman.logic.commands.LogOutAdminCommand;
import seedu.ptman.logic.commands.RedoCommand;
import seedu.ptman.logic.commands.SelectCommand;
import seedu.ptman.logic.commands.UndoCommand;
import seedu.ptman.logic.commands.ViewOutletCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class PartTimeManagerParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case LogOutAdminCommand.COMMAND_WORD:
            return new LogOutAdminCommand();

        case LogInAdminCommand.COMMAND_WORD:
            return new LogInAdminCommandParser().parse(arguments);

        case ChangePasswordCommand.COMMAND_WORD:
        case ChangePasswordCommand.COMMAND_ALIAS:
            return new ChangePasswordCommandParser().parse(arguments);

        case ChangeMasterPasswordCommand.COMMAND_WORD:
        case ChangeMasterPasswordCommand.COMMAND_ALIAS:
            return new ChangeMasterPasswordCommandParser().parse(arguments);

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case AddShiftCommand.COMMAND_WORD:
        case AddShiftCommand.COMMAND_ALIAS:
            return new AddShiftCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            return new DeleteCommandParser().parse(arguments);

        case DeleteShiftCommand.COMMAND_WORD:
        case DeleteShiftCommand.COMMAND_ALIAS:
            return new DeleteShiftCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        case EditOutletCommand.COMMAND_WORD:
        case EditOutletCommand.COMMAND_ALIAS:
            return new EditOutletCommandParser().parse(arguments);

        case ViewOutletCommand.COMMAND_WORD:
        case ViewOutletCommand.COMMAND_ALIAS:
            return new ViewOutletCommand();

        case AnnouncementCommand.COMMAND_WORD:
        case AnnouncementCommand.COMMAND_ALIAS:
            return new AnnouncementCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Extract out password when given a commandText
     * @param commandText
     * @return password in String
     */
    public String parseCommandForPassword(String commandText) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(commandText, PREFIX_NAME, PREFIX_ADDRESS,
                PREFIX_PHONE, PREFIX_SALARY, PREFIX_EMAIL, PREFIX_PASSWORD, PREFIX_TAG);
        Optional<String> passwordOptional = argMultimap.getValue(PREFIX_PASSWORD);

        if (!passwordOptional.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandText));
        }
        return argMultimap.getValue(PREFIX_PASSWORD).get();
    }
}
