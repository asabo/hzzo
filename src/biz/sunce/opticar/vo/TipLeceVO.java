// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TipLeceVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class TipLeceVO extends ValueObject
    implements Cloneable
{

    public TipLeceVO()
    {
    }

    @Override
	public Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }

    public String getNapomena()
    {
        return napomena;
    }

    public String getNaziv()
    {
        return naziv;
    }

    public TipLeceVO getPodvrstaOd()
    {
        return podvrstaOd;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    public void setPodvrstaOd(TipLeceVO lece)
    {
        podvrstaOd = lece;
    }

    @Override
	public void setSifra(Integer integer)
    {
        sifra = integer;
    }

    @Override
	public String toString()
    {
        return getNaziv();
    }

    Integer sifra;
    String naziv;
    String napomena;
    TipLeceVO podvrstaOd;
}