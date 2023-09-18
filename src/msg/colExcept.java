package msg;

import java.util.Vector;

import utils._;

public class colExcept {

	private static colExcept  mi = new colExcept();
	private static Vector<Exception> mv = new Vector<Exception>();
	
	public static colExcept getInstance(){ return(mi); }
	
	public static void add(Exception ex){ mv.add(ex); }
	
	public static void printMsg(){
		int i;
		Exception ex;
		for(i=0;i<mv.size();i++){
			ex=mv.get(i);
			_.println(ex.toString());
		}
	}
	
}
