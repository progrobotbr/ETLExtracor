package sped.tree.capa.bloco.valid;

import java.sql.ResultSet;

import sped.db.util.CSql;
import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.exit.types.baseChar;
import sped.tree.capa.bloco.valid.exit.types.baseDate;
import sped.tree.capa.bloco.valid.exit.types.baseNumeric;
import utils._;

public class validRules {
	
	public static final String VARCHAR = "VARCHAR";
	public static final String NUMERIC = "NUMERIC";
	public static final String DATE = "DATE";
	public static final String ERROR = "E";
	public static final String SUCCESS = "S";
	public static final String VALIDTYPE = "validType";
	public static final String TYPEFIELD = "FIELD";
	public static final String TYPERECORD = "RECORD";
	public static final String TYPEBASE = "BASE";
	public static final String TYPEFIELDCOD = "F";
	public static final String TYPERECORDCOD = "R";
	
	public static messageValid validAttribs(Field f, Record record){
		messageValid mv = null;
		int i=0;
		String smsg="",s="", sdomt, sdomf, sv;
		
		if(_.notEmpty(f.Obrigatorio)){
			if(!_.notEmpty(f.Valor)){
				mv = new messageValid();
				mv.putMsg(validRules.ERROR, 11, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "validAttribs", "");
				smsg = messageValid.getMsgFromDb(11, record.getBeanSped().getSql());
				mv.setMessage(smsg);
				return(mv);
			}
		}
		if(_.notEmpty(f.Obrig_Tam_Exato) && _.notEmpty(f.Valor)){
			try{
				i=Integer.parseInt(f.Tamanho);
			}catch(Exception ex){
				
			}
			if(_.notEmpty(f.Valor)){
				s=f.Valor;
			}
			if(s.length() != i){
				mv = new messageValid();
				mv.putMsg(validRules.ERROR, 12, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "validAttribs", "");
				smsg = messageValid.getMsgFromDb(12, record.getBeanSped().getSql());
				smsg = smsg.replace("&1", f.Valor).replace("&2",f.Tamanho);
				mv.setMessage(smsg);
				return(mv);
			}
		}
		
		if(_.notEmpty(f.Dom_Tabela) && _.notEmpty(f.Dom_Tabela_Chave)){
			sdomt=f.Dom_Tabela;
			sdomf=f.Dom_Tabela_Chave;
			s="select @v1 from @v2 where @v1='@v3'";
			if(_.notEmpty(f.Valor)){
				sv=f.Valor;
			}else{
				sv="";
			}
			s=s.replaceAll("@v1", sdomf).replace("@v2",sdomt).replace("@v3", sv);
			try{
				ResultSet rs = record.getBeanSped().getSql().select(s);
				if(!rs.next()){
					mv = new messageValid();
					mv.putMsg(validRules.ERROR, 14, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "validAttribs", "");
					smsg = messageValid.getMsgFromDb(14, record.getBeanSped().getSql());
					smsg = smsg.replace("&1", sv).replace("&2", sdomt).replace("&3", sdomf);
					mv.setMessage(smsg);
					return(mv);
				}
				rs.getStatement().close();
				rs.close();
			}catch(Exception ex){}
		}
			
		return(mv);
	}
	
	public static messageValid validType(Field f, Record record){
		
		String s="";
		messageValid mv = null;
		
		if(f.Tipo.equals(validRules.VARCHAR)){
			baseChar bc = new baseChar();
			mv = bc.exec(f, record);
			if(mv!=null){
				return(mv);
			}
			
		}else if(f.Tipo.equals(validRules.NUMERIC)){
			baseNumeric bn = new baseNumeric();
			mv = bn.exec(f, record);
			if(mv!=null){
				return(mv);
			}
		
		}else if(f.Tipo.equals(validRules.DATE)){
			baseDate bd = new baseDate();
			mv = bd.exec(f, record);
			if(mv!=null){
				return(mv);
			}
			
		}else{
			
			s=messageValid.getMsgFromDb(13, record.getBeanSped().getSql())+". Tipo do campo: "+f.Tipo;
			s=s.replace("&1", f.Valor).replace("&2", f.Tipo);
			mv = new messageValid();
			mv.putMsg(validRules.ERROR, 13, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "validType", s);
			return(mv);
			
		}
		
		mv=validRules.validAttribs(f,record);
		return(mv);
		
	}
	
	public static messageValid validRE(Field f, Record record){
		messageValid mv = null;
		String sv,smsg;
		if(_.notEmpty(f.Validacao_RE)){
			if(_.notEmpty(f.Valor)){
				sv=f.Valor;
			}else{
				sv="";
			}
			if(!sv.matches(f.Validacao_RE)){
				mv = new messageValid();
				mv.putMsg(validRules.ERROR, 15, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "validRE", sv);
				smsg = messageValid.getMsgFromDb(15, record.getBeanSped().getSql());
				smsg = smsg.replace("&1", sv).replace("&2", f.Validacao_RE);
				mv.setMessage(smsg);
				return(mv);
			}
		}
		return(mv);
	}
	
	public static messageValid validJVField(String seq, String jv, Field f, Record record){
		messageValid mv = null;
		Class<?> ocls;
		IValidRuleField orule;
		
		if(_.notEmpty(jv)){
			try{
				ocls = Class.forName(jv);
				orule= (IValidRuleField) ocls.newInstance();
				mv = orule.exec(seq, jv, f, record);
			}catch(Exception ex){
				mv = new messageValid();
				mv.putMsg(validRules.ERROR,2, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, seq, validRules.TYPEFIELD, jv, _.stackTrace(ex));
			}	
		}
		return(mv);
	}
	
	public static messageValid validJVRecord(String seq, String jv, Record record){
		messageValid mv = null;
		Class<?> ocls;
		IValidRuleRecord orule;
		
		if(_.notEmpty(jv)){
			try{
				ocls = Class.forName(jv);
				orule= (IValidRuleRecord) ocls.newInstance();
				orule.init(record);
				orule.exec(seq, jv, record);
				mv = orule.getMsg(); //pega as mensagens
			}catch(Exception ex){
				mv = new messageValid();
				mv.putMsg(validRules.ERROR,3, record.getIdCapa(), record.getId(), record.getReg(), record.getReg(), seq, validRules.TYPERECORD, jv, _.stackTrace(ex));
				
			}
		}
		return(mv);
	}

}
