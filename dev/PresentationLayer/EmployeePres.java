package PresentationLayer;

import java.time.LocalDate;

import ServiceLayer.ServiceFactory;
import ServiceLayer.employeeService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DTOs.LocalDateAdapter;
import DTOs.Role;
import DTOs.ShiftTime;

public class EmployeePres {
    private String empId ;
    private String empPassword;
    private ServiceFactory serviceFactory;
    private boolean hasLoggedIn;
    private boolean isStoreKeeper;
    private MainController stockC;
    private int storeNum;
    
    public static final String SET_PASSWORD = "set-Password";
    public static final String ADD_CONSTRAINS = "add-constrains";
    public static final String ADD_ROLE = "add-role";
    public static final String REMOVE_ROLE = "remove-role";
    public static final String TERMINATE_JOB = "terminate-job";
    public static final String GET_WEAK_SHIFT_FOR_ALL = "get-weak-shift-for-all";
    public static final String SET_NEW_BANK_ACCOUNT = "set-new-bank-account";
    public static final String PROFIE = "profile";
    public static final String LOGOUT = "logout";

    public EmployeePres(ServiceFactory serviceFactory){
        this.serviceFactory = serviceFactory;
        stockC = new MainController();
    }



    public boolean login(String input){
        String[] params = extractIdAndPassword(input);
        String res = serviceFactory.loginForEmployee(params[0], params[1]);
        if(hasFailed(res)){
            System.out.println("Failed :"+getError(res));
            return false;
        }else{
            setEmployeeProbs(input);
            
            System.out.println("Login successed!!");
            dealWithActions();
            return true;
        }
    }




    //this is the main loop , while employee is logged in , the loop will continue to ask for more actions.
    private void dealWithActions() {

        while(hasLoggedIn){
            Logs.logEmployeeActionsInstructions(isStoreKeeper);
            Logs.print("Please choose an option :-");
            dealWithInput(Logs.getInput());
        }
    }


    
    
    private void dealWithInput(String input) {

        switch(input){
            case "1":
                setEmpPassword();
                break;
            case "2":
                addConstrains();
                break;
            case "3":
                addRole();
                break;
            case "4":
                removeRole();
                break;
            case "5":
                terminateJobReq();
                break;
            case "6":
                printAllShiftsWithEmployees();
                break;
            case "7":
                setNewBankAccount();
                break;
            case "8":
                printProfile();
                break;
            case "9":
                logout();
                break;
            case "10":
            case "11":
            case "12":
                if(isStoreKeeper){
                    dealWithStoreKeeper(input);
                }
                break; 
            default:
                Logs.print("invalid input");
        }
    }

    //============================================= Actions ==================================================

    private void dealWithStoreKeeper(String input) {
        try{

            switch (input) {
                case "10":
                    stockC.addingCategoryAction();
                    break;
                case "11":
                    stockC.addingProductAction();
                    break;
                case "12":
                    stockC.addingItem(serviceFactory.getEmployeeStoreNumber(empId));
                    break;
            
                default:
                    Logs.print("invalid input");
                    
            }
        }catch(Exception e){
            System.out.print(e.getStackTrace());
        }
    }



    private void logout(){
        hasLoggedIn = false;
    }

    private void printProfile() {
        ResponseManager res = new ResponseManager(serviceFactory.getProfile(empId));
        Logs.print((String) res.value);
    }


    private void setEmpPassword() {
        String input = Logs.getNewPassword();
        String res = serviceFactory.setPassword(empId, input);
        if(hasFailed(res)) Logs.print("Failed :"+getError(res));
        else {
            empPassword = input;
            Logs.print(getValueString(res));
        }

    }

    private void terminateJobReq() {
        Logs.logGetTerminateDate();
        LocalDate date = Logs.getInputDate();
        String res = serviceFactory.terminateJobReq(empId, empPassword, date);
        printValue(res);
    }

    private void setNewBankAccount() {
        Logs.logSetNewBankAccount();
        String input = Logs.getInput();
        String res = serviceFactory.setBankAccount(empId, empPassword, input);
        printValue(res);
    }
    
    private void printAllShiftsWithEmployees(){
        ResponseManager res = new ResponseManager(serviceFactory.getWeekShiftForAll(empId));
        if(res.hasErrorOccured) System.out.println(res.errorMessage);
        else System.out.println(res.value);
    }
   
    
    private void addRole() {
        Role roleToAdd = Logs.getRoleToAdd();
        String res = serviceFactory.addRole(empId, empPassword, roleToAdd);
        printValue(res);
    }
    
    private void addConstrains() {
        Logs.logGetConstrainsDateToAdd();
        LocalDate date = Logs.getInputDate();
        ShiftTime shiftTime = Logs.logGetShiftInGivenDate();
        String res = serviceFactory.addConstrains(empId, empPassword, date, shiftTime);
        printValue(res);
    }

    private void removeRole(){
        Logs.logGetRoleToRemove();
        Logs.logRolesInShift();
        Role roleToRemove = Logs.getRoleToRemove();
        String res = serviceFactory.removeRole(empId, empPassword, roleToRemove);
        printValue(res);
    }
    //============================================= Deal with response ==================================================
    private void printValue(String res){
        if(hasFailed(res)){
            System.out.println("Failed :"+getError(res));
        }else{
            System.out.println(getValueString(res));
        }
    }

    //recieve the response that contain Id and password , we have to set them and declare them 
    private void setEmployeeProbs(String res) {
        hasLoggedIn=true;
        String[] params = extractIdAndPassword(res);
        this.empId=params[0];
        this.empPassword = params[1];
        this.isStoreKeeper = serviceFactory.employeeIsStoreKeeperToday(empId);
        storeNum = serviceFactory.getEmployeeStoreNumber(empId);
    }

    public static String[] extractIdAndPassword(String input) {
        if (input == null || !input.startsWith("login-")) {
            return null;
        }
        String[] parts = input.split("-", 3);
        Logs.debug(parts.length+"");
        if (parts.length != 3) {
            return null;
        }
        String id = parts[1];
        String password = parts[2];
        return new String[]{id, password};
    }

    // this func must return the value of the response.
    private String getValueString(String res){
        ResponseManager response = new ResponseManager(res);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        return gson.toJson(response.value);
    }




    //return error message
    private String getError(String res) {
        ResponseManager response = new ResponseManager(res);
        return response.errorMessage;
    }




    //this function return true iff Error has occured 
    private boolean hasFailed(String res) {
        return new ResponseManager(res).hasErrorOccured;
    }

}
