package PresentationLayer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import DTOs.Role;
import DTOs.ShiftTime;

public class Logs {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static void print(String toPrint) {
        System.out.println(toPrint);
    }

    public static void withDataOrNot() {
        print("options :-");
        print(" 1 . start with the data in data file.");
        print(" 2 . start without the data.");
    }

    public static void logWelcomeToSystem() {
        System.out.println("welcome to super lee ");
        System.out.println(" - to login as an employee write: login-<Id>-<password>");
        System.out.println(" - to login as HR manager write: hr-<password>");
        System.out.println(" - to check inventory funcs that *in progress* write : inventory");
        System.out.println(" - to finish write: finish");
    }

    public static void logEmployeeActionsInstructions(boolean isStoreKeeper) {
        print("1. " + EmployeePres.SET_PASSWORD + " :to set a new Password");
        print("2. " + EmployeePres.ADD_CONSTRAINS + " : to add constrains.");
        print("3. " + EmployeePres.ADD_ROLE + " :to add role.");
        print("4. " + EmployeePres.REMOVE_ROLE + " :to remove role.");
        print("5. " + EmployeePres.TERMINATE_JOB + " to terminate from job.");
        print("6. " + EmployeePres.GET_WEAK_SHIFT_FOR_ALL
                + " :to get the shifts of the weak with the employees that work in.");
        print("7. " + EmployeePres.SET_NEW_BANK_ACCOUNT + " :to set new bank account");
        print("8. " + EmployeePres.PROFIE + " :to print profile info");
        print("9. " + EmployeePres.LOGOUT + " :to logout from the employee user.");
        if (isStoreKeeper) {
            print("10. adding-category :to add new category");
            print("11. adding-product :to add new product");
            print("12. adding-item :to add new item");
        }

    }

    public static void logHRActionsInstructions() {
        print("1. " + HRPres.GET_CONSTRAINS + " :to get the employees that can work in");
        print("2. " + HRPres.ADD_EMPLOYEE + " :to add employee");
        print("3. " + HRPres.REMOVE_EMPLOYEE + " :to remove employee");
        print("4. " + HRPres.GET_SHIFT_HISTORY + " :to get the history of shifts from specific date");
        print("5. " + HRPres.UPDATE_SALARY + " :to update salary for employee");
        print("6. " + HRPres.SET_SHIFT + " :to set a shift for employee");
        print("7. " + HRPres.START_ADDING_CONSTRAINS_FOR_NEXT_WEEK
                + " :to start a new week and make employees start add constrains");
        print("8. " + HRPres.GET_EMPLOYEE_PROFILE + " :to get employee profile");
        print("9. " + HRPres.GET_CURRENT_WEEK_SCHEDULE + " : to get the current week schedule");
        print("10. " + HRPres.PUBLISH_SCHEDULE + " : to publish the schedule");
        print("11. logout");
    }

    public static String getInput() {
        return scanner.nextLine();
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void logRolesInShift() {
        print("0-StoreManager,\n1-Cashier,\n2-Storekeeper,\n3-ShiftManager,\n4-GroubManager,\n5-Driver");
    }

    public static Role fromInt(int i) {
        switch (i) {
            case 0:
                return Role.StoreManager;
            case 1:
                return Role.Cashier;
            case 2:
                return Role.Storekeeper;
            case 3:
                return Role.ShiftManager;
            case 4:
                return Role.GroubManager;
            case 5:
                return Role.Driver;
            default:
                print("Invaild num");
                return getRoleToAdd();
        }
    }

    public static Role getRoleToAdd() {
        print("choose a role to add/work as:");
        logRolesInShift();
        String input = getInput();
        int roleNum = -1;
        try {
            roleNum = Integer.parseInt(input);
        } catch (Exception e) {
            print("not valid input! try again");
            return getRoleToAdd();
        }
        return fromInt(roleNum);

    }

    public static void logGetConstrainsDateToAdd() {
        print("please enter a date of constrain (Format:\"yyyy-MM-dd\"):");
    }

    public static void logGetTerminateDate() {
        print("enter finish job date :");
    }

    public static LocalDate getInputDate() {
        String input = getInput();
        LocalDate date = null;
        try {
            date = LocalDate.parse(input, formatter);
        } catch (Exception e) {
            print("not valid input! try again");
            return getInputDate();
        }
        return date;
    }

    public static ShiftTime logGetShiftInGivenDate() {
        print("Enter shift time (Day/Night): ");

        return getInputShiftTime();
    }

    private static ShiftTime getInputShiftTime() {
        String input = getInput();

        try {
            return ShiftTime.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input.");
            return logGetShiftInGivenDate();
        }
    }

    public static void logGetRoleToRemove() {
        print("enter the role that you want to remove :");
    }

    public static Role getRoleToRemove() {
        String input = getInput();
        try {
            return fromInt(Integer.parseInt(input));
        } catch (Exception e) {
            print("invalid input! try again");
            return getRoleToRemove();
        }
    }

    public static void logSetNewBankAccount() {
        print("enter the bank account number :");
    }

    public static String getIdForNewEmpl() {
        print("enter the id of the new employee :");
        return getInput();
    }

    public static String getNameForNewEmpl() {
        print("enter the name of the employee :");
        return getInput();
    }

    public static String getBankForNEwEmpl() {
        print("set Bank Account for employee :");
        return getInput();
    }

    public static int getSalaryForEmployee() {
        print("enter salary number :");
        return getIntInput();
    }

    private static int getIntInput() {
        try {
            String input = getInput();
            return Integer.parseInt(input);
        } catch (Exception e) {
            return getIntInput();
        }
    }

    public static Role[] getRoleForNewEmpl() {
        print("enter a role number :");
        logRolesInShift();
        Role role = getRoleToRemove();
        return new Role[] { role };
    }

    public static LocalDate getStartDateForNewEmpl() {
        print("enter start date of contract : (yyyy-MM-dd)");
        return getInputDate();
    }

    public static LocalDate getEndDateForNewEmpl() {
        print("enter end date of contract : (yyyy-MM-dd)");
        return getInputDate();
    }

    public static int getStoreNumForNewEmpl() {
        print("enter the number of the store :");
        return getIntInput();
    }

    public static String logRemoveEmplAndGetId() {
        print("enter the id of the employee to remove :");
        return getInput();
    }

    public static String getIdToGetConstrains() {
        print("enter the id of the employee you want to check :");
        return getInput();
    }

    public static LocalDate getDateTogetShiftHistory() {
        print("enter the date of the shift history :");
        return getInputDate();
    }

    public static int getNewMounthSalary() {
        print("enter the new mounth salary :");
        return getIntInput();
    }

    public static LocalDate chooseShift() {
        print("enter the date :");
        return getInputDate();
    }

    public static ShiftTime chooseShiftTime() {
        print("enter the shift time (Day/Night) :");
        return getInputShiftTime();
    }

    public static String getEmployeeIdToWorkIn() {
        print("enter the id of the employee :");
        return getInput();
    }

    public static int getStoreNumber() {
        print("Enter store number :");
        return getIntInput();
    }

    public static String getStoreName() {
        print("Enter store name :");
        return getInput();
    }

    public static String getStoreAddress() {
        print("Enter store adress :");
        return getInput();
    }

    public static String getNewPassword() {
        print("enter new password (cannot be empty!) :");
        return getInput();
    }

    public static void debug(String string) {
        print(ANSI_RED + string + ANSI_RESET);
    }

}
