package DataAccessLayer;

import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.Product;
import BusinessLayer.Objects.Purchase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class PurchaseDTO {
    private int purchaseID;
    private LocalDate purchaseDate;
    private double total;
    private int customerID;
    public PurchaseDTO(int purchaseID, LocalDate purchaseDate, double total, int customerID){
        this.purchaseDate = purchaseDate;
        this.purchaseID = purchaseID;
        this.total = total;
        this.customerID = customerID;
    }
    public int getPurchaseID(){
        return purchaseID;
    }
    public int getCustomerID(){
        return customerID;
    }
    public double getTotal(){
        return total;
    }
    public void setTotal(double total){
        this.total = total;
    }
    public LocalDate getPurchaseDate(){
        return purchaseDate;
    }
}
