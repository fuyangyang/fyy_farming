package el;


import javax.script.ScriptException;

public class ElExpressionTest {
    public static void main(String[] args) throws ScriptException {
        String expression = "(0*1-3)-5/-4-(3*(-2.13))";
        double result = Calculator.calculate(expression);
        System.out.println(expression + " = " + result);
    }
}
