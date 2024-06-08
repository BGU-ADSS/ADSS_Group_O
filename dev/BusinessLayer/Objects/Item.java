package BusinessLayer.Objects;

import java.time.LocalDate;


public class Item {

    private int itemID;
    private Product product;
    private LocalDate expirationDate;
    private ItemCondition condition;

    public Item(int itemID, Product product, LocalDate expirationDate, ItemCondition condition) {
        this.itemID = itemID;
        this.product = product;
        this.expirationDate = expirationDate;
        this.condition = condition;
    }
    public Product getProduct(){
        return this.product;
    }
//    public void markDefective() {
//        this.isDefective = true;
//        this.condition = ItemCondition.DEFECTIVE;
//    }
    public LocalDate getExpirationDate(){
        return expirationDate;
    }
    public int getItemID(){
        return itemID;
    }
    public ItemCondition getCondition()
    {
        return  this.condition;
    }
}
