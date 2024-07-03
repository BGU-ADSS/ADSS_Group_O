package ServiceLayer;

import BuisnessLayer.Controller.EmployeeController;
import DTOs.Response;
import DTOs.Role;
import DTOs.ShiftTime;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ServiceFactory {

    private employeeService employeeService;
    private HRservice hrService;
    private EmployeeController employeeController;

    public ServiceFactory(boolean withData) {
        if (withData) {
            employeeController = new EmployeeController(new File("dev\\DTOs\\config.txt"),
                    new File("dev\\DTOs\\Data.txt"));
        } else {
            employeeController = new EmployeeController(new File("dev\\DTOs\\config.txt"));
        }
        employeeService = new employeeService(employeeController);
        hrService = new HRservice(employeeController);
    }

    public ServiceFactory(EmployeeController controller) {
        employeeService = new employeeService(controller);
        hrService = new HRservice(controller);
    }

    public String addHRmanager(String password) {
        try {
            employeeController.addHRmanager(password);
//            List<String> lines = Files.readAllLines(Paths.get("dev\\DTOs\\config.txt"));
//            int hrPassLine = -1;
//            for (int i = 0; i < lines.size(); i++)
//                if (lines.get(i).startsWith("#hrPassword"))
//                    hrPassLine = i;
//            if(hrPassLine==-1) hrPassLine= lines.size();
//            lines.set(hrPassLine, "#hrPassword-"+password);
//            Files.write(Paths.get("dev\\DTOs\\config.txt"), lines);

            return "hr added successfuly";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String addStore(int storeNumber, String storeName, String StoreAddress) {
        try {
            employeeController.addStore(storeNumber, storeName, StoreAddress);
            return "store added successfuly";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String loginForHR(String password) {
        return hrService.loginForHR(password);
    }

    public String getConstrains(String Id) {
        return hrService.getConstrains(Id);
    }

    public String setShift(LocalDate localDate, ShiftTime shiftTime, String emplId, Role role) {
        return hrService.setShift(localDate, shiftTime, emplId, role);
    }

    public String addEmployee(String emplId, String emplName, String bankAccount, int mounthSalary, Role[] roles,
            LocalDate startDate, LocalDate endDate, int storeNum) {
        return hrService.addEmployee(emplId, emplName, bankAccount, mounthSalary, roles, startDate, endDate, storeNum);
    }

    public String removeEmployee(String emplId) {
        return hrService.removeEmployee(emplId);
    }

    public String getShiftHistory(LocalDate day, int storeNumber) {
        return hrService.getShiftHistory(day, storeNumber);
    }

    public String updateSalary(String emplId, int monthSalary) {
        return hrService.updateSalary(emplId, monthSalary);
    }

    public String startAddingConstrainsForNextWeek(int storeNum) {
        return hrService.startAddingConstrainsForNextWeek(storeNum);
    }

    public String scheduleReadyToPublish(int storeNumber) {
        return hrService.scheduleReadyToPublish(storeNumber);
    }

    public String getEmployeeProf(String employeeId) {
        return hrService.getEmployeeProf(employeeId);
    }

    public String getCurrentSchedule(int storeNumber) {
        return hrService.getCurrentSchedule(storeNumber);
    }

    public String setPassword(String empId, String password) {
        return employeeService.setPassword(empId, password);

    }

    public String addConstrains(String Id, String password, LocalDate localDate, ShiftTime shiftTime) {
        return employeeService.addConstrains(Id, password, localDate, shiftTime);
    }

    public String addRole(String Id, String password, Role role) {
        return employeeService.addRole(Id, password, role);
    }

    public String removeRole(String Id, String password, Role role) {
        return employeeService.removeRole(Id, password, role);
    }

    public String terminateJobReq(String Id, String password, LocalDate day) {
        return employeeService.terminateJobReq(Id, password, day);
    }

    public String getWeekShiftForAll(String empId) {
        return employeeService.getWeekShiftForAll(empId);
    }

    public String setBankAccount(String Id, String password, String newAccount) {
        return employeeService.setBankAccount(Id, password, newAccount);
    }

    public String loginForEmployee(String Id, String password) {
        return employeeService.loginForEmployee(Id, password);
    }

    public String getProfile(String id) {
        return employeeService.getProfile(id);
    }
}
