package PresentationLayer;

import java.security.PublicKey;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DTOs.LocalDateAdapter;
import DTOs.Role;
import DTOs.ShiftTime;
import ServiceLayer.HRservice;

public class HRPres {
    private HRservice hrService;
    private boolean loggedIn;


    public static final String GET_CONSTRAINS = "get-constraints";
    public static final String SET_SHIFT = "set-shift";
    public static final String ADD_EMPLOYEE = "add-employee";
    public static final String REMOVE_EMPLOYEE = "remove-employee";
    public static final String GET_SHIFT_HISTORY = "get-shift-history";
    public static final String UPDATE_SALARY = "update-salary";
    public static final String LOGIN = "login";
    public static final String START_ADDING_CONSTRAINS_FOR_NEXT_WEEK = "start-adding-constraints-for-next-week";

    public HRPres() {
        hrService = new HRservice();
    }

    public boolean loginAndStart(String password) {
        String res = hrService.login(password);
        if (hasFailed(res)) {
            System.out.println("Failed :" + getError(res));
            return false;
        } else {
            loggedIn = true;
            System.out.println("Login successed!!");
            dealWithActions();
            return false;
        }
    }

    private void dealWithActions() {
        while (loggedIn) {
            Logs.logHRActionsInstructions();
            dealWithInput(Logs.getInput());
        }
    }

    private void dealWithInput(String input) {
        switch (input) {
            case "1":
                getConstrains();
                break;
            case "2":
                addEmployee();
                break;
            case "3":
                removeEmployee();
                break;
            case "4":
                getShiftHistory();
                break;
            case "5":
                updateSalaryForEmployee();
                break;
            case "6":
                setShifts();
                break;
            case "7":
                startAddingConstrainsForNextWeek();
                break;
            case "8":
                logout();
                break;
            default:
                Logs.print("invalid input");
        }

    }

    // ==================================ACTIONS===================================================
    private void logout() {
        loggedIn = false;
    }

    private void setShifts() {

        LocalDate shiftDate = Logs.chooseShift();
        ShiftTime shiftTime = Logs.chooseShiftTime();
        String id = Logs.getEmployeeIdToWorkIn();
        Role role = Logs.getRoleToAdd();
        String res = hrService.setShift(shiftDate, shiftTime, id,role);
        printValue(res);

    }

    private void updateSalaryForEmployee() {
        int newSalary = Logs.getNewMounthSalary();
        String res = hrService.updateSalary(null, newSalary);
        printValue(res);
    }

    private void getShiftHistory() {
        LocalDate date = Logs.getDateTogetShiftHistory();
        int storeNum = Logs.getStoreNumber();
        String res = hrService.getShiftHistory(date,storeNum);
        printValue(res);
    }

    private void removeEmployee() {
        String input = Logs.logRemoveEmplAndGetId();
        String res = hrService.removeEmployee(input);
        printValue(res);
    }

    private void addEmployee() {
        String Id = Logs.getIdForNewEmpl();
        String emplName = Logs.getNameForNewEmpl();
        String bankAccount = Logs.getBankForNEwEmpl();
        int salary = Logs.getSalaryForEmployee();
        Role[] role = Logs.getRoleForNewEmpl();
        LocalDate startDate = Logs.getStartDateForNewEmpl();
        LocalDate endDate = Logs.getEndDateForNewEmpl();
        int StoreNumber = Logs.getStoreNumForNewEmpl();
        String res = hrService.addEmployee(Id, emplName, bankAccount, salary, role, startDate, endDate, StoreNumber);
        printValue(res);
    }

    private void getConstrains() {
        String input = Logs.getIdToGetConstrains();
        String res = hrService.getConstrains(input);
        printValue(res);
    }

    private void startAddingConstrainsForNextWeek(){
        int input = Logs.getStoreNumber();
        String res = hrService.startAddingConstrainsForNextWeek(input);
        printValue(res);

    }
    // ============================================= Deal with response
    // ==================================================
    private void printValue(String res) {
        if (hasFailed(res)) {
            System.out.println("Failed :" + getError(res));
        } else {
            System.out.println(getValueString(res));
        }
    }

    // this func must return the value of the response.
    private String getValueString(String res) {
        ResponseManager response = new ResponseManager(res);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        return gson.toJson(response.value);
    }

    // return error message
    private String getError(String res) {
        ResponseManager response = new ResponseManager(res);
        return response.errorMessage;
    }

    // this function return true iff Error has occured
    private boolean hasFailed(String res) {
        return new ResponseManager(res).hasErrorOccured;
    }

}
