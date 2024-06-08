package BusinessLayer.Fascades;

import BusinessLayer.Objects.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReportFascade {
    private HashMap<Integer, Report> reports;
    private ProductFacade productFacade;
    private int counterID;
    public ReportFascade(ProductFacade productFacade){
        reports = new HashMap<>();
        counterID = 0;
        this.productFacade=productFacade;
    }
    public void addReport(Report report){
        reports.put(report.getID(),report);
    }
    public String buildReport(List<Category> categories) throws Exception{
        String ans = "";
        if(categories == null){
            throw new Exception("Categories are null");
        }
        HashMap<Item, ItemCondition> itemConditions=new HashMap<>();
        List<Product>  productNeeds=new LinkedList<>();
        for (Category category:categories)
        {
            try {
                List<Product> products= productFacade.getProductsByCategory(category.getCategoryName());
                for (Product product : products)
                {
                    if(!productFacade.checkMinimumStock(product.getMKT()))
                    {
                        productNeeds.add(product);
                    }
                }
                for(Item item:productFacade.getItemsInStore().values())
                {
                    if(products.contains(item.getProduct())){
                        if(item.getCondition()==ItemCondition.DEFECTIVE || item.getCondition()==ItemCondition.EXPIRED)
                        {
                            itemConditions.put(item,item.getCondition());
                            productFacade.removeItem(item.getItemID(),true);
                        }
                    }
                }
                for(Item item:productFacade.getItemsInStorage().values())
                {
                    if(products.contains(item.getProduct())){
                        if(item.getCondition()==ItemCondition.DEFECTIVE || item.getCondition()==ItemCondition.EXPIRED)
                        {
                            itemConditions.put(item,item.getCondition());
                            productFacade.removeItem(item.getItemID(),false);
                        }
                    }
                }
            }
            catch (Exception e){
               throw new Exception("Checking the minimum didn't work");
            }
        }
        Report r = new Report(counterID, LocalTime.now(), itemConditions ,productNeeds);
        addReport(r);
        ans = ans + "Report ID: " + counterID + "\nDate of report: " + r.getReportDate() + "\n";
//        System.out.println("Report ID: " + counterID + "\nDate of report: " + r.getReportDate() + "\n");
        for (Map.Entry<Item, ItemCondition> entry : itemConditions.entrySet()){
//            System.out.println("Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() +"\n");
            ans = ans + "Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() +"\n";
        }
        for (Product p : productNeeds){
//            System.out.println("The required product: " + p.getProductName() + "\n");
            ans = ans + "The required product: " + p.getProductName() + "\n";
        }
        counterID++;
        return ans;
    }
    public String buildReport(Category category) throws Exception{
        String ans = "";
        HashMap<Item, ItemCondition> itemConditions=new HashMap<>();
        List<Product>  productNeeds=new LinkedList<>();
        if(category == null){
            throw new Exception("Category is null");
        }
            try {
                List<Product> products= productFacade.getProductsByCategory(category.getCategoryName());
                for (Product product:products)
                {
                    if(!productFacade.checkMinimumStock(product.getMKT()))
                    {
                        productNeeds.add(product);
                    }
                }
                for(Item item:productFacade.getItemsInStore().values())
                {
                    if(products.contains(item.getProduct())){
                        if(item.getCondition()==ItemCondition.DEFECTIVE || item.getCondition()==ItemCondition.EXPIRED)
                        {
                            itemConditions.put(item,item.getCondition());
                            productFacade.removeItem(item.getItemID(),true);
                        }
                    }
                }
                for(Item item:productFacade.getItemsInStorage().values())
                {
                    if(products.contains(item.getProduct())){
                        if(item.getCondition()==ItemCondition.DEFECTIVE || item.getCondition()==ItemCondition.EXPIRED)
                        {
                            itemConditions.put(item,item.getCondition());
                            productFacade.removeItem(item.getItemID(),false);
                        }
                    }
                }
            }
            catch (Exception e){
                throw new Exception("Checking the minimum didn't work");
            }
        Report r = new Report(counterID, LocalTime.now(), itemConditions ,productNeeds);
        addReport(r);
        ans = ans + "Report ID: " + counterID + "\nDate of report: " + r.getReportDate() + "\n";
//        System.out.println("Report ID: " + counterID + "\nDate of report: " + r.getReportDate() + "\n");
        for (Map.Entry<Item, ItemCondition> entry : itemConditions.entrySet()){
//            System.out.println("Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() +"\n");
            ans = ans + "Item Information: ID = " + entry.getKey() + ", Item Condition: " + entry.getValue() +"\n";
        }
        for (Product p : productNeeds){
//            System.out.println("The required product: " + p.getProductName() + "\n");
            ans = ans + "The required product: " + p.getProductName() + "\n";
        }
        counterID++;
        return ans;
    }
}
