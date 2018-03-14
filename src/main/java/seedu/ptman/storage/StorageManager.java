package seedu.ptman.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.ptman.commons.core.ComponentManager;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.model.PartTimeManagerChangedEvent;
import seedu.ptman.commons.events.storage.DataSavingExceptionEvent;
import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.UserPrefs;

/**
 * Manages storage of PartTimeManager data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private PartTimeManagerStorage partTimeManagerStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(PartTimeManagerStorage partTimeManagerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.partTimeManagerStorage = partTimeManagerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ PartTimeManager methods ==============================

    @Override
    public String getPartTimeManagerFilePath() {
        return partTimeManagerStorage.getPartTimeManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyPartTimeManager> readPartTimeManager() throws DataConversionException, IOException {
        return readPartTimeManager(partTimeManagerStorage.getPartTimeManagerFilePath());
    }

    @Override
    public Optional<ReadOnlyPartTimeManager> readPartTimeManager(String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return partTimeManagerStorage.readPartTimeManager(filePath);
    }

    @Override
    public void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException {
        savePartTimeManager(partTimeManager, partTimeManagerStorage.getPartTimeManagerFilePath());
    }

    @Override
    public void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        partTimeManagerStorage.savePartTimeManager(partTimeManager, filePath);
    }

    @Override
    public void backupPartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException {
        partTimeManagerStorage.backupPartTimeManager(partTimeManager);
    }

    @Override
    @Subscribe
    public void handlePartTimeManagerChangedEvent(PartTimeManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            savePartTimeManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
