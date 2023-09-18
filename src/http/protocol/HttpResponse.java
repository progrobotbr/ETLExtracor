package http.protocol;

import java.io.OutputStream;
import java.util.HashMap;

public class HttpResponse  {
	
	private OutputStream out;
	private HttpHeader head;
	private String nextJsp;
	public HttpResponse(){
		
	}
	
	public HttpResponse(OutputStream pout, HttpHeader phead){
		out = pout;
		head = phead;
	}
	public void setHeader(String key, String value){
		head.setHeader(key, value);
	}
	public void printErrHtml(int number, String s) throws Exception{
		if(s!=null){
			s="<err>"+number+"-"+s+"</err>";
			out.write(s.getBytes());
		}
	}
	public void print(String s) throws Exception {
		if(s!=null){
		  out.write(s.getBytes());
		  //out.write("\r\n".getBytes());
		}
	}
	public void print(int i) throws Exception {
		String s = ""+i;
		out.write(s.getBytes());
	}
	public void print(long l) throws Exception {
		String s = ""+l;
		out.write(s.getBytes());
	}
	public void print(char c) throws Exception {
		String s = ""+c;
		out.write(s.getBytes());
	}
	public void print(double d) throws Exception {
		String s = ""+d;
		out.write(s.getBytes());
	}
	public void print(byte b) throws Exception {
		String s = ""+b;
		out.write(s.getBytes());
	}
	public void print(boolean b) throws Exception {
		String s = ""+b;
		out.write(s.getBytes());
	}
	public void print(byte b[]) throws Exception {
		out.write(b);
	}
	public void flush() throws Exception{
		out.flush();
	}
	public void setNextJsp(String s){
		this.nextJsp=s;
	}
	public String getNextJsp(){
		return(this.nextJsp);
	}
}
