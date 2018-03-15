package seedu.ptman.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.ptman.commons.core.ComponentManager;
import seedu.ptman.commons.core.LogsCenter;
import seedu.ptman.logic.commands.Command;
import seedu.ptman.logic.commands.CommandResult;
import seedu.ptman.logic.commands.exceptions.CommandException;
import seedu.ptman.logic.parser.PartTimeManagerParser;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Model;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.exceptions.InvalidPasswordException;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final PartTimeManagerParser partTimeManagerParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        partTimeManagerParser = new PartTimeManagerParser();
        undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = partTimeManagerParser.parseCommand(commandText);
            if (command.isAdminCommand()) {
                checkAuthorization(partTimeManagerParser.parseCommandForPassword(commandText));
            }
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    /**
     * Check if user is authorized
     * @param password in String
     * @throws InvalidPasswordException
     */
    public void checkAuthorization (String password) throws InvalidPasswordException {
        if (!model.isAdmin(password)) {
            throw new InvalidPasswordException();
        }
    }

    @Override
    public ObservableList<Employee> getFilteredEmployeeList() {
        return model.getFilteredEmployeeList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
