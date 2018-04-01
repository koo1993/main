package guitests.guihandles;

import java.util.List;

import javafx.scene.control.Label;

//@@author hzxcaryn
/**
 * A handler for the {@code AdminModeDetails} of the UI
 */
public class AdminModeDisplayHandle extends NodeHandle<Label> {

    public static final String ADMIN_MODE_DISPLAY_LABEL_ID = "#adminModeDisplay";

    public AdminModeDisplayHandle (Label adminModeDisplayNode) {
        super(adminModeDisplayNode);
    }

    /**
     * @return the text in the admin mode label
     */
    public String getText() {
        return getRootNode().getText();
    }

    /**
     * @return the list of style classes present in the admin mode display
     */
    public List<String> getStyleClass() {
        return getRootNode().getStyleClass();
    }

}
