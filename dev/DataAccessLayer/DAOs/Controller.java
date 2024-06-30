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
import PresentationLayer.Logs;

public abstract class Controller {
    // for each child class we have to :
    // in the constructor we have to set the table name
    // implement the function getObjectDTOFromOneResult that convert from result to
    // DTO object
    // we have to add the names of the columns as a static members

    private String url = "jdbc:sqlite:dev\\DataAccessLayer\\ADSS_DB_EMPLOYEE_MODULE.db";

    protected String tableName;
    protected List<String> columnNamesSet;

    public void updateSpecifecColumnForOneRow(HashMap<String, Object> toDelIdentiferMap,String columnName,Object newValueToUpdate,String typeIntOrString){
        String sql = "UPDATE "+tableName+" SET "+columnName+"=?"+buildWhereQuery();
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setValuesToPreparedStatmnetInWherePart(toDelIdentiferMap, pstmt);
            if(typeIntOrString=="int") pstmt.setInt(toDelIdentiferMap.size()+1, (int)newValueToUpdate);
            else if(typeIntOrString=="string") pstmt.setString(toDelIdentiferMap.size()+1, (String)newValueToUpdate);
            else throw new IllegalArgumentException("the type of the updated column is not 'string' or 'int'"); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteDTO(HashMap<String,Object  > toDelIdentiferMap) {
        String sql = "DELETE FROM " + tableName + buildWhereQuery();
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setValuesToPreparedStatmnetInWherePart(toDelIdentiferMap, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllData() {
        String sql = "DELETE FROM " + tableName;
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertDTO(DTO toInsert) {
        String sql = "INSERT INTO " + tableName + getTheRestOfInsertQuery(toInsert);
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setTheValuesToTheInsertQuery(pstmt, toInsert);
            pstmt.executeUpdate();
            Logs.debug("dto must be added to the db");

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

    protected abstract void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert);

    protected abstract String getTheRestOfInsertQuery(DTO toInsert);

    // according to the order of the buildWhereQuery() set the values to the pstmt
    public abstract void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,PreparedStatement pstmt);

    // build where id=? ...
    public abstract String buildWhereQuery();
}
