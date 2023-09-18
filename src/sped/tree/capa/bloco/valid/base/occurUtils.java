package sped.tree.capa.bloco.valid.base;

import java.sql.ResultSet;
import java.util.ArrayList;

import sped.bean.beanSped;
import sped.http.app.app001.RegKey;
import sped.tree.capa.bloco.record.Record;
import utils._;

public class occurUtils {
	
	public static int N = -1;
	public static int DE = 0;
	public static int ATE = 1;
	public static int _0_0[]  = { 0,0 };  //0  
	public static int _0_1[]  = { 0,1 };  //1
	public static int _0_N[]  = { 0,N };  //2
	public static int _1_1[]  = { 1,1 };  //3
	public static int _1_12[] = { 1,12 }; //4
	public static int _1_13[] = { 1,13 }; //5
	public static int _1_N[]  = { 1,N };  //6
	public static int _2_N[]  = { 2,N };  //7
	public static int _0_13[] = { 0,13 }; //5
	//public static int OCCURS[][] = {  _0_0, _0_1, _0_N, _1_1, _1_12, _1_13, _1_N, _2_N };
	public static boolean PODE = true;
	public static boolean NAO_PODE = false;
	public static boolean CORRETO = true;
	public static boolean INCORRETO = false;
	
	private beanSped mbean=null;
	private Record mrecord=null;
	
	public occurUtils(beanSped pbeansped, Record precord){
		mbean=pbeansped;
		mrecord=precord;
	}
	
	public occurUtils(beanSped pbeansped, RegKey regKey) throws Exception{
		String sql;
		mbean=pbeansped;
		if(regKey.id==0){
			sql = "select idcapa, id_pai, id, regpai, reg from ecf_m"+regKey.regpai+" where idcapa=" + regKey.idcapa+ " and id="+regKey.idpai;
		}else{
			sql = "select idcapa, id_pai, id, regpai, reg from ecf_m"+regKey.reg+" where idcapa=" + regKey.idcapa+ " and id="+regKey.id;
		}
		
		try{
			ResultSet rs = 	mbean.getSql().select(sql);
			if(rs.next()){
				mrecord = new Record(pbeansped, regKey.nivel,rs.getInt("idcapa"), rs.getString("regpai"), rs.getInt("id_pai"), rs.getString("reg"),rs.getInt("id"));
			}
			return;
		}catch(Exception ex){
			_.log(ex.toString());
		}
		mrecord = null;
	}
	
	
	public boolean podeInserirNovo(int ocor[], int qtdOcorrenciaExistente){
		if(ocor[ATE]==N){
			return(PODE);
		}
		if(qtdOcorrenciaExistente < ocor[DE]){
			return(PODE);
		}
		if(qtdOcorrenciaExistente < ocor[ATE]){
			return(PODE);
		}
		return(NAO_PODE);
	}
	
	public boolean verificaOcorrencias(int ocor[], int qtdOcorrenciaExistente){
		if(ocor[ATE]==N){
			return(CORRETO);
		}
		if(qtdOcorrenciaExistente < ocor[DE]){
			return(INCORRETO);
		}
		if(qtdOcorrenciaExistente > ocor[ATE]){
			return(INCORRETO);
		}
		return(CORRETO);
	}
	
	public boolean verificaOcorrenciasPodeListar(int ocor[], int qtdOcorrenciaExistente){
		if(ocor[ATE]==N){
			return(CORRETO);
		}
		if(( qtdOcorrenciaExistente == 0 &&  ocor[ATE] == 0 )){ 
			return(INCORRETO);
		}
		return(CORRETO);
	}
	
	public int count(int pidcapa, int pidpai, String pregpai, String preg ){
		int i;
		String s = "select count(id) as qtdid from "+beanSped.getNomeProcesso()+"_m"+preg+
				   " where regpai='"+pregpai+"' and "+
				         "reg='"+preg+"' and "+
				         "id_pai='"+pidpai+"' and "+
				         "idcapa='"+pidcapa+"'";
		try{
		  ResultSet rs = mbean.getSql().select(s);
		  i=rs.getInt("qtdid");
		  return(i);
		}catch(Exception ex){
		  _.log(ex.toString());
		}
		return(0);
	}
	
	
	public boolean podeInserir(RegKey regKey){
		boolean b=false;
		int icount;
		int scond[];
		String sreg, sregra, sclass;
		Class clazz;
		IConditional ocond;
		ArrayList<String>lsres=new ArrayList<String>();
		String sql = "select reg, regraoccur from ecf_b001 where reg = '"+regKey.reg+"' ";
		
		try{
		  ResultSet rs = mbean.getSql().select(sql);
		  if(rs.next()){
		 
			  sreg = rs.getString("reg");
			  sregra = rs.getString("regraoccur");
			  
			  icount = this.count(regKey.idcapa,regKey.idpai, regKey.regpai, sreg);
			  
			  try{
				  if(!_.notNull(sregra)){
					  return(false);
				  }
				  sclass = "sped.tree.capa.bloco.valid.exit.occur."+sregra;
				  //sclass = sregra;
				  clazz = Class.forName(sclass);
				  ocond = (IConditional) clazz.newInstance();
				  ocond.init(mbean, mrecord);
				  scond = ocond.exec();
				  b=this.podeInserirNovo(scond, icount);
				  if(b==true){
					  lsres.add(sreg);
				  }
				  
			  }catch(Exception ex){
				  _.log(ex.toString());
			  }
		  }
		  
		  
		}catch(Exception ex){
			_.log(ex.toString());
		}
		return(b);
		
	}
	

	public ArrayList<String> getRegFilhosAoInserir(RegKey regKey){
		boolean b;
		int icount;
		int scond[];
		String sreg, sregra, sclass;
		Class clazz;
		IConditional ocond;
		ArrayList<String>lsres=new ArrayList<String>();
		String sql = "select reg, regraoccur from ecf_b001 where regpai = '"+regKey.reg+"'";
		
		try{
		  ResultSet rs = mbean.getSql().select(sql);
		  while(rs.next()){
			  
			  sreg = rs.getString("reg");
			  sregra = rs.getString("regraoccur");
			  
			  icount = this.count(regKey.idcapa, regKey.id, regKey.reg, sreg);
			  
			  try{
				  if(!_.notNull(sregra)){
					  continue;
				  }
				  sclass = "sped.tree.capa.bloco.valid.exit.occur."+sregra;
				  //sclass = sregra;
				  clazz = Class.forName(sclass);
				  ocond = (IConditional) clazz.newInstance();
				  ocond.init(mbean, mrecord);
				  scond = ocond.exec();
				  b=this.verificaOcorrenciasPodeListar(scond, icount);
				  if(b==true){
					  lsres.add(sreg);
				  }
				  
			  }catch(Exception ex){
				  _.log(ex.toString());
			  }
		  }
		  
		  
		}catch(Exception ex){
			_.log(ex.toString());
		}
		return(lsres);
	}

}


/*
public boolean occurInserir(int pidcapa, int pidpai, String pregpai, String preg ){
boolean b;
int ioccur;
int qtd =count(pidcapa, pidpai, pregpai, preg );
String sql = "select numoccur from ecf_b001 where reg = '"+preg+"'";
try{
  ResultSet rs = mbean.getSql().select(sql);
  ioccur=rs.getInt("qtdid");
}catch(Exception ex){
	ioccur=0;
  _.log(ex.toString());
}
b=podeInserirNovo(OCCURS[ioccur],qtd);
return(b);
}

public boolean occurValid(int pidcapa, int pidpai, String pregpai, String preg ){
boolean b;
int ioccur;
int qtd =count(pidcapa, pidpai, pregpai, preg );
String sql = "select numoccur from ecf_b001 where reg = '"+preg+"'";
try{
  ResultSet rs = mbean.getSql().select(sql);
  ioccur=rs.getInt("qtdid");
}catch(Exception ex){
	ioccur=0;
  _.log(ex.toString());
}
b=verificaOcorrencias(OCCURS[ioccur],qtd);
return(b);
}
*/