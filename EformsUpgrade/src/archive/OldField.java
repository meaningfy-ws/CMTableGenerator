/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

/**
 * class OldField
 * 
 * Old version of Field, used only for EformStats class
 *For each field we have (extracted from the field.json file) the:id parentNodeId name
 * btId xpathAbsolute xpathRelative repeatable
 * 
 * @author achid
 */
public class OldField {
    
    String id, parentNodeId, name, btId, xpathAbsolute, xpathRelative;
    boolean repeatable;
    int intId;
    
     int getIntId(){
        return intId;
    }
    
     String getId(){
        return id;
    }
    
    String getparentNodeId(){
        return parentNodeId;
    }
    
    String getName(){
        return name;
    }
    
    String getbtId(){
        return btId;
    }
      
    String getXpathAbsolute(){
        return xpathAbsolute;
    }
    
    String getXpathRelative(){
        return xpathRelative;
    }
       
    boolean isRepeatable(){
        return repeatable;
    }
       
    OldField(int intId,String id, String parentNodeId, String name, String btId, String xpathAbsolute, String xpathRelative, boolean repeatable){
        this.intId=intId;
        this.id=id;
        this.parentNodeId=parentNodeId;
        this.name=name;
        this.btId=btId;
        this.xpathAbsolute=xpathAbsolute;
        this.xpathRelative=xpathRelative;
        this.repeatable=repeatable;
                
    }
    
    
}
