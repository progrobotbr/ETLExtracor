package http.bean;

import http.protocol.HttpSession;

import java.util.HashMap;

import config.Config;

public class beanHTTP {

	private static beanHTTP INSTANCE = null;
	
	//public  String PATH="C:\\Renato\\http";
	//public  int PORT=8080;
	public  String PATH=Config.getString(Config.http_folder);
	public  int PORT=Config.getInt(Config.http_port);
	public  HashMap<String,String> MIME = new HashMap<String,String>();
	public  String HTML404 = "<html><body><b>404-Arquivo nao encontrado</b></body></html>";
	public  String HTMLNCLASS = "<html><body><b>404-Arquivo compilado nao encontrado</b></body></html>";
	public  String DJSP = ".jsp";
	public  int H200 = 200;
	public  int H400 = 400;
	public  int H404 = 404;
	public  int H500 = 500;
	//public  String SJAVA = "C:\\Program Files\\Java\\jdk1.7.0_67";
	public  String SOPATHSEP = Config.getString(Config.so_path_separator);
	public  String SJAVA = Config.getString(Config.java_folder);;
	public  String DJAVA = ".java";
	public  HashMap<String,HttpSession> memsession = new HashMap<String,HttpSession>();
	
	public static beanHTTP factory(){
		if(beanHTTP.INSTANCE == null){
			beanHTTP.INSTANCE = new beanHTTP();
			beanHTTP.INSTANCE.loadMimes();
		}
		if(beanHTTP.INSTANCE.SOPATHSEP==null || beanHTTP.INSTANCE.SOPATHSEP.trim().length()==0){
			beanHTTP.INSTANCE.SOPATHSEP="\\";
		}
		return(beanHTTP.INSTANCE);
	}
	
	public HttpSession getSession(String pId){
		HttpSession ss = this.memsession.get(pId);
		if(ss==null){
			ss = new HttpSession();
			this.memsession.put(ss.SID, ss);
		}
		return(ss);
	}
    public void setSession(String pId, HttpSession ss){
		this.memsession.put(pId, ss);
	}
	
	private void loadMimes(){
		MIME.put(".jsp",  "text/html");
		MIME.put(".jpg",  "image/jpeg");
		MIME.put(".html", "text/html");
		MIME.put(".htm",  "text/htm");
		MIME.put(".js",   "text/javascript");
		MIME.put(".json", "text/json");
		MIME.put(".css",  "text/css");
		MIME.put(".gif",  "image/gif");
		MIME.put(".txt",  "text/plain");
	}
	
	
}
