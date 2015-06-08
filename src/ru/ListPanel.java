package ru;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListPanel extends JPanel {
    private LinkedList<MessageBean> messageList = null;
    private LinkedList<MessageBean> list = null;
    private JTable table;
    private JScrollPane pane;
    private JTextArea text;
    private boolean isIn;
    private DefaultTableModel model;

    private boolean isDelete;
    private MainForm mainForm;

    ListPanel(final JTextArea text, MainForm mainForm) {
        this.text = text;
        this.mainForm = mainForm;
    }

    public void createIn() {
        if (table != null) {
            this.remove(table);
            this.remove(pane);
        }
        String data[][] = {};
        final File file = new File("saveIn.txt");
        boolean[] isNew = {};
        if (file.exists()) {
            try {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream is = new ObjectInputStream(fi);
                if (fi.available() > 0) {
                    messageList = (LinkedList<MessageBean>) is.readObject();
                    data = new String[messageList.size()][3];
                    MessageBean message;
                    isNew = new boolean[messageList.size()];
                    for (int i = 0; i < messageList.size(); i++) {
                        message = messageList.get(i);
                        data[i][0] = message.getSubject();
                        data[i][1] = message.getFrom();
                        data[i][2] = message.getDateSent();
                        if (message.isNew()) isNew[i] = true;
                    }
                }
                is.close();
                fi.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        String col[] = {"Тема", "Отправитель", "Дата"};
        model = new myModel(data, col);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.getHSBColor(152, 200, 212));
        pane = new JScrollPane(table);

        table.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==127){
                    mainForm.deleteMessage();    
                }
            }
            public void keyReleased(KeyEvent e) {}
        });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    if (!isDelete) {
                        if (file.exists()) {
                            try {
                                FileInputStream fi = new FileInputStream(file);
                                ObjectInputStream is = new ObjectInputStream(fi);
                                if (fi.available() > 0) {
                                    messageList = (LinkedList<MessageBean>) is.readObject();
                                }
                                is.close();
                                fi.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        int selectedRow = table.getSelectedRow();
                        MessageBean message = messageList.get(selectedRow);
                        messageList.get(selectedRow).setNew(false);
                        try {
                            file.delete();
                            FileOutputStream fo = new FileOutputStream(file);
                            ObjectOutputStream os = new ObjectOutputStream(fo);
                            os.writeObject(messageList);
                            os.close();
                            fo.close();
                            text.setText(message.getContent());
                            text.setCaretPosition(0);
                            mainForm.getAttachPanel().deleteAttachButton();
                            if (message.getAttachments()!=null){
                                for (int i=0; i<message.getAttachments().size();i++){
                                    String path = System.getProperty("user.dir")+"\\"+message.getAttachments().get(i);
                                    mainForm.getAttachPanel().addAttachButton(path);
                                    mainForm.getAttachPanel().updateUI();
                                }
                            }
                                if (mainForm.getTreePanel().getCount()>0) {
                                    mainForm.getTreePanel().create(mainForm.getAddress(), mainForm.getListPanel(), mainForm.getTreePanel().getCount()-1);
                                    mainForm.getTreePanel().getTree().expandPath(mainForm.getTreePanel().getTree().getPathForRow(1));
                                }
                            setUnBoldRow(selectedRow);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else isDelete = false;

                }
            }
        }
        );

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
        layout.setConstraints(pane, constraints);
        this.add(pane);
        
        ArrayList<Integer> rows = new ArrayList<Integer>();
        for (int i = 0; i < isNew.length; i++) {
            if (isNew[i]) rows.add(i);
        }
        setBoldRow(rows);
    }

    public void createOut() {
        if (table != null) {
            this.remove(table);
            this.remove(pane);
        }
        String data[][] = {};
        File file = new File("saveOut.txt");
        if (file.exists()) {
            try {
                FileInputStream fi = new FileInputStream(file);
                ObjectInputStream is = new ObjectInputStream(fi);
                list = new LinkedList<MessageBean>();
                while (fi.available() > 0) {
                    MessageBean messageBean = (MessageBean) is.readObject();
                    list.add(messageBean);
                }
                is.close();
                fi.close();
                data = new String[list.size()][3];
                for (int i = 0; i < list.size(); i++) {
                    data[i][0] = list.get(i).getSubject();
                    data[i][1] = list.get(i).getTo();
                    data[i][2] = list.get(i).getDateSent();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        String col[] = {"Тема", "Получатель", "Дата"};
        model = new myModel(data, col);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.getHSBColor(152, 200, 212));
        pane = new JScrollPane(table);

        table.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==127){
                    mainForm.deleteMessage();
                }
            }
            public void keyReleased(KeyEvent e) {}
        });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    if (!isDelete) {
                        int selectedRow = table.getSelectedRow();
                        MessageBean message = list.get(selectedRow);
                        text.setText(message.getContent());
                        text.setCaretPosition(0);
                    } else isDelete = false;
                }
            }
        });

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
        layout.setConstraints(pane, constraints);
        this.add(pane);
    }

    public void setBoldRow(ArrayList<Integer> rows) {
        BoldCellRenderer render = new BoldCellRenderer();
        render.setiRow(rows);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(render);
        }
    }

    public void setUnBoldRow(int row) {
        BoldCellRenderer render = new BoldCellRenderer();
        render.setUnSelectRow(row);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(render);
        }
    }

    public void setMessageList(LinkedList<MessageBean> messageList) {
        this.messageList = messageList;
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JTextArea getText() {
        return text;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    public boolean isIn() {
        return isIn;
    }
}

class myModel extends DefaultTableModel {
    public myModel(String[][] data, String[] col) {
        super(data, col);
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}

class BoldCellRenderer extends DefaultTableCellRenderer {
    private ArrayList<Integer> iRow = new ArrayList<Integer>();
    private int unSelectRow;

    public void setiRow(ArrayList<Integer> rows) {
        this.iRow = rows;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (iRow.contains((Integer) row)) {
            c.setFont(new Font("Tahoma", Font.BOLD, 12));
            c.setForeground(Color.black);
        }
        if (row == unSelectRow) {
            c.setFont(new Font("Time New Roman", 0, 12));           //Хз че за шрифт, но больше всех похож
            c.setForeground(Color.black);
        }
        return c;
    }

    public void setUnSelectRow(int unSelectRow) {
        this.unSelectRow = unSelectRow;
    }
}