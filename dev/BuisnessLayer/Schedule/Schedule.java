package BuisnessLayer.Schedule;

import java.time.LocalDate;
import java.util.Date;
import java.util.Dictionary;

import DTOs.ShiftTime;

public class Schedule {

    private Dictionary<LocalDate, Shift[]> dayShifts;
    private Date deadline;
    public Dictionary<LocalDate, Dictionary<ShiftTime, Boolean>> getAvaliableDaysForEmployee(String empId) {
        for (LocalDate day : dayShifts.keys()) {
            
        }
    }

}
