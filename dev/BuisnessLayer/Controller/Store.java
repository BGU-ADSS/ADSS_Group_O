package BuisnessLayer.Controller;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import DTOs.Errors;
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
        isEmployeeExist(empId);
        schedule.addConstrains(empId, day, shiftTime);
    }

    public List<Shift> getAvailableDaysForEmployee(String empId) {
        return schedule.getAvaliableDaysForEmployee(empId);
    }

    public Boolean containsEmp(String empId) {
        return null;
    }

    public void addEmployee(Employee employee) {

        if (employees.get(employee.getID()) != null) {
            throw new IllegalArgumentException(Errors.EmployeeAlreadyExistInStore);
        }
        employees.put(employee.getID(), employee);
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

    public boolean terminateJobReq(String empId, LocalDate finishDate) {
        isEmployeeExist(empId);
        return LocalDate.now().isBefore(finishDate.minusMonths(1)) ||
                LocalDate.now().isEqual(finishDate.minusMonths(1));
    }

    public boolean removeRoleFromEmployee(String empId, Role role) {
        isEmployeeExist(empId);
        return employees.get(empId).removeRole(role);
    }

    public void removeEmployee(String empId) {
        isEmployeeExist(empId);
        employees.remove(empId);
        schedule.removeEmployee(empId);
    }

    private void isEmployeeExist(String empId) {
        if (employees.get(empId) == null) {
            throw new IllegalArgumentException(Errors.EmployeeNotFoundInStore);
        }
    }

    public void setEmployeeInShift(LocalDate date, ShiftTime shiftTime, String empId,Role role) {
        isEmployeeExist(empId);
        schedule.setEmployeeInShift(date,shiftTime,empId,role);
    }

    public void addRoleForEmployee(String empId, Role role) {
        isEmployeeExist(empId);
        Employee employee = employees.get(role);
        employee.addRole(role);
        schedule.addRoleForEmployee(empId,role);
    }

    public Employee getEmployee(String empId) {
        isEmployeeExist(empId);
        return employees.get(empId);
    }

    public Role[] getEmployeeRoles(String empId) {
        isEmployeeExist(empId);
        return (Role[]) employees.get(empId).getRoles().toArray();
    }

    public void setBankAccountForEmployee(String empId, String newBankAccount) {
        isEmployeeExist(empId);
        employees.get(empId).setBankAccount(newBankAccount);
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
