# Awk-to-Java-Interpreter
Java-based program designed to interpret AWK programs as Java code. This tool utilizes lexical analysis and abstract syntax trees to seamlessly execute AWK programs within the Java environment.

<ins>How to Use?</ins>
Main takes command prompt arguments 1) awk file, 2) relevant text file (if any)

This program takes in an awk file, lexes it to return a list of tokens.
Then parses the list of tokens, creating an AST.
Th interpreter then traverses the AST running the awk program in java.
