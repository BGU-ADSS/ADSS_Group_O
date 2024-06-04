import BuisnessLayer.Workers.HRManager;
import PresentationLayer.Logs;
import ServiceLayer.employeeService;

public class Main {


    private employeeService employeeService ;
    private HRManager hrManager;

    public static void main(String[] args) {
        Logs.logWelcomeToSystem();
        employeeService em = new employeeService();
        em.logIn(null, null);
    }
}