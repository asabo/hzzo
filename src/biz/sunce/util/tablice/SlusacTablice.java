package biz.sunce.util.tablice;

import biz.sunce.util.tablice.sort.SabotovSortModel;

/**
 * <p>Title: Optika Passage</p>
 * <p>Description: software za web trgovinu tvrtke optika-passage d.o.o.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: ANSA</p>
 * @author Ante Sabo
 * @version 1.0
 */

public class SlusacTablice extends java.awt.event.MouseAdapter implements javax.swing.event.TableModelListener
{
  private SabotovSortModel model;

  public SlusacTablice(SabotovSortModel model)
  {
    this.model=model;
  }

 public void tableChanged(javax.swing.event.TableModelEvent tableEvent)
 {
 this.model.tablicaIzmjenjena(tableEvent);
 } // tableChanged

@Override
public void mouseClicked(java.awt.event.MouseEvent me)
{
this.model.misKliknut(me);
}


} // SlusacTablice

