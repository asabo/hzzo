/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.ValueObject;

/**
 * datum:2005.12.13
 * @author asabo
 * jaki objekt onaj je sto moze samostalno stajati i shodno tome moze
 * ga se editirati u posebnoj formi ...
 */
public interface JakiDAO<VO extends ValueObject> extends DAOSaKontrolomKonzistencije<VO>
{

}
