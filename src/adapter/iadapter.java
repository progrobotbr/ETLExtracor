package adapter;

public interface iadapter {

	public int parse(String pSetName);
	public String getMsg();
	
	public static String CTBBASES     = "tbbases";
	public static String CTBBASESSQL  = "tbbasessql";
	public static String CTBBASE      = "tbbase";
	public static String CTBNAME      = "tbname";
	public static String CFIELDS      = "fields";
	public static String CFIELD       = "field";
	public static String CKEYS        = "keys";
	public static String CFIELD_VALUE = "field_value";
	public static String CVAL         = "val";
	public static String CWHERE       = "where";
	public static String CTBJOIN      = "tbjoin";
	public static String CJOIN        = "join";
	public static String CCONDW       = "condw";
	public static String CCONDJ       = "condj";
	public static String CSETNR       = "setnr";
	public static String CSQL         = "sql";
	
}
