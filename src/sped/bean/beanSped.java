package sped.bean;

import http.protocol.HttpSession;
import sped.db.sqlLocal;
import sped.db.log.logsql;
import sped.http.app.app001.TelaCadCapa;
import sped.http.app.app001.TelaCadLog;
import sped.http.app.app001.TelaSpedTree;
import sped.msg.mensagens;
import sped.tree.capa.Capa;

public class beanSped {
	
	public static String BEANSPED = "BEANSPED";
	
	private sqlLocal  msqllocal=null;
	private logsql    mlogsql  =null;
	private mensagens mmsgs    =null;
	private Capa      mcapa    =null;
	private TelaCadCapa  mtelacadcapa  = null;
	private TelaSpedTree mtelaspedtree = null;
	private TelaCadLog   mtelacadlog   = null;
	private static String mProcesso = "ecf"; //"spdf"; //ecf
	
	private void initDbLocal(){
		if(msqllocal==null){
			msqllocal = new sqlLocal();
			msqllocal.setBeanSped(this);
		}
	}
	
	private void initLogSql(){
		mlogsql = new logsql();
	}
	
	private void initMsgs(){
		mmsgs = new mensagens();
	}
	
	private void initCapa(){
		mcapa = new Capa(this);
	}
	
	public void init(){
		this.initDbLocal();
		this.initLogSql();
		this.initMsgs();
		this.initCapa();
	}
	
	public logsql getLogSql(){
		return(mlogsql);
	}
	
	public sqlLocal getSql(){
		return(msqllocal);
	}
	
	public mensagens getMensagens(){
		return(mmsgs);
	}
	
	public Capa getCapa(){
		return(mcapa);
	}
	
	public static beanSped api(HttpSession session){
		
		beanSped bs = (beanSped) session.getObjectValue(beanSped.BEANSPED);
		if(bs==null){
			bs = new beanSped();
			bs.init();
			session.setObjectValue(beanSped.BEANSPED, bs);
		}
		return(bs);
		
	}
	
	public void setTelaCadCapa(TelaCadCapa t){
		this.mtelacadcapa = t;
	}
	
	public TelaCadCapa getTelaCadCapa(){
		return(this.mtelacadcapa);
	}
	
	public void setTelaSpedTree(TelaSpedTree t){
		this.mtelaspedtree = t;
	}
	
	public TelaSpedTree getTelaSpedTree(){
		return(this.mtelaspedtree);
	}
	
	public void setTelaCadLog(TelaCadLog t){
		this.mtelacadlog = t;
	}
	
	public TelaCadLog getTelaCadLog(){
		return(this.mtelacadlog);
	}
	
	public static String getNomeProcesso(){
		return(beanSped.mProcesso);
	}
	
	

}
