package BusinessLayer.Objects;
import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Discount;
import BusinessLayer.Objects.Location;

public class Product {
    private int MKT;
    private String productName;
    private String companyManufacturer;
    private Category category;
    private int priceBeforeDiscount;
    private double priceAfterDiscount;
    private int size;
    private int minimumQuantity;
    private int storeQuantity;
    private int storageQuantity;
    private Location location;
    private Discount discount;
    private boolean onSale;

    //check about this consructor
    public Product(int MKT , String productName, String companyManufacturer, int price, int size, int minimumQuantity , Category catagory, Location location, Discount discount)
    {
        this.MKT=MKT;
        this.productName=productName;
        this.companyManufacturer=companyManufacturer;
        this.priceBeforeDiscount=price;
        this.priceAfterDiscount = price;
        this.size=size;
        this.minimumQuantity=minimumQuantity;
        this.storeQuantity = 0;
        this.storageQuantity = 0;
        this.category = catagory;
        this.location = location;
        this.discount = discount;
        this.onSale=false;
    }

    public Product(int MKT , String productName, String companyManufacturer, int price, int size, int minimumQuantity, int storeQuantity, int storageQuantity , Category catagory, Location location, Discount discount)
    {
        this.MKT=MKT;
        this.productName=productName;
        this.companyManufacturer=companyManufacturer;
        this.priceBeforeDiscount=price;
        this.priceAfterDiscount = price;
        this.size=size;
        this.minimumQuantity=minimumQuantity;
        this.storeQuantity = storeQuantity;
        this.storageQuantity = storageQuantity;
        this.category = catagory;
        this.location = location;
        this.discount = discount;
        this.onSale=false;


    }
    public int getMKT(){
        return this.MKT;
    }
    public int getStoreQuantity() {
        return storeQuantity;
    }

    public void setStoreQuantity(int storeQuantity) {
        this.storeQuantity = storeQuantity;
    }

    public int getStorageQuantity() {
        return storageQuantity;
    }

    public void setStorageQuantity(int storageQuantity) {
        this.storageQuantity = storageQuantity;
    }
    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public int getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }
    public void setPriceAfterDiscount(double price) {
        this.priceAfterDiscount = price;
    }

    public void putInSale()
    {
        this.onSale=true;
    }
    public void EndInSale()
    {
        this.onSale=false;
    }
    public  boolean getOnsale()
    {
        return onSale;
    }
    public Discount getDiscount()
    {
        return discount;
    }
    public void setDiscount(Discount discount)
    {
        this.discount=discount;
    }
    public Category getCategory(){
        return this.category;
    }
    public String getProductName(){
        return productName;
    }
    public void setCategory(Category c){
        this.category = c;
    }

    public Location getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public String getCompanyManufacturer() {
        return companyManufacturer;
    }

}
