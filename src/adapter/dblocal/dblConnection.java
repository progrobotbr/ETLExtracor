package adapter.dblocal;

import java.sql.Connection;
import java.sql.DriverManager;

import msg.msg;
import scheduler.bean.beanScheduler;
import utils._;

public class dblConnection {

	private Connection conn = null;

	public int connect() {

		conn = null;
		beanScheduler bs;
		try {
			
			bs = beanScheduler.factory();
			Class.forName(bs.getDriverdblocal());
			String dbURL = bs.getUrldblocal(); //"jdbc:sqlite:product.db";
			//Class.forName("org.apache.derby.jdbc.ClientDriver");
			//String dbURL = bs.getUrldblocal(); //"jdbc:sqlite:product.db";
			//conn = DriverManager.getConnection("jdbc:derby://localhost:1527/mydb1;user=rlima;password=rlima");
			conn = DriverManager.getConnection(dbURL);
			conn.setAutoCommit(false);
	
		} catch (Exception ex) {
			_.log(msg.getMsg(2));
			return(2);
		}

		return(0);

	}
	
	public Connection getConnection(){
		return(conn);
	}
	
	public void close(){
		try{
			conn.close();
		}catch(Exception ex){
			
		}
	}

}
