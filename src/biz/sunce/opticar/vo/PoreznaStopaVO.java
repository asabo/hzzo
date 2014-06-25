// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PoreznaStopaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class PoreznaStopaVO extends ValueObject
{

    public PoreznaStopaVO()
    {
    }

    public String getNaziv()
    {
        return naziv;
    }

    public Integer getStopa()
    {
        return stopa;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    public void setStopa(Integer integer)
    {
        stopa = integer;
    }

    @Override
	public String toString()
    {
        return getStopa().intValue() + "% - " + getNaziv();
    }

    String naziv;
    Integer stopa;
}