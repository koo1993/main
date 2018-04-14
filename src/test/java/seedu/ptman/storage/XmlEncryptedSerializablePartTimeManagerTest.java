package seedu.ptman.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.commons.util.FileUtil;
import seedu.ptman.commons.util.XmlUtil;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.util.SampleDataUtil;

public class XmlEncryptedSerializablePartTimeManagerTest {

    private static final String TEST_DATA_FOLDER =
            FileUtil.getPath("src/test/data/XmlSerializablePartTimeManagerTest/");
    private static final File TYPICAL_EMPLOYEES_FILE =
            new File(TEST_DATA_FOLDER + "typicalEncryptedEmployeesPartTimeManager.xml");
    private static final File INVALID_EMPLOYEE_FILE =
            new File(TEST_DATA_FOLDER + "invalidEmployeePartTimeManager.xml");
    private static final File INVALID_TAG_FILE = new File(TEST_DATA_FOLDER + "invalidTagPartTimeManager.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalEmployeesFile_success() throws Exception {
        XmlEncryptedSerializablePartTimeManager dataFromFile = XmlUtil.getDataFromFile(TYPICAL_EMPLOYEES_FILE,
                XmlEncryptedSerializablePartTimeManager.class);
        PartTimeManager partTimeManagerFromFile = dataFromFile.toModelType();
        assertEquals(partTimeManagerFromFile.getEmployeeList(), SampleDataUtil.getSamplePartTimeManager()
                .getEmployeeList());
    }

    @Test
    public void toModelType_invalidEmployeeFile_throwsIllegalValueException() throws Exception {
        XmlEncryptedSerializablePartTimeManager dataFromFile = XmlUtil.getDataFromFile(INVALID_EMPLOYEE_FILE,
                XmlEncryptedSerializablePartTimeManager.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_invalidTagFile_throwsIllegalValueException() throws Exception {
        XmlEncryptedSerializablePartTimeManager dataFromFile = XmlUtil.getDataFromFile(INVALID_TAG_FILE,
                XmlEncryptedSerializablePartTimeManager.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void equals() throws Exception {
        XmlEncryptedSerializablePartTimeManager object = XmlUtil.getDataFromFile(TYPICAL_EMPLOYEES_FILE,
                XmlEncryptedSerializablePartTimeManager.class);
        XmlEncryptedSerializablePartTimeManager other = XmlUtil.getDataFromFile(TYPICAL_EMPLOYEES_FILE,
                XmlEncryptedSerializablePartTimeManager.class);
        assertEquals(object, other);
        assertTrue(object.equals(object));
        assertFalse(object.equals(null));
        assertFalse(object.equals(1));
    }
}
