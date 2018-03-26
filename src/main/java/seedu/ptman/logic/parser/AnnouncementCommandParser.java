package seedu.ptman.logic.parser;

import seedu.ptman.logic.commands.AnnouncementCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.outlet.Announcement;

/**
 * Parses input arguments and creates a new AnnouncementCommand object
 */
public class AnnouncementCommandParser implements Parser {

    /**
     * Constructs an AnnouncementCommand parser
     * @param args
     * @return
     * @throws ParseException
     */
    public AnnouncementCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(AnnouncementCommand.MESSAGE_EDIT_OUTLET_FAILURE);
        }

        return new AnnouncementCommand(new Announcement(trimmedArgs));
    }
}
