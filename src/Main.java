import java.util.*;

public class Main {
    static String inf, prefix, post, operator = "+-*/^";
    static int last = 0;
    static HashMap<Character, String> Values = new HashMap<>();

    public static boolean validacion() {
        boolean val = true, operand = true;
        int par = 0;
        for (int i = 0; i < inf.length(); i++) {
            if (inf.charAt(i) == 32) continue;
            if (operand) {
                if (inf.charAt(i) >= 97 && inf.charAt(i) <= 122) {
                    operand = false;
                    last = inf.charAt(i);
                } else if (inf.charAt(i) == 40) {
                    par++;
                    last = inf.charAt(i);
                } else val = false;
            } else {
                if (operator.contains(Character.toString(inf.charAt(i)))) {
                    operand = true;
                    last = inf.charAt(i);
                } else if ((inf.charAt(i) == 41)) {
                    if (last == 40) val = false;
                    else par--;
                } else val = false;
            }
            if (!val) {
                break;
            }
        }
        if (par != 0 || operator.contains(Character.toString((char) last)) || inf.equals("")) {
            val = false;
        }
        return val;
    }

    public static String pos() {
        Stack<Character> pila = new Stack<>();
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < inf.length(); i++) {
            char caracter = inf.charAt(i);

            if (caracter == 32) continue;

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

            if (caracter == 32) continue;

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

    public static void error() {
            System.out.println("Errores encontrados:");

            if (inf.contains("()")) {
                System.out.println("Error: Paréntesis vacío");
            }

            if (!validacionParentesis()) {
                System.out.println("Error: Paréntesis mal colocados");
            }

            if (inf.matches(".[\\+\\-\\/\\^]{2,}.*")) {
                System.out.println("Error: Dos operadores juntos");
            }

            if (inf.matches(".[a-z0-9][a-z0-9].")) {
                System.out.println("Error: Dos operandos juntos");
            }

            if (inf.length() > 0 && !Character.isLetter(inf.charAt(0)) && inf.charAt(0) != '(') {
                System.out.println("Error: La expresión inicia con un operando diferente de paréntesis de apertura");
            }

            for (int i = 0; i < inf.length(); i++) {
                char c = inf.charAt(i);
                if (!Character.isLetter(c) && c != '(' && c != ')' && c != '+' && c != '-' && c != '*' && c != '/' && c != '^') {
                    System.out.println("Error: Caracter no válido en la posición " + (i + 1));
                }
            }

            if (inf.matches(".\\d[a-z].")) {
                System.out.println("Error: Se escribieron números en lugar de letras");
            }
            if (operator.contains(Character.toString(inf.charAt(inf.length() - 1))))
                System.out.println("Error, la expresión termina en un operador");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            System.out.println("Escriba la expresión infija: ");
            inf = sc.nextLine();
            inf = inf.toLowerCase();
            end = validacion();
            if (!end)
               error();
        }
        System.out.println("La expresión está bien escrita");
        prefix = prefija();
        post = pos();
        System.out.println("Expresión prefija: " + prefix);
        System.out.println("Expresión postfija: " + post);
        valueOf();
        System.out.println("Expresión infija con valores: " + replaceVariables(inf));
        System.out.println("Expresión postfija con valores: " + replaceVariables(post));
        System.out.println("Expresión prefija con valores: " + replaceVariables(prefix));
        System.out.println(EvalPost());
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

    public static String replaceVariables(String formula){
        StringBuilder reemplazo = new StringBuilder();
        for(int i=0;i<formula.length();i++){
            char c = formula.charAt(i);
            if(c>='a' && c<='z')
                reemplazo.append(Values.get(c));
            else
                reemplazo.append(c);
        }
        return reemplazo.toString();
    }
    public static int eval(String operator, String n2, String n1) {
        int num1 = Integer.parseInt(n1);
        int num2 = Integer.parseInt(n2);
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            case "^" -> num1 ^ num2;
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
}