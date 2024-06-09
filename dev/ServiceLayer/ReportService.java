package ServiceLayer;

import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Fascades.PurchaseFacade;
import BusinessLayer.Fascades.ReportFascade;
import BusinessLayer.Objects.Category;

import java.util.List;

public class ReportService {
    private ReportFascade reportFacade;
    private ProductFacade productFacade;
    public ReportService() {
        productFacade =  new ProductFacade();
        this.reportFacade = new ReportFascade(productFacade);
    }
    public String buildReport(List<Category> categories){
        try {
            String ans = reportFacade.buildReport(categories);
            return ans;
        }
        catch(Exception e){
            return e.getMessage();
        }
    }
    public String buildReport(Category category) throws Exception {

        try {
            String ans = reportFacade.buildReport(category);
            return ans;
        }
        catch(Exception e){
            return e.getMessage();
        }
    }
}
