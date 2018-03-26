package seedu.ptman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalEmployees.BENSON;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.model.employee.NameContainsKeywordsPredicate;
import seedu.ptman.model.outlet.Announcement;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletEmail;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.OutletName;
import seedu.ptman.model.outlet.exceptions.NoOutletInformationFieldChangeException;
import seedu.ptman.testutil.PartTimeManagerBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredEmployeeList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredEmployeeList().remove(0);
    }

    @Test
    public void updateOutlet() throws NoOutletInformationFieldChangeException {
        PartTimeManager partTimeManager = new PartTimeManager();
        PartTimeManager differentPartTimeManager = new PartTimeManager();
        UserPrefs userPrefs = new UserPrefs();

        OutletInformation outlet = new OutletInformation(new OutletName("test"), new OperatingHours("10:00-15:00"),
                new OutletContact("123"), new OutletEmail("test@test.com"),
                new Password(), new Announcement("New Announcement."));

        ModelManager modelManager = new ModelManager(differentPartTimeManager, userPrefs, new OutletInformation());
        ModelManager differentModelManager = new ModelManager(partTimeManager, userPrefs, new OutletInformation());

        assertEquals(modelManager, differentModelManager);
        modelManager.updateOutlet(outlet);
        assertNotEquals(modelManager, differentModelManager);
    }

    @Test
    public void equals() {
        PartTimeManager partTimeManager = new PartTimeManagerBuilder().withEmployee(ALICE).withEmployee(BENSON).build();
        PartTimeManager differentPartTimeManager = new PartTimeManager();
        UserPrefs userPrefs = new UserPrefs();
        OutletInformation outlet = new OutletInformation();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(partTimeManager, userPrefs, outlet);
        ModelManager modelManagerCopy = new ModelManager(partTimeManager, userPrefs, outlet);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different partTimeManager -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentPartTimeManager, userPrefs, outlet)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredEmployeeList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(partTimeManager, userPrefs, outlet)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setPartTimeManagerName("differentName");
        assertTrue(modelManager.equals(new ModelManager(partTimeManager, differentUserPrefs, outlet)));
    }
}
