package assignment01;

import java.util.Optional;

public class VariableReferenceNode extends Node{
	private String variableName;
	private Optional<Node> indexExpression;

	VariableReferenceNode(String name) {
		this(name, Optional.empty());
	}

	VariableReferenceNode(String name, Optional<Node> expression) {
		variableName = name;
		indexExpression = expression;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(variableName);
		if (indexExpression.isPresent()) {
			sb.append("[");
			sb.append(indexExpression.get());
			sb.append("]");
		}

		return sb.toString();
	}
	
	public String getName() {
		return variableName;
	}
	public Optional<Node> getExpression() {
		return indexExpression;
	}


}
