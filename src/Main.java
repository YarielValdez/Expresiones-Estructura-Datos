import java.util.Scanner;
import java.util.Stack;

public class Main {
    static String inf,operator = "+-*/^";
    static int error = 0;

    public static boolean validacion() {
        boolean val = true, operand = true;
        int par = 0;
        char last = 'o';
        inf = inf.toLowerCase();
        for (int i = 0; i < inf.length(); i++) {
            char caracter = inf.charAt(i);
            if (caracter == 32)
                continue;
            if (operand) {
                if (caracter >= 'a' & caracter <= 'z') {
                    operand = false;
                    last = caracter;
                } else if (caracter == '(') {
                    par++;
                    last = caracter;
                } else
                    val = false;
            } else {
                if (operator.contains(Character.toString(caracter))) {
                    operand = true;
                    last = caracter;
                } else if ((caracter == ')')) {
                    if (last == '(')
                        val = false;
                    else {
                        par--;
                        last = caracter;
                    }
                } else
                    val = false;
            }
            if (!val) {
                error = i + 1;
                break;
            }
        }
        if (par != 0 || operator.contains(Character.toString(last)) || inf.isEmpty()) {
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
                pila.pop();
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

    public static String pre() {
        Stack<Character> pila = new Stack<>();
        StringBuilder resultado = new StringBuilder();

        for (int i = inf.length() - 1; i >= 0; i--) {
            char caracter = inf.charAt(i);

            if (caracter == 32)
                continue;

            if (Character.isLetterOrDigit(caracter)) {
                resultado.append(caracter);
            } else if (caracter == ')') {
                pila.push(caracter);
            } else if (caracter == '(') {
                while (!pila.isEmpty() && pila.peek() != ')') {
                    resultado.append(pila.pop());
                }
                pila.pop();
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
        return resultado.reverse().toString();
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            System.out.println("Escriba la expresión infija: ");
            inf = sc.nextLine();
            end = validacion();

            if (!end) {
                System.out.println("Error en el caracter: " + error);
                System.out.println("""
                        Errores comunes:
                        1.- Paréntesis vacío
                        2.- Paréntesis mal colocados
                        3.- Dos operadores juntos
                        4.- Dos operandos juntos
                        6.- La expresión inicia con un operando diferente de parentesis de apertura
                        7.- Se escribieron números en lugar de letras
                        8.- Se utilizaron espacios en la expresión
                        """);
            } else
                System.out.println("La expresión está bien escrita");
        }
        System.out.println("Postfija: " + pos());
        System.out.println("Prefija: " + pre());
    }
}