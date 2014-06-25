// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DjelatnikVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class DjelatnikVO extends ValueObject
{

    public DjelatnikVO()
    {
    }

    public Boolean getAdministrator()
    {
        return administrator;
    }

    public String getIme()
    {
        return ime;
    }

    public String getLozinka()
    {
        return lozinka;
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

    public void setAdministrator(Boolean boolean1)
    {
        administrator = boolean1;
    }

    public void setIme(String string)
    {
        ime = string;
    }

    public void setLozinka(String string)
    {
        lozinka = string;
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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String string)
    {
        username = string;
    }

    Integer sifra;
    String ime;
    String prezime;
    String lozinka;
    Boolean administrator;
    String username;
}