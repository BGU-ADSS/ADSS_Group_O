package BusinessLayer.Fascades;

import BusinessLayer.Objects.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductFacade {
    private static ProductFacade instance = null;
    private HashMap<Integer, Product> products;
    private HashMap<Integer, Item> items;
    private HashMap<Integer, Item> itemsInStore;
    private HashMap<Integer, Item> itemsInStorage;
    private DiscountFacade discountFacade;
    private int firstItemID;
    public ProductFacade(HashMap<Integer, Product> products ,DiscountFacade discountFacade)
    {
        this.items=new HashMap<>();
        this.itemsInStorage = new HashMap<>();
        this.itemsInStore = new HashMap<>();
        this.products=products;
        this.discountFacade=discountFacade;
        firstItemID = -1;
    }
    public ProductFacade()
    {
        this.items=new HashMap<>();
        this.products=new HashMap<>();
        this.itemsInStore = new HashMap<>();
        this.itemsInStorage = new HashMap<>();
        firstItemID = -1;
    }

    public static ProductFacade getInstance() {
        if (instance == null) {
            instance = new ProductFacade();
        }
        return instance;
    }
    public HashMap<Integer, Product> getProducts(){
        return this.products;
    }

    public void updateStock(int productId ,int storeQuantity, int storageQuantity) throws Exception{
        Product product=products.get(productId);
        if(product==null)
        {
            throw new Exception("no product with this id in the store");
        } else if (storeQuantity <0 || storageQuantity <0) {
            throw new Exception("Quantity invalid");
        } else {
            for (int i = 0; i < storageQuantity; i++){
               addToStorage(getFirstItemByProductID(productId));
            }
            for (int i = 0; i < storeQuantity; i++){
                addToStore(getFirstItemByProductID(productId));
            }
            firstItemID = -1;
        }
    }

    public HashMap<Integer, Item> getItemsInStorage() {
        return itemsInStorage;
    }

    public HashMap<Integer, Item> getItemsInStore() {
        return itemsInStore;
    }

    public boolean checkMinimumStock(int productId) throws Exception{
        Product product=products.get(productId);
        if(product==null)
        {
            throw new Exception("no product with this id in the store");
        }
        else {
            if(product.getStoreQuantity() <= product.getMinimumQuantity())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
    public void addProduct(Product product)
    {
        products.put(product.getMKT(),product);
    }
    public void applyProductDiscount(Product product, double percent, LocalDate startDate, LocalDate endDate){
        try{
            Discount d = discountFacade.builedProductDiscount(percent, startDate, endDate);
            product.putInSale();
            double finalPrice=product.getPriceBeforeDiscount();
            finalPrice=finalPrice-(finalPrice*percent/100);
            product.setDiscount(d);
            product.setPriceAfterDiscount(finalPrice);
        }
        catch (Exception e){

        }
    }
    public void applyProductDiscount(int productID, Discount discount){
        try{
            Product product = products.get(productID);
            product.putInSale();
            double finalPrice=product.getPriceBeforeDiscount();
            finalPrice=finalPrice-(finalPrice*discount.getPercent()/100);
            product.setDiscount(discount);
            product.setPriceAfterDiscount(finalPrice);
        }
        catch (Exception e){

        }
    }
    public void checkAllDiscounts(){
        for (Product product : products.values()){
            if(product.getOnsale())
            {
                if(product.getDiscount().getEndDate().isBefore(LocalDate.now()))
                {
                    product.EndInSale();
                }
            }
        }
    }

    public List<Product> getProductsByCategory(String categoryName) throws Exception{
        if(categoryName == null){
            throw new Exception("Category is null");
        }
        List<Product> ps = new LinkedList<>();
        for (Product product : products.values()){
            if(product.getCategory().getCategoryName().equals(categoryName))
            {
                ps.add(product);
            }
            if(product.getCategory().getCategoryFather().getCategoryName().equals(categoryName)){
                ps.add(product);
            }
            for(Category c : product.getCategory().getSubCategories()){
                if(c.getCategoryName().equals((categoryName))){
                    ps.add(product);
                }
            }
        }


        return ps;
    }

    public HashMap<Integer, BusinessLayer.Objects.Item> getItems() {
        return items;
    }

    public void addToStorage(int itemID) throws Exception{
        Item i = items.get(itemID);
        if(i==null)
        {
            throw new Exception("item doesnt exist");
        }
        if(itemsInStorage.values().contains(i)){
            throw new Exception("The item is already exist in the storage");
        }
        for(Product p1: products.values()){
            if(i.getProduct().equals(p1)){
                p1.setStorageQuantity(p1.getStorageQuantity()+1);
            }
        }
    }
    public void addToStore(int itemID) throws Exception{
        Item i = items.get(itemID);
        if(i==null)
        {
            throw new Exception("item doesnt exist");
        }
        if(itemsInStore.values().contains(i)){
            throw new Exception("The item is already exist in the store");
        }
        for(Product p1: products.values()){
            if(i.getProduct().equals(p1)){
                if(p1.getStorageQuantity() > 0) {
                    p1.setStorageQuantity(p1.getStorageQuantity() - 1);
                    p1.setStoreQuantity(p1.getStorageQuantity() + 1);
                }

                else throw new Exception("There's no more products of this type");
            }
        }
    }
    public void checkExpirationDates() throws Exception {
        for (Item item : itemsInStore.values()) {
            if(LocalDate.now().isAfter(item.getExpirationDate())){
                removeItem(item.getItemID(), true);
            }
        }
        for (Item item : itemsInStorage.values()) {
            if(LocalDate.now().isAfter(item.getExpirationDate())){
                removeItem(item.getItemID(),false);
            }
        }
        Alert();
    }
    public void removeItem(int itemID, boolean inStore) throws Exception{
        Item item = items.get(itemID);
        if(item==null)
        {
            throw new Exception("item doesnt exist");
        }
        Product p = item.getProduct();
        if(!inStore) {
            if(p.getStorageQuantity() == 0){
                throw new Exception("This Item is not found in the storage: " + item.getProduct().getProductName() + " with ID: " + item.getItemID());
            }
            if(!itemsInStorage.values().contains(item)){
                throw new Exception("This Item is not found in the storage: " + item.getProduct().getProductName() + " with ID: " + item.getItemID());
            }
            p.setStorageQuantity(p.getStorageQuantity() - 1);
            itemsInStorage.remove(item.getItemID(), item);
            items.remove(itemID);
        }
        else{
            if(p.getStoreQuantity() == 0){
                throw new Exception("This Item is not found in the store: " + item.getProduct().getProductName() + " with ID: " + item.getItemID());
            }
            if(!itemsInStore.values().contains(item)){
                throw new Exception("This Item is not found in the store: " + item.getProduct().getProductName() + " with ID: " + item.getItemID());
            }
            p.setStoreQuantity(p.getStoreQuantity() - 1);
            itemsInStore.remove(item.getItemID(), item);
        }
        Alert();

    }
    public void Alert(){
        for (Product product : products.values()) {
            int quantity=product.getStorageQuantity()+product.getStoreQuantity();
            if(quantity<=(product.getMinimumQuantity())){
                if(quantity==0){
                    System.out.println("Product MKt: "+product.getMKT()+" is out of stock");}

                else System.out.println("Product MKt: "+product.getMKT()+" is almost  out of stock and the current amount: "+quantity);
            }
        }
    }

    public void buildProduct(String productName, String companyManufacturer, int price, int size, int minimumQuantity, Category c, String description,int section,int shelf) throws Exception{

        Location l = new Location(description,section,shelf);
        if(productName == null || companyManufacturer == null){
            throw new Exception("one of the names that been provided is invalid");
        }
        Product p = new Product(products.size() + 1, productName, companyManufacturer, price, size, minimumQuantity, 0, 0, c, l, null);
        products.put(p.getMKT(), p);
    }
    public void buildItem(int prodID, LocalDate expirationDate) throws Exception{
        if(products.get(prodID) == null){
            throw new Exception("Product is null");
        }
        if(expirationDate == null){
            throw new Exception("Expiration Date is null");
        }
        Item i = new Item(items.size() + 1, products.get(prodID), expirationDate, ItemCondition.GOOD);
        items.put(i.getItemID(),i);
    }
    public int getProductIDByName(String name) throws Exception{
        for (Product p: products.values()){
            if(p.getProductName().equals(name)){
                return p.getMKT();
            }
        }
        throw new Exception("There's no product with a provided name");
    }

    public int getFirstItemByProductID(int prodID) throws Exception{
        for (Item i: items.values()){
            if(i.getProduct().getMKT() == prodID && i.getItemID() > firstItemID){
                firstItemID = i.getItemID();
                return i.getItemID();
            }
        }
        throw new Exception("Provided product doesn't have items yet");
    }
}
