package sped.http.app.app001;

import java.sql.ResultSet;

import config.Config;
import sped.bean.beanSped;
import sped.db.util.CSql;
import sped.msg.mensagem;
import sped.tree.Table;
import sped.tree.capa.Capa;
import sped.tree.capa.bloco.Bloco;
import sped.tree.capa.bloco.record.Record;
import utils._;
import http.bean.beanHTTP;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public class TelaCadLog implements ITela{

	private static final String ONGETTABLELOG   = "ongettablelog";
	private static final String ONVALID         = "onvalid";
	private static final String ONGETFORM       = "ongetform";
	private static final String ONCREATEFILE    = "oncreatefile";
	private static final String telasped_07_log = "appServer/sped/sped_07_log";
	private String action="";
	private beanSped bean   = null;
	private Table metaTable = null;
	
	private int idcapa;
	private int idpai;
	private int id;
	private int ilevel;
	private String reg;
	private String regpai;
	
	private HttpSession session;
	private HttpRequest request;
	private HttpResponse response;
	
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception {
		
		this.session  = session;
		this.request  = request;
		this.response = response;
		
		this.getVarRequests();
		
		this.bean = beanSped.api(session);
		bean.getSql().beginTransaction();
		
		try{
			
			if(this.action.equals(TelaSpedTree.ONLOAD)){
				this.onLoad();
				
			}else if(this.action.equals(TelaCadLog.ONGETTABLELOG)){
				this.onGetTableLog();
				
			}else if(this.action.equals(TelaCadLog.ONVALID)){
				this.onValid();
				
			}else if(this.action.equals(TelaCadLog.ONGETFORM)){
				this.onGetForm();
				
			}else if(this.action.equals(TelaCadLog.ONCREATEFILE)){
				this.onCreateFile();
			}
			
			this.bean.getSql().commit();
			
		}catch(Exception ex){
			this.bean.getSql().rollback();
			String s=jsonUtil.doMessage(4, ex.toString()+";"+_.stackTrace(ex));
			response.print(s);
		}
		
	}
	
	private void onCreateFile() throws Exception{
		String json;
		Capa capa;
		String sfile, spath, sfix;
		
		beanHTTP beanhttp = beanHTTP.factory();
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		if(this.idcapa==0){
			response.print(jsonUtil.doMessage(4, "Informar o id da Capa"));
			return;
		}
		
		capa = this.bean.getCapa();
		sfile = capa.createFile(idcapa);
		if(_.notEmpty(sfile)){
			
			spath = Config.getString(Config.sped_file_path);
			sfix = _.zeroRight(5, CSql.itoc(idcapa));
			sfix = spath+beanhttp.SOPATHSEP+"sfs_"+sfix+"_"+_.getTimeString2()+".txt";
			
			if(_.saveFile(sfix, sfile)==0){
			response.print(jsonUtil.doMessage(0, "Arquivo gerado"));
			}else{
				response.print(jsonUtil.doMessage(4, "Erro ao gravar o arquivo"));
			}
		}else{
			response.print(jsonUtil.doMessage(4, "Não há dados para geração do arquivo"));
		}
	
	}
	
	private void onGetForm() throws Exception{
		
		ResultSet rs;
		int iidcapa, iidpai, iid;
		String ssregpai, ssreg;
		String shtml;
		Capa capa;
		Bloco bloco;
		
		//response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		String sql = "select idcapa, id_pai, id, regpai, reg from "+beanSped.getNomeProcesso()+"_m@v1 where idcapa = '@v2' and id='@v3'";
		sql=sql.replace("@v1", reg).replace("@v2", CSql.itoc(idcapa)).replace("@v3",CSql.itoc(id));
		
		rs = bean.getSql().select(sql);
		if(rs.next()){
			iidcapa  = rs.getInt("IDCAPA");
			iidpai   = rs.getInt("ID_PAI");
			iid      = rs.getInt("ID");
			ssregpai = rs.getString("REGPAI");
			ssreg    = rs.getString("REG");
			
			request.setParameter(TelaSpedTree.IDCAPA, CSql.itoc(iidcapa));
			request.setParameter(TelaSpedTree.IDPAI, CSql.itoc(iidpai));
			request.setParameter(TelaSpedTree.ID, CSql.itoc(iid));
			request.setParameter(TelaSpedTree.REG, ssreg);
			request.setParameter(TelaSpedTree.REGPAI, ssregpai);
			request.setParameter(TelaSpedTree.ACTION, "ongetform");
			
			TelaSpedTree telatree = bean.getTelaSpedTree();
			if(telatree == null){
				telatree = new TelaSpedTree();
				bean.setTelaSpedTree(telatree);
			}
			
			telatree.post(session, request, response);
			
			return;
			
		}
		
		response.print(jsonUtil.doMessage(4, "Não foi gerado formulário"));
	}
	
	private void onLoad() throws Exception{
		String sql;
		Record record;
		Capa capa;
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		if(this.idcapa==0){
			response.print(jsonUtil.doMessage(4, "Informar o id da Capa"));
			return;
		}
		
		capa = this.bean.getCapa();
		capa.selectCapa(idcapa);
		if(capa.rc != mensagem.ISUCESSO){
			response.print(jsonUtil.doMessage(4, "Capa não encontrada"));
			return;
		}
		
		capa.selectBloco();
		if(capa.rc != mensagem.ISUCESSO){
			response.print(jsonUtil.doMessage(4, "Bloco não encontrado"));
			return;
		}
		
		record = new Record(bean,"MLOG");
		record.putField("IDCAPA",  "INTEGER", "10", "");
		record.putField("ID",      "INTEGER", "10", "");
		record.putField("REG",     "VARCHAR",  "4", "");
		record.putField("DATE",    "DATE",     "8", "");
		record.putField("TYPE",    "VARCHAR",  "1", "");
		record.putField("NUMBER",  "INTEGER", "10", "");
		record.putField("FIELD",   "VARCHAR", "50", "");
		record.putField("RULESEQ", "VARCHAR", "50", "");
		record.putField("RULETYPE","VARCHAR", "50", "");
		record.putField("RULENAME","VARCHAR", "50", "");
		record.putField("MESSAGE", "VARCHAR", "20", "");
		
		sql = "select * from "+beanSped.getNomeProcesso()+"_mlog where idcapa = '@v1'";
		sql = sql.replace("@v1", CSql.itoc(idcapa));
		
		metaTable = new Table(bean.getSql(), record, sql);
		
		response.setNextJsp(TelaCadLog.telasped_07_log);
		
	}
	
	private void onGetTableLog() throws Exception{
		String json;
	
		response.setHeader("content-type", "text/json; charset=iso-8859-1");
	
		if(metaTable.rc==mensagem.ISUCESSO){
			json = metaTable.getJSONData();
			response.print(json);
			return;
		}
	
		response.print(jsonUtil.doMessage(4, "Erro ao selecionar tabs"));
	}
	
	private void onValid() throws Exception{
		String json;
		Capa capa;
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		if(this.idcapa==0){
			response.print(jsonUtil.doMessage(4, "Informar o id da Capa"));
			return;
		}
		
		capa = this.bean.getCapa();
		capa.validCapa(idcapa);
	
		response.print(jsonUtil.doMessage(0, "Processamento terminado"));
		
	}
	
	private void getVarRequests(){
		
		String sidcapa, sidpai, sid, sregpai, sreg, slevel;
		
		this.action = request.getParameter(ITela.ACTION);
		
		sidcapa     = request.getParameter(TelaSpedTree.IDCAPA);
		sidpai      = request.getParameter(TelaSpedTree.IDPAI);	
		sid         = request.getParameter(TelaSpedTree.ID);
		this.regpai = request.getParameter(TelaSpedTree.REGPAI);
		this.reg    = request.getParameter(TelaSpedTree.REG);
		slevel      = request.getParameter(TelaSpedTree.LEVEL);
		
		//this.idcapa = 0;
		if(_.notNull(sidcapa)){
	    	this.idcapa = CSql.ctoi(sidcapa);
	    }
		
		//this.idpai = 0;
		if(_.notNull(sidpai)){
			this.idpai = CSql.ctoi(sidpai);
	    }
		
		//this.id = 0;
		if(_.notNull(sid)){
			this.id = CSql.ctoi(sid);
	    }
		
		//this.idpai = 0;
		if(_.notNull(slevel)){
			this.ilevel = CSql.ctoi(slevel);
	    }
	}

}
