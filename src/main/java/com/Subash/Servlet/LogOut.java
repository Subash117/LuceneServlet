package com.Subash.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session=request.getSession(false);
		String s=session.getAttribute("id").toString();
		
		int id=Integer.parseInt(s);
		
		try
		{ 
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","CAPSlock007@");  
			
			
			PreparedStatement p=con.prepareStatement("update users set sessionid=? where ID=?;");
			
			p.setString(1,null);
			p.setInt(2,id);
			
			p.executeUpdate();
			session.invalidate();
			response.sendRedirect("http://localhost:4200");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		
		
	}

}
