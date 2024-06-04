package ServiceLayer;

import BuisnessLayer.Controller.EmployeeController;
import DTOs.*;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;

public class employeeService {

    private EmployeeController empController;
    private Gson gson = new Gson();


    public employeeService(EmployeeController employeeController) {
        empController = employeeController;
    }

    public employeeService() {
        //TODO Auto-generated constructor stub
    }

    public String setPassword(String empId,String password) {

        Response res;
        try
        {
            empController.setPassword(password,empId);
            res = new Response("Successfully set password");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }

        return gson.toJson(res);
    }

    public String addConstrains(String Id,String password, LocalDate localDate, ShiftTime shiftTime) {

        Response res;
        try
        {
            empController.addConstrains(Id, localDate, shiftTime);
            res = new Response("Successfully add constrains");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String addRole(String Id, String password, Role role){

        Response res;
        try
        {
            empController.addRoleForEmployee(Id,role);
            res = new Response("Successfully add role");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String removeRole(String Id,String password, Role role) {

        Response res;
        try
        {

            empController.removeRoleFromEmployee(Id,role);
            res = new Response("Successfully remove role");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String terminateJobReq(String Id,String password, LocalDate day) {

        Response res;
        try
        {
            empController.terminateJobReq(Id,day);
            res = new Response("Successfully terminate job request");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String getWeekShiftForAll(String Id){

        Response res;
        try
        {
            String str = empController.getCurrentWeekSchedule(Id) + empController.getNextWeekSchedule(Id);
            res = new Response(str);
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String setBankAccount(String Id,String password, String newAccount){

        Response res;
        try
        {
            empController.setBankAccountForEmployee(Id,newAccount);
            res = new Response("Successfully set bank account");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String logIn(String Id,String password){

        Response res;
        try
        {
            empController.loginForEmployee(Id,password);
            res = new Response("Successfully logged in");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }
}
