package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.testutil.EventsUtil.postNow;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.ResultDisplayHandle;
import seedu.ptman.commons.events.ui.NewResultAvailableEvent;

public class ResultDisplayTest extends GuiUnitTest {

    private static final NewResultAvailableEvent NEW_RESULT_EVENT_SUCCESS =
            new NewResultAvailableEvent("Success", false);
    private static final NewResultAvailableEvent NEW_RESULT_EVENT_FAILED =
            new NewResultAvailableEvent("Failed", true);

    private ArrayList<String> defaultStyleOfResultDisplay;
    private ArrayList<String> errorStyleOfResultDisplay;

    private ResultDisplayHandle resultDisplayHandle;

    @Before
    public void setUp() {
        ResultDisplay resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(getChildNode(resultDisplay.getRoot(),
                ResultDisplayHandle.RESULT_DISPLAY_ID));

        defaultStyleOfResultDisplay = new ArrayList<>(resultDisplayHandle.getStyleClass());

        errorStyleOfResultDisplay = new ArrayList<>(defaultStyleOfResultDisplay);
        errorStyleOfResultDisplay.add(CommandBox.ERROR_STYLE_CLASS);
    }

    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());
        assertEquals(defaultStyleOfResultDisplay, resultDisplayHandle.getStyleClass());

        // new results received
        assertExpectedResultDisplay(NEW_RESULT_EVENT_SUCCESS);
        assertExpectedResultDisplay(NEW_RESULT_EVENT_FAILED);
    }

    /**
     * Runs a new result event, then verifies that <br>
     *      - the text remains <br>
     *      - a successful event result display's style is the same as {@code defaultStyleOfResultDisplay}.
     *      - a failed event result display's style is the same as {@code errorStyleOfResultDisplay}.
     */
    private void assertExpectedResultDisplay(NewResultAvailableEvent event) {
        postNow(event);
        guiRobot.pauseForHuman();
        assertEquals(event.message, resultDisplayHandle.getText());

        if (event.isError) {
            assertEquals(errorStyleOfResultDisplay, resultDisplayHandle.getStyleClass());
        } else {
            assertEquals(defaultStyleOfResultDisplay, resultDisplayHandle.getStyleClass());
        }

    }


}
