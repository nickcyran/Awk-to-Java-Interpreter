package assignment01;

public class Token {
	private TokenTypes tokenType;
	private String tokenValue;
	private int lineNumber;
	private int startPosition;

	Token(TokenTypes tokenType, int lineNumber, int charPos) {
		this.tokenType = tokenType;
		this.lineNumber = lineNumber;
		startPosition = charPos;
	}

	Token(TokenTypes tokenType, int lineNumber, int charPos, String value) {
		this(tokenType, lineNumber, charPos);
		tokenValue = value;
	}
	
	@Override
	public String toString() { // if applicable print out tokenValue; else Only print out the type
		return tokenValue != null ? tokenType + " (" + tokenValue + ")" : tokenType.toString();
	}
	
	// currently only for testing unit tests
	public TokenTypes getType() {
		return tokenType;
	}
	
	public String getValue() {
		return tokenValue;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public int getStartPosition() {
		return startPosition;
	}

}
