package BuisnessLayer.Controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.*;
import PresentationLayer.Logs;

import com.google.gson.GsonBuilder;

public class EmployeeController {

    private HashMap<Integer,Store> stores;
    private HashMap<String, Integer> employeesStore;
    private HRManager hrManager;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    public EmployeeController(File configFile, File dataFile) {
        int deadLineConstrains=0;
        int minEmployees=0;
        List<LocalDate> breakDays = new ArrayList<>();
        this.employeesStore=new HashMap<>();
        try {
            List<String> configLines = Files.readAllLines(configFile.toPath());
            for (String line : configLines) {
                if (line.startsWith("#")) {
                    int index = getSplitIndex(line);
                    String key = line.substring(1, index);
                    String value = line.substring(index + 1);
                    switch (key) {
                        case "deadLineConstrains":
                            deadLineConstrains = Integer.parseInt(value);
                            break;
                        case "breakDayes":
                            breakDays.add(LocalDate.parse(value, formatter));
                            break;
                        case "minEmployees":
                            minEmployees = Integer.parseInt(value);
                        default:
                            break;
                    }

                }
            }
            stores = new HashMap();
            List<String> dataLines = Files.readAllLines(dataFile.toPath());
            HashMap<Integer,List<Employee>> employeesToStores = new HashMap<>();
            for(String line:dataLines){
                if(line.startsWith("#store")){
                    String storeJson = line.substring(7);
                    StoreToSend toSend = gson.fromJson(storeJson, StoreToSend.class);
                    Store toAdd = new Store(toSend.name, toSend.address, toSend.storeNumber,employeesToStores.get(toSend.storeNumber) , deadLineConstrains, minEmployees, breakDays);
                    stores.put(toSend.storeNumber,toAdd);
                }else if(line.startsWith("#hr")){
                    this.hrManager = gson.fromJson(line.substring(4), HRManager.class);
                }else if(line.startsWith("#Employee")){
                    Employee em = gson.fromJson(line.substring(10), Employee.class);
                    if(employeesToStores.get(em.getStoreNum())==null) {
                        employeesToStores.put(em.getStoreNum(), new ArrayList<>());
                    }
                    employeesToStores.get(em.getStoreNum()).add(em);
                    Logs.debug("size of store "+em.getStoreNum()+" , is "+employeesToStores.get(em.getStoreNum()).size());
                    employeesStore.put(em.getID(),em.getStoreNum());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }


    private int getSplitIndex(String line) {
        int index = 0;
        while(line.charAt(index)!='-'){
            index++;
        }    
        return index;
    }


    public EmployeeController(HRManager hrManager) {

        this.hrManager = hrManager;
        stores = new HashMap<Integer, Store>();
        employeesStore = new HashMap<String, Integer>();

    }

    public void setStoreForTest(String storeName, String address, Employee manager, List<Employee> employees, int storeNum,int minEmps,int deadLine) {
        Store store = new Store(storeName, address, storeNum, employees,deadLine,minEmps  ,new ArrayList<>() );
        stores.put(storeNum,store);
        for(Employee employee:employees ){
            if(storeNum==employee.getStoreNum()) employeesStore.put(employee.getID(), storeNum);
        }
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
        Logs.debug( employeesStore.get(employee.getID())+" the store returned for this employee "+employeesStore.size());
        if( employeesStore.get(employee.getID()) != null ){
            throw new IllegalArgumentException("Employee ID already exists");
        }
        stores.get(employee.getStoreNum()).addEmployee(employee);
        employeesStore.put(employee.getID(), employee.getStoreNum());
        Logs.debug( employeesStore.get(employee.getID())+" the store returned for this employee "+employeesStore.size());

    }

    public String getShiftHistory(LocalDate fromDate, int StoreNum){

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
        return store!=null?store.getEmployee(empId):null;
    }

    public HashMap<Integer, Store> getStores() {
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
        Logs.debug(empId+" employee id to add in");
        store.addRoleForEmployee(empId,role);
        return true;
    }


    public void setBankAccountForEmployee(String empId,String newBankAccount){
        Store store = getStoreForEmployee(empId);
        store.setBankAccountForEmployee(empId,newBankAccount);
    }

    public void updateSalary(String emplId, int monthSalary) {
        if(monthSalary<0) throw new IllegalArgumentException(Errors.cantUpdateNegativeSalary);
        if ( employeesStore.get(emplId) == null ){
            throw new IllegalArgumentException("Employee does not exist");
        }
        Store store = stores.get(employeesStore.get(emplId));
        store.updateSalary(emplId,monthSalary);
    }


   

}
