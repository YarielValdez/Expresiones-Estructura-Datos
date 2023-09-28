import java.util.*;

public class Main {
    static String inf, operator = "+-*/^";
    static int last = 0;
    static boolean end = false;
    static HashMap<Character, Integer> Values = new HashMap<>();

    public static boolean validacion() {
        boolean val = true, operand = true;
        int par = 0;
        for (int i = 0; i < inf.length(); i++) {
            if (inf.charAt(i) == 32)
                continue;
            if (operand) {
                if (inf.charAt(i) >= 97 && inf.charAt(i) <= 122) {
                    operand = false;
                    last = inf.charAt(i);
                } else if (inf.charAt(i) == 40) {
                    par++;
                    last = inf.charAt(i);
                } else
                    val = false;
            } else {
                if (operator.contains(Character.toString(inf.charAt(i)))) {
                    operand = true;
                    last = inf.charAt(i);
                } else if ((inf.charAt(i) == 41)) {
                    if (last == 40)
                        val = false;
                    else
                        par--;
                } else
                    val = false;
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

            if (caracter == 32)
                continue;

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

            if (caracter == 32)
                continue;

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
        if (!end) {
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

        } else {
            System.out.println("La expresión está bien escrita");
        }
    }

    public static String replaceVariablesPos() {
        StringBuilder replaced = new StringBuilder();
        for (int i = 0; i < pos().length(); i++) {
            char c = pos().charAt(i);
            if (Character.isLetter(c) && Values.containsKey(c)) {
                replaced.append(Values.get(c));
            } else {
                replaced.append(c);
            }
        }
        return replaced.toString();
    }

    public static String replaceVariablesPre() {
        StringBuilder replaced = new StringBuilder();
        for (int i = 0; i < prefija().length(); i++) {
            char c = prefija().charAt(i);
            if (Character.isLetter(c) && Values.containsKey(c)) {
                replaced.append(Values.get(c));
            } else {
                replaced.append(c);
            }
        }
        return replaced.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (!end) {
            System.out.println("Escriba la expresión infija: ");
            inf = sc.nextLine();
            inf = inf.toLowerCase();
            end = validacion();

            error();
            System.out.println("Expresión postfija: " + pos());
            System.out.println("Expresión prefija: " + prefija());
            valueOf();
            String var = pos();
            replaceVariables();

            System.out.println("Expresión infija con valores: " + replaceVariables());
            System.out.println("Expresión postfija con valores: " + replaceVariablesPos());
            System.out.println("Expresión prefija con valores: " + replaceVariablesPre());
        }
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
                Values.put(caracter, sc.nextInt());
            }
        }
    }

    public static String replaceVariables() {
        StringBuilder replaced = new StringBuilder();
        for (int i = 0; i < inf.length(); i++) {
            char c = inf.charAt(i);
            if (Character.isLetter(c) && Values.containsKey(c)) {
                replaced.append(Values.get(c));
            } else {
                replaced.append(c);
            }
        }
        return replaced.toString();
    }
}