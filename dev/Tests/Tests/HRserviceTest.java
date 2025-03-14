package Tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
// import static DTOs.Errors;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import ServiceLayer.ServiceFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import BuisnessLayer.Controller.*;
import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.*;
import DTOs.Errors;
import DTOs.Response;
import DTOs.Role;
import DTOs.ShiftTime;
import ServiceLayer.HRservice;
import ServiceLayer.employeeService;
// import jdk.vm.ci.meta.Local;

public class HRserviceTest {

    private ServiceFactory serviceFactory;
    private EmployeeController empC;
    private HRManager hrManager;
    private String HRPassword;
    private List<Employee> employees;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Response R(String res) {
        return gson.fromJson(res, Response.class);
    }
    // ==================================================== init funcs :

    public void initHRManager() {
        // ID: 1, Name: bhaa, Phone: 777-55555, Salary: 5000, Bonus: -1, Roles: Cashier,
        // Start Date: June 1, 2024, End Date: June 1, 2025
        // ID: 2, Name: ghanem, Phone: 777-55556, Salary: 5000, Bonus: -1, Roles: Group
        // Manager, Start Date: June 1, 2024, End Date: January 1, 2025
        // ID: 3, Name: rami, Phone: 777-55557, Salary: 5000, Bonus: -1, Roles:
        // Storekeeper, Start Date: June 1, 2024, End Date: June 1, 2025
        // ID: 4, Name: dahleh, Phone: 777-55558, Salary: 5000, Bonus: -1, Roles: Shift
        // Manager, Start Date: June 1, 2024, End Date: July 1, 2024
        // ID: 5, Name: alaoi, Phone: 777-55559, Salary: 5000, Bonus: -1, Roles: Store
        // Manager, Start Date: June 1, 2024, End Date: June 1, 2025
        // Store : Name : lee sheeba , address beer sheeba , Num : 1.
        // hrManager = new HRManager(HRPassword);
        // empC = new EmployeeController(hrManager);
        // employees = new ArrayList<>();
        // employees = getEmployees();
        // empC.setStoreForTest("lee sheeba", "Beer Sheba", null, employees, 1, 0, 6);
        serviceFactory = new ServiceFactory(false);
        serviceFactory.addHRmanager("123");
        serviceFactory.addStore(1, "bhaa store", "arraba");
        List<Employee> emps = getEmployees();
        for (Employee employee : emps) {
            Role[] roles = new Role[employee.getRoles().size()];
            for(int i=0;i<roles.length;i++) roles[i]=employee.getRoles().get(i);
            System.out.println(employee.getName());
            serviceFactory.addEmployee(employee.getID(), employee.getName(), employee.getBankAccount(), employee.getMounthSalary(),roles,employee.getStartDate()   , employee.getEndDate(), employee.getStoreNum());
        }
    }

    private List<Employee> getEmployees() {
        int storeNum = 1;
        // one Employee
        LocalDate start1 = LocalDate.of(2024, 6, 1);
        LocalDate end1 = LocalDate.of(2025, 6, 1);
        List<Role> roles1 = new ArrayList<>();
        roles1.add(Role.Cashier);
        Employee em1 = new Employee("1", "bhaa", "777-55555", 5000, -1, roles1, start1, end1, storeNum);

        // one Employee
        LocalDate start2 = LocalDate.of(2024, 6, 1);
        LocalDate end2 = LocalDate.of(2025, 1, 1);
        List<Role> roles2 = new ArrayList<>();
        roles2.add(Role.Cashier);
        Employee em2 = new Employee("2", "ghanem", "777-55556", 5000, -1, roles2, start2, end2, storeNum);

        // one Employee
        LocalDate start3 = LocalDate.of(2024, 6, 1);
        LocalDate end3 = LocalDate.of(2025, 6, 1);
        List<Role> roles3 = new ArrayList<>();
        roles3.add(Role.Storekeeper);
        Employee em3 = new Employee("3", "rami", "777-55557", 5000, -1, roles3, start3, end3, storeNum);

        // one Employee
        LocalDate start4 = LocalDate.of(2024, 6, 1);
        LocalDate end4 = LocalDate.of(2024, 7, 1);
        List<Role> roles4 = new ArrayList<>();
        roles4.add(Role.ShiftManager);
        Employee em4 = new Employee("4", "dahleh", "777-55558", 5000, -1, roles4, start4, end4, storeNum);

        // one Employee
        LocalDate start5 = LocalDate.of(2024, 6, 1);
        LocalDate end5 = LocalDate.of(2025, 6, 1);
        List<Role> roles5 = new ArrayList<>();
        roles5.add(Role.StoreManager);
        Employee em5 = new Employee("5", "alaoi", "777-55559", 5000, -1, roles5, start5, end5, storeNum);

        List<Employee> employees = new ArrayList<>();
        employees.add(em1);
        employees.add(em2);
        employees.add(em3);
        employees.add(em4);
        employees.add(em5);

        return employees;
    }


    // ====================================================== set shift
    private void beforeTest() {
        initHRManager();
        serviceFactory.startAddingConstrainsForNextWeek(1);
        serviceFactory.addConstrains("2", "1234567", LocalDate.of(2024,7,10), ShiftTime.Night);
    }

    @Test
    public void setShiftTest2_1() {

        beforeTest();
        // String JSONresponse = ;
        Response res = R(serviceFactory.setShift(LocalDate.of(2024, 7, 10), ShiftTime.Day, "2", Role.Cashier));
        assertEquals(false, res.isErrorOccured());
        assertEquals(null,res.getErrorMessage());
        assertEquals("shift successfully added to the list", res.getReturnValue());

    }

    @Test
    public void setShiftTest2_2() {
        beforeTest();
        Response res = R(serviceFactory.setShift(LocalDate.of(2024, 7, 10), ShiftTime.Night, "2", Role.Cashier));
        assertEquals(true, res.isErrorOccured());
        assertEquals(null,res.getReturnValue());
        assertEquals(Errors.cantSetShiftDueConstrains, res.getErrorMessage());
    }

    // ========================================= add Employee

    @Test
    public void addEmployeeTest2_1Posetive() {
        initHRManager();
        Response res = R(serviceFactory.addEmployee("6", "abo elmori", "777-5555", 5000,
                new Role[] { Role.Cashier, Role.ShiftManager }, LocalDate.now(), LocalDate.of(2025, 6, 1), 1));
        assertEquals(false, res.isErrorOccured());
        assertEquals("employee added successfully", res.getReturnValue());
        //checkIfContainsEmployee("6", "abo elmori", true, true);
    }

    private void checkIfContainsEmployee(String EmpId, String name, Boolean mustExist, Boolean UniqeID) {
        if (mustExist) {
            Employee empToTest = empC.getEmployee(EmpId);
            assertEquals(mustExist || UniqeID, empToTest.getID().equals(EmpId));
        }
        Store store = empC.getStore(1);
        HashMap<String, Employee> storeWorkers = store.getWorkers();
        assertEquals(mustExist, storeWorkers.get(EmpId) != null);
        LocalDate nextWeek = LocalDate.now().plusDays(1);
        Shift shift = store.getShift(nextWeek, ShiftTime.Day);
        assertEquals(mustExist, shift.empCanWork(EmpId));
    }

    @Test
    public void addEmployeeTest2_2Negative() {
        // groub manageer cant start
        initHRManager();
        Response resOfGroubManagerError = R(serviceFactory.addEmployee("7", "omar", "666-5555", 5000,
                new Role[] { Role.GroubManager }, LocalDate.now(), LocalDate.of(2025, 6, 1), 1));
        assertEquals(true, resOfGroubManagerError.isErrorOccured());
        assertEquals(resOfGroubManagerError.getErrorMessage(), Errors.cantSetGroubManagerToNewEmployee);
        //checkIfContainsEmployee("7", HRPassword, false, false);

        Response resOfTheSameId = R(serviceFactory.addEmployee("5", "abo elmori", "777-5555", 5000,
                new Role[] { Role.Cashier, Role.ShiftManager }, LocalDate.now(), LocalDate.of(2025, 6, 1), 1));
        assertEquals(true, resOfTheSameId.isErrorOccured());
        assertEquals(resOfTheSameId.getErrorMessage(), Errors.EmployeeAlreadyExistInStore);
        //checkIfContainsEmployee("7", HRPassword, false, true);
    }

    // ==================================== update salary

    @Test
    public void updateSalaryTest3Positive() {
        initHRManager();
        Response res = R(serviceFactory.updateSalary("1", 7000));
        assertEquals("salary updated successfully", res.getReturnValue());
        
    }

    @Test
    public void updateSalaryTest3Neg() {
        initHRManager();
        Response res = R(serviceFactory.updateSalary("1", -7000));
        // Employee emp = empC.getEmployee("1");
        assertEquals(null, res.getReturnValue());
        // assertEquals(5000, emp.getMounthSalary());
        assertEquals(Errors.cantUpdateNegativeSalary, res.getErrorMessage());
    }

    // =================================== get constrains

    @Test
    public void getConstrainsTest_pos(){
        initHRManager();
        serviceFactory.startAddingConstrainsForNextWeek(1);
        Response res = R(serviceFactory.getConstrains("1"));
        assertEquals(false, res.isErrorOccured());
    }

    @Test
    public void getConstrainsTest_neg(){
        initHRManager();
        Response res = R(serviceFactory.getConstrains("1"));
        assertEquals(true, res.isErrorOccured());
    }


    //===================================================================================================
    //===================================================================================================
    //====================
    //         new TESTS :
    //====================



    private ServiceFactory service ;
    @Test
    public void persistEmployee(){
        addEmployee();
        service = new ServiceFactory(false);
        service.addStore(0, "bhaa store", "arraba");
        service.addEmployee("212", "bhaa", "123", 0,new Role[]{Role.Cashier} , LocalDate.now(), LocalDate.now().plusYears(1), 0);
        
        service = null;
        service = new ServiceFactory(true);
        Response returnedProfRespString =R( service.getEmployeeProf("212"));
        String returnedProf =(String) returnedProfRespString.getReturnValue();
        String str = "Employee name:" + "bhaa" +"\n";
        str += "Employee ID:" + "212" +"\n";
        str += "Employee Roles:" + "[Cashier]" + "\n";
        str += "Employee Salary:" + "0" + "\n";
        str += "Store Number:" + 0 + "\n";
        str += "Bank Account:" + 123 + "\n";
        assertEquals(str, returnedProf);
    }

    private void addEmployee(){
        service = new ServiceFactory(false);
        service.addStore(0, "bhaa store", "arraba");
        service.addEmployee("212", "bhaa", "123", 0,new Role[]{Role.Cashier} , LocalDate.now(), LocalDate.now().plusYears(1), 0);
        
    }

    @Test 
    public void persistPassword(){
        addEmployee();

        service.setPassword("212", "123");
        service = null;
        service=new ServiceFactory(true);
        Response res =  R(service.loginForEmployee("212", "123"));
        assertEquals((String)res.getReturnValue(), "Successfully logged in");

    }

    @Test
    public void persistConstrains(){
        addEmployee();
        service.startAddingConstrainsForNextWeek(0);
        service.addConstrains("212", "123", LocalDate.now().plusDays(7), ShiftTime.Day);
        service = null;
        service = new ServiceFactory(true);
        Response res =R(service.setShift(LocalDate.now().plusDays(7), ShiftTime.Day, "212", Role.Cashier));
        assertEquals(res.getErrorMessage(), "cant set shift due to constrains");
    }

    @Test
    public void check_if_driver_with_storekeeper(){
        addEmployee();
        service.addEmployee("1", "driver", "123", 1, new Role[]{Role.Driver}, LocalDate.now(), LocalDate.now().plusYears(1), 0);
        service.addEmployee("2", "shift manager", "123", 123, new Role[]{Role.ShiftManager}, LocalDate.now() , LocalDate.now().plusYears(7), 0);
        service.startAddingConstrainsForNextWeek(0);
        for(int i=7;i<14;i++){
            service.setShift(LocalDate.of(2024,7,i), ShiftTime.Day, "2",Role.ShiftManager );
            service.setShift(LocalDate.of(2024,7,i), ShiftTime.Night, "2",Role.ShiftManager );
        }
        
        service.setShift(LocalDate.of(2024,7,8), ShiftTime.Night, "1",Role.Driver );
        Response res = R(service.scheduleReadyToPublish(0));
        assertEquals(res.isErrorOccured(), true);
        assertEquals(res.getErrorMessage(), "shift "+LocalDate.of(2024,7,8)+" "+ShiftTime.Night.toString()+"has a driver without storeKeeper!");
    }




}
