package cmd;

import thread.ithread;

public class console extends Thread implements Runnable, ithread {
	
	private int iType=0;
	
	public int getThreadType(){
		return(iType);
	}
	public void setThreadType(int i){
		iType=i;
	}
	
	public void run() {
		command cd = new command();
		cd.execCmds(System.in, System.out);
	}

}


/*
_.print("CMD: ");
i=System.in.read(b);
if(i>0){
	s = new String(b,0,i-2);
    s1=s.split(",");
	s=s1[0];
	d=s1[1];
	
	job j = scheduler.createJob(s, d);
	scheduler.scale(j);
	
*/