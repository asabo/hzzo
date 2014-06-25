package biz.sunce.dao;

import java.util.Hashtable;

public abstract class CacheabilniDAO<VO> {

	Hashtable<Integer, VO> cache=new Hashtable<Integer, VO>(128);
	
	public VO povuciIzCachea(Integer sifra)
	{
		return cache.get(sifra);
	}
	
	public void ubaciUCache(Integer sifra,VO objekt)
	{
		cache.put(sifra, objekt);
	}
	
	public void izbaciIzCachea(Integer sifra)
	{
		cache.remove(sifra);
	}
	
	public void pocistiCache()
	{
		cache.clear();
	}
	
}
