package expressions;

import java.util.List;

import functions.FunctionTail;
import kaskell.Instructions;
import kaskell.SymbolTable;
import types.Type;

public class Call implements Expression {
	private Identifier identifier;
	private List<Expression> variables;
	private List<Type> arguments;
	private Type type;
	private FunctionTail address;

	public Call(Identifier identifier, List<Expression> arguments) {
		this.identifier = identifier;
		this.variables = arguments;
		this.type = null;
		this.arguments = null;
		this.address = null;
	}

	@Override
	public boolean checkType() {
		if ((variables != null && arguments != null) && (variables.size() == arguments.size())) {
			for (int i = 0; i < variables.size(); i++) {
				if (variables.get(i).checkType() && (!(variables.get(i).getType().equals(arguments.get(i)))
						|| !(arguments.get(i).equals(variables.get(i).getType())))) {
					int num = i + 1;
					System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column "
							+ (this.getColumn() + 1) + " the variable type number " + num
							+ " does not match with the argument type number " + num + "!");
					return false;
				}
			}
			return true;
		} else if (variables == null && arguments == null) {
			return true;
		} else {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column " + (this.getColumn() + 1)
					+ " the size of the call variables and the arguments doesn't match!");
			return false;
		}
	}

	/*
	 * Checks if the function identifier is defined, and also checks if the list of
	 * expressions (arguments) is well identified and fill the types
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = symbolTable.searchFunctionIdentifier(identifier);
		if (variables != null) {
			for (int i = 0; i < variables.size(); i++) {
				wellIdentified = wellIdentified && variables.get(i).checkIdentifiers(symbolTable);
			}
		}
		if (wellIdentified) {
			type = symbolTable.searchCallType(identifier);
			arguments = symbolTable.searchCallArguments(identifier);
			address = symbolTable.searchFunctionTailFromCall(identifier);
			//identifier.setDeltaDepth(symbolTable.getDepth() - 1);
			identifier.setDeltaDepth(1);
		}
		return wellIdentified;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getRow() {
		return this.identifier.getRow();
	}

	@Override
	public int getColumn() {
		return this.identifier.getColumn();
	}

	@Override
	public void generateCode(Instructions instructions) {
		instructions.addComment("{ Function call }\n");
		//instructions.add("mst " + identifier.getDeltaDepth() + ";\n");
		instructions.add("mst " + 0 + ";\n");
		for (int i = 0; i < arguments.size(); i++) {
			/* Non simple types are passed by reference */
			if (arguments.get(i).getType() == null) {
				variables.get(i).generateCode(instructions);
				instructions.remove(instructions.size() - 1);
			} else {
				variables.get(i).generateCode(instructions);
			}
		}
		instructions.add("cup " + arguments.size() + " " + this.address.getAddress() + ";\n");
		/* maybe and ind here?? */
		instructions.addComment("{ End function call }\n");
	}

	public List<Expression> getVariables() {
		return this.variables;
	}

	@Override
	public void setInsideFunction(boolean b) {
		for (int i = 0; i < variables.size(); i++) {
			variables.get(i).setInsideFunction(b);
		}
	}

	@Override
	public void setFunctionInside(FunctionTail f) {
		for (int i = 0; i < variables.size(); i++) {
			variables.get(i).setFunctionInside(f);
		}
	}
}
