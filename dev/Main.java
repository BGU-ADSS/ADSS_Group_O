import java.util.HashMap;

import BuisnessLayer.Workers.HRManager;
import PresentationLayer.Logs;
import ServiceLayer.employeeService;

public class Main {


    private employeeService employeeService ;
    private HRManager hrManager;

    public static void main(String[] args) {
        Logs.logWelcomeToSystem();
        HashMap<String,Object> toRet = new HashMap<>();
        toRet.put("value", new A());
        toRet.put("errorMessage",null);
        System.out.println(toRet.toString());
        
    }
}
