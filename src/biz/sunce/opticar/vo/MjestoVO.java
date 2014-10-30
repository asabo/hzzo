// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MjestoVO.java

package biz.sunce.opticar.vo;

import java.io.Serializable;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, DrzavaVO

public class MjestoVO extends ValueObject
    implements Serializable
{
	private static final long serialVersionUID = 8329609775904790223L;
	
	public MjestoVO()
    {
    }

    @Override
	public String toString()
    {
        String cc = drzava == null ? null : drzava.getCc();
        Integer zip = getZip();
        return getNaziv() + " " + (cc == null ? "" : cc + "-") + (zip == null ? "" : "" + zip.intValue());
    }

    public String getNaziv()
    {
        return naziv;
    }

    public Integer getSifDrzave()
    {
        return sifDrzave;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public void setNaziv(String naziv)
    {
        this.naziv = naziv;
    }

    public void setSifDrzave(Integer sifDrzave)
    {
        this.sifDrzave = sifDrzave;
    }

    @Override
	public void setSifra(Integer sifra)
    {
        this.sifra = sifra;
    }

    public DrzavaVO getDrzava()
    {
        return drzava;
    }

    public void setDrzava(DrzavaVO drzavaVO)
    {
        drzava = drzavaVO;
    }

    public Integer getZip()
    {
        return zip;
    }

    public void setZip(Integer integer)
    {
        zip = integer;
    }

    public Integer getSifPodruzniceHzzo()
    {
        return sifPodruzniceHzzo;
    }

    public void setSifPodruzniceHzzo(Integer integer)
    {
        sifPodruzniceHzzo = integer;
    }

    Integer sifra;
    String naziv;
    Integer sifDrzave;
    DrzavaVO drzava;
    Integer zip;
    Integer sifPodruzniceHzzo;
    public static final String KRITERIJ_PRETRAZIVANJA_PODRUZNICE = "kriterij_podruznica";
}