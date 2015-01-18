/*
 * Project opticari
 *
 */
package biz.sunce.optika.zakrpe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.csc.SynchModul;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.StringUtils;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

import com.ansa.util.ResourceLoader;

/**
 * datum:2006.03.20
 * 
 * @author asabo zakrpa koja kreira tablicu sa adresama podruznica hzzo-a i
 *         dodaje cijenu artiklima
 */
public final class Zakrpe
{	
	private static final String NOVE_CIJENE_01_01_2013 = "nove_cijene_01_01_2013";
	private static final String NOVE_CIJENE_01_01_2015 = "nove_cijene_01_01_2015";

	String[] upiti = null;
	private static final String verzijaZakrpe = "038";
	private static final String poruka = "ažurirane cijene optièkih pomagala od 01.01.15.";
	private static final String zakrpa = "zakrpa" + verzijaZakrpe;

	public Zakrpe() {
		upiti = new String[19];

		upiti[0] = "alter table racuni alter column broj_potvrde2 set data type varchar(10)";
		upiti[1] = "alter table stavke_racuna drop constraint str_sifart";
		upiti[2] = "alter table artikli alter column sifra set data type varchar(12)";
		upiti[3] = "alter table stavke_racuna alter column sif_artikla set data type varchar(12)";
		upiti[4] = "alter table stavke_racuna add constraint STR_SIFART foreign key (sif_artikla) references artikli(sifra)";
		upiti[5] = "alter table artikli add column rokdo7g integer default null";
		upiti[6] = "alter table artikli add column rokdo18g integer default null";
		upiti[7] = "alter table artikli add column rok integer default null";
		upiti[8] = "update porezne_stope set stopa=25 where stopa=22";
		upiti[9] = "alter table klijenti alter column datrodjenja null";
		upiti[10] = "update artikli set rokdo7g=12,rokdo18g=24,rok=36 where sifra in('210306140101','210306140102','BNOS013','BNOS021')";
		upiti[11] = "create index i_racuni_status on racuni (status asc)";
		upiti[12] = "update artikli set status='D' where length(sifra)=7";
		upiti[13] = "INSERT INTO POREZNE_STOPE (SIFRA,NAZIV,STOPA) VALUES (12,'PDV 5% - ortopedska pomagala',5)";
		upiti[14] = "UPDATE porezne_stope SET NAZIV='PDV 25%' WHERE sifra=1";
		upiti[15] = "alter TABLE artikli add column CREATED timestamp default current_timestamp NOT NULL";

		upiti[16] = "alter TABLE artikli add column   CREATED_BY int NOT NULL default 1";

		upiti[17] = "alter TABLE artikli add column   UPDATED timestamp";

		upiti[18] = "alter TABLE artikli add column   UPDATED_BY int";
		
	}// konstruktor

	public String getVerzijaZakrpe() {
		return verzijaZakrpe;
	}

	public boolean zakrpaj() 
	{
		boolean rez = true;

		// srediKontrolneBrojeve();
		
		String zakrpano = PostavkeBean.getPostavkaDB(zakrpa, "nije");

		// zakrpa je vec obavljena? Ako je vracamo nazad poruku kao da smo
		// zakrpali..
		if (zakrpano.equals("nije"))
		{

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
			if (rez) 
			{
				boolean koristiSvaPomagala = GlavniFrame.isKoristiSvaPomagala();
				String isoArtikliKljuc = "iso_artikli_"+(koristiSvaPomagala?"svi":"opt");
				String isoArtikli=PostavkeBean.getPostavkaDB(isoArtikliKljuc, "nije");
				
				if (!koristiSvaPomagala && isoArtikli.equals("nije"))
				{
					prebaciNoveOptickeArtikle();
					PostavkeBean.setPostavkaDB(isoArtikliKljuc, "jest");
				}
				if (koristiSvaPomagala &&
						isoArtikli.equals("nije") ) {
					System.out
							.println("Prebacujemo nove ISO9999 artikle svih pomagala..");
					int komada = prebaciNoveOrtopedskeArtikle();
					System.out.println("Prebacili komada: " + komada);
					PostavkeBean.setPostavkaDB(isoArtikliKljuc, "jest");
				}
				
				Calendar c = Calendar.getInstance();
				String sc = Util.convertCalendarToString(c);
				PostavkeBean.setPostavkaDB(zakrpa, sc);
				JOptionPane.showMessageDialog(GlavniFrame.getInstanca(),
						"Vaš softver je uspješno nadograðen na verziju 1.0.0."
								+ verzijaZakrpe+". "+poruka, "Obavijest",
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
		}//if nije zakrpano
		
		String noveCijeneOdradjene = PostavkeBean.getPostavkaDB(NOVE_CIJENE_01_01_2013, "nije");
		if (noveCijeneOdradjene.equals("nije")){
			prebaciNoveCijene();
		}
		
		String noveCijeneOptikaOdradjene = PostavkeBean.getPostavkaDB(NOVE_CIJENE_01_01_2015, "nije");
		if (noveCijeneOptikaOdradjene.equals("nije")){
			prebaciNoveOptickeArtikle();
		}
		
		return rez;
	}// zakrpaj

	final String insArtPom = "insert into artikli (sifra,naziv,porezna_stopa,status,ocno_pomagalo,po_cijeni,rokdo7g,rokdo18g,rok) values"
			+ " (?,?,2,'U','N',?,?,?,?)";
	
	final String selArt = "select sifra from artikli where sifra=?";

	private int prebaciNoveOrtopedskeArtikle() {
		String zip = "biz/sunce/optika/zakrpe/data/novi_orto_artikli_iso.zip";
		String file = "novi_orto_artikli_iso.csv";

		ResourceLoader loader = new ResourceLoader();

		Connection con = null;
		PreparedStatement ps = null, ps2 = null;

		InputStream ins = loader.getCsvInputStreamFromZip(zip, file);

		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		String line = null;
		String[] zapis;
		String cijenaBroj = null;
		String rbr = null;
		String rok7, rok18, rok;
		int irok7, irok18, irok;
		int brojac = 0;
		ResultSet rs = null;

		try {
			con = DAOFactory.getConnection();
			ps = con.prepareStatement(insArtPom);
			ps2 = con.prepareStatement(selArt);

			String iso, artikl, cijena;
			while ((line = br.readLine()) != null) {
				zapis = line.split(";");
				rbr = zapis[0].trim();
				if (rbr.equals(""))
					continue;

				iso = zapis[1];
				
				if (iso==null || iso.trim().equals("")) continue;
				
				ps2.setString(1, iso);
				rs=ps2.executeQuery();
				if (rs.next()){
					rs.close();
					continue;
				}
				rs.close();
				
				artikl = zapis[5].trim();
				if (artikl.equals(""))
					artikl = zapis[2].trim();

				rok7 = zapis[13];
				rok18 = zapis[14];
				rok = zapis[15];

				irok7 = vratiMjesece(rok7);
				irok18 = vratiMjesece(rok18);
				irok = vratiMjesece(rok);

				cijena = zapis[16];
				ps.setString(1, iso);
				ps.setString(2, artikl);
				cijenaBroj = cijena.replaceFirst("\\.", "")
						.replaceFirst("\\,", ".").replaceAll("\\*", "");
				float cj;
				try{
					cj=Float.valueOf(cijenaBroj);
				}
				catch(NumberFormatException nfe){
					cj=-1;
				}
				if (cj==-1)
				ps.setNull(3, Types.FLOAT);
				else
				ps.setInt(3, (int) (cj * 100.0f));
				
				if (irok7 == -1)
					ps.setNull(4, Types.INTEGER);
				else
					ps.setInt(4, irok7);
				if (irok18 == -1)
					ps.setNull(5, Types.INTEGER);
				else
					ps.setInt(5, irok18);
				if (irok == -1)
					ps.setNull(6, Types.INTEGER);
				else
					ps.setInt(6, irok);

				int inserted = ps.executeUpdate();
				if (inserted != 1)
					Logger.warn("Nije uspjelo zapisivanje novog iso artikla"
							+ iso + ":" + artikl + ":" + cijena, null);
				brojac += inserted;

			}
		} catch (NumberFormatException nfe) {
			System.out.println("Problem sa brojem:" + cijenaBroj);
			Logger.fatal("Problem sa parsirannjem broja " +cijenaBroj, nfe);
		} catch (IOException ioe) {
			Logger.fatal("IO iznimka kod unošenja novog ISO pomagala", ioe);
		} catch (SQLException e) {
			System.out.println("SQLExc:" + e);
			Logger.fatal("SQL iznimka kod unošenja novog ISO pomagala", e);
		} catch (IndexOutOfBoundsException inde) {
			Logger.fatal("IndexOutOfBounds iznimka kod unošenja novog ISO pomagala", inde);
			System.out.println(line);
		}
		catch(Exception e){
			Logger.fatal("Iznimka kod unošenja novog ISO pomagala", e);
		}
		finally {
			try {
				br.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (SQLException sqle) {
			}
			try {
				ps2.close();
			} catch (SQLException sqle) {
			}
			try {
				DAOFactory.freeConnection(con);
			} catch (SQLException sqle) {
			}
		}
		return brojac;
	}

	private int vratiMjesece(String rok) {
		if (rok == null || rok.trim().equals(""))
			return -1;
		
		int faktor = 1;
		if (rok.contains("god"))
			faktor = 12;
		else if (rok.contains("mj"))
			faktor = 1;
		else if (rok.contains("g"))
			faktor = 12;
		else
			return -1;

		rok = StringUtils.makniSlova(rok);
		rok = rok.replaceAll("\\.", "").replaceAll("\\,", ".").trim();
		float irok = -1.0f;
		try {
			irok = Float.parseFloat(rok);
		} catch (NumberFormatException nfe) {
			return -1;
		}
		return (int) (irok * faktor);
	}

	final String insArtOpt = "insert into artikli (sifra,naziv,porezna_stopa,status,ocno_pomagalo,po_cijeni) values"
			+ " (?,?,?,'U','D',?)";

	private void prebaciNoveOptickeArtikle() 
	{
		String zip = "biz/sunce/optika/zakrpe/data/novi_artikli_ISO.zip";
		String file = "novi_artikli_ISO.csv";

		ResourceLoader loader = new ResourceLoader();

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement psUpd = null;
		
		ResultSet rs = null;
		
		InputStream ins = loader.getCsvInputStreamFromZip(zip, file);

		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		String line=null;
		String[] zapis;
		String cijenaBroj = null;
		
		int insertirano=0, updateano=0;
		
		
		try {
			con = DAOFactory.getConnection();

			ps = con.prepareStatement( insArtOpt );
			ps2 = con.prepareStatement( selArt );
			psUpd = con.prepareStatement( updArt );

			String iso, artikl, cijena;
			boolean artiklPostoji = false;
			int poreznaStopa;
			int cijenaInt;
			String poreznaStopaStr;
			
			
			while ((line = br.readLine()) != null) 
			{
				zapis  = line.split(";");
				
				if (zapis.length<18)
					continue;
				
				iso    = zapis[1];
				artikl = zapis[5];
				cijena = zapis[16];
				poreznaStopaStr = zapis[17];
				
				ps2.setString(1, iso);
				rs=ps2.executeQuery();
				
				artiklPostoji=rs.next();
				
				cijenaBroj = cijena.replaceFirst("\\.", "")
						.replaceFirst("\\,", ".").replaceAll("\\*", "");
				
				cijenaInt = (int) (Float.valueOf(cijenaBroj) * 100.0f);
				
				poreznaStopa=12;
				
				if (poreznaStopaStr.equals("5%"))
					poreznaStopa=12;
				else
					if (poreznaStopaStr.equals("25%"))
						poreznaStopa=1;
				 else if (poreznaStopaStr.equals("0%"))
					poreznaStopa=2;
				 else{
					 System.err.println( " Nepoznata porezna stopa!!!" );
				 Logger.log(" nepoznata porezna stopa: "+poreznaStopaStr);
				 }
			 	
				rs.close();	rs=null;
				
				if ( artiklPostoji )
				{
					//naziv necemo mjenjati, nije ni bitan na kraju
					psUpd.setInt(1, poreznaStopa);
					psUpd.setInt(2, cijenaInt);
					psUpd.setString(3, iso);
					
					int updated = psUpd.executeUpdate();

					if (updated != 1)
						Logger.warn("Nije uspjeo update ISO artikla"
								+ iso + ":" + artikl + ":" + cijena, null);
					else
						updateano++;
				}
				else
				{
				ps.setString(1, iso);
				ps.setString(2, artikl);
				
				ps.setInt(3, poreznaStopa);
				
				ps.setInt(4, cijenaInt);

				int inserted = ps.executeUpdate();

				if (inserted != 1)
					Logger.warn("Nije uspjelo zapisivanje novog iso artikla"
							+ iso + ":" + artikl + ":" + cijena, null);
				else 
					insertirano++;
				}
			}//while
		} 
		catch (NumberFormatException nfe) {
			Logger.warn("Problem sa brojem: "+ cijenaBroj, nfe);
			System.out.println("Problem sa brojem:" + cijenaBroj);
		} catch (IOException ioe) {
			Logger.warn("IOException kod prebacivanja artikala", ioe);
		} catch (SQLException e) {
			Logger.warn("SQL iznimka kdo prebacivanja artikala, line: "+line, e);
		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
			try {
				if (ps!=null) ps.close();
			} catch (SQLException sqle) {
			}
			try {
				if (psUpd!=null) psUpd.close();
			} catch (SQLException sqle) {
			}
			try {
				if (ps2!=null) ps2.close();
			} catch (SQLException sqle) {
			}
			try {
				DAOFactory.freeConnection(con);
			} catch (SQLException sqle) {
			}
		}

		GlavniFrame.info("Ažurirali smo optièka pomagala, cjenik od 01.01.2015. Izmjenjenih cijena: "+updateano+" uneseno novih artikala: "+insertirano);

		PostavkeBean.setPostavkaDB(NOVE_CIJENE_01_01_2015, new Date().toString());

	}// prebaciNoveOptickeArtikle

	final String updArt = "update artikli set porezna_stopa=?,po_cijeni=? where sifra=?";
	
	private void prebaciNoveCijene() 
	{
		String zip = "biz/sunce/optika/zakrpe/data/cijene.zip";
		String file = "cijene.dta";

		ResourceLoader loader = new ResourceLoader();

		Connection con = null;
		PreparedStatement ps = null;

		InputStream ins = loader.getCsvInputStreamFromZip(zip, file);

		ObjectInputStream oins =null;
		
		Hashtable<String, PomagaloVO> pomagala=null;

		int izmjenjeno=0;
		PomagaloVO pomagalo=null;
		
		try {
			oins = new ObjectInputStream(ins);			
			pomagala=(Hashtable<String, PomagaloVO>)oins.readObject();
						
			con = DAOFactory.getConnection();
			ps = con.prepareStatement(updArt);
			
			Enumeration<String> keys = pomagala.keys();

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				pomagalo = pomagala.get(key);
				
				ps.setInt(1, pomagalo.getPoreznaSkupina());
				ps.setInt(2, pomagalo.getCijenaSPDVom());
				ps.setString(3, pomagalo.getSifraArtikla());
								
				int updated = ps.executeUpdate();

				izmjenjeno+=updated;
			}
			
			PostavkeBean.setPostavkaDB(NOVE_CIJENE_01_01_2013, new Date().toString());
			
			GlavniFrame.alert("Ukupno izmjenjeno cijena: "+izmjenjeno+"! Unosite li raèune od 2012.g. morat æete ruèno mijenjati cijene! Morat æete ponovno pokrenuti program!");

		}  catch (IOException ioe) {
			Logger.warn("IOException kod prebacivanja prebaciNoveCijene", ioe);
		} catch (SQLException e) {
			Logger.warn("SQL iznimka kod prebaciNoveCijene, pomagalo: "+(pomagalo==null?"NULL":pomagalo.getSifraArtikla()), e);
		} catch (ClassNotFoundException e) {
			Logger.warn("ClassNotFoundException kod  prebaciNoveCijene", e);
		} finally {
			try {
				oins.close(); oins=null;
			} catch (Exception e) {
			}
			try {
				if (ps!=null) ps.close();
			} catch (SQLException sqle) {
			}
			try {
				DAOFactory.freeConnection(con);
			} catch (SQLException sqle) {
			}
		}
		System.exit(0);

	}// prebaciNoveCijene

	
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
