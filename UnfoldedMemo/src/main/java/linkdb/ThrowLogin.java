package linkdb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Controller;
import model.UserDAO;

public class ThrowLogin implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("ThrowLogin");
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		String result = UserDAO.getInstance().login(id, pw);
		System.out.println(result);
		
		if(result != null) {
			HttpSession session = request.getSession();
			session.setAttribute("user_idx", result);
			
			response.sendRedirect("./main");
		}
	}

}
