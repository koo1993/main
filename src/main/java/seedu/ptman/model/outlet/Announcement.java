package seedu.ptman.model.outlet;

import static java.util.Objects.requireNonNull;

/**
 * Represents an announcement in PTMan.
 * Guarantees: immutable;
 */
public class Announcement {

    public final String value;

    public Announcement(String announcement) {
        requireNonNull(announcement);
        this.value = announcement;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Announcement // instanceof handles nulls
                && this.value.equals(((Announcement) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
