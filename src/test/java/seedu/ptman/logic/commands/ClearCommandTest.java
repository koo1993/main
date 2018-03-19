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

public class ClearCommandTest {
    private final Password defaultPassword = new Password();

    @Test
    public void execute_emptyPartTimeManager_success() {
        Model model = new ModelManager();
        model.setTrueAdminMode(defaultPassword);
        assertCommandSuccess(prepareCommand(model), model, ClearCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_nonEmptyPartTimeManager_success() {
        Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());
        model.setTrueAdminMode(defaultPassword);
        assertCommandSuccess(prepareCommand(model), model, ClearCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_notInAdminMode_accessDenied() {
        Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());
        ClearCommand clearCommand = new  ClearCommand();
        clearCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(clearCommand, model, MESSAGE_ACCESS_DENIED);
    }

    /**
     * Generates a new {@code ClearCommand} which upon execution, clears the contents in {@code model}.
     */
    private ClearCommand prepareCommand(Model model) {
        ClearCommand command = new ClearCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
