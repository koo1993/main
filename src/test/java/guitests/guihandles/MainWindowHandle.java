package guitests.guihandles;

import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final EmployeeListPanelHandle employeeListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final CommandBoxHandle commandBoxOutput;
    private final AdminModeDisplayHandle adminModeDisplay;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final OutletDetailsPanelHandle outletPanel;
    private final TimetableViewHandle timetableView;

    public MainWindowHandle(Stage stage) {
        super(stage);

        employeeListPanel = new EmployeeListPanelHandle(getChildNode(EmployeeListPanelHandle.EMPLOYEE_LIST_VIEW_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBoxOutput = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_OUTPUT_FIELD_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        adminModeDisplay = new AdminModeDisplayHandle(getChildNode(AdminModeDisplayHandle.ADMIN_MODE_DISPLAY_LABEL_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        outletPanel = new OutletDetailsPanelHandle(getChildNode(OutletDetailsPanelHandle.OUTLET_ID));
        timetableView = new TimetableViewHandle(getChildNode(TimetableViewHandle.TIMETABLE_ID));
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

    public CommandBoxHandle getCommandBoxOutput() {
        return commandBoxOutput;
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

    public TimetableViewHandle getTimetableView() {
        return timetableView;
    }

}
