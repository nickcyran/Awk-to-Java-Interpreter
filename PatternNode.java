package assignment01;

public class PatternNode extends Node{
	String pattern;
	
	PatternNode(String pattern){
		this.pattern = pattern;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("`");
		sb.append(pattern);
		sb.append("`");
		
		return sb.toString();
	}
	
	public String getPattern() {
		return pattern;
	}

}
