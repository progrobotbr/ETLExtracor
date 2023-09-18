package sped.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import config.Config;
import sped.bean.beanSped;
import sped.msg.mensagem;

public class sqlLocal {

	public int         rc=mensagem.ISUCESSO;
	public String      errmsg="";
	
	//private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DRIVER = Config.getString(Config.sped_db_local_driver);
	//private static final String DBASE  = "jdbc:sqlite:C:\\Renato\\programas\\rserver\\Rep\\db\\bsic.db";
	//private static final String DBASE  = "jdbc:sqlite:C:\\Users\\Renato\\workspace\\ETLExtractor\\product.db";
	private static final String DBASE  = Config.getString(Config.sped_db_local_url);
	private Connection conn=null;
	private boolean    bcommit=false;
	private int        rcsql=mensagem.ISUCESSO;
	private beanSped   mbeansped;
	private int irowsffected = 0;
	
	private ArrayList<ResultSet> arrrec = new ArrayList<ResultSet>();
	
	public void setBeanSped(beanSped bs){
		mbeansped=bs;
	}
	
	public void clearRcSql(){
		rcsql=mensagem.ISUCESSO;
	}
	private void setRcSql(int i){
		if(rcsql==mensagem.ISUCESSO){
			rcsql=i;
		}
	}
	public int getRcSql(){
		return(rcsql);
	}
	
	private int getConn() throws Exception{
		rc=mensagem.IERRO;
		if(conn==null){
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(DBASE);
		} else if(conn.isClosed()){
			conn=DriverManager.getConnection(DBASE);
		}
		rc=mensagem.ISUCESSO;
		return(rc);
	}
	
	public void setAutoCommit() throws Exception{
		this.getConn();
		conn.setAutoCommit(true);
	}
	
	public void setNotAutoCommit() throws Exception{
		conn.setAutoCommit(false);
	}
	
	public int commit() throws Exception{
		rc=mensagem.IERRO;
		this.closeAllRs();
		conn.commit();
		rc=mensagem.ISUCESSO;
		this.setRcSql(rc);
		return(rc);
	}
	
	public int rollback() throws Exception{
		rc=mensagem.IERRO;
		this.closeAllRs();
		conn.rollback();
		rc=mensagem.ISUCESSO;
		return(rc);
	}
	
	public int beginTransaction() throws Exception{
		rc=mensagem.IERRO;
		this.getConn();
		this.setNotAutoCommit();
		rc=mensagem.ISUCESSO;
		return(rc);
	}
	
	public ResultSet select(String query, boolean pCommit) throws Exception{
		bcommit = pCommit;
		return(select(query));
	}
	
	public ResultSet select(String query) throws Exception{
		ResultSet rs=null;
		Statement stat=null;
		
		rc = mensagem.IERRO;
		stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		rs=stat.executeQuery(query);
		this.putRsInArray(rs);
		mbeansped.getLogSql().putSql(query, mensagem.SUCESSO);
		rc = mensagem.ISUCESSO;
		
		return(rs);
	}

	public ResultSet select2(int pfechsize, int pmaxsize, String query) throws Exception{
		ResultSet rs=null;
		Statement stat;
		
		rc = mensagem.IERRO;
		
		stat = conn.createStatement();
		stat.setFetchSize(pfechsize);
		rs=stat.executeQuery(query);
		stat.setMaxRows(pmaxsize);
		this.putRsInArray(rs);
		mbeansped.getLogSql().putSql(query, mensagem.SUCESSO);
		
		rc = mensagem.ISUCESSO;
		
		return(rs);
	}
	public void dispose(){
		try{
			conn.close();
		}catch(Exception ex){
			
		}
	}
	
	public int change(String command, boolean pCommit) throws Exception{
		//bcommit = pCommit;
		return(this.change(command));
	}
	public int change(String command) throws Exception{
		Statement stat=null;
		
		rc=mensagem.IERRO;
		stat = conn.createStatement();
		irowsffected = stat.executeUpdate(command);
		mbeansped.getLogSql().putSql(command, mensagem.SUCESSO);
		rc=mensagem.ISUCESSO;
	    return(rc);
	}
	
	public void closeResultSet(ResultSet rs){
		try{
			rs.getStatement().close();
			rs.close();
		}catch(Exception ex){
			
		}
	}
	
	private void putRsInArray(ResultSet rs){
		arrrec.add(rs);
	}
	
	private void closeAllRs(){
		Iterator <ResultSet> it = arrrec.iterator();
		ResultSet rs;
		while(it.hasNext()){
			rs=it.next();
			this.closeResultSet(rs);
		}
		arrrec = new ArrayList<ResultSet>();
	}
	
	public int getRowsAffected(){
		return(irowsffected);
	}
}

