package sped.tree.capa.bloco.valid;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;

public interface IValidRuleRecord{
	
	//messageValid == null representa sucesso
	public void init(Record precord);
	public void exec(String seq, String jname, Record record);
	public void putMsg(messageValid pmv);
	public messageValid getMsg();

}
