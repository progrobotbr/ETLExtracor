package adapter.sap;

import java.util.HashMap;
import java.util.Vector;

import adapter.dblocal.tbStruct;

import com.sap.mw.jco.JCO.Table;

public class saputil {
	
	public String data   =null;
	private Vector <String> vK = null;
	private HashMap<String, tbStruct> hd = new HashMap<String,tbStruct>();
	
	private void createKeyPoint(){
		int i,t,tam;
		String s, sk="", sk1="", sk2="", sCd, sOr="";
		tam=vK.size();
		for(i=0;i<tam;i++){
			sCd=" = ";
			sk2= " ( ";
			for(t=0;t<tam-i;t++){
				s=vK.get(t);
				sk2+= s + sCd + "'" + getFieldValue(s) +"'";
				sCd = " > ";
			}
			sk2+= " ) ";
			sk1 = sk2 + sOr + sk1;
			sOr = " OR ";
		}
	}
	
	public String getFieldValue(String pName){
		String s;
		tbStruct ts=hd.get(pName);
		s=data.substring(ts.pos, ts.pos+ts.tam);
		return(s);
	}

}
