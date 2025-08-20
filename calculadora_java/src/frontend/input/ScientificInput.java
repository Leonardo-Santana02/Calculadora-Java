package frontend.input;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import backend.Parser;
import backend.Utils;
import view.ButtonTexts;
import view.CustomButton;
import view.Theme;

public class ScientificInput {
    static final private String buttons[] = {
            "(", ")", ButtonTexts.x_sqr, ButtonTexts.x_cube, ButtonTexts.pow,
            ButtonTexts.inverse, ButtonTexts.squareroot, ButtonTexts.cuberoot, ButtonTexts.yroot, ButtonTexts.factorial,
            ButtonTexts.ln, ButtonTexts.log, ButtonTexts.e_pow, ButtonTexts.ten_pow, ButtonTexts.e,
            ButtonTexts.shift, ButtonTexts.pi, ButtonTexts.sin, ButtonTexts.cos, ButtonTexts.tan,
            ButtonTexts.rad, ButtonTexts.sinh, ButtonTexts.cosh, ButtonTexts.tanh
    };

    private static boolean shiftDown = false;

    private static final String substituteText[] = {
            "(", ")", "^2", "^3", "^",
            "^-1", "2√", "3√", "√", "!",
            "ln(", "log(", "e^", "10^", "e",
            "", "π", "sin(", "cos(", "tan(",
            "rad", "sinh(", "cosh(", "tanh(",
    };

    private static final String substituteTextShifted[] = {
            "(", ")", "^2", "^3", "^",
            "^-1", "2√", "3√", "√", "!",
            "ln(", "log(", "e^", "10^", "e",
            "", "π", "sin⁻¹(", "cos⁻¹(", "tan⁻¹(",
            "rad", "sinh⁻¹(", "cosh⁻¹(", "tanh⁻¹(",
    };

    private JTextField textField = Parser.textField;

    private CustomButton button_arr[] = new CustomButton[buttons.length];

    /**
     * Define o painel de entrada científica
     *
     * @param parent Painel pai para adicionar botões
     */

    public ScientificInput(JPanel parent) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        createButtons();

        if (shiftDown) {
            button_arr[17].setText(ButtonTexts.arcsin);
            button_arr[18].setText(ButtonTexts.arccos);
            button_arr[19].setText(ButtonTexts.arctan);
            button_arr[21].setText(ButtonTexts.arcsinh);
            button_arr[22].setText(ButtonTexts.arccosh);
            button_arr[23].setText(ButtonTexts.arctanh);
        }

        button_arr[20].setText(Parser.radians ? ButtonTexts.rad : ButtonTexts.deg);

        button_arr[20].removeActionListener(button_arr[20].getActionListeners()[0]);
        button_arr[20].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parser.radians = !Parser.radians;
                if (Parser.radians) {
                    button_arr[20].setText(ButtonTexts.rad);
                } else {
                    button_arr[20].setText(ButtonTexts.deg);
                }
            }
        });

        button_arr[15].removeActionListener(button_arr[15].getActionListeners()[0]);
        button_arr[15].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shiftDown = !shiftDown;
                if (shiftDown) {
                    button_arr[17].setText(ButtonTexts.arcsin);
                    button_arr[18].setText(ButtonTexts.arccos);
                    button_arr[19].setText(ButtonTexts.arctan);
                    button_arr[21].setText(ButtonTexts.arcsinh);
                    button_arr[22].setText(ButtonTexts.arccosh);
                    button_arr[23].setText(ButtonTexts.arctanh);
                } else {
                    button_arr[17].setText(ButtonTexts.sin);
                    button_arr[18].setText(ButtonTexts.cos);
                    button_arr[19].setText(ButtonTexts.tan);
                    button_arr[21].setText(ButtonTexts.sinh);
                    button_arr[22].setText(ButtonTexts.cosh);
                    button_arr[23].setText(ButtonTexts.tanh);
                }
            }
        });

        for (int i = 0; i < button_arr.length - 4; i++) {
            gbc.gridx = i % 5;
            gbc.gridy = i / 5;
            parent.add(button_arr[i], gbc);
        }

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        parent.add(button_arr[20], gbc);

        gbc.gridx += 2;
        gbc.gridwidth = 1;
        parent.add(button_arr[21], gbc);
        gbc.gridx++;
        parent.add(button_arr[22], gbc);
        gbc.gridx++;
        parent.add(button_arr[23], gbc);

        parent.setBackground(Theme.BG_COLOR);
    }

    private void createButtons() {
        for (int i = 0; i < buttons.length; i++) {
            button_arr[i] = new CustomButton(buttons[i]);
            final int index = i;
            button_arr[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Parser.clearOutput();
                    textField.setText(
                            textField.getText() + (shiftDown ? substituteTextShifted[index]
                                    : substituteText[index]));
                }
            });

            button_arr[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (!Utils.valInArray((Character) e.getKeyChar(), Utils.ALLOWED_KEYS)) {
                        e.consume();
                        return;
                    }

                    Parser.clearOutput();

                    if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE &&
                            textField.getText().length() > 0) {
                        String text = textField.getText();
                        textField.setText(text.substring(0, text.length() - 1));
                    } else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        Parser.calculate();
                    } else {
                        switch (e.getKeyChar()) {
                            case '*':
                                e.setKeyChar('x');
                                break;
                            case '/':
                                e.setKeyChar('÷');
                                break;
                            default:
                                break;
                        }
                        textField.setText(textField.getText() + e.getKeyChar());
                    }
                }
            });
        }
    }
}
