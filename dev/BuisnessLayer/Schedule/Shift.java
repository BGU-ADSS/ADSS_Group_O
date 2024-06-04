package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Errors;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Shift {

    private LocalDate day;
    private ShiftTime shiftTime;
    private HashMap<Role, List<Employee>> workersInShift;
    private HashMap<Role, List<Employee>> workersAvailable;
    private int minEmployeeNumberInShift;

    public void submit(){
        int sum = 0;
        Iterator<Role> iterator = workersInShift.keySet().iterator();
        while(iterator.hasNext()){
            sum += workersInShift.get(iterator.next()).size();
        }
        if(sum < minEmployeeNumberInShift){
            throw new IllegalArgumentException("cannot submit shift with fewer than minimum employees");
        }
    }

    public Shift(List<Employee> employees, LocalDate dateForDayToAdd, ShiftTime day2, int minEmployees) {
        //TODO Auto-generated constructor stub
    }
    public HashMap<Role, List<Employee>> getWorkersInShift() {
        return workersInShift;
    }
    public boolean empCanWork(String empId) {
        return getEmployeeFromAvailable(empId)!=null;
    }

    public void removeFromAvailableWorkers(String empId) {

        Employee employee = getEmployeeFromAvailable(empId);
        if ( employee == null ){
            throw new IllegalArgumentException("constrains already added!");
        }
        removeEmployeeFromGivenDict(empId,workersAvailable);
    }

    public Employee getEmployeeFromAvailable(String empId){
        return getEmployeeFromgivenDict(empId, workersAvailable);
    }


    public void addEmployee(Employee employee) {

        for( Role r : employee.getRoles() ){
            workersAvailable.get(r).add(employee);
        }
    }

    private Employee getEmployeeFromgivenDict(String empId,HashMap<Role,List<Employee>> toSearchIn){
        Employee employee = null;
        Iterator<Role> iter = toSearchIn.keySet().iterator();
        while (iter.hasNext()){
            for (Employee emp : toSearchIn.get(iter.next())){
                if ( emp.getID().equals(empId)){
                    employee = emp;
                }
            }
        }
        return employee;
    }

    private void removeEmployeeFromGivenDict(String empId ,HashMap<Role,List<Employee>> toRemoveFrom ){
        Iterator<Role> iter = toRemoveFrom.keySet().iterator();
        while (iter.hasNext()){
            List<Employee> employees = toRemoveFrom.get(iter.next());
            for(int i =0;i<employees.size();i++){
                if(employees.get(i).getID().equals(empId)) employees.remove(i);
            }
        }
    }


    public void removeEmployee(String empId){
        removeEmployeeFromGivenDict(empId, workersAvailable);
        removeEmployeeFromGivenDict(empId, workersInShift);
    }

    public ShiftTime getShiftTime() {
        return shiftTime;
    }


    //
    public void setEmployeeToShift(String empId, Role role) {
        Employee empl = getEmployeeFromAvailable(empId);
        if(empl.containsRole(role) && workersAvailable.get(role).contains(empl)){
            removeEmployeeFromGivenDict(empId, workersAvailable);
            workersInShift.get(role).add(empl);
        }else throw new IllegalArgumentException(Errors.EmployeeHasNoGivenRole);
    }

    public String toStringForSchedule(){

        String str = day.toString() +" "+ shiftTime.toString() +" : ";
        for ( Role r : workersInShift.keySet() ){
            List<Employee> employees = workersInShift.get(r);
            for (int i = 0; i < employees.size() ; i++){
                str += employees.get(i).getName() + " ,";
            }

        }
        return str + "\n";
    }

    public String toString(){
        return day.toString() +" "+ shiftTime.toString() + "\n";
    }
}
