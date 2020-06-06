package linkdb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.MemoDAO;
import myutil.HttpUtil;

public class ThrowMemoList implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("moemo.view");
		
		//로그인 체크(세션에 없을 시 로그인페이지로 리다이렉트)
		String user_idx = myutil.HttpUtil.checkLogin(request, response);
		if(user_idx == null) return;

		String keyword = null;
		if(request.getParameter("keyword") != null) {
			keyword = request.getParameter("keyword");
			//keyword = keyword.replace("?keyword=", "");
		}
		
		String hashtag = null;
		if(request.getParameter("hashtag") != null) {
			hashtag = request.getParameter("hashtag");
		}
		
		int page = Integer.parseInt(request.getParameter("page"));
		
		
		response.setContentType("text/xml;charset=utf-8"); // 한글이 깨질 경우 처리.
		response.setHeader("Cache-Control", "no-cache");
		
		if(keyword == null && hashtag == null)
			response.getWriter().write(HttpUtil.sendJson(true, MemoDAO.getInstance().getMemoJson(user_idx, page)));
		else if(keyword != null && hashtag == null)
			response.getWriter().write(HttpUtil.sendJson(true, MemoDAO.getInstance().getSearchMemoJson(user_idx, keyword, page)));
		else if(keyword == null && hashtag != null)
			response.getWriter().write(HttpUtil.sendJson(true, MemoDAO.getInstance().getSearchMemoByHashtag_Json(user_idx, hashtag, page)));
	}

}
