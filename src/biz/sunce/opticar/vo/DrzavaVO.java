// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DrzavaVO.java

package biz.sunce.opticar.vo;

import java.io.Serializable;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class DrzavaVO extends ValueObject
    implements Serializable
{

    public DrzavaVO()
    {
    }

    public DrzavaVO(Integer sifra, String naziv, String cc)
    {
        setSifra(sifra);
        setNaziv(naziv);
        setCc(cc);
    }

    public String getNaziv()
    {
        return naziv;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    @Override
	public void setSifra(Integer i)
    {
        sifra = i;
    }

    public void setSifra(int i)
    {
        sifra = Integer.valueOf(i);
    }

    @Override
	public String toString()
    {
        return getNaziv();
    }

    public String getCc()
    {
        return cc;
    }

    public void setCc(String string)
    {
        cc = string;
    }

    public String getCc3()
    {
        return cc3;
    }

    public void setCc3(String string)
    {
        cc3 = string;
    }

    private Integer sifra;
    private String naziv;
    private String cc;
    private String cc3;
}