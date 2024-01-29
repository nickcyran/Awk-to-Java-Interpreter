package assignment01;

enum processes {NORMAL, BREAK, CONTINUE, RETURN}

public class ReturnType {
	private String toReturn;
	private processes process;
	
	ReturnType(processes process){
		this(process, null);
	}
	
	ReturnType(processes process, String retur){
		this.process = process;
		toReturn = retur;
	}
	
	public processes getProcess() {
		return process;
	}
	
	public String getReturnValue() {
		return toReturn;
	}
	
	@Override
	public String toString() {
		return (toReturn == null) ? process.toString() :  process.toString() + " " + toReturn;
	}
}
