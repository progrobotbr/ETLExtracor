package scheduler;

import msg.msg;
import adapter.iadapter;
import adapter.db.adapterDB;
import adapter.sap.adapterSAP;
import adapter.util.adapterUtil;
import scheduler.bean.beanScheduler;
import utils._;

public class taskSet implements itask {
	
	public int execute(int iqtd, String sId, String sAllCmd, String sCmd){
		
		int i, t;
		String s, smsg;
		iadapter ad=null;
		
		//Pega nome do set
		s=adapterUtil.getSetName(sCmd);
		
		//Verifica o tipo do adapter
		t=adapterUtil.checkAdpterType(s);
		
		//Seleciona o tipo de adapter
		switch(t){
		case adapterUtil.ISDB:
			 ad = new adapterDB();
			break;
		case adapterUtil.ISSAP:
			ad = new adapterSAP();
			break;
		case adapterUtil.ISERR:
			_.log(msg.getMsg(15));
			return(15);
		}
		
		//Processamento
		_.log(msg.getMsg(9));
		
		smsg = "job_executed:" + sId + ":" + sAllCmd + "\r\n";
		
		i=ad.parse(s);
		
		smsg += ad.getMsg();
		if(i>msg.SUCESSO){
			t = beanScheduler.IERR;
		}else{
			t = beanScheduler.ISUCCESS;
		}
		beanScheduler.flog(t, smsg);
		
		_.log(msg.getMsg(i));
	   
		//Fim
		return(msg.SUCESSO);
	}
	
}
