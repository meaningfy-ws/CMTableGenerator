/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eformsupgrade;

/**
 * class Node 
 * class node represents a node as found in fields.json file of the eforms SDK. 
 * It has the following attributes:
 * id, parentId, xpathAbsolute, xpathRelative, repeatable 
 * Also, If the node corresponds to a field group (i.e. it appears as a value for a nodeId property of a group, 
 * i.e. items marked with "contentType" : "group" and whose id starts with GR-) according to the eForms subtype
 * configuration file, also provide the id and description values of that group.
 * 
 * @author achid
 */
public class Node {
    String id,parentId,xpathAbsolute,xpathRelative,groupId,groupDescription;
    boolean repeatable;
    int intId;
    
     int getIntId(){
        return intId;
    }
    
    String getId(){
        return id;
    }
    
    String getParentId(){
        return parentId;
    }
      
    String getXpathAbsolute(){
        return xpathAbsolute;
    }
    
    String getXpathRelative(){
        return xpathRelative;
    }
    
    String getGroupId(){
        return groupId;
    }
    
    String getGroupDescription(){
        return groupDescription;
    }
    
    boolean isRepeatable(){
        return repeatable;
    }
   
    void setGroupId(String groupId){
        this.groupId=groupId;
    }
    
    void setGroupDescription(String groupDescription){
        this.groupDescription=groupDescription;
    }
                  
    Node(int intId,String id, String parentId, String xpathAbsolute, String xpathRelative,boolean repeatable){
        this.intId=intId;
        this.id=id;
        this.parentId=parentId;
        this.xpathAbsolute=xpathAbsolute;
        this.xpathRelative=xpathRelative;
        this.repeatable=repeatable;
                
    }
    
}
