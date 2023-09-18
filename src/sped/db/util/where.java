package sped.db.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class where {
	
	private int tam=0;
	private HashMap<String,String> hwhere = new HashMap<String,String>();
	
	public void clear(){
		hwhere = new HashMap<String,String>();
		tam=0;
	}
	
	public void put(String campo, String valor){
		hwhere.put(campo, valor);
		tam++;
	}
	
	public String getWhere(){
		String sand="", sk, sv, swhere=" ";
		
		Set<String> set=hwhere.keySet();
		Iterator <String>it = set.iterator();
		while(it.hasNext()){
			sk=it.next();
			sv=hwhere.get(sk);
			swhere+=sand+sk+"='"+sv+"' ";
			sand = "and ";
		}
		return(swhere);
	}
	
	public int getTam(){
		return(tam);
	}

}
