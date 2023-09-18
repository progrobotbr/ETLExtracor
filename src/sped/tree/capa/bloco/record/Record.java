package sped.tree.capa.bloco.record;

import http.protocol.HttpRequest;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import sped.bean.beanSped;
import sped.db.sqlLocal;
import sped.db.util.CSql;
import sped.db.util.where;
import sped.http.app.app001.TelaSpedTree;
import sped.msg.mensagem;
import sped.tree.Field;
import sped.tree.RegAttrib;
import sped.tree.Table;
import sped.tree.createIds;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.validRules;
import sped.tree.capa.bloco.valid.exit.types.baseDate;
import utils._;

public class Record {
	
	/*Constants */
	public static final int    MAXFECHSIZE = 100000;
	public static final String NA          = "#NA"; 
	public static final String SPDF_M      = beanSped.getNomeProcesso()+"_m"; //"spdf_m";
	public static final int    CRECTPREGS  = 1;
	public static final int    CRECTPGERAL = 2;
	public static final String FIELDSEP = "|";
	public static final String FIELDSEPUP = ";";
	public static final String RECORDSEP = "\r\n";
	
	/*Extern access*/
	public int rc;
	
	/*local*/
	private int    rectipo;
	private int    nivel;
	private int    idpai;
	private int    id;
	private int    idcapa;
	private String regpai="";
	private String reg="";
	private HashMap<String,Field>   fields=null;
	private ArrayList<String>      lfields=null;
	//private HashMap<String,RegAttrib> regs=null; //Todos os registros filhos
	private beanSped mbeansped=null;
	private sqlLocal msql=null;
	private Table    mtblocal=null;
	private Record   mrecfilho=null;
	private Record   mrecpai=null;
	private boolean  bhasData =false;
	
	private int ifieldseq = -1;
	
	/*selects*/
	private String   mselect ="select field,tipo,tamanho,decimais,obrigatorio,obrig_tam_exato,tamanho_tela,descr_curta,descr_longa,validacao_re,validacao_jv,dom_tabela,dom_tabela_chave,mascara_format from " + beanSped.getNomeProcesso()+"_b002 where reg='@V1' order by seq ";
	private String   mselect2="select @V1 from @V2 where idcapa='@V3' and id_pai='@V4' and id='@V5' and regpai='@V6' and reg='@V7'";
	private String   mselect3="select @V1 from @V2 where idcapa='@V3' and id_pai='@V4'";
	private String   mselect4="select * from " + beanSped.getNomeProcesso()+"_b001 where regpai='@V1'";
	private String   mdelete ="delete from @V1 where idcapa='@V2' and idpai='@V3' and id='@V4'";
	
	/*inicia o record*/
	public Record(beanSped pbeanSped, int pnivel, int pidcapa, String pregpai, int pidpai, String preg, int pid) throws Exception{
		
		Field mfield;
		RegAttrib ra;
		
		int i;
		
		this.rectipo = Record.CRECTPREGS;
		this.mbeansped  = pbeanSped;
		this.msql   = this.mbeansped.getSql();
		this.nivel  = pnivel;
		this.reg    = preg.toUpperCase();
		this.idcapa = pidcapa;
		this.regpai = pregpai.toUpperCase();
		this.idpai  = pidpai;
		this.id     = pid;
		
		rc=mensagem.IERRO;
		
		String ssql=this.mselect;
		
		fields = new HashMap<String,Field>();
		lfields = new ArrayList<String>();
		ssql   = CSql.conc(ssql, "@V1", reg);
		ResultSet rs=msql.select(ssql);
		rc=msql.rc;
		if(msql.rc!=mensagem.ISUCESSO){
			return;
		}
		i=0;
		try{
			while(rs.next()){
				mfield                  = new Field();
				mfield.Posicao          = i++;
				mfield.Nome             = rs.getString("FIELD");
				mfield.Tipo             = rs.getString("TIPO");
				mfield.Tamanho          = rs.getString("TAMANHO");
				mfield.Tamanho_Tela     = rs.getString("TAMANHO_TELA");
				mfield.Decimal          = rs.getString("DECIMAIS");
				mfield.Obrigatorio      = rs.getString("OBRIGATORIO");
				mfield.Obrig_Tam_Exato  = rs.getString("OBRIG_TAM_EXATO");
				mfield.Descr_Curta      = rs.getString("DESCR_CURTA");
				mfield.Descr_Longa      = rs.getString("DESCR_LONGA");
				mfield.Validacao_JV     = rs.getString("VALIDACAO_JV");
				mfield.Validacao_RE     = rs.getString("VALIDACAO_RE");
				mfield.Mascara_format   = rs.getString("MASCARA_FORMAT");
				mfield.Dom_Tabela       = rs.getString("DOM_TABELA");
				mfield.Dom_Tabela_Chave = rs.getString("DOM_TABELA_CHAVE");
				if(_.notEmpty(mfield.Tipo)){
					if(mfield.Tipo.equals(validRules.DATE)){
						mfield.Tamanho = "10";
					}
				}
				fields.put(mfield.Nome, mfield);
				lfields.add(mfield.Nome);
			}
		}catch(Exception ex){
			rc=mensagem.IERRO;
			return;
		}
		msql.closeResultSet(rs);
		
		this.bhasData = this.selectUmChave();
		
		/*
		ssql=this.mselect4;
		ssql   = CSql.conc(ssql, "@V1", reg);
		regs = new HashMap<String,RegAttrib>();
		rs=msql.select(ssql);
		rc=msql.rc;
		if(msql.rc!=mensagem.ISUCESSO){
			return;
		}
		try{
			while(rs.next()){
				ra = new RegAttrib();
				ra.reg       = rs.getString("REG");
				ra.regpai    = rs.getString("REGPAI");
				ra.numocor   = rs.getInt("NUMOCCUR");
				ra.descricao = rs.getString("DESCRICAO");
				if(_.notEmpty(rs.getString("REGPAI"))){
					ra.ebase = true;
				}
				regs.put(ra.regpai,ra);
			}
		}catch(Exception ex){
			rc=mensagem.IERRO;
			return;
		}
		msql.closeResultSet(rs);
		rc=mensagem.ISUCESSO;
		*/
	}
	
	//Usado para select geral, somente estrutura
	public Record(beanSped pbeanSped, String preg){
		this.rectipo = Record.CRECTPGERAL;
		this.reg=preg;
		this.mbeansped=pbeanSped;
		this.msql = pbeanSped.getSql();
		fields = new HashMap<String,Field>();
		lfields = new ArrayList<String>();
	}
	
	//para record tipo Geral coloca campo
	public void putField(String nome, String tipo, String tamanho, String valida_re){
		Field field = new Field();
		ifieldseq++;
		field.Nome = nome;
		field.Tipo = tipo;
		field.Tamanho = tamanho;
		field.Posicao = ifieldseq;
		field.Validacao_RE = valida_re;
	    fields.put(nome, field);
	    lfields.add(nome);
	    
	}
	
	public Set<String>getKeySet(){
		return(fields.keySet());
	}
	
	public Iterator<String> getKeySetList(){
		return(lfields.iterator());
	}
	
	public Field getFieldDefinition(String key){
		Field mfield;
		if(fields.containsKey(key)){
			mfield = fields.get(key);
			rc=mensagem.ISUCESSO;
			return(mfield);
		}else{
			rc=mensagem.IERRO;
			return(null);
		}
	}
	
	
	//funções que leem e escrevem valores 
	public void putValor(String Name, String Value){
		Field mfield;
		if(fields.containsKey(Name)){
			mfield = fields.get(Name);
			mfield.Valor=Value;
			rc=mensagem.ISUCESSO;
		}else{
			rc=mensagem.IERRO;
		}
	}
	public String getValor(String Name){
		Field mfield;
		if(fields.containsKey(Name)){
			rc=mensagem.ISUCESSO;
			mfield=fields.get(Name);
			return(mfield.Valor);
		}else{
			rc=mensagem.IERRO;
			return(Record.NA);
		}
	}
	
	public int getFieldPosition(String Name){
		Field mfield;
		if(fields.containsKey(Name)){
			rc=mensagem.ISUCESSO;
			mfield=fields.get(Name);
			return(mfield.Posicao);
		}else{
			rc=mensagem.IERRO;
			return(-1);
		}
		
	}
	
	//Crud
	//rotina auxiliar
	private String sqlCampos(){
		String sk;
		StringBuffer sb = new StringBuffer();
		sb.append("idcapa,");
		sb.append("id_pai,");
		sb.append("id,");
		sb.append("reg");
		Set<String> set=fields.keySet();
		Iterator <String>it = set.iterator();
		while(it.hasNext()){
			sk=it.next();
			sb.append(",");
			sb.append(sk);
		}
		return(sb.toString());
	}
	
	private String sqlCampos2(){
		String sk, virg="";
		StringBuffer sb = new StringBuffer();
		Iterator <String>it = lfields.iterator();
		while(it.hasNext()){
			sk=it.next();
			sb.append(virg);
			sb.append(sk);
			virg=",";
		}
		return(sb.toString());
	}
	
	private String nometabela(){
		return(Record.SPDF_M+reg);
	}
	
	
	//select somente na chave
	public boolean selectUmChave(){
		boolean b=false;
		String ssql="",sk;
		Field mfield;
		ResultSet rs;
		
		//rc=mensagem.IERRO;
		if(id==0 || idpai==0 || idcapa==0){
			return b;
		}
		
		try{
		
			ssql = this.mselect2;
			ssql=CSql.conc(ssql, "@V1",this.sqlCampos());
			ssql=CSql.conc(ssql, "@V2", this.nometabela());
			ssql=CSql.conc(ssql, "@V3", idcapa);
			ssql=CSql.conc(ssql, "@V4", idpai);
			ssql=CSql.conc(ssql, "@V5", id);
			ssql=CSql.conc(ssql, "@V6", regpai);
			ssql=CSql.conc(ssql, "@V7", reg);
			rs=msql.select(ssql);
			if(msql.rc!=mensagem.ISUCESSO){
				return b;
			}
			if(rs.next()){
				b=true;
				Set<String> set=fields.keySet();
				Iterator <String>it = set.iterator();
				while(it.hasNext()){
					sk=it.next();
					mfield=fields.get(sk);
					mfield.Valor=rs.getString(sk);
				}
				//rc=mensagem.ISUCESSO;
			}
			msql.closeResultSet(rs);
			
		}catch(Exception ex){
			//rc=mensagem.IERRO;
			_.log("Record.SelectUmChave-"+ex.toString());
			ex.printStackTrace();
			return b;
		}
		
		return(b);
	}
	
	//com id do pai
	public Table selectVariosDoPai(int maxrows) throws Exception {
		String ssql="";
		ResultSet rs;
		Table tb=null;
		
		rc=mensagem.IERRO;
		if(idpai==0 || idcapa==0){
			return(null);
		}
		
		ssql = this.mselect3;
		ssql=CSql.conc(ssql, "@V1",this.sqlCampos());
		ssql=CSql.conc(ssql, "@V2", this.nometabela());
		ssql=CSql.conc(ssql, "@V3", idcapa);
		ssql=CSql.conc(ssql, "@V4", idpai);
		
		rs=msql.select2(MAXFECHSIZE, maxrows, ssql);
		if(msql.rc!=mensagem.ISUCESSO){
			return(null);
		}
		tb = new Table(this,rs);
		msql.closeResultSet(rs);
		rc = tb.rc;
		
		return(tb);
	}
	
	public Table selectVariosDoPai2(int maxrows) throws Exception {
		String ssql="";
		ResultSet rs;
		Table tb=null;
		
		rc=mensagem.IERRO;
		if(idpai==0 || idcapa==0){
			return(null);
		}
		
		ssql = this.mselect3;
		ssql=CSql.conc(ssql, "@V1",this.sqlCampos2());
		ssql=CSql.conc(ssql, "@V2", this.nometabela());
		ssql=CSql.conc(ssql, "@V3", idcapa);
		ssql=CSql.conc(ssql, "@V4", idpai);
		
		rs=msql.select2(MAXFECHSIZE, maxrows, ssql);
		if(msql.rc!=mensagem.ISUCESSO){
			return(null);
		}
		tb = new Table(this,rs);
		msql.closeResultSet(rs);
		rc = tb.rc;
		
		return(tb);
	}
	//Com clausula where
	public Table selectQualquer(int maxrows, where pwhere) throws Exception{
		String swhere="", ssql; 
		ResultSet rs;
		Table tb;
		
		rc = mensagem.IERRO;
		
		if(pwhere != null){
			swhere = pwhere.getWhere();
		}
		
		ssql = "select "+this.sqlCampos()+ " from "+this.nometabela()+swhere;
		rs=msql.select2(MAXFECHSIZE, maxrows, ssql);
		tb = new Table(this,rs);
		msql.closeResultSet(rs);
		
		rc = tb.rc;
		
		return(tb);
		
	}
	
	public void update() throws Exception {
		String ssql, sk,virg="";
		Field mfield;
		
		rc=mensagem.IERRO;
		if(id==0 || idpai==0 || idcapa==0){
			return;
		}
		
		ssql="update "+this.nometabela()+" set ";
		Set<String> set=fields.keySet();
		Iterator <String>it = set.iterator();
		while(it.hasNext()){
			sk=it.next();
			mfield=fields.get(sk);
			ssql+=virg+sk+"='"+mfield.Valor+"'";
			virg=",";
		}
		ssql+=" where idcapa='"+idcapa+"' and idpai='"+idpai+"' and id='"+id+"'";
		rc=msql.change(ssql);
	}
	
	public void updateFromRequest(HttpRequest request, int pidcapa, int pidpai, int pid, String pregpai, String preg) throws Exception {
		String ssql, sk,virg="";
		Field mfield;
		
		rc=mensagem.IERRO;
		if(pid==0 || pidpai==0){
			return;
		}
		
		ssql="update "+this.nometabela()+" set ";
		Set<String> set=fields.keySet();
		Iterator <String>it = set.iterator();
		while(it.hasNext()){
			sk=it.next();
			if(sk.equals("IDCAPA") ||
	   		   sk.equals("ID_PAI") || 
			   sk.equals("ID") ||
			   sk.equals("REGPAI")     ||
			   sk.equals("REG")){
					continue;
			}
			mfield=fields.get(sk);
			ssql+=virg+sk+"='"+request.getParameter(sk)+"'";
			virg=",";
		}
		ssql+=" where idcapa='"+pidcapa+"' and id_pai='"+pidpai+"' and id='"+pid+"' and regpai='"+pregpai+"' and reg='"+preg+"' ";
		rc=msql.change(ssql);
	}
	
	public void insert() throws Exception{
		String ssql, sk, sv="";
		Field mfield;
		
		rc=mensagem.IERRO;
		if(idpai==0 || idcapa==0){
			return;
		}
		
		if(id==0){
			createIds oid = new createIds(msql);
			id=oid.createId(idcapa, reg);
		}
		
		ssql="insert into "+this.nometabela()+" (idcapa,id_pai,id,regpai,reg";
		sv=" values ('"+idcapa+"','"+idpai+"','"+id+"','"+regpai+"','"+reg+"'";
		Set<String> set=fields.keySet();
		Iterator <String>it = set.iterator();
		while(it.hasNext()){
			sk=it.next();
			mfield=fields.get(sk);
			if(mfield.Nome.equals("ID") ||
			   mfield.Nome.equals("ID_PAI") ||		
			   mfield.Nome.equals("IDCAPA") ||
			   mfield.Nome.equals("REG") ||	
			   mfield.Nome.equals("REGPAI")){
				continue;
			}
			ssql+=","+sk;
			sv+=",'"+mfield.Valor+"'";
		}
		
		ssql+=") "+sv+")";
		rc=msql.change(ssql);
	}
	
	public void insertFromRequest(HttpRequest request, int pidcapa, int pidpai, int pid, String pregpai, String preg) throws Exception{
		String ssql, sk, sv="";
		Field mfield;
		
		rc=mensagem.IERRO;
		id = 0;
		
		if(pidpai==0 || pidcapa==0){
			return;
		}
		
		if(pid==0){
			createIds oid = new createIds(msql);
			pid=oid.createId(pidcapa, preg);
		}
		
		ssql="insert into "+this.nometabela()+" (idcapa,id_pai,id,regpai,reg";
		sv="('"+pidcapa+"','"+pidpai+"','"+pid+"','"+pregpai+"','"+preg+"'";
		Set<String> set=fields.keySet();
		Iterator <String>it = set.iterator();
		while(it.hasNext()){
			sk=it.next();
			ssql+=","+sk;
			mfield=fields.get(sk);
			sv+=",'"+request.getParameter(sk)+"'";
		}
		
		ssql+=") values "+sv+")";
		rc=msql.change(ssql);
		
		id = pid;
	}
	
	public void insertFromUpload(HttpRequest request, String nmfile, int pidcapa, int pidpai, int pid, String pregpai, String preg) throws Exception{
		int tam, idxf, idxp;
		String ssql, sv="", sfile, lines[], flds[], fld;
		createIds oid;
		
		rc=mensagem.IERRO;
		id = 0;
		
		if(pidpai==0 || pidcapa==0){
			return;
		}
		
		sfile = request.getParameter(nmfile);
		lines = sfile.split(Record.RECORDSEP);
		
		for(String line: lines){
			
			tam=0;
			idxf=0;
			oid = new createIds(msql);
			pid=oid.createId(pidcapa, preg);
		
			flds = line.split(Record.FIELDSEPUP);
			if(flds!=null){
				tam = flds.length;
			}
			ssql="insert into "+this.nometabela()+" (idcapa,id_pai,id,regpai,reg";
			sv="('"+pidcapa+"','"+pidpai+"','"+pid+"','"+pregpai+"','"+preg+"'";
			idxp=0;
			for(String sk : lfields){
				if(idxp<5){
					idxp++;
					continue;
				}
				if(idxf>=tam){
					break;
				}
				ssql+=","+sk;
				if(tam>0){
				  sv+=",'"+flds[idxf]+"'";
				}
				idxf++;
				
			}
		
			ssql+=") values "+sv+")";
			rc=msql.change(ssql);
			
		}
		
	}
	
	public void putRequestInRecord(HttpRequest request) throws Exception{
		Field f;
		for(String sk: lfields){
			f=fields.get(sk);
			f.Valor = request.getParameter(sk);
		}
	}
	
	/*
	 * Depois de um delete, deve-se reordenar tudo, pois o delete
	 *  é somente num nó e deveria ser recursivo
	 */
	public void delete() throws Exception{
		String ssql;
		
		rc=mensagem.IERRO;
		if(id==0 || idpai==0 || idcapa==0){
			return;
		}
		
		ssql=this.mdelete;
		ssql=CSql.conc(ssql, "@V1", this.nometabela());
		ssql=CSql.conc(ssql, "@V2", idcapa);
		ssql=CSql.conc(ssql, "@V3", idpai);
		ssql=CSql.conc(ssql, "@V4", id);
		
		rc=msql.change(ssql);
		
	}
	
	
	public void clearLocalTable(){
		if(mtblocal!=null){
			mtblocal.record=null;
		}
	}
	
	/////////////////
	///Validação
	/////////////////
	public ArrayList<messageValid> validRecord() throws Exception{
		String sseq, sj, smsg, sql;
		Field f;
		messageValid mv=null;
		ArrayList<messageValid> msgs = new ArrayList<messageValid>();
		ResultSet rs;
		
		for(String skey: lfields){
			
			if(skey.equals("IDCAPA") ||
			   skey.equals("ID")     ||
			   skey.equals("REG")    ||
			   skey.equals("REGPAI") ||
			   skey.equals("ID_PAI")){
				continue;
			}
			f = fields.get(skey); 
			if(skey.equals("NUM_DOC")){
				_.print(skey);
			}
			//1- Valida tipos primitivos
			mv = validRules.validType(f, this);
			if(mv!=null){
				msgs.add(mv);
			}
			
			//2 - Valida regular expression
			if(_.notEmpty(f.Validacao_RE)){
				mv = validRules.validRE(f, this);
				if(mv!=null){
					msgs.add(mv);
				}
			}
			//3 - Valida java base
			if(_.notEmpty(f.Validacao_JV)){
				sseq = "0";
				sj   = f.Validacao_JV;
				mv   = validRules.validJVField(sseq, sj, f, this);
				if(mv!=null){
					mv.msgFromDB(this.msql);
					msgs.add(mv);
				}
			}
			
			//4 - Valida regras cadastradas sequenciais para campos
			sql = "select * from "+beanSped.getNomeProcesso()+"_b005 where reg='@v1' and field='@v2' and type = '@v3' order by seq";
			sql = sql.replace("@v1", this.reg).replace("@v2",f.Nome).replace("@v3",validRules.TYPEFIELDCOD);
			rs =  msql.select(sql);
			while(rs.next()){
				sseq = rs.getString("SEQ");
				sj   = rs.getString("VALIDACAO_JV");
				mv   = validRules.validJVField(sseq, sj,f,this);
				if(mv!=null){
					msgs.add(mv);
				}
			}
			rs.getStatement().close();
			rs.close();
			
			
			/*
			if(b==false){
				mv = new messageValid();
				mv.putMsg(validRules.ERROR, 4, idcapa, id, skey, null, "0", validRules.TYPEBASE, "", "");
				msgs.add(mv);
			}
			*/
			
		}
		
		//5 - Valida regras cadastradas sequenciais para o registro
		sql = "select * from "+beanSped.getNomeProcesso()+"_b005 where reg='@v1' and type = '@v2' order by seq";
		sql = sql.replace("@v1", this.reg).replace("@v2", validRules.TYPERECORDCOD);
		rs =  msql.select(sql);
		while(rs.next()){
			sseq = rs.getString("SEQ");
			sj   = rs.getString("VALIDACAO_JV");
			mv   = validRules.validJVRecord(sseq, sj, this);
			if(mv!=null){
				if(!mv.msgs.isEmpty()){
					for(messageValid mv2: mv.msgs){
						msgs.add(mv2);
					}
				}else{
					msgs.add(mv);
				}
			}
		}
		rs.getStatement().close();
		rs.close();
		
		
		return(msgs);
	}
	//
	// Fim validação
	//
	
	public String getTextRecord(){
		String ssep="";
		StringBuffer sb = new StringBuffer();
		Field mfield;
		
		rc=mensagem.IERRO;
		if(id==0 || idpai==0 || idcapa==0){
			return("");
		}
		
		for(String sk: lfields){
			mfield=fields.get(sk);
			
			//pula id id_pai regpai
			if(mfield.Nome.equals("ID") ||
			   mfield.Nome.equals("ID_PAI") ||		
			   mfield.Nome.equals("IDCAPA") ||
			  // mfield.Nome.equals("REG") ||	
			   mfield.Nome.equals("REGPAI")){
			   continue;
			}
					
			ssep=Record.FIELDSEP;
			if(mfield.Nome.matches(".*X([2-9]|([1-9]+[0-9]+))")){ // campos do campo_x1 campo_x2 ... campo_xn são um único campo e não possuem separadores
				ssep="";
			}
			if(mfield.Tipo.equals(Types.DATE)){ //formato data não pode ter / somente ddmmaaaa
				sb.append(ssep+mfield.Valor.replaceAll("/", ""));
			}else{
				sb.append(ssep+mfield.Valor);
			}
						
		}
		sb.append(Record.FIELDSEP);
		sb.append(Record.RECORDSEP);
		return(sb.toString());
	}
	
	//Get and Setters
	public int  getRecordTipo() { return this.rectipo;}
	public void seRecordTipo(int rectipo) {this.rectipo = rectipo;}
	
	public int  getNivel() { return nivel;}
	public void setNivel(int nivel) {this.nivel = nivel;}
	
	public int getIdPai() { return idpai;}
	public void setIdPai(int idpai) {this.idpai = idpai;}

	public int getId() {return id;	}
	public void setId(int id) {this.id = id;}

	public int getIdCapa() {return idcapa;	}
	public void setIdCapa(int idcapa) {this.idcapa = idcapa;}

	public String getRegPai() {	return regpai;}
	public void   setNomeRegPai(String regpai) {this.regpai = regpai;	}

	public String getReg() {return reg;	}
	public void   setReg(String reg) {this.reg = reg;	}
	
	public String getRegDescricao(){
		if(!_.notEmpty(this.reg)){
			return("");
		}
		String s = this.reg.toUpperCase();
		String sql = "select descricao from "+beanSped.getNomeProcesso()+"_b001 where reg = '"+this.reg+"'";
		try{
			ResultSet rs = this.msql.select(sql);
			if(rs.next()){
				s +=" - "+rs.getString("DESCRICAO");
			}
			rs.getStatement().close();
			rs.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return(s);
		
	}
	/*Hierarquia  */
	public void   setRecordFilho(Record filho){mrecfilho=filho;}
	public Record getRecordFilho(){return(mrecfilho);}
	
	public void   setRecordPai(Record pai) {mrecpai=pai;}
	public Record getRecordPai(){return(mrecpai);}
	
	public Table getTable(){return(this.mtblocal);}
	
	public beanSped getBeanSped(){return(this.mbeansped);}
	
	public Iterator <String> getIterator(){
	   return(lfields.iterator());	
	}
	
	public static int tamDefault(int i){
		if(i==0){
			return(60);
		}
		return(i);
	}
	public static String tamDefaultString(String s){
		if(s==null){
			return("60");
		}
		return(s);
	}
	
	public static String nullToString(String s){
		if(s==null){
			return("");
		}else{
			return(s);
		}
	}
	
	public boolean hasData(){
		return(this.bhasData);
	}
}
