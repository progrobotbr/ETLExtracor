package sped.msg;

import java.util.ArrayList;
import java.util.Iterator;

public class mensagens {

	private ArrayList<mensagem> mmsgs=new ArrayList<mensagem>();
	
	public mensagem criarMensagem(String fonte, int numero, String tipo, String mensagem){
		mensagem mm = new mensagem();
		mm.fonte=fonte;
		mm.numero=numero;
		mm.tipo=tipo;
		mm.mensagem=mensagem;
		mmsgs.add(mm);
		return(mm);
	}
	
	public void limparMensagens(){
		mmsgs=new ArrayList<mensagem>();
	}
	
	public Iterator<mensagem> getIterator(){
		Iterator<mensagem> it = mmsgs.iterator();
		return(it);
	}
	
}
