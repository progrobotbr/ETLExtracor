package http;

///
/// Não funciona queryestring get parameter 
///
import http.bean.beanHTTP;
import http.filter.HttpCompileJSP;
import http.protocol.HttpHeader;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import utils._;

public class httpClient extends Thread implements Runnable{

	private Socket mos;
	//private PrintWriter out;
	private BufferedReader in;
	private InputStream ip;
	private OutputStream out;
	private HashMap<String,String> par = new HashMap<String,String>();
	private Vector<String> vhead = new Vector<String>();
	private Vector<String> vbody = new Vector<String>();
	private String mRec = "";
	private beanHTTP beanhttp = beanHTTP.factory();
	
	private HttpRequest  req  = null;
	private HttpResponse resp = null;
	private HttpSession  sess = null;
	private HttpHeader   head = null;
	
	public httpClient(Socket pos){
		mos = pos;
	}
	
	private void printb(byte b[]){
		try{
			out.write(b);
		}catch(Exception ex){
			_.print(ex.toString());
		}
	}
	private void printflush(){
		try{
			out.flush();
		}catch(Exception ex){
			_.print(ex.toString());
		}
	}
	
	
	public static int count=0;
	
	private String readLine(BufferedReader pin){
		String s;
		try{
			s = pin.readLine();
			return(s);
		}catch(Exception ex){
			return(null);
		}
	}
	
	private int readHeader(StringBuffer sb, BufferedReader pin){
		int tam=0;
		String s;
		
		do{ 
			s=this.readLine(pin);
			if(s==null){
				return(0);
			}
			if(s.equals("")){
				sb.append("\n");
				return(tam);
			}
			sb.append(s+"\n");
			if(s.startsWith("Content-Length")){
				String sc[] = s.split(":");
				String st = sc[1];
				tam = Integer.parseInt(st.trim());
			}
		}while(true);

	}
	
	private int readBody(BufferedReader in, StringBuffer sb, int ptam){
		byte b, f[];
		int idx=0, n;
		f = new byte[ptam];
		
		//ptam-=3;
		try{
			
			do{
				n = in.read();
				if(n==-1){
					return(0);
				}
				b = (byte)n;
				f[idx] = b;
				idx++;
				//_.print(""+n+"-");_.println(""+idx);
				if(idx==ptam){
					break;
				}
			}while(n!=-1);
			
			//n = ip.read(f);
		}catch(Exception ex){
			_.log(ex.toString());
			return(-1);
		}
		
		_.println("saiu");
		if(idx>0){
			String ss = new String(f);
			sb.append(ss);
		}
		return(1);
			
	}
	
	
	private int read(StringBuffer sb){
		
		int i;
		BufferedReader in = new BufferedReader(new InputStreamReader(ip));
		if(sb==null){
			return(4);
		}
		
		i = this.readHeader(sb, in);
		if(i>0){
			i=this.readBody(in, sb, i);
		}
		if(sb.toString().length()>0){
			i=0;
		}else{
			i=4;
		}
		return(i);
		
	}
	
	private int read_bk2(StringBuffer sb){
		int i;
		byte b[]=new byte[1024];
		
		if(sb==null){
			return(4);
		}
		
		if(httpClient.count==1){
			httpClient.count++;
		}
		httpClient.count++;
			
		do{
			try{
				i=ip.read(b);
				if(i<0){
					break;
				}
				sb.append(new String(b,0,i));
				//if(httpClient.count<2){
				  if(i<1024){ // &&  ip..available()==0){
					if(ip.available()==0){
				    break;
					}
				  }
				//}else{
				  //if(i<=0){ // &&  ip..available()==0){
					//break;
					//}
				//}
			}catch(Exception ex){
				i=4;
			}
		}while(i>0);
		if(sb.toString().length()>0){
			i=0;
		}else{
			i=4;
		}
		return(i);
		
	}
	
	private int read_bk(StringBuffer sb){
		int i;
		byte b[]=new byte[1024];
		
		if(sb==null){
			return(4);
		}
		
		BufferedInputStream in = new BufferedInputStream(ip);
		
		do{
			try{
				i=in.read(b);
				if(i<0){
					break;
				}
				sb.append(new String(b,0,i));
				//if(httpClient.count<2){
				  if(i<1024){ // &&  ip..available()==0){
					//if(ip.available()==0){
				    break;
					//}
				  }
				//}else{
				  //if(i<=0){ // &&  ip..available()==0){
					//break;
					//}
				//}
			}catch(Exception ex){
				i=4;
			}
		}while(i>0);
		if(sb.toString().length()>0){
			i=0;
		}else{
			i=4;
		}
		return(i);
		
	}
	
	private int init(){
		try{
			//out = new PrintWriter(mos.getOutputStream(), true);
			out = mos.getOutputStream();
			in = new BufferedReader(new InputStreamReader(mos.getInputStream()));
			ip = mos.getInputStream();
			vhead = new Vector<String>();
			vbody = new Vector<String>();
			mRec = "";
			return(0);
		}catch(Exception ex){
			_.println(ex.toString());
			return(4);
		}
	}
	
	private void close(){
		try{
			mos.getOutputStream().close();
			mos.close();
		}catch(Exception ex){
			_.print("err:"+ex.toString());
		}
	}
	
	private void doParameters(String s){
		boolean b=false;
		int i;
		String ss[];
		
		ss=s.split("\n");
		for(i=0;i<ss.length;i++){
			if(b==false){
				if(ss[i].startsWith("\n") || ss[i].startsWith("\r") || ss[i].isEmpty()){
					b=true;
				}else{
					vhead.add(ss[i]);
				}
			}else{
				vbody.add(ss[i]);
			}
		}
		mRec = vhead.get(0);
		mRec = mRec.split(" ")[1];
		mRec = mRec.replaceFirst("/", "");
		//mRec = mRec.replaceFirst(this.beanhttp.SOPATHSEP, "");
		i=mRec.indexOf('?');
		if(i>0){
			mRec = mRec.substring(0,i);
		}
	}
	
	private void setEnv(ByteArrayOutputStream ous, String allData){
		int i,t;
		String s,ss[],sss[],sget="", scont, sfile;;
		
		req = new HttpRequest();
		head = new HttpHeader();
		resp = new HttpResponse(ous,head);
		
		for(i=0;i<vhead.size();i++){
			s=vhead.get(i);
			if(i==0){
				if(s.startsWith("GET")){
					req.mHead.put("GET", s);
					sget=s;
				}else if(s.startsWith("POST")){
					req.mHead.put("POST", s);
					sget=s;
				}
			}else{
				ss=s.split(":",2);
				if(ss.length>1){
					req.mHead.put(ss[0], ss[1]);
				}else{
					req.mHead.put(ss[0], ss[0]);	
				}
			}
		}
		
		if(_.notEmpty(sget)){
			sget = sget.replace("GET", "");
			ss = sget.split(" ");
			sget = ss[1];
			i=sget.indexOf('?');
			sget = sget.substring(i+1,sget.length());
			/*
			sget = sget.replace("?","@");
			ss = sget.split("@",1);
			sget = ss[1];
			*/
			vbody.add(sget);
		}
		
		//Lógica para quando houve upload de arquivo por multipart/form-data
		//  falta pegar o mimetype
		scont = req.getHeaderValue("Content-Type");
		if(scont !=null && scont.startsWith(" multipart/form-data")){
			String sconts[] = scont.split("=");
			String sparts[] = allData.split("--"+sconts[1]);
			for(int z=1; z<sparts.length-1;z++){
				int index = sparts[z].indexOf("\r\n\r\n"); //2 enters
				String lado1 = new String(sparts[z].getBytes(),0,index);
				int indexn = lado1.indexOf("name");
				String lado11 = new String(sparts[z].getBytes(),indexn+5, sparts[z].length()-(indexn+5));
				indexn = lado11.indexOf("\"", 1);
				String chave = new String(lado11.getBytes(),1,indexn-1);
				String file = new String(sparts[z].getBytes(),index+4,sparts[z].length()-index-6);
				req.mPar.put(chave, file);
			}
		//Tratamento normal quando não é arquivo	
		}else{
			for(i=0;i<vbody.size();i++){
				s=vbody.get(i);
				ss=s.split("&");
				for(t=0;t<ss.length;t++){
					sss = ss[t].split("=");
					try{
						if(sss.length>1){
							sss[1] = java.net.URLDecoder.decode(sss[1], "UTF-8");
						}
					}catch(Exception ex){ _.print(ex.toString()); }
					if(sss.length>1){
						req.mPar.put(sss[0], sss[1]);
					}else{
						req.mPar.put(sss[0], "");
					}
				}
			}
		}
		/*
		 * sessionId
		 */
		String scookie = req.getHeaderValue("Cookie");
		if(scookie!=null){
			ss=scookie.split(";");
			for(i=0;i<ss.length;i++){
				sss=ss[i].split("=",2);
				s=sss[1].replaceAll("\r", "");
				s=s.replaceAll("\n", "");
				req.mCookie.put(sss[0].trim(), s);
			}
		}
		String sId = req.getCookie("sessionId");
		sess = beanhttp.getSession(sId);
	}
	
	private int getFile(ByteArrayOutputStream ous){
		byte[] b = new byte[1024];
		int read;
		String sfile;
		
		if(ous==null || mRec==null || mRec.length()==0){
			return(404);
		}
		try{
			//sfile = beanhttp.PATH+"\\"+mRec; 
			sfile = beanhttp.PATH+this.beanhttp.SOPATHSEP+mRec;
			_.print("Arquivo: "+sfile+"\n");
			FileInputStream ios = new FileInputStream(new File(sfile));
			read = 0;
			while((read = ios.read(b))!= -1){
				ous.write(b,0,read);
			}
			ios.close();
		}catch(Exception ex){
			String ss = beanhttp.HTML404;
			ous.write(ss.getBytes(),0,ss.length());
			return(beanhttp.H404);
		}
		return(200);
	}
	
	private String setHeader(int ihttp, ByteArrayOutputStream ous){
		int tam;
		String s="";
		StringBuffer sb = new StringBuffer();
				
		tam = ous.size();
		switch(ihttp){
		case 200:
			s="HTTP/1.1 200 OK\n";
			break;
		case 404:
			s="HTTP/1.1 404 File not found\n";
			break;
		case 500:
			s="HTTP/1.1 500 Err program\n";
			break;
		default: 
			s="HTTP/1.1 500 Err program\n";
			break;
		}
		this.setMime(sb);
		head.setHeaderInitial("Content-Type", sb.toString());
		for(int i=0;i<head.head.size();i++){
			s+=head.getHeader2(i) + "\n";
		}
		//s+= head.getHeader("Content-Type", sb.toString()) + "\n"+   //"Content-Type: " + sb.toString() + " charset=utf-8\n"
		s+= "Content-Length: "+tam+ "\n"+
		    /*"Cache-Control: max-age=31536000\n"+*/
		    /*"Expires: Mon, 25 Jun 2017 21:31:12 GMT\n"+*/
		    "Set-Cookie: sessionId="+sess.SID+"; path=/; HttpOnly\n\n";
		_.print(s);
		return(s);
	}
	
	private int setMime(StringBuffer sb){
		int i;
		String s,sm=null;
		i=mRec.lastIndexOf(".");
		if(i>0){
		  s=mRec.substring(i,mRec.length());
		  sm = beanhttp.MIME.get(s);
		  sb.append(sm);
		}
		if(sm!=null) return(0); else return(4);
		
	}
	
	private int filter(ByteArrayOutputStream ous){
		int i;
		if(mRec.toLowerCase().endsWith(beanhttp.DJSP)){
			i=this.execJsp(ous);
		}else{
			i=this.getFile(ous);
		}
		return(i);
	}
	
	private int execJsp(ByteArrayOutputStream ous){
		int i;
		String sname, nextJsp;
		HttpCompileJSP cc = new HttpCompileJSP();
		
		sname = mRec.replace(beanhttp.DJSP, "");
		
		i=cc.setJsp(sname, ous);
		if(i!=beanhttp.H200) return(i);
		
		i=cc.comp(sname, ous);
		if(i!=beanhttp.H200) return(i);
		
		Object o = cc.load(sname, ous);
		if(o==null) return(beanhttp.H500);
		
		resp.setNextJsp("");
		i=cc.execjsp(o,ous, sess, req, resp);
		if(_.notEmpty(resp.getNextJsp())){
			sname=resp.getNextJsp();
			sname = sname.replace(beanhttp.DJSP, "");
			//cc = new HttpCompileJSP();
			ous.reset();
			i=cc.setJsp(sname, ous);
			if(i!=beanhttp.H200){
				return(beanhttp.H500);
			}
			i=cc.comp(sname, ous);
			if(i!=beanhttp.H200) return(i);
			
			o = cc.load(sname, ous);
			if(o==null) return(beanhttp.H500);
			resp.setNextJsp("");
			i=cc.execjsp(o,ous, sess, req, resp);
		}
		
		return(i);
	}
	
	/* 
	 * Processamento principal
	 */
	public void run() {
		
		boolean keep = false;
		int i, ihttp;
		String s, skeep;
		StringBuffer sb = new StringBuffer();
		ByteArrayOutputStream ous = new ByteArrayOutputStream();
		
		_.print("request: inicio\n");
		
		//this.init();        //seta canais
		
		do{
			this.init();
			
			i = this.read(sb);  //lê socket
		
			if(i==0){
			
				_.print("request: lido: "+ sb.toString()+"\n");
			
				this.doParameters(sb.toString());     //pega dados chamada
				this.setEnv(ous,sb.toString());       //session, request, response
				ihttp=this.filter(ous);               //aplica filtro 
				s=this.setHeader(ihttp,ous);          //seta cabeçalho
				this.printb(s.getBytes());            //envia texto   cabeçalho
				this.printb(ous.toByteArray());       //envia binário corpo
				this.printflush();
				//_.print("request: trabalhado1: " + s+"\n");
				//_.print("request: trabalhado2: " + new String(ous.toByteArray()) +"\n");
		
				skeep = req.getHeaderValue("Connection");
				System.out.println(skeep);
				if(skeep.trim().equals("keep-alive")){
					keep=true;
					sb=new StringBuffer();
					ous = new ByteArrayOutputStream();
				}else{
					keep=false;
				}
			
			}else{
			
				_.print("request: erro ao ler socket\n");
				keep = false;
			}
		
		}while(keep==true);
		
		this.close(); //fecha socket
		
		System.out.println("saida1");
		
		_.print("request: fim\n");
		
	}
}
