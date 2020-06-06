package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("LoginController");
		
//		//로그아웃
//		HttpSession session = request.getSession();
//		if(session.getAttribute("user_idx") != null) {
//			session.removeAttribute("user_idx");
//			//session.invalidate();
//			System.out.println("logout!");
//		}
		
		RequestDispatcher requestDispatehcer = request.getRequestDispatcher("./login.jsp");
		requestDispatehcer.forward(request, response);
	}

}
