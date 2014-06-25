package biz.sunce.optika;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.ProizvodjaciDAO;
import biz.sunce.dao.RacunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.HzzoStavkaIzvjescaVO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.hzzo.KreiranjeHzzoIzvjescaNapredakFrame;
import biz.sunce.util.GUI;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.SlusacDateChoosera;

// Referenced classes of package biz.sunce.optika:
//            KreiranjeHzzoIzvjescaNapredakFrame, GlavniFrame, Logger

public final class IzvjesceNarudzbePanel extends JPanel implements
		SlusacDateChoosera,
		SlusacOznaceneLabelePretrazivanja {
 
	private static final long serialVersionUID = -411683531656854390L;
	public IzvjesceNarudzbePanel() {
		datOd = null;
		datDo = null;
		racuni = null;
		stavke = null;
		klijenti = null;
		proizvodjaci = null;
		factory = null;
		mod = null;
		jLabel = null;
		dOd = null;
		jLabel1 = null;
		dDo = null;
		jScrollPane = null;
		podaci = null;
		jbIspisi = null;
		jlUkupnoTeretHZZO = null;
		jLabel2 = null;
		jtLijecnik = null;
		jLabel3 = null;
		jtPreporucio = null;
		oznaceniLijecnik = null;
		lijecniciPretrazivanje = null;
		preporucioPretrazivanje = null;
		oznaceniKlijent = null;
		initialize();
		factory = DAOFactory.getInstance();
		racuni = factory.getRacuni();
		stavke = factory.getStavkeRacuna();
		klijenti = factory.getKlijenti();
		proizvodjaci = factory.getProizvodjaci();
		(new Thread() {

			@Override
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					return;
				}
				yield();
				Calendar c = Calendar.getInstance();
				boolean lenient = c.isLenient();
				c.setLenient(true);
				c.set(2, c.get(2) - 1);
				c.setLenient(lenient);
				dOd.setDatum(c);
				yield();
				napuniPodatke();
			}

		}).start();
	}

	private void initialize() {
		GridBagConstraints consGridBagConstraints1 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints3 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints2 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints5 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints6 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints11 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints12 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints4 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints21 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints41 = new GridBagConstraints();
		GridBagConstraints consGridBagConstraints31 = new GridBagConstraints();
		consGridBagConstraints41.fill = 0;
		consGridBagConstraints41.weightx = 1.0D;
		consGridBagConstraints41.gridy = 0;
		consGridBagConstraints41.gridx = 8;
		consGridBagConstraints31.gridy = 0;
		consGridBagConstraints31.gridx = 7;
		consGridBagConstraints21.fill = 0;
		consGridBagConstraints21.weightx = 1.0D;
		consGridBagConstraints21.gridy = 0;
		consGridBagConstraints21.gridx = 6;
		consGridBagConstraints21.anchor = 17;
		consGridBagConstraints12.gridy = 0;
		consGridBagConstraints12.gridx = 5;
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints11.gridy = 2;
		consGridBagConstraints11.gridwidth = 6;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints5.fill = 1;
		consGridBagConstraints5.weighty = 1.0D;
		consGridBagConstraints5.weightx = 1.0D;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints5.gridy = 2;
		consGridBagConstraints5.gridwidth = 10;
		consGridBagConstraints3.gridx = 2;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints6.gridy = 0;
		consGridBagConstraints6.gridx = 9;
		consGridBagConstraints2.fill = 0;
		consGridBagConstraints2.weighty = 1.0D;
		consGridBagConstraints2.weightx = 1.0D;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints2.anchor = 17;
		consGridBagConstraints6.anchor = 13;
		consGridBagConstraints4.fill = 0;
		consGridBagConstraints4.weighty = 1.0D;
		consGridBagConstraints4.weightx = 1.0D;
		consGridBagConstraints4.gridy = 0;
		consGridBagConstraints4.gridx = 3;
		consGridBagConstraints4.anchor = 17;
		consGridBagConstraints3.anchor = 17;
		consGridBagConstraints1.anchor = 13;
		setLayout(new GridBagLayout());
		add(getJLabel(), consGridBagConstraints1);
		add(getJScrollPane(), consGridBagConstraints5);
		add(getDOd(), consGridBagConstraints2);
		add(getJLabel1(), consGridBagConstraints3);
		add(getDDo(), consGridBagConstraints4);
		add(getJbIspisi(), consGridBagConstraints6);
		add(getJlUkupnoTeretHZZO(), consGridBagConstraints11);
		add(getJLabel2(), consGridBagConstraints12);
		add(getJtLijecnik(), consGridBagConstraints21);
		add(getJLabel3(), consGridBagConstraints31);
		add(getJtPreporucio(), consGridBagConstraints41);
		setSize(800, 600);
		setToolTipText("Izvje\u0161\u0107e... ");
		setPreferredSize(new Dimension(800, 600));
	}

	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText("Razdoblje od: ");
		}
		return jLabel;
	}

	private JDateChooser getDOd() {
		if (dOd == null) {
			dOd = new JDateChooser();
			Calendar c = Calendar.getInstance();
			dOd.setDatum(c);
			dOd.dodajSlusaca(this);
			dOd.setPreferredSize(new Dimension(135, 20));
			dOd.setMinimumSize(new Dimension(135, 20));
		}
		return dOd;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("do: ");
		}
		return jLabel1;
	}

	private JPanel getDDo() {
		if (dDo == null) {
			dDo = new JDateChooser();
			dDo.setDatum(Calendar.getInstance());
			dDo.dodajSlusaca(this);
			dDo.setPreferredSize(new Dimension(135, 20));
			dDo.setMinimumSize(new Dimension(135, 20));
		}
		return dDo;
	}

	private JTable getPodaci() {
		if (podaci == null) {
			podaci = new JXTable();
			podaci.setAutoResizeMode(4);
			mod = new TableModel(DAOFactory.getInstance().getHzzoIzvjesca(),
					podaci);
			podaci.setModel(mod);
		}
		return podaci;
	}

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPodaci());
		}
		return jScrollPane;
	}

	private JButton getJbIspisi() {
		if (jbIspisi == null) {
			jbIspisi = new JButton();
			jbIspisi.setText("Ispi\u0161i");
			jbIspisi.setToolTipText("Ispi\u0161ite izvje\u0161\u0107e");
			jbIspisi.setMnemonic(73);
			jbIspisi.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					(new Thread() {

						@Override
						public void run() {
							ispisiDopis();
						}

					}).start();
				}
			});
		}
		return jbIspisi;
	}

	private void ispisiDopis() {
		KreiranjeHzzoIzvjescaNapredakFrame napredakFrame;
		napredakFrame = null;
		String ispis = null;
		napredakFrame = new KreiranjeHzzoIzvjescaNapredakFrame();
		napredakFrame.setVisible(true);
		napredakFrame.pack();
		GUI.centrirajFrame(napredakFrame);
		HtmlPrintParser parser = new HtmlPrintParser();
		//PostavkeBean postavke = new PostavkeBean();
		DjelatnikVO dvo = GlavniFrame.getDjelatnik();
		napredakFrame.setStatus("U\u010Ditavam predlo\u017Eak");
		ispis = parser
				.ucitajHtmlPredlozak("biz/sunce/obrasci/hzzo_tromjesecno_izvjesce.html");
		ispis = ispis.replaceFirst("<!--djelatnik-->", ""
				+ (dvo == null ? "" : dvo.getIme() + " " + dvo.getPrezime()));
		String datOdStr = Util.convertCalendarToString(datOd);
		ispis = ispis.replaceFirst("<!--datum_od-->", datOdStr);
		String datDoStr = Util.convertCalendarToString(datDo);
		ispis = ispis.replaceFirst("<!--datum_do-->", datDoStr);
		ispis = ispis.replaceFirst("<!--stavke_specifikacija-->",
				kreirajSpecifikacijuZaDopis(napredakFrame));
		napredakFrame.setStatus("Slanje dokumenta na ispis");
		HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis,
				"hzzo_tromjesecno_izvjesce" + datOdStr + "_" + datDoStr);
		napredakFrame.setStatus("Ispis zaklju\u010Den");
		// Exception e;
		try {
			if (napredakFrame != null)
				napredakFrame.dispose();
			napredakFrame = null;
		} catch (Exception ecx) {
			Logger.fatal(
					"Op\u0107enita iznimka kod HzzoIzvjescePanel pri kreiranju izvje\u0161\u0107a...",
					ecx);
			if (napredakFrame != null)
				napredakFrame.setStatus("Iznimka pri ispisu dokumenta!");
			GlavniFrame
					.alert("Op\u0107enita iznimka kod ispisivanja HZZO izvje\u0161\u0107a: "
							+ ecx);
		}
	}

	private String kreirajSpecifikacijuZaDopis(
			KreiranjeHzzoIzvjescaNapredakFrame napredak) {
		napredak.setStatus("Pripremam specifikaciju");
		StringBuffer tmp = new StringBuffer();
		tmp.append("<table class='podaci' cellspacing='1' cellpadding='1' width='100%'><tr class='zaglavlje'><td width='70'>tip<td width='260'>naziv pomagala<td width='70'>proizvo\u0111a\u010D<td width='100'>br. potv.<td width='42'>koli\u010Dina<td width='70'>ime<td width='70'>prezime<td width='40'>std. pomagalo<td width='55'>osoba nadoplatila<td width='100'>\u0161if. nstd. pom.<td></tr>");
		int uzetoSkupljihModela = 0;
		int strankeNadoplatile = 0;
		int kolicina = 0;
		int vrijednostRobe = 0;
		int iznosSudjelovanja = 0;
		int tmpSfr = -1;
		String sfp = "";
		ArrayList l = (ArrayList) mod.getData();
		if (l != null)
			napredak.setMaksimum(l.size());
		if (l != null) {
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
							"SQL iznimka kod tra\u017Eenja pomagala pri kreiranju HZZO izvje\u0161\u0107a (kreirajSpecifikacijuZaDopis)",
							ex1);
					GlavniFrame
							.alert("Nastao je problem pri kreiranju izvje\u0161\u0107a. Molimo provjerite poruke sustava i kontaktirajte administratora");
				}
				PoreznaStopaVO stopa = nadjiPoreznuSkupinu(pvo
						.getPoreznaSkupina());
				int cijenaBezPDVa = (int) (s.getCijena().intValue()
						/ (1.0F + stopa.getStopa().intValue() / 100F) + 0.5F);
				int totalBezPDVa = s.getKolicina().intValue() * cijenaBezPDVa;
				int totalStavka = (int) (totalBezPDVa
						* (1.0F + stopa.getStopa().intValue() / 100F) + 0.5F);
				int poreznaOsnova = totalBezPDVa;
				int porez = totalStavka - totalBezPDVa;
				vrijednostRobe += totalStavka;
				if (tmpSfr != s.getSifRacuna().intValue()) {
					iznosSudjelovanja += s.getIznosSudjelovanja().intValue();
					tmpSfr = s.getSifRacuna().intValue();
				}
			}

		}
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
		napredak.setVrijednost(l == null ? 0 : l.size());
		napredak.setStatus("Specifikacija gotova...");
		return tmp.toString();
	}

	 

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
					"SQL iznimka kod Ispisa ra\u010Duna osn. osiguranje pri citanju porezne stope.. ",
					e);
		}
		return ps;
	}

	private void napuniPodatke() {
		SearchCriteria sc = new SearchCriteria();
		sc.dodajPodatak(datOd);
		sc.dodajPodatak(datDo);
		sc.dodajPodatak(oznaceniKlijent);
		sc.dodajPodatak(oznaceniLijecnik);
		ArrayList l = null;
		try {
			mod.setFilter(sc);
			(new Thread() {

				@Override
				public void run() {
				}

			}).start();
		} catch (Exception e) {
			Logger.fatal(
					"iznimka kod tra\u017Eenja ra\u010Duna za Hzzo izvje\u0161\u010De",
					e);
			GlavniFrame
					.alert("Nastao je problem kod tra\u017Eenja ra\u010Duna. Provjerite poruke sustava!");
		}
	}

	private JLabel getJlUkupnoTeretHZZO() {
		if (jlUkupnoTeretHZZO == null) {
			jlUkupnoTeretHZZO = new JLabel();
			jlUkupnoTeretHZZO.setText("");
		}
		return jlUkupnoTeretHZZO;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("Lije\u010Dnik:");
		}
		return jLabel2;
	}

	private JTextField getJtLijecnik() {
		if (jtLijecnik == null) {
			jtLijecnik = new JTextField();
			jtLijecnik.setPreferredSize(new Dimension(120, 20));
			jtLijecnik.setToolTipText("naziv lije\u010Dnika");
			jtLijecnik.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					if (jtLijecnik.getText().trim().equals("")) {
						oznaceniLijecnik = null;
						napuniPodatke();
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					jtLijecnik.selectAll();
				}

			});
			lijecniciPretrazivanje = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getLijecnici(), 10, 10, 130, 70, jtLijecnik);
			lijecniciPretrazivanje.dodajSlusaca(this);
		}
		return jtLijecnik;
	}

	private JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Preporu\u010Dio: ");
		}
		return jLabel3;
	}

	private JTextField getJtPreporucio() {
		if (jtPreporucio == null) {
			jtPreporucio = new JTextField();
			jtPreporucio.setPreferredSize(new Dimension(120, 20));
			jtLijecnik.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					if (jtLijecnik.getText().trim().equals("")) {
						oznaceniKlijent = null;
						napuniPodatke();
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					jtPreporucio.selectAll();
				}

			});
			preporucioPretrazivanje = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getKlijenti(), 10, 10, 130, 70, jtPreporucio);
			preporucioPretrazivanje.dodajSlusaca(this);
		}
		return jtPreporucio;
	}

	public void labelaOznacena(Labela labela1) {
	}

	Calendar datOd;
	Calendar datDo;
	RacunDAO racuni;
	StavkaRacunaDAO stavke;
	KlijentDAO klijenti;
	ProizvodjaciDAO proizvodjaci;
	DAOFactory factory;
	TableModel mod;
	private JLabel jLabel;
	private JDateChooser dOd;
	private JLabel jLabel1;
	private JDateChooser dDo;
	private JScrollPane jScrollPane;
	private JXTable podaci;
	private JButton jbIspisi;
	private JLabel jlUkupnoTeretHZZO;
	private JLabel jLabel2;
	private JTextField jtLijecnik;
	private JLabel jLabel3;
	private JTextField jtPreporucio;
	private LijecnikVO oznaceniLijecnik;
	PretrazivanjeProzor lijecniciPretrazivanje;
	PretrazivanjeProzor preporucioPretrazivanje;
	KlijentVO oznaceniKlijent;
	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		datDo = dDo.getDatum();
		datOd = dOd.getDatum();
		napuniPodatke();
	}

}
