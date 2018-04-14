package seedu.ptman.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.ptman.commons.core.Config;
import seedu.ptman.commons.core.GuiSettings;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.commons.events.ui.ExitAppRequestEvent;
import seedu.ptman.commons.events.ui.ShowHelpRequestEvent;
import seedu.ptman.commons.events.ui.TimetableWeekChangeRequestEvent;
import seedu.ptman.logic.Logic;
import seedu.ptman.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    //@@author hzxcaryn
    private final KeyCombination keyCtrlShiftLeft =
            new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN);
    private final KeyCombination keyCtrlShiftRight =
            new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN);
    private final KeyCombination keyCtrlShiftDown =
            new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN);

    //@@author
    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private OutletDetailsPanel outletPanel;
    private TimetablePanel timetablePanel;
    private EmployeeListPanel employeeListPanel;
    private Config config;
    private UserPrefs prefs;

    @FXML
    private StackPane browserPlaceholder;

    @FXML
    private StackPane outletDetailsPanelPlaceholder;

    @FXML
    private StackPane ptmanLogoDisplayPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane adminModeDisplayPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane employeeListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane timetablePanelPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setWindowDefaultSize(prefs);

        setEventHandlerForTimetableWeekChangeRequestEvent();
        setAccelerators();
        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        outletPanel = new OutletDetailsPanel(logic.getOutletInformation());
        outletDetailsPanelPlaceholder.getChildren().add(outletPanel.getRoot());

        timetablePanel = new TimetablePanel(logic);
        timetablePanelPlaceholder.getChildren().add(timetablePanel.getRoot());

        employeeListPanel = new EmployeeListPanel(logic.getFilteredEmployeeList());
        employeeListPanelPlaceholder.getChildren().add(employeeListPanel.getRoot());

        PtmanLogoDisplay ptmanLogoDisplay = new PtmanLogoDisplay();
        ptmanLogoDisplayPlaceholder.getChildren().add(ptmanLogoDisplay.getRoot());

        AdminModeDisplay adminModeDisplay = new AdminModeDisplay(logic.isAdminMode());
        adminModeDisplayPlaceholder.getChildren().add(adminModeDisplay.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getPartTimeManagerFilePath(),
                logic.getFilteredEmployeeList().size());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    //@@author hzxcaryn
    /**
     * Listens to the {@code keyEvent} that requests to navigate to the prev/next timetable view and handles it.
     * Windows: Ctrl, Mac: Command
     */
    private void setEventHandlerForTimetableWeekChangeRequestEvent() {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (keyCtrlShiftLeft.match(event)) {
                event.consume();
                logger.fine("Timetable view requested to display the previous week.");
                raise(new TimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent.WeekChangeRequest.PREVIOUS));
            } else if (keyCtrlShiftRight.match(event)) {
                event.consume();
                logger.fine("Timetable view requested to display the next week.");
                raise(new TimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent.WeekChangeRequest.NEXT));
            } else if (keyCtrlShiftDown.match(event)) {
                event.consume();
                logger.fine("Timetable view requested to display the current week.");
                raise(new TimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent.WeekChangeRequest.CURRENT));
            }
        });
    }

    //@@author
    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public EmployeeListPanel getEmployeeListPanel() {
        return this.employeeListPanel;
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }
}
