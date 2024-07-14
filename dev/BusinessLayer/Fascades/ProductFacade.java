package BusinessLayer.Fascades;

import BusinessLayer.Objects.*;
import DataAccessLayer.ItemDAO;
import DataAccessLayer.ItemDTO;
import DataAccessLayer.LocationDAO;
import DataAccessLayer.LocationDTO;
import DataAccessLayer.ReportProduct.ProductDAO;
import DataAccessLayer.ReportProduct.ProductDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
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
    private HashMap<Integer, Location> locations;
    private DiscountFacade discountFacade;
    private int firstItemID;
    private Connection connection;

    private int locationId=1;
    private ProductDAO productDAO;
    private ItemDAO itemDAO;
    private LocationDAO locationDAO;
    public ProductFacade(HashMap<Integer, Product> products ,DiscountFacade discountFacade)
    {
        this.items=new HashMap<>();
        this.itemsInStorage = new HashMap<>();
        this.itemsInStore = new HashMap<>();
        locations = new HashMap<>();
        this.products=products;
        this.discountFacade=discountFacade;
        firstItemID = -1;
        productDAO = new ProductDAO();
        itemDAO = new ItemDAO();
        locationDAO = new LocationDAO();
    }
    public ProductFacade()
    {

        this.items=new HashMap<>();
        this.products=new HashMap<>();
        this.itemsInStore = new HashMap<>();
        this.itemsInStorage = new HashMap<>();
        locations = new HashMap<>();
        firstItemID = -1;
        productDAO = new ProductDAO();
        itemDAO = new ItemDAO();
        locationDAO = new LocationDAO();
    }


    public HashMap<Integer, Product> getProducts(){
        return this.products;
    }

    public void updateStock(int productId ,int storeQuantity, int storageQuantity) throws Exception{
        Product product=products.get(productId);
        if(product==null)
        {
            throw new Exception("no product with this id in the store");
        }
        else if (storeQuantity <0 || storageQuantity <0) {
            throw new Exception("Quantity invalid");
        } else {
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
    public void addProduct(Product product) throws SQLException {
        products.put(product.getMKT(),product);
        productDAO.addProduct(productDAO.getProductById(product.getMKT()));
    }
    public void applyProductDiscount(Product product, double percent, LocalDate startDate, LocalDate endDate){
        try{
            Discount d = discountFacade.builedProductDiscount(percent, startDate, endDate);
            product.putInSale();
            double finalPrice=product.getPriceBeforeDiscount();
            finalPrice=finalPrice-(finalPrice*percent/100);
            product.setDiscount(d);
            product.setPriceAfterDiscount(finalPrice);
            ProductDTO productDTO=new ProductDTO(product.getMKT(),product.getProductName(),product.getCompanyManufacturer(),
                    product.getCategory().getCategoryID(),product.getPriceBeforeDiscount(),product.getPriceAfterDiscount(),product.getSize()
                    ,product.getMinimumQuantity(),product.getStoreQuantity(),product.getStorageQuantity(),product.getLocation().getId(),product.getDiscount().getDiscountID());
            productDAO.update(productDTO);
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
            productDAO.getProductById(productID).setDiscountID(discount.getDiscountID());
            Product p1=products.get(productID);
            ProductDTO p2 = new ProductDTO(p1.getMKT(),p1.getProductName(), p1.getCompanyManufacturer(), p1.getCategory().getCategoryID(),p1.getPriceBeforeDiscount(),p1.getPriceBeforeDiscount(), p1.getSize(), p1.getMinimumQuantity(),p1.getStoreQuantity(),p1.getStorageQuantity(),p1.getLocation().getId(),p1.getDiscount().getDiscountID());
            productDAO.update(p2);
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
            if(product.getCategory() !=null) {
                if (product.getCategory().getCategoryName() != null && product.getCategory().getCategoryName().equals(categoryName)) {
                    ps.add(product);
                }
                else
                if (product.getCategory().getCategoryFather() != null && product.getCategory().getCategoryFather().getCategoryName().equals(categoryName)) {
                    ps.add(product);
                }
                else
                if(product.getCategory().getSubCategories() !=null) {
                    for (Category c : product.getCategory().getSubCategories()) {
                        if (c.getCategoryName().equals((categoryName))) {
                            ps.add(product);
                        }
                    }
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
                itemsInStorage.put(i.getItemID(),i);
                productDAO.getProductById(p1.getMKT()).setStorageQuantity(productDAO.getProductById(p1.getMKT()).getStorageQuantity()+1);
                if(p1.getDiscount()!=null) {
                    ProductDTO p2 = new ProductDTO(p1.getMKT(), p1.getProductName(), p1.getCompanyManufacturer(), p1.getCategory().getCategoryID(),
                            p1.getPriceBeforeDiscount(), p1.getPriceBeforeDiscount(), p1.getSize(), p1.getMinimumQuantity(), p1.getStoreQuantity(),
                            p1.getStorageQuantity(), p1.getLocation().getId(), p1.getDiscount().getDiscountID());
                    productDAO.update(p2);
                }
                else {
                    ProductDTO p2 = new ProductDTO(p1.getMKT(), p1.getProductName(), p1.getCompanyManufacturer(), p1.getCategory().getCategoryID(),
                            p1.getPriceBeforeDiscount(), p1.getPriceBeforeDiscount(), p1.getSize(), p1.getMinimumQuantity(), p1.getStoreQuantity(),
                            p1.getStorageQuantity(), p1.getLocation().getId(), -1);
                    productDAO.update(p2);
                }
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
//                    p1.setStorageQuantity(p1.getStorageQuantity() - 1);
//                    p1.setStoreQuantity(p1.getStorageQuantity() + 1);
                    itemsInStorage.remove(i);
                    itemsInStore.put(i.getItemID(),i);
//                    productDAO.getProductById(p1.getMKT()).setStoreQuantity(productDAO.getProductById(p1.getMKT()).getStoreQuantity()+1);

                    if(p1.getDiscount()!=null) {
                        ProductDTO p2 = new ProductDTO(p1.getMKT(), p1.getProductName(), p1.getCompanyManufacturer(), p1.getCategory().getCategoryID(),
                                p1.getPriceBeforeDiscount(), p1.getPriceBeforeDiscount(), p1.getSize(), p1.getMinimumQuantity(), p1.getStoreQuantity() + 1,
                                p1.getStorageQuantity() - 1, p1.getLocation().getId(), p1.getDiscount().getDiscountID());
                        productDAO.update(p2);
                    }
                    else {
                        ProductDTO p2 = new ProductDTO(p1.getMKT(), p1.getProductName(), p1.getCompanyManufacturer(), p1.getCategory().getCategoryID(),
                                p1.getPriceBeforeDiscount(), p1.getPriceBeforeDiscount(), p1.getSize(), p1.getMinimumQuantity(), p1.getStoreQuantity() + 1,
                                p1.getStorageQuantity() - 1, p1.getLocation().getId(), -1);
                        productDAO.update(p2);
                    }
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
//            p.setStorageQuantity(p.getStorageQuantity() - 1);
            itemsInStorage.remove(item.getItemID(), item);
            items.remove(itemID);
            itemDAO.delete(itemID);
//            ProductDTO p2 = productDAO.getProductById(p.getMKT());
            if (p.getDiscount()!=null) {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(), p.getStoreQuantity(), p.getStorageQuantity() -1, p.getLocation().getId(), p.getDiscount().getDiscountID());
                productDAO.update(p2);
            }
            else
            {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(), p.getStoreQuantity(), p.getStorageQuantity()-1, p.getLocation().getId(), -1);
                productDAO.update(p2);

            }
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
            itemDAO.delete(itemID);
            if (p.getDiscount()!=null) {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(), p.getStoreQuantity()-1, p.getStorageQuantity(), p.getLocation().getId(), p.getDiscount().getDiscountID());
                productDAO.update(p2);
            }
            else
            {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(), p.getStoreQuantity()-1, p.getStorageQuantity(), p.getLocation().getId(), -1);
                productDAO.update(p2);

            }
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
        Location l = new Location(locationId,description,section,shelf);
        locations.put(l.getId(),l);
        locationId=locationId+1;
        if(productName == null || companyManufacturer == null){
            throw new Exception("one of the names that been provided is invalid");
        }
        Product p = new Product(products.size() + 1, productName, companyManufacturer, price, size, minimumQuantity, 0, 0, c, l, null);
        products.put(p.getMKT(), p);
        LocationDTO l1 = new LocationDTO(l.getId(),description,section,shelf);
        locationDAO.addLocation(l1);
        if (p.getDiscount()!=null) {
            ProductDTO p1 = new ProductDTO(p.getMKT(),productName,companyManufacturer,c.getCategoryID(),price,price,size,minimumQuantity,0,0,l.getId(),p.getDiscount().getDiscountID());
            productDAO.addProduct(p1);

        }
        else
        {
            ProductDTO p1 = new ProductDTO(p.getMKT(),productName,companyManufacturer,c.getCategoryID(),price,price,size,minimumQuantity,0,0,l.getId(),-1);
            productDAO.addProduct(p1);

        }

    }
    public void buildItem(int prodID, LocalDate expirationDate, boolean inStore) throws Exception{
        if(products.get(prodID) == null){
            throw new Exception("Product is null");
        }
        if(expirationDate == null){
            throw new Exception("Expiration Date is null");
        }
        Item i = new Item(items.size() + 1, products.get(prodID), expirationDate, ItemCondition.GOOD);
        itemDAO.addItem(new ItemDTO(i.getItemID(),i.getProduct().getMKT(),i.getExpirationDate(),i.getCondition().toString()));
        items.put(i.getItemID(),i);

        Product p = i.getProduct();
        if(inStore){
            itemsInStore.put(i.getItemID(),i);
            if (p.getDiscount()!=null) {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(),
                        p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(),
                        p.getStoreQuantity()+1, p.getStorageQuantity(), p.getLocation().getId(),
                        p.getDiscount().getDiscountID());
                productDAO.update(p2);
            }
            else
            {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(),
                        p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(),
                        p.getMinimumQuantity(), p.getStoreQuantity()+1, p.getStorageQuantity(),
                        p.getLocation().getId(), -1);
                productDAO.update(p2);
            }
        }
        else{
            if (p.getDiscount()!=null) {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(), p.getStoreQuantity(), p.getStorageQuantity() +1, p.getLocation().getId(), p.getDiscount().getDiscountID());
                productDAO.update(p2);
            }
            else
            {
                ProductDTO p2 = new ProductDTO(p.getMKT(), p.getProductName(), p.getCompanyManufacturer(), p.getCategory().getCategoryID(), p.getPriceBeforeDiscount(), p.getPriceBeforeDiscount(), p.getSize(), p.getMinimumQuantity(), p.getStoreQuantity(), p.getStorageQuantity()+1, p.getLocation().getId(), -1);
                productDAO.update(p2);

            }
            itemsInStorage.put(i.getItemID(),i);
            i.getProduct().setStorageQuantity(i.getProduct().getStorageQuantity()+1);
        }
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
    public void loadData(HashMap<Integer, Category> categories, HashMap<Integer, Discount> discounts) throws SQLException {

        for (ProductDTO p: productDAO.getAllProducts()){
            for(LocationDTO l: locationDAO.getAllLocations()){
                locations.put(l.getLocationID(), new Location(l.getLocationID(),l.getDescription(),l.getSection(),l.getShelf()));
                if(discounts.size() !=0) {
                    if (l.getLocationID() == p.getLocationID()) {
                        products.put(p.getMKT(), new Product(p.getMKT(), p.getName(), p.getCompanyManufacturer(), (int) p.getPriceAfterDiscount()
                                , p.getSize(), p.getMinimumQuantity(), categories.get(p.getCategoryID()), locations.get(l.getLocationID()), discounts.get(p.getDiscountID())));
                    }
                }
                else
                {
                    if (l.getLocationID() == p.getLocationID()) {
                        products.put(p.getMKT(), new Product(p.getMKT(), p.getName(), p.getCompanyManufacturer(), (int) p.getPriceAfterDiscount()
                                , p.getSize(), p.getMinimumQuantity(), categories.get(p.getCategoryID()), locations.get(l.getLocationID()), null));
                    }
                }
            }

        }
    }
    public void deleteData() throws SQLException {
        productDAO.deleteData();
        products.clear();
        itemDAO.deleteData();
        items.clear();
        itemsInStore.clear();
        itemsInStorage.clear();
        locationDAO.deleteData();
        locations.clear();

    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }
}
