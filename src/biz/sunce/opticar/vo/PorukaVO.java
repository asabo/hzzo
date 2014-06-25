// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PorukaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class PorukaVO extends ValueObject
{

    public PorukaVO()
    {
    }

    public String getPoruka()
    {
        return poruka;
    }

    public long getPoslano()
    {
        return poslano;
    }

    public int getSifKlijenta()
    {
        return sifKlijenta;
    }

    public int getTipPoruke()
    {
        return tipPoruke;
    }

    public void setPoruka(String string)
    {
        poruka = string;
    }

    public void setPoslano(long datumVrijeme)
    {
        poslano = datumVrijeme;
    }

    public void setSifKlijenta(int sifra_klijenta)
    {
        sifKlijenta = sifra_klijenta;
    }

    public void setTipPoruke(int tip_poruke)
    {
        tipPoruke = tip_poruke;
    }

    public int getVrstaPoruke()
    {
        return vrstaPoruke;
    }

    public void setVrstaPoruke(int i)
    {
        vrstaPoruke = i;
    }

    public String getAdresa()
    {
        return adresa;
    }

    public void setAdresa(String string)
    {
        adresa = string;
    }

    public static final int TIP_PORUKE_SMS = 1;
    public static final int TIP_PORUKE_MAIL = 2;
    public static final int TIP_PORUKE_PISMO = 3;
    public static final int VRSTA_PORUKE_RODJENDAN = 1;
    public static final int VRSTA_PORUKE_POZIV_NA_PREGLED = 2;
    public static final String NAZIV_TIPA_PORUKE[] = {
        "", "SMS", "MAIL", "PISMO"
    };
    public static final String NAZIV_VRSTE_PORUKE[] = {
        "", "\u010Destitka", "poziv na pregled"
    };
    public static final String NAZIV_TIP_PORUKE_SMS = NAZIV_TIPA_PORUKE[1];
    public static final String NAZIV_TIP_PORUKE_MAIL = NAZIV_TIPA_PORUKE[2];
    public static final String NAZIV_TIP_PORUKE_PISMO = NAZIV_TIPA_PORUKE[3];
    private String poruka;
    private long poslano;
    private int tipPoruke;
    private int vrstaPoruke;
    private int sifKlijenta;
    private String adresa;

}