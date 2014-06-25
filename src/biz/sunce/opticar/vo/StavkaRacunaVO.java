// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StavkaRacunaVO.java

package biz.sunce.opticar.vo;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public final class StavkaRacunaVO extends ValueObject {

	public StavkaRacunaVO() {
	}

	public Integer getKolicina() {
		return kolicina;
	}

	public Integer getPoCijeni() {
		return poCijeni;
	}

	public String getSifArtikla() {
		return sifArtikla;
	}

	public Integer getSifRacuna() {
		return sifRacuna;
	}

	public void setKolicina(Integer integer) {
		kolicina = integer;
	}

	public void setPoCijeni(Integer integer) {
		poCijeni = integer;
	}

	public void setSifArtikla(String string) {
		sifArtikla = string;
	}

	public void setSifRacuna(Integer integer) {
		sifRacuna = integer;
	}

	public Integer getPoreznaStopa() {
		return poreznaStopa;
	}

	public void setPoreznaStopa(Integer integer) {
		poreznaStopa = integer;
	}

	public Integer getSifProizvodjaca() {
		return sifProizvodjaca;
	}

	public void setSifProizvodjaca(Integer integer) {
		sifProizvodjaca = integer;
	}

	public String getEkstraSifProizvoda() {
		return ekstraSifProizvoda;
	}

	public void setEkstraSifProizvoda(String string) {
		ekstraSifProizvoda = string;
	}

	public Short getSifraVelicineObloge() {
		return sifraVelicineObloge;
	}

	public void setSifraVelicineObloge(Short sifraVelicineObloge) {
		this.sifraVelicineObloge = sifraVelicineObloge;
	}

	Integer sifRacuna;
	String sifArtikla;
	Integer kolicina;
	Integer poCijeni;
	Integer poreznaStopa;
	Integer sifProizvodjaca;
	String ekstraSifProizvoda;
	Short sifraVelicineObloge;
}