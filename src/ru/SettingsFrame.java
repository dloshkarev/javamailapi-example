package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.Properties;

public class SettingsFrame extends JFrame {
    private JLabel smtpPortLabel = new JLabel("Порт исходящей почты: ");
    private JLabel smtpHostLabel = new JLabel("Сервер исходящей почты: ");
    private JLabel loginLabel = new JLabel("Логин: ");
    private JLabel passwordLabel = new JLabel("Пароль: ");
    private JLabel pop3HostLabel = new JLabel("Сервер входящей почты: ");
    private JTextField smtpPortField = new JTextField(20);
    private JTextField smtpHostField = new JTextField(20);
    private JTextField loginField = new JTextField(30);
    private JPasswordField passwordField = new JPasswordField(30);
    private JTextField pop3HostField = new JTextField(20);
    private JPanel outPanel = new JPanel();
    private JPanel inPanel = new JPanel();
    private JPanel authPanel = new JPanel();
    private static JButton okButton = new JButton("Применить");

    SettingsFrame(final MainForm mainForm) {
        final JFrame me = this;
        Font font = new Font("Verdana", Font.PLAIN, 11);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        mainForm.setProp();
        smtpPortField.setText(mainForm.getSmtpPort());
        smtpHostField.setText(mainForm.getSmtpHost());
        loginField.setText(mainForm.getLogin());
        passwordField.setText(mainForm.getPassword());
        pop3HostField.setText(mainForm.getPop3Host());

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File settings = new File("settings.properties");
                if (settings.exists()) {
                    try {
                        mainForm.setProp();
                        FileInputStream fi = new FileInputStream(settings);
                        FileOutputStream fo = new FileOutputStream(settings);
                        Properties prop = new Properties();
                        prop.load(fi);
                        prop.setProperty("smtpPort", smtpPortField.getText());
                        prop.setProperty("smtpHost", smtpHostField.getText());
                        prop.setProperty("login", loginField.getText());
                        prop.setProperty("password", new String(passwordField.getPassword()));
                        prop.setProperty("address", mainForm.getAddress());
                        prop.setProperty("pop3Host", pop3HostField.getText());
                        prop.store(fo, null);
                        fi.close();
                        fo.close();
                        mainForm.setProp();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException es) {
                        es.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Файл настроек не найден и будет создан!", "Ошибка!", JOptionPane.ERROR_MESSAGE);                    
                    try {
                        FileOutputStream fo = new FileOutputStream(settings);
                        Properties prop = new Properties();
                        prop.setProperty("smtpPort", smtpPortField.getText());
                        prop.setProperty("smtpHost", smtpHostField.getText());
                        prop.setProperty("login", loginField.getText());
                        prop.setProperty("password", new String(passwordField.getPassword()));
                        prop.setProperty("pop3Host", pop3HostField.getText());
                        prop.store(fo, null);
                        mainForm.setProp();
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

        loginField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                loginField.selectAll();
            }

            public void focusLost(FocusEvent e) {
            }
        });

        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                passwordField.selectAll();
            }

            public void focusLost(FocusEvent e) {
            }
        });

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
        layout.setConstraints(authPanel, constraints);
        authPanel.setBorder(BorderFactory.createTitledBorder("Настройка аутентификации: "));
        this.add(authPanel);

        smtpPortLabel.setFont(font);
        smtpHostLabel.setFont(font);
        loginLabel.setFont(font);
        passwordLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 0;
        layout.setConstraints(outPanel, constraints);
        outPanel.setBorder(BorderFactory.createTitledBorder("Настройка исходящей почты: "));
        this.add(outPanel);

        smtpPortLabel.setFont(font);
        smtpHostLabel.setFont(font);
        pop3HostLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.ipady = 0;
        layout.setConstraints(inPanel, constraints);
        inPanel.setBorder(BorderFactory.createTitledBorder("Настройка входящей почты: "));
        this.add(inPanel);

        okButton.setFont(font);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.ipady = 0;
        layout.setConstraints(okButton, constraints);
        this.add(okButton);

        outPanel.setLayout(layout);
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 0;
        layout.setConstraints(smtpHostLabel, constraints);
        outPanel.add(smtpHostLabel);
        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(smtpPortLabel, constraints);
        outPanel.add(smtpPortLabel);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
        layout.setConstraints(smtpHostField, constraints);
        outPanel.add(smtpHostField);
        constraints.gridx = 1;
        constraints.gridy = 1;
        layout.setConstraints(smtpPortField, constraints);
        outPanel.add(smtpPortField);

        inPanel.setLayout(layout);
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        layout.setConstraints(pop3HostLabel, constraints);
        inPanel.add(pop3HostLabel);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
        layout.setConstraints(pop3HostField, constraints);
        inPanel.add(pop3HostField);          

        authPanel.setLayout(layout);
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        layout.setConstraints(loginLabel, constraints);
        authPanel.add(loginLabel);

        constraints.gridy = 1;
        layout.setConstraints(passwordLabel, constraints);
        authPanel.add(passwordLabel);

        constraints.gridy = 2;

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
        layout.setConstraints(loginField, constraints);
        authPanel.add(loginField);

        constraints.gridy = 1;
        layout.setConstraints(passwordField, constraints);
        authPanel.add(passwordField);

        this.setSize(500, 400);
        this.setTitle("Настройки сервера");
        this.setResizable(true);
        this.setLocation(250, 150);
        this.setVisible(true);
    }
}
