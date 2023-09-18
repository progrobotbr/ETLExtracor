package cmd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class api {
	
	public String execute(String s){
		String sret=null;
		s+="\nexit\n";
		byte[] bytes = s.toString().getBytes();
		InputStream inp = new ByteArrayInputStream(bytes);
		command cmd = new command();
		OutputStream ous = new OutputStream(){
	        private StringBuilder string = new StringBuilder();
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    cmd.execCmds(inp, ous);
	    sret = ous.toString();
	    //s="exit\n";
	    //bytes = s.toString().getBytes();
		//inp = new ByteArrayInputStream(bytes);
	    //cmd.execCmds(inp, ous);
		return(sret);
		
	}

}
