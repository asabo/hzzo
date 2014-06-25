/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.RacunVO;

/**
 * datum:2006.02.26
 * @author asabo
 *
 */
public interface RacunDAO extends JakiDAO<RacunVO>
{
	public static final String KRITERIJ_RACUNI_PO_VISE_KRITERIJA="racuni_po_vise_kriterija";
    public static final String KRITERIJ_RACUNI_SA_BROJEM_OSOBNOG_RACUNA="racun_sa_brojem_osobnog_racuna";
}
