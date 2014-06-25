package biz.sunce.optika.net;

import java.io.Serializable;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public final class Paket implements Serializable
{
  public transient static final int TIP_PAKETA_TRANSAKCIJE=1;
  public transient static final int TIP_PAKETA_ZAHTJEVI=2;
  //10.10.05. -asabo- dodano 
  public transient static final int TIP_PAKETA_SISTEMSKE_TABLICE=3;
  
  private int tipPaketa=0;
  private byte[] podaci=null;
  private byte[] kljucEnkripcije=null; // tu ce biti smjesten simetricni key kojim je vrsena enkripcija podataka, ZAKLJUCAN javnim kljucem, ako je null-paket nije zakriptiran
  
  // 12.01.06. -asabo- dodano
  private int sifTvrtke;
  private int sifPoslovnice;
  private int djelatnikPoslao; // sifra djelatnika koji je poslao, - NE sifra u passage sustavu 
  private long trenutakSlanja; // tocna milisekunda kada je paket krenio na put prema serveru
  
  private String characterEncoding; //27.02.06. -asabo- char. encoding u kojem se nalaze podaci... 
  private String javaVersion; // 12.03.06. -asabo- verzija jave bi nam trebala isto.. 
  private String os;  // operacijski sustav takodjer
  private String userLanguage; //korisnicko ime i jezik na OS-u koje je poslalo podatke
  
  public Paket(int tipPaketa)
  {
    this.setTipPaketa(tipPaketa);
  }

  public int getTipPaketa() {
    return tipPaketa;
  }

  public byte[] getPodaci() {
    return podaci;
  }

  public void setTipPaketa(int tipPaketa) {
    this.tipPaketa = tipPaketa;
  }

  public void setPodaci(byte[] podaci) {
    this.podaci = podaci;
  }
	public int getDjelatnikPoslao() {
		return djelatnikPoslao;
	}

	public int getSifPoslovnice() {
		return sifPoslovnice;
	}

	public int getSifTvrtke() {
		return sifTvrtke;
	}

	public long getTrenutakSlanja() {
		return trenutakSlanja;
	}

	public void setDjelatnikPoslao(int i) {
		djelatnikPoslao = i;
	}

	public void setSifPoslovnice(int i) {
		sifPoslovnice = i;
	}

	public void setSifTvrtke(int i) {
		sifTvrtke = i;
	}

	public void setTrenutakSlanja(long l) {
		trenutakSlanja = l;
	}

	public byte[] getKljucEnkripcije() {
		return kljucEnkripcije;
	}

	public void setKljucEnkripcije(byte[] bs) {
		kljucEnkripcije = bs;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String string) {
		characterEncoding = string;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String string) {
		javaVersion = string;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String string) {
		os = string;
	}



	public String getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(String string) {
		userLanguage = string;
	}

}
