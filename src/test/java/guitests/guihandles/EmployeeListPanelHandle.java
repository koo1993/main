package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.ui.EmployeeCard;

/**
 * Provides a handle for {@code EmployeeListPanel} containing the list of {@code EmployeeCard}.
 */
public class EmployeeListPanelHandle extends NodeHandle<ListView<EmployeeCard>> {
    public static final String EMPLOYEE_LIST_VIEW_ID = "#employeeListView";

    private Optional<EmployeeCard> lastRememberedSelectedEmployeeCard;

    public EmployeeListPanelHandle(ListView<EmployeeCard> employeeListPanelNode) {
        super(employeeListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code EmployeeCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public EmployeeCardHandle getHandleToSelectedCard() {
        List<EmployeeCard> employeeList = getRootNode().getSelectionModel().getSelectedItems();

        if (employeeList.size() != 1) {
            throw new AssertionError("Employee list size expected 1.");
        }

        return new EmployeeCardHandle(employeeList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<EmployeeCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the employee.
     */
    public void navigateToCard(Employee employee) {
        List<EmployeeCard> cards = getRootNode().getItems();
        Optional<EmployeeCard> matchingCard = cards.stream().filter(card -> card.employee.equals(employee)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("Employee does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the employee card handle of a employee associated with the {@code index} in the list.
     */
    public EmployeeCardHandle getEmployeeCardHandle(int index) {
        return getEmployeeCardHandle(getRootNode().getItems().get(index).employee);
    }

    /**
     * Returns the {@code EmployeeCardHandle} of the specified {@code employee} in the list.
     */
    public EmployeeCardHandle getEmployeeCardHandle(Employee employee) {
        Optional<EmployeeCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.employee.equals(employee))
                .map(card -> new EmployeeCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Employee does not exist."));
    }

    /**
     * Selects the {@code EmployeeCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code EmployeeCard} in the list.
     */
    public void rememberSelectedEmployeeCard() {
        List<EmployeeCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedEmployeeCard = Optional.empty();
        } else {
            lastRememberedSelectedEmployeeCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code EmployeeCard} is different from the value remembered by the most recent
     * {@code rememberSelectedEmployeeCard()} call.
     */
    public boolean isSelectedEmployeeCardChanged() {
        List<EmployeeCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedEmployeeCard.isPresent();
        } else {
            return !lastRememberedSelectedEmployeeCard.isPresent()
                    || !lastRememberedSelectedEmployeeCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
