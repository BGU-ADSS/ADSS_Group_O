package ServiceLayer;

import BuisnessLayer.Controller.EmployeeController;
import DTOs.*;
import PresentationLayer.Logs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.time.LocalDate;

public class employeeService {

    private EmployeeController empController;
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();


    public employeeService(EmployeeController employeeController) {
        empController = employeeController;
    }

    public employeeService() {
        empController=new EmployeeController(new File("dev\\DTOs\\config.txt"), new File("dev\\DTOs\\Data.txt"));
    }

    public String setPassword(String empId,String password) {

        Response res;
        try
        {
            empController.setPassword(password,empId);
            res = new Response(null,"Successfully set password");
        }
        catch (Exception e){
            Logs.debug("set pass ex: "+e.getMessage());
            res = new Response(e.getMessage());
        }

        return gson.toJson(res);
    }

    public String addConstrains(String Id,String password, LocalDate localDate, ShiftTime shiftTime) {

        Response res;
        try
        {
            empController.addConstrains(Id, localDate, shiftTime);
            res = new Response(null,"Successfully add constrains");
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
            res = new Response(null,"Successfully add role");
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
            res = new Response(null,"Successfully remove role");
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
            res = new Response(null,"Successfully terminate job request");
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
            res = new Response(null,str);
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
            res = new Response(null,"Successfully set bank account");
        }
        catch (Exception e){
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String loginForEmployee(String Id, String password){

        Response res;
        try
        {
            empController.loginForEmployee(Id,password);
            res = new Response(null,"Successfully logged in");
            Logs.debug("loginnnnn");
        }
        catch (Exception e){
            Logs.debug(e.getMessage());
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String getProfile(String id){
        Response res;
        try{
            res = new Response(null,empController.getEmployee(id).getProf());
        }catch(Exception e ){
            res = new Response(e.getMessage(), null);
        }
        return gson.toJson(res);
    }
}
