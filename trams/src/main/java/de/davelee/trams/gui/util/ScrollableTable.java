package de.davelee.trams.gui.util;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * http://stackoverflow.com/questions/10972228/java-jscrollpane-with-jtable-not-shown
 * @author User
 *
 */
public class ScrollableTable extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 594517386167966397L;

	public ScrollableTable(String[][] aData, String[] aColumnNames) {
		super();

		TableModel model = getTableModel(aData, aColumnNames);
		JTable overviewTable = new JTable(model);
		overviewTable.setGridColor(new Color(240, 240, 240));
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		overviewTable.setRowSorter(sorter);

		this.setViewportView(overviewTable);
	}
	
	private TableModel getTableModel(String[][] aData, String[] aColumnNames) {
        TableModel model = new DefaultTableModel(aData, aColumnNames) {
            /**
			 * 
			 */
			private static final long serialVersionUID = -2545694082305307721L;

			public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        return model;
    }
	
}
