package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.control.Label;

//@@author hzxcaryn
/**
 * A handler for the {@code OutletDetailsPanel} of the UI
 */
public class OutletDetailsPanelHandle extends NodeHandle<Node> {

    public static final String OUTLET_ID = "#outletDetailsPanelPlaceholder";

    private static final String OUTLET_NAME_ID = "#outletNamePanelHeader";
    private static final String OUTLET_OPERATING_HOURS_ID = "#operatingHours";
    private static final String OUTLET_CONTACT_ID = "#outletContact";
    private static final String OUTLET_EMAIL_ID = "#outletEmail";
    private static final String OUTLET_ANNOUNCEMENT_ID = "#announcement";

    private final Label outletNameNode;
    private final Label outletOperatingHoursNode;
    private final Label outletContactNode;
    private final Label outletEmailNode;
    private final Label outletAnnouncementNode;

    private String lastRememberedOutletName;
    private String lastRememberedOutletOperatingHours;
    private String lastRememberedOutletContact;
    private String lastRememberedOutletEmail;
    private String lastRememberedOutletAnnouncement;

    public OutletDetailsPanelHandle(Node outletDetailsNode) {
        super(outletDetailsNode);

        this.outletNameNode = getChildNode(OUTLET_NAME_ID);
        this.outletOperatingHoursNode = getChildNode(OUTLET_OPERATING_HOURS_ID);
        this.outletContactNode = getChildNode(OUTLET_CONTACT_ID);
        this.outletEmailNode = getChildNode(OUTLET_EMAIL_ID);
        this.outletAnnouncementNode = getChildNode(OUTLET_ANNOUNCEMENT_ID);
    }

    /**
     * @return the outlet name in outlet panel.
     */
    public String getOutletName() {
        return outletNameNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getAnnouncement() {
        return outletAnnouncementNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getOutletOperatingHours() {
        return outletOperatingHoursNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getOutletContact() {
        return outletContactNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getOutletEmail() {
        return outletEmailNode.getText();
    }

    /**
     * Remembers the outlet name in the outlet panel.
     */
    public void rememberOutletName() {
        lastRememberedOutletName = getOutletName();
    }

    /**
     * Returns true if the current outlet name is different from the value remembered by the most recent
     * {@code rememberOutletName()} call.
     */
    public boolean isOutletNameChanged() {
        return !lastRememberedOutletName.equals(getOutletName());
    }

    /**
     * Remembers the outlet info in the outlet panel.
     */
    public void rememberOutletInformation() {
        lastRememberedOutletOperatingHours = getOutletOperatingHours();
        lastRememberedOutletContact = getOutletContact();
        lastRememberedOutletEmail = getOutletEmail();
    }

    /**
     * Returns true if the current outlet information is different from the value remembered by the most
     * recent {@code rememberOutletInformation()} call.
     */
    public boolean isOutletInformationChanged() {
        boolean isOutletOperatingHoursChanged = !lastRememberedOutletOperatingHours.equals(getOutletOperatingHours());
        boolean isOutletContactChanged = !lastRememberedOutletContact.equals(getOutletContact());
        boolean isOutletEmailChanged = !lastRememberedOutletEmail.equals(getOutletContact());
        return isOutletOperatingHoursChanged && isOutletContactChanged && isOutletEmailChanged;
    }

    /**
     * Remembers the outlet name in the outlet panel.
     */
    public void rememberOutletAnnouncement() {
        lastRememberedOutletAnnouncement = getAnnouncement();
    }

    /**
     * Returns true if the current outlet name is different from the value remembered by the most recent
     * {@code rememberOutletName()} call.
     */
    public boolean isOutletAnnouncementChanged() {
        return !lastRememberedOutletAnnouncement.equals(getAnnouncement());
    }

}
