package BuisnessLayer.Controller;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Schedule.Shift;
import BuisnessLayer.Workers.Employee;
import DTOs.ShiftTime;

import java.time.LocalDate;
import java.util.Dictionary;

public class Store {

    private String name;
    private String address;
    private Employee storeManager;
    private Schedule schedule;
    private Dictionary<String, Employee> employees;
    private int storeNumber;
    public Dictionary<String, Employee> getWorkers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorkers'");
    }
    public Shift getShift(LocalDate nextWeek, ShiftTime day) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShift'");
    }
}
