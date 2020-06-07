package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemoDAO {

	private static MemoDAO instance = new MemoDAO();
	private MemoDAO() {}
	public static MemoDAO getInstance() {
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

	//마지막으로 집어넣은 idx가져오기
	private String get_last_insert_idx(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
	        pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID()");
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	        	return rs.getString(1);
	        }
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return null;
	}

	private boolean count_updown_hashtagTable(String count, String idx, Connection conn, PreparedStatement pstmt) {
		if(count != "-1" && count != "+1") return false;
		
		String query = "UPDATE hashtag SET count= count" + count + " WHERE idx=" + idx;
        try {
			pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return false;
	}
	
	private boolean insert_hashtag(String user_idx, String hashtag, Connection conn, PreparedStatement pstmt) {
		String query = "INSERT INTO hashtag (user_idx, hashtag, count) VALUES (?, ?, ?)";
		
		try {
        	pstmt = conn.prepareStatement(query);
        	pstmt.setString(1, user_idx);
        	pstmt.setString(2, hashtag);
        	pstmt.setInt(3, 1);

            pstmt.executeUpdate();
            
            return true;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private ArrayList<HashtagDTO> select_hashtag(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		ArrayList<HashtagDTO> hashtagList = new ArrayList<HashtagDTO>();
        String query = "SELECT * FROM hashtag";
        
        try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
            	HashtagDTO dto = new HashtagDTO();
            	dto.setIdx(rs.getString("idx"));
            	dto.setHashtag(rs.getString("hashtag"));
            	dto.setCount(rs.getString("count"));
            	hashtagList.add(dto);
            }
            
            return hashtagList;
            
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

        return null;
	}
	
	private HashtagDTO select_hashtag_by_idx(String idx, Connection conn, PreparedStatement pstmt, ResultSet rs) {
		HashtagDTO dto = new HashtagDTO();
		dto.setIdx(idx);
		String query = "SELECT * FROM hashtag WHERE idx=" + idx;
		
		try {
			pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if(rs.next()) {
            	dto.setHashtag(rs.getString("hashtag"));
            	dto.setCount(rs.getString("count"));
            }
            
            return dto;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//메모 헤즈 해쉬태그 테이블에 집어넣기
	private boolean insert_memo_has_hashtag(String user_idx, String memo_idx, String hashtag_idx, Connection conn, PreparedStatement pstmt) {
		String query = "INSERT INTO memo_has_hashtag (user_idx, memo_idx, hashtag_idx) VALUES (?, ?, ?)";
    	
		try {
			pstmt = conn.prepareStatement(query);
	    	pstmt.setString(1, user_idx);
	    	pstmt.setString(2, memo_idx);
	    	pstmt.setString(3, hashtag_idx);
	    	
	        pstmt.executeUpdate();
	        
	        return true;
	    	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	//메모 헤즈 해쉬태그 테이블에서 삭제
	private boolean delete_memo_has_hashtag(String memo_idx, String hashtag_idx, Connection conn, PreparedStatement pstmt) {
		String query = "DELETE FROM memo_has_hashtag WHERE memo_idx=" + memo_idx + " AND hashtag_idx=" + hashtag_idx;
    	
		try {
			pstmt = conn.prepareStatement(query);
	        pstmt.executeUpdate();
	        
	        return true;
	    	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	private ArrayList<HashtagDTO> select_memo_has_hashtag_by_memo_idx(String memo_idx, Connection conn, PreparedStatement pstmt, ResultSet rs) {
		ArrayList<HashtagDTO> hashtagList = new ArrayList<HashtagDTO>();
		String query = "SELECT hashtag_idx FROM memo_has_hashtag WHERE memo_idx=" + memo_idx;
		
		try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
            	HashtagDTO item = new HashtagDTO();
            	item.setIdx(rs.getString("hashtag_idx"));
            	hashtagList.add(item);
            }
            
            return hashtagList;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private ArrayList<String> select_memo_has_hashtag_by_hashtag_idx(String hashtag_idx, Connection conn, PreparedStatement pstmt, ResultSet rs) {
		ArrayList<String> memo_has_hashtag = new ArrayList<>();
		String query = "SELECT memo_idx FROM memo_has_hashtag WHERE hashtag_idx=" + hashtag_idx;
		
		try {
			pstmt = conn.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	memo_has_hashtag.add(rs.getString("memo_idx"));
	        }
	        
	        return memo_has_hashtag;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private MemoDTO select_memo(String user_idx, String idx, Connection conn, PreparedStatement pstmt, ResultSet rs) {
        String query = "SELECT * FROM memo WHERE idx=" + idx + " AND user_idx=" + user_idx + " ORDER BY mdate DESC";
    	MemoDTO dto = new MemoDTO();
        
		try {
			pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                dto.setIdx(rs.getString("idx"));
                dto.setUser_idx(rs.getString("user_idx"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setHashtag(rs.getString("hashtag"));
                dto.setColorbar(rs.getString("colorbar"));
                dto.setWowpoint(rs.getString("wowpoint"));
                dto.setStar(rs.getString("star"));
                dto.setWdate(rs.getString("wdate"));
                dto.setMdate(rs.getString("mdate"));
                dto.setFold(rs.getString("fold"));
                dto.setBox(rs.getString("box"));
            }
            
            return dto;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return null;
	}
	
	private JSONArray select_memo_to_json(String query) {
        //ArrayList<MemoDTO> dtoList = new ArrayList<MemoDTO>();
        JSONArray jsonarr = new JSONArray();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
            	MemoDTO dto = new MemoDTO();
                dto.setIdx(rs.getString("idx"));
                dto.setUser_idx(rs.getString("user_idx"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setHashtag(rs.getString("hashtag"));
                dto.setColorbar(rs.getString("colorbar"));
                dto.setWowpoint(rs.getString("wowpoint"));
                dto.setStar(rs.getString("star"));
                dto.setWdate(rs.getString("wdate"));
                dto.setMdate(rs.getString("mdate"));
                dto.setFold(rs.getString("fold"));
                dto.setBox(rs.getString("box"));
                //dtoList.add(dto);
                
                jsonarr.put(new JSONObject(dto.toString()));
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
        
        return jsonarr;
    }
	
	public JSONArray getMemoJson(String user_idx, int page) {
        return select_memo_to_json("SELECT * FROM memo WHERE user_idx=" + user_idx + " ORDER BY mdate DESC LIMIT " + (page-1)*10 + ", 10");
    }
	
	public JSONArray getSearchMemoJson(String user_idx, String keyword, int page) {
        return select_memo_to_json("SELECT * FROM memo WHERE user_idx=" + user_idx 
        		+ " AND (content LIKE '%" + keyword + "%' OR title LIKE'%" + keyword + "%') ORDER BY mdate DESC LIMIT " + (page-1)*10 + ", 10");
    }
	
	public JSONArray getSearchMemoByHashtag_Json(String user_idx, String hashtag_idx, int page) {
		String query = "SELECT memo.idx, memo.user_idx, hashtag, title, content, colorbar, wowpoint, star, wdate, mdate, fold, box " +
				"FROM memo JOIN memo_has_hashtag ON memo.idx=memo_has_hashtag.memo_idx " + 
				"WHERE memo.user_idx=" + user_idx + " AND (hashtag_idx=" + hashtag_idx + ") " + 
				"ORDER BY mdate DESC LIMIT " + (page-1)*10 + ", 10";
		return select_memo_to_json(query);
    }
	
	public JSONArray getStarJson(String user_idx, int page) {
        return select_memo_to_json("SELECT * FROM memo WHERE user_idx=" + user_idx + " AND star=1" + " ORDER BY mdate DESC LIMIT " + (page-1)*10 + ", 10");
    }
	
	public JSONArray getOneMemoJson(String user_idx, String idx) {
        JSONArray jsonarr = new JSONArray();
    	MemoDTO dto = new MemoDTO();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM memo WHERE idx='" + idx + "' ORDER BY mdate DESC";
        
        try {
            conn = getConnection();
            //메모가져오기        
            dto = select_memo(user_idx, idx, conn, pstmt, rs);
            
            //해쉬태그 목룍
            ArrayList<HashtagDTO> hashtagList = select_memo_has_hashtag_by_memo_idx(idx, conn, pstmt, rs);
            
            //해쉬태그 정보 가져오기
            for(int i=0; i<hashtagList.size(); i++) {
            	String hashtag_idx = hashtagList.get(i).getIdx();
            	hashtagList.set(i, select_hashtag_by_idx(hashtag_idx, conn, pstmt, rs));
            }
            
            //json 변환
            dto.setHashtaglist(hashtagList);
            jsonarr.put(new JSONObject(dto.toString()));
            
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
        
        return jsonarr;
    }
	
	public boolean setStar(String idx, String star) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "UPDATE memo SET star='" + star + "' WHERE idx='" + idx + "'";
                
        try {
            conn = getConnection();
	        pstmt = conn.prepareStatement(query);
	        pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
                
                return true;
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return false;
	}
	
	public JSONArray getHashtagJson() {
		ArrayList<HashtagDTO> hashtagList = new ArrayList<HashtagDTO>();
        JSONArray jsonarr = new JSONArray();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM hashtag";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
            	HashtagDTO dto = new HashtagDTO();
            	dto.setIdx(rs.getString("idx"));
            	dto.setHashtag(rs.getString("hashtag"));
            	dto.setCount(rs.getString("count"));
            	
            	hashtagList.add(dto);
            }
            
            //해당 태그를 가진 게시글 목룍
            for(int i=0; i<hashtagList.size(); i++) {
                ArrayList<String> memo_has_hashtag
                	= select_memo_has_hashtag_by_hashtag_idx(hashtagList.get(i).getIdx(), conn, pstmt, rs);
                
                hashtagList.get(i).setMemo_has_hashtag(memo_has_hashtag);
                jsonarr.put(new JSONObject(hashtagList.get(i).toString()));
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

        return jsonarr;
	}
	
	public JSONArray getHashtagJson(String user_idx, String hashtag) {
		ArrayList<HashtagDTO> hashtagList = new ArrayList<HashtagDTO>();
        JSONArray jsonarr = new JSONArray();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM hashtag WHERE user_idx=" + user_idx + " AND hashtag LIKE '%" + hashtag + "%'";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
            	HashtagDTO dto = new HashtagDTO();
            	dto.setIdx(rs.getString("idx"));
            	dto.setHashtag(rs.getString("hashtag"));
            	dto.setCount(rs.getString("count"));
            	
            	hashtagList.add(dto);
            }
            
            //해당 태그를 가진 게시글 목룍
            for(int i=0; i<hashtagList.size(); i++) {
                ArrayList<String> memo_has_hashtag
                	= select_memo_has_hashtag_by_hashtag_idx(hashtagList.get(i).getIdx(), conn, pstmt, rs);
                
                hashtagList.get(i).setMemo_has_hashtag(memo_has_hashtag);
                jsonarr.put(new JSONObject(hashtagList.get(i).toString()));
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

        return jsonarr;
	}
	
	public boolean insertMemo(MemoDTO memoDto, ArrayList<HashtagDTO> hashtagDto) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String memo_query = "INSERT INTO memo (user_idx, title, content, hashtag, colorbar, wowpoint, fold) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
        try {
        	//메모업뎃
            conn = getConnection();
            pstmt = conn.prepareStatement(memo_query);
            
            //MemoDTO dto = new MemoDTO(json);

            pstmt.setString(1, memoDto.getUser_idx());
            pstmt.setString(2, memoDto.getTitle());
            pstmt.setString(3, memoDto.getContent());
            pstmt.setString(4, memoDto.getHashtag());
            pstmt.setString(5, memoDto.getColorbar());
            pstmt.setString(6, memoDto.getWowpoint());
            pstmt.setString(7, memoDto.getFold());

            pstmt.executeUpdate();
            
            //마지막으로 집어넣은 메모의 idx가져오기
            memoDto.setIdx(get_last_insert_idx(conn, pstmt, rs));
            
            //해쉬태그 업뎃
            for(int i=0; i<hashtagDto.size(); i++) {
            	if(hashtagDto.get(i).getIdx() == null) {
            		//해쉬태그 테이블에 새로운 해쉬태그 생성
            		insert_hashtag(memoDto.getUser_idx(), hashtagDto.get(i).getHashtag(), conn, pstmt);
                    
                    //마지막으로 집어넣은 메모의 idx가져와서 변수에 저장
                    hashtagDto.get(i).setIdx(get_last_insert_idx(conn, pstmt, rs));
            	}
            	else {
            		//해쉬태그 테이블의 기존 해쉬태그 카운트 증가
            		count_updown_hashtagTable("+1", hashtagDto.get(i).getIdx(), conn, pstmt);
            	}

                //memo_has_hashtag 테이블 업뎃
            	insert_memo_has_hashtag(memoDto.getUser_idx(), memoDto.getIdx(), hashtagDto.get(i).getIdx(), conn, pstmt);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
                
                return true;
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return false;
	}

	public boolean updateMemo(MemoDTO memoDto, ArrayList<HashtagDTO> hashtagDto) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "UPDATE memo SET title=?, content=?, hashtag=?, colorbar=?, wowpoint=?, mdate=CURRENT_TIMESTAMP WHERE idx=" + memoDto.getIdx();
                
        try {
            conn = getConnection();

        	//업뎃할 메모의 정보(메모, 태그리스트)를 가져오기
        	MemoDTO before = select_memo(memoDto.getUser_idx(), memoDto.getIdx(), conn, pstmt, rs);
            ArrayList<HashtagDTO> beforeHashtaglist = select_memo_has_hashtag_by_memo_idx(memoDto.getIdx(), conn, pstmt, rs);
        	
        	//TODO :: 메모 업뎃
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, memoDto.getTitle());
            pstmt.setString(2, memoDto.getContent());
            pstmt.setString(3, memoDto.getHashtag());
            pstmt.setString(4, memoDto.getColorbar());
            pstmt.setString(5, memoDto.getWowpoint());
            pstmt.executeUpdate();
            
            //삭제할 태그 솎아내기
            for(int b=0; b<beforeHashtaglist.size(); b++) {
            	for(int n=0; n<hashtagDto.size(); n++) {
            		if(beforeHashtaglist.get(b).getIdx() == hashtagDto.get(n).getIdx()) {
            			//같은것은 삭제
            			beforeHashtaglist.remove(b);
            			b--;
            			hashtagDto.remove(n);
            			n--;
            		}
            	}
            }
            
            //해쉬태그 count down
            for(int i=0; i<beforeHashtaglist.size(); i++) {
            	//해쉬태그 테이블의 기존 해쉬태그 카운트 감소
        		count_updown_hashtagTable("-1", beforeHashtaglist.get(i).getIdx(), conn, pstmt);
            	
        		//memo_has_hashtag 테이블 업뎃(삭제)
        		delete_memo_has_hashtag(memoDto.getIdx(), beforeHashtaglist.get(i).getIdx(), conn, pstmt);
            }
        	
            //해쉬태그 추가 및 count up
            for(int i=0; i<hashtagDto.size(); i++) {
            	//뉴 태그 추가
            	if(hashtagDto.get(i).getIdx() == null) {
            		//해쉬태그 테이블에 새로운 해쉬태그 생성
            		insert_hashtag(memoDto.getUser_idx(), hashtagDto.get(i).getHashtag(), conn, pstmt);
                    
                    //마지막으로 집어넣은 메모의 idx가져와서 변수에 저장
                    hashtagDto.get(i).setIdx(get_last_insert_idx(conn, pstmt, rs));
            	}
            	else {
            		//해쉬태그 테이블의 기존 해쉬태그 카운트 증가
            		count_updown_hashtagTable("+1", hashtagDto.get(i).getIdx(), conn, pstmt);
            	}

                //memo_has_hashtag 테이블 업뎃(추가)
            	insert_memo_has_hashtag(memoDto.getUser_idx(), memoDto.getIdx(), hashtagDto.get(i).getIdx(), conn, pstmt);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
                
                return true;
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return false;
	}
	
	public boolean deleteMemo(String idx) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "DELETE FROM memo WHERE idx=" + idx;
        
        try {
            conn = getConnection();
            
            //삭제할 메모의 태그목룍 가져오기
            ArrayList<HashtagDTO> hashtaglist = select_memo_has_hashtag_by_memo_idx(idx, conn, pstmt, rs);
        	
        	//태그 카운트 다운
            for(int i=0; i<hashtaglist.size(); i++) {
                count_updown_hashtagTable("-1", hashtaglist.get(i).getIdx(), conn, pstmt);
            }
        	
        	//메모 삭제
            System.out.println(query);
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
        	
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
                
                return true;
                
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return false;
	}
}
