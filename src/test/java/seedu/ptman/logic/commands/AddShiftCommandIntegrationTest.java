package seedu.ptman.logic.commands;

import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalShifts.getTypicalPartTimeManagerWithShifts;

import java.time.LocalDate;

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
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
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
        model.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        model.setTrueAdminMode(new Password());
    }

    @Test
    public void execute_newShift_success() throws Exception {
        Shift validShift = new ShiftBuilder().withDate(LocalDate.now()).build();

        Model expectedModel =
                new ModelManager(getTypicalPartTimeManagerWithShifts(), new UserPrefs(), new OutletInformation());
        expectedModel.setTrueAdminMode(new Password());
        expectedModel.updateFilteredShiftList(Model.PREDICATE_SHOW_ALL_SHIFTS);
        expectedModel.addShift(validShift);

        assertCommandSuccess(prepareCommand(validShift, model), model,
                String.format(AddShiftCommand.MESSAGE_SUCCESS, validShift), expectedModel);
    }

    @Test
    public void execute_duplicateShift_throwsCommandException() throws DuplicateShiftException {
        Shift shift = new ShiftBuilder().withDate(LocalDate.now()).build();
        model.addShift(shift);
        assertCommandFailure(prepareCommand(shift, model), model, AddShiftCommand.MESSAGE_DUPLICATE_SHIFT);
    }

    @Test
    public void execute_invalidShiftDate_throwsCommandException() {
        Shift shift = new ShiftBuilder().withDate("01-01-10").build();
        assertCommandFailure(prepareCommand(shift, model), model, AddShiftCommand.MESSAGE_DATE_OVER);
    }

    /**
     * Generates a new {@code AddShiftCommand} which upon execution, adds {@code shift} into the {@code model}.
     */
    private AddShiftCommand prepareCommand(Shift shift, Model model) {
        AddShiftCommand command = new AddShiftCommand(shift);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
