package biz.sunce.util.tablice.sort;

import javax.swing.table.*;

public interface SortTableModel
  extends TableModel
{
  public boolean isSortable(int col);
  public void sortColumn(int col, boolean ascending);
}

