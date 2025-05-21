package assignment01;

public class AssignmentNode extends StatementNode{
	private Node target;
	private Node expression;
	
	AssignmentNode(Node targ, Node expr){
		target = targ;
		expression = expr;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(target + " = ");
		sb.append(expression);
		return sb.toString();
	}
	
	public Node getTarget() {
		return target;
	}
	public Node getExpression() {
		return expression;
	}

}
