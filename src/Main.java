import java.util.*;

public class Main {
    static String inf, prefix, post, operator = "+-*/^";
    static int code=0;
    static HashMap<Character, String> Values = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean end = true, val = false;
        while (end) {
            while (!val) {
                System.out.println("Escriba la expresión infija: ");
                inf = sc.nextLine();
                inf = inf.replace(" ", "");
                inf = inf.toLowerCase();
                val = validacion();
                if (!val)
                    error();
            }
            System.out.println("La expresión está bien escrita");
            prefix = prefija();
            post = pos();
            System.out.println("Expresión postfija: " + post);
            System.out.println("Expresión prefija: " + prefix);
            valueOf();
            System.out.println("Expresión infija con valores: " + replaceVariables(inf));
            System.out.println("Expresión postfija con valores: " + replaceVariables(post));
            System.out.println("Expresión prefija con valores: " + replaceVariables(prefix));
            System.out.println("Resultado Postfija: " + EvalPost());
            System.out.println("Resultado Prefija: " + EvalPre());
            System.out.println("Resultado Infija" + EvalInf());
            System.out.println("¿Desea evaluar otra expresión?  Y/N");
            String answer = sc.nextLine();
            end = answer.equalsIgnoreCase("y");
            if(end){
                val = false;
                Values.clear();
            }
        }
        System.out.println("Gracias por usar nuestro programa ;)");
    }

    public static boolean validacion() {
        boolean operand = true;
        for (int i = 0; i < inf.length(); i++) {
            char c = inf.charAt(i);
            if (operand) {
                if (c >= 'a' && c <= 'z')
                    operand = false;
                else if(c=='(')
                    continue;
                else {
                    code = 1;
                    return false;
                }
            } else
            if (operator.contains(Character.toString(c)))
                operand = true;
            else if(c==')')
                continue;
            else {
                code = 2;
                return false;
            }
        }
        return !inf.contains("()") && !inf.isEmpty() && validacionParentesis();
    }

    public static boolean validacionParentesis() {
        Stack<Character> pila = new Stack<>();
        for (int i = 0; i < inf.length(); i++) {
            char c = inf.charAt(i);
            if (c == '(') {
                pila.push(c);
            } else if (c == ')') {
                if (pila.isEmpty() || pila.pop() != '(') {
                    return false;
                }
            }
        }
        return pila.isEmpty();
    }

    public static void error() {
        System.out.println("Errores encontrados:");
        if (inf.contains("()"))
            System.out.println("Error: Paréntesis vacío");
        if (!validacionParentesis())
            System.out.println("Error: Paréntesis mal colocados");
        if (code == 1)
            System.out.println("Error: Dos operadores juntos");
        if (code == 2)
            System.out.println("Error: Dos operandos juntos");
        if (inf.length() > 0 && !Character.isLetter(inf.charAt(0)) && inf.charAt(0) != '(')
            System.out.println("Error: La expresión inicia con un operando diferente de paréntesis de apertura");
        for (int i = 0; i < inf.length(); i++) {
            char c = inf.charAt(i);
            if (!Character.isLetter(c) && c != '(' && c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '^')
                System.out.println("Error: Caracter no válido en la posición " + (i + 1));
        }
        if (inf.matches("(.*)\\d(.*)"))
            System.out.println("Error: Se escribieron números en lugar de letras");
        if (!inf.isEmpty() && operator.contains(Character.toString(inf.charAt(inf.length() - 1))))
            System.out.println("Error, la expresión termina en un operador");
        if (inf.isEmpty())
            System.out.println("Error, la expresión está vacía");
    }


    public static String pos() {
        Stack<Character> pila = new Stack<>();
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < inf.length(); i++) {
            char caracter = inf.charAt(i);
            if (Character.isLetterOrDigit(caracter)) {
                resultado.append(caracter);
            } else if (caracter == '(') {
                pila.push(caracter);
            } else if (caracter == ')') {
                while (!pila.isEmpty() && pila.peek() != '(') {
                    resultado.append(pila.pop());
                }
                if (!pila.isEmpty()) {
                    pila.pop();
                }
            } else {
                while (!pila.isEmpty() && jerarquia(caracter) <= jerarquia(pila.peek())) {
                    resultado.append(pila.pop());
                }
                pila.push(caracter);
            }
        }
        while (!pila.isEmpty()) {
            resultado.append(pila.pop());
        }
        return resultado.toString();
    }

    public static String prefija() {
        StringBuilder expresionInvertida = new StringBuilder();
        Stack<Character> pila = new Stack<>();
        for (int i = inf.length() - 1; i >= 0; i--) {
            char caracter = inf.charAt(i);
            if (Character.isLetterOrDigit(caracter)) {
                expresionInvertida.append(caracter);
            } else if (caracter == ')') {
                pila.push(caracter);
            } else if (caracter == '(') {
                while (!pila.isEmpty() && pila.peek() != ')') {
                    expresionInvertida.append(pila.pop());
                }
                if (!pila.isEmpty()) {
                    pila.pop();
                }
            } else {
                while (!pila.isEmpty() && jerarquia(caracter) <= jerarquia(pila.peek())) {
                    expresionInvertida.append(pila.pop());
                }
                pila.push(caracter);
            }
        }
        while (!pila.isEmpty()) {
            expresionInvertida.append(pila.pop());
        }
        return expresionInvertida.reverse().toString();
    }

    public static int jerarquia(char operador) {
        return switch (operador) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            case '(', ')' -> 0;
            default -> 25;
        };
    }

    public static void valueOf() {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < inf.length(); i++) {
            char caracter = inf.charAt(i);
            if (caracter >= 'a' && caracter <= 'z' && !Values.containsKey(caracter)) {
                System.out.println("Ingrese el valor de " + caracter);
                Values.put(caracter, sc.nextLine());
            }
        }
    }

    public static String replaceVariables(String formula) {
        StringBuilder reemplazo = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c >= 'a' && c <= 'z')
                reemplazo.append(Values.get(c));
            else
                reemplazo.append(c);
        }
        return reemplazo.toString();
    }

    public static double eval(String operator, String n2, String n1) {
        double num1 = Double.parseDouble(n1);
        double num2 = Double.parseDouble(n2);
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            case "^" -> Math.pow(num1, num2);
            default -> 25;
        };
    }

    public static String EvalPost() {
        Stack<String> funcion = new Stack<>();
        Stack<String> evaluacion = new Stack<>();
        for (int i = post.length() - 1; i >= 0; i--) {
            char elemento = post.charAt(i);
            if (operator.contains(Character.toString(elemento)))
                funcion.push(Character.toString(elemento));
            else {
                funcion.push(Values.get(elemento));
            }
        }
        while (!funcion.empty()) {
            if (operator.contains(funcion.peek()))
                evaluacion.push(eval(funcion.pop(), evaluacion.pop(), evaluacion.pop()) + "");
            else
                evaluacion.push(funcion.pop());
        }
        return evaluacion.peek();
    }

    public static String EvalPre() {
        Stack<String> operando = new Stack<>();
        Stack<String> operador = new Stack<>();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (c >= 'a' && c <= 'z') {
                operando.push(Values.get(c));
                if (operando.size() == 2) {
                    operando.push(eval(operador.pop(), operando.pop(), operando.pop()) + "");
                }
            } else
                operador.push(Character.toString(c));
        }
        return operando.peek();
    }

    public static double eval_i(char operador, double primero, double segundo) {
        switch (operador) {
            case '+':
                return primero + segundo;
            case '-':
                return primero - segundo;
            case '*':
                return primero * segundo;
            case '/':
                return primero / segundo;
            case '^':
                return Math.pow(primero, segundo);
            default:
                return 0.0;
        }
    }

    public static String EvalInf() {
        Stack<Double> operandos = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        for (int i = 0; i < inf.length(); i++) {
            char c = inf.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                operandos.push(Double.parseDouble(Values.get(c)));
            } else if (c == '(') {
                operadores.push(c);
            } else if (c == ')') {
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    char operador = operadores.pop();
                    double segundo = operandos.pop();
                    double primero = operandos.pop();
                    operandos.push(eval_i(operador, primero, segundo));
                }
                operadores.pop(); // Desapilar el '('
            } else if (operator.contains(Character.toString(c))) {
                while (!operadores.isEmpty() && jerarquia(c) <= jerarquia(operadores.peek())) {
                    char operador = operadores.pop();
                    double segundo = operandos.pop();
                    double primero = operandos.pop();
                    operandos.push(eval_i(operador, primero, segundo));
                }
                operadores.push(c);
            }
        }

        while (!operadores.isEmpty()) {
            char operador = operadores.pop();
            double segundo = operandos.pop();
            double primero = operandos.pop();
            operandos.push(eval_i(operador, primero, segundo));
        }

        return operandos.peek().toString();
    }
}