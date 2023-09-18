package scheduler;

import sped.bean.beanSped;
import sped.msg.mensagem;
import sped.tree.capa.Capa;
import utils._;
import msg.msg;

public class taskSped implements itask {
	
	public int execute(int iid, String sIdTime, String sAllCmd, String sCmd){
		int iidcapa;
		Capa capa;
		beanSped bsped = new beanSped(); 
		
		String s="";
		try{
			
			bsped.init();
			bsped.getSql().beginTransaction();
			
			iidcapa = Integer.parseInt(sCmd.trim());
			capa = new Capa(bsped);
			mensagem msg = capa.validCapa(iidcapa);
			if(msg==null){
				s = msg.SUCESSO + "-" + "terminado com sucesso";
			}else{
				s = msg.tipo + "-"+msg.mensagem;
			}
			_.log(s);
			
			bsped.getSql().commit();
			
		}catch(Exception ex){
			_.log(ex.toString());
			try{
				bsped.getSql().rollback();
			}catch(Exception ex1){
				
			}
			return(msg.ERRO);
		}
		
		return(msg.SUCESSO);
	}

}
