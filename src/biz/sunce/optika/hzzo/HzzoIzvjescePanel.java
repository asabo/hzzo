/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.ProizvodjaciDAO;
import biz.sunce.dao.RacunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.HzzoStavkaIzvjescaVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Konstante;
import biz.sunce.optika.Logger;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.Util;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.SlusacDateChoosera;

/**
 * datum:2006.06.20
 * 
 * @author asabo
 * 
 */
public final class HzzoIzvjescePanel extends JPanel implements
		SlusacDateChoosera {
	Calendar datOd = null, datDo = null;
	RacunDAO racuni = null;
	StavkaRacunaDAO stavke = null;
	KlijentDAO klijenti = null;
	ProizvodjaciDAO proizvodjaci = null;
	DAOFactory factory = null;

	biz.sunce.opticar.vo.TableModel mod = null;

	private javax.swing.JLabel jLabel = null;
	private JDateChooser dOd = null;
	private javax.swing.JLabel jLabel1 = null; // @jve:visual-info decl-index=0
												// visual-constraint="811,313"
	private JDateChooser dDo = null;
	private javax.swing.JScrollPane jScrollPane = null; // @jve:visual-info
														// decl-index=0
														// visual-constraint="301,611"
	private JXTable podaci = null;
	private javax.swing.JButton jbIspisi = null;
	private javax.swing.JLabel jlUkupnoTeretHZZO = null; // @jve:visual-info
															// decl-index=0
															// visual-constraint="173,611"

	/**
	 * This is the default constructor
	 */
	public HzzoIzvjescePanel() {
		super();
		initialize();
		factory = DAOFactory.getInstance();
		racuni = factory.getRacuni();
		stavke = factory.getStavkeRacuna();
		klijenti = factory.getKlijenti();
		proizvodjaci = factory.getProizvodjaci();
		Thread t = new Thread() {
			@Override
			public void run() {
				this.setPriority(Thread.MIN_PRIORITY);
				this.setName("Ucitavac podataka u HzzoIzvjesce");
				GlavniFrame.getInstanca().busy();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					return;
				}
				yield();
				Calendar c = Calendar.getInstance();
				boolean lenient = c.isLenient();
				c.setLenient(true);
				c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 3);
				c.setLenient(lenient);
				dOd.setDatum(c);

				napuniPodatke();
				
				podaci.packAll();
				GlavniFrame.getInstanca().idle();
			}
		};

		SwingUtilities.invokeLater(t);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints11.gridy = 2;
		consGridBagConstraints11.gridwidth = 6;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints5.weighty = 1.0;
		consGridBagConstraints5.weightx = 1.0;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints5.gridy = 1;
		consGridBagConstraints5.gridwidth = 6;
		consGridBagConstraints3.gridx = 2;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints6.gridy = 0;
		consGridBagConstraints6.gridx = 5;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints2.weighty = 1.0;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints4.weighty = 1.0;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 0;
		consGridBagConstraints4.gridx = 3;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJScrollPane(), consGridBagConstraints5);
		this.add(getDOd(), consGridBagConstraints2);
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getDDo(), consGridBagConstraints4);
		this.add(getJbIspisi(), consGridBagConstraints6);
		this.add(getJlUkupnoTeretHZZO(), consGridBagConstraints11);
		this.setSize(800, 600);
		this.setToolTipText("HZZO izvješæe... ");
		this.setPreferredSize(new java.awt.Dimension(800, 600));
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Razdoblje od: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes dOd
	 * 
	 * @return javax.swing.JPanel
	 */
	private JDateChooser getDOd() {
		if (dOd == null) {
			dOd = new JDateChooser();
			dOd.setDatum(Calendar.getInstance());
			dOd.dodajSlusaca(this);
			dOd.setPreferredSize(new java.awt.Dimension(135, 20));
			dOd.setMinimumSize(new java.awt.Dimension(135, 20));
		}
		return dOd;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("do: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes dDo
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getDDo() {
		if (dDo == null) {
			dDo = new JDateChooser();
			dDo.setDatum(Calendar.getInstance());
			dDo.dodajSlusaca(this);
			dDo.setPreferredSize(new java.awt.Dimension(135, 20));
			dDo.setMinimumSize(new java.awt.Dimension(135, 20));
		}
		return dDo;
	}

	/**
	 * This method initializes podaci
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getPodaci() {
		if (podaci == null) {
			podaci = new JXTable();

			podaci.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			mod = new biz.sunce.opticar.vo.TableModel(DAOFactory.getInstance()
					.getHzzoIzvjesca(), podaci);
			podaci.setModel(mod);

		}
		return podaci;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getPodaci());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jbIspisi
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIspisi() {
		if (jbIspisi == null) {
			jbIspisi = new javax.swing.JButton();
			jbIspisi.setText("Ispiši");
			jbIspisi.setToolTipText("Ispišite izvješæe");
			jbIspisi.setMnemonic(java.awt.event.KeyEvent.VK_I);
			jbIspisi.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new Thread() {
						@Override
						public void run() {
							ispisiDopis();
						}
					}.start();

				}
			});
		}
		return jbIspisi;
	}

	private void ispisiDopis() {
		KreiranjeHzzoIzvjescaNapredakFrame napredakFrame = null;
		String ispis = null;

		try {
			napredakFrame = new KreiranjeHzzoIzvjescaNapredakFrame();

			napredakFrame.setVisible(true);
			napredakFrame.pack();
			biz.sunce.util.GUI.centrirajFrame(napredakFrame);

			HtmlPrintParser parser = new HtmlPrintParser();

			DjelatnikVO dvo = GlavniFrame.getDjelatnik();
			napredakFrame.setStatus("Uèitavam predložak");

			ispis = parser
					.ucitajHtmlPredlozak(Konstante.PREDLOZAK_HZZO_IZVJESCE_3MJ);

			// ispis=ispis.replaceFirst("<!--mjesto-->",
			// postavke.getMjestoRada());
			ispis = ispis.replaceFirst(
					"<!--djelatnik-->",
					""
							+ (dvo != null ? dvo.getIme() + " "
									+ dvo.getPrezime() : ""));

			ispis = ispis.replaceFirst("<!--datum_od-->",
					Util.convertCalendarToString(this.datOd));
			ispis = ispis.replaceFirst("<!--datum_do-->",
					Util.convertCalendarToString(this.datDo));

			ispis = ispis.replaceFirst("<!--stavke_specifikacija-->",
					kreirajSpecifikacijuZaDopis(napredakFrame));

			napredakFrame.setStatus("Slanje dokumenta na ispis");
			HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis, "HZZO_izvjesce");
			napredakFrame.setStatus("Ispis zakljuèen");
		} catch (Exception e) {
			Logger.fatal(
					"Opæenita iznimka kod HzzoIzvjescePanel pri kreiranju izvješæa...",
					e);
			if (napredakFrame != null)
				napredakFrame.setStatus("Iznimka pri ispisu dokumenta!");
			GlavniFrame
					.alert("Opæenita iznimka kod ispisivanja HZZO izvješæa: "
							+ e);
		} finally {
			try {
				if (napredakFrame != null)
					napredakFrame.dispose();
				napredakFrame = null;
			} catch (Exception e) {
			}
			ispis = null;
		}

	}// ispisiDopis

	private String kreirajSpecifikacijuZaDopis(
			KreiranjeHzzoIzvjescaNapredakFrame napredak) {
		// "","naziv pomagala","proizvoðaè","broj potvrde","kolièina",
		// "ime","prezime","standardno pomagalo","osoba nadoplatila"

		napredak.setStatus("Pripremam specifikaciju");

		StringBuffer tmp = new StringBuffer();

		tmp.append("<table class='podaci' cellspacing='1' cellpadding='1' width='100%'>"
				+ "<tr class='zaglavlje'><td width='70'>tip<td width='260'>naziv pomagala<td width='70'>proizvoðaè<td width='100'>br. potv.<td width='42'>kolièina<td width='70'>ime<td width='70'>prezime<td width='40'>std. pomagalo<td width='55'>osoba nadoplatila<td width='100'>šif. nstd. pom.<td></tr>");
		int uzetoSkupljihModela = 0;
		int strankeNadoplatile = 0;
		int kolicina = 0;
		int vrijednostRobe = 0, iznosSudjelovanja = 0;
		int tmpSfr = -1;

		String sfp = "";
		ArrayList l = (ArrayList) this.mod.getData();
		if (l != null)
			napredak.setMaksimum(l.size());
		if (l != null)
			for (int i = 0; i < l.size(); i++) {
				napredak.setVrijednost(i);
				HzzoStavkaIzvjescaVO s = (HzzoStavkaIzvjescaVO) l.get(i);

				if (s.getSifProizvodjaca() != null)
					sfp = "" + s.getSifProizvodjaca().intValue();
				else
					sfp = "";

				tmp.append("<tr><td>" + s.getSifArtikla() + "<td>"
						+ s.getNazivArtikla() + "<td>" + sfp + "<td>"
						+ s.getBrojPotvrde() + "<td align='right'>"
						+ s.getKolicina().intValue());
				tmp.append("<td>"
						+ s.getIme()
						+ "<td>"
						+ s.getPrezime()
						+ "<td>"
						+ (s.getUzetSkupljiModel().booleanValue() ? "ne" : "da"));
				tmp.append("<td>"
						+ (s.getStrankaNadoplatila().booleanValue() ? "da"
								: "ne")
						+ "<td>"
						+ (s.getUzetSkupljiModel().booleanValue() ? s
								.getTvrtkaSifraNestandardnogArtikla() : "")
						+ "<td></tr>");

				kolicina += s.getKolicina().intValue();
				uzetoSkupljihModela += s.getUzetSkupljiModel().booleanValue() ? 0
						: s.getKolicina().intValue();
				strankeNadoplatile += s.getStrankaNadoplatila().booleanValue() ? s
						.getKolicina().intValue() : 0;

				PomagaloVO pvo = null;

				try {
					pvo = (PomagaloVO) DAOFactory.getInstance().getPomagala()
							.read(s.getSifArtikla());
				} catch (SQLException ex1) {
					Logger.log(
							"SQL iznimka kod traženja pomagala pri kreiranju HZZO izvješæa (kreirajSpecifikacijuZaDopis)",
							ex1);
					GlavniFrame
							.alert("Nastao je problem pri kreiranju izvješæa. Molimo provjerite poruke sustava i kontaktirajte administratora");
				}

				PoreznaStopaVO stopa = nadjiPoreznuSkupinu(pvo
						.getPoreznaSkupina());

				int cijenaBezPDVa = (int) (s.getCijena().intValue()
						/ (1.0f + stopa.getStopa().intValue() / 100.0f) + 0.5f);
				int totalBezPDVa = s.getKolicina().intValue() * cijenaBezPDVa;
				int totalStavka = (int) (totalBezPDVa
						* (1.0f + stopa.getStopa().intValue() / 100.0f) + 0.5f);

				int poreznaOsnova = totalBezPDVa;
				int porez = totalStavka - totalBezPDVa;

				vrijednostRobe += totalStavka;

				if (tmpSfr != s.getSifRacuna().intValue()) {
					iznosSudjelovanja += s.getIznosSudjelovanja().intValue();
					tmpSfr = s.getSifRacuna().intValue();
				}
			}// for i
		String vrr = Util.pretvoriLipeUIznosKaoString(vrijednostRobe);
		String vrizn = Util.pretvoriLipeUIznosKaoString(vrijednostRobe
				- iznosSudjelovanja);
		tmp.append("<tr><td colspan='4' align='right'><b>Ukupno za zadano razdoblje: </b><td align='right'>"
				+ kolicina
				+ "<td><td><td>"
				+ uzetoSkupljihModela
				+ "<td>"
				+ strankeNadoplatile + "<td></tr>");
		tmp.append("<tr><td colspan='6' align='right'><b>Ukupna vrijednost robe: "
				+ vrr + "kn - na teret Hzzo: " + vrizn + "kn</tr>");
		tmp.append("</table>");
		napredak.setVrijednost(l != null ? l.size() : 0);
		napredak.setStatus("Specifikacija gotova...");
		return tmp.toString();
	}// kreirajSpecifikacijuZaDopis

	private PoreznaStopaVO nadjiPoreznuSkupinu(Integer sifra) {
		PoreznaStopaVO ps = null;

		try {
			if (sifra != null)
				ps = (PoreznaStopaVO) DAOFactory.getInstance()
						.getPorezneStope().read(sifra);
			else
				ps = null;
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod Ispisa raèuna osn. osiguranje pri citanju porezne stope.. ",
					e);
		}

		return ps;
	}// nadjiPoreznuSkupinu

	private void napuniPodatke() {
		SearchCriteria sc = new SearchCriteria();
		sc.dodajPodatak(this.datOd);
		sc.dodajPodatak(this.datDo);

		try {
			// l=(ArrayList)
			// DAOFactory.getInstance().getHzzoIzvjesca().findAll(sc);
			// this.model.setData(l);
			this.mod.setFilter(sc);
			Thread t = new Thread() {
				@Override
				public void run() {
					this.setPriority(Thread.MIN_PRIORITY);
					this.setName("Postavljac Izvjesce panela");
					try {
						Thread.sleep(50);
					} catch (InterruptedException ex) {
						return;
					}

					int kolicina = 0, uzetoSkupljihModela = 0, strankeNadoplatile = 0, iznosSudjelovanja = 0, vrijednostRobe = 0;
					int tmpSfr = 0;

					ArrayList l = (ArrayList) mod.getData();
					int lsize = l != null ? l.size() : -1;
					for (int i = 0; i < lsize; i++) {
						HzzoStavkaIzvjescaVO s = (HzzoStavkaIzvjescaVO) l
								.get(i);

						kolicina += s.getKolicina().intValue();
						uzetoSkupljihModela += s.getUzetSkupljiModel()
								.booleanValue() ? 0 : s.getKolicina()
								.intValue();
						strankeNadoplatile += s.getStrankaNadoplatila()
								.booleanValue() ? s.getKolicina().intValue()
								: 0;

						PomagaloVO pvo = null;

						try {
							pvo = (PomagaloVO) DAOFactory.getInstance()
									.getPomagala().read(s.getSifArtikla());
						} catch (SQLException ex1) {
							Logger.log(
									"SQL iznimka kod traženja pomagala pri kreiranju HZZO izvješæa",
									ex1);
							GlavniFrame
									.alert("Nastao je problem pri kreiranju izvješæa. Molimo provjerite poruke sustava i kontaktirajte administratora");
						}

						PoreznaStopaVO stopa = nadjiPoreznuSkupinu(pvo
								.getPoreznaSkupina());

						int cijenaBezPDVa = (int) (s.getCijena().intValue()
								/ (1.0f + stopa.getStopa().intValue() / 100.0f) + 0.5f);
						int totalBezPDVa = s.getKolicina().intValue()
								* cijenaBezPDVa;
						int totalStavka = (int) (totalBezPDVa
								* (1.0f + stopa.getStopa().intValue() / 100.0f) + 0.5f);

						int poreznaOsnova = totalBezPDVa;
						int porez = totalStavka - totalBezPDVa;

						int vrijednost = totalStavka;

						vrijednostRobe += totalStavka;

						if (tmpSfr != s.getSifRacuna().intValue()) {
							iznosSudjelovanja += s.getIznosSudjelovanja()
									.intValue();
							tmpSfr = s.getSifRacuna().intValue();
						}
					}// for i
					String vrr = Util
							.pretvoriLipeUIznosKaoString(vrijednostRobe);
					String vrizn = Util
							.pretvoriLipeUIznosKaoString(vrijednostRobe
									- iznosSudjelovanja);
					String strPl = Util
							.pretvoriLipeUIznosKaoString(iznosSudjelovanja);

					getJlUkupnoTeretHZZO().setText(
							"Kolièina: " + kolicina + " Vrijednost: " + vrr
									+ " - na teret HZZO: " + vrizn
									+ " stranke nadoplatile: " + strPl);
				}// run
			};
			SwingUtilities.invokeLater(t);
		} catch (Exception e) {
			Logger.fatal("iznimka kod traženja raèuna za Hzzo izvješæe", e);
			GlavniFrame
					.alert("Nastao je problem kod traženja raèuna. Provjerite poruke sustava!");
		}
	}// napuniPodatke

	/**
	 * This method initializes jlUkupnoTeretHZZO
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlUkupnoTeretHZZO() {
		if (jlUkupnoTeretHZZO == null) {
			jlUkupnoTeretHZZO = new javax.swing.JLabel();
			jlUkupnoTeretHZZO.setText("");
		}
		return jlUkupnoTeretHZZO;
	}

	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		this.datDo = this.dDo.getDatum();
		this.datOd = this.dOd.getDatum();
		this.napuniPodatke();
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
