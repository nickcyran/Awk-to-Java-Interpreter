# Awk-to-Java-Interpreter
Java-based program designed to interpret AWK programs as Java code. 
This tool utilizes lexical analysis and abstract syntax trees (AST) to seamlessly execute AWK programs within the Java environment.

This program takes in an AWK file then lexes it to return a list of tokens.
After which it parses the list of tokens, creating an AST.
The interpreter then traverses the AST running the the AWK code as Java.

## How to Use?
The Main method accepts 1-2 additional arguments:
  1) AWK code file
  2) The file the AWK code references (if any)
