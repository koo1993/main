package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.testutil.EventsUtil.postNow;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.OutletDetailsPanelHandle;
import seedu.ptman.commons.events.ui.AnnouncementChangedEvent;
import seedu.ptman.commons.events.ui.OutletInformationChangedEvent;
import seedu.ptman.commons.events.ui.OutletNameChangedEvent;
import seedu.ptman.model.outlet.OutletInformation;

//@@author hzxcaryn
public class OutletDetailsPanelTest extends GuiUnitTest {
    private OutletInformationChangedEvent outletInformationChangedEventStub;
    private OutletNameChangedEvent outletNameChangedEventStub;
    private AnnouncementChangedEvent announcementChangedEventStub;

    private OutletDetailsPanel outletDetailsPanel;
    private OutletDetailsPanelHandle outletDetailsPanelHandle;
    private OutletInformation outlet = new OutletInformation();

    @Before
    public void setUp() {
        outletInformationChangedEventStub = new OutletInformationChangedEvent("New Operating Hours",
                "New Outlet Contact", "New Outlet Email");
        outletNameChangedEventStub = new OutletNameChangedEvent("New Outlet Name");
        announcementChangedEventStub = new AnnouncementChangedEvent("New Announcement");
        outletDetailsPanel = new OutletDetailsPanel(outlet);

        uiPartRule.setUiPart(outletDetailsPanel);
        outletDetailsPanelHandle = new OutletDetailsPanelHandle(outletDetailsPanel.getRoot());
    }

    @Test
    public void display() {
        // Default outlet name and information
        String expectedDefaultOutletName = "DefaultOutlet";
        String expectedTrimmedDefaultOutletOperatingHours = "09:00-22:00";
        String expectedTrimmedDefaultOutletContact = "91234567";
        String expectedTrimmedDefaultOutletEmail = "DefaultOutlet@gmail.com";
        assertEquals(expectedTrimmedDefaultOutletOperatingHours,
                outletDetailsPanelHandle.getOutletOperatingHours().trim());
        assertEquals(expectedTrimmedDefaultOutletContact, outletDetailsPanelHandle.getOutletContact().trim());
        assertEquals(expectedTrimmedDefaultOutletEmail, outletDetailsPanelHandle.getOutletEmail().trim());
        assertEquals(expectedDefaultOutletName, outletDetailsPanelHandle.getOutletName());

        // Outlet Information: Operating Hours Updated
        postNow(outletInformationChangedEventStub);
        String expectedTrimmedOutletOperatingHours = "New Operating Hours";
        assertEquals(expectedTrimmedOutletOperatingHours, outletDetailsPanelHandle.getOutletOperatingHours().trim());

        // Outlet Information: Contact Updated
        postNow(outletInformationChangedEventStub);
        String expectedTrimmedOutletContact = "New Outlet Contact";
        assertEquals(expectedTrimmedOutletContact, outletDetailsPanelHandle.getOutletContact().trim());

        // Outlet Information: Email Updated
        postNow(outletInformationChangedEventStub);
        String expectedTrimmedOutletEmail = "New Outlet Email";
        assertEquals(expectedTrimmedOutletEmail, outletDetailsPanelHandle.getOutletEmail().trim());

        // Outlet Name Updated
        postNow(outletNameChangedEventStub);
        String expectedOutletName = "New Outlet Name";
        assertEquals(expectedOutletName, outletDetailsPanelHandle.getOutletName());

        // Announcement Updated
        postNow(announcementChangedEventStub);
        String expectedAnnouncement = "New Announcement";
        assertEquals(expectedAnnouncement, outletDetailsPanelHandle.getAnnouncement());

    }

}
