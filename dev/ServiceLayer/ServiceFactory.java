package ServiceLayer;

import BuisnessLayer.Controller.EmployeeController;
import DTOs.Response;
import DTOs.Role;
import DTOs.ShiftTime;

import java.io.File;
import java.time.LocalDate;

public class ServiceFactory {

    private employeeService employeeService;
    private HRservice hrService;
    private EmployeeController employeeController;

    public ServiceFactory(){
        employeeController = new EmployeeController(new File("C:\\Users\\WINDOWS 10 PRO\\OneDrive\\Desktop\\ADSS\\ADSS_Group_O\\dev\\DTOs\\config.txt"),
                new File("C:\\Users\\WINDOWS 10 PRO\\OneDrive\\Desktop\\ADSS\\ADSS_Group_O\\dev\\DTOs\\Data.txt"));
        employeeService = new employeeService(employeeController);
        hrService = new HRservice(employeeController);
    }

    public String loginForHR(String password) {
        return hrService.loginForHR(password);
    }

    public String getConstrains(String Id)  {
        return hrService.getConstrains(Id);
    }

    public String setShift(LocalDate localDate , ShiftTime shiftTime, String emplId, Role role)  {
        return hrService.setShift(localDate,shiftTime,emplId,role);
    }

    public String addEmployee(String emplId , String emplName, String bankAccount, int mounthSalary, Role[] roles, LocalDate startDate, LocalDate endDate, int storeNum)  {
        return hrService.addEmployee(emplId,emplName,bankAccount,mounthSalary,roles,startDate,endDate,storeNum);
    }

    public String removeEmployee(String emplId)  {
        return hrService.removeEmployee(emplId);
    }

    public String getShiftHistory(LocalDate day, int storeNumber)  {
        return hrService.getShiftHistory(day,storeNumber);
    }

    public String updateSalary(String emplId , int monthSalary)  {
        return hrService.updateSalary(emplId,monthSalary);
    }

    public String startAddingConstrainsForNextWeek(int storeNum) {
        return hrService.startAddingConstrainsForNextWeek(storeNum);
    }

    public String scheduleReadyToPublish(int storeNumber){
        return hrService.scheduleReadyToPublish(storeNumber);
    }

    public String getEmployeeProf(String employeeId) {
        return hrService.getEmployeeProf(employeeId);
    }

    public String getCurrentSchedule(int storeNumber) {
        return hrService.getCurrentSchedule(storeNumber);
    }
    public String setPassword(String empId,String password) {
        return employeeService.setPassword(empId,password);

    }
    public String addConstrains(String Id,String password, LocalDate localDate, ShiftTime shiftTime) {
        return employeeService.addConstrains(Id,password,localDate,shiftTime);
    }

    public String addRole(String Id, String password, Role role){
        return employeeService.addRole(Id,password,role);
    }

    public String removeRole(String Id,String password, Role role) {
        return employeeService.removeRole(Id,password,role);
    }

    public String terminateJobReq(String Id,String password, LocalDate day) {
        return employeeService.terminateJobReq(Id,password,day);
    }

    public String getWeekShiftForAll(String empId){
        return employeeService.getWeekShiftForAll(empId);
    }

    public String setBankAccount(String Id,String password, String newAccount){
        return employeeService.setBankAccount(Id,password,newAccount);
    }

    public String loginForEmployee(String Id, String password){
        return employeeService.loginForEmployee(Id,password);
    }

    public String getProfile(String id){
        return employeeService.getProfile(id);
    }
}
