package DataAccessLayer;

import java.time.LocalDate;

public class ItemDTO {
    private int itemID;
    private int productID;
    private LocalDate expirationDate;
    private String itemCondition;
    private int purchaseID;
    public ItemDTO(int itemID, int productID, LocalDate expirationDate, String itemCondition){
        this.itemCondition = itemCondition;
        this.itemID = itemID;
        this.productID = productID;
        this.expirationDate = expirationDate;
        this.purchaseID=-1;
    }
    public ItemDTO(int itemID, int productID, LocalDate expirationDate, String itemCondition,int purchaseID){
        this.itemCondition = itemCondition;
        this.itemID = itemID;
        this.productID = productID;
        this.expirationDate = expirationDate;
        this.purchaseID=purchaseID;
    }
    public int getItemID(){
        return itemID;
    }
    public int getProductID(){
        return productID;
    }
    public LocalDate getExpirationDate(){
        return expirationDate;
    }
    public String getItemCondition(){
        return itemCondition;
    }

    public int getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID = purchaseID;
    }
}
