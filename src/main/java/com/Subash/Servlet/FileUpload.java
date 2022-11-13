package com.Subash.Servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


import com.Subash.Lucene.LuceneIndexWriter;

@WebServlet("/FileUpload")
@MultipartConfig
public class FileUpload extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	File datafile,theDir;
    String fileName,DATA_DIR="F:\\ZOHO_Project_DATA\\USER_DATA\\";
    
    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int n=0;
		
		HttpSession session=request.getSession(false);
		
		if(session==null)
		{
			response.sendRedirect("http://localhost:4200/login");
		}
		else
		{
		
		String id=session.getAttribute("id").toString();  //////Changes Here
		
        
        List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
  
        for(Part filePart:fileParts)
        {
        	fileName = Paths.get(getSubmittedFileName(filePart)).getFileName().toString().split("\\.")[0];
            
            
            theDir=new File(DATA_DIR+id+"\\"+fileName);
            
            
            while(theDir.exists())
            {
                theDir=new File(DATA_DIR+id+"\\"+fileName+ ++n);		//creating a Directory for the File
            }
            theDir.mkdirs();
            
            File uploads = new File(theDir.getCanonicalPath());
            datafile = new File(uploads, fileName+".evtx");

            
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, datafile.toPath());
            }
            
            
            
            File csvFile=new File(theDir.getCanonicalFile()+"\\"+fileName+".csv");
			csvFile.createNewFile();						//Creating a CSV File to 
			
			EvtxToCsv e=new EvtxToCsv();
			e.convertToCsv(datafile.getCanonicalPath(), csvFile.getCanonicalPath());
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","CAPSlock007@");
				
				Random r=new Random();
				int file_id=r.nextInt();
				
				PreparedStatement p=con.prepareStatement("select * from files where fileId=?");
				p.setInt(1, file_id);
				
				ResultSet rs=p.executeQuery();
				
				while(rs.next())
				{
					rs.close();
					file_id=r.nextInt();
					p.setInt(1, file_id);
					rs=p.executeQuery();
				}
				
				p=con.prepareStatement("insert into files(fileId,name,uid,path) values(?,?,?,?)");
				
				p.setInt(1, file_id);
				
				p.setInt(3,Integer.parseInt(id));
				if(n==0)
				{
					p.setString(2, fileName);
					p.setString(4, DATA_DIR+id+"\\"+fileName);
				}
				else
				{
					p.setString(2, fileName+n);
					p.setString(4, DATA_DIR+id+"\\"+fileName+n);
				}
				
				p.executeUpdate();
				
			}
			catch(Exception ex)
			{
				System.out.println(ex);
			}
			
			LuceneIndexWriter lw=new LuceneIndexWriter();   //Passing the Indexdir file obj to Index
			lw.startIndexing(csvFile,theDir);   
        }		
				response.sendRedirect("http://localhost:4200/dashboard/");
			}
	}
		
			
	}

