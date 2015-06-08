package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.Properties;

public class CreateBoxFrame extends JFrame {
    private JLabel addressLabel = new JLabel("Адрес: ");
    private JTextField jAddress = new JTextField(30);
    private static JButton createButton = new JButton("Готово");

    CreateBoxFrame(final MainForm mainForm, final TreePanel treePanel) {
        final JFrame me = this;
        mainForm.setProp();
        jAddress.setText(mainForm.getAddress());

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainForm.setProp();
                File settings = new File("settings.properties");
                if (settings.exists()) {
                    try {
                        FileInputStream fi = new FileInputStream(settings);
                        FileOutputStream fo = new FileOutputStream(settings);
                        Properties prop = new Properties();
                        prop.load(fi);
                        prop.setProperty("address", jAddress.getText());
                        prop.setProperty("smtpPort", mainForm.getSmtpPort());
                        prop.setProperty("smtpHost", mainForm.getSmtpHost());
                        prop.setProperty("login", mainForm.getLogin());
                        prop.setProperty("password", mainForm.getPassword());
                        prop.setProperty("pop3Host", mainForm.getPop3Host());
                        prop.store(fo, null);
                        mainForm.setProp();
                        treePanel.create(jAddress.getText(), mainForm.getListPanel(), 0);
                        fi.close();
                        fo.close();                              
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException es) {
                        es.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Файл настроек не найден и будет создан!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    try {
                        mainForm.setProp();
                        FileOutputStream fo = new FileOutputStream(settings);
                        Properties prop = new Properties();
                        prop.setProperty("address", jAddress.getText());
                        prop.setProperty("smtpPort", mainForm.getSmtpPort());
                        prop.setProperty("smtpHost", mainForm.getSmtpHost());
                        prop.setProperty("login", mainForm.getLogin());
                        prop.setProperty("password", mainForm.getPassword());
                        prop.store(fo, null);
                        mainForm.setProp();
                    //    treePanel.create(jAddress.getText());
                        fo.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                me.setVisible(false);
            }
        });

        jAddress.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                jAddress.selectAll();
            }

            public void focusLost(FocusEvent e) {
            }
        });

        Font font = new Font("Verdana", Font.PLAIN, 11);
        addressLabel.setFont(font);
        createButton.setFont(font);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(10, 0, 10, 0);
        layout.setConstraints(addressLabel, constraints);
        this.add(addressLabel);

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
        layout.setConstraints(jAddress, constraints);
        this.add(jAddress);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(createButton, constraints);
        this.add(createButton);

        this.setSize(400, 120);
        this.setTitle("Создание почтового ящика");
        this.setResizable(true);
        this.setLocation(150, 150);
        this.setVisible(true);
    }
}
