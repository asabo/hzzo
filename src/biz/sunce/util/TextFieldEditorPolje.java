/*
 * Project opticari
 *
 */
package biz.sunce.util;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * datum:2005.07.15
 * @author asabo
 *
 */
public class TextFieldEditorPolje extends JTextField implements TableCellEditor
{
PretrazivanjeProzor nosac;
JTable tablica;

public TextFieldEditorPolje(PretrazivanjeProzor nosac)
{
super("");
this.nosac=nosac;
}

public Object getCellEditorValue() 
{
	return "bkjsdkjfb2";
}

public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) 
	{
		this.setText("");
		this.tablica=arg0;
		 		
		return this;		
	}//getTableCellEditorComponent

public void cancelCellEditing() {
}

public boolean stopCellEditing() {
	
	return true;
}

public boolean isCellEditable(EventObject arg0) {
	return true;
}

public boolean shouldSelectCell(EventObject arg0) {
	return true;
}

public void addCellEditorListener(CellEditorListener arg0) {
	
}

public void removeCellEditorListener(CellEditorListener arg0) {
}


}
