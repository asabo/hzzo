// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NaocaleVO.java

package biz.sunce.opticar.vo;

import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, NaocalnaLecaVO

public class NaocaleVO extends ValueObject
{

    public NaocaleVO()
    {
    }

    public Calendar getDatum()
    {
        return datum;
    }

    public NaocalnaLecaVO getDesna()
    {
        return desna;
    }

    public NaocalnaLecaVO getLijeva()
    {
        return lijeva;
    }

    public String getModel()
    {
        return model;
    }

    public Integer getRazZjenica1()
    {
        return razZjenica1;
    }

    public Integer getRazZjenica2()
    {
        return razZjenica2;
    }

    public Integer getSifBoje()
    {
        return sifBoje;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public Integer getVod()
    {
        return vod;
    }

    public Integer getVos()
    {
        return vos;
    }

    public void setDatum(Calendar calendar)
    {
        datum = calendar;
    }

    public void setDesna(NaocalnaLecaVO leca)
    {
        desna = leca;
    }

    public void setLijeva(NaocalnaLecaVO leca)
    {
        lijeva = leca;
    }

    public void setModel(String string)
    {
        model = string;
    }

    public void setRazZjenica1(Integer integer)
    {
        razZjenica1 = integer;
    }

    public void setRazZjenica2(Integer integer)
    {
        razZjenica2 = integer;
    }

    public void setSifBoje(Integer integer)
    {
        sifBoje = integer;
    }

    @Override
	public void setSifra(Integer integer)
    {
        sifra = integer;
    }

    public void setVod(Integer integer)
    {
        vod = integer;
    }

    public void setVos(Integer integer)
    {
        vos = integer;
    }

    public String getNapomena()
    {
        return napomena;
    }

    public boolean isZaSunce()
    {
        return zaSunce;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public void setZaSunce(boolean b)
    {
        zaSunce = b;
    }

    public long getBrojKartice()
    {
        return brojKartice;
    }

    public void setBrojKartice(long l)
    {
        brojKartice = l;
    }

    public Integer getSifProizvodjacaStakla()
    {
        return sifProizvodjacaStakla;
    }

    public void setSifProizvodjacaStakla(Integer integer)
    {
        sifProizvodjacaStakla = integer;
    }

    public String toHtml()
    {
        String html = "<table class='ValueObject' width='350' border='2' cellspacing='0'>";
        html = html + "<thead><tr class='zaglavlje' align='right'><td width='32'><td width='12'><td width='26'>Dsph<td width='26'>DCyl<td width='22'>AX<td width='22'>PD<td>napomena</td><TD></TD></tr></thead>";
        NaocalnaLecaVO l = getLijeva();
        NaocalnaLecaVO d = getDesna();
        html = html + "<tr align='right'><td rowspan='2' valign='middle'><i>stakla</i><td><b>D</b><td>" + d.getDsph() + "<td>" + d.getDcyl() + "<td>" + d.getAx() + "<td>" + d.getPd() + "<td>" + d.getNapomena() + "<TD></tr>";
        html = html + "<tr align='right'>                             <td><b>L</b><td>" + l.getDsph() + "<td>" + l.getDcyl() + "<td>" + l.getAx() + "<td>" + l.getPd() + "<td>" + l.getNapomena() + "<TD></tr>";
        if(getNapomena() != null && !getNapomena().equals(""))
            html = html + "<tr><td>Napomena:<td colspan='5'><i>" + (getNapomena() == null ? "" : getNapomena()) + "</i></td></tr>";
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    public String getAdd()
    {
        return add;
    }

    public String getFi1()
    {
        return fi1;
    }

    public String getFi2()
    {
        return fi2;
    }

    public String getKvalitetaLeca()
    {
        return kvalitetaLeca;
    }

    public String getSloj()
    {
        return sloj;
    }

    public void setAdd(String string)
    {
        add = string;
    }

    public void setFi1(String string)
    {
        fi1 = string;
    }

    public void setFi2(String string)
    {
        fi2 = string;
    }

    public void setKvalitetaLeca(String string)
    {
        kvalitetaLeca = string;
    }

    public void setSloj(String string)
    {
        sloj = string;
    }

    Integer sifra;
    Calendar datum;
    Integer vod;
    Integer vos;
    Integer razZjenica1;
    Integer razZjenica2;
    String model;
    Integer sifBoje;
    NaocalnaLecaVO lijeva;
    NaocalnaLecaVO desna;
    boolean zaSunce;
    String napomena;
    long brojKartice;
    String fi1;
    String fi2;
    String add;
    String sloj;
    String kvalitetaLeca;
    Integer sifProizvodjacaStakla;
}