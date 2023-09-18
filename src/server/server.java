package server;

import java.net.ServerSocket;
import java.net.Socket;

import config.Config;
import thread.ithread;
import utils._;

public class server extends Thread implements Runnable, ithread{
	
	private int iType=0;
	
	public int getThreadType(){
		return(iType);
	}
	public void setThreadType(int i){
		iType=i;
	}
	
	public void run() {
		int i=0;
		Socket os;
		ServerSocket ss;
		client nc;
		try{
			i = Config.getInt(Config.server_port);
			ss = new ServerSocket(i);
			while(true){
				os = ss.accept();
				_.log("novo guest "+ os.getRemoteSocketAddress().toString());
				nc = new client(os);
				nc.start();
			}
		}catch(Exception ex){
			_.log("Erro no server:"+ex.toString());
		}
		
	}
	
	

}
