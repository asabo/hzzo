package biz.sunce.opticar.vo;

import java.io.Serializable;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public final class PomagaloVO extends ValueObject implements Serializable {

	private static final long serialVersionUID = -687196382091754837L;

	public PomagaloVO() {
	}

	public Integer getCijenaSPDVom() {
		return cijenaSPDVom;
	}

	public String getNaziv() {
		return naziv;
	}

	public Integer getPoreznaSkupina() {
		return poreznaSkupina;
	}

	public void setCijenaSPDVom(Integer integer) {
		cijenaSPDVom = integer;
	}

	public void setNaziv(String string) {
		naziv = string;
	}

	public void setPoreznaSkupina(Integer integer) {
		poreznaSkupina = integer;
	}

	public String getSifraArtikla() {
		return sifraArtikla;
	}

	public void setSifraArtikla(String sifArtikla) {
		sifraArtikla = sifArtikla;
	}

	@Override
	public String toString() {
		return getSifraArtikla() + " " + getNaziv();
	}

	public Boolean getOptickoPomagalo() {
		return optickoPomagalo;
	}

	public void setOptickoPomagalo(Boolean boolean1) {
		optickoPomagalo = boolean1;
	}

	String sifraArtikla;
	String naziv;
	Integer poreznaSkupina;
	Integer cijenaSPDVom;
	Boolean optickoPomagalo;
}