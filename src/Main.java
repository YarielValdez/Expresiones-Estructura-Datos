import java.util.Scanner;
import java.util.Stack;

public class Main {
    static String inf;
    static int error = 0;
    static String operator = "+-*/^";

    public static boolean validacion() {
        boolean val = true, operand = true;
        int par = 0, last = 0;
        inf = inf.toLowerCase();
        for (int i = 0; i < inf.length(); i++) {
            if (inf.charAt(i) == 32)
                continue;
            if (operand) {
                if (inf.charAt(i) >= 97 & inf.charAt(i) <= 122) {
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
                error = i + 1;
                break;
            }
        }
        if (par != 0 || operator.contains(Character.toString((char) last)) || inf.equals("")) {
            val = false;
        }
        return val;
    }

    public static String pos(String inf) {
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
        System.out.println("Expresión postfija: " + resultado.toString());
        return resultado.toString();
    }

    public static String prefija(String inf) {
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
                while (!pila.isEmpty() && jerarquia(caracter) < jerarquia(pila.peek())) {
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
            case '(' -> 0;
            default -> 25;
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            System.out.println("Escriba la expresión infija: ");
            inf = sc.nextLine();
            end = validacion();

            if (!end) {
                System.out.println("Errores encontrados:");

                if (inf.contains("()")) {
                    System.out.println("Error: Paréntesis vacío");
                }

                if (!validacionParentesis()) {
                    System.out.println("Error: Paréntesis mal colocados");
                }

                if (inf.matches(".*[\\+\\-\\*/\\^]{2,}.*")) {
                    System.out.println("Error: Dos operadores juntos");
                }

                if (inf.matches(".*[a-z0-9][a-z0-9].*")) {
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

                if (inf.matches(".*\\d[a-z].*")) {
                    System.out.println("Error: Se escribieron números en lugar de letras");
                }

            } else {
                System.out.println("La expresión está bien escrita");
                String expresionPrefija = prefija(inf);
                System.out.println("Expresión prefija: " + expresionPrefija);
                pos(inf);
            }
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
}