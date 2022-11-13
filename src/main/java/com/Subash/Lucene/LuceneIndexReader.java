package com.Subash.Lucene;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class LuceneIndexReader
{
    String result[];
    
    public String[] startSearch(String searchQuery,String IndexDir) throws Exception
    {
    	
        IndexSearcher searcher = createSearcher(IndexDir);
         
        TopDocs foundDocs = searchInContent(searchQuery, searcher);
         
        result=new String[(int) foundDocs.totalHits.value];
        int i=0;
        for (ScoreDoc sd : foundDocs.scoreDocs)
        {
            Document d = searcher.doc(sd.doc);
            result[i++]=d.get("FullLog");
        }
        return result;
    }
     
    TopDocs searchInContent(String inpquery, IndexSearcher searcher) throws Exception
    {
        QueryParser qp = new QueryParser("FullLog", new StandardAnalyzer());
        Query query = qp.parse(inpquery);
        
        return searcher.search(query, 1000);
    }
 
    IndexSearcher createSearcher(String IndexDir) throws IOException
    {
        Directory dir = FSDirectory.open(Paths.get(IndexDir));
         
        IndexReader reader = DirectoryReader.open(dir);
         
        IndexSearcher searcher = new IndexSearcher(reader);
        
        return searcher;
    }
}
