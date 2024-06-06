package ServiceLayer;

import java.io.File;

import BuisnessLayer.Controller.EmployeeController;

public class ServiceManager {
    public employeeService employeeService;
    public HRservice hrService;
    public ServiceManager(){
        EmployeeController eC = new EmployeeController(new File("dev\\DTOs\\config.txt"), new File("dev\\DTOs\\Data.txt"));
        employeeService = new employeeService(eC);
        hrService = new HRservice(eC);
    }
}
