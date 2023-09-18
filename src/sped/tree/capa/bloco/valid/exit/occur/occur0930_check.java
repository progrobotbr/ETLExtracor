package sped.tree.capa.bloco.valid.exit.occur;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.Conditional;
import sped.tree.capa.bloco.valid.base.IConditional;
import sped.tree.capa.bloco.valid.base.occurUtils;
import utils._;

public class occur0930_check extends Conditional implements IConditional{
	
	public void init(beanSped pbeanSped, Record precord){
		super.init(pbeanSped, precord);
	}
	
	public int[] exec(){
		return(occurUtils._2_N);
		/*
		if
		(
		    
			  pai("0000.COD_SCP").eq("R")
		 
		)	
		{
			_.log("achou");
			return(occurUtils._1_N);
			
		}else{
			_.log("nao achou");
			return(occurUtils._0_0);
			
		}*/
		
	}
	
}
