package assignment01;

import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
	HashMap<String,InterpreterDataType> map; // String: Key IDT: data at key
	
	InterpreterArrayDataType(){
		map = new HashMap<>();
	}
	InterpreterArrayDataType(HashMap<String,InterpreterDataType> map){
		this.map = map;
	}
	
	public HashMap<String,InterpreterDataType> getMap() {
		return map;
	}
	
	public void setMap(HashMap<String,InterpreterDataType> newMap) {
		map = newMap;
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
