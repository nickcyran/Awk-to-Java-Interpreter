package assignment01;

import java.util.LinkedList;

public class FunctionDefinitionNode extends Node implements DefineFunction{
	private String functionName;
	private LinkedList<String> parameterNames;
	private LinkedList<StatementNode> statementNodes;
	
	FunctionDefinitionNode(String name){
		functionName = name;
		parameterNames = new LinkedList<>();
		statementNodes = new LinkedList<>();
	}	
	
	FunctionDefinitionNode(String name, LinkedList<String> pNames,LinkedList<StatementNode> sNodes){
		functionName = name;
		parameterNames = pNames;
		statementNodes = sNodes;
	}	
	
	public String getName() {
		return functionName;
	}
	
	public LinkedList<StatementNode> getStatements(){
		return statementNodes;
	}
	
	@Override
	public LinkedList<String> getParameterNames() {
		return parameterNames;
	}
	
	@Override
	public String toString() {
		 StringBuilder sb = new StringBuilder();
		    sb.append("function ").append(functionName).append("(");

		    for (String s : parameterNames) {
		        sb.append(s).append(", ");
		    }

		    if (!parameterNames.isEmpty()) {
		        sb.setLength(sb.length() - 2);
		    }

		    sb.append("){");
		    
		    for (StatementNode s : statementNodes) {
		        sb.append("\n" + s + ";");
		    }
		    sb.setLength(sb.length() - 1);
		    sb.append("\n}");
		    return sb.toString();
		}
}
