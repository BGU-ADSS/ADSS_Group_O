package DataAccessLayer.DAOs;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataAccessLayer.DTOs.DTO;

public abstract class Controller {
    // for each child class we have to :
    // in the constructor we have to set the table name
    // implement the function getObjectDTOFromOneResult that convert from result to
    // DTO object
    // we have to add the names of the columns as a static members

    private String url = "jdbc:sqlite:dev\\DataAccessLayer\\ADSS_DB_EMPLOYEE_MODULE.db";

    private String tableName;
    private List<String> columnNamesSet;

    public void updateData


    public void deleteDTO(HashMap<String,Object> toDelIdentiferMap) {
        String sql = "DELETE FROM " + tableName + " WHERE " + getIDForDTO(toDel);
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setValuesToPrepareStatment(toDel, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllData(){
        String sql = "DELETE FROM " + tableName ;
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    }

    public void insertDTO(DTO toInsert) {
        String sql = "INSERT INTO " + tableName + getTheRestOfInsertQuery(toInsert);
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setTheValuesToTheInsertQuery(pstmt, toInsert);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<DTO> getAllDTOs() {
        String sql = "SELECT * FROM " + tableName;
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet queryResult = stmt.executeQuery(sql)) {

            return excuteResultSetToDTOs(queryResult);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private List<DTO> excuteResultSetToDTOs(ResultSet queryResult) {
        List<DTO> toRet = new ArrayList<>();
        try {
            while (queryResult.next()) {
                toRet.add(getObjectDTOFromOneResult(queryResult));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return toRet;
    }

    // convert the result using result.getInt(<column name>)
    public abstract DTO getObjectDTOFromOneResult(ResultSet result);

    // using pstmt set the ids columns values
    protected abstract void setValuesToPrepareStatment(DTO toDel, PreparedStatement pstmt);

    // get the rest of the query (WHERE <to be returned>)
    protected abstract String getIDForDTO(DTO toDel);

    protected abstract void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert);


    protected abstract String getTheRestOfInsertQuery(DTO toInsert);
}
