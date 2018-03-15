package seedu.ptman.model.employee;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.ptman.testutil.Assert;

public class SalaryTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Salary(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidSalary = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Salary(invalidSalary));
    }

    @Test
    public void isValidSalary() {
        //null salary
        Assert.assertThrows(NullPointerException.class, () -> Salary.isValidSalary(null));

        // invalid Salary
        assertFalse(Salary.isValidSalary("")); // empty string
        assertFalse(Salary.isValidSalary(" ")); // space only
        assertFalse(Salary.isValidSalary("abc")); // alphabets
        assertFalse(Salary.isValidSalary("123abc")); // alphanumeric
        assertFalse(Salary.isValidSalary("123 123")); // space within digits
        assertFalse(Salary.isValidSalary("-10")); // negative digits

        //valid Salary
        assertTrue(Salary.isValidSalary("0")); // no salary yet
        assertTrue(Salary.isValidSalary("100"));
        assertTrue(Salary.isValidSalary("999999"));
    }
}
