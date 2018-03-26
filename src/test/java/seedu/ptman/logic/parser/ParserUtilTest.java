package seedu.ptman.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.outlet.Capacity;
import seedu.ptman.model.outlet.Day;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletName;
import seedu.ptman.model.outlet.Time;
import seedu.ptman.model.tag.Tag;
import seedu.ptman.testutil.Assert;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_OUTLET_NAME = "Awesome@Outlet";
    private static final String INVALID_OUTLET_CONTACT = "+1234";
    private static final String INVALID_OPERATING_HOURS = "09:00/18:00";
    private static final String INVALID_DAY = "tue";
    private static final String INVALID_TIME = "1pm";
    private static final String INVALID_CAPACITY = "one";
    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_OUTLET_NAME = "AwesomeOutlet";
    private static final String VALID_OUTLET_CONTACT = "91234567";
    private static final String VALID_OPERATING_HOURS = "09:00-18:00";
    private static final String VALID_DAY = "tuesday";
    private static final String VALID_TIME = "1300";
    private static final String VALID_CAPACITY = "10";
    private static final String WHITESPACE = " \t\r\n";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_EMPLOYEE, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_EMPLOYEE, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseDay_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDay((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDay((Optional<String>) null));
    }

    @Test
    public void parseDay_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseDay(INVALID_DAY));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseDay(Optional.of(INVALID_DAY)));
    }

    @Test
    public void parseDay_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseDay(Optional.empty()).isPresent());
    }

    @Test
    public void parseDay_validValueWithoutWhitespace_returnsDay() throws Exception {
        Day expectedDay = new Day(VALID_DAY);
        assertEquals(expectedDay, ParserUtil.parseDay(VALID_DAY));
        assertEquals(Optional.of(expectedDay), ParserUtil.parseDay(Optional.of(VALID_DAY)));
    }

    @Test
    public void parseDay_validValueWithWhitespace_returnsTrimmedDay() throws Exception {
        String dayWithWhitespace = WHITESPACE + VALID_DAY + WHITESPACE;
        Day expectedDay = new Day(VALID_DAY);
        assertEquals(expectedDay, ParserUtil.parseDay(dayWithWhitespace));
        assertEquals(Optional.of(expectedDay), ParserUtil.parseDay(Optional.of(dayWithWhitespace)));
    }
    //
    @Test
    public void parseTime_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTime((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTime((Optional<String>) null));
    }

    @Test
    public void parseTime_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseTime(INVALID_TIME));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseTime(Optional.of(INVALID_TIME)));
    }

    @Test
    public void parseTime_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseTime(Optional.empty()).isPresent());
    }

    @Test
    public void parseTime_validValueWithoutWhitespace_returnsTime() throws Exception {
        Time expectedTime = new Time(VALID_TIME);
        assertEquals(expectedTime, ParserUtil.parseTime(VALID_TIME));
        assertEquals(Optional.of(expectedTime), ParserUtil.parseTime(Optional.of(VALID_TIME)));
    }

    @Test
    public void parseTime_validValueWithWhitespace_returnsTrimmedTime() throws Exception {
        String timeWithWhitespace = WHITESPACE + VALID_TIME + WHITESPACE;
        Time expectedTime = new Time(VALID_TIME);
        assertEquals(expectedTime, ParserUtil.parseTime(timeWithWhitespace));
        assertEquals(Optional.of(expectedTime), ParserUtil.parseTime(Optional.of(timeWithWhitespace)));
    }
    //

    @Test
    public void parseCapacity_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseCapacity((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseCapacity((Optional<String>) null));
    }

    @Test
    public void parseCapacity_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseCapacity(INVALID_CAPACITY));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseCapacity(Optional.of(INVALID_CAPACITY)));
    }

    @Test
    public void parseCapacity_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseCapacity(Optional.empty()).isPresent());
    }

    @Test
    public void parseCapacity_validValueWithoutWhitespace_returnsCapacity() throws Exception {
        Capacity expectedCapacity = new Capacity(VALID_CAPACITY);
        assertEquals(expectedCapacity, ParserUtil.parseCapacity(VALID_CAPACITY));
        assertEquals(Optional.of(expectedCapacity), ParserUtil.parseCapacity(Optional.of(VALID_CAPACITY)));
    }

    @Test
    public void parseCapacity_validValueWithWhitespace_returnsTrimmedCapacity() throws Exception {
        String capacityWithWhitespace = WHITESPACE + VALID_CAPACITY + WHITESPACE;
        Capacity expectedCapacity = new Capacity(VALID_CAPACITY);
        assertEquals(expectedCapacity, ParserUtil.parseCapacity(capacityWithWhitespace));
        assertEquals(Optional.of(expectedCapacity), ParserUtil.parseCapacity(Optional.of(capacityWithWhitespace)));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseName((Optional<String>) null));
    }

    @Test
    public void parseName_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseName(INVALID_NAME));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseName(Optional.of(INVALID_NAME)));
    }

    @Test
    public void parseName_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseName(Optional.empty()).isPresent());
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
        assertEquals(Optional.of(expectedName), ParserUtil.parseName(Optional.of(VALID_NAME)));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
        assertEquals(Optional.of(expectedName), ParserUtil.parseName(Optional.of(nameWithWhitespace)));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((Optional<String>) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parsePhone(Optional.of(INVALID_PHONE)));
    }

    @Test
    public void parsePhone_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parsePhone(Optional.empty()).isPresent());
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
        assertEquals(Optional.of(expectedPhone), ParserUtil.parsePhone(Optional.of(VALID_PHONE)));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
        assertEquals(Optional.of(expectedPhone), ParserUtil.parsePhone(Optional.of(phoneWithWhitespace)));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((Optional<String>) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseAddress(Optional.of(INVALID_ADDRESS)));
    }

    @Test
    public void parseAddress_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseAddress(Optional.empty()).isPresent());
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
        assertEquals(Optional.of(expectedAddress), ParserUtil.parseAddress(Optional.of(VALID_ADDRESS)));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
        assertEquals(Optional.of(expectedAddress), ParserUtil.parseAddress(Optional.of(addressWithWhitespace)));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((Optional<String>) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseEmail(Optional.of(INVALID_EMAIL)));
    }

    @Test
    public void parseEmail_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseEmail(Optional.empty()).isPresent());
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
        assertEquals(Optional.of(expectedEmail), ParserUtil.parseEmail(Optional.of(VALID_EMAIL)));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
        assertEquals(Optional.of(expectedEmail), ParserUtil.parseEmail(Optional.of(emailWithWhitespace)));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTag(null);
    }

    @Test
    public void parseTag_invalidValue_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseTag(INVALID_TAG);
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTags(null);
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseOutletName_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletName((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletName((Optional<String>) null));
    }

    @Test
    public void parseOutletName_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletName(INVALID_OUTLET_NAME));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletName(
                Optional.of(INVALID_OUTLET_NAME)));
    }

    @Test
    public void parseOutletName_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseOutletName(Optional.empty()).isPresent());
    }

    @Test
    public void parseOutletName_validValueWithoutWhitespace_returnsOutletName() throws Exception {
        OutletName expectedName = new OutletName(VALID_OUTLET_NAME);
        assertEquals(expectedName, ParserUtil.parseOutletName(VALID_OUTLET_NAME));
        assertEquals(Optional.of(expectedName), ParserUtil.parseOutletName(Optional.of(VALID_OUTLET_NAME)));
    }

    @Test
    public void parseOutletName_validValueWithWhitespace_returnsTrimmedOutletName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_OUTLET_NAME + WHITESPACE;
        OutletName expectedName = new OutletName(VALID_OUTLET_NAME);
        assertEquals(expectedName, ParserUtil.parseOutletName(nameWithWhitespace));
        assertEquals(Optional.of(expectedName), ParserUtil.parseOutletName(Optional.of(nameWithWhitespace)));
    }

    @Test
    public void parseOutletContact_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletContact((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOutletContact((Optional<String>) null));
    }

    @Test
    public void parseOutletContact_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletContact(
                INVALID_OUTLET_CONTACT));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOutletContact(
                Optional.of(INVALID_OUTLET_CONTACT)));
    }

    @Test
    public void parseOutletContact_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseOutletContact(Optional.empty()).isPresent());
    }

    @Test
    public void parseOutletContact_validValueWithoutWhitespace_returnsOutletContact() throws Exception {
        OutletContact expectedOutletContact = new OutletContact(VALID_OUTLET_CONTACT);
        assertEquals(expectedOutletContact, ParserUtil.parseOutletContact(VALID_OUTLET_CONTACT));
        assertEquals(Optional.of(expectedOutletContact), ParserUtil.parseOutletContact(
                Optional.of(VALID_OUTLET_CONTACT)));
    }

    @Test
    public void parseOutletContact_validValueWithWhitespace_returnsTrimmedOutletContact() throws Exception {
        String outletContactWithWhitespace = WHITESPACE + VALID_OUTLET_CONTACT + WHITESPACE;
        OutletContact expectedOutletContact = new OutletContact(VALID_OUTLET_CONTACT);
        assertEquals(expectedOutletContact, ParserUtil.parseOutletContact(outletContactWithWhitespace));
        assertEquals(Optional.of(expectedOutletContact), ParserUtil.parseOutletContact(
                Optional.of(outletContactWithWhitespace)));
    }




    @Test
    public void parseOperatingHours_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOperatingHours((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseOperatingHours((Optional<String>) null));
    }

    @Test
    public void parseOperatingHours_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOperatingHours(
                INVALID_OPERATING_HOURS));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseOperatingHours(
                Optional.of(INVALID_OPERATING_HOURS)));
    }

    @Test
    public void parseOperatingHours_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseOperatingHours(Optional.empty()).isPresent());
    }

    @Test
    public void parseOperatingHours_validValueWithoutWhitespace_returnsOperatingHours() throws Exception {
        OperatingHours expectedOperatingHours = new OperatingHours(VALID_OPERATING_HOURS);
        assertEquals(expectedOperatingHours, ParserUtil.parseOperatingHours(VALID_OPERATING_HOURS));
        assertEquals(Optional.of(expectedOperatingHours), ParserUtil.parseOperatingHours(
                Optional.of(VALID_OPERATING_HOURS)));
    }

    @Test
    public void parseOperatingHours_validValueWithWhitespace_returnsTrimmedOperatingHours() throws Exception {
        String operatingHoursWithWhitespace = WHITESPACE + VALID_OPERATING_HOURS + WHITESPACE;
        OperatingHours expectedOperatingHours = new OperatingHours(VALID_OPERATING_HOURS);
        assertEquals(expectedOperatingHours, ParserUtil.parseOperatingHours(operatingHoursWithWhitespace));
        assertEquals(Optional.of(expectedOperatingHours), ParserUtil.parseOperatingHours(
                Optional.of(operatingHoursWithWhitespace)));
    }

    @Test
    public void parseSecondIndex_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseSecondIndex("1 a"));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseSecondIndex("1 -1"));
    }
}
