/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * datum:2005.05.08
 * @author asabo
 *
 */

public final class SearchCriteria<O extends Object>
{
public static final String KRITERIJ_KLJUC="kljuc";
public static final String KRITERIJ_NAZIV="naziv";

List<O> podaci;
String kriterij;
 
public String getKriterij() {
	return kriterij;
}

public void dodajPodatak(O podatak)
{
	getPodaci().add(podatak);
}

public List<O> getPodaci() 
{
	if (podaci==null) podaci=new ArrayList<O>();
	
	return podaci;
}

 
public void setKriterij(String string) {
	kriterij = string;
}

 
public void setPodaci(List<O> list) {
	podaci = list;
}

}
