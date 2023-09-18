package sped.http.app.app001;

import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public interface ITela {
	
	public static final String ACTION = "action";
	public static final String TELA   = "tela";
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception;
	
}
