package assignment01;

public class InterpreterDataType {
	private String data;
	
	//variables that are created but not initialized
	InterpreterDataType(){
		this("");
	}
	
	InterpreterDataType(String data){
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String s) {
		data = s;
	}
	
	public String toString() {
		return data;
	}
	
}
