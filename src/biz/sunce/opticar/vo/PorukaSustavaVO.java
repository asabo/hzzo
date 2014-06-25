// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PorukaSustavaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class PorukaSustavaVO extends ValueObject
{

    public PorukaSustavaVO()
    {
    }

    public String getIznimka()
    {
        return iznimka;
    }

    public String getPoruka()
    {
        return poruka;
    }

    public int getRazina()
    {
        return razina;
    }

    public void setIznimka(String string)
    {
        iznimka = string;
    }

    public void setPoruka(String string)
    {
        poruka = string;
    }

    public void setRazina(int i)
    {
        razina = i;
    }

    private int razina;
    String iznimka;
    String poruka;
}