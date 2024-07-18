package DataAccessLayer.ReportProduct;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
public class ReportDAO {
    protected Connection connection(){
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
    public ReportDAO() {
        this.connection = connection();
    }
    public ReportDAO(Connection connection) {
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
            stmt.execute("DELETE FROM Report");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public void insert(ReportDTO reportDTO) throws SQLException {
        String query = "INSERT INTO Report (reportID, reportDate, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reportDTO.getReportID());
            pstmt.setDate(2, Date.valueOf(reportDTO.getReportDate()));
            pstmt.setString(3, reportDTO.getDescription());
            pstmt.executeUpdate();
        }
    }

    public ReportDTO getReportById(int id) throws SQLException {
        String query = "SELECT * FROM reports WHERE reportID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ReportDTO(
                            rs.getInt("reportID"),
                            rs.getDate("reportDate").toLocalDate(),
                            rs.getString("description")
                    );
                }
            }
        }
        return null;
    }

    public List<ReportDTO> getAllReports() throws SQLException {
        List<ReportDTO> reports = new LinkedList<>();
        String query = "SELECT * FROM Report";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new ReportDTO(
                        rs.getInt("reportID"),
                        rs.getDate("reportDate").toLocalDate(),
                        rs.getString("description")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no reports data");
        }
        return reports;
    }

    public void update(ReportDTO reportDTO) throws SQLException {
        String query = "UPDATE Report SET reportDate = ? WHERE reportID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(reportDTO.getReportDate())); // Convert LocalDate to java.sql.Date
            pstmt.setInt(2, reportDTO.getReportID());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Report WHERE reportID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    public void loadData()
    {

    }

}
