package seedu.ptman.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.ptman.commons.events.model.OutletDataChangedEvent;
import seedu.ptman.commons.events.model.PartTimeManagerChangedEvent;
import seedu.ptman.commons.events.storage.DataSavingExceptionEvent;
import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.UserPrefs;
import seedu.ptman.model.outlet.OutletInformation;

/**
 * API of the Storage component
 */
public interface Storage extends PartTimeManagerStorage, UserPrefsStorage, OutletInformationStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getPartTimeManagerFilePath();

    @Override
    String getOutletInformationFilePath();

    @Override
    Optional<ReadOnlyPartTimeManager> readPartTimeManager() throws DataConversionException, IOException;

    @Override
    void savePartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException;

    @Override
    Optional<OutletInformation> readOutletInformation() throws DataConversionException, IOException;

    @Override
    void saveOutletInformation(OutletInformation outletInformation) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handlePartTimeManagerChangedEvent(PartTimeManagerChangedEvent abce);

    void handleOutletDataChangedEvent(OutletDataChangedEvent odce);
}
