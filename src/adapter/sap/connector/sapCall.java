package adapter.sap.connector;

import java.util.Vector;

import utils._;
import adapter.bean.beanAdapterSAP;
import adapter.dblocal.dblConnection;
import adapter.dblocal.localTable;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.Repository;
import com.sap.mw.jco.JCO.Table;

import msg.msg;

public class sapCall {
	
	private beanAdapterSAP beanAdpSap = null;
	public Table tbfields =null;
	public Table tbdata   =null;
	public Table tboptions=null;
	public Function function  = null;
	private String sTbName="";
	private String sTbNamePai="";
	private localTable tbDataLoc = new localTable();
	private boolean bWhereEmpty=true;
	private Vector<String> vK = new Vector<String>();
	private int qtdRec = 0;
 
	
  public int init(beanAdapterSAP pbeanAdpSap){
	
	try{
		
		beanAdpSap = pbeanAdpSap;
		tbDataLoc.setBeanAdapterSap(pbeanAdpSap);
		function = this.createFunction("RFC_READ_TABLE");
		tbfields = function.getTableParameterList().getTable("FIELDS");
		tboptions = function.getTableParameterList().getTable("OPTIONS");
		tbdata = function.getTableParameterList().getTable("DATA");
		this.setDelimiter("");
		
	}catch(Exception ex){
		_.log(ex.toString());
		pbeanAdpSap.addException(ex);
		return(3);
	}
	
	return(msg.SUCESSO);
	  
  }
	
  public JCO.Function createFunction(String name) throws Exception {
	  try {
	      IFunctionTemplate ft = beanAdpSap.getSapAdapterConnection().getRepository().getFunctionTemplate(name.toUpperCase());
	      if (ft == null){
	         return(null);
	      }
	      return(ft.getFunction());
	  }catch (Exception ex) {
		  beanAdpSap.addException(ex);
	      throw new Exception(msg.getMsg(25));
	  }
  }
  
  public int call(){
	  int i=0;
	  try{
		 function.getImportParameterList().setValue(""+qtdRec,"ROWCOUNT"); 
		 beanAdpSap.getSapAdapterConnection().getConnection().execute(function);
		 tbfields = function.getTableParameterList().getTable("FIELDS");
		 tbdata = function.getTableParameterList().getTable("DATA");
		 tbDataLoc.setDataField(sTbName, tbdata,  tbfields);
		 tbDataLoc.setHeader();
		 i = tbDataLoc.saveDataDb();
		 if(i!=msg.SUCESSO){
			 return(i);
		 }
	  }catch(Exception ex){
		  _.log(ex.toString());
		  beanAdpSap.addException(ex);
		  return(4);
	  }
	  return(msg.SUCESSO);
  }
  
  public void setDelimiter(String s){
	  function.getImportParameterList().setValue(s, "DELIMITER");
  }
  
  public void setTbName(String s){
	  s=s.toUpperCase();
	  sTbName=s;
	  function.getImportParameterList().setValue(s, "QUERY_TABLE");
  }
  
  public String getTbName(){
	  return(sTbName);
  }
  
  public void setTbNamePai(String s){
	  s=s.toUpperCase();
	  sTbNamePai=s;
  }
  
  public String getTbNomePai(){
	  return(sTbNamePai);
  }
  
  public void addFieldName(String s){
	  s=s.toUpperCase();
	  tbfields.appendRow();
	  tbfields.setValue(s, "FIELDNAME");
  }
  
  public void addKeyName(String s){
	  s=s.toUpperCase();
	  vK.add(s);
  }
  
  public void addWhere(String s){
	  s=s.toUpperCase();
	  tboptions.appendRow();
	  tboptions.setValue(s, "TEXT");
	  bWhereEmpty=false;
  }
  
  public boolean isWheIsEmpty(){
	  return(bWhereEmpty);
  }
  
  public localTable getTableData(){
	  return(tbDataLoc);
  }
  
  private void createKeyPoint(){
	  int i,t;
	  String s,sK;
	  for(i=0;i<vK.size();i++){
		  s=vK.get(i);
		  //sk = " ( " + s + " = '" 
		  for(t=i-1;t>=0;t--){
			  
		  }
	  }
  }
  
}
