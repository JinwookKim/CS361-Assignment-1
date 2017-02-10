/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blp.simple.security;
import java.util.ArrayList;

/**
 *
 * @author jinwook
 */
public class ReferenceMonitor 
{
    protected ObjectManager objMan;
    protected ArrayList<SecurityInfo> subjSecurityInfoList;
    protected ArrayList<SecurityInfo> objSecurityInfoList;
    
    
    protected class SecurityInfo
    {
        protected SecurityLevel level;
        protected Object item;
        
        public SecurityInfo(Object item, SecurityLevel level)
        {
            this.level = level;
            this.item = item;
        }
    }
    
    private SecurityLevel getSubjSecurityLevel(Object item)
    {
        for(SecurityInfo si: subjSecurityInfoList)
            if(si.item.equals(item))
                return si.level;
        return null;
    }
    
    private SecurityLevel getObjSecurityLevel(Object item)
    {
        for(SecurityInfo si: objSecurityInfoList)
            if(si.item.equals(item))
                return si.level;
        return null;
    }
    
    public void createSubjectEntry(String name, SecurityLevel level)
    {
        subjSecurityInfoList.add(new SecurityInfo(name, level));
    }
    
    public void createObject(String name, SecurityLevel level)
    {
        objMan.createObject(name);
        objSecurityInfoList.add(new SecurityInfo(name, level));
    }
    
    public int execute(InstructionObject instruction)
    {
        SecurityLevel objSL = getObjSecurityLevel(instruction.obj);
        SecurityLevel subjSL = getSubjSecurityLevel(instruction.subj);
        Integer value;
        try
        {
            value = Integer.valueOf(instruction.val);
        }
        catch(NumberFormatException e)
        {
            value = null;
        }
        
        switch(instruction.command)
        {
            case READ:
                if(objSL == null || subjSL == null)
                {
                    // BAD INSTRUCTION - SYNTAX ERROR
                }
                else if (!subjSL.dominates(objSL))
                {
                    // BAD INSTRUCTION - PERMISSION DENIED
                }
                else
                {
                    return objMan.read(instruction.obj);
                }
            case WRITE:
                if(objSL == null || subjSL == null || value == null)
                {
                    // BAD INSTRUCTION - SYNTAX ERROR
                }
                else if (subjSL == SecurityLevel.LOW || !subjSL.dominates(objSL))
                {
                    // BAD INSTRUCTION - PERMISSION DENIED
                }
                else
                {
                    if(objMan.write(instruction.obj, value))
                        return 0;
                    return -1;
                }
            default:
                break;
        }
        
    }
}