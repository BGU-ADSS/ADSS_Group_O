package BusinessLayer.Fascades;

import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Discount;
import BusinessLayer.Objects.Product;
import DataAccessLayer.Categories.CategoryDAO;
import DataAccessLayer.Categories.CategoryDTO;
import DataAccessLayer.Categories.CategorySubsDAO;
import DataAccessLayer.Categories.CategorySubsDTO;
import DataAccessLayer.ReportProduct.ProductDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class CategoryFascade {
    private ProductFacade productFascade;
    private DiscountFacade discountFacade;
    private HashMap<Integer, Category> categories;
    private CategoryDAO categoryDAO;
    private CategorySubsDAO categorySubsDAO;
    public CategoryFascade(DiscountFacade discountFacade, ProductFacade productFascade)
    {
        Category c = new Category(0,null,null,null);

        this.discountFacade=discountFacade;
        this.productFascade = productFascade;
        this.categories = new HashMap<>();
        this.categoryDAO=new CategoryDAO();
        this.categorySubsDAO=new CategorySubsDAO();
    }
    //    public CategoryFascade()
//    {
//        this.productFascade = new ProductFacade();
//        this.discountFacade = new DiscountFacade();
//    }
    public List<Category> getCategories(){
        return (List<Category>) categories.values();
    }
    public HashMap<Integer,Category> getCatohriess()
    {
        return categories;
    }
    public void applyCategoryDiscount(Category category, double percent, LocalDate startDate, LocalDate endDate){
        try{
            Discount d = discountFacade.builedCategoryDiscount(percent, startDate, endDate);
            HashMap<Integer, Product> products = productFascade.getProducts();
            for (Product product : products.values()){
                productFascade.applyProductDiscount(product.getMKT(),d);
                productFascade.getProductDAO().getProductById(product.getMKT()).setDiscountID(d.getDiscountID());
                Product p1=productFascade.getProducts().get(product.getMKT());
                ProductDTO p2 = new ProductDTO(p1.getMKT(),p1.getProductName(), p1.getCompanyManufacturer(), p1.getCategory().getCategoryID(),p1.getPriceBeforeDiscount(),p1.getPriceBeforeDiscount(), p1.getSize(), p1.getMinimumQuantity(),p1.getStoreQuantity(),p1.getStorageQuantity(),p1.getLocation().getId(),p1.getDiscount().getDiscountID());
                productFascade.getProductDAO().update(p2);
            }
        }
        catch (Exception e){

        }
    }
    public void buildCategory(String categoryName, Category categoryFather) throws Exception {
        if(categoryFather == null){
            throw new Exception("Category father is null");
        }
        if(!categories.values().contains(categoryFather)){
            throw new Exception("Category father doesn't exist");
        }
        int s = categories.size()+1;
        Category c = new Category(s, categoryName ,null ,categoryFather);
        categories.put(s, c);
        CategoryDTO c1 = new CategoryDTO(c.getCategoryID(),categoryName,categoryFather.getCategoryID());
        categoryDAO.insert(c1);
    }
    public void buildCategory(String categoryName, List<Category> subCategories, Category categoryFather) throws Exception {
        if(categoryFather == null){
            throw new Exception("Category father is null");
        }
        if(!categories.values().contains(categoryFather)){
            throw new Exception("Category father doesn't exist");
        }
        if(subCategories == null){
            throw new Exception("Category father is null");
        }
        for (Category subCategory : subCategories){
            if(!categories.values().contains(subCategory)){
                throw new Exception("Sub category doesn't exist");
            }
        }
        int s = categories.size()+1;
        Category c = new Category(s, categoryName ,subCategories,categoryFather);
        categories.put(s, c);
    }
    public void buildCategory(String categoryName, List<Category> subCategories) throws Exception{
        if(subCategories == null){
            throw new Exception("Category father is null");
        }
        for (Category subCategory : subCategories){
            if(!categories.values().contains(subCategory)){
                throw new Exception("Sub category doesn't exist");
            }
        }
        int s = categories.size()+1;
        Category c = new Category(s, categoryName ,subCategories ,null);
        categories.put(s, c);
        categoryDAO.insert(new CategoryDTO(c.getCategoryID(),c.getCategoryName(),-1));
        for (Category c1: subCategories){
            c1.setCategoryFather(c);
            CategoryDTO c2 = new CategoryDTO(c1.getCategoryID(),c1.getCategoryName(),c.getCategoryID());
            categoryDAO.update(c2);
        }
    }
    public void setFatherCategory(int givenID, int fatherID) throws  Exception{
        if(givenID <= 0 || fatherID <= 0){
            throw new Exception("Invalid ids have been provided");
        }
        if(categories.get(givenID) == null || categories.get(fatherID) == null){
            throw new Exception("One or two of the categories you provided doesn't exist");
        }
        categories.get(givenID).setCategoryFather(categories.get(fatherID));
//        dataBaseController.getCategoryById(givenID).setFatherCategoryID(fatherID);
        categoryDAO.getCategoryById(givenID).setFatherCategoryID(fatherID);
        Category c=categories.get(givenID);
        CategoryDTO cdt=new CategoryDTO(c.getCategoryID(), c.getCategoryName(),fatherID);
        categoryDAO.update(cdt);
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
        for(Category s: subs1){
            CategoryDTO cdt= new CategoryDTO(s.getCategoryID(),s.getCategoryName(),givenID);
            categoryDAO.update(cdt);
        }

    }
    public void buildCategory(String categoryName) throws Exception{
        int s = categories.size()+1;
        Category c = new Category(s, categoryName ,null ,new Category(-1,null,null,null));
        categories.put(s, c);
        CategoryDTO cd = new CategoryDTO(c.getCategoryID(),categoryName,-1);
        categoryDAO.insert(cd);

    }
    public Category getCategoryByName(String name) throws Exception{
        for(Category c: categories.values()){
            if(c.getCategoryName().equals(name)){
                return c;
            }
        }
        throw new Exception("Category with name " + name + " doesn't exist");
    }

    public void loadData() throws SQLException
    {
        HashMap<Category,Integer> categoryFatherHashMap=new HashMap<>();
        for(CategoryDTO categoryDTO:categoryDAO.getAllCategories()) {
            Category c=new Category(categoryDTO.getCategoryID(),categoryDTO.getCategoryName(),null,null);
            categoryFatherHashMap.put(c,categoryDTO.getFatherCategoryID());
            categories.put(c.getCategoryID(),c);
        }

        for(HashMap.Entry<Category,Integer> cs: categoryFatherHashMap.entrySet()){
            if(cs.getValue() >=0){
                Category father=categories.get(cs.getValue());

                cs.getKey().setCategoryFather(father);
                List<Category> subs=father.getSubCategories();
                subs.add(cs.getKey());
                father.setSubCategories(subs);
            }
        }

        /*for(CategoryDTO categoryDTO:categoryDAO.getAllCategories()) {
            if(categoryDTO.getFatherCategoryID() >= 0){
                for(HashMap.Entry<Category,Integer> cs: categoryFatherHashMap.entrySet()){
                    if(cs.getValue() == categoryDTO.getFatherCategoryID()){

                    }
                }
            }
        }
        for(CategoryDTO categoryDTO:categoryDAO.getAllCategories())
        {
            List<Category> subs = new LinkedList<>();
            for(CategorySubsDTO categorySubsDTO:categorySubsDAO.getAllCategorySubs()) {
                if(categoryDTO.getCategoryID() == categorySubsDTO.getCategoryID()){
                    subs.add(new Category(categorySubsDTO.getSubCategoryID(),categorySubsDTO.getCategoryName(),null,categoryDTO.));
                }

            }
            categories.put(categoryDTO.getCategoryID(), new Category(categoryDTO.getCategoryID(), categoryDTO.getCategoryName(), categorySubsDAO.))
        }*/
    }

    public void deleteData() throws SQLException {
        categoryDAO.deleteData();
        categories.clear();
    }
}