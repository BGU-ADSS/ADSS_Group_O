package PresentationLayer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import BusinessLayer.Objects.Category;
import ServiceLayer.DiscountService;
import ServiceLayer.ProductService;
import ServiceLayer.PurchaseService;
import ServiceLayer.ReportService;

public class MainController {

    private ProductService productService;
    private DiscountService discountService;
    private ReportService reportService;
    private PurchaseService purchaseService;
    private int itemCounter;
    public MainController(){
        productService = new ProductService();
        discountService = new DiscountService();
        reportService = new ReportService();
        purchaseService = new PurchaseService();
        itemCounter = 0;
    }
    public void start() throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int input = -1;
        if(LocalDate.now().getDayOfWeek().equals(0)){

            System.out.println(reportService.buildReport(productService.getCategories()));
        }
        while(true){
            System.out.println("Enter number for action:\n0 for Exit\n1 for adding category\n2 for adding product\n3 for adding item\n4 for adding to store,\n5 for adding to storage\n" +
                    "6 for searching for products by category\n7 fot updating stock\n8 for building product discount\n9 for building category discount\n10 for applying discount on a product\n" +
                    "11 for applying discount on a category\n12 for calculating the purchase\n13 for building a report\n14 for removing item\n15 for adding item to purchase" +
                    "\n16 for building a purchase");

            input = Integer.parseInt(reader.readLine());
            if(input == 0){
                System.out.println("You are exiting....");
                break;
            }
            if(input == 1){
                System.out.println("Is it father or sub category or neither one?\n");
                String kind = reader.readLine();
                if(kind.equals("neither")){
                    System.out.println("Enter name: ");
                    String name = reader.readLine();
                    System.out.println(productService.buildCategory(name));
                }else
                if(kind.equals("father")){
                    System.out.println("Enter name: ");
                    String father = reader.readLine();
                    System.out.println("Enter sub category name: ");
                    String name1 = reader.readLine();
                    productService.buildCategory(father);
                    productService.getCategoryByName(name1).setCategoryFather(productService.getCategoryByName(father));
                    List<Category> l = new LinkedList<Category>();
                    l.add(productService.getCategoryByName(name1));
                    productService.getCategoryByName(father).setSubCategories(l);
                }else
                if(kind.equals("sub category")){
                    System.out.println("Enter name: ");
                    String name = reader.readLine();
                    while(!name.equals("nothing")){
                        System.out.println(productService.buildCategory(name));

                    }
                }else{
                    System.out.println("Invalid category kind entered, Try again");
                }
            }
            if(input == 2){
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                System.out.println("Enter company manufacturer name: ");
                String com = reader.readLine();
                System.out.println("Enter price: ");
                int price = Integer.parseInt(reader.readLine());
                System.out.println("Enter size: ");
                int size = Integer.parseInt(reader.readLine());
                System.out.println("Enter minimum quantity: ");
                int min = Integer.parseInt(reader.readLine());
                System.out.println("Enter category name: ");
                String cname = reader.readLine();
                System.out.println("Enter location description");
                String dname = reader.readLine();
                System.out.println("Enter location section");
                int sname = Integer.parseInt(reader.readLine());
                System.out.println("Enter location shelf");
                int shname = Integer.parseInt(reader.readLine());
                System.out.println(productService.buildProduct(name,com,price,size,min,productService.getCategoryByName(cname),dname,sname,shname));
            }
            if(input == 3){
                System.out.println("Enter expiration date: ");
                String userInput = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(userInput, dateFormat);
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                System.out.println(productService.buildItem(productService.getProductIDByName(name), date));
            }
            if(input == 4){
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                System.out.println(productService.addToStore(productService.getFirstItemByProductID(productService.getProductIDByName(name))));
            }
            if(input == 5){
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                System.out.println(productService.addToStorage(productService.getFirstItemByProductID(productService.getProductIDByName(name))));
            }
            if(input == 6){
                System.out.println("Enter Category name: ");
                String name = reader.readLine();
                productService.getProductsByCategory(name);

            }
            if(input == 7){
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                productService.getProductIDByName(name);
                System.out.println("Enter quantity for storage: ");
                int s1 = Integer.parseInt(reader.readLine());
                System.out.println("Enter quantity for store: ");
                int s2 = Integer.parseInt(reader.readLine());
                System.out.println(productService.updateStock(productService.getProductIDByName(name), s2, s1));
            }
            if(input == 8){
                System.out.println("Enter discount percentage: ");
                double per = Double.parseDouble(reader.readLine());
                System.out.println("Enter start date: ");
                String sd = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = LocalDate.parse(sd, dateFormat);
                System.out.println("Enter end date: ");
                String ed = reader.readLine();
                LocalDate endDate = LocalDate.parse(ed, dateFormat);
                System.out.println(discountService.builedProductDiscount(per, startDate, endDate));
            }
            if(input == 9)
                System.out.println("Enter category name: ");
                String name = reader.readLine();
                productService.getCategoryByName(name);
                System.out.println("Enter discount percentage: ");
                double per = Double.parseDouble(reader.readLine());
                System.out.println("Enter start date: ");
                String sd = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = LocalDate.parse(sd, dateFormat);
                System.out.println("Enter end date: ");
                String ed = reader.readLine();
                LocalDate endDate = LocalDate.parse(ed, dateFormat);
            System.out.println(discountService.builedCategoryDiscount(per,startDate,endDate));
            }
            if(input == 10){
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                System.out.println(discountService.applyProductDiscount(productService.getProductIDByName(name),discountService.getDiscountByProductID(productService.getProductIDByName(name))));
            }
            if(input == 11){
                System.out.println("Enter category name: ");
                String name = reader.readLine();
                System.out.println("Enter discount percentage: ");
                double per = Double.parseDouble(reader.readLine());
                System.out.println("Enter start date: ");
                String sd = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = LocalDate.parse(sd, dateFormat);
                System.out.println("Enter end date: ");
                String ed = reader.readLine();
                LocalDate endDate = LocalDate.parse(ed, dateFormat);
                System.out.println(discountService.applyCategoryDiscount(productService.getCategoryByName(name), per,startDate,endDate));
            }
            if(input == 12){
                System.out.println("Enter customer name: ");
                String name = reader.readLine();
                System.out.println("Enter purchase date: ");
                String pd = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate purchaseDate = LocalDate.parse(pd, dateFormat);
                System.out.println(purchaseService.calculateTotal(purchaseService.getPurchaseIDByCustomerAndDate(name,purchaseDate)));
            }
            if(input == 13){
                System.out.println("Enter category name: ");
                String name = reader.readLine();
                System.out.println(reportService.buildReport(productService.getCategoryByName(name)));
            }
            if(input == 14){
                System.out.println("Enter Product name: ");
                String name = reader.readLine();
                System.out.println("From Store or Storage: ");
                String name1 = reader.readLine();
                if(name1.equals("Store")){
                    System.out.println(productService.removeItem(productService.getFirstItemByProductID(productService.getProductIDByName(name)), true));
                }
                else
                if(name1.equals("Storage")){
                    System.out.println(productService.removeItem(productService.getFirstItemByProductID(productService.getProductIDByName(name)), false));
                }
                else{
                    System.out.println("You typed neither store nor storage, try again");
                }
            }
            if (input == 15){
                System.out.println("Enter customer name: ");
                String name = reader.readLine();
                System.out.println("Enter purchase date: ");
                String pd = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate purchaseDate = LocalDate.parse(pd, dateFormat);
                System.out.println("Enter product name: ");
                String pname = reader.readLine();
                System.out.println(purchaseService.addItem(purchaseService.getPurchaseIDByCustomerAndDate(name,purchaseDate), productService.getProductIDByName(pname)));
            }
            if(input == 16){
                System.out.println("Enter purchase date: ");
                String pd = reader.readLine();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate purchaseDate = LocalDate.parse(pd, dateFormat);
                System.out.println("Enter customer ID: ");
                int id = Integer.parseInt(reader.readLine());
                System.out.println("Enter customer name: ");
                String name = reader.readLine();
                System.out.println(purchaseService.buildPurchase(purchaseDate,id,name));
            }
    }
}
