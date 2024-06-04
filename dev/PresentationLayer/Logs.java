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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdForNewEmpl'");
    }

    public static String getNameForNewEmpl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNameForNewEmpl'");
    }

    public static String getBankForNEwEmpl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBankForNEwEmpl'");
    }

    public static int getSalaryForEmployee() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSalaryForEmployee'");
    }

    public static Role[] getRoleForNewEmpl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleForNewEmpl'");
    }

    public static LocalDate getStartDateForNewEmpl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStartDateForNewEmpl'");
    }

    public static LocalDate getEndDateForNewEmpl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEndDateForNewEmpl'");
    }

    public static int getStoreNumForNewEmpl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStoreNumForNewEmpl'");
    }

    public static String logRemoveEmplAndGetId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logRemoveEmplAndGetId'");
    }

    public static String getIdToGetConstrains() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdToGetConstrains'");
    }

    public static LocalDate getDateTogetShiftHistory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDateTogetShiftHistory'");
    }

    public static int getNewMounthSalary() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNewMounthSalary'");
    }

    public static LocalDate chooseShift() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'chooseShift'");
    }

    public static ShiftTime chooseShiftTime() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'chooseShiftTime'");
    }

    public static String getEmployeeIdToWorkIn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmployeeIdToWorkIn'");
    }


}
