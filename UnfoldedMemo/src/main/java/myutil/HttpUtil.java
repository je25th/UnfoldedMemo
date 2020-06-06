package myutil;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpUtil {

	public static void forward(HttpServletRequest req, HttpServletResponse resp, String path) {
		try {
			RequestDispatcher rd = req.getRequestDispatcher(path);
			rd.forward(req, resp);
			
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String sendJson(boolean success, JSONArray result) {
		JSONObject send = new JSONObject();
		send.put("success", success);
		send.put("result", result);
		
		return send.toString();
	}
	
	public static String checkLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		String user_idx = (String)session.getAttribute("user_idx");
		
		if(user_idx != null)
			return user_idx;
		else {
			resp.sendRedirect("./login");
			return null;
		}
	}

}
