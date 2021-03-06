package ru;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

public class SendFrame extends JFrame {
    private JLabel toLabel = new JLabel("����: ");
    private JLabel fromLabel = new JLabel("�� ����: ");
    private JLabel subjectLabel = new JLabel("����: ");
    private JTextField jTo = new JTextField(30);
    private JTextField jFrom = new JTextField(30);
    private JTextField jSubject = new JTextField(30);
    private JPanel infoPanel = new JPanel();
    private JPanel contentPanel = new JPanel();
    private JPanel attachPanel = new JPanel();
    private JTextArea text = new JTextArea(10, 20);
    private JScrollPane textPane = new JScrollPane(text);
    private JList listAttach = new JList();
    private JScrollPane attachPane = new JScrollPane();

    private final JMenuItem itemChange;
    private final JMenuItem itemDelete;
    private final JMenuItem changeItem;
    private final JMenuItem deleteItem;

    private String from;
    private String to;
    private String content;
    private String subject;
    private ArrayList<String> attachments = new ArrayList<String>();

    SendFrame(final String login, final String password, final String smtpPort, final String smtpHost, final ListPanel listPanel) {
        final SendFrame me = this;
        Font font = new Font("Verdana", Font.PLAIN, 11);

        JMenuBar menuBar = new JMenuBar();
        JMenu msgMenu = new JMenu("���������");
        msgMenu.setFont(font);
        JMenuItem sendItem = new JMenuItem("���������");
        sendItem.setFont(font);
        msgMenu.add(sendItem);
        JMenuItem attachItem = new JMenuItem("�������� �����");
        attachItem.setFont(font);
        msgMenu.add(attachItem);
        changeItem = new JMenuItem("�������� �����");
        changeItem.setFont(font);
        msgMenu.add(changeItem);
        deleteItem = new JMenuItem("������� �����");
        deleteItem.setFont(font);
        msgMenu.add(deleteItem);
        menuBar.add(msgMenu);
        this.setJMenuBar(menuBar);
        jFrom.setEnabled(false);

        final JPopupMenu menu = new JPopupMenu("��������: ");
        menu.setFont(font);
        JMenuItem itemAdd = new JMenuItem("��������");
        itemAdd.setFont(font);
        itemChange = new JMenuItem("��������");
        itemChange.setFont(font);
        itemDelete = new JMenuItem("�������");
        itemDelete.setFont(font);

        itemAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                addList();
            }
        });

        itemChange.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                changeList();
            }
        });

        itemDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                deleteList();
            }
        });

        attachItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addList();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteList();
            }
        });

        changeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeList();    
            }
        });

        sendItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Mail mail = new Mail();
                from = jFrom.getText();
                subject = jSubject.getText();
                content = text.getText();
                to = jTo.getText();

                try {
                    mail.send(login, password, from, to, content, subject, attachments, smtpPort, smtpHost);
                    Date date = new Date();
                    File file = new File("saveOut.txt");
                    SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    LinkedList<MessageBean> messageList = new LinkedList<MessageBean>();

                    if (file.exists()) {
                        FileInputStream fi = new FileInputStream(file);
                        ObjectInputStream oi = new ObjectInputStream(fi);
                        while (fi.available() > 0) {
                            messageList.add((MessageBean) oi.readObject());
                        }
                        fi.close();
                        oi.close();
                    }
                    FileOutputStream fo = new FileOutputStream(file);
                    ObjectOutputStream oo = new ObjectOutputStream(fo);
                    if (messageList.size() > 0) {
                        for (MessageBean aMessageList : messageList) {
                            oo.writeObject(new MessageBean(aMessageList.getMsgId(), aMessageList.getSubject(), aMessageList.getFrom(), aMessageList.getTo(), aMessageList.getDateSent(), aMessageList.getContent(), false, attachments));
                        }
                    }
                    oo.writeObject(new MessageBean(0, subject, from, to, f.format(date), content, false, attachments));
                    fo.close();
                    oo.close();
                    JOptionPane.showMessageDialog(null, "��������� ����������", "����������", JOptionPane.INFORMATION_MESSAGE);
                    me.setVisible(false);
                    listPanel.createOut();
                    //listPanel.updateUI();                    ������� ��� ��������� �� ��� ����� ���� � ��� ������������� ������������
                    attachments.clear();
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "������ �������� ���������! " + e1, "������", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (IOException e2) {
                    JOptionPane.showMessageDialog(null, "������ �������� ���������! " + e2, "������", JOptionPane.ERROR_MESSAGE);
                    e2.printStackTrace();
                } catch (ClassNotFoundException e3) {
                    JOptionPane.showMessageDialog(null, "������ �������� ���������! " + e3, "������", JOptionPane.ERROR_MESSAGE);
                    e3.printStackTrace();
                } catch (MessagingException e4) {
                    JOptionPane.showMessageDialog(null, "������ �������� ���������! " + e4, "������", JOptionPane.ERROR_MESSAGE);
                    e4.printStackTrace();
                }
            }
        });

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        jTo.setText("yourmail@rambler.ru");
        jSubject.setText("Test");
        text.setText("��� �������� ���� ���������!");

        from = jFrom.getText();
        to = jTo.getText();
        subject = jSubject.getText();
        content = text.getText();

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
        layout.setConstraints(infoPanel, constraints);
        infoPanel.setBorder(BorderFactory.createEtchedBorder(1));
        this.add(infoPanel);

        toLabel.setFont(font);
        fromLabel.setFont(font);
        subjectLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 40;
        layout.setConstraints(attachPanel, constraints);
        attachPanel.setBorder(BorderFactory.createTitledBorder("��������: "));
        this.add(attachPanel);

        toLabel.setFont(font);
        fromLabel.setFont(font);
        subjectLabel.setFont(font);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.ipady = 310;
        layout.setConstraints(contentPanel, constraints);
        contentPanel.setBorder(BorderFactory.createEtchedBorder(1));
        this.add(contentPanel);

        infoPanel.setLayout(layout);
        constraints.insets = new Insets(10, 0, 10, 0);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 0;
        layout.setConstraints(fromLabel, constraints);
        infoPanel.add(fromLabel);
        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(toLabel, constraints);
        infoPanel.add(toLabel);
        constraints.gridx = 0;
        constraints.gridy = 2;
        layout.setConstraints(subjectLabel, constraints);
        infoPanel.add(subjectLabel);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
        layout.setConstraints(jFrom, constraints);
        infoPanel.add(jFrom);
        constraints.gridx = 1;
        constraints.gridy = 1;
        layout.setConstraints(jTo, constraints);
        infoPanel.add(jTo);
        constraints.gridx = 1;
        constraints.gridy = 2;
        layout.setConstraints(jSubject, constraints);
        infoPanel.add(jSubject);

        contentPanel.setLayout(layout);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        layout.setConstraints(textPane, constraints);
        contentPanel.add(textPane);

       // item.addActionListener(new ActionListener(){

     //   });
        itemChange.setEnabled(false);
        itemDelete.setEnabled(false);
        changeItem.setEnabled(false);
        deleteItem.setEnabled(false);

        menu.add(itemAdd);
        menu.add(itemChange);
        menu.add(itemDelete);

        DefaultListModel listModel = new DefaultListModel();
        listAttach = new JList(listModel);
        listAttach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listAttach.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    if (listAttach.getSelectedValues().length>0){
                        itemChange.setEnabled(true);
                        itemDelete.setEnabled(true);
                        changeItem.setEnabled(true);
                        deleteItem.setEnabled(true);
                    }
                        menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    if (listAttach.getSelectedValues().length>0){
                        itemChange.setEnabled(true);
                        itemDelete.setEnabled(true);
                        changeItem.setEnabled(true);
                        deleteItem.setEnabled(true);
                    }
                        menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
        attachPane = new JScrollPane(listAttach);

        attachPanel.setLayout(layout);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        layout.setConstraints(attachPane, constraints);
        attachPanel.add(attachPane);

        this.setSize(500, 620);
        this.setTitle("����������� ���������");
        this.setResizable(true);
        this.setLocation(250, 100);
        this.setVisible(true);
    }

    private void addList() {
        JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                ImagePreviewPanel preview = new ImagePreviewPanel();
                chooser.setAccessory(preview);
                chooser.addPropertyChangeListener(preview);

                int result = chooser.showDialog(this, "Attach");
                if (result == 0) {
                    boolean isHere = false;
                    File selectedFile = chooser.getSelectedFile();
                    for (int i = 0; i < attachments.size(); i++)
                        if (selectedFile.getPath().equals(attachments.get(i))) isHere = true;
                    if (!isHere) {
                        attachments.add(selectedFile.getPath());
                        setViewAttach(selectedFile.getPath());
                        itemChange.setEnabled(true);
                        itemDelete.setEnabled(true);
                        changeItem.setEnabled(true);
                        deleteItem.setEnabled(true);
                    }
                }
    }

    private void deleteList() {
        DefaultListModel listModel = (DefaultListModel) listAttach.getModel();
                listModel.remove(listAttach.getSelectedIndex());
                attachments.remove(listAttach.getSelectedIndex()+1);
                    if (listModel.getSize()==0){
                        itemChange.setEnabled(false);
                        itemDelete.setEnabled(false);
                        changeItem.setEnabled(false);
                        deleteItem.setEnabled(false);
                    }
    }

    public void clearList() {
        DefaultListModel listModel = (DefaultListModel) listAttach.getModel();
        listModel.removeAllElements();
    }

    public void setTo(String to) {
        this.jTo.setText(to);
    }

    public void setSubject(String subject) {
        this.jSubject.setText(subject);
    }

    public void setContent(String content) {
        this.text.setText(content);
    }

    public void setFrom() {
        File settings = new File("settings.properties");
        if (settings.exists()) {
            try {
                FileInputStream fi = new FileInputStream(settings);
                Properties prop = new Properties();
                prop.load(fi);
                this.jFrom.setText(prop.getProperty("address"));
                fi.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "���� �� ������! " + e, "������!", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "������ �����-������! " + e, "������!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setViewAttach(String fileName) {
        DefaultListModel listModel = (DefaultListModel) listAttach.getModel();
        listModel.addElement(fileName);
    }

    public void changeList(){
        String oldPath = (String) listAttach.getSelectedValue();
                int oldIndex = listAttach.getSelectedIndex()+1;
                DefaultListModel listModel = (DefaultListModel) listAttach.getModel();

                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                ImagePreviewPanel preview = new ImagePreviewPanel();
                chooser.setAccessory(preview);
                chooser.addPropertyChangeListener(preview);

                int result = chooser.showDialog(this, "Attach");
                if (result == 0) {
                    boolean isHere = false;
                    File selectedFile = chooser.getSelectedFile();
                    for (int i = 0; i < attachments.size(); i++)
                        if (selectedFile.getPath().equals(attachments.get(i))) isHere = true;
                    if (!isHere) {
                        oldIndex = attachments.indexOf(oldPath);
                        attachments.remove(oldIndex);
                        attachments.add(oldIndex, selectedFile.getPath());
                        listModel.remove(oldIndex);
                        listModel.add(oldIndex, selectedFile.getPath());
                    }
                }
    }
}

class ImagePreviewPanel extends JPanel
        implements PropertyChangeListener {

    private int width, height;
    private ImageIcon icon;
    private Image image;
    private static final int ACCSIZE = 155;
    private Color bg;

    public ImagePreviewPanel() {
        setPreferredSize(new Dimension(ACCSIZE, -1));
        bg = getBackground();
    }

    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();

        // Make sure we are responding to the right event.
        if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            File selection = (File) e.getNewValue();
            String name;

            if (selection == null)
                return;
            else
                name = selection.getAbsolutePath();

            /*
             * Make reasonably sure we have an image format that AWT can
             * handle so we don't try to draw something silly.
             */
            if ((name != null) &&
                    name.toLowerCase().endsWith(".jpg") ||
                    name.toLowerCase().endsWith(".jpeg") ||
                    name.toLowerCase().endsWith(".gif") ||
                    name.toLowerCase().endsWith(".png")) {
                icon = new ImageIcon(name);
                image = icon.getImage();
                scaleImage();
                repaint();
            }
        }
    }

    private void scaleImage() {
        width = image.getWidth(this);
        height = image.getHeight(this);
        double ratio = 1.0;

        /*
         * Determine how to scale the image. Since the accessory can expand
         * vertically make sure we don't go larger than 150 when scaling
         * vertically.
         */
        if (width >= height) {
            ratio = (double) (ACCSIZE - 5) / width;
            width = ACCSIZE - 5;
            height = (int) (height * ratio);
        } else {
            if (getHeight() > 150) {
                ratio = (double) (ACCSIZE - 5) / height;
                height = ACCSIZE - 5;
                width = (int) (width * ratio);
            } else {
                ratio = (double) getHeight() / height;
                height = getHeight();
                width = (int) (width * ratio);
            }
        }

        image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    public void paintComponent(Graphics g) {
        g.setColor(bg);

        /*
         * If we don't do this, we will end up with garbage from previous
         * images if they have larger sizes than the one we are currently
         * drawing. Also, it seems that the file list can paint outside
         * of its rectangle, and will cause odd behavior if we don't clear
         * or fill the rectangle for the accessory before drawing. This might
         * be a bug in JFileChooser.
         */
        g.fillRect(0, 0, ACCSIZE, getHeight());
        g.drawImage(image, getWidth() / 2 - width / 2 + 5,
                getHeight() / 2 - height / 2, this);
    }

}