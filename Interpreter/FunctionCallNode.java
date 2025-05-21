package assignment01;

import java.util.LinkedList;

public class FunctionCallNode extends StatementNode{
	private String functionName;
	private LinkedList<Node> params;
	
	FunctionCallNode(String functionName, LinkedList<Node> params){
		this.functionName = functionName;
		this.params = params;
	}
	
	public String getFunctionName() {
		return functionName;
	}
	
	public LinkedList<Node> getParams() {
		return params;
	}
	@Override
	public String toString() {
		// keeping as linkedList string, toString not necessity still gets point across
		return functionName + "(" + params + ")";
	}

}
