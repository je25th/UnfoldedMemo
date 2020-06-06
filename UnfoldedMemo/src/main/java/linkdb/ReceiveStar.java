package linkdb;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.HashtagDTO;
import model.MemoDAO;
import model.MemoDTO;
import myutil.HttpUtil;

public class ReceiveStar implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ReceiveStar");
		
		//로그인 체크(세션에 없을 시 로그인페이지로 리다이렉트)
		String user_idx = myutil.HttpUtil.checkLogin(request, response);
		if(user_idx == null) return;
		
		String idx = request.getParameter("idx");
		String star = request.getParameter("star");
		
		boolean result = MemoDAO.getInstance().setStar(idx, star);
		
		response.getWriter().write(HttpUtil.sendJson(result, null));
	}

}
