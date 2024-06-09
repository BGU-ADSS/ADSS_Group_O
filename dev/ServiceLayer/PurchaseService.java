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

    public PurchaseService(ProductFacade productFascade, DiscountFacade discountFacade) {
        this.productFascade = productFascade;
        this.discountFacade = discountFacade;
        this.purchaseFacade = new PurchaseFacade(productFascade, discountFacade);
    }
    public PurchaseService() {
        this.productFascade = new ProductFacade();
        this.discountFacade = new DiscountFacade();
        this.purchaseFacade = new PurchaseFacade(productFascade, discountFacade);
    }

    public String calculateTotal(int purchaseId) throws Exception {
        try {
            double c = purchaseFacade.calculateTotal(purchaseId);
            return c+"";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addItem(int purchaseId, int prodID) throws Exception {
        try {
            purchaseFacade.addItem(purchaseId, prodID);
            return "added item";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public int getPurchaseIDByCustomerAndDate(String customerName, LocalDate purchaseDate) throws Exception {
        try {
            return purchaseFacade.getPurchaseIDByCustomerAndDate(customerName, purchaseDate);
        } catch (Exception e) {
            throw new Exception("Error occurred");
        }
    }

    public String buildPurchase(LocalDate purchaseDate, int customerID, String customerName) throws Exception {
        try {
            purchaseFacade.buildPurchase(purchaseDate,customerID,customerName);
            return "Building purchase succeeded";
        } catch (Exception e) {
            throw new Exception("Error occurred");
        }
    }
}