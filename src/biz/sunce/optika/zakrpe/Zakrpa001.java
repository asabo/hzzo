/*
 * Project opticari
 *
 */
package biz.sunce.optika.zakrpe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.JOptionPane;

import com.ansa.util.ResourceLoader;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.csc.SynchModul;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;

import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.03.20
 * 
 * @author asabo zakrpa koja kreira tablicu sa adresama podruznica hzzo-a i
 *         dodaje cijenu artiklima
 */
public final class Zakrpa001 {
	String[] upiti = null;
	String verzijaZakrpe = "028";
	String zakrpa = "zakrpa" + verzijaZakrpe;

	public Zakrpa001() {
		upiti = new String[8];

		upiti[0] = "alter table racuni alter column broj_potvrde2 set data type varchar(10)";
		upiti[1] = "alter table stavke_racuna drop constraint str_sifart";
		upiti[2] = "alter table artikli alter column sifra set data type varchar(12)";
		upiti[3] = "alter table stavke_racuna alter column sif_artikla set data type varchar(12)";
		upiti[4] = "alter table stavke_racuna add constraint STR_SIFART foreign key (sif_artikla) references artikli(sifra)";
		upiti[5] = "alter table artikli add column rokdo7g integer default null";
		upiti[6] = "alter table artikli add column rokdo18g integer default null";
		upiti[7] = "alter table artikli add column rok integer default null";
	}// konstruktor

	public String getVerzijaZakrpe() {
		return verzijaZakrpe;
	}

	public boolean zakrpaj() {
		boolean rez = true;

		// srediKontrolneBrojeve();

		String zakrpano = PostavkeBean.getPostavkaDB(zakrpa, "nije");

		// zakrpa je vec obavljena? Ako je vracamo nazad poruku kao da smo
		// zakrpali..
		if (!zakrpano.equals("nije"))
			return true;

		Connection con = null;
		Statement stmt = null;
		SynchModul sm = (SynchModul) DAOFactory.getInstance().getSynchModul();

		try {
			con = DAOFactory.getConnection();
			stmt = con.createStatement();

			int upLen = upiti.length;
			for (int i = 0; i < upLen; i++) {
				try {
					stmt.executeUpdate(upiti[i]);
				} catch (SQLException sqle) {
					if (sm != null)
						sm.log("SQL iznimka kod izvrsavanja zakrpe: " + sqle
								+ " upit[" + i + "] " + upiti[i]);
				} // nema hvatanja, ako ne prodje mozda je vec zakrpano

			}// for i
				// ako je zakrpa obavljena uspjesno
			if (rez) {
				if (verzijaZakrpe.equals("027"))
					prebaciNoveOptickeArtikle();

				if (verzijaZakrpe.equals("028"))
					prebaciNoveOrtopedskeArtikle();

				Calendar c = Calendar.getInstance();
				String sc = Util.convertCalendarToString(c);
				PostavkeBean.setPostavkaDB(zakrpa, sc);
				JOptionPane.showMessageDialog(GlavniFrame.getInstanca(),
						"Vaš softver je uspješno nadograðen na verziju 1.0.0."
								+ verzijaZakrpe, "Obavijest",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (SQLException e) {
			if (sm != null)
				sm.log("SQL iznimka kod izvrsavanja zakrpe: " + e);

			Logger.fatal("SQL iznimka kod zakrpe " + zakrpa, e);
			return false;
		} catch (Exception e) {
			if (sm != null)
				sm.log("Opæa iznimka kod izvršavanja zakrpe: " + e);

			Logger.fatal("Opæenita iznimka kod zakrpe " + zakrpa, e);
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqle) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
			}
			con = null;
		}

		return rez;
	}// zakrpaj

	private void prebaciNoveOrtopedskeArtikle() {
		String zip = "biz/sunce/optika/zakrpe/data/novi_orto_artikli_ISO.zip";
		String file = "novi_orto_artikli_ISO.csv";

		ResourceLoader loader = new ResourceLoader();

		Connection con = null;
		PreparedStatement ps = null;

		InputStream ins = loader.getCsvInputStreamFromZip(zip, file);

		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		String line;
		String[] zapis;
		String cijenaBroj = null;
		String rbr=null;
		String rok7,rok18,rok;
		try {
			con = DAOFactory.getConnection();
			ps = con.prepareStatement(insArt);

			String iso, artikl, cijena;
			while ((line = br.readLine()) != null) {
				zapis = line.split(";");
				rbr=zapis[0].trim();
				if (rbr.equals(""))
					continue;
				
				iso = zapis[1];
				artikl = zapis[5];
				rok7=zapis[13];
				rok18=zapis[14];
				rok=zapis[15];
				cijena = zapis[16];
				ps.setString(1, iso);
				ps.setString(2, artikl);
				cijenaBroj = cijena.replaceFirst("\\.", "")
						.replaceFirst("\\,", ".").replaceAll("\\*", "");
				ps.setInt(3, (int) (Float.valueOf(cijenaBroj) * 100.0f));

				int inserted = ps.executeUpdate();
				if (inserted != 1)
					Logger.warn("Nije uspjelo zapisivanje novog iso artikla"
							+ iso + ":" + artikl + ":" + cijena, null);
			}
		} catch (NumberFormatException nfe) {
			System.out.println("Problem sa brojem:" + cijenaBroj);
		} catch (IOException ioe) {
		} catch (SQLException e) {

		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (SQLException sqle) {
			}
			try {
				DAOFactory.freeConnection(con);
			} catch (SQLException sqle) {
			}
		}

	}

	String insArt = "insert into artikli (sifra,naziv,porezna_stopa,status,ocno_pomagalo,po_cijeni) values"
			+ " (?,?,2,'U','D',?)";

	private void prebaciNoveOptickeArtikle() {
		String zip = "biz/sunce/optika/zakrpe/data/novi_artikli_ISO.zip";
		String file = "novi_artikli_ISO.csv";

		ResourceLoader loader = new ResourceLoader();

		Connection con = null;
		PreparedStatement ps = null;

		InputStream ins = loader.getCsvInputStreamFromZip(zip, file);

		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		String line;
		String[] zapis;
		String cijenaBroj = null;
		try {
			con = DAOFactory.getConnection();
			ps = con.prepareStatement(insArt);

			String iso, artikl, cijena;
			while ((line = br.readLine()) != null) {
				zapis = line.split(";");
				iso = zapis[1];
				artikl = zapis[5];
				cijena = zapis[16];
				ps.setString(1, iso);
				ps.setString(2, artikl);
				cijenaBroj = cijena.replaceFirst("\\.", "")
						.replaceFirst("\\,", ".").replaceAll("\\*", "");
				ps.setInt(3, (int) (Float.valueOf(cijenaBroj) * 100.0f));

				int inserted = ps.executeUpdate();

				if (inserted != 1)
					Logger.warn("Nije uspjelo zapisivanje novog iso artikla"
							+ iso + ":" + artikl + ":" + cijena, null);
			}
		} catch (NumberFormatException nfe) {
			System.out.println("Problem sa brojem:" + cijenaBroj);
		} catch (IOException ioe) {
		} catch (SQLException e) {

		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (SQLException sqle) {
			}
			try {
				DAOFactory.freeConnection(con);
			} catch (SQLException sqle) {
			}
		}

	}// prebaciNoveArtikle

	private void srediKontrolneBrojeve() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String upit = "select sifra, broj_potvrde_lijecnika from racuni where broj_potvrde_lijecnika is not null";

		int sifra;
		String brojPotvrde;

		try {
			con = DAOFactory.getConnection();
			stmt = con.createStatement();

			rs = stmt.executeQuery(upit);
			int upd = 0;
			int cnt = 0;
			while (rs.next()) {
				sifra = rs.getInt(1);
				brojPotvrde = rs.getString(2);
				cnt++;

				if (brojPotvrde != null && brojPotvrde.length() == 14) {
					String orig = brojPotvrde.charAt(brojPotvrde.length() - 1)
							+ "";
					brojPotvrde = brojPotvrde.substring(0,
							brojPotvrde.length() - 1);
					int kontrolni = Util
							.izracunajKontrolniBrojPotvrdeLijecnika(brojPotvrde);
					if (!("" + kontrolni).equals(orig)) {
						brojPotvrde += kontrolni;
						upit = "update racuni set broj_potvrde_lijecnika='"
								+ brojPotvrde + "' where sifra=" + sifra;
						upd += stmt.executeUpdate(upit);
					}
				}
			}// while
			Logger.log("Ukupno izmjenjeno " + upd
					+ " brojeva potvrda lijeènika od prisutnih " + cnt);
		} catch (Exception e) {
			Logger.fatal(
					"Opæenita iznimka kod zakrpe - srediKontrolneBrojeve: "
							+ zakrpa, e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException sqle) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException sqle) {
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
			}
		}

	}// srediKB

}// klasa
