package DataAccessLayer.ReportProduct;

import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Discount;
import BusinessLayer.Objects.Location;

public class ProductDTO {
    private int MKT;
    private String name;
    private String companyManufacturer;
    private int categoryID;
    private double priceBeforeDiscount;
    private double priceAfterDiscount;
    private int size;
    private int minimumQuantity;
    private int storeQuantity;
    private int storageQuantity;
    private int locationID;
    private int discountID;
    public ProductDTO(int MKT, String name, String companyManufacturer, int categoryID,
                      double priceBeforeDiscount, double priceAfterDiscount, int size,
                      int minimumQuantity, int storeQuantity, int storageQuantity,
                      int locationID, int discountID){
        this.MKT = MKT;
        this.name = name;
        this.companyManufacturer = companyManufacturer;
        this.categoryID = categoryID;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.size = size;
        this.minimumQuantity = minimumQuantity;
        this.storeQuantity = storeQuantity;
        this.storageQuantity = storageQuantity;
        this.locationID = locationID;
        this.discountID = discountID;
    }
    public int getMKT(){
        return MKT;
    }
    public int getLocationID(){
        return locationID;
    }
    public int getMinimumQuantity(){
        return minimumQuantity;
    }
    public int getStoreQuantity(){
        return storeQuantity;
    }
    public int getStorageQuantity(){
        return storageQuantity;
    }
    public int getDiscountID(){
        return discountID;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public int getSize(){
        return size;
    }
    public String getName(){
        return name;
    }
    public String getCompanyManufacturer(){
        return companyManufacturer;
    }
    public double getPriceAfterDiscount(){
        return priceAfterDiscount;
    }
    public double getPriceBeforeDiscount(){
        return priceBeforeDiscount;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public void setCompanyManufacturer(String companyManufacturer) {
        this.companyManufacturer = companyManufacturer;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setPriceBeforeDiscount(double priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public void setStorageQuantity(int storageQuantity) {
        this.storageQuantity = storageQuantity;
    }

    public void setStoreQuantity(int storeQuantity) {
        this.storeQuantity = storeQuantity;
    }
}
