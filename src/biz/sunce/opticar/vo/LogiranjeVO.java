// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LogiranjeVO.java

package biz.sunce.opticar.vo;

import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public final class LogiranjeVO extends ValueObject
{

    public LogiranjeVO()
    {
    }

    public Calendar getLogin()
    {
        return login;
    }

    public Calendar getLogout()
    {
        return logout;
    }

    public Integer getSifDjelatnika()
    {
        return sifDjelatnika;
    }

    public void setLogin(Calendar calendar)
    {
        login = calendar;
    }

    public void setLogout(Calendar calendar)
    {
        logout = calendar;
    }

    public void setSifDjelatnika(Integer integer)
    {
        sifDjelatnika = integer;
    }

    private Integer sifDjelatnika;
    private Calendar login;
    private Calendar logout;
}