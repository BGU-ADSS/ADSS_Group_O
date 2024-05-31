package BuisnessLayer.Schedule;

import java.time.LocalDate;
import java.util.Date;
import java.util.Dictionary;
import java.util.Iterator;

import DTOs.ShiftTime;

public class Schedule {

    private Dictionary<LocalDate, Shift[]> dayShifts;
    private Date deadline;
    public Dictionary<LocalDate, Dictionary<ShiftTime, Boolean>> getAvaliableDaysForEmployee(String empId) {
        Iterator<LocalDate> datesInSchedule = dayShifts.keys().asIterator();
        for (LocalDate day :datesInSchedule. ) {
            Shift[] shift = dayShifts.get(day);
            if(dayShifts.get(day).isEmployeeAvaliable(empId)){
                
            }
        }
    }

}
