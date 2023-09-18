package sped.tree.capa.bloco.valid.exit.occur;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.Conditional;
import sped.tree.capa.bloco.valid.base.IConditional;
import sped.tree.capa.bloco.valid.base.occurUtils;
import utils._;

public class occurC001_check extends Conditional implements IConditional{
	
	public void init(beanSped pbeanSped, Record precord){
		super.init(pbeanSped, precord);
	}
	
	public int[] exec(){
		if
		(
		    //
			  pai("0000.COD_SCP").eq("R") && pai("0000.TIP_ECF").eq("0")
		 
		)	
		{
			_.log("achou");
			return(occurUtils._1_N);
			
		}else{
			_.log("nao achou");
			return(occurUtils._0_0);
			
		}
		
	}
	
}
