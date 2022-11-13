package com.Subash.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	int id;
	String sesid;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email=request.getParameter("email");
		String pass=request.getParameter("pass1");
		
		PrintWriter out=response.getWriter();
		
		try
		{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","CAPSlock007@");  
			
			
			PreparedStatement p=con.prepareStatement("select ID from users where email=? and pass=?");
			
			p.setString(1,email);  
			p.setString(2,pass);
			
			ResultSet rs=p.executeQuery(); 
			
			if(!rs.next())
			{
				out.print("Login Failed Email/Password Incorrect");
			}
			else
			{
				id=rs.getInt("ID");
				
				p=con.prepareStatement("update users set sessionid=? where ID=?;");
				
				System.out.println("Login Server");
				HttpSession session=request.getSession();
				System.out.println(session.getId());
				session.setAttribute("id",id);
				System.out.println(session.getAttribute("id").toString());
				sesid=session.getId();
				
				p.setString(1, sesid);
				p.setInt(2, id);
				
				p.executeUpdate();
				
				
				response.sendRedirect("http://localhost:4200//dashboard");
			}
			con.close();
		}
			catch(Exception e){ 
				System.out.println(e);
			}
		
	}

}
