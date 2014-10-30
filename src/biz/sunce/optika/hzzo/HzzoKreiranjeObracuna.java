package biz.sunce.optika.hzzo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.print.PrintService;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.HzzoObracunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.opticar.vo.HzzoObracunVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.optika.hzzo.ispis.IspisRacunaDopunskoOsiguranje;
import biz.sunce.optika.hzzo.ispis.IspisRacunaOsnovnoOsiguranje;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.RacuniUtil;
import biz.sunce.util.StringUtils;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

public final class HzzoKreiranjeObracuna extends JFrame {
	private static final long serialVersionUID = 8356663697758275577L;
	private static HzzoKreiranjeObracuna instanca=null;

	public HzzoKreiranjeObracuna() {
		jContentPane = null;
		jbKreirajDisketu = null;
		jbIspisiDopis = null;
		jbZatvori = null;
		obracun = null;
		obracunEditor = null;
		obracunDao = null;
		izmjeneMoguce = true;
		pocSifra = -1;
	 
		jpObracunEditor = null;
		jbIspravak = null;
		automatskoPunjenje = true;
		
		initialize();
		instanca=this;
	}

	public HzzoKreiranjeObracuna(HzzoObracunVO obr) {
		this();	
		setObracun(obr);
	}
	
	public HzzoKreiranjeObracuna(HzzoObracunVO obr, boolean omoguci) {
	this();
	if (!omoguci)
   	 this.onemoguciIzmjene();
	this.setObracun(obr);
	  
	}

	public void setObracun(HzzoObracunVO obr) {
		obracun = obr;
		obracunEditor.napuniPodatke(obracun);
	}
	
	private void initialize() {
		setSize(350, 200);
		setContentPane(getJContentPane());
		setTitle("HZZO obra\u010Dun");
		this.setIconImage(GlavniFrame.getImageIcon().getImage());
		setDefaultCloseOperation(2);
		setName("kreiranjeHzzoObracuna");
		disableIspravak();
		Thread t = new Thread() {
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				yield();
				pack();

				Date d = PostavkeBean.getDatumValjanosti();
				Date danas = new Date();
				yield();
				if (d != null && d.before(danas)) {
					alert("Licenca za korištenje proizvoda je istekla! Kontaktirajte Vašeg dobavljaèa!");
					dispose();
				}
			}
		};
		
		SwingUtilities.invokeLater(t);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			GridBagConstraints consGridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints consGridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints consGridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints consGridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints consGridBagConstraints31 = new GridBagConstraints();
			GridBagConstraints consGridBagConstraints5 = new GridBagConstraints();
			consGridBagConstraints4.gridy = 0;
			consGridBagConstraints4.gridx = 0;
			consGridBagConstraints4.gridwidth = 2;
			consGridBagConstraints31.gridy = 3;
			consGridBagConstraints31.gridx = 0;
			consGridBagConstraints5.gridy = 0;
			consGridBagConstraints5.gridx = 0;
			consGridBagConstraints5.gridwidth = 2;
			consGridBagConstraints5.fill = 2;
			consGridBagConstraints3.gridy = 3;
			consGridBagConstraints3.gridx = 1;
			consGridBagConstraints2.gridy = 2;
			consGridBagConstraints2.gridx = 0;
			consGridBagConstraints1.gridy = 1;
			consGridBagConstraints1.gridx = 0;
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJbKreirajDisketu(), consGridBagConstraints1);
			jContentPane.add(getJbIspisiDopis(), consGridBagConstraints2);
			jContentPane.add(getJbZatvori(), consGridBagConstraints3);
			jContentPane.add(getJbIspravak(), consGridBagConstraints31);
			jContentPane.add(getJpObracunEditor(), consGridBagConstraints5);
			jContentPane.add(getJbIspisiRacune(), new GridBagConstraints(1, 2,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jContentPane;
	}

	private JButton getJbKreirajDisketu() {
		if (jbKreirajDisketu == null) {
			jbKreirajDisketu = new JButton();
			jbKreirajDisketu.setText("Kreiraj disketu");
			jbKreirajDisketu
					.setToolTipText("ovim pripremate disketu sa svim ra\u010Dunima u zadanom razdoblju");
			jbKreirajDisketu.setMnemonic(75);
			jbKreirajDisketu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					kreirajDisketu();
				}
			});
		}
		return jbKreirajDisketu;
	}

	public int vratiRedniBrojObracunaUGodini() {
		SearchCriteria krit = new SearchCriteria();
		ArrayList l = new ArrayList();
		l.add(obracun.getDatum());
		l.add(obracun.getSifPodruznice());
		int godina = obracun.getDatum().get(1);
		l.add(Integer.valueOf(godina));
		l.add(obracun.getSifOsiguranja());
		krit.setKriterij("krit_svi_obracuni_za_podruznicu_hzzo");
		krit.setPodaci(l);
		try {
			l = (ArrayList) DAOFactory.getInstance().getHzzoObracuni()
					.findAll(krit);
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod trazenja svih obracuna za podruznicu u godini",
					e);
			alert("Nastala je iznimka kod tra\u017Eenja popisa svih obra\u010Duna za podru\u017Enicu. Provjerite poruke sustava!");
			return -1;
		}
		if (l == null) {
			alert("Nastao je problem pri kreiranju diskete. Molimo kontaktirajte administratora sustava");
			Logger.log("Pri pretra\u017Eivanju podataka o ve\u010D obavljenim obra\u010Dunima vratila se null lista");
			return -1;
		} else {
			int ls = l.size();
			l.clear();
			l = null;
			krit = null;
			return ls + 1;
		}
	}

	public String kreirajImeDatoteke() {
		String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();
		if (sifIsporucitelja.length() != 9) {
			return "";
		}
		String ozn = sifIsporucitelja.substring(4);
		String oPomagala = "O";
		pocSifra = vratiRedniBrojObracunaUGodini();
		String sSif = "";
		if (pocSifra < 10)
			sSif = "00" + pocSifra;
		else if (pocSifra < 100)
			sSif = "0" + pocSifra;
		else
			sSif = "" + pocSifra;
		int godina = obracun.getDatum().get(1);
		godina = obracun.getDatum().get(1) - 2000;
		String sGod;
		if (godina < 10)
			sGod = "0" + godina;
		else
			sGod = "" + godina;
		String imeDatoteke = ozn + sSif + "." + oPomagala + sGod;
		return imeDatoteke;
	}

	@SuppressWarnings("unchecked")
	private void kreirajDisketu() {
		JFileChooser jfc;
		String sifIsporucitelja;
		PostavkeBean postavke;
		if (!spremiObracun())
			return;

		obracun = (HzzoObracunVO) obracunEditor.vratiPodatke();
		if (obracun == null) {
			alert("Problem! Obraèun ne postoji?!?");
			return;
		}

		jfc = new JFileChooser();
		jfc.setFileSelectionMode(1);
		jfc.setToolTipText("Molimo, odaberite mjesto gdje \u0107e se pohraniti datoteka");

		String dir = GlavniFrame.getDirektorijZaPohranuObracuna();
		jfc.setCurrentDirectory(new File(dir));

		sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();
		int rez = jfc.showDialog(this, "Pohrani");
		if (rez != JFileChooser.APPROVE_OPTION)
			return;
		postavke = new PostavkeBean();

		String imeDatoteke;
		String datoteka;
		BufferedWriter out;
		imeDatoteke = kreirajImeDatoteke();
		if (imeDatoteke.equals(""))
			imeDatoteke = "demo.hzzo";

		GlavniFrame.setDirektorijZaPohranuObracuna(jfc.getSelectedFile()
				.toString());

		datoteka = jfc.getSelectedFile().toString()
				+ System.getProperty("file.separator") + imeDatoteke;
		out = null;
		List<HzzoObracunVO> obracuni = null;
		StavkaRacunaDAO stavkeDAO;

		SearchCriteria krit = new SearchCriteria();
		krit.setKriterij("krit_svi_obracuni_za_podruznicu_hzzo");
		Calendar datumOd = null;

		ArrayList<Object> li = new ArrayList<Object>(2);
		li.add(obracun.getDatum());
		li.add(obracun.getSifPodruznice());
		krit.setPodaci(li);

		try {
			obracuni = obracunDao.findAll(krit);
		} catch (SQLException sqle) {
			Logger.log("Iznimka - potraga za hzzo obraèunima vratila", sqle);
			alert("Postoje odre\u0111eni problemi pri kreiranju diskete. Molimo pogledajte poruke sustava i kontaktirajte administratora");
		}

		li.clear();
		li = null;

		// trazimo prvi prethodni obracun prije ovog danasnjeg
		int listaSize = obracuni != null ? obracuni.size() : -1;
		HzzoObracunVO zadnjiObracun =  (obracuni != null
				&& listaSize > 0 ? obracuni.get(0) : null);
		datumOd = zadnjiObracun != null ? zadnjiObracun.getDatum() : null;
		
		obracuni.clear(); obracuni=null;

		// if(l == null || l.size() == 0)
		// {
		// Logger.log("potraga za hzzo izdanim obracunima vratila nazad null?!? ");
		// alert("Postoje odre\u0111eni problemi pri kreiranju diskete. Molimo pogledajte poruke sustava i kontaktirajte administratora");
		// try {if(out != null)out.close();}catch(IOException ioe) { }
		// return;
		// }

		SearchCriteria kriterij = new SearchCriteria();
		kriterij.setKriterij(HzzoObracunDAO.KRITERIJ_SVI_RACUNI_ZA_OBRACUN);
		ArrayList<Object> l = new ArrayList<Object>(4);
		l.add(datumOd);
		l.add(obracun.getDatum());
		l.add(obracun.getSifPodruznice());
		l.add(obracun.getSifOsiguranja());
		kriterij.setPodaci(l);
		try {
			l = (ArrayList) DAOFactory.getInstance().getRacuni()
					.findAll(kriterij);
		} catch (SQLException sqle) {
			Logger.fatal(
					"SQL iznimka kod tra\u017Eenja ra\u010Duna za kreiranje diskete ",
					sqle);
			alert("Problem pri poku\u0161aju pristupanja podacima. Provjerite poruke sustava i kontaktirajte administratora");
			return;
		}

		listaSize = l != null ? l.size() : -1;

		stavkeDAO = DAOFactory.getInstance().getStavkeRacuna();
		// int total = 0;

		StavkaRacunaVO svo = null;
		RacunVO rvo = null;
		String d;
		String tvrtkaNaziv;
		String osobniRacunZaDopunsko;
		String osobniRacunZaOsnovno;
		String brojPoliceDopunsko;
		ArrayList stavke;
		String sUkupno;
		String sSudjelovanje;
		String sTeretDopunsko;
		String sPorezOsnovno;
		String sPorezDopunsko;
		String sTeretOsnovno;
		String brojPotvrdeHzzo;
		String brojIskaznice1;
		String brojIskaznice2;
		String aktivnostZZR;
		String aktivnostDop;
		Integer sifDrzave;
		String inoBroj1;
		String inoBroj2;
		DrzavaVO drzava;
		String sifProizvodjaca;
		String sifraPotvrdeLijecnika;

		try {

			try {
				out = new BufferedWriter(new FileWriter(datoteka));
			} catch (IOException e) {
				Logger.log(
						"IO iznimka kod poku\u0161aja otvaranja datoteke za kreiranje hzzo obra\u010Duna: dat:"
								+ datoteka, e);
				alert("Problem pri poku\u0161aju kreiranja datoteke. Provjerite poruke sustava");
				return;
			}

			if (l != null)
				for (int i = 0; i < listaSize; i++) {

					rvo = (RacunVO) l.get(i);
					d = ":";
					tvrtkaNaziv = new String(postavke.getTvrtkaNaziv()
							.replaceAll("\\:", ";").trim()
							.getBytes("iso8859-2"));
					osobniRacunZaDopunsko = rvo.getBrojOsobnogRacunaDopunsko();
					if (osobniRacunZaDopunsko == null)
						osobniRacunZaDopunsko = "";
					osobniRacunZaOsnovno = rvo.getBrojOsobnogRacunaOsnovno();
					if (osobniRacunZaOsnovno == null)
						osobniRacunZaOsnovno = "";
					boolean osnovno = rvo.getOsnovnoOsiguranje().booleanValue();
					if (!osnovno && osobniRacunZaDopunsko.trim().equals("")) {
						GlavniFrame
								.alert("Ra\u010Dun osobnog br. za osn. osiguranje: '"
										+ osobniRacunZaOsnovno
										+ "' jest ra\u010Dun i za dopunsko osiguranje,\n a nema broja osobnog ra\u010Duna za dopunsko osiguranje! (Ispravak)");
						Logger.warn(
								"Ra\u010Dun osobnog br. za osn. osiguranje: '"
										+ osobniRacunZaOsnovno
										+ "' jest ra\u010Dun i za dopunsko osiguranje,\n a nema broja osobnog ra\u010Duna za dopunsko osiguranje! (Ispravak diskete)",
								null);
					}
					brojPoliceDopunsko = osnovno
							|| rvo.getBrojPoliceDopunsko() == null ? "" : rvo
							.getBrojPoliceDopunsko().trim();
					if (osnovno)
						osobniRacunZaDopunsko = "";
					int ukupniIznosPomagala = 0;
					int ukupniIznosPoreza = 0;
					PoreznaStopaVO stopa = null;
					stavke = (ArrayList) stavkeDAO.findAll(rvo);
					int stavkeSize = stavke.size();
					for (int j = 0; j < stavkeSize; j++) {

						svo = (StavkaRacunaVO) stavke.get(j);

						int totalBezPDVa = RacuniUtil.getNettoIznosStavke(svo);
						int totalStavka  = RacuniUtil.getBruttoIznosStavke(svo);

						int porez = totalStavka - totalBezPDVa;
						int vrijednost = totalStavka;
						ukupniIznosPomagala += vrijednost;
						if (vrijednost == 0)
							Logger.log(" u racunu br.os. "
									+ rvo.getBrojOsobnogRacunaOsnovno()
									+ " jedna od " + stavkeSize
									+ " stavki ima vrijednost nula: "
									+ svo.getSifArtikla() + ":"
									+ svo.getKolicina().intValue() + ":"
									+ svo.getPoCijeni());
						ukupniIznosPoreza += porez;
					}// for j

					if (ukupniIznosPomagala == 0) {
						Logger.log(" u racunu br.os. "
								+ rvo.getBrojOsobnogRacunaOsnovno()
								+ " br stavki: "
								+ (stavke == null ? -1 : stavkeSize)
								+ " ima vrijednost nula!!!");
						alert("Nastao je ozbiljan problem pri kreiranju obra\u010Duna. Ra\u010Dun broj "
								+ (rvo == null ? "null?" : rvo
										.getBrojOsobnogRacunaOsnovno())
								+ " ima ukupnu vrijednost 0 kn?!? Racun se nece prikazati u obracunu!");
						return;
					}

					int ukKn = ukupniIznosPomagala / 100;
					int ukLp = ukupniIznosPomagala % 100;
					sUkupno = ukKn + "."
							+ (ukLp >= 10 ? "" + ukLp : "0" + ukLp);
					int sudj = rvo.getIznosSudjelovanja().intValue();
					int teretDopunsko = 0;
					int teretOsnovno = ukupniIznosPomagala - sudj;
					if (!rvo.getOsnovnoOsiguranje().booleanValue()) {
						teretDopunsko = sudj;
						sudj = 0;
					} else {
						teretDopunsko = 0;
					}
					int sKn = sudj / 100;
					int sLp = sudj % 100;
					sSudjelovanje = sKn + "."
							+ (sLp >= 10 ? "" + sLp : "0" + sLp);
					sKn = teretDopunsko / 100;
					sLp = teretDopunsko % 100;
					sTeretDopunsko = sKn + "."
							+ (sLp >= 10 ? "" + sLp : "0" + sLp);
					double omjer = (double) (sudj <= teretDopunsko ? teretDopunsko
							: sudj)
							/ (double) ukupniIznosPomagala;
					int porezSudjelovanja = (int) (ukupniIznosPoreza * omjer + 0.5D);
					int porezOsnovnoOsiguranje = ukupniIznosPoreza
							- porezSudjelovanja;
					sKn = porezOsnovnoOsiguranje / 100;
					sLp = porezOsnovnoOsiguranje % 100;
					sPorezOsnovno = sKn + "."
							+ (sLp >= 10 ? "" + sLp : "0" + sLp);
					int porezDopunsko = teretDopunsko <= 0 ? 0
							: porezSudjelovanja;
					sKn = porezDopunsko / 100;
					sLp = porezDopunsko % 100;
					sPorezDopunsko = sKn + "."
							+ (sLp >= 10 ? "" + sLp : "0" + sLp);
					sKn = teretOsnovno / 100;
					sLp = teretOsnovno % 100;
					sTeretOsnovno = sKn + "."
							+ (sLp >= 10 ? "" + sLp : "0" + sLp);

					if (rvo.getBrojPotvrde2() != null
							&& rvo.getBrojPotvrde2().length() > 3)
						brojPotvrdeHzzo = rvo.getBrojPotvrde1()
								+ "/"
								+ rvo.getBrojPotvrde2()
								+ (rvo.getOsnovnoOsiguranje().booleanValue() ? ""
										: "");
					else
						brojPotvrdeHzzo = "";

					brojIskaznice1 = rvo.getBrojIskaznice1() == null ? "" : rvo
							.getBrojIskaznice1();
					brojIskaznice2 = rvo.getBrojIskaznice2() == null ? "" : rvo
							.getBrojIskaznice2();
					aktivnostZZR = rvo.getAktivnostZZR();
					aktivnostDop = osnovno ? "" : rvo.getAktivnostDop();
					sifDrzave = rvo.getSifDrzave();
					sifraPotvrdeLijecnika = rvo.getBrojPotvrdePomagala() != null ? rvo
							.getBrojPotvrdePomagala() : "";
					inoBroj1 = "";
					inoBroj2 = "";
					drzava = null;
					sifProizvodjaca = rvo.getSifProizvodjaca() == null ? ""
							: rvo.getSifProizvodjaca().trim();
					String tvrtkaRacun = postavke.getTvrtkaRacun() == null ? ""
							: postavke.getTvrtkaRacun().trim();
					if (sifDrzave != null) {
						inoBroj1 = rvo.getBrojInoBolesnickogLista1();
						inoBroj2 = rvo.getBrojInoBolesnickogLista2();
						drzava = (DrzavaVO) DAOFactory.getInstance()
								.getDrzava().read(sifDrzave);
						if (drzava == null) {
							Logger.log("Iako postoji drzava u racunu br."
									+ rvo.getSifra().intValue()
									+ " sa sifrom: " + sifDrzave.intValue()
									+ " ne postoji adekvatna drzava objekt?!?");
							alert("Nastao je problem pri kreiranju podataka na disketi. Provjerite poruke sustava i kontaktirajte administratora!");
							try {
								if (out != null)
									out.close();
							} catch (IOException ioe) {
							}
						}
					}// if sifDrzave!=null

					String vrstaPomagala = ""
							+ rvo.getVrstaPomagala().intValue();

					String rac = "60"
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
							+ (sifDrzave == null ? "" : inoBroj1 + "/"
									+ inoBroj2) + d
							+ (sifDrzave == null ? "" : drzava.getCc3()) + d
							+ sifProizvodjaca + d + "0.00" + d + "0.00" + d
							+ osobniRacunZaOsnovno + d
							+ (aktivnostZZR == null ? "" : aktivnostZZR) + d
							+ (aktivnostDop == null ? "" : aktivnostDop) + ""
							+ d + sifraPotvrdeLijecnika + d;

					out.write(rac + "\r\n");
					String datObracuna = Util.convertCalendarToString(rvo
							.getDatumIzdavanja());
					Short sifraVelicineObloge;
					for (int j = 0; j < stavkeSize; j++) {
						svo = (StavkaRacunaVO) stavke.get(j);

						int vrijednost = RacuniUtil.getBruttoIznosStavke(svo);

						sKn = vrijednost / 100;
						sLp = vrijednost % 100;

						String sifArt = svo.getSifArtikla();
						boolean isoArt = sifArt != null
								&& sifArt.length() == 12
								&& StringUtils.imaSamoBrojeve(sifArt);

						String sfp = svo.getSifProizvodjaca() == null ? "" : ""
								+ svo.getSifProizvodjaca().intValue();

						sifraVelicineObloge = svo.getSifraVelicineObloge();

						if (!isoArt && sfp.equals("")) {
							Logger.log("stavka "
									+ svo.getSifArtikla()
									+ " u racunu br. "
									+ rvo.getBrojOsobnogRacunaOsnovno()
									+ " nema \u0161ifre proizvo\u0111a\u010Da! (kreirajDisketu)");
							GlavniFrame
									.alert("stavka "
											+ svo.getSifArtikla()
											+ " u racunu br. "
											+ rvo.getBrojOsobnogRacunaOsnovno()
											+ " nema \u0161ifre proizvo\u0111a\u010Da!\nPopravite to pa ponovno kreirajte disketu! (krD)");
						}
						String sIznos = sKn + "."
								+ (sLp >= 10 ? "" + sLp : "0" + sLp);
						String stavka = "61"
								+ d
								+ brojPotvrdeHzzo
								+ d
								+ datObracuna
								+ d
								+ sifArt
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

						out.write(stavka + "\r\n");
					}// for j

				}// for i

			PostavkeBean p = new PostavkeBean();
			String sSif = "";
			if (pocSifra < 10)
				sSif = "00" + pocSifra;
			else if (pocSifra < 100)
				sSif = "0" + pocSifra;
			else
				sSif = "" + pocSifra;
			try {
				if (out != null)
					out.close();
				out = null;
			} catch (IOException ioe) {
			}
			alert("Ne zaboravite na disketu(medij) napisati: " + imeDatoteke
					+ "\n \u0161ifru isporu\u010Ditelja: "
					+ PostavkeBean.getHzzoSifraIsporucitelja()
					+ "\n naziv tvrtke: " + p.getTvrtkaNaziv()
					+ "\n i redni broj diskete: " + sSif + "/"
					+ obracun.getDatum().get(1));
			p = null;
		} catch (Exception e) {
			StackTraceElement el[] = e.getStackTrace();
			String prvi = el == null || el.length <= 0 ? "(prazno)" : el[0]
					.toString();
			Logger.fatal(
					"Op\u0107enita iznimka kod HzzoKreiranjeRacuna pri kreiranju diskete: "
							+ prvi, e);
			alert("Op\u0107enita iznimka kod kreiranja diskete: " + e);
		} finally {
			try {
				if (out != null)
					out.close();
				out = null;
			} catch (IOException ioe) {
			}
		}
	}// kreirajDisketu



	public PoreznaStopaVO nadjiPoreznuSkupinu(Integer sifra) {
		PoreznaStopaVO ps = null;
		try {
			if (sifra != null)
				ps = (PoreznaStopaVO) DAOFactory.getInstance()
						.getPorezneStope().read(sifra);
			else
				ps = null;
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod HzzoKreiranjeRacuna pri citanju porezne stope.. ",
					e);
		} catch (Exception e) {
			Logger.fatal(
					"Op\u0107enita iznimka kod HzzoKreiranjeRacuna pri citanju porezne stope.. ",
					e);
			alert("Op\u0107enita iznimka kod tra\u017Eenja porezne skupine: "
					+ e);
		}
		return ps;
	}

	private static void alert(String poruka) {
		JOptionPane.showMessageDialog(instanca!=null&&instanca.isVisible()?instanca:null, poruka, "Upozorenje!", 2);
	}

	private void ispisiDopis() {
		if (!spremiObracun()) {
			alert(" Problem kod pohranjivanja obraèuna!");
			return;
		}
		try {
			obracun = (HzzoObracunVO) obracunEditor.vratiPodatke();

			if (obracun == null) {
				alert("Obraèun ne postoji?!? Kontaktirajte administratora!");
				return;
			}

			HtmlPrintParser parser = new HtmlPrintParser();
			PostavkeBean postavke = new PostavkeBean();
			DjelatnikVO dvo = null;
			try {
				if (obracun != null && obracun.getCreatedBy() == null) {
					Integer a = Integer.valueOf(GlavniFrame.getSifDjelatnika());
					obracun.setCreatedBy(a);
				}

				dvo = (DjelatnikVO) DAOFactory.getInstance().getDjelatnici()
						.read(obracun.getCreatedBy());
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod tra\u017Eenja djelatnika za obra\u010Dun br."
								+ obracun.getSifra(), e);
				alert("Problem sa traženjem podataka. Provjerite poruke sustava!");
				return;
			}

			final String predlozakObracun = "biz/sunce/obrasci/hzzo_obracun.html";
			String ispis = parser
					.ucitajHtmlPredlozak(predlozakObracun);

		    if (ispis==null){
	        	Logger.warn("Nema predloška datoteke: "+predlozakObracun, null);
	        	alert("Problem sa ispisom predloška. Provjerite poruke sustava!");
	        	return;
	        }
			
			int godina = obracun.getDatum().get(1);
			String sfo = PostavkeBean.isBrojaciOvisniOGodini() ? vratiRedniBrojObracunaUGodini()
					+ "/" + godina
					: "" + obracun.getSifra().intValue();
			ispis = ispis.replaceFirst("<!--broj_obracuna-->", "" + sfo);
			ispis = ispis.replaceFirst("<!--mjesto-->",
					postavke.getMjestoRada());
			String datObracunaStr = Util.convertCalendarToString(obracun
					.getDatum());
			ispis = ispis.replaceFirst("<!--datum-->", datObracunaStr);
			ispis = ispis.replaceFirst("<!--datum_obracuna-->", datObracunaStr);
			ispis = ispis.replaceFirst("<!--labela_diskete-->",
					kreirajImeDatoteke());
			ispis = ispis.replaceFirst(
					"<!--djelatnik-->",
					""
							+ (dvo == null ? "" : dvo.getIme() + " "
									+ dvo.getPrezime()));
			ispis = ispis.replaceFirst("<!--racuni_specifikacija-->",
					kreirajSpecifikacijuZaDopis(obracun));
			MjestoVO mvo = null;
			try {
				mvo = (MjestoVO) DAOFactory.getInstance().getMjesta()
						.read(obracun.getSifPodruznice());
			} catch (SQLException e1) {
				Logger.fatal("SQL iznimka kod trazenja mjesta za ispis dopisa",
						e1);
				alert("Nastao je problem pri kreiranju dopisa. Provjerite poruke sustava i kontaktirajte administratora. Dopis ce biti ispisan!");
			}
			String hzzo_podaci = iskreirajHzzoAdresuHTML(mvo);
			ispis = ispis.replaceFirst("<!--hzzo_podaci-->", hzzo_podaci);
			HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis, "hzzo_dopis_"
					+ sfo + "_" + datObracunaStr);
			ispis = null;
		} catch (Exception e) {
			StackTraceElement[] str = e.getStackTrace();
			String stck = "";

			if (str != null && str.length > 0)
				stck = str[0].getFileName() + ":" + str[0].getLineNumber();

			if (str != null && str.length > 1)
				stck += str[1].getFileName() + ":" + str[1].getLineNumber();

			if (str != null && str.length > 2)
				stck += str[2].getFileName() + ":" + str[2].getLineNumber();

			Logger.fatal(
					"Op\u0107enita iznimka kod HzzoKreiranjeRacuna pri kreiranju dopisa. Stc:"
							+ stck, e);
			alert("Op\u0107enita iznimka kod ispisivanja dopisa: " + e);
		}
		return;
	}

	public String iskreirajHzzoAdresuHTML(MjestoVO podr) {
		if (podr == null)
			return "?!?";
		Integer osig = obracun.getSifOsiguranja();
		String hzzo = "HZZO";
		if (osig != null && osig.intValue() == 2)
			hzzo = "HZZO ZZR";
		String hzzoKljuc = osig.intValue() != 1 ? "hzzozzr_adr_" : "hzzo_adr_";
		String adr = PostavkeBean.getPostavkaDB(hzzoKljuc
				+ podr.getSifra().intValue(), "");
		String html = "<table class='ValueObject' width='300'>";
		html = html + "<tr><td height='10'></tr>";
		html = html + "<tr style='font-size:13pt'><td><b>" + hzzo + " "
				+ podr.getNaziv() + "</b>";
		html = html + "<tr><td height='10'></tr>";
		html = html + "<tr><td><i>" + adr + "</i></tr>";
		html = html
				+ "<tr style='font-family:Arial,SansSerif; font-size:13pt'><td><b>"
				+ podr.toString() + "</b></tr>";
		html = html + "</table>";
		return html.replaceAll("null", "");
	}

	public void onemoguciIzmjene() {
		if (obracunEditor != null)
			obracunEditor.onemoguci();
		izmjeneMoguce = false;
	}

	public static final ArrayList<RacunVO> nadjiSveRacuneObracuna(HzzoObracunDAO obracunDao, HzzoObracunVO obracun) {
		try {
			ArrayList<Object> l = null;
			SearchCriteria krit = new SearchCriteria();
			krit.setKriterij("krit_svi_obracuni_za_podruznicu_hzzo");
			ArrayList<Object> li = new ArrayList<Object>(2);
			li.add(obracun.getDatum());
			li.add(obracun.getSifPodruznice());
			krit.setPodaci(li);
			l = (ArrayList) obracunDao.findAll(krit);
			li.clear();
			li = null;
			Calendar datumOd = null;
			int listaSize = l.size();
			if (l != null && listaSize > 0) {
				HzzoObracunVO zadnjiObracun = (HzzoObracunVO) l.get(0);
				datumOd = zadnjiObracun.getDatum();
			}
			SearchCriteria kriterij = new SearchCriteria();
			kriterij.setKriterij("krit_svi_racuni_za_obracun");
			l = new ArrayList<Object>(4);
			l.add(datumOd);
			l.add(obracun.getDatum());
			l.add(obracun.getSifPodruznice());
			l.add(obracun.getSifOsiguranja());
			kriterij.setPodaci(l);
			
			ArrayList<RacunVO> res = (ArrayList<RacunVO>) DAOFactory
					.getInstance().getRacuni().findAll(kriterij);
			
			return res;
		} catch (Exception e) {
			Logger.log("Iznimka kod traženja svih raèuna nekog obraèuna", e);
			return null;
		}
	}// nadjiSveRacuneObracuna

	public static final String kreirajSpecifikacijuZaDopis(HzzoObracunVO obracun) {
		HzzoObracunDAO obrDao = DAOFactory.getInstance().getHzzoObracuni();
		ArrayList<RacunVO> racuniObracuna = nadjiSveRacuneObracuna(obrDao,obracun);
		Integer osig = obracun.getSifOsiguranja();
		
		return kreirajSpecifikacijuZaDopis(racuniObracuna,osig);
	}
	
	public static final String kreirajSpecifikacijuZaDopis(ArrayList<RacunVO> racuniObracuna,Integer sifOsiguranja  ) {
		StringBuilder spec = new StringBuilder(8096);
		
		spec.append("<table align='center' cellpadding='2' cellspacing='0'>");
		spec.append("<thead><td width='70' align='center'><b>\u0161ifra</b><td width='80' align='center'><b>broj potvrde</b><td width='60'><b>datum narud\u017Ebe</b><td width='60'><b>datum obra\u010Duna</b><td width='70' align='right'><b>ukupni iznos</b><td><b>sudjelovanje</b><td width='70' align='right'><b>teret dop. osiguranje</b><td width='70' align='right'><b>PDV</b></thead>");

		int obracunUkupniTeretHzzo = 0;
		
		String hzzo = "HZZO";
		if (sifOsiguranja != null && sifOsiguranja.intValue() == 2)
			hzzo = "HZZO ZZR";
		
		Hashtable<Integer, Integer> skupine=new Hashtable<Integer, Integer>(5);
		Hashtable<Integer, Integer> brutta=new Hashtable<Integer, Integer>(5);
		
		int listaSize = racuniObracuna != null ? racuniObracuna.size() : -1;

		StavkaRacunaDAO stavkeDAO = null;
		int total = 0;
		int totalNaTeretHzzo = 0;
		StavkaRacunaVO svo = null;
		int ukupnoObracun = 0;
		int ukupnoObracunPDV = 0;
		int ukupnoKlijentiPDV = 0; // PDV koji su platili klijenti
		int ukupnoDopunsko = 0;
		int ukupnoObracunSudjelovanje=0;
		stavkeDAO = DAOFactory.getInstance().getStavkeRacuna();

		try {
			int ukupnoStavki = 0;
				
			for (int i = 0; i < listaSize; i++) 
			{
				RacunVO rvo = (RacunVO) racuniObracuna.get(i);
				int ukupniIznosPomagala = 0;
				int ukupniIznosPoreza = 0;
				ArrayList stavke = (ArrayList) stavkeDAO.findAll(rvo);
				int stavkeSize = stavke.size();
 
				if (stavke == null || stavkeSize == 0) {
					alert("Nastao je ozbiljan problem pri ispisu dopisa. Ra\u010Dun broj "
							+ (rvo == null ? "null?" : rvo
									.getBrojOsobnogRacunaOsnovno())
							+ " nema stavki?!? Racun se nece prikazati u dopisu!");
					continue;
				}// if

				int totalRacunBto=0;
				int totalRacunPorez=0;
				//prvi prolaz da se dobije brutto iznos racuna kako bi mogli dobiti omjer
				for (int j = 0; j < stavkeSize; j++) 
				{
					svo = (StavkaRacunaVO) stavke.get(j);

					int totalBezPDVa = RacuniUtil.getNettoIznosStavke(svo);
					int totalStavka = RacuniUtil.getBruttoIznosStavke(svo);
					
					//porez umanjen za omjer, koliko je kupac uplatio
					int porez = (int)((totalStavka - totalBezPDVa));

					totalRacunBto += totalStavka;
					totalRacunPorez += porez;
										
				} // for j (stavke) sudjelovanje 
				
				//ukupnoObracun += ukupniIznosPomagala;
				//ukupnoObracunPDV += ukupniIznosPoreza;
				
				int ukKn = totalRacunBto / 100;
				int ukLp = totalRacunBto % 100; 
				String sUkupno = ukKn + "."
						+ (ukLp >= 10 ? "" + ukLp : "0" + ukLp);
				int sudj = rvo.getIznosSudjelovanja() == null ? 0 : rvo
						.getIznosSudjelovanja().intValue();
				
				ukupnoObracunSudjelovanje+=rvo.getDopunskoOsiguranje()?0:sudj;
				
				int teretDopunsko = 0;
				int teretOsnovno = totalRacunBto - sudj;
				obracunUkupniTeretHzzo += teretOsnovno;

				boolean jeliDopunsko = !rvo.getOsnovnoOsiguranje().booleanValue();
				if (jeliDopunsko) {
					teretDopunsko = sudj;
					sudj = 0;
				} else {
					teretDopunsko = 0;
				}
				int sKn = sudj / 100;
				int sLp = sudj % 100;
				String sSudjelovanje = getNovac(sKn, sLp);
				sKn = teretDopunsko / 100;
				sLp = teretDopunsko % 100;
				String sTeretDopunsko = getNovac(sKn, sLp);
				double omjer = 1.0D;
				
				if (jeliDopunsko)
					omjer=1.0d;
				else
				if (totalRacunBto > 0)
					omjer = 1.0d-(double)(sudj <= teretDopunsko ? teretDopunsko : sudj)
							/ (double)totalRacunBto;

				int ukBtoPorez=0;
				
				//drugi prolaz (sudjelovanje)
				for (int j = 0; j < stavkeSize; j++) 
				{
					svo = (StavkaRacunaVO) stavke.get(j);

					int totalBezPDVa = RacuniUtil.getNettoIznosStavke(svo);
					int totalStavka = RacuniUtil.getBruttoIznosStavke(svo);
					
					//porez umanjen za omjer, koliko je kupac uplatio
					int btoPorez=(totalStavka - totalBezPDVa);
					int porez = (int)(btoPorez*omjer+0.5d);
					//int umBto=(int)(totalStavka*omjer+0.5d);
					int umTotal=(int)(totalStavka*omjer+0.5d);

					Integer poreznaStopa=svo.getPoreznaStopa();
					
					Integer porezSkupina = skupine.get(poreznaStopa);
					
					if (porezSkupina==null) porezSkupina=Integer.valueOf(porez);
					 else 
						porezSkupina=Integer.valueOf(porezSkupina.intValue()+porez);
					
					skupine.put(poreznaStopa, porezSkupina);
					
					Integer bruttoIznos=brutta.get(poreznaStopa);
					
					if (bruttoIznos==null) bruttoIznos=Integer.valueOf(umTotal);
					 else 
						bruttoIznos=Integer.valueOf(bruttoIznos.intValue()+umTotal);

					brutta.put(poreznaStopa, bruttoIznos);
					
					ukupniIznosPomagala += totalStavka; //umTotal
					ukupniIznosPoreza += porez;
					ukBtoPorez+=btoPorez;
					
					ukupnoStavki++;					
				} // for j (stavke) sudjelovanje 
				
				
				int porezSudjelovanja = (int) (ukBtoPorez-ukupniIznosPoreza);

				int porezOsnovnoOsiguranje = ukBtoPorez-porezSudjelovanja;

				ukupnoKlijentiPDV += porezSudjelovanja;
				
				 ukupnoObracun += ukupniIznosPomagala;
	             ukupnoObracunPDV += ukupniIznosPoreza;

				sKn = porezOsnovnoOsiguranje / 100;
				sLp = porezOsnovnoOsiguranje % 100;
				String sPorezOsnovno = getNovac(sKn, sLp);
				sKn = ukupniIznosPoreza / 100;
				sLp = ukupniIznosPoreza % 100;
				String sPorezUkupno = getNovac(sKn, sLp);
				int porezDopunsko = teretDopunsko <= 0 ? 0 : porezSudjelovanja;
				sKn = porezDopunsko / 100;
				sLp = porezDopunsko % 100;
				String sPorezDopunsko = getNovac(sKn, sLp);
				sKn = teretOsnovno / 100;
				sLp = teretOsnovno % 100;
				String sTeretOsnovno = getNovac(sKn, sLp);
				String brojPotvrdeHzzo = rvo.getBrojPotvrde1() + "/"
						+ rvo.getBrojPotvrde2()
						+ (rvo.getOsnovnoOsiguranje().booleanValue() ? "" : "");

				spec.append("<tr valign='top'><td align='left'>");
				spec.append(rvo.getBrojOsobnogRacunaOsnovno());
				spec.append("<td><nobr>");
				spec.append(brojPotvrdeHzzo);
				spec.append("</nobr><td>");
				spec.append(
						Util.convertCalendarToString(rvo.getDatumNarudzbe()))
						.append("<td>");
				spec.append(
						Util.convertCalendarToString(rvo.getDatumIzdavanja()))
						.append("<td align='right'>");
				spec.append(sUkupno).append("<td align='right'>");
				spec.append(sSudjelovanje).append("<td align='right'>")				
						.append(sTeretDopunsko)
						.append("<td align='right'>" + sPorezUkupno);
				ukupnoDopunsko += teretDopunsko;
			}// for i

			int ukKn = ukupnoObracun / 100;
			int ukLp = ukupnoObracun % 100;
			String sUkupno = getNovac(ukKn, ukLp);
			ukKn = ukupnoDopunsko / 100;
			ukLp = ukupnoDopunsko % 100;
			String sUkupnoTeretDopunsko = getNovac(ukKn, ukLp);
			String sPdv = Util.pretvoriLipeUIznosKaoString(ukupnoObracunPDV);
			String sKlijentiPdv = Util
					.pretvoriLipeUIznosKaoString(ukupnoKlijentiPDV);
			String sSudjelovanjeUkupno=Util.pretvoriLipeUIznosKaoString(ukupnoObracunSudjelovanje);

			String sObracunUkupnoTeretHzzo = Util.pretvoriLipeUIznosKaoString(obracunUkupniTeretHzzo);

			String sUkupnoObracunPdv = Util.pretvoriLipeUIznosKaoString(ukupnoObracunPDV);

			spec.append("<tr><td colspan='9'><hr>");
			spec.append("<tr><td colspan='4' align='right'>Ukupno:<td align='right'><b>");
			spec.append(sUkupno)
			.append("</b><td align='right'><b>").append(sSudjelovanjeUkupno).append("</b></td>")
			.append("</b><td align='right'><b>").append(sUkupnoTeretDopunsko).append("</b><td>")
			.append("</b><td align='right'><b>").append(sUkupnoObracunPdv).append("</b><td></tr>");

			spec.append("<tr><td colspan='4' align='right'>na teret ")
					.append(hzzo).append(":<td align='right'><b>");
			spec.append(sObracunUkupnoTeretHzzo).append(
					"</b><td align='right'><b></b><td></tr>");

			spec.append("</table>");
			spec.append("<p>Ukupan broj raèuna u izvješæu: " + listaSize
					+ " - sadržava ukupno stavki: " + ukupnoStavki+"</p>");
			
			spec.append("<br>Rekapitulacija PDV-a:" );
			
			Enumeration<Integer> porezi = skupine.keys();
			Integer porezSkupina,bruttoIznos;
			spec.append("<table><tr><td><b>porezna skupina</b><td><b>osnovica<td><b>porez<td><b>iznos</b></tr>");
			while(porezi.hasMoreElements()){
				Integer porez=porezi.nextElement();
				PoreznaStopaVO stvo = RacuniUtil.nadjiPoreznuSkupinu(porez);
				//porezSkupina=skupine.get(porez);
				bruttoIznos=brutta.get(porez);
				porezSkupina=(int)(bruttoIznos-((double)bruttoIznos/(1.0d+stvo.getStopa()/100.0d)));
				spec.append("<tr><td>"+stvo.getNaziv()
				+"<td align='right'>"+Util.pretvoriLipeUIznosKaoString(bruttoIznos-porezSkupina)
				+"<td align='right'>"+Util.pretvoriLipeUIznosKaoString(porezSkupina)
				+"<td align='right'>"+Util.pretvoriLipeUIznosKaoString(bruttoIznos)
				+"</tr>"
				);
			}
			spec.append("</table>");
			
		} 
		catch (Exception sqle) {
			Logger.fatal("Iznimka kod traženja raèuna za ispisivanje dopisa ",
					sqle);
			alert("Problem pri pokušaju ispisivanja dopisa. Provjerite poruke sustava i kontaktirajte administratora");
		}

		String res = spec.toString();
		spec.setLength(0);
		spec = null;
		return res;
	}// kreirajSpecifikacijuZaDopis

	private static String getNovac(int kn, int lp) {
		return kn + "." + (lp >= 10 ? "" + lp : "0" + lp);
	}

	private JButton getJbIspisiDopis() {
		if (jbIspisiDopis == null) {
			jbIspisiDopis = new JButton();
			jbIspisiDopis.setText("Ispi\u0161i dopis");
			jbIspisiDopis.setMnemonic(68);
			jbIspisiDopis
					.setToolTipText("popratni dopis sa specifikacijom kreiranih ra\u010Duna");
			jbIspisiDopis.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ispisiDopis();
				}
			});
		}
		return jbIspisiDopis;
	}

	private JButton getJbZatvori() {
		if (jbZatvori == null) {
			jbZatvori = new JButton();
			jbZatvori.setText("Zatvori");
			jbZatvori.setMnemonic(90);
			jbZatvori.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return jbZatvori;
	}

	private JPanel getJpObracunEditor() {
		if (jpObracunEditor == null) {
			obracunDao = DAOFactory.getInstance().getHzzoObracuni();
			obracunEditor = obracunDao.getGUIEditor();
			jpObracunEditor = (JPanel) obracunEditor;
			if (automatskoPunjenje)
				try {
					obracun = (HzzoObracunVO) obracunDao.read(Calendar
							.getInstance());
				} catch (SQLException e) {
					Logger.fatal(
							"SQL iznimka kod poku\u0161aja \u010Ditanja postoje\u0107eg hzzo obra\u010Duna",
							e);
				}
			if (automatskoPunjenje && obracun == null) {
				obracun = new HzzoObracunVO();
				obracunEditor.napuniPodatke(obracun);
			} else if (automatskoPunjenje) {
				obracun = new HzzoObracunVO();
				obracunEditor.napuniPodatke(obracun);
			}
		}// if
		return jpObracunEditor;
	}

	private boolean spremiObracun() {
		boolean rez;
		HzzoObracunVO obj;
		rez = true;

		if (!izmjeneMoguce)
			return true;

		obj = (HzzoObracunVO) obracunEditor.vratiPodatke();

		if (obj != null && obj.isModified()) {
			obj.setCreated(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
			obj.setSifra(Integer.valueOf(-1));
		}
		if (obj == null || !obj.isModified())
			return true;

		if (obj != null && obj.getSifra() == null)
			obj.setSifra(Integer.valueOf(-1));

		String poruka = obracunDao.narusavaLiObjektKonzistentnost(obj);

		if (poruka != null) {
			boolean upozorenje = false;
			if (poruka.startsWith("@")) {
				poruka = poruka.substring(1);
				upozorenje = true;
			}
			JOptionPane
					.showMessageDialog(getParent(), poruka, "upozorenje!", 2);
			if (!upozorenje)
				return false;
		}
		try {
			if (obj.getSifra() == null || obj.getSifra().intValue() == -1) {
				obj.setCreated(System.currentTimeMillis());
				obj.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
				obracunDao.insert(obj);
				if (obj.getSifra() == null || obj.getSifra().intValue() == -1)
					JOptionPane
							.showMessageDialog(
									getParent(),
									"Nastala je gre\u0161ka pri poku\u0161aju upisivanja podataka!",
									"Upozorenje!", 2);
			} else {
				obj.setLastUpdatedBy(Integer.valueOf(GlavniFrame
						.getSifDjelatnika()));
				boolean rezultat = obracunDao.update(obj);
				if (!rezultat)
					JOptionPane
							.showMessageDialog(
									getParent(),
									"Nastala je gre\u0161ka pri poku\u0161aju mijenjanja podataka!",
									"Upozorenje!", 2);
			}
		} catch (SQLException sqle) {
			Logger.fatal(
					"SQL Iznimka kod updateanja ValueObjecta - HzzoKreiranjeObracuna ",
					sqle);
			alert("Iznimka kod pohranjivanja obra\u010Duna: " + sqle);
			rez = false;
		} catch (Exception e) {
			Logger.fatal(
					"Iznimka kod updateanja ValueObjecta - HzzoKreiranjeObracuna ",
					e);
			alert("Op\u0107enita iznimka kod pohranjivanja obra\u010Duna: " + e);
			rez = false;
		}

		return rez;
	}// spremiObracun

	private JButton getJbIspravak() {
		if (jbIspravak == null) {
			jbIspravak = new JButton();
			jbIspravak.setText("Ispravak");
			jbIspravak
					.setToolTipText("ukoliko vam su se neki ra\u010Duni vratili zbog neispravnosti, nakon \u0161to ih ispravite ovdje mo\u017Eete kreirati posebnu disketu i dopis samo sa doti\u010Dnim ra\u010Dunima");
			jbIspravak.setMnemonic(73);
			jbIspravak.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ispravakObracuna();
				}
			});
		}
		return jbIspravak;
	}

	private void ispravakObracuna() 
	{
		
		Thread t = new Thread()
		{
		 public void run()
		 {
		 this.setPriority(Thread.MIN_PRIORITY);
		 GlavniFrame instanca2 = GlavniFrame.getInstanca();
		 instanca2.busy();
		 HzzoIspravakObracunaPanel panel = new HzzoIspravakObracunaPanel();
		 yield();
		 
		 instanca2.setContentPane(panel);
		 yield();
		 instanca2.pack();
		 panel.setOznaceniObracun(obracun);
		 panel.revalidate();
		 yield();
		 dispose();
		 panel.getRacuni().packAll();
		 yield();
		 instanca2.idle();
		 }
		};
		
		SwingUtilities.invokeLater(t);
	}

	public void disableIspravak() {
		getJbIspravak().setEnabled(false);
	}

	public void enableIspravak() {
		getJbIspravak().setEnabled(true);
	}

	private JButton getJbIspisiRacune() {
		if (jbIspisiRacune == null) {
			jbIspisiRacune = new JButton();
			jbIspisiRacune.setText("Ispi\u0161i sve ra\u010dune");
			jbIspisiRacune.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					ispisiSveRacune();
				}

			});
		}
		return jbIspisiRacune;
	}

	private final boolean ispisUDefaultPrinter = PostavkeBean
			.isIspisUGlavniPrinter();

	private void ispisiSveRacune() {

		obracun = (HzzoObracunVO) obracunEditor.vratiPodatke();
		if (obracun == null) {
			return;
		}

		if (obracun.getSifPodruznice() == null) {
			alert("Podružnica nije odabrana!");
			return;
		}

		if (obracun.getDatum() == null) {
			alert("Datum nije definiran!");
		}

		ArrayList<RacunVO> l = nadjiSveRacuneObracuna(obracunDao,obracun);
		int listaSize = l != null ? l.size() : -1;

		if (listaSize < 1)
			return;

		int odg = GlavniFrame.pitanjeDaNe("Želite li ispisati " + listaSize
				+ " raèuna odjednom?");

		if (odg == GlavniFrame.ODGOVOR_NE)
			return;

		PrinterJob pJob = PrinterJob.getPrinterJob();
		boolean printerOdabran = false;
		
		String printer = PostavkeBean.getPostavkaDB(GlavniFrame.ODABRANI_PRINTER, "");
		
		PrintService servis = null;
		
		if (!"".equals(printer))
		{
			PrintService[] servisi = PrinterJob.lookupPrintServices();
			
			for (PrintService ps: servisi)
			{
			 if (printer.equals(ps.getName()))
			 {
				servis = ps;
				try 
				{
					pJob.setPrintService(servis);
				} 
				catch (PrinterException e) 
				{
					Logger.warn("Nismo uspjeli postaviti printer za ispis (krobr): "+printer, e);
				}
				break;
			 }
			}
		}

		if (!ispisUDefaultPrinter) {
			printerOdabran = pJob.printDialog();
		} else
			printerOdabran = true;

		RacunVO rvo;
		if (printerOdabran)
			for (int i = 0; i < listaSize; i++) {
				rvo = l.get(i);
				this.ispisiRacun(rvo, pJob);
			}
	}// ispisiSveRacune
 

	private void ispisiRacun(RacunVO rvo, PrinterJob pJob) {
		if (rvo == null)
			return;

		IspisRacunaOsnovnoOsiguranje ispis = new IspisRacunaOsnovnoOsiguranje(
				rvo);
		ispis.printaj(false, pJob);
		ispis.finalize();
		ispis = null;

		if (!rvo.getOsnovnoOsiguranje().booleanValue()) {
			IspisRacunaDopunskoOsiguranje ispisDop = new IspisRacunaDopunskoOsiguranje(
					rvo);
			ispisDop.printaj(false, pJob);
			// ispisDop.finalize();
			ispisDop = null;
		}
	}// ispisiRacun

	private JPanel jContentPane;
	private JButton jbKreirajDisketu;
	private JButton jbIspisiDopis;
	private JButton jbZatvori;
	private HzzoObracunVO obracun;
	private GUIEditor<HzzoObracunVO> obracunEditor;
	private HzzoObracunDAO obracunDao;
	boolean izmjeneMoguce;
	private JButton jbIspisiRacune;
	public int pocSifra;
	 
	public static final String HZZO_CHARSET = "iso8859-2";
	public static final String HZZO_DELIMITER = ":";
	public static final String HZZO_SIFRA_RACUN = "60";
	public static final String HZZO_SIFRA_STAVKA_POMAGALA = "61";
	public static final String HZZO_SIFRA_POSTUPAKA = "62";
	private JPanel jpObracunEditor;
	private JButton jbIspravak;
	boolean automatskoPunjenje;
}