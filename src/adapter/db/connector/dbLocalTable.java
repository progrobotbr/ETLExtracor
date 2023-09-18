package adapter.db.connector;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import utils._;
import msg.msg;
import adapter.bean.beanAdapterDB;
import adapter.dblocal.dblCall;

public class dbLocalTable {

	private beanAdapterDB     beanAdp  = null;
	private ResultSet         tbfields = null;
	private ResultSet         tbdata   = null;
	private String sTbName = "";
	
	public void setBeanAdapterDB(beanAdapterDB pbeanAdp){
		beanAdp = pbeanAdp;
	}
	
	public void setDataField(String psTbName, ResultSet ptbfields, ResultSet ptbdata){
		sTbName  = psTbName;
		tbfields = ptbfields;
		tbdata  = ptbdata;
	}
	
	public int saveDataDb(){
		
		int i,t;
		String sd,sc,si,sif,sip,sVirg="";
		dblCall oDbl = new dblCall();
		ResultSetMetaData om;
		
		oDbl.setBeanAdapterDB(beanAdp);
		t = 0;
		sd = "drop table "+sTbName+"_"+beanAdp.getSetName();
		sc = "create table "+sTbName+"_"+beanAdp.getSetName() + " (";
		sif = "";
		sip = "";
		si = "";
		
		try{
		
			om  = tbdata.getMetaData();
			t = om.getColumnCount();
			for(i=0;i<t;i++){
				sc+=sVirg+om.getColumnName(i+1) + " varchar(100)"; // + "+om.getColumnTypeName(i+1) + ")";
				sif+=sVirg+om.getColumnName(i+1);
				sip+=sVirg+"?";
				sVirg=",";
			}
			sc+=")";
		}catch(Exception ex){
			i=18;
			beanAdp.addException(ex);
			return(i);
		}
		
		/*
		try{
		_.log(tbdata.getString("DOCNUM"));
		}catch(Exception ex){ _.log(ex.toString());}
		*/
		//if i...
		
		si = "insert into "+sTbName+"_"+beanAdp.getSetName()+" ("+sif+") values ("+sip+")";
		
		i = oDbl.createTableGen(sTbName,sd, sc);
		i = oDbl.createSqlInsertGen(sTbName, si);
		
		try{
			if(tbdata.next()){
				do{
					for(i=0;i<t;i++){
						oDbl.moveInsertGen(i+1, tbdata.getString(i+1), false);
						//_.log("a---"+tbdata.getString("DOCNUM"));
					}
					oDbl.moveInsertGen(0, null, true);
				}while(tbdata.next());
				i = oDbl.execInsert();
			}
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdp.addException(ex);
			i=19;
			return(i);
		}
			
		if(i > msg.SUCESSO){
			oDbl.rollback();
		}else{
			i = oDbl.commit();
		}
			
		oDbl.closeStmt();
			
		return(i);
		
	}
		
}
