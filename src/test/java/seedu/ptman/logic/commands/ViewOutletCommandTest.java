package seedu.ptman.logic.commands;

import static seedu.ptman.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;

import org.junit.Before;
import org.junit.Test;

import seedu.ptman.logic.CommandHistory;
import seedu.ptman.logic.UndoRedoStack;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OutletInformation;

/**
 * Contains integration tests (interaction with the Model) for {@code ViewOutletCommand}.
 */
public class ViewOutletCommandTest {
    private ViewOutletCommand command;
    private Model model;
    private Model expectedModel;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(),
                new OutletInformation());
        expectedModel = new ModelManager(getTypicalPartTimeManager(), new UserPrefs(),
                new OutletInformation());
        command = new ViewOutletCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_validOutletInformation_showsCorrectInformation() {
        String expectedMessage = "Outlet Name: " + model.getOutletInformation().getName() + " "
                + model.getOutletInformationMessage() + " Announcement: "
                + model.getOutletInformation().getAnnouncement();
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
