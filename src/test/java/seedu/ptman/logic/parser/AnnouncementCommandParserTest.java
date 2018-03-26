package seedu.ptman.logic.parser;

import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.ptman.logic.commands.AnnouncementCommand;
import seedu.ptman.model.outlet.Announcement;

public class AnnouncementCommandParserTest {

    private AnnouncementCommandParser parser = new AnnouncementCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", AnnouncementCommand.MESSAGE_EDIT_OUTLET_FAILURE);
    }

    @Test
    public void parse_validArgs_returnsAnnouncementCommand() {
        AnnouncementCommand expectedCommand = new AnnouncementCommand(new Announcement("new announcement"));
        assertParseSuccess(parser, "new announcement", expectedCommand);
    }
}
