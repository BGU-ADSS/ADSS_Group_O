package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Errors;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

public class Schedule {

    private Dictionary<LocalDate, Shift[]> dayShifts;
    private LocalDate deadline;
    private LocalDate currentWeek;
    private LocalDate nextWeek;



    public void addConstrains(String empId, LocalDate day, ShiftTime shiftTime) {

        if (dayShifts.get(day) == null) {
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

        Iterator<LocalDate> iter = dayShifts.keys().asIterator();
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
        Iterator<LocalDate> iter = dayShifts.keys().asIterator();
        while (iter.hasNext()) {
            LocalDate day = iter.next();
            if (fromDate.isBefore(day) || fromDate.isEqual(day)) {
                shifts.add(dayShifts.get(day));
            }
        }
        return shifts;
    }

    public void startAddingConstrainsForNextWeek() {

        this.currentWeek = this.nextWeek;
        this.nextWeek = nextWeek.plusWeeks(1);
        LocalDate d = nextWeek;
        while ( d.isBefore(nextWeek.plusWeeks(1)) ){
            dayShifts.put(d, new Shift[2]);
            d = d.plusDays(1);
        }
    }

    public List<Shift[]> getCurrentWeekSchedule() {

        List<Shift[]> currentWeekShifts = new ArrayList<>();
        LocalDate curr = currentWeek;
        while (curr.isBefore(nextWeek)) {

            currentWeekShifts.add(dayShifts.get(curr));
            curr = curr.plusDays(1);

        }
        return currentWeekShifts;
    }

    public List<Shift[]> getNextWeekSchedule() {

        if ( LocalDate.now().isBefore(deadline) ){
            throw new IllegalArgumentException("next week schedule is not ready yet!");
        }
        List<Shift[]> nextWeekShifts = new ArrayList<>();
        LocalDate curr = nextWeek;

        while (curr.isBefore(nextWeek.plusWeeks(1)) ){
            nextWeekShifts.add(dayShifts.get(curr));
            curr = curr.plusDays(1);
        }

        return nextWeekShifts;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkRelatedDateShift'");
    }

    //we have to employee to the avaliable , but we have to know when can employee start
	public void addRoleForEmployee(String empId, Role role) {
		
	}

}
