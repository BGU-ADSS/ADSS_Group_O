package BusinessLayer.Objects;

import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.ItemCondition;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class Report {
    private int reportID;
    private LocalTime reportDate;
    private HashMap<Item, ItemCondition> itemConditions;
    private List<Product> productsNeeds;
    public Report(int reportID, LocalTime reportDate, HashMap<Item, ItemCondition> itemConditions ,List<Product> productsNeeds){
        this.reportDate = reportDate;
        this.reportID = reportID;
        this.itemConditions = itemConditions;
        this.productsNeeds=productsNeeds;
    }
    public int getID(){
        return this.reportID;
    }
    public LocalTime getReportDate(){
        return this.reportDate;
    }

}
