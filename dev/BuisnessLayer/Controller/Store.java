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
import java.util.HashMap;
import java.util.List;

public class Store {

    private String name;
    private String address;
    private Schedule schedule;
    private HashMap<String, Employee> employees;
    private int storeNumber;

    public Store(String name,String address,int StoreNum,List<Employee> employees,int deadLine,int minEmps, List<LocalDate> breakDayes){
        schedule = new Schedule(deadLine,employees,minEmps,breakDayes);
        this.address=address;
        this.name = name;
        this.storeNumber= StoreNum;
        this.employees = new HashMap<>(); 
        for(Employee employeeToAdd:employees){
            this.employees.put(employeeToAdd.getID(),employeeToAdd);
        }
    }

    public void addConstrains(String empId, LocalDate day, ShiftTime shiftTime) {
        isEmployeeExist(empId);
        schedule.addConstrains(empId, day, shiftTime);
    }

    public List<Shift> getAvailableDaysForEmployee(String empId) {
        return schedule.getAvaliableDaysForEmployee(empId);
    }

    public Boolean containsEmp(String empId) {
        isEmployeeExist(empId);
        return true;
    }

    public void scheduleReadyToPublish(){
        schedule.scheduleReadyToPublish();
    }
    public void setPassword(String password, String empId){
        isEmployeeExist(empId);
        employees.get(empId).setPassword(password);
    }

    public void addEmployee(Employee employee) {

        if (employees.get(employee.getID()) != null) {
            throw new IllegalArgumentException(Errors.EmployeeAlreadyExistInStore);
        }
        schedule.addEmployee(employee);
        employees.put(employee.getID(), employee);
    }

    public Shift getShift(LocalDate nextWeek, ShiftTime shiftTime) {
        int index= shiftTime==ShiftTime.Day?0:1;
        return schedule.getShift(nextWeek,index) ;
    }

    public HashMap<String, Employee> getWorkers() {
        return employees;
    }

    public String getShiftsHistory(LocalDate fromDate) {
        return schedule.getShiftsHistory(fromDate);
    }

    public boolean terminateJobReq(String empId, LocalDate finishDate) {
        isEmployeeExist(empId);
        if( finishDate.isAfter(LocalDate.now().plusMonths(1)) ||
        finishDate.isEqual(LocalDate.now().plusMonths(1))) {
            return true;
        }
        throw new IllegalArgumentException("finish date must be after one month or more!");
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

    public String getEmployeeProf(String empId) {
        isEmployeeExist(empId);
        return employees.get(empId).getProf();
    }

    public String getCurrentSchedule(){
        return schedule.getCurrentSchedule();
    }
    public void setEmployeeInShift(LocalDate date, ShiftTime shiftTime, String empId,Role role) {
        isEmployeeExist(empId);
        schedule.setEmployeeInShift(date,shiftTime,empId,role);
    }

    public void addRoleForEmployee(String empId, Role role) {
        isEmployeeExist(empId);
        Employee employee = employees.get(empId);
        employee.addRole(role);
        schedule.addRoleForEmployee(employee,role);
    }

    public Employee getEmployee(String empId) {
        isEmployeeExist(empId);
        return employees.get(empId);
    }

    public Role[] getEmployeeRoles(String empId) {
        isEmployeeExist(empId);
        List<Role> rolesForEmployee = employees.get(empId).getRoles();
        Role [] rolesToRet = new Role[rolesForEmployee.size()];
        for(int index=0;index<rolesForEmployee.size();index++) rolesToRet[index]= rolesForEmployee.get(index);
        return rolesToRet;
    }

    public void setBankAccountForEmployee(String empId, String newBankAccount) {
        isEmployeeExist(empId);
        employees.get(empId).setBankAccount(newBankAccount);
    }

    public void startAddingConstrainsForNextWeek() {
        schedule.startAddingConstrainsForNextWeek(employees);
    }

    public String getCurrentWeekSchedule() {

        return schedule.getCurrentWeekSchedule();
    }

    public String getNextWeekSchedule() {

        return schedule.getNextWeekSchedule() ;
    }

    public LocalDate getNextWeek(){
        return schedule.getNextWeek();
    }

    public boolean loginForEmployee(String empId, String password) {
        return employees.get(empId).login(password);
    }

    public void updateSalary(String emplId, int monthSalary) {
        employees.get(emplId).updateSalary(monthSalary);
    }
}
