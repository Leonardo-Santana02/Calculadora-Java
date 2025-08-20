import javax.swing.*;

import frontend.Home;

import java.awt.*;

// O principal ponto de entrada para o aplicativo
public class Calculator extends JFrame {
    public Calculator() {
        add(new Home());

        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 500));
        setVisible(true);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}