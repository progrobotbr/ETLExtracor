package scheduler;

import java.io.InputStream;

import utils._;
import msg.msg;

public class taskCmd implements itask {
	
	public int execute(int iid, String sIdTime, String sAllCmd, String sCmd){
		int i;
		byte b[] = new byte[512];
		
		String s="";
		try{
			InputStream is = Runtime.getRuntime().exec(sCmd).getInputStream();
			i=is.read(b);if(i>0){ _.log(iid+"-"+sIdTime+"-"+_.getTime()); }
			while(i>0){
				s = new String(b,0,i);
				_.log(s);
				i=is.read(b);
			}
		}catch(Exception ex){
			_.log(ex.toString());
		}
		
		return(msg.SUCESSO);
	}

}
