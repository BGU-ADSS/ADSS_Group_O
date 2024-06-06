package PresentationLayer;

import java.time.LocalDate;

import ServiceLayer.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DTOs.LocalDateAdapter;
import DTOs.Role;
import DTOs.ShiftTime;
import ServiceLayer.employeeService;

public class EmployeePres {
    private String empId ;
    private String empPassword;
    private ServiceFactory serviceFactory;
    private boolean hasLoggedIn;
    
    
    public static final String GET_WEAK_SHIFT_FOR_ALL_INPUT_FORMAT = "get-weak-shift-for-all";
    public static final String ADD_CONSTRAINS_INPUT_FORMAT = "add-constrains";
    public static final String ADD_ROLE_INPUT_FORMAT = "add-role";
    public static final String REMOVE_ROLE_INPUT_FORMAT = "remove-role";
    public static final String TERMINATE_JOB_INPUT_FORMAT = "terminate-job";
    public static final String SET_NEW_BANK_ACCOUNT_INPUT_FORMAT = "set-new-bank-account";
    public static final String LOGOUT_INPUT_FORMAT = "logout";
    public static final String PROFIE_INPUT_FORMAT = "profile";
    

    public EmployeePres(ServiceFactory serviceFactory){
        this.serviceFactory = serviceFactory;
    }

    


    



    public boolean login(String input){
        String[] params = extractIdAndPassword(input);

        String res = serviceFactory.loginForEmployee(params[0], params[1]);
        if(hasFailed(res)){
            System.out.println("Failed :"+getError(res));
            return false;
        }else{
            setIdPassword(input);
            System.out.println("Login successed!!");
            dealWithActions();
            return true;
        }
    }




    //this is the main loop , while employee is logged in , the loop will continue to ask for more actions.
    private void dealWithActions() {
        Logs.logEmployeeActionsInstructions();
        while(hasLoggedIn){
            String input = Logs.getInput();
            dealWithInput(input);
        }
    }


    
    
    private void dealWithInput(String input) {
        if(isEqualWithoutSpaces(GET_WEAK_SHIFT_FOR_ALL_INPUT_FORMAT,input)){
            printAllShiftsWithEmployees();
        }else  if( isEqualWithoutSpaces(input, ADD_ROLE_INPUT_FORMAT)){
            addRole();
        }else if(isEqualWithoutSpaces(input, ADD_CONSTRAINS_INPUT_FORMAT)){
            addConstrains();
        }else if(isEqualWithoutSpaces(input, REMOVE_ROLE_INPUT_FORMAT)){
            removeRole();
        }else if(isEqualWithoutSpaces(input, SET_NEW_BANK_ACCOUNT_INPUT_FORMAT)){
            setNewBankAccount();
        }else if(isEqualWithoutSpaces(input, TERMINATE_JOB_INPUT_FORMAT)){
            terminateJobReq();
        }else if( isEqualWithoutSpaces(input, LOGOUT_INPUT_FORMAT)){
            hasLoggedIn=false;
        }else if(isEqualWithoutSpaces(input, PROFIE_INPUT_FORMAT)){
            printProfile();
        }
    }
    
    
    
    
    
    
    private boolean isEqualWithoutSpaces(String format, String input) {
        return format.equals(input);
    }
    //============================================= Actions ==================================================
    private void printProfile() {
        printValue(serviceFactory.getProfile(empId));
    }


    private void terminateJobReq() {
        String res = serviceFactory.terminateJobReq(empId, empPassword, Logs.getInputDate());
        printValue(res);
    }

    private void setNewBankAccount() {
        Logs.logSetNewBankAccount();
        String input = Logs.getInput();
        String res = serviceFactory.setBankAccount(empId, empPassword, input);
        printValue(res);
    }
    
    private void printAllShiftsWithEmployees(){
        String res = serviceFactory.getWeekShiftForAll(empId);
        printValue(res);
    }
   
    
    private void addRole() {
        Logs.logRolesInShift();
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
    private void setIdPassword(String res) {
        hasLoggedIn=true;
        String[] params = extractIdAndPassword(res);
        this.empId=params[0];
        this.empPassword=params[1];
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
