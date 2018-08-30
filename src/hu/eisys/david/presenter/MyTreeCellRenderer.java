package hu.eisys.david.presenter;

import java.awt.Component;
import java.awt.Font;
import java.io.File;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof File) {
			File fl = (File) value;
			if (fl.isDirectory() && (row == 0)) {
				setFont(new Font("Consolas", Font.BOLD, 20));
				setIcon(UIManager.getIcon("FileView.computerIcon"));
				setText(fl.getPath());

			}

			else if (fl.isDirectory()) {
				setFont(new Font("Consolas", Font.BOLD, 16));
				setIcon(UIManager.getIcon("Tree.openIcon"));
				setText(fl.getName());
			}

			else {
				setFont(new Font("Consolas", Font.ITALIC, 14));
				setIcon(UIManager.getIcon("Tree.leafIcon"));
				setText(fl.getName());
			}
		}
		return this;
	}

}
