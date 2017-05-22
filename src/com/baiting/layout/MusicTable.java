package com.baiting.layout;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.baiting.font.Fonts;

public class MusicTable extends JTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8019332528831298841L;

	public MusicTable() {
		super();
	}
	
	public MusicTable(TableModel dm, TableColumnModel cm) {
		super(dm,cm);
	}
	
	
	public MusicTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm,cm,sm);
	}
	
	public MusicTable(int numRows, int numColumns) {
		super(numRows,numColumns);
	}
	
	@SuppressWarnings("rawtypes")
	public MusicTable(Vector rowData, Vector columnNames) {
		super(rowData,columnNames);
	}
	
	public MusicTable(Object[][] obj,Object[] obj2) {
		super(obj, obj2);
	}
	
	public MusicTable(TableModel dm) {
		super(dm);
	}
	
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void setTableHeader(JTableHeader tableHeader) {
		tableHeader.setPreferredSize(new Dimension(0, 25));
		tableHeader.setFont(Fonts.songTiB13());
		super.setTableHeader(tableHeader);
	}

	@Override
	public void setCellEditor(TableCellEditor anEditor) {
		super.setCellEditor(anEditor);
	}
	
	@Override
	public void setEditingColumn(int aColumn) {
		// TODO Auto-generated method stub
		super.setEditingColumn(aColumn);
	}


	@Override
	public void setEditingRow(int aRow) {
		// TODO Auto-generated method stub
		super.setEditingRow(aRow);
	}

	@Override
	public JTableHeader getTableHeader() {
		JTableHeader tableHeader = super.getTableHeader();  
        //tableHeader.setReorderingAllowed(false);//表格列不可移动  
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();  
        hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);//列名居中
        hr.setVerticalAlignment(DefaultTableCellRenderer.CENTER);
        return tableHeader;
	}
}
