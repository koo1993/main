package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;

import org.junit.Test;
import seedu.ptman.logic.commands.DeleteShiftCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class ChangeEmployeePasswordCommandParserTest {

    private ChangeEmployeePasswordCommandParser parser = new ChangeEmployeePasswordCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteShiftCommand() {
        try {
            parser.parse("1" + " pw/  test1    " + "pw/   testfdsa2 " + "pw/   testfdsa2 ");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
