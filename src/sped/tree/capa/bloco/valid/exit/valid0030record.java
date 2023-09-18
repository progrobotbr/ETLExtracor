package sped.tree.capa.bloco.valid.exit;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;

import java.util.regex.Pattern;

public class valid0030record  extends AValidRuleRecord implements IValidRuleRecord{

	public void exec(String seq, String jname, Record record) {
		
		//REGRA_VALIDA_EMAIL
		String email = record.getValor("EMAIL");
		if(email.trim().length()>0){
			if(!Pattern.matches(".*@.+([.].+)*", email)){
			  messageValid mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_VALIDA_EMAIL", "EMAIL", email);
			  this.putMsg(mv);
			}
		}
	}
	
}
