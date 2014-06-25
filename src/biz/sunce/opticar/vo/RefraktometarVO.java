// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RefraktometarVO.java

package biz.sunce.opticar.vo;


// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, RefraktometarOko

public class RefraktometarVO extends ValueObject
{

    public RefraktometarVO()
    {
    }

    public RefraktometarOko getDesno()
    {
        return desno;
    }

    public RefraktometarOko getLijevo()
    {
        return lijevo;
    }

    public void setDesno(RefraktometarOko oko)
    {
        desno = oko;
    }

    public void setLijevo(RefraktometarOko oko)
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
        String html = "<table class='ValueObject'  cellspacing='5'>";
        html = html + "<thead><tr class='zaglavlje' align='right'><td><b>Refraktometar</b><td width='10'><td>Dsph<td>DCyl<td>AX</td></tr></thead>";
        RefraktometarOko l = getLijevo();
        RefraktometarOko d = getDesno();
        html = html + "<tr valign='middle' align='right'><td rowspan='2' align='center'><i>oko</i><td><b>D </b><td>" + d.getDsph() + "<td>" + d.getDcyl() + "<td>" + d.getAx() + "</tr>";
        html = html + "<tr valign='middle' align='right'>                             <td><b>L </b><td>" + l.getDsph() + "<td>" + l.getDcyl() + "<td>" + l.getAx() + "</tr>";
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    Integer sifPregleda;
    RefraktometarOko lijevo;
    RefraktometarOko desno;
}