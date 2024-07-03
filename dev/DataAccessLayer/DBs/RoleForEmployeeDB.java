package DataAccessLayer.DBs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import DataAccessLayer.DTOs.DTO;
import DataAccessLayer.DTOs.RoleForEmployeeDTO;
import PresentationLayer.Logs;

public class RoleForEmployeeDB extends DB {

    public static final String emplID_column = "ID";
    public static final String role_column = "ROLE";

    public RoleForEmployeeDB() {
        this.tableName = "ROLE_FOR_EMPLYEE";
    }

    @Override
    public DTO getObjectDTOFromOneResult(ResultSet result) {
        try {
            return new RoleForEmployeeDTO(result.getString(emplID_column), result.getString(role_column));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert) {
        try {
            pstmt.setString(1, ((RoleForEmployeeDTO) toInsert).employeeId);
            pstmt.setString(2, ((RoleForEmployeeDTO) toInsert).Role);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getTheRestOfInsertQuery(DTO toInsert) {
        return "(" + emplID_column + "," + role_column + ") VALUES (?,?)";
    }

    @Override
    public void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,
            PreparedStatement pstmt, int index) {
        if (toDelIdentiferMap.containsKey(emplID_column)&&toDelIdentiferMap.containsKey(role_column)) {
            try {
                pstmt.setString(index, (String) toDelIdentiferMap.get(emplID_column));
                pstmt.setString(index+1,(String) toDelIdentiferMap.get(role_column));
                Logs.debug(pstmt.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("there is no 'ID' to the DTO");
        }
    }

    @Override
    public String buildWhereQuery() {
        return " WHERE " + emplID_column + "=? AND " + role_column + "=?";
    }

    public void deleteEmployeeRoles(String empId){
        String sql = "DELETE FROM "+tableName+" WHERE "+emplID_column+"="+empId;
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
