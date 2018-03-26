package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import seedu.ptman.testutil.TypicalEmployees;

//@@author hzxcaryn
public class TimetablePanelTest extends GuiUnitTest {

    private TimetablePanel timetablePanel;

    @Before
    public void setUp() {
        timetablePanel = new TimetablePanel(TypicalEmployees.getTypicalPartTimeManager().getShiftList(),
                TypicalEmployees.getTypicalPartTimeManager().getOutletInformation());
        uiPartRule.setUiPart(timetablePanel);
    }

    @Test
    public void display() {
        assertNotNull(timetablePanel.getRoot());
        // Displays week view
        assertEquals(timetablePanel.getRoot().getSelectedPage(), timetablePanel.getRoot().getWeekPage());
    }

}
