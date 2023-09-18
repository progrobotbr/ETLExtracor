package sped.db.util;

public class CSql {
	
	public static String conc(String pSql, String scampo, String svalor){
		String s;
		s=pSql.replace(scampo, svalor);
		return(s);
	}
	
	public static String conc(String pSql, String scampo, long svalor){
		String s;
		s=pSql.replace(scampo, (""+svalor).replaceAll("'", "").replaceAll("\"", ""));
		return(s);
	}
	
	public static String valuesql(String value){
		String s=value.replaceAll("'", "").replaceAll("\"", "");
		return(s);
	}
	
	public static String itoc(int i){
		return(""+i);
	}

	public static int ctoi(String s){
		int i=0;
		try{
			i = Integer.parseInt(s);
		}catch(Exception ex){
			
		}
		return(i);
	}
}
