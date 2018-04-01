package seedu.ptman.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.model.UserModeChangedEvent;

/**
 * Admin mode panel of the app. Displays whether the user is in admin mode or not.
 */
public class AdminModeDisplay extends UiPart<Region> {

    public static final String LABEL_STYLE_CLASS_ADMIN = "label-admin-mode";
    public static final String LABEL_STYLE_CLASS_NON_ADMIN = "label-non-admin-mode";

    private static final String adminModeText = "Admin Mode";
    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "AdminModeDisplay.fxml";

    @FXML
    private Label adminModeDisplay;

    public AdminModeDisplay(boolean isAdminMode) {
        super(FXML);

        adminModeDisplay.setText(adminModeText);
        setLabelStyle(isAdminMode);

        registerAsAnEventHandler(this);
    }

    private void setLabelStyle(boolean isAdminMode) {
        ObservableList<String> styleClass = this.adminModeDisplay.getStyleClass();
        if (isAdminMode) {
            styleClass.remove(LABEL_STYLE_CLASS_NON_ADMIN);
            styleClass.add(LABEL_STYLE_CLASS_ADMIN);
        } else {
            styleClass.remove(LABEL_STYLE_CLASS_ADMIN);
            styleClass.add(LABEL_STYLE_CLASS_NON_ADMIN);
        }
    }

    @Subscribe
    private void handleUserModeChangedEvent(UserModeChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setLabelStyle(event.isAdminMode));
    }

}
