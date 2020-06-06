package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public interface Controller {
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
