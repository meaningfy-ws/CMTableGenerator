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
public class ReadNotice {
    
    public String outputString;
    
    
    public ReadNotice(String path) throws IOException{
        outputString="";
        String input=reader(path);
        String lines[]=input.split("\n");

         for (String line : lines) {
            if (line.contains("\"id\" : \"")){
                line=line.replace("\"id\" : ", "");
                line=line.replace(" ", "");
                //System.out.println(line);
                outputString+="  "+line+"\n";
                                                   
            }
            if (line.contains("\"nodeId\" :")){
                line=line.replace("\"nodeId\" :", "");
                line=line.replace(" ", "");
                //System.out.println(line);
                outputString+="  "+line+"\n";
            }
               if (line.contains("\"description\" :")){
                line=line.replace("\"description\"", "");
                line=line.substring(line.indexOf(": ")+1);
                
                //System.out.println(line);
                outputString+=" "+line+"\n";
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
        
        String path="C:\\Users\\achid\\OneDrive\\Desktop\\fields.json\\1.12.0\\1.json";
        
        ReadNotice read=new ReadNotice(path);
        read.makeFile("C:\\Users\\achid\\OneDrive\\Desktop\\test.txt",read.getOutputString());
    }
}