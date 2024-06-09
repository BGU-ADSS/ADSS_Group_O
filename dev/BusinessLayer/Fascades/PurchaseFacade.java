package BusinessLayer.Fascades;

import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.Product;
import BusinessLayer.Objects.Purchase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PurchaseFacade {

    private int purchaseCounter;
    private ProductFacade productFacade;
    private DiscountFacade discountFacade;
    private HashMap<Integer, Purchase> purchases;

    public PurchaseFacade(ProductFacade productFacade, DiscountFacade discountFacade)
    {
        this.purchaseCounter=0;
        this.productFacade=productFacade;
        this.discountFacade=discountFacade;
        purchases=new HashMap<>();
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
//                productFacade.removeItem(itemID,true);
                if(t == null){
                    throw new Exception("Error occurred");
                }
                if(!purchases.get(purchaseId).getProducts().containsKey(p)){
                    List<Item> l = new LinkedList<>();
                    l.add(t);
                    purchases.get(purchaseId).getProducts().put(p,l);
                }
                else{
                    purchases.get(purchaseId).getProducts().get(p).add(t);
                }
            }
        }
//        try {
//            calculateTotal(purchaseId);
//        }
//        catch (Exception e) {
//
//        }
    }
    public void buildPurchase(LocalDate purchaseDate, int customerID, String customerName) throws Exception{
        if(customerName == null){
            throw new Exception("Customer name is invalid");
        }
        Purchase p = new Purchase(purchases.size() + 1, purchaseDate, customerID, customerName);
        purchases.put(p.getPurchaseID(), p);
    }
    public int getPurchaseIDByCustomerAndDate(String customerName, LocalDate purchaseDate) throws Exception{
        for(HashMap.Entry<Integer, Purchase> p : purchases.entrySet()){
            if(p.getValue().getCustomerName().equals(customerName) && p.getValue().getPurchaseDate().equals(purchaseDate)){
                return p.getKey();
            }
        }
        throw new Exception("The person name and purchase date are not found in list of purchases");
    }
}
