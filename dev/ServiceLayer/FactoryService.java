package ServiceLayer;

import BusinessLayer.Fascades.*;
import BusinessLayer.Objects.Category;

public class FactoryService {
    private ProductFacade productFacade;
    private CategoryFascade categoryFascade;
    private DiscountFacade discountFacade;
    private PurchaseFacade purchaseFacade;
    private ReportFascade reportFascade;
    public FactoryService(){

    }
}
