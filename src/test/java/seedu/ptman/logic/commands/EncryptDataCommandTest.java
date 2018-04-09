package seedu.ptman.logic.commands;

import static seedu.ptman.commons.core.Messages.MESSAGE_ACCESS_DENIED;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;

import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OutletInformation;

//@@author SunBangjie
/**
 * Contains integration tests (interaction with the Model) for {@code EncryptDataCommand}.
 */
public class EncryptDataCommandTest {

    private Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());

    @Test
    public void execute_nonAdminMode_failure() {
        EncryptDataCommand command = prepareCommand();
        assertCommandFailure(command, model, MESSAGE_ACCESS_DENIED);
    }

    @Test
    public void execute_adminModeDataNotEncrypted_success() {
        model.setTrueAdminMode(new Password());
        EncryptDataCommand command = prepareCommand();
        Model expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.encryptLocalStorage();
        String expectedMessage = EncryptDataCommand.MESSAGE_ENCRYPT_SUCCESS;
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_adminModeDataAlreadyEncrypted_failure() {
        model.setTrueAdminMode(new Password());
        model.encryptLocalStorage();
        EncryptDataCommand command = prepareCommand();
        String expectedMessage = EncryptDataCommand.MESSAGE_ENCRYPT_FAILURE;
        assertCommandFailure(command, model, expectedMessage);
    }

    /**
     * Returns an {@code EncryptDataCommand}
     */
    private EncryptDataCommand prepareCommand() {
        EncryptDataCommand command = new EncryptDataCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
