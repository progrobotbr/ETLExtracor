package adapter.dblocal;

import java.sql.*;

import msg.msg;
import adapter.bean.beanAdapterDB;
import adapter.bean.beanAdapterSAP;

import com.sap.mw.jco.JCO.Table;

import utils._;

public class dblCall {
	
	private beanAdapterDB  beanAdp = null;
	private beanAdapterSAP beanAdpSap = null;
	private Connection conn=null;
	private PreparedStatement ps=null;
	
	public void setBeanAdapterDB(beanAdapterDB pbeanAdp){
		beanAdp = pbeanAdp;
		conn = pbeanAdp.getDblConnection().getConnection();
	}
	
	public void setBeanAdapterSap(beanAdapterSAP pbeanAdp){
		beanAdpSap = pbeanAdp;
		conn = pbeanAdp.getDblConnection().getConnection();
	}
	
	public int createTable(String sTbName, tbStruct mtbs[]){
		int i;
		String s="", s1="", sV="", sTab;
		PreparedStatement ps=null;
		
		sTab = sTbName+"_"+beanAdpSap.getSetName();
		s= "create table "+sTab+ " (";
		for(i=0;i<mtbs.length;i++){
			s+= sV + mtbs[i].sf + " varchar(" + mtbs[i].tam + ")";
			sV = ",";
		}
		s+=")";
		s1 = "drop table " + sTab;
		try{
			ps = conn.prepareStatement(s1);
			ps.execute();
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdpSap.addException(ex);
		}
		
		try{
			ps = conn.prepareStatement(s);
			ps.execute();
			return(msg.SUCESSO);
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdpSap.addException(ex);
			return(24);
		}
	}
	
	public int createTableGen(String sTbName, String sDrop, String sCreate){
		//Connection conn = beanAdp.getDblConnection().getConnection();
		ps=null;
		try{
			ps = conn.prepareStatement(sDrop);
			ps.execute();
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdp.addException(ex);
		}
		try{
			ps = conn.prepareStatement(sCreate);
			ps.execute();
			return(msg.SUCESSO);
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdp.addException(ex);
			return(24);
		}
	}
	
	public int createSqlInsert(String sTbName, tbStruct mtbs[]){
		int i=0;
		String s="", s1="", sV="", sTab;
		ps=null;
		
		sTab = sTbName+"_"+beanAdpSap.getSetName();
		s="insert into " + sTab + " (";
		for(i=0;i<mtbs.length;i++){
			s += sV + mtbs[i].sf;
			s1+= sV + "?";
			sV=",";
		}
		s+=") values (" + s1 + ")";
		try{
			ps=conn.prepareStatement(s);
		}catch(Exception ex){
			beanAdpSap.addException(ex);
			return(1);
		}
		return(0);
	}
	
	public int createSqlInsertGen(String sTbName, String pSql){
		//Connection conn = beanAdp.getDblConnection().getConnection();
		ps=null;
		try{
			ps=conn.prepareStatement(pSql);
		}catch(Exception ex){
			beanAdp.addException(ex);
			_.log(ex.toString());
			return(21);
		}
		return(msg.SUCESSO);
	}
	
	
	public int moveInsert(Table td, tbStruct st[]){
		int i=0,t=0;
		String s="";
		tbStruct ot;
		td.firstRow();
		do{
			try{
				s=td.getString("WA");
				for(i=0;i<st.length;i++){
					ot=st[i];
					if(ot.pos+ot.tam > s.length()) {
						if(ot.pos < s.length()){
							t = s.length();
						}else{
							continue;
						}
					}else{
						t=ot.pos+ot.tam;
					}
					ps.setString(i+1,s.substring(ot.pos, t));
				}
				ps.addBatch();
			}catch(Exception ex){
				_.log(s);
				beanAdpSap.addException(ex);
				return(22); 
			}
		}while(td.nextRow()==true);
		return(msg.SUCESSO);
	}
	
	public int moveInsertGen(int ipos, String sVal, boolean bBatch){
		try{
			if(bBatch){
				ps.addBatch();
				return(msg.SUCESSO);
			}
			ps.setString(ipos,sVal);
			
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdp.addException(ex);
			return(22); 
		}
		return(msg.SUCESSO);
	}
	
	public int execInsert(){
		try{
			ps.executeBatch();
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdp.addException(ex);
			return(22); 
		}
		return(msg.SUCESSO);
	}
	
	public void closeStmt(){
		try{
			ps.close();
		}catch(Exception ex){
			beanAdp.addException(ex);
		}
	}
	
	public void closeConn(){
		//Connection conn = beanAdp.getDblConnection().getConnection();
		try{
			conn.close();
		}catch(Exception ex){
			beanAdp.addException(ex);
		}
	}
	
	public void closeAll(){
		this.closeStmt();
		this.closeConn();
	}
	
	public int commit(){
		//Connection conn = beanAdp.getDblConnection().getConnection();
		try{
			conn.commit();
			return(msg.SUCESSO);
		}catch(Exception ex){
			_.log(ex.toString());
			beanAdp.addException(ex);
			return(23);
		}
		
	}
	
	public void rollback(){
		//Connection conn = beanAdp.getDblConnection().getConnection();
		try{
			conn.rollback();
		}catch(Exception ex){}
	}
	
}
