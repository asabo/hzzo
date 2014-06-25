/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.HzzoObracunVO;

/**
 * datum:2006.03.28
 * @author asabo
 *
 */
public interface HzzoObracunDAO extends JakiDAO<HzzoObracunVO>
{

public static final String KRITERIJ_SVI_RACUNI_ZA_OBRACUN="krit_svi_racuni_za_obracun";
//popis svih obracuna napravljenih za odredjenu podruznicu, ako datum!=null sve koje prethode tom datumu
public static final String KRITERIJ_SVI_OBRACUNI_ZA_PODRUZNICU_HZZO="krit_svi_obracuni_za_podruznicu_hzzo";
public static final String KRITERIJ_OBRACUN_DATUM_SIFRA_PODRUZNICE="krit_obracuni_datum_sifra_podruznice";

}
