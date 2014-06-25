/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.StavkaRacunaVO;

/**
 * datum:2006.02.26
 * @author asabo
 *
 */
public interface StavkaRacunaDAO extends DAOSaKontrolomKonzistencije<StavkaRacunaVO>
{
//koristit ce se za upite koliko je izdano robe na nekog lijecnika u nekom razdoblju
//po nekoj skupini artikala, hzzo podruznici...
public static final String KRITERIJ_UKUPNO_PO_STAVKAMA="ukupno_po_stavkama";
public static final String KRITERIJ_HZZO_IZVJESCE="stavke_rac_hzzo_izvjesce";
}
