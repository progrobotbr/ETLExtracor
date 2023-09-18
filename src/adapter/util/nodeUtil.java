package adapter.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class nodeUtil {
	private int t=0;
	private int i=0;
	private Node n=null;
	private NodeList nl;
	
	public nodeUtil(Node n){
		nl = n.getChildNodes();
		t=nl.getLength();
		i=0;
	}
	
	public Node getNext(){
		if(i<t){
			n=nl.item(i);
			i++;
		}else{
			n=null;
		}
		return(n);
		
	}
	
	public String getName(){
		String s=n.getNodeName();
		return(s);
	}
	
	public boolean isTag(String s){
		String s1=n.getNodeName();
		if(s1.equals(s))
			return(true);
		else
			return(false);
	}
	
	public Node getNode(){
		return(n);
	}
	
	public String getValue(){
		String s;
		NodeList nl = n.getChildNodes();
		s = nl.item(0).getNodeValue().toUpperCase();
		return(s);
	}

}
