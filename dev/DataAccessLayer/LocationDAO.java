package DataAccessLayer;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class LocationDAO {
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
    public LocationDAO() {
        this.connection = connection();
    }
    public LocationDAO(Connection connection) {
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
            stmt.execute("DELETE FROM Location");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }




    public void addLocation(LocationDTO locationDTO) throws SQLException {
        String query = "INSERT INTO Location (locationID, description, section, shelf) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, locationDTO.getLocationID());
            pstmt.setString(2, locationDTO.getDescription());
            pstmt.setInt(3, locationDTO.getSection());
            pstmt.setInt(4, locationDTO.getShelf());
            pstmt.executeUpdate();
        }
    }

    public LocationDTO getLocationById(int id) throws SQLException {
        String query = "SELECT * FROM Location WHERE locationID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new LocationDTO(
                            rs.getInt("locationID"),
                            rs.getString("description"),
                            rs.getInt("section"),
                            rs.getInt("shelf")
                    );
                }
            }
        }
        return null;
    }


    //dont forget to check about statment
    public List<LocationDTO> getAllLocations() throws SQLException {
        List<LocationDTO> locations = new ArrayList<>();
        String query = "SELECT * FROM Location";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                locations.add(new LocationDTO(
                        rs.getInt("locationID"),
                        rs.getString("description"),
                        rs.getInt("section"),
                        rs.getInt("shelf")
                ));
            }
        }
        catch (Exception e){
            System.out.println("there are no locations data");
        }
        return locations;
    }



    public void update(LocationDTO locationDTO) throws SQLException {
        String query = "UPDATE Location SET description = ?, section = ?, shelf = ? WHERE locationID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, locationDTO.getDescription());
            pstmt.setInt(2, locationDTO.getSection());
            pstmt.setInt(3, locationDTO.getShelf());
            pstmt.setInt(4, locationDTO.getLocationID());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Location WHERE locationID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    public void loadData()
    {

    }

}
