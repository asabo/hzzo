// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   HzzoStavkaIzvjescaVO.java

package biz.sunce.opticar.vo;

import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class HzzoStavkaIzvjescaVO extends ValueObject
{

    public HzzoStavkaIzvjescaVO()
    {
    }

    public String getBrojPotvrde()
    {
        return brojPotvrde;
    }

    public Calendar getDatIzdavanja()
    {
        return datIzdavanja;
    }

    public String getIme()
    {
        return ime;
    }

    public Integer getKolicina()
    {
        return kolicina;
    }

    public String getNazivArtikla()
    {
        return nazivArtikla;
    }

    public String getPrezime()
    {
        return prezime;
    }

    public String getSifArtikla()
    {
        return sifArtikla;
    }

    public Integer getSifProizvodjaca()
    {
        return sifProizvodjaca;
    }

    public Boolean getUzetSkupljiModel()
    {
        return uzetSkupljiModel;
    }

    public void setBrojPotvrde(String string)
    {
        brojPotvrde = string;
    }

    public void setDatIzdavanja(Calendar calendar)
    {
        datIzdavanja = calendar;
    }

    public void setIme(String string)
    {
        ime = string;
    }

    public void setKolicina(Integer integer)
    {
        kolicina = integer;
    }

    public void setNazivArtikla(String string)
    {
        nazivArtikla = string;
    }

    public void setPrezime(String string)
    {
        prezime = string;
    }

    public void setSifArtikla(String string)
    {
        sifArtikla = string;
    }

    public void setSifProizvodjaca(Integer integer)
    {
        sifProizvodjaca = integer;
    }

    public void setUzetSkupljiModel(Boolean boolean1)
    {
        uzetSkupljiModel = boolean1;
    }

    public Boolean getStrankaNadoplatila()
    {
        return strankaNadoplatila;
    }

    public void setStrankaNadoplatila(Boolean boolean1)
    {
        strankaNadoplatila = boolean1;
    }

    public Integer getCijena()
    {
        return cijena;
    }

    public void setCijena(Integer integer)
    {
        cijena = integer;
    }

    public Integer getIznosSudjelovanja()
    {
        return iznosSudjelovanja;
    }

    public Integer getSifRacuna()
    {
        return sifRacuna;
    }

    public void setIznosSudjelovanja(Integer integer)
    {
        iznosSudjelovanja = integer;
    }

    public void setSifRacuna(Integer integer)
    {
        sifRacuna = integer;
    }

    public String getTvrtkaSifraNestandardnogArtikla()
    {
        return tvrtkaSifraNestandardnogArtikla;
    }

    public Calendar getDatNarudzbe()
    {
        return datNarudzbe;
    }

    public String getLijecnikIme()
    {
        return lijecnikIme;
    }

    public String getLijecnikPrezime()
    {
        return lijecnikPrezime;
    }

    public Integer getSifPreporucio()
    {
        return sifPreporucio;
    }

    public void setTvrtkaSifraNestandardnogArtikla(String string)
    {
        tvrtkaSifraNestandardnogArtikla = string;
    }

    public void setDatNarudzbe(Calendar datNarudzbe)
    {
        this.datNarudzbe = datNarudzbe;
    }

    public void setLijecnikIme(String lijecnikIme)
    {
        this.lijecnikIme = lijecnikIme;
    }

    public void setLijecnikPrezime(String lijecnikPrezime)
    {
        this.lijecnikPrezime = lijecnikPrezime;
    }

    public void setSifPreporucio(Integer sifPreporucio)
    {
        this.sifPreporucio = sifPreporucio;
    }

    Calendar datIzdavanja;
    String sifArtikla;
    Integer sifProizvodjaca;
    Integer kolicina;
    String nazivArtikla;
    String ime;
    String prezime;
    String brojPotvrde;
    Boolean uzetSkupljiModel;
    Boolean strankaNadoplatila;
    Integer cijena;
    Integer sifRacuna;
    Integer iznosSudjelovanja;
    String tvrtkaSifraNestandardnogArtikla;
    Calendar datNarudzbe;
    String lijecnikIme;
    String lijecnikPrezime;
    Integer sifPreporucio;
}