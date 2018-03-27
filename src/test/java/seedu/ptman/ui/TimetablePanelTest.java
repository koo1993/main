package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.testutil.EventsUtil.postNow;
import static seedu.ptman.ui.TimetablePanel.TIMETABLE_IMAGE_FILE_FORMAT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.ptman.commons.events.ui.ExportTimetableAsImageAndEmailRequestEvent;
import seedu.ptman.commons.events.ui.ExportTimetableAsImageRequestEvent;
import seedu.ptman.model.employee.Email;
import seedu.ptman.testutil.TypicalEmployees;

//@@author hzxcaryn
public class TimetablePanelTest extends GuiUnitTest {

    private static final String TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST = "Testing1";
    private static final String TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST = "Testing2";
    private static final Email TIMETABLE_IMAGE_EMAIL_TEST = new Email("example@gmail.com");

    private ExportTimetableAsImageRequestEvent exportTimetableAsImageRequestEventStub;
    private ExportTimetableAsImageAndEmailRequestEvent exportTimetableAsImageAndEmailRequestEventStub;

    private TimetablePanel timetablePanel;

    private Path testFilePathFirst;
    private Path testFilePathSecond;

    @Before
    public void setUp() {
        exportTimetableAsImageRequestEventStub =
                new ExportTimetableAsImageRequestEvent(TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST);
        exportTimetableAsImageAndEmailRequestEventStub = new ExportTimetableAsImageAndEmailRequestEvent(
                TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST, TIMETABLE_IMAGE_EMAIL_TEST);

        testFilePathFirst = Paths.get("." + File.separator + TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST + "."
                + TIMETABLE_IMAGE_FILE_FORMAT);
        testFilePathSecond = Paths.get("." + File.separator + TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST + "."
                + TIMETABLE_IMAGE_FILE_FORMAT);

        timetablePanel = new TimetablePanel(TypicalEmployees.getTypicalPartTimeManager().getShiftList(),
                TypicalEmployees.getTypicalPartTimeManager().getOutletInformation());
        uiPartRule.setUiPart(timetablePanel);
    }

    @Test
    public void display() {
        // Default timetable view: Displays week view
        assertNotNull(timetablePanel.getRoot());
        assertEquals(timetablePanel.getRoot().getSelectedPage(), timetablePanel.getRoot().getWeekPage());

        // Snapshot taken when export command called
        postNow(exportTimetableAsImageRequestEventStub);
        assertTrue(Files.exists(testFilePathFirst) && Files.isRegularFile(testFilePathFirst));

        postNow(exportTimetableAsImageAndEmailRequestEventStub);
        assertTrue(Files.exists(testFilePathSecond) && Files.isRegularFile(testFilePathSecond));
    }

    @After
    public void tearDown() {
        try {
            Files.delete(testFilePathFirst);
            Files.delete(testFilePathSecond);
        } catch (IOException e) {
            throw new AssertionError("Error deleting test files.");
        }
    }

}
