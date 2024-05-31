package BuisnessLayer.Controller;

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.*;

public class EmployeeController {

    private Dictionary<Integer, Store> stores;
    private Dictionary<String, Integer> employeesStore;
    private HRManager hrManager;


    public EmployeeController(HRManager hrManager) {

        this.hrManager = hrManager;
        stores = new Hashtable<Integer, Store>();
        employeesStore = new Hashtable<String, Integer>();

    }

    public void setStoreForTest(String storeName, String address, Employee manager, List<Employee> employees, int storeNum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStoreForTest'");
    }

    public boolean login(String password){
        return hrManager.login(password);
    }

    //must add start constrains function that takes a date.

    public void addConstrains(String empId, LocalDate day, ShiftTime shift){

    if ( employeesStore.get(empId) == null ){
        throw new IllegalArgumentException("Employee does not exist");
    }

    int storeNum = employeesStore.get(empId);
    Store store = stores.get(storeNum);
    store.addConstrains(empId,day,shift);
    }

    public void addEmployee(Employee employee){

        if( employeesStore.get(employee.getID()) != null ){
            throw new IllegalArgumentException("Employee already exists");
        }
        employeesStore.put(employee.getID(), employee.getStoreNum());
        stores.get(employee.getStoreNum()).addEmployee(employee);
    }

    //public void addBreakDay(){}

    public List<Shift[]> getShiftHistory(LocalDate fromDate, int StoreNum){

        if ( stores.get(StoreNum) == null ){
            throw new IllegalArgumentException("Store does not exist");
        }
        Store store = stores.get(StoreNum);
        return store.getShiftsHistory(fromDate);
    }

    public boolean terminateJobReq(String empId, LocalDate finishDate){

        if ( employeesStore.get(empId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }
        Store store = stores.get(employeesStore.get(empId));
        return store.terminateJobReq(empId,finishDate);

    }

    public boolean removeRoleFromEmployee(String empId, Role role){

        if ( employeesStore.get(empId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }

        Store store = stores.get(employeesStore.get(empId));
        return store.removeRoleFromEmployee(empId,role);
    }

    public List<Shift[]> getWeekSchedule(){
        return null;
    }











    //--------------------------------------------------------------------------------------------------//
    public Employee getEmployee(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmployee'");
    }

    public Dictionary<Integer, Store> getStores() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStores'");
    }

    public Store getStore(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStore'");
    }

    public Object[] getEmployeeRoles(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmployeeRoles'");
    }


    public Boolean containsEmp(String empId,String empName){
        Boolean containsInEmpDict =  employeesStore.get(empId)!=null;
        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        Boolean contatinsInStore = store.containsEmp(empId);
        return containsInEmpDict && contatinsInStore;
    }


    public Dictionary<LocalDate,Dictionary<ShiftTime,Boolean>> getAvaliableDaysForEmployee(String empId){
        int storeNumOfEmployee = employeesStore.get(empId);
        Store storeOfEmployee = stores.get(storeNumOfEmployee);
        return storeOfEmployee.getAvailableDaysForEmployee(empId);
    }



}
