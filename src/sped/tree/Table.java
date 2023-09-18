package sped.tree;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import sped.db.sqlLocal;
import sped.db.util.CSql;
import sped.http.app.app001.datagrid.datagrid;
import sped.msg.mensagem;
import sped.tree.capa.bloco.record.Record;
import utils._;

public class Table {

	public static final int BEFORFIRST = -1;
	public int rc;
	public String nome="";
	public Record record=null;
	public int colcount=Table.BEFORFIRST;
	public int rowcount=Table.BEFORFIRST;
	private int pointer=0;
	private HashMap<String,Integer> hfields=new HashMap<String,Integer>();
	
	
	private ArrayList <String[]> data=null;
	
	/*
	public Table(String nome, Record record, ArrayList <String[]>data, int colcount, int rowcount){
		this.nome = nome;
		this.record = record;
		this.data = data;
		this.colcount = colcount;
		this.rowcount = rowcount;
	}
	*/
	
	public Table(Record precord, ResultSet prs){
		int i;
		String sdata[]=null, skey;
		Integer value;
		Set <String>set;
		Iterator <String>it;
		
		rc=mensagem.IERRO;
		try{
			this.nome = precord.getReg();
			this.record = precord;
			
			//set = precord.getKeySet();
			//it = set.iterator();
			it = precord.getKeySetList();
			this.colcount=Table.BEFORFIRST;
			while(it.hasNext()){
				this.colcount++;
				skey = it.next();
				value = new Integer(this.colcount);
				hfields.put(skey, value);
			}
			
			this.data=new ArrayList<String[]>();
			//this.colcount=prs.getMetaData().getColumnCount();
			this.rowcount=Table.BEFORFIRST;
			while(prs.next()){
				this.rowcount++;
				sdata = new String[this.colcount+1];
				for(i=1;i<=this.colcount+1;i++){
					sdata[i-1]=prs.getString(i);
				}
				data.add(sdata);
				rc=mensagem.ISUCESSO;
			}
		}catch(Exception ex){
			rc=mensagem.IERRO;
		}
		i=13;
	}
	
	//Versão 2
	public Table(sqlLocal psql, Record precord, String pSql){
		int i;
		String sdata[]=null, skey;
		ResultSet prs;
		Integer value;
		Set <String>set;
		Iterator <String>it ;
		
		rc=mensagem.IERRO;
		
		try{
			
			set = precord.getKeySet();
			it = set.iterator();
			this.colcount=Table.BEFORFIRST;
			while(it.hasNext()){
				this.colcount++;
				skey = it.next();
				value = new Integer(precord.getFieldPosition(skey));
				hfields.put(skey, value);
			}
			
			this.nome = precord.getReg();
			this.record = precord;
			prs = psql.select2(Record.MAXFECHSIZE, 100000, pSql);
			this.data=new ArrayList<String[]>();
			//this.colcount=prs.getMetaData().getColumnCount();
			this.rowcount=Table.BEFORFIRST;
			while(prs.next()){
				this.rowcount++;
				sdata = new String[this.colcount+1];
				for(i=0;i<this.colcount+1;i++){
					sdata[i]=prs.getString(i+1);
				}
				data.add(sdata);
				rc=mensagem.ISUCESSO;
			}
			prs.close();
		}catch(Exception ex){
			rc=mensagem.IERRO;
		}
	}
	
	private String getFieldByName(String nome){
		String[] sdata;
		String svalue;
		if(data==null){
			return("");
		}
		try{
			if(hfields.containsKey(nome)){
				Integer integer = hfields.get(nome);
				sdata = data.get(pointer);
				svalue = sdata[integer.intValue()];
				if(svalue==null){
					svalue="";
				}
				return(svalue);
			}
		}catch(Exception ex){
			_.println("Erro dentro de Table.getFieldByName "+ pointer + " "+nome);
		}
		return("");
	}
	
	public String getFieldByNameString(String nome){
		return(this.getFieldByName(nome));
	}
	
	public int getFieldByNameInt(String nome){
		int i;
		String s = this.getFieldByName(nome);
		if(_.notEmpty(s)){
			i = new Integer(s);
			return(i);
		}
		return(0);
	}
	
	public void setIndex(int index){
		if(index < this.rowcount){
			pointer = index;
			rc=mensagem.ISUCESSO;
		}else{
			rc=mensagem.IERRO;
		}
	}
	public void initIterator(){
		pointer = Table.BEFORFIRST;
	}
	
	public void moveNext(){
		pointer++;
	}
	
	public boolean hasNext(){
		if(pointer<this.rowcount){
			return(true);
		}else{
			return(false);
		}
			
	}
	
	//Opçoes datagrid
	public String getHTMLMeta(String dtsource, 
			                  String funccriar, 
			                  String funclist,
			                  String funcup,
			                  int level, 
			                  int idcapa, 
                              int idpai, 
                              int id, 
                              String reg, 
                              String regpai){
		String s="", key;
		datagrid dg = new datagrid();
		Field f;
		/////Set<String> fieldkeys = record.getKeySet();
		Iterator <String> iter = record.getIterator(); /////fieldkeys.iterator();
		//Iterator <String>iter = record.getKeySetList();
		
		while(iter.hasNext()){
			key = iter.next();
			f = record.getFieldDefinition(key);
			dg.putTitle(f.Nome,f.Nome, f.Tamanho);
		}
		s = dg.getDataGrid(record.getRegDescricao(), 
				           record.getReg(), 
				           funccriar, 
				           funclist, 
				           funcup,
				           "1000", 
				           "300",  
				           "telaspedtree", 
				           dtsource,
				           level, 
				           idcapa, 
				           idpai, 
				           id, 
				           reg, 
				           regpai);
		return(s);
	}
	
	public String getHTMLMeta2(String dtsource, 
            String funccriar, 
            String funclist,
            String funcup,
            int level, 
            int idcapa, 
            int idpai, 
            int id, 
            String reg, 
            String regpai){

		String s="", key;
		datagrid dg = new datagrid();
		Field f;
		//Set<String> fieldkeys = record.getKeySet();
		//Iterator <String> iter = fieldkeys.iterator();
		Iterator <String>iter = record.getKeySetList();
		while(iter.hasNext()){
			key = iter.next();
			f = record.getFieldDefinition(key);
			dg.putTitle(f.Nome,f.Descr_Curta, f.Tamanho_Tela);
		}
		s = dg.getDataGrid(record.getRegDescricao(), 
				record.getReg(), 
				funccriar, 
				funclist, 
				funcup,
				"1000", 
				"300",  
				"telaspedtree", 
				dtsource,
				level, 
				idcapa, 
				idpai, 
				id, 
				reg, 
				regpai);
		return(s);
	}
	
	public String getJSONData(){
		String s,key;
		datagrid dg = new datagrid();
		
		this.initIterator();
		
		while(this.hasNext()){
			this.moveNext();
			
			Set<String> fieldkeys = record.getKeySet();
			dg.initRecord();
			Iterator <String> iter = fieldkeys.iterator();
			while(iter.hasNext()){
				key = iter.next();
				dg.putField(key, this.getFieldByName(key));
			}
			dg.endRecord();
			
		}
		
		s = dg.getJson();
		return(s);
	}
	
	public String getJSONData2(){
		String s,key;
		datagrid dg = new datagrid();
	    int i=0;//RENATO
		this.initIterator();
		
		while(this.hasNext()){
			this.moveNext();
			
			//Set<String> fieldkeys = record.getKeySet();
			dg.initRecord();
			Iterator <String> iter = record.getKeySetList();
			while(iter.hasNext()){
				key = iter.next();
				dg.putField(key, this.getFieldByName(key));
			}
			dg.endRecord();
			i++;
			if(i>10000){
				break;
			}
			
		}
		
		s = dg.getJson();
		return(s);
	}
	
}
