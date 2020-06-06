package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StarController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("StarController");
		
		//로그인 체크(세션에 없을 시 로그인페이지로 리다이렉트)
		String user_idx = myutil.HttpUtil.checkLogin(request, response);
		if(user_idx == null) return;
		
		RequestDispatcher requestDispatehcer = request.getRequestDispatcher("./main.jsp");
		requestDispatehcer.forward(request, response);

	}

}
