package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WriteController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//여기서 해당페이지에서 해야할 뭔가를 처리 해줌
		System.out.println("WriteController");
		
		//로그인 체크(세션에 없을 시 로그인페이지로 리다이렉트)
		String user_idx = myutil.HttpUtil.checkLogin(request, response);
		if(user_idx == null) return;
		
		String a = request.getParameter("idx");
		System.out.println(a);
		//값 넘기기
		//response.setContentType("text/html;charset=utf-8"); // 한글이 깨질 경우 처리.
		//response.setHeader("Cache-Control", "no-cache");
		//response.getWriter().write("test=test");
		
		RequestDispatcher requestDispatehcer = request.getRequestDispatcher("./write.jsp");
		requestDispatehcer.forward(request, response);
	}

}
