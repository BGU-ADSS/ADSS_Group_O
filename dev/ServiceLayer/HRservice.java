package ServiceLayer;

import BuisnessLayer.Controller.EmployeeController;
import DTOs.*;
import jdk.jshell.spi.ExecutionControl;

import java.time.LocalDate;
import java.util.Date;

public class HRservice {

    private EmployeeController empController;

    public HRservice(EmployeeController employeeController) {
        this.empController = employeeController;
    }

    public String getConstrains(String Id)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public Response setShift(LocalDate localDate , ShiftTime shiftTime, String emplId)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public Response addEmployee(String emplId , String emplName,String bankAccount,int mounthSalary,Role[] roles,LocalDate startDate,LocalDate endDate,int storeNum)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String removeEmployee(String emplId)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String addBreakDay(Date day, ShiftTime shiftTime)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String addConstrainsDeadline(Date day)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public String getShiftHistory(Date day) throws ExecutionControl.NotImplementedException {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }

    public Response updateSalary(String emplId , int monthSalary)  {
        throw new UnsupportedOperationException("This method is not yet implemented");
    }
}
