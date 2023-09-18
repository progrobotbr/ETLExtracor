package sped.tree.capa.bloco.valid.base;

import java.sql.ResultSet;
import java.util.HashMap;

import sped.bean.beanSped;
import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;
import utils._;

public class Conditional {

	private HashMap<String,Record> buffer = new HashMap<String,Record>();
	
	beanSped mbean=null;
	Record record=null;
	
	public void init(beanSped pbeanSped, Record precord){
		this.mbean=pbeanSped;
		this.record=precord;
	}
	
	//refazer
	public Operador pai(String s){
		int idcapa, idpai;
		String sql, sreg, sfield="", sregpai;
        Record orecord=null;
        Operador op;
        
        try{
        
	      if(s.indexOf(".")>0){
	    	  
	        String m[] = s.split("[.]");
	        sreg = m[0];
	        sfield = m[1];
	        
	        orecord = this.record;
	        
	        //pega do buffer
	        if(buffer.containsKey(sreg)){
	        	orecord = buffer.get(sreg);
		    	op = new Operador(orecord, sfield);
        		return(op);
	        }
	        
	        do{
	        	if(orecord.getReg().equals(sreg)){
	        		op = new Operador(orecord, sfield);
	        		return(op);
	        	}
	        	
	        	sregpai = orecord.getRegPai();
	        	idcapa = orecord.getIdCapa();
	        	idpai = orecord.getIdPai();
	        	
	        	sql = "select idcapa, id, id_pai, reg, regpai from ecf_m"+sregpai+" where idcapa = '"+idcapa+"' and id='"+idpai+"'";
	        	
	        	ResultSet rs = mbean.getSql().select(sql);
		        if(rs.next()){
		          orecord = new Record(mbean, 0, rs.getInt("idcapa"),rs.getString("regpai"),  rs.getInt("id_pai"),rs.getString("reg"),rs.getInt("id"));
		          buffer.put(sregpai, orecord);
		        }else{
		        	op = new Operador(null, sfield);
		        	return(op);
		        }
	        	
	        }while(true);
	        
	    }
	    
        }catch(Exception ex){
        	_.log(ex.toString());
        }
        
        op = new Operador(null, sfield);
    	return(op);
	}
	
	public Operador rec(String s){
		String sql;
        Record record=null;
        Operador op;
        
        try{
        
	    if(s.indexOf(".")>0){
	      String m[] = s.split("[.]");
	      if(buffer.containsKey(m[0])){
	    	record = buffer.get(m[0]);
	      }
	   
	      if(record == null){
	         sql = "select idcapa, id, id_pai, reg, regpai from ecf_m"+m[0]+" where idcapa = '"+this.record.getIdCapa()+"' limit 1";
	         ResultSet rs = mbean.getSql().select(sql);
	         if(rs.next()){
	           record = new Record(mbean, 0, rs.getInt("idcapa"),rs.getString("regpai"),  rs.getInt("id_pai"),rs.getString("reg"),rs.getInt("id"));
	           buffer.put(m[0], record);
	         }
	      }
	      
	      op = new Operador(record, m[1]);
	      return(op);
	      
	    }else{
	      op = new Operador(this.record, s);
	      return(op);
	    }
	    
        }catch(Exception ex){
        	_.log(ex.toString());
        	return null;
        }
	  }
	  
}
	

