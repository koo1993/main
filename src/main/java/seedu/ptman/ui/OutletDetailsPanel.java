package seedu.ptman.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.ui.AnnouncementChangedEvent;
import seedu.ptman.commons.events.ui.OutletInformationChangedEvent;
import seedu.ptman.commons.events.ui.OutletNameChangedEvent;
import seedu.ptman.model.outlet.OutletInformation;

//@@author SunBangjie
/**
 * The Outlet Panel of the App, which displays the Outlet name and details
 */
public class OutletDetailsPanel extends UiPart<Region> {

    private static final String FXML = "OutletDetailsPanel.fxml";

    public final OutletInformation outlet;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Label outletNamePanelHeader;

    @FXML
    private Label operatingHours;

    @FXML
    private Label outletContact;

    @FXML
    private Label outletEmail;

    @FXML
    private Label announcement;


    public OutletDetailsPanel(OutletInformation outlet) {
        super(FXML);
        this.outlet = outlet;
        //outletInformation.setWrapText(true);
        setOutletInformation(outlet.getOperatingHours().getDisplayedMessage(),
                outlet.getOutletContact().toString(),
                outlet.getOutletEmail().toString());
        setOutletName(outlet.getName().toString());
        setAnnouncement(outlet.getAnnouncement().toString());

        registerAsAnEventHandler(this);
    }

    private void setOutletName(String name) {
        outletNamePanelHeader.setText(name);
    }

    private void setOutletInformation(String operatingHours, String outletContact, String outletEmail) {
        this.operatingHours.setText(operatingHours + "    ");
        this.outletContact.setText(outletContact + "    ");
        this.outletEmail.setText(outletEmail);
    }

    private void setAnnouncement(String text) {
        announcement.setText(text);
    }

    @Subscribe
    private void handleOutletInformationChangedEvent(OutletInformationChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setOutletInformation(event.operatingHours, event.outletContact, event.outletEmail));
    }

    @Subscribe
    private void handleOutletNameChangedEvent(OutletNameChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setOutletName(event.message));
    }

    //@@author SunBangjie
    @Subscribe
    public void handleAnnouncementChangedEvent(AnnouncementChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setAnnouncement(event.information));
    }
}
