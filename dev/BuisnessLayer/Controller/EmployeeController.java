package BuisnessLayer.Controller;

import java.util.Dictionary;
import java.util.List;

import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;

public class EmployeeController {

    private Dictionary<Integer, Store> stores;
    private Dictionary<String, Integer> employeeStore;
    private HRManager hrManager;


    public EmployeeController(HRManager hrManager) {
        //TODO Auto-generated constructor stub
    }

    public void setStoreForTest(String storeName, String address, Employee manager, List<Employee> employees, int storeNum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStoreForTest'");
    }



}
