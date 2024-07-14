package BusinessLayer.Fascades;

import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.ItemCondition;
import BusinessLayer.Objects.Product;
import BusinessLayer.Objects.Purchase;
import DataAccessLayer.ItemDAO;
import DataAccessLayer.ItemDTO;
import DataAccessLayer.PurchaseDAO;
import DataAccessLayer.PurchaseDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PurchaseFacade {
    private int purchaseCounter;
    private ProductFacade productFacade;
    private DiscountFacade discountFacade;
    private HashMap<Integer, Purchase> purchases;
    private PurchaseDAO purchaseDAO;
    private ItemDAO itemDAO;

    public PurchaseFacade(ProductFacade productFacade, DiscountFacade discountFacade)
    {
        this.purchaseCounter=0;
        this.productFacade=productFacade;
        this.discountFacade=discountFacade;
        purchases=new HashMap<>();
        purchaseDAO = new PurchaseDAO();
        itemDAO=new ItemDAO();
    }
    public double calculateTotal(int purchaseId) throws Exception{
        Purchase purchase=purchases.get(purchaseId);
        if(purchase==null)
        {
            throw new Exception("no purchase with this id");
        }
        for (HashMap.Entry<Product,List<Item>> entry : purchase.getProducts().entrySet()) {
            Product product=entry.getKey();
//            purchase.setTotal(purchase.getTotal() + entry.getValue().size() * entry.getKey().getPriceAfterDiscount());

            if(product.getOnsale())
            {
                if(!product.getDiscount().getEndDate().isAfter(LocalDate.now()))
                {
                    purchase.setTotal(purchase.getTotal() + entry.getValue().size() * product.getPriceAfterDiscount());
                }
                else
                {
                    product.EndInSale();
                    purchase.setTotal(purchase.getTotal() + entry.getValue().size() * product.getPriceBeforeDiscount());
                }
            }
            else
            {
                purchase.setTotal(purchase.getTotal() + entry.getValue().size() * product.getPriceBeforeDiscount());
            }
        }
        purchaseDAO.getPurchaseById(purchaseId).setTotal(purchase.getTotal());
        PurchaseDTO purchaseDTO=new PurchaseDTO(purchase.getPurchaseID(),purchase.getPurchaseDate(), purchase.getTotal(), purchase.getCustomerID());
        purchaseDAO.update(purchaseDTO);
        return purchase.getTotal();

    }
    public void addItem(int purchaseId, int prodID) throws Exception{
        Purchase purchase=purchases.get(purchaseId);
        if(purchase==null)
        {
            throw new Exception("no purchase with this id");
        }
        for (Product p : productFacade.getProducts().values()) {
            if(p.getMKT() == prodID){
                int itemID = productFacade.getFirstItemByProductID(prodID);
                Item t = null;
                for (Item i : productFacade.getItems().values()){
                    if(i.getItemID() == itemID){
                        t = i;
                    }
                }
                if(t == null){
                    throw new Exception("Error occurred");
                }
                if(t.getCondition()== ItemCondition.SOLD)
                {
                    throw new Exception("cannot add sold item");
                }
                if(!purchases.get(purchaseId).getProducts().containsKey(p)){
                    List<Item> l = new LinkedList<>();
                    l.add(t);
                    purchases.get(purchaseId).getProducts().put(p,l);
                }
                else{
                    purchases.get(purchaseId).getProducts().get(p).add(t);
                }
                t.setCondition(ItemCondition.SOLD);
                itemDAO.update(new ItemDTO(t.getItemID(),t.getProduct().getMKT(),t.getExpirationDate(),t.getCondition().toString(),purchaseId));
                Purchase purchase1=purchases.get(purchaseId);
                double total=calculateTotal(purchaseId);
                purchase1.setTotal(total);
                purchaseDAO.update(new PurchaseDTO(purchase1.getPurchaseID(),purchase1.getPurchaseDate(),purchase1.getTotal(),purchase1.getCustomerID()));

            }
        }
//        try {
//            calculateTotal(purchaseId);
//        }
//        catch (Exception e) {
//
//        }
    }
    public void buildPurchase(LocalDate purchaseDate, int customerID) throws Exception{
        if(customerID < 0){
            throw new Exception("Customer id is invalid");
        }
        Purchase p = new Purchase(purchases.size() + 1, purchaseDate, customerID);
        purchases.put(p.getPurchaseID(), p);
        PurchaseDTO p1 = new PurchaseDTO(p.getPurchaseID(),purchaseDate,0,customerID);
        purchaseDAO.addPurchase(p1);
    }
    public int getPurchaseIDByCustomerAndDate(int customerID, LocalDate purchaseDate) throws Exception{
        for(HashMap.Entry<Integer, Purchase> p : purchases.entrySet()){
            if(p.getValue().getCustomerID() == customerID && p.getValue().getPurchaseDate().equals(purchaseDate)){
                return p.getKey();
            }
        }
        throw new Exception("The person name and purchase date are not found in list of purchases");
    }
    public void loadData() throws SQLException {
        for(PurchaseDTO p : purchaseDAO.getAllPurchases()){
            purchases.put(p.getPurchaseID(), new Purchase(p.getPurchaseID(), p.getPurchaseDate(), p.getCustomerID()));
        }
    }
    public void deleteData() throws SQLException {
        purchaseDAO.deleteData();
        if(purchases!=null) {
            purchases.clear();
        }
    }
    public Purchase getPurchase(int id)
    {
        return purchases.get(id);
    }
}
