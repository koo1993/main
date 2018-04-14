package seedu.ptman.storage;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.storage.XmlEncryptedAdaptedEmployee.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.ptman.testutil.TypicalEmployees.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.testutil.Assert;

public class XmlEncryptedAdaptedEmployeeTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_SALARY = "-2030";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_SALARY = BENSON.getSalary().toString();
    private static final String DEFAULT1_HASH = BENSON.getPassword().getPasswordHash();

    private static final List<XmlEncryptedAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(XmlEncryptedAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validEmployeeDetails_returnsEmployee() throws Exception {
        XmlEncryptedAdaptedEmployee employee = new XmlEncryptedAdaptedEmployee(BENSON);
        assertEquals(BENSON, employee.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee =
                new XmlEncryptedAdaptedEmployee(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee = new XmlEncryptedAdaptedEmployee(null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee =
                new XmlEncryptedAdaptedEmployee(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee = new XmlEncryptedAdaptedEmployee(VALID_NAME, null, VALID_EMAIL,
                VALID_ADDRESS, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee =
                new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee = new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, null,
                VALID_ADDRESS, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee =
                new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee = new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_invalidSalary_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee =
                new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        INVALID_SALARY, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = Salary.MESSAGE_SALARY_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_nullSalary_throwsIllegalValueException() {
        XmlEncryptedAdaptedEmployee employee = new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, null, DEFAULT1_HASH, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Salary.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, employee::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlEncryptedAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlEncryptedAdaptedTag(INVALID_TAG));
        XmlEncryptedAdaptedEmployee employee =
                new XmlEncryptedAdaptedEmployee(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        DEFAULT1_HASH, invalidTags);
        Assert.assertThrows(IllegalValueException.class, employee::toModelType);
    }

    @Test
    public void setAttributesFromSource_validInputs_returnsEqualObject() {
        Employee employee = new Employee(new Name(VALID_NAME), new Phone(VALID_PHONE), new Email(VALID_EMAIL),
                new Address(VALID_ADDRESS), new Salary(VALID_SALARY), new Password(DEFAULT1_HASH),
                BENSON.getTags());
        XmlEncryptedAdaptedEmployee adaptedEmployee = new XmlEncryptedAdaptedEmployee();
        adaptedEmployee.setAttributesFromSource(employee);
        XmlEncryptedAdaptedEmployee sameAdaptedEmployee = new XmlEncryptedAdaptedEmployee();
        sameAdaptedEmployee.setAttributesFromSource(employee);
        assertEquals(adaptedEmployee, sameAdaptedEmployee);
    }

    @Test
    public void setAttributesFromStrings_validInputs_returnEqualObject() {
        XmlEncryptedAdaptedEmployee adaptedEmployee = new XmlEncryptedAdaptedEmployee();
        XmlEncryptedAdaptedEmployee sameAdaptedEmployee = new XmlEncryptedAdaptedEmployee();
        adaptedEmployee.setAttributesFromStrings(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, DEFAULT1_HASH);
        sameAdaptedEmployee.setAttributesFromStrings(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, DEFAULT1_HASH);
        assertEquals(adaptedEmployee, sameAdaptedEmployee);
    }
}
