// Contém funções para converter de infixo para pós-fixo, bem como analisar a equação pós-fixa.

package backend;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JTextField;

public class Parser {
    public static JTextField textField = new JTextField();
    public static boolean clear_output = false;
    public static BigDecimal ans = new BigDecimal(0);
    public static boolean radians = true;

    /**
     * Verifica a precedência do operador
     *
     * @param operator
     * @return
     */

    private static int prec(String operator) {
        String operators[] = { "-", "+", "x", "%", "/", "√", "^", "!" };
        return Arrays.asList(operators).indexOf(operator);
    }

    /**
     * Determina se a string é um número ou não.
     *
     * @param str
     * @return
     */

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void clearOutput() {
        if (clear_output) {
            textField.setText("");
            clear_output = false;
        }
    }

    /**
     * Calcule a expressão no campo de texto.
     *
     * @apiNote Também verifica erros de sintaxe e matemáticos. Salva o resultado na memória.
     *
     * @return
     */

    public static BigDecimal calculate() {
        clear_output = true;
        if (textField.getText().isEmpty()) {
            ans.subtract(ans);
            textField.setText("");
            return new BigDecimal(0);
        }

        String infix[];
        try {
            infix = checkSyntaxErrors();
        } catch (NumberFormatException e) {
            textField.setText("Syntax Error!");
            return BigDecimal.valueOf(0);
        }

        try {
            ans = postfixParse(infixToPostfix(infix));
        } catch (Exception e) {
            textField.setText("Math Error!");
            return BigDecimal.valueOf(0);
        }

        History.storeCalcs.add(textField.getText());
        if (History.storeCalcs.size() > 15) {
            History.storeCalcs.removeFirst();
        }
        History.length = History.storeCalcs.size();
        textField.setText(Double.toString(ans.doubleValue()));
        return ans;
    }

    /**
     * Verifica se há erros de sintaxe em uma lista de expressões.
     *
     * @param expression
     * @return O formato infixo da expressão.
     */

    public static String[] checkSyntaxErrors() throws NumberFormatException {
        ArrayList<String> infix;
        try {
            infix = createExpression();
        } catch (Exception e) {
            textField.setText("Syntax Error!");
            throw new NumberFormatException("Syntax error");
        }

        if (!(isNumber(infix.get(0)) || Utils.valInArray(infix.get(0), Utils.FUNCTIONS)
                || (Utils.valInArray(infix.get(0), Utils.NON_OPERATORS) && !infix.get(0).equals(")")))) {
            textField.setText("Syntax Error!");
            throw new NumberFormatException("Syntax error");
        }

        for (int i = 0; i < infix.size(); i++) {
            if (isNumber(infix.get(i))
                    || (Utils.valInArray(infix.get(i), Utils.NON_OPERATORS) && !infix.get(i).equals(")"))) {
                if (i > 0 && Utils.valInArray(infix.get(i - 1), Utils.NON_OPERATORS)) {
                    textField.setText("Syntax Error!");
                    throw new NumberFormatException("Syntax error");
                }
            } else if (i > 0 && !isNumber(infix.get(i - 1))
                    && !Utils.valInArray(infix.get(i - 1), Utils.NON_OPERATORS)
                    && !Utils.valInArray(infix.get(i), Utils.FUNCTIONS)
                    && !(infix.get(i).equals("!") && infix.get(i - 1).equals("!"))) {
                textField.setText("Syntax Error!");
                throw new NumberFormatException("Syntax error");
            } else if (infix.get(i).equals(".")) {
                textField.setText("Syntax Error!");
                throw new NumberFormatException("Syntax error");
            }
        }
        return infix.toArray(new String[infix.size()]);
    }

    /**
     * Converte a entrada da calculadora em uma lista de expressões. Garante que
     * quaisquer funções como
     * tan, sin, cos, ln, etc... estejam corretas. Caso contrário, gera uma
     * NumberFormatException.
     *
     * @return
     * @throws NumberFormatException
     */

    public static ArrayList<String> createExpression() throws NumberFormatException {
        ArrayList<String> expression = new ArrayList<String>();
        char input[] = textField.getText().toCharArray();

        int i = 0;
        while (i < input.length) {
            char c = input[i];
            // Detected a number character.
            if (isNumber(Character.toString(c))) {
                // Check if the last element before was a number or not.
                if (!expression.isEmpty()) {
                    if (isNumber(expression.getLast())) {
                        expression.add(expression.removeLast() + c);
                    } else if (expression.getLast().equals("-")) {
                        // Check if the last character was - and make the number negative.
                        if (expression.size() > 1 && (isNumber(expression.get
                                (expression.size() - 2))
                                || Utils.valInArray(expression.get(expression.size() - 2),
                                Utils.NON_OPERATORS))) {
                            expression.removeLast();
                            expression.add("+");
                            expression.add("-" + c);
                        } else {
                            expression.add(expression.removeLast() + c);
                        }
                    } else if (expression.getLast().equals(".") ||
                            expression.getLast().equals("-.")) {
                        expression.add(expression.removeLast() + c);
                    } else {
                        expression.add(Character.toString(c));
                    }
                } else {
                    expression.add(Character.toString(c));
                }
            } else {
                String func = "";
                for (String function : Utils.FUNCTIONS) {
                    try {
                        if (textField.getText().subSequence
                                (i, i + function.length()).equals(function)) {
                            i += function.length();
                            func = function;
                        }
                    } catch (Exception e) {
                    }
                    if (!func.isEmpty()) {
                        break;
                    }
                }

                if (!func.isEmpty()) {
                    if (!expression.isEmpty() && (isNumber(expression.getLast())
                            || Utils.valInArray(expression.getLast(),
                            Utils.NON_OPERATORS))) {
                        expression.add("x");
                    } else if (!expression.isEmpty() &&
                            expression.getLast().equals("-")) {
                        expression.removeLast();
                        if (!expression.isEmpty() &&
                                (isNumber(expression.getLast())
                                || Utils.valInArray(expression.getLast(),
                                        Utils.NON_OPERATORS))) {
                            expression.add("+");
                        }
                        expression.add("-1");
                        expression.add("x");
                    }
                    expression.add(func);
                    continue;
                }

                String constants[] = { "Ans", "e", "π" };
                for (String constant : constants) {
                    try {
                        if (textField.getText().subSequence(i, i + constant.length()).
                                equals(constant)) {
                            if (!expression.isEmpty() && expression.
                                    getLast().equals("-") &&
                                    (expression.size() > 1 &&
                                            !isNumber(expression.get(expression.size() - 2))
                                            && !Utils.valInArray(expression.get
                                                    (expression.size() - 2),
                                            Utils.NON_OPERATORS)
                                            || expression.size() == 1)) {
                                func = expression.removeLast() + constant;
                            } else {
                                func = constant;
                            }
                            i += constant.length();
                        }
                    } catch (Exception e) {
                    }
                    if (!func.isEmpty()) {
                        break;
                    }
                }

                if (!func.isEmpty()) {
                    if (!expression.isEmpty() && (isNumber(expression.getLast())
                            || Utils.valInArray(expression.getLast(),
                            Utils.NON_OPERATORS))) {
                        expression.add("x");
                    } else if (!expression.isEmpty() &&
                            expression.getLast().equals("-")) {
                        expression.removeLast();
                        if (!expression.isEmpty() &&
                                isNumber(expression.getLast())) {
                            expression.add("+");
                        }
                        expression.add("-1");
                        expression.add("x");
                    }
                    expression.add(func);
                    continue;
                }

                switch (c) {
                    case '.':
                        if (!expression.isEmpty() &&
                                isNumber(expression.getLast())) {
                            expression.add(expression.removeLast() + ".");
                        } else if (!expression.isEmpty() &&
                                expression.getLast().equals("-") &&
                                (expression.size() > 1 && !isNumber
                                        (expression.get(expression.size() - 2))
                                        && !Utils.valInArray(expression.get
                                                (expression.size() - 2),
                                        Utils.NON_OPERATORS)
                                        || expression.size() == 1)) {
                            expression.add(expression.removeLast() + ".");
                        } else {
                            expression.add(".");
                        }
                        break;

                    default:
                        expression.add(Character.toString(c));
                        break;
                }
            }

            i++;
        }

        return expression;
    }

    /**
     * Converter expressão infixa em posfixa.
     *
     * @param infix
     * @return Expressão pós-fixada.
     */
    public static String[] infixToPostfix(String infix[]) {
        Stack<String> stack = new Stack<String>();
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < infix.length; i++) {
            if (isNumber(infix[i])) {
                result.add(infix[i]);
            } else if (Utils.valInArray(infix[i], Utils.FUNCTIONS)) {
                stack.push(infix[i]);
            } else if (infix[i].equals(")")) {
                // is found
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                stack.pop();
                // Check if the brackets are part of a function
                if (!stack.isEmpty() && Utils.valInArray(stack.peek(),
                        Utils.FUNCTIONS) && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
            } else if (Utils.valInArray(infix[i], Utils.NON_OPERATORS)) {
                if (infix[i].equals("π")) {
                    result.add(Double.toString(Math.PI));
                } else if (infix[i].equals("-π")) {
                    result.add(Double.toString(-Math.PI));
                } else if (infix[i].equals("e")) {
                    result.add(Double.toString(Math.E));
                } else if (infix[i].equals("-e")) {
                    result.add(Double.toString(-Math.E));
                } else if (infix[i].equals("Ans")) {
                    result.add(Double.toString(ans.doubleValue()));
                } else if (infix[i].equals("-Ans")) {
                    result.add(Double.toString(-ans.doubleValue()));
                }
            } else {
                while (!stack.isEmpty() && prec(infix[i]) < prec(stack.peek())) {
                    result.add(stack.pop());
                }
                stack.push(infix[i]);
            }
        }

        while (!stack.isEmpty()) {
            if (!stack.peek().equals("(")) {
                result.add(stack.pop());
            } else {
                stack.pop();
            }
        }

        String result_2[] = new String[result.size()];
        return result.toArray(result_2);
    }

    /**
     * Avalia o valor da expressão pós-fixada.
     *
     * @param postfix
     * @return
     */

    public static BigDecimal postfixParse(String postfix[]) throws Exception {
        Stack<BigDecimal> stack = new Stack<BigDecimal>();

        for (int i = 0; i < postfix.length; i++) {
            if (isNumber(postfix[i])) {
                stack.push(new BigDecimal(postfix[i]));
            } else if (Utils.valInArray(postfix[i], Utils.FUNCTIONS) || postfix[i].equals("!")) {
                BigDecimal num = stack.pop();
                double value;

                switch (postfix[i]) {
                    case "sin":
                        if (!radians) {
                            num = new BigDecimal(Math.toRadians(num.doubleValue()));
                        }
                        value = Math.sin(num.doubleValue());
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "cos":
                        if (!radians) {
                            num = new BigDecimal(Math.toRadians(num.doubleValue()));
                        }
                        value = Math.cos(num.doubleValue());
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "tan":
                        if (!radians) {
                            num = new BigDecimal(Math.toRadians(num.doubleValue()));
                        }
                        value = Math.tan(num.doubleValue());
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "sin⁻¹":
                        value = Math.asin(num.doubleValue());
                        if (!radians) {
                            value = Math.toDegrees(value);
                        }
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "cos⁻¹":
                        value = Math.acos(num.doubleValue());
                        if (!radians) {
                            value = Math.toDegrees(value);
                        }
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "tan⁻¹":
                        value = Math.atan(num.doubleValue());
                        if (!radians) {
                            value = Math.toDegrees(value);
                        }
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "sinh":
                        value = Math.sinh(num.doubleValue());
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "cosh":
                        value = Math.cosh(num.doubleValue());
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "tanh":
                        value = Math.tanh(num.doubleValue());
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "sinh⁻¹":
                        value = Math.log(num.doubleValue() +
                                Math.sqrt(num.pow(2).doubleValue() + 1));
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "cosh⁻¹":
                        value = Math.log(num.doubleValue() +
                                Math.sqrt(num.pow(2).doubleValue() - 1));
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "tanh⁻¹":
                        value = 0.5 * Math.log((1 + num.doubleValue()) /
                                (1 - num.doubleValue()));
                        if (Double.isNaN(value)) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(value));
                        break;

                    case "ln":
                        if (num.doubleValue() <= 0) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(Math.log(num.doubleValue())));
                        break;

                    case "log":
                        if (num.doubleValue() <= 0) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(Math.log10(num.doubleValue())));
                        break;

                    case "!":
                        if (!isInt(num.doubleValue()) || num.doubleValue() < 0) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(factorial(BigInteger.valueOf
                                (Math.round(num.doubleValue())))));
                        break;

                    default:
                        throw new Exception("Syntax error");
                }
            } else {
                BigDecimal num2 = stack.pop();
                BigDecimal num1 = stack.pop();

                switch (postfix[i]) {
                    case "^":
                        if (num2.equals(BigDecimal.valueOf(0)) && num1.
                                equals(BigDecimal.valueOf(0))) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(Math.pow(num1.doubleValue(),
                                num2.doubleValue())));
                        break;
                    case "/":
                        if (num2.equals(BigDecimal.valueOf(0))) {
                            throw new Exception("Math error");
                        }
                        stack.push(num1.divide(num2, 11,
                                RoundingMode.HALF_EVEN));
                        break;

                    case "÷":
                        if (num2.equals(BigDecimal.valueOf(0))) {
                            throw new Exception("Math error");
                        }
                        stack.push(num1.divide(num2, 11,
                                RoundingMode.HALF_EVEN));
                        break;

                    case "%":
                        if (num2.equals(BigDecimal.valueOf(0))) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(num1.doubleValue() % num2.doubleValue()));
                        break;
                    case "x":
                        stack.push(num1.multiply(num2));
                        break;
                    case "+":
                        stack.push(num1.add(num2));
                        break;
                    case "-":
                        stack.push(num1.subtract(num2));
                        break;
                    case "√":
                        if (num1.equals(BigDecimal.valueOf(0))) {
                            throw new Exception("Math error");
                        }
                        stack.push(new BigDecimal(Math.pow(num2.doubleValue(),
                                1 / num1.doubleValue())));
                        break;
                    default:
                        break;
                }
            }
        }
        return stack.pop();
    }

    /**
     * Determinar se um número é um inteiro ou não
     * @param num
     * @return
     */

    private static boolean isInt(double num) {
        return num == Math.round(num);
    }

    private static BigInteger factorial(BigInteger num) {
        if (num.equals(BigInteger.valueOf(0))) {
            return BigInteger.valueOf(1);
        }
        return num.multiply(factorial(num.subtract(BigInteger.valueOf(1))));
    }
}
