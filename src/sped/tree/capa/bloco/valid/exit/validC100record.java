package sped.tree.capa.bloco.valid.exit;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import sped.tree.capa.bloco.valid.base.Conditional;

public class validC100record extends AValidRuleRecord implements IValidRuleRecord {

	public void exec(String seq, String jname, Record record){
		boolean bok;
		String s;
		messageValid mv=null;
		bok = false;
		if(bok==false){
			mv = new messageValid();
			s = messageValid.getMsgFromDb(9, record.getBeanSped().getSql());
			mv.putMsg(validRules.ERROR, 9, record.getIdCapa(), record.getId(), record.getReg(), record.getReg(), seq, validRules.TYPERECORDCOD, "validC100record", s);
			this.putMsg(mv);
		}
		
	}

}
