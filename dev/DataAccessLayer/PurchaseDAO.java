package DataAccessLayer;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class PurchaseDAO {
    protected Connection connection(){
        String path = Paths.get("").toAbsolutePath().toString() ;
        String _connectionString = "jdbc:sqlite:../StockData.db";

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
    public PurchaseDAO() {
        this.connection = connection();
    }
    public PurchaseDAO(Connection connection) {
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
            stmt.execute("DELETE FROM Purchase");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public void addPurchase(PurchaseDTO purchaseDTO) throws SQLException {
        String query = "INSERT INTO Purchase (purchaseID, purchaseDate, total, customerID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, purchaseDTO.getPurchaseID());
            pstmt.setDate(2, Date.valueOf(purchaseDTO.getPurchaseDate())); // Convert LocalDate to java.sql.Date
            pstmt.setDouble(3, purchaseDTO.getTotal());
            pstmt.setInt(4, purchaseDTO.getCustomerID());
            pstmt.executeUpdate();
        }
    }

    public PurchaseDTO getPurchaseById(int id) throws SQLException {
        String query = "SELECT * FROM Purchase WHERE purchaseID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new PurchaseDTO(
                            rs.getInt("purchaseID"),
                            rs.getDate("purchaseDate").toLocalDate(),
                            rs.getDouble("total"),
                            rs.getInt("customerID")
                    );
                }
            }
        }
        return null;
    }

    public List<PurchaseDTO> getAllPurchases() throws SQLException {
        List<PurchaseDTO> purchases = new LinkedList<>();
        String query = "SELECT * FROM Purchase";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                purchases.add(new PurchaseDTO(
                        rs.getInt("purchaseID"),
                        rs.getDate("purchaseDate").toLocalDate(),
                        rs.getDouble("total"),
                        rs.getInt("customerID")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no purchases data");
        }
        return purchases;
    }

    public void update(PurchaseDTO purchaseDTO) throws SQLException {
        String query = "UPDATE Purchase SET purchaseDate = ?, total = ?, customerID = ? WHERE purchaseID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(purchaseDTO.getPurchaseDate())); // Convert LocalDate to java.sql.Date
            pstmt.setDouble(2, purchaseDTO.getTotal());
            pstmt.setInt(3, purchaseDTO.getCustomerID());
            pstmt.setInt(4, purchaseDTO.getPurchaseID());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Purchase WHERE purchaseID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    public void loadData()
    {

    }

}
