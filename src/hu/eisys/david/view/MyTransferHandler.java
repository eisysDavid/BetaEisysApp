package hu.eisys.david.view;


import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class MyTransferHandler extends TransferHandler {

	private int startPositionRow;
	private int startPositionCol;
	private IMyTransferHandler iUpdate;

	public int getSourceActions(JComponent c) {	
		return DnDConstants.ACTION_COPY_OR_MOVE;
	}

	@SuppressWarnings("unused")
	public Transferable createTransferable(JComponent comp) {	
		JTable table = (JTable) comp;
		startPositionRow = table.getSelectedRow();
		startPositionCol = table.getSelectedColumn();
		Object value = table.getValueAt(startPositionRow, startPositionCol);
		return new CellDataTransferable(new CellData(table));
	}

	public boolean canImport(TransferHandler.TransferSupport info) {
		return true;
	}
	
	   
	protected void exportDone(JComponent c, Transferable t, int act) {
	      
	}

	public boolean importData(TransferSupport support) {

		boolean imported = false;
		// Only import into JTables...
		Component comp = support.getComponent();
		if (comp instanceof JTable) {
			JTable target = (JTable) comp;
			// Need to know where we are importing to...
			DropLocation dl = support.getDropLocation();
			Point dp = dl.getDropPoint();
			int dropRow = target.rowAtPoint(dp);			
			
			try {
				// Get the Transferable at the heart of it all
				Transferable t = support.getTransferable();
				CellData cd = (CellData) t.getTransferData(CellDataTransferable.CELL_DATA_FLAVOR);	
				if (cd.getTable() == target) {
					iUpdate.updateArrayList(dropRow);
					imported = true;
				
			}
			} 
			catch (UnsupportedFlavorException | IOException ex) {
					ex.printStackTrace();
				}

			}
			return imported;
	}

	public MyTransferHandler(IMyTransferHandler iUpdate) {
		this.iUpdate = iUpdate;
	}

}