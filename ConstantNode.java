package assignment01;

public class ConstantNode extends Node {
	private String value;

	ConstantNode(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}