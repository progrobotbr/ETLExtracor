package sped.tree.capa;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import sped.bean.beanSped;
import sped.constants.IRegs;
import sped.db.sqlLocal;
import sped.db.util.CSql;
import sped.db.util.where;
import sped.msg.mensagem;
import sped.tree.Table;
import sped.tree.createIds;
import sped.tree.capa.bloco.Bloco;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.messageValid;

//1o. Nível
public class Capa {
	
	public static final String ID_CAPA = "ID_CAPA";
	public static final String ID_CAPA_CAD_BASICO = "-1";
	public static final int    ID_CAPA_CAD_BASICO_INT = -1;
	public static final String ALLREC = "ALLREC";
	public static final String R0000  = "00000";
	public static final String R0001  = "0001";
	public static final String ID     = "ID";
	public static final String IDCAPA = "IDCAPA";
	public static final String ID_PAI = "ID_PAI";
	public static final String REGPAI = "REGPAI";;
	public static final String REG    = "REG";
	public static final String BL_0   = "BL_0";
	public static final String BL_9   = "BL_9";
	public static final String BL_C   = "BL_C";
	public static final String BL_    = "BL_";
	public static final String ALL    = "ALL";
	public static final String R9999  = "9999";
	public static final String R9900  = "9900";
	public static final String P001   = "001";
	public static final String P990   = "990";
	
	public int rc;
	
	private int idcapa        = 0;
	private beanSped mbeansped= null;
	private sqlLocal msql     = null;
	private String mltinse = "insert into " + beanSped.getNomeProcesso()+"_mcapa (idcapa, descricao, dt_inicio, dt_fim) values ('@V1','@V2','@V3','@V4')";
	private String mltupda = "update " + beanSped.getNomeProcesso()+"_mcapa set descricao='@V1',dt_inicio='@V2',dt_fim='@V3' where idcapa='@V4'";
	private String mselect = "select idcapa,descricao,dt_inicio,dt_fim from " + beanSped.getNomeProcesso()+"_mcapa";
	
	private int iqtdrecblo;  //usado para geração do arquivo - quantidade de registros do bloco
	private int iqtdrecarq;  //usado para geração do arquivo - quantidade de registros do arquivo
	
	private Bloco mBloco=null; //2o.nível
	
	private Table table=null;
	
	public Capa(beanSped pbeansped){
		mbeansped = pbeansped;
		msql = mbeansped.getSql();
	}
	
	public int createCapa(String pdescricao, String pdtinicio, String pdtfim) throws Exception{
		int id=createIds.EMPTY, id2=createIds.EMPTY;
		String ssql = mltinse, ssqlbase = "select REG from " + beanSped.getNomeProcesso()+"_b001 where regpai = 'BL_0'", ssql2;
		rc = mensagem.IERRO;
		
		createIds ocrid = new createIds(msql);
		id = ocrid.createId(IRegs.ZERO, IRegs.CAPA);
		if(ocrid.rc!=mensagem.ISUCESSO){
			return(-1);
		}
		
		ssql = CSql.conc(ssql, "@V1", id);
		ssql = CSql.conc(ssql, "@V2", pdescricao);
		ssql = CSql.conc(ssql, "@V3", pdtinicio);
		ssql = CSql.conc(ssql, "@V4", pdtfim);
		
		rc = msql.change(ssql);
		
		if(rc != mensagem.ISUCESSO){
			msql.rollback();
			return(createIds.EMPTY);
		}
		
		ssql = "select reg from "+beanSped.getNomeProcesso()+"_b001 where regpai = 'CAPA'";
		
		ResultSet rs = msql.select(ssql);
		while(rs.next()){
			ssql = "insert into " + beanSped.getNomeProcesso()+"_mblocos (idcapa, reg, id) values ('@v1','@v2','@v3')";
			id2 = ocrid.createId(id, rs.getString("REG"));
			rc = ocrid.rc;
			if(rc!=mensagem.ISUCESSO){
				msql.rollback();
				return(createIds.EMPTY);
			}
			ssql = ssql.replace("@v1", CSql.itoc(id)).replace("@v2",rs.getString("REG")).replace("@v3", CSql.itoc(id2));
			rc = msql.change(ssql);
			if(rc!=mensagem.ISUCESSO){
				msql.rollback();
				return(createIds.EMPTY);
			}
		}
		
		Record record;
		ResultSet rs2;
		rs = msql.select(ssqlbase);
		while(rs.next()){
			ssql2 = "select idcapa, id_pai, id, regpai, reg from " + beanSped.getNomeProcesso()+"_m@v1 where idcapa = '@v2'";
			ssql2 = ssql2.replace("@v1",  rs.getString("REG")).replace("@v2", Capa.ID_CAPA_CAD_BASICO);
			rs2 = msql.select(ssql2);
			while(rs2.next()){
				record = new Record(this.mbeansped, 0, Capa.ID_CAPA_CAD_BASICO_INT, "BL_0", 1, rs.getString("REG"), rs2.getInt("ID"));
				if(record.hasData()){
					record.setIdCapa(id);
					record.setId(0);
					record.insert();
				}
			}
		}
		
		return(id);
	}
	
	public void changeCapa(int idcapa, String pdescricao, String pdtinicio, String pdtfim) throws Exception{
		String ssql = this.mltupda;
		rc = mensagem.IERRO;
		ssql = CSql.conc(ssql, "@V1", pdescricao);
		ssql = CSql.conc(ssql, "@V2", pdtinicio);
		ssql = CSql.conc(ssql, "@V3", pdtfim);
		ssql = CSql.conc(ssql, "@V4", idcapa);
		rc = msql.change(ssql);
	}
	
	public void selectCapa(int idcapa){
		where wh = new where();
		wh.put("idcapa", CSql.itoc(idcapa));
		this.selectCapa(wh);
	}
	
	public void selectCapa(where pwhere){
		String swhere="";
		String ssql = this.mselect;
		Record record;
		
		rc = mensagem.IERRO;
		
		record = new Record(this.mbeansped, "capa");
		record.putField("IDCAPA",    "INTEGER",  "10", "");
		record.putField("DESCRICAO", "VARCHAR", "255", "");
		record.putField("DT_INICIO", "VARCHAR",   "8", "");
		record.putField("DT_FIM",    "VARCHAR",   "8", "");
		
		if(pwhere!=null){
			swhere = " where " + pwhere.getWhere();
		}else{
			swhere = " where idcapa <> '"+ Capa.ID_CAPA_CAD_BASICO+"'";
		}
		ssql+=swhere;
		
		table=new Table(msql, record, ssql);
		
		rc = table.rc;
	}
	
	/*
	 * Valilda Capa
	 */
	public mensagem validCapa(int idcapa) throws Exception{
		
		ResultSet rsb, rsb001;
		
		String ssqlblocos = "select * from " + beanSped.getNomeProcesso()+"_mblocos where idcapa='@v1' order by reg";
		String ssqlb001   = "select * from " + beanSped.getNomeProcesso()+"_b001 where regpai = '@v1' order by reg";
		String ssqldel    = "delete from " + beanSped.getNomeProcesso()+"_mlog";
		String sqlblocos = "", sqlb001 = "";
		
		if(idcapa==0){
			mensagem msg = new mensagem();
			msg.tipo = mensagem.ERRO;
			msg.mensagem = "Informar id da capa";
			return(msg);
		}
		
		msql.change(ssqldel);
		sqlblocos = ssqlblocos.replace("@v1", CSql.itoc(idcapa));
		rsb = msql.select(sqlblocos);
		while(rsb.next()){
			sqlb001 = ssqlb001.replace("@v1", rsb.getString(Capa.REG));
			rsb001 =  msql.select(sqlb001);
			while(rsb001.next()){
			
				this.validRecursive(rsb.getInt(Capa.IDCAPA), rsb.getInt(Capa.ID), rsb.getString(Capa.REG), rsb001.getString(Capa.REG));
				
			}
			rsb001.getStatement().close();
			rsb001.close();
		}
		
		return(null);
			
	}
	
	private boolean validRecursive(int idcapa, int idpai, String regpai, String reg) throws Exception{
		boolean b=false;
		String sqlr = "select * from " + beanSped.getNomeProcesso()+"_m@v1 where idcapa='@v2' and id_pai='@v3' and regpai='@v4'";
		String sqlb001 = "select * from " + beanSped.getNomeProcesso()+"_b001 where regpai = '@v1' order by reg";
		String sqlb001s="";
		ResultSet rsr, rsb;
		Record record;
		ArrayList <messageValid>msgs;
		int iidcapa, iidpai, iid;
		String ssregpai, ssreg, ssreg2;
		
		/*debug
		if(regpai.equals("BL_C") && reg.equals("C100")){
			int i = 0;
			i=5;
		}
		*/
		sqlr = sqlr.replace("@v1", reg).
				    replace("@v2", CSql.itoc(idcapa)).
				    replace("@v3", CSql.itoc(idpai)).
				    replace("@v4", regpai);
		
		rsr = msql.select(sqlr);
		
		if(rsr.next()){
			do{
				iid      = rsr.getInt(Capa.ID);
				iidpai   = rsr.getInt(Capa.ID_PAI);
				iidcapa  = rsr.getInt(Capa.IDCAPA);
				ssregpai = rsr.getString(Capa.REGPAI);
				ssreg    = rsr.getString(Capa.REG);
				
				record = new Record(mbeansped,
			             0,
			             idcapa,
			             ssregpai,
			             iidpai,
			             ssreg,
			             iid);
				msgs = record.validRecord();
				for(messageValid msg: msgs){
					msg.saveDB(msql);
				}
				
				sqlb001s = sqlb001.replace("@v1", ssreg);
				rsb =  msql.select(sqlb001s);
				while(rsb.next()){
					ssreg2 =  rsb.getString(Capa.REG);
					this.validRecursive(iidcapa, iid, reg, ssreg2);
				}
				
				b=true;
				
			}while(rsr.next());
		}else{
			return(false);
		}
		
		return(b);
	}
	/*
	 *  Fim validar
	 */
	
	/*
	 * Gera arquivo
	 */
	public String createFile(int pcapa) throws Exception{
		//boolean bfirst=true;
		String ssqlblocos = "select a.* from " + beanSped.getNomeProcesso()+"_mblocos as a inner join " + beanSped.getNomeProcesso()+"_b001 as b on b.regpai = 'CAPA' and a.reg = b.reg  where idcapa='@v1' order by b.seq";
		String ssqlb001   = "select * from " + beanSped.getNomeProcesso()+"_b001 where regpai = '@v1' order by seq";
		//String ssqldel    = "delete from spdf_mlog";
		String sqlblocos = "", sqlb001 = "";
		String slin001, slin990, slin9900, slin9999="", sbl="";
		ResultSet rsb, rsb001;
		StringBuffer sb  = new StringBuffer();
		StringBuffer sb1;
		StringBuffer sb0000 = new StringBuffer();
		
		RegCountCollection regCollection = new RegCountCollection();
		
		if(pcapa==0){
			return("");
		}
		
		this.iqtdrecarq=0;
		this.iqtdrecblo=0;
		sqlblocos = ssqlblocos.replace("@v1", CSql.itoc(pcapa));
		rsb = msql.select(sqlblocos);
		while(rsb.next()){
			
			//Aborta o bloco 9
			if(rsb.getString(Capa.REG).equals(Capa.BL_9)){
				continue;
			}
			
			sb1 = new StringBuffer();
			sqlb001 = ssqlb001.replace("@v1", rsb.getString(Capa.REG));
			rsb001 =  msql.select(sqlb001);
			
			while(rsb001.next()){
			
				
				this.iqtdrecblo=0;
				this.createFileRecursive(regCollection, 
						                 sb0000, 
						                 sb1, 
						                 rsb.getInt(Capa.IDCAPA), 
						                 rsb.getInt(Capa.ID), 
						                 rsb.getString(Capa.REG), 
						                 rsb001.getString(Capa.REG), 
						                 rsb.getString(Capa.REG));
				
			}
			
			slin001=this.createReg001(regCollection, rsb.getString(Capa.REG));
			slin990=this.createReg990(regCollection, rsb.getString(Capa.REG));
			if(rsb.getString(Capa.REG).equals(Capa.BL_0)){ //senão coloco 0001 0000 .... certo 0000 0001
				sb.append(sb1.toString().replaceFirst("\n", "\n"+slin001));
				sb.append(slin990);
			}else{
				sb.append(slin001);
				sb.append(sb1.toString());
				sb.append(slin990);
			}
			
			rsb001.getStatement().close();
			rsb001.close();
		}
		
		//
		//Início Bloco 9
		//
		int ibloc9count=0;
		sb1 = new StringBuffer();
		for(RegCount rc: regCollection.getList()){
			if(rc.reg.startsWith(Capa.BL_) || rc.reg.startsWith(Capa.ALL)){
				continue;
			}
			slin9900 = this.createReg9900(regCollection, rc.reg);
			ibloc9count++;
			sb1.append(slin9900);
		}
		
		//++ibloc9count;
		regCollection.setQtdRec(Capa.BL_9, ibloc9count);
		slin001=this.createReg001(regCollection, Capa.BL_9);
		slin990=this.createReg990(regCollection, Capa.BL_9);
		slin9999=this.createReg9999(regCollection);
		sb.append(slin001);
		sb.append(sb1.toString());
		sb.append(slin990);
		sb.append(slin9999);
		//
		// Fim do bloco 9
		//
		sb0000.append(sb.toString());
		return(sb0000.toString());
			
	}
	
	private String createReg001(RegCountCollection rc, String reg){
		int qtdrec, ind_mov;
		String s="", sreg;
		qtdrec = rc.getQtdRec(reg);
		if(qtdrec>0){
			ind_mov = 0;
		}else{
			ind_mov = 1;
		}
		sreg = reg.substring(3,4)+Capa.P001;
		s+=Record.FIELDSEP+sreg+Record.FIELDSEP+ind_mov+Record.FIELDSEP+Record.RECORDSEP;
		rc.put(sreg);
		rc.put(Capa.ALLREC);
		return(s);
		
	}
	private String createReg990(RegCountCollection rc, String reg){
		int qtdrec;
		String s="", sreg;
		qtdrec = rc.getQtdRec(reg)+2;
		sreg = reg.substring(3,4)+Capa.P990;
		s+=Record.FIELDSEP+sreg+Record.FIELDSEP+qtdrec+Record.FIELDSEP+Record.RECORDSEP;
		rc.put(sreg);
		rc.put(Capa.ALLREC);
		return(s);
		
	}
	
	private String createReg9900(RegCountCollection rc, String reg){
		int qtdrec;
		String s="";
		
		qtdrec = rc.getQtdRec(reg);
		s+=Record.FIELDSEP+Capa.R9900+Record.FIELDSEP+reg+Record.FIELDSEP+qtdrec+Record.FIELDSEP+Record.RECORDSEP;
		rc.put(Capa.ALLREC);
		return(s);
	}
	
	private String createReg9999(RegCountCollection rc){
		int qtdrec;
		String s="";
		rc.put(Capa.ALLREC);
		qtdrec = rc.getQtdRec(Capa.ALLREC);
		//rc.put("9900");
		s+=Record.FIELDSEP+Capa.R9999+Record.FIELDSEP+qtdrec+Record.FIELDSEP+Record.RECORDSEP;
		return(s);
		
	}
	
	private boolean createFileRecursive(RegCountCollection regCollection,  
			                            StringBuffer sb0000, //usado para guardar o registro 0000, ele é separado pois pode ter problema de ordem 
			                            StringBuffer sb,     //usado para guardar os registros
			                            int idcapa, 
			                            int idpai, 
			                            String regpai, 
			                            String reg, 
			                            String bloco         //sumariza a quantidade por blocos
			                            ) throws Exception{
		boolean b=false;
		int iidcapa, iidpai, iid;
		String ssregpai, ssreg, ssreg2, slin;
		String sqlr = "select * from " + beanSped.getNomeProcesso()+"_m@v1 where idcapa='@v2' and id_pai='@v3' and regpai='@v4'";
		String sqlb001 = "select * from " + beanSped.getNomeProcesso()+"_b001 where regpai = '@v1' order by reg";
		String sqlb001s="";
		ResultSet rsr, rsb;
		Record record;
		
		/* debug
		if(regpai.equals("BL_C") && reg.equals("C100")){
			int i = 0;
			i=5;
		}
		*/
		
		sqlr = sqlr.replace("@v1", reg).
				    replace("@v2", CSql.itoc(idcapa)).
				    replace("@v3", CSql.itoc(idpai)).
				    replace("@v4", regpai);
		
		rsr = msql.select(sqlr);
		
		if(rsr.next()){
			do{
				iid      = rsr.getInt(Capa.ID);
				iidpai   = rsr.getInt(Capa.ID_PAI);
				iidcapa  = rsr.getInt(Capa.IDCAPA);
				ssregpai = rsr.getString(Capa.REGPAI);
				ssreg    = rsr.getString(Capa.REG);
				
				record = new Record(mbeansped,
			             0,
			             idcapa,
			             ssregpai,
			             iidpai,
			             ssreg,
			             iid);
				slin = record.getTextRecord();
				if(reg.equals(Capa.R0000)){
					sb0000.append(slin);
				}else{
					sb.append(slin);
				}
				regCollection.put(ssreg);
				regCollection.put(bloco);
				regCollection.put(Capa.ALLREC);
				
				sqlb001s = sqlb001.replace("@v1", ssreg);
				rsb =  msql.select(sqlb001s);
				while(rsb.next()){
					ssreg2 =  rsb.getString(Capa.REG);
					this.createFileRecursive(regCollection, sb0000, sb, iidcapa, iid, reg, ssreg2, bloco);
				}
				
				b=true;
				
			}while(rsr.next());
		}else{
			return(false);
		}
		
		return(b);
	}

	/*
	 * Fim gera arquivo
	 */
	
	public void selectBloco(){ //Com base na primeira linha
		int idcapa;
		Bloco bloco;
		if(table!=null){
			table.initIterator();
			if(table.hasNext()){
				table.moveNext();
				idcapa = table.getFieldByNameInt(Capa.IDCAPA);
				bloco = new Bloco(mbeansped);
				bloco.selectBloco(idcapa);
				rc = bloco.rc;
				this.setBloco(bloco);
				return;
			}
		}
		rc=mensagem.IERRO;
	}
	
	public void setBloco(Bloco bloco){
		mBloco = bloco;
	}
	public Bloco getBloco(){
		return(mBloco);
	}
	public Table getTable(){
		return(table);
	}
	public void setIdCapa(int id){
		this.idcapa = id;
	}
	public int getIdCapa(){
		return(this.idcapa);
	}
	

}
