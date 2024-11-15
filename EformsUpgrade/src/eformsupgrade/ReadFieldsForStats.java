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
public class ReadFieldsForStats {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
  
    
    public String outputString;
    
    
    public ReadFieldsForStats(String path) throws IOException{
        //outputString="id\tparentNodeId\tname\tbtId\txpathAbsolute\txpathRelative\ttype\tlegalType\trepeatable\tcodeList";
        outputString="";
        String input=reader(path);
        String lines[]=input.split("\n");
        boolean flag=false;
        boolean repFlag=false;
        boolean codFlag=false;
        boolean firstflag=true;
        boolean PrivFlag=false;
         for (String line : lines) {
             
            if(!flag && !line.contains("\"fields\" : [ {"))
               continue;
            else
               flag=true;
             
            
            if(PrivFlag){
                if(line.contains("},")){
                   outputString+="]\t";
                   PrivFlag=false;
                }
                if (line.contains("\"code\" : ")){
                    line=line.replace("\"code\" : ","");
                    line=line.replace(" ","");
                    line=line.replace("\"","'");
                    line=line.replace(",",", ");
                    outputString+=line;
                }
                if (line.contains("\"unpublishedFieldId\" : ")){
                    line=line.replace("\"unpublishedFieldId\" : ", "");
                    line=line.replace(" ","");
                    line=line.replace("\"","'");
                    line=line.replace(",",", ");
                    outputString+=line;    
                }
                 if (line.contains("\"reasonCodeFieldId\" : ")){
                    line=line.replace("\"reasonCodeFieldId\" : ", "");
                    line=line.replace(" ","");
                    line=line.replace("\"","'");
                    line=line.replace(",",", ");
                    outputString+=line;    
                }
                  if (line.contains("\"reasonDescriptionFieldId\" : ")){
                    line=line.replace("\"reasonDescriptionFieldId\" : ", "");
                    line=line.replace(" ","");
                    line=line.replace("\"","'");
                    line=line.replace(",",", ");
                    outputString+=line;    
                }
                   if (line.contains("\"publicationDateFieldId\" : ")){
                    line=line.replace("\"publicationDateFieldId\" : ", "");
                    line=line.replace(" ","");
                    line=line.replace("\"","'");
                    line=line.replace(",",", ");
                    outputString+=line;    
                }
            }
             
            
            
            
            if(repFlag){
                if(line.contains("},")){
                   outputString+="  }\t";
                   repFlag=false;
                }
                if (line.contains("\"value\" : ")){
                    line=line.replace(" ", "");
                    line=line.replace(":", ": ");
                    outputString+="    "+line+"";
                }
                if (line.contains("\"severity\" : ")){
                    line=line.replace(" ", "");
                    line=line.replace(":", ": ");
                    outputString+="    "+line;    
                }
            }
            
            if(codFlag){
                if(line.contains("\"severity\" : ")){
                  line=line.replace(" ", "");
                  line=line.replace(":", ": ");
                  outputString+="    "+line+"";
                  outputString+="  }";
                  codFlag=false;
                }
                if (line.contains("\"value\" : ")){
                    line=line.replace(" ", "");
                    line=line.replace(":", ": ");
                    outputString+="    "+line+"";
                }
                if (line.contains("\"id\" : ")){
                    line=line.replace(" ", "");
                    line=line.replace(":", ": ");
                    outputString+="      "+line+"";
                }
                 if (line.contains("\"type\" : ")){
                    line=line.replace(" ", "");
                    line=line.replace(":", ": ");
                    outputString+="      "+line+"";
                }
                if (line.contains("\"parentId\" : ")){
                    line=line.replace(" ", "");
                    line=line.replace(":", ": ");
                    outputString+="      "+line+"";
                }
                if (line.contains("},")){
                   outputString+="    }";
                }  
                 
            }
            
            if (line.contains("\"id\" : \"")&& !codFlag){
                //make sure that this is an id
                if(line.contains("\"BT-")||line.contains("\"OPA-")||line.contains("\"OPP-")||line.contains("\"OPT-")||line.contains("\"BT-")){
                    
                    if(!firstflag){
                        outputString+="\n";
                    }
                    firstflag=false;
                    line=line.replace("\"id\" : ", "");
                    line=line.replace(" ", "");
                    line=line.replace(",", "");
                    outputString+="  "+line+"\t";
                }                               
            }
            if (line.contains("\"parentNodeId\" :")){
                line=line.replace("\"parentNodeId\" :", "");
                line=line.replace(" ", "");
                line=line.replace(",", "");
                outputString+="  "+line+"\t";
            }
            if (line.contains("\"name\" :")){
                line=line.replace("\"name\" :", "");
                line=line.substring(line.indexOf(": ")+4);
                line=line.replace(",", "");
                outputString+=line+"\t";
            }
            if (line.contains("\"btId\" :")){
                line=line.replace("\"btId\" :", "");
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
     
            if (line.contains("\"type\" :")&& !codFlag&& !line.contains("\"flat\"")) {
               line=line.replace("\"type\" :", "");
               line=line.substring(line.indexOf(": ")+4);
               line=line.replace(",", "");
               outputString+=line+"\t";
            }
            
            if (line.contains("\"attributeName\" :")){
                line=line.replace(" ", "");
                line=line.replace(",", "");
                outputString+="  "+line+"\t";
            }
            
            if (line.contains("\"attributeOf\" :")){
                line=line.replace(" ", "");
                line=line.replace(",", "");
                outputString+="  "+line+"\t";
            }
            
       
            if (line.contains("\"legalType\" :")){
               line=line.replace("\"legalType\" :", "");
               line=line.substring(line.indexOf(": ")+4);
               line=line.replace(",", "");
               outputString+=line+"\t";
            }
                       
            if (line.contains("\"privacy\" :")){
               outputString+="  "+ "privacy[";
               PrivFlag=true;
            }
            
            
            if (line.contains("\"repeatable\" :")){
                outputString+="  "+ "{";
                repFlag=true;
            }
            
            if (line.contains("\"codeList\" :")){
               outputString+="  "+ "{";
               codFlag=true;
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
        
        //String path="C:\\Users\\achid\\OneDrive\\Desktop\\ef16ef29Different versions\\ef13\\1.8\\fields.json";
        String path="C:\\Users\\achid\\OneDrive\\Desktop\\fields.json\\1.10.4\\";    
        String fieldsJsonPath=path+"fields.json";
        ReadFieldsForStats read=new ReadFieldsForStats(fieldsJsonPath);
        read.makeFile("C:\\Users\\achid\\OneDrive\\Desktop\\1.10.4Fieldsstats.txt",read.getOutputString());
        
    }
}



