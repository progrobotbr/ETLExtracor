package adapter.bean;

import java.util.HashMap;
import java.util.Vector;

import msg.msg;
import adapter.db.connector.dbAdapterCall;
import adapter.db.connector.dbAdapterConnection;
import adapter.dblocal.dblConnection;

public class beanAdapterDB{
	
	private String sSetName="";
	private String sFileName="";
	private Vector<Exception> vmsg= new Vector<Exception>();
	private HashMap<String, dbAdapterCall> mem= new HashMap<String, dbAdapterCall>();
	private dblConnection          dbConn=null;
	private dbAdapterConnection dbAdpConn=null;
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
	public dbAdapterConnection getdbAdapterConnection(){
		return(dbAdpConn);
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
		dbAdpConn = new dbAdapterConnection();
		dbAdpConn.setBeanAdpDb(this);
		i = dbAdpConn.connect();
		if(i>msg.SUCESSO){
			dbConn.close();
			return(i);
		}
		
		return(i);
	}
	
	public void end(){
		dbConn.close();
		dbAdpConn.close();
	}
	
	//Set
	public void setSetName(String s){
		sSetName = s;
	}
	public String getSetName(){
		return(sSetName);
	}
	public void setFileName(String s){
		sFileName = s;
	}
	public String getFileName(){
		return(sFileName);
	}
	
	//Mem
	public void setMem(String sn, dbAdapterCall ob){
		mem.put(sn, ob);
	}
	
}
