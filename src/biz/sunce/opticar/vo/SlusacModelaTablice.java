package biz.sunce.opticar.vo;

import java.awt.event.MouseEvent;
import javax.swing.event.TableModelEvent;

public interface SlusacModelaTablice
{

    public abstract void redakOznacen(int i, MouseEvent mouseevent, TableModel tablemodel);

    public abstract void redakIzmjenjen(int i, TableModelEvent tablemodelevent, TableModel tablemodel);
}