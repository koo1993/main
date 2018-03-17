package seedu.ptman.logic.commands;

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
        assertCommandSuccess(prepareCommand(model, defaultPassword), model, ClearCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_nonEmptyPartTimeManager_success() {
        Model model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs());
        assertCommandSuccess(prepareCommand(model, defaultPassword), model, ClearCommand.MESSAGE_SUCCESS, model);
    }

    /**
     * Generates a new {@code ClearCommand} which upon execution, clears the contents in {@code model}.
     */
    private ClearCommand prepareCommand(Model model, Password password) {
        ClearCommand command = new ClearCommand(password);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
