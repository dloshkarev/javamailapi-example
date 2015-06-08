package ru;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    public JTextArea text = new JTextArea(10, 20);
    private JScrollPane textPane = new JScrollPane(text);

    ContentPanel() {
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        layout.setConstraints(textPane, constraints);
        this.add(textPane);
    }

}
