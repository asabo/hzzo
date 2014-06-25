// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:53
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UvaljivoUTablicu.java

package biz.sunce.opticar.vo;


public interface UvaljivoUTablicu
{

    public abstract String getColumnName(int i);

    public abstract int getColumnCount();

    public abstract Class getColumnClass(int i);
}