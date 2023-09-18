package sped.http.app.app001;

import sped.db.util.CSql;
import utils._;

public class RegKey {
	
	public static final int EMPTY = -2;
	public int idcapa=EMPTY;
	public int idpai=EMPTY;
	public int id=EMPTY;
	public int nivel=EMPTY;
	public String regpai="";
	public String reg="";
	public boolean isEmpty = true;
	
	public RegKey(){
		isEmpty=true;
	}
	
	public RegKey(int pidcapa, int pidpai, int pid, int pnivel, String preg, String pregpai ){
		idcapa = pidcapa;
		idpai = pidpai;
		id =pid;
		nivel=pnivel;
		if(_.notNull(preg)){
		  reg=preg;
		}else{
		  reg="";	
		}
		if(_.notNull(pregpai)){
		  regpai = pregpai;
		}else{
		  regpai ="";
		}
		isEmpty = false;
	}
	
}
