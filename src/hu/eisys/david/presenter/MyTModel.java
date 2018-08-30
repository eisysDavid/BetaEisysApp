package hu.eisys.david.presenter;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import hu.eisys.david.model.Adhock;
import hu.eisys.david.model.DbChange;
import hu.eisys.david.model.Efile;
import hu.eisys.david.model.Script;
import hu.eisys.david.model.StepNames;

@SuppressWarnings("serial")
public class MyTModel extends AbstractTableModel {

	private Script sc;
	private String[] columnNames = { "", "Script", "Comment" };
	private Object[][] data = { { new JLabel(), new Integer(1), new String() } };
	private JLabel myLabel;
	private int j = 0;
	private String myComment;
	private final Icon EFILE = new ImageIcon("eFile.png");
	private final Icon ADHOCK = new ImageIcon("adhock.png");
	private final Icon DBCHANGE = new ImageIcon("dbChange.png");

	public MyTModel(Script sc) {
		this.sc = sc;
		data = new Object[sc.getStepContainer().size()][columnNames.length];
		getModel();
	}

	public void getModel() {
		sc.getStepContainer().forEach(mySteplist -> {
			myComment = mySteplist.getComment() == null ? "" : mySteplist.getComment();
			if (mySteplist.getType().equals(StepNames.EFIEL)) {
				Efile myEfile = (Efile) mySteplist;
				myLabel = new JLabel(myEfile.getShowElement(), EFILE, SwingConstants.LEFT);

				if (!myEfile.getFile().exists()) {
					myLabel.setBackground(Color.RED);
				}

				data[j][0] = new Integer(j + 1);
				data[j][1] = myLabel;
				data[j][2] = myComment;
			}

			else if (mySteplist.getType().equals(StepNames.DBCHANGE)) {
				DbChange myDb = (DbChange) mySteplist;
				myLabel = new JLabel(myDb.getShowElement(), DBCHANGE, SwingConstants.LEFT);
				myLabel.setBackground(Color.YELLOW);

				data[j][0] = new Integer(j + 1);
				data[j][1] = myLabel;
				data[j][2] = myComment;
			} else {
				Adhock myAdhock = (Adhock) mySteplist;
				myLabel = new JLabel(myAdhock.getShowElement(), ADHOCK, SwingConstants.LEFT);

				data[j][0] = new Integer(j + 1);
				data[j][1] = myLabel;
				data[j][2] = myComment;
			}
			j++;
		});
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for each
	 * cell. If we didn't implement this method, then the last column would contain
	 * text ("true"/"false"), rather than a check box.
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
//Note that the data/cell address is constant,
//no matter where the cell appears onscreen.
		if (col == 1 || col == 0) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);

	}

}
