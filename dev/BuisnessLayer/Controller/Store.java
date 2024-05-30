package BuisnessLayer.Controller;

import BuisnessLayer.Schedule.Schedule;
import BuisnessLayer.Workers.Employee;

import java.util.Dictionary;

public class Store {

    private String name;
    private String address;
    private Employee storeManager;
    private Schedule schedule;
    private Dictionary<String, Employee> employees;
    private int storeNumber;
}
