package sped.tree.capa.bloco.valid;

import sped.tree.Field;
import sped.tree.capa.bloco.record.Record;

public interface IValidRuleField {
	
	public messageValid exec(String seq, String jname, Field f, Record record);

}
