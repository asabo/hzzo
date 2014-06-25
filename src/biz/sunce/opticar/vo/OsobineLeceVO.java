// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   OsobineLeceVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class OsobineLeceVO extends ValueObject
{

    public OsobineLeceVO()
    {
    }

    public String getAx()
    {
        return ax;
    }

    public String getBaznaKrivina()
    {
        return baznaKrivina;
    }

    public String getBoja()
    {
        return boja;
    }

    public String getDijametar()
    {
        return dijametar;
    }

    public String getDl()
    {
        return dl;
    }

    public String getDsph()
    {
        return dsph;
    }

    public String getSpecIzrada()
    {
        return specIzrada;
    }

    public void setAx(String string)
    {
        ax = string;
    }

    public void setBaznaKrivina(String string)
    {
        baznaKrivina = string;
    }

    public void setBoja(String string)
    {
        boja = string;
    }

    public void setDijametar(String string)
    {
        dijametar = string;
    }

    public void setDl(String string)
    {
        dl = string;
    }

    public void setDsph(String string)
    {
        dsph = string;
    }

    public void setSpecIzrada(String string)
    {
        specIzrada = string;
    }

    public String getDcyl()
    {
        return dcyl;
    }

    public void setDcyl(String string)
    {
        dcyl = string;
    }

    String dl;
    String baznaKrivina;
    String dijametar;
    String dsph;
    String ax;
    String specIzrada;
    String boja;
    String dcyl;
}