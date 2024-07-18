package DataAccessLayer;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class ItemDAO {
    //We should edit insert!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //public static ItemDAO getInstance() {
    //  return instance;
    //}
    //private static ItemDAO instance = new ItemDAO();
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    protected Connection connection2(){
        String path = Paths.get("").toAbsolutePath().toString() ;
        String _connectionString = "jdbc:sqlite:StockData.db";

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
    public ItemDAO() {
        this.connection = connection2();
    }
    private Connection connection;

    public void addItem(ItemDTO itemDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO Item (itemID,productID,expirationDate,itemCondition,purchaseID) VALUES (?,?,?,?,?)");
            pstmt.setInt(1, itemDTO.getItemID());
            pstmt.setInt(2, itemDTO.getProductID());
            pstmt.setDate(3, Date.valueOf(itemDTO.getExpirationDate()));
            pstmt.setString(4, itemDTO.getItemCondition());
            pstmt.setInt(5, itemDTO.getPurchaseID());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }


    public ItemDTO getItemById(int id) throws SQLException {
        String query = "SELECT * FROM Item WHERE itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ItemDTO(
                            rs.getInt("itemID"),
                            rs.getInt("productID"),
                            rs.getDate("expirationDate").toLocalDate(),
                            rs.getString("itemCondition"),
                            rs.getInt("purchaseID")
                    );
                }
            }
        }
        return null;
    }


    public List<ItemDTO> getAllItems() throws SQLException {
        List<ItemDTO> items = new ArrayList<>();
        String query = "SELECT * FROM Item";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(new ItemDTO(
                        rs.getInt("itemID"),
                        rs.getInt("productID"),
                        rs.getDate("expirationDate").toLocalDate(),
                        rs.getString("itemCondition"),
                        rs.getInt("purchaseID")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no items data");
        }
        return items;
    }

    public void update(ItemDTO itemDTO) throws SQLException {
        String query = "UPDATE Item SET productID = ?, expirationDate = ?, itemCondition = ?, purchaseID = ? WHERE itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, itemDTO.getProductID());
            pstmt.setDate(2, Date.valueOf(itemDTO.getExpirationDate()));
            pstmt.setString(3, itemDTO.getItemCondition());
            pstmt.setInt(4, itemDTO.getItemID());
            pstmt.setInt(5, itemDTO.getPurchaseID());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Item WHERE itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<ItemDTO> getItemsByCondition(String condition) throws SQLException {
        List<ItemDTO> items = new ArrayList<>();
        String query = "SELECT * FROM Item WHERE itemCondition = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, condition);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new ItemDTO(
                            rs.getInt("itemID"),
                            rs.getInt("productID"),
                            rs.getDate("expirationDate").toLocalDate(),
                            rs.getString("itemCondition"),
                            rs.getInt("purchaseID")
                    ));
                }
            }
        }
        return items;
    }

    public List<ItemDTO> getItemsByProductId(int productId) throws SQLException {
        List<ItemDTO> items = new ArrayList<>();
        String query = "SELECT * FROM Item WHERE productID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new ItemDTO(
                            rs.getInt("itemID"),
                            rs.getInt("productID"),
                            rs.getDate("expirationDate").toLocalDate(),
                            rs.getString("itemCondition"),
                            rs.getInt("purchaseID")
                    ));
                }
            }
        }
        return items;
    }



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
            stmt.execute("DELETE FROM Item");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void loadData()
    {

    }

}
