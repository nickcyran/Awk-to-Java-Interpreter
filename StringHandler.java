package assignment01;

public class StringHandler {
	private String fileString;
	private int index;

	StringHandler(String file) {
		fileString = file;
		index = 0;
	}

	public char peek(int i) {
		return fileString.charAt(index + i);
	}

	public String peekString(int i) {
		return fileString.substring(index, index + i);
	}

	public char getChar() {
		char currentChar = fileString.charAt(index);
		index++;
		
		return currentChar;
	}

	public void swallow(int i) {
		index = index + i;
	}

	public boolean isDone() {
		return index >= fileString.length();
	}

	public String remainder() {
		return fileString.substring(index);
	}
}
