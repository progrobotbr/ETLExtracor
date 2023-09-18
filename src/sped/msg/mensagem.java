package sped.msg;

public class mensagem {
	
	public static int IERRO    = 4;
	public static int ISUCESSO = 0;
	public static final String ERRO    = "E";
	public static final String SUCESSO = "S";
	
	public int       numero=0;
	public String    tipo="";
	public String    mensagem="";
	public String    fonte="";
	public Exception exception=null;

}
