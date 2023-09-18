package mem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import cmd.command;
import msg.msg;
import scheduler.job;
import utils._;

public class jobs {
	
	private int iid=0;
	private Vector<job> ojobs = new Vector<job>();
	
	public void addJob(job j){
		j.iid=iid++;
		ojobs.add(j);
	}
	/*
	 * retorna se houve deleção ou se há job novo que não houve gravação no cron
	 */
	public int adjust(){
		job j;
		int t=0;
		for(int i=0;i<ojobs.size();i++){
			j = ojobs.get(i);
			if(j.bsaveCron == false){
				t=1;
			}
			if(j.iState==job.JOBST_REND || j.iState==job.JOBST_NULL){
				ojobs.remove(i);
				t=1;
			}
		}
		return(t);
	}
	
	public int getSize(){
		return(ojobs.size());
	}
	
	public int jet(){
		int t=0;
		long l;
		job j;
		
		l = _.getTime();
		
		for(int i=0;i<ojobs.size();i++){
			j = ojobs.get(i);
			if(j.iState==job.JOBST_SCLD){
				if(j.lDateTime<l){
					if(!this.isSetRunnig(j)){
						j.iState = job.JOBST_RUNN;
						j.start();
						t=1;
					}
				}
			}
		}
		return(t);
	}
	
	/*
	 * Evita dois set's rodarem ao mesmo tempo. Show.
	 */
	public boolean isSetRunnig(job pj){
		int i;
		job j;
		
		if(pj.getType() != job.JOBSET){
			return(false);
		}
		
		for(i=0;i<ojobs.size();i++){
			j = ojobs.get(i);
			if(j.getSetName().equals(pj.getSetName())){
				if(j.iState == job.JOBST_RUNN){
					return(true);
				}
			}
		}
		return(false);
	}
	
	public int deleteJob(int piid){
		job j;
		for(int i=0;i<ojobs.size();i++){
			j = ojobs.get(i);
			if(j.iid!=piid){
				continue;
			}
			if(j.iState==job.JOBST_RUNN){
				return(12);
			}
			j.iState=job.JOBST_NULL;
			//ojobs.remove(i);
			return(msg.SUCESSO);
		}
		return(13);
	}
	
	public String listJobs(){
		
		int i,t;
		String s="",s1="";
		job j;
		
		t=ojobs.size();
		if(t==0){
			s=msg.getMsg(10);
		}else
		  for(i=0;i<ojobs.size();i++){
			 s1="";
			 j = ojobs.get(i);
			 switch(j.iState){
			 case job.JOBST_NULL: s1 = job.SNULL;break;
			 case job.JOBST_SCLD: s1 = job.SSCLD; break;	
			 case job.JOBST_RUNN: s1 = job.SRUNN; break;
			 case job.JOBST_REND: s1 = job.SREND; break;
			 }
			 s+= "job_" + j.getIid() + "_" + j.getDateTime() + "_" + j.getDateTimeEnd() + "_"+s1 + "_" + j.getAllcmd() + "\n\r";
		  }
		
		return(s);
	}
	
	public int saveCron(int adjust, String pfile){
		job j;
	    
		/*
		 * não tem nada para fazer
		 */
		if(adjust == 0) {
			return(msg.SUCESSO);
		}
		
		try{
			PrintWriter cron = new PrintWriter(new BufferedWriter(new FileWriter(pfile, false)));
			
			for(int i=0;i<ojobs.size();i++){
				j = ojobs.get(i);
				if(j.bsaveCron == false || j.iState == job.JOBST_SCLD){
					cron.println(j.getAllcmd());
					cron.flush();
				}
			}
			cron.println("exit"); // se não colocar exit, dá um erro em command
			cron.flush();
			cron.close();
			return(msg.SUCESSO);
		}catch(Exception ex){
			_.log(ex.toString());
			return(27);
		}
	}
	
	public void setCronOk(){
		int i;
		job j;
		for(i=0;i<ojobs.size();i++){
			j = ojobs.get(i);
			j.bsaveCron=true;
		}
		
	}

}
