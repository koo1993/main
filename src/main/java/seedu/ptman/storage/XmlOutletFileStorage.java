package seedu.ptman.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import seedu.ptman.commons.exceptions.DataConversionException;
import seedu.ptman.commons.util.XmlUtil;

/**
 * Stores outlet information data in an XML file
 */
public class XmlOutletFileStorage {

    /**
     * Saves the given parttimemanager data to the specified file.
     */
    public static void saveDataToFile(File file, XmlAdaptedOutletInformation outletInformation)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, outletInformation);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns outlet information in the file or an empty outlet information
     */
    public static XmlAdaptedOutletInformation loadDataFromSaveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlAdaptedOutletInformation.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }
}
