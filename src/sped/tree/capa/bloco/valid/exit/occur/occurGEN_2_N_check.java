package sped.tree.capa.bloco.valid.exit.occur;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.Conditional;
import sped.tree.capa.bloco.valid.base.IConditional;
import sped.tree.capa.bloco.valid.base.occurUtils;

public class occurGEN_2_N_check extends Conditional implements IConditional{
	
	public void init(beanSped pbeanSped, Record precord){
		super.init(pbeanSped, precord);
	}
	
	public int[] exec(){
		return(occurUtils._2_N);
	}
	
}
