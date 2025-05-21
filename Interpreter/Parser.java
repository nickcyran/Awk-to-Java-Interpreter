package assignment01;

import java.util.LinkedList;
import java.util.Optional;

public class Parser {
	private TokenHandler tokenManager;

	Parser(LinkedList<Token> tokenList) {
		tokenManager = new TokenHandler(tokenList);
	}

	private boolean acceptSeparators() {
		boolean seperatorFound = false;

		while (tokenManager.moreTokens()) {
			Optional<Token> token = tokenManager.matchAndRemove(TokenTypes.SEPARATOR);
			
			// accept them in a chain. e.g) return 0; \n \n
			if (!token.isPresent()) {
				break;
			} else {
				seperatorFound = true;
			}
		}
		return seperatorFound;
	}

	public ProgramNode parse() {
		ProgramNode program = new ProgramNode();

		while (tokenManager.moreTokens()) {
			if (parseFunctionDefinition(program)) {
				acceptSeparators();

			} else if (parseAction(program)) {
				acceptSeparators();

			} else {
				awkCodeFormatError();
			}
		}
		return program;
	}

	private boolean parseFunctionDefinition(ProgramNode node) {
		if (currentTokenPresent(TokenTypes.FUNCTION)) {
			// varaibles to pass through FunctionDefinitionNode constructor
			String name = null;
			LinkedList<String> parameterList = null;
			LinkedList<StatementNode> statementList = null;

			Optional<Token> currentToken = tokenManager.matchAndRemove(TokenTypes.WORD);

			if (currentToken.isPresent()) {
				name = getValueFromOptional(currentToken);
			} else {
				awkCodeFormatError();
			}

			if (currentTokenPresent(TokenTypes.LEFTPARENTHESIS)) {
				parameterList = parseParameter();
			} else {
				awkCodeFormatError();
			}

			if (!currentTokenPresent(TokenTypes.RIGHTPARENTHESIS)) {
				awkCodeFormatError();
			}
			acceptSeparators();

			statementList = parseBlock().getStatementNodes();

			FunctionDefinitionNode thisFunction = new FunctionDefinitionNode(name, parameterList, statementList);
			node.addFunctionDefinitionNode(thisFunction);
		} else {
			return false;
		}
		return true;
	}

	private LinkedList<String> parseParameter() {
		LinkedList<String> parameterList = new LinkedList<>();

		while (tokenManager.moreTokens() && !peekAtEquals(0, TokenTypes.RIGHTPARENTHESIS)) {
			acceptSeparators();
			Optional<Token> name = tokenManager.matchAndRemove(TokenTypes.WORD);

			if (name.isPresent()) {
				parameterList.add(getValueFromOptional(name));
			} else {
				awkCodeFormatError();
			}
			acceptSeparators();

			if (currentTokenPresent(TokenTypes.COMMA)) {
				acceptSeparators();

				if (peekAtEquals(0, TokenTypes.RIGHTPARENTHESIS)) { // Peek because parse function will handle ')'
					awkCodeFormatError();
				}
			} else if (!peekAtEquals(0, TokenTypes.RIGHTPARENTHESIS)) {
				awkCodeFormatError();
			}
		}
		return parameterList;
	}

	private boolean parseAction(ProgramNode node) {
		if (currentTokenPresent(TokenTypes.BEGIN)) {
			node.addBeginBlockNode(parseBlock());
			return true;

		} else if (currentTokenPresent(TokenTypes.END)) {
			node.addEndBlockNode(parseBlock());
			return true;

		} else {
			var condition = parseOperation();
			BlockNode block = parseBlock();
			block.setCondition(condition);
			node.addOtherBlockNode(block);
		}
		return true;
	}

	private BlockNode parseBlock() {
		if (!currentTokenPresent(TokenTypes.LEFTBRACE)) {
			awkCodeFormatError();
		}
		acceptSeparators();

		LinkedList<StatementNode> statements = getStatements();

		if (!currentTokenPresent(TokenTypes.RIGHTBRACE)) {
			awkCodeFormatError();
		}

		return new BlockNode(statements);
	}

	// This function is called when checking certain statements that contain blocks
	// (i.e while(x){})
	// checking to see if there is a single statement it contains, (i.e while(x)
	// k++;)
	private BlockNode parseStatementBlock() {
		acceptSeparators();

		Optional<Node> statement = parseStatement();

		// single line code such as while(x) k--; is valid, must check
		if (statement.isPresent()) {
			LinkedList<StatementNode> single = new LinkedList<>();
			single.add((StatementNode) statement.get());

			if (!peekAtEquals(0, TokenTypes.RIGHTBRACE) && !acceptSeparators()) {
				awkCodeFormatError();
			}
			return new BlockNode(single);
		} else {
			return parseBlock();
		}
	}

	private LinkedList<StatementNode> getStatements() {
		LinkedList<StatementNode> statements = new LinkedList<>();

		// goes through each statement in block
		while (tokenManager.moreTokens()) {
			Optional<Node> statement = parseStatement();
			
			if (statement.isEmpty()) {
				break;
			}
			statements.add((StatementNode) statement.get());
			acceptSeparators();
		}
		return statements;
	}
	
	// protected for unit tests
	protected Optional<Node> parseStatement() {
		// No parse function for Continue or Break because its 1 line.
		if (currentTokenPresent(TokenTypes.CONTINUE)) {
			return Optional.of(new ContinueNode());
		}
		if (currentTokenPresent(TokenTypes.BREAK)) {
			return Optional.of(new BreakNode());
		}
		if (peekAtEquals(0, TokenTypes.IF)) {
			return Optional.of(parseIfElse().get());
		}
		if (currentTokenPresent(TokenTypes.FOR)) {
			return Optional.of(parseFor());
		}
		if (currentTokenPresent(TokenTypes.DELETE)) {
			return Optional.of(parseDelete());
		}
		if (currentTokenPresent(TokenTypes.WHILE)) {
			return Optional.of(parseWhile());
		}
		if (currentTokenPresent(TokenTypes.DO)) {
			return Optional.of(parseDoWhile());
		}
		if (currentTokenPresent(TokenTypes.RETURN)) {
			return Optional.of(parseReturn());
		}

		Optional<Node> statement = parseOperation();

		// Statements can only be of these types; i.e. k < 3 is not a valid statement 
		if (statement.isPresent()) {
			Object statementObject = statement.get();

			if (statementObject instanceof AssignmentNode || statementObject instanceof FunctionCallNode) {
				return statement;
			} else if (statementObject instanceof OperationNode op && isOperationStatement(op)) {
				return statement;
			} else {
				awkCodeFormatError();
			}
		}
		return Optional.empty();
	}

	private Optional<Node> parseLValue() {
		if (currentTokenPresent(TokenTypes.DOLLAR)) {
			return Optional.of(new OperationNode(parseBottomLevel().get(), operations.DOLLAR));
		}

		Optional<Token> current = tokenManager.matchAndRemove(TokenTypes.WORD);

		if (current.isPresent()) {
			String name = getValueFromOptional(current);
			Optional<Node> operation = Optional.empty();

			// an arrayVariable MUST have an index (ex: array[x])
			if (tokenManager.moreTokens() && currentTokenPresent(TokenTypes.LEFTBRACKET)) {
				operation = parseOperation();
				if (operation.isEmpty() || !currentTokenPresent(TokenTypes.RIGHTBRACKET))
					awkCodeFormatError();
			}
			return Optional.of(new VariableReferenceNode(name, operation));
		}
		return Optional.empty();
	}

	private Optional<Node> parseBottomLevel() {
		Optional<Token> current; // first 3 use the value of the result. must be saved

		// if found return the info in its coreesponding Node
		current = tokenManager.matchAndRemove(TokenTypes.STRINGLITERAL);
		if (current.isPresent()) {
			return Optional.of(new ConstantNode(getValueFromOptional(current)));
		}

		current = tokenManager.matchAndRemove(TokenTypes.NUMBER);
		if (current.isPresent()) {
			return Optional.of(new ConstantNode(getValueFromOptional(current)));
		}

		current = tokenManager.matchAndRemove(TokenTypes.REGEXPATTERN);
		if (current.isPresent()) {
			return Optional.of(new PatternNode(getValueFromOptional(current)));
		}

		// Parenthesis have their own operations and must be parsed as a whole
		if (currentTokenPresent(TokenTypes.LEFTPARENTHESIS)) {
			Optional<Node> operation = parseOperation();
			if (currentTokenPresent(TokenTypes.RIGHTPARENTHESIS))
				return operation;
		}

		// setting the operation type within an operation Node
		if (currentTokenPresent(TokenTypes.NOT)) {
			Optional<Node> operation = parseOperation();
			return Optional.of(new OperationNode(operation.get(), operations.NOT));
		}

		if (currentTokenPresent(TokenTypes.MINUS)) {
			Optional<Node> operation = parseOperation();
			return Optional.of(new OperationNode(operation.get(), operations.UNARYNEG));
		}

		if (currentTokenPresent(TokenTypes.PLUS)) {
			Optional<Node> operation = parseOperation();
			return Optional.of(new OperationNode(operation.get(), operations.UNARYPOS));
		}

		if (currentTokenPresent(TokenTypes.INCREMENT)) {
			Optional<Node> operation = parseLValue();
			return Optional.of(new OperationNode(operation.get(), operations.PREINC));
		}

		if (currentTokenPresent(TokenTypes.DECREMENT)) {
			Optional<Node> operation = parseLValue();
			return Optional.of(new OperationNode(operation.get(), operations.PREDEC));
		}

		Optional<Node> functionCall = parseFunctionCall();
		if (functionCall.isPresent()) {
			return functionCall;
		}

		return parseLValue();
	}

	private Optional<Node> parseOperation() {
		return parseAssignment();
	}

	private Optional<Node> parseAssignment() {
		Optional<Node> left = parseTernary();

		if (left.isPresent()) {
			operations operator = null;

			if (currentTokenPresent(TokenTypes.EXPONENTASSIGN)) {
				operator = operations.EXPONENT;
			} else if (currentTokenPresent(TokenTypes.MODULUSASSIGN)) {
				operator = operations.MODULO;
			} else if (currentTokenPresent(TokenTypes.MULTIPLYASSIGN)) {
				operator = operations.MULTIPLY;
			} else if (currentTokenPresent(TokenTypes.DIVIDEASSIGN)) {
				operator = operations.DIVIDE;
			} else if (currentTokenPresent(TokenTypes.ADDASSIGN)) {
				operator = operations.ADD;
			} else if (currentTokenPresent(TokenTypes.SUBTRACTASSIGN)) {
				operator = operations.SUBTRACT;
			} else if (currentTokenPresent(TokenTypes.EQUALS)) {
				operator = operations.EQ; // set as EQ but not used!!! just to have non-null value
			}

			if (operator != null) {
				Optional<Node> right = parseOperation();

				if (right.isEmpty()) {
					awkCodeFormatError();
				}

				if (operator == operations.EQ) { // Just equals sign make node w/ just right
					return Optional.of(new AssignmentNode(left.get(), right.get()));
				}
				return Optional.of(new AssignmentNode(left.get(), new OperationNode(left.get(), operator, right)));
			}
		}
		return left;
	}

	private Optional<Node> parseTernary() {
		Optional<Node> condition = parseOr();

		if (currentTokenPresent(TokenTypes.QUESTIONMARK)) {
			tokenManager.matchAndRemove(TokenTypes.QUESTIONMARK);

			Optional<Node> trueExpression = parseTernary(); // checks for nested ternary

			if (currentTokenPresent(TokenTypes.COLON)) {
				tokenManager.matchAndRemove(TokenTypes.COLON);

				Optional<Node> falseExpression = parseTernary();

				return Optional.of(new TernaryNode(condition.get(), trueExpression.get(), falseExpression.get()));
			} else {
				awkCodeFormatError();
			}
		}

		return condition;
	}

	private Optional<Node> parseOr() {
		Optional<Node> left = parseAnd();

		while (tokenManager.moreTokens() && currentTokenPresent(TokenTypes.OR)) {
			Optional<Node> right = parseAnd();

			if (right.isEmpty()) {
				awkCodeFormatError();
			}

			left = Optional.of(new OperationNode(left.get(), operations.OR, right));
		}
		return left;
	}

	private Optional<Node> parseAnd() {
		Optional<Node> left = parseArrayMembership();

		while (tokenManager.moreTokens() && currentTokenPresent(TokenTypes.AND)) {
			Optional<Node> right = parseArrayMembership();

			if (right.isEmpty()) {
				awkCodeFormatError();
			}

			left = Optional.of(new OperationNode(left.get(), operations.AND, right));
		}
		return left;
	}

	private Optional<Node> parseArrayMembership() {
		Optional<Node> left = parseEREMatch();

		if (currentTokenPresent(TokenTypes.IN)) {
			Optional<Node> right = parseLValue();

			// in only used by arrays; only allow variableReference
			if (right.isEmpty() || !(right.get() instanceof VariableReferenceNode) || !(left.get() instanceof VariableReferenceNode)) {
				awkCodeFormatError();
			}
			left = Optional.of(new OperationNode(left.get(), operations.IN, right));
		}
		return left;
	}

	private Optional<Node> parseEREMatch() {
		Optional<Node> left = parseBooleanOperators();

		operations operator = operations.MATCH;
		if (!currentTokenPresent(TokenTypes.MATCH)) {
			operator = operations.NOTMATCH;
			if (!currentTokenPresent(TokenTypes.NOMATCH)) {
				return left;
			}
		}
		Optional<Node> right = parseBottomLevel();

		// only used by regex; only allow regexs
		if (right.isEmpty() || !(right.get() instanceof PatternNode)) {
			awkCodeFormatError();
		}

		return Optional.of(new OperationNode(left.get(), operator, right));
	}

	private Optional<Node> parseBooleanOperators() {
		Optional<Node> left = parseConcatination();
		
		operations operator;
		if (currentTokenPresent(TokenTypes.LESSTHAN)) {
			operator = operations.LT;
		} else if (currentTokenPresent(TokenTypes.LESSOREQUAL)) {
			operator = operations.LE;
		} else if (currentTokenPresent(TokenTypes.NOTEQUALTO)) {
			operator = operations.NE;
		} else if (currentTokenPresent(TokenTypes.EQUALTO)) {
			operator = operations.EQ;
		} else if (currentTokenPresent(TokenTypes.GREATERTHAN)) {
			operator = operations.GT;
		} else if (currentTokenPresent(TokenTypes.GREATEROREQUAL)) {
			operator = operations.GE;
		} else {
			return left;
		}

		if (operator != null) {
			Optional<Node> right = parseConcatination();
			if (right.isEmpty()) {
				awkCodeFormatError();
			}
			left = Optional.of(new OperationNode(left.get(), operator, right));
		}
		return left;
	}

	private Optional<Node> parseConcatination() {
		Optional<Node> left = expression();
		Optional<Node> right;

		if (left.isPresent() && catType(left.get()) && (right = expression()).isPresent()) {
			left = Optional.of(new OperationNode(left.get(), operations.CONCATENATION, right));
		}
		return left;
	}

	private Optional<Node> parsePostOperations() {
		Optional<Node> operation = parseBottomLevel();

		if (currentTokenPresent(TokenTypes.INCREMENT)) {
			return Optional.of(new OperationNode(operation.get(), operations.POSTINC));
		} else if (currentTokenPresent(TokenTypes.DECREMENT)) {
			return Optional.of(new OperationNode(operation.get(), operations.POSTDEC));
		} else {
			return operation;
		}
	}

	private Optional<Node> parseExponent() {
		Optional<Node> left = parsePostOperations();

		while (tokenManager.moreTokens() && currentTokenPresent(TokenTypes.EXPONENT)) {
			Optional<Node> right = parseExponent();
			if (right.isEmpty()) {
				awkCodeFormatError();
			}
			left = Optional.of(new OperationNode(left.get(), operations.EXPONENT, right));
		}

		return left;
	}

	private Optional<Node> expression() {
		Optional<Node> left = term();
		

		while (tokenManager.moreTokens()) {
			operations operator = operations.ADD;
			if (!currentTokenPresent(TokenTypes.PLUS)) {
				operator = operations.SUBTRACT;
				if (!currentTokenPresent(TokenTypes.MINUS)) {
					return left;
				}
			}
			Optional<Node> right = term();
			if (right.isEmpty()) {
				awkCodeFormatError();
			}
			left = Optional.of(new OperationNode(left.get(), operator, right));
		}
		return left;
	}

	private Optional<Node> term() {
		Optional<Node> left = parseExponent();

		while (tokenManager.moreTokens()) {
			operations operator = operations.MULTIPLY;

			if (!currentTokenPresent(TokenTypes.MULTIPLY)) {
				operator = operations.DIVIDE;
				if (!currentTokenPresent(TokenTypes.DIVIDE)) {
					operator = operations.MODULO;
					if (!currentTokenPresent(TokenTypes.MODULUS)) {
						return left;
					}
				}
			}
			Optional<Node> right = parseExponent();
			if (right.isEmpty()) {
				awkCodeFormatError();
			}
			left = Optional.of(new OperationNode(left.get(), operator, right));
		}
		return left;
	}

	// Certain operations are statements, return true if its a valid statement
	private boolean isOperationStatement(OperationNode op) {
		operations operator = op.getOperator();
		switch (operator) {
		case POSTDEC, PREINC, PREDEC, POSTINC:
			return true;
		default:
			return false;
		}
	}

	private Optional<Node> parseCondition() {
		Optional<Node> condition;

		if (!currentTokenPresent(TokenTypes.LEFTPARENTHESIS)) {
			awkCodeFormatError();
		}

		condition = parseOperation();

		// conditions for statements must ALWAYS be populated
		if (condition.isEmpty()) {
			awkCodeFormatError();
		}

		if (!currentTokenPresent(TokenTypes.RIGHTPARENTHESIS)) {
			awkCodeFormatError();
		}
		return condition;
	}

	private StatementNode parseFor() {
		// for loops have 3 fields; these statments correspond
		Optional<Node> statement1;
		Optional<Node> statement2 = Optional.empty();
		Optional<Node> statement3 = Optional.empty();

		boolean forEach = false;

		if (!currentTokenPresent(TokenTypes.LEFTPARENTHESIS)) {
			awkCodeFormatError();
		}

		statement1 = parseOperation();

		// flag if contains operation 'in'
		if (statement1.isPresent() && statement1.get() instanceof OperationNode op) {
			if (op.getOperator() == operations.IN) {
				forEach = true;
			}
		} else {
			// any statement CAN be empty this will account for infinite loop for(;;)
			if (!currentTokenPresent(TokenTypes.SEPARATOR)) {
				awkCodeFormatError();
			}
			statement2 = parseOperation();
			if (!currentTokenPresent(TokenTypes.SEPARATOR)) {
				awkCodeFormatError();
			}
			statement3 = parseOperation();
		}

		if (!currentTokenPresent(TokenTypes.RIGHTPARENTHESIS)) {
			awkCodeFormatError();
		}

		BlockNode block = parseStatementBlock();

		// return a forEachNode if it was found, otherwise return proper forNode
		return forEach ? new ForEachNode((OperationNode)statement1.get(), block)
				: new ForNode(statement1, statement2, statement3, block);
	}

	private DeleteNode parseDelete() {
		// NOTE: Piaza post said ok to skip multi dimensional i.e a[1,2]
		Optional<Node> arrayReference = parseLValue();

		// delete only works with references to arrays
		if (arrayReference.isEmpty() || !(arrayReference.get() instanceof VariableReferenceNode)) {
			awkCodeFormatError();
		}

		return new DeleteNode((VariableReferenceNode) arrayReference.get());
	}

	private Optional<IfElseNode> parseIfElse() {
		Optional<Node> condition = Optional.empty();

		if (currentTokenPresent(TokenTypes.IF)) {
			condition = parseCondition();
		}

		BlockNode block = parseStatementBlock();

		if (tokenManager.moreTokens() && currentTokenPresent(TokenTypes.ELSE)) {
			// Recursively call this, recursion ends if not else IF
			Optional<IfElseNode> nextElse = parseIfElse();
			return Optional.of(new IfElseNode(condition, block, nextElse));
		}
		return Optional.of(new IfElseNode(condition, block));
	}

	private WhileNode parseWhile() {
		Optional<Node> condition = parseCondition();
		return new WhileNode(false, condition.get(), parseStatementBlock());
	}

	private WhileNode parseDoWhile() {
		BlockNode block = parseStatementBlock();

		if (!currentTokenPresent(TokenTypes.WHILE)) {
			awkCodeFormatError();
		}
		Optional<Node> condition = parseCondition();

		return new WhileNode(true, condition.get(), block);
	}

	private ReturnNode parseReturn() {
		Optional<Node> valueToReturn = parseOperation();

		if (valueToReturn.isEmpty()) {
			awkCodeFormatError();
		}
		return new ReturnNode(valueToReturn.get());
	}

	public Optional<Node> parseFunctionCall() {
		String name = null;
		LinkedList<Node> parameterList = new LinkedList<>();
		
		if (currentTokenPresent(TokenTypes.GETLINE)) {
			var param = parseOperation();
			if(param.isPresent()) {	// the operation > is valid for this function
				parameterList.add(param.get());
			}
			return Optional.of(new FunctionCallNode("getline", parameterList));
		}
		if (currentTokenPresent(TokenTypes.PRINT)) {
			addParametersToList(parameterList);
			return Optional.of(new FunctionCallNode("print", parameterList));
		}
		if (currentTokenPresent(TokenTypes.PRINTF)) {
			addParametersToList(parameterList);
			return Optional.of(new FunctionCallNode("printf", parameterList));
		}
		if (currentTokenPresent(TokenTypes.EXIT)) {	//takes a single non-parenthesised param
			var param = parseOperation();
			if(param.isPresent()) {
				parameterList.add(param.get());
			}
			return Optional.of(new FunctionCallNode("exit", parameterList));
		}
		if (currentTokenPresent(TokenTypes.NEXTFILE)) {
			return Optional.of(new FunctionCallNode("nextfile", parameterList));
		}
		if (currentTokenPresent(TokenTypes.NEXT)) {
			return Optional.of(new FunctionCallNode("next", parameterList));
		}
		
		if (peekAtEquals(0, TokenTypes.WORD) && peekAtEquals(1, TokenTypes.LEFTPARENTHESIS)) {
			// get function name
			Optional<Token> current = tokenManager.matchAndRemove(TokenTypes.WORD);
			name = getValueFromOptional(current);

			if (currentTokenPresent(TokenTypes.LEFTPARENTHESIS)) {
				while (tokenManager.moreTokens() && !peekAtEquals(0, TokenTypes.RIGHTPARENTHESIS)) {
					acceptSeparators();
					var currentParam = parseOperation();

					if (currentParam.isPresent()) {
						parameterList.add(currentParam.get());
					} else {
						awkCodeFormatError();
					}
					acceptSeparators();

					if (currentTokenPresent(TokenTypes.COMMA)) {
						acceptSeparators();

						if (peekAtEquals(0, TokenTypes.RIGHTPARENTHESIS)) { // Peek -> parse function will handle ')'
							awkCodeFormatError();
						}
					} else if (!peekAtEquals(0, TokenTypes.RIGHTPARENTHESIS)) {
						awkCodeFormatError();
					}
				}

				if (currentTokenPresent(TokenTypes.RIGHTPARENTHESIS)) {
					return Optional.of(new FunctionCallNode(name, parameterList));

				} else {
					awkCodeFormatError();
				}
			}
		}
		return Optional.empty();
	}
	
	private void addParametersToList(LinkedList<Node> list) {
		currentTokenPresent(TokenTypes.LEFTPARENTHESIS);
		var currentParam = parseOperation();
		
		while(currentParam.isPresent()) {
			list.add(currentParam.get());
			
			if(currentTokenPresent(TokenTypes.SEPARATOR) || currentTokenPresent(TokenTypes.RIGHTPARENTHESIS)) {	//implies parenthesis-less functions are done
				break;
			}
			
			if (!currentTokenPresent(TokenTypes.COMMA)) {
				awkCodeFormatError();
			}
			if(currentTokenPresent(TokenTypes.SEPARATOR) || currentTokenPresent(TokenTypes.RIGHTPARENTHESIS)) {	//cant end on ,
				awkCodeFormatError();
			}
			currentParam = parseOperation();
		}
	}

	private boolean currentTokenPresent(TokenTypes t) {
		if (!tokenManager.moreTokens())
			awkCodeFormatError();

		return tokenManager.matchAndRemove(t).isPresent();
	}

	// checks if any token is a match w/out removing it
	private boolean peekAtEquals(int i, TokenTypes t) {
		Token currentToken = tokenManager.peek(i).get();
		return currentToken.getType() == t;
	}

	private String getValueFromOptional(Optional<Token> t) {
		return t.get().getValue();
	}

	// create method for errors so code blocks are easier to look at
	private void awkCodeFormatError() {
		Token t = tokenManager.peek(0).get();
		throw new IllegalArgumentException(
				"Code does not match AWK format. Line: " + t.getLineNumber() + " Pos: " + t.getStartPosition());
	}

	// Checks if object is one that can be concatinated
	private boolean catType(Node n) {
		if (n instanceof OperationNode op) {
			if (op.getOperator() == operations.DOLLAR) {
				return true;
			}
		} else if (n instanceof ConstantNode || n instanceof VariableReferenceNode) {
			return true;
		}
		return false;
	}

}
