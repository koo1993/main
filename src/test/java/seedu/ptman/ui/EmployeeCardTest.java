package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.ui.testutil.GuiTestAssert.assertCardDisplaysEmployee;

import org.junit.Test;

import guitests.guihandles.EmployeeCardHandle;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.testutil.EmployeeBuilder;

public class EmployeeCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Employee employeeWithNoTags = new EmployeeBuilder().withTags(new String[0]).build();
        EmployeeCard employeeCard = new EmployeeCard(employeeWithNoTags, 1);
        uiPartRule.setUiPart(employeeCard);
        assertCardDisplay(employeeCard, employeeWithNoTags, 1);

        // with tags
        Employee employeeWithTags = new EmployeeBuilder().build();
        employeeCard = new EmployeeCard(employeeWithTags, 2);
        uiPartRule.setUiPart(employeeCard);
        assertCardDisplay(employeeCard, employeeWithTags, 2);
    }

    @Test
    public void equals() {
        Employee employee = new EmployeeBuilder().build();
        EmployeeCard employeeCard = new EmployeeCard(employee, 0);

        // same employee, same index -> returns true
        EmployeeCard copy = new EmployeeCard(employee, 0);
        assertTrue(employeeCard.equals(copy));

        // same object -> returns true
        assertTrue(employeeCard.equals(employeeCard));

        // null -> returns false
        assertFalse(employeeCard.equals(null));

        // different types -> returns false
        assertFalse(employeeCard.equals(0));

        // different employee, same index -> returns false
        Employee differentEmployee = new EmployeeBuilder().withName("differentName").build();
        assertFalse(employeeCard.equals(new EmployeeCard(differentEmployee, 0)));

        // same employee, different index -> returns false
        assertFalse(employeeCard.equals(new EmployeeCard(employee, 1)));
    }

    /**
     * Asserts that {@code employeeCard} displays the details of {@code expectedEmployee} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(EmployeeCard employeeCard, Employee expectedEmployee, int expectedId) {
        guiRobot.pauseForHuman();

        EmployeeCardHandle employeeCardHandle = new EmployeeCardHandle(employeeCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", employeeCardHandle.getId());

        // verify employee details are displayed correctly
        assertCardDisplaysEmployee(expectedEmployee, employeeCardHandle);
    }
}
