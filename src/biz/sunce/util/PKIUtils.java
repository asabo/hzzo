package biz.sunce.util;

/**
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: ANSA</p>
 * @author Ante Sabo
 * @version 1.0
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public final class PKIUtils {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private String algoritam = "RSA";
	private String provider = null;
	private String kljuceviAlgoritam = algoritam;// moze biti i drugacije npr.
													// alg/mode/padding..
	// javni kljuc druge komunikacijske jedinke... sluzi za zakodirati poruku
	// koja se salje doticnoj osobi...
	private PublicKey otherPublicKey;
	private PrivateKey otherPrivateKey;

	public PKIUtils() {
		provider = "BC";
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public void generateKeys() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(
					kljuceviAlgoritam, provider);
			keyGen.initialize(1024);

			KeyPair pair = keyGen.generateKeyPair();

			this.privateKey = pair.getPrivate();
			this.publicKey = pair.getPublic();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] getPubKeyAsByteArray() {
		return this.getPubKeyAsByteArray(true);
	}

	public byte[] getPubKeyAsByteArray(boolean koristiEncoded) {
		if (koristiEncoded)
			return this.publicKey.getEncoded();
		else {
			ByteArrayOutputStream baos = null;
			ObjectOutputStream oos = null;
			try {
				baos = new ByteArrayOutputStream(256);
				oos = new ObjectOutputStream(baos);

				oos.writeObject(this.publicKey);
				oos.flush();
				try {
					if (oos != null)
						oos.close();
				} catch (IOException ioe) {
				}
				try {
					if (baos != null)
						baos.close();
				} catch (IOException ioe) {
				}
				return baos.toByteArray();
			} catch (IOException e) {
				System.out
						.println("Iznimka kod pretvaranja javnog kljuca u niz byteova: "
								+ e);
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (oos != null)
						oos.close();
				} catch (IOException ioe) {
				}
				try {
					if (baos != null)
						baos.close();
				} catch (IOException ioe) {
				}
			}
		}// else
	}// getPubKeyAsByteArray

	public byte[] getPrivateKeyAsByteArray() {
		return this.getPrivateKeyAsByteArray(true);
	}

	public byte[] getPrivateKeyAsByteArray(boolean koristiEncoded) {
		if (koristiEncoded)
			return this.privateKey.getEncoded();
		else {
			ByteArrayOutputStream baos = null;
			ObjectOutputStream oos = null;
			try {
				baos = new ByteArrayOutputStream(256);
				oos = new ObjectOutputStream(baos);

				oos.writeObject(this.privateKey);
				oos.flush();
				try {
					if (oos != null)
						oos.close();
				} catch (IOException ioe) {
				}
				try {
					if (baos != null)
						baos.close();
				} catch (IOException ioe) {
				}
				return baos.toByteArray();
			} catch (IOException e) {
				System.out
						.println("Iznimka kod pretvaranja privatnog kljuca u niz byteova: "
								+ e);
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (oos != null)
						oos.close();
				} catch (IOException ioe) {
				}
				try {
					if (baos != null)
						baos.close();
				} catch (IOException ioe) {
				}
			}
		}// else
	}// getPrivateKeyAsByteArray

	public byte[] encrypt(byte[] podaci) {
		return this.encrypt(podaci, true);
	}

	// boolean otherPubKey kaze koristi li se 'umjetno' postavljeni pubKey ili
	// svjeze proizvedeni par radi posao..
	public byte[] encrypt(byte[] podaci, boolean otherPubKey) {
		byte[] rez = null;
		try {
			Cipher cip = Cipher.getInstance(this.algoritam, provider);
			cip.init(Cipher.ENCRYPT_MODE, otherPubKey ? this.otherPublicKey
					: this.publicKey);
			rez = cip.doFinal(podaci);
		} catch (NoSuchPaddingException ex) {
			System.out.println("No such Padding exc: " + ex);
			return null;
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("No Such alg exc: " + ex);
			return null;
		} catch (InvalidKeyException inke) {
			System.out.println("Invalid key exc: " + inke);
			return null;
		} catch (Exception e) {
			System.out.println("Ex: " + e);
			e.printStackTrace();
			return null;
		}
		return rez;
	}// encrypt

	public byte[] decrypt(byte[] podaci) {
		return decrypt(podaci, false);
	}

	public byte[] decrypt(byte[] podaci, boolean otherPrivateKey) {
		byte[] rez = null;
		try {
			Cipher cip = Cipher.getInstance(this.algoritam, provider);
			cip.init(
					Cipher.DECRYPT_MODE,
					(otherPrivateKey || this.privateKey == null) ? this.otherPrivateKey
							: this.privateKey);
			rez = cip.doFinal(podaci);
		} catch (NoSuchPaddingException ex) {
			System.out.println("No such Padding exc: " + ex);
			return null;
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("No Such alg exc: " + ex);
			return null;
		} catch (InvalidKeyException inke) {
			System.out.println("Invalid key exc: " + inke);
			return null;
		} catch (Exception e) {
			System.out.println("Ex: " + e);
			return null;
		}

		return rez;
	}// decrypt

	public byte[] sign(byte[] podaci) {
		try {
			Signature dsa = Signature.getInstance("SHA1withDSA");
			dsa.initSign(privateKey);
			dsa.update(podaci);
			byte[] signature = dsa.sign();
			return signature;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}// sign

	public boolean verifySignature(byte[] podaci, byte[] signature) {
		try {
			Signature dsa = Signature.getInstance("SHA1withDSA");
			dsa.initVerify(publicKey != null ? publicKey : otherPublicKey);

			dsa.update(podaci);
			boolean verifies = dsa.verify(signature);

			return verifies;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}// verifySignature

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
				return false;
			}
			for (int i = 0; i < token.length(); i++) {
				if (!Character.isDigit(token.charAt(i))) {
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

	public PublicKey getOtherPublicKey() {
		return otherPublicKey;
	}

	public void setOtherPublicKey(PublicKey otherPublicKey) {
		this.otherPublicKey = otherPublicKey;
	}

	public void setOtherPublicKey(byte[] podaci) {
		setOtherPublicKey(podaci, true);
	}// setOtherPublicKey(byte[])

	public void setOtherPublicKey(byte[] podaci, boolean koristiEncoded) {
		if (koristiEncoded) {
			PublicKey pubKey = null;

			try {
				KeyFactory keyFac = KeyFactory.getInstance(algoritam);

				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(podaci);

				pubKey = keyFac.generatePublic(x509KeySpec);
			} catch (Exception anyEx) {
				anyEx.printStackTrace();
			}

			this.otherPublicKey = pubKey;
		} else {
			ByteArrayInputStream bains = null;
			ObjectInputStream oins = null;
			try {
				bains = new ByteArrayInputStream(podaci);
				oins = new ObjectInputStream(bains);

				this.otherPublicKey = (PublicKey) oins.readObject();

				try {
					if (oins != null)
						oins.close();
				} catch (IOException ioe) {
				}
				try {
					if (bains != null)
						bains.close();
				} catch (IOException ioe) {
				}
				return;
			} catch (IOException e) {
				System.out
						.println("Iznimka kod dobivanja javnog kljuca iz niza byteova: "
								+ e);
				e.printStackTrace();
				return;
			} catch (ClassNotFoundException e) {
				System.out
						.println("CNFE Iznimka kod dobivanja javnog kljuca iz niza byteova: "
								+ e);
				e.printStackTrace();
			} finally {
				try {
					if (oins != null)
						oins.close();
				} catch (IOException ioe) {
				}
				try {
					if (bains != null)
						bains.close();
				} catch (IOException ioe) {
				}
			}
		}// else

	}// setOtherPublicKey(byte[],boolean)

	public void setOtherPrivateKey(PrivateKey otherPrivateKey) {
		this.otherPrivateKey = otherPrivateKey;
	}

	public void setOtherPrivateKey(byte[] podaci) {
		setOtherPrivateKey(podaci, true);
	}// setOtherPublicKey(byte[])

	public void setOtherPrivateKey(byte[] podaci, boolean koristiEncoded) {
		if (koristiEncoded) {
			PrivateKey privKey = null;

			try {
				KeyFactory keyFac = KeyFactory.getInstance(algoritam);

				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(podaci);

				privKey = keyFac.generatePrivate(x509KeySpec);
			} catch (Exception anyEx) {
				anyEx.printStackTrace();
			}

			this.otherPrivateKey = privKey;
		} else {
			ByteArrayInputStream bains = null;
			ObjectInputStream oins = null;
			try {
				bains = new ByteArrayInputStream(podaci);
				oins = new ObjectInputStream(bains);

				this.otherPrivateKey = (PrivateKey) oins.readObject();

				try {
					if (oins != null)
						oins.close();
				} catch (IOException ioe) {
				}
				try {
					if (bains != null)
						bains.close();
				} catch (IOException ioe) {
				}
				return;
			} catch (IOException e) {
				System.out
						.println("Iznimka kod dobivanja privatnog kljuca iz niza byteova: "
								+ e);
				e.printStackTrace();
				return;
			} catch (ClassNotFoundException e) {
				System.out
						.println("CNFE Iznimka kod dobivanja privatnog kljuca iz niza byteova: "
								+ e);
				e.printStackTrace();
			} finally {
				try {
					if (oins != null)
						oins.close();
				} catch (IOException ioe) {
				}
				try {
					if (bains != null)
						bains.close();
				} catch (IOException ioe) {
				}
			}
		}// else
	}// setOtherPrivateKey(byte[],boolean)

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

}// PKIUtils
