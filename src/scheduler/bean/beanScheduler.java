package scheduler.bean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import cmd.console;
import server.server;
import utils._;

public class beanScheduler {
	
	public static final int      ISUCCESS = 0;
	public static final int      IERR     = 1;
	public static final int      ISSTOP   = 0;
	public static final int      ISRUNN   = 1;
	public static       int      state    = 0;
	private static beanScheduler INSTANCE = null;
	private PrintWriter          pwLog    = null;
	private String               pathset  = "";
	private String               pathlog  = "";
	private String               urldblocal  = "";
	private String               driverdblocal  = "";
	private server               serverTCP = null;
	private console              consoleCMD = null;
	private String               sopathseparator = "//"; 
	
	public void setServerTcp(server sv){
		serverTCP = sv;
	}
	
	public void setConsoleCmd(console cl){
		consoleCMD = cl;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		beanScheduler.state = state;
	}

	public String getDriverdblocal() {
		return driverdblocal;
	}

	public void setDriverdblocal(String driverdblocal) {
		this.driverdblocal = driverdblocal;
	}

	public String getUrldblocal() {
		return urldblocal;
	}

	public void setUrldblocal(String urldblocal) {
		this.urldblocal = urldblocal;
	}

	public String getPathlog() {
		return pathlog;
	}

	public void setPathlog(String pathlog) {
		this.pathlog = pathlog;
	}
	
	public void setOsPathSeparator(String sep) {
		this.sopathseparator=sep;
	}
	
	public String getOsPathSeparator() {
		return(this.sopathseparator);
	}

	public String getPathset() {
		return pathset;
	}

	public void setPathset(String pathset) {
		this.pathset = pathset;
	}

	public static beanScheduler factory(){
		if(INSTANCE==null){
			INSTANCE = new beanScheduler();
		}
		return(INSTANCE);
	}
	
	public void lLogFile(int itype, String smsg){
		try{
			if(pwLog==null){
				pwLog = new PrintWriter(new BufferedWriter(new FileWriter(this.pathlog, true)));
			}
			pwLog.print(""+itype+":"+_.getTime()+":"+smsg);
			pwLog.flush();
		}catch(Exception ex){
			_.log(ex.toString());
		}
	}
	
	public void lClose(){
		try{
			if(pwLog!=null){
				pwLog.close();
			}
		}catch(Exception ex){
			_.log(ex.toString());
		}
	}
	
	public static void flog(int itype, String smsg){
		beanScheduler bs = beanScheduler.factory();
		bs.lLogFile(itype, smsg);
	}
	
	public static void fclose(){
		beanScheduler bs = beanScheduler.factory();
		bs.lClose();
	}
	
	public void End(){
		this.lClose();
		if(consoleCMD!=null) { 
			this.consoleCMD.interrupt();
		}
		if(serverTCP!=null){
			this.serverTCP.interrupt();
		}
		System.exit(0);
	}
	/*
	public static void Init(){
		beanScheduler.factory();
		beanScheduler.state = beanScheduler.ISRUNN;
	}
	*/

}
