package thread;

import java.util.Vector;

public class threadManager {
	
	private static threadManager INSTANCE = null;
	private Vector<ithread> mthread = new Vector<ithread>();
	
	public static threadManager factory(){
		if(INSTANCE == null){
			INSTANCE = new threadManager();
		}
		return(INSTANCE);
	}
	
	public void addThread(ithread tr, int itp){
		tr.setThreadType(itp);
		mthread.add(tr);
	}
	
	public static synchronized void add(ithread tr, int itp){
		threadManager tm = threadManager.factory();
		tm.addThread(tr, itp);
	}
	
}
