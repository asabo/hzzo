/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.ProizvodjacVO;

/**
 * datum:2005.10.09
 * @author asabo
 *
 */
public interface ProizvodjaciDAO extends JakiDAO<ProizvodjacVO>
{
// ako cemo bas po pravilima struke, staklima se smatraju 'stakla' na naocalama
// koja vise ne bi smjela biti staklena (tj. mineralna) (radi posjekotina i sl.)
// a lecama se smatraju kontaktne lece koje idu na direktno na oko  
public static String KRITERIJ_PROIZVODJACI_SAMO_STAKLA="krit_proizv_samo_stakla";
public static String KRITERIJ_PROIZVODJACI_SAMO_LECE="krit_proizv_samo_lece";
public static String KRITERIJ_PROIZVODJACI_SAMO_NAOCALE="krit_proizv_samo_naocale";
public static String KRITERIJ_PROIZVODJACI_LECE_STAKLA="krit_proizv_lece_stakla";
public static String KRITERIJ_PROIZVODJACI_HZZO_SIFRA="krit_proizv_hzzo_sifra";
}
