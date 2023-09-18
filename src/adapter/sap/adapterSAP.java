package adapter.sap;

//import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Node;

import adapter.iadapter;
import adapter.bean.beanAdapterSAP;
//import adapter.dblocal.dblConnection;
import adapter.dblocal.localTable;
import adapter.dblocal.tbStruct;
import adapter.sap.connector.sapCall;
//import adapter.sap.connector.sapConnection;
import adapter.util.adapterUtil;
import adapter.util.nodeUtil;
import utils._;
import msg.msg;

public class adapterSAP implements iadapter{

	private beanAdapterSAP    beanAdpSap = null;
	private Vector<String> vH = new Vector<String>();
	private sapCall oSc=null;
	
	public int parse(String pSetName){
		int i;
		Node n;
		
		i = adapterUtil.validNameSet(pSetName);
		if(i!=msg.SUCESSO){
		   return(11);
		}
		beanAdpSap = new beanAdapterSAP();
		beanAdpSap.setSetName(pSetName);
		
		n=adapterUtil.openXml(pSetName);
		if(n==null){
			return(1);
		}
		
		i = beanAdpSap.initConnections();
		if(i>msg.SUCESSO){
			beanAdpSap.end();
			return(i);
		}
				
		if(n.getNodeName().equals(CTBBASES)){
			i=this.ftbbases(n);
			if(i>msg.SUCESSO){
				beanAdpSap.end();
				return(i);
			}
		}
		
		beanAdpSap.end();
		
		return(msg.SUCESSO);
	}
	
	public String getMsg(){
		int i;
		String s=null;
		Exception ex=null;
		s=" T:"+beanAdpSap.getMsgNr()+":"+beanAdpSap.getMsgType()+":"+beanAdpSap.getMsgTxt()+"\r\n";
		i=0;
		while((ex=beanAdpSap.getException(i++))!=null){
			s+=" E:"+ex.toString()+"\r\n";
		}
		return(s);
	}
	
	public int ftbbases(Node n){
		int i=0;
		nodeUtil nu = new nodeUtil(n);
		
		while(nu.getNext()!=null){
			if(nu.isTag(CSETNR)){
				//this.sSetNr = nu.getValue();
			} else if(nu.isTag(CTBBASE)){
				i=this.ftbbase(nu.getNode());
				if(i>msg.SUCESSO){
					return(i);
				}
			}
		}
		
		return(msg.SUCESSO);
	}
	
	public int ftbbase(Node n){
		int iExec=0;
		
		nodeUtil nu = new nodeUtil(n);
		oSc = new sapCall();
		iExec=oSc.init(beanAdpSap);
		
		if(iExec>msg.SUCESSO){
			return(iExec);
		}
		
		while(nu.getNext()!=null){
			if(nu.isTag(CTBNAME)){
				oSc.setTbName(nu.getValue());
				beanAdpSap.setMem(nu.getValue(), oSc);
				//_.log(nu.getName());
				iExec++;
			}else if(nu.isTag(CFIELDS)){
				//this.ffields(nu.getNode());
				this.ffields(nu.getValue());
				iExec++;
			}else if(nu.isTag(CWHERE)){
				this.fwhere(nu.getValue());
				iExec++;
			}else if(nu.isTag(CTBJOIN)){
				iExec = oSc.call();
				if(iExec>msg.SUCESSO){
					return(iExec);
				}
				iExec=0;
				this.ftbjoin(nu.getNode());
			}
			
		}
		if(iExec>msg.SUCESSO){
			iExec = oSc.call();
		}
		return(iExec);
	}
	
	public int ftbjoin(Node n){
		int i, iExec=0;
		String sTbPai;
		nodeUtil nu = new nodeUtil(n);
		
		sTbPai = oSc.getTbName();
		oSc = new sapCall();
		i=oSc.init(beanAdpSap);
		
		if(i>msg.SUCESSO){
			return(i);
		}
		
		oSc.setTbNamePai(sTbPai);
		
		while(nu.getNext()!=null){

			if(nu.isTag(CTBNAME)){
				oSc.setTbName(nu.getValue());
				beanAdpSap.setMem(nu.getValue(), oSc);
				iExec++;
			}else if(nu.isTag(CFIELDS)){
				this.ffields(nu.getValue());
				iExec++;
			}else if(nu.isTag(CKEYS)){
				this.fkeys(nu.getValue());
				iExec++;	
			}else if(nu.isTag(CWHERE)){
				this.fwhere(nu.getValue());
				iExec++;
			}else if(nu.isTag(CJOIN)){
				this.fjoin(nu.getNode());
				iExec++;
			}else if(nu.isTag(CTBJOIN)){
				iExec = oSc.call();
				if(iExec>msg.SUCESSO){
					return(iExec);
				}
				iExec=0;
				this.ftbjoin(nu.getNode());
			}
			
		}
		
		if(iExec>0){
			iExec = oSc.call();
			if(iExec!=msg.SUCESSO){
				return(iExec);
			}
		}
		return(msg.SUCESSO);
	}
	
	public int ffields(String s){
		int i;
		String sf[]=null;
		//nodeUtil nu = new nodeUtil(n);
		//s=nu.getValue();
		if(s==null || s.trim().length()==0){
			return(msg.SUCESSO);
		}
		
		sf = s.split(",");
		for(i=0;i<sf.length;i++){
			oSc.addFieldName(sf[i]);
		}
		/*
		while(nu.getNext()!=null){
			if(nu.isTag(CFIELD)){
				//_.println(nu.getName());
				oSc.addFieldName(nu.getValue());
			}
				
		}
		*/
		
		return(msg.SUCESSO);
	}
	
	public int fkeys(String s){
		int i;
		String sf[]=null;
		if(s==null || s.trim().length()==0){
			return(msg.SUCESSO);
		}
		
		sf = s.split(",");
		for(i=0;i<sf.length;i++){
			oSc.addKeyName(sf[i]);
		}
		return(msg.SUCESSO);
	}
		
	public int fwhere(String s){
		int i;
		String sm[];
		
		//nodeUtil nu = new nodeUtil(n);
		//s=nu.getValue();
		sm = adapterUtil.breakString(512, s);
		
		if(sm!=null){
			s = "( " + s + " )";
			for(i=0;i<sm.length;i++){
				oSc.addWhere(s);
			}
		}
		
		/*
		while(nu.getNext()!=null){
			if(nu.isTag(CCONDW)){
				this.fcondw(nu.getNode());
				_.println(nu.getName());
			}
				
		}
		*/
		return(msg.SUCESSO);
	}

	public int fjoin(Node n){
		nodeUtil nu = new nodeUtil(n);
		
		while(nu.getNext()!=null){

			if(nu.isTag(CTBNAME)){
				oSc.setTbNamePai(nu.getValue());
			}else if(nu.isTag(CCONDJ)){
				fcondjs(nu.getNode());
			}
		}
		
		this.fcondj();
		
		return(msg.SUCESSO);
	}
	
	public int fcondw(Node n){
		String s="",s1="";
		nodeUtil nu = new nodeUtil(n);
		
		while(nu.getNext()!=null){

			if(nu.isTag(CFIELD)){
				s=s1 +nu.getValue();
			}else if(nu.isTag(CVAL)){
				s+=" = "+nu.getValue();
				s1 = " AND ";
				oSc.addWhere(s);
			}
				
		}
		return(msg.SUCESSO);
	}

	public int fcondjs(Node n){ //Tem que ter o for nas chaves do pai
		String sf="",sv="";
		nodeUtil nu = new nodeUtil(n);
		
		while(nu.getNext()!=null){

			if(nu.isTag(CFIELD)){
				sf = nu.getValue();
			}else if(nu.isTag(CFIELD_VALUE)){
				sv = nu.getValue();
				sf+= "," + sv;
				vH.add(sf);
				sf=""; sv="";
			}
				
		}
		return(msg.SUCESSO);
	}
	
	public int fcondj(){ //Tem que ter o for nas chaves do pai
		boolean b=false;
		int i,t;
		String sf="",sv="", sAnd="", sOr = "";
		String st[];
		localTable mLtb;
		tbStruct mLts[];
		int mIdx[];
		String mSfd[];
		sapCall olSc;
		
		olSc = beanAdpSap.getMem(oSc.getTbNomePai());
		mLtb = olSc.getTableData();
		mLts = mLtb.getIdxField();
		mIdx = new int[vH.size()];
		mSfd = new String[vH.size()];
		
		for(i=0;i<vH.size();i++){
			sf = vH.get(i);
			st = sf.split(",");
			sf = st[0];
			sv = st[1];
			mSfd[i] = sf;
			for(t=0; t<mLts.length;t++){
				if(mLts[t].sf.equals(sf)){
					mIdx[i]=t;
					continue;
				}
			}
		}
		  
		sv = "";
		sOr = "";
		
		if(mLtb.getNext()){
			if(!oSc.isWheIsEmpty()){
				oSc.addWhere(" AND ( ");
				b=true;
			}
			do{
				sAnd="";
				sv += sOr + " ( ";		  	
				for(i=0;i<mSfd.length;i++){
					sf = sAnd + " " + mSfd[i] + " = " + mLtb.getFieldString(mIdx[i]);
					sAnd = " AND ";
				}
				sv += sf + " )";
				sOr = " OR ";
				oSc.addWhere(sv);
				sv = "";
			}while(mLtb.getNext());
			if(b==true){
				oSc.addWhere(" ) ");
			}
		}
		
		return(msg.SUCESSO);
	}

}
