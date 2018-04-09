package seedu.ptman.storage;

import java.awt.Point;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.ptman.commons.core.ComponentManager;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.model.OutletDataChangedEvent;
import seedu.ptman.commons.events.model.PartTimeManagerChangedEvent;
import seedu.ptman.commons.events.storage.DataSavingExceptionEvent;
import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OutletInformation;

/**
 * Manages storage of PartTimeManager data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private PartTimeManagerStorage partTimeManagerStorage;
    private UserPrefsStorage userPrefsStorage;
    private OutletInformationStorage outletInformationStorage;


    public StorageManager(PartTimeManagerStorage partTimeManagerStorage, UserPrefsStorage userPrefsStorage,
                          OutletInformationStorage outletInformationStorage) {
        super();
        this.partTimeManagerStorage = partTimeManagerStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.outletInformationStorage = outletInformationStorage;
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
        Point coordinate = userPrefs.getGuiSettings().getWindowCoordinates();
        if (coordinate.x > 1600 || coordinate.x < 0 || coordinate.y > 1600 || coordinate.y < 0) {
            userPrefs.resetGuiPosition();
        }
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ PartTimeManager methods ==============================

    @Override
    public String getPartTimeManagerFilePath() {
        return partTimeManagerStorage.getPartTimeManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyPartTimeManager> readPartTimeManager(boolean isDataEncrypted)
            throws DataConversionException, IOException {
        return readPartTimeManager(isDataEncrypted, partTimeManagerStorage.getPartTimeManagerFilePath());
    }

    @Override
    public Optional<ReadOnlyPartTimeManager> readPartTimeManager(boolean isDataEncrypted, String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return partTimeManagerStorage.readPartTimeManager(isDataEncrypted, filePath);
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


    // ================ OutletInformation methods ==============================

    //@@author SunBangjie
    @Override
    public String getOutletInformationFilePath() {
        return outletInformationStorage.getOutletInformationFilePath();
    }

    @Override
    public Optional<OutletInformation> readOutletInformation() throws DataConversionException, IOException {
        return readOutletInformation(outletInformationStorage.getOutletInformationFilePath());
    }

    @Override
    public Optional<OutletInformation> readOutletInformation(String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return outletInformationStorage.readOutletInformation(filePath);
    }

    @Override
    public void saveOutletInformation(OutletInformation outletInformation) throws IOException {
        saveOutletInformation(outletInformation, outletInformationStorage.getOutletInformationFilePath());
    }

    @Override
    public void saveOutletInformation(OutletInformation outletInformation, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        outletInformationStorage.saveOutletInformation(outletInformation, filePath);
    }

    @Override
    @Subscribe
    public void handleOutletDataChangedEvent(OutletDataChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveOutletInformation(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
}
