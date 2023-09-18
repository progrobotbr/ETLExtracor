package sped.tree.capa.bloco.valid.exit;

import java.sql.ResultSet;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import sped.tree.capa.bloco.valid.exit.types.baseDate;

public class validJ051record extends AValidRuleRecord implements IValidRuleRecord{
	
	public void exec(String seq, String jname, Record record) {
		messageValid mv=null;
		
		//REGRA_CCUS_UNICO
		//REGRA_CCUS_NO_CENTRO_CUSTOS
		String sql;
		sql="select id from "+beanSped.getNomeProcesso()+"_mj100 where idcapa='"+record.getIdCapa()+"' and COD_CCUS='"+record.getValor("COD_CCUS")+"'";
		try{
			ResultSet rs = record.getBeanSped().getSql().select(sql);
			if(!rs.next()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CCUS_NO_CENTRO_CUSTOS", "COD_CCUS", record.getValor("COD_CCUS"));
				this.putMsg(mv);
			}
		}catch(Exception ex){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CCUS_NO_CENTRO_CUSTOS", "COD_CCUS", record.getValor("COD_CCUS"));
			this.putMsg(mv);
		}
		
		//REGRA_NAO_EXISTE_COD_CTA_REF //arquivo L100
		sql="select codigo, tipo from "+beanSped.getNomeProcesso()+"_RECURSOS_L100 where codigo='"+record.getValor("COD_CTA_REF")+"'";
		try{
			ResultSet rs = record.getBeanSped().getSql().select(sql);
			if(!rs.next()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_EXISTE_COD_CTA_REF", "COD_CTA_REF", record.getValor("COD_CTA_REF"));
				this.putMsg(mv);
			}else{
				//REGRA_COD_CTA_REF_SINTETICA
				if(!rs.getString("TIPO").equals("S")){
					mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_COD_CTA_REF_SINTETICA", "COD_CTA_REF", record.getValor("COD_CTA_REF"));
					this.putMsg(mv);
				}
			}
		}catch(Exception ex){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_EXISTE_COD_CTA_REF", "COD_CTA_REF", record.getValor("COD_CTA_REF"));
			this.putMsg(mv);
		}
	}
}
