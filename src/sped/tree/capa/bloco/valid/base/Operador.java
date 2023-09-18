package sped.tree.capa.bloco.valid.base;

import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.exit.types.baseDate;
import utils._;

public class Operador{

  String nmf;
  Record rec;
	 
  public Operador(Record r, String nmf){
    rec = r;
    this.nmf=nmf;
  }
	   
  public boolean eq(String s){
	if(rec==null){
	  return(false);
	}
	String sv = rec.getValor(nmf);
    return(sv.equals(s));   
  }
  
  public String value(){
    if(rec==null){
      return("");
	}
	return(rec.getValor(nmf));
  }
  
  public boolean dge(String pdt){
	  String sdt;
	  if(rec==null){
	      return(false);
	  }
	  sdt = rec.getValor(nmf);
	  if(! _.notEmpty(pdt) || !_.notEmpty(sdt)){
		  return(false);
	  }
	  
	  try{
	    int i1 = baseDate.getNumberDataAll(pdt);
	    int i2 = baseDate.getNumberDataAll(sdt);
	    if(i1<=i2){
	    	return(true);
	    }else{
	    	return(false);
	    }
	    	
	  }catch(Exception ex){
		  return(false);
	  }
  }

}
		
	