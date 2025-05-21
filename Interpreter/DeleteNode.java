package assignment01;

public class DeleteNode extends StatementNode{
	private VariableReferenceNode toDelete;
	
	DeleteNode(VariableReferenceNode toDelete){
		this.toDelete = toDelete;
	}
	
	public VariableReferenceNode getToDelete() {
		return toDelete;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("delete ").append(toDelete);
		
		return sb.toString();
	}	
}
