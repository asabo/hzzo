package biz.sunce.opticar.vo;

import java.awt.Image;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public final class ArtiklVO extends ValueObject
{

    public ArtiklVO()
    {
    }

    private Image getSlikaNaocale()
    {
        return null;
    }

    private Image setSlikaBoje()
    {
        return null;
    }

    public String getKatOznaka()
    {
        return katOznaka;
    }

    public String getOznakaBoje()
    {
        return oznakaBoje;
    }

    public Integer getSifNaocale()
    {
        return sifNaocale;
    }

    @Override
	public Integer getSifra()
    {
        return sifra;
    }

    public Image getSlikaBoje()
    {
        return slikaBoje;
    }

    public void setKatOznaka(String string)
    {
        katOznaka = string;
    }

    public void setOznakaBoje(String string)
    {
        oznakaBoje = string;
    }

    public void setSifNaocale(Integer integer)
    {
        sifNaocale = integer;
    }

    @Override
	public void setSifra(Integer integer)
    {
        sifra = integer;
    }

    public String getNazivMarke()
    {
        return nazivMarke;
    }

    public Integer getSifMarke()
    {
        return sifMarke;
    }

    public void setNazivMarke(String string)
    {
        nazivMarke = string;
    }

    public void setSifMarke(Integer integer)
    {
        sifMarke = integer;
    }

    public Integer getPrvaGodProizvodnje()
    {
        return prvaGodProizvodnje;
    }

    public Integer getZadnjaGodProizvodnje()
    {
        return zadnjaGodProizvodnje;
    }

    public void setPrvaGodProizvodnje(Integer integer)
    {
        prvaGodProizvodnje = integer;
    }

    public void setZadnjaGodProizvodnje(Integer integer)
    {
        zadnjaGodProizvodnje = integer;
    }

    @Override
	public String toString()
    {
        return getKatOznaka() + "." + getOznakaBoje();
    }

    private String katOznaka;
    private String oznakaBoje;
    private Integer sifra;
    private Integer sifNaocale;
    private Integer sifMarke;
    private String nazivMarke;
    private Image slikaNaocale;
    private Image slikaBoje;
    private Integer prvaGodProizvodnje;
    private Integer zadnjaGodProizvodnje;
}