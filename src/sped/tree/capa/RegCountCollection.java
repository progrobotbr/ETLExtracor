package sped.tree.capa;

import java.util.ArrayList;
import java.util.HashMap;

public class RegCountCollection {
	
	ArrayList<RegCount> lregs = new ArrayList<RegCount>();
    HashMap<String,RegCount> hregs = new HashMap<String,RegCount>();
    
    public void put(String reg){
    	RegCount rc;
    	if(hregs.containsKey(reg)){
    		rc = hregs.get(reg);
    		rc.count++;
    	}else{
    		rc=new RegCount();
    		rc.reg=reg;
    		rc.count=1;
    		hregs.put(reg, rc);
    		lregs.add(rc);
    	}
    }
    
    public void setQtdRec(String reg, int qtd){
    	this.put(reg);
    	RegCount rc = hregs.get(reg);
    	rc.count=qtd;
    }
    public int getQtdRec(String reg){
    	int i;
    	RegCount rc;
    	if(hregs.containsKey(reg)){
    		rc = hregs.get(reg);
    		i=rc.count;
    	}else{
    		i=0;
    	}
    	return(i);
    }
    
    public ArrayList<RegCount> getList(){
    	return(lregs);
    }
}
