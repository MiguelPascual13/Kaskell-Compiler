package kaskell;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import functions.Function;
import types.StructType;

/*Implements the dummy interface definition so we can insert
 * instances of program in the symbol table (not really necessary)*/
public class Program {
	private List<StructType> structs;
	private List<Block> blocks;
	private List<Function> functions;
	private SymbolTable symbolTable = new SymbolTable();

	public Program(List<Block> blocks, List<Function> functions, List<StructType> structs) {
		this.blocks = blocks;
		this.functions = functions;
		this.structs = structs;
	}

	public boolean checkIdentifiers() {
		/* The first block is for global identifiers */
		symbolTable.startBlock();
		boolean wellIdentified = true;
		/* First consider structs */
		if (structs != null) {
			for (int i = 0; i < structs.size(); i++) {
				/* We don't want recursion (order matters) */
				wellIdentified = wellIdentified && structs.get(i).checkIdentifiers(symbolTable);
				wellIdentified = wellIdentified
						&& symbolTable.insertIdentifier(structs.get(i).getIdentifier(), structs.get(i));
			}
		}
		/* Consider functions, you can call a function from another */
		symbolTable.startBlock();
		if (functions != null) {
			for (int i = 0; i < functions.size(); i++) {
				wellIdentified = wellIdentified
						&& symbolTable.insertIdentifier(functions.get(i).getIdentifier(), functions.get(i).getTail());
				wellIdentified = wellIdentified && functions.get(i).checkIdentifiers(symbolTable);
			}
		}
		/* Checks the blocks (there must be at least one) */
		for (int i = 0; i < blocks.size(); i++) {
			symbolTable.startBlock();
			wellIdentified = wellIdentified && blocks.get(i).checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		symbolTable.closeBlock();
		symbolTable.closeBlock();
		return wellIdentified;
	}

	public boolean checkType() {
		boolean wellTyped = true;
		/* First checks the types of structs */
		if (structs != null) {
			for (int i = 0; i < structs.size(); i++) {
				wellTyped = wellTyped && structs.get(i).checkType();
			}
		}
		/* Checks the types of functions */
		if (functions != null) {
			for (int i = 0; i < functions.size(); i++) {
				wellTyped = wellTyped && functions.get(i).checkType();
			}
		}
		/* Then checks the types of blocks (there must be at least one) */
		for (int i = 0; i < blocks.size(); i++) {
			wellTyped = wellTyped && blocks.get(i).checkType();
		}
		return wellTyped;
	}

	public void generateCode() {
		
		FileOutputStream pMachinInput;
		try {
			pMachinInput = new FileOutputStream("inputMaquinaP");
			
			pMachinInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}