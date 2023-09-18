package scheduler;

import http.httpServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import config.Config;
import adapter.sap.connector.sapConnection;
import adapter.util.adapterUtil;
import cmd.command;
import cmd.console;
import mem.jobs;
import msg.msg;
import scheduler.bean.beanScheduler;
import server.server;
import thread.ithread;
import utils._;

public class scheduler{
	
	private int iSleep=1000;
	private static jobs js = new jobs();
	private static jobs jsList = new jobs();
	private console cmd;
	private server osv;
	private httpServer http;
	private static String CRON = "cron.txt";
	private static String CFG = "config.ini";
	
	public void run() {
		this.execute();
	}
	
	public void execute(){
		int i,t;
		beanScheduler bs;
		
		/*init config*/
		i=Config.initConfig(CFG);
		if(i!=msg.SUCESSO){
			_.log(msg.getMsg(i));
			return;
		}
		
		/*init bean */
		bs = beanScheduler.factory();
		bs.setState(beanScheduler.ISRUNN);
		bs.setPathlog(Config.getString(Config.path_log));             //("C:\\Renato\\programas\\adapter\\log.txt");
		bs.setPathset(Config.getString(Config.path_set));             //("C:\\Renato\\programas\\adapter\\");
		bs.setUrldblocal(Config.getString(Config.local_db_url));       //("jdbc:sqlite:product.db");
		bs.setDriverdblocal(Config.getString(Config.local_db_driver)); //("org.sqlite.JDBC");
		bs.setOsPathSeparator(Config.getString(Config.so_path_separator));
		
		/* console - habilita digitar comandos para scheduler*/
		cmd = new console();
		cmd.start();
		bs.setConsoleCmd(cmd);
		
		/*rede*/
		osv = new server();
		osv.start();
		bs.setServerTcp(osv);
		
		/*http*/
		http = new httpServer();
		http.start();
		//_.print("Http iniciado\n");
		
		/*pool sap*/
		i=sapConnection.mountPool();
		if(i!=msg.SUCESSO){
			_.log(msg.getMsg(i));
			return;
		}
		
		/*load cron */
		this.loadCron(CRON);
		
		/*loop principal*/
		while(beanScheduler.state==beanScheduler.ISRUNN){
			_.sleep(iSleep);
			t=js.adjust();
			t+=js.jet();
			js.saveCron(t, CRON);
			
		}
		/*finaliza bean*/
		bs.End();
	}
	
	public static job createJob(int pJobType, String allcmd, String cmd, String s){
		long l;
		String sSetName="";
		job j;
		
		if(cmd==null || cmd.length()==0){
			return(null);
		}
		
		if(s==null || s.length()==0){
			return(null);
		}
		
		if(pJobType!=job.JOBCMD && pJobType!=job.JOBSET && pJobType != job.JOBSPED){
			return(null);
		}
		
		if(pJobType==job.JOBSET){
			sSetName = adapterUtil.getSetName(cmd);
		}
		
		j=new job();
				
		l = _.fmtTime(s);
		j.setAllcmd(allcmd);
		j.setType(pJobType);
		j.setDateTime(l);	
		j.setCmd(cmd);
		j.sIdTime = ""+_.getTime();
		j.setSetName(sSetName);
		
		return(j);
		
	}
	
	public synchronized static int scale(job j){
		
		if(j==null){
			return(6);
		}
		
		js.addJob(j);
		jsList.addJob(j);
		
		return(msg.SUCESSO);
		
	}
	
	public static String getListJob(){
		return(jsList.listJobs());
	}
	
	public static int deleteJob(int piid){
		return(js.deleteJob(piid));
	}
	
	public void loadCron(String pfile){
		try{
			InputStream ins;
			OutputStream ous;
			command cmd = new command();
			ous = System.out;
			ins =  new FileInputStream(new File(pfile));
			cmd.execCmds(ins, ous);
			ins.close();
			js.setCronOk();
		}catch(Exception ex){
			_.log(ex.toString());
		}
	}
	
}
