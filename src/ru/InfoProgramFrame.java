package ru;

import javax.swing.*;
import java.awt.*;

public class InfoProgramFrame extends JFrame {
    private JTextArea text = new JTextArea(5, 5);
    private JScrollPane textPane = new JScrollPane(text);

    public InfoProgramFrame() {
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        text.setText("������ �������� ������ ���������� ���������� ��� ����� www.quizful.net � ������������� �������� ������ ������ � Java Mail Api:" +
                "\n"+" - �������� ������� ����� ����� �������� smtp" +
                "\n"+" - ��������� ������� ����� � ������� ��������� pop3" +
                "\n"+" - �������� � ��������� ����� � ����������" +
                "\n"+" - �������� ����� (� ������� ����)" +
                "\n"+" - ����� �� ������" +
                "\n"+"\n"+"��� ������ ������ ����������:" +
                "\n"+" - ������� ����� �������� ����" +
                "\n"+" - ���������� ��������� �������" +
                "\n"+"\n"+"��������� ���������� � ������� ���������� �� ������� �����, ������� ������ � ��� �������� � ��� ���������� � ����������. ��������� � ������ ��������� ����� ������� �������� 'delete'. �������� ����� ��������� ����� ����������� ���� � �������� ���������. ������ ���������� � ���������� ����� ��������� � ������ �����.");
        textPane.setEnabled(false);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);

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


        this.setSize(500, 400);
        this.setTitle("� ���������");
        this.setResizable(false);
        this.setLocation(250, 150);
        this.setVisible(true);
    }
}
