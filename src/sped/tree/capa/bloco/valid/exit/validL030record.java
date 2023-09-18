package sped.tree.capa.bloco.valid.exit;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;

public class validL030record extends AValidRuleRecord implements IValidRuleRecord{

	public void exec(String seq, String jname, Record record) {
		
		//REGRA_PERIODO
		String sforma_apur = record.getValor("FORMA_APUR");
		if(sforma_apur.equals("A")){
		  String sper_apur = record.getValor("PER_APUR");
		  if(!sper_apur.startsWith("A")){
			  messageValid mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PERIODO", "PER_APUR", record.getValor("PER_APUR"));
			  this.putMsg(mv);
		  }
		}else{
			if(sforma_apur.equals("T")){
				String sper_apur = record.getValor("PER_APUR");
				  if(!sper_apur.startsWith("T")){
					  messageValid mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PERIODO", "PER_APUR", record.getValor("PER_APUR"));
					  this.putMsg(mv);
				  }
			}
		}
		
	}

}
