package BusinessLayer.Objects;

import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.ItemCondition;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class Report {
    private int reportID;
    private LocalDate reportDate;
    private String description;
    private HashMap<Item, ItemCondition> itemConditions;
    private List<Product> productsNeeds;
    public Report(int reportID, LocalDate reportDate, HashMap<Item, ItemCondition> itemConditions ,List<Product> productsNeeds, String description){
        this.reportDate = reportDate;
        this.reportID = reportID;
        this.itemConditions = itemConditions;
        this.productsNeeds=productsNeeds;
        this.description = description;
    }
    public int getID(){
        return this.reportID;
    }
    public LocalDate getReportDate(){
        return this.reportDate;
    }
    public String getDescription(){
        return description;
    }

}
