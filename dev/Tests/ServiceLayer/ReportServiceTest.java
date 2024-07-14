package ServiceLayer;

import static org.junit.Assert.*;
import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Fascades.ReportFascade;


import org.junit.Assert;
import org.junit.Before;

class ReportServiceTest {
    private static ReportService reportService;
    private ProductService productService;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
    private static CategoryFascade categoryFascade;
    private static ReportFascade reportFascade;


    @Before
    static void setUp() {
        productFacade = new ProductFacade();
        discountFacade = new DiscountFacade();
        categoryFascade = new CategoryFascade(discountFacade,productFacade);
        reportFascade=new ReportFascade(productFacade);
        reportService = new ReportService(productFacade,reportFascade);
    }
    @org.junit.Test
    void buildReport() throws Exception {
        productService = new ProductService(productFacade, discountFacade, categoryFascade);
        productService.buildCategory("Cola");
        String res = reportService.buildReport(productService.getCategoryByName("Cola"));
        assertEquals("No report of category", res);
    }



}