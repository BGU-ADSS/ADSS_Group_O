package DataAccessLayer;

import javax.sound.sampled.FloatControl;
import java.time.LocalDate;

public class DiscountDTO {
    private int discountID;
    private double percent;
    private LocalDate startDate;
    private LocalDate endDate;
    public DiscountDTO(int discountID, double percent, LocalDate startDate, LocalDate endDate){
        this.discountID = discountID;
        this.percent = percent;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public int getDiscountID(){
        return discountID;
    }
    public double getPercent(){
        return percent;
    }
    public LocalDate getStartDate(){
        return startDate;
    }
    public LocalDate getEndDate(){
        return endDate;
    }
}
