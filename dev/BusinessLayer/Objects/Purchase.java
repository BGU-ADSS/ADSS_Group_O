package BusinessLayer.Objects;

import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.Product;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class Purchase {
    private int purchaseID;
    private LocalDate purchaseDate;
    private double total;
    private HashMap<Product, List<Item>> products;
    private int customerID;
    private String customerName;
    public Purchase(int purchaseID, LocalDate purchaseDate, int customerID, String customerName){
        this.purchaseID = purchaseID;
        this.purchaseDate = purchaseDate;
        this.customerID = customerID;
        this.products = new HashMap<>();
        this.total = total;
        this.customerName = customerName;
    }
    public HashMap<Product, List<Item>> getProducts()
    {
        return this.products;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public int getCustomerID(){
        return customerID;
    }
    public String getCustomerName(){
        return customerName;
    }
    public int getPurchaseID(){
        return purchaseID;
    }
    public LocalDate getPurchaseDate(){
        return purchaseDate;
    }
}
