package seedu.ptman.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.ptman.testutil.EventsUtil.postNow;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.ui.BrowserPanel.DEFAULT_PAGE;
import static seedu.ptman.ui.UiPart.FXML_FILE_FOLDER;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import seedu.ptman.MainApp;
import seedu.ptman.commons.events.ui.EmployeePanelSelectionChangedEvent;

public class BrowserPanelTest extends GuiUnitTest {
    private EmployeePanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new EmployeePanelSelectionChangedEvent(new EmployeeCard(ALICE, 0));

        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a employee
        postNow(selectionChangedEventStub);
        URL expectedEmployeeUrl =
                new URL(BrowserPanel.SEARCH_PAGE_URL + ALICE.getName().fullName.replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedEmployeeUrl, browserPanelHandle.getLoadedUrl());
    }
}
