import java.util.Scanner;
import java.util.Stack;

public class Main {
    static String inf;
    static int error = 0;
    static String operator = "+-*/^";
    public static boolean validacion() {
        boolean val = true, operand = true;
        int par = 0, last = 0;
        System.out.println("Among us");
        inf = inf.toLowerCase();
        for (int i = 0; i < inf.length(); i++) {
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
                } else if ((inf.charAt(i) == 41)){
                    if (last == 40)
                        val = false;
                    else
                        par--;}
                else
                    val = false;
            }
            if (!val) {
                error = i + 1;
                break;
            }
        }
        if(par != 0 || operator.contains(Character.toString((char)last)) || inf.equals("")){
            val = false;
        }
        return val;
    }

    public static String pos() {
        for (int i = 0; i < inf.length(); i++) {
            int jerry = jerarquia(inf.charAt(i));
            int select = inf.charAt(i);

        }
        return "amoung us";
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
    }
}