package ServiceLayer;

import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Fascades.PurchaseFacade;
import BusinessLayer.Fascades.ReportFascade;
import BusinessLayer.Objects.Category;

import java.util.List;

public class ReportService {
    private ReportFascade reportFacade;
    private ProductFacade productFacade;
    public ReportService(ProductFacade productFacade ,ReportFascade reportFascade ) {
        this.productFacade =  productFacade;
        this.reportFacade = reportFascade;
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
    public  String buildShoratgeReport() throws  Exception{
        try
        {
            return reportFacade.buildReportShortages();
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
