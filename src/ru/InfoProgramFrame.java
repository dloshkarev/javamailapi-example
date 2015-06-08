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

        text.setText("Данный почтовый клиент разработан специально для сайта www.quizful.net и демонстрирует основные приемы работы с Java Mail Api:" +
                "\n"+" - Отправку простых писем через протокол smtp" +
                "\n"+" - Получение простых писем с помощью протокола pop3" +
                "\n"+" - Отправка и получение писем с вложениями" +
                "\n"+" - Удаление писем (с сервера тоже)" +
                "\n"+" - Ответ на письмо" +
                "\n"+"\n"+"Для начала работы необходимо:" +
                "\n"+" - Создать новый почтовый ящик" +
                "\n"+" - Установить настройки сервера" +
                "\n"+"\n"+"Сообщения полученные с сервера кэшируются во внешнем файле, поэтому доступ к ним возможен и без соединения с интернетом. Сообщения в списке сообщений можно удалять клавишей 'delete'. Аттачами можно управлять через контекстное меню в Отправке сообщений. Аттачи полученные в сообщениях можно сохранить в другое место.");
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
        this.setTitle("О программе");
        this.setResizable(false);
        this.setLocation(250, 150);
        this.setVisible(true);
    }
}
