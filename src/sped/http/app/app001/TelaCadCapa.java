package sped.http.app.app001;

import sped.bean.beanSped;
import sped.db.util.where;
import sped.msg.mensagem;
import sped.tree.Table;
import utils._;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public class TelaCadCapa implements ITela{

	private String action="";
	private static final String ONLIST    = "onlist";
	private static final String ONINSERT  = "oninsert";
	private static final String ONSELECT  = "onselect";
	private static final String ONGETCAPA = "ongetcapa";
	private static final String ONCLEAR   = "onclear";
	
	private int idcapa = -1;
	private String JSON_QUERY = "";
	private String JSON_QUERY_TPL = "{'id':\"@v1\",'descricao': \"@v2\", 'datai': \"@v3\", 'dataf': \"@v4\", 'rc':\"@v5\", 'msg':\"@v6\"}";
	
	
	private HttpSession session;
	private HttpRequest request;
	private HttpResponse response;
	
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception {

		this.session = session;
		this.request = request;
		this.response = response;
		
		this.action = request.getParameter(ITela.ACTION);
		
		beanSped bean = beanSped.api(session);
		bean.getSql().beginTransaction();
		
		try{
			
			if(this.action.equals(TelaCadCapa.ONLIST)){
				this.onList();
			}else if(this.action.equals(TelaCadCapa.ONINSERT)){
				this.onInsert();
			}else if(this.action.equals(TelaCadCapa.ONSELECT)){
				this.onSelect();
			}else if(this.action.equals(TelaCadCapa.ONGETCAPA)){
				this.onGetCapa();
			}else if(this.action.equals(TelaCadCapa.ONCLEAR)){
				this.onClear();
			}
			
			bean.getSql().commit();
			
		}catch(Exception ex){
			bean.getSql().rollback();
		}
	}
	
	private void onClear() throws Exception{
		this.idcapa = -1;
		this.JSON_QUERY = "";
	}
	
	private void onGetCapa() throws Exception{
		response.setHeader("content-type", "text/html");
		if(this.idcapa!=-1){
			response.print(this.JSON_QUERY);
		}else{
			response.print("{ 'rc':'4', 'msg':'Erro'}");
		}
	}
	
	private void onSelect() throws Exception{
		
		response.setHeader("content-type", "text/html");
		
		this.JSON_QUERY = "";
		this.idcapa = -1;
		
		String json_body = this.JSON_QUERY_TPL;
		String sid;
		where pw = new where();
		Table tb = null;
		
		sped.bean.beanSped ap = sped.bean.beanSped.api(session);
		
		sid    = request.getParameter("did");
		if(_.notEmpty(sid)){
			pw.put("idcapa", sid);
			ap.getCapa().selectCapa(pw);
			tb = ap.getCapa().getTable();
			tb.initIterator();
			if(tb.hasNext()){
				tb.moveNext();
				this.idcapa = tb.getFieldByNameInt("IDCAPA");
				json_body = json_body.replace("@v1", tb.getFieldByNameString("IDCAPA"));
				json_body = json_body.replace("@v2", tb.getFieldByNameString("DESCRICAO"));
				json_body = json_body.replace("@v3", tb.getFieldByNameString("DT_INICIO"));
				json_body = json_body.replace("@v4", tb.getFieldByNameString("DT_FIM"));
				json_body = json_body.replace("@v5", "0");
				json_body = json_body.replace("@v6", "Sucesso ao Selecionar Dados");
				this.JSON_QUERY = json_body;
			}
		}
		json_body = json_body.replace("@v5", "4");
		json_body = json_body.replace("@v6", "Erro");
		
		response.print(json_body);
		
	}
	
	private void onInsert() throws Exception{
		
		response.setHeader("content-type", "text/html");
		
		String json_body = "{'id':\"@v1\",'rc':\"@v2\", 'msg':\"@v3\"}";
		int iid;
		String sid, sdescr, sdatai, sdataf;
		
		sped.bean.beanSped ap = sped.bean.beanSped.api(session);
		
		sid    = request.getParameter("did");
		sdescr = request.getParameter("ddescr");
		sdatai = request.getParameter("ddatai");
		sdataf = request.getParameter("ddataf");
		
		if(_.notEmpty(sid)){
			iid=Integer.parseInt(sid);
			ap.getCapa().changeCapa(iid, sdescr, sdatai, sdataf);
		}else{
			iid = ap.getCapa().createCapa(sdescr, sdatai, sdataf);
		}
		
		if(ap.getCapa().rc!=mensagem.ISUCESSO){
			json_body = json_body.replace("@v1","");
			json_body = json_body.replace("@v2",""+ap.getCapa().rc);
			json_body = json_body.replace("@v3","erro ao atualizar");
		}else{
			json_body = json_body.replace("@v1",""+iid);
			json_body = json_body.replace("@v2",""+ap.getCapa().rc);
			json_body = json_body.replace("@v3","Sucesso ao Gravar o Registro");
		}
		response.print(json_body);
	}
	
	private void onList() throws Exception{
		
		String json_body = "{\"total\":0,\"rows\":[ @V1 ]}";
		String json_row =  "{\"idcapa\":\"@V1\", \"descricao\":\"@V2\", \"datai\":\"@V3\", \"dataf\":\"@V4\"}";
		String jlin ="";
		String jlins="";
		String jbod="";
		String virg="";

		response.setHeader("content-type", "text/json");
		
		sped.tree.Table tb;
		sped.bean.beanSped ap = sped.bean.beanSped.api(session);
		ap.getCapa().selectCapa(null);

		if(ap.getCapa().rc==0){
			tb = ap.getCapa().getTable();
			tb.initIterator();
			while(tb.hasNext()){
				tb.moveNext();
				jlin = json_row.replace("@V1",tb.getFieldByNameString("IDCAPA"));
				jlin = jlin.replace("@V2",tb.getFieldByNameString("DESCRICAO"));
				jlin = jlin.replace("@V3",tb.getFieldByNameString("DT_INICIO"));
				jlin = jlin.replace("@V4",tb.getFieldByNameString("DT_FIM"));
				jlins+=virg+jlin;
				virg=",";
			}
			jbod = json_body.replace("@V1",jlins);
		}else{
			jbod = "teste";
		}
		
		response.print(jbod);
		
	}

}
