package hu.eisys.david.view;

import javax.swing.JTable;

public class CellData {
	
	private  Object value;
	private  int col;
	private  JTable table;
	private  int row;

	
	
	public int getRow() {
		return row;
	}

	public CellData(JTable source) {
		this.col = source.getSelectedColumn();
		this.row = source.getSelectedRow();
		this.value = source.getValueAt(row, col);
		this.table = source;
	}
	
	public CellData() {
	}

	public int getColumn() {
		return col;
	}

	public Object getValue() {
		return value;
	}

	public JTable getTable() {
		return table;
	}

	
}

