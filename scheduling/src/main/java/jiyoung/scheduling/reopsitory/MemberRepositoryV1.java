package jiyoung.scheduling.reopsitory;

import jiyoung.scheduling.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static jiyoung.scheduling.connection.ConnectionConst.*;

@Slf4j
public class MemberRepositoryV1 {
    private final DataSource dataSource;
    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
    조회
     */
    public List<Member> findByMember1() throws SQLException {
        String sql = "select member_id, member_name, money from member";
        List<Member> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setMoney(rs.getInt("money"));
                list.add(member);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
        return list;
    }

    public List<Member> findByMember2() throws SQLException {
        String sql = "select member_id, member_name, money from member2";
        List<Member> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setMoney(rs.getInt("money"));
                list.add(member);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
        return list;
    }

    public void update(String memberId, String memberName, int money) throws SQLException {
        String sql = "update member2 set money=?, member_name=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberName);
            pstmt.setString(3, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void insert(String memberId, String memberName, int money) throws SQLException {
        String sql = "insert into member2 (member_id, member_name, money) values (?,?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.setString(2, memberName);
            pstmt.setInt(3, money);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
    /*
    조회 - 동적
     */

    // 0. ResultsetMetaData를 이용해서 컬럼 set 할때 동적으로 set 해주기 -> 어떤 테이블이 들어와도 컬럼을 동적으로 셋팅해주기위해 java resultset get columns name
    // 1. 매개변수로 sql을 받아와서 동적으로 데이터를 조회하는 메서드를 만들어주기 -> 조회 메서드 중복제거
    // 2. connection 정보를 받아와서 오버로딩 해서 default일때와 if 받아왔을때 다르게 구현하기
    // 3.

    public List<Map<String, Object>> tableInfo(String sql) throws SQLException {

        List<Map<String, Object>> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> tableMappingInfo = new HashMap<String, Object>();
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String colName = rsmd.getColumnName(i);
                    tableMappingInfo.put(colName, rs.getObject(colName));  //컬럼의 타입이 각각 들어오는것마다 다를수 있기 때문에 getObject형태로 가져오기
                }
                list.add(tableMappingInfo);

            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
        return list;
    }

    public List<Map<String, Object>> tableInfo(String url, String username, String password, String sql) throws SQLException {

        List<Map<String, Object>> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection(url, username, password);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> tableMappingInfo = new HashMap<String, Object>();
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String colName = rsmd.getColumnName(i);
                    tableMappingInfo.put(colName, rs.getObject(colName));  //컬럼의 타입이 각각 들어오는것마다 다를수 있기 때문에 getObject형태로 가져오기
                }
                list.add(tableMappingInfo);

            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
        return list;
    }

    public Connection getConnection(String url, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }



}
