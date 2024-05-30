package BuisnessLayer.Controller;

import java.util.Date;
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


}
