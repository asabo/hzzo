/*
 * Project opticari
 *
 */
package biz.sunce.optika.net;

import java.io.Serializable;

/**
 * datum:2006.01.14
 * @author asabo
 *
 */
public final class Kolona implements Serializable
{
String naziv;
int tip;
String nazivTipa;
int redniBrojUKljucu; // 0 - nije u kljucu >0 - redni broj u kljucu

public String getNaziv() {
	return naziv;
}

public String getNazivTipa() {
	return nazivTipa;
}

public int getRedniBrojUKljucu() {
	return redniBrojUKljucu;
}

public int getTip() {
	return tip;
}

public void setNaziv(String string) {
	naziv = string;
}

public void setNazivTipa(String string) {
	nazivTipa = string;
}

public void setRedniBrojUKljucu(int i) {
	redniBrojUKljucu = i;
}

public void setTip(int i) {
	tip = i;
}

@Override
public String toString() {
String s = naziv+":"+nazivTipa+":"+redniBrojUKljucu;
return s;
}
}
