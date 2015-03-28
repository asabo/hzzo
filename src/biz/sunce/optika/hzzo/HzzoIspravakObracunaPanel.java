/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.HzzoObracunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.opticar.vo.HzzoObracunVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Konstante;
import biz.sunce.optika.Logger;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.RacuniUtil;
import biz.sunce.util.StringUtils;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.05.14
 * 
 * @author asabo
 * 
 */
public final class HzzoIspravakObracunaPanel extends JPanel implements
		SlusacModelaTablice {

	private static final long serialVersionUID = -1002244219440844632L;
	private javax.swing.JLabel jLabel1 = null; // @jve:visual-info decl-index=0
												// visual-constraint="239,237"
	private javax.swing.JLabel jlIspravakObracuna = null;
	private javax.swing.JLabel jlKreiranDana = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private JXTable racuni = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jlBrojOznacenihRacuna = null;
	private javax.swing.JButton jbKreirajDisketu = null;
	private javax.swing.JButton jbIspisiDopis = null;
	private JXTable tRacuni = null;
	private TableModel model = null;
	private Hashtable<String,String> oznaceniRacuni = new Hashtable<String,String>();

	private HzzoObracunVO oznaceniObracun = null;
	private javax.swing.JLabel jLabel3 = null;

	/**
	 * This is the default constructor
	 */
	public HzzoIspravakObracunaPanel() {
		super();
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 2;
		consGridBagConstraints1.gridwidth = 2;
		consGridBagConstraints13.gridy = 4;
		consGridBagConstraints13.gridx = 3;
		consGridBagConstraints12.gridy = 4;
		consGridBagConstraints12.gridx = 2;
		consGridBagConstraints8.gridy = 2;
		consGridBagConstraints8.gridx = 0;
		consGridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints9.weighty = 1.0;
		consGridBagConstraints9.weightx = 1.0;
		consGridBagConstraints9.gridy = 3;
		consGridBagConstraints9.gridx = 0;
		consGridBagConstraints9.gridwidth = 4;
		consGridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
		consGridBagConstraints11.gridy = 4;
		consGridBagConstraints11.gridx = 1;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
		consGridBagConstraints10.gridy = 4;
		consGridBagConstraints10.gridx = 0;
		consGridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHEAST;
		consGridBagConstraints7.gridy = 1;
		consGridBagConstraints7.gridx = 1;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints5.gridy = 1;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints6.gridy = 0;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints6.gridwidth = 3;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel1(), consGridBagConstraints5);
		this.add(getJlIspravakObracuna(), consGridBagConstraints6);
		this.add(getJlKreiranDana(), consGridBagConstraints7);
		this.add(getJLabel(), consGridBagConstraints8);
		this.add(getJScrollPane(), consGridBagConstraints9);
		this.add(getJLabel2(), consGridBagConstraints10);
		this.add(getJlBrojOznacenihRacuna(), consGridBagConstraints11);
		this.add(getJbKreirajDisketu(), consGridBagConstraints12);
		this.add(getJbIspisiDopis(), consGridBagConstraints13);
		this.add(getJLabel3(), consGridBagConstraints1);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(790*faktor, 574*faktor);
		this.setToolTipText("Ispravak obra�una... ");
		this.setPreferredSize(new java.awt.Dimension(790*faktor, 574*faktor));
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Kreiran datuma: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jlIspravakObracuna
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlIspravakObracuna() {
		if (jlIspravakObracuna == null) {
			jlIspravakObracuna = new javax.swing.JLabel();
			jlIspravakObracuna.setText("Ispravak obra�una");
			jlIspravakObracuna.setFont(new java.awt.Font("Dialog",
					java.awt.Font.BOLD, 24));
		}
		return jlIspravakObracuna;
	}

	/**
	 * This method initializes jlKreiranDana
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlKreiranDana() {
		if (jlKreiranDana == null) {
			jlKreiranDana = new javax.swing.JLabel();
			jlKreiranDana.setText("1.1.1");
			jlKreiranDana.setToolTipText("datum kada je obra�un kreiran");
		}
		return jlKreiranDana;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Izdani HZZO ra�uni: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes racuni
	 * 
	 * @return javax.swing.JTable
	 */
	public JXTable getRacuni() {
		if (racuni == null) {
			//racuni = new JXTable();
			//racuni.setToolTipText("");
			if (true) {
				this.tRacuni = new JXTable();
				this.model = new TableModel(DAOFactory.getInstance()
						.getRacuni(), this.tRacuni);
				this.tRacuni.setModel(this.model);
				this.model.dodajSlusaca(this);
				this.racuni = this.tRacuni;
				this.tRacuni
						.setToolTipText("ra�uni sa obojanom pozadinom pojavit �e se u ispravljenom obra�unu. Sustav je ozna�io neke za koje 'pretpostavlja' da bi trebali biti ozna�eni");
			}
		}
		return racuni;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getRacuni());
			jScrollPane.setMinimumSize(new java.awt.Dimension(500, 300));
			jScrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
			jScrollPane
					.setToolTipText("ra�uni sa obojanom pozadinom pojavit �e se u ispravljenom obra�unu. Sustav je ozna�io neke za koje 'pretpostavlja' da bi trebali biti ozna�eni");
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Broj ozna�enih ra�una: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jlBrojOznacenihRacuna
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlBrojOznacenihRacuna() {
		if (jlBrojOznacenihRacuna == null) {
			jlBrojOznacenihRacuna = new javax.swing.JLabel();
			jlBrojOznacenihRacuna.setText("0");
		}
		return jlBrojOznacenihRacuna;
	}

	/**
	 * This method initializes jbKreirajDisketu
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbKreirajDisketu() {
		if (jbKreirajDisketu == null) {
			jbKreirajDisketu = new javax.swing.JButton();
			jbKreirajDisketu.setText("Kreiraj disketu");
			jbKreirajDisketu.setMnemonic(java.awt.event.KeyEvent.VK_K);
			jbKreirajDisketu
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							kreirajDisketuIspravljenihRacuna();
						}
					});
		}
		return jbKreirajDisketu;
	}

	private void kreirajDisketuIspravljenihRacuna() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setToolTipText("Molimo, odaberite mjesto gdje �e se pohraniti datoteka");

		String dir = GlavniFrame.getDirektorijZaPohranuObracuna();
		jfc.setCurrentDirectory(new java.io.File(dir));

		String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();

		int rez = jfc.showDialog(this, "Pohrani");
		
		ArrayList l = new ArrayList(), svi = null;
		svi = (ArrayList) this.model.getData();

		int[] reci = this.tRacuni.getSelectedRows();
		for (int i = 0; i < reci.length; i++) {
			l.add(svi.get(reci[i]));
		} // for i

		if (rez == JFileChooser.APPROVE_OPTION) {
			PostavkeBean postavke = new PostavkeBean();

			HzzoKreiranjeObracuna kr = new HzzoKreiranjeObracuna();
			kr.setVisible(false);
			kr.setObracun(this.oznaceniObracun);

			String imeDatoteke = kr.kreirajImeDatoteke();

			String datoteka = jfc.getSelectedFile().toString()
					+ System.getProperty("file.separator") + imeDatoteke;

			GlavniFrame.setDirektorijZaPohranuObracuna(jfc.getSelectedFile()
					.toString());

			BufferedWriter out = null;
			// ArrayList vpom=this.getVrstePomagala();

			try {

				StavkaRacunaDAO stavkeDAO = DAOFactory.getInstance()
						.getStavkeRacuna();

				int total = 0, totalNaTeretHzzo = 0;
				StavkaRacunaVO svo = null;

				// otvaranje datoteke kad znamo da cemo u nju nesto i pisati...
				out = new BufferedWriter(new FileWriter(datoteka));

				int listaSize = l.size();
				for (int i = 0; i < listaSize; i++) {
					RacunVO rvo = (RacunVO) l.get(i);
					String d = HzzoKreiranjeObracuna.HZZO_DELIMITER; // da ne
																		// pisemo
																		// ispod
																		// veliki
																		// naziv

					// konvertiramo naziv tvrtke iz kodne stranice u kojoj vec
					// korisnik radi u HZZO-ovu ISO-8859-2 kodnu stranicu
					String tvrtkaNaziv = new String(postavke.getTvrtkaNaziv()
							.replaceAll("\\:", ";").trim()
							.getBytes(HzzoKreiranjeObracuna.HZZO_CHARSET));

					String osobniRacunZaDopunsko = rvo
							.getBrojOsobnogRacunaDopunsko();

					if (osobniRacunZaDopunsko == null) {
						osobniRacunZaDopunsko = "";
					}

					String osobniRacunZaOsnovno = rvo
							.getBrojOsobnogRacunaOsnovno();

					if (osobniRacunZaOsnovno == null) {
						osobniRacunZaOsnovno = "";
					}

					boolean osnovno = rvo.getOsnovnoOsiguranje().booleanValue();

					// 12.12.06. -asabo- dodatna provjera jeli racun za
					// dopunsko, ako jest, onda ne bi mogao imati nista kao broj
					// osobnog racuna za dop. osiguranje
					if (!osnovno && osobniRacunZaDopunsko.trim().equals("")) {
						GlavniFrame
								.alert("Ra�un osobnog br. za osn. osiguranje: '"
										+ osobniRacunZaOsnovno
										+ "' jest ra�un i za dopunsko osiguranje,\n a nema broja osobnog ra�una za dopunsko osiguranje! (Ispravak)");
						Logger.warn(
								"Ra�un osobnog br. za osn. osiguranje: '"
										+ osobniRacunZaOsnovno
										+ "' jest ra�un i za dopunsko osiguranje,\n a nema broja osobnog ra�una za dopunsko osiguranje! (Ispravak diskete)",
								null);
					}// if racun za dop nema broj osobnog racuna

					String brojPoliceDopunsko = !osnovno
							&& rvo.getBrojPoliceDopunsko() != null ? rvo
							.getBrojPoliceDopunsko().trim() : "";

					// dodatni osigurac, tesko da se moze dogoditi, da je racun
					// za osnovno, a da ima broj rac za dop.
					if (osnovno)
						osobniRacunZaDopunsko = "";

					int ukupniIznosPomagala = 0; // ukupni iznos racuna u nasem
													// slucaju
					int ukupniIznosPoreza = 0;
					PoreznaStopaVO stopa;

					ArrayList<StavkaRacunaVO> stavke = (ArrayList<StavkaRacunaVO>) stavkeDAO.findAll(rvo);

					int stavkeSize = stavke.size();
					for (int j = 0; j < stavkeSize; j++) {
						svo = stavke.get(j);

						int totalBezPDVa = RacuniUtil.getNettoIznosStavke(svo);
						int totalStavka = RacuniUtil.getBruttoIznosStavke(svo);

						int porez = totalStavka - totalBezPDVa;
						int vrijednost = totalStavka;

						ukupniIznosPomagala += vrijednost;
						ukupniIznosPoreza += porez;
					} // for j

					int ukKn = (ukupniIznosPomagala / 100);
					int ukLp = ukupniIznosPomagala % 100;

					// prikaz tocno u formatu u kojem hoce...
					String sUkupno = ukKn + "."
							+ (ukLp < 10 ? "0" + ukLp : "" + ukLp);

					int sudj = rvo.getIznosSudjelovanja()==null? 0 : rvo.getIznosSudjelovanja().intValue();
					int teretDopunsko = 0;
					int teretOsnovno = ukupniIznosPomagala - sudj;

					// ako je dopunsko osiguranje, iznos sudjelovanja ide u
					// drugu varijablu
					if (!rvo.getOsnovnoOsiguranje().booleanValue()) {
						teretDopunsko = sudj;
						sudj = 0;
					} else { // ako je dopunsko
						teretDopunsko = 0; // da smo sigurni
					}

					int sKn = sudj / 100;
					int sLp = sudj % 100;

					String sSudjelovanje = sKn + "."
							+ (sLp < 10 ? "0" + sLp : "" + sLp);

					sKn = teretDopunsko / 100;
					sLp = teretDopunsko % 100;
					String sTeretDopunsko = sKn + "."
							+ (sLp < 10 ? "0" + sLp : "" + sLp);

					// iznos sudjelovanja u odnosu na ukupni iznos nam treba da
					// izracunamo koliko poreza ide unutar sudjelovanja i
					// ostalog iznosa
					double omjer = (double) ((double) (sudj > teretDopunsko ? sudj
							: teretDopunsko) / (double) ukupniIznosPomagala);
					int porezSudjelovanja = (int) (double) (ukupniIznosPoreza
							* omjer + 0.5d);

					int porezOsnovnoOsiguranje = ukupniIznosPoreza
							- porezSudjelovanja; // porez koji je platio hzzo
					sKn = porezOsnovnoOsiguranje / 100;
					sLp = porezOsnovnoOsiguranje % 100;
					String sPorezOsnovno = sKn + "."
							+ (sLp < 10 ? "0" + sLp : "" + sLp);

					// jeli sudjelovanje ide na teret dopunskog znamo po
					// teretDopunsko>0 iznosu
					int porezDopunsko = teretDopunsko > 0 ? porezSudjelovanja
							: 0;

					sKn = porezDopunsko / 100;
					sLp = porezDopunsko % 100;
					String sPorezDopunsko = sKn + "."
							+ (sLp < 10 ? "0" + sLp : "" + sLp);

					sKn = teretOsnovno / 100;
					sLp = teretOsnovno % 100;
					String sTeretOsnovno = sKn + "."
							+ (sLp < 10 ? "0" + sLp : "" + sLp);

					String brojPotvrdeHzzo;

					if (rvo.getBrojPotvrde2() != null
							&& rvo.getBrojPotvrde2().length() > 3)
						brojPotvrdeHzzo = rvo.getBrojPotvrde1()
								+ "/"
								+ rvo.getBrojPotvrde2()
								+ (rvo.getOsnovnoOsiguranje().booleanValue() ? ""
										: "");
					else
						brojPotvrdeHzzo = "";

					String brojIskaznice1 = rvo.getBrojIskaznice1() != null ? rvo
							.getBrojIskaznice1() : "";
					String brojIskaznice2 = rvo.getBrojIskaznice2() != null ? rvo
							.getBrojIskaznice2() : "";
					Integer sifDrzave = rvo.getSifDrzave(); // ako je null,
															// osoba nije
															// stranac...
					String inoBroj1 = "", inoBroj2 = "";
					DrzavaVO drzava = null;
					String sifProizvodjaca = rvo.getSifProizvodjaca() != null ? rvo
							.getSifProizvodjaca().trim() : "";
					 
					// po sifri drzave znamo jeli racun za stranca ili
					// domaceg... sifDrzave je null kod domacih
					if (sifDrzave != null) {
						inoBroj1 = rvo.getBrojInoBolesnickogLista1();
						inoBroj2 = rvo.getBrojInoBolesnickogLista2();
						drzava = (DrzavaVO) DAOFactory.getInstance()
								.getDrzava().read(sifDrzave);
						if (drzava == null) {
							Logger.log("Iako postoji dr�ava u ra�unu br."
									+ rvo.getSifra().intValue()
									+ " sa �ifrom: " + sifDrzave.intValue()
									+ " ne postoji adekvatna dr�ava objekt?!?");
							alert("Nastao je problem pri kreiranju podataka na disketi. Provjerite poruke sustava i kontaktirajte administratora!");
							return;
						}// if drzava==null
					} // if sifDrzave nije null

					// trenutno radimo ili ortopedska ili ocna pomagala samo
					String vrstaPomagala = ""
							+ rvo.getVrstaPomagala().intValue();

					String sifraPotvrdeLijecnika = rvo.getBrojPotvrdePomagala() != null ? rvo
							.getBrojPotvrdePomagala() : "";
					String aktivnostDop = osnovno ? "" : rvo.getAktivnostDop();
					String aktivnostZZR = rvo.getAktivnostZZR();

					final String rac = "60"
							+ d
							+ sifIsporucitelja
							+ d
							+ tvrtkaNaziv
							+ d
							+ brojPotvrdeHzzo
							+ d
							+ Util.convertCalendarToString(rvo
									.getDatumIzdavanja())
							+ d
							+ vrstaPomagala
							+ d
							+ Util.convertCalendarToString(rvo
									.getDatumNarudzbe())
							+ d
							+ osobniRacunZaDopunsko
							+ d
							+ brojPoliceDopunsko
							+ d
							+ sUkupno
							+ d
							+ sSudjelovanje
							+ d
							+ sTeretDopunsko
							+ d
							+ sPorezDopunsko
							+ d
							+ sTeretOsnovno
							+ d
							+ sPorezOsnovno
							+ d
							+ brojIskaznice2
							+ d
							+ brojIskaznice1
							+ d
							+ ""
							+ d
							+ (sifDrzave == null ? "" : 
								( StringUtils.isEmpty(inoBroj1)?inoBroj2:(inoBroj1 + "/" + inoBroj2) )
							  ) + d
							+ (sifDrzave == null ? "" : drzava.getCc3()) + d
							+ sifProizvodjaca + d + "0.00" + d + "0.00" + d
							+ osobniRacunZaOsnovno + d
							+ (aktivnostZZR == null ? "" : aktivnostZZR) + d
							+ (aktivnostDop == null ? "" : aktivnostDop) + ""
							+ d + sifraPotvrdeLijecnika + d;

					out.write(rac + "\r\n"); // obavezno u ovom formatu mora
												// biti znak za novi red...

					String datObracuna = Util.convertCalendarToString(rvo
							.getDatumIzdavanja());
					Short sifraVelicineObloge;

					for (int j = 0; j < stavkeSize; j++) {
						svo =   stavke.get(j);
						int vrijednost = RacuniUtil.getBruttoIznosStavke(svo);

						sKn = vrijednost / 100;
						sLp = vrijednost % 100;

						sifraVelicineObloge = svo.getSifraVelicineObloge();

						String sifArt = svo.getSifArtikla();
						boolean isoArt = sifArt != null
								&& sifArt.length() == 12
								&& StringUtils.imaSamoBrojeve(sifArt);

						// 05.03.07. -asabo- dodano
						String sfp = (svo.getSifProizvodjaca() != null ? ""
								+ svo.getSifProizvodjaca().intValue() : "");

						if (!isoArt && sfp.equals("")) {
							Logger.log("stavka " + svo.getSifArtikla()
									+ " u racunu br. "
									+ rvo.getBrojOsobnogRacunaOsnovno()
									+ " nema �ifre proizvo�a�a!");
							GlavniFrame
									.alert("stavka "
											+ svo.getSifArtikla()
											+ " u racunu br. "
											+ rvo.getBrojOsobnogRacunaOsnovno()
											+ " nema �ifre proizvo�a�a!\nPopravite to pa ponovno kreirajte disketu!");
						}

						final String sIznos = sKn + "."
								+ (sLp < 10 ? "0" + sLp : "" + sLp);

						final String stavka = "61"
								+ d
								+ brojPotvrdeHzzo
								+ d
								+ datObracuna
								+ d
								+ svo.getSifArtikla()
								+ d
								+ svo.getKolicina().intValue()
								+ ".00"
								+ d
								+ sIznos
								+ d
								+ sfp
								+ d
								+ sifraPotvrdeLijecnika
								+ d
								+ (sifraVelicineObloge == null ? ""
										: sifraVelicineObloge.toString()) + d;

						out.write(stavka + "\r\n"); // obavezno u ovom formatu
													// mora biti znak za novi
													// red...
					} // for j
				} // for i
				PostavkeBean p = new PostavkeBean();
				String sSif = "";
				if (kr.pocSifra < 10) {
					sSif = "00" + kr.pocSifra;
				} else if (kr.pocSifra < 100) {
					sSif = "0" + kr.pocSifra;
				} else {
					sSif = "" + kr.pocSifra;
				}

				try {
					if (out != null)
						out.close();
					out = null;
				} catch (IOException ioe) {
				}

				alert("Ne zaboravite na disketu napisati: " + imeDatoteke
						+ "\n �ifru isporu�itelja: "
						+ PostavkeBean.getHzzoSifraIsporucitelja()
						+ "\n naziv tvrtke: " + p.getTvrtkaNaziv()
						+ "\n i redni broj diskete: " + sSif + "/"
						+ this.oznaceniObracun.getDatum().get(Calendar.YEAR));
			} catch (IOException e) {
				Logger.log(
						"IO iznimka kod poku�aja otvaranja datoteke za kreiranje ispravljenog hzzo obra�una: dat:"
								+ datoteka, e);
				alert("Problem pri poku�aju kreiranja datoteke. Provjerite poruke sustava");
			} catch (SQLException sqle) {
				Logger.fatal(
						"SQL iznimka kod tra�enja ra�una za kreiranje diskete (ispravak obra�una)",
						sqle);
				alert("Problem pri poku�aju pristupanja podacima. Provjerite poruke sustava i kontaktirajte administratora");
			} finally {
				try {
					if (out != null)
						out.close();
					out = null;
				} catch (IOException ioe) {
				}
			} // finally

		} // if

	}// kreirajDisketuIspravljenihRacuna

	/**
	 * This method initializes jbIspisiDopis
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIspisiDopis() {
		if (jbIspisiDopis == null) {
			jbIspisiDopis = new javax.swing.JButton();
			jbIspisiDopis.setText("Ispi�i dopis");
			jbIspisiDopis
					.setToolTipText("kreiraj dopis sa spedcifikacjiom izmjenjenih ra�una");
			jbIspisiDopis.setMnemonic(java.awt.event.KeyEvent.VK_I);
			jbIspisiDopis
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							ispisiDopisIspravljenihRacuna();
						}
					});
		}
		return jbIspisiDopis;
	}

	private void ispisiDopisIspravljenihRacuna() {

		HtmlPrintParser parser = new HtmlPrintParser();
		PostavkeBean postavke = new PostavkeBean();

		HzzoKreiranjeObracuna kr = new HzzoKreiranjeObracuna();
		kr.setVisible(false);
		kr.setObracun(this.oznaceniObracun);

		DjelatnikVO dvo = null;
		try {
			dvo = (DjelatnikVO) DAOFactory.getInstance().getDjelatnici()
					.read(this.oznaceniObracun.getCreatedBy());
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod tra�enja djelatnika za obra�un br."
					+ this.oznaceniObracun.getSifra(), e);
		}

		String ispis = parser
				.ucitajHtmlPredlozak(Konstante.PREDLOZAK_HZZO_OBRACUN_ISPRAVAK);

		if (ispis == null) {
			Logger.warn("Nema predlo�ka datoteke: "
					+ Konstante.PREDLOZAK_HZZO_OBRACUN_ISPRAVAK, null);
			alert("Problem sa ispisom predlo�ka. Provjerite poruke sustava!");
		}

		ispis = ispis.replaceFirst("<!--broj_obracuna-->", ""
				+ this.oznaceniObracun.getSifra().intValue());

		ispis = ispis.replaceFirst("<!--mjesto-->", postavke.getMjestoRada());

		ispis = ispis.replaceFirst("<!--datum-->",
				Util.convertCalendarToString(this.oznaceniObracun.getDatum()));
		ispis = ispis.replaceFirst("<!--datum_obracuna-->",
				Util.convertCalendarToString(this.oznaceniObracun.getDatum()));

		ispis = ispis.replaceFirst("<!--labela_diskete-->",
				kr.kreirajImeDatoteke());

		ispis = ispis
				.replaceFirst(
						"<!--djelatnik-->",
						""
								+ (dvo != null ? dvo.getIme() + " "
										+ dvo.getPrezime() : ""));

		ispis = ispis.replaceFirst("<!--racuni_specifikacija-->",
				kreirajSpecifikacijuZaIzmjenjeniDopis());

		MjestoVO mvo = null;

		try {
			mvo = (MjestoVO) DAOFactory.getInstance().getMjesta()
					.read(this.oznaceniObracun.getSifPodruznice());
		} catch (SQLException e1) {
			Logger.fatal("SQL iznimka kod trazenja mjesta za ispis dopisa", e1);
			alert("Nastao je problem pri kreiranju dopisa. Provjerite poruke sustava i kontaktirajte administratora. Dopis ce biti ispisan!");
		}

		String hzzo_podaci = kr.iskreirajHzzoAdresuHTML(mvo);

		ispis = ispis.replaceFirst("<!--hzzo_podaci-->", hzzo_podaci);

		HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis,
				"ispravljeni_obracun_" + mvo.getNaziv() + "_"
						+ this.oznaceniObracun.getId());
	} //

	private String kreirajSpecifikacijuZaIzmjenjeniDopis() {
		int[] reci = this.tRacuni.getSelectedRows();
		ArrayList listaRacuna = new ArrayList(reci.length);
		ArrayList svi = null;
		svi = (ArrayList) this.model.getData();

		for (int i = 0; i < reci.length; i++) {
			listaRacuna.add(svi.get(reci[i]));
		} // for i
		Integer sfo = this.oznaceniObracun.getSifOsiguranja();

		return HzzoKreiranjeObracuna.kreirajSpecifikacijuZaDopis(listaRacuna,
				sfo);
	} // kreirajSpecifikacijuZaIzmjenjeniDopis

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		int retci = this.tRacuni.getSelectedRows().length;
		this.jlBrojOznacenihRacuna.setText("" + retci);
	}

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	public HzzoObracunVO getOznaceniObracun() {
		return oznaceniObracun;
	}

	public void setOznaceniObracun(HzzoObracunVO obracunVO) {
		oznaceniObracun = obracunVO;
		if (oznaceniObracun != null) {
			this.jlKreiranDana.setText(biz.sunce.util.Util
					.convertCalendarToString(this.oznaceniObracun.getDatum())
					+ " ");

			this.napuniRacuneUTablicu();
			this.oznaciPotencijalneRacuneZaObracun();
		}
	}

	private void napuniRacuneUTablicu() {
		ArrayList l = null;
		SearchCriteria krit = new SearchCriteria();
		krit.setKriterij(HzzoObracunDAO.KRITERIJ_SVI_OBRACUNI_ZA_PODRUZNICU_HZZO);

		ArrayList li = new ArrayList(2);

		li.add(this.oznaceniObracun.getDatum());
		li.add(this.oznaceniObracun.getSifPodruznice());
		krit.setPodaci(li);

		// prvo trazimo postoji li kakav prethodni obracun za donju granicu
		try {
			l = (ArrayList) DAOFactory.getInstance().getHzzoObracuni()
					.findAll(krit);
		} catch (SQLException e1) {
			Logger.fatal(
					"SQL Iznimka kod tra�enja prethodnog obra�una (ispravak obra�una)",
					e1);
			alert("Nastao je problem pri pripremanju podataka za dati obra�un. Molimo provjerite poruke sustava!");
		}

		li.clear();
		li = null;

		Calendar datumOd = null;

		// gledamo kad je bio prethodni obracun (ako je bio)
		if (l != null && l.size() > 0) {
			HzzoObracunVO zadnjiObracun = (HzzoObracunVO) l.get(0);
			datumOd = zadnjiObracun.getDatum();
		} // if

		// sad mi daj sve racune koji spadaju pod nas obracun
		SearchCriteria kriterij = new SearchCriteria();
		kriterij.setKriterij(HzzoObracunDAO.KRITERIJ_SVI_RACUNI_ZA_OBRACUN);
		l = new ArrayList(4);
		l.add(datumOd); // bio null, ne bio...
		l.add(this.oznaceniObracun.getDatum()); // datum ovog obracuna je
												// zavrsni dan koji se ukljucuje
												// u obracun
		l.add(this.oznaceniObracun.getSifPodruznice()); // za koju podruznicu
														// obracun ide
		l.add(this.oznaceniObracun.getSifOsiguranja());
		kriterij.setPodaci((List) l);

		try {
			// model ce se sam pobrinuti potraziti podatke za kriterij i
			// ispisati ih na ekran
			this.model.setFilter(kriterij);
			// l=(ArrayList)
			// DAOFactory.getInstance().getRacuni().findAll(kriterij);
		} catch (Exception e) {
			Logger.fatal(
					"SQL iznimka kod povla�enja svih ra�una datog obra�una", e);
			alert("Nastao je problem pri prikupljanju ra�una koji potpadaju pod ovaj obra�un. Provjerite poruke sustava!");
		}

	} // napuniRacuneUTablicu

	// gleda koji bi sve racuni mogli biti na popisu i oznacava ih
	private void oznaciPotencijalneRacuneZaObracun() {
		// kupimo racune koji su 'taknuti' jucer ili danas
		Calendar danas = Calendar.getInstance();
		Calendar jucer = Calendar.getInstance();
		jucer.setLenient(true);
		jucer.set(Calendar.DAY_OF_MONTH, jucer.get(Calendar.DAY_OF_MONTH) - 1);
		jucer.setLenient(false);

		this.tRacuni
				.removeRowSelectionInterval(0, this.model.getRowCount() - 1);

		ArrayList l = (ArrayList) this.model.getData();

		Calendar cr = Calendar.getInstance();
		Calendar up = Calendar.getInstance();

		if (l != null) {
			int lsize = l.size();
			for (int i = 0; i < lsize; i++) {
				RacunVO rvo = (RacunVO) l.get(i);

				cr.setTimeInMillis(rvo.getCreated());
				up.setTimeInMillis(rvo.getLastUpdated());

				if ((cr.equals(jucer) || cr.after(jucer) || up.equals(jucer) || up
						.after(jucer))
						&& (cr.equals(danas) || cr.before(danas)
								|| up.equals(danas) || up.before(danas))) {
					oznaciRedak(i);
				}
			}
		}

		int retci = this.tRacuni.getSelectedRows().length;
		this.jlBrojOznacenihRacuna.setText("" + retci);

	} // oznaciPotencijalneRacuneZaObracun

	private void oznaciRedak(int redak) {
		if (!this.oznaceniRacuni.containsKey("" + redak)) {
			this.oznaceniRacuni.put("" + redak, "DA");
			this.tRacuni.addRowSelectionInterval(redak, redak);
		} else if (this.oznaceniRacuni.containsKey("" + redak)) {
			this.oznaceniRacuni.remove("" + redak);
			this.tRacuni.removeRowSelectionInterval(redak, redak);
		}
	}

	private void alert(String poruka) {
		JOptionPane.showMessageDialog(this, poruka, "Upozorenje!",
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("CTRL tipku dr�ite pritisnutu i klikanjem ozna�avajte ili odzna�avajte ra�une ...");
		}
		return jLabel3;
	}
}
