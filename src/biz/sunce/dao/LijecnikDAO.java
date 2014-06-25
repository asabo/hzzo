/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.LijecnikVO;

/**
 * datum:2005.05.15
 * @author dstanic
 *
 */
public interface LijecnikDAO extends JakiDAO<LijecnikVO>
{
//SearchCriteria kriterij za pretrazivanje postoji li u bazi lijecnik odredjenog imena i prezimena
public static String KRITERIJ_IME_PREZIME="lij_kriterij_ime_prezime";
}
