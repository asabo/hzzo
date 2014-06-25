// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   KeratometrijaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, KeratometrijaOko

public class KeratometrijaVO extends ValueObject
{

    public KeratometrijaVO()
    {
    }

    public Integer getSifPregleda()
    {
        return sifPregleda;
    }

    public void setSifPregleda(Integer integer)
    {
        sifPregleda = integer;
    }

    public KeratometrijaOko getDesno()
    {
        return desno;
    }

    public KeratometrijaOko getLijevo()
    {
        return lijevo;
    }

    public void setDesno(KeratometrijaOko oko)
    {
        desno = oko;
    }

    public void setLijevo(KeratometrijaOko oko)
    {
        lijevo = oko;
    }

    public String toHtml()
    {
        String html = "<table class='ValueObject' cellspacing='0' cellpadding='0'>";
        html = html + "<thead><tr class='zaglavlje' align='right'><td><b>Keratometrija</b><td width='15'><td width='50'>Baza-1<td width='50'>Baza-2<td width='50'>AX<td width='40'>Visus<td></tr></thead>";
        KeratometrijaOko l = getLijevo();
        KeratometrijaOko d = getDesno();
        html = html + "<tr valign='middle' align='right'><td rowspan='2' align='center'><i>oko</i><td><b>D</b><td width='50'>" + d.getBaza1() + "<td width='50'>" + d.getBaza2() + "<td width='40'>" + d.getAx() + "<td width='50'>" + d.getVisus() + "</tr>";
        html = html + "<tr valign='middle' align='right'>                                    <td><b>L</b><td width='50'>" + l.getBaza1() + "<td width='50'>" + l.getBaza2() + "<td width='40'>" + l.getAx() + "<td width='50'>" + l.getVisus() + "</tr>";
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    Integer sifPregleda;
    KeratometrijaOko lijevo;
    KeratometrijaOko desno;
}