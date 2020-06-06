package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MemoDAO;
import myutil.HttpUtil;

public class ModifyController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ModifyController");
		
		//로그인 체크(세션에 없을 시 로그인페이지로 리다이렉트)
		String user_idx = myutil.HttpUtil.checkLogin(request, response);
		if(user_idx == null) return;
		
		//파라미터 가져오기
		String get = request.getParameter("idx");
		System.out.println(get);
		
		//값 넘기기
		String memolist = MemoDAO.getInstance().getOneMemoJson(user_idx, get).toString();
		request.setAttribute("memodata", memolist);
//		String hashtaglist = MemoDAO.getInstance().getHashtagJson(get).toString();
//		request.setAttribute("memodata", memolist);
		
		RequestDispatcher requestDispatehcer = request.getRequestDispatcher("./write.jsp");
		requestDispatehcer.forward(request, response);

	}

}
