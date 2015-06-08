package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;
import java.util.Properties;
import javax.mail.*;

public class MainForm extends JFrame {
    private ContentPanel contentPanel = new ContentPanel();
    private ListPanel listPanel = new ListPanel(contentPanel.text, this);
    private TreePanel treePanel;
    private AttachPanel attachPanel = new AttachPanel();
    private CreateBoxFrame createBoxFrame;
    private SendFrame sendFrame;
    private SettingsFrame settingsFrame;
    private LinkedList<MessageBean> messageList = new LinkedList<MessageBean>();
    private JMenuItem replyMessageItem;
    private InfoProgramFrame infoProgramFrame;

    private String login = "";
    private String password = "";
    private String smtpPort = "";
    private String smtpHost = "";
    private String address = "";
    private String pop3Host = "";

    public MainForm() {
        final MainForm me = this;
        setProp();
        treePanel = new TreePanel(this);

        Font font = new Font("Verdana", Font.PLAIN, 11);
        JMenuBar menuBar = new JMenuBar();
        JMenu postBoxMenu = new JMenu("Почтовый ящик");
        postBoxMenu.setFont(font);
        JMenuItem createItem = new JMenuItem("Создать");
        createItem.setFont(font);
        postBoxMenu.add(createItem);

        JMenu messageMenu = new JMenu("Сообщения");
        messageMenu.setFont(font);
        JMenuItem sendItem = new JMenuItem("Отправить");
        sendItem.setFont(font);
        messageMenu.add(sendItem);
        JMenuItem recieveItem = new JMenuItem("Получить");
        recieveItem.setFont(font);
        messageMenu.add(recieveItem);
        replyMessageItem = new JMenuItem("Ответить");
        replyMessageItem.setFont(font);
        messageMenu.add(replyMessageItem);
        JMenuItem deleteMessageItem = new JMenuItem("Удалить");
        deleteMessageItem.setFont(font);
        messageMenu.add(deleteMessageItem);

        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setFont(font);
        JMenuItem serverItem = new JMenuItem("Настройки сервера");
        serverItem.setFont(font);
        settingsMenu.add(serverItem);

        JMenu helpMenu = new JMenu("Помощь");
        helpMenu.setFont(font);
        JMenuItem byProgramItem = new JMenuItem("О программе");
        byProgramItem.setFont(font);
        helpMenu.add(byProgramItem);

        menuBar.add(postBoxMenu);
        menuBar.add(messageMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);

        createItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (createBoxFrame != null) {
                    createBoxFrame.setVisible(true);
                } else createBoxFrame = new CreateBoxFrame(me, treePanel);
            }
        });

        byProgramItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                infoProgramFrame = new InfoProgramFrame();
            }
        });

        sendItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sendFrame != null) {
                    sendFrame.clearList();
                    sendFrame.setVisible(true);
                 //   sendFrame.setTo("");
                 //   sendFrame.setContent("");
                 //   sendFrame.setSubject("");
                    sendFrame.setFrom();
                } else sendFrame = new SendFrame(login, password, smtpPort, smtpHost, listPanel);
                 //   sendFrame.setTo("");
                 //   sendFrame.setContent("");
                 //   sendFrame.setSubject("");      //todo убрать потом эту бодягу
                    sendFrame.setFrom(); 
            }
        });

        replyMessageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sendFrame != null) {
                    sendFrame.setVisible(true);
                } else sendFrame = new SendFrame(login, password, smtpPort, smtpHost, listPanel);
                JTable table = listPanel.getTable();
                sendFrame.setTo((String) table.getValueAt(table.getSelectedRow(), 1));
                sendFrame.setSubject("re: "+ (String) table.getValueAt(table.getSelectedRow(), 0));
                sendFrame.setContent("Вы писали: "+"\n"+listPanel.getText().getText());
            }
        });

        recieveItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Mail mail = new Mail();
                boolean isFile = false;
                boolean isNotHereGlobal = false;
                boolean isInFile = false;
                try {
                    LinkedList<MessageBean> messages = mail.receive(login, password, pop3Host);
                    File file = new File("saveIn.txt");
                    if (file.exists()) {
                        isFile = true;
                        FileInputStream fi = new FileInputStream(file);
                        ObjectInputStream oi = new ObjectInputStream(fi);
                        if (fi.available() > 0) {
                            messageList = (LinkedList<MessageBean>) oi.readObject();
                            fi.close();
                            oi.close();
                        }
                    }
                    FileOutputStream fo = new FileOutputStream(file);
                    ObjectOutputStream os = new ObjectOutputStream(fo);
                    if (messageList.size() > 0) isInFile = true;
                    boolean isHere = false;
                    int count = 0;
                    for (MessageBean message : messages) {
                        if (messageList.size() > 0)
                            for (MessageBean msg : messageList) {
                                if (msg.getSubject().equals(message.getSubject()) && msg.getFrom().equals(message.getFrom()) && msg.getDateSent().equals(message.getDateSent())) {
                                    if (msg.isNew()) message.setNew(true);
                                    isHere = true;
                                }
                            }
                        if (!isHere || !isFile) {
                            count++;
                            isNotHereGlobal = true;
                            message.setNew(true);
                            messageList.add(message);
                        }
                        isHere = false;

                    }
                    treePanel.create(address, listPanel, count);
                    treePanel.getTree().expandPath(treePanel.getTree().getPathForRow(1));
                    count = 0;
                    os.writeObject(messageList);
                    listPanel.setMessageList(messageList);
                    os.close();
                    fo.close();
                    listPanel.createIn();
                    listPanel.updateUI();
                    if (!isInFile && !isNotHereGlobal && messageList.size() > 0)
                        JOptionPane.showMessageDialog(null, "Сообщения получены", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    else if (isNotHereGlobal)
                        JOptionPane.showMessageDialog(null, "У вас есть новые сообщения", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    else if (!isNotHereGlobal)
                        JOptionPane.showMessageDialog(null, "Новых сообщений нет", "Информация", JOptionPane.INFORMATION_MESSAGE);
                } catch (MessagingException e1) {
                    JOptionPane.showMessageDialog(null, "Ошибка получения сообщений! " + e1, "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + ex, "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (IOException ec) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода вывода! " + ec, "Ошибка!", JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        deleteMessageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMessage();
            }
        });

        serverItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (settingsFrame != null) {
                    settingsFrame.setVisible(true);
                } else settingsFrame = new SettingsFrame(me);
            }
        });

        this.setSize(Config.widthForm, Config.heigthForm);
        this.setFont(font);
        this.setTitle("Почтовый клиент Quizful");
        this.setResizable(true);
        this.setLocation(100, 50);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = Config.widthTreePanel;
        constraints.ipady = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        layout.setConstraints(treePanel, constraints);
        treePanel.setBorder(BorderFactory.createEtchedBorder(1, Color.gray, Color.blue));
        this.add(treePanel);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.ipadx = Config.widthListPanel;
        constraints.ipady = Config.heigthListPanel;
        layout.setConstraints(listPanel, constraints);
        listPanel.setBorder(BorderFactory.createEtchedBorder(1, Color.gray, Color.blue));
        this.add(listPanel);

        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.ipadx = Config.widthContentPanel;
        constraints.ipady = Config.heigthContentPanel;
        layout.setConstraints(contentPanel, constraints);
        this.add(contentPanel);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.ipadx = Config.widthContentPanel;
        constraints.ipady = Config.heigthAttachPanel;
        attachPanel.setBorder(BorderFactory.createEtchedBorder(1, Color.gray, Color.blue));
        layout.setConstraints(attachPanel, constraints);
        this.add(attachPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getAddress() {
        return address;
    }

    public ListPanel getListPanel() {
        return listPanel;
    }

    public void setProp() {
        File settings = new File("settings.properties");
        if (settings.exists()) {
            try {
                FileInputStream fi = new FileInputStream(settings);
                Properties prop = new Properties();
                prop.load(fi);
                this.login = prop.getProperty("login");
                this.password = prop.getProperty("password");
                this.smtpPort = prop.getProperty("smtpPort");
                this.smtpHost = prop.getProperty("smtpHost");
                this.address = prop.getProperty("address");
                this.pop3Host = prop.getProperty("pop3Host");
                fi.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Файл не найден! " + e, "Ошибка!", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка ввода-вывода! " + e, "Ошибка!", JOptionPane.ERROR_MESSAGE);
            }
        } else
            JOptionPane.showMessageDialog(null, "Настройки сервера не установлены!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
    }

    public String getPop3Host() {
        return pop3Host;
    }

    public TreePanel getTreePanel() {
        return treePanel;
    }

    public AttachPanel getAttachPanel() {
        return attachPanel;
    }

    public JMenuItem getReplyMessageItem() {
        return replyMessageItem;
    }

    public void deleteMessage(){
        if (JOptionPane.showConfirmDialog(null, "Вы хотите удалить это сообщение?", "Предупреждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    attachPanel.deleteAttachButton();
                    int selectedRow = listPanel.getTable().getSelectedRow();
                    listPanel.setDelete(true);
                    listPanel.getModel().removeRow(selectedRow);
                    if (listPanel.isIn()) {
                        File file = new File("saveIn.txt");
                        if (file.exists()) {
                            try {
                                FileInputStream fi = new FileInputStream(file);
                                ObjectInputStream is = new ObjectInputStream(fi);
                                if (fi.available() > 0) {
                                    messageList = (LinkedList<MessageBean>) is.readObject();
                                }
                                Mail mail = new Mail();
                                mail.delete(login, password, pop3Host, messageList.get(selectedRow).getMsgId());

                                is.close();
                                fi.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            } catch (ClassNotFoundException e3) {
                                e3.printStackTrace();
                            } catch (MessagingException e1) {
                                JOptionPane.showMessageDialog(null, "Не удалось удалить сообщение с сервера! " + e1, "Ошибка!", JOptionPane.ERROR_MESSAGE);
                                e1.printStackTrace();
                            }
                            messageList.remove(selectedRow);
                            for (int i = 0; i < messageList.size(); i++) {
                                if (i >= selectedRow) {
                                    messageList.get(i).setMsgId(messageList.get(i).getMsgId() - 1);
                                }
                            }
                            file.delete();
                            try {
                                FileOutputStream fo = new FileOutputStream(file);
                                ObjectOutputStream os = new ObjectOutputStream(fo);
                                os.writeObject(messageList);
                                os.close();
                                fo.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        File file = new File("saveOut.txt");
                        if (file.exists()) {
                            try {
                                FileInputStream fi = new FileInputStream(file);
                                ObjectInputStream is = new ObjectInputStream(fi);
                                messageList.clear();
                                while (fi.available() > 0) {
                                    messageList.add((MessageBean) is.readObject());
                                }
                                is.close();
                                fi.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            } catch (ClassNotFoundException e3) {
                                e3.printStackTrace();
                            }
                            messageList.remove(selectedRow);
                            file.delete();
                            try {
                                FileOutputStream fo = new FileOutputStream(file);
                                ObjectOutputStream os = new ObjectOutputStream(fo);
                                if (messageList.size()>0) {
                                    for (MessageBean aMessageList : messageList) {
                                        os.writeObject(new MessageBean(aMessageList.getMsgId(), aMessageList.getSubject(), aMessageList.getFrom(), aMessageList.getTo(), aMessageList.getDateSent(), aMessageList.getContent(), false, aMessageList.getAttachments()));
                                    }
                                }
                                os.close();
                                fo.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
    }
}