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
    public String buildReport(List<Category> categories ,int storeId){
        try {
            String ans = reportFacade.buildReport(categories,storeId);
            return ans;
        }
        catch(Exception e){
            return e.getMessage();
        }
    }
//    public String buildReport(List<Category> categories){
//        try {
//
//            String ans = reportFacade.buildReport(categories,storeId);
//            return ans;
//        }
//        catch(Exception e){
//            return e.getMessage();
//        }
//    }
    public String buildReport(Category category,int storeId) throws Exception {

        try {
            String ans = reportFacade.buildReport(category, storeId);
            return ans;
        }
        catch(Exception e){
            return e.getMessage();
        }
    }
    public  String buildShoratgeReport(int storeId) throws  Exception{
        try
        {
            return reportFacade.buildReportShortages(storeId);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
