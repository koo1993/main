package seedu.ptman.logic.commands;

import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalShifts.getTypicalPartTimeManagerWithShifts;

import org.junit.Before;
import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.testutil.ShiftBuilder;

//@@author shanwpf
/**
 * Contains integration tests (interaction with the Model) for {@code AddShiftCommand}.
 */
public class AddShiftCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPartTimeManagerWithShifts(), new UserPrefs(), new OutletInformation());
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_newShift_success() throws Exception {
        Shift validShift = new ShiftBuilder().build();

        Model expectedModel = new ModelManager(model.getPartTimeManager(), new UserPrefs(), new OutletInformation());
        expectedModel.addShift(validShift);

        assertCommandSuccess(prepareCommand(validShift, model), model,
                String.format(AddShiftCommand.MESSAGE_SUCCESS, validShift), expectedModel);
    }

    @Test
    public void execute_duplicateShift_throwsCommandException() {
        Shift shiftInList = model.getPartTimeManager().getShiftList().get(0);
        assertCommandFailure(prepareCommand(shiftInList, model), model, AddShiftCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code employee} into the {@code model}.
     */
    private AddShiftCommand prepareCommand(Shift shift, Model model) {
        AddShiftCommand command = new AddShiftCommand(shift);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
