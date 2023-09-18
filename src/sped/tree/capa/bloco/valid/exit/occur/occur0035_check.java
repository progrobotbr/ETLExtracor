package sped.tree.capa.bloco.valid.exit.occur;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.Conditional;
import sped.tree.capa.bloco.valid.base.IConditional;
import sped.tree.capa.bloco.valid.base.occurUtils;

public class occur0035_check extends Conditional implements IConditional{
	
	public void init(beanSped pbeanSped, Record precord){
		super.init(pbeanSped, precord);
	}
	
	public int[] exec(){
		if
		(
		    
			  rec("0000.TIP_ECF").eq("1")
		 
		)	
		{
			return(occurUtils._1_N);
			
		}else{
			
			return(occurUtils._0_0);
			
		}
		
	}
	
}
