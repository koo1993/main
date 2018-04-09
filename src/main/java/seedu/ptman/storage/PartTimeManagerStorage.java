package seedu.ptman.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.model.ReadOnlyPartTimeManager;

/**
 * Represents a storage for {@link seedu.ptman.model.PartTimeManager}.
 */
public interface PartTimeManagerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getPartTimeManagerFilePath();

    /**
     * Returns PartTimeManager data as a {@link ReadOnlyPartTimeManager}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyPartTimeManager> readPartTimeManager(boolean isDataEncrypted)
            throws DataConversionException, IOException;

    /**
     * @see #getPartTimeManagerFilePath()
     */
    Optional<ReadOnlyPartTimeManager> readPartTimeManager(boolean isDataEncrypted, String filePath)
            throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyPartTimeManager} to the storage.
     * @param partTimeManager cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException;

    /**
     * @see #savePartTimeManager(ReadOnlyPartTimeManager)
     */
    void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager, String filePath) throws IOException;

    void backupPartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException;

}
