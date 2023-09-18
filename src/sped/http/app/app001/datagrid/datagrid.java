package sped.http.app.app001.datagrid;

import sped.db.util.CSql;
import utils._;

public class datagrid {
	
	private static final String columns  = "{ field:'@field', title:'@title' ,width:@width ,sortable:true}"; //vários separados por vírgula
	private static final String dg = 
	      "<table id='@name@level' style='width:@widthpx;height:@heightpx'></table>" +
	      "<a href='#' id='btcr@name@level' onclick=\"@funccriar\">Novo</a>"+
	      "<a href='#' id='btls@name@level' onclick=\"@funclist\">Listar</a>"+
	      "<a href='#' id='btup@name@level' onclick=\"@funcuplo\">Upload</a>"+
		  "<script language='javascript'>" +
		  "$('#btcr@name@level').linkbutton({iconCls:'icon-add',width:'110px'});" +
	      "$('#btls@name@level').linkbutton({iconCls:'icon-remove',width:'110px'});" +
	      "$('#btup@name@level').linkbutton({iconCls:'icon-add',width:'110px'});" +
	      " $('#@name@level').datagrid({" +
		  " url:'sped_crtl.jsp'," + //?tela=@tela,action=@action,level=@level,regpai=@regpai,reg=@reg,idcapa=@idcapa,idpai=@idpai,id=@id'," +
		  " queryParams: { tela: '@tela', action: '@action', level: '@level', regpai:'@regpai', reg: '@reg', idcapa: '@idcapa', idpai: '@idpai', id: '@id' }," +
		  " title:'@title'," +
		  " singleSelect:true," +
		  " collapsible:true," +
		  " method:'post'," +
		  " remoteSort:false," +
		  " multiSort:true," +
		  " columns:[[ @columns ]]" +
		  " });"+
		  "location.href='#bottom'"+
		"</script>";
	
	private static final String dgfunc = "@func('#@tab@level','@level','@idcapa','@idpai','@id','@regpai','@reg')";
		
	private String virg="";
	private String cols="";
	private String json="";
	private int qtdrec=0;
	private int qtdcol=0;
	
	public void putTitle(String field, String title, String width){
		String swidth,stitle; 
		if(_.notEmpty(width)){
			swidth=width;
		}else{
			swidth="50";
		}
		if(_.notEmpty(title)){
			stitle=title;
		}else{
			stitle=field;
		}
		
		this.cols += this.virg + columns.replace("@field", field).replace("@title",stitle).replace("@width", swidth);
		this.virg=",";
	}
	public static String getMetaFunc(String function, String tab, int level, int idcapa, int idpai, int id, String regpai, String reg){
		String s = datagrid.dgfunc.replace("@func", function)
				                  .replace("@tab",tab)
				                  .replaceAll("@level", CSql.itoc(level))
				                  .replace("@idcapa", CSql.itoc(idcapa))
				                  .replace("@idpai", CSql.itoc(idpai))
				                  .replace("@id",CSql.itoc(id))
				                  .replace("@regpai", regpai)
				                  .replace("@reg", reg);
		return(s);
	}

	
	public String getDataGrid(String title, 
			                  String name, 
			                  String funccriar, 
			                  String funclist, 
			                  String funcup,
			                  String width, 
			                  String height, 
			                  String tela, 
			                  String action, 
			                  int level, 
			                  int idcapa, 
			                  int idpai, 
			                  int id, 
			                  String reg, 
			                  String regpai){
		
		String stab = "tb"+level;
		String s = datagrid.dg.replaceAll("@name", name.toUpperCase())
				              .replace("@width", width)
				              .replace("@tab", stab)
				              .replaceAll("@level", ""+(level+1))
				              .replace("@funccriar", funccriar)
				              .replace("@funclist", funclist)
				              .replace("@funcuplo", funcup)
				              .replace("@idcapa", ""+idcapa)
				              .replace("@idpai", ""+idpai)
				              .replace("@id", ""+id)
				              .replace("@regpai",regpai)
				              .replace("@reg",reg)
				              .replace("@height", height)
				              .replace("@tela",tela)
				              .replace("@action",action)
				              .replace("@title",title)
				              .replace("@columns", this.cols);
		return(s);
	}
	
	public String getJson(){
		String s= "{\"total\":0,\"rows\":[ @V1 ]}";
		s=s.replace("@V1",this.json);
		return(s);
	}
	public void setJson(String json){
		this.json = json;
	}
	
	/*
	 *  initRecord() putfield(nome, renato) putfield(idade, 21) endRecord()
	 * 
	 */
	public void initRecord(){
		if(this.qtdrec==0){
          this.json += "{";
		}else{ 
		  this.json += ",{";	
		}
		this.virg = "";
		this.qtdrec++;
	}
	
	public void endRecord(){
		this.json += "}";
	}
	
	public void putField(String field, String value){
		this.json += this.virg + "\"" + field + "\":\"" +CSql.valuesql(value) + "\"";
		this.virg = ",";		
	}
	
	
}
