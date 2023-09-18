package sped.tree.capa.bloco.valid.exit;

import java.util.ArrayList;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.AValidRuleRecord;
import sped.tree.capa.bloco.valid.IValidRuleRecord;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;

public class valid0010record extends AValidRuleRecord implements IValidRuleRecord {

	public Record mrec=null;
	public String mcampo="";
	
	public void exec(String seq, String jname, Record record) {
		
		String s1, s2, s3;
		messageValid mv=null;
		
		mrec = record;
		
		//REGRA_FORMA_APUR_VALIDA
		s1=record.getValor("FORMA_APUR");
		s2=record.getValor("FORMA_TRIB");
		s3=record.getValor("OPT_REFIS");
		if(s1.equals("A")){
			if( !(s3.equals("S") && (s2.equals("1")||s2.equals("2")||s2.equals("3")||s2.equals("4")))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_APUR_VALIDA", "FORMA_APUR", s1);
				this.putMsg(mv);
			}
		}
		
		if(s1.equals("T")){
			if( !(s2.equals("1")||s2.equals("2")||s2.equals("3")||s2.equals("4")||s2.equals("5")||s2.equals("6")||s2.equals("7")) ){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_APUR_VALIDA", "FORMA_APUR", s1);
				this.putMsg(mv);
			}
		}
		
		//REGRA_NAO_PREENCHER_IMUNE
		if( s2.equals("8") || s2.equals("9") ){
			if(!s1.isEmpty()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_PREENCHER_IMUNE", "FORMA_APUR", s1);
				this.putMsg(mv);
			}
		}else{
			if(s1.isEmpty()){ //deve preencher
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_APUR_VALIDA", "FORMA_APUR", s1);
				this.putMsg(mv);
			}
		}
		
		//REGRA_COD_QUALIF_PJ
		if( s2.equals("3")||s2.equals("4")||s2.equals("5")||s2.equals("7") ){
			if(!record.getValor("COD_QUALIF_PJ").equals("01")){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_COD_QUALIF_PJ", "COD_QUALIF_PJ", record.getValor("COD_QUALIF_PJ"));
				this.putMsg(mv);
				
			}
		}
		
		//REGRA_NAO_PREENCHER_IMUNE
		if( s2.equals("8")||s2.equals("9") ){
			if(!record.getValor("COD_QUALIF_PJ").isEmpty()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_PREENCHER_IMUNE", "COD_QUALIF_PJ", record.getValor("COD_QUALIF_PJ"));
				this.putMsg(mv);
				
			}
		}
		
		//REGRA_COD_QUALIF_PJ_OBRIGATORIO
		if( s2.equals("1")|| s2.equals("2")||s2.equals("3")||s2.equals("4")||s2.equals("5")||s2.equals("6")||s2.equals("7")  ){
			if(record.getValor("COD_QUALIF_PJ").isEmpty()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_COD_QUALIF_PJ_OBRIGATORIO", "COD_QUALIF_PJ", record.getValor("COD_QUALIF_PJ"));
				this.putMsg(mv);
				
			}
		}
		
		//REGRA_NAO_PREENCHER_IMUNE
		if( s2.equals("8")||s2.equals("9") ){
			if(!this.is_empty_forma_atrip_per()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_PREENCHER_IMUNE", "FORMA_TRIB_PER", record.getValor("FORMA_TRIB_PER"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_TRIBUT_INVALIDA
		
		ArrayList<String> ar=new ArrayList<String>();
		if(s2.equals("1")){   
			ar=new ArrayList<String>();ar.add("0");ar.add("R");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA1",this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("2")){
			ar=new ArrayList<String>();ar.add("0");ar.add("R");ar.add("A");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA2", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("3") && record.getValor("OPT_REFIS").equals("N")){
			ar=new ArrayList<String>();ar.add("0");ar.add("P");ar.add("R");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA3", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("3") && record.getValor("OPT_REFIS").equals("S") && record.getValor("FORMA_APUR").equals("A")){
			ar=new ArrayList<String>();ar.add("0");ar.add("E");ar.add("P");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA4", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("3") && record.getValor("OPT_REFIS").equals("S") && record.getValor("FORMA_APUR").equals("T")){
			ar=new ArrayList<String>();ar.add("0");ar.add("R");ar.add("P");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA5", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("4") && record.getValor("OPT_REFIS").equals("N") ){
			ar=new ArrayList<String>();ar.add("0");ar.add("A");ar.add("P");ar.add("R");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA6", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("4") && record.getValor("OPT_REFIS").equals("S")  && record.getValor("FORMA_APUR").equals("A")){
			ar=new ArrayList<String>();ar.add("0");ar.add("A");ar.add("E");ar.add("P");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA7", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("4") && record.getValor("OPT_REFIS").equals("S")  && record.getValor("FORMA_APUR").equals("T")){
			ar=new ArrayList<String>();ar.add("0");ar.add("A");ar.add("E");ar.add("P");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA8", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("5")){
			ar=new ArrayList<String>();ar.add("0");ar.add("P");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA9", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("6")){
			ar=new ArrayList<String>();ar.add("0");ar.add("A");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA10", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("7")){
			ar=new ArrayList<String>();ar.add("0");ar.add("A");ar.add("P");
			if( ! (this.or_forma_atrip_per(ar))){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA11", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("8")){
			if( !this.is_empty_forma_atrip_per()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA12", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		if(s2.equals("9")){
			if( !this.is_empty_forma_atrip_per() ){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_INVALIDA13", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		
		//REGRA_TRIBUT_PER_R_E
		ar=new ArrayList<String>();ar.add("R");ar.add("E");
		if(this.simul_forma_atrip_per(ar)){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_TRIBUT_PER_R_E", this.mcampo, record.getValor(this.mcampo));
			this.putMsg(mv);
		}
		
		//REGRA_PRESUMIDO_PRIMEIRO
		ar=new ArrayList<String>();ar.add("R");ar.add("E");
		if(!this.simul_prim_forma_atrip_per(ar, "P")){
			mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PRESUMIDO_PRIMEIRO", this.mcampo, record.getValor(this.mcampo));
			this.putMsg(mv);
		}
		
		//REGRA_FORMA_TRIB_EXISTENTE
		ar=new ArrayList<String>();ar.add("R");ar.add("A");
		if(s2.equals("2")){
			if(!this.simul_forma_atrip_per2(ar)){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_TRIB_EXISTENTE", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		
		//REGRA_FORMA_TRIB_EXISTENTE
		ar=new ArrayList<String>();ar.add("P");ar.add("R");
		if(s2.equals("3")){
			if(!this.simul_forma_atrip_per2(ar)){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_TRIB_EXISTENTE", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}	

		//REGRA_FORMA_TRIB_EXISTENTE
		ar=new ArrayList<String>();ar.add("P");ar.add("R");ar.add("A");
		if(s2.equals("4")){
			if(!this.simul_forma_atrip_per2(ar)){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_TRIB_EXISTENTE", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		
		//REGRA_FORMA_TRIB_EXISTENTE
		ar=new ArrayList<String>();ar.add("P");ar.add("R");
		if(s2.equals("7")){
			if(!this.simul_forma_atrip_per2(ar)){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_TRIB_EXISTENTE", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		
		//REGRA_FORMA_TRIB_PER_OBRIGATORIO
		if(!(s2.equals("8") || s2.equals("9"))){
			if(is_empty_forma_atrip_per()){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_FORMA_TRIB_PER_OBRIGATORIO", this.mcampo, record.getValor(this.mcampo));
				this.putMsg(mv);
			}
		}
		
		//REGRA_PREENCHER_TIP_ESC - TIP_ESC_PRE
		if(s2.equals("3") || s2.equals("4") || s2.equals("5") || s2.equals("7") || s2.equals("8") || s2.equals("9")){
			if(record.getValor("TIP_ESC_PRE").trim().length()==0){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PREENCHER_TIP_ESC", "TIP_ESC_PRE", record.getValor("TIP_ESC_PRE"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_PREENCHER_IMUNE
		if(s2.equals("8") || s2.equals("9")){
			if(record.getValor("TIP_ENT ").trim().length()==0){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PREENCHER_IMUNE", "TIP_ENT", record.getValor("TIP_ENT"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_PREENCHER_IMUNE
		if(s2.equals("8") || s2.equals("9")){
			if(record.getValor("FORMA_APUR_I").trim().length()==0){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PREENCHER_IMUNE", "FORMA_APUR_I", record.getValor("FORMA_APUR_I"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_APUR_IGUAL
		if(record.getValor("APUR_CSLL ").trim().length()>0 && record.getValor("APUR_CSLL ").equals("D")){
			if(!record.getValor("FORMA_APUR_I ").equals("APUR_CSLL")){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_APUR_IGUAL", "FORMA_APUR_I", record.getValor("FORMA_APUR_I"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_PREENCHER_IMUNE
		if(s2.equals("8") || s2.equals("9")){
			if(record.getValor("APUR_CSLL").trim().length()==0){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PREENCHER_IMUNE", "APUR_CSLL", record.getValor("APUR_CSLL"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_MES_BAL_RED_OBRIGATORIO
		if(record.getValor("FORMA_APUR").equals("A")){
			if(this.is_empty_bal_red(record)){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_PREENCHER_IMUNE", "APUR_CSLL", record.getValor("APUR_CSLL"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_NAO_PREENCHER_TRIMESTRAL
		if(record.getValor("FORMA_APUR").equals("T")){
			if(!this.is_empty_bal_red(record)){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_NAO_PREENCHER_TRIMESTRAL", "MES_BAL_RED_X1", record.getValor("MES_BAL_RED_X1"));
				this.putMsg(mv);
			}
		}
		
		//REGRA_MES_BAL_RED_INVALIDO
		for(int i=1; i<=4; i++){
			if( valid_mes_bal_red_invalid(record, i) == false ){
				mv = messageValid.getMessageField(record, seq, validRules.ERROR, 99, "REGRA_MES_BAL_RED_INVALIDO", "MES_BAL_RED_X1", record.getValor("MES_BAL_RED_X1"));
				this.putMsg(mv);
				break;
			}
		}
		
	}
	
	public boolean valid_mes_bal_red_invalid(Record rs, int itrimestre){
		int t;
		String smes = "MES_BAL_RED_X";
		String stri = "FORMA_TRIB_PER_X";
		String svper = rs.getValor(stri+itrimestre);
		String svbal;
		
		t=((itrimestre -1 ) * 3);
		/*
		if(!svper.equals("R") && !svper.equals("E")){
			for(int i=1;i<=4;i++){
				svbal =  rs.getValor(smes+(t+i));
				if(!svbal.equals("0")){
					return(false);
				}
			}
		}
		*/
		if(svper.equals("R") || svper.equals("E")){
			for(int i=1;i<=3;i++){
				svbal =  rs.getValor(smes+(t+i));
				if(! (svbal.equals("0") || svbal.equals("E") || svbal.equals("B")) ){
					return(false);
				}
			}
		}
		
		return(true);
	}
	
	public boolean is_empty_bal_red(Record rs){
		String smes = "MES_BAL_RED_X", s;
		
		for(int i=1;i<=12;i++){
			s=rs.getValor(smes+i);
			if(s.trim().length()!=0){
				return(false);
			}
		}
		return(true);
	}
	
	public boolean or_forma_atrip_per(ArrayList<String> opts){
		boolean b=false;
		int i;
		String sname1, sname="FORMA_TRIB_PER_X", sv;
		this.mcampo="";
		i=0;
		for(i=1;i<=4;i++){
			b=false;
			sname1=sname+i;
			sv=mrec.getValor(sname1);
			for(String s: opts){
				if(sv.equals(s)){
					b=true;
					break;
				}
			}
			if(b==false){
				this.mcampo=sname1;
				return(false);
			}
		}
		return(b);
	}
	
	public boolean is_empty_forma_atrip_per(){
		int i;
		String sname1, sname="FORMA_TRIB_PER_X", sv;
		this.mcampo="";
		for(i=1;i<=4;i++){
			sname1=sname+i;
			sv=mrec.getValor(sname1);
			this.mcampo=sname1;
			if(sv.isEmpty() || sv.equals("0")){
				return(true);
			}
		}
		return(false);
	}
	
	public boolean simul_forma_atrip_per(ArrayList<String> opts){
		int count=0, len=0;
		String sname1, sname="FORMA_TRIB_PER_X", sv, sr="";
		this.mcampo="FORMA_TRIB_PER_X1";
		for(int i=1;i<=4;i++){
			sname1=sname+i;
			sv=mrec.getValor(sname1);
			sr+=sv;
		}
		
		for(String s: opts){
			len++;
			if(sr.indexOf(s)>-1){
				count++;
			}
		}
		
		if(count>=len){
			return(true);
		}else if(count==1){
			return(false);
		}else{
			return(true);
		}
		
	}
	
	public boolean simul_forma_atrip_per2(ArrayList<String> opts){
		int count=0, len=0;
		String sname1, sname="FORMA_TRIB_PER_X", sv, sr="", sm[]=new String[4];
		this.mcampo="FORMA_TRIB_PER_X1";
		
		for(int i=1;i<=4;i++){
			sname1=sname+i;
			sv=mrec.getValor(sname1);
			sr+=sv;
			sm[i-1]=sv;
		}
		
		for(String s: opts){
			if(sr.indexOf(s)==-1){
				return(false);
			}
		}
		
		for(int i=0;i<4;i++){
			sv=sm[i];
			count=0;
			for(String s: opts){
				len++;
				if(sr.indexOf(s)>-1){
					count++;
				}
			}
			if(count==0){
				return(false);
			}

		}
		
		return(true);
		
	}
	
	public boolean simul_prim_forma_atrip_per(ArrayList<String> opts, String pfirst){
		boolean b;
		int count=0, len=0, ipassou;
		String sname1, sname="FORMA_TRIB_PER_X", sv, sr="";
		String sm[] = new String[4];
		
		this.mcampo="FORMA_TRIB_PER_X1";
		
		for(int i=1;i<=4;i++){
			sname1=sname+i;
			sv=mrec.getValor(sname1);
			sr+=sv;
			sm[i-1]=sv;
		}
		
		if(!(sr.indexOf(pfirst)>-1)){
			return(true);
		}
		
		for(String s: opts){
			len++;
			if(sr.indexOf(s)>-1){
				count++;
			}
		}
		if(count>0){
			ipassou=0;
			b=false;
			for(int i=0;i<4;i++){
				if(sm[i].equals(pfirst)){
					b=true;
					if(ipassou==2){
						return(false);
					}
					ipassou=1;
					
				}else{
					if(ipassou==0){
						return(false);
					}
					ipassou=2;
				}
			}
		}else{
			return(true);
		}
		
		return(true);
	}

}
