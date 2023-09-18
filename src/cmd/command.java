package cmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.regex.*;

import scheduler.job;
import scheduler.scheduler;
import scheduler.bean.beanScheduler;
import utils._;
import msg.*;

public class command {

	private PrintWriter out;
	private BufferedReader in;
	private boolean brun=true;
	
	//Lista de comandos
	private static final String CDOS = "dos";
	private static final String CSET = "set";
	private static final String CSPED = "sped";
	private static final String CLIST = "list";
	private static final String CEXIT = "exit";
	private static final String CHELP = "help";
	private static final String CDELE = "delete";
	private static final String CSHUT = "shutdown";
	
	private void println(String s){
		out.println(s);out.flush();
	}
	
	private void print(String s){
		out.print(s);out.flush();
	}
	
	private void printok(){
		out.println("ok:>ok");out.flush();
	}
	
	public String getln() throws Exception {
		String s;
		try{
			s=in.readLine();
			return(s);
			
		}catch(Exception ex){
			_.log(ex.toString());
			brun = false;
			throw(new Exception(ex));
		}
	}
	
	public void err(String s){
		println("err:>"+s);
	}
	
	public void exit(){
		println("\nbye");
		brun = false;
	}
	
	public String reDate(){
		String yearReg = "(201[4-9]|202[0-9])";            ///< Allows a number between 2014 and 2029
		String monthReg = "(0[1-9]|1[0-2])";               ///< Allows a number between 00 and 12
		String dayReg = "(0[1-9]|1[0-9]|2[0-9]|3[0-1])";   ///< Allows a number between 00 and 31
		String hourReg = "([0-1][0-9]|2[0-3])";            ///< Allows a number between 00 and 24
		String minReg = "([0-5][0-9])";                    ///< Allows a number between 00 and 59
		String s = "(now|("+dayReg + "/" + monthReg + "/" + yearReg + " "+hourReg + ":" + minReg + ":" +minReg+"))";
		return(s);
	}
	public int cmd(String s){
		int i;
		String scmd, sre, sc, sd, s1[];
		
		try{
			//Valida comando
			sre = "dos[ ]+.*[ ]*,[ ]*" + reDate()+"$";
			Pattern pt = Pattern.compile(sre);
			Matcher mt = pt.matcher(s);
			if(!mt.find()){
				println("err:>comando invalido, correto [dos <comandos dos>,<dd/mm/aaaa hh:mm:ss>]");
				return(4);
			}
			scmd = s;
			s=s.substring(command.CDOS.length(),s.length());
			s1=s.split(",");
			sc=s1[0];
			sd=s1[1];
			//"<comando dos>,<data>"
			job j = scheduler.createJob(job.JOBCMD, scmd, sc, sd);
			i=scheduler.scale(j);
			if(i==msg.SUCESSO){
				printok();
			}else{
				println("err:>Erro ao criar job scheduler");
			}
			i = msg.SUCESSO;
		}catch(Exception ex){
			println("err:>sintaxe do comando set incorreta. Correto: dos <comando_dos>,[now|<dd/mm/aaaa hh/MM/ss]");
			i = 4;
		}
		return(i);
	}
	
	public int set(String s){
		int i;
		String scmd, sre,sc, sd, s1[];
		try{
			
			sre = "set[ ]+([a-z]|[A-Z]|[0-9])*[ ]*,[ ]*" + reDate()+"$";
			Pattern pt = Pattern.compile(sre);
			Matcher mt = pt.matcher(s);
			if(!mt.find()){
				println("err:>comando invalido, correto [set <nome>,[now|<dd/mm/aaaa hh:mm:ss>]");
				return(4);
			}
			scmd = s; //guarda comando completo
			s=s.substring(command.CDOS.length(),s.length());
			s1=s.split(",");
			sc=s1[0];
			sd=s1[1];
			//set <nome>,<data>"
			job j = scheduler.createJob(job.JOBSET, scmd, sc, sd);
			i=scheduler.scale(j);
			if(i==msg.SUCESSO){
				printok();
			}else{
				println("err:>Erro ao criar job scheduler");
			}
			i = 0;
		}catch(Exception ex){
			println("err:>sintaxe do comando set incorreta. Correto: set <nome_set>,<dd/mm/aaaa hh/MM/ss");
			i = 4;
		}
		return(i);
	}
	
	public int sped(String s){
		int i;
		String scmd, sre,sc, sd, s1[];
		try{
			
			sre = "sped[ ]+[0-9]+[ ]*,[ ]*" + reDate()+"$";
			Pattern pt = Pattern.compile(sre);
			Matcher mt = pt.matcher(s);
			if(!mt.find()){
				println("err:>comando invalido, correto [set <nome>,[now|<dd/mm/aaaa hh:mm:ss>]");
				return(4);
			}
			scmd = s; //guarda comando completo
			s=s.substring(command.CSPED.length(),s.length());
			s1=s.split(",");
			sc=s1[0];
			sd=s1[1];
			//set <nome>,<data>"
			job j = scheduler.createJob(job.JOBSPED, scmd, sc, sd);
			i=scheduler.scale(j);
			if(i==msg.SUCESSO){
				printok();
			}else{
				println("err:>Erro ao criar job scheduler");
			}
			i = 0;
		}catch(Exception ex){
			println("err:>sintaxe do comando set incorreta. Correto: sped <id>,<dd/mm/aaaa hh/MM/ss");
			i = 4;
		}
		return(i);
	}
	
	public int list(){
		println(scheduler.getListJob());
		return(msg.SUCESSO);
	}
	
	public int delete(String s){
		int i;
		String sre = "delete[ ]+[0-9]+$";
		
		try{
			
			Pattern pt = Pattern.compile(sre);
			Matcher mt = pt.matcher(s);
			if(!mt.find()){
				println("err:>comando invalido, correto [delete 9..9]");
				return(4);
			}
		
			sre = "[0-9]+";
			pt = Pattern.compile(sre);
			mt = pt.matcher(s);
			mt.find();
			sre = mt.group(0);
			i = Integer.parseInt(sre);
			i=scheduler.deleteJob(i);
			sre=msg.getMsg(i);
			println(sre);
		
			return(msg.SUCESSO);
			
		}catch(Exception ex){
			_.log(ex.toString());
			return(4);
		}
		
	}
	
	public int shutdown(){
		beanScheduler bs = beanScheduler.factory();
		bs.setState(beanScheduler.ISSTOP);
		//deve ser feito mais código aqui, pois deve-se derrubar todos os thread
		//System.exit(0); //dettuba tudo não importa o que esteja fazendo
		//
		return(0);
	}
	
	public int help(){
		println("   set <nome>,[now|<dd/mm/aaaa hh:mm:ss>]   [agenda um set de extração]\r\n" +
				"   dos <comandos dos>,[now|<dd/mm/aaaa hh:mm:ss>] [agenda comando do so]\r\n" + 
				"   sped <id>, [now|<dd/mm/aaaa hh:mm:ss>]\r\n"+
				"   list                                     [lista jobs agendados]\r\n" + 
				"   delete <9..9>                            [deleta job]\r\n" +
				"   help                                     [lista os comandos do sistema]\r\n" +
				"   exit                                     [fecha conexão]" +
				"   shutdown                                 [derruba servidor]");
		return(msg.SUCESSO);
	}
	
	public void execCmds(InputStream ins, OutputStream ous){
		String s; //, sc, sd, s1[];
		out = new PrintWriter(ous, true);
		in = new BufferedReader(new InputStreamReader(ins));
		while(brun){
			try{
				print("cmd:>");
				s=getln();
				
				if(!s.equals(""))
				if(s.startsWith(command.CDOS)){
					//s = s.substring(command.CDOS.length(), s.length());
					this.cmd(s);
					
				}else if(s.startsWith(command.CSET)){
					//s = s.substring(command.CSET.length(), s.length());
					this.set(s);
				
				}else if(s.startsWith(command.CSPED)){
					//s = s.substring(command.CSET.length(), s.length());
					this.sped(s);
					
				}else if(s.startsWith(command.CLIST)){
					this.list();	
					
				}else if(s.startsWith(command.CEXIT)){
					this.exit();
				
				}else if(s.startsWith(command.CHELP)){
					this.help();	
				
				}else if(s.startsWith(command.CDELE)){
					this.delete(s);
					
				}else if(s.startsWith(command.CSHUT)){
						this.shutdown();
				}else{
					this.err("commando não entendido");
					
				}
				
			}catch(Exception ex){
				_.log("command: "+ex.toString());
				brun=false;
			}
		}
	}
}
