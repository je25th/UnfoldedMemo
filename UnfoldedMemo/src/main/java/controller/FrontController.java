package controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import linkdb.*;

@WebServlet("/")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//서브 컨트롤러를 관리하는 객체
	HashMap<String, Controller> list = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
        System.out.println("init");
		
		//서브 컨트롤러 에 대한 정보를 가지는 객체
        list = new HashMap<String, Controller>();
        
        //url
        list.put("/main", new MainController());
        list.put("/write", new WriteController());
        list.put("/viewer", new ViewerController());
        list.put("/modify", new ModifyController());
        list.put("/search", new SearchController());
        list.put("/star", new StarController());
        list.put("/login", new LoginController());
        
        //ajax통신용 or DB접속
        list.put("/moemo.view", new ThrowMemoList());
        list.put("/memoone.view", new ThrowMemoOne());
        list.put("/hashtag.view", new ThrowHashtagList());
        list.put("/login.view", new ThrowLogin());
        list.put("/star.view", new ThrowStarList());
        
        //디비에 집어넣는 그거
        list.put("/memo.receive", new ReceiveMemo());
        list.put("/deletememo.receive", new ReceiveDeleteMemo());
        list.put("/star.receive", new ReceiveStar());
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("frontController");
		
		try {
			//파일 경로까지 다 얻음   ex) /test2
			String url = req.getRequestURI();
			System.out.println("url : " + url);
			
	        //프로젝트 path까지 얻음   ex) /test2/next.do
	        String contextPath = req.getContextPath();
	        
	        //파일이름만 남김   ex) hello.do
	        String path = url.substring(contextPath.length());
	         
	        //인코딩 설정
	        req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");

	        //서브 컨트롤러를 관리하는 객체에서 path에 해당하는 서브 컨트롤러를 찾아 실행한다.
	        Controller subController = list.get(path);
	        subController.execute(req, resp);
			
		} catch (Exception e) {
			System.out.println(e);
			RequestDispatcher requestDispatehcer = req.getRequestDispatcher("./front.html");
			requestDispatehcer.forward(req, resp);
			//resp.sendRedirect("./front.html");
		}
	}

}
