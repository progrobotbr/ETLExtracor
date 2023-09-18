package server;

import java.net.Socket;

import cmd.command;
import thread.ithread;
import utils._;

public class client extends Thread implements Runnable, ithread{
	
	private int iType=0;
	private Socket os;
	
	public client(Socket ps){
		os = ps;
	}
	
	public int getThreadType(){
		return(iType);
	}
	public void setThreadType(int i){
		iType=i;
	}
	
	public void run() {
		try{
			command cd = new command();
			cd.execCmds(os.getInputStream(), os.getOutputStream());
			os.close();
		}catch(Exception ex){
			try{
				os.close();
			}catch(Exception ex1){ _.println("client1: "+ex.toString());}
			_.println("client2: "+ex.toString());
		}
	}

}

/*
PrintWriter out = new PrintWriter(os.getOutputStream(), true);
BufferedReader in = new BufferedReader(new InputStreamReader(os.getInputStream()));

out.println("conectado");out.flush();
while(!s.equals("exit")){
	s=in.readLine();
	s1=s.split(",");
	s=s1[0];
	d=s1[1];
	job j = scheduler.createJob(s, d);
	scheduler.scale(j);
	
	out.println("agendado: "+s);out.flush();
	_.println(s);
}*/

