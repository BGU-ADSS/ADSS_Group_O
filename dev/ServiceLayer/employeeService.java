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

    public String setPassword(String empId,String password) {
        throw new UnsupportedOperationException("This method is not yet implemented");

    }

    public String addConstrains(String Id,String password, LocalDate localDate, ShiftTime shiftTime) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String addRole(String Id, String password, Role role){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String removeRole(String Id,String password, Role role) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String terminateJobReq(String Id,String password, LocalDate day) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String getWeekShiftForAll(){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String getWeekShiftForEmp(String Id){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }


    public String setBankAccount(String Id,String password, String newAccount){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String logIn(String Id,String password){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }
}
