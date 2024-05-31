package BuisnessLayer.Controller;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Store {

    private String name;
    private String address;
    private Employee storeManager;
    private Schedule schedule;
    private Dictionary<String, Employee> employees;
    private int storeNumber;

    public void addConstrains(String empId, LocalDate day, ShiftTime shiftTime) {

        if( employees.get(empId) == null ) {
            throw  new IllegalArgumentException("Employee does not exist");
        }
        schedule.addConstrains(empId,day,shiftTime);
    }

    public Dictionary<LocalDate, Dictionary<ShiftTime, Boolean>> getAvailableDaysForEmployee(String empId) {
        return null;
    }

    public Boolean containsEmp(String empId) {
        return null;
    }

    public void addEmployee(Employee employee) {

        if( employees.get(employee.getID()) != null ){
            throw  new IllegalArgumentException("Employee already exists");
        }
        employees.put(employee.getID(),employee);
        schedule.addEmployee(employee);
    }

    public Shift getShift(LocalDate nextWeek, ShiftTime shiftTime) {
        return null;
    }

    public Dictionary<String, Employee> getWorkers() {
        return null;
    }

    public List<Shift[]> getShiftsHistory(LocalDate fromDate) {

        return schedule.getShiftsHistory(fromDate);
    }

    public boolean terminateJobReq(String empId, LocalDate finishDate){

        if ( employees.get(empId) == null ){
            throw  new IllegalArgumentException("Employee does not exist in this store");
        }
        return LocalDate.now().isBefore(finishDate.minusMonths(1)) ||
                LocalDate.now().isEqual(finishDate.minusMonths(1));
    }


    public boolean removeRoleFromEmployee(String empId, Role role) {

        if ( employees.get(empId) == null ){
            throw  new IllegalArgumentException("Employee does not exist in this store");
        }
        return employees.get(empId).removeRole(role);
    }

    public void startAddingConstrainsForNextWeek() {
        schedule.startAddingConstrainsForNextWeek();
    }

    public List<Shift[]> getCurrentWeekSchedule() {

        return schedule.getCurrentWeekSchedule();
    }

    public List<Shift[]> getNextWeekSchedule() {

        return schedule.getNextWeekSchedule();
    }
}
