/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eformsupgrade;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author achid
 */
public class ReadNodes {

    
    public String outputString;
    
    
    public ReadNodes(String path) throws IOException{
        outputString="";
        String input=reader(path);
        String lines[]=input.split("\n");
        boolean nodesflag=false;
         for (String line : lines) {
             
            if(!nodesflag && !line.contains("\"xmlStructure\" : ["))
                continue;
            if(line.contains("\"xmlStructure\" : [")){
                nodesflag=true;
                continue;
            }
                
             
            if (line.contains("\"id\" : \"")){
                line=line.replace("\"id\" : ", "");
                line=line.replace(" ", "");
                System.out.println(line);
                outputString+="  "+line+"\n";
                                                   
            }
            if (line.contains("\"parentId\" :")){
                line=line.replace("\"parentId\" :", "");
                line=line.replace(" ", "");
                System.out.println(line);
                outputString+="  "+line+"\n";
            }
            if (line.contains("\"xpathAbsolute\" :")){
                line=line.replace("\"xpathAbsolute\" :", "");
                line=line.substring(line.indexOf(": ")+4);
                System.out.println(line);
                outputString+=line+"\n";
            }
            if (line.contains("\"xpathRelative\" :")){
                line=line.replace("\"xpathRelative\" :", "");
                line=line.substring(line.indexOf(": ")+4);
                System.out.println(line);
                outputString+=line+"\n";
            }
            if (line.contains("\"repeatable\" :")){
                line=line.replace("\"repeatable\" :", "");
                line=line.replace(" ", "");
                System.out.println(line);
                if(line.contains(","))             
                   outputString+="  "+line+"\n";
                else
                   outputString+="  "+line+","+"\n";
            }
            if(line.contains("\"fields\" : [ {")){
                break;
            }
               
         }
    }
    
    public String getOutputString(){
        return outputString;
    }
    public String reader(String path) throws FileNotFoundException{
        Scanner scan = new Scanner(new BufferedReader(new FileReader(path)));
        String output = scan.nextLine()+"\n\n";
        while(scan.hasNext())
            output+=scan.nextLine()+"\n\n";
        return output; 
    }

    //makeFile method, opens a specified file,and writes a text String to it. 
    private void makeFile(String path, String text) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            if (!text.isEmpty()) {
                writer.write(text);
            }
            writer.flush();
        }
    }  
      
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        
        String path="C:\\Users\\achid\\OneDrive\\Desktop\\fields.json\\1.11.1\\";    
        String fieldsJsonPath=path+"fields.json";
        ReadNodes read=new ReadNodes(fieldsJsonPath);
        read.makeFile("C:\\Users\\achid\\OneDrive\\Desktop\\1.11.1Nodes.txt",read.getOutputString());
    }
}

