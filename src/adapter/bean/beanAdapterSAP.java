package adapter.bean;

import java.util.HashMap;
import java.util.Vector;

import msg.msg;
import adapter.db.connector.dbAdapterCall;
import adapter.db.connector.dbAdapterConnection;
import adapter.dblocal.dblConnection;
import adapter.sap.connector.sapCall;
import adapter.sap.connector.sapConnection;

public class beanAdapterSAP{
	
	private String sSetName="";
	private String sFileName="";
	private Vector<Exception>        vmsg= new Vector<Exception>();
	private HashMap<String, sapCall>  mem= new HashMap<String, sapCall>();
	private dblConnection          dbConn=null;
	private sapConnection      sapAdpConn=null;
	
	private int    msgNr = 0;
	private int    msgTp = 0;
	private String msgTx = "";
	
	public void setMsg(int pmsgTp, int pmsgNr, String pmsg){
		msgNr = pmsgNr;
		msgTp = pmsgTp;
		msgTx = pmsg;
	}
	
	public int getMsgNr(){
		return(msgNr);
	}
	public int getMsgType(){
		return(msgTp);
	}
	public String getMsgTxt(){
		return(msgTx);
	}
	
	public dblConnection getDblConnection(){
		return(dbConn);
	}
	public sapConnection getSapAdapterConnection(){
		return(sapAdpConn);
	}
	//msg
	public void addException(Exception ex){
		vmsg.add(ex);
	}
	
	public Exception getException(int i){
		Exception ex=null;
		if(vmsg!=null && i<vmsg.size()){
			ex=vmsg.get(i);
		}
		return(ex);
	}
	
	//Connections
	public int initConnections(){
		int i;
		
		dbConn = new dblConnection();
		i = dbConn.connect();
		if(i>msg.SUCESSO){
			return(i);
		}
		sapAdpConn = new sapConnection();
		sapAdpConn.setBeanAdpSap(this);
		i = sapAdpConn.connect();
		if(i>msg.SUCESSO){
			dbConn.close();
			return(i);
		}
		
		return(i);
	}
	
	public void end(){
		dbConn.close();
		sapAdpConn.close();
	}
	
	//Set
	public void setSetName(String s){
		sSetName = s;
	}
	public String getSetName(){
		return(sSetName);
	}
	
	//Mem
	public void setMem(String sn, sapCall sc){
		mem.put(sn, sc);
	}
	
	public sapCall getMem(String s){
		return(mem.get(s));
	}
	
	
	
}
