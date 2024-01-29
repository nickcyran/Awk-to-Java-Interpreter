package assignment01;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.LinkedList;

public class LexerTests {
    private Lexer lexer;
    
    @Test
    public void testLex() {
        lexer = new Lexer("if (x > 0) { print(\"Hello, world!\"); }");        
        LinkedList<Token> tokens = lexer.lex();


        assertEquals(13, tokens.size());

        assertEquals(TokenTypes.IF, tokens.get(0).getType());
        assertEquals(TokenTypes.LEFTPARENTHESIS, tokens.get(1).getType());
        assertEquals(TokenTypes.WORD, tokens.get(2).getType());
        assertEquals(TokenTypes.GREATERTHAN, tokens.get(3).getType());
        assertEquals(TokenTypes.NUMBER, tokens.get(4).getType());
        assertEquals(TokenTypes.RIGHTPARENTHESIS, tokens.get(5).getType());
        assertEquals(TokenTypes.LEFTBRACE, tokens.get(6).getType());
        assertEquals(TokenTypes.PRINT, tokens.get(7).getType());
        assertEquals(TokenTypes.LEFTPARENTHESIS, tokens.get(8).getType());
        assertEquals(TokenTypes.STRINGLITERAL, tokens.get(9).getType());
        assertEquals(TokenTypes.RIGHTPARENTHESIS, tokens.get(10).getType());
        assertEquals(TokenTypes.SEPARATOR, tokens.get(11).getType());
        assertEquals(TokenTypes.RIGHTBRACE, tokens.get(12).getType());
    }
    
    @Test
    public void testStringLiteral() {
    	lexer = new Lexer("\"hello\"");
    	LinkedList<Token> tokens = lexer.lex();
    	
        assertEquals(TokenTypes.STRINGLITERAL, tokens.get(0).getType());
        assertEquals("hello", tokens.get(0).getValue());
    }

    @Test
    public void testPatterns() {
        lexer = new Lexer("`0-9*`");
    	LinkedList<Token> tokens = lexer.lex();
    	
        assertEquals(TokenTypes.REGEXPATTERN, tokens.get(0).getType());
        assertEquals("0-9*", tokens.get(0).getValue());
    }

    @Test
    public void testSingleSymbol() {
        lexer = new Lexer("{ } +");
    	LinkedList<Token> tokens = lexer.lex();
    	
        assertEquals(TokenTypes.LEFTBRACE, tokens.get(0).getType());
        assertEquals(TokenTypes.RIGHTBRACE, tokens.get(1).getType());
        assertEquals(TokenTypes.PLUS, tokens.get(2).getType());
    }
    
    @Test
    public void testDoubleSymbols() {
        lexer = new Lexer("++ == ||");
    	LinkedList<Token> tokens = lexer.lex();
    	
        assertEquals(TokenTypes.INCREMENT, tokens.get(0).getType());
        assertEquals(TokenTypes.EQUALTO, tokens.get(1).getType());
        assertEquals(TokenTypes.OR, tokens.get(2).getType());
    }

    @Test
    public void testNewTokens() {
    	lexer = new Lexer("This is test 123");
        LinkedList<Token> tokens = lexer.lex();
        
        assertEquals(TokenTypes.WORD, tokens.get(0).getType());
        assertEquals("This", tokens.get(0).getValue());
        
        assertEquals(TokenTypes.WORD, tokens.get(1).getType());
        assertEquals("is", tokens.get(1).getValue());
        
        assertEquals(TokenTypes.WORD, tokens.get(2).getType());
        assertEquals("test", tokens.get(2).getValue());
        
        assertEquals(TokenTypes.NUMBER, tokens.get(3).getType());
        assertEquals("123", tokens.get(3).getValue());
    }
    
    

    @Test
    public void testNewlineHandling() {
    	lexer = new Lexer("This is a test 123 \n");
        LinkedList<Token> tokens = lexer.lex();

        assertEquals(TokenTypes.SEPARATOR, tokens.get(5).getType());
    }

    @Test
    public void testReturnHandling() {
    	lexer = new Lexer("This is a \r \r \n test");
        LinkedList<Token> tokens = lexer.lex();
        
        assertEquals(5, tokens.size());
        
        assertEquals(TokenTypes.WORD, tokens.get(2).getType());
        assertEquals("a", tokens.get(2).getValue());
        
        assertEquals(TokenTypes.SEPARATOR, tokens.get(3).getType());
    }

    @Test
    public void testWordTokens() {
    	lexer = new Lexer("new test");
        LinkedList<Token> tokens = lexer.lex();
        
        assertEquals(2, tokens.size());
        
        assertEquals(TokenTypes.WORD, tokens.get(0).getType());
        assertEquals("new", tokens.get(0).getValue());
        
        assertEquals(TokenTypes.WORD, tokens.get(1).getType());
        assertEquals("test", tokens.get(1).getValue());
    }

    @Test
    public void testNumberTokens() {
    	lexer = new Lexer("This 23.4 .421 is a test 123\n");
        LinkedList<Token> tokens = lexer.lex();
 
        assertEquals(TokenTypes.NUMBER, tokens.get(1).getType());
        assertEquals("23.4", tokens.get(1).getValue());
        
        assertEquals(TokenTypes.NUMBER, tokens.get(2).getType());
        assertEquals(".421", tokens.get(2).getValue());
        
        assertEquals(TokenTypes.NUMBER, tokens.get(6).getType());
        assertEquals("123", tokens.get(6).getValue());
    }
   
    @Test
    public void testSymbolProximity() {
    	lexer = new Lexer("$1 {{");
        LinkedList<Token> tokens = lexer.lex();
        
        assertEquals(TokenTypes.DOLLAR, tokens.get(0).getType());
        
        assertEquals(TokenTypes.NUMBER, tokens.get(1).getType());
        assertEquals("1", tokens.get(1).getValue());
        
        assertEquals(TokenTypes.LEFTBRACE, tokens.get(2).getType());
        assertEquals(TokenTypes.LEFTBRACE, tokens.get(3).getType());
    }
    
    @Test
    public void testBacktickAtEnd() {
    	lexer = new Lexer("from `sasda`\nhappy `ad`");
    	lexer.lex();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingquoteNewLine() {
    	lexer = new Lexer("Hello \"rea\n test");
        lexer.lex();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingBackTickNewLine() {
    	lexer = new Lexer("Hello `rea\n test");
        lexer.lex();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUnrecognizedCharacters() {
    	lexer = new Lexer("ThisInvalid@Token");
        lexer.lex();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMultipleDecimals() {
    	lexer = new Lexer("Testign 1.2.33");
        lexer.lex();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingQuote() {
    	lexer = new Lexer("hello \"a");
        lexer.lex();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingBacktick() {
    	lexer = new Lexer("'a");
        lexer.lex();
    }
    
}