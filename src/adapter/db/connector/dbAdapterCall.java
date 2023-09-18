package adapter.db.connector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import msg.msg;
import utils._;
import adapter.bean.beanAdapterDB;

public class dbAdapterCall {

	private beanAdapterDB     beanAdp   = null;
	private ResultSet         tbfields  = null;
	private ResultSet         tbdata    = null;
	private dbLocalTable      tbDataLoc = null;
	private String sSql="";
	private String sTbName="";
	private String sTbNamePai="";
	
  public int init(beanAdapterDB pbean){
	    beanAdp = pbean;
	    tbDataLoc = new dbLocalTable();
		tbDataLoc.setBeanAdapterDB(beanAdp);
		return(msg.SUCESSO);
  }
 	
  public void setSql(String pSql){
	  sSql = pSql; 
  }
  
  public int call(){
	  int i=0;
	  try{
		 Connection oconn = beanAdp.getdbAdapterConnection().getConnection();
		 Statement st =  oconn.createStatement();
		 DatabaseMetaData dbm = oconn.getMetaData();
		 tbfields = dbm.getColumns(null, null, sTbName, null);
		 tbdata = st.executeQuery(sSql);
		 tbDataLoc.setDataField(sTbName, tbfields, tbdata);
		 i = tbDataLoc.saveDataDb();
		 if(i!=msg.SUCESSO){
			 return(i);
		 }
	  }catch(Exception ex){
		  _.log(ex.toString());
		  beanAdp.addException(ex);
		  return(16);
	  }
	  return(msg.SUCESSO);
  }
  
  public void setTbName(String s){
	  s=s.toUpperCase();
	  sTbName=s;
  }
 
  public String getTbName(){
	  return(sTbName);
  }
  
  public void setTbNamePai(String s){
	  s=s.toUpperCase();
	  sTbNamePai=s;
  }
  
  public String getTbNomePai(){
	  return(sTbNamePai);
  }
  
  public dbLocalTable getTableData(){
	  return(tbDataLoc);
  }
  
}
