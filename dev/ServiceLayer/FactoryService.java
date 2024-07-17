package ServiceLayer;

import BusinessLayer.Fascades.*;
import BusinessLayer.Objects.Category;

public class FactoryService {
    private ProductService productService;
    private DiscountService discountService;
    private ReportService reportService;
    private PurchaseService purchaseService;

    private ProductFacade productFacade;
    private PurchaseFacade purchaseFacade;
    private DiscountFacade discountFacade;
    private CategoryFascade categoryFascade;
    private ReportFascade reportFascade;
    public FactoryService(){
        productFacade = new ProductFacade();
        discountFacade = new DiscountFacade();
        purchaseFacade = new PurchaseFacade(productFacade, discountFacade);
        categoryFascade = new CategoryFascade(discountFacade, productFacade);
        reportFascade = new ReportFascade(productFacade);
        productService = new ProductService(productFacade, discountFacade, categoryFascade);
        discountService = new DiscountService(discountFacade, productFacade, categoryFascade);
        reportService = new ReportService(productFacade, reportFascade);
        purchaseService = new PurchaseService(productFacade, discountFacade, purchaseFacade);
    }

    public ProductService getProductService() {
        return productService;
    }

    public DiscountService getDiscountService() {
        return discountService;
    }

    public PurchaseService getPurchaseService() {
        return purchaseService;
    }

    public ReportService getReportService() {
        return reportService;
    }
}
