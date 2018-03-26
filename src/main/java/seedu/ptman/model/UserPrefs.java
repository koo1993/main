package seedu.ptman.model;

import java.util.Objects;

import seedu.ptman.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String partTimeManagerFilePath = "data/parttimemanager.xml";
    private String partTimeManagerName = "MyPartTimeManager";
    private String outletInformationFilePath = "data/outletinformation.xml";

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public String getPartTimeManagerFilePath() {
        return partTimeManagerFilePath;
    }

    public void setPartTimeManagerFilePath(String partTimeManagerFilePath) {
        this.partTimeManagerFilePath = partTimeManagerFilePath;
    }

    public String getOutletInformationFilePath() {
        return outletInformationFilePath;
    }

    public String getPartTimeManagerName() {
        return partTimeManagerName;
    }

    public void setPartTimeManagerName(String partTimeManagerName) {
        this.partTimeManagerName = partTimeManagerName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(partTimeManagerFilePath, o.partTimeManagerFilePath)
                && Objects.equals(partTimeManagerName, o.partTimeManagerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, partTimeManagerFilePath, partTimeManagerName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + partTimeManagerFilePath);
        sb.append("\nPartTimeManager name : " + partTimeManagerName);
        return sb.toString();
    }

}
