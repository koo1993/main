package seedu.ptman.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.commons.util.FileUtil;
import seedu.ptman.model.outlet.Announcement;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletEmail;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.OutletName;

//@@author SunBangjie
public class XmlOutletInformationStorageTest {
    private static final String TEST_DATA_FOLDER =
            FileUtil.getPath("./src/test/data/XmlOutletInformationStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readOutletInformation_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readOutletInformation(null);
    }

    private java.util.Optional<OutletInformation> readOutletInformation(String filePath) throws Exception {
        return new XmlOutletInformationStorage(filePath).readOutletInformation(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readOutletInformation("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {
        thrown.expect(DataConversionException.class);
        readOutletInformation("NotXmlFormatOutletInformation.xml");
    }

    @Test
    public void readOutletInformation_invalidOutletInformation_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readOutletInformation("invalidOutletInformation.xml");
    }

    @Test
    public void readAndSaveOutletInformation_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempOutletInformation.xml";
        OutletInformation original = new OutletInformation();
        XmlOutletInformationStorage xmlOutletInformationStorage = new XmlOutletInformationStorage(filePath);

        //Save in new file and read back
        xmlOutletInformationStorage.saveOutletInformation(original, filePath);
        OutletInformation readBack = xmlOutletInformationStorage.readOutletInformation(filePath).get();
        assertEquals(original, readBack);

        //Modify data, overwrite existing file, and read back
        original.setAnnouncement(new Announcement("new announcement"));
        original.setOutletInformation(new OutletName("newName"), new OperatingHours("1000-2000"),
                new OutletContact("92223333"), new OutletEmail("newOutlet@gmail.com"));
        xmlOutletInformationStorage.saveOutletInformation(original, filePath);
        readBack = xmlOutletInformationStorage.readOutletInformation(filePath).get();
        assertEquals(original, readBack);

        //Save and read without specifying file path
        original.setAnnouncement(new Announcement("new announcement"));
        original.setOutletInformation(new OutletName("newName"), new OperatingHours("1000-2000"),
                new OutletContact("92223333"), new OutletEmail("newOutlet@gmail.com"));
        xmlOutletInformationStorage.saveOutletInformation(original);
        readBack = xmlOutletInformationStorage.readOutletInformation().get();
        assertEquals(original, readBack);
    }

    @Test
    public void saveOutletInformation_nullOutletInformation_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveOutletInformation(null, "SomeFile.xml");
    }

    @Test
    public void saveOutletInformation_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveOutletInformation(new OutletInformation(), null);
    }

    /**
     * Saves {@code outletInformation} at the specified {@code filePath}.
     */
    private void saveOutletInformation(OutletInformation outletInformation, String filePath) {
        try {
            new XmlOutletInformationStorage(filePath)
                    .saveOutletInformation(outletInformation, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }
}
