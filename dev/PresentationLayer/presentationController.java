package PresentationLayer;

import ServiceLayer.ServiceFactory;

public class presentationController {
    private EmployeePres emPres;
    private HRPres hrPres;
    private ServiceFactory serviceFactory;
    private boolean finish;

    public presentationController(boolean withData){
        if(withData) {
            serviceFactory = new ServiceFactory(withData);
            emPres = new EmployeePres(serviceFactory,withData);
            hrPres = new HRPres(serviceFactory);
        }
        else {
            runIfWithoutData();
        }
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

    public void runIfWithoutData(){
        serviceFactory = new ServiceFactory(false);
        Logs.print("choose an option:-");
        Logs.print("1 - set HR mannager password");
        Logs.print("2 - add store");
        Logs.print("3 - done");
        String opt = Logs.getInput();
        while (!opt.equals("3")){
            if (opt.equals("1")){
                Logs.print(serviceFactory.addHRmanager(Logs.getNewPassword()));
            }
            else if (opt.equals("2")){
                Logs.print(serviceFactory.addStore(Logs.getStoreNumber(),Logs.getStoreName(),Logs.getStoreAddress()));
            }
            Logs.print("\nchoose an option:-");
            Logs.print("1 - add HR mannager");
            Logs.print("2 - add store");
            Logs.print("3 - done");
            opt = Logs.getInput();
        }
        hrPres = new HRPres(serviceFactory);
        emPres = new EmployeePres(serviceFactory);
    }
}
