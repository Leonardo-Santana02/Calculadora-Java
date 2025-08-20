// Ponto de entrada para a interface do usuário do aplicativo

package frontend;

import javax.swing.*;

import frontend.input.*;
import frontend.output.*;
import view.Theme;

import java.awt.*;
import java.awt.event.*;


public class Home extends JPanel {
    private GridBagConstraints gbc = new GridBagConstraints();

    private void build() {
        removeAll();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.25;
        Output output = new Output();
        add(output, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        add(new BasicInput(), gbc);

        revalidate();
    }

    public Home() {
        setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.BOTH;

        build();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getComponent().getWidth() >= 800) {
                    BasicInput.showScientific = true;
                } else {
                    BasicInput.showScientific = false;
                }
                build();
            }
        });

        setBackground(Theme.BG_COLOR);
    }
}
