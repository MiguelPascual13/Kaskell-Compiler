package expressions;

import kaskell.SymbolTable;
import types.Type;

public class UnaryExpression implements Expression {
	private UnaryOperators operator;
	private Expression expression;

	public UnaryExpression(UnaryOperators operator, Expression expression) {
		this.operator = operator;
		this.expression = expression;
	}

	/*
	 * Checks the expression and if the type of the expression equals the type of
	 * the operator
	 */
	@Override
	public boolean checkType() {
		return expression.checkType() && expression.getType().equals(operator.getType());
	}

	/* Just checks the expression */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return expression.checkIdentifiers(symbolTable);
	}

	@Override
	public Type getType() {
		return operator.getType();
	}
}
