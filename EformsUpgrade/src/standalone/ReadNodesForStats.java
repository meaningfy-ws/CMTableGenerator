/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standalone;

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
public class ReadNodesForStats {

    
    public String outputString;
    
    
    public ReadNodesForStats(String path) throws IOException{
        outputString="id\tparentId\txpathAbsolute\txpathRelative\trepeatable\t";
        String input=reader(path);
        String lines[]=input.split("\n");
        boolean nodesflag=false;
        boolean firstNode=true;
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
                outputString+="\n";
                line=line.replace(",", "");
                outputString+="  "+line+"\t";
                if(firstNode){
                   outputString+="\t"; //first Node ND-Root does not have a parentId, so we need to add an extra tab
                   firstNode=false;
                }
            }
            if (line.contains("\"parentId\" :")){
                line=line.replace("\"parentId\" :", "");
                line=line.replace(" ", "");
                line=line.replace(",", "");
                outputString+="  "+line+"\t";
            }
            if (line.contains("\"xpathAbsolute\" :")){
                line=line.replace("\"xpathAbsolute\" :", "");
                line=line.substring(line.indexOf(": ")+4);
                line=line.replace(",", "");
                outputString+=line+"\t";
            }
            if (line.contains("\"xpathRelative\" :")){
                line=line.replace("\"xpathRelative\" :", "");
                line=line.substring(line.indexOf(": ")+4);
                line=line.replace(",", "");
                outputString+=line+"\t";
            }
            if (line.contains("\"repeatable\" :")){
                line=line.replace("\"repeatable\" :", "");
                line=line.replace(" ", "");
                if(line.contains(",")){
                   line=line.replace(",", "");
                   outputString+="  "+line+"\t";
                }
                else
                    
                   outputString+="  "+line+"\t";
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
        
        
        String path="C:\\Users\\achid\\OneDrive\\Desktop\\fields.json\\1.12.123\\";    
        String fieldsJsonPath=path+"fields.json";
        ReadNodesForStats read=new ReadNodesForStats(fieldsJsonPath);
        read.makeFile("C:\\Users\\achid\\OneDrive\\Desktop\\1.12.123Nodes.txt",read.getOutputString());
    }
}

