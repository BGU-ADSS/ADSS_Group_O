package BuisnessLayer.Controller;

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.ShiftTime;

public class EmployeeController {

    private Dictionary<Integer, Store> stores;
    private Dictionary<String, Integer> employeeStore;
    private HRManager hrManager;


    public EmployeeController(HRManager hrManager) {

        this.hrManager = hrManager;
        stores = new Hashtable<Integer, Store>();
        employeeStore = new Hashtable<String, Integer>();

    }

    public void setStoreForTest(String storeName, String address, Employee manager, List<Employee> employees, int storeNum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStoreForTest'");
    }

    public boolean login(String password){

        return hrManager.login(password);

    }

    //must add start constrains function that takes a date.

    public void addConstrains(String empId, Date day, ShiftTime shift){

    if ( employeeStore.get(empId) == null ){
        throw new IllegalArgumentException("Employee does not exist");
    }

    int storeNum = employeeStore.get(empId);
    Store store = stores.get(storeNum);
    store.addConstrains()

    }
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
        Boolean containsInEmpDict =  employeeStore.get(empId)!=null;
        int storeNum = employeeStore.get(empId);
        Store store = stores.get(storeNum);
        Boolean contatinsInStore = store.containsEmp(empId);
        return containsInEmpDict && contatinsInStore;
    }


    public Dictionary<LocalDate,Dictionary<ShiftTime,Boolean>> getAvaliableDaysForEmployee(String empId){
        int storeNumOfEmployee = employeeStore.get(empId);
        Store storeOfEmployee = stores.get(storeNumOfEmployee);
        return storeOfEmployee.getAvaliableDayesForEmployee(empId);
    }



}
