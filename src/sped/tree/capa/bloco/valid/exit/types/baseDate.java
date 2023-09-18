package sped.tree.capa.bloco.valid.exit.types;

import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import utils._;

public class baseDate {
	
	private Record record = null;
	
	public messageValid exec(Field f, Record record) {
		boolean b=false;
		int idia=0, imes=0, iano=0;
		String smsg="", sdia, smes, sano;
		messageValid mv = null;
		
		this.record = record;
		
		if(_.notEmpty(f.Valor)){
		  	//if(!f.Valor.matches("^[0-9]{8,8}$")){
			if(!f.Valor.matches("^[0-9]{2,2}/[0-9]{2,2}/[0-9]{4,4}$")){
		  		mv = new messageValid();
		  		mv.putMsg(validRules.ERROR, 10, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "baseDate", null);
		  		smsg = messageValid.getMsgFromDb(10, record.getBeanSped().getSql());
		  		smsg = smsg.replace("&1", f.Valor);
		  		mv.setMessage(smsg);
		  		return(mv);
		  	}
		  	
		  	sdia = f.Valor.substring(0,2);
		  	smes = f.Valor.substring(3, 5);
		  	sano = f.Valor.substring(6,10);
		  	
		  	try{
		  		idia = Integer.parseInt(sdia);
		  	}catch(Exception ex){
		  		
		  	}
		  	
		  	try{
		  		imes = Integer.parseInt(smes);
		  	}catch(Exception ex){
		  		
		  	}

		  	try{
		  		iano = Integer.parseInt(sano);
		  	}catch(Exception ex){
		
		  	}
		  	
		  	if(imes > 12){
		  		return(this.setMsg(f, "Mes inválido: "+smes));
		  	}
		  	
		  	if(idia > 31){
		  		return(this.setMsg(f, "Dia inválido: "+sdia));
		  	}
		  	
		  
		}
		
		return(mv);
		
	}

	private messageValid setMsg(Field f, String pmsg){
		String smsg="";
		messageValid mv = new messageValid();
		mv.putMsg(validRules.ERROR, 10, record.getIdCapa(), record.getId(), record.getReg(), f.Nome, "0", validRules.TYPEBASE, "baseDate", pmsg);
		smsg=messageValid.getMsgFromDb(10, record.getBeanSped().getSql()).replace("&1",f.Valor) + " ."+pmsg;
		mv.setMessage(smsg);
		return(mv);
	}
	
	public static int getNumberDataCalc(String sd) throws Exception{
		int dia, mes, ano, bi, res;
		int mesdias[] = { 31,59,90,120,151,181,212,243,273,304,334,365 };
		
		dia=Integer.parseInt(sd.substring(0, 2));
		mes=Integer.parseInt(sd.substring(3, 5));
		ano=Integer.parseInt(sd.substring(6, 10));
		bi=ano/4;
		if(mes<3) bi--;
		ano=ano*365;
		res=dia+mesdias[mes-1]+ano+bi;
		return(res);
		
	}
	
	public static int getNumberDataAll(String sd) throws Exception{
		int i=0;
		String s;
		s=sd.substring(6, 10) + sd.substring(3, 5) + sd.substring(0, 2);
		i=Integer.parseInt(s);
		return(i);
	}
	
	public static int getNumberDataDiaMes(String sd) throws Exception{
		int i=0;
		String s;
		s=sd.substring(0, 2) + sd.substring(3, 5);
		i=Integer.parseInt(s);
		return(i);
	}
	
	public static String getStringDataDiaMes(String sd) throws Exception{
		int i=0;
		String s;
		s=sd.substring(3, 5) + sd.substring(0, 2);
		return(s);
	}
	
	public static int getNumberDataDia(String sd)  throws Exception{
		int i=0;
		String s;
		s=sd.substring(0, 2);
		i=Integer.parseInt(s);
		return(i);
	}
	
	public static int getNumberDataMes(String sd)  throws Exception{
		int i=0;
		String s;
		s=sd.substring(3, 5);
		i=Integer.parseInt(s);
		return(i);
	}
	
	public static int getNumberDataAno(String sd)  throws Exception{
		int i=0;
		String s;
		s=sd.substring(6, 10) ;
		i=Integer.parseInt(s);
		return(i);
	}
}
