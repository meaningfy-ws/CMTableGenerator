/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standalone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author achid
 * Calculates mandatory fields
 */
public class CalcMandatory {
    
    
    
    
    
    
    
    CalcMandatory(){}
    
    
 //reader method, opens a file, and returns its contents as a String. 
    private String reader(String path) throws FileNotFoundException{
        Scanner scan = new Scanner(new BufferedReader(new FileReader(path)));
        String output = scan.nextLine()+"\n\n";
        while(scan.hasNext())
            output+=scan.nextLine()+"\n\n";
        return output; 
    }
    List<String> findAll(String path,String form) throws FileNotFoundException{
        List<String> list = new ArrayList<>();
        String mandatory=reader(path);      
        String lines[]=mandatory.split("\n");
        String field="";
        boolean firsthookFlag=false;
        for (int i=0;i<lines.length;i++) {
            if (!firsthookFlag && (lines[i].contains("{"))&&(lines[i-2].contains("-"))){
                
                field=lines[i-2];
                firsthookFlag=true;
            }
            if((firsthookFlag&&lines[i].contains("noticeTypes"))){
                firsthookFlag=false;
            }
            if((lines[i].contains(form))&& (!lines[i].contains("-"))){
                list.add(field);
            }
            
        }
        return list;
    }
    void remainder(String path,String a, String b) throws FileNotFoundException{
        List<String> alist =findAll(path,a);
        
        
        
        
        List<String> blist =findAll(path,b);
        for (String c: alist)
            System.out.println(c);
        
                      
    }

 public static void main(String[] args) throws FileNotFoundException, IOException {
         CalcMandatory calc=new CalcMandatory();
         calc.remainder("C:/Users/achid/OneDrive/Desktop/ef16ef29Different versions/allForms_1.9.1/mandatory.txt","29","16");
     //calc.findAll("C:/Users/achid/OneDrive/Desktop/ef16ef29Different versions/allForms_1.9.1/mandatory.txt","16");
       
        //stats.ef16ANDef29CompletedPercentages();
        
        //stats.getIDs("C:\\Users\\achid\\OneDrive\\Desktop\\ef16ef29Different versions\\allForms_1.9.1\\X01.json", "X01.json",1);
        //stats.getDescriptionFromAllforms("BT-01-notice");
        //stats.checkIfDescriptionChangesForAllFields();
        
    } 
    
}
