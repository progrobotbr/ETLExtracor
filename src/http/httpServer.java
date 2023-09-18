package http;

import http.bean.beanHTTP;

import java.net.ServerSocket;
import java.net.Socket;

import utils._;
import config.Config;

public class httpServer extends Thread implements Runnable{

	public void run() {
		int i=0;
		Socket os;
		ServerSocket ss;
		httpClient nc;
		beanHTTP beanhttp = beanHTTP.factory();
		try{
			i = beanhttp.PORT;
			ss = new ServerSocket(i);
			//ss.setSoTimeout(5000);
			while(true){
				os = ss.accept();
				os.setSoTimeout(10000);
				System.out.println("passei aqui");
				os.setReceiveBufferSize(8192);
				_.log("http guest "+ os.getRemoteSocketAddress().toString());
				nc = new httpClient(os);
				nc.start();
			}
		}catch(Exception ex){
			_.log("Erro no server:"+ex.toString());
		}
	}
	
	public static void main(String args[]){
		
		httpServer hs = new httpServer();
		hs.start();
		_.print("Http iniciado");
		
	}
}

