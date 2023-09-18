package http.filter;

import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public interface ijsp {
	
	public void post(HttpSession ss, HttpRequest req, HttpResponse out) throws Exception;

}
