package adapter.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import msg.msg;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import adapter.iadapter;
import scheduler.bean.beanScheduler;
import utils._;

public class adapterUtil {
	
	public static final int ISERR = 4;
	public static final int ISSAP = 1;
	public static final int ISDB  = 2;
	
	public static Node openXml(String pSetName){
		Node n=null;
		beanScheduler bs;
		try{
			bs=beanScheduler.factory();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(bs.getPathset()+"set"+pSetName+".xml"));
			n=doc.getFirstChild();
		}catch(Exception ex){ _.log(ex.toString()); }
		return(n);
	}
	
	public static int checkAdpterType(String sSetName){
		int i;
		Node n=null;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File("C:\\Renato\\programas\\adapter\\set"+sSetName+".xml"));
			n=doc.getFirstChild();
			if(n.getNodeName().equals(iadapter.CTBBASES)){
				i = ISSAP;
			}else if(n.getNodeName().equals(iadapter.CTBBASESSQL)){
				i = ISDB;
			}else{
				i = ISERR;
			}
		}catch(Exception ex){ 
			_.log(ex.toString()); 
			i = ISERR;
		}
		return(i);
	}
	
	public static int validNameSet(String s){
		String sre = "[0-9][0-9][0-9][0-9]";
		Pattern pt = Pattern.compile(sre);
		Matcher mt = pt.matcher(s);
		if(!mt.find()){
			return(11);
		}
		return(msg.SUCESSO);
	}
	
	public static String getSetName(String s){
		String sre = "[0-9][0-9][0-9][0-9]";
		Pattern pt = Pattern.compile(sre);
		Matcher mt = pt.matcher(s);
		if(!mt.find()){
			return("");
		}else{
			return(mt.group(0));
		}
	}
	
	public static String[] breakString(int tam, String s){
		int i,t,x,z;
		String sm[];
		
		if(s==null || s.trim().length()==0){
			return(null);
		}
		if(tam==0 || s.length()<tam){
			sm = new String[1];
			sm[0] = s;
			return(sm);
		}
		t = s.length() / tam;
		if((s.length() % tam)>0){
			t++;
		}
		sm = new String[t];
		x=0;z=tam;
		for(i=0;i<t;i++){
			sm[i]=s.substring(x, z);
			x+=tam;
			z+=tam;
			if(z>s.length()){
				z=s.length();
			}
		}
		return(sm);
	}
	
}
