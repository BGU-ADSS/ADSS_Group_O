package BusinessLayer.Objects;

import java.util.List;

public class Category {
    private int categoryID;
    private String categoryName;
    private List<Category> subCategories;
    private Category categoryFather;
    public Category(int categoryID, String categoryName, List<Category> subCategories, Category categoryFather){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.subCategories = subCategories;
        this.categoryFather = categoryFather;
    }

    public Category getCategoryFather() {
        return categoryFather;
    }
    public List<Category> getSubCategories(){
        return subCategories;
    }
    public void setCategoryFather(Category father){
        this.categoryFather = father;
    }
    public void setSubCategories(List<Category> subs){
        this.subCategories = subs;
    }
    public String getCategoryName(){
        return this.categoryName;
    }
}
