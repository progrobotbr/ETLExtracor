package sped.http.app.app001;

import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public class TelaTabelas implements ITela{

	private String       action;
	private HttpSession  session;
	private HttpRequest  request;
	private HttpResponse response;
	
	//Eventos
	public static final String ONCREATETELA  = "oncreatetela";
	public static final String ONLINHA       = "onlinha";
	public static final String ONPAGPRIMEIRA = "onpagprimeira";
	public static final String ONINSERIR     = "oninserir";
	
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception {
		this.session = session;
		this.request = request;
		this.response = response;
		
		this.action = request.getParameter(ITela.ACTION);
		
		if(this.action.equals(TelaTabelas.ONCREATETELA)){
			this.onCreateTela();
			
		}else if(this.action.equals(TelaTabelas.ONLINHA)){
			this.onLinha();
			
		}else if(this.action.equals(TelaTabelas.ONPAGPRIMEIRA)){
			this.onPagPrimeira();
			
		}else if(this.action.equals(TelaTabelas.ONINSERIR)){
			this.onInserir();
		}
	}
	
	private void out(String s) throws Exception {
		response.print(s);
	}
	
	private void onCreateTela(){
		
	}
	
	private void onLinha(){
		
	}
	
	private void onPagPrimeira(){
		
	}
	
	private void onPagAnterior(){
		
	}
	
	private void onPagPosterior(){
		
	}
	
	private void onPAgUltima(){
		
	}
	
	private void onInserir() throws Exception{
		TelaForm tf = new TelaForm();
		request.setParameter(ITela.ACTION, TelaForm.ONCREATEFORM);
		request.setParameter("key","valor");
		request.setParameter("key","valor");
		tf.post(session, request, response);
	}
	
}
