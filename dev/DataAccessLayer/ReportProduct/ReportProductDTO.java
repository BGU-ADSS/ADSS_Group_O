package DataAccessLayer.ReportProduct;

public class ReportProductDTO {
    private int reportID;
    private int productID;
    public ReportProductDTO(int reportID, int productID){
        this.reportID = reportID;
        this.productID = productID;
    }

    public int getReportID() {
        return this.reportID;
    }

    public int getProductID() {
        return this.productID;
    }
}
