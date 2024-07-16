package DataAccessLayer.Categories;

import BusinessLayer.Objects.Category;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO {
    public static CategoryDAO getInstance() {
        return instance;
    }

    private static CategoryDAO instance = new CategoryDAO();
    protected Connection connection(){
        String path = Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:C:\\Users\\bhaah\\ADSS_Group_O\\dev\\StockData.db";

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
    private Connection connection;

    public CategoryDAO() {

        this.connection = connection();
    }
    // public CategoryDAO(Connection connection) {
    //     this.connection = connection;
    //}

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public void insert(CategoryDTO categoryDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO Category (categoryID,categoryName,fatherCategoryID) VALUES (?,?,?)");
            pstmt.setInt(1,categoryDTO.getCategoryID());
            pstmt.setString(2,categoryDTO.getCategoryName());
            pstmt.setInt(3,categoryDTO.getFatherCategoryID());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public List<CategoryDTO> getAllCategories() throws SQLException {
        List<CategoryDTO> categories = new LinkedList<>();
        String query = "SELECT * FROM Category";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new CategoryDTO(
                        rs.getInt("categoryID"),
                        rs.getString("categoryName"),
                        rs.getInt("fatherCategoryID")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no category data");
        }
        return categories;
    }
    public void changeCategoryName(String name,int id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE Category SET name=?  WHERE categoryID=?");
            pstmt.setString(1, name);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }


    public void update(CategoryDTO categoryDTO) throws SQLException {
        String query = "UPDATE Category SET categoryName = ?, fatherCategoryID = ? WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, categoryDTO.getCategoryName());
            pstmt.setInt(2, categoryDTO.getFatherCategoryID());
            pstmt.setInt(3, categoryDTO.getCategoryID());
            pstmt.executeUpdate();
        }
    }
//    public void changeCategoryFather(String name,int id) throws SQLException {
//        PreparedStatement pstmt = null;
//        try {
//            pstmt = connection.prepareStatement("UPDATE Categories SET fatherCategoryID=?  WHERE name=?");
//            pstmt.setString(1, namec);
//            pstmt.setString(2, id);
//            pstmt.executeUpdate();
//        } finally {
//            if (pstmt != null) {
//                pstmt.close();
//            }
//        }
//    }

//    public CategoryDTO getCategoryById(int id){
//
//    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Category WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    public void deleteCategory(int categoryId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DELETE FROM Category WHERE categoryID=?");
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }



    public CategoryDTO getCategoryById(int id) throws SQLException {
        String query = "SELECT * FROM Category WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CategoryDTO(
                            rs.getInt("categoryID"),
                            rs.getString("categoryName"),
                            rs.getInt("fatherCategoryID")
                    );
                }
            }
        }
        return null;
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
            stmt.execute("DELETE FROM Category");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadData() throws SQLException
    {
        for(CategoryDTO categoryDTO: getAllCategories())
        {

        }
    }

}