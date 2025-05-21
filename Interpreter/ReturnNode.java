package assignment01;

import java.util.Optional;

public class ReturnNode extends StatementNode{
	private Optional<Node> valueToReturn;
	
	ReturnNode(){
		valueToReturn = Optional.empty();
	}
	
	ReturnNode(Node valueToReturn){
		this.valueToReturn =  Optional.of(valueToReturn);
	}
	
	public Optional<Node> getValue(){
		return valueToReturn;
	}
	
	@Override
	public String toString() {
		return "return " + valueToReturn + ";";
	}

}
