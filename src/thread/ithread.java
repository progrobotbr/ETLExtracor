package thread;

public interface ithread {

	public static final int ITCP = 1;
	public static final int ICMD = 1;
	public static final int IJOB = 1;
	
	public int getThreadType();
	public void setThreadType(int i);
	
}
