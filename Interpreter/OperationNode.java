package assignment01;

import java.util.EnumMap;
import java.util.Optional;

enum operations {
	EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR, PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS,
	UNARYNEG, IN, EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION
}

public class OperationNode extends StatementNode {

	private Node left;
	private operations operator;
	private Optional<Node> right;
	
	// Create a static shared EnumMap [only create it once]
    private static final EnumMap<operations, String> opStringMap = fillOperators();

	OperationNode(Node left, operations op) {
	    this(left, op, Optional.empty());
	}

	OperationNode(Node left, operations op, Optional<Node> right) {
	    this.left = left;
	    operator = op;
	    this.right = right;
	}
	
	public operations getOperator() {
		return operator;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public Optional<Node> getRight() {
		return right;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (right.isEmpty()) {
			if (operator == operations.POSTDEC || operator == operations.POSTINC) {
				sb.append(left).append(opStringMap.get(operator));
			} else {
				sb.append(opStringMap.get(operator)).append(left);
			}
		} else {
			sb.append(left).append(" " + opStringMap.get(operator) + " ");
			sb.append(right.get());
		}
		sb.append(")");

		return sb.toString();
	}

	//for printing purposes
	private static EnumMap<operations, String> fillOperators() {
		EnumMap<operations, String> operatorStringMap;
		operatorStringMap = new EnumMap	<>(operations.class);
		operatorStringMap.put(operations.EQ, "==");
		operatorStringMap.put(operations.NE, "!=");
		operatorStringMap.put(operations.LT, "<");
		operatorStringMap.put(operations.LE, "<=");
		operatorStringMap.put(operations.GT, ">");
		operatorStringMap.put(operations.GE, ">=");
		operatorStringMap.put(operations.AND, "&&");
		operatorStringMap.put(operations.OR, "||");
		operatorStringMap.put(operations.NOT, "!");
		operatorStringMap.put(operations.MATCH, "~");
		operatorStringMap.put(operations.NOTMATCH, "!~");
		operatorStringMap.put(operations.DOLLAR, "$");
		operatorStringMap.put(operations.PREINC, "++");
		operatorStringMap.put(operations.POSTINC, "++");
		operatorStringMap.put(operations.PREDEC, "--");
		operatorStringMap.put(operations.POSTDEC, "--");
		operatorStringMap.put(operations.UNARYPOS, "+");
		operatorStringMap.put(operations.UNARYNEG, "-");
		operatorStringMap.put(operations.IN, "in");
		operatorStringMap.put(operations.EXPONENT, "^");
		operatorStringMap.put(operations.ADD, "+");
		operatorStringMap.put(operations.SUBTRACT, "-");
		operatorStringMap.put(operations.MULTIPLY, "*");
		operatorStringMap.put(operations.DIVIDE, "/");
		operatorStringMap.put(operations.MODULO, "%");
		operatorStringMap.put(operations.CONCATENATION, "");
		return operatorStringMap;
	}
	

}
