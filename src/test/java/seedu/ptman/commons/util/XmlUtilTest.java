package seedu.ptman.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.model.PartTimeManager;
import seedu.ptman.storage.XmlAdaptedEmployee;
import seedu.ptman.storage.XmlAdaptedTag;
import seedu.ptman.storage.XmlSerializablePartTimeManager;
import seedu.ptman.testutil.EmployeeBuilder;
import seedu.ptman.testutil.PartTimeManagerBuilder;
import seedu.ptman.testutil.TestUtil;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validEncryptedPartTimeManager.xml");
    private static final File MISSING_EMPLOYEE_FIELD_FILE = new File(TEST_DATA_FOLDER + "missingEmployeeField.xml");
    private static final File INVALID_EMPLOYEE_FIELD_FILE = new File(TEST_DATA_FOLDER + "invalidEmployeeField.xml");
    private static final File VALID_EMPLOYEE_FILE = new File(TEST_DATA_FOLDER + "validEmployee.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempPartTimeManager.xml"));

    private static final String INVALID_PHONE = "alexyeoh@example.com";

    private static final String VALID_NAME = "Alex Yeoh";
    private static final String VALID_PHONE = "87438807";
    private static final String VALID_EMAIL = "alexyeoh@example.com";
    private static final String VALID_ADDRESS = "Blk 30 Geylang Street 29, #06-40";
    private static final String VALID_SALARY = "0";
    private static final String DEFAULT1_HASH = "wkqTFuX6NX3hucWqn2ZxB24cRo73LssRq7IDOk6Zx00=";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, PartTimeManager.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, PartTimeManager.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, PartTimeManager.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        PartTimeManager dataFromFile =
                XmlUtil.getDataFromFile(VALID_FILE, XmlSerializablePartTimeManager.class).toModelType();
        assertEquals(6, dataFromFile.getEmployeeList().size());
        assertEquals(5, dataFromFile.getTagList().size());
    }

    @Test
    public void xmlAdaptedEmployeeFromFile_fileWithMissingEmployeeField_validResult() throws Exception {
        XmlAdaptedEmployee actualEmployee = XmlUtil.getDataFromFile(
                MISSING_EMPLOYEE_FIELD_FILE, XmlAdaptedEmployeeWithRootElement.class);
        XmlAdaptedEmployee expectedEmployee = new XmlAdaptedEmployee(
                null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void xmlAdaptedEmployeeFromFile_fileWithInvalidEmployeeField_validResult() throws Exception {
        XmlAdaptedEmployee actualEmployee = XmlUtil.getDataFromFile(
                INVALID_EMPLOYEE_FIELD_FILE, XmlAdaptedEmployeeWithRootElement.class);
        XmlAdaptedEmployee expectedEmployee = new XmlAdaptedEmployee(
                VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void xmlAdaptedEmployeeFromFile_fileWithValidEmployee_validResult() throws Exception {
        XmlAdaptedEmployee actualEmployee = XmlUtil.getDataFromFile(
                VALID_EMPLOYEE_FILE, XmlAdaptedEmployeeWithRootElement.class);
        XmlAdaptedEmployee expectedEmployee = new XmlAdaptedEmployee(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY, DEFAULT1_HASH, VALID_TAGS);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new PartTimeManager());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new PartTimeManager());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        TEMP_FILE.createNewFile();
        XmlSerializablePartTimeManager dataToWrite = new XmlSerializablePartTimeManager(new PartTimeManager());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializablePartTimeManager dataFromFile =
                XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializablePartTimeManager.class);
        assertEquals(dataToWrite, dataFromFile);

        PartTimeManagerBuilder builder = new PartTimeManagerBuilder(new PartTimeManager());
        dataToWrite = new XmlSerializablePartTimeManager(
                builder.withEmployee(new EmployeeBuilder().build()).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializablePartTimeManager.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement}
     * to allow unmarshalling of .xml data to {@code XmlAdaptedEmployee}
     * objects.
     */
    @XmlRootElement(name = "employee")
    private static class XmlAdaptedEmployeeWithRootElement extends XmlAdaptedEmployee {}
}
