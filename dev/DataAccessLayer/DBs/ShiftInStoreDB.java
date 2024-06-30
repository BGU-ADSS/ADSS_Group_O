package DataAccessLayer.DBs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import DataAccessLayer.DTOs.DTO;
import DataAccessLayer.DTOs.ShiftInStoreDTO;

public class ShiftInStoreDB extends DB {
    public static final String storeId_column = "STORE_ID";
    public static final String date_column = "DATE";
    public static final String shiftTime_column = "SHIFT_TIME";
    public static final String shiftId_column = "SHIFT_ID";

    public ShiftInStoreDB() {
        this.tableName = "SHIFT_IN_STORE";
    }

    @Override
    public DTO getObjectDTOFromOneResult(ResultSet result) {
        try {
            return new ShiftInStoreDTO(result.getInt(storeId_column), result.getString(date_column),
                    result.getString(shiftTime_column), result.getInt(shiftId_column));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void setTheValuesToTheInsertQuery(PreparedStatement pstmt, DTO toInsert) {
        try {
            pstmt.setInt(1,((ShiftInStoreDTO)toInsert).storeId );
            pstmt.setString(2,((ShiftInStoreDTO)toInsert).date );
            pstmt.setString(3,((ShiftInStoreDTO)toInsert).shiftTime );
            pstmt.setInt(4,((ShiftInStoreDTO)toInsert).shiftId );
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected String getTheRestOfInsertQuery(DTO toInsert) {
        return "("+storeId_column+","+date_column+","+shiftTime_column+","+shiftId_column+") VALUES (?,?,?,?)";
    }

    @Override
    public void setValuesToPreparedStatmnetInWherePart(HashMap<String, Object> toDelIdentiferMap,
            PreparedStatement pstmt, int index) {
        
    }

    @Override
    public String buildWhereQuery() {
        return "";
    }

}
