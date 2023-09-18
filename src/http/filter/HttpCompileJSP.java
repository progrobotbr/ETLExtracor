package http.filter;

import http.bean.beanHTTP;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import utils._;

public class HttpCompileJSP {
	
	private beanHTTP beanhttp = beanHTTP.factory();
	
	private static final String SCLASS = "import http.filter.ijsp;\n"+
	                                     "import http.protocol.HttpSession;\n"+
	                                     "import http.protocol.HttpRequest;\n"+
			                             "import http.protocol.HttpResponse;\n"+
										 "public class &:1 implements ijsp {\n"+ 
	                                     "  public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception{\n"+ 
			                             "  &:2\n"+
	                                     "  }\n"+
	                                     "}\n";
	
	public int execjsp(Object o, ByteArrayOutputStream ous, HttpSession sess, HttpRequest req, HttpResponse resp){
		
		//request req = new request();
		//response out = new response(ous);
		//session ss = beanhttp.getSession("001");
		
		try{
			ijsp jsp = (ijsp) o;
			jsp.post(sess, req, resp);
			return(beanhttp.H200);
			
		}catch(Exception ex){
			_.print(ex.toString());
			try{
				resp.print(ex.toString());
			}catch(Exception ex1){}
			return(beanhttp.H500);
		}
	}
	
	public Object load(String sFile, ByteArrayOutputStream ous){
		
		try{
			String sd, sf;
			sd="";
			sf=sFile;
			while(sf.indexOf("/")>0){
				sf=sf.substring(sf.indexOf("/")+1);
				sd="/"+sFile.substring(0,sFile.lastIndexOf("/"));
				
			}
			File root = new File(beanhttp.PATH+sd);
			URLClassLoader classLoader = URLClassLoader.newInstance( new URL[] { root.toURI().toURL() });
			Class<?> cls = Class.forName(sf, true, classLoader); 
			Object instance = cls.newInstance();
			return(instance);
				
		}catch(Exception ex){
			try{
				ous.write(beanhttp.HTMLNCLASS.getBytes());
			}catch(Exception ex1){
				_.print(ex1.toString());
			}
			_.print(ex.toString());
		}
		return(null);
		
	}
	
	public int comp(String sFile, ByteArrayOutputStream ous){
		
		//Se o ambiente for Unix, não compila, usa o que tiver
		if(beanhttp.SOPATHSEP.equals("/")){
			return(beanhttp.H200);
		}
		
		try{
			int i;
			String sp = beanhttp.PATH + "\\"+sFile+".java";
			System.setProperty("java.home", beanhttp.SJAVA);
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			i=compiler.run(null, null,  ous, sp);
			if(ous.toString().length()>0){
			//if(i>0){
				_.print(ous.toString());
				return(beanhttp.H500);
			}
			return(beanhttp.H200);
		}catch(Exception ex){
			_.print(ex.toString());
			return(beanhttp.H500);
		}
	}
	
	public int setJsp(String sname, ByteArrayOutputStream ous){
		int i;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 =  new StringBuffer();
		
		i = this.getFile(sname, sb);
		if(i!=beanhttp.H200){
			try{
			   ous.write(beanhttp.HTML404.getBytes());
			}catch(Exception ex){
				_.print(ex.toString());
			}
			return(beanhttp.H404);
		}
		
		i = this.setJava(sname, sb, sb2);
		if(i!=beanhttp.H200) return(beanhttp.H500);
		
		i = this.saveFile(sname, sb2);
		if(i!=beanhttp.H200) return(beanhttp.H500);
		
		_.print(sb2.toString());
		return(beanhttp.H200);
	}
	
	private int getFile(String sname, StringBuffer bf){
		String line;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(beanhttp.PATH+this.beanhttp.SOPATHSEP+sname+beanhttp.DJSP));
			while((line = br.readLine())!=null){
				bf.append(line+"\n");
			}
			br.close();
			return(beanhttp.H200);
		}catch(Exception ex){
			_.print(ex.toString());
			bf.append(beanhttp.HTML404);
			return(beanhttp.H500);
		}

	}
	
	private int setJava(String sname, StringBuffer sbin, StringBuffer sbout){
		int i;
		boolean b;
		String s, sn="";
		String ss[];
		String sprg;
		String senter="";
		StringBuffer sb = new StringBuffer();
		
		s=sbin.toString().replace("<%", "\n<%");
		s=s.replace("%>", "\n%>");
		ss=s.split("\n");
		b=true;
		for(i=0;i<ss.length;i++){
			if(ss[i].startsWith("<%")){
				b=false;
				ss[i] = ss[i].replace("<%", "");
			}else if(ss[i].startsWith("%>")){
				b=true;
				ss[i] = ss[i].replace("%>", "");
			}
			
			if(i<(ss.length-1)){
				senter = "";
				if(!ss[i+1].startsWith("%>") && !ss[i+1].startsWith("<%")){
					senter = "\\n";
				}
			}
			if(b==true){
				sb.append("response.print(\""+ss[i]+senter+"\");\n");
			}else{
				if(ss[i].length()>0){
					sb.append(ss[i]+"\n");
				}
			}
		}
		
		sprg = SCLASS;
		sn=sname;
		while(sn.indexOf("/")>0){
			sn=sn.substring(sn.indexOf("/")+1);
		}
		sprg = sprg.replace("&:1", sn);
		sprg = sprg.replace("&:2", sb.toString());
		sbout.append(sprg);
		return(beanhttp.H200);
		
	}
	
	public int saveFile(String sname, StringBuffer sb){
		try{
			String spath = beanhttp.PATH+this.beanhttp.SOPATHSEP+sname+beanhttp.DJAVA;
			PrintWriter fl = new PrintWriter(new BufferedWriter(new FileWriter(spath, false)));
			fl.print(sb.toString());
			fl.flush();
			fl.close();
			return(beanhttp.H200);
		}catch(Exception ex){
			_.print(ex.toString());
			return(beanhttp.H500);
		}
	}
	
	/*
	String source = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";

	// Save source in .java file.
	File root = new File("/java"); // On Windows running on C:\, this is C:\java.
	File sourceFile = new File(root, "test/Test.java");
	sourceFile.getParentFile().mkdirs();
	new FileWriter(sourceFile).append(source).close();
    */
	
	/*
	OutputStream err = new OutputStream(){
		private StringBuilder string = new StringBuilder();
		public void write(int b) throws IOException{
			this.string.append((char)b);
		}
		public String toString(){
			return(this.string.toString());
		}
	};*/
	
	
}
