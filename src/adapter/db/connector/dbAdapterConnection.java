package adapter.db.connector;

import java.sql.Connection;
import java.sql.DriverManager;

import config.Config;
import adapter.bean.beanAdapterDB;
import msg.msg;
import utils._;

public class dbAdapterConnection {
	
	private beanAdapterDB     beanAdp   = null;
	private Connection conn = null;

	public void setBeanAdpDb(beanAdapterDB pbeanAdpDB){
		beanAdp = pbeanAdpDB;
	}
	
	public int connect() {

		conn = null;
		try {

			Class.forName(Config.getString(Config.db_adapter_driver)); //"org.sqlite.JDBC");
			String dbURL = Config.getString(Config.db_adapter_url);   //"jdbc:sqlite:productb.db";
			conn = DriverManager.getConnection(dbURL);
			conn.setAutoCommit(false);
	
		} catch (Exception ex) {
			_.log(msg.getMsg(20));
			beanAdp.addException(ex);
			return (20);
		}

		return(0);

	}
	
	public Connection getConnection(){
		return(conn);
	}
	
	public void close(){
		try{
			conn.close();
		}catch(Exception ex){}
	}

}
