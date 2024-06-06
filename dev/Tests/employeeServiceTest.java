package Tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import BuisnessLayer.Controller.EmployeeController;
import BuisnessLayer.Workers.Employee;
import BuisnessLayer.Workers.HRManager;
import DTOs.Errors;
import DTOs.Response;
import DTOs.Role;
import ServiceLayer.HRservice;
import ServiceLayer.employeeService;

public class employeeServiceTest {
    private HRservice hrs;
    private EmployeeController empC;
    private HRManager hrManager;
    private String HRPassword;
    private List<Employee> employees;
    private employeeService emS;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Response R(String res) {
        return gson.fromJson(res, Response.class);
    }
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
        emS = new employeeService(empC);
        employees = new ArrayList<>();
        empC.setStoreForTest("lee sheeba", "Beer Sheba", null, employees, 1, 0, 1);
        employees = getEmployees();
        for (Employee employee : employees) {
            empC.addEmployee(employee);
            emS.setPassword(employee.getID(), "12345678");
        }

        hrs = new HRservice(empC);
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
        roles2.add(Role.GroubManager);
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
        // employees.add(em2);
        employees.add(em3);
        employees.add(em4);
        employees.add(em5);

        return employees;
    }

    // =================================login
    @Test
    public void loginTestPos() {
        initHRManager();
        Response res = R(emS.loginForEmployee("1", "12345678"));
        assertEquals("Successfully logged in", res.getReturnValue());
    }

    @Test
    public void loginTestNeg() {
        initHRManager();
        Response res = R(emS.loginForEmployee("1", "1234567"));
        assertEquals(Errors.InvalidPassword, res.getErrorMessage());
    }

    // ===================add connstrains test:

    private void beforeConstrainsTest(int deadLine){
        initHRManager();
        List<Employee> emps= new ArrayList<>();
        LocalDate start5 = LocalDate.of(2024, 6, 1);
        LocalDate end5 = LocalDate.of(2025, 6, 1);
        List<Role> roles5 = new ArrayList<>();
        roles5.add(Role.StoreManager);
        empC.setStoreForTest("hi", "saa", null, employees, 2, 1, deadLine);
        empC.addEmployee(new Employee("1","alaoi", "777-55559", 5000, -1, roles5, start5, end5, 2));
    }

    @Test
    public void setConstrainsAfterDeadlineTest(){
        emS.addConstrains("1", "12345678", , null)
    }


    // =================================== add Role :

    @Test
    public void addRoleTest1_pos() {
        initHRManager();
        Response res = R(emS.addRole("1", "12345678", Role.Storekeeper));
        assertEquals("Successfully add role", res.getReturnValue());
        assertArrayEquals(new Role[] { Role.Cashier, Role.Storekeeper }, empC.getEmployeeRoles("1"));

        Response res2 = R(emS.addRole("1", "12345678", Role.GroubManager));
        assertEquals("Successfully add role", res2.getReturnValue());
        assertArrayEquals(new Role[] { Role.Cashier, Role.Storekeeper, Role.GroubManager }, empC.getEmployeeRoles("1"));

    }

    // =================================== remove Role
    @Test
    public void removeRoleTest2_pos() {
        initHRManager();
        Response resOfAdd = R(emS.addRole("1", "12345678", Role.Storekeeper));
        Response res = R(emS.removeRole("1", "12345678", Role.Cashier));
        assertEquals("Successfully remove role", res.getReturnValue());
        assertArrayEquals(new Role[] { Role.Storekeeper }, empC.getEmployeeRoles("1"));
    }

    @Test
    public void removeRoleTest2_neg() {
        initHRManager();
        Response resOfLastRole = R(emS.removeRole("3", "12345678", Role.Storekeeper));
        assertEquals(true, resOfLastRole.isErrorOccured());
        assertEquals(Errors.cantRemoveTheLastRole, resOfLastRole.getErrorMessage());
        assertArrayEquals(new Role[] { Role.Storekeeper }, empC.getEmployeeRoles("3"));
    }

}
