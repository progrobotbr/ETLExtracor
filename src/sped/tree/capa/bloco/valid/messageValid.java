package sped.tree.capa.bloco.valid;

import java.sql.ResultSet;
import java.util.ArrayList;

import sped.bean.beanSped;
import sped.db.sqlLocal;
import sped.db.util.CSql;
import sped.msg.mensagem;
import sped.tree.capa.bloco.record.Record;
import utils._;

public class messageValid {
	
	private String type;
	private int    number;
	private int    idcapa;
	private int    id;
	private String reg;
	private String field;
	private String ruleSeq;
	private String ruleType;
	private String ruleName;
	private String message;
	public ArrayList<messageValid> msgs;
	
	public static messageValid getMessageRecord(Record rs, String seq, String type, int msgnumber, String nomerule){
		String sMsg="";
		messageValid mv = new messageValid();
		try{
			sMsg = messageValid.getMsgFromDb(msgnumber, rs.getBeanSped().getSql());
		}catch(Exception ex){
			sMsg = "Mensagem não encontrada (" + msgnumber + "): "+ex.toString();
		}
		mv.putMsg(type, msgnumber, rs.getIdCapa(), rs.getId(), rs.getReg(), rs.getReg(), seq, validRules.TYPERECORDCOD, nomerule, sMsg);
		return(mv);
	}
	
	public static messageValid getMessageField(Record rs, String seq, String type, int msgnumber, String nomerule, String field, String valor){
		String sMsg="";
		messageValid mv = new messageValid();
		try{
			sMsg = messageValid.getMsgFromDb(msgnumber, rs.getBeanSped().getSql()) + ". Regra["+nomerule.toLowerCase()+"("+ field.toLowerCase()+"["+valor+"])]";
			//sMsg+= " Valor entrado: "+valor;
		}catch(Exception ex){
			sMsg = "Mensagem não encontrada (" + msgnumber + "): "+ex.toString();
		}
		mv.putMsg(type, msgnumber, rs.getIdCapa(), rs.getId(), rs.getReg(), field, seq, validRules.TYPEFIELDCOD, nomerule, sMsg);
		return(mv);
	}
	
	public messageValid(){
		type="";
		number=0;
		idcapa=0;
		id=0;
		reg="";
		field="";
		ruleSeq="";
		ruleType="";
		ruleName="";
		message="";
		msgs=new ArrayList<messageValid>();
		
	}
	
	public void putMsg(String type, int number, int idcapa, int id, String reg, String field, String ruleSeq, String ruleType, String ruleName, String message){
		this.type=type;
		this.number=number;
		this.idcapa=idcapa;
		this.id=id;
		this.reg=reg;
		this.field=field;
		this.ruleSeq=ruleSeq;
		this.ruleType=ruleType;
		this.ruleName=ruleName;
		this.message=message;
	}
	
	public void msgFromDB(sqlLocal sqldb){
		String sql = "select msg from "+beanSped.getNomeProcesso()+"_b004 where num = '"+this.number+"'";
		try{
			ResultSet rs = sqldb.select(sql);
			if(rs.next()){
				message = rs.getString("MSG");
			}
			rs.getStatement().close();
			rs.close();
		}catch(Exception ex){}
	}
	
	public static String getMsgFromDb(int num, sqlLocal sqldb){
		String s="";
		String sql = "select msg from "+beanSped.getNomeProcesso()+"_b004 where num = '"+num+"'";
		try{
			ResultSet rs = sqldb.select(sql);
			if(rs.next()){
				s = rs.getString("MSG");
			}
			rs.getStatement().close();
			rs.close();
		}catch(Exception ex){}
		return(s);
	}
	
	public void saveDB(sqlLocal sqldb){
		String sdata = _.getTimeString();
		String sqldel = "delete from "+beanSped.getNomeProcesso()+"_mlog where idcapa='@v1' and id='@v2' and reg='@v3'";
		String sqlins = "insert into "+beanSped.getNomeProcesso()+"_mlog (idcapa, id, reg, date, type, number, field, ruleseq, ruletype, rulename, message) "+
		                " values ('@v1','@v2','@v3','@v4','@v5','@v6','@v7','@v8','@v9','@v10','@v11')";
		
		sqldel = sqldel.replace("@v1",CSql.itoc(idcapa))
			    	   .replace("@v2",CSql.itoc(id))
			    	   .replace("@v3",reg);
		
		sqlins = sqlins.replaceFirst("@v1",CSql.itoc(idcapa));
		sqlins = sqlins.replaceFirst("@v2",CSql.itoc(id));
		sqlins = sqlins.replaceFirst("@v3",reg);
		sqlins = sqlins.replaceFirst("@v4",sdata);
		sqlins = sqlins.replaceFirst("@v5",type);
		sqlins = sqlins.replaceFirst("@v6",CSql.itoc(number));
		sqlins = sqlins.replaceFirst("@v7",field);
		sqlins = sqlins.replaceFirst("@v8",ruleSeq);
		sqlins = sqlins.replaceFirst("@v9",ruleType);
		sqlins = sqlins.replaceFirst("@v10",ruleName);
		sqlins = sqlins.replaceFirst("@v11",message);
		
		try{
			//sqldb.change(sqldel);
			//if(sqldb.rc==mensagem.ISUCESSO){
				sqldb.change(sqlins);
			//}
		}catch(Exception ex){
		}	
		
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getIdcapa() {
		return idcapa;
	}
	public void setIdcapa(int idcapa) {
		this.idcapa = idcapa;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void concatMessage(String message){
		this.message += "-"+message;
	}
	public String getRuleSeq() {
		return ruleSeq;
	}

	public void setRuleSeq(String ruleSeq) {
		this.ruleSeq = ruleSeq;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleName() {
		return ruleName;
	}

	

}
