/*
 * Project opticari
 *
 */
package biz.sunce.optika;

/**
 * datum:2006.01.29
 * @author asabo
 * svaki vizualni objekt na kojem bi se trebalo prikazivati trenutno stanje 
 * sinkronizacije mora implementirati ovaj interface jer ce SynchEngine po prijavi
 * objekta za pracenje stanja prijenosa pozivati doticne metode... 
 */
public interface PrikazivacSinkronizacijskihPoruka   
{
public void postaviPoruku(String poruka);
public void postaviMax(int max);
public void postaviTrenutnuVrijednost(int vrijednost);
}
