package DataAccessLayer.Categories;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
public class CategorySubsDAO {
    protected Connection connection(){
        String path = Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:" + path+ "\\StockData.db";

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
    public CategorySubsDAO() {
        this.connection = connection();
    }
    public CategorySubsDAO(Connection connection) {
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
            stmt.execute("DELETE FROM CategorySubs");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }





    public void insert(CategorySubsDTO categorySubsDTO) throws SQLException {
        String query = "INSERT INTO CategorySubs (categoryID, subCategoryName, subCategoryID) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, categorySubsDTO.getCategoryID());
            pstmt.setString(2, categorySubsDTO.getCategoryName());
            pstmt.setInt(3, categorySubsDTO.getSubCategoryID());
            pstmt.executeUpdate();
        }
    }

    public CategorySubsDTO getCategorySubById(int categoryID, int subCategoryID) throws SQLException {
        String query = "SELECT * FROM CategorySubs WHERE categoryID = ? AND subCategoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, categoryID);
            pstmt.setInt(2, subCategoryID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CategorySubsDTO(
                            rs.getInt("categoryID"),
                            rs.getString("subCategoryName"),
                            rs.getInt("subCategoryID")
                    );
                }
            }
        }
        return null;
    }

    public List<CategorySubsDTO> getAllCategorySubs() throws SQLException {
        List<CategorySubsDTO> categorySubs = new LinkedList<>();
        String query = "SELECT * FROM CategorySubs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categorySubs.add(new CategorySubsDTO(
                        rs.getInt("categoryID"),
                        rs.getString("subCategoryName"),
                        rs.getInt("subCategoryID")
                ));
            }
        }
        return categorySubs;
    }

    public List<CategorySubsDTO> getSubCategoriesByCategoryId(int categoryID) throws SQLException {
        List<CategorySubsDTO> subCategories = new LinkedList<>();
        String query = "SELECT * FROM CategorySubs WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, categoryID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    subCategories.add(new CategorySubsDTO(
                            rs.getInt("categoryID"),
                            rs.getString("subCategoryName"),
                            rs.getInt("subCategoryID")
                    ));
                }
            }
        }
        return subCategories;
    }

    public void update(CategorySubsDTO categorySubsDTO) throws SQLException {
        String query = "UPDATE CategorySubs SET categoryName = ? WHERE categoryID = ? AND subCategoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, categorySubsDTO.getCategoryName());
            pstmt.setInt(2, categorySubsDTO.getCategoryID());
            pstmt.setInt(3, categorySubsDTO.getSubCategoryID());
            pstmt.executeUpdate();
        }
    }

    public void delete(int categoryID, int subCategoryID) throws SQLException {
        String query = "DELETE FROM CategorySubs WHERE categoryID = ? AND subCategoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, categoryID);
            pstmt.setInt(2, subCategoryID);
            pstmt.executeUpdate();
        }
    }

    public void loadData()
    {

    }
}
