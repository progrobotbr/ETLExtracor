package sped.http.app.app001;

import java.util.ArrayList;

import sped.bean.beanSped;
import sped.db.util.CSql;
import sped.http.app.app001.datagrid.datagrid;
import sped.msg.mensagem;
import sped.tree.Table;
import sped.tree.capa.Capa;
import sped.tree.capa.bloco.Bloco;
import sped.tree.capa.bloco.record.Record;
import sped.tree.capa.bloco.valid.messageValid;
import sped.tree.capa.bloco.valid.base.occurUtils;
import utils._;
import http.protocol.HttpRequest;
import http.protocol.HttpResponse;
import http.protocol.HttpSession;

public class TelaSpedTree implements ITela{

	private String       action="";
	private HttpSession  session=null;
	private HttpRequest  request=null;
	private HttpResponse response=null;
	
	//Eventos
	public static final String ONLOAD              = "onload";
	public static final String ONINSERIR           = "oninserir";
	public static final String ONGETMETATABLEBLOCO = "ongetmetatablebloco";
	public static final String ONGETTABLEDATABLOCO = "ongettabledatabloco";
	public static final String ONGETMETATABFILHOS  = "ongetmetatabfilhos";
	public static final String ONGETTABFILHOS      = "ongettabfilhos";
	public static final String ONGEMETAFILHOS      = "ongetmetafilhos";
	public static final String ONGETFILHOS         = "ongetfilhos";
	public static final String ONGETFORM           = "ongetform";
	public static final String ONSAVEFORM          = "onsaveform";
	public static final String ONVALIDFORM         = "onvalidform";
	public static final String ONUPLOADFORM        = "onuploadform";
	public static final String ONUPLOADSAVE        = "onuploadsave";
	
	public static final String IDCAPA     = "idcapa";
	public static final String IDPAI      = "idpai";
	public static final String ID         = "id";
	public static final String REGPAI     = "regpai";
	public static final String REG        = "reg";
	public static final String LEVEL      = "level";
	public static final String FILEUP001  = "fileup001";
	
	private beanSped bean   = null;
	private Table metaTable = null;
	
	public int idcapa;
	public int idpai;
	public int id;
	public int ilevel;
	public String reg;
	public String regpai;
	
	private Record recordForm;
	private RegKey regKey = new RegKey();
	
	public void post(HttpSession session, HttpRequest request, HttpResponse response) throws Exception {
		
		this.session  = session;
		this.request  = request;
		this.response = response;
		
		this.getVarRequests();
		
		this.bean = beanSped.api(session);
		bean.getSql().beginTransaction();
		
		try{
			
		
			if(this.action.equals(TelaSpedTree.ONLOAD)){
				//int i=10/0;
				this.onLoad();
				
			}else if(this.action.equals(TelaSpedTree.ONGETMETATABLEBLOCO)){
				this.onGetMetaTableBloco();	
				
			}else if(this.action.equals(TelaSpedTree.ONGETTABLEDATABLOCO)){
				this.onGetTableDataBloco();
				
			}else if(this.action.equals(TelaSpedTree.ONGETMETATABFILHOS)){
				this.onGetMetaTabFilhos();
				
			}else if(this.action.equals(TelaSpedTree.ONGETTABFILHOS)){
				this.onGetTabFilhos();
				
			}else if(this.action.equals(TelaSpedTree.ONGEMETAFILHOS)){
				this.onGetMetaFilhos();
				
			}else if(this.action.equals(TelaSpedTree.ONGETFILHOS)){
				this.onGetFilhos();
				
			}else if(this.action.equals(TelaSpedTree.ONGETFORM)){
				this.onGetForm();
				
			}else if(this.action.equals(TelaSpedTree.ONSAVEFORM)){
				this.onSaveForm();
				
			}else if(this.action.equals(TelaSpedTree.ONVALIDFORM)){
				this.onValidForm();
			
			}else if(this.action.equals(TelaSpedTree.ONUPLOADFORM)){
				this.onUploadForm();
			
			}else if(this.action.equals(TelaSpedTree.ONUPLOADSAVE)){
				this.onUploadSave();
			}
		
			this.bean.getSql().commit();
		
		}catch(Exception ex){
			this.bean.getSql().rollback();
			String s=jsonUtil.doMessage(4, ex.toString()+";"+_.stackTrace(ex));
			response.print(s);
		}
	}
	
	private void onValidForm() throws Exception{
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		boolean berr = false;
		String s,jv,s2;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		ArrayList <messageValid> msgs;
		
		this.recordForm.putRequestInRecord(this.request);
		msgs = this.recordForm.validRecord();
		
		//Replaces para javascript
		//@1 = script @2 = "
		//
		sb.append("<table border=0>");
		sb1.append("<@1 language=@2javascript@2>");  
		sb1.append("clearErrForm();");
		for(messageValid mv: msgs){
			//mv.saveDB(this.recordForm.getBeanSped().getSql());
			s2 = mv.getField() + "msg";
			jv="$(@2#@v1@2).html($(@2#@v1@2).html()+@2 @2+@2@v2@2);$(@2#@v1@2).css(@2color@2,@2red@2);";
			jv=jv.replaceAll("@v1",s2).replaceAll("@v2", "ERRO:"+mv.getMessage()+"<br>");
			sb1.append(jv);
			sb.append("<tr>");
			sb.append("<td>"+mv.getType()+"</td><td>"+mv.getField()+"</td><td>"+mv.getMessage()+"</td>"+"<td>"+mv.getIdcapa()+"</td>"+"<td>"+mv.getId()+"</td>"+"<td>"+mv.getReg()+"</td>"+"<td>"+mv.getRuleName()+"</td>");
			sb.append("</tr>");
			berr = true;
			//sb.append(s);
		}
		sb1.append("</@1>");
		if(berr==false){
			sb1.append("Sucesso");
		}else{
			sb1.append("Registro com erros");
		}
		
		sb.append("</table>");
		sb.append(sb1.toString());
		s=jsonUtil.doMessage(4,sb1.toString());
		response.print(s);
		
		
	}
	
	private void onSaveForm() throws Exception{
		
		String sidcapa;
		String json_body = "{'id':\"@v1\",'rc':\"@v2\", 'msg':\"@v3\"}";
		ArrayList<messageValid> msgs;
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		sidcapa = ""+idcapa;
		if(sidcapa.equals(Capa.ID_CAPA_CAD_BASICO)){
			msgs = this.recordForm.validRecord();
			if(msgs!=null && msgs.size()>0){
				json_body =jsonUtil.doMessage(4, "Não é possível gravar, registro possui erro de validação");
				response.print(json_body);
				return;
			}
		}
		
		if(id>0){
			this.recordForm.updateFromRequest(request, idcapa, idpai, id, regpai, reg);
		}else{
			this.recordForm.insertFromRequest(request, idcapa, idpai, id, regpai, reg);
		}
		if(this.recordForm.rc==mensagem.ISUCESSO){
			json_body = json_body.replace("@v1", ""+this.recordForm.getId()).replace("@v2", "0").replace("@v3", "Sucesso");
		}else{
			json_body = json_body.replace("@v1", ""+this.recordForm.getId()).replace("@v2", "4").replace("@v3", "Erro ao gravar registro");
		}
		
		response.print(json_body);
		
	}
	
	private void onGetForm() throws Exception{
		boolean b;
		String sret;
		Capa capa;
		Bloco bloco;
		occurUtils oc;
		
		//
		// se id = 0, indica que é inserção
		//
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1 ");
		
		capa = this.bean.getCapa();
		bloco = capa.getBloco();
		
		if(id==0){
		  oc = new occurUtils(this.bean, regKey);
		  b = oc.podeInserir(regKey);
		  if(b==false){
			response.printErrHtml(10, "Não é possível inserir registro "+regKey.reg+" devido cardinalidade");
			return;
		  }
		}
		
		sret = bloco.getHTMLMetaForm(idcapa, idpai, id, regpai, reg);
		this.recordForm = bloco.getRecordTemp();
		
		response.print(sret);
		
		
	}
	
	private void onGetMetaFilhos() throws Exception{
		
		String html, sfalterar, sflistar, sfup;
		Capa   capa;
		Bloco  bloco;
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		capa      = this.bean.getCapa();
		bloco     = capa.getBloco();
		sfalterar = datagrid.getMetaFunc("alterarform", reg, ilevel+1, idcapa, idpai, id, regpai, reg);
		//sfcriar="\" style=\"display:none\"";
		sflistar = datagrid.getMetaFunc("onfilhotabs", reg, ilevel+1, idcapa, idpai, id, regpai, reg);
		sfup="\" style=\"display:none\"";
		metaTable = bloco.getHTMLMetaFilho("", "", this.ilevel, this.idcapa, this.id, 0, this.regpai, this.reg);
		html      = metaTable.getHTMLMeta2("ongetfilhos", sfalterar, sflistar, sfup, this.ilevel, this.idcapa, this.id, 0, this.regpai, this.reg);
		html      = html.replace(">Novo<",">Alterar<"); //Texto do botão
		
		response.print(html);
		
	}
	
	private void onGetFilhos() throws Exception{
		String json;
		
		response.setHeader("content-type", "text/json; charset=iso-8859-1");
		
		if(metaTable.rc==mensagem.ISUCESSO){
			json = metaTable.getJSONData2();
			response.print(json);
			return;
		}
		
		response.print(jsonUtil.doMessage(4, "Erro ao selecionar tabs"));
	}
	
	private void onGetTabFilhos() throws Exception{
		
		String json;
		
		response.setHeader("content-type", "text/json; charset=iso-8859-1");
		
		if(metaTable.rc==mensagem.ISUCESSO){
			json = metaTable.getJSONData();
			response.print(json);
			return;
		}
		
		response.print(jsonUtil.doMessage(4, "Erro ao selecionar tabs"));
	}
	
	private void onGetMetaTabFilhos() throws Exception{
		Capa capa;
		Bloco bloco;
		String htmlData;
		String sflist, sfcriar, sfup;
		
		response.setHeader("content-type", "text/html; charset=iso-8859-1");
		
		capa = this.bean.getCapa();

		bloco = capa.getBloco();
		
		metaTable = bloco.getHTMLMetaTabFilho(regKey);
		
		if(metaTable.rc==mensagem.ISUCESSO){
			sflist = datagrid.getMetaFunc("onlistfilhos", reg, ilevel+1, idcapa, idpai, id, regpai, reg);
			//sflist="\" style=\"display:none\"";
			sfcriar = datagrid.getMetaFunc("criarnovoform", reg, ilevel+1, idcapa, idpai, id, regpai, reg);
			sfup = datagrid.getMetaFunc("uploadform", reg, ilevel+1, idcapa, idpai, id, regpai, reg);
			//sfcriar="\" style=\"display:none\"";
			htmlData = metaTable.getHTMLMeta(TelaSpedTree.ONGETTABFILHOS,sfcriar,sflist,sfup,ilevel, idcapa, idpai, id,  regpai, reg);
			response.print(htmlData);
			return;
		}
		
		response.print(jsonUtil.doMessage(4, "Não há dados"));
	}
	
	private void onGetMetaTableBloco() throws Exception{
		Capa capa;
		Bloco bloco;
		String htmlData;
		String sfcriar, sflist, sfup;
		
		response.setHeader("content-type", "text/html");
		
		capa = this.bean.getCapa();
		
		bloco = capa.getBloco();
		
		if(bloco.getPai().equals(regpai)){
			sflist = datagrid.getMetaFunc("onblocotabs", regpai, ilevel+1, idcapa, idcapa, id, regpai, "");
			sfcriar = datagrid.getMetaFunc("dummy", regpai, ilevel+1, idcapa, idcapa, id, regpai, "");
			sfcriar="\" style=\"display:none\"";
			sfup="\" style=\"display:none\"";
			htmlData = bloco.getHTMLMetaBloco(sfcriar, sflist, sfup,  1, idcapa, idpai, id, regpai, "");
			response.print(htmlData);
			return;
		}
		
		response.print(jsonUtil.doMessage(4, "Erro ao selecionar bloco"));
	}
	
	private void onGetTableDataBloco() throws Exception{
		Capa capa;
		Bloco bloco;
		String json="";
		
		response.setHeader("content-type", "text/json");
		
		capa = this.bean.getCapa();
		bloco = capa.getBloco();
		json = bloco.getJSONData();
		response.print(json);
		
	}
	
	private void onLoad() throws Exception{
		
		String sret;
		Capa capa;
		
		response.setHeader("content-type", "text/html");
		
		if(this.idcapa==0){
			response.print(jsonUtil.doMessage(4, "Informar o id da Capa"));
			return;
		}
		
		capa = this.bean.getCapa();
		capa.selectCapa(idcapa);
		if(capa.rc != mensagem.ISUCESSO){
			response.print(jsonUtil.doMessage(4, "Capa não encontrada"));
			return;
		}
		
		capa.selectBloco();
		if(capa.rc != mensagem.ISUCESSO){
			response.print(jsonUtil.doMessage(4, "Bloco não encontrado"));
			return;
		}
		
		sret = jsonUtil.doMessage(0, "Sucesso");
		response.print(sret);
		
	}
	
	
	private void onUploadForm() throws Exception{
		
		String htmlData;
		
		response.setHeader("content-type", "text/html");
		
		/*
		htmlData="<form method='post' action='file.jsp' enctype=multipart/form-data>\n"+
		         "<input type='file' id='file' name='file'>\n"+
			     "<br>\n"+
		         "<input type=button name=enviar onclick='upload()'></form>\n"
		         + "\n<script>"
		         + "\nfunction upload(){"
		         + "\nalert($('#file')[0].files[0]);"
		         + "\nvar formData = new FormData();"
		         + "\nformData.append('file',$('#file')[0].files[0]);"
		         + "\nformData.append('tela','telaspedtree');"
		         + "\nformData.append('action','uploadsave');"
		         + "\nformData.append('dummy','dummy');"
		         + "\nalert($('#file')[0].files[0]);"
		         + "\nconsole.log(formData);"
		         +"\n $.ajax({"
		         + "\n   url : 'sped_crtl.jsp',"
		         + "\n   type : 'POST',"
		         + "\n   data : formData,"
		         + "\n   processData: false,"
		         + "\n   contentType: false,"
		         + "\n   success : function(data) {"
		         + "\n       console.log(data);"
		         + "\n       alert(data);"
		         + "\n   }"
		      +"\n})"
		      + "\n};"
		         +"\n</script>";
		
		response.print(htmlData);
		*/
		response.setNextJsp("appServer/sped/sped_08_upload.jsp");
		
	}
	
	private void onUploadSave() throws Exception{
		
		int rc;
		String json_body, smsg;
		Record record;
		
		response.setHeader("content-type", "text/html");
		record = new Record(bean, 0, idcapa, regpai, idpai, reg, id) ;
		record.insertFromUpload(request, TelaSpedTree.FILEUP001, idcapa, idpai, id, regpai, reg);
		rc=record.rc;
		if(rc==0){
			smsg = "Upload efetuado com sucesso";
		}else{
			smsg = "Erro ao efetuar o upload";
		}
		json_body=jsonUtil.doMessage(rc,smsg);
		response.print(json_body);
		
	}
	
	private void getVarRequests(){
		
		String sidcapa, sidpai, sid, sregpai, sreg, slevel;
		
		this.action = request.getParameter(ITela.ACTION);
		
		sidcapa     = request.getParameter(TelaSpedTree.IDCAPA);
		sidpai      = request.getParameter(TelaSpedTree.IDPAI);	
		sid         = request.getParameter(TelaSpedTree.ID);
		this.regpai = request.getParameter(TelaSpedTree.REGPAI);
		this.reg    = request.getParameter(TelaSpedTree.REG);
		slevel      = request.getParameter(TelaSpedTree.LEVEL);
		
		//this.idcapa = 0;
		if(_.notNull(sidcapa)){
	    	this.idcapa = CSql.ctoi(sidcapa);
	    }
		
		//this.idpai = 0;
		if(_.notNull(sidpai)){
			this.idpai = CSql.ctoi(sidpai);
	    }
		
		//this.id = 0;
		if(_.notNull(sid)){
			this.id = CSql.ctoi(sid);
	    }
		
		//this.idpai = 0;
		if(_.notNull(slevel)){
			this.ilevel = CSql.ctoi(slevel);
	    }
		
		regKey = new RegKey(this.idcapa, this.idpai, this.id, this.ilevel, this.reg, this.regpai);
	}
	
	
}
