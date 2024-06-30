package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Errors;
import DTOs.Role;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Shift {

    private LocalDate day;
    private ShiftTime shiftTime;
    private HashMap<Role, List<Employee>> workersInShift;
    private HashMap<Role, List<Employee>> workersAvailable;
    private int minEmployeeNumberInShift;
    private int id;

    public Shift(List<Employee> employees, LocalDate dateForDayToAdd, ShiftTime day2, int minEmployees, int id) {
        this.day = dateForDayToAdd;
        this.shiftTime = day2;
        this.id = id;
        this.minEmployeeNumberInShift = minEmployees;
        this.workersInShift = new HashMap<>();
        this.workersAvailable = new HashMap<>();
        workersAvailable.put(Role.ShiftManager, new ArrayList<>());
        workersAvailable.put(Role.Cashier, new ArrayList<>());
        workersAvailable.put(Role.GroubManager, new ArrayList<>());
        workersAvailable.put(Role.Storekeeper, new ArrayList<>());
        workersAvailable.put(Role.StoreManager, new ArrayList<>());
        workersInShift.put(Role.ShiftManager, new ArrayList<>());
        workersInShift.put(Role.Cashier, new ArrayList<>());
        workersInShift.put(Role.GroubManager, new ArrayList<>());
        workersInShift.put(Role.Storekeeper, new ArrayList<>());
        workersInShift.put(Role.StoreManager, new ArrayList<>());
        for (Employee employee : employees) {
            for (Role role : employee.getRoles()) {
                workersAvailable.get(role).add(employee);
            }
        }

    }
    public void submit(){
        int sum = 0;
        if( workersInShift.get(Role.ShiftManager).isEmpty() ){
            throw new IllegalArgumentException("there is no shift manager in shift " + day + ", " + shiftTime );
        }
        Iterator<Role> iterator = workersInShift.keySet().iterator();
        while(iterator.hasNext()){
            sum += workersInShift.get(iterator.next()).size();
        }
        if(sum < minEmployeeNumberInShift){
            throw new IllegalArgumentException("cannot submit shift with fewer than minimum employees");
        }
    }
    public HashMap<Role, List<Employee>> getWorkersInShift() {
        return workersInShift;
    }
    public boolean empCanWork(String empId) {
        return getEmployeeFromgivenDict(empId, workersAvailable)!=null;
    }

    public void removeFromAvailableWorkers(String empId) {

        Employee employee = getEmployeeFromgivenDict(empId, workersAvailable);
        if ( employee == null ){
            throw new IllegalArgumentException("constrains already added!");
        }
        removeEmployeeFromGivenDict(empId,workersAvailable);
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
        Employee emp = getEmployeeFromgivenDict(empId, workersAvailable);
        while (iter.hasNext()){
            Role r = iter.next();
            for(int i =0;i<toRemoveFrom.get(r).size();i++){
                if(toRemoveFrom.get(r).get(i).getID().equals(empId)) {
                    toRemoveFrom.get(r).remove(emp);
                }
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

    public void addRoleForEmployee(Employee employee, Role role){
        workersAvailable.get(role).add(employee);
    }

    //
    public void setEmployeeToShift(String empId, Role role) {
        Employee empl = getEmployeeFromgivenDict(empId, workersAvailable);
        if(empl == null) throw new IllegalArgumentException(Errors.cantSetShiftDueConstrains);
        if(!empl.containsRole(role)) throw new IllegalArgumentException("employee doesn't have role " + role);
        if(workersAvailable.get(role).contains(empl)){
            removeEmployeeFromGivenDict(empId, workersAvailable);
            workersInShift.get(role).add(empl);
        }else {
            throw new IllegalArgumentException(Errors.cantSetShiftDueConstrains);
        }
    }

    public String toStringForSchedule(){

        String str = day.toString() +" "+ shiftTime.toString() +" : ";
        for ( Role r : workersInShift.keySet() ){
            List<Employee> employees = workersInShift.get(r);
            for (int i = 0; i < employees.size() ; i++){
                str += employees.get(i).getName() + " as " + employees.get(i).getRoles().toString() + ",";
            }

        }
        return str + "\n";
    }

    public String toString(){
        return day.toString() +" "+ shiftTime.toString() + '\n';
    }
}
