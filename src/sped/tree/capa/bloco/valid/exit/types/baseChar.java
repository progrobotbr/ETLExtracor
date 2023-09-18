package sped.tree.capa.bloco.valid.exit.types;

import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import utils._;

public class baseChar {

	public messageValid exec(Field f, Record record) {
		char c[],c1;
		int i, itam;
		String smsg="";
		messageValid mv = null;
		
		if(_.notEmpty(f.Valor)){
			if(f.Tamanho!=null){
				itam = Integer.parseInt(f.Tamanho);
				if(f.Valor.length()>itam){
					mv = new messageValid();
					mv.putMsg(validRules.ERROR, 16, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "baseChar", null);
					smsg = messageValid.getMsgFromDb(16, record.getBeanSped().getSql());
					smsg = smsg.replace("&1",""+itam);
					smsg = smsg.replace("&2",""+f.Valor.length());
					mv.setMessage(smsg);
					return(mv);
				}
			}
			c = f.Valor.toCharArray();
			for(i=0;i<c.length;i++){
				c1=c[i];
				if(c1=='|' ||
					(c1>=0 && c1<=31) ){ //||
				   //(c1>=150 && c1<=255)){
					//int zz = c1; //somente para debug, pode retirar
					mv = new messageValid();
					mv.putMsg(validRules.ERROR, 6, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "baseChar", null);
					smsg = messageValid.getMsgFromDb(6, record.getBeanSped().getSql());
					smsg = smsg.replace("&1",f.Valor);
					mv.setMessage(smsg);
					return(mv);
				}
			}
		}
		return null;
	}
	
	/*
	( 0010.forma_trib = 8 or 0010.forma_trib = 9 ) and 0010.FORMA_TRIB_PER = branco

			( 
			  rec("0010").fld("forma_trib").equals("8") || 
			  rec("0010").fld("forma_trib").equals("9") 
			) && rec("0010").fld("forma_apur").isBranco

			hie(pai).fld("campo").equals
    */
}
