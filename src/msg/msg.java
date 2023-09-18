package msg;

public class msg {
	
	public static final int SUCESSO = 0;
	public static final int ERRO = 4;
	
	public static final String M[] = { 	/* 0*/"ok", 
										/* 1*/"Erro ao abrir xml set",
										/* 2*/"Erro ao abrir banco local",
										/* 3*/"Erro ao inicializar tabela da rfc",
										/* 4*/"Erro ao executar rfc",
										/* 5*/"Erro ao conectar o Sap",
										/* 6*/"Não foi possível escalonar job",
										/* 7*/"Data com formato inválido",
										/* 8*/"Erro ao gravar dados no banco",
										/* 9*/"Início",
										/*10*/"não há job agendado",
										/*11*/"Informar nr. set [9999]",
										/*12*/"Job em execucao. Nao e possivel exclusao",
										/*13*/"Job nao encontrado",
										/*14*/"Sap: Erro ao criar pool de conexão",
										/*15*/"Erro: tipo de adapter não cadastrado",
										/*16*/"Tabela não existente", 
										/*17*/"Erro ao gravar dados em tabela local",
										/*18*/"Erro ao gravar dados em tabela local (pegar metadata)",
										/*19*/"Erro ao gravar dados em tabela local (pegar metadata)",
										/*20*/"Erro ao abrir banco de origem",
										/*21*/"Erro ao criar comando insert no banco",
										/*22*/"Erro ao mover dados no insert",
										/*23*/"Erro ao executar commit no banco",
										/*24*/"Erro ao criar tabela",
										/*25*/"Erro ao criar rfc",
										/*26*/"Erro ao abrir config.ini"};
	
	public static String getMsg(int i){
		return(M[i]);
	}
}
