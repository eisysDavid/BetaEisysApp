package hu.eisys.david.presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import hu.eisys.david.model.Efile;
import hu.eisys.david.model.Script;
import hu.eisys.david.model.Step;
import hu.eisys.david.model.StepNames;

public class MyTreeModel implements TreeModel {

	private File root;
	private boolean checkBox;
	private Script script;
	private List<String> returnValue;

	public MyTreeModel(File root, boolean checkBox, Script script) {
		this.root = root;
		this.checkBox = checkBox;
		this.script = script;
	}

	public Object getRoot() {
		return root;
	}

	// Fetch any numbered child of a node for the JTree.
	// Our model returns File objects for all nodes in the tree. The
	// JTree displays these by calling the File.toString() method.

	public Object getChild(Object parent, int index) {
		if ((returnValue == null) || (index >= returnValue.size()))
			return null;
		else
			return new File((File) parent, returnValue.get(index));
	}

	// A hozzáadott elemek meghatározása!!
	public void getList(File[] file) {

		returnValue = new ArrayList<>();
		boolean help = true;

		if (!checkBox) {
			for (int i = 0; i < file.length; i++) {
				if (!(file[i].getName().equals(IConstans.JSON_FILE_NAME))) {
					returnValue.add(file[i].getName());
				}
			}
		} else {
			for (int i = 0; i < file.length; i++) {
				for (Step srp : script.getStepContainer()) {
					if (srp.getType().equals(StepNames.EFIEL)) {
						Efile efile = (Efile) srp;
						if ((file[i].getAbsolutePath().equals(efile.getFile().getAbsolutePath())
								|| (file[i].getName().equals(IConstans.JSON_FILE_NAME)))) {
							help = false;
						}
					}
				}
				if (help) {
					returnValue.add(file[i].getName());
				}
				help = true;
			}
		}
	}

	// Tell JTree how many children a node has

	public int getChildCount(Object parent) {
		File[] children = ((File) parent).listFiles();
		try {
			getList(children);
		} catch (Exception e) {
			// Ennek nincsen gyermek nodeja!!
			System.out.println(System.err);
		}
		if (returnValue == null)
			return 0;
		return returnValue.size();
	}

	// Tell JTree whether an object in the tree is a leaf or not
	public boolean isLeaf(Object node) {
		return ((File) node).isFile();
	}

	// Figure out a child's position in its parent node.
	public int getIndexOfChild(Object parent, Object child) {
		String[] children = ((File) parent).list();
		if (children == null)
			return -1;
		String childname = ((File) child).getName();
		for (int i = 0; i < children.length; i++) {
			if (childname.equals(children[i]))
				return i;
		}
		return -1;
	}

	public void addTreeModelListener(TreeModelListener l) {
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	public void removeTreeModelListener(TreeModelListener l) {
	}

}
