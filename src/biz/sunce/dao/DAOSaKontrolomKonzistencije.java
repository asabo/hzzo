/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.ValueObject;

/**
 * datum:2006.03.07
 * @author asabo
 *
 */
 
public interface DAOSaKontrolomKonzistencije<VO extends ValueObject> extends DAO<VO> 
{
/**
 metoda gleda jesu li sve vrijednosti objekta ispravne i vraca nazad null ako je sve ok, 
 vraca String poruku koja opisuje 'jednostavnim' jezikom di je problem. Metoda vraca nazad 
 samo prvi problem. Popravljanje doticne greske ne garantira da ce ova metoda u slijedecem pozivu vratiti null
 metoda je duzna javiti samo prvu od nekoliko potencijalnih gresaka*/	
public String narusavaLiObjektKonzistentnost(ValueObject objekt);
}