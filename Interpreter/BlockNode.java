package assignment01;

import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node {
	private LinkedList<StatementNode> statementNodes;
	private Optional<Node> condition;

	// techincally the block '{}' could exist and be valid
	BlockNode() {
		this(new LinkedList<>(), Optional.empty());
	}

	BlockNode(Optional<Node> condition) {
		this(new LinkedList<>(), condition);
	}

	// No conditions
	BlockNode(LinkedList<StatementNode> sNodes) {
		this(sNodes, Optional.empty());
	}

	BlockNode(LinkedList<StatementNode> sNodes, Optional<Node> condition) {
		statementNodes = sNodes;
		this.condition = condition;
	}
	
	public void setCondition(Optional<Node> con) {
		condition = con;
	}

	public LinkedList<StatementNode> getStatementNodes() {
		return statementNodes;
	}
	
	public Optional<Node> getCondition() {
		return condition;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (!condition.isEmpty()) {
			sb.append(condition);
		}
		
		sb.append("{\n" );
		
		for (Node s : statementNodes) {			
			sb.append(s);
			
			if (s instanceof OperationNode || s instanceof AssignmentNode) {
				sb.append(";");
			}
			sb.append("\n");
		}
		sb.setLength(sb.length() - 1);
		
		sb.append("\n}");
		
		return sb.toString();
	}
}
