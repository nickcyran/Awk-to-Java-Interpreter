package assignment01;

import java.util.LinkedList;

public class ProgramNode extends Node {
	private LinkedList<BlockNode> beginBlockNodes;
	private LinkedList<BlockNode> endBlockNodes;
	private LinkedList<BlockNode> otherBlockNodes;
	private LinkedList<FunctionDefinitionNode> functionDefinitionNodes;

	public ProgramNode() {
		beginBlockNodes = new LinkedList<>();
		endBlockNodes = new LinkedList<>();
		otherBlockNodes = new LinkedList<>();
		functionDefinitionNodes = new LinkedList<>();
	}

	public void addBeginBlockNode(BlockNode node) {
		beginBlockNodes.add(node);
	}

	public void addEndBlockNode(BlockNode node) {
		endBlockNodes.add(node);
	}

	public void addOtherBlockNode(BlockNode node) {
		otherBlockNodes.add(node);
	}

	public void addFunctionDefinitionNode(FunctionDefinitionNode node) {
		functionDefinitionNodes.add(node);
	}

	public LinkedList<FunctionDefinitionNode> getFunctions() {
		return functionDefinitionNodes;
	}

	public LinkedList<BlockNode> getBeginBlocks() {
		return beginBlockNodes;
	}

	public LinkedList<BlockNode> getEndBlocks() {
		return endBlockNodes;
	}

	public LinkedList<BlockNode> getOtherBlocks() {
		return otherBlockNodes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Begin Blocks:\n");
		for (BlockNode node : beginBlockNodes) {
			sb.append(node + "\n");
		}
		sb.append("-------------------------\n");

		sb.append("End Blocks:\n");
		for (BlockNode node : endBlockNodes) {
			sb.append(node + "\n");
		}
		sb.append("-------------------------\n");

		sb.append("Other Blocks:\n");
		for (BlockNode node : otherBlockNodes) {
			sb.append(node + "\n");
		}
		sb.append("-------------------------\n");

		sb.append("Function Definitions:\n");
		for (FunctionDefinitionNode node : functionDefinitionNodes) {
			sb.append(node + "\n");
		}
		sb.append("-------------------------\n");

		return sb.toString();
	}
}