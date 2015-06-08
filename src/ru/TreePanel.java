package ru;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.awt.*;

public class TreePanel extends JPanel {
    private JTree tree;
    private Object[] hierarchy;
    private GridBagLayout layout;
    private int count;
    private MainForm mainForm;

    TreePanel(MainForm mainForm) {
        this.mainForm = mainForm;
        create(mainForm.getAddress(), mainForm.getListPanel(), 0);
    }

    public void create(String rootName, final ListPanel listPanel, int n) {
        if (n!=0){
        hierarchy = new Object[]{"Quizful", new Object[]{rootName,
                "Входящие ("+n+")",
                "Исходящие"}};
            count = n;
        }
            else hierarchy = new Object[]{"Quizful", new Object[]{rootName,
                "Входящие",
                "Исходящие"}};
        DefaultMutableTreeNode root = processHierarchy(hierarchy);
            if (tree!=null) this.remove(tree);
        tree = new JTree(root);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreeModel m = tree.getModel();

                if (tree.getSelectionCount()==1) {
                    int num = tree.getSelectionRows()[0];
                        if (num==2){
                           // listPanel.setIn(true);
                            listPanel.setIn(true);
                            listPanel.createIn();
                            mainForm.getReplyMessageItem().setVisible(true);
                              //  else listPanel.getPane().setVisible(true);
                        } else {
                            if (num==3){
                         //       listPanel.setIn(false);
                                listPanel.setIn(false);
                                listPanel.createOut();
                                mainForm.getReplyMessageItem().setVisible(false);
                          //          else listPanel.getPane().setVisible(true);
                            }
                        }
                }
            }
        });

        layout = new GridBagLayout();
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
        //treeView = new JScrollPane(tree);
        layout.setConstraints(tree, constraints);
        this.add(tree);
        this.updateUI();
        this.repaint();
    }

    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(hierarchy[0]);
        DefaultMutableTreeNode child;
        for (int i = 1; i < hierarchy.length; i++) {
            Object nodeSpecifier = hierarchy[i];
            if (nodeSpecifier instanceof Object[])  // Ie node with children
                child = processHierarchy((Object[]) nodeSpecifier);
            else
                child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
            node.add(child);
        }
        return (node);
    }

    public JTree getTree() {
        return tree;
    }

    public int getCount() {
        return count;
    }
}
