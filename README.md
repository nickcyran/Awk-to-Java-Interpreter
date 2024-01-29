# Awk-to-Java-Interpreter
Awk to Java Interpreter. Using abstract syntax trees

How to Use?
Main takes command prompt arguments 1) awk file, 2) relevant text file (if any)

This program takes in an awk file, lexes it to return a list of tokens.
Then parses the list of tokens, creating an AST.
Th interpreter then traverses the AST running the awk program in java.
