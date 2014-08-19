package biz.sunce.opticar.vo;

import java.awt.event.MouseEvent;
import javax.swing.event.TableModelEvent;

public interface SlusacModelaTablice<VO extends ValueObject>
{
    public abstract void redakOznacen(int i, MouseEvent mouseevent, TableModel<VO> tablemodel);
    public abstract void redakIzmjenjen(int i, TableModelEvent tablemodelevent, TableModel<VO> tablemodel);
}