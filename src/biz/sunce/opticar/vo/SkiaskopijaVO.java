// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SkiaskopijaVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, SkiaskopijaOko

public class SkiaskopijaVO extends ValueObject
{

    public SkiaskopijaVO()
    {
    }

    public SkiaskopijaOko getDesno()
    {
        return desno;
    }

    public SkiaskopijaOko getLijevo()
    {
        return lijevo;
    }

    public void setDesno(SkiaskopijaOko oko)
    {
        desno = oko;
    }

    public void setLijevo(SkiaskopijaOko oko)
    {
        lijevo = oko;
    }

    public Integer getSifPregleda()
    {
        return sifPregleda;
    }

    public void setSifPregleda(Integer integer)
    {
        sifPregleda = integer;
    }

    public String toHtml()
    {
        String html = "<table class='ValueObject' border='2' cellspacing='0'>";
        html = html + "<thead><tr class='zaglavlje' valign='bottom' align='right'><td><b>Skiaskopija</b><td><td>Visus SC<td>Dshp<td>Dcyl<td>AX</td><td>Visus</td></tr></thead>";
        SkiaskopijaOko l = getLijevo();
        SkiaskopijaOko d = getDesno();
        html = html + "<tr align='right'><td rowspan='2' valign='middle' align='center'><i>oko</i><td><b>D</b><td>" + d.getVisus_sc() + "<td>" + d.getDsph() + "<td>" + d.getDcyl() + "<td>" + d.getAx() + "<td>" + d.getVisus() + "</tr>";
        html = html + "<tr align='right'>                             <td><b>L</b><td>" + l.getVisus_sc() + "<td>" + l.getDsph() + "<td>" + l.getDcyl() + "<td>" + l.getAx() + "<td>" + l.getVisus() + "</tr>";
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    Integer sifPregleda;
    SkiaskopijaOko lijevo;
    SkiaskopijaOko desno;
}