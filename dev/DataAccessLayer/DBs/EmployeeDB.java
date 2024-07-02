package DataAccessLayer.DBs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DTOs.Errors;
import DataAccessLayer.DTOs.DTO;
import DataAccessLayer.DTOs.EmployeeDTO;
import PresentationLayer.Logs;

public class EmployeeDB extends DB {

    public static String id_COLUMN = "ID";
    public static String name_COLUMN = "NAME";
    public static String bankAccount_COLUMN = "BANK_ACCOUNT";
    public static String monthSalary_COLUMN = "MOUNTH_SALARY";
    public static String startDate_COLUMN = "START_DATE";
    public static String endDate_COLUMN = "END_DATE";
    public static String storeId_COLUMN = "STORE_ID";
    public static String password_COLUMN = "PASSWORD";
    public static String terminationDate_COLUMN = "TERMINATION_DATE";



    public EmployeeDB(){
        this.tableName = "EMPLOYEE_MODULE";
        columnNamesSet = new ArrayList<>();
        columnNamesSet.add(id_COLUMN);
        columnNamesSet.add(name_COLUMN);
        columnNamesSet.add(bankAccount_COLUMN);
        columnNamesSet.add(monthSalary_COLUMN);
        columnNamesSet.add(startDate_COLUMN);
        columnNamesSet.add(endDate_COLUMN);
        columnNamesSet.add(storeId_COLUMN);
        columnNamesSet.add(password_COLUMN);
        columnNamesSet.add(terminationDate_COLUMN);
    }


    @Override
    public DTO getObjectDTOFromOneResult(ResultSet result) {
        try {
            return new EmployeeDTO(result.getString(id_COLUMN),result.getString(name_COLUMN) , result.getString(bankAccount_COLUMN), result.getInt(monthSalary_COLUMN), result.getString(startDate_COLUMN), result.getString(endDate_COLUMN), result.getInt(storeId_COLUMN), result.getString(password_COLUMN), result.getString(terminationDate_COLUMN));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


   

    @Override
    protected void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert) {
        try {
            pstmt.setString(1,((EmployeeDTO)toInsert).id);
            pstmt.setString(2,((EmployeeDTO)toInsert).name);
            pstmt.setString(3,((EmployeeDTO)toInsert).bankAccount);
            pstmt.setInt(4,((EmployeeDTO)toInsert).monthSalary);
            pstmt.setString(5,((EmployeeDTO)toInsert).startDate);
            pstmt.setString(6,((EmployeeDTO)toInsert).endDate);
            pstmt.setInt(7,((EmployeeDTO)toInsert).storeId);
            pstmt.setString(8,((EmployeeDTO)toInsert).password);
            pstmt.setString(9,((EmployeeDTO)toInsert).terminationDate);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getTheRestOfInsertQuery(DTO toInsert) {
        return "(" + id_COLUMN + ", " + name_COLUMN + ", " + bankAccount_COLUMN + ", " + 
        monthSalary_COLUMN + ", " + startDate_COLUMN + ", " + endDate_COLUMN + ", " + 
        storeId_COLUMN + ", " + password_COLUMN + ", " + terminationDate_COLUMN + ") VALUES (?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,
            PreparedStatement pstmt,int index) {
        if(toDelIdentiferMap.containsKey(id_COLUMN)){
            try {
                pstmt.setString(index, (String)toDelIdentiferMap.get(id_COLUMN));
                Logs.debug(pstmt.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new IllegalArgumentException("there is no 'ID' to the DTO");
        }
    }


    @Override
    public String buildWhereQuery() {
        return " WHERE "+id_COLUMN+"=?";
    }


    public EmployeeDTO getEmployeeWithId(String empId) {
        List<DTO> dtoBox =  getDTOsWhere(" WHERE "+id_COLUMN+"="+empId);
        if(dtoBox.size()==0) throw new IllegalArgumentException(Errors.EmployeeNotFound);
        return (EmployeeDTO) dtoBox.get(0);
    }


}
