package biz.sunce.dao;

import biz.sunce.optika.net.Tablica;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author asabo
 * @date 30.08.2005.
 * @version 1.0
 */
public interface SynchModul
{
  //ispitat ce kada je bila posljednja uspjesna sinkronizacija podataka, te prikupiti sve nove podatke
  //za zadanu tablicu i spremiti ju u posebnu listu
  public java.util.List collectNewData(String table);
  // zapisat ce u bazu podataka datum-vrijeme cinjenicu o uspjesno obavljenoj sinkronizaciji
  public boolean registerSuccesfulSynchronization(java.util.Calendar date);

	//10.10.05. -asabo- neke tablice ce biti 'sistemske' i njih ce moci mjenjati samo 
	// nadzorni sustav. To su readonly tablice potrebne za normalno funkcioniranje 
	// kao npr. mjesta, proizvodjaci, tipovi_leca, boje, naocale, slikeBoja, slikeNaocala.. 
	public boolean updateSystemTable(Tablica tablica);
	public void log(String poruka);
  
}//SynchModul
