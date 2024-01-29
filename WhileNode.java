package assignment01;

import java.util.LinkedList;

public class WhileNode extends StatementNode{
	private boolean doWhile;
	private Node condition;
	private BlockNode block;
	
	WhileNode(Node condition, BlockNode block){
		this(false, condition, block);
	}
	
	WhileNode(boolean doWhile, Node condition, BlockNode block){
		this.doWhile = doWhile;
		this.condition = condition;
		this.block = block;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(doWhile) {
			sb.append("do");
			sb.append(block);
			sb.append("while(" + condition + ")");
			return sb.toString();
		}
		
		sb.append("while(" + condition + ")");
		sb.append(block);
		return sb.toString();
	}
	
	public LinkedList<StatementNode> getStatements(){
		return block.getStatementNodes();
	}
	
	public Node getCondition(){
		return condition;
	}
	
	public boolean isDoWhile() {
		return doWhile;
	}

}
