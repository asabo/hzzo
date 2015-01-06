package biz.sunce.optika;

import org.junit.Assert;
import org.junit.Test;

import biz.sunce.optika.azurirac.AzuriracPomagala;

public class AzuriracPomagalaTest 
{

	
	@Test 
	public void testPotezArtikala()
	{
		boolean rez = true;
		
		AzuriracPomagala azurirac = new AzuriracPomagala();
		
		rez = azurirac.azurirajPomagala(null);
		
		Assert.assertTrue( rez );
	}
	
}
