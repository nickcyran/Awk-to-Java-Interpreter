# Awk-to-Java-Interpreter 

![Status](https://img.shields.io/badge/status-completed-brightgreen)

This project is a Java-based interpreter designed to parse and execute AWK programs within the Java runtime environment (JRE). To achieve these results the program utilizes lexical analysis and Abstract Syntax Trees (AST), resulting in a reliable tool for running AWK logic within the Java framework.

## How it Works
**The AWK to Java interpretation pipeline is setup as such:**

1.  **Lexical Analysis (Lexing)**: The input AWK file is processed by a **Lexer**. This component analyzes the AWK code and breaks it down into a list of relevant tokens (e.g., keywords, identifiers, operators, and literals).
2.  **Parsing (Abstract Syntax Tree Generation)**: The list of tokens is then input to a **Parser** which employs a **recursive descent** parsing strategy to analyze the structure of the token stream. This process will result in an Abstract Syntax Tree (AST), which is a tree-like representation of the AWK program's structure.
3.  **Interpretation (Execution)**: Finally, the **Interpreter** traverses the AST executing the AWK logic by mapping them to corresponding Java operations.

Essentially, this program takes an AWK file, lexes it into tokens, parses those tokens into an AST using recursive descent, and then the interpreter traverses the AST to run the AWK code as Java.

## Features

* **AWK Language Subset Support**: Interprets a significant portion of the AWK programming language, including:
    * `BEGIN` and `END` blocks
    * Pattern-action blocks
    * Function definitions and calls (including built-in functions like `print`, `printf`, `getline`, `gsub`, `sub`, `match`, `length`, `substr`, `split`, `tolower`, `toupper`, `index`)
    * Flow control statements: `if-else`, `while`, `do-while`, `for`, `for-each` (in array), `break`, `continue`, `return`
    * Variable assignments and various arithmetic, logical, comparison, and string operations
    * Array support (associative arrays) and the `delete` statement
    * Ternary operations
    * Regular expression matching (`~`, `!~`) and pattern literals
* **Input File Handling**: Processes input files line by line, updating built-in variables like `NR`, `NF`, and `FNR`.
* **Modular Design**: Separated components for lexing, parsing, and interpretation, promoting maintainability and extensibility.

## How to Use 

The `Main` method serves as the entry point for the interpreter and accepts one or two command-line arguments:

1.  **AWK Code File (Required)**: Path to the `.awk` file containing the AWK script you want to execute.
2.  **Input Data File (Optional)**: If your AWK script processes an input file (e.g., for record processing), provide the path to this data file as the second argument.

**Example:** 
```bash 
java assignment01.Main your_script.awk input_data.txt
