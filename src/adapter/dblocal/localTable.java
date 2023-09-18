package adapter.dblocal;

import java.util.HashMap;

import msg.msg;
import adapter.bean.beanAdapterDB;
import adapter.bean.beanAdapterSAP;

import com.sap.mw.jco.JCO.Table;

public class localTable {
	
	private beanAdapterSAP beanAdpSap = null;
	private Table tData;
	private Table tField;
	private HashMap<String, tbStruct> hd = new HashMap<String,tbStruct>();
	private tbStruct hda[];
	private boolean bFirstRead=true;
	public String sTbName="";
	private String sSetName="";
	private dblConnection odblConn=null;
	
	public void setBeanAdapterSap(beanAdapterSAP pbeanAdp){
		beanAdpSap = pbeanAdp;
	}
	public void setDataField(String sTableName,Table tb,Table tf){
		tData = tb;
		tField = tf;
		sTbName = sTableName;
		tData.firstRow();
		tField.firstRow();
	}
	
	public boolean getNext(){
		boolean b=false;
		
		if(tData.isEmpty()){
			return(b);
		}
		if(bFirstRead==false){
			b=tData.nextRow();
		}else{
			tData.firstRow();
			bFirstRead=false;
			b=true;
		}
		return(b);
	}
		
	public void setHeader(){
		int i=0;
		tbStruct otb;
		
		tField.firstRow();
		i=tField.getNumRows();
		hda = new tbStruct[i];
		i=0;
		
		do{
			otb = new tbStruct();
			otb.pos = Integer.parseInt(tField.getString("OFFSET"));
			otb.tam = Integer.parseInt(tField.getString("LENGTH"));
			otb.idx=i;
			otb.sf = tField.getString("FIELDNAME");
			hd.put(otb.sf, otb);
			hda[i]=otb;
			i++;
		}while(tField.nextRow());
	}
	
	public String getFieldString(int i){
		String s="";
		tbStruct tb = hda[i];
		s=tData.getString("WA");
		s=s.substring(tb.pos, tb.pos+tb.tam);
		return(s);
	}
	
	public tbStruct[] getIdxField(){
		return(hda);
	}
	
	public int saveDataDb(){
		int i;
		dblCall oDbl = new dblCall();
		oDbl.setBeanAdapterSap(beanAdpSap);
		i = oDbl.createTable(sTbName, hda);
		i = oDbl.createSqlInsert(sTbName, hda);
		i = oDbl.moveInsert(tData, hda);
		i = oDbl.execInsert();
		if(i > msg.SUCESSO){
			oDbl.rollback();
		}else{
			i = oDbl.commit();
		}
		oDbl.closeStmt();
		return(i);
	}
	
	public void setDblConnection(dblConnection odb){
		odblConn = odb;
	}
	
	public void setSetName(String s){
		sSetName = s;
	}

}
