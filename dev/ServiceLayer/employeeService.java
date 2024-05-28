package ServiceLayer;

import BuisnessLayer.Controller.employeeController;
import jdk.jshell.spi.ExecutionControl;

import java.util.Date;

public class employeeService {

    private employeeController empController;

    public employeeService(employeeController employeeController) {
        empController = employeeController;
    }

    public String addConstrains(String Id, Date day, Enum shift) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String addRole(String Id, Enum role) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String removeRole(String Id, Enum role) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String terminateJobReq(String Id, Date day) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String getWeekShift() throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String setBankAccount(String Id, String newAccount) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    public String logIn(String Id, String userName) throws ExecutionControl.NotImplementedException{
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }
}
