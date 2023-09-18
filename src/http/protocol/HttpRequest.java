package http.protocol;

import java.util.HashMap;
import java.util.Iterator;

public class HttpRequest {

	public HashMap<String,String> mHead = new HashMap<String,String>();
	public HashMap<String,String> mPar  = new HashMap<String,String>();
	public HashMap<String,String> mCookie  = new HashMap<String,String>();
	
	public String getHeaderValue(String pkey){
		String s=null;
		if(mHead!=null){
			s = mHead.get(pkey);
		}
		return(s);
	}
	public String getParameter(String pkey){
		String s=null;
		if(mPar!=null){
			s = mPar.get(pkey);
		}
		return(s);
	}
	
	public void setParameter(String pKey, String pValue){
		mPar.put(pKey, pValue);
	}
	
	public String getCookie(String pkey){
		String s=null;
		if(mCookie!=null){
			s = mCookie.get(pkey);
		}
		return(s);
	}
	
	public Iterator<String> getKeysList(){
		return(mPar.keySet().iterator());
	}
}
