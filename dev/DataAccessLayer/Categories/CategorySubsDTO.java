package DataAccessLayer.Categories;

public class CategorySubsDTO {
    private int categoryID;
    private String categoryName;
    private int subCategoryID;
    public CategorySubsDTO(int categoryID, String categoryName, int subCategoryID){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.subCategoryID = subCategoryID;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public String getCategoryName(){
        return categoryName;
    }
    public int getSubCategoryID(){
        return subCategoryID;
    }
}
