package config;

import java.io.BufferedReader;
import java.io.FileReader;
import msg.msg;
import java.util.HashMap;

import utils._;

public class Config {

	public static final String  path_log             ="path_log";
	public static final String  path_set             ="path_set";
	public static final String  local_db_url         ="local_db_url";
	public static final String  local_db_driver      ="local_db_driver";
	public static final String  db_adapter_driver    ="db_adapter_driver";
	public static final String	db_adapter_url       ="db_adapter_url";
	public static final String	sap_adapter_qtd_pool ="sap_adapter_qtd_pool";
	public static final String	sap_adapter_client   ="sap_adapter_client";
	public static final String	sap_adapter_server   ="sap_adapter_server";
	public static final String	sap_adapter_system   ="sap_adapter_system";
	public static final	String	sap_adapter_language ="sap_adapter_language";
	public static final String  sap_adapter_user     ="sap_adapter_user";
	public static final String  sap_adapter_pass     ="sap_adapter_pass";
	public static final String  server_port          ="server_port";
	public static final String  http_port            ="http_port";
	public static final String  http_folder          ="http_folder";
	public static final String  java_folder          ="java_folder";
	public static final String  sped_db_local_url    ="sped_db_local_url";
	public static final String  sped_db_local_driver ="sped_db_local_driver";
	public static final String  sped_file_path       ="sped_file_path";
	public static final String  so_path_separator    ="so_path_separator";
	
	private HashMap<String,String> hmc = new HashMap<String,String>();
	private static Config INSTANCE;
	
	public static Config factory(){
		if(INSTANCE==null){
			INSTANCE=new Config();
		}
		return(INSTANCE);
		
	}
	public int initC(String pFile){
		String s="";
		String s1[];
		try{
			BufferedReader bf = new BufferedReader(new FileReader(pFile));
			while((s=bf.readLine())!=null){
				s1 = s.split("=");
				if(s1.length>1){
					hmc.put(s1[0], s1[1]);
				}else{
					hmc.put(s1[0], "");
				}
			}
			bf.close();
			return(msg.SUCESSO);
		}catch(Exception ex){
			_.log(ex.toString());
			return(26);
		}
	}
	
	public static int initConfig(String pFile){
		int i;
		Config co = Config.factory();
		i = co.initC(pFile);
		return(i);
	}
	
	public String getS(String pchave){
		String s="";
		s=hmc.get(pchave);
		return(s);
	}
	public int getI(String pchave){
		int i=0;
		String s="";
		s=hmc.get(pchave);
		try{
			i = Integer.parseInt(s);
		}catch(Exception ex){
			
		}
		return(i);
	}
	
	public static int getInt(String pchave){
		int i;
		Config co = Config.factory();
		i=co.getI(pchave);
		return(i);
	}
	
	public static String getString(String pchave){
		String s="";
		Config co = Config.factory();
		s=co.getS(pchave);
		return(s);
	}
	
}
