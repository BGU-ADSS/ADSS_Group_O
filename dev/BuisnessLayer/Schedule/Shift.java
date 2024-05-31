package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

public class Shift {

    private LocalDate day;
    private ShiftTime shiftTime;
    private Dictionary<Role, Employee> workersInShift;
    private Dictionary<Role, List<Employee>> workersAvailable;
    private Dictionary<Role, Integer> constrainsForRole;


    public Object empCanWork(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'empCanWork'");
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

        Employee employee = null;
        Iterator<Role> iter = workersAvailable.keys().asIterator();
        while (iter.hasNext()){
            for (Employee emp : workersAvailable.get(iter.next())){
                if ( emp.getID().equals(empId)){
                    employee = emp;
                }
            }
        }
        return employee;
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
}
