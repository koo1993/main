package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.testutil.EventsUtil.postNow;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.AdminModeDisplayHandle;
import seedu.ptman.commons.events.model.UserModeChangedEvent;
import seedu.ptman.logic.Logic;
import seedu.ptman.logic.LogicManager;
import seedu.ptman.model.Model;
import seedu.ptman.model.ModelManager;

//@@author hzxcaryn
public class AdminModeDisplayTest extends GuiUnitTest {

    private UserModeChangedEvent userModeChangedEventLoginStub = new UserModeChangedEvent(true);
    private UserModeChangedEvent userModeChangedEventLogoutStub = new UserModeChangedEvent(false);

    private ArrayList<String> defaultStyleOfAdminModeDetails;
    private ArrayList<String> loginStyleOfAdminModeDetails;

    private AdminModeDisplayHandle adminModeDisplayHandle;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        Logic logic = new LogicManager(model);

        AdminModeDisplay adminModeDisplay = new AdminModeDisplay(logic.isAdminMode());
        uiPartRule.setUiPart(adminModeDisplay);

        adminModeDisplayHandle = new AdminModeDisplayHandle(getChildNode(adminModeDisplay.getRoot(),
                adminModeDisplayHandle.ADMIN_MODE_DISPLAY_LABEL_ID));

        defaultStyleOfAdminModeDetails = new ArrayList<>(adminModeDisplayHandle.getStyleClass());

        loginStyleOfAdminModeDetails = new ArrayList<>(defaultStyleOfAdminModeDetails);
        loginStyleOfAdminModeDetails.remove(AdminModeDisplay.LABEL_STYLE_CLASS_NON_ADMIN);
        loginStyleOfAdminModeDetails.add(AdminModeDisplay.LABEL_STYLE_CLASS_ADMIN);
    }

    @Test
    public void display() {
        // default Admin Mode Display text
        assertEquals("Admin Mode", adminModeDisplayHandle.getText());
        assertEquals(defaultStyleOfAdminModeDetails, adminModeDisplayHandle.getStyleClass());

        // login performed
        postNow(userModeChangedEventLoginStub);
        assertEquals(loginStyleOfAdminModeDetails, adminModeDisplayHandle.getStyleClass());

        // logout performed
        postNow(userModeChangedEventLogoutStub);
        assertEquals(defaultStyleOfAdminModeDetails, adminModeDisplayHandle.getStyleClass());
    }

}
