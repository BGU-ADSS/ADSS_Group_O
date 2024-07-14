package DataAccessLayer.DBs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import DataAccessLayer.DTOs.AvaliableWorkerInShiftDTO;
import DataAccessLayer.DTOs.DTO;

public class AvaliableWorkerInShiftDB extends DB {

    public static final String shiftId_column = "SHIFT_ID";
    public static final String empId_column = "EMP_ID";
    public static final String storeId_column = "STORE_ID";

    public AvaliableWorkerInShiftDB(){
        this.tableName = "AVALIABLE_WORKERS";
    }


    @Override
    public DTO getObjectDTOFromOneResult(ResultSet result) {
        try {
            return new AvaliableWorkerInShiftDTO(result.getString(empId_column), result.getInt(shiftId_column),result.getInt(storeId_column));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert) {
        try {
            pstmt.setString(1, ((AvaliableWorkerInShiftDTO)toInsert).empId);
            pstmt.setInt(2, ((AvaliableWorkerInShiftDTO)toInsert).shiftId);
            pstmt.setInt(3, ((AvaliableWorkerInShiftDTO)toInsert).storeId);
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


    public AvaliableWorkerInShiftDTO[] getAvaliableWorkers(int shiftId, int storeId) {
        List<DTO> dtos =  getDTOsWhere(" WHERE "+shiftId_column+"="+shiftId+" AND "+storeId_column+"="+storeId);
        AvaliableWorkerInShiftDTO [] toRet = new AvaliableWorkerInShiftDTO[dtos.size()];
        for(int i=0;i<dtos.size();i++) toRet[i] = (AvaliableWorkerInShiftDTO) dtos.get(i);
        return toRet;
    }
    
}
