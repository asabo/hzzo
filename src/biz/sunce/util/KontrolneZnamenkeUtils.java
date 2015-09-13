package biz.sunce.util;

import biz.sunce.optika.Logger;


public final class KontrolneZnamenkeUtils {

  // private static final hr.ikb.utils.log.Logger LOG = Logger
  // .getLogger( KontrolneZnamenkeUtils.class );

   private KontrolneZnamenkeUtils() throws Exception {
      throw new Exception( "!" );
   }

   public static final String MODEL_00 = "00";

   public static final String MODEL_01 = "01";


   /**
    * Kontrolna znamenka za MOD11INI - koristi se u kontroli brojeva za poziv na broj
    */
   public final static int kontrolnaZnamenkaZaMod11INI( String s ) {
      // maknuti sve sta nisu brojevi
      String n = s.replaceAll( "\\D", "" );
      int zbroj = 0;
      int nlen = n.length();
	for ( int i = 0; i < nlen; i++ ) {
         char c = n.charAt( nlen - i - 1 );
         int d = c - '0';
         // LOG.info( "+=" + ( i + 2 ) + "*" + d );
         zbroj += d * ( i + 2 );
      }
      int ostatak = zbroj % 11;
      if ( ostatak <= 1 ) {
         return 0;
      }
      int rezultat = 11 - ostatak;
      rezultat = rezultat == 10 ? 0 : rezultat;
      return rezultat;
   }//kontrolnaZnamenkaZaMod11INI


   /**
    * Isto kao i {@link #kontrolnaZnamenkaZaModel(String, String)}, ali vraća rezultat kao
    * <code>int</code>.
    * @param pModel
    * @param pString
    * @return u slučaju da ne postoji model vraća <code>-1</code>
    */
   public static final int kontrolnaZnamenkaZaModelAsInt( String pModel, String pString ) {
      if ( pModel.equals( MODEL_01 ) ) {
         return kontrolnaZnamenkaZaMod11INI( pString );
      }
      return -1;
   }//kontrolnaZnamenkaZaModelAsInt


   /**
    * Vraća kontrolnu znamenku za zadani model i zadani string.
    * @param pModel
    *           - model za koji se racuna kontrolna znamenka
    * @param pString
    *           - ulazni string za koji se računa kontrolna znamenka
    * @return vraća prazan string ako ne postoji model za kojeg treba vratiti k.znamenku
    */
   public static final String kontrolnaZnamenkaZaModel( String pModel, String pString ) {
      int result = kontrolnaZnamenkaZaModelAsInt( pModel, pString );
      if ( result == -1 ) return "";

      return String.valueOf( result );
   }//kontrolnaZnamenkaZaModel

   /**
    * Vraća PNB za vrstu i ugovoreni proizvod, za model 01
    * @param vrsta
    *           vrsta proizvoda, vidi {@link ProizvodVo#SEFOVI}
    * @param proizvodId
    *           id ugovorenog proizvoda
    * @return
    */
   public static final String pozivNaBrojZaUgovoreniProizvod( String vrsta, String proizvodId ) {

      StringBuilder pnb = new StringBuilder();
      pnb.append( vrsta );
      pnb.append( "-" );
      pnb.append( proizvodId );
      pnb.append( KontrolneZnamenkeUtils.kontrolnaZnamenkaZaModel( MODEL_01, pnb
            .toString() ) );
      return pnb.toString();
   }//pozivNaBrojZaUgovoreniProizvod

   /**
    * provjerava 1. dali OIB ima 11 znakova i 2. dali je ispravan po ISO 7064 10,11 standardu
    * @param oib
    * @return jesi-nisi
    */
   public static final boolean ispravanOIB( String oib ) {
      return oib != null && oib.length() == 11 && ispravanISO_7064_MOD_11_10( oib );
   }//ispravanOIB

   /**
    * <p>
    * Ispravnost kontrolne znamenke po algoritmu opisanom u <a href=
    * 'http://www.regos.hr/UserDocsImages/downloads/kont_br_rac.pdf'>REGOS-ovom
    * dokumentu</a>.
    * </p>
    * <p>
    * Koristi se, na primjer, kod kontrole OIB-a.
    * </p>
    * @param oib
    * @return
    */
   public static final boolean ispravanISO_7064_MOD_11_10( String oib ) {
      int x = kontrolnaZnamenkaISO_7064_MOD_11_10( oib );

      int zadnjaZnamenka = oib.charAt( oib.length() - 1 ) - '0';
      int kontrolna = 11 - x;

      if( kontrolna == 10 ) {
         kontrolna = 0;
      }

      return kontrolna == zadnjaZnamenka;
   }//ispravanISO_7064_MOD_11_10

   public static final int kontrolnaZnamenkaISO_7064_MOD_11_10( String oib ) {
      return kontrolnaZnamenkaISO_7064_MOD_11_10( oib, true );
   }

   private static final int kontrolnaZnamenkaISO_7064_MOD_11_10( String oib,
         boolean sadrziKontrolnuZnamenku ) {
      int x = 0;
      int oiblen = oib.length() - ( sadrziKontrolnuZnamenku ? 1 : 0 );
      for ( int i = 0; i < oiblen; i++ ) {
         int znamenka = oib.charAt( i ) - '0';
         x = x + znamenka;
         x += 10;
         x %= 10;
         if ( x == 0 ) {
            x = 10;
         }
         x *= 2;
         x %= 11;
      }
      return x;
   }// kontrolnaZnamenkaISO_7064_MOD_11_10

   /**
    * Provjera konzistentnosti JMBG-a. JMBG koji nema 13 znakova ne moze biti nikako
    * ispravan sto se prvo provjerava, a tek zatim kontrolni broj.
    */
   public static final boolean ispravanJMBG( String jmbg ) {
      return ispravanJMBG( jmbg, true );
   }

   /**
    * Provjera konzistentnosti JMBG-a. JMBG koji nema 13 znakova ne moze biti nikako
    * ispravan sto se prvo provjerava, a tek zatim kontrolni broj.
    */
   public static final boolean ispravanJMBG( String jmbg, boolean samoHrvatski ) {
      // ako se validatoru ne pošalje podatak onda on ne smije vratiti
      // vrijednost true ...
      boolean res = false;

      int len;

      if ( jmbg != null ) {
         jmbg = jmbg.trim();
         len = jmbg.length();
         if ( len == 13 ) {
            // ako je samoHrvatski==true, gledamo jeli jmbg hrvatski po oznakama regija:
            // oznaka regije 03 ili 30..39, pocetne znamenke predstavljaju datum (ddmmgg)
            int oznakaRegije = Integer.parseInt( jmbg.substring( 7, 9 ) );
            if ( !samoHrvatski || (oznakaRegije == 3 || oznakaRegije >= 30 && oznakaRegije <= 39) ) {
               int kz, dan, mjes, god;
               boolean cond1 = false;
               boolean cond2 = false;
               // boolean cond3 = false;

               kz = kontr_mod11_jmbg( jmbg, len );
               dan = Integer.parseInt( jmbg.substring( 0, 2 ) );
               mjes = Integer.parseInt( jmbg.substring( 2, 4 ) );
               god = Integer.parseInt( jmbg.substring( 4, 6 ) );

               cond1 = ( kz == ( jmbg.charAt( len - 1 ) - '0' ) );
               //ako pregledavamo samo hrvatske JMBG-ove, onda datumi moraju biti unutar normalnih vrijednosti, za
               //izmisljenje JMBG-ove ne vrsimo ovu kontrolu. Pitanje se postavlja, sta ako moramo provjeravati bosanski? U
               //tom slucaju bilo bi korisno pregledati i raspone datuma...
               cond2 =  !samoHrvatski || ( ( dan > 0 && dan < 32 ) && ( mjes > 0 && mjes < 13 ) && ( ( god >= 89 && god <= 99 ) || ( god >= 0 && god <= 10 ) ) );
               // cond3 = ( jmbg.startsWith( "9" ) || jmbg.startsWith( "4" ) ); //$NON-NLS-1$//$NON-NLS-2$

               // res = cond1 && ( cond2 || cond3 );
               res = cond1 && cond2;
            }//if oznakaRegije
         }//if len == 13
      }//if jmbg != null

      if ( res ) {
         return true;
      }
      return false;
   }//ispravanJMBG

   public static final boolean ispravanPAN(String pan){
      if ( pan==null || pan.trim().length()==0 ) return false;
      pan=pan.trim();

      int kb=generateLuhnCheckDigit( pan.substring( 0,pan.length()-1 ) );

      int zadnji=pan.charAt( pan.length()-1 )-'0';

      return zadnji==kb;
   }//ispravanPAN

   /**
    * MBO je maticni broj osigurane osobe u HZZO-u, ima 9 znakova, od kojih 
    * je zadnja znamenka kontrolni broj po njihovom algoritmu
    * @param mbo
    * @return
    */
   public static final boolean ispravanMBO(String mbo){
	      if ( mbo==null || mbo.trim().length()==0 ) return false;
	      mbo=mbo.trim();
	      
	      if (mbo.length()!=9) return false;

	      int kb=kontrolnaZnamenkaZaMbo( mbo.substring( 0, mbo.length()-1 ) );

	      int zadnji=mbo.charAt( mbo.length()-1 )-'0';

	      return zadnji==kb;
	}//ispravanPAN

   //	7 6 5 4 3 2 7 6  
   final static int[] ponderiMBO={7,6,5,4,3,2,7,6};

   private static int kontrolnaZnamenkaZaMbo(String mboBezKz) {
	      int zbroj = 0;
	      int nlen = mboBezKz.length();
		for ( int i = 0; i < nlen; i++ ) {
	         char c = mboBezKz.charAt( i );
	         int d = c - '0';
	         // LOG.info( "+=" + ( i + 2 ) + "*" + d );
	         zbroj += d * ponderiMBO[i];
	      }//for
		
	      int ostatak = zbroj % 11;
	      if ( ostatak ==0 ) {
	         return ostatak;
	      }

	      int rezultat = 11 - ostatak;
	      
	      if ( rezultat==10 ){
	    	  Logger.log("Za MBO: "+mboBezKz+" dobili smo pri ra�unanju KZ ostatak="+ostatak+" �to nije dozvoljeno!");	    	  
	    	  throw new IllegalArgumentException("Krivi kontrolni broj za MBO: "+mboBezKz);
	      }//if
	      
	      //rezultat = rezultat == 10 ? 0 : rezultat;
	      return rezultat;
}//kontrolnaZnamenkaZaMbo


/** Provjera konzistentnosti matičnog broja poslovnog subjekta. */
   // TODO preklapanje koda s MBValidator - ujediniti kôd
   public static final boolean ispravanMB( String mb ) {
      // ako se validatoru ne posalje podatak onda on ne smije vratiti
      // vrijednost true ...
      boolean res = false;

      int len;

      if ( mb != null ) {
         mb = mb.trim();
         len = mb.length();
         if ( len == 8 ) {
            int kz;
            boolean cond1 = false;
            boolean cond2 = false;

            kz = kontr_mod11_mb( mb, len );

            cond1 = ( kz == ( mb.charAt( len - 1 ) - '0' ) );

            char pocetnaZnamenka = mb.charAt( 0 );
            switch ( pocetnaZnamenka ) {
               case '0':
               case '5':
               case '7':
               case '8':
               case '9':
                  cond2 = true;
            }

            res = cond1 && cond2;
         }
      }// if mb != null

      if ( res ) {
         return true;
      }
      return false;
   }//ispravanMB

   /**
    * Kontrolna znamenka za JMBG.
    * @return Broj od 0 do 9 (uključivši 0 i 9). U slučaju da je JMBG po definiciji
    *         neispravan, broj izvan tog raspona.
    */
   public static final int kontr_mod11_jmbg( String s1, int len ) {
      int i, suma, kb;

      suma = 0;
      for ( i = 1; i < len; i++ ) {
         suma += ( s1.charAt( i - 1 ) - '0' ) * ( ( len - i - 1 ) % 6 + 2 );
      }
      kb = 11 - suma % 11;
      // ako kb == 10, JMBG je po definiciji neispravan pa puštamo kako je
      if ( kb == 11 ) {
         kb = 0;
      }
      return kb;
   }//kontr_mod11_jmbg

   /**
    * Kontrolna znamenka za MB poslovnog subjekta.
    * @return Broj od 0 do 9 (uključivši 0 i 9). U slučaju da je MB po definiciji
    *         neispravan, broj izvan tog raspona.
    */
   public static final int kontr_mod11_mb( String s1, int len ) {
      int i, suma, kb;

      suma = 0;
      for ( i = 1; i < len; i++ ) {
         suma += ( s1.charAt( i - 1 ) - '0' ) * ( len - i + 1 );
      }
      kb = 11 - suma % 11;
      // ako kb == 11, MB je po definiciji neispravan pa puštamo kako je
      if ( kb == 10 ) {
         kb = 0;
      }
      return kb;
   }//kontr_mod11_mb

   public static final int kontrolnaZnamenkaZaOib( String oib,
         boolean sadrziKontrolnuZnamenku ) {
      int x = kontrolnaZnamenkaISO_7064_MOD_11_10( oib, sadrziKontrolnuZnamenku );
      int kontrolna = 11 - x;
      if ( kontrolna == 10 ) {
         kontrolna = 0;
      }
      return kontrolna;
   }

 
   /**
   *
   *
  ovo je za izracun kontrolnog broja po Luhnu - modulu 10
   <p>
   1. udvostrucavanje vrijednosti naizmjenicnih brojki pocevsi od desna
   <p>
   2. zbrajanje pojedinacnih brojki koje obuhvacaju brojke dobivene korakom 1 svakoj
   od neudvostrucenih brojki prvobitnog broja
   <p>
   3. ukoliko dobiveni (sveukupni) zbir zavrsava s nulom kb je nula.
   U drugom slucaju se zbir odbija od prvog veceg broja djeljivog sa 10.
   Razlika je Kontrolni Broj.
   *
   * <p>
  primjer 553105100011001 znamenke A1...15
  Z1=A1*2 = 10, Z3=A3*2 = 6, Z5=A5*2 = 0, Z7=A7*2 = 2, Z9=A9*2 = 0, Z11=A11*2 = 2, Z13=A13*2 = 0, A15*2 = 2
  eh sad dodje do lipih stvari, dobiveni brojevi se svode na jednoznamenkaste i zbroje sa preostalima
  Z1A=1+0 u ovom slucaju samo taj prvi koji je dvoznamenkasti se svodi na jednoznamenkaste Z1AA=1 i Z1AB=0
  Z1AA+Z1AB +A2+Z3+A4+Z5+A6+Z7+A8+Z9+A10+Z11+A12+Z13+A14+Z15
  1+0+5+6+1+0+5+2+0+0+0+2+1+0+0+2 = 25
  dobiveni rezultat nema nulu na kraju...prvi veci djeljiv s 10 je 30 pa...
  30-25= 5 -> to je kontrolni broj
   *
   *
   */

  public static final int generateLuhnCheckDigit( String aCardNumber ) {
     int sum = 0, added;
     boolean doubleFlag = true;

     for ( int i = aCardNumber.length() - 1; i >= 0; i-- ) {
        if ( doubleFlag ) {
           added = ( Integer.parseInt( aCardNumber.substring( i, i + 1 ) ) ) << 1;
           if ( added > 9 ) added -= 9;
        }
        else
           added = Integer.parseInt( aCardNumber.substring( i, i + 1 ) );
        sum += added;
        doubleFlag = !doubleFlag;
     }

     int mod10 = sum % 10;
     mod10 = 10 - mod10;
     if (mod10 == 10){
         mod10=0;
     }
     return mod10;

  }//generateLuhnCheckDigit

  /**
   * vrsi provjeru ispravnosti VBDI-a banke
   * @param vbdiBanke
   * @return
   */
  public final static boolean ispravanVbdi(String vbdiBanke){
     if ( vbdiBanke.length() != 7 ) return false;

     for (int i = 0; i < 7; i++){
        try {
           Integer.parseInt(vbdiBanke.substring(i, i+1));
        }
        catch (NumberFormatException e) {
           return false;
        }
     }//for i
     int ostatak = 10;
     int medjuostatak;

     for ( int i = 0; i < 6; i++ ) {
        medjuostatak = ( Integer.parseInt(vbdiBanke.substring(i, i+1)) + ostatak ) % 10;
        if (medjuostatak == 0) medjuostatak = 10;
        ostatak = (medjuostatak * 2) % 11;
     }//for i

     if (ostatak == 1) ostatak = 11;
     if (Integer.parseInt(vbdiBanke.substring(6)) != 11 - ostatak){
        return false;
     }//if
     return true;
  }//checkVbdi

  /**
   * vrsi provjeru dali je tekuci racun ispravan, tj. dali mu je kontrolna znamenka
   * na kraju ispravna
   * @param racun
   * @return
   */
  public final static boolean ispravanRacun(String racun){
     if( racun == null ) {
        return false;
     }//if

     if ( racun.length() != 10 ) return false;
     String kz=kontrolnaZnamenkaZaRacun( racun.substring( 0,9 ) );

     return kz.equals( racun.substring( 9 ) );
  }//checkRacun

  /**
   * računa kontrolnu znamenku za broj računa. Ulazni račun mora imati 9 znakova!
   * @param racun
   * @return
   */
  public final static String kontrolnaZnamenkaZaRacun(String racun){
     if( racun == null ) {
        return null;
     }//if

     if ( racun.length() != 9 ) return null;
     int ostatak = 10;
     int medjuostatak;

     for ( int i = 0; i < 9; i++ ) {
        medjuostatak = (Integer.parseInt(racun.substring(i, i+1)) + ostatak) % 10;
        if ( medjuostatak == 0 ) medjuostatak = 10;
        ostatak = (medjuostatak * 2) % 11;
     }//for i

     if (ostatak == 1) ostatak = 11;
     String rez=""+( 11 - ostatak );
     return rez;
  }//kontrolnaZnamenkaZaRacun

}//klasa