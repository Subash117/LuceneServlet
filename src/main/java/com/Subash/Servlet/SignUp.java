package com.Subash.Servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String uname=request.getParameter("name");
		String uemail=request.getParameter("email");
		String upass=request.getParameter("pass1");
		
		PrintWriter out=response.getWriter();

		
		try
		{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","CAPSlock007@");  
			
			
			PreparedStatement p=con.prepareStatement("select email from users where email=?");
			
			p.setString(1,uemail);  
			
			ResultSet rs=p.executeQuery(); 
			
			if(rs.next())
				out.print("Email Id Already Registered");
			
			else
			{
				
				Random rand=new Random();
				p=con.prepareStatement("select ID from users where ID=?");
				int id=rand.nextInt();
				p.setInt(1, id);
				rs=p.executeQuery();
				
				while(rs.next())
				{
					id=rand.nextInt();
					p.setInt(1, id);
					rs=p.executeQuery();
				}
				
				File f=new File("F:\\ZOHO_Project_DATA\\USER_DATA"+"\\"+id);
				f.mkdir();
				
				p=con.prepareStatement("insert into users(ID,email,name,pass,file_path,sessionid) values(?,?,?,?,?,?)");
				p.setInt(1, id);
				p.setString(2, uemail);
				p.setString(3, uname);
				p.setString(4, upass);
				p.setString(5, f.getCanonicalPath());
				p.setString(6, null);
				
				
				p.executeUpdate();
				
				response.sendRedirect("http://localhost:4200/log-redirect");
			}
			con.close();
			
			
			
		}
			catch(Exception e){ 
				System.out.println(e);
			}
		
			}
}

