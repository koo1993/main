package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.testutil.EventsUtil.postNow;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalEmployees;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_SECOND_EMPLOYEE;
import static seedu.ptman.ui.testutil.GuiTestAssert.assertCardDisplaysEmployee;
import static seedu.ptman.ui.testutil.GuiTestAssert.assertCardEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.EmployeeCardHandle;
import guitests.guihandles.EmployeeListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.ptman.commons.events.ui.JumpToListRequestEvent;
import seedu.ptman.model.employee.Employee;

public class EmployeeListPanelTest extends GuiUnitTest {
    private static final ObservableList<Employee> TYPICAL_EMPLOYEES =
            FXCollections.observableList(getTypicalEmployees());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT =
            new JumpToListRequestEvent(INDEX_SECOND_EMPLOYEE);

    private EmployeeListPanelHandle employeeListPanelHandle;

    @Before
    public void setUp() {
        EmployeeListPanel employeeListPanel = new EmployeeListPanel(TYPICAL_EMPLOYEES);
        uiPartRule.setUiPart(employeeListPanel);

        employeeListPanelHandle = new EmployeeListPanelHandle(getChildNode(employeeListPanel.getRoot(),
                EmployeeListPanelHandle.EMPLOYEE_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_EMPLOYEES.size(); i++) {
            employeeListPanelHandle.navigateToCard(TYPICAL_EMPLOYEES.get(i));
            Employee expectedEmployee = TYPICAL_EMPLOYEES.get(i);
            EmployeeCardHandle actualCard = employeeListPanelHandle.getEmployeeCardHandle(i);

            assertCardDisplaysEmployee(expectedEmployee, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        EmployeeCardHandle expectedCard =
                employeeListPanelHandle.getEmployeeCardHandle(INDEX_SECOND_EMPLOYEE.getZeroBased());
        EmployeeCardHandle selectedCard = employeeListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedCard, selectedCard);
    }
}
