package PresentationLayer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import DTOs.Role;
import DTOs.ShiftTime;

public class Logs {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static void print(String toPrint){
        System.out.println(toPrint);
    }

    public static void logWelcomeToSystem(){
        System.out.println("welcome to super lee ");
        System.out.println(" - to login as an employee write: login-<Id>-<password>");
        System.out.println(" - to login as HR manager write: hr-<password>");
        System.out.println(" - to finish write: finish");
    }

    public static void logEmployeeActionsInstructions() {
        print(EmployeePres.SET_NEW_BANK_ACCOUNT_INPUT_FORMAT+" :to set new bank account");
        print(EmployeePres.ADD_CONSTRAINS_INPUT_FORMAT+" : to add constrains.");
        print(EmployeePres.ADD_ROLE_INPUT_FORMAT+" :to add role.");
        print(EmployeePres.REMOVE_ROLE_INPUT_FORMAT+" :to remove role.");
        print(EmployeePres.GET_WEAK_SHIFT_FOR_ALL_INPUT_FORMAT+" :to get the shifts of the weak with the employees that work in.");
        print(EmployeePres.TERMINATE_JOB_INPUT_FORMAT+" to terminate from job.");
        print(EmployeePres.LOGOUT_INPUT_FORMAT+" :to logout from the employee user.");
    }

    public static String getInput() {
        return scanner.nextLine();
    }

    public static void logRolesInShift() {
        print("0-StoreManager,\n1-Cashier,\n2-Storekeeper,\n3-ShiftManager,\n4-GroubManager");
    }
    public static Role fromInt(int i) {
        switch (i) {
            case 0: return Role.StoreManager;
            case 1: return Role.Cashier;
            case 2: return Role.Storekeeper;
            case 3: return Role.ShiftManager;
            case 4: return Role.GroubManager;
            default: print("Invaild num");return getRoleToAdd();
        }
    }
    public static Role getRoleToAdd() {
        print("enter the number of the role you want to add");
        String input = getInput();
        int roleNum=-1;
        try{ roleNum = Integer.parseInt(input);}
        catch(Exception e){ print("this is not a number!!");return getRoleToAdd();}
        return fromInt(roleNum);

    }

    public static void logGetConstrainsDateToAdd() {
        print("please enter a date of constrain (Format:\"yyyy-MM-dd\"):");
    }

    public static LocalDate getInputDate() {
        String input = getInput();
        LocalDate date = LocalDate.parse(input, formatter);
        return date;
    }

    public static ShiftTime logGetShiftInGivenDate() {
        print("Enter shift time (Day/Night): ");
        
        return getInputShiftTime();
    }

    private static ShiftTime getInputShiftTime(){
        String input = getInput();

        try {
            return ShiftTime.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input.");
            return logGetShiftInGivenDate();
        }
    }

    public static void logGetRoleToRemove() {
        print("enter the role that you want to remove ( StoreManager,Cashier,Storekeeper,ShiftManager,GroubManager):");
    }

    public static Role getRoleToRemove() {
        String input = getInput();
        try{
            return Role.valueOf(input);
        }catch(Exception e ){
            print("please enter one of the roles below");
            print(" StoreManager,Cashier,Storekeeper,ShiftManager,GroubManager");
            return getRoleToRemove();
        }
    }

    public static void logSetNewBankAccount() {
        print ("enter the bank account number");
    }

    public static String getIdForNewEmpl() {
        print("enter the id of the new employee : ");
        return getInput();
    }

    public static String getNameForNewEmpl() {
        print("enter the name of the employee:");
        return getInput();
    }

    public static String getBankForNEwEmpl() {
        print("set Bank Account for employee");
        return getInput();
    }

    public static int getSalaryForEmployee() {
        print("enter salary number ")    ;
        return getIntInput();
    }

    private static int getIntInput() {
        try{
            String input = getInput();
            return Integer.parseInt(input);
        }catch(Exception e){
            return getIntInput();
        }
    }

    public static Role[] getRoleForNewEmpl() {
        print ("enter an role number : ");
        logRolesInShift();
        String input = getInput();
        Role role =  getRoleToRemove();
        return new Role[]{role};
    }

    public static LocalDate getStartDateForNewEmpl() {
        print("enter start date of contract : ");
        return getInputDate();
    }

    public static LocalDate getEndDateForNewEmpl() {
        print("enter end date of contract : ");
        return getInputDate();
    }

    public static int getStoreNumForNewEmpl() {
        print("enter the number of the store:");
        return getIntInput();    
    }

    public static String logRemoveEmplAndGetId() {
        print("enter the id of the employee to remove:");
        return getInput();    
    }

    public static String getIdToGetConstrains() {
        print("enter the id of the employee you want to check:");
        return getInput();
    }

    public static LocalDate getDateTogetShiftHistory() {
        print("enter the date of the shift history:");
        return getInputDate();    
    }

    public static int getNewMounthSalary() {
        print("enter the new mounth salary");
        return getIntInput();    
    }

    public static LocalDate chooseShift() {
        print("enter the date:");
        return getInputDate();
    }

    public static ShiftTime chooseShiftTime() {
        print("enter the shift time (Day/Night):");
        return getInputShiftTime();
    }

    public static String getEmployeeIdToWorkIn() {
        print("enter the id of the employee");
        return getInput();    
    }

    public static int getStoreNumber() {
        print("Enter store number");
        return getIntInput();    
    }


}
