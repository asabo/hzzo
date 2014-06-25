// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LeceVO.java

package biz.sunce.opticar.vo;

import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, OsobineLeceVO, TipLeceVO

public class LeceVO extends ValueObject
{

    public LeceVO()
    {
    }

    public Calendar getDatum()
    {
        return datum;
    }

    public OsobineLeceVO getDesna()
    {
        if(desna == null)
            desna = new OsobineLeceVO();
        return desna;
    }

    public OsobineLeceVO getLijeva()
    {
        if(lijeva == null)
            lijeva = new OsobineLeceVO();
        return lijeva;
    }

    public TipLeceVO getPodvrsta()
    {
        return podvrsta;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public TipLeceVO getVrsta()
    {
        return vrsta;
    }

    public void setDatum(Calendar calendar)
    {
        datum = calendar;
    }

    public void setDesna(OsobineLeceVO leceVO)
    {
        desna = leceVO;
    }

    public void setLijeva(OsobineLeceVO leceVO)
    {
        lijeva = leceVO;
    }

    public void setPodvrsta(TipLeceVO leceVO)
    {
        sifPodvrste = leceVO.getSifra();
    }

    @Override
	public void setSifra(Integer integer)
    {
        sifra = integer;
    }

    public void setVrsta(TipLeceVO leceVO)
    {
        sifVrste = leceVO.getSifra();
    }

    public String getNapomena()
    {
        return napomena;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String string)
    {
        model = string;
    }

    public Integer getSifPodvrste()
    {
        return sifPodvrste;
    }

    public Integer getSifVrste()
    {
        return sifVrste;
    }

    public void setSifPodvrste(Integer integer)
    {
        sifPodvrste = integer;
    }

    public void setSifVrste(Integer integer)
    {
        sifVrste = integer;
    }

    public String toHtml()
    {
        String html = "<table class='ValueObject' width='100%' cellspacing='5'>";
        if(getVrsta() != null)
        {
            String vrsta = getVrsta() == null ? "" : getVrsta().getNaziv();
            String podvrsta = getPodvrsta() == null ? "" : getPodvrsta().getNaziv();
            html = html + "<tr><td>Vrsta le\u0107e:<td>" + vrsta + "<td>Podvrsta le\u0107e:<td>" + podvrsta;
        }
        html = html + "<tr><td><td><td>Bazna krivina<td>Dijametar<td>Dsph<td>DCyl<td>AX<td>Posebna izrada<td>Boja";
        OsobineLeceVO l = getLijeva();
        OsobineLeceVO d = getDesna();
        html = html + "<tr><td rowspan='2' valign='middle'>le\u0107e<td><b>D</b><td>" + d.getBaznaKrivina() + "<td>" + d.getDijametar() + "<td>" + d.getDsph() + "<td>" + d.getDcyl() + "<td>" + d.getAx() + "<td>" + d.getSpecIzrada() + "<td>" + d.getBoja();
        html = html + "<tr>                                    <td><b>L</b><td>" + l.getBaznaKrivina() + "<td>" + l.getDijametar() + "<td>" + l.getDsph() + "<td>" + l.getDcyl() + "<td>" + l.getAx() + "<td>" + l.getSpecIzrada() + "<td>" + l.getBoja();
        html = html + "<tr><td>Model: <td colspan='3'>" + getModel() + "<td colspan='3'>" + getNapomena();
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    public Integer getSifProizvodjaca()
    {
        return sifProizvodjaca;
    }

    public void setSifProizvodjaca(Integer integer)
    {
        sifProizvodjaca = integer;
    }

    Integer sifra;
    Calendar datum;
    TipLeceVO vrsta;
    TipLeceVO podvrsta;
    Integer sifVrste;
    Integer sifPodvrste;
    OsobineLeceVO lijeva;
    OsobineLeceVO desna;
    String napomena;
    String model;
    Integer sifProizvodjaca;
}