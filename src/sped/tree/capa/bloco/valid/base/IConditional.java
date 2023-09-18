package sped.tree.capa.bloco.valid.base;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;

public interface IConditional {
	
	public void init(beanSped pbeanSped, Record precord);
	
	public int[] exec();

}
