package DataAccessLayer.DBs;

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

public abstract class DB {
    // for each child class we have to :
    // in the constructor we have to set the table name
    // implement the function getObjectDTOFromOneResult that convert from result to
    // DTO object
    // we have to add the names of the columns as a static members

    protected String url = "jdbc:sqlite:ADSS_DB_EMPLOYEE_MODULE.db";

    protected String tableName;
    protected List<String> columnNamesSet;

    public void updateSpecifecColumnForOneRow(HashMap<String, Object> toDelIdentiferMap, String columnName,
            Object newValueToUpdate, String typeIntOrString) {
        String sql = "UPDATE " + tableName + " SET " + columnName + "=?" + buildWhereQuery();
        Logs.debug(sql);
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (typeIntOrString == "int")
                pstmt.setInt(1, (int) newValueToUpdate);
            else if (typeIntOrString == "string")
                pstmt.setString(1, (String) newValueToUpdate);
            else
                throw new IllegalArgumentException("the type of the updated column is not 'string' or 'int'");
            Logs.debug("we have to excute this update");
            setValuesToPreparedStatmnetInWherePart(toDelIdentiferMap, pstmt, 2);
            pstmt.executeUpdate();
            Logs.debug("must updated +" + (typeIntOrString == "string"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDTO(HashMap<String, Object> toDelIdentiferMap) {
        String sql = "DELETE FROM " + tableName + buildWhereQuery();
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setValuesToPreparedStatmnetInWherePart(toDelIdentiferMap, pstmt, 1);
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
        Logs.debug("joining insert query");
        String sql = "INSERT INTO " + tableName + getTheRestOfInsertQuery(toInsert);
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Logs.debug("joining the try scoope");
            setTheValuesToTheInsertQuery(pstmt, toInsert);
            pstmt.executeUpdate();
            Logs.debug("dto must be added to the db");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<DTO> getDTOsWhere(String whereQuery) {
        String sql = "SELECT * FROM " + tableName + whereQuery;
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet queryResult = stmt.executeQuery(sql)) {

            return excuteResultSetToDTOs(queryResult);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    protected List<DTO> excuteResultSetToDTOs(ResultSet queryResult) {
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

    protected abstract void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert);

    protected abstract String getTheRestOfInsertQuery(DTO toInsert);

    // according to the order of the buildWhereQuery() set the values to the pstmt
    public abstract void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,
            PreparedStatement pstmt, int index);

    // build where id=? ...
    public abstract String buildWhereQuery();
}
