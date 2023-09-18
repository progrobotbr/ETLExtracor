package sped.tree.capa.bloco.valid.exit.occur;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.Conditional;
import sped.tree.capa.bloco.valid.base.IConditional;
import sped.tree.capa.bloco.valid.base.occurUtils;

public class occurM200_check extends Conditional implements IConditional{
	
	public void init(beanSped pbeanSped, Record precord){
		super.init(pbeanSped, precord);
	}
	
	public int[] exec(){
		if
		(
		    
			  rec("0000.FORMA_TRIB").eq("1") ||
		      rec("0000.FORMA_TRIB").eq("2") ||
		      rec("0000.FORMA_TRIB").eq("3") ||
		      rec("0000.FORMA_TRIB").eq("4") ||
		    ( rec("0000.FORMA_TRIB").eq("5") && rec("0000.TIP_ESC_PRE").eq("C") ) ||
		    ( rec("0000.FORMA_TRIB").eq("6") && rec("0000.TIP_ESC_PRE").eq("C") ) ||
		    ( rec("0000.FORMA_TRIB").eq("7") && rec("0000.TIP_ESC_PRE").eq("C") ) ||
		    ( rec("0000.FORMA_TRIB").eq("8") && rec("0000.TIP_ESC_PRE").eq("C") ) 
		 
		)	
		{
			return(occurUtils._1_13);
			
		}else{
			
			return(occurUtils._0_0);
			
		}
		
	}
	
}
