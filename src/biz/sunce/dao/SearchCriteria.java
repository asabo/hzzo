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

public final class SearchCriteria 
{
public static final String KRITERIJ_KLJUC="kljuc";
public static final String KRITERIJ_NAZIV="naziv";

List<Object> podaci;
String kriterij;
 
public String getKriterij() {
	return kriterij;
}

public void dodajPodatak(Object podatak)
{
	getPodaci().add(podatak);
}

public List<Object> getPodaci() 
{
	if (podaci==null) podaci=new ArrayList<Object>();
	
	return podaci;
}

 
public void setKriterij(String string) {
	kriterij = string;
}

 
public void setPodaci(List<Object> list) {
	podaci = list;
}

}
