package BuisnessLayer.Schedule;

import DTOs.ShiftTime;

import java.util.Date;
import java.util.Dictionary;

public class Schedule {

    private Dictionary<Date, Shift[]> dayShifts;
    private Date deadline;


    public void addConstrains(String empId, Date day, ShiftTime shiftTime) {

        if(dayShifts.get(day) == null) {
            throw new IllegalArgumentException("not suitable date!");
        }
        Shift[] shifts = dayShifts.get(day);
        if (shiftTime == ShiftTime.Day){
            shifts[0].re;
        }
    }
}
