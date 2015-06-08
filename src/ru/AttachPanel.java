package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AttachPanel extends JPanel {
    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private AttachPanel me = this;

    AttachPanel() {
        layout = new GridBagLayout();
        this.setLayout(layout);
        constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = GridBagConstraints.RELATIVE;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 5, 0, 5);
    }

    public void addAttachButton(final String filename) {
        ImageIcon attachIcon = new ImageIcon("mail-attachment.png");
        JButton attach = new JButton(attachIcon);
        layout.setConstraints(attach, constraints);
        this.add(attach);

        attach.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                ImagePreviewPanel preview = new ImagePreviewPanel();
                chooser.setAccessory(preview);
                chooser.addPropertyChangeListener(preview);
                File file = new File(filename);
                byte[] content = new byte[(int) file.length()];
                try {
                    FileInputStream fi = new FileInputStream(file);
                    fi.read(content);
                    fi.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                chooser.setSelectedFile(file);
                int result = chooser.showSaveDialog(chooser);
                if (result == 0) {
                    try {
                        FileOutputStream fo = new FileOutputStream(chooser.getSelectedFile().getPath());
                        fo.write(content);
                        fo.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public void deleteAttachButton(){
        this.removeAll();
        this.updateUI();
    }

}
