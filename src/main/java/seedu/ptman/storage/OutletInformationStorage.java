package seedu.ptman.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.model.outlet.OutletInformation;

/**
 * Represents a storage for {@link seedu.ptman.model.outlet.OutletInformation}.
 */
public interface OutletInformationStorage {
    /**
     * Returns the file path of the data file.
     */
    String getOutletInformationFilePath();

    Optional<OutletInformation> readOutletInformation() throws DataConversionException, IOException;

    Optional<OutletInformation> readOutletInformation(String filePath) throws DataConversionException, IOException;

    void saveOutletInformation(OutletInformation outletInformation) throws IOException;

    void saveOutletInformation(OutletInformation outletInformation, String filePath) throws IOException;
}
