package seedu.ptman.ui.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.calendarfx.model.Entry;

import guitests.guihandles.EmployeeCardHandle;
import guitests.guihandles.EmployeeListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.ui.EmployeeCard;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    private static final String LABEL_DEFAULT_STYLE = "label";

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(EmployeeCardHandle expectedCard, EmployeeCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getEmail(), actualCard.getEmail());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
        expectedCard.getTags().forEach(tag -> {
            assertEquals(expectedCard.getTagStyleClasses(tag), actualCard.getTagStyleClasses(tag));
        });
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedEmployee}.
     */
    public static void assertCardDisplaysEmployee(Employee expectedEmployee, EmployeeCardHandle actualCard) {
        assertEquals(expectedEmployee.getName().fullName, actualCard.getName());
        assertEquals(expectedEmployee.getPhone().value, actualCard.getPhone());
        assertEquals(expectedEmployee.getEmail().value, actualCard.getEmail());
        assertEquals(expectedEmployee.getAddress().value, actualCard.getAddress());
        assertEquals(expectedEmployee.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
        assertTagEquals(expectedEmployee, actualCard);
    }

    /**
     * Asserts that {@code actualEntry} displays the details of {@code expectedShift}.
     */
    public static void assertEntryDisplaysShift(Shift expectedShift, Entry actualEntry, int index) {
        assertEquals(expectedShift.getDate().getLocalDate(), actualEntry.getStartDate());
        assertEquals(expectedShift.getDate().getLocalDate(), actualEntry.getEndDate());
        assertEquals(expectedShift.getStartTime().getLocalTime(), actualEntry.getStartTime());
        assertEquals(expectedShift.getEndTime().getLocalTime(), actualEntry.getEndTime());
        assertEquals("SHIFT " + index + "\nSlots left: " + expectedShift.getSlotsLeft() + "/"
                        + expectedShift.getCapacity().getCapacity(),
                actualEntry.getTitle());
    }

    /**
     * Returns the color style for {@code tagName}'s label. The tag's color is determined by looking up the color
     * in {@code EmployeeCard#TAG_COLOR_STYLES}, using an index generated by the hash code of the tag's content.
     *
     * @see EmployeeCard#getTagColor(String)
     */
    private static String getTagColor(String tagName) {
        switch (tagName) {
        case "classmates":
        case "owesMoney":
            return "salmon";
        case "colleagues":
        case "neighbours":
            return "teal";
        case "family":
        case "friend":
            return "pink";
        case "friends":
            return "pale-blue";
        case "husband":
            return "yellow";
        default:
            fail(tagName + " does not have a color assigned.");
            return "";
        }
    }

    /**
     * Asserts that the tags in {@code actualCard} matches all the tags in
     * {@code expectedEmployee} with the correct color.
     * */
    private static void assertTagEquals(Employee expectedEmployee,
                                        EmployeeCardHandle actualCard) {
        List<String> expectedTags = expectedEmployee.getTags().stream().map(tag -> tag.tagName)
                .collect(Collectors.toList());
        assertEquals(expectedTags, actualCard.getTags());
        expectedTags.forEach(tag ->
                assertEquals(Arrays.asList(LABEL_DEFAULT_STYLE, getTagColor(tag)), actualCard.getTagStyleClasses(tag)));
    }

    /**
     * Asserts that the list in {@code employeeListPanelHandle} displays the details of {@code employees} correctly and
     * in the correct order.
     */
    public static void assertListMatching(EmployeeListPanelHandle employeeListPanelHandle, Employee... employees) {
        for (int i = 0; i < employees.length; i++) {
            assertCardDisplaysEmployee(employees[i], employeeListPanelHandle.getEmployeeCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code employeeListPanelHandle} displays the details of {@code employees} correctly and
     * in the correct order.
     */
    public static void assertListMatching(EmployeeListPanelHandle employeeListPanelHandle, List<Employee> employees) {
        assertListMatching(employeeListPanelHandle, employees.toArray(new Employee[0]));
    }

    /**
     * Asserts the size of the list in {@code employeeListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(EmployeeListPanelHandle employeeListPanelHandle, int size) {
        int numberOfPeople = employeeListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
