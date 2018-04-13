package seedu.ptman.logic.commands;

import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;

//@@author koo1993
/**
 * Contains integration tests (interaction with the Model) for {@code LogOutAdminCommand}.
 */
public class LogOutAdminCommandTest {
    private final Password defaultPassword = new Password();

    @Test
    public void execute_notAdminModePartTimeManager_alreadyLoggedOut() {
        Model model = new ModelManager();
        assertCommandSuccess(prepareCommand(model), model,  LogOutAdminCommand.MESSAGE_LOGGEDOUT, model);
    }

    @Test
    public void execute_adminModePartTimeManager_success() {
        Model model = new ModelManager();
        model.setTrueAdminMode(defaultPassword);
        assertCommandSuccess(prepareCommand(model), model,  LogOutAdminCommand.MESSAGE_SUCCESS, model);
    }


    /**
     * Generates a new {@code LogOutAdminCommand} which upon execution, log out from AdminMode.
     */
    private LogOutAdminCommand prepareCommand(Model model) {
        LogOutAdminCommand command = new LogOutAdminCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
