package ServiceLayer;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Fascades.PurchaseFacade;
import BusinessLayer.Objects.Item;

import java.time.LocalDate;

public class PurchaseService {
    private PurchaseFacade purchaseFacade;
    public ProductFacade productFascade;
    private DiscountFacade discountFacade;

    public PurchaseService(ProductFacade productFascade, DiscountFacade discountFacade ,PurchaseFacade purchaseFacade) {
        this.productFascade = productFascade;
        this.discountFacade = discountFacade;
        this.purchaseFacade = purchaseFacade;
    }
//    public PurchaseService() {
//        this.productFascade = new ProductFacade();
//        this.discountFacade = new DiscountFacade();
//        this.purchaseFacade = new PurchaseFacade(productFascade, discountFacade);
//    }

    public String calculateTotal(int purchaseId) throws Exception {
        try {
            double c = purchaseFacade.calculateTotal(purchaseId);
            return c+"";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addItem(int purchaseId, int prodID ,int storeId) throws Exception {
        try {
            purchaseFacade.addItem(purchaseId, prodID,storeId);
            return "added item";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public int getPurchaseIDByCustomerAndDate(int customerID, LocalDate purchaseDate) throws Exception {
        try {
            return purchaseFacade.getPurchaseIDByCustomerAndDate(customerID, purchaseDate);
        } catch (Exception e) {
            throw new Exception("Error occurred");
        }
    }

    public String buildPurchase(LocalDate purchaseDate, int customerID) throws Exception {
        try {
            purchaseFacade.buildPurchase(purchaseDate,customerID);
            return "Building purchase succeeded";
        } catch (Exception e) {
            throw new Exception("Error occurred");
        }
    }
}