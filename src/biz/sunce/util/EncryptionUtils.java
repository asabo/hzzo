package biz.sunce.util;

/**
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: ANSA</p>
 * @author Ante Sabo
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptionUtils {
	private static String KEY_STRING = "123-145-118-97-244-156-10-24";
	private static SecretKey s;
	private static String algoritam = "DES";

	public static String encrypt(String source) {
		return getString(encrypt(source.getBytes()));
	}

	public static byte[] encrypt(byte[] source) {
		try {
			// Get our secret key
			Key key = getKey();

			// Create the cipher
			Cipher desCipher = Cipher.getInstance(algoritam);

			// Initialize the cipher for encryption
			desCipher.init(Cipher.ENCRYPT_MODE, key);

			// Our cleartext as bytes
			byte[] cleartext = source;

			// Encrypt the cleartext
			byte[] ciphertext = desCipher.doFinal(cleartext);

			// Return a String representation of the cipher text
			return ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKey generateKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(algoritam);
			s = keygen.generateKey();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decrypt(String source) {
		return new String(decrypt(source.getBytes()));
	}

	public static byte[] decrypt(byte[] source) {
		try {
			// Get our secret key
			Key key = getKey();

			// Create the cipher
			Cipher cipher = Cipher.getInstance(algoritam);

			// Encrypt the cleartext
			byte[] ciphertext = source;

			// Initialize the same cipher for decryption
			cipher.init(Cipher.DECRYPT_MODE, key);

			// Decrypt the ciphertext
			byte[] cleartext = cipher.doFinal(ciphertext);

			// Return the clear text
			return cleartext;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKey getKey() {
		return s;
	}

	// generira tajni (NE privatni!!!) kljuc na osnovi ulaznih bajtova, dakle
	// kada preko mreze doputuje tajni kljuc
	// ovako ga se postavlja u shemu za dekriptiranje.
	public static void generateKey(byte[] podaci) {
		s = new SecretKeySpec(podaci, algoritam);
	}

	/**
	 * Returns true if the specified text is encrypted, false otherwise
	 */
	public static boolean isEncrypted(String text) {
		// If the string does not have any separators then it is not
		// encrypted
		if (text.indexOf('-') == -1) {
			// /System.out.println( "text is not encrypted: no dashes" );
			return false;
		}

		StringTokenizer st = new StringTokenizer(text, "-", false);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.length() > 3) {
				// System.out.println(
				// "text is not encrypted: length of  token greater than 3: " +
				// token );
				return false;
			}
			for (int i = 0; i < token.length(); i++) {
				if (!Character.isDigit(token.charAt(i))) {
					// System.out.println(
					// "text is not encrypted: token is not a digit" );
					return false;
				}
			}
		}
		// System.out.println( "text is encrypted" );
		return true;
	}

	private static String getString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			sb.append((0x00FF & b));
			if (i + 1 < bytes.length) {
				sb.append("-");
			}
		}
		return sb.toString();
	}

	private static byte[] getBytes(String str) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StringTokenizer st = new StringTokenizer(str, "-", false);
		while (st.hasMoreTokens()) {
			int i = Integer.parseInt(st.nextToken());
			bos.write((byte) i);
		}
		return bos.toByteArray();
	}

	public static void showProviders() {
		try {
			Provider[] providers = Security.getProviders();
			for (int i = 0; i < providers.length; i++) {
				System.out.println("Provider: " + providers[i].getName() + ", "
						+ providers[i].getInfo());
				for (Iterator itr = providers[i].keySet().iterator(); itr
						.hasNext();) {
					String key = (String) itr.next();
					String value = (String) providers[i].get(key);
					System.out.println("\t" + key + " = " + value);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
