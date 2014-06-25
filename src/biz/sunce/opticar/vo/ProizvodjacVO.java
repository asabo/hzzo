// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProizvodjacVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class ProizvodjacVO extends ValueObject
{

    public ProizvodjacVO()
    {
    }

    public String getNaziv()
    {
        return naziv;
    }

    public boolean isProizvodiLece()
    {
        return proizvodiLece;
    }

    public boolean isProizvodiStakla()
    {
        return proizvodiStakla;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    public void setProizvodiLece(boolean b)
    {
        proizvodiLece = b;
    }

    public void setProizvodiStakla(boolean b)
    {
        proizvodiStakla = b;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
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

    public Integer getHzzoSifra()
    {
        return hzzoSifra;
    }

    public void setHzzoSifra(Integer integer)
    {
        hzzoSifra = integer;
    }

    String naziv;
    boolean proizvodiLece;
    boolean proizvodiStakla;
    Integer sifra;
    Integer hzzoSifra;
}