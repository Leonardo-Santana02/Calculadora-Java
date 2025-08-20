package view;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 28));
        setForeground(Theme.FG_COLOR);
        setBackground(Theme.TERTAIRY_COLOR);
        setBorder(BorderFactory.createLineBorder(Theme.BG_COLOR));
        setFocusPainted(false);
    }
}