//Classe Utils básica com variáveis globais e funções genéricas.

package backend;

import java.awt.event.KeyEvent;

public class Utils {
    public final static Character ALLOWED_KEYS[] = {
            '%', '+', '-', '/', 'x', '*', '(', ')', '^',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.',
            KeyEvent.VK_BACK_SPACE, KeyEvent.VK_ENTER
    };

    public final static String NON_OPERATORS[] = {
            ")", "π", "e", "-π", "-e", "Ans", "-Ans"
    };

    public final static String FUNCTIONS[] = {
            "tanh⁻¹", "sinh⁻¹", "cosh⁻¹", "sin⁻¹", "cos⁻¹", "tan⁻¹", "tanh", "sinh", "cosh", "tan", "sin", "cos", "log", "ln",
            "("
    };

    public static <T> boolean valInArray(T val, T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (val.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }
}
