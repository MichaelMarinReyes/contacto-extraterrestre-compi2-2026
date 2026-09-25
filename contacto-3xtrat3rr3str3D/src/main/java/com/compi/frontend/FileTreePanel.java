package com.compi.frontend;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

public class FileTreePanel extends JPanel {
    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    private Consumer<File> onFileSelected; // Callback para notificar a MainWindow

    public FileTreePanel(){
        setLayout(new BorderLayout());
        rootNode = new DefaultMutableTreeNode("Explorer");
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree = new JTree(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        // Renderizador personalizado para mostrar solo el nombre limpio del archivo/carpeta
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (value instanceof DefaultMutableTreeNode) {
                    Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
                    if (userObject instanceof File) {
                        File file = (File) userObject;
                        setText(file.getName().isEmpty() ? file.getPath() : file.getName());
                    }
                }
                return this;
            }
        });

        add(new JScrollPane(tree), BorderLayout.CENTER);

        // Manejador de selección de elementos en el árbol
        tree.addTreeSelectionListener(e -> {
            TreePath path = e.getPath();
            if(path != null){
                Object node = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
                if(node instanceof File){
                    File f = (File)node;
                    if(f.isFile() && onFileSelected != null){
                        onFileSelected.accept(f); // Notificamos al MainWindow
                    }
                }
            }
        });
    }

    /**
     * Permite registrar la acción que se ejecutará al hacer clic en un archivo.
     */
    public void setOnFileSelected(Consumer<File> listener) {
        this.onFileSelected = listener;
    }

    /**
     * Carga y despliega una carpeta de proyecto completa en el árbol.
     */
    public void loadDirectory(File dir){
        rootNode.removeAllChildren();
        if(dir != null && dir.isDirectory()){
            rootNode.setUserObject(dir.getName()); // Muestra el nombre del proyecto en la raíz
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
                buildTree(child, f); // Recursividad para subcarpetas
            }
        }
    }

    public void addFileNode(File f) {

    }
}