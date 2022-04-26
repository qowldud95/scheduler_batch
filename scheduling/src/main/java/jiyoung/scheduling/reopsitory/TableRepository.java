package jiyoung.scheduling.reopsitory;

import jiyoung.scheduling.dto.ConnectionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.*;

@Slf4j
public class TableRepository {
    @Autowired
    private Environment environment;

    /*
    조회 - 동적
     */
    // 0. ResultsetMetaData를 이용해서 컬럼 set 할때 동적으로 set 해주기 -> 어떤 테이블이 들어와도 컬럼을 동적으로 셋팅해주기위해 java resultset get columns name
    // 1. 매개변수로 sql을 받아와서 동적으로 데이터를 조회하는 메서드를 만들어주기 -> 조회 메서드 중복제거
    // 2. connection 정보를 받아와서 오버로딩 해서 default일때와 받아왔을때 다르게 구현하기

    public List<Map<String, Object>> tableData(String sql) throws SQLException {
        return tableData(sql, null);
    }

    public List<Map<String, Object>> tableData(String sql, ConnectionDTO connect) throws SQLException {

        List<Map<String, Object>> dataList = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            log.info("connect ={}", connect);
            con = getConnection(connect);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> tableMappingInfo = new HashMap<String, Object>();
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String colName = rsmd.getColumnName(i);
                    tableMappingInfo.put(colName, rs.getObject(colName));  //컬럼의 타입이 각각 들어오는것마다 다를수 있기 때문에 getObject형태로 가져오기
                }
                dataList.add(tableMappingInfo);
            }
            log.info("dataList :: " + dataList);

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
        return dataList;
    }

    public void targetDataInsert(Map<String, Object> source, String targetTableName, List<String> targetColumnName, List<String> sourceColumnName, ConnectionDTO connection) throws SQLException {
        log.info(String.valueOf(targetColumnName));

        String setInsertColumn = "";
        List setTargetColumnValue = new ArrayList();
        List setSourceColumnValue = new ArrayList();

        for(int i = 0; i < targetColumnName.size(); i++){
            if( i == targetColumnName.size()-1){
                setInsertColumn += targetColumnName.get(i);
            } else {
                setInsertColumn += targetColumnName.get(i) + ", ";
            }
        }
        String sql = "INSERT INTO " + targetTableName + "(" + setInsertColumn + ") VALUES (?,?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection(connection);
            pstmt = con.prepareStatement(sql);

            for(int i=0; i<targetColumnName.size(); i++) {
                setTargetColumnValue.add(targetColumnName.get(i));
            }
            for(int i=0; i<sourceColumnName.size(); i++) {
                setSourceColumnValue.add(sourceColumnName.get(i));
            }

            for(int i = 0; i < setTargetColumnValue.size(); i++){
                if(source.get(String.valueOf(setSourceColumnValue.get(i))) instanceof String){
                    pstmt.setString(i+1, String.valueOf(source.get(String.valueOf(setSourceColumnValue.get(i)))));
                } else if(source.get(String.valueOf(setSourceColumnValue.get(i))) instanceof Integer){
                    pstmt.setInt(i+1, (int)source.get(String.valueOf(setSourceColumnValue.get(i))));
                }

                //문제점 : source에 m_name, m_id, m_money가 없기때문에 null로 인식

//                if(source.get(String.valueOf(setInsertColumnValue.get(i))) instanceof String){
//                    pstmt.setString(i+1, String.valueOf(source.get(String.valueOf(setInsertColumnValue.get(i)))));
//                } else if(source.get(String.valueOf(setInsertColumnValue.get(i))) instanceof Integer){
//                    pstmt.setInt(i+1, (int)source.get(String.valueOf(setInsertColumnValue.get(i))));
//                }
            }
            //pstmt.setString(1, String.valueOf(source.get("MEMBER_ID")));
            //pstmt.setString(2, String.valueOf(source.get("MEMBER_NAME")));
            //pstmt.setInt(3, (int)source.get("MONEY"));
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void targetDataUpdate(Map<String, Object> source, String targetTableName, List<String> targetColumnName, List<String> sourceColumnName, Map<String, Object> pkColumn, ConnectionDTO connection) throws SQLException {
        String setUpdateColumn = "";
        List setUpdateColumnValue = new ArrayList();
        String targetPrimaryKey = String.valueOf(pkColumn.get("TARGET_COLUMNNAME"));
        String sourcePrimaryKey = String.valueOf(pkColumn.get("SOURCE_COLUMNNAME"));
        String setPrimaryColumn = targetPrimaryKey + "=?";
        log.info("source.size() :: "+source.size());
        for(int i=0; i<targetColumnName.size(); i++){
            if(targetColumnName.get(i).equals(targetPrimaryKey)){
                continue;
            }
            if(i < targetColumnName.size()-1){
                setUpdateColumn += targetColumnName.get(i) + "=?, ";
            } else {
                setUpdateColumn += targetColumnName.get(i) + "=?";
            }

        }
        for(int i=0; i<sourceColumnName.size(); i++){
            if(!sourceColumnName.get(i).equals(sourcePrimaryKey)){
                setUpdateColumnValue.add(sourceColumnName.get(i));
            }
        }
        log.info("setUpdateColumn={} ", setUpdateColumn);
        log.info("targetPrimaryKey={}",targetPrimaryKey);
        log.info("setUpdateColumnValue={}",setUpdateColumnValue);

        String sql = "UPDATE " + targetTableName + " SET " + setUpdateColumn + " WHERE " +  setPrimaryColumn;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection(connection);
            pstmt = con.prepareStatement(sql);
            for(int i = 0; i < setUpdateColumnValue.size(); i++){
                if(source.get(String.valueOf(setUpdateColumnValue.get(i))) instanceof String){
                    pstmt.setString(i+1, String.valueOf(source.get(String.valueOf(setUpdateColumnValue.get(i)))));
                } else if(source.get(String.valueOf(setUpdateColumnValue.get(i))) instanceof Integer){
                    pstmt.setInt(i+1, (int)source.get(String.valueOf(setUpdateColumnValue.get(i))));
                }
            }
            if(source.get(sourcePrimaryKey) instanceof String){
                pstmt.setString(setUpdateColumnValue.size()+1, String.valueOf(source.get(sourcePrimaryKey)));
            } else if(source.get(sourcePrimaryKey) instanceof Integer) {
                pstmt.setInt(setUpdateColumnValue.size()+1, (int)source.get(sourcePrimaryKey));
            }

            //pstmt.setString(1, String.valueOf(source.get("MEMBER_NAME")));
            //pstmt.setInt(2, (int)source.get("MONEY"));
            //pstmt.setString(3, source.get(sourcePrimaryKey));

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Connection getConnection(ConnectionDTO connect) {

        try {
            String url = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username");
            String password = environment.getProperty("spring.datasource.password");
            if(connect != null){
                url = String.valueOf(connect.getConnectionUrl());
                username = String.valueOf(connect.getConnectionID());
                password = String.valueOf(connect.getConnectionPW());
            }

            Connection connection = DriverManager.getConnection(url, username, password);

            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
}
