package assignment01;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTests {
	private Parser parser;

	public void setUp(String s) {
		Lexer lexer = new Lexer(s);
		parser = new Parser(lexer.lex());
	}

	// Not quite sure how to test these, so tests will mostly be done visually w/
	// strings
	// testing and proving that these statements WILL not run into errors
	@Test
	public void testParseFor() {
		setUp("for(;;) x++;");
		assertEquals(parser.parseStatement().get().toString(), "for(;;){\n" + "(x++);\n" + "}");

		setUp("for(x = 0; x < 10; x++) {k--}");
		assertEquals(parser.parseStatement().get().toString(), "for(x = 0;(x < 10);(x++)){\n(k--);\n}");
	}

	@Test
	public void testDelete() {
		setUp("delete x");
		assertEquals(parser.parseStatement().get().toString(), "delete x");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteOnNonArray() {
		setUp("delete 12");
		parser.parseStatement();
	}
	
	@Test
	public void testBreakAndContinue() {
		setUp("break");
		assertEquals(parser.parseStatement().get().toString(), "break;");
		
		setUp("continue");
		assertEquals(parser.parseStatement().get().toString(), "continue;");
	}
	
	@Test
	public void testReturn() {
		setUp("return x;");
		assertEquals(parser.parseStatement().get().toString(), "return Optional[x];");
	}
	
	@Test
	public void testWhile() {
		setUp("while(x) {k++; l--}");
		assertEquals(parser.parseStatement().get().toString(), "while(x){\n(k++);\n(l--);\n}");
		
		
		setUp("while(x > 10) ++j;");
		assertEquals(parser.parseStatement().get().toString(), "while((x > 10)){\n(++j);\n}");
	}
	
	@Test
	public void testDoWhile() {
		setUp("do{k++}while(x);");
		assertEquals(parser.parseStatement().get().toString(), "do{\n(k++);\n}while(x)");
	}
	
	@Test
	public void testElseIf() {
		setUp("if(x){x++}");
		assertEquals(parser.parseStatement().get().toString(), "if(x){\n(x++);\n}");
		
		setUp("if(y){y++}else{k--}");
		assertEquals(parser.parseStatement().get().toString(), "if(y){\n(y++);\n}else {\n(k--);\n}");
		
		setUp("if(a){return a;}else if(b){k--}else k++;");
		assertEquals(parser.parseStatement().get().toString(), "if(a){\nreturn Optional[a];\n}else if(b){\n(k--);\n}else {\n(k++);\n}");
	}

	@Test
	public void parseEntireCode() {
		setUp("BEGIN{k = 1; j = 40; array[x] = \"k\"}\n" + "function newFunction(x){\n" + "  for(i = 0; i < 3; i++){\n"
				+ "    x--;\n" + " }\n" + "}\n" + "\n" + "{for(;;){\n" + " k++\n" + " a = l ? 12 : 13\n" + " --j\n"
				+ "}\r\n" + "abc = 14;\n" + "}\n" + "\n" + "END{delete array[x]}");
		System.out.println(parser.parse());
	}

}
