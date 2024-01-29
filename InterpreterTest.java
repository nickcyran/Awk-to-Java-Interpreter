package assignment01;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.Test;

public class InterpreterTest {
	private Interpreter interpreter;
	private HashMap<String, InterpreterDataType> globalVariables;

	public void setup(String awkFile, String txtFile) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(awkFile)));
		var x = new Lexer(content).lex();
		ProgramNode pNode = new Parser(x).parse();

		interpreter = (txtFile != null) ? new Interpreter(pNode, Paths.get(txtFile)) : new Interpreter(pNode);
		globalVariables = interpreter.getGlobals();
	}

	public void printMyVars() {
		System.out.println("\u001B[36mMAP(global)\u001B[0m--------");
		globalVariables.forEach((x, y) -> {
			if (!x.equals("ORS") && !x.equals("OFMT") && !x.equals("FNR") && !x.equals("FS") && !x.equals("OFS")
					&& !x.equals("NR") && !x.equals("NF") && !x.equals("FILENAME")) {
				System.out.println("\u001B[33m" + x + ":\u001B[0m " + y);
			}
		});
		System.out.println("-------------------");
	}

	// tests conditional block using $ and conditions
	@Test
	public void testConditionalBlocks() throws IOException {
		System.out.println("[testConditionalBlocks]---------------");
		setup("testConditionalBlocks.awk", "conditionalInput.txt"); 
		interpreter.interpretProgram();
		
		var lastx = Integer.parseInt(globalVariables.get("x").getData());
		var i = Integer.parseInt(globalVariables.get("$1").getData());
		var j = Integer.parseInt(globalVariables.get("$2").getData());
		
		assertEquals(lastx, i + j);
		
		var total = Integer.parseInt(globalVariables.get("total").getData());
		assertEquals(total, 181);
		
		printMyVars();
	}

	
	// TESTS:
	// 1) A RECURSIVE FACTORIAL FUNCTION
	// 2) FUNCTION THAT TAKES A PARAM OF WORD THEN SPLITS THEN PRINTS EACH CHAR FROM AN ARRAY
	// 3) TESTING USING A FUNCTION CALL AS A PARAM
	// 4) FUNCTION THAT TAKES IN MULTIPLE PARAMETERS
	// 5) TESTS BUILT INS
	@Test
	public void testFunctions() throws IOException {
		System.out.println("[testFunctions]------------------------");
		setup("testFunctions.awk", null);
		interpreter.interpretProgram();
		
		var factOfSix = Integer.parseInt(globalVariables.get("factOfSix").getData());
		assertEquals(factOfSix, factorial(6));
		
		var addedFour = Integer.parseInt(globalVariables.get("addedFour").getData());
		assertEquals(addedFour, factorial(4) + 4);
		
		var repeated = globalVariables.get("repeated").getData();
		assertEquals(repeated, "-0".repeat(8));
		
		var characters = ((InterpreterArrayDataType)globalVariables.get("characters")).getMap();
		assertEquals(characters.get("0").getData(), "he");
		assertEquals(characters.get("1").getData(), "");
		assertEquals(characters.get("2").getData(), "o");
		
		var l_list = ((InterpreterArrayDataType)globalVariables.get("l_list")).getMap();
		l_list.values().forEach(x -> assertEquals(x.getData(), "l"));
		
		var len = Integer.parseInt(globalVariables.get("len").getData());
		assertEquals(len, 3);
		
		
		printMyVars();
	}
	
	public int factorial(int n) {
		return (n == 0) ? 1 : n*factorial(n-1);
	}
	
	// Tests using an input record 
	@Test
	public void testInputProcessing() throws IOException {
		System.out.println("[testInputProcessing]------------------------");
		setup("testInputProcessing.awk", "input.txt");
		interpreter.interpretProgram();
		
		var sum = Float.parseFloat(globalVariables.get("sum").getData());
		assertTrue(sum == 23.2f);
		
		var avgGPA = Float.parseFloat(globalVariables.get("avgGPA").getData());
		var numOfStudents = Float.parseFloat(globalVariables.get("FNR").getData());
		
		assertTrue(avgGPA == sum/numOfStudents);
	}

	// Tests: Assignment, Ternaries, And operations, Or operations, 
	// Multiplication, Subtraction, Exponentiation, Divison, Less than, Equal To, Greater Than
	@Test
	public void testMathAndLogic() throws IOException {
		System.out.println("[testMathAndLogic]------------------------");
		setup("testMathAndLogic.awk", null);
		interpreter.interpretProgram();
		printMyVars();

		assertEquals(globalVariables.get("xAsBool").getData(), "true");
		assertEquals(globalVariables.get("yAsBool").getData(), "false");

		assertEquals(globalVariables.get("first").getData(), "fail");
		assertEquals(globalVariables.get("second").getData(), "success");

		var k = Integer.parseInt(globalVariables.get("k").getData());
		assertEquals(k, 21);

		var j = Float.parseFloat(globalVariables.get("j").getData());
		assertTrue(j == 4.5);

		var w = Float.parseFloat(globalVariables.get("w").getData());
		assertTrue(w == k + j);

		assertEquals(globalVariables.get("n").getData(), "wow this is true!!!");
	}

	// This test's while loops & for loops.
	// 1) while loop with break condition tested
	// 2) for loop tested
	// 3) while loop inside a function with a return condition
	@Test
	public void testLoops() throws IOException {
		System.out.println("[testLoops]------------------------");
		setup("testLoops.awk", null);
		interpreter.interpretProgram();
		printMyVars();

		var x = Integer.parseInt(globalVariables.get("x").getData());
		var y = Integer.parseInt(globalVariables.get("y").getData());

		assertTrue(x > 15);
		assertTrue(y == 6);
		assertTrue(Integer.parseInt(globalVariables.get("k").getData()) == 10);
		assertEquals(globalVariables.get("fin").getData(), "fn returned after loop: " + (y + x));
	}
}
