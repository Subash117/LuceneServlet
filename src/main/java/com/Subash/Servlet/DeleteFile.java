package com.Subash.Servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String path;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id=Integer.parseInt(request.getParameter("fileid"));
		
		HttpSession session=request.getSession(false);
		
		if(session==null)
		{
			response.sendRedirect("http://localhost:4200/login");
		}

		
		try
		{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","CAPSlock007@");
			
			
			PreparedStatement p=con.prepareStatement("select path from files where fileId=?");
			p.setInt(1, id);
			
			ResultSet rs=p.executeQuery();
			
			rs.next();
			path=rs.getString("path");
			
			p=con.prepareStatement("delete from files where fileId=?");
			p.setInt(1, id);
			
			p.executeUpdate();
			
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		path=path.split("//")[0];
		
		File f=new File(path);
		System.out.println(f.getCanonicalPath());
		
		String[]entries = f.list();
		for(String s: entries){
		    File currentFile = new File(f.getPath(),s);
		    currentFile.delete();
		}
		
		f.delete();
//		response.sendRedirect("http://localhost:4200/dashboard/"+request.getParameter("uid"));
		
		response.sendRedirect("http://localhost:4200/dashboard");
	}

}
