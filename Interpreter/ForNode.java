package assignment01;

import java.util.LinkedList;
import java.util.Optional;

public class ForNode extends StatementNode {
	private Optional<Node> initial;
	private Optional<Node> condition;
	private Optional<Node> statement;
	private BlockNode block;

	ForNode(BlockNode block){
		this(Optional.empty(),Optional.empty(),Optional.empty(), block);
	}
	
	ForNode(Optional<Node> initial, Optional<Node> condition, Optional<Node> statement, BlockNode block) {
		this.initial = initial;
		this.condition = condition;
		this.statement = statement;
		this.block = block;
	}

	public Optional<Node> getInitial() {
		return initial;
	}
	
	public Optional<Node> getCondition() {
		return condition;
	}
	
	public Optional<Node> getIncriment() {
		return statement;
	}
	
	public LinkedList<StatementNode> getStatements() {
		return block.getStatementNodes();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("for(");

		if (getInitial().isPresent()) {
			sb.append(getInitial().get());
		}
		sb.append(";");
		if (condition.isPresent()) {
			sb.append(condition.get());
		}
		sb.append(";");
		if (statement.isPresent()) {
			sb.append(statement.get());
		}
		sb.append(")" + block);

		return sb.toString();
	}

}
