package seedu.ptman.model;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalEmployees.BENSON;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;
import static seedu.ptman.testutil.TypicalShifts.MONDAY_AM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.tag.Tag;
import seedu.ptman.testutil.EmployeeBuilder;
import seedu.ptman.testutil.PartTimeManagerBuilder;

public class PartTimeManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final PartTimeManager partTimeManager = new PartTimeManager();
    private final PartTimeManager partTimeManagerWithAliceAndBenson = new PartTimeManagerBuilder().withEmployee(ALICE)
            .withEmployee(BENSON).build();


    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), partTimeManager.getEmployeeList());
        assertEquals(Collections.emptyList(), partTimeManager.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        partTimeManager.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyPartTimeManager_replacesData() {
        PartTimeManager newData = getTypicalPartTimeManager();
        partTimeManager.resetData(newData);
        assertEquals(newData, partTimeManager);
    }

    @Test
    public void resetData_withDuplicateEmployees_throwsAssertionError() {
        // Repeat ALICE twice
        List<Employee> newEmployees = Arrays.asList(ALICE, ALICE);
        List<Shift> newShifts = new ArrayList<>();
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        OutletInformation outlet = new OutletInformation();
        PartTimeManagerStub newData = new PartTimeManagerStub(newEmployees, newTags, newShifts, outlet);

        thrown.expect(AssertionError.class);
        partTimeManager.resetData(newData);
    }

    //@@author shanwpf
    @Test
    public void resetData_withDuplicateShifts_throwsAssertionError() {
        List<Employee> newEmployees = Arrays.asList(ALICE);
        List<Shift> newShifts = Arrays.asList(MONDAY_AM, MONDAY_AM);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        OutletInformation outlet = new OutletInformation();
        PartTimeManagerStub newData = new PartTimeManagerStub(newEmployees, newTags, newShifts, outlet);

        thrown.expect(AssertionError.class);
        partTimeManager.resetData(newData);
    }
    //@@author

    @Test
    public void getEmployeeList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        partTimeManager.getEmployeeList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        partTimeManager.getTagList().remove(0);
    }

    @Test
    public void removeTagFromAllEmployees_tagNonExisting_partTimeManagerNoChange() {
        partTimeManagerWithAliceAndBenson.removeTagFromAllEmployees(new Tag("NoSuchTag"));

        PartTimeManager expectedPartTimeManager = new PartTimeManagerBuilder().withEmployee(ALICE)
                .withEmployee(BENSON).build();

        assertEquals(expectedPartTimeManager, partTimeManagerWithAliceAndBenson);
    }

    @Test
    public void removeTagFromAllEmployees_tagExistInMultipleEmployees_tagChanged() {
        partTimeManagerWithAliceAndBenson.removeTagFromAllEmployees(new Tag("friends"));

        Employee aliceWithoutFriendTag = new EmployeeBuilder(ALICE).withTags().build();
        Employee bensonWithoutFriendTag = new EmployeeBuilder(BENSON).withTags("owesMoney").build();
        PartTimeManager expectedPartTimeManager = new PartTimeManagerBuilder().withEmployee(aliceWithoutFriendTag)
                .withEmployee(bensonWithoutFriendTag).build();

        assertEquals(expectedPartTimeManager, partTimeManagerWithAliceAndBenson);
    }

    @Test
    public void getOutletInformationMessage_defaultData_returnCorrectMessage() {
        String actualMessage = partTimeManager.getOutletInformationMessage();
        String expectedMessage = new OutletInformation().toString();
        assertEquals(actualMessage, expectedMessage);
    }

    /**
     * A stub ReadOnlyPartTimeManager whose employees and tags lists can violate interface constraints.
     */
    private static class PartTimeManagerStub implements ReadOnlyPartTimeManager {
        private final ObservableList<Employee> employees = FXCollections.observableArrayList();
        private final ObservableList<Shift> shifts = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();
        private final OutletInformation outlet;

        PartTimeManagerStub(Collection<Employee> employees, Collection<? extends Tag> tags,
                            Collection<Shift> shifts, OutletInformation outlet) {
            this.employees.setAll(employees);
            this.tags.setAll(tags);
            this.shifts.setAll(shifts);
            this.outlet = outlet;
        }

        @Override
        public ObservableList<Employee> getEmployeeList() {
            return employees;
        }

        @Override
        public ObservableList<Shift> getShiftList() {
            return shifts;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }

        @Override
        public OutletInformation getOutletInformation() {
            return outlet;
        }
    }

}
