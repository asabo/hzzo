// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:53
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VrstaPomagalaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class VrstaPomagalaVO extends ValueObject
{

    public VrstaPomagalaVO()
    {
    }

    public String getNaziv()
    {
        return naziv;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    @Override
	public String toString()
    {
        return naziv;
    }

    private String naziv;
}