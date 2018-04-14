package guitests.guihandles;

import static seedu.ptman.ui.TimetablePanel.getTimetableAvail;
import static seedu.ptman.ui.TimetablePanel.getTimetableEmployee;
import static seedu.ptman.ui.TimetablePanel.getTimetableFull;
import static seedu.ptman.ui.TimetablePanel.getTimetableOthers;
import static seedu.ptman.ui.TimetablePanel.getTimetableRunningOut;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.PageBase;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//@@author hzxcaryn
/**
 * A handler for the {@code TimetableView} of the UI
 */
public class TimetablePanelHandle extends NodeHandle<Node> {

    public static final String TIMETABLE_PANEL_PLACEHOLDER_ID = "#timetablePanelPlaceholder";

    private static final String TIMETABLE_VIEW_ID = "#timetableView";
    private static final String PREV_BUTTON_ID = "#prevButton";
    private static final String NEXT_BUTTON_ID = "#nextButton";
    private static final String MONTH_DISPLAY_ID = "#monthDisplay";

    private final CalendarView timetableViewNode;
    private final Button prevButtonNode;
    private final Button nextButtonNode;
    private final Label monthDisplayNode;

    public TimetablePanelHandle(Node rootNode) {
        super(rootNode);

        this.timetableViewNode = getChildNode(TIMETABLE_VIEW_ID);
        this.prevButtonNode = getChildNode(PREV_BUTTON_ID);
        this.nextButtonNode = getChildNode(NEXT_BUTTON_ID);
        this.monthDisplayNode = getChildNode(MONTH_DISPLAY_ID);
    }

    /**
     * @return current date timetable is using to display the week
     */
    public LocalDate getTimetableDate() {
        return timetableViewNode.getDate();
    }

    /**
     * @return selected page type that timetable is displaying
     */
    public PageBase getSelectedPage() {
        return timetableViewNode.getSelectedPage();
    }

    /**
     * @return a week page type
     */
    public PageBase getWeekPage() {
        return timetableViewNode.getWeekPage();
    }

    /**
     * @return month and year displayed in {@code monthDisplayNode}
     */
    public String getDisplayedMonthYear() {
        return monthDisplayNode.getText();
    }

    /**
     * Navigate to the prev timetable week by clicking the next button
     */
    public void navigateToPrevUsingButton() {
        guiRobot.clickOn(prevButtonNode);
    }

    /**
     * Navigate to the next timetable week by clicking the next button
     */
    public void navigateToNextUsingButton() {
        guiRobot.clickOn(nextButtonNode);
    }

    /**
     * Check for all the entries from all entry types in the timetable view
     * and returns a list of all the entries in sorted order
     * @return sorted list of all entries in timetable view
     */
    public List<Entry> getTimetableEntries() {
        List<Entry<?>> availEntries = getEntriesForEntryType(getTimetableAvail());
        List<Entry<?>> runningOutEntries = getEntriesForEntryType(getTimetableRunningOut());
        List<Entry<?>> fullEntries = getEntriesForEntryType(getTimetableFull());
        List<Entry<?>> employeeEntries = getEntriesForEntryType(getTimetableEmployee());
        List<Entry<?>> othersEntries = getEntriesForEntryType(getTimetableOthers());

        ArrayList<Entry> entries = new ArrayList<>(Stream.of(
                availEntries, runningOutEntries, fullEntries, employeeEntries, othersEntries)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Collections.sort(entries, Comparator.comparing(Entry::getStartAsLocalDateTime));
        return entries;
    }

    /**
     * For the given {@code entryType}, check for all entries in the timetable view
     * of that entry type and return them in a list
     * @param entryType
     * @return list of all entries of the {@code entryType}
     */
    public List<Entry<?>> getEntriesForEntryType(Calendar entryType) {
        Map<LocalDate, List<Entry<?>>> entryMap = entryType.findEntries(
                LocalDate.of(2018, 3, 19).minusDays(7),
                LocalDate.of(2018, 3, 19).plusDays(7), ZoneId.systemDefault());
        List<Entry<?>> entryList = entryMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return entryList;
    }
}
