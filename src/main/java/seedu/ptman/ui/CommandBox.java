package seedu.ptman.ui;

import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.logging.Logger;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.ui.NewResultAvailableEvent;
import seedu.ptman.logic.ListElementPointer;
import seedu.ptman.logic.Logic;
import seedu.ptman.logic.commands.CommandResult;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.parser.exceptions.ParseException;


/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;
    private final Tooltip tooltip = new Tooltip();

    @FXML
    private TextField commandTextFieldOutput;
    @FXML
    private TextField commandTextFieldInput;

    //@@author koo1993
    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;

        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextFieldOutput.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        commandTextFieldOutput.setEditable(false);

        // copying text from input to output textfield
        commandTextFieldInput.textProperty()
                .addListener((observable, oldText, newText) -> commandTextFieldOutput.setText(processInput(newText)));

        commandTextFieldInput.setSkin(new TextFieldCaretControlSkin(commandTextFieldInput, Color.WHITE));
        historySnapshot = logic.getHistorySnapshot();
        tooltip.setText("Tip: Enter \"help\" when you get stuck");
        commandTextFieldInput.setTooltip(tooltip);

    }

    /**
     * obscure sensitive information like password by replacing it with "_"
     * @param input
     * @return the processed input
     */
    private String processInput(String input) {
        StringBuilder tempBuild = new StringBuilder(input);
        int indexOfPrefix = input.indexOf(PREFIX_PASSWORD.getPrefix());
        int indexOfSpace = input.indexOf(" ", indexOfPrefix);

        while (indexOfPrefix >= 0) {
            if (indexOfSpace == -1) {
                indexOfSpace = input.length();
            }
            for (int i = indexOfPrefix + 3; i < indexOfSpace; i++) {
                tempBuild.replace(i, i + 1, "*");
            }
            indexOfPrefix = input.indexOf(PREFIX_PASSWORD.getPrefix(), indexOfPrefix + 3);
            indexOfSpace = input.indexOf(" ", indexOfPrefix);
        }
        return tempBuild.toString();
    }

    /**
     * Disable caret at certain Password Field to avoid funny caret location when entering password
     */
    private void updateCaret() {
        int indexOfPrefix = commandTextFieldInput.getText().indexOf(PREFIX_PASSWORD.getPrefix());

        if (indexOfPrefix == -1) {
            indexOfPrefix = commandTextFieldInput.getText().length();
        }

        if (indexOfPrefix + 3 > commandTextFieldInput.getCaretPosition()) {
            commandTextFieldInput.setOpacity(1);
        } else {
            commandTextFieldInput.setOpacity(0);
        }
    }

    //@@author
    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();
            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        default:
            // let JavaFx handle the keypress
        }
        updateCaret();
    }

    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }

        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }

        replaceText(historySnapshot.next());
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        commandTextFieldInput.setText(text);
        commandTextFieldInput.positionCaret(commandTextFieldInput.getText().length());
    }



    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextFieldInput.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextFieldInput.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser, false));

        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextFieldInput.getText());
            raise(new NewResultAvailableEvent(e.getMessage(), true));
        }
    }

    /**
     * Handle clicks
     * @param event
     */
    @FXML
    private void handleClick(MouseEvent event) {
        updateCaret();
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextFieldOutput.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextFieldOutput.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }
    //@@author koo1993
    /**
     * class to set up caret colour for textField
     */
    public class TextFieldCaretControlSkin extends TextFieldSkin {

        public TextFieldCaretControlSkin(TextField textField, Color caretColor) {
            super(textField);
            setCaretColor(caretColor);
        }

        private void setCaretColor(Color color) {
            caretPath.strokeProperty().unbind();
            caretPath.fillProperty().unbind();
            caretPath.setStroke(color);
            caretPath.setFill(color);
        }
    }

}

