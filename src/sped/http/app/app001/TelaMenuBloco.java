package sped.http.app.app001;

import sped.bean.beanSped;
import sped.db.util.where;
import sped.tree.Table;
import sped.tree.capa.Capa;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public class TelaMenuBloco implements ITela{

	private String       action;
	private HttpSession  session;
	private HttpRequest  request;
	private HttpResponse response;
	private beanSped     beansped;
	
	//Eventos
	public static final String ONCREATEMENUBLOCO  = "oncreatemenubloco";
	public static final String ONSELECTED         = "onselected";
	
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception {
		
		this.session = session;
		this.request = request;
		this.response = response;
		this.beansped = (beanSped) session.getObjectValue(AppMain.SPEDBEAN);
		this.action = request.getParameter(ITela.ACTION);
		
		if(this.action.equals(TelaMenuBloco.ONCREATEMENUBLOCO)){
			this.onCreate();
			
		}else if(this.action.equals(TelaMenuBloco.ONSELECTED)){
			this.onSelected();
		}
	}
	
	private void out(String s) throws Exception {
		response.print(s);
	}
	
	public void onCreate() throws Exception{
		Capa capa = beansped.getCapa();
		where ow = new where();
		ow.put(Capa.ID_CAPA, "1");
		capa.selectCapa(ow);
		if(capa.rc!=0){
			throw(new Exception("Erro ao criar Capa"));
		}
		Table tb = capa.getTable();
		
	}
	
	public void onSelected(){
		
	}
	
}
