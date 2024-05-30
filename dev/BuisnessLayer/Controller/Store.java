package BuisnessLayer.Controller;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Workers.Employee;
import DTOs.ShiftTime;

import java.util.Date;
import java.util.Dictionary;

public class Store {

    private String name;
    private String address;
    private Employee storeManager;
    private Schedule schedule;
    private Dictionary<String, Employee> employees;
    private int storeNumber;

    public void addConstrains(String empId, Date day, ShiftTime shiftTime){

        if(employees.get(empId) == null){
            throw new IllegalArgumentException("Employee does not exist");
        }
        schedule.addConstrains()
    }
}
