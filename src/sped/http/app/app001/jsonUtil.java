package sped.http.app.app001;

import sped.db.util.CSql;
import utils._;

public class jsonUtil {

	private static String MSG = "{ 'rc':'@number', 'msg':'@mensagem' @data }";
	
	public static String doMessage(int number, String msg, String data){
		String smsg;
		
		String snr = CSql.itoc(number);
		smsg = jsonUtil.MSG.replace("@number", snr).replace("@mensagem", msg);
		if(_.notEmpty(data)){
			smsg = smsg.replace("@data", ", "+data);
		}else{
			smsg = smsg.replace("@data", "");
		}
		
		return(smsg);
	}
	
	public static String doMessage(int number, String msg){
		String smsg;
		
		String snr = CSql.itoc(number);
		smsg = jsonUtil.MSG.replace("@number", snr).replace("@mensagem", msg);
		smsg = smsg.replace("@data", "");
		
		return(smsg);
	}

}
