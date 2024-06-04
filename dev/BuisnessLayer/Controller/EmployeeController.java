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

    public boolean loginForHR(String password){
        return hrManager.login(password);
    }

    public boolean loginForEmployee(String empId,String password){

        if ( employeesStore.get(empId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }
        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.loginForEmployee(empId,password);
    }
    public void setPassword(String password, String empId){

        if ( employeesStore.get(empId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        store.setPassword(password,empId);
    }
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

    public String getCurrentWeekSchedule(String empId){

        if ( employeesStore.get(empId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.getCurrentWeekSchedule();
    }

    public String getNextWeekSchedule(String empId){

        if ( employeesStore.get(empId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }

        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        return store.getNextWeekSchedule();
    }

    public void startAddingConstrainsForNextWeek(int storeNum){

        if ( stores.get(storeNum) == null ){
            throw new IllegalArgumentException("Store does not exist");
        }
        Store store = stores.get(storeNum);
        store.startAddingConstrainsForNextWeek();

    }

    //--------------------------------------------------------------------------------------------------//
    public Employee getEmployee(String empId) {
        Store store = getStoreForEmployee(empId);
        return store.getEmployee(empId);
    }

    public Dictionary<Integer, Store> getStores() {
        return stores;
    }

    public Store getStore(int numOfStore) {
        return stores.get(numOfStore);
    }

    public Object[] getEmployeeRoles(String empId) {
        Store store = getStoreForEmployee(empId);
        return store.getEmployeeRoles(empId);

    }

    private Store getStoreForEmployee(String empId){
        if(!containsEmp(empId)) throw new IllegalArgumentException(Errors.EmployeeNotFound);
        int storeNumber = employeesStore.get(empId);
        return stores.get(storeNumber);
    }

    public Boolean containsEmp(String empId){
        Boolean containsInEmpDict =  employeesStore.get(empId)!=null;
        int storeNum = employeesStore.get(empId);
        Store store = stores.get(storeNum);
        Boolean contatinsInStore = store.containsEmp(empId);
        return containsInEmpDict && contatinsInStore;
    }


    public List<Shift> getAvailableDaysForEmployee(String empId){
        int storeNumOfEmployee = employeesStore.get(empId);
        Store storeOfEmployee = stores.get(storeNumOfEmployee);
        return storeOfEmployee.getAvailableDaysForEmployee(empId);
    }

    public boolean removeEmployee(String empId){
        Store store = getStoreForEmployee(empId);
        store.removeEmployee(empId);
        employeesStore.remove(empId);
        return true;
    }


    //to check if we have to add hour
//    public void addConstrainsDeadLine(LocalDate date){
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getEmployeeRoles'");
//    }

    //
    //addBreakDay()
    //

    public void setEmployeeInShift(LocalDate date,ShiftTime shiftTime , String empId,Role role){
        Store store = getStoreForEmployee(empId);
        store.setEmployeeInShift(date,shiftTime,empId,role);
    }


    public boolean addRoleForEmployee(String empId,Role role){
        Store store = getStoreForEmployee(empId);
        store.addRoleForEmployee(empId,role);
        return true;
    }


    public void setBankAccountForEmployee(String empId,String newBankAccount){
        Store store = getStoreForEmployee(empId);
        store.setBankAccountForEmployee(empId,newBankAccount);
    }

    public void updateSalary(String emplId, int monthSalary) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSalary'");
    }

}
