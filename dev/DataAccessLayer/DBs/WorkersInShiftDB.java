package DataAccessLayer.DBs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import DataAccessLayer.DTOs.AvaliableWorkerInShiftDTO;
import DataAccessLayer.DTOs.DTO;
import DataAccessLayer.DTOs.WorkerInShiftDTO;

public class WorkersInShiftDB extends DB{

    public static final String shiftId_column = "SHIFT_ID";
    public static final String empId_column = "EMP_ID";
    public static final String storeId_column = "STORE_ID";

    public WorkersInShiftDB(){
        this.tableName = "WORKERS_IN_SHIFT";
    }



    @Override
    public DTO getObjectDTOFromOneResult(ResultSet result) {
        try {
            return new WorkerInShiftDTO(result.getString(empId_column), result.getInt(shiftId_column),result.getInt(storeId_column));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert) {
        try {
            pstmt.setString(1, ((WorkerInShiftDTO)toInsert).empId);
            pstmt.setInt(2, ((WorkerInShiftDTO)toInsert).shiftId);
            pstmt.setInt(3, ((WorkerInShiftDTO)toInsert).storeId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getTheRestOfInsertQuery(DTO toInsert) {
        return new StringBuilder().append("(").append(empId_column+",").append(shiftId_column).append(","+storeId_column+") VALUES (?,?,?)").toString();
    }

    @Override
    public void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,
            PreparedStatement pstmt, int index) {
                try {
                    pstmt.setString(index, (String)toDelIdentiferMap.get(empId_column));
                    pstmt.setInt(index+1,(int)toDelIdentiferMap.get(shiftId_column));
                    pstmt.setInt(index+2,(int)toDelIdentiferMap.get(storeId_column));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    }

    @Override
    public String buildWhereQuery() {
        return " WHERE "+empId_column+"=? AND "+shiftId_column+"=? AND "+storeId_column+"=?";
    }



    public WorkerInShiftDTO[] getWorkers(int shiftId, int storId) {
        return (WorkerInShiftDTO[]) (DTO[] ) getDTOsWhere(" WHERE "+shiftId_column+"="+shiftId+" AND "+storeId_column+"="+storId).toArray();
    }
    
}
