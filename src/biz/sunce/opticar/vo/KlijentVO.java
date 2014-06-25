package biz.sunce.opticar.vo;

import biz.sunce.util.Util;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, MjestoVO

@SuppressWarnings("serial")
public final class KlijentVO extends ValueObject
    implements Serializable
{

    public KlijentVO()
    {
    }

    public String getNazivMjesta()
    {
        return mjesto == null ? null : mjesto.getNaziv();
    }

    @Override
	public String toString()
    {
        return getIme() + " " + getPrezime() + ", " + getNazivMjesta();
    }

    public void setSifra(int i)
    {
        sifra = Integer.valueOf(i);
    }

    public String getAdresa()
    {
        return adresa;
    }

    public Calendar getDatRodjenja()
    {
        return datRodjenja;
    }

    public Calendar getDatUpisa()
    {
        return datUpisa;
    }

    public String getEmail()
    {
        return email;
    }

    public String getGsm()
    {
        return gsm;
    }

    public String getHzzo()
    {
        return hzzo;
    }

    public String getIme()
    {
        return ime;
    }

    public String getJmbg()
    {
        return jmbg;
    }

    public MjestoVO getMjesto()
    {
        return mjesto;
    }

    public KlijentVO getPreporucio()
    {
        return preporucio;
    }

    public String getPrezime()
    {
        return prezime;
    }

    public Integer getSifMjesta()
    {
        return mjesto == null ? null : mjesto.getSifra();
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public Calendar getSlijedeciPregled()
    {
        return slijedeciPregled;
    }

    public BufferedImage getSlika()
    {
        return slika;
    }

    public Character getSpol()
    {
        return spol;
    }

    public String getTel()
    {
        return tel;
    }

    public Boolean getUmro()
    {
        return umro;
    }

    public String getZanimanje()
    {
        return zanimanje;
    }

    public void setAdresa(String string)
    {
        adresa = string;
    }

    public void setDatRodjenja(Calendar calendar)
    {
        datRodjenja = calendar;
    }

    public void setDatUpisa(Calendar calendar)
    {
        datUpisa = calendar;
    }

    public void setEmail(String string)
    {
        email = string;
    }

    public void setGsm(String string)
    {
        gsm = string;
    }

    public void setHzzo(String string)
    {
        hzzo = string;
    }

    public void setIme(String string)
    {
        ime = string;
    }

    public void setJmbg(String string)
    {
        jmbg = string;
    }

    public void setMjesto(MjestoVO mjestoVO)
    {
        mjesto = mjestoVO;
    }

    public void setPreporucio(KlijentVO klijent)
    {
        preporucio = klijent;
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

    public void setSlijedeciPregled(Calendar calendar)
    {
        slijedeciPregled = calendar;
    }

    public void setSlika(BufferedImage image)
    {
        slika = image;
    }

    public void setSpol(Character character)
    {
        spol = character;
    }

    public void setTel(String string)
    {
        tel = string;
    }

    public void setUmro(Boolean boolean1)
    {
        umro = boolean1;
    }

    public void setZanimanje(String string)
    {
        zanimanje = string;
    }

    public boolean isPrimaInfo()
    {
        return primaInfo;
    }

    public void setPrimaInfo(boolean b)
    {
        primaInfo = b;
    }

    public String toHtml()
    {
        String html = "<table class='ValueObject' width='300'>";
        html = html + "<tr valign='top'><td align='right'>Prezime i ime:<td><b>" + getPrezime() + ", " + getIme() + "</b>";
        html = html + "<tr valign='top'><td align='right'>Adresa:<td><i>" + getAdresa() + "</i>, " + getMjesto().toString() + "";
        html = html + "<tr valign='top'><td align='right'>Ro\u0111en" + (getSpol().charValue() != 'z' ? "" : "a") + ":<td>" + Util.convertCalendarToString(getDatRodjenja());
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    public String toHtmlForEnvelope()
    {
        String html = "<table class='ValueObject' width='300'>";
        html = html + "<tr><td height='10'></tr>";
        html = html + "<tr style='font-size:13pt'><td><b>" + getIme() + " " + getPrezime() + "</b>";
        html = html + "<tr><td height='10'></tr>";
        html = html + "<tr><td><i>" + getAdresa() + "</i></tr>";
        html = html + "<tr style='font-family:Arial,SansSerif; font-size:13pt'><td><b>" + getMjesto().toString() + "</b></tr>";
        html = html + "</table>";
        return html.replaceAll("null", "");
    }

    public String getNapomena()
    {
        return napomena;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public static final char SPOL_MUSKO = 109;
    public static final char SPOL_ZENSKO = 122;
    private Integer sifra;
    private String ime;
    private String prezime;
    private String adresa;
    private Character spol;
    private Calendar datRodjenja;
    private Calendar datUpisa;
    private String tel;
    private String gsm;
    private String email;
    private String zanimanje;
    private String jmbg;
    private String hzzo;
    private Boolean umro;
    private KlijentVO preporucio;
    private boolean primaInfo;
    private Calendar slijedeciPregled;
    private transient BufferedImage slika;
    private MjestoVO mjesto;
    private String napomena;
}