package assignment01;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Optional;

public class TokenHandlerTest {
	private TokenHandler tokenHandler;

	@Before
	public void setUpListForTests() {
		LinkedList<Token> tokens = new LinkedList<>();

		tokens.add(new Token(TokenTypes.RIGHTPARENTHESIS, 0, 0)); // token pos negligible ATM
		tokens.add(new Token(TokenTypes.SEPARATOR, 0, 0));
		tokens.add(new Token(TokenTypes.BEGIN, 0, 0));
		tokenHandler = new TokenHandler(tokens);
	}

	@Test
	public void testPeek() {
		Optional<Token> token = tokenHandler.peek(0);
		
		assertTrue(token.isPresent());	//check presence as to not get null in next section
		assertEquals(TokenTypes.RIGHTPARENTHESIS, token.get().getType());

		token = tokenHandler.peek(1);
		assertTrue(token.isPresent());
		assertEquals(TokenTypes.SEPARATOR, token.get().getType());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testOutOfBounds() {
		tokenHandler.peek(3);
	}

	@Test
	public void testMoreTokens() {
		assertTrue(tokenHandler.moreTokens());

		// Remove all tokens, and then check if moreTokens returns false
		tokenHandler.matchAndRemove(TokenTypes.RIGHTPARENTHESIS);
		tokenHandler.matchAndRemove(TokenTypes.SEPARATOR);
		tokenHandler.matchAndRemove(TokenTypes.BEGIN);

		assertFalse(tokenHandler.moreTokens());
	}

	@Test
	public void testMatchAndRemove() {
		Optional<Token> token = tokenHandler.matchAndRemove(TokenTypes.RIGHTPARENTHESIS);

		assertTrue(token.isPresent());
		assertEquals(TokenTypes.RIGHTPARENTHESIS, token.get().getType());

		token = tokenHandler.matchAndRemove(TokenTypes.SEPARATOR);
		assertTrue(token.isPresent());
		assertEquals(TokenTypes.SEPARATOR, token.get().getType());

		//test a false
		token = tokenHandler.matchAndRemove(TokenTypes.LEFTPARENTHESIS);
		assertFalse(token.isPresent());
		assertTrue(tokenHandler.moreTokens());
	}
}