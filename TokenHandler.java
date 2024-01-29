package assignment01;

import java.util.LinkedList;
import java.util.Optional;

public class TokenHandler {
	private LinkedList<Token> tokenList;

	TokenHandler(LinkedList<Token> tokens) {
		tokenList = tokens;
	}

	public Optional<Token> peek(int j) {
		if (j < tokenList.size()) {
			return Optional.of(tokenList.get(j));
		} else {
			throw new IndexOutOfBoundsException("Incomplete Expression. No tokens Left to parse. Empty at index: " + j);
		}
	}

	public boolean moreTokens() {
		return !tokenList.isEmpty();
	}

	public Optional<Token> matchAndRemove(TokenTypes t) {
		
		TokenTypes currentType = peek(0).get().getType(); // typeOf first token in list
		return currentType == t ? Optional.of(tokenList.remove()) : Optional.empty(); // not found -> dont remove -> return empty
	} 
}
