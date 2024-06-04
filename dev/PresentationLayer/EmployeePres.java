package PresentationLayer;

import java.time.LocalDate;

import DTOs.Role;
import DTOs.ShiftTime;
import ServiceLayer.employeeService;

public class EmployeePres {
    private String empId ;
    private String empPassword;
    private employeeService employeeService;
    private boolean hasLoggedIn;
    
    
    public static final String GET_WEAK_SHIFT_FOR_ALL_INPUT_FORMAT = "get-weak-shift-for-all";
    public static final String ADD_CONSTRAINS_INPUT_FORMAT = "add-constrains";
    public static final String ADD_ROLE_INPUT_FORMAT = "add-role";
    public static final String REMOVE_ROLE_INPUT_FORMAT = "remove-role";
    public static final String TERMINATE_JOB_INPUT_FORMAT = "terminate-job";
    public static final String SET_NEW_BANK_ACCOUNT_INPUT_FORMAT = "set-new-bank-account";
    public static final String LOGOUT_INPUT_FORMAT = "logout";
    

    public EmployeePres(){
        employeeService = new employeeService();
    }

    


    public void login(String id,String password){
        String res = employeeService.logIn(id, password);
        if(hasFailed(res)){
            System.out.println("Failed :"+getError(res));
        }else{
            setIdPassword(res);
            System.out.println("Login successed!!");
            dealWithActions();
        }
    }




    //this is the main loop , while employee is logged in , the loop will continue to ask for more actions.
    private void dealWithActions() {
        while(hasLoggedIn){
            Logs.logEmployeeActionsInstructions();
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
        }
    }
    
    
    private boolean isEqualWithoutSpaces(String gET_WEAK_SHIFT_FOR_ALL_INPUT_FORMAT2, String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEqual'");
    }
    //============================================= Actions ==================================================
    private void terminateJobReq() {
        String res = employeeService.terminateJobReq(empId, empPassword, Logs.getInputDate());
        printValue(res);
    }

    private void setNewBankAccount() {
        Logs.logSetNewBankAccount();
        String input = Logs.getInput();
        String res = employeeService.setBankAccount(empId, empPassword, input);
        printValue(res);
    }
    
    private void printAllShiftsWithEmployees(){
        String res = employeeService.getWeekShiftForAll();
        printValue(res);
    }
   
    
    private void addRole() {
        Logs.logRolesInShift();
        Role roleToAdd = Logs.getRoleToAdd();
        String res =employeeService.addRole(empId, empPassword, roleToAdd);
        printValue(res);
    }
    
    private void addConstrains() {
        Logs.logGetConstrainsDateToAdd();
        LocalDate date = Logs.getInputDate();
        ShiftTime shiftTime = Logs.logGetShiftInGivenDate();
        String res = employeeService.addConstrains(empId, empPassword, date, shiftTime);
        printValue(res);
    }

    private void removeRole(){
        Logs.logGetRoleToRemove();
        Role roleToRemove = Logs.getRoleToRemove();
        String res = employeeService.removeRole(empId, empPassword, roleToRemove);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setIdPassword'");
    }

    // this func must return the value of the response.
    private String getValueString(String res){
        ResponseManager response = new ResponseManager(res);
        return response.value;
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
