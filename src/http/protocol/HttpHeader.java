package http.protocol;

import java.util.HashMap;

public class HttpHeader {
	
	public HashMap<String, String> head = new HashMap<String, String>();
	
	public void setHeader(String key, String value){
		head.put(key.toUpperCase(), value);
	}
	
	public void setHeaderInitial(String key, String value){
		String skey = key.toUpperCase();
		String s = head.get(skey);
		if(s==null){
			head.put(key.toUpperCase(), value);
		}
	}
	
	public String getHeader(String key, String value){
		String skey = key.toUpperCase();
		String s = head.get(skey);
		if(s==null){
			s = skey + ": "+value;
			return(s);
		}else{
			s = skey + ": "+s;
			return(s);
		}
	}
	
	public int size(){
		return(head.size());
	}
	
	public String getHeader2(int i){
		String skey, svalue;
		skey = (String) head.keySet().toArray()[i];
		svalue = head.get(skey);
		skey = skey + ": " + svalue;
		return(skey);
	}
	
}
