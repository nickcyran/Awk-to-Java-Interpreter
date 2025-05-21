package assignment01;

import java.util.LinkedList;

public class ForEachNode extends StatementNode{
	private OperationNode inStatement;
	private BlockNode block;

	ForEachNode(OperationNode inStatement, BlockNode block){
		this.inStatement = inStatement;
		this.block = block;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("for (" + inStatement + ")");
		sb.append(block);
		return sb.toString();
	}
	
	public OperationNode getInStatement() {
		return inStatement;
	}
	
	public LinkedList<StatementNode> getStatements() {
		return block.getStatementNodes();
	}

}
