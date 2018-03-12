package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {

	public Event parse(IniSection sec) throws SimulationException;
	
	public default boolean isValidId(String id){
		return id.matches("[a-zA-Z0-9_]+");
	}
	
	public default boolean parseIdList(IniSection sec, String key) {
		if(sec.getValue(key) == null){
			return false;
		}
		return true;
	}
	
	public default boolean parseInt(IniSection sec, String key, int def) {
		if (parseIdList(sec, key)) {
			if (Integer.parseInt(sec.getValue(key)) >= def) return true;
		}
		return false;
	}
	
}
