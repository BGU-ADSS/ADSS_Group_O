package DataAccessLayer;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemReportDAO {
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
    public ItemReportDAO() {
        this.connection = connection();
    }
    public ItemReportDAO(Connection connection) {
        this.connection = connection;
    }
    private static Connection connection;
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
            stmt.execute("DELETE FROM ItemConditionReport");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void insert(ItemReportDTO itemReportDTO) throws SQLException {
        String query = "INSERT INTO ItemConditionReport (ItemID, reportID) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, itemReportDTO.getItemID());
            pstmt.setInt(2, itemReportDTO.getReportID());
            pstmt.executeUpdate();
        }
    }
    public ItemReportDTO getItemReportById(int reportID, int itemID) throws SQLException {
        String query = "SELECT * FROM ItemConditionReport WHERE reportID = ? AND itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportID);
            pstmt.setInt(2, itemID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ItemReportDTO(
                            rs.getInt("reportID"),
                            rs.getInt("itemID")
                    );
                }
            }
        }
        return null;
    }

    public List<ItemReportDTO> getAllItemReports() throws SQLException {
        List<ItemReportDTO> itemReports = new ArrayList<>();
        String query = "SELECT * FROM ItemConditionReport";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                itemReports.add(new ItemReportDTO(
                        rs.getInt("reportID"),
                        rs.getInt("itemID")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no itemsReport data");
        }
        return itemReports;
    }

    public void update(ItemReportDTO itemReport) throws SQLException {
        String query = "UPDATE ItemConditionReport SET itemID = ? WHERE reportID = ? AND itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, itemReport.getItemID());
            pstmt.setInt(2, itemReport.getReportID());
            pstmt.setInt(3, itemReport.getItemID());
            pstmt.executeUpdate();
        }
    }

    public void delete(int reportID, int itemID) throws SQLException {
        String query = "DELETE FROM ItemConditionReport WHERE reportID = ? AND itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportID);
            pstmt.setInt(2, itemID);
            pstmt.executeUpdate();
        }
    }
}
