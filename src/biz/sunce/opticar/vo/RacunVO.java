 
package biz.sunce.opticar.vo;

import java.util.*;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, MjestoVO

public final class RacunVO extends ValueObject
{

    public RacunVO()
    {
    }

    public Calendar getDatumIzdavanja()
    {
        return datum;
    }

    public Calendar getDatumNarudzbe()
    {
        return datumNarudzbe;
    }

    public Boolean getDopunskoOsiguranje()
    {
        return dopunskoOsiguranje;
    }

    public Integer getIznosOsnovnogOsiguranja()
    {
        return iznosOsnovnogOsiguranja;
    }

    public Integer getIznosPDV()
    {
        return iznosPDV;
    }

    public Integer getIznosSudjelovanja()
    {
        return iznosSudjelovanja;
    }

    public Boolean getOsnovnoOsiguranje()
    {
        return osnovnoOsiguranje;
    }

    public String getPozivNaBroj1()
    {
        return pozivNaBroj1;
    }

    public String getPozivNaBroj2()
    {
        return pozivNaBroj2;
    }

    public Integer getSifKlijenta()
    {
        return sifKlijenta;
    }

    public void setDatumIzdavanja(Calendar calendar)
    {
        datum = calendar;
    }

    public void setDatumNarudzbe(Calendar calendar)
    {
        datumNarudzbe = calendar;
    }

    public void setDopunskoOsiguranje(Boolean boolean1)
    {
        dopunskoOsiguranje = boolean1;
    }

    public void setIznosOsnovnogOsiguranja(Integer integer)
    {
        iznosOsnovnogOsiguranja = integer;
    }

    public void setIznosPDV(Integer integer)
    {
        iznosPDV = integer;
    }

    public void setIznosSudjelovanja(Integer integer)
    {
        iznosSudjelovanja = integer;
    }

    public void setOsnovnoOsiguranje(Boolean boolean1)
    {
        osnovnoOsiguranje = boolean1;
    }

    public void setPozivNaBroj1(String string)
    {
        pozivNaBroj1 = string;
    }

    public void setPozivNaBroj2(String string)
    {
        pozivNaBroj2 = string;
    }

    public void setSifKlijenta(Integer integer)
    {
        sifKlijenta = integer;
    }

    public ArrayList<StavkaRacunaVO> getStavkeRacuna()
    {
        return stavkeRacuna;
    }

    public void setStavkeRacuna(ArrayList<StavkaRacunaVO> list)
    {
        stavkeRacuna = list;
    }

    public String getBrojPoliceDopunsko()
    {
        return brojPoliceDopunsko;
    }

    public String getBrojPotvrde1()
    {
        return brojPotvrde1;
    }

    public String getBrojPotvrde2()
    {
        return brojPotvrde2;
    }

    public String getSifProizvodjaca()
    {
        return sifProizvodjaca;
    }

    public void setBrojPoliceDopunsko(String string)
    {
        brojPoliceDopunsko = string;
    }

    public void setBrojPotvrde1(String string)
    {
        brojPotvrde1 = string;
    }

    public void setBrojPotvrde2(String string)
    {
        brojPotvrde2 = string;
    }

    public void setSifProizvodjaca(String string)
    {
        sifProizvodjaca = string;
    }

    public MjestoVO getPodrucniUred()
    {
        return podrucniUred;
    }

    public void setPodrucniUred(MjestoVO mjestoVO)
    {
        podrucniUred = mjestoVO;
    }

    public Integer getSifPodrucnogUreda()
    {
        return sifPodrucnogUreda;
    }

    public void setSifPodrucnogUreda(Integer integer)
    {
        sifPodrucnogUreda = integer;
    }

    public String getNapomena()
    {
        return napomena;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public String getBrojInoBolesnickogLista()
    {
        return brojInoBolesnickogLista;
    }

    public String getBrojIskaznice1()
    {
        return brojIskaznice1;
    }

    public String getBrojIskaznice2()
    {
        return brojIskaznice2;
    }

    public Integer getSifDrzave()
    {
        return sifDrzave;
    }

    public void setBrojInoBolesnickogLista(String string)
    {
        brojInoBolesnickogLista = string;
    }
 
    public void setBrojIskaznice1(String string)
    {
        brojIskaznice1 = string;
    }

    public void setBrojIskaznice2(String string)
    {
        brojIskaznice2 = string;
    }

    public void setSifDrzave(Integer integer)
    {
        sifDrzave = integer;
    }

    public Integer getSifLijecnika()
    {
        return sifLijecnika;
    }

    public void setSifLijecnika(Integer integer)
    {
        sifLijecnika = integer;
    }

    public String getBrojOsobnogRacunaDopunsko()
    {
        return brojOsobnogRacunaDopunsko;
    }

    public String getBrojOsobnogRacunaOsnovno()
    {
        return brojOsobnogRacunaOsnovno;
    }

    public void setBrojOsobnogRacunaDopunsko(String dopunsko)
    {
        brojOsobnogRacunaDopunsko = dopunsko;
    }

    public void setBrojOsobnogRacunaOsnovno(String osnovno)
    {
        brojOsobnogRacunaOsnovno = osnovno;
    }

    public Boolean getKupljenSkupljiArtikl()
    {
        return kupljenSkupljiArtikl;
    }

    public void setKupljenSkupljiArtikl(Boolean boolean1)
    {
        kupljenSkupljiArtikl = boolean1;
    }

    public Integer getVrstaPomagala()
    {
        return vrstaPomagala;
    }

    public void setVrstaPomagala(Integer integer)
    {
        vrstaPomagala = integer;
    }

    public Boolean getRobaIsporucena()
    {
        return robaIsporucena;
    }

    public Integer getPreporucio()
    {
        return preporucio;
    }

    public Date getDatumSlijedecegPrava()
    {
        return datumSlijedecegPrava;
    }

    public String getAktivnostZZR()
    {
        return aktivnostZZR;
    }

    public String getAktivnostDop()
    {
        return aktivnostDop;
    }

    public void setRobaIsporucena(Boolean boolean1)
    {
        robaIsporucena = boolean1;
    }

    public void setPreporucio(Integer preporucio)
    {
        this.preporucio = preporucio;
    }

    public void setDatumSlijedecegPrava(Date datumSlijedecegPrava)
    {
        this.datumSlijedecegPrava = datumSlijedecegPrava;
    }

    public void setAktivnostZZR(String aktivnostZZR)
    {
        this.aktivnostZZR = aktivnostZZR;
    }

    public void setAktivnostDop(String aktivnostDop)
    {
        this.aktivnostDop = aktivnostDop;
    }

    Boolean dopunskoOsiguranje;
    Boolean osnovnoOsiguranje;
    Integer iznosSudjelovanja;
    Integer iznosOsnovnogOsiguranja;
    Integer iznosPDV;
    Integer sifKlijenta;
    Calendar datumNarudzbe;
    Calendar datum;
    Date datumSlijedecegPrava;
    String pozivNaBroj1;
    String pozivNaBroj2;
    ArrayList<StavkaRacunaVO> stavkeRacuna;
    String sifProizvodjaca;
    String brojPotvrde1;
    String brojPotvrde2;
    String brojPoliceDopunsko;
    MjestoVO podrucniUred;
    Integer sifPodrucnogUreda;
    String brojIskaznice1;
    String brojIskaznice2;
    String brojInoBolesnickogLista;
    Integer sifDrzave;
    Integer sifLijecnika;
    String napomena;
    String brojOsobnogRacunaOsnovno;
    String brojOsobnogRacunaDopunsko;
    Boolean kupljenSkupljiArtikl;
    Integer vrstaPomagala;
    Boolean robaIsporucena;
    Integer preporucio;
    String aktivnostZZR;
    String aktivnostDop;
    String brojPotvrdePomagala=null;
    
    
	public String getBrojPotvrdePomagala() {
		return brojPotvrdePomagala;
	}

	public void setBrojPotvrdePomagala(String brojPotvrdePomagala) {
		this.brojPotvrdePomagala = brojPotvrdePomagala;
	}
    
}