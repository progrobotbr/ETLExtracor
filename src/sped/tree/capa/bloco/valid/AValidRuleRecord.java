package sped.tree.capa.bloco.valid;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.Conditional;

public abstract class AValidRuleRecord extends Conditional implements IValidRuleRecord {
	
	private messageValid msgs=null;
	
	public void init(Record precord){
		super.init(precord.getBeanSped(),precord);
	}
	
	public void putMsg(messageValid pmv){
		if(msgs==null){
			msgs=new messageValid();
		}
		msgs.msgs.add(pmv);
	}
	
	public messageValid getMsg(){
		return(msgs);
	}
}
