package BuisnessLayer.Schedule;

import BuisnessLayer.Workers.Employee;
import DTOs.Role;
import DTOs.ShiftTime;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

public class Shift {

    private Date day;
    private ShiftTime shiftTime;
    private Dictionary<Role, Employee> workersInShift;
    private Dictionary<Role, List<Employee>> workersAvailable;
    private Dictionary<Role, Integer> constrainsForRole;

    public void
}
