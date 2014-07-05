/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.ValueObject;

/**
 * datum:2005.11.29
 * 
 * @author asabo
 * 
 */
public interface GUIEditor<VO extends ValueObject> {
	public void napuniPodatke(VO ulaz);

	public VO vratiPodatke();

	public void pobrisiFormu();

	public boolean isFormaIspravna(); // jesu li podaci na formi ispravno
										// uneseni

	public void omoguci();

	public void onemoguci();

	public boolean jeliIzmjenjen(); // jeli korisnik nesto na formi promjenio..

	public interface SlusacSpremnostiPodataka {
		public void podaciSpremni(GUIEditor pozivatelj); // kada su podaci na
															// formi
															// kompletirani
															// forma na ovaj
															// nacin obavjestava
															// svoje slusace da
															// su podaci spremni
	}

	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac);
}// GUIEditor
