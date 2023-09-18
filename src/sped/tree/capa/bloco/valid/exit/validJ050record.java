package sped.tree.capa.bloco.valid.exit;

import java.sql.ResultSet;

import sped.bean.beanSped;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import sped.tree.capa.bloco.valid.exit.types.baseDate;

public class validJ050record extends AValidRuleRecord implements IValidRuleRecord{
	
	public void exec(String seq, String jname, Record record) {
		messageValid mv=null;
		//REGRA_REGISTRO_OBRIGATORIO_J051
		if(record.getValor("IND_CTA").equals("A") && (
			record.getValor("COD_NAT").equals("01")||
			record.getValor("COD_NAT").equals("02")||
			record.getValor("COD_NAT").equals("03")||
			record.getValor("COD_NAT").equals("04"))){
			String sql="select id from "+beanSped.getNomeProcesso()+"_mj051 where idcapa = '"+record.getIdCapa()+"' and id_pai='"+record.getIdPai()+"' limit 1";
			try{
				ResultSet rs = record.getBeanSped().getSql().select(sql);
				if(!rs.next()){
					mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_REGISTRO_OBRIGATORIO_J051", "IND_CTA", record.getValor("IND_CTA"));
					this.putMsg(mv);
				}
			}catch(Exception ex){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_REGISTRO_OBRIGATORIO_J051", "IND_CTA", record.getValor("IND_CTA"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_DT_ALT_DATA_MAIOR
		if(!rec("0000.DT_FIN").dge(record.getValor("DT_ALT"))){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DT_ALT_DATA_MAIOR", "DT_ALT", record.getValor("DT_ALT"));
			this.putMsg(mv);
		}
		
		//REGRA_MAIOR_QUE_UM
		int inivel;
		String s;
		s=record.getValor("NIVEL");
		try{
			inivel=Integer.parseInt(s);
			if(inivel<1){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_MAIOR_QUE_UM", "NIVEL", record.getValor("NIVEL"));
				this.putMsg(mv);
			}
		}catch(Exception ex){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_MAIOR_QUE_UM", "NIVEL", record.getValor("NIVEL"));
			this.putMsg(mv);
		}
		
		//REGRA_ANALITICA_NIVEL4
		/*
		s=record.getValor("NIVEL");
		try{
			i=Integer.parseInt(s);
			if(i<1){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_MAIOR_QUE_UM", "NIVEL", record.getValor("NIVEL"));
				this.putMsg(mv);
			}
		}catch(Exception ex){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_MAIOR_QUE_UM", "NIVEL", record.getValor("NIVEL"));
			this.putMsg(mv);
		}
		*/		
		
		//REGRA_CAMPO_OBRIGATORIO 
		s=record.getValor("NIVEL");
		try{
			inivel=Integer.parseInt(s);
			if(inivel>1){
				if(record.getValor("COD_CTA_SUP").trim().length()==0){
					mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CAMPO_OBRIGATORIO", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
					this.putMsg(mv);
				}else{
					//REGRA_CONTA_NO_PLANO_CONTAS
					String sql = "select id, ind_cta, nivel, cod_nat from "+beanSped.getNomeProcesso()+"_mj050 where idcapa='"+record.getIdCapa()+"' and id_pai='"+record.getIdPai()+"' and cod_cta ='"+record.getValor("COD_CTA_SUP")+"' limit 1";
					ResultSet rs = record.getBeanSped().getSql().select(sql);
					if(!rs.next()){
						mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CONTA_NO_PLANO_CONTAS", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
						this.putMsg(mv);
					}else{
						//REGRA_CONTA_NIVEL_SUPERIOR_NAO_SINTETICA
						if(!rs.getString("IND_CTA").equals("S")){
							mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CONTA_NIVEL_SUPERIOR_NAO_SINTETICA", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
							this.putMsg(mv);
						}
						//REGRA_NIVEL_DE_CONTA_NIVEL_SUPERIOR_INVALIDO
						String snivel = rs.getString("NIVEL");
						try{
							int inivelpai = Integer.parseInt(snivel);
							if(inivelpai>inivel){ //erro do nível
								mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NIVEL_DE_CONTA_NIVEL_SUPERIOR_INVALIDO", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
								this.putMsg(mv);
							}else{
								//REGRA_NATUREZA_CONTA
								if(inivel>2){
									String snat = rs.getString("COD_NAT");
									if(!snat.equals(record.getValor("COD_NAT"))){
										mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NATUREZA_CONTA", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
										this.putMsg(mv);
									}
								}
							}
						}catch(Exception ex){
							mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NIVEL_DE_CONTA_NIVEL_SUPERIOR_INVALIDO", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
							this.putMsg(mv);
						}
					}
				}
				//
			}else{
				//REGRA_CONTA_SUPERIOR_NAO_SE_APLICA
				if(!record.getValor("COD_CTA_SUP").isEmpty()){
					mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CONTA_SUPERIOR_NAO_SE_APLICA", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
					this.putMsg(mv);
				}
			}
		}catch(Exception ex){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CAMPO_OBRIGATORIO", "COD_CTA_SUP", record.getValor("COD_CTA_SUP"));
			this.putMsg(mv);
		}
	}
}
