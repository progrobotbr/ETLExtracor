package sped.http.app.app001;

import sped.bean.beanSped;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public class AppMain {
	
	public static final String SPEDBEAN  = "SPEDBEAN";
	private static final String CADCAPA  = "telacadcapa";
	private static final String SPEDTREE = "telaspedtree";
	private static final String CADLOG   = "telacadlog";
	
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception {
		
		
		beanSped bean = beanSped.api(session);
		
		String tela = request.getParameter(ITela.TELA);
		
		if(tela.equals(AppMain.CADCAPA)){
			TelaCadCapa telacadcapa = bean.getTelaCadCapa();
			if(telacadcapa==null){
				telacadcapa = new TelaCadCapa();
				bean.setTelaCadCapa(telacadcapa);
			}
			telacadcapa.post(session, request, response);
			
		}else if(tela.equals(AppMain.SPEDTREE)){
			TelaSpedTree telaspedtree = bean.getTelaSpedTree();
			if(telaspedtree==null){
				telaspedtree = new TelaSpedTree();
				bean.setTelaSpedTree(telaspedtree);
			}
			telaspedtree.post(session, request, response);
			
		}else if(tela.equals(AppMain.CADLOG)){
			TelaCadLog telacadlog = bean.getTelaCadLog();
			if(telacadlog==null){
				telacadlog = new TelaCadLog();
				bean.setTelaCadLog(telacadlog);
			}
			telacadlog.post(session, request, response);
		}
		
	}
	
	
}
