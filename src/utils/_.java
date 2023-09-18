package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import scheduler.bean.beanScheduler;

public class _ {
	
	public static final int FALSE = 0;
	public static final int TRUE = 1;
	
	public static void print(String s){
		System.out.print(s);
	}
	
	public static void println(String s){
		System.out.println(s);
	}
	
	public static int sleep(int l){
		try{
			Thread.sleep(l);
			return(TRUE);
		}catch(Exception e){ 
			return(FALSE);
		}
		
	}
	
	public static long getTime(){
		long l;
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");   
		formato.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		l = Long.parseLong(formato.format(new Date( System.currentTimeMillis()))); 
		return(l);
	}
	
	public static String getTimeString(){
		String s;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");   
		formato.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		s = formato.format(new Date( System.currentTimeMillis())); 
		return(s);
	}
	
	public static String getTimeString2(){
		String s = _.getTimeString();
		s = s.replaceAll("/", "").replaceAll(":", "").replaceAll(" ","_");
		return(s);
	}
	
	public static long fmtTime(String s){
		long l;
		Date dt=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");   
		formato.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		try{
		  dt = formato.parse(s);
		}catch(Exception ex){ return(7); }
		
		formato = new SimpleDateFormat("yyyyMMddHHmmss"); 
		formato.setTimeZone(TimeZone.getTimeZone("Brazil/East"));
		l = Long.parseLong(formato.format(dt)); 
		return(l);
	}
	
	public static void log(String s){
		_.println(s);
		//beanScheduler.flog(beanScheduler.ISUCCESS, s);
	}
	
	public static String stackTrace(Exception ex){
		String s="";
		StackTraceElement st[] = ex.getStackTrace();
		for(int i=0;i<st.length;i++){
			s+=st[i].getFileName()+"."+st[i].getMethodName()+":"+st[i].getLineNumber()+";";
		}
		return(s);
		
		
	}
	
	public static boolean notEmpty(String s){
		if(s!=null && s.trim().length()>0){
			return true;
		}
		return false;
	}
	
	public static boolean notNull(String s){
		if(s!=null){
			return true;
		}
		return false;
	}
		
	public static int saveFile(String pNome, String pDados){
		try{
			File file = new File(pNome);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(pDados);
			output.close();
			return(0);
		}catch(Exception ex){
			return(4);
		}
	}
	
	public static String zeroRight(int qtdZero, String pValor){
		String s;
		StringBuffer sb=new StringBuffer();
		for(int i=0; i<qtdZero; i++){
			sb.append("0");
		}
		sb.append(pValor);
		s = sb.toString();
		s = s.substring(s.length()-qtdZero, s.length());
		return(s);
	}


}
