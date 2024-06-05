package ServiceLayer;

import BuisnessLayer.Controller.EmployeeController;
import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import DTOs.*;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class HRservice {

    private EmployeeController empController;
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    public HRservice(EmployeeController employeeController) {
        this.empController = employeeController;
    }

    public HRservice() {
        empController = new EmployeeController(new File("C:\\Users\\WINDOWS 10 PRO\\OneDrive\\Desktop\\ADSS\\ADSS_Group_O\\dev\\DTOs\\config.txt"), new File("C:\\Users\\WINDOWS 10 PRO\\OneDrive\\Desktop\\ADSS\\ADSS_Group_O\\dev\\DTOs\\Data.txt"));
    }

    public String getConstrains(String Id) {
        Response res = new Response();
        try {
            List<Shift> value = empController.getAvailableDaysForEmployee(Id);
            String str = "";
            for(Shift shift:value){
                str += shift.toString();
            }
            res.setReturnValue(str);
            
        } catch (Exception ex) {
            res = new Response(ex.getMessage(), null);
        }
        return gson.toJson(res);
    }

    public String setShift(LocalDate localDate, ShiftTime shiftTime, String emplId, Role role) {
        Response res;
        try {
            empController.setEmployeeInShift(localDate, shiftTime, emplId, role);
            res = new Response(true);
        } catch (Exception ex) {
            res = new Response(ex.getMessage(), null);
        }
        return gson.toJson(res);
    }

    public String addEmployee(String emplId, String emplName, String bankAccount, int mounthSalary, Role[] roles,
            LocalDate startDate, LocalDate endDate, int storeNum) {
        Response res;
        try {
            LinkedList<Role> rolesToSend = new LinkedList<>();
            for (Role role : roles)
                rolesToSend.add(role);
            empController.addEmployee(new Employee(emplId, emplName, bankAccount, mounthSalary, mounthSalary,
                    rolesToSend, startDate, endDate, storeNum));
            res = new Response(true);
        } catch (Exception ex) {
            res = new Response(ex.getMessage(), null);
        }
        return gson.toJson(res);
    }

    public String removeEmployee(String emplId) {
        Response res;
        try {

            res = new Response(empController.removeEmployee(emplId));
        } catch (Exception ex) {
            res = new Response(ex.getMessage(), null);
        }
        return gson.toJson(res);
    }

    public String getShiftHistory(LocalDate day, int storeNumber) {
        Response res;
        try {
            List<Shift[]> value = empController.getShiftHistory(day, storeNumber);

            res = new Response(value.toArray());
        } catch (Exception ex) {
            res = new Response(ex.getMessage(), null);
        }
        return gson.toJson(res);
    }

    public String updateSalary(String emplId, int monthSalary) {
        Response res;
        try {
            empController.updateSalary(emplId, monthSalary);
            res = new Response(true);
        } catch (Exception ex) {
            res = new Response(ex.getMessage(), null);
        }
        return gson.toJson(res);
    }

    public String login(String password) {

        Response res;
        try {
            empController.loginForHR(password);
            res = new Response(null, "Login Successful");
        } catch (Exception e) {
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }

    public String startAddingConstrainsForNextWeek(int storeNum) {

        Response res;
        try {
            empController.startAddingConstrainsForNextWeek(storeNum);
            res = new Response(true);
        } catch (Exception e) {
            res = new Response(e.getMessage());
        }
        return gson.toJson(res);
    }
}
