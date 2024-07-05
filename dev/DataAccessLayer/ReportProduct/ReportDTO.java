package DataAccessLayer.ReportProduct;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReportDTO {
    private int reportID;
    private LocalDate reportDate;
    private String description;
    public ReportDTO(int reportID, LocalDate reportDate, String description){
        this.reportID = reportID;
        this.reportDate = reportDate;
        this.description = description;
    }
    public int getReportID(){
        return reportID;
    }
    public LocalDate getReportDate(){
        return reportDate;
    }
    public String getDescription(){
        return description;
    }
}
