package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {

	//싱글톤 패턴(클래스를 통해 객체를 딱 한개만 생성 가능하도록 하는 디자인패턴)
	private static UserDAO instance = new UserDAO();//static으로 객체 생성
	private UserDAO() {}//생성자 못만들도록
	public static UserDAO getInstance() {//static 객체를 리턴
        return instance;
    }
	
	private Connection getConnection() {
		
		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://je25th.cafe24.com/je25th";//"jdbc:mariadb://localhost:3306/je25th";
		String username = "je25th"; 
		String password = "awow35se!";
		
        Connection connection = null;
        
        try {
        	Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
			
        } catch(ClassNotFoundException e) { 
			System.out.println("드라이버 로드 실패...");
			e.printStackTrace(); 
		} catch (SQLException e) {
			System.out.println("DB접속 실패..."); 
			e.printStackTrace(); 
		} catch (Exception e) {
            e.printStackTrace();
        }
        
        return connection;
	}
	
	public String login(String id, String pw) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT idx FROM user WHERE id='" + id + "' AND pw='" + pw + "'";
        
        String idx = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            if(rs.next())
            	idx = rs.getString("idx");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
                
                return idx;
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return null;
	}
	
	public ArrayList<UserDTO> getMemberList() {
        ArrayList<UserDTO> dtoList = new ArrayList<UserDTO>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "select * from users";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getString("id"));
                dto.setPw(rs.getString("pw"));
                dto.setNickname(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setEmail(rs.getString("jdate"));
                dtoList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return dtoList;
    }
}
