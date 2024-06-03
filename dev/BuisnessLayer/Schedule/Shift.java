package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Errors;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Shift {

    private LocalDate day;
    private ShiftTime shiftTime;
    private HashMap<Role, List<Employee>> workersInShift;
    private HashMap<Role, List<Employee>> workersAvailable;
    private HashMap<Role, Integer> constrainsForRole;


    

    public boolean empCanWork(String empId) {
        return getEmployeeFromAvailable(empId)!=null;
    }

    public void removeFromAvailableWorkers(String empId) {

        Employee employee = getEmployeeFromAvailable(empId);
        if ( employee == null ){
            throw new IllegalArgumentException("constrains already added!");
        }

        if (checkIfCanAddConstrain(employee)){
            for ( Role r : employee.getRoles()){
                constrainsForRole.put( r ,constrainsForRole.get(r) - 1 );
            }
        }else {
            throw new IllegalArgumentException("cannot add constrain!, minimum number of Role has reach!");
        }
    }

    public Employee getEmployeeFromAvailable(String empId){
        return getEmployeeFromgivenDict(empId, workersAvailable);
    }

    public boolean checkIfCanAddConstrain(Employee employee) {

        for ( Role r : employee.getRoles() ){
            if ( constrainsForRole.get(r) <= 1 ){
                return false;
            }
        }
        return true;
    }

    public void addEmployee(Employee employee) {

        for( Role r : employee.getRoles() ){
            workersAvailable.get(r).add(employee);
            constrainsForRole.put(r,constrainsForRole.get(r) + 1);
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

    public void setEmployeeToShift(String empId, Role role) {
        Employee empl = getEmployeeFromAvailable(empId);
        if(empl.containsRole(role) && workersAvailable.get(role).contains(empl)){
            removeEmployeeFromGivenDict(empId, workersAvailable);
            workersInShift.get(role).add(empl);
        }else throw new IllegalArgumentException(Errors.EmployeeHasNoGivenRole);
    }
}
