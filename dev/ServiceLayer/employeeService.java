package ServiceLayer;

import BuisnessLayer.Controller.employeeController;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

import java.time.LocalDate;
import java.util.Date;

public class employeeService {

    private employeeController empController;

    public employeeService(employeeController employeeController) {
        empController = employeeController;
    }

    public String setPassword(String empId,String password) {
        throw new UnsupportedOperationException("This method is not yet implemented");

    }

    public String addConstrains(String Id,String password, LocalDate localDate, Enum shift) {
           throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String addRole(String Id, String password,Enum role){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String removeRole(String Id,String password, Enum role) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String terminateJobReq(String Id,String password, Date day) {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String getWeekShift(){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String setBankAccount(String Id,String password, String newAccount){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String logIn(String Id,String password, String userName){
        throw new UnsupportedOperationException("This method is not yet implemented");
    }
}
