package DataAccessLayer.Categories;

import BusinessLayer.Objects.Category;

import java.util.List;

public class CategoryDTO {
    private int categoryID;
    private String categoryName;
    private int fatherCategoryID;
    public CategoryDTO(int categoryID, String categoryName, int fatherCategoryID){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.fatherCategoryID = fatherCategoryID;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public String getCategoryName(){
        return categoryName;
    }
    public int getFatherCategoryID(){
        return fatherCategoryID;
    }

    public void setFatherCategoryID(int fatherCategoryID) {
        this.fatherCategoryID = fatherCategoryID;
    }
}
