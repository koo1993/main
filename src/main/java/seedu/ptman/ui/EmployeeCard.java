package seedu.ptman.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.ptman.model.employee.Employee;

/**
 * An UI component that displays information of a {@code Employee}.
 */
public class EmployeeCard extends UiPart<Region> {

    private static final String FXML = "EmployeeListCard.fxml";
    private static final String[] TAG_COLORS =
        {"salmon", "mint", "teal", "turquoise", "pink", "pale-blue", "purple", "blue", "indigo", "yellow"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see
     * <a href="https://github.com/se-edu/parttimemanager-level4/issues/336">The issue on PartTimeManager level 4</a>
     */

    public final Employee employee;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public EmployeeCard(Employee employee, int displayedIndex) {
        super(FXML);
        this.employee = employee;
        id.setText(displayedIndex + ". ");
        name.setText(employee.getName().fullName);
        phone.setText(employee.getPhone().value);
        address.setText(employee.getAddress().value);
        email.setText(employee.getEmail().value);
        addTagLabels(employee);
    }

    /**
     * Tag colors are derived by calculating the hash code of the {@code tagName} string to
     * select a color from the {@code TAG_COLORS} array
     *
     * @param tagName
     * @return color for the {@code tagName}'s tag label
     */
    private String getTagColor(String tagName) {
        int index = Math.abs(tagName.hashCode()) %  TAG_COLORS.length;
        return TAG_COLORS[index];
    }

    /**
     * Adds all tags of {@code employee} as color-coded labels
     */
    private void addTagLabels(Employee employee) {
        employee.getTags().forEach(tag -> {
            Label newLabel = new Label(tag.tagName);
            newLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(newLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmployeeCard)) {
            return false;
        }

        // state check
        EmployeeCard card = (EmployeeCard) other;
        return id.getText().equals(card.id.getText())
                && employee.equals(card.employee);
    }
}
