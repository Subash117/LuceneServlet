package com.Subash.Servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class EvtxToCsv {
	
	public void convertToCsv(String evtxPath,String csv) throws FileNotFoundException,IOException 
	{

        String command="powershell.exe  Get-Winevent -path "+evtxPath+" -max 1000  | Export-Csv "+csv;
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        
        
        String line;
        System.out.println("Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
         System.out.println(line);
        }
	}
}
