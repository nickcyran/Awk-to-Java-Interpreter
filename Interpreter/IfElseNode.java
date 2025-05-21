package assignment01;

import java.util.LinkedList;
import java.util.Optional;

public class IfElseNode extends StatementNode {
	Optional<Node> condition;
	Optional<IfElseNode> elseNext;
	BlockNode block;
	
	IfElseNode(BlockNode block){
		this(Optional.empty(), block, Optional.empty());
	}
	
	IfElseNode(Optional<Node> condition, BlockNode block){
		this(condition, block, Optional.empty());
	}
	
	IfElseNode(Optional<Node> condition, BlockNode block, Optional<IfElseNode> elseNext){
		this.condition = condition;
		this.block = block;
		this.elseNext = elseNext;
	}
	
	public Optional<Node> getCondition(){
		return condition;
	}
	
	public Optional<IfElseNode> getNext(){
		return elseNext;
	}
	
	public LinkedList<StatementNode> getStatements(){
		return block.getStatementNodes();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(condition.isPresent()) {
			sb.append("if(");
			sb.append(condition.get() + ")");
		}
		
		sb.append(block);
		
		if(elseNext.isPresent()) {
			sb.append("else ");
			sb.append(elseNext.get());
		}

		return sb.toString();
	}

}
