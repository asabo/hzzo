package biz.sunce.util;

public final class StringUtils {

	/**
	 * Provjerava da li se neki String nalazi unutar polja stringova.
	 * 
	 * @param pValue
	 *            - string koji se provjerava
	 * @param pArray
	 *            - polje koje sadrži vrijednosti
	 * @return
	 */
	public static boolean isIn(String pValue, String[] pArray) {
		if (pArray == null) {
			return false;
		}

		for (int i = 0; i < pArray.length; i++) {
			if (pArray[i].equals(pValue)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Vraca negaciju {@link #isIn(String, String[]) isIn} metode.
	 * 
	 * @param pValue
	 * @param pArray
	 * @return
	 */
	public static boolean isNotIn(String pValue, String[] pArray) {
		if (pArray == null) {
			return true;
		}

		return !isIn(pValue, pArray);
	}

	public static boolean imaSamoBrojeve(String ulaz) {
		for (int i = 0; i < ulaz.length(); i++) {
			if (!Character.isDigit(ulaz.charAt(i)))
				return false;
		}
		return true;
	}

	public static boolean imaSamoBrojeveISlova(String ulaz) {
		for (int i = 0; i < ulaz.length(); i++) {
			char znak = ulaz.charAt(i);
			if (!Character.isDigit(znak) && !Character.isLetter(znak))
				return false;
		}
		return true;
	}

	public static boolean imaSamoSlova(String ulaz) {
		for (int i = 0; i < ulaz.length(); i++) {
			if (!Character.isLetter(ulaz.charAt(i)))
				return false;
		}
		return true;

	}

	public static String makniSlova(String ulaz) {
		StringBuilder izlaz = new StringBuilder(ulaz.length());

		int ulLen = ulaz.length();
		for (int i = 0; i < ulLen; i++) {
			char charAt = ulaz.charAt(i);
			if (Character.isLetter(charAt))
				continue;

			izlaz.append(charAt);
		}
		return izlaz.toString();
	}

	public static boolean isEmpty(String iznSudjForma) {
		return iznSudjForma==null || iznSudjForma.trim().equals("");
	}
}
