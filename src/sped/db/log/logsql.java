package sped.db.log;

import java.util.ArrayList;
import java.util.Iterator;

public class logsql {

	private ArrayList<logsqlitem>list=new ArrayList<logsqlitem>();
	
	public void putSql(String sql, String tipo){
		logsqlitem ls = new logsqlitem(sql, tipo); 
		list.add(ls);
	}
	public void clear(){
		list=new ArrayList<logsqlitem>();
	}
	public Iterator<logsqlitem> getIterator(){
		Iterator<logsqlitem> it = list.iterator();
		return(it);
	}
	
}
