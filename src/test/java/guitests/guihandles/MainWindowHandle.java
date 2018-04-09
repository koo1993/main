package guitests.guihandles;

import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final EmployeeListPanelHandle employeeListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final AdminModeDisplayHandle adminModeDisplay;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final OutletDetailsPanelHandle outletPanel;
    private final TimetablePanelHandle timetablePanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        employeeListPanel = new EmployeeListPanelHandle(getChildNode(EmployeeListPanelHandle.EMPLOYEE_LIST_VIEW_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_FIELD_ID));
        adminModeDisplay = new AdminModeDisplayHandle(getChildNode(AdminModeDisplayHandle.ADMIN_MODE_DISPLAY_LABEL_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        outletPanel = new OutletDetailsPanelHandle(getChildNode(OutletDetailsPanelHandle.OUTLET_ID));
        timetablePanel = new TimetablePanelHandle(getChildNode(TimetablePanelHandle.TIMETABLE_PANEL_PLACEHOLDER_ID));
    }

    public EmployeeListPanelHandle getEmployeeListPanel() {
        return employeeListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public AdminModeDisplayHandle getAdminModeDisplay() {
        return adminModeDisplay;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    public OutletDetailsPanelHandle getOutletDetailsPanel() {
        return outletPanel;
    }

    public TimetablePanelHandle getTimetablePanel() {
        return timetablePanel;
    }

}
