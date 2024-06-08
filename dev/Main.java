import PresentationLayer.MainController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("           Login");
        String login = "";
        String pass = "";
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (!login.equals("omryadam@gmail.com") || !pass.equals("omryadam123")){
            System.out.print("Email: ");
            login = r.readLine();
            System.out.println();
            System.out.print("Password: ");
            pass = r.readLine();
            System.out.println();
            if(!login.equals("omryadam@gmail.com") || !pass.equals("omryadam123")){
                System.out.println("Email or Password that you entered is wrong, Try again");
            }
        }
        MainController m = new MainController();
        m.start();
    }
}