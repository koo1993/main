package seedu.ptman.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalEmployees.HOON;
import static seedu.ptman.testutil.TypicalEmployees.IDA;
import static seedu.ptman.testutil.TypicalEmployees.getTypicalPartTimeManager;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.commons.util.FileUtil;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.ReadOnlyPartTimeManager;

public class XmlPartTimeManagerStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlPartTimeManagerStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readPartTimeManager_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readPartTimeManager(null);
    }

    private java.util.Optional<ReadOnlyPartTimeManager> readPartTimeManager(String filePath) throws Exception {
        return new XmlPartTimeManagerStorage(filePath).readPartTimeManager(false,
                addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readPartTimeManager("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readPartTimeManager("NotXmlFormatPartTimeManager.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readPartTimeManager_invalidEmployeePartTimeManager_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readPartTimeManager("invalidEmployeePartTimeManager.xml");
    }

    @Test
    public void readPartTimeManager_invalidAndValidEmployeePartTimeManager_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readPartTimeManager("invalidAndValidEmployeePartTimeManager.xml");
    }

    @Test
    public void readAndSavePartTimeManager_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempPartTimeManager.xml";
        PartTimeManager original = getTypicalPartTimeManager();
        XmlPartTimeManagerStorage xmlPartTimeManagerStorage = new XmlPartTimeManagerStorage(filePath);

        //Save in new file and read back
        xmlPartTimeManagerStorage.savePartTimeManager(original, filePath);
        ReadOnlyPartTimeManager readBack = xmlPartTimeManagerStorage.readPartTimeManager(false, filePath).get();
        assertEquals(original, new PartTimeManager(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addEmployee(HOON);
        original.removeEmployee(ALICE);
        xmlPartTimeManagerStorage.savePartTimeManager(original, filePath);
        readBack = xmlPartTimeManagerStorage.readPartTimeManager(false, filePath).get();
        assertEquals(original, new PartTimeManager(readBack));

        //Save and read without specifying file path
        original.addEmployee(IDA);
        xmlPartTimeManagerStorage.savePartTimeManager(original); //file path not specified
        readBack = xmlPartTimeManagerStorage.readPartTimeManager(false).get(); //file path not specified
        assertEquals(original, new PartTimeManager(readBack));

    }

    @Test
    public void savePartTimeManager_nullPartTimeManager_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        savePartTimeManager(null, "SomeFile.xml");
    }

    /**
     * Saves {@code partTimeManager} at the specified {@code filePath}.
     */
    private void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager, String filePath) {
        try {
            new XmlPartTimeManagerStorage(filePath)
                    .savePartTimeManager(partTimeManager, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void savePartTimeManager_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        savePartTimeManager(new PartTimeManager(), null);
    }

    @Test
    public void backupPartTimeManager_nullPartTimeManager_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        backupPartTimeManager(null, TEST_DATA_FOLDER + "SomeBackupFile.xml");
    }

    @Test
    public void backupPartTimeManager_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        backupPartTimeManager(new PartTimeManager(), null);
    }

    /**
     * Backups {@code partTimeManager} at the specified {@code filePath}.
     */
    private void backupPartTimeManager(ReadOnlyPartTimeManager partTimeManager, String filePath) {
        try {
            new XmlPartTimeManagerStorage(filePath).backupPartTimeManager(partTimeManager);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }


}
