package sped.tree.capa.bloco.valid.exit;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import sped.tree.capa.bloco.valid.exit.types.baseDate;

public class valid0000record extends AValidRuleRecord implements IValidRuleRecord{
	
	public void exec(String seq, String jname, Record record) {
		int d1,d2;
		String sv;
		messageValid mv2;
		
		//REGRA_VALIDA_CNPJ
		//sv=rec("CNPJ").value();
		if(!validUtils.isCnpjValido(record.getValor("CNPJ"))){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 17, "REGRA_VALIDA_CNPJ", "CNPJ", record.getValor("CNPJ"));
			this.putMsg(mv2);
		}
		
		//REGRA_PAT_REMAN_CIS_OBRIGATORIO
		if(record.getValor("SIT_ESPECIAL").equals("6")){
			if(record.getValor("PAT_REMAN_CIS").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 19, "REGRA_PAT_REMAN_CIS_OBRIGATORIO", "PAT_REMAN_CIS", record.getValor("PAT_REMAN_CIS"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_NAO_PREENCHER_SIT_ESP_CISAO_PARCIAL
		if(!record.getValor("SIT_ESPECIAL").equals("6")){
			if(!record.getValor("PAT_REMAN_CIS").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 18, "REGRA_NAO_PREENCHER_SIT_ESP_CISAO_PARCIAL", "PAT_REMAN_CIS", record.getValor("PAT_REMAN_CIS"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_DT_SIT_OBRIGATORIO
		if(!record.getValor("SIT_ESPECIAL").isEmpty() && !record.getValor("SIT_ESPECIAL").equals("0")){
			if(record.getValor("DT_SIT_ESP").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 20, "REGRA_DT_SIT_OBRIGATORIO", "DT_SIT_ESP", record.getValor("DT_SIT_ESP"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_NAO_PREENCHER_SIT_ESP_NORMAL
		if(record.getValor("SIT_ESPECIAL").equals("0")){
			if(!record.getValor("DT_SIT_ESP").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_PREENCHER_SIT_ESP_NORMAL", "DT_SIT_ESP", record.getValor("DT_SIT_ESP"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_DATA_MINIMA
		try{
			d1 = baseDate.getNumberDataAll(record.getValor("DT_INI"));
			d2=20140101;
			if(d1<d2){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DATA_MINIMA", "DT_INI", record.getValor("DT_INI"));
				this.putMsg(mv2);
			}	
		}catch(Exception ex){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DATA_MINIMA", "DT_INI", record.getValor("DT_INI"));
			this.putMsg(mv2);
		}
		
		//REGRA_DT_INICIO_ESCRITURACAO
		try{
			sv = baseDate.getStringDataDiaMes(record.getValor("DT_INI"));
			if(!record.getValor("IND_SIT_INI_PER").equals("0")){
				if(sv.equals("0101")){
					mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DT_INICIO_ESCRITURACAO", "DT_INI", record.getValor("DT_INI"));
					this.putMsg(mv2);
				}
			}
		}catch(Exception ex){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DATA_MINIMA", "DT_INI", record.getValor("DT_INI"));
			this.putMsg(mv2);
		}
		
		//REGRA_DATA_INI_MAIOR
		try{
			d1=baseDate.getNumberDataAll(record.getValor("DT_INI"));
			d2=baseDate.getNumberDataAll(record.getValor("DT_FIN"));
			if(d1>=d2){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DATA_INI_MAIOR", "DT_INI", record.getValor("DT_INI"));
				this.putMsg(mv2);
			}	
		}catch(Exception ex){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DATA_INI_MAIOR", "DT_INI", record.getValor("DT_INI"));
			this.putMsg(mv2);
		}
		
		//REGRA_ANO_DIFERENTE
		try{
			d1=baseDate.getNumberDataAno(record.getValor("DT_INI"));
			d2=baseDate.getNumberDataAno(record.getValor("DT_FIN"));
			if(d1!=d2){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_ANO_DIFERENTE", "DT_INI", record.getValor("DT_INI"));
				this.putMsg(mv2);
			}	
		}catch(Exception ex){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_ANO_DIFERENTE", "DT_INI", record.getValor("DT_INI"));
			this.putMsg(mv2);
		}
		
		//REGRA_DT_FINAL_ESCRITURACAO
		try{
			d1=baseDate.getNumberDataDiaMes(record.getValor("DT_FIN"));
			if(record.getValor("SIT_ESPECIAL").equals("0") &&
			   d1 != 3112){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DT_FINAL_ESCRITURACAO", "DT_FIN", record.getValor("DT_FIN"));
				this.putMsg(mv2);
			}	
		}catch(Exception ex){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_DT_FINAL_ESCRITURACAO", "DT_FIN", record.getValor("DT_FIN"));
			this.putMsg(mv2);
		}
		
		//REGRA_EVENTO_ACONTECIMENTO
		try{
			if(record.getValor("SIT_ESPECIAL").equals("1") ||
					record.getValor("SIT_ESPECIAL").equals("2") ||
					record.getValor("SIT_ESPECIAL").equals("3") ||
					record.getValor("SIT_ESPECIAL").equals("4") ||
					record.getValor("SIT_ESPECIAL").equals("5") ||
					record.getValor("SIT_ESPECIAL").equals("6")){
				if(!record.getValor("DT_FIN").equals(record.getValor("DT_SIT_ESP"))){
					mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_EVENTO_ACONTECIMENTO", "DT_FIN", record.getValor("DT_FIN"));
					this.putMsg(mv2);
				}
			}
		
			if(record.getValor("SIT_ESPECIAL").equals("7") ||
					record.getValor("SIT_ESPECIAL").equals("8") ||
					record.getValor("SIT_ESPECIAL").equals("9")){
				d1=baseDate.getNumberDataCalc(record.getValor("DT_FIN"));
				d2=baseDate.getNumberDataCalc(record.getValor("DT_SIT_ESP"));
				d2--;
				if(d1!=d2){
					mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_EVENTO_ACONTECIMENTO", "DT_FIN", record.getValor("DT_FIN"));
					this.putMsg(mv2);
				}
			}
		}catch(Exception ex){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_EVENTO_ACONTECIMENTO", "DT_FIN", record.getValor("DT_FIN"));
			//mv2.concatMessage("Erro tipo Data");
			this.putMsg(mv2);
		}
		
		//REGRA_REC_ANTERIOR_OBRIGATORIO
		if(record.getValor("RETIFICADORA").equals("S") || record.getValor("RETIFICADORA").equals("F")) {
			if(record.getValor("NUM_REC").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_REC_ANTERIOR_OBRIGATORIO", "NUM_REC", record.getValor("NUM_REC"));
				this.putMsg(mv2);
			}
		}
		   
		//REGRA_NRO_REC_ANTERIOR_NAO_SE_APLICA
		if(record.getValor("RETIFICADORA").equals("N")){
			if(!record.getValor("NUM_REC").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NRO_REC_ANTERIOR_NAO_SE_APLICA", "NUM_REC", record.getValor("NUM_REC"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_SCP_OBRIGATORIO:
		if(record.getValor("TIP_ECF").equals("2")){
			if(record.getValor("COD_SCP").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_SCP_OBRIGATORIO", "COD_SCP", record.getValor("COD_SCP"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_SCP_NAO_PREENCHER
		if(record.getValor("TIP_ECF").equals("0") || record.getValor("TIP_ECF").equals("1")){
			if(!record.getValor("COD_SCP").isEmpty()){
				mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_SCP_NAO_PREENCHER", "COD_SCP", record.getValor("COD_SCP"));
				this.putMsg(mv2);
			}
		}
		
		//REGRA_VALIDA_TAM_SCP
		if(!record.getValor("COD_SCP").isEmpty() && record.getValor("COD_SCP").length()!=14){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_VALIDA_TAM_SCP", "COD_SCP", record.getValor("COD_SCP"));
			this.putMsg(mv2);
		}
		
		//REGRA_CNPJ_DIFERENTE_SCP
		if(record.getValor("COD_SCP").equals(record.getValor("CNPJ"))){
			mv2 = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_CNPJ_DIFERENTE_SCP", "COD_SCP", record.getValor("COD_SCP"));
			this.putMsg(mv2);
		}
		
	}
}
