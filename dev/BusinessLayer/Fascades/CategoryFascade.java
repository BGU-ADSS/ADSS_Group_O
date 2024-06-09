package BusinessLayer.Fascades;

import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Discount;
import BusinessLayer.Objects.Product;

import java.time.LocalDate;
import java.util.*;

public class CategoryFascade {
    private ProductFacade productFascade;
    private DiscountFacade discountFacade;
    private HashMap<Integer, Category> categories;
    public CategoryFascade(DiscountFacade discountFacade, ProductFacade productFascade)
    {
        Category c = new Category(0,null,null,null);

        this.discountFacade=discountFacade;
        this.productFascade = productFascade;
        this.categories = new HashMap<>();
    }
//    public CategoryFascade()
//    {
//        this.productFascade = new ProductFacade();
//        this.discountFacade = new DiscountFacade();
//    }
    public List<Category> getCategories(){
        return (List<Category>) categories.values();
    }
    public void applyCategoryDiscount(Category category, double percent, LocalDate startDate, LocalDate endDate){
        try{
            Discount d = discountFacade.builedCategoryDiscount(percent, startDate, endDate);
            HashMap<Integer, Product> products = productFascade.getProducts();
            for (Product product : products.values()){
                productFascade.applyProductDiscount(product.getMKT(),d);
            }
        }
        catch (Exception e){

        }
    }
//    public void buildCategory(String categoryName, Category categoryFather) throws Exception {
//        if(categoryFather == null){
//            throw new Exception("Category father is null");
//        }
//        if(!categories.values().contains(categoryFather)){
//            throw new Exception("Category father doesn't exist");
//        }
//        int s = categories.size()+1;
//        Category c = new Category(s, categoryName ,null ,categoryFather);
//        categories.put(s, c);
//    }
//    public void buildCategory(String categoryName, List<Category> subCategories, Category categoryFather) throws Exception {
//        if(categoryFather == null){
//            throw new Exception("Category father is null");
//        }
//        if(!categories.values().contains(categoryFather)){
//            throw new Exception("Category father doesn't exist");
//        }
//        if(subCategories == null){
//            throw new Exception("Category father is null");
//        }
//        for (Category subCategory : subCategories){
//            if(!categories.values().contains(subCategory)){
//                throw new Exception("Sub category doesn't exist");
//            }
//        }
//        int s = categories.size()+1;
//        Category c = new Category(s, categoryName ,subCategories,categoryFather);
//        categories.put(s, c);
//    }
//    public void buildCategory(String categoryName, List<Category> subCategories) throws Exception{
//        if(subCategories == null){
//            throw new Exception("Category father is null");
//        }
//        for (Category subCategory : subCategories){
//            if(!categories.values().contains(subCategory)){
//                throw new Exception("Sub category doesn't exist");
//            }
//        }
//        int s = categories.size()+1;
//        Category c = new Category(s, categoryName ,subCategories ,null);
//        categories.put(s, c);
//    }
    public void setFatherCategory(int givenID, int fatherID) throws  Exception{
        if(givenID <= 0 || fatherID <= 0){
            throw new Exception("Invalid ids have been provided");
        }
        if(categories.get(givenID) == null || categories.get(fatherID) == null){
            throw new Exception("One or two of the categories you provided doesn't exist");
        }
        categories.get(givenID).setCategoryFather(categories.get(fatherID));
    }
    public void setSubCategories(int givenID, int...subs) throws  Exception{
        if(givenID <= 0){
            throw new Exception("Invalid id have been provided");
        }
        List<Category> subs1 = new LinkedList<>();
        for(int s: subs){
            if(!categories.values().contains(s)){
                throw new Exception("One of sub categories doesn't exist");
            }
            subs1.add(categories.get(s));
        }
        categories.get(givenID).setSubCategories(subs1);
    }
    public void buildCategory(String categoryName) throws Exception{
        int s = categories.size()+1;
        Category c = new Category(s, categoryName ,null ,categories.get(0));
        categories.put(s, c);
    }
    public Category getCategoryByName(String name) throws Exception{
        for(Category c: categories.values()){
            if(c.getCategoryName().equals(name)){
                return c;
            }
        }
        throw new Exception("Category with name " + name + " doesn't exist");
    }
}
