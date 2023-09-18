package sped.tree.capa.bloco;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import sped.bean.beanSped;
import sped.constants.IRegs;
import sped.db.sqlLocal;
import sped.db.util.CSql;
import sped.http.app.app001.RegKey;
import sped.http.app.app001.datagrid.datagrid;
//import sped.db.util.where;
import sped.msg.mensagem;
import sped.tree.Field;
import sped.tree.Table;
import sped.tree.createIds;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.base.occurUtils;
import utils._;

//2o nível
public class Bloco {

	public int rc;
	private String regpai = "CAPA";
	private beanSped mbeansped=null;
	private sqlLocal msql=null;;
	private String mblsele = "select reg,regpai from " + beanSped.getNomeProcesso()+"_b001 where regpai = '@V1'";
	private String mblinse = "insert into " + beanSped.getNomeProcesso()+"_mblocos (idcapa, reg, id) values ('@V1','@V2','@V3')";
	private String mselect = "select a.reg, a.idcapa, a.id, b.descricao from " + beanSped.getNomeProcesso()+"_mblocos as a left outer join ecf_b001 as b on a.reg = b.reg  where idcapa = '@V1' ";
	
	private Table mTable=null;
	
	private Record mRecord=null; //3 nível
	private Record mRecordTemp=null;
	
	public Bloco(beanSped pbeansped){
		mbeansped=pbeansped;
		msql = mbeansped.getSql();
	}
	
	public void createBloco(long id_capa){
		long id;
		String ssql = this.mblsele;
		String ssql2 = this.mblinse;
		rc=4;
		
		createIds ocrid = new createIds(msql);
		ssql = CSql.conc(ssql, "@V1", IRegs.CAPA);
		
		try{
			ResultSet rs = msql.select(ssql);
			while(rs.next()){
				id = ocrid.createId(id_capa, rs.getString("REG"));
			    if(ocrid.rc!=0){
			    	rc=4;
			    	return;
			    }
			    ssql2 = CSql.conc(ssql2, "@V1", id_capa);
			    ssql2 = CSql.conc(ssql2, "@V2", rs.getString("REG"));
			    ssql2 = CSql.conc(ssql2, "@V3", id);
			    
			    rc = msql.change(ssql2);
			    
			    if(rc!=0){
			    	return;
			    }
			    
			}
		}catch(Exception ex){
			_.log(ex.toString());
			ex.printStackTrace();
		}
	}
	
	public void selectBloco(int pidcapa){
		String ssql = this.mselect;
		Record record;
		
		rc = mensagem.IERRO;
		
		record = new Record(this.mbeansped, "capa");
		record.putField("REG"   ,    "VARCHAR", "60", "");
		record.putField("IDCAPA",    "INTEGER", "60", "");
		record.putField("ID",        "INTEGER", "50", "");
		record.putField("DESCRICAO", "VARCHAR", "500", "");
		
		ssql = CSql.conc(ssql, "@V1",pidcapa);
		mTable=new Table(msql, record, ssql);
		
		rc = mTable.rc;
	}
	
	public void selectRecord(int pindex) throws Exception{
		int idcapa, idpai, nivel, id;
		String reg, regpai;
		mTable.setIndex(pindex);
		if(mTable.rc==mensagem.ISUCESSO){
			nivel  = 0;
			idcapa = mTable.getFieldByNameInt("IDCAPA");
			idpai  = mTable.getFieldByNameInt("IDCAPA");
			id     = mTable.getFieldByNameInt("ID");
			reg    = mTable.getFieldByNameString("REG");
			regpai = "CAPA";
			this.mRecord = new Record(mbeansped, nivel, idcapa, regpai, idpai, reg, id);
		}
		
	}
	
	public String getHTMLMetaBloco(String funccriar, String funclist, String funcup, int level, int idcapa, int idpai, int id, String regpai, String reg){
		String s;
		s = mTable.getHTMLMeta("ongettabledatabloco", funccriar, funclist, funcup, level, idcapa, idpai, id,  regpai, reg);
		return(s);
	}
	
	public Table getHTMLMetaFilho(String funccriar, String funclist, int level, int idcapa, int idpai, int id, String regpai, String reg) throws Exception{
		String s;
		Record record;
		Table table;
		record = new Record(mbeansped, level, idcapa, regpai, idpai, reg, id);
		table = record.selectVariosDoPai2(2000);
		return(table);
	}
	
	public Table getHTMLMetaTabFilho(RegKey regKey) throws Exception{
		String swhere="", sOr="";
		String ssql = "select regpai, reg, descricao from " + beanSped.getNomeProcesso()+"_b001 where regpai = '@v1'";
		Record record;
		Table table;
		occurUtils oc;
		ArrayList<String>regs;
		
		rc = mensagem.IERRO;
		
		oc = new occurUtils(this.mbeansped, regKey);
		regs = oc.getRegFilhosAoInserir(regKey);
		for(String reg: regs){
			swhere+= sOr +" reg = '"+reg+"'" ;
			sOr=" or ";
		}
		
		ssql = ssql.replace("@v1", regKey.reg);
		if(!swhere.isEmpty()){
			ssql+=" and ( "+swhere+" )";
		}else{
			ssql+=" and ( reg = '____' )"; //para não trazer nada
		}
		
		record = new Record(this.mbeansped, regKey.reg);
		record.putField("REGPAI"   ,"VARCHAR",  "80", "");
		record.putField("REG"      ,"VARCHAR",  "80", "");
		record.putField("DESCRICAO","VARCHAR", "300", "");
		
		//ssql = CSql.conc(ssql, "@v1",regpai);
		table=new Table(msql, record, ssql);
		
		rc = table.rc;
		return(table);
		
	}
	
	public String getHTMLMetaForm(int idcapa, int idpai, int id, String regpai, String reg) throws Exception{
		
		int i=1;
		String ssql = "", skey;
		Field field = null;
		Table table   = null;
		Iterator <String>iterator;
		StringBuffer sbf = new StringBuffer();
		StringBuffer sbj = new StringBuffer();
		String ss, ssqldescr = "select descricao from " + beanSped.getNomeProcesso()+"_b001 where reg='@v1' and regpai = '@v2'", sdescrreg="";
		String virg="";
		String jsfields="["; //Cria um array no javascript que é utilizado clearErrForm para limpar a mensagem de erro
		
		mRecordTemp = null;
		
		//seleciona deescricao
		ssqldescr = ssqldescr.replace("@v1", reg).replace("@v2", regpai);
		ResultSet rs = msql.select(ssqldescr);
		if(rs.next()){
			sdescrreg = rs.getString("DESCRICAO");
		}
		
		sbf.append("<div id='msg'></div>");
		sbf.append("<table border='0'>");
		sbf.append("<tr><td colspan='3'><h1>"+reg+" - "+sdescrreg+"</h1></td></tr>");
		mRecordTemp = new Record(mbeansped, 0, idcapa, regpai, idpai, reg, id);
		if(id>0){
			table = new Table(this.msql, mRecordTemp, ssql);
		}
		
		iterator = mRecordTemp.getIterator();
		while(iterator.hasNext()){
			skey = iterator.next();
			if(_.notEmpty(skey)){
				if(skey.equals("IDCAPA") ||
				   skey.equals("ID_PAI") || 
				   skey.equals("ID")     ||
				   skey.equals("REGPAI") ||
				   skey.equals("REG")){
					continue;
				}
			}
			field = mRecordTemp.getFieldDefinition(skey);
			this.formPutField(i, sbf, sbj, field, table);
			jsfields+=virg+"'#"+field.Nome+"msg'";
			virg=",";
			i++;
		}
		
		jsfields+="]";
		jsfields = "var LFIELDS = "+jsfields+";\n";
				
		sbf.append("</table>");
		sbf.append("<div id='msgfrmbot'></div>");
		sbf.append("<br><a href='#' id='execfrm' onclick='criarnovofrm()'>Gravar</a>");
		sbf.append("<a href='#' id='voltarfrm' onclick='gobackfrm()'>Voltar</a>");
		sbf.append("<a href='#' id='validfrm' onclick='validfrm()'>Validar</a><br>");
		sbf.append("\n");
		sbf.append("\n<script language='javascript'>\n");
		
		//
		// LFIELDS - Variável com todos os campos para limpeza do erro
		//
		sbf.append(jsfields);
		
		//
		// localID - quando for igual a 0 indica que é CRIAÇÂO maior que 0 indica que é ALTERAÇÂO 
		//
		sbf.append("\nvar localID="+id+";\n");
		
		//
		// Coloca botões
		//
		sbf.append("\n$('#execfrm').linkbutton({iconCls:'icon-add',width:'110px'});");
		sbf.append("\n$('#voltarfrm').linkbutton({iconCls:'icon-back',width:'110px'});");
		sbf.append("\n$('#validfrm').linkbutton({iconCls:'icon-add',width:'110px'});");
		sbf.append("\nfunction criarnovofrm(){\n");
		sbf.append("\n	$('#msg').css('color','blue');");
		sbf.append("\n	$('#msg').html('');");
		sbf.append("\n	$('#msgfrmbot').css('color','blue');");
		sbf.append("\n	$('#msgfrmbot').html('');");
		
		ss = "{ tela: 'telaspedtree', action: 'onsaveform' , idcapa: '"+idcapa+"',idpai:'"+idpai+"',id: localID,reg:'"+reg+"',regpai:'"+regpai+"'," + sbj.toString() + " }";
		
		sbf.append("\n$.post('sped_crtl.jsp',"+ss+").done(function(data){\n");
		sbf.append("  var obj = eval('('+data+')')\n");
		sbf.append("  localID = obj.id;\n");

		sbf.append("\nif(obj.rc=='0'){");
		sbf.append("\n	$('#msg').css('color','blue');");
		sbf.append("\n	$('#msg').html(obj.msg);");
		sbf.append("\n	$('#msgfrmbot').css('color','blue');");
		sbf.append("\n	$('#msgfrmbot').html(obj.msg);");
		
		sbf.append("\n}else{");
		sbf.append("\n	$('#msg').css('color','red');");
		sbf.append("\n	$('#msg').html(obj.msg);");
		sbf.append("\n	$('#msgfrmbot').css('color','red');");
		sbf.append("\n	$('#msgfrmbot').html(obj.msg);");
		
		sbf.append("\n}");
		sbf.append("\n});}\n");
		
		ss = "{ tela: 'telaspedtree', action: 'onvalidform' , idcapa: '"+idcapa+"',idpai:'"+idpai+"',id: localID,reg:'"+reg+"',regpai:'"+regpai+"'," + sbj.toString() + " }";
		sbf.append("\nfunction validfrm(){\n");
		sbf.append("\n$.post('sped_crtl.jsp',"+ss+").done(function(data){\n");
		sbf.append("  var obj = eval('('+data+')')\n");
		sbf.append("\n  obj.msg = replaceAll(obj.msg, '@1','script');");
		sbf.append("\n  obj.msg = replaceAll(obj.msg, '@2','\"');"); 
		sbf.append("\n	$('#msg').css('color','red');");
		sbf.append("\n	$('#msg').html(obj.msg);");
		sbf.append("\n	$('#msgfrmbot').css('color','red');");
		sbf.append("\n	$('#msgfrmbot').html(obj.msg);");
		//sbf.append("\n}");
		sbf.append("\n});}\n");
		
		
		sbf.append("\nfunction gobackfrm(){\n");
		sbf.append("\n$('#form').hide();\n");
		sbf.append("\n$('#form').html('');\n");
		sbf.append("\n$('#tree').show();\n");
		sbf.append("\nlocation.href='#bottom';\n");
		sbf.append("\n}\n");
		sbf.append("\n</script>");
		
		return(sbf.toString());
		
	}
	
	private void formPutField(int pseq, StringBuffer sbf, StringBuffer sbj, Field f, Table tb){
		int i;
		String sf, sj, sv, snome;
		sf = "<tr><td>"+pseq+")</td><td>@v4</td><td><input id='@v1' type='text' @v2 style='width:@v3px' maxlength='@v5'></td><td id='@v6'></td><td id='@v7'></td></tr>";
		//sj = "\n$('#@v1').textbox({}).attr('maxlength',6);";
		sj = "@v1: $('#@v1').val() ";
		if(_.notEmpty(f.Valor)){
			//sv = tb.getFieldByNameString(f.Nome);
			sf=sf.replace("@v2","value='"+f.Valor+"'");
		}else{
			sf=sf.replace("@v2", "");
		}
		try{
			i=Integer.parseInt(Record.tamDefaultString(f.Tamanho));
		}catch(Exception ex){
			i=1;
		}
		//i=i*10;
		if(_.notEmpty(f.Descr_Curta)){
			if(f.Descr_Curta.trim().length()==0){
				snome=f.Nome;
			}else{
				snome=f.Descr_Curta;
			}
		}else{
			snome=f.Nome;
		}
		
		if(_.notEmpty(f.Dom_Tabela)){
			sf = "<tr><td>"+pseq+")</td><td>@v4</td><td>"+this.htmlUtilSelec(f)+"</td><td id='@v6'></td><td id='@v7'></td></tr>";
			sf = sf.replace("@v1", f.Nome).replace("@v3px", 300+"px").replace("@v4", snome).replace("@v5", ""+i).replace("@v6",f.Nome+"err").replace("@v7",f.Nome+"msg");
			sbf.append(sf);
			if(sbj.length()>0){
				sbj.append(",");
			}
			sbj.append(sj.replace("@v1", f.Nome));
			return;
		}
		
		sbf.append(sf.replace("@v1", f.Nome).replace("@v3px", 300+"px").replace("@v4", snome).replace("@v5", ""+i).replace("@v6",f.Nome+"err").replace("@v7",f.Nome+"msg"));
		if(sbj.length()>0){
			sbj.append(",");
		}
		sbj.append(sj.replace("@v1", f.Nome));
	}
	
	private String htmlUtilSelec(Field f){
		String s="", sel="";
		String sql = "select cod, descr from "+f.Dom_Tabela + " order by cod";
		
		try{
		ResultSet rs = msql.select(sql);
		s="<select name='"+f.Nome+"' id='"+f.Nome+"' style='width:300px'>";
		while(rs.next()){
			sel="";
			if(f.Valor!=null && f.Valor.equals(rs.getString("COD"))){
				sel=" selected";
			}
			s+="<option value='"+rs.getString("COD")+"'"+ sel+">"+rs.getString("COD")+"-"+rs.getString("DESCR")+"</option>";
		}
		s+="</select>";
		}catch(Exception ex){
			
		}
		return(s);
		
	}
	
	public String getJSONData(){
		String s;
		s = mTable.getJSONData();
		return(s);
	}
	
	/*
	public void setRecord(Record record){
		mRecord=record;
	}
	*/
	public Record getRecord(){
		return(mRecord);
	}
	
	public String getPai(){
		return(this.regpai);
	}
	
	public Record getRecordTemp(){
		return(mRecordTemp);
	}
	
	
}
