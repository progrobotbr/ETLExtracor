package http.protocol;

import java.util.HashMap;
import java.util.UUID;

public class HttpSession { 
	
	private HashMap<String,Object> mSession=new HashMap<String,Object>();
	public String SID = null;
	
	public HttpSession(){
		UUID uuid = UUID.randomUUID();
		SID = uuid.toString();
	}
	
	public String getStringValue(String pkey){
		String s=null;
		if(mSession!=null){
			s= (String)mSession.get(pkey);
		}
		return(s);
	}
	
	public Object getObjectValue(String pkey){
		Object o=null;
		if(mSession!=null){
			o=mSession.get(pkey);
		}
		return(o);
	}
	
	public void setStringValue(String pkey, String s){
		mSession.put(pkey, s);
	}
	
	public void setObjectValue(String pkey, Object o){
		mSession.put(pkey, o);
	}

}
