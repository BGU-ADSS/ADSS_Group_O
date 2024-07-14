package DataAccessLayer.ReportProduct;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
public class ReportProductDAO {
    protected Connection connection(){
        String path = Paths.get("").toAbsolutePath().toString() ;
        String _connectionString = "jdbc:sqlite:" + path+"\\StockData.db";
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
    public ReportProductDAO() {
        this.connection = connection();
    }
    public ReportProductDAO(Connection connection) {
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
            stmt.execute("DELETE FROM ReportProduct");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }




    public void insert(ReportProductDTO reportProductDTO) throws SQLException {
        String query = "INSERT INTO ReportProduct (reportID, productID) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportProductDTO.getReportID());
            pstmt.setInt(2, reportProductDTO.getProductID());
            pstmt.executeUpdate();
        }
    }

    public ReportProductDTO getReportProductByReportIdAndProductId(int reportID, int productID) throws SQLException {
        String query = "SELECT * FROM ReportProduct WHERE reportID = ? AND productID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportID);
            pstmt.setInt(2, productID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ReportProductDTO(
                            rs.getInt("reportID"),
                            rs.getInt("productID")
                    );
                }
            }
        }
        return null;
    }

    public List<ReportProductDTO> getReportProductsByReportId(int reportID) throws SQLException {
        List<ReportProductDTO> reportProducts = new LinkedList<>();
        String query = "SELECT * FROM ReportProduct WHERE reportID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reportProducts.add(new ReportProductDTO(
                            rs.getInt("reportID"),
                            rs.getInt("productID")
                    ));
                }
            }
        }
        return reportProducts;
    }

    public List<ReportProductDTO> getReportProductsByProductId(int productID) throws SQLException {
        List<ReportProductDTO> reportProducts = new LinkedList<>();
        String query = "SELECT * FROM ReportProduct WHERE productID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reportProducts.add(new ReportProductDTO(
                            rs.getInt("reportID"),
                            rs.getInt("productID")
                    ));
                }
            }
        }
        return reportProducts;
    }

    public void deleteByReportIdAndProductId(int reportID, int productID) throws SQLException {
        String query = "DELETE FROM ReportProduct WHERE reportID = ? AND productID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportID);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();
        }
    }

    public void deleteByReportId(int reportID) throws SQLException {
        String query = "DELETE FROM ReportProduct WHERE reportID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportID);
            pstmt.executeUpdate();
        }
    }


    public List<ReportProductDTO> getAllReportProducts() throws SQLException {
        List<ReportProductDTO> reportProducts = new LinkedList<>();
        String query = "SELECT * FROM ReportProduct";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reportProducts.add(new ReportProductDTO(
                        rs.getInt("reportID"),
                        rs.getInt("productID")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no reportProduct data");
        }
        return reportProducts;
    }


    public void deleteByProductId(int productID) throws SQLException {
        String query = "DELETE FROM ReportProduct WHERE productID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productID);
            pstmt.executeUpdate();
        }
    }
    public void loadData()
    {

    }

}
