package hu.eisys.david.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import hu.eisys.david.presenter.MyResourceBundle;

@SuppressWarnings("serial")
public class TreePathShow extends JPopupMenu {

	private JMenuItem[] menuItems = { new JMenuItem(MyResourceBundle.myResource.getString("treeExpand")),
			new JMenuItem(MyResourceBundle.myResource.getString("treeCollapse")) };

	public TreePathShow(MyTreePath myTreePath) {

		add(menuItems[0]);
		add(menuItems[1]);

		menuItems[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myTreePath.expasndTree();
			}
		});
		menuItems[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myTreePath.collapseTree();
			}
		});
	}

}
