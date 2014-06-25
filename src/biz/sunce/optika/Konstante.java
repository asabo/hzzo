/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2005.11.25
 * @author asabo
 *
 */
public final class Konstante
{
public static final String PREDLOZAK_ROOT="biz/sunce/obrasci/";
public static final String PREDLOZAK_ZAGLAVLJE=PREDLOZAK_ROOT+"zaglavlje.html";
public static final String PREDLOZAK_PREGLED=PREDLOZAK_ROOT+"pregled.html";
public static final String PREDLOZAK_POZIV_KLIJENTU=PREDLOZAK_ROOT+"poziv_klijentu.html";
public static final String PREDLOZAK_PODSJETNIK=PREDLOZAK_ROOT+"podsjetnik.html";
public static final String PREDLOZAK_TJEDNE_OBVEZE=PREDLOZAK_ROOT+"tjedne_obaveze.html";
public static final String PREDLOZAK_HZZO_OBRACUN=PREDLOZAK_ROOT+"hzzo_obracun.html";
public static final String PREDLOZAK_HZZO_OBRACUN_ISPRAVAK=PREDLOZAK_ROOT+"hzzo_obracun_ispravak.html";
public static final String PREDLOZAK_HZZO_IZVJESCE_3MJ=PREDLOZAK_ROOT+"hzzo_tromjesecno_izvjesce.html";


//21.04.06. -asabo- dodano
public static final String PREDLOZAK_ORDINIRANE_NAOCALE=PREDLOZAK_ROOT+"ordinirane_naocale.html";


public static final String POSTAVKE_SIFRA_TVRTKE=PostavkeBean.TVRTKA_SIFRA;
public static final String POSTAVKE_SIFRA_POSLOVNICE=PostavkeBean.TVRTKA_SIFRA_POSLOVNICE;
public static final String POSTAVKE_SIFRA_KLIJENTA="sifra_klijenta"; // ne treba u postavke bean gurati
public static final String POSTAVKE_SYNCH_USERNAME="synch_username";
public static final String POSTAVKE_SYNCH_PASSWORD="synch_password";
public static final String POSTAVKE_KORISNICKO_IME="korisnicko_ime";

public static final String POSTAVKE_SYNCH_SERVER="synch_server";

public static final String VRIJEME_ZADNJE_SINKRONIZACIJE="vrijemeSinkronizacije";
public static final String DEFAULT_CHARSET="CP1250";

public static final String HZZO_POSTAVKA="asdoghsad"; // stvarno nema veze kako ce se zvati postavka, jos bolje bezveze
public static final String HZZO_POSTAVKA_SVA_POMAGALA="sdljfsd"; // stvarno nema veze kako ce se zvati postavka, jos bolje bezveze

public static final String RAZMJENA_PODATAKA_O_REGLEDIMA_POSTAVKA="idu_pregledi"; // stvarno nema veze kako ce se zvati postavka, jos bolje bezveze

    public Konstante() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    }
}//Konstante
