package adapter.sap.connector;

import utils._;
import adapter.bean.beanAdapterSAP;

import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.Repository;

import config.Config;
import msg.msg;

public class sapConnection {
	
	static final String POOL_NAME = "adapter_sap";
	static final String REPO_NAME = "repo_sap";
	private beanAdapterSAP    beanAdpSap = null;
	private Client     mConnection=null;
	private Repository mRepository=null;
	
	public void setBeanAdpSap(beanAdapterSAP pbeanAdpSap){
		beanAdpSap = pbeanAdpSap;
	}
	
	public synchronized int connect(){
		
		try{
			mConnection = JCO.getClient(POOL_NAME);
		    mRepository = new JCO.Repository(REPO_NAME, mConnection);
		 }catch (Exception ex) {
		     _.log("SAP:connect :"+ex.toString());
		     beanAdpSap.addException(ex);
		     return(5);
		 }
		return(msg.SUCESSO);
	}
	
	public Client getConnection(){
		return(mConnection);
	}
	
	public Repository getRepository(){
		return(mRepository);
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		JCO.releaseClient(mConnection);
	}
	
	public static int  mountPool(){
		try{
			JCO.Pool pool = JCO.getClientPoolManager().getPool(POOL_NAME);
		    if (pool == null) {
		    	
		    	/*
		    	JCO.addClientPool(POOL_NAME,  // pool name
		                          1,           // maximum number of connections
		                          "130",
		                          "rlima",
		                          "SONDA@1",
		                          "PT",
		                          "172.28.182.5",
		                          "10"
		                          );
		         */
		    	JCO.addClientPool(POOL_NAME,  // pool name
                        Config.getInt(Config.sap_adapter_qtd_pool),     // maximum number of connections
                        Config.getString(Config.sap_adapter_client),   //"300",
                        Config.getString(Config.sap_adapter_user),     //"usermsaf",
                        Config.getString(Config.sap_adapter_pass),     //"SAP@15saf",
                        Config.getString(Config.sap_adapter_language), //"EN",
                        Config.getString(Config.sap_adapter_server),   //"/H/201.28.216.226/H/192.168.0.226",
                        Config.getString(Config.sap_adapter_system)    //"00"
                        );
		    	/*
		    	JCO.addClientPool(POOL_NAME,  // pool name
                        1,           // maximum number of connections
                        "300",
                        "usermsaf",
                        "SAP@15saf",
                        "EN",
                        "/H/201.28.216.226/H/192.168.0.226",
                        "00"
                        );
                */
		    	/*
		    	JCO.addClientPool(POOL_NAME,  // pool name
                        5,           // maximum number of connections
                        "001",
                        "SAP*",
                        "sapn4sadm",
                        "EN",
                        "192.168.129.128",
                        "42"
                        );
                */
		    }
		    return(msg.SUCESSO);
		}catch(Exception ex){
			_.log("Sap pool:> "+ex.toString());
		}
		return(14);
		     
	}
	
	/*
			JCO.Pool pool = JCO.getClientPoolManager().getPool(POOL_NAME);
		      if (pool == null) {
		         JCO.addClientPool(POOL_NAME,  // pool name
		                          5,           // maximum number of connections
		                          "130",
		                          "rlima",
		                          "SONDA@1",
		                          "PT",
		                          "172.28.182.5",
		                          "10"
		                          );
		      }
		      mConnection = JCO.getClient(POOL_NAME);
			mConnection =
		          JCO.createClient("130", // sap client
		          "rlima",                // user id
		          "SONDA@1",              // password
		          "PT",                   // language
		          "172.28.182.5",         // application server host name, router "/H/200.184.174.249/H/172.28.182.5"
		          "10"                   
		          ); 
			_.log("RENATO_2");
			mConnection.connect();
			_.log("RENATO_3");
		    mRepository = new JCO.Repository(REPO_NAME, mConnection);
		    _.log("RENATO_4");
		    
		  //"172.28.182.5, 200.184.174.249",     
		 }catch (Exception ex) {
		     _.log("SAP:connect :"+ex.toString());
		     return(5);
		 }
		 //}finally {
		   //   JCO.releaseClient(mConnection);
		 //}
		return(msg.SUCESSO);
	*/	

}
