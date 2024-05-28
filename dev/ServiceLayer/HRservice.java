package ServiceLayer;

import BuisnessLayer.Controller.employeeController;
import jdk.jshell.spi.ExecutionControl;

import java.util.Date;

public class HRservice {

    private employeeController empController;

    public HRservice(employeeController employeeController) {
        this.empController = employeeController;
    }

    public String getConstrains(String Id) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String setShift(Date date , Enum shift , String emplId) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String addEmployee(String emplId , String emplName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String removeEmployee(String emplId) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String addBreakDay(Date day, Enum shift) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String addConstrainsDeadline(Date day) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String getShiftHistory(Date day) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }

    public String updateSalary(String emplId , int hourSalary , int monthSalary) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("not implemented");
    }
}
