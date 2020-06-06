package linkdb;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.HashtagDTO;
import model.MemoDAO;
import model.MemoDTO;
import myutil.HttpUtil;

public class ReceiveMemo implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ReceiveMemo");
		
		//로그인 체크(세션에 없을 시 로그인페이지로 리다이렉트)
		String user_idx = myutil.HttpUtil.checkLogin(request, response);
		if(user_idx == null) return;
		
		//메모
		MemoDTO memo = new MemoDTO();
		memo.setUser_idx(user_idx);
		memo.setTitle(request.getParameter("title"));
		memo.setContent(request.getParameter("content"));
		memo.setHashtag(request.getParameter("hashtag"));
		memo.setColorbar(request.getParameter("colorbar"));
		memo.setWowpoint(request.getParameter("wowpoint"));
		memo.setFold(request.getParameter("fold"));
		
		System.out.println(memo.toString());
		
		//해쉬태그 리스트
		String[] exist_hashtag = request.getParameter("exist_hashtag").split(",");
		String[] new_hashtag = request.getParameter("new_hashtag").split("#");
		ArrayList<HashtagDTO> hashtag = new ArrayList<>();
		for(int i=0; i<exist_hashtag.length; i++) {
			if(exist_hashtag[i].equals("")) continue;
			
			HashtagDTO item = new HashtagDTO();
			item.setIdx(exist_hashtag[i]);
			hashtag.add(item);
		}
		for(int i=0; i<new_hashtag.length; i++) {
			if(new_hashtag[i].equals("")) continue;
			
			HashtagDTO item = new HashtagDTO();
			item.setHashtag(new_hashtag[i]);
			hashtag.add(item);
		}

		boolean result = false;
		
		//수정인지 추가인지
		if(request.getParameter("idx") == "") {
			//추가
			result = MemoDAO.getInstance().insertMemo(memo, hashtag);
		}
		else {
			//수정
			memo.setIdx(request.getParameter("idx"));
			result = MemoDAO.getInstance().updateMemo(memo, hashtag);
		}
		
		//리다이렉트
		response.getWriter().write(HttpUtil.sendJson(result, null));
	}

}
