package sped.tree.capa.bloco.valid.exit.types;

import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import utils._;

public class baseNumeric{

	public messageValid exec(Field f, Record record) {
		
		// ->  ^[0-9]{1,3}(([,]([0-9]{1,2})){0,1})$
		char c[],c1;
		int i;
		String decimal="",tamanho="", expression="", smsg="";;
		
		messageValid mv = new messageValid();
		mv.putMsg(validRules.ERROR, 7, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "baseNumeric", null);
		
		if(_.notEmpty(f.Valor)){
			c = f.Valor.toCharArray();
			decimal=f.Decimal;
			tamanho=f.Tamanho;
			expression = "^[0-9]";
			if(_.notEmpty(tamanho)){
				expression+="{1,"+tamanho+"}";
				smsg = "inteiro: "+tamanho;
			}else{
				expression+="+";
			}
			if(_.notEmpty(decimal)){
				expression+="(([,]([0-9]{1,"+decimal+"})){0,1})";
				smsg+= " decimais: "+ decimal;
			}
			expression+="$";
			
			if(f.Valor.matches(expression)){
				mv = null;
			}else{
				smsg = messageValid.getMsgFromDb(7, record.getBeanSped().getSql());
				smsg = smsg.replace("&1", f.Valor).replace("&2", tamanho).replace("&3", decimal);
				mv.setMessage(smsg);
			}
			
		}else{
			//Vamos deixar entrar com nada
			//smsg = messageValid.getMsgFromDb(7, record.getBeanSped().getSql());
			//smsg = smsg.replace("&1", f.Valor).replace("&2", f.Tamanho).replace("&3", f.Decimal);
			//mv.setMessage(smsg);
			return(null);
		}
	
		return(mv);
	}

}
