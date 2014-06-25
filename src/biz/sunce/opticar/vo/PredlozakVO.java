// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PredlozakVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class PredlozakVO extends ValueObject
{

    public PredlozakVO()
    {
    }

    public String getNaziv()
    {
        return naziv;
    }

    public String getTekst()
    {
        return tekst;
    }

    public String getTipPodataka()
    {
        return tipPodataka;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    public void setTekst(String string)
    {
        tekst = string;
    }

    public void setTipPodataka(String string)
    {
        tipPodataka = string;
    }

    String naziv;
    String tekst;
    String tipPodataka;
    public static String DEFAULT_TIP_PODATAKA = "text/plain";

}