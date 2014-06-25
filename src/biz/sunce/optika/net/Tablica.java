package biz.sunce.optika.net;


import java.io.*;
import java.util.*;

/**
 * @author Ante Sabo
 * @version 1.0
 * predstavlja tablicu pri sinkronizaciji podataka
 */

public final class Tablica implements Serializable
{
private String naziv;
 // s obzirom da nema mogucnosti da se na dva odvojena sustava pise po istim podacima, nema ni
 // potrebe pri replikaciji obracati pozornost na prednost pri zapisivanju podataka
 //int prednost=Table.PREDNOST_POTPUNA;

private Collection podaci;

 public Tablica(String naziv)
 {
  this.naziv=naziv;

 }//konstruktor

 public Tablica()
 {
  this.naziv="(nema)";
 }

  public String getNaziv() {
    return naziv;
  }

  public Collection getPodaci() {
    return podaci;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

  public void setPodaci(Collection podaci) {
    this.podaci = podaci;
  }

  public void destroy()
{
 this.podaci.clear();
 this.naziv=null;
 this.podaci=null;
}
  
  @Override
	public String toString() {
		return this.naziv+":"+(this.podaci==null?-1:this.podaci.size());
	}

}//Tablica klasa
