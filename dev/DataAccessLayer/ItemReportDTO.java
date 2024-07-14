package DataAccessLayer;

public class ItemReportDTO {
    private int reportID;
    private int itemID;
    public ItemReportDTO(int reportID, int itemID){
        this.reportID = reportID;
        this.itemID = itemID;
    }

    public int getReportID(){
        return reportID;
    }
    public int getItemID(){
        return itemID;
    }
}
