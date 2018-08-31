package hu.eisys.david.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import hu.eisys.david.presenter.IBundle;

@SuppressWarnings("serial")
public class MyTreePath extends JPopupMenu {

	IBundle res;
	
	private JMenuItem[] menuItems;

	public MyTreePath(IMyTreePath myTreePath, IBundle r) {
		
		res = r;
		
		menuItems = new JMenuItem[] {
				new JMenuItem(res.getString("treeExpand")),
     			new JMenuItem(res.getString("treeCollapse"))
     			};

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
