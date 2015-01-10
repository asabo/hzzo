package biz.sunce.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public final class Util {
	public static final boolean checkEmailAddress(String adresa) {
		int At = adresa.indexOf('@');
		if (At == -1)
			return false;
		if (adresa.indexOf('@', At + 1) != -1)
			return false;
		int brojTocaka = 0;
		for (int i = 0; i < adresa.length(); i++) {
			if (adresa.charAt(i) == ' ')
				return false;
			if (adresa.charAt(i) == '.')
				brojTocaka++;
		}

		if (brojTocaka < 1)
			return false;
		int tocka[] = new int[brojTocaka];
		int j = 0;
		for (int i = 0; i < brojTocaka; i++) {
			tocka[i] = adresa.indexOf(".", j);
			j = tocka[i] + 1;
		}

		if (brojTocaka == 1 && tocka[0] < At)
			return false;
		if (At < 2 || tocka[0] < 2)
			return false;
		if (adresa.length() - At < 6)
			return false;
		if (tocka[brojTocaka - 1] < At)
			return false;
		if (adresa.length() - tocka[brojTocaka - 1] < 3)
			return false;
		if (brojTocaka == 1 && tocka[0] - At < 3)
			return false;
		if (brojTocaka > 1) {
			for (int i = 0; i < brojTocaka - 1; i++) {
				if (tocka[i + 1] - tocka[i] < 3)
					return false;
				if (At <= tocka[i] || At >= tocka[i + 1])
					continue;
				if (At - tocka[i] < 3)
					return false;
				if (tocka[i + 1] - At < 3)
					return false;
			}

		}
		String malaSlova = "abcdefghijklmnopqrstuvyxwz";
		String velikaSlova = "ABCDEFGHIJKLMNOPQRSTUVYXWZ";
		String ostalo = "-_@.0123456789";
		int duzina = adresa.length();
		int slo = malaSlova.length();
		int ost = ostalo.length();
		for (int i = 0; i < duzina; i++) {
			char slovo = adresa.charAt(i);
			boolean nasao = false;
			j = 0;
			do {
				if (j >= slo)
					break;
				if (malaSlova.charAt(j) == slovo
						|| velikaSlova.charAt(j) == slovo) {
					nasao = true;
					break;
				}
				j++;
			} while (true);
			if (!nasao) {
				int k = 0;
				do {
					if (k >= ost)
						break;
					if (ostalo.charAt(k) == slovo) {
						nasao = true;
						break;
					}
					k++;
				} while (true);
			}
			if (!nasao)
				return false;
		}

		return true;
	}

	public static final String pretvoriLipeUIznosKaoString(int lipe) {
		int sKn = lipe / 100;
		int sLp = lipe % 100;
		return sKn + "." + (sLp >= 10 ? "" + sLp : "0" + sLp);
	}

	public static final String zaravnajNasaSlova(String ulaz) {
		return zaravnajNasaSlova(ulaz, false);
	}

	public static String zaravnajNasaSlova(String ulaz, boolean sms) {
		String tm = ulaz == null ? "?!?" : ulaz;
		tm = tm.replaceAll("\u010D", "c");
		tm = tm.replaceAll("\u0107", "c");
		tm = tm.replaceAll("\u0161", "s");
		if (!sms)
			tm = tm.replaceAll("\u0111", "dj");
		else
			tm = tm.replaceAll("\u0111", "d");
		tm = tm.replaceAll("\u017E", "z").trim();
		tm = tm.replaceAll("\u010C", "C");
		tm = tm.replaceAll("\u0106", "C");
		tm = tm.replaceAll("\u0160", "S");
		if (!sms)
			tm = tm.replaceAll("\u0110", "DJ");
		else
			tm = tm.replaceAll("\u0110", "D");
		tm = tm.replaceAll("\u017D", "Z").trim();
		return tm;
	}

	public static final String convertDateToString(java.util.Date d,
			boolean prikaziVrijeme) {
		if (!prikaziVrijeme) {
			return convertDateToString(d);
		} else {
			String dat = convertDateToString(d);
			dat = dat + " ";
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(d.getTime());
			int sat = c.get(11);
			int min = c.get(12);
			dat = dat + (sat >= 10 ? "" + sat : "0" + sat) + ":"
					+ (min >= 10 ? "" + min : "0" + min);
			return dat;
		}
	}

	public static final String convertDateToString(java.util.Date d) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d.getTime());
		return convertCalendarToString(c);
	}

	public static Timestamp convert(java.util.Date utilDate) {
		return new Timestamp(utilDate.getTime());
	}

	public static final String convertCalendarToString(Calendar c,
			boolean prikaziVrijeme) {
		if (!prikaziVrijeme) {
			return convertCalendarToString(c);
		} 
		else 
		{
			String dat = convertCalendarToString(c);
			StringBuilder sb = new StringBuilder(24);
			sb.append(dat);
			sb.append(" ");
			int sat = c.get(11);
			int min = c.get(12);

			sb.append(sat >= 10 ? "" + sat : "0" + sat);
			sb.append( ":" );			
			sb.append(min >= 10 ? "" + min : "0" + min);
			
			return sb.toString();
		}
	}

	public static final String convertCalendarToStringForSQLQuery(Calendar c,
			boolean prikaziVrijeme) {
		if (!prikaziVrijeme)
			return convertCalendarToStringForSQLQuery(c);
		String dat = convertCalendarToStringForSQLQuery(c);
		dat = dat + " ";
		int sat = c.get(11);
		int min = c.get(12);
		int mili = c.get(14);
		int sek = c.get(13);
		String sMili = "";
		if (mili < 10)
			sMili = "00" + mili;
		else if (mili < 100)
			sMili = "0" + mili;
		else
			sMili = "" + mili;
		dat = dat + (sat >= 10 ? "" + sat : "0" + sat) + ":"
				+ (min >= 10 ? "" + min : "0" + min) + ":"
				+ (sek >= 10 ? "" + sek : "0" + sek) + "." + sMili;
		return dat;
	}

	public static final String convertCalendarToStringForSQLQuery(Calendar c) {
		if (c == null) {
			return null;
		} else {
			int dan = c.get(5);
			int mj = c.get(2) + 1;
			int god = c.get(1);
			return god + "-" + (mj >= 10 ? "" + mj : "0" + mj) + "-"
					+ (dan >= 10 ? "" + dan : "0" + dan);
		}
	}

	public static String convertCalendarToString(Calendar c) {
		if (c == null) {
			return null;
		} else {
			int dan = c.get(5);
			int mj = c.get(2) + 1;
			int god = c.get(1);
			StringBuilder sb = new StringBuilder(12);
			sb.append((dan >= 10 ? "" + dan : "0" + dan));
			sb.append(".");
			sb.append(mj >= 10 ? "" + mj : "0" + mj);
			sb.append(".");
			sb.append( god );
			
			return sb.toString();
		}
	}

	public static String convertCalendarToStringForForm(Calendar c) {
		if (c == null)
			return "";
		if (c == null) {
			return null;
		} else {
			int dan = c.get(5);
			int mj = c.get(2) + 1;
			int god = c.get(1);
			god = god >= 2000 ? god - 2000 : god;
			return (dan >= 10 ? "" + dan : "0" + dan) + ""
					+ (mj >= 10 ? "" + mj : "0" + mj)
					+ (god >= 10 ? "" + god : "0" + god);
		}
	}

	public static Calendar provjeriIspravnostDatuma(String dat) {
		if (dat == null)
			return null;
		dat = dat.trim();
		if (dat.length() != 8 && dat.length() != 6)
			return null;
		Calendar cal;
		try {
			int dan = Integer.parseInt(dat.substring(0, 2));
			int mj = Integer.parseInt(dat.substring(2, 4));
			int god;
			if (dat.length() == 8)
				god = Integer.parseInt(dat.substring(4, 8));
			else
				god = Integer.parseInt(dat.substring(4, 6)) + 2000;
			cal = Calendar.getInstance();
			cal.setLenient(false);
			cal.set(god, mj - 1, dan);
			try {
				cal.get(5);
			} catch (IllegalArgumentException iae) {
				return null;
			}
		} catch (NumberFormatException nfe) {
			return null;
		}
		return cal;
	}

	/**
	 * izracunava kontrolnu znamenku za 13-znamenkasti broj potvrde lijecnika
	 * 
	 * @param potvrda
	 * @return
	 */
	public static final int izracunajKontrolniBrojPotvrdeLijecnika(
			String potvrda) {
		int ponderi[] = { 7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 7 };
		int umnosci[] = new int[ponderi.length];
		if (potvrda == null || potvrda.trim().length() != 13)
			return -1;
		potvrda = potvrda.trim();
		int zbroj = 0;

		for (int i = 0; i < 13; i++) {
			String br = "" + potvrda.charAt(i);
			int znamenka = 0;
			try {
				znamenka = Integer.parseInt(br);
			} catch (NumberFormatException nfe) {
				return -1;
			}
			umnosci[i] = znamenka * ponderi[i];
			zbroj += umnosci[i];
		}

		int ostatak = (zbroj % 11);
		int kz = -1;
		if (ostatak == 0)
			kz = 5;
		else if (ostatak == 1)
			kz = 0;
		else
			kz = 11 - ostatak;

		return kz;
	}// izracunajKontrolniBrojPotvrdeLijecnika

	public static final boolean brojPotvrdeLijecnikaIspravna(String potvrda) {

		int kz = izracunajKontrolniBrojPotvrdeLijecnika(potvrda
				.substring(0, 13));
		int kontrolna;

		try {
			kontrolna = Integer.parseInt("" + potvrda.charAt(13));
		} catch (NumberFormatException nfe) {
			return false;
		}

		return kz == kontrolna;
	}// brojPotvrdeLijecnikaIspravna

	public static final boolean brojHzzoIskazniceIspravan(String broj) {
		boolean rez = false;
		int ponderi[] = { 7, 6, 5, 4, 3, 2, 7, 6 };
		int umnosci[] = new int[ponderi.length];
		if (broj == null || broj.trim().length() != 9)
			return false;
		broj = broj.trim();
		int zbroj = 0;
		int kontrolna = 0;
		for (int i = 0; i < 8; i++) {
			String br = "" + broj.charAt(i);
			int znamenka = 0;
			try {
				znamenka = Integer.parseInt(br);
			} catch (NumberFormatException nfe) {
				return false;
			}
			umnosci[i] = znamenka * ponderi[i];
			zbroj += umnosci[i];
		}

		try {
			kontrolna = Integer.parseInt("" + broj.charAt(8));
		} catch (NumberFormatException nfe) {
			return false;
		}
		int ostatak = 11 - zbroj % 11;
		if (ostatak == 11 && kontrolna == 0)
			return true;
		if (ostatak == 10)
			return true;
		else
			return ostatak == kontrolna;
	}

	public static final boolean sifraHzzoIsporuciteljaIspravna(String broj) {
		boolean rez = false;
		int ponderi[] = { 9, 8, 7, 6, 5, 4, 3, 2 };
		int umnosci[] = new int[ponderi.length];
		if (broj == null || broj.trim().length() != 9)
			return false;
		broj = broj.trim();
		int zbroj = 0;
		int kontrolna = 0;
		for (int i = 0; i < 8; i++) {
			String br = "" + broj.charAt(i);
			int znamenka = 0;
			try {
				znamenka = Integer.parseInt(br);
			} catch (NumberFormatException nfe) {
				return false;
			}
			umnosci[i] = znamenka * ponderi[i];
			zbroj += umnosci[i];
		}

		try {
			kontrolna = Integer.parseInt("" + broj.charAt(8));
		} catch (NumberFormatException nfe) {
			return false;
		}
		int ostatak = zbroj % 11;
		if (ostatak > 1) {
			int kontr = 11 - ostatak;
			return kontr == kontrolna;
		}
		if (ostatak == 1 && kontrolna == 0)
			return true;
		return ostatak == 0 && kontrolna == 0;
	}

	public static final boolean tekstPodlijezeHzzoPravilimaA(String tekst) {
		boolean rez = true;
		if (tekst == null)
			return false;
		String slova = "abcdefghijklmnopqrrstuvwxyz";
		String slovaUp = slova.toUpperCase();
		for (int i = 0; i < tekst.length(); i++)
			if (slova.indexOf(tekst.charAt(i)) == -1
					&& slovaUp.indexOf(tekst.charAt(i)) == -1)
				return false;

		return rez;
	}

	public static final boolean tekstPodlijezeHzzoPravilimaANS(String tekst) {
		boolean rez = true;
		if (tekst == null)
			return false;
		String slova = "abcdefghijklmnopqrrstuvwxyz1234567890/-";
		String slovaUp = slova.toUpperCase();
		for (int i = 0; i < tekst.length(); i++)
			if (slova.indexOf(tekst.charAt(i)) == -1
					&& slovaUp.indexOf(tekst.charAt(i)) == -1)
				return false;

		return rez;
	}

	public static final boolean tekstPodlijezeHzzoPravilimaNS(String tekst) {
		boolean rez = true;
		if (tekst == null)
			return false;
		String slova = "1234567890/-";
		String slovaUp = slova.toUpperCase();
		for (int i = 0; i < tekst.length(); i++)
			if (slova.indexOf(tekst.charAt(i)) == -1
					&& slovaUp.indexOf(tekst.charAt(i)) == -1)
				return false;

		return rez;
	}

	public static final boolean jeliCijeliBroj(String tekst) {
		if (tekst == null)
			return false;
		try {
			Integer.parseInt(tekst);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static String formatDate(java.util.Date date, String pattern,
			Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		return date == null ? "" : sdf.format(date);
	}

	public static String formatDate(java.util.Date date, String pattern) {
		return formatDate(date, pattern, new Locale("hr", "HR"));
	}

	public static String formatDate(java.util.Date date) {
		return formatDate(date, "dd.MM.yyyy", new Locale("hr", "HR"));
	}

	public static String formatDatetime(java.util.Date date) {
		return date == null ? "" : sdfDateTime.format(date);
	}

	public static String formatTime(java.util.Date time) {
		return time == null ? "" : sdfTime.format(time);
	}

	public static String formatBoolean(Boolean bool) {
		return bool == null ? "" : bool.booleanValue() ? "Da" : "Ne";
	}

	public static String formatCurrency(Number novac) {
		DecimalFormat format = new DecimalFormat("#0.00");
		return format.format(novac);
	}

	public static String formatDouble(double value) {
		DecimalFormat format = new DecimalFormat("#0.00");
		return format.format(value);
	}

	public static Number getCurrency(String novac) {
		DecimalFormat format = new DecimalFormat("#0.00");
		Number result = null;
		try {
			result = format.parse(novac);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean checkJMBG(String jmbgstring) {
		char maska[] = { '7', '6', '5', '4', '3', '2', '7', '6', '5', '4', '3',
				'2' };
		char broj[] = new char[13];
		int kontr = 0;
		boolean flag = false;
		int sx = 150;
		int sy = 10;
		if (jmbgstring == null || jmbgstring.length() != 13)
			return false;
		jmbgstring.getChars(0, 13, broj, 0);
		for (int i = 0; i <= 12; i++)
			if (broj[i] < '0' || broj[i] > '9')
				return false;

		for (int i = 0; i <= 11; i++)
			kontr += (broj[i] - 48) * (maska[i] - 48);

		kontr %= 11;
		if (kontr != 0)
			kontr = 11 - kontr;
		return broj[12] - 48 == kontr;
	}

	public static Date getSQLDate(String myDate, String pattern) {
		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if (myDate == null || pattern == null)
			return null;
		try {
			result = sdf.parse(myDate);
		} catch (ParseException e) {
			return null;
		}
		return new Date(result.getTime());
	}

	public static java.util.Date getDate(String myDate) {
		return getDate(myDate, "dd.MM.yyyy");
	}

	public static java.util.Date getDate(String myDate, String pattern) {
		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if (myDate == null || pattern == null)
			return null;
		try {
			result = sdf.parse(myDate);
		} catch (ParseException e) {
			return null;
		}
		return result;
	}

	public static boolean isBefore(java.util.Date first, java.util.Date second) {
		
		if (first==null || second==null)
			return false;
		
		return first.before(second);
	}

	public static Date now() {
		return new Date((new java.util.Date()).getTime());
	}

	public static Timestamp nowTime() {
		return new Timestamp((new java.util.Date()).getTime());
	}

	public static String doubleBackSlash(String mostlyPaths) {
		StringBuffer sb = new StringBuffer(mostlyPaths);
		for (int i = 0; i < sb.length(); i++)
			if (sb.charAt(i) == '\\')
				sb.insert(i++, '\\');

		return sb.toString();
	}

	public static String replace(String pattern, String what, String with,
			int beginPositon) {
		if (pattern != null) {
			if (what == null)
				what = "";
			if (with == null)
				with = "";
			int i = pattern.indexOf(what, beginPositon);
			if (i != -1) {
				beginPositon = i + with.length();
				pattern = pattern.substring(0, i) + with
						+ pattern.substring(i + what.length());
				pattern = replace(pattern, what, with, beginPositon);
			}
		}
		return pattern;
	}

	public static String transformToHTML(java.util.Date myDate) {
		return formatDate(myDate);
	}

	public static String transformToXML(String text) {
		String result = text;
		if (result != null) {
			result = result.trim();
			if (result.indexOf("&") != -1)
				result = replace(result, "&", "&amp;", 0);
			if (result.indexOf("<") != -1)
				result = replace(result, "<", "&lt;", 0);
			if (result.indexOf(">") != -1)
				result = replace(result, ">", "&gt;", 0);
			if (result.indexOf("\n") != -1)
				result = replace(result, "\n", " ", 0);
		} else {
			result = "";
		}
		return result;
	}

	public static String decodeHexCharacters(String text) {
		String result = text;
		if (result != null) {
			result = result.trim();
			if (result.indexOf("&#x10C;") != -1)
				result = replace(result, "&#x10C;", "\u010C", 0);
			else if (result.indexOf("&#x10D;") != -1)
				result = replace(result, "&#x10D;", "\u010D", 0);
			else if (result.indexOf("&#x106;") != -1)
				result = replace(result, "&#x106;", "\u0106", 0);
			else if (result.indexOf("&#x107;") != -1)
				result = replace(result, "&#x107;", "\u0107", 0);
			else if (result.indexOf("&#x110;") != -1)
				result = replace(result, "&#x110;", "\u0110", 0);
			else if (result.indexOf("&#x111;") != -1)
				result = replace(result, "&#x111;", "\u0111", 0);
			else if (result.indexOf("&#x160;") != -1)
				result = replace(result, "&#x160;", "\u0160", 0);
			else if (result.indexOf("&#x161;") != -1)
				result = replace(result, "&#x161;", "\u0161", 0);
			else if (result.indexOf("&#x17D;") != -1)
				result = replace(result, "&#x17D;", "\u017D", 0);
			else if (result.indexOf("&#x17E;") != -1)
				result = replace(result, "&#x17E;", "\u017E", 0);
		} else {
			result = "";
		}
		return result;
	}

	public static String transformToHTML(String text) {
		String result = text;
		if (result != null) {
			result = result.trim();
			if (result.indexOf("&") != -1)
				result = replace(result, "&", "&amp;", 0);
			if (result.indexOf("\"") != -1)
				result = replace(result, "\"", "&quot;", 0);
			if (result.indexOf("'") != -1)
				result = replace(result, "'", "&acute;", 0);
			if (result.indexOf("<") != -1)
				result = replace(result, "<", "&lt;", 0);
			if (result.indexOf(">") != -1)
				result = replace(result, ">", "&gt;", 0);
			if (result.indexOf("\n") != -1)
				result = replace(result, "\n", " ", 0);
		} else {
			result = "";
		}
		return result;
	}

	public static String transformToHTML(Integer number) {
		String result = "";
		try {
			result = number.toString();
		} catch (Exception e) {
		}
		return result;
	}

	public static String transformToHTML(Double number) {
		String result = "";
		try {
			result = number.toString();
		} catch (Exception e) {
		}
		return result;
	}

	public static String transformToHTML(String text, int options) {
		text = transformToHTML(text);
		switch (options) {
		case 0: // '\0'
			text = replace(text, "\n", "<br/>", 0);
			text = replace(text, "\r", "", 0);
			break;

		case 1: // '\001'
			text = replace(text, "\n", " ", 0);
			text = replace(text, "\r", "", 0);
			break;
		}
		return text;
	}

	public static String firstLetterToUper(String str) {
		if (str == null || str.equals(""))
			return str;
		else
			return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static double getPercentage(int whole, int part) {
		if (whole > 0 && part > 0)
			return (((double) part * 1.0D) / (double) whole) * 100D;
		else
			return 0.0D;
	}

	public static Integer[] toIntegerArray(String integers[]) {
		Integer result[] = null;
		if (integers != null) {
			result = new Integer[integers.length];
			for (int i = 0; i < integers.length; i++)
				result[i] = Integer.valueOf(integers[i]);

		} else {
			result = new Integer[1];
			result[0] = Integer.valueOf(-1);
		}
		return result;
	}

	public static String displayAccors(Object obj) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		if (obj == null) {
			result = "Object is null";
		} else {
			sb.append("\r\n" + obj.getClass().getName() + "\r\n");
			sb.append("----------------------------------------\n");
			Method methods[] = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				String name = methods[i].getName();
				if (!name.startsWith("get") || name.equals("getClass"))
					continue;
				sb.append(name + "  : ");
				try {
					String str = "" + methods[i].invoke(obj, null);
					sb.append(str);
					sb.append(" \r\n");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			sb.append("----------------------------------------\n");
			result = sb.toString();
		}
		return result;
	}

	public static String partnerToHTML(String text) {
		String result = text;
		if (result != null) {
			result = result.trim();
			if (result.indexOf("\"") != -1)
				result = replace(result, "\"", "", 0);
			if (result.indexOf("'") != -1)
				result = replace(result, "'", "", 0);
		} else {
			result = "";
		}
		return result;
	}

	private static final String defaultDateFormat = "dd.MM.yyyy";
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat(
			"dd.MM.yyyy", new Locale("hr", "HR"));
	private static final SimpleDateFormat sdfTime = new SimpleDateFormat(
			"HH:mm", new Locale("hr", "HR"));
	private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm", new Locale("hr", "HR"));
	public static final String daniUTjednu[] = { "nedjelja", "ponedjeljak",
			"utorak", "srijeda", "\u010Detvrtak", "petak", "subota" };
	public static final String daniUTjednuSkraceni[] = { "ned", "pon", "uto",
			"sri", "\u010Det", "pet", "sub" };
	public static final String mjeseci[] = { "sije\u010Danj", "velja\u010Da",
			"o\u017Eujak", "travanj", "svibanj", "lipanj", "srpanj", "kolovoz",
			"rujan", "listopad", "studeni", "prosinac" };
	public static String PATH_SEPARATOR = System.getProperty("file.separator");
	public static final int TTH_ENTER_TO_BR = 0;
	public static final int TTH_ENTER_TO_SPACE = 1;
	public static final int TTH_ENTER_NO_CHANGES = 2;

}