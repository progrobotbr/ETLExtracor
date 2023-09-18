package sped.db.log;

import utils._;

public class logsqlitem {

	public String datetime=null;
	public String sql=null;
			
    public logsqlitem(String sql, String tipo){
    	this.datetime=""+_.getTime();
    	this.sql=sql;
    }
}
