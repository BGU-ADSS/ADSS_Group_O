import PresentationLayer.Logs;
import PresentationLayer.presentationController;

public class Main {

    public static void main(String[] args) {
        new presentationController(withOrWithout()).runPresentation();
    }

    public static boolean withOrWithout(){
        Logs.withDataOrNot();
        String input = Logs.getInput();
        if(input.equals("1")){ return true;}
        else if (input.equals("2")) { return false;}
        else {
            System.out.println("invalid input! try again");
            return withOrWithout();
        }
    }
}
