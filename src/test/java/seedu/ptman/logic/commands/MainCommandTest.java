package seedu.ptman.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.ptman.logic.commands.MainCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.ptman.commons.events.ui.EmployeePanelSelectionChangedEvent;
import seedu.ptman.ui.testutil.EventsCollectorRule;

public class MainCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_main_success() {
        CommandResult result = new MainCommand().execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof EmployeePanelSelectionChangedEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }
}
