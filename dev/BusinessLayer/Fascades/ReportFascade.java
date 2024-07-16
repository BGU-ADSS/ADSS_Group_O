package BusinessLayer.Fascades;

import BusinessLayer.Objects.*;
import DataAccessLayer.ItemReportDAO;
import DataAccessLayer.ItemReportDTO;
import DataAccessLayer.ReportProduct.ReportDAO;
import DataAccessLayer.ReportProduct.ReportDTO;
import DataAccessLayer.ReportProduct.ReportProductDAO;
import DataAccessLayer.ReportProduct.ReportProductDTO;

import java.awt.font.LineMetrics;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReportFascade {
    private HashMap<Integer, Report> reports;
    private ProductFacade productFacade;
    private int counterID;
    private ReportDAO reportDAO;
    private ReportProductDAO reportProductDAO;
    private ItemReportDAO itemReportDAO;

    public ReportFascade(ProductFacade productFacade) {
        reports = new HashMap<>();
        counterID = 0;
        this.productFacade = productFacade;
        reportDAO = new ReportDAO();
        reportProductDAO = new ReportProductDAO();
        itemReportDAO = new ItemReportDAO();
    }

    public void addReport(Report report) {
        reports.put(report.getID(), report);
    }

    public String buildReport(List<Category> categories,int storeId) throws Exception {
        String ans = "";
        if (categories == null) {
            throw new Exception("Categories are null");
        }
        HashMap<Item, ItemCondition> itemConditions = new HashMap<>();
        List<Product> productNeeds = new LinkedList<>();
        for (Category category : categories) {
            try {
                List<Product> products = productFacade.getProductsByCategory(category.getCategoryName(),storeId);
                if (products.size() == 0) {
                    return "No report of given categories";
                }
                for (Product product : products) {
                    if (!productFacade.checkMinimumStock(product.getMKT(),storeId)) {
                        ReportProductDTO rp = new ReportProductDTO(counterID, product.getMKT());
                        reportProductDAO.insert(rp);
                        productNeeds.add(product);
                    }
                }
                for (Item item : productFacade.getItemsInStore().values()) {
                    if (products.contains(item.getProduct())) {
                        if (item.getCondition() == ItemCondition.DEFECTIVE || item.getCondition() == ItemCondition.EXPIRED) {
                            ItemReportDTO rp = new ItemReportDTO(counterID, item.getItemID());
                            ItemReportDAO.insert(rp);
                            itemConditions.put(item, item.getCondition());
                            productFacade.removeItem(item.getItemID(), true,storeId);
                        }
                    }
                }
                for (Item item : productFacade.getItemsInStorage().values()) {
                    if (products.contains(item.getProduct())) {
                        if (item.getCondition() == ItemCondition.DEFECTIVE || item.getCondition() == ItemCondition.EXPIRED) {
                            ItemReportDTO rp = new ItemReportDTO(counterID, item.getItemID());
                            ItemReportDAO.insert(rp);
                            itemConditions.put(item, item.getCondition());
                            productFacade.removeItem(item.getItemID(), false,storeId);
                        }
                    }
                }
            } catch (Exception e) {
                throw new Exception("Checking the minimum didn't work");
            }
        }
        LocalDate n = LocalDate.now();
        ans = ans + "Report ID: " + counterID + "\nDate of report: " + n + "\n";
//        System.out.println("Report ID: " + counterID + "\nDate of report: " + r.getReportDate() + "\n");
        for (Map.Entry<Item, ItemCondition> entry : itemConditions.entrySet()) {
//            System.out.println("Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() +"\n");
            ans = ans + "Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() + "\n";
        }
        for (Product p : productNeeds) {
//            System.out.println("The required product: " + p.getProductName() + "\n");
            ans = ans + "The required product: " + p.getProductName() + "\n";
        }
        Report r = new Report(counterID, n, itemConditions, productNeeds, ans);
        ReportDTO rd = new ReportDTO(r.getID(), r.getReportDate(), r.getDescription());
        reportDAO.insert(rd);
        addReport(r);
        counterID++;
        return ans;
    }

    public String buildReport(Category category,int storeId) throws Exception {
        String ans = "";
        HashMap<Item, ItemCondition> itemConditions = new HashMap<>();
        List<Item> itemInStore = new LinkedList<>();
        List<Item> itemInStorage = new LinkedList<>();


        List<Product> productNeeds = new LinkedList<>();
        if (category == null) {

            throw new Exception("Category is null");
        }
        try {
            List<Product> products = productFacade.getProductsByCategory(category.getCategoryName(),storeId);
            if (products.size() == 0) {

                return "No report of category";
            }
            for (Product product : products) {

                if (!productFacade.checkMinimumStock(product.getMKT(),storeId)) {
                    ReportProductDTO rp = new ReportProductDTO(counterID, product.getMKT());
                    reportProductDAO.insert(rp);
                    productNeeds.add(product);
                }
            }
            for (Item item : productFacade.getItemsInStore().values()) {

                if (products.contains(item.getProduct())) {
//                        if(item.getCondition()==ItemCondition.DEFECTIVE || item.getCondition()==ItemCondition.EXPIRED)
//                        {
                    ItemReportDTO rp = new ItemReportDTO(counterID, item.getItemID());
                    ItemReportDAO.insert(rp);
                    itemConditions.put(item, item.getCondition());
                    itemInStore.add(item);
//                            productFacade.removeItem(item.getItemID(),true);
//                        }
                }
            }
            for (Item item : productFacade.getItemsInStorage().values()) {
                if (products.contains(item.getProduct())) {
//                        if(item.getCondition()==ItemCondition.DEFECTIVE || item.getCondition()==ItemCondition.EXPIRED)
//                        {
                    ItemReportDTO rp = new ItemReportDTO(counterID, item.getItemID());
                    ItemReportDAO.insert(rp);
                    itemConditions.put(item, item.getCondition());
                    itemInStorage.add(item);
//                            productFacade.removeItem(item.getItemID(),false);
//                        }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Checking the minimum didn't work");
        }
        LocalDate n = LocalDate.now();
        ans = ans + "Report ID: " + counterID + "\nDate of report: " + n + "\n";
//        System.out.println("Report ID: " + counterID + "\nDate of report: " + r.getReportDate() + "\n");

        for (Map.Entry<Item, ItemCondition> entry : itemConditions.entrySet()) {
//            System.out.println("Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() +"\n");
            if (itemInStore.contains(entry.getKey())) {
                ans = ans + "In Store: Item Information: ID = " + entry.getKey().getItemID() + ", Item Condition: " + entry.getValue() + "\n";
            } else {
                ans = ans + "In Storage: Item Information: ID = " + entry.getKey().getItemID() + ", Item Condition: " + entry.getValue() + "\n";
            }
        }
        for (Product p : productNeeds) {
//            System.out.println("The required product: " + p.getProductName() + "\n");
            ans = ans + "The required product: " + p.getProductName() + "\n";
        }
        Report r = new Report(counterID, n, itemConditions, productNeeds, ans);
        addReport(r);
        ReportDTO rd = new ReportDTO(r.getID(), r.getReportDate(), r.getDescription());
        reportDAO.insert(rd);
        counterID++;
        return ans;
    }

    public String buildReportShortages(int storeId) throws Exception {
        String ans = "";
        if (productFacade.getProducts() == null || productFacade.getProducts().size() == 0) {
            throw new Exception("there are no products yet");
        }
        if(productFacade.getProducts().get(storeId)==null)
        {
            throw new Exception("storeId invalid");
        }
        HashMap<Item, ItemCondition> itemConditions = new HashMap<>();
        List<Product> productNeeds = new LinkedList<>();
        for (Product product : productFacade.getProducts().get(storeId).values()) {
            if (!productFacade.checkMinimumStock(product.getMKT(),storeId)) {
                ReportProductDTO rp = new ReportProductDTO(counterID, product.getMKT());
                reportProductDAO.insert(rp);
                productNeeds.add(product);
            }
        }
        LocalDate n = LocalDate.now();
        ans = ans + "Report ID: " + counterID + "\nDate of report: " + n + "\n";
        for (Product p : productNeeds) {
//            System.out.println("The required product: " + p.getProductName() + "\n");
            ans = ans + "The required product: " + p.getProductName() + "\n";
        }
        Report r = new Report(counterID, n, itemConditions, productNeeds, ans);
        addReport(r);
        ReportDTO rd = new ReportDTO(r.getID(), r.getReportDate(), r.getDescription());
        reportDAO.insert(rd);
        counterID++;
        return ans;

    }

    public void loadData() throws SQLException {
        HashMap<Integer, List<Product>> rp = new HashMap<>();
        for (ReportProductDTO r : reportProductDAO.getAllReportProducts()) {
            if (rp.containsKey(r.getReportID())) {
                for (HashMap<Integer, Product> reportProduct : productFacade.getProducts().values()) {
                    if(reportProduct.get(r.getProductID()) != null)
                    {
                        rp.get(r.getReportID()).add(reportProduct.get(r.getProductID()));
                    }
                }
            } else {
                List<Product> prod = new LinkedList<>();
                for (HashMap<Integer, Product> reportProduct : productFacade.getProducts().values()) {
                    if(reportProduct.get(r.getProductID()) != null)
                    {
                        prod.add(reportProduct.get(r.getProductID()));
                    }
                }
                rp.put(r.getReportID(), prod);
            }
        }


        HashMap<Integer, HashMap<Item, ItemCondition>> Reportitems = new HashMap<>();
        for (ItemReportDTO r : itemReportDAO.getAllItemReports()) {
            if (Reportitems.containsKey(r.getReportID())) {

            } else {
                HashMap<Item, ItemCondition> itemConditionHashMap = new HashMap<>();
                Item it = productFacade.getItems().get(r.getItemID());
                itemConditionHashMap.put(it, it.getCondition());
                Reportitems.put(r.getReportID(), itemConditionHashMap);
            }
        }
        for (ReportDTO reportDTO : reportDAO.getAllReports()) {
            reports.put(reportDTO.getReportID(), new Report(reportDTO.getReportID(), reportDTO.getReportDate(), Reportitems.get(reportDTO.getReportID()), rp.get(reportDTO.getReportID()), reportDTO.getDescription()));
        }
    }

    public void deleteData() throws SQLException {
        reportDAO.deleteData();
        reportProductDAO.deleteData();
        itemReportDAO.deleteData();
        reports.clear();
    }
}

