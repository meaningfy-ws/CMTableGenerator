/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eformsupgrade;

/**
 *
 * @author achid
 */
public class Field {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    
    String id, parentNodeId, name, btId, xpathAbsolute, xpathRelative, type, legalType, codeList_id, attributeName, attributeOf, privacy; 
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
    String getType(){
        return type;
    }
    String getLegalType(){
        return legalType;
    }
    String getCodeList_id(){
        return codeList_id;
    }
       
    boolean isRepeatable(){
        return repeatable;
    }
    String getAttributeName(){
        return attributeName;
    }
    
    String getAttributeOf(){
        return attributeOf;
    }
  
    String getPrivacy(){
        return privacy;
    }

    
    Field(int intId,String id, String parentNodeId, String name, String btId, String xpathAbsolute, String xpathRelative, boolean repeatable, String type,String legalType,String codeList_id,String attributeName, String attributeOf,String privacy){
        this.intId=intId;
        this.id=id;
        this.parentNodeId=parentNodeId;
        this.name=name;
        this.btId=btId;
        this.xpathAbsolute=xpathAbsolute;
        this.xpathRelative=xpathRelative;
        this.type=type;
        this.legalType=legalType;
        this.codeList_id=codeList_id;
        this.repeatable=repeatable;
        this.attributeName=attributeName;
        this.attributeOf=attributeOf;
        this.privacy=privacy;
                
    }
    

    
}
