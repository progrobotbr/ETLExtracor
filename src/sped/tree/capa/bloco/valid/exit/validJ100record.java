package sped.tree.capa.bloco.valid.exit;

import java.sql.ResultSet;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import sped.tree.capa.bloco.valid.exit.types.baseDate;

public class validJ100record extends AValidRuleRecord implements IValidRuleRecord{
	
	public void exec(String seq, String jname, Record record) {
		messageValid mv=null;
		
		//REGRA_DT_ALT_DATA_MAIOR
		if(!rec("0000.DT_FIN").dge(record.getValor("DT_ALT"))){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DT_ALT_DATA_MAIOR", "DT_ALT", record.getValor("DT_ALT"));
			this.putMsg(mv);
		}

	}
}
