package assignment01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	// lexer [takes: awk file, returns: list of tokens] -> 
	// parser [takes: list of tokens, returns: programNode] -> 
	// interpreter[takes: programNode (and txt file input), *runs the awk program*]
	public static void main(String[] args) throws IOException {
		Path awkFile = Paths.get(args[0]); 
		
		String content = new String(Files.readAllBytes(awkFile)); 
		var lexer = new Lexer(content).lex();
		ProgramNode pNode = new Parser(lexer).parse();
	
		Interpreter interpreter = (args.length > 1) ? new Interpreter(pNode, Paths.get(args[1])) : new Interpreter(pNode);

		interpreter.interpretProgram();
	}
}
