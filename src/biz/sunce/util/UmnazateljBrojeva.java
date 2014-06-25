package biz.sunce.util;

public class UmnazateljBrojeva {

	public static String pomnozi(String prvi, String drugi)
	{
		String rez="";
		
	int prviSize = prvi.length();
	
	String[] faktori = new String[prviSize];
	
	 for (int i=prviSize-1; i>=0; i--)
	 {
		int potencija = (prviSize-i);
		
		char znamenka = prvi.charAt(i);
		
	 faktori[i] = pomnoziDio(znamenka, drugi, potencija);
		
	 }
	
	 rez = zbroji(faktori);
	 
	 faktori = null;
	 
	return rez;
	}

	private static String zbroji(String[] faktori) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String pomnoziDio(char znamenka, String drugi, int potencija) {
		
		try
		{
		long prvi = (long) (Long.parseLong(""+znamenka));
		double desetka = Math.pow(10, potencija);
		
		 int drugiSize = drugi.length();
		
		 int[] umnosci = new int[drugiSize];
		 		 
		 for (int i=drugiSize-1; i>=0; i--)
		 {
			 char dz = drugi.charAt(i);
			 long dr = Long.parseLong(""+dz);
			 
		 }
		 
		 return null;
		
		}
		catch (Exception e)
		{
			System.err.println(e);
			return null;
		}
				
	}
	
}
