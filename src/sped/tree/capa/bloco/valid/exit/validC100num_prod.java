package sped.tree.capa.bloco.valid.exit;

import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.IValidRuleField;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import utils._;

public class validC100num_prod implements IValidRuleField{

	public messageValid exec(String seq, String jname, Field f, Record record) {
		boolean bok;
		String s;
		messageValid mv=null;
		
		bok = false;
		if(_.notEmpty(f.Valor)){
			if(f.Valor.equals("01")){
				bok=true;
			}
		}
		if(bok==false){
			
			mv = new messageValid();
			s = messageValid.getMsgFromDb(8, record.getBeanSped().getSql());
			s+= " Valor entrado:  "+f.Nome+"-"+f.Valor;
			mv.putMsg(validRules.ERROR, 8, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, seq, "F", "validC100num_prod", s);
		}
		
		return(mv);

	}

}
