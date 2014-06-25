// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NaocalnaLecaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class NaocalnaLecaVO extends ValueObject
{

    public NaocalnaLecaVO()
    {
    }

    public String getAx()
    {
        return ax;
    }

    public String getDb()
    {
        return db;
    }

    public String getDcyl()
    {
        return dcyl;
    }

    public String getDl()
    {
        return dl;
    }

    public String getDsph()
    {
        return dsph;
    }

    public String getNapomena()
    {
        return napomena;
    }

    public void setAx(String string)
    {
        ax = string;
    }

    public void setDb(String string)
    {
        db = string;
    }

    public void setDcyl(String string)
    {
        dcyl = string;
    }

    public void setDl(String string)
    {
        dl = string;
    }

    public void setDsph(String string)
    {
        dsph = string;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public String getPd()
    {
        return pd;
    }

    public void setPd(String string)
    {
        pd = string;
    }

    public String getAdicija()
    {
        return adicija;
    }

    public String getBpris()
    {
        return bpris;
    }

    public String getPris()
    {
        return pris;
    }

    public void setAdicija(String string)
    {
        adicija = string;
    }

    public void setBpris(String string)
    {
        bpris = string;
    }

    public void setPris(String string)
    {
        pris = string;
    }

    String dl;
    String db;
    String dsph;
    String dcyl;
    String ax;
    String napomena;
    String pd;
    String pris;
    String bpris;
    String adicija;
}