package seedu.ptman.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.commons.util.FileUtil;
import seedu.ptman.model.ReadOnlyPartTimeManager;

/**
 * A class to access PartTimeManager data stored as an xml file on the hard disk.
 */
public class XmlPartTimeManagerStorage implements PartTimeManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlPartTimeManagerStorage.class);
    private static final String BACKUP_FILE_EXTENSION = ".backup";

    private String filePath;

    public XmlPartTimeManagerStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getPartTimeManagerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyPartTimeManager> readPartTimeManager(boolean isDataEncrypted)
            throws DataConversionException, IOException {
        return readPartTimeManager(isDataEncrypted, filePath);
    }

    /**
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyPartTimeManager> readPartTimeManager(boolean isDataEncrypted, String filePath)
            throws DataConversionException, FileNotFoundException {
        requireNonNull(filePath);

        File partTimeManagerFile = new File(filePath);

        if (!partTimeManagerFile.exists()) {
            logger.info("PartTimeManager file "  + partTimeManagerFile + " not found");
            return Optional.empty();
        }

        if (isDataEncrypted) {
            XmlEncryptedSerializablePartTimeManager xmlEncryptedPartTimeManager =
                    XmlFileStorage.loadEncryptedDataFromSaveFile(new File(filePath));
            try {
                return Optional.of(xmlEncryptedPartTimeManager.toModelType());
            } catch (IllegalValueException ive) {
                logger.info("Illegal values found in " + partTimeManagerFile + ": " + ive.getMessage());
                throw new DataConversionException(ive);
            }
        } else {
            XmlSerializablePartTimeManager xmlPartTimeManager = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
            try {
                return Optional.of(xmlPartTimeManager.toModelType());
            } catch (IllegalValueException ive) {
                logger.info("Illegal values found in " + partTimeManagerFile + ": " + ive.getMessage());
                throw new DataConversionException(ive);
            }
        }
    }

    @Override
    public void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException {
        savePartTimeManager(partTimeManager, filePath);
    }

    /**
     * Similar to {@link #savePartTimeManager(ReadOnlyPartTimeManager)}
     * @param filePath location of the data. Cannot be null
     */
    public void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager, String filePath) throws IOException {
        requireNonNull(partTimeManager);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        if (partTimeManager.getOutletInformation().getEncryptionMode()) {
            XmlFileStorage.saveEncryptedDataToFile(file, new XmlEncryptedSerializablePartTimeManager(partTimeManager));
        } else {
            XmlFileStorage.saveDataToFile(file, new XmlSerializablePartTimeManager(partTimeManager));
        }
    }

    @Override
    public void backupPartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException {
        savePartTimeManager(partTimeManager, addFileNameExtentionIfNotNull(filePath));
    }

    /**
     *
     * @param filePath location of data.
     * @return
     */
    private String addFileNameExtentionIfNotNull(String filePath) {
        if (filePath == null) {
            return null;
        } else {
            return filePath + BACKUP_FILE_EXTENSION;
        }
    }

}
