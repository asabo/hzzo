/*
 * Project opticari
 *
 */
package biz.sunce.dao;

import biz.sunce.opticar.vo.PomagaloVO;

/**
 * datum:2006.02.26
 * @author asabo
 *
 */
public interface PomagaloDAO extends JakiDAO<PomagaloVO> 
{
	public static final String KRITERIJ_KORISTIMO_SVA_POMAGALA="krit_sva_pomagala";
	
	/**
	 * cisti odredjeni objekt iz cache-a, ako doticni DAO implementira nekako kesiranje..
	 * @param vo
	 */
	public void clearFromCache(PomagaloVO vo);

}
