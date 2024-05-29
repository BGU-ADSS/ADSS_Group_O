package ServiceLayer;

import BuisnessLayer.Controller.employeeController;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

import java.util.Date;

public class employeeService {

    private employeeController empController;

    public employeeService(employeeController employeeController) {
        empController = employeeController;
    }

    public String setPassword(String empId,String password) throws NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String addConstrains(String Id,String password, Date day, Enum shift) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String addRole(String Id, String password,Enum role) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String removeRole(String Id,String password, Enum role) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String terminateJobReq(String Id,String password, Date day) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String getWeekShift() throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String setBankAccount(String Id,String password, String newAccount) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String logIn(String Id,String password, String userName) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }
}
