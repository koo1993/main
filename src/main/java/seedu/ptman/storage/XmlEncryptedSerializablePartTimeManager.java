package seedu.ptman.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.outlet.OutletInformation;

/**
 * An Immutable PartTimeManager that is serializable to XML format
 */
@XmlRootElement(name = "parttimemanager")
public class XmlEncryptedSerializablePartTimeManager {

    private static final String ENCRYPTED_MESSAGE = OutletInformation.DATA_ENCRYPTED_MESSAGE;
    private static final String DECRYPTED_MESSAGE = OutletInformation.DATA_NOT_ENCRYPTED_MESSAGE;

    @XmlElement
    private String encryptionMode;
    @XmlElement
    private List<XmlEncryptedAdaptedEmployee> employees;
    @XmlElement
    private List<XmlEncryptedAdaptedTag> tags;
    @XmlElement
    private List<XmlEncryptedAdaptedShift> shifts;

    /**
     * Creates an empty XmlEncryptedSerializablePartTimeManager.
     * This empty constructor is required for marshalling.
     */
    public XmlEncryptedSerializablePartTimeManager() {
        employees = new ArrayList<>();
        tags = new ArrayList<>();
        shifts = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlEncryptedSerializablePartTimeManager(ReadOnlyPartTimeManager src) {
        this();
        this.encryptionMode = src.getOutletInformation().getEncryptionMode()
                ? ENCRYPTED_MESSAGE : DECRYPTED_MESSAGE;
        employees.addAll(src.getEmployeeList().stream().map(XmlEncryptedAdaptedEmployee::new)
                .collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlEncryptedAdaptedTag::new).collect(Collectors.toList()));
        shifts.addAll(src.getShiftList().stream().map(XmlEncryptedAdaptedShift::new).collect(Collectors.toList()));
    }

    /**
     * Converts this parttimemanager into the model's {@code PartTimeManager} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedEmployee} or {@code XmlAdaptedTag}.
     */
    public PartTimeManager toModelType() throws IllegalValueException {
        PartTimeManager partTimeManager = new PartTimeManager();
        for (XmlEncryptedAdaptedTag t : tags) {
            partTimeManager.addTag(t.toModelType());
        }
        for (XmlEncryptedAdaptedEmployee p : employees) {
            partTimeManager.addEmployee(p.toModelType());
        }
        for (XmlEncryptedAdaptedShift s : shifts) {
            partTimeManager.addShift(s.toModelType());
        }
        return partTimeManager;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlEncryptedSerializablePartTimeManager)) {
            return false;
        }

        XmlEncryptedSerializablePartTimeManager otherPtm = (XmlEncryptedSerializablePartTimeManager) other;
        return employees.equals(otherPtm.employees)
                && tags.equals(otherPtm.tags)
                && shifts.equals(otherPtm.shifts);
    }
}
