package com.compi.frontend;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;

public class FileTreePanel extends JPanel {
    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;

    public FileTreePanel(){
        setLayout(new BorderLayout());
        rootNode = new DefaultMutableTreeNode("Explorer");
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree = new JTree(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        add(new JScrollPane(tree), BorderLayout.CENTER);
        tree.addTreeSelectionListener(e -> {
            TreePath path = e.getPath();
            if(path != null){
                Object node = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
                if(node instanceof File){
                    File f = (File)node;
                    if(f.isFile()){
                        // notify listener
                    }
                }
            }
        });
    }

    public void loadDirectory(File dir){
        rootNode.removeAllChildren();
        if(dir != null && dir.isDirectory()){
            buildTree(rootNode, dir);
        }
        ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(rootNode);
    }

    private void buildTree(DefaultMutableTreeNode parent, File dir){
        File[] files = dir.listFiles();
        if(files == null) return;
        for(File f : files){
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
            parent.add(child);
            if(f.isDirectory()){
                buildTree(child, f);
            }
        }
    }
}
