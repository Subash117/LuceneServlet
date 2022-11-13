package com.Subash.Servlet;

import com.Subash.Lucene.*;

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

import org.json.JSONArray;
import org.json.JSONObject;

class Result
{
	String[] arr;
}

@WebServlet("/Runner")
public class Runner extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String[] res;
	String path;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String query=request.getParameter("query");
		int fileid=Integer.parseInt(request.getParameter("fileid"));
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","CAPSlock007@");
			
			PreparedStatement p=con.prepareStatement("select path from files where fileId=?");
			p.setInt(1, fileid);
			
			System.out.print(path);
			ResultSet rs=p.executeQuery();
			
			while(rs.next())
			{
				path=rs.getString("path");
			}
		}
		catch(Exception e){
			System.out.println(e);
			return;
		}
		
		try{
			LuceneIndexReader lr=new LuceneIndexReader();
			System.out.println(path);
			res=lr.startSearch(query,path);
					
			JSONArray ja = new JSONArray();
			
			for(String s:res)
			{
				ja.put(s);
			}
			
			
			
			JSONObject mainObj = new JSONObject();
			mainObj.put("logs", ja);
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
	        response.setCharacterEncoding("UTF-8");
	        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
	        response.addHeader("Access-Control-Allow-Credentials", "true");
	        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	        response.addHeader("Access-Control-Max-Age", "1728000");
	        
	        out.print(mainObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
}

