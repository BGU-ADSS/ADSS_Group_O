package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Errors;
import DTOs.Role;
import DTOs.ShiftTime;
import DataAccessLayer.DBControllers.DBEmployeeController;
import DataAccessLayer.DTOs.AvaliableWorkerInShiftDTO;
import PresentationLayer.Logs;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Schedule {

    private HashMap<LocalDate, Shift[]> dayShifts;
    private int deadline;
    private LocalDate currentWeek;
    private LocalDate nextWeek;
    private List<LocalDate> breakDates = new ArrayList<>();
    private int minEmployees;
    private boolean isReadyToPublish;
    private int idCounter;
    private DBEmployeeController dbEmployeeController = new DBEmployeeController();

    public Schedule(int deadline, List<Employee> employees, int minEmployees, List<LocalDate> breakDays) {
        this.isReadyToPublish = false;
        dayShifts = new HashMap<>();
        this.deadline = deadline;
        breakDates=breakDays;
        this.idCounter = 0;
        this.currentWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.nextWeek = currentWeek.plusWeeks(1);
        for (int day = 0; day < 7; day++) {
            LocalDate dateForDayToAdd = currentWeek.plusDays(day);
            if (!breakDates.contains(dateForDayToAdd)) {
                Shift[] shiftsInDayToAdd = new Shift[2];
                shiftsInDayToAdd[0] = new Shift(employees, dateForDayToAdd, ShiftTime.Day, minEmployees, idCounter);
                idCounter++;
                shiftsInDayToAdd[1] = new Shift(employees, dateForDayToAdd, ShiftTime.Night, minEmployees, idCounter);
                idCounter++;
                dayShifts.put(dateForDayToAdd, shiftsInDayToAdd);
            }
        }
        this.minEmployees = minEmployees;

    }

    public Schedule(){}
    public void loadData( HashMap<LocalDate, Shift[]> dayShifts, LocalDate currentWeek, LocalDate nextWeek, List<LocalDate> breakDates, int minEmployees, boolean isReadyToPublish , int idCounter,int deadLine) {
        this.dayShifts = dayShifts;
        this.currentWeek = currentWeek;
        this.nextWeek = nextWeek;
        this.breakDates = breakDates;
        this.minEmployees = minEmployees;
        this.isReadyToPublish = isReadyToPublish;
        this.idCounter = idCounter;
        this.deadline = deadLine;
    }
    public int addConstrains(String empId, LocalDate day, ShiftTime shiftTime) {

        checkRelatedDateShift(day);
        if (dayShifts.get(day) == null) {
            throw new IllegalArgumentException("the HR manager does not permit to add constrain yet!");
        }
        if( LocalDate.now().isAfter(currentWeek.plusDays(deadline)) ) {
            Logs.debug(currentWeek.plusDays(deadline).toString()+" <=");
            Logs.debug(deadline+"");
            Logs.debug(currentWeek.toString());
            throw new IllegalArgumentException("deadline have been reached! cannot add constrain");
        }
        int id;
        Shift[] shifts = dayShifts.get(day);
        if (shiftTime == ShiftTime.Day) {
            Logs.debug("hi from day and the shift time is "+shifts[0].getShiftTime().toString()+" and the id is "+shifts[0].getId());
            id = shifts[0].removeFromAvailableWorkers(empId);
        } else {

            id = shifts[1].removeFromAvailableWorkers(empId);
        }
        return id;
    }

    public void addEmployee(Employee employee, int store) {
        if(employee.getRoles().contains(Role.GroubManager)) {
            throw new IllegalArgumentException(Errors.cantSetGroubManagerToNewEmployee);
        }
        Iterator<LocalDate> iter = dayShifts.keySet().iterator();
        while (iter.hasNext()) {
            LocalDate day = iter.next();
            Shift[] shifts = dayShifts.get(day);
            shifts[0].addEmployee(employee);
            dbEmployeeController.insertWorkerAvailableInShift(new AvaliableWorkerInShiftDTO(employee.getID(),shifts[0].getId(),store));
            shifts[1].addEmployee(employee);
            dbEmployeeController.insertWorkerAvailableInShift(new AvaliableWorkerInShiftDTO(employee.getID(),shifts[1].getId(),store));
            dayShifts.put(day, shifts);
        }
    }

    public String getShiftsHistory(LocalDate fromDate) {

        if( LocalDate.now().isBefore(fromDate) ) {
            throw new IllegalArgumentException("not suitable date!");
        }
        List<Shift[]> shifts = new ArrayList<Shift[]>();
        Iterator<LocalDate> iter = dayShifts.keySet().iterator();
        while (iter.hasNext()) {
            LocalDate day = iter.next();
            if ((fromDate.isBefore(day) || fromDate.isEqual(day)) && day.isBefore(LocalDate.now())) {
                shifts.add(dayShifts.get(day));
            }
        }
        return shiftsHistoryToString(shifts);
    }

    public void startAddingConstrainsForNextWeek(HashMap<String,Employee> employees) {

        LocalDate d = nextWeek;
        while (d.isBefore(nextWeek.plusWeeks(1))) {
            Shift[] shift = new Shift[2];
            shift[0] = new Shift(getEmployees(employees),d,ShiftTime.Day,minEmployees, idCounter);
            idCounter++;
            shift[1] = new Shift(getEmployees(employees),d,ShiftTime.Night,minEmployees, idCounter);
            idCounter++;
            dayShifts.put(d, shift);
            d = d.plusDays(1);
        }
        isReadyToPublish = false;
    }

    private List<Employee> getEmployees(HashMap<String, Employee> employees) {
        List<Employee> toRet = new ArrayList<>();
        Iterator<Employee> iter = employees.values().iterator();
        while(iter.hasNext()){
            Employee employee= iter.next();
            if(employee.canWorkInShift(currentWeek)) toRet.add(employee);
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

        if( !isReadyToPublish ) {
            return "next week schedule is not ready to publish!";
        }
        if (LocalDate.now().isBefore(currentWeek.plusDays(deadline))) {
            throw new IllegalArgumentException("next week schedule is not ready yet!");
        }
        List<Shift[]> nextWeekShifts = new ArrayList<>();
        LocalDate curr = nextWeek;

        while (curr.isBefore(nextWeek.plusWeeks(1))) {
            if (dayShifts.get(curr) != null) {
                nextWeekShifts.add(dayShifts.get(curr));
                curr = curr.plusDays(1);
            }
        }

        return weekShiftsToString(nextWeekShifts);
    }

    public List<Shift> getAvaliableDaysForEmployee(String empId) {
        List<Shift> shiftsInCurrentWeak = getShiftsInWeak(nextWeek);
        List<Shift> shiftsWithAvaliableEmployee = new ArrayList<>();
        for (Shift shift : shiftsInCurrentWeak) {
            if (shift.empCanWork(empId))
                shiftsWithAvaliableEmployee.add(shift);
        }
        return shiftsWithAvaliableEmployee;
    }

    private List<Shift> getShiftsInWeak(LocalDate sunday) {
        if( dayShifts.get(sunday) == null ) {
            throw new IllegalArgumentException("you must permit adding constrains for next week first! by opt 7");
        }
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

    public int setEmployeeInShift(LocalDate date, ShiftTime shiftTime, String empId, Role role) {
        checkRelatedDateShift(date);
        int id;
        if ( shiftTime == ShiftTime.Day) {
            id = dayShifts.get(date)[0].setEmployeeToShift(empId,role);
        }
        else {
            id = dayShifts.get(date)[1].setEmployeeToShift(empId,role);
        }
        return id;
    }

    // this function must check if there are shift in the given date
    private void checkRelatedDateShift(LocalDate date) {

        if( dayShifts.get(date) == null ){
            throw new IllegalArgumentException("the hr must start the week shift first!");
        }
        if( breakDates.contains(date) ){
            throw new IllegalArgumentException("cannot set shift/add constrain in break dates!");
        }
    }

    public void scheduleReadyToPublish(){

        Iterator<LocalDate> iter = dayShifts.keySet().iterator();
        while ( iter.hasNext() ) {
            LocalDate nextDay = iter.next();
            Logs.debug("we got this "+nextDay+" : "+nextDay.isAfter(nextWeek)+" , next weak is "+nextWeek);
            if ( nextDay.isEqual(nextWeek) || nextDay.isAfter(nextWeek)) {
                dayShifts.get(nextDay)[0].submit();
                dayShifts.get(nextDay)[1].submit();
            }
        }
        this.isReadyToPublish = true;
    }

    public String weekShiftsToString(List<Shift[]> weekShifts) {

        String schedule = "~WEEK SCHEDULE~\n";

        for (int i = 0; i < weekShifts.size(); i++) {

            schedule += weekShifts.get(i)[0].toStringForSchedule();
            schedule += weekShifts.get(i)[1].toStringForSchedule();

        }
        return schedule + "\n";
    }

    public String shiftsHistoryToString(List<Shift[]> shifts) {

        String history = "~SHIFTS HISTORY~\n";
        for (int i = 0; i < shifts.size(); i++) {
            history += shifts.get(i)[0].toStringForSchedule();
            history += shifts.get(i)[1].toStringForSchedule();
        }
        return history + "\n";
    }

    public String getCurrentSchedule(){

        if( dayShifts.get(nextWeek) == null ){
            throw new IllegalArgumentException("you must start the week shift first!");
        }
        String schedule = "~CURRENT SCHEDULE~\n";
        List<Shift[]> nextWeekShifts = new ArrayList<>();
        LocalDate curr = nextWeek;
        while (curr.isBefore(nextWeek.plusWeeks(1))) {
            nextWeekShifts.add(dayShifts.get(curr));
            curr = curr.plusDays(1);
        }

        schedule += weekShiftsToString(nextWeekShifts);
        return schedule;

    }
    public Shift getShift(LocalDate nextWeek2, int index) {
        return dayShifts.get(nextWeek2)[index];
    }

    public void addRoleForEmployee(Employee emp, Role role) {

        Iterator<LocalDate> iter = dayShifts.keySet().iterator();
        while ( iter.hasNext() ) {

            LocalDate nextDay = iter.next();
            if (nextDay.isEqual(nextWeek) || nextDay.isAfter(nextWeek)) {
                dayShifts.get(nextDay)[0].addRoleForEmployee(emp,role);
                dayShifts.get(nextDay)[1].addRoleForEmployee(emp,role);
            }
        }
    }

    public boolean doesItReadyToPublish(){
        return isReadyToPublish;
    }

    public LocalDate getNextWeek() {
        return nextWeek;
    }

    public LocalDate getCurrentWeek() {
        return currentWeek;
    }

    public HashMap<LocalDate, Shift[]> getDayShifts(){
        return dayShifts;
    }

    public boolean workerIsStoreKeeperToday(String empId){
        Logs.debug("we are here in schedule");
        Shift[] todayShifts = dayShifts.get(LocalDate.now());

        if(todayShifts==null || todayShifts[0]==null) return false;
        Logs.debug("there are shifts today ");
        return todayShifts[0].employeeIsStoreKeeperInShift(empId);
    }
}
