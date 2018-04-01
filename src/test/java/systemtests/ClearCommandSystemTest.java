package systemtests;

import static seedu.ptman.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.ptman.logic.commands.CommandTestUtil.DEFAULT_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.ClearCommand;
import seedu.ptman.logic.commands.LogInAdminCommand;
import seedu.ptman.logic.commands.RedoCommand;
import seedu.ptman.logic.commands.UndoCommand;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;

public class ClearCommandSystemTest extends PartTimeManagerSystemTest {

    @Test
    public void clear() {
        final Model defaultModel = getModel();
        executeDefaultAdminLogin();
        /* Case: clear non-empty ptman book, command with leading spaces and trailing alphanumeric characters and
         * spaces -> cleared
         */
        assertCommandSuccess("   " + ClearCommand.COMMAND_WORD + " ab12   ");
        assertSelectedCardUnchanged();

        /* Case: undo clearing ptman book -> original ptman book restored */
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command,  expectedResultMessage, defaultModel);
        assertSelectedCardUnchanged();

        /* Case: redo clearing ptman book -> cleared */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, new ModelManager());
        assertSelectedCardUnchanged();

        /* Case: selects first card in employee list and clears ptman book -> cleared and no card selected */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original ptman book
        selectEmployee(Index.fromOneBased(1));
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardDeselected();

        /* Case: filters the employee list before clearing -> entire ptman book cleared */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original ptman book
        showEmployeesWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: clear empty ptman book -> cleared */
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearCommand#MESSAGE_SUCCESS} and the model related components equal to an empty model.
     * These verifications are done by
     * {@code PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     * @see PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command) {
        assertCommandSuccess(command, ClearCommand.MESSAGE_SUCCESS, new ModelManager());
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     * @see ClearCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertAdminModeDisplayShowsLoginStyle();
        assertStatusBarChangedExceptSaveLocation();
    }

    /**
     * Perform to transform PTMan to admin mode.
     */
    private void executeDefaultAdminLogin() {
        executeCommand(LogInAdminCommand.COMMAND_WORD + " " + PREFIX_PASSWORD + DEFAULT_PASSWORD);
    }


    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see PartTimeManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxAndResultDisplayShowsErrorStyle();
        assertStatusBarUnchanged();
        assertOutletDetailsPanelUnchanged();
    }
}
