package PresentationLayer;

public class presentationController {
    private EmployeePres emPres;
    private HRPres hrPres;
    private boolean finish;

    public presentationController(){
        emPres = new EmployeePres();
        hrPres = new HRPres();
    }

    public void runPresentation(){
        while(!finish){
            Logs.logWelcomeToSystem();
            String input = Logs.getInput();
            if(input.startsWith("hr")){
                hrPres.loginAndStart(input.substring(3));
            }else if(input.startsWith("login")){
                emPres.login(input);
            }else if(input.equals("finish")){
                finish=true;
            }else{
                System.out.println("Invalid Input!!");
            }
        }
    }
}
