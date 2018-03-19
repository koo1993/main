package seedu.ptman.model.outlet;

import static seedu.ptman.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Objects;

import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.exceptions.DuplicateShiftException;

/**
 * Represents an outlet in PTMan.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OutletInformation {

    private static final String DEFAULT_OUTLET_NAME = "DefaultOutlet";
    private static final String DEFAULT_OPERATING_HOURS = "09:00-22:00";

    private OutletName name;
    private Password masterPassword;
    private OperatingHours operatingHours;
    private Timetable timetable;

    /**
     * Constructs an {@code OutletInformation}.
     *
     * @param name a valid outlet name
     * @param masterPassword a valid master password
     * @param operatingHours a valid operating hours
     */
    public OutletInformation(OutletName name, Password masterPassword, OperatingHours operatingHours,
                             Timetable timetable) {
        requireAllNonNull(name, masterPassword, operatingHours, timetable);
        this.name = name;
        this.masterPassword = masterPassword;
        this.operatingHours = operatingHours;
        this.timetable = timetable;
    }

    /**
     * Default constructor with default values
     */
    public OutletInformation() {
        this.name = new OutletName(DEFAULT_OUTLET_NAME);
        this.masterPassword = new Password();
        this.operatingHours = new OperatingHours(DEFAULT_OPERATING_HOURS);
        this.timetable = new Timetable(LocalDate.now());
    }

    public void addShift(Shift shift) throws DuplicateShiftException {
        timetable.addShift(shift);
    }

    public OutletName getName() {
        return name;
    }

    public Password getMasterPassword() {
        return masterPassword;
    }

    public OperatingHours getOperatingHours() {
        return operatingHours;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setOutletInformation(OutletName name, OperatingHours operatingHours) {
        requireAllNonNull(name, operatingHours);
        this.name = name;
        this.operatingHours = operatingHours;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OutletInformation
                && ((OutletInformation) other).getName().equals(this.getName())
                && ((OutletInformation) other).getMasterPassword().equals(this.getMasterPassword())
                && ((OutletInformation) other).getOperatingHours().equals(this.getOperatingHours())
                && ((OutletInformation) other).getTimetable().equals(this.getTimetable()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, masterPassword, operatingHours, timetable);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Outlet Name: ")
                .append(getName())
                .append(" Operating Hour: ")
                .append(getOperatingHours());
        return builder.toString();
    }

}
