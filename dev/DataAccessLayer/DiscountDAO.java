package DataAccessLayer;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
public class DiscountDAO {
    protected Connection connection(){
        String path = Paths.get("").toAbsolutePath().toString() ;
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
    public DiscountDAO() {
        this.connection = connection();
    }
    public DiscountDAO(Connection connection) {
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
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DELETE FROM Discount");
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }






    public void buildDiscount(DiscountDTO discountDTO) throws SQLException {
        String query = "INSERT INTO Discount (discountID, percent, startDate, endDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, discountDTO.getDiscountID());
            pstmt.setDouble(2, discountDTO.getPercent());
            pstmt.setDate(3, java.sql.Date.valueOf(discountDTO.getStartDate())); // Convert LocalDate to java.sql.Date
            pstmt.setDate(4, java.sql.Date.valueOf(discountDTO.getEndDate())); // Convert LocalDate to java.sql.Date
            pstmt.executeUpdate();
        }
    }

    public DiscountDTO getDiscountById(int id) throws SQLException {
        String query = "SELECT * FROM Discount WHERE discountID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DiscountDTO(
                            rs.getInt("discountID"),
                            rs.getDouble("percent"),
                            rs.getDate("startDate").toLocalDate(),
                            rs.getDate("endDate").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    public List<DiscountDTO> getAllDiscounts() throws SQLException {
        List<DiscountDTO> discounts = new LinkedList<>();
        String query = "SELECT * FROM Discount";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                discounts.add(new DiscountDTO(
                        rs.getInt("discountID"),
                        rs.getDouble("percent"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate()
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no discounts data");
        }
        return discounts;
    }

    public void update(DiscountDTO discountDTO) throws SQLException {
        String query = "UPDATE Discount SET percent = ?, startDate = ?, endDate = ? WHERE discountID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, discountDTO.getPercent());
            pstmt.setDate(2, java.sql.Date.valueOf(discountDTO.getStartDate())); // Convert LocalDate to java.sql.Date
            pstmt.setDate(3,  java.sql.Date.valueOf(discountDTO.getEndDate())); // Convert LocalDate to java.sql.Date
            pstmt.setInt(4, discountDTO.getDiscountID());
            pstmt.executeUpdate();
        }
    }


    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Discount WHERE discountID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
