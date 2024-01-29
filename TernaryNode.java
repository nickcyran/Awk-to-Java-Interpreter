package assignment01;

public class TernaryNode extends Node{
	private Node booleanCondition;
	private Node trueCase;
	private Node falseCase;
	
	TernaryNode(Node bool, Node t, Node f){
		booleanCondition = bool;
		trueCase = t;
		falseCase = f;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(booleanCondition + " ? ");
		sb.append(trueCase + " : ");
		sb.append(falseCase);
		return sb.toString();
	}
	
	public Node getBooleanCondition() {
		return booleanCondition;	
	}
	
	public Node getTrueCase() {
		return trueCase;	
	}
	
	public Node getFalseCase() {
		return falseCase;	
	}
}
