package seedu.ptman.model.util;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import seedu.ptman.commons.util.DateUtil;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.model.Password;
import seedu.ptman.model.ReadOnlyPartTimeManager;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.employee.exceptions.DuplicateEmployeeException;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;
import seedu.ptman.model.shift.exceptions.DuplicateShiftException;
import seedu.ptman.model.shift.exceptions.ShiftFullException;
import seedu.ptman.model.tag.Tag;

//@@author hzxcaryn
/**
 * Contains utility methods for populating {@code PartTimeManager} with sample data.
 */
public class SampleDataUtil {

    public static Employee[] getSampleEmployees() {
        return new Employee[] {
            new Employee(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("300"), new Password(),
                getTagSet("barista", "cashier")),
            new Employee(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Salary("0"), new Password(),
                getTagSet("barista", "supervisor")),
            new Employee(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Salary("100"), new Password(),
                getTagSet("bartender")),
            new Employee(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Salary("450"), new Password(),
                getTagSet("paperwork")),
            new Employee(new Name("Eden Lim"), new Phone("92624417"), new Email("eden123@example.com"),
                new Address("Blk 25 Aljunied Street 85, #10-10"), new Salary("250"), new Password(),
                getTagSet("supervisor", "bartender")),
            new Employee(new Name("Emelia Tan"), new Phone("91275306"), new Email("tanmeme@example.com"),
                new Address("Blk 45 Bras Basah Rd 55, #11-11"), new Salary("60"), new Password(),
                getTagSet("paperwork", "barista")),
            new Employee(new Name("Faith Foo"), new Phone("82935501"), new Email("faithful@example.com"),
                new Address("Blk 01 Pasir Ris Street 81, #01-01"), new Salary("110"), new Password(),
                getTagSet("barista", "bartender")),
            new Employee(new Name("Irfan Ibrahim"), new Phone("82492021"), new Email("irfan@example.com"),
                new Address("Blk 17 Tampines Street 20, #17-35"), new Salary("200"), new Password(),
                getTagSet("barista")),
            new Employee(new Name("Jessica Liu"), new Phone("92823467"), new Email("liushabao@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("300"), new Password(),
                getTagSet("alfresco", "cashier")),
            new Employee(new Name("Jorge Keng"), new Phone("90129036"), new Email("kengjjj@example.com"),
                new Address("Blk 105 Tampines Street 85, #04-01"), new Salary("80"), new Password(),
                getTagSet("supervisor", "paperwork")),
            new Employee(new Name("Katrina Rose"), new Phone("80924520"), new Email("rosie@example.com"),
                new Address("Blk 555 Simei Street 05, #06-06"), new Salary("100"), new Password(),
                getTagSet("cashier", "bartender", "paperwork")),
            new Employee(new Name("Lee Wenqi"), new Phone("88124243"), new Email("wenqiqi@example.com"),
                new Address("Blk 01 Defu Lane 05, #02-11"), new Salary("500"), new Password(),
                getTagSet("bartender", "barista")),
            new Employee(new Name("Liu Shi Qi"), new Phone("87438807"), new Email("liushishi@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("300"), new Password(),
                getTagSet("barista", "cashier")),
            new Employee(new Name("Mallory Hek"), new Phone("99272758"), new Email("maliciousme@example.com"),
                new Address("Blk 30 Serangoon Gardens, #07-18"), new Salary("1000"), new Password(),
                getTagSet("barista", "chef")),
            new Employee(new Name("Matthew Koh"), new Phone("93210283"), new Email("madmatt@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Salary("100"), new Password(),
                getTagSet("bartender")),
            new Employee(new Name("Nathan Gay"), new Phone("91031282"), new Email("gaygaygay@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Salary("450"), new Password(),
                getTagSet("paperwork", "cashier")),
            new Employee(new Name("Ophelia Grey"), new Phone("92492021"), new Email("shades0fgrey@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new Salary("200"), new Password(),
                getTagSet("barista")),
            new Employee(new Name("Patrick Soo"), new Phone("91234417"), new Email("guais00s00@example.com"),
                new Address("Blk 99 Boon Keng Road 85, #08-01"), new Salary("50"), new Password(),
                getTagSet("bartender", "barista")),
            new Employee(new Name("Philips Loy"), new Phone("89801253"), new Email("pheeloy@example.com"),
                new Address("Blk 103 Hougang Street 32, #05-03"), new Salary("60"), new Password(),
                getTagSet("alfresco")),
            new Employee(new Name("Quentin Cool"), new Phone("92624417"), new Email("iamcool123@example.com"),
                new Address("Blk 111 Punggol Street 05, #01-05"), new Salary("300"), new Password(),
                getTagSet("chef")),
            new Employee(new Name("Roy Balakrishnan"), new Phone("83623312"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("100"), new Password(),
                getTagSet("paperwork", "alfresco")),
            new Employee(new Name("Vaibhavi Shandilya"), new Phone("91530773"), new Email("chiobu@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("100"), new Password(),
                getTagSet("supervisor")),
        };
    }

    public static Shift[] getSampleShifts() {
        LocalDate mondayDateOfCurrWeek = DateUtil.getMondayOfDate(LocalDate.now());
        LocalDate mondayDateOfPrevWeek = DateUtil.getPrevWeekDate(mondayDateOfCurrWeek);
        LocalDate mondayDateOfNextWeek = DateUtil.getNextWeekDate(mondayDateOfCurrWeek);
        return new Shift[] {
            // Build shifts for previous week
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(1)), new Time("0900"), new Time("1600"),
                new Capacity("3")),
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(2)), new Time("1500"), new Time("2200"),
                new Capacity("1")),
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(4)), new Time("1000"), new Time("1600"),
                new Capacity("5")),
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(5)), new Time("1100"), new Time("1900"),
                new Capacity("2")),

            // Build shifts for current week
            new Shift(new Date(mondayDateOfCurrWeek), new Time("1500"), new Time("2200"), new Capacity("1")),
            new Shift(new Date(mondayDateOfCurrWeek.plusDays(1)), new Time("0900"), new Time("1600"),
                new Capacity("5")),
            new Shift(new Date(mondayDateOfCurrWeek.plusDays(2)), new Time("1000"), new Time("1700"),
                new Capacity("3")),
            new Shift(new Date(mondayDateOfCurrWeek.plusDays(4)), new Time("0900"), new Time("1700"),
                new Capacity("3")),

            // Build shifts for next week
            new Shift(new Date(mondayDateOfNextWeek.plusDays(2)), new Time("1100"), new Time("1900"),
                new Capacity("2")),
            new Shift(new Date(mondayDateOfNextWeek.plusDays(3)), new Time("0900"), new Time("1600"),
                new Capacity("5")),
            new Shift(new Date(mondayDateOfNextWeek.plusDays(5)), new Time("1500"), new Time("2200"),
                new Capacity("3")),
            new Shift(new Date(mondayDateOfNextWeek.plusDays(6)), new Time("1200"), new Time("1900"),
                new Capacity("5"))
        };

    }

    /**
     * This method is used in {@code getSamplePartTimeManager} to add employees to the sample shifts.
     * Each nested array contains employee indices that corresponds to an employee in the sample employee array.
     * Each sample shift is assigned one of these nested arrays according to their index in {@code getSampleShifts}.
     */
    public static int[][] getSampleEmployeesForEachShift() {
        return new int[][] {
            // previous week
            new int[] {5, 17, 21},
            new int[] {6},
            new int[] {1, 2, 3, 4, 5},
            new int[] {7, 9},

            // current week
            new int[] {},
            new int[] {2, 10},
            new int[] {2, 5, 7},
            new int[] {3},

            // next week
            new int[] {2},
            new int[] {15, 20},
            new int[] {3, 5, 7},
            new int[] {2, 8, 10},
        };
    }

    public static ReadOnlyPartTimeManager getSamplePartTimeManager() {
        try {
            PartTimeManager sampleAb = new PartTimeManager();
            Employee[] sampleEmployees = getSampleEmployees();
            Shift[] sampleShifts = getSampleShifts();
            int[][] sampleEmployeesForEachShift = getSampleEmployeesForEachShift();

            for (Employee sampleEmployee : sampleEmployees) {
                sampleAb.addEmployee(sampleEmployee);
            }
            for (int i = 0; i < sampleShifts.length; i++) {
                for (int employeeIndex : sampleEmployeesForEachShift[i]) {
                    sampleShifts[i].addEmployee(sampleEmployees[employeeIndex]);
                }
                sampleAb.addShift(sampleShifts[i]);
            }
            return sampleAb;
        } catch (DuplicateEmployeeException e) {
            throw new AssertionError("sample data cannot contain duplicate employees", e);
        } catch (DuplicateShiftException e) {
            throw new AssertionError("sample data cannot contain duplicate shifts", e);
        } catch (ShiftFullException e) {
            throw new AssertionError("sample data shifts cannot have more employees than capacity", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
