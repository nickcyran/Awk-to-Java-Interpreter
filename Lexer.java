package assignment01;

import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {
	private StringHandler file;
	private int lineNumber;
	private int linePosition;
	private int startPos;

	private HashMap<String, TokenTypes> keywords;
	private HashMap<String, TokenTypes> twoCharSymbols;
	private HashMap<String, TokenTypes> symbols;

	Lexer(String value) {
		lineNumber = 1;
		linePosition = 1;

		file = new StringHandler(value); // pass file string through to StringHandler
		initiateHashMaps();
	}

	public void initiateHashMaps() {
		keywords = new HashMap<>();
		twoCharSymbols = new HashMap<>();
		symbols = new HashMap<>();

		keywords.put("while", TokenTypes.WHILE);
		keywords.put("if", TokenTypes.IF);
		keywords.put("do", TokenTypes.DO);
		keywords.put("for", TokenTypes.FOR);
		keywords.put("break", TokenTypes.BREAK);
		keywords.put("continue", TokenTypes.CONTINUE);
		keywords.put("else", TokenTypes.ELSE);
		keywords.put("return", TokenTypes.RETURN);
		keywords.put("BEGIN", TokenTypes.BEGIN);
		keywords.put("END", TokenTypes.END);
		keywords.put("print", TokenTypes.PRINT);
		keywords.put("printf", TokenTypes.PRINTF);
		keywords.put("next", TokenTypes.NEXT);
		keywords.put("in", TokenTypes.IN);
		keywords.put("delete", TokenTypes.DELETE);
		keywords.put("getline", TokenTypes.GETLINE);
		keywords.put("exit", TokenTypes.EXIT);
		keywords.put("nextfile", TokenTypes.NEXTFILE);
		keywords.put("function", TokenTypes.FUNCTION);

		twoCharSymbols.put(">=", TokenTypes.GREATEROREQUAL);
		twoCharSymbols.put("++", TokenTypes.INCREMENT);
		twoCharSymbols.put("--", TokenTypes.DECREMENT);
		twoCharSymbols.put("<=", TokenTypes.LESSOREQUAL);
		twoCharSymbols.put("==", TokenTypes.EQUALTO);
		twoCharSymbols.put("!=", TokenTypes.NOTEQUALTO);
		twoCharSymbols.put("^=", TokenTypes.EXPONENTASSIGN);
		twoCharSymbols.put("%=", TokenTypes.MODULUSASSIGN);
		twoCharSymbols.put("*=", TokenTypes.MULTIPLYASSIGN);
		twoCharSymbols.put("/=", TokenTypes.DIVIDEASSIGN);
		twoCharSymbols.put("+=", TokenTypes.ADDASSIGN);
		twoCharSymbols.put("-=", TokenTypes.SUBTRACTASSIGN);
		twoCharSymbols.put("!~", TokenTypes.NOMATCH);
		twoCharSymbols.put("&&", TokenTypes.AND);
		twoCharSymbols.put(">>", TokenTypes.APPEND);
		twoCharSymbols.put("||", TokenTypes.OR);

		symbols.put("{", TokenTypes.LEFTBRACE);
		symbols.put("}", TokenTypes.RIGHTBRACE);
		symbols.put("[", TokenTypes.LEFTBRACKET);
		symbols.put("]", TokenTypes.RIGHTBRACKET);
		symbols.put("(", TokenTypes.LEFTPARENTHESIS);
		symbols.put(")", TokenTypes.RIGHTPARENTHESIS);
		symbols.put("$", TokenTypes.DOLLAR);
		symbols.put("~", TokenTypes.MATCH);
		symbols.put("=", TokenTypes.EQUALS);
		symbols.put("<", TokenTypes.LESSTHAN);
		symbols.put(">", TokenTypes.GREATERTHAN);
		symbols.put("!", TokenTypes.NOT);
		symbols.put("+", TokenTypes.PLUS);
		symbols.put("^", TokenTypes.EXPONENT);
		symbols.put("-", TokenTypes.MINUS);
		symbols.put("?", TokenTypes.QUESTIONMARK);
		symbols.put(":", TokenTypes.COLON);
		symbols.put("*", TokenTypes.MULTIPLY);
		symbols.put("/", TokenTypes.DIVIDE);
		symbols.put("%", TokenTypes.MODULUS);
		symbols.put("|", TokenTypes.PIPE);
		symbols.put(",", TokenTypes.COMMA);
		symbols.put(";", TokenTypes.SEPARATOR);
	}

	public LinkedList<Token> lex() {
		LinkedList<Token> tokenList = new LinkedList<>();

		while (!file.isDone()) {
			startPos = linePosition; // track firstPosition for tokens

			switch (file.peek(0)) {
			case ' ', '\t':
				file.swallow(1);
				linePosition++;
				break;

			case '"':
				tokenList.add(processStringLiteral());
				linePosition++;
				break;

			case '`':
				tokenList.add(processPattern());
				linePosition++;
				break;

			case '#':
				while (!file.isDone() && file.peek(0) != '\n') { // go to end of line
					file.swallow(1);
				}
				break;

			case '\n':
				tokenList.add(new Token(TokenTypes.SEPARATOR, lineNumber, linePosition));
				file.swallow(1);
				linePosition = 1;
				lineNumber++;
				break;

			case '\r':
				file.swallow(1);
				break;

			default:
				tokenList.add(defaultCase()); // check criteria for Strings
			}
		}
		return tokenList;
	}

	private Token defaultCase() {
		// Check if number or decimal followed by number
		if (Character.isDigit(file.peek(0)) || (file.peek(0) == '.' && Character.isDigit(file.peek(1)))) {
			return processNumber();
		}
		if (Character.isLetter(file.peek(0))) {
			return processWord();
		}

		return processSymbol(); // if nothing else; then its probably symbol so check & return
	}

	private Token processWord() {
		StringBuilder tokenValue = new StringBuilder();

		while (!file.isDone() && Character.toString(file.peek(0)).matches("\\w")) { // same as regex "[A-Za-z0-9_]"
			char currentChar = file.getChar(); // Get the current char
			tokenValue.append(currentChar); // Add the current char to the string
			linePosition++;
		}

		if (keywords.containsKey(tokenValue.toString())) { // check if a keyword [i.e. for]
			return new Token(keywords.get(tokenValue.toString()), lineNumber, startPos);
		}

		return new Token(TokenTypes.WORD, lineNumber, startPos, tokenValue.toString()); // create a Word token
	}

	private Token processNumber() {
		StringBuilder tokenValue = new StringBuilder();
		boolean pointPresent = false;

		// keep going while there are numbers, and no decimal points accounted for
		while (!file.isDone() && (Character.isDigit(file.peek(0)) || (file.peek(0) == '.' && !pointPresent))) {
			if (file.peek(0) == '.') {
				pointPresent = true;
			}

			tokenValue.append(file.getChar());
			linePosition++;
		}
		// if more than one decimal present in number throw error
		if (pointPresent && file.peek(0) == '.') {
			throw new IllegalArgumentException("Invalid number: too many decimal points");
		}

		return new Token(TokenTypes.NUMBER, lineNumber, startPos, tokenValue.toString());
	}

	private Token processStringLiteral() {
		StringBuilder tokenValue = new StringBuilder();

		file.swallow(1);
		linePosition++; // skip first `

		while (!file.isDone()) {
			char currentChar = file.getChar();

			if (currentChar == '"') {
				return new Token(TokenTypes.STRINGLITERAL, lineNumber, startPos, tokenValue.toString());
			} else if (currentChar == '\\' && file.peek(0) == '"') {
				tokenValue.append('"'); // if escaped quote fond replace with normal quote
				file.swallow(1);
				linePosition += 2;
			} else if(currentChar == '\\' && file.peek(0) == 'n'){
				tokenValue.append('\n');
				file.swallow(1);
				linePosition += 2;
			}else {
				tokenValue.append(currentChar);
				linePosition++;
			}
		}
		throw new IllegalArgumentException("Missing end quote. First Pos: " + startPos + " Second Pos: ?");
	}

	private Token processPattern() {
		StringBuilder tokenValue = new StringBuilder();

		file.swallow(1);
		linePosition++; // skip first `

		while (!file.isDone()) {
			char currentChar = file.getChar();

			if (currentChar == '`') {
				return new Token(TokenTypes.REGEXPATTERN, lineNumber, startPos, tokenValue.toString());
			} else {
				tokenValue.append(currentChar);
				linePosition++;
			}

			if (currentChar == '\n' || file.isDone()) { // if new line or file is done and no end backtick throw error
				throw new IllegalArgumentException("Missing end backtick. First Pos: " + startPos + " Second Pos: ?");
			}
		}
		return null;
	}

	private Token processSymbol() {
		Token symbolToken = null;

		// Check hashmap for currentChar & nextChar; only if more than 1 char remainins
		if (file.remainder().length() > 1 && twoCharSymbols.containsKey(file.peekString(2))) {
			symbolToken = new Token(twoCharSymbols.get(file.peekString(2)), lineNumber, startPos); // make token hashVal
			file.swallow(2);
			linePosition += 2;

			// Check hashmap for currentChar
		} else if (symbols.containsKey(file.peekString(1))) {
			symbolToken = new Token(symbols.get(file.peekString(1)), lineNumber, startPos); // make token w/ hashvalue
			file.swallow(1);
			linePosition++;
		}

		if (symbolToken == null) { // only returns null if symbol is not accounted for [i.e \]
			throw new IllegalArgumentException("Unrecognized Character: " + file.peek(0));
		}

		return symbolToken;
	}

}
