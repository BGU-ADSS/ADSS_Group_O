package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Schedule {

    private HashMap<LocalDate, Shift[]> dayShifts;
    private int deadline;
    private LocalDate currentWeek;
    private LocalDate nextWeek;
<<<<<<< HEAD
    private List<LocalDate> breakDates = new ArrayList<>();
    private int minEmployees;
=======
    private List<LocalDate> breakDates;
>>>>>>> 7ba42efbc4cfad5e89f7629d00c3332f23fed3cd

    public Schedule(int deadline, List<Employee> employees, int minEmployees, List<LocalDate> breakDays) {
        dayShifts = new HashMap<>();
        this.deadline = deadline;
        breakDates=breakDays;
        this.currentWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.nextWeek = currentWeek.plusDays(7);
        for (int day = 0; day < 14; day++) {
            LocalDate dateForDayToAdd = currentWeek.plusDays(day);
            if (!breakDates.contains(dateForDayToAdd)) {
                Shift[] shiftsInDayToAdd = new Shift[2];
                shiftsInDayToAdd[0] = new Shift(employees, dateForDayToAdd, ShiftTime.Day, minEmployees);
                shiftsInDayToAdd[1] = new Shift(employees, dateForDayToAdd, ShiftTime.Nigth, minEmployees);
                dayShifts.put(dateForDayToAdd, shiftsInDayToAdd);
            }
        }
        this.minEmployees = minEmployees;

    }

    public void addConstrains(String empId, LocalDate day, ShiftTime shiftTime) {

        checkRelatedDateShift(day);
        boolean valid = LocalDate.now().isBefore(currentWeek.plusDays(deadline));
        if (dayShifts.get(day) == null || !valid) {
            throw new IllegalArgumentException("not suitable date!");
        }
        Shift[] shifts = dayShifts.get(day);
        if (shiftTime == ShiftTime.Day) {
            shifts[0].removeFromAvailableWorkers(empId);
        } else {
            shifts[1].removeFromAvailableWorkers(empId);
        }
    }

    public void addEmployee(Employee employee) {

        Iterator<LocalDate> iter = dayShifts.keySet().iterator();
        while (iter.hasNext()) {
            LocalDate day = iter.next();
            Shift[] shifts = dayShifts.get(day);
            shifts[0].addEmployee(employee);
            shifts[1].addEmployee(employee);
            dayShifts.put(day, shifts);
        }
    }

    public List<Shift[]> getShiftsHistory(LocalDate fromDate) {

        List<Shift[]> shifts = new ArrayList<Shift[]>();
        Iterator<LocalDate> iter = dayShifts.keySet().iterator();
        while (iter.hasNext()) {
            LocalDate day = iter.next();
            if (fromDate.isBefore(day) || fromDate.isEqual(day)) {
                shifts.add(dayShifts.get(day));
            }
        }
        return shifts;
    }

    public void startAddingConstrainsForNextWeek(HashMap<String,Employee> employees) {

        this.currentWeek = this.nextWeek;
        this.nextWeek = nextWeek.plusWeeks(1);
        deadline += 7;
        LocalDate d = nextWeek;
        while (d.isBefore(nextWeek.plusWeeks(1))) {
            Shift[] shift = new Shift[2];
            shift[0] = new Shift(getEmployees(employees),d,ShiftTime.Day,minEmployees);
            shift[0] = new Shift(getEmployees(employees),d,ShiftTime.Day,minEmployees);
            dayShifts.put(d, new Shift[2]);
            d = d.plusDays(1);
        }
    }

    private List<Employee> getEmployees(HashMap<String, Employee> employees) {
        List<Employee> toRet = new ArrayList<>();
        Iterator<Employee> iter = employees.values().iterator();
        while(iter.hasNext()){
            toRet.add(iter.next());
        }
        return toRet;
    }

    public String getCurrentWeekSchedule() {

        List<Shift[]> currentWeekShifts = new ArrayList<>();
        LocalDate curr = currentWeek;
        while (curr.isBefore(nextWeek)) {

            currentWeekShifts.add(dayShifts.get(curr));
            curr = curr.plusDays(1);

        }
        return weekShiftsToString(currentWeekShifts);
    }

    public String getNextWeekSchedule() {

        if (LocalDate.now().isBefore(currentWeek.plusDays(deadline))) {
            throw new IllegalArgumentException("next week schedule is not ready yet!");
        }
        List<Shift[]> nextWeekShifts = new ArrayList<>();
        LocalDate curr = nextWeek;

        while (curr.isBefore(nextWeek.plusWeeks(1))) {
            nextWeekShifts.add(dayShifts.get(curr));
            curr = curr.plusDays(1);
        }

        return weekShiftsToString(nextWeekShifts);
    }

    public List<Shift> getAvaliableDaysForEmployee(String empId) {
        List<Shift> shiftsInCurrentWeak = getShiftsInWeak(currentWeek);
        List<Shift> shiftsWithAvaliableEmployee = new ArrayList<>();
        for (Shift shift : shiftsInCurrentWeak) {
            if (shift.empCanWork(empId))
                shiftsWithAvaliableEmployee.add(shift);
        }
        return shiftsWithAvaliableEmployee;
    }

    private List<Shift> getShiftsInWeak(LocalDate sunday) {
        List<Shift> toRet = new ArrayList<>();

        for (int day = 0; day < 7; day++) {
            LocalDate date = sunday.plusDays(day);
            Shift[] shiftsInDate = dayShifts.get(date);
            for (int index = 0; index < shiftsInDate.length; index++) {
                toRet.add(shiftsInDate[index]);
            }
        }

        return toRet;
    }

    public void removeEmployee(String empId) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        for (int days = 0; days < 14; days++) {
            LocalDate nextDay = tomorrow.plusDays(days);
            if (dayShifts.get(nextDay) != null) {
                for (Shift shift : dayShifts.get(nextDay))
                    shift.removeEmployee(empId);
            }
        }
    }

    public void setEmployeeInShift(LocalDate date, ShiftTime shiftTime, String empId, Role role) {
        checkRelatedDateShift(date);
        Shift[] shiftsInDate = dayShifts.get(empId);
        for (Shift shift : shiftsInDate) {
            if (shift.getShiftTime() == shiftTime) {
                shift.setEmployeeToShift(empId, role);
            }
        }
    }

    // this function must check if there are shift in the given date
    private void checkRelatedDateShift(LocalDate date) {
        if( breakDates.contains(date) ){
            throw new IllegalArgumentException("cannot set shift in break dates!");
        }
    }

    // we have to employee to the avaliable , but we have to know when can employee
    // start
    public void addRoleForEmployee(String empId, Role role) {

    }

    public String weekShiftsToString(List<Shift[]> weekShifts) {

        String schedule = "~WEEK SCHEDULE~\n";

        for (int i = 0; i < weekShifts.size(); i++) {

            schedule += weekShifts.get(i)[0].toStringForSchedule();
            schedule += weekShifts.get(i)[1].toStringForSchedule();

        }
        return schedule + "\n";
    }
}
