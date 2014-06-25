// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PostavkaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class PostavkaVO extends ValueObject
{

    public PostavkaVO()
    {
    }

    public String getNaziv()
    {
        return naziv;
    }

    public String getVrijednost()
    {
        return vrijednost;
    }

    public void setNaziv(String string)
    {
        naziv = string;
    }

    public void setVrijednost(String string)
    {
        vrijednost = string;
    }

    private String naziv;
    private String vrijednost;
}