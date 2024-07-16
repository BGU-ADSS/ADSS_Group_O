package DataAccessLayer.ReportProduct;
import BusinessLayer.Objects.Product;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
public class ProductDAO {
    protected Connection connection(){
        String path = Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:dev\\StockData.db";


        Connection connection=null;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection
            connection = DriverManager.getConnection(_connectionString);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        return connection;
    }
    public ProductDAO() {
        this.connection = connection();
    }
    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        try {
            String path = new File("StockData.db").getAbsolutePath();
            DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteData() throws SQLException{
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Product");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO Product (MKT, productName, companyManufacturer, categoryID, priceBeforeDiscount, priceAfterDiscount, size, minimumQuantity, storageQuantity,storeQuantity , discountID, locationID, storeId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, product.getMKT());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getCompanyManufacturer());
            pstmt.setInt(4, product.getCategory().getCategoryID());
            pstmt.setDouble(5, product.getPriceBeforeDiscount());
            pstmt.setDouble(6, product.getPriceAfterDiscount());
            pstmt.setInt(7, product.getSize());
            pstmt.setInt(8, product.getMinimumQuantity());
            pstmt.setInt(9, product.getStoreQuantity());
            pstmt.setInt(10, product.getStorageQuantity());
            pstmt.setInt(11, product.getLocation().getId());
            pstmt.setInt(12, product.getDiscount().getDiscountID());
            pstmt.setInt(13, product.getStoreId());

            pstmt.executeUpdate();
        }
    }

    public void addProduct(ProductDTO productDTO) throws SQLException {
        String query = "INSERT INTO Product (MKT, productName, companyManufacturer, categoryID, priceBeforeDiscount, priceAfterDiscount, size, minimumQuantity,storageQuantity , storeQuantity, discountID , locationID , storeId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productDTO.getMKT());
            pstmt.setString(2, productDTO.getName());
            pstmt.setString(3, productDTO.getCompanyManufacturer());
            pstmt.setInt(4, productDTO.getCategoryID());
            pstmt.setDouble(5, productDTO.getPriceBeforeDiscount());
            pstmt.setDouble(6, productDTO.getPriceAfterDiscount());
            pstmt.setInt(7, productDTO.getSize());
            pstmt.setInt(8, productDTO.getMinimumQuantity());
            pstmt.setInt(9, productDTO.getStoreQuantity());
            pstmt.setInt(10, productDTO.getStorageQuantity());
            pstmt.setInt(11, productDTO.getDiscountID());
            pstmt.setInt(12, productDTO.getLocationID());
            pstmt.setInt(13, productDTO.getStoreId());

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
        }
    }

    public ProductDTO getProductById(int id) throws SQLException {
        String query = "SELECT * FROM Product WHERE MKT = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ProductDTO(
                            rs.getInt("MKT"),
                            rs.getString("productName"),
                            rs.getString("companyManufacturer"),
                            rs.getInt("categoryID"),
                            rs.getDouble("priceBeforeDiscount"),
                            rs.getDouble("priceAfterDiscount"),
                            rs.getInt("size"),
                            rs.getInt("minimumQuantity"),
                            rs.getInt("storageQuantity"),
                            rs.getInt("storeQuantity"),
                            rs.getInt("discountID"),
                            rs.getInt("locationID"),
                            rs.getInt("storeId")
                    );
                }
            }
        }
        return null;
    }

    public List<ProductDTO> getAllProducts() throws SQLException {
        List<ProductDTO> products = new LinkedList<>();

        String query = "SELECT * FROM Product";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (!rs.isBeforeFirst()) { // Check if the ResultSet is empty
                System.out.println("No data found. Loading initial data.");
            } else {
                while (rs.next()) {
                    products.add(new ProductDTO(
                            rs.getInt("MKT"),
                            rs.getString("productName"),
                            rs.getString("companyManufacturer"),
                            rs.getInt("categoryID"),
                            rs.getDouble("priceBeforeDiscount"),
                            rs.getDouble("priceAfterDiscount"),
                            rs.getInt("size"),
                            rs.getInt("minimumQuantity"),
                            rs.getInt("storageQuantity"),
                            rs.getInt("storeQuantity"),
                            rs.getInt("discountID"),
                            rs.getInt("locationID"),
                            rs.getInt("storeId")
                    ));
                }
            }
        }
        catch (Exception e){
            System.out.println("there are no products data");
        }

        return products;
    }

    public void update(ProductDTO productDTO) throws SQLException {
        String query = "UPDATE Product SET productName = ?, companyManufacturer = ?, categoryID = ?, priceBeforeDiscount = ?, priceAfterDiscount = ?, size = ?, minimumQuantity = ?, storageQuantity = ?, storeQuantity = ?, locationID = ?, storeId = ?, discountID = ? WHERE MKT = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, productDTO.getName());
            pstmt.setString(2, productDTO.getCompanyManufacturer());
            pstmt.setInt(3, productDTO.getCategoryID());
            pstmt.setDouble(4, productDTO.getPriceBeforeDiscount());
            pstmt.setDouble(5, productDTO.getPriceAfterDiscount());
            pstmt.setInt(6, productDTO.getSize());
            pstmt.setInt(7, productDTO.getMinimumQuantity());
            pstmt.setInt(8, productDTO.getStorageQuantity());
            pstmt.setInt(9, productDTO.getStoreQuantity());
            pstmt.setInt(10, productDTO.getLocationID());
            pstmt.setInt(11, productDTO.getStoreId());

            pstmt.setInt(12, productDTO.getDiscountID());
            pstmt.setInt(13, productDTO.getMKT());
            pstmt.executeUpdate();
        }
    }
    public void update(Product product) throws SQLException {
        String query = "UPDATE Product SET productName = ?, companyManufacturer = ?, categoryID = ?, priceBeforeDiscount = ?, priceAfterDiscount = ?, size = ?, minimumQuantity = ?, storageQuantity = ?, storeQuantity = ?, locationID = ?, storeId = ?, discountID = ? WHERE MKT = ? AND storeId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getCompanyManufacturer());
            pstmt.setInt(3, product.getCategory().getCategoryID());
            pstmt.setDouble(4, product.getPriceBeforeDiscount());
            pstmt.setDouble(5, product.getPriceAfterDiscount());
            pstmt.setInt(6, product.getSize());
            pstmt.setInt(7, product.getMinimumQuantity());
            pstmt.setInt(8, product.getStorageQuantity());
            pstmt.setInt(9, product.getStoreQuantity());
            pstmt.setInt(10, product.getLocation().getId());

            pstmt.setInt(11, product.getDiscount().getDiscountID());
            pstmt.setInt(12, product.getMKT());
            pstmt.setInt(13, product.getStoreId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id,int storeid) throws SQLException {
        String query = "DELETE FROM Product WHERE MKT = ? AND storeId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, storeid);

            pstmt.executeUpdate();
        }
    }

    public void loadData() {
    }
}
