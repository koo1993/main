package seedu.ptman.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.ui.EmployeePanelSelectionChangedEvent;
import seedu.ptman.commons.events.ui.JumpToListRequestEvent;
import seedu.ptman.model.employee.Employee;

/**
 * Panel containing the list of employees.
 */
public class EmployeeListPanel extends UiPart<Region> {
    private static final String FXML = "EmployeeListPanel.fxml";
    private static final String PANEL_HEADER = "Employees";
    private final Logger logger = LogsCenter.getLogger(EmployeeListPanel.class);

    @FXML
    private ListView<EmployeeCard> employeeListView;

    @FXML
    private Label employeeListPanelHeader;

    public EmployeeListPanel(ObservableList<Employee> employeeList) {
        super(FXML);
        employeeListPanelHeader.setText(PANEL_HEADER);
        setConnections(employeeList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Employee> employeeList) {
        ObservableList<EmployeeCard> mappedList = EasyBind.map(
                employeeList, (employee) -> new EmployeeCard(employee, employeeList.indexOf(employee) + 1));
        employeeListView.setItems(mappedList);
        employeeListView.setCellFactory(listView -> new EmployeeListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        employeeListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in employee list panel changed to : '" + newValue + "'");
                    } else {
                        logger.fine("Employee list panel deselected");
                    }
                    raise(new EmployeePanelSelectionChangedEvent(newValue));
                });
    }

    /**
     * Scrolls to the {@code EmployeeCard} at the {@code index} and selects it.
     */
    private void scrollToAndSelect(int index) {
        Platform.runLater(() -> {
            employeeListView.scrollTo(index);
            employeeListView.getSelectionModel().clearAndSelect(index);
        });
    }

    //@@author hzxcaryn
    /**
     * Scrolls to the top of the {@code EmployeeListPanel} and deselect any current selection.
     */
    private void scrollToTopAndDeselect() {
        Platform.runLater(() -> {
            employeeListView.scrollTo(0);
            employeeListView.getSelectionModel().clearSelection();
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.isNewSelection) {
            scrollToAndSelect(event.targetIndex);
        } else {
            scrollToTopAndDeselect();
        }
    }

    //@@author
    /**
     * Custom {@code ListCell} that displays the graphics of a {@code EmployeeCard}.
     */
    class EmployeeListViewCell extends ListCell<EmployeeCard> {

        @Override
        protected void updateItem(EmployeeCard employee, boolean empty) {
            super.updateItem(employee, empty);

            if (empty || employee == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(employee.getRoot());
            }
        }
    }

}
