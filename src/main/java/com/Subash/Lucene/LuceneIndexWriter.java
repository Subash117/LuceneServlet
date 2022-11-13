package com.Subash.Lucene;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.opencsv.CSVReader;
 
public class LuceneIndexWriter
{
	String indexPath;
	String[] fields;
    public void startIndexing(File f,File fol) throws IOException
    {
        String docsPath = f.getCanonicalPath();
        indexPath=fol.getCanonicalPath();
        
        try
        {
            Directory dir = FSDirectory.open( Paths.get(indexPath));

            Analyzer analyzer = new StandardAnalyzer();
            
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            
            IndexWriter writer = new IndexWriter(dir, iwc);
             
            indexDocs(writer, docsPath);
 
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
     
    void indexDocs(IndexWriter writer, String path) throws IOException
    {
    	CSVReader reader = new CSVReader(new FileReader(path),',');  
    	String [] nextLine;
    	System.out.println("Header "+reader.readNext());
    	
    	fields=reader.readNext();
    	
    	for(int i=0;i<fields.length;i++)
    		fields[i]=fields[i].toLowerCase();
    	
    	System.out.println(Arrays.toString(fields));
    	
    	FileWriter myWriter = new FileWriter(indexPath+"\\"+"fields.txt");
        
    	for(String s:fields)
    	{
    		myWriter.write(s+"\n");
    	}
    	myWriter.close();
    	
    	
    	while ((nextLine = reader.readNext()) != null)  
    	{
    		StringBuilder sb=new StringBuilder();
	    	for(int i=0;i<nextLine.length;i++)  
	    	{  
	    		if(i==nextLine.length-1)
	    		{
	    			sb.append(nextLine[i]);
	    			break;
	    		}
	    		sb.append(nextLine[i]+" || ");
	    	}
	    	indexDoc(writer,sb.toString(),nextLine);
    	}
    }
 
    void indexDoc(IndexWriter writer, String log,String[] logD) throws IOException
    {
            Document doc = new Document();
             
            
            if(fields.length==logD.length)
            {
	            for(int i=0;i<fields.length;i++)
	            {
	            	doc.add(new TextField(fields[i],logD[i].replaceAll("\"", ""),Store.YES));
	            }
            }
            doc.add(new TextField("FullLog",log,Store.YES));
            writer.addDocument(doc);
    }
}
