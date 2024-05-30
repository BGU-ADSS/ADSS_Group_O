package Tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
// import static DTOs.Errors;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import org.junit.Test;

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

    private HRservice hrs;
    private EmployeeController empC;
    private HRManager hrManager;
    private String HRPassword;
    private List<Employee> employees;
    private employeeService emS; 
    // ==================================================== init funcs :

    private void initHRManager() {
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
        hrManager = new HRManager(HRPassword);
        empC = new EmployeeController(hrManager);
        employees = getEmployees();
        emS = new employeeService(empC);
        empC.setStoreForTest("lee sheeba", "Beer Sheba", employees.remove(4), employees, 0);
        for (Employee employee : employees) {
            emS.setPassword(employee.getID(), "12345678");
        }
    }

    private List<Employee> getEmployees() {
        int storeNum = 1;
        // one Employee
        LocalDate start1 = LocalDate.of(2024, 6, 1);
        LocalDate end1 = LocalDate.of(2025, 6, 1);
        List<Role> roles1 = new ArrayList<>();
        roles1.add(Role.Cashier);
        Employee em1 = new Employee("1", "bhaa", "777-55555", 5000, -1, roles1, start1, end1,storeNum);

        // one Employee
        LocalDate start2 = LocalDate.of(2024, 6, 1);
        LocalDate end2 = LocalDate.of(2025, 1, 1);
        List<Role> roles2 = new ArrayList<>();
        roles2.add(Role.GroubManager);
        Employee em2 = new Employee("2", "ghanem", "777-55556", 5000, -1, roles2, start2, end2,storeNum);

        // one Employee
        LocalDate start3 = LocalDate.of(2024, 6, 1);
        LocalDate end3 = LocalDate.of(2025, 6, 1);
        List<Role> roles3 = new ArrayList<>();
        roles3.add(Role.Storekeeper);
        Employee em3 = new Employee("3", "rami", "777-55557", 5000, -1, roles3, start3, end3,storeNum);

        // one Employee
        LocalDate start4 = LocalDate.of(2024, 6, 1);
        LocalDate end4 = LocalDate.of(2024, 7, 1);
        List<Role> roles4 = new ArrayList<>();
        roles4.add(Role.ShiftManager);
        Employee em4 = new Employee("4", "dahleh", "777-55558", 5000, -1, roles4, start4, end4,storeNum);

        // one Employee
        LocalDate start5 = LocalDate.of(2024, 6, 1);
        LocalDate end5 = LocalDate.of(2025, 6, 1);
        List<Role> roles5 = new ArrayList<>();
        roles5.add(Role.StoreManager);
        Employee em5 = new Employee("5", "alaoi", "777-55559", 5000, -1, roles5, start5, end5,storeNum);

        List<Employee> employees = new ArrayList<>();
        employees.add(em1);
        employees.add(em2);
        employees.add(em3);
        employees.add(em4);
        employees.add(em5);

        return employees;
    }

    // ==================================================== getConstrains
    // to check if constrains returned correctly : +
    // the test will add constrain for employee and check if the HR manager can see
    // it
    @Test
    public void getConstrainsPosTest1() {

    }

    
    
    //====================================================== set shift
    private void beforeTest2_1(){
        emS.addConstrains("1", "1234567", LocalDate.of(2024, 7, 15), ShiftTime.Day);
    }

    @Test
    public void setShiftTest2_1(){
        
        beforeTest2_1();
        //String JSONresponse = ;
        Response res = hrs.setShift(LocalDate.of(2024, 7, 15), ShiftTime.Day, "1");
        assertEquals(true, res.isErrorOccured());
        assertEquals(Errors.cantSetShiftDueConstrains, res.getErrorMessage());

    }

   

    @Test
    public void setShiftTest2_2(){
        Response res = hrs.setShift(LocalDate.of(2024, 7, 15), ShiftTime.Day, "2");
        assertEquals(false, res.isErrorOccured());
        assertEquals(true,res.getReturnValue() );
    }
    


    //========================================= add Employee
    
    @Test
    public void addEmployeeTest2_1Posetive(){
        Response res = hrs.addEmployee("6","abo elmori","777-5555",5000,new Role[]{Role.Cashier,Role.ShiftManager},LocalDate.now(),LocalDate.of(2025, 6, 1),1);
        assertEquals(true, res.getReturnValue());
        checkIfContainsEmployee("6", "abo elmori",true,true );
    }

    private void checkIfContainsEmployee(String EmpId,String name,Boolean mustExist,Boolean UniqeID){
        Employee empToTest = empC.getEmployee(EmpId);
        assertEquals(mustExist||UniqeID, empToTest.getID().equals(EmpId));
        Store store = empC.getStore(1);
        Dictionary<String,Employee> storeWorkers = store.getWorkers();
        assertEquals(mustExist, storeWorkers.get(EmpId)!=null);
        LocalDate nextWeek = LocalDate.now().plusDays(7);
        Shift shift = store.getShift(nextWeek,ShiftTime.Day);
        assertEquals(mustExist, shift.empCanWork(EmpId));
    }


    @Test
    public void addEmployeeTest2_2Negative(){
        Response resOfGroubManagerError = hrs.addEmployee("7","omar" , "666-5555", 5000, new Role[]{Role.GroubManager}, LocalDate.now(), LocalDate.of(2025,6, 1),1);
        assertEquals(true,resOfGroubManagerError.isErrorOccured());
        assertEquals(true,Errors.cantSetGroubManagerToNewEmployee);
        checkIfContainsEmployee("7", HRPassword, false,false);

        Response resOfTheSameId = hrs.addEmployee("5","abo elmori","777-5555",5000,new Role[]{Role.Cashier,Role.ShiftManager},LocalDate.now(),LocalDate.of(2025, 6, 1), 1);
        assertEquals(true,resOfTheSameId.isErrorOccured());
        assertEquals(true,Errors.cantSetGroubManagerToNewEmployee);
        checkIfContainsEmployee("7", HRPassword, false,true);
    }

    //==================================== update salary

    @Test
    public void updateSalaryTest3Positive(){
        Response res= hrs.updateSalary("1", 7000);
        Employee emp = empC.getEmployee("1");
        assertEquals(true, res.getReturnValue());
        assertEquals(7000, emp.getMounthSalary());
    }

    

}
