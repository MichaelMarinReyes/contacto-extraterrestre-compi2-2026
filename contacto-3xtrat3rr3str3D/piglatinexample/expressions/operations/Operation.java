/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions.operations;

import igriega.piglatin.expressions.Expression;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
public abstract class Operation extends Expression{
    protected Expression left;
    protected Expression right;
    protected String operator;

    public Operation(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public void toPigLatin(StringBuffer sb) {
        left.toPigLatin(sb);
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        right.toPigLatin(sb);
    }
    
    public static Operation create(Expression left, Expression right, String operator) {
        switch (operator) {
            case "+", "-", "/", "*" -> {
                return new AritmeticOperation(left, right, operator);
            }
            case "<", ">", ">=", "<=", "==", "!=" -> {
                return new BooleanOperation(left, right, operator);
            }
            case "||" -> {
                return new OrOperation(left, right);
            }
            case "&&" -> {
                return new AndOperation(left, right);
            }
            default -> throw new IllegalArgumentException("Operador no reconocido de momento: " + operator);
        }
    }
    
}
