/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author achid
 * Old Class that calculated percentages of eform coverage 
 */
public class Eformstats {

    //Lists of all fields and their attributes
    ArrayList<OldField> fields;
    String path;    
    List<List<String>> forms;     
    int numberOfFieldsPerForm[];
    int numberofcommonfieldsperForm[];
    Eformstats(String path){
        this.path=path;
        numberOfFieldsPerForm= new int[45];
        numberofcommonfieldsperForm=new int[45];
    }
   
    //To create just_fields:Inut fields.json in https://jsonpath.com. Use 
    //$.fields[*].id,parentNodeId,name,btId,xpathAbsolute,xpathRelative,repeatable
    //and remove the '[' and ']' from start and finish 
    private void loadFields() throws FileNotFoundException, IOException{
        //read all fields of a specific version and save them as a structure
        String just_fields;
        just_fields=reader(path+"just_fields.txt");
        String lines[]=just_fields.split(",");
        fields=new ArrayList<>();
        boolean repeatable;
        int j=0;
        String id,parentNodeId,name,btId,xpathAbsolute,xpathRelative;
        String fieldsLineByLine="";
       
         for (String line : lines) {
            if (line.contains("}")){
                //here we have all the information of a field that we require 
                String singleField[]=fieldsLineByLine.split("\",");
                id=singleField[0];
                id=id.replace("\"","");
                parentNodeId=singleField[1];
                parentNodeId=parentNodeId.replace("\"","");
                name=singleField[2];
                name=name.replace("\"","");                
                btId=singleField[3]; 
                btId=btId.replace("\"","");                
                xpathAbsolute=singleField[4];
                xpathAbsolute=xpathAbsolute.replace("\"","");               
                xpathRelative=singleField[5];
                xpathRelative=xpathRelative.replace("\"","");               
                repeatable = !singleField[6].contains("false");   
                OldField fl=new OldField(j,id,parentNodeId,name,btId,xpathAbsolute,xpathRelative,repeatable);
                
                //put field in the fields list
                fields.add(fl);
                j++;
                fieldsLineByLine="";                               
            }
            else 
            { 
                //that line has information about a field but it is not complete yet 
                line= line.replace("\n","");
                line= line.replace(" ", "");
                //line= line.replace("\"","");
                fieldsLineByLine+=line+",";
            }    
        }
        //test what we have for each field  
        System.out.print("\n########################## All the FIELDS ##########################\n");
        String print;
        String prints="";
        for (OldField field : fields) {
            //for showing
            print=field.getIntId()+"\t"+field.getId()+"\t"+field.getparentNodeId()+"\t"+field.getName()+"\t"+field.getbtId()+"\t"+field.getXpathAbsolute()+"\t"+field.getXpathRelative()+"\t"+field.isRepeatable()+"\n";
            System.out.print(print);
            prints+=print;
  
        }
    }
  
    //For each of the 45 forms, get all the "ids" that exist in fields and put
    //them into forms. The first 40 are the forty forms, 41 is CEI, 42 is T01,
    //43 is T02, 44 is X01, 45 is X02. 
    private void loadForms() throws FileNotFoundException, IOException{
       
      forms = new ArrayList<>(45); 
      String form;
      for(int i=1;i<41;i++){
          forms.add(getIDs(path+i+".json","ef"+i,i-1));
         
      }
      forms.add(getIDs(path+"CEI.json","CEI",40));
      forms.add(getIDs(path+"T01.json","T01",41));
      forms.add(getIDs(path+"T02.json","T02",42));
      forms.add(getIDs(path+"X01.json","X01",43));
      forms.add(getIDs(path+"X02.json","X02",44));
   }
    
    private List<String> getIDs(String formPath,String formname,int pointer) throws FileNotFoundException{
        List<String> list = new ArrayList<>();
        list.add(formname);
        String form=reader(formPath);
        String lines[]=form.split("\n");
        int i=0;
        for (OldField field : fields) {
                               
                //check if the field exists in the eform
                for(String line:lines){    
                    String ticks="\""+field.getId()+"\"";
                    if((line.contains(ticks)) &&(line.contains("\"id\" :"))){
                        list.add(field.getId());
                        i++;
                        break;
                    }
                }
        }
        
        //print everything for each form
        /* 
        System.out.print("\n########################## form "+formname+" # of fields: "+i+" ##########################\n");
        for (String string:list){
            System.out.println(string);
        }
        */
        numberOfFieldsPerForm[pointer]=i;
        return list;
    }
   
    
    //Lets assume that ef16 is completed. Calculate for all the other lists the 
    //
    
    private void ef16CompletedPercentages() {
        //find the eform16
        List<String> list = new ArrayList<>();
        //this prints ef16 :)
        //System.out.println("Forms "+forms.get(15).get(0));
        list=forms.get(15);
        //For each of the forms exceptef 16 calculate the number of fields that 
        //they share with ef16.  
        for(int i=0;i<45;i++){
            int meter=0;
            if(i!=15){
               int size=forms.get(i).size();
               //avoid j=0 as it's the number of the form
               for (int j=1;j<size;j++){
                   for(int x=1;x<list.size();x++){
                       if(list.get(x).equals(forms.get(i).get(j))){
                            meter++;
                            break;
                       }
                   }
               }
               numberofcommonfieldsperForm[i]=meter;
               float b=numberofcommonfieldsperForm[i];
               float c=numberOfFieldsPerForm[i];
               //System.out.println("eForm "+(i+1)+" is "+numberofcommonfieldsperForm[i]+" / "+numberOfFieldsPerForm[i]+" = "+(b/c*100)+ " complete, if ef16 is complete.");
               System.out.println("eForm"+(i+1)+"\t"+c+"\t"+b+"\t"+(int)(b/c*100));

            }
        }
    }
   private void ef16ANDef29CompletedPercentages() {
        //find the eform16
        List<String> list = new ArrayList<>();
        //this prints ef16 :)
        //System.out.println("Forms "+forms.get(15).get(0));
        list=forms.get(15);
        System.out.println("LIST HAS "+list.size()+" elements!");
        //also add fields of 29 not common to 16. 
         boolean exists=false;
        for (int k=1;k<forms.get(28).size();k++){
       
            for(int l=1;l<list.size();l++){
                if(forms.get(28).get(k).equals(list.get(l))){
                    exists=true;
                }
            }
            if (exists==false){
                list.add(forms.get(28).get(k));
            }
            exists=false;
        }
        System.out.println("LIST HAS "+list.size()+" elements!");
        for (int h=0;h<list.size();h++){
            System.out.println(list.get(h));
        }
                
        //For each of the forms except 16 and 29 calculate the number of fields that 
        //they share with ef16 and 29.  
        for(int i=0;i<45;i++){
            int meter=0;
            if((i!=15)&&(i!=28)){
               int size=forms.get(i).size();
               //avoid j=0 as it's the number of the form
               for (int j=1;j<size;j++){
                   for(int x=1;x<list.size();x++){
                       if(list.get(x).equals(forms.get(i).get(j))){
                            meter++;
                            break;
                       }
                   }
               }
               numberofcommonfieldsperForm[i]=meter;
               float b=numberofcommonfieldsperForm[i];
               float c=numberOfFieldsPerForm[i];
               //System.out.println("eForm "+(i+1)+" is "+numberofcommonfieldsperForm[i]+" / "+numberOfFieldsPerForm[i]+" = "+(b/c*100)+ " complete, if ef16 AND ef29 are complete.");
               System.out.println("eForm"+(i+1)+"\t"+c+"\t"+b+"\t"+(int)(b/c*100));
            }
        }
    } 
   
   
   
    private void getDescriptionFromAllforms(String groupID) throws FileNotFoundException{
        for(int i=1;i<41;i++){
         getGroupDescriptionForGroup(path+i+".json","form"+i,groupID);
        }
        getGroupDescriptionForGroup(path+"CEI.json","form CEI",groupID);
        getGroupDescriptionForGroup(path+"T01.json","form T01",groupID);
        getGroupDescriptionForGroup(path+"T02.json","form T02",groupID);
        getGroupDescriptionForGroup(path+"X01.json","form X01",groupID);
        getGroupDescriptionForGroup(path+"X02.json","form X02",groupID);
       
   }
     
    private void getGroupDescriptionForGroup(String formPath,String formString,String groupID) throws FileNotFoundException{
          
        String form=reader(formPath);
        String lines[]=form.split("\n");
        boolean GRDetected=false;
                               
        //check if the field exists in the eform
        for(String line:lines){    
            if((line.contains(groupID)) &&(line.contains("\"id\" :")))
                GRDetected = true;
            if((GRDetected)&&(line.contains("\"description\" :"))){
                System.out.println(formString+line.substring(line.indexOf(":")));
                break;
            }                       
        }
    }
    
    void checkIfDescriptionChangesForAllFields() throws FileNotFoundException{
        String form=reader(path+"X01.json");
        String lines[]=form.split("\n");
        String currentlyInvestigating="";
        boolean fieldDetected=false;
        for(String line:lines){    
            if(line.contains("\"id\" :")){
                currentlyInvestigating=line.substring(line.indexOf(":")+1);
                System.out.print("Currently investigating "+currentlyInvestigating);
                fieldDetected = true;
            }
            if((fieldDetected)&&(line.contains("\"description\" :"))){
                fieldDetected=false;
                String DescCurrentlyInvestigating=line.substring(line.indexOf(":")+1);
                System.out.print(" Description String "+DescCurrentlyInvestigating);
                
                for(int i=1;i<41;i++){
                    if(!IsGroupDescriptionForGroupThesame(path+i+".json",currentlyInvestigating,DescCurrentlyInvestigating)){
                        System.out.println(" dif in form"+i);
                        break;
                    }
                }
                System.out.println(" Description is the same in all forms");
            }
                         
        }
    }
    boolean IsGroupDescriptionForGroupThesame(String formPath, String id,String desc) throws FileNotFoundException{
        boolean result=true;
        String form=reader(formPath);
        String lines[]=form.split("\n");
        boolean GRDetected=false;
                               
        //check if the field exists in the eform
        for(String line:lines){    
            if((line.contains(id)) &&(line.contains("\"id\" :")))
                GRDetected = true;
            if((GRDetected)&&(line.contains("\"description\" :"))){
                if(!desc.equals(line.substring(line.indexOf(":")+1))){
                    result=false;
                }
                break;
            }                       
        }
        return result;
    }
            
            
            
    //reader method, opens a file, and returns its contents as a String. 
    private String reader(String path) throws FileNotFoundException{
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
        
         Eformstats stats = new Eformstats("C:\\Users\\achid\\OneDrive\\Desktop\\ef16ef29Different versions\\allForms_1.9.1\\");
        stats.loadFields();
        stats.loadForms();
        //stats.ef16CompletedPercentages();
        stats.ef16ANDef29CompletedPercentages();
        
        //stats.getIDs("C:\\Users\\achid\\OneDrive\\Desktop\\ef16ef29Different versions\\allForms_1.9.1\\X01.json", "X01.json",1);
        //stats.getDescriptionFromAllforms("BT-01-notice");
        //stats.checkIfDescriptionChangesForAllFields();
        
    } 
    
}
