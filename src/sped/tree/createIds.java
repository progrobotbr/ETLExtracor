package sped.tree;

import java.sql.ResultSet;

import sped.bean.beanSped;
import sped.db.sqlLocal;
import sped.db.util.CSql;
import sped.msg.mensagem;
import utils._;

public class createIds {
	
	public static int EMPTY = -1;
	
    private sqlLocal osql = null;
    private String mselect = "select id from " + beanSped.getNomeProcesso()+"_IDS where id_capa = '@1' and reg = '@2'";
    private String minsert = "insert into " + beanSped.getNomeProcesso()+"_IDS (ID_CAPA, REG, ID) values ('@1','@2','@3')";
    private String mupdate = "update " + beanSped.getNomeProcesso()+"_IDS set id='@1' where ID_CAPA=@2 and REG='@3'";
    public int rc=0;
    
    public createIds(sqlLocal psql){
    	osql = psql;
    }
    
	public int createId(long idcapa, String reg){
		boolean b;
		int id=0;
		String ssql;
		
		rc = mensagem.IERRO;
		
		ssql = this.mselect;
		ssql = CSql.conc(ssql, "@1", idcapa);
		ssql = CSql.conc(ssql, "@2", reg);
		
		try{
			ResultSet rs = osql.select(ssql);
			b=rs.next();
			if(b){
				id = rs.getInt("ID");
				id++;
				ssql = this.mupdate;
				ssql = CSql.conc(ssql, "@1", id);
				ssql = CSql.conc(ssql, "@2", idcapa);
				ssql = CSql.conc(ssql, "@3", reg);
			}else{
				id=1;
				ssql = this.minsert;
				ssql = CSql.conc(ssql, "@1", idcapa);
				ssql = CSql.conc(ssql, "@2", reg);
				ssql = CSql.conc(ssql, "@3", id);
			}
			
			rc=osql.change(ssql);
			
		}catch(Exception ex){
			_.log(ex.toString());
			ex.printStackTrace();
		}
		return(id);
	}
	
	
	
	

}
