package biz.sunce.util.tablice.sort;

/**
 * svaki BEAN koji bi se htio predstaviti u sortabilnoj tablici mora implementirati ovaj interface
 * na taj nacin se implementiraju pozivi pozivajuci koje se dobivaju potrebni podaci u zeljenim kolonama
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: ANSA</p>
 * @author Ante Sabo
 * @version 1.0
 */

public abstract class UvaljivoUTablicu
{
// tu ce lezati popis kolona i svaka klasa ce morati implementirati staticnu metodu koja ce puniti ovo polje
protected static String[] kolone;


public static String[] getKolone()
 {
  return kolone;
 }

public static void setKolone(String[] kolon)
 {
  kolone=kolon;
 }

 /**
  * svaka klasa sa ovim ponasanjem mora implementirati metodu getValueAt(int)
  * u kojoj vraca nazad zeljeni objekt ovisno o kojoj se koloni radi
  * @param kolona
  * @return
  */
public abstract Object getValueAt(int kolona);

 /**
  * ovu metodu bi trebalo nadjacati i u njoj definirati koje je klase koja kolona
  * ako se to ne ucini sve ce kolone biti tipa String, jer je to univerzalni tip
  * koji ce u sebe primiti bilo koju klasu podataka i prikazati je kakvom-takvom
  * (Svaki objekt ima naslijedjenu metodu java.lang.Object.toString())
  * @param kolona
  * @return
  */
public static Class getColumnClass(int kolona)
{
try {
 return Class.forName(Konstante.DEFAULT_RETURN_CLASS);
}
catch (ClassNotFoundException ex) {
 return null;
}
} // getColumnClass

 public int compareAtColumn(UvaljivoUTablicu objekt,int kolona)
  {
   Comparable a=null,b=null;
   try {
    a = (Comparable)this.getValueAt(kolona);
    b = (Comparable) objekt.getValueAt(kolona);
   }
   catch (ClassCastException ex)
   {
    Object ta,tb;
    ta=this.getValueAt(kolona);
    tb=objekt.getValueAt(kolona);

    if (ta instanceof java.util.GregorianCalendar &&
        tb instanceof java.util.GregorianCalendar)
    {
     java.util.GregorianCalendar ga=(java.util.GregorianCalendar)ta;
     java.util.GregorianCalendar gb=(java.util.GregorianCalendar)tb;

     if (ga.after(gb))
      return 1;
     else
      if (ga.before(gb))
      return -1;
     else return 0;
    }
    return 0;
   }
   catch(Exception e)
   {
    return 0;
   }

   if (a==null) return 1;
   else
    if (b==null) return -1;

   return a.compareTo(b);
  }//compareAtColumn



} //UvaljivoUTablicu