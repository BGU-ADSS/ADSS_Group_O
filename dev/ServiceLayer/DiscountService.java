package ServiceLayer;
import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Discount;
import BusinessLayer.Objects.Product;

import java.time.LocalDate;

public class DiscountService {
    private DiscountFacade discountFacade;
    private ProductFacade productFacade;
    private CategoryFascade categoryFascade;
    public DiscountService(DiscountFacade discountFacade,ProductFacade productFacade ,CategoryFascade categoryFascade){
        this.discountFacade = discountFacade;
        this.productFacade = productFacade;
        this.categoryFascade = categoryFascade;
    }
    public DiscountFacade getDiscountFacade(){
        return discountFacade;
    }

    public String builedProductDiscount(double percent, LocalDate startDate, LocalDate endDate) throws Exception{
        try {
            discountFacade.builedProductDiscount(percent, startDate, endDate);
            return "Product discount built";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public String builedCategoryDiscount(double percent, LocalDate startDate, LocalDate endDate) throws Exception{
        try {
            discountFacade.builedCategoryDiscount(percent, startDate, endDate);
            return "Category discount built";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public String applyProductDiscount(int productID, Discount discount ,int storeId){
        try {
            productFacade.applyProductDiscount(productID, storeId,discount);
            return "Applied product discount";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public String applyCategoryDiscount(Category category, double percent, LocalDate startDate, LocalDate endDate,int storeId){
        try {
            categoryFascade.applyCategoryDiscount(category,percent, startDate, endDate,storeId);
            return "Applied category discount";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    public Discount getDiscountByProductID(int prodID) throws Exception{
        try {
            return discountFacade.getDiscountByProductID(prodID);
        }
        catch (Exception e){
            throw new Exception("Discount is not found with given product ID");
        }
    }
}
