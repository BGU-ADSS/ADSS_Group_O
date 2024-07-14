package ServiceLayer;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;

class ReportServiceTest {
    private static ReportService reportService;
    private ProductService productService;
    @Before
    static void setUp() {
        reportService = new ReportService();
    }
    @org.junit.Test
    void buildReport() throws Exception {
        productService = new ProductService();
        productService.buildCategory("Cola");
        String res = reportService.buildReport(productService.getCategoryByName("Cola"));
        assertEquals("No report of category", res);
    }



}