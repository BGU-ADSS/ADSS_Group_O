package ServiceLayer;
import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.Product;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class ProductService {
    public ProductFacade productFascade;
    private DiscountFacade discountFacade;
    private CategoryFascade categoryFascade;

    /*public ProductService() {
        this.productFascade = new ProductFacade();
        this.discountFacade = new DiscountFacade();
        this.categoryFascade = new CategoryFascade(discountFacade, productFascade);
    }*/
    public ProductService(ProductFacade productFacade, DiscountFacade discountFacade, CategoryFascade categoryFascade) {
        this.productFascade = productFacade;
        this.discountFacade = discountFacade;
        this.categoryFascade = categoryFascade;
    }
    public ProductFacade getProductFascade(){
        return productFascade;
    }
    public List<Category> getCategories(){
        return categoryFascade.getCategories();
    }

    public CategoryFascade getCategoryFascade() {
        return categoryFascade;
    }

    public String removeItem(int itemID, boolean inStore , int storeId) throws Exception {
        try {
            productFascade.removeItem(itemID, inStore, storeId);
            if(inStore) {
                return "Item removed from store successfully";
            }
            return "Item removed from storage successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addToStorage(int itemID ,int storeId) throws Exception {
        try {
            productFascade.addToStorage(itemID,storeId);
            return "Item added to storage successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addToStore(int itemID,int storeId) throws Exception {
        try {
            productFascade.addToStore(itemID,storeId);
            return "Item added to store successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<Product> getProductsByCategory(String categoryName ,int storeId) throws Exception {
        try {
            List<Product> products = productFascade.getProductsByCategory(categoryName,storeId);

            return products;
        } catch (Exception e) {
//            return e.getMessage();
            return null;
        }
    }

    public String checkMinimumStock(int productId,int storeId) throws Exception {
        try {
            boolean isMin = productFascade.checkMinimumStock(productId,storeId);
            if (isMin) {
                return "true";
            }
            return "false";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String updateStock(int productId, int storeQuantity, int storageQuantity,int storeId) throws Exception {
        try {
            productFascade.updateStock(productId, storeQuantity, storageQuantity,storeId);
            return "Stock updated successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String setFatherCategory(int givenID, int fatherID) throws Exception {
        try {
            categoryFascade.setFatherCategory(givenID, fatherID);
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String setSubCategories(int givenID, int... subs) throws Exception {
        try {
            categoryFascade.setSubCategories(givenID, subs);
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String buildCategory(String categoryName) throws Exception {
        try {
            categoryFascade.buildCategory(categoryName);
            return "Category built successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Category getCategoryByName(String name) throws Exception {
        try {
            Category c = categoryFascade.getCategoryByName(name);
            return c;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public int getProductIDByName(String name,int storeId) throws Exception {
        try {
            int p = productFascade.getProductIDByName(name,storeId);
            return p;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String buildProduct(String productName, String companyManufacturer, int price, int size, int minimumQuantity, Category c, String description,int section,int shelf,int storeId) throws Exception {
        try {
            productFascade.buildProduct(productName, companyManufacturer, price, size, minimumQuantity, c, description, section, shelf,storeId);
            return "Product built successfully";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String buildItem(int prodID, LocalDate expirationDate, boolean inStore,int storeId) throws Exception{
        try {
            productFascade.buildItem(prodID, expirationDate, inStore,storeId);
//            productFascade.addToStorage(productFascade.getFirstItemByProductID(prodID));
            return "Item built successfully";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public int getFirstItemByProductID(int prodID) throws Exception{
        try {
            return productFascade.getFirstItemByProductID(prodID);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
