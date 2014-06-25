// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LijecnikVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class LijecnikVO extends ValueObject
{

    public LijecnikVO()
    {
    }

    public String getIme()
    {
        return ime;
    }

    public String getPrezime()
    {
        return prezime;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public String getTitula()
    {
        return titula;
    }

    public void setIme(String string)
    {
        ime = string;
    }

    public void setPrezime(String string)
    {
        prezime = string;
    }

    @Override
	public void setSifra(Integer integer)
    {
        sifra = integer;
    }

    public void setTitula(String string)
    {
        titula = string;
    }

    @Override
	public String toString()
    {
        return getIme() + " " + getPrezime();
    }

    Integer sifra;
    String ime;
    String prezime;
    String titula;
}