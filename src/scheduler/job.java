package scheduler;

import adapter.util.adapterUtil;
import utils._;

public class job extends Thread implements Runnable {

	private int jobType;
	private itask otask;
	private String scmd="";
	private String allcmd="";
    private String setName ="";
	public static final int JOBCMD = 1;
	public static final int JOBSET = 2;
	public static final int JOBSPED = 3;
	public static final int JOBST_NULL = 0;
	public static final int JOBST_SCLD = 1;
	public static final int JOBST_RUNN = 2;
	public static final int JOBST_REND = 3;
	public static final String SSCLD = "escalonado";
	public static final String SRUNN = "rodando";
	public static final String SREND = "finalizado";
	public static final String SNULL = "null";
	
	public long lDateTime=0;
	public long lDateTimeEnd=0;
	public int iState=JOBST_SCLD;
	public String sIdTime="";
	public int iid;
	public boolean bsaveCron=false;
	
	public void run() {
		
		/*** inicial ***/
		iState=JOBST_RUNN;
		
		/*** Seleção do tipo job ***/
		switch(jobType){
			case job.JOBCMD: {
				otask = new taskCmd();
				break;
			}
			case job.JOBSET: {
				otask = new taskSet();
				break;
			}
			case job.JOBSPED: {
				otask = new taskSped();
				break;
			}
		}
		
		/*** executa ***/
		try{
			otask.execute(iid, sIdTime,allcmd, scmd);
		}catch(Exception ex){
			_.log(ex.toString());
		}
		
		/*** fim ***/
		lDateTimeEnd = _.getTime();
		iState=JOBST_REND;
		
	}
	
	public boolean isRunning(){
		if(iState==JOBST_RUNN)
			return(true);
		else
			return(false);
	}
	
	public void setDateTime(long l){
		lDateTime = l;
	}
	
	public String getDateTime(){
		return(""+lDateTime);
	}
	
	public void setDateTimeEnd(long l){
		lDateTime = l;
	}
	
	public String getDateTimeEnd(){
		return(""+lDateTimeEnd);
	}
	
	public void setCmd(String s){
		scmd=s;
	}
	
	public String getCmd(){
		return(scmd);
	}
	
	public void setType(int i){
		jobType = i;
	}
	
	public int getType(){
		return(jobType);
	}
	
	public int getIid(){
		return(iid);
	}

	public String getAllcmd() {
		return allcmd;
	}

	public void setAllcmd(String allcmd) {
		this.allcmd = allcmd;
	}
	

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}
	


	
}
