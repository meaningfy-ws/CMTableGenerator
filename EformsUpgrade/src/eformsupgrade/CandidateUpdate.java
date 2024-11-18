/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eformsupgrade;

import static eformsupgrade.DFS.isReachable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author achid
 */
public class CandidateUpdate {
    
   //fields to replace fieldsOld and fieldsNew
   ArrayList<Field> fields;

   String fieldsJsonPath;
   ArrayList<Node> nodes;
    
   //nodesGraph creates a graph of the nodes and parents. We use this graph with DFS
   //in order to find the path between a node and the root. 
   Graph nodesGraph;
    
   //constructor 
   CandidateUpdate(String fieldsJsonPath) throws IOException, IOException, IOException, IOException, IOException{
        this.fieldsJsonPath=fieldsJsonPath;
    }
  

   //uses ReadFieldsForStats class to read all fields from the fields.json file and loads them to ArrayList<Field> fields as Field class object . 
   private void loadFields() throws FileNotFoundException, IOException{
        //read all fields of a specific version and save them as a structure
        ReadFieldsForStats read=new ReadFieldsForStats(fieldsJsonPath);
        String just_fields=read.getOutputString();
        
        //String just_fields=reader("C:\\Users\\achid\\OneDrive\\Desktop\\1.10.4Fieldsstats.txt");
        ArrayList<Field> localFields = new ArrayList<>();
        String id,parentNodeId,name,btId,xpathAbsolute,xpathRelative,type,legalType,codeList_id,attributeName,attributeOf,privacy;
        boolean repeatable;
        String fieldlines[]=just_fields.split("\n");
        for  (int i=0; i<fieldlines.length;i++){
            //System.out.print("########################## field "+i+" ##########################");
            legalType="";
            codeList_id="";
            id="";
            parentNodeId="";
            name="";
            btId="";
            xpathAbsolute="";
            xpathRelative="";
            type="";
            repeatable=false;
            privacy="";
            attributeName="";
            attributeOf="";
             //System.out.println("i = "+i); 
            String cols[]=fieldlines[i].split("\t");
            
            for  (int j=0; j<cols.length;j++){
                //System.out.println("j = "+j);
                //System.out.println("cols[j] = "+cols[j]);

                //if eForms SDK ID(0), parentId(1), Name(2), BT ID(3),Absolute XPath(4),xpathRelative(5),type(6)  
                if((j==0)||(j==1)||(j==2)||(j==3)||(j==4)||(j==5)||(j==6)){
                    cols[j]=cols[j].substring(cols[j].indexOf("\""));
                    cols[j]=cols[j].replace("\"","");
                    if(j==0)
                        id = cols[j];
                    if(j==1)
                        parentNodeId = cols[j];
                    if(j==2)
                        name = cols[j];
                    if(j==3)
                        btId = cols[j];
                    if(j==4)
                        xpathAbsolute = cols[j];
                    if(j==5)
                        xpathRelative = cols[j];
                    if (j==6)
                        type = cols[j];
                     
                }
                if(j>=7){                    
                    //if legalType
                    if((!cols[j].contains("{"))&&(!cols[j].contains("attributeName"))&&(!cols[j].contains("attributeOf"))&&(!cols[j].contains("privacy["))){
                        cols[j]=cols[j].substring(cols[j].indexOf("\""));
                        cols[j]=cols[j].replace("\"","");
                        legalType = cols[j];  
                    }
                
                
                    //if repeatable        
                    if(cols[j].contains("{")){
                        if(cols[j].contains("false,"))
                            repeatable = false;
                   
                        else
                            if (cols[j].contains("true,"))
                                repeatable = true;
                    }
          
                
                    //if attributeName
                    if(cols[j].contains("attributeName")){
                        cols[j] = cols[j].substring(cols[j].indexOf(":")+1);
                        cols[j] = cols[j].replace("\"", "");
                        attributeName = cols[j];                   
                    }
                
                    //if attributeName
                    if(cols[j].contains("attributeOf")){
                        cols[j] = cols[j].substring(cols[j].indexOf(":")+1);
                        cols[j] = cols[j].replace("\"", "");
                        attributeOf = cols[j];                   
                    }                
                        
                
                    //if codeList/id
                    if(cols[j].contains("{")&&(!cols[j].contains("false")&&(!cols[j].contains("true")))){
                        cols[j]=cols[j].substring(cols[j].indexOf("\"id\":")-5);
                        cols[j]=cols[j].substring(cols[j].indexOf("\""), cols[j].indexOf(","));
                        cols[j]=cols[j].replace("\"","");     
                        cols[j]=cols[j].replace("id: ","");  
                        codeList_id = cols[j];
                    }
                
                    if(cols[j].contains("privacy[")){
                        cols[j]=cols[j].replace("privacy","");   
                        privacy= cols[j];
                    }
                }
                //System.out.println(cols[j]);

            }
            Field fl = new Field(i, id, parentNodeId, name, btId, xpathAbsolute, xpathRelative, repeatable, type, legalType, codeList_id,attributeName,attributeOf,privacy);
            //put field in the fields list
            localFields.add(fl);

        }
            
        //test what we have for each field  
        //System.out.print("\n########################## FIELDS ##########################\n");
                
        //for (Field field : localFields) {
            //for showing
           // print=field.getIntId()+"\t"+field.getId()+"\t"+field.getparentNodeId()+"\t"+field.getName()+"\t"+field.getbtId()+"\t"+field.getXpathAbsolute()+"\t"+field.getXpathRelative()+"\t"+field.isRepeatable()+"\t"+field.getType()+"\t"+field.getLegalType()+"\t"+field.getCodeList_id()+"\n";
         //   System.out.print(print);
        //}
      
        this.fields=localFields;   
             
    }

   //loadNodes uses ReadNodes class to read all nodes from the fields.json file and loads them to ArrayList<Node> nodes as Node class objects. 
   //loadNodes also creates a nodesGraph 
   private void loadNodes() throws FileNotFoundException, IOException {
        //read all nodes of a specific version and save them as a structure
        ReadNodes readN=new ReadNodes(fieldsJsonPath);
        String just_nodes=readN.getOutputString();
               
        ArrayList<Node> localNodes=new ArrayList<>();
        String nodeslinebyline="";
        String lines[]=just_nodes.split(",");
        String id,parentId,xpathAbsolute,xpathRelative;
        boolean repeatable;
        int j=0;
        //initialize edges list 
        List<Edge> edges=new ArrayList<>();
        //find each node 
        for (String line : lines) {
            if (line.contains("false")||(line.contains("true"))){
                line= line.replace("\n", "");
                line= line.replace(" ", "");
                
                nodeslinebyline+=line+"\n";
                //here we have all the information of a node that we require 
                String singleNode[]=nodeslinebyline.split("\",");
                id=singleNode[0];
                id=id.replace("\"","");   
                if(id.equals("ND-Root")){
                    parentId="";
                    xpathAbsolute=singleNode[1];
                    xpathRelative=singleNode[2];
                    repeatable=false;
                }
                else{
                    parentId=singleNode[1];
                    
                    xpathAbsolute=singleNode[2];
                    xpathRelative=singleNode[3];
                    repeatable = singleNode[4].contains("true");
                }
                parentId=parentId.replace("\"","");
                xpathAbsolute=xpathAbsolute.replace("\"","");
                xpathRelative=xpathRelative.replace("\"","");
                
                Node nd=new Node(j,id,parentId,xpathAbsolute,xpathRelative,repeatable);
                
                //put node in the nodes list
                localNodes.add(nd);
                //if the node has a parent add an edge from this node, to the parent.
                if(!id.equals("ND-Root")){
                    
                    for (Node node : localNodes) {
                        if (node.getId().equals(parentId)) 
                            edges.add(Edge.of(j, node.getIntId()));
                    }
                }
               
                j++;
                nodeslinebyline="";
                               
            }
            else 
            { 
                //that line has information of a node but it is not complete yet 
                line= line.replace("\n","");
                line= line.replace(" ", "");
                //line= line.replace("\"","");
                nodeslinebyline+=line+",";
            }    
        }
         
       //create graph and save nodes 
        Graph localNodesGraph;
        this.nodesGraph= new Graph(edges, j+1);
        localNodesGraph=this.nodesGraph;
        this.nodes=localNodes;
                  
        //test what we have for each node  
       
        System.out.print("\n########################## NODES ##########################\n");
        String print;
        String prints="";
        for (Node node : localNodes) {
            
            print=node.getIntId()+"\t"+node.getId()+"\t"+node.getParentId()+"\t"+node.getXpathAbsolute()+"\t"+node.getXpathRelative()+"\t"+node.isRepeatable()+"\n";
            System.out.print(print);
            prints+=print;       
            // to keep track of whether a vertex is discovered or not
            boolean[] discovered = new boolean[j+1];
            // source and destination vertex
            // To store the complete path between source and destination
            Stack<Integer> path = new Stack<>();
            // perform DFS traversal from the source vertex to check the connectivity
            // and store path from the source vertex to the destination vertex
            if (isReachable(localNodesGraph, node.getIntId(),0, discovered, path)){
                //System.out.println("Path exists from node " + node.getIntId() + " to node " + 0);
                //System.out.println("The complete path is " + path);
            }
            else {
                //System.out.println("No path exists between nodes " + node.getIntId() + " and " + 0);
            }
        }
        //Just for test
        //makeFile("C:/Users/achid/OneDrive/Desktop/Candidate_Upgrade1/test/nodes.txt",prints);
    }     

    
   private void Mmode(String noticepath, String outputPath) throws IOException {
        String output="";
        String eform;
        ArrayList<Field> fieldsTemp;
        ArrayList<Node> localNodes;
        Graph localNodesGraph;
        
        ReadNotice read=new ReadNotice(noticepath);
        eform=read.getOutputString();
        fieldsTemp=this.fields;
        localNodes=this.nodes;
        localNodesGraph=this.nodesGraph;
                
        System.out.print("\n########################## Mnode ##########################\n");

        String lines[]=eform.split(",");
        ArrayList<String> nodesIdsInOutput=new ArrayList<>();
                
        //get all the information of the groups, and update their respective nodes. 
        for(int i=0;i<lines.length;i++){
            if(lines[i].contains("GR-")){
                //remove/n," and spaces.
                lines[i]=lines[i].replace("\n", "");
                lines[i]=lines[i].replace("\"", "");
                lines[i]=lines[i].replace(" ", "");
                //2 cases: either the next line has the respective node
                //or if there is no node, we have the group's description.
                //however we do not care of the description of a group that
                //has no corresponding node.
                if(lines[i+1].contains("ND-")){
                    //if the next line is a node, search the nodes list
                    //for that node, and update the groupID and groupDescription
                    //attributes.
                    lines[i+1]=lines[i+1].replace("\n", "");
                    lines[i+1]=lines[i+1].replace("\"", "");
                    lines[i+1]=lines[i+1].replace(" ", "");
                    for(Node node:localNodes){
                        if(lines[i+1].equals(node.getId())){
                            node.setGroupId(lines[i]);
                            lines[i+2]=lines[i+2].replace("\n", "");
                            lines[i+2]=lines[i+2].replace("\"", "");
                            node.setGroupDescription(lines[i+2]);
                            break;
                        }
                    }
                }                       
            }                         
        }    
       
        //test
        //for(Node node:nodes){
        //    System.out.print(node.getId()+" "+node.getGroupId()+" "+node.getGroupDescription()+"\n");
        //}
        
        for(int i=0;i<lines.length;i++){
                //if line contains a field, 
                if(lines[i].contains("BT-")||lines[i].contains("OPT-")|| lines[i].contains("OPP-")){
                    //remove/n," and spaces.
                    lines[i]=lines[i].replace("\n", "");
                    lines[i]=lines[i].replace("\"", "");
                    lines[i]=lines[i].replace(" ", "");
                    
                    //seatch field list for the field referenced in lines[i] in
                    //order to create the output line about the fields, and the corresponding ND.
                    for (Field field : fieldsTemp) {
                        if(lines[i].equals(field.getId())){
                            //found the field. But before getting all the required information
                            //to output string we need to create its father node (if it's not already used).
                            //Check for all the nodesIdsInOutput
                            Boolean redFlag=false;
                            for(String nodeIDinOutput:nodesIdsInOutput){
                                
                                if(field.getparentNodeId().equals(nodeIDinOutput)){
                                    redFlag=true;
                                    break;
                                }
                            }
                            if(!redFlag){
                                //node not in output
                                //find the node in nodes
                                for(Node node:localNodes){
                                    if(field.getparentNodeId().equals(node.getId())){
                                        //found the parent node
                                        
                                        //find all the ancestors of the node
                                        // to keep track of whether a vertex is discovered or not
                                        boolean[] discovered = new boolean[localNodesGraph.getSize()+1];
                                        // source and destination vertex
                                        // To store the complete path between source and destination
                                        Stack<Integer> path = new Stack<>();
                                        // perform DFS traversal from the source vertex to check the connectivity
                                        // and store path from the source vertex to the destination vertex
                                        if (isReachable(localNodesGraph, node.getIntId(),0, discovered, path)){
                                            //System.out.println("Path exists from node " + node.getIntId() + " to node " + 0);
                                            //System.out.println("The complete path is " + path);
                                            //for each ancestor 
                                            for(int it=path.size()-1;it>=0;it--){
                                               path.get(it);
                                               //find that node in nodeslist
                                               for(Node node2:localNodes){
                                                   if(path.get(it).equals(node2.getIntId())){
                                                       //is it in the nodeIDinOutput
                                                       Boolean blackFlag=false;
                                                       for(String nodeIDinOutput:nodesIdsInOutput){
                                
                                                            if(node2.getId().equals(nodeIDinOutput)){
                                                                blackFlag=true;
                                                                break;
                                                            }
                                                        }
                                                        if(!blackFlag){
                                                            //ancestor node node2 not in output
                                                            //insert it
                                                            nodesIdsInOutput.add(node2.getId());
                                                            //System.out.print(printNodeForOutput(node2));
                                                            output+=printNodeForOutput(node2);  
                                                            //the last ancestor in the path its the node itself
                                                        }
                                                    break;
                                                   }
                                               }
                                            }

                                        }
                                                                       
                                        break;                                        
                                    }
                                    
                                }
                            }
                            //ok, parent node and all its parents are accounted for
                            //proceed with the field. 
                            
                            
                          // comment this area to only generate NDs/*  
                            output+=field.getId()+"\t"+field.getName()+"\t"+field.getbtId()+"\t"+field.getparentNodeId()+"\t"+"\t"+field.getXpathAbsolute()+"\t"+field.getXpathRelative();
                            if(field.isRepeatable()){
                                output+="\t\ttrue";
                            }
                            else{
                                output+="\t\tfalse";
                            }
                            output+="\t"+field.getType()+"\t"+field.getLegalType()+"\t"+field.getCodeList_id()+"\n";
                            
                            break;
                            //comment this area to only generate NDs*/
                        }
                    }                  
                }                                
        }
        System.out.print(output);
        makeFile(outputPath,output);
    }
        
   //generate a flat list of fields and Nodes for a specific notice     
   private String specialMnode(String noticepath, String notice) throws IOException {
        String output="";
        String eform;
        ArrayList<Field> fieldsTemp;
        ArrayList<Node> localNodes;
        Graph localNodesGraph;
        
        ReadNotice read=new ReadNotice(noticepath);
        eform=read.getOutputString();
        fieldsTemp=this.fields;
        localNodes=this.nodes;
        localNodesGraph=this.nodesGraph;
                    
        System.out.print("\n########################## Mnode ##########################\n");

        String lines[]=eform.split(",");
        ArrayList<String> nodesIdsInOutput=new ArrayList<>();
                
        //get all the information of the groups, and update their respective nodes. 
        for(int i=0;i<lines.length;i++){
            if(lines[i].contains("GR-")){
                //remove/n," and spaces.
                lines[i]=lines[i].replace("\n", "");
                lines[i]=lines[i].replace("\"", "");
                lines[i]=lines[i].replace(" ", "");
                //2 cases: either the next line has the respective node
                //or if there is no node, we have the group's description.
                //however we do not care of the description of a group that
                //has no corresponding node.
                if(lines[i+1].contains("ND-")){
                    //if the next line is a node, search the nodes list
                    //for that node, and update the groupID and groupDescription
                    //attributes.
                    lines[i+1]=lines[i+1].replace("\n", "");
                    lines[i+1]=lines[i+1].replace("\"", "");
                    lines[i+1]=lines[i+1].replace(" ", "");
                    for(Node node:localNodes){
                        if(lines[i+1].equals(node.getId())){
                            node.setGroupId(lines[i]);
                            lines[i+2]=lines[i+2].replace("\n", "");
                            lines[i+2]=lines[i+2].replace("\"", "");
                            node.setGroupDescription(lines[i+2]);
                            break;
                        }
                    }
                }                       
            }                         
        }    
       
        //test
        //for(Node node:nodes){
        //    System.out.print(node.getId()+" "+node.getGroupId()+" "+node.getGroupDescription()+"\n");
        //}
        
        for(int i=0;i<lines.length;i++){
                //if line contains a field, 
                if(lines[i].contains("BT-")||lines[i].contains("OPT-")|| lines[i].contains("OPP-")){
                    //remove/n," and spaces.
                    lines[i]=lines[i].replace("\n", "");
                    lines[i]=lines[i].replace("\"", "");
                    lines[i]=lines[i].replace(" ", "");
                    
                    //seatch field list for the field referenced in lines[i] in
                    //order to create the output line about the fields, and the corresponding ND.
                    for (Field field : fieldsTemp) {
                        if(lines[i].equals(field.getId())){
                            //found the field. But before getting all the required information
                            //to output string we need to create its father node (if it's not already used).
                            //Check for all the nodesIdsInOutput
                            Boolean redFlag=false;
                            for(String nodeIDinOutput:nodesIdsInOutput){
                                
                                if(field.getparentNodeId().equals(nodeIDinOutput)){
                                    redFlag=true;
                                    break;
                                }
                            }
                            if(!redFlag){
                                //node not in output
                                //find the node in nodes
                                for(Node node:localNodes){
                                    if(field.getparentNodeId().equals(node.getId())){
                                        //found the parent node
                                        
                                        //find all the ancestors of the node
                                        // to keep track of whether a vertex is discovered or not
                                        boolean[] discovered = new boolean[localNodesGraph.getSize()+1];
                                        // source and destination vertex
                                        // To store the complete path between source and destination
                                        Stack<Integer> path = new Stack<>();
                                        // perform DFS traversal from the source vertex to check the connectivity
                                        // and store path from the source vertex to the destination vertex
                                        if (isReachable(localNodesGraph, node.getIntId(),0, discovered, path)){
                                            //System.out.println("Path exists from node " + node.getIntId() + " to node " + 0);
                                            //System.out.println("The complete path is " + path);
                                            //for each ancestor 
                                            for(int it=path.size()-1;it>=0;it--){
                                               path.get(it);
                                               //find that node in nodeslist
                                               for(Node node2:localNodes){
                                                   if(path.get(it).equals(node2.getIntId())){
                                                       //is it in the nodeIDinOutput
                                                       Boolean blackFlag=false;
                                                       for(String nodeIDinOutput:nodesIdsInOutput){
                                
                                                            if(node2.getId().equals(nodeIDinOutput)){
                                                                blackFlag=true;
                                                                break;
                                                            }
                                                        }
                                                        if(!blackFlag){
                                                            //ancestor node node2 not in output
                                                            //insert it
                                                            nodesIdsInOutput.add(node2.getId());
                                                            //System.out.print(printNodeForOutput(node2));
                                                            output+=node2.getId()+"\t"+notice+"\n";
                                                            //the last ancestor in the path its the node itself
                                                        }
                                                    break;
                                                   }
                                               }
                                            }

                                        }
                                                                       
                                        break;                                        
                                    }
                                    
                                }
                            }
                            //ok, parent node and all its parents are accounted for
                            //proceed with the field. 
                            
                            
                          // comment this area to only generate NDs/*  
                            output+=field.getId()+"\t"+notice+"\n";
                            
                            break;
                            //comment this area to only generate NDs*/
                        }
                    }
                  
                }                                
        }
        System.out.print(output);
        return(output);
    }
     
   //how should a Node be printed in a CMtable
   String printNodeForOutput(Node node){
        String temp=node.getId()+"\t\t\t"+node.getParentId()+"\t\t"+node.getXpathAbsolute()+"\t"+node.getXpathRelative()+"\t\t";
        if(node.isRepeatable()){
            temp+="\ttrue";
        }
        else{
            temp+="\tfalse";
        }
               //just for specific eforms
        //temp+="\t\t\t\t\t"+node.getGroupId()+"\t"+node.getGroupDescription()+"\n";
         temp+="\n";
        return temp;
    }  
    
   //how should a Field be printed in a CMtable
   String prinFieldForOutput(Field field){    
        String output=field.getId()+"\t"+field.getName()+"\t"+field.getbtId()+"\t\t"+field.getparentNodeId()+"\t"+field.getXpathAbsolute()+"\t"+field.getXpathRelative()+"\t"+field.getAttributeName()+"\t"+field.getAttributeOf()+"\t";
        if(field.isRepeatable()){
            output+="\ttrue";
        }
        else{
            output+="\tfalse";
        }
        output+="\t"+field.getType()+"\t"+field.getLegalType()+"\t"+field.getCodeList_id()+"\t"+field.getPrivacy()+"\n";
        return output;
    }                
    
   //Generate the text that can be exported as a TSV table with all fields and Nodes of an SDK 
   private String generateSDKCMtableString() {
        String output="";
       for (Node node : nodes) {
           output += printNodeForOutput(node);
       }
       for (Field field: fields) {
           output +=prinFieldForOutput(field);
       }
       return output;
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
   
   
   //Generate table for a specific SDK.
   //HEADER: eForms SDK ID	Name	BT ID	parentId	parentNodeId	Absolute XPath	xpathRelative	attributeName	attributeOf	repeatable	repeatable/value	type	legalType	codeList/id	privacy
   public void generateSDKTable(String outputPath) throws IOException{
        loadNodes();
        loadFields();
        String outputString=generateSDKCMtableString();
        makeFile(outputPath, outputString);
    }
   
   //For a certain SDK, for each notice, generate a flat ordered list of every node and field belonging to that notice
   public void generateFieldNodeFlatlist(String path,String outputPath) throws IOException{
       loadNodes();
       loadFields();
       String outputString="";
       outputString+=specialMnode(path+"1"+".json","1")+"\n";
       outputString+=specialMnode(path+"2"+".json","2")+"\n";
       outputString+=specialMnode(path+"3"+".json","3")+"\n";
       outputString+=specialMnode(path+"4"+".json","4")+"\n";
       outputString+=specialMnode(path+"5"+".json","5")+"\n";
       outputString+=specialMnode(path+"6"+".json","6")+"\n";
       outputString+=specialMnode(path+"7"+".json","7")+"\n";
       outputString+=specialMnode(path+"8"+".json","8")+"\n";
       outputString+=specialMnode(path+"9"+".json","9")+"\n";
       outputString+=specialMnode(path+"10"+".json","10")+"\n";
       outputString+=specialMnode(path+"11"+".json","11")+"\n";
       outputString+=specialMnode(path+"12"+".json","12")+"\n";
       outputString+=specialMnode(path+"13"+".json","13")+"\n";
       outputString+=specialMnode(path+"14"+".json","14")+"\n";
       outputString+=specialMnode(path+"15"+".json","15")+"\n";
       outputString+=specialMnode(path+"16"+".json","16")+"\n";
       outputString+=specialMnode(path+"17"+".json","17")+"\n";
       outputString+=specialMnode(path+"18"+".json","18")+"\n";
       outputString+=specialMnode(path+"19"+".json","19")+"\n";
       outputString+=specialMnode(path+"20"+".json","20")+"\n";
       outputString+=specialMnode(path+"21"+".json","21")+"\n";
       outputString+=specialMnode(path+"22"+".json","22")+"\n";
       outputString+=specialMnode(path+"23"+".json","23")+"\n";
       outputString+=specialMnode(path+"24"+".json","24")+"\n";
       outputString+=specialMnode(path+"25"+".json","25")+"\n";
       outputString+=specialMnode(path+"26"+".json","26")+"\n";
       outputString+=specialMnode(path+"27"+".json","27")+"\n";
       outputString+=specialMnode(path+"28"+".json","28")+"\n";
       outputString+=specialMnode(path+"29"+".json","29")+"\n";
       outputString+=specialMnode(path+"30"+".json","30")+"\n";
       outputString+=specialMnode(path+"31"+".json","31")+"\n";
       outputString+=specialMnode(path+"32"+".json","32")+"\n";
       outputString+=specialMnode(path+"33"+".json","33")+"\n";
       outputString+=specialMnode(path+"34"+".json","34")+"\n";
       outputString+=specialMnode(path+"35"+".json","35")+"\n";
       outputString+=specialMnode(path+"36"+".json","36")+"\n";
       outputString+=specialMnode(path+"37"+".json","37")+"\n";
       outputString+=specialMnode(path+"38"+".json","38")+"\n";
       outputString+=specialMnode(path+"39"+".json","39")+"\n";
       outputString+=specialMnode(path+"40"+".json","40")+"\n";
       outputString+=specialMnode(path+"CEI"+".json","CEI")+"\n";
       //outputString+=specialMnode(path+"E1"+".json","E1")+"\n";
       //outputString+=specialMnode(path+"E2"+".json","E2")+"\n";
       //outputString+=specialMnode(path+"E3"+".json","E3")+"\n";
       //outputString+=specialMnode(path+"E4"+".json","E4")+"\n";
       //outputString+=specialMnode(path+"E5"+".json","E5")+"\n";
       //outputString+=specialMnode(path+"E6"+".json","E6")+"\n";
       outputString+=specialMnode(path+"T01"+".json","T01")+"\n";
       outputString+=specialMnode(path+"T02"+".json","T02")+"\n";
       outputString+=specialMnode(path+"X01"+".json","X01")+"\n";
       outputString+=specialMnode(path+"X02"+".json","X02")+"\n";
      
       makeFile(outputPath, outputString);
   }
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
               
    String path="C:\\Users\\achid\\OneDrive\\Desktop\\fields.json\\1.10.4\\";    
    String fieldsJsonPath=path+"fields.json";
    String outputPath="C:\\Users\\achid\\OneDrive\\Desktop\\1.10.4.txt";
    
    CandidateUpdate update= new CandidateUpdate(fieldsJsonPath);  
    
    //script 1)Generate table for a specific notice of a specific SDK.
     //String noticePath=path+"21.json";
     //update.loadNodes();
     //update.loadFields();
     //update.Mmode(noticePath,outputPath);  
    
    
    //script 2)Generate table for a specific SDK.
    //HEADER: eForms SDK ID	Name	BT ID	parentId	parentNodeId	Absolute XPath	xpathRelative	attributeName	attributeOf	repeatable	repeatable/value	type	legalType	codeList/id	privacy
    //update.generateSDKTable(outputPath);
 
    //script 3) For a certain SDK, for each notice, generate a flat ordered list of every node and field belonging to that notice
    update.generateFieldNodeFlatlist(path,outputPath);
    
    } 
       
}
