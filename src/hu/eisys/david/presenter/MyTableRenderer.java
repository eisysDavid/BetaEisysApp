package hu.eisys.david.presenter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import sun.swing.DefaultLookup;

@SuppressWarnings("serial")
public class MyTableRenderer extends DefaultTableCellRenderer {

	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
	private Color unselectedBackground;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (table == null) {
			return this;
		}

		Color fg = null;

		table.getColumnModel().getColumn(0).setMaxWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);

		if (value instanceof JLabel) {
			JTable.DropLocation dropLocation = table.getDropLocation();
			JLabel label = (JLabel) value;
			label.setOpaque(true);
			Color color = label.getBackground();

			if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn()
					&& dropLocation.getRow() == row && dropLocation.getColumn() == column) {

				fg = DefaultLookup.getColor(label, ui, "Table.dropCellForeground");
				isSelected = true;
			}

			if (isSelected) {
				label.setForeground(fg == null ? table.getSelectionForeground() : fg);
				label.setBackground(color == null ? table.getSelectionBackground() : color);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
				label.setFont(new Font("Consolas", Font.CENTER_BASELINE, 17));
			} else {
				Color background = unselectedBackground != null ? unselectedBackground : table.getBackground();
				if (background == null || background instanceof javax.swing.plaf.UIResource) {
					Color alternateColor = DefaultLookup.getColor((JLabel) value, ui, "Table.alternateRowColor");
					if (alternateColor != null && row % 2 != 0) {
						background = alternateColor;
					}
				}
				if (color == Color.YELLOW) {
					label.setBackground(Color.YELLOW);
				} else if (color == Color.RED) {
					label.setBackground(Color.RED);
				} else {
					label.setBackground(background);
				}
				label.setFont(new Font("Consolas", Font.CENTER_BASELINE, 12));
				label.setBorder(noFocusBorder);
			}

			table.setRowHeight(30);
			return label;
		} else {
			setValue(value);
			return this;
		}
	}

}
