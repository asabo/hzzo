package biz.sunce.optika;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.hzzo.HzzoRacunPanel;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.Util;
import biz.sunce.util.tablice.sort.JSortTable;
 
 
/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Ante Sabo
 * @version 1.0
 */
public final class DobroDosliPanel extends JPanel implements
		SlusacModelaTablice, PrikazivacSinkronizacijskihPoruka {

	private static final long serialVersionUID = 1397622231119658011L;
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabel1 = new JLabel();
	JScrollPane jspPregledi = new JScrollPane();
	JXTable jtPregledi = new JXTable();
	JLabel jLabel2 = new JLabel();
	JScrollPane jspIstekli = new JScrollPane();
	JXTable jxIstekliArtikli = new JXTable();
	TableModel<KlijentVO> trebaZakazatiModel = null;
	TableModel<ValueObject> istekliArtikliModel = null;
	JLabel jLabel4 = new JLabel();
	JButton jbSinkronizacija = new JButton();
	Calendar sad = Calendar.getInstance();

	private javax.swing.JLabel jlStanjePrijenosa = null;
	private javax.swing.JButton jbIspisiPodsjetnik = null;

	public DobroDosliPanel() {
		try {
			jbInit();
			final DobroDosliPanel ja = this;
			Thread t = new Thread("DobroDosliPunjacForme") {
				@Override
				public void run() {
					try {

						this.setPriority(Thread.MIN_PRIORITY);
						ja.setCursor(new Cursor(Cursor.WAIT_CURSOR));

						DAOFactory inst = DAOFactory.getInstance();
						KlijentDAO kdao = inst.getKlijenti();
						trebaZakazatiModel = new TableModel<KlijentVO>(kdao, jtPregledi);
						jtPregledi.setModel(trebaZakazatiModel);
						Calendar c = Calendar.getInstance();
						trebaZakazatiModel.setFilter(c);

						istekliArtikliModel = new TableModel(null,
								jxIstekliArtikli);
						jxIstekliArtikli.setModel(istekliArtikliModel);

						ArrayList<ValueObject> lista = nadjiIstekleArtikle();

						if (lista != null && lista.size() > 0)
							istekliArtikliModel.setData(lista);
						istekliArtikliModel
								.setColumnIdentifiers(koloneIstekliArtikli);

						trebaZakazatiModel.dodajSlusaca(ja);
						istekliArtikliModel.dodajSlusaca(ja);
						jxIstekliArtikli.packAll();

						yield();
						// samo da se instancira i kick-in module
						HzzoRacunPanel racun = new HzzoRacunPanel(null);
						racun = null;

					} finally {
						ja.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}// run

				final String[] koloneIstekliArtikli = { "kupljeni artikl",
						"dat. izd.", "ime", "prezime", "spol", "roð.",
						"telefon", "poš.br.", "mjesto", "mj." };

				final int kolona = koloneIstekliArtikli.length;

				final String sql = "select a.NAZIV, trim(char(day(r.DATUM_IZDAVANJA)))||'.'||trim(char(month(r.datum_izdavanja)))||'.'||trim(char(year(r.datum_izdavanja))), k.IME,k.PREZIME, "
						+ "case when k.SPOL='m' then 'muško' else 'žensko' end as spol,k.DATRODJENJA as rodjen, "
						+ "case when k.TEL is null then '(nema)' else k.tel end as telefon,m.ZIP as PBR,m.NAZIV "
						+ ",month(current_date)-month(r.datum_izdavanja)+(year(current_date)-year(r.DATUM_IZDAVANJA))*12 as proteklo_mjeseci "
						+ "from STAVKE_RACUNA sr, RACUNI r,  artikli a, KLIJENTI k "
						+ "left outer join  MJESTA m on m.sifra=k.SIFMJESTA "
						+ "where a.SIFRA=sr.SIF_ARTIKLA and r.SIFRA=sr.SIF_RACUNA and k.sifra=r.SIF_KLIJENTA "
						+ "and (month(current_date)-month(r.datum_izdavanja)+(year(current_date)-year(r.DATUM_IZDAVANJA))*12) = a.ROK";

				private ArrayList<ValueObject> nadjiIstekleArtikle() {
					Connection con = null;
					Statement st = null;
					ResultSet rs = null;
					ArrayList<ValueObject> lista = new ArrayList<ValueObject>();

					try {
						con = DAOFactory.getConnection();
						st = con.createStatement();
						rs = st.executeQuery(sql);

						while (rs.next()) {
							ValueObject vo = new ValueObject();
							vo.setKolone(koloneIstekliArtikli);
							for (int i = 0; i < kolona; i++) {
								vo.setValue(i, rs.getString(i + 1));
							}
							lista.add(vo);
						}
					} catch (SQLException sqle) {
						System.err.println("Greška: " + sqle);
					} catch (Exception e) {
						System.err.println("Greška: " + e);
					} finally {
						try {
							if (rs != null)
								rs.close();
						} catch (SQLException sqle) {
						}
						try {
							if (st != null)
								st.close();
						} catch (SQLException sqle) {
						}
						try {
							DAOFactory.freeConnection(con);
						} catch (SQLException sql) {
						}
					}
					return lista;
				}// nadjiIstekleArtikla
			};

			SwingUtilities.invokeLater(t); // dretvo trci :)

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}// konstruktor

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jLabel1.setText("Pregledi koje bi trebalo zakazati: ");
		int faktor = GlavniFrame.getFaktor();
		jspPregledi.setMinimumSize(new Dimension(500*faktor, 180*faktor));
		jspPregledi.setPreferredSize(new Dimension(500*faktor, 180*faktor));
		jLabel2.setText("Prodaje koje se mogu pokušati obnoviti:");
		this.setMinimumSize(new Dimension(500*faktor, 200*faktor));
		this.setPreferredSize(new java.awt.Dimension(790*faktor, 580*faktor));
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints(
				2, 1, 1, 1, 0.0, 0.0, java.awt.GridBagConstraints.NORTHEAST,
				java.awt.GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0,
						0), 0, 0);
		consGridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints11.gridy = 2;
		consGridBagConstraints11.gridx = 2;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints1.gridy = 3;
		consGridBagConstraints1.gridx = 2;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		jtPregledi.setPreferredSize(new Dimension(800*faktor, 400*faktor));
		jspIstekli.setMinimumSize(new Dimension(500*faktor, 200*faktor));
		jspIstekli.setPreferredSize(new Dimension(700, 220));
		jLabel4.setText("Dobrodošli!");
		jbSinkronizacija.setText("Ažuriraj podatke");
		jbSinkronizacija
				.setToolTipText("razmjena informacija sa sink. serverom, SMS ili mail poruke, zahtjevi za kreiranjem novih èlanskih iskaznica, rezervnim dijelovima..");
		jLabel4.setFont(new java.awt.Font("Dotum", java.awt.Font.BOLD, 18));
		jspPregledi
				.setToolTipText("pregledi koje biste trebali zakazati ovih dana...");
		jbSinkronizacija
				.addActionListener(new DobroDosliPanel_jbSinkronizacija_actionAdapter(
						this));
		jspPregledi.setViewportView(jtPregledi);
		jspIstekli.setViewportView(jxIstekliArtikli);
		String istekliTT = "popis prodanih artikala koji su istekli i za koje klijenti imaju pravo ponovno tražiti doznaku od HZZO-a";
		jspIstekli.setToolTipText(istekliTT);
		jspIstekli.getViewport().setToolTipText(istekliTT);

		jxIstekliArtikli.setPreferredSize(new java.awt.Dimension(497*faktor, 56*faktor));
		jxIstekliArtikli.setToolTipText(istekliTT);
		this.add(jLabel2, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jspPregledi, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(jspIstekli, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jLabel4, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(jbSinkronizacija, consGridBagConstraints12);
		this.add(getJlStanjePrijenosa(), consGridBagConstraints1);
		this.add(getJbIspisiPodsjetnik(), consGridBagConstraints11);
		this.setToolTipText("podsjetnik ...");
		this.jbSinkronizacija.setEnabled(false);
	}// jbInit

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		KlijentVO kvo = null;
		if (posiljatelj != null && posiljatelj.getData() != null
				&& posiljatelj.getData().size() > redak && redak >= 0) {
			Object object = posiljatelj.getData().get(redak);
			if (object instanceof KlijentVO)
				kvo = (KlijentVO) object;
			else
				return;
			// 24.02.06. -asabo- treba klijenta opet ucitati iz baze podataka za
			// slucaj da je odnekud drugdje izmjenjen...
			try {
				kvo = (KlijentVO) DAOFactory.getInstance().getKlijenti()
						.read(kvo.getSifra());
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod povlacenja klijenta u DobroDošli formi šifra: "
								+ (kvo != null && kvo.getSifra() != null ? ""
										+ kvo.getSifra().intValue() : "?!?"), e);
				JOptionPane
						.showMessageDialog(
								this.getParent(),
								"Problem sa uèitavanjem podataka klijenta!\nOdaberite Datoteka -> Poruke sustava i provjerite poruke");
			}
			KlijentFrame kf = new KlijentFrame(kvo);
			if (posiljatelj == this.istekliArtikliModel)
				kf.dodajPoruku("trebali biste klijentu poslati roðendansku èestitku");
			else if (posiljatelj == this.trebaZakazatiModel)
				kf.dodajPoruku("klijentu biste trebali poslati poziv na pregled!");

			kf.show();
		}
	}// redakOznacen

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	public void jbSinkronizacija_actionPerformed(ActionEvent e) {
		GlavniFrame.sinkronizacijaPodataka(this);
	}

	/**
	 * This method initializes jlStanjePrijenosa
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlStanjePrijenosa() {
		if (jlStanjePrijenosa == null) {
			jlStanjePrijenosa = new javax.swing.JLabel();
			jlStanjePrijenosa.setText("");
			jlStanjePrijenosa.setFont(new java.awt.Font("Courier New",
					java.awt.Font.BOLD, 12));
			jlStanjePrijenosa
					.setToolTipText("poruke pri komunikaciji sustava sa web servisom");
		}
		return jlStanjePrijenosa;
	}

	public void postaviPoruku(String poruka) {
		if (poruka == null)
			poruka = "";
		this.jlStanjePrijenosa.setText(poruka);
	}

	public void postaviMax(int max) {
	}

	public void postaviTrenutnuVrijednost(int vrijednost) {
	}

	/**
	 * This method initializes jbIspisiPodsjetnik
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIspisiPodsjetnik() {
		if (jbIspisiPodsjetnik == null) {
			jbIspisiPodsjetnik = new javax.swing.JButton();
			jbIspisiPodsjetnik.setText("Ispiši podsjetnik");
			jbIspisiPodsjetnik
					.setToolTipText("ako želite podsjetnik ispisati na papir kliknite ovdje");
			jbIspisiPodsjetnik
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							ispisiPodsjetnik();
						}
					});
		}
		return jbIspisiPodsjetnik;
	}

	// priprema dokument i ispisuje ga na papir
	private void ispisiPodsjetnik() {
		HtmlPrintParser parser = new HtmlPrintParser();
		String ispis = parser
				.ucitajHtmlPredlozak(Konstante.PREDLOZAK_PODSJETNIK);
		ispis = ugradiPodsjetnikUHTmlDokument(ispis);
		String dokumentNaziv = "Podsjetnik na dan "
				+ Util.convertCalendarToString(Calendar.getInstance());

		HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis, dokumentNaziv);

	}

	private String ugradiPodsjetnikUHTmlDokument(String ispis) {
		String isp = ispis;

		isp = isp.replaceFirst("<!--datum_podsjetnika-->",
				Util.convertCalendarToString(sad));
		isp = isp.replaceFirst("<!--pregledi_za_obaviti-->",
				vratiPregledeZaObavitiKaoHtml());
		isp = isp.replaceFirst("<!--pregledi_za_zakazati-->",
				vratiPregledeZaZakazatiKaoHtml());
		isp = isp.replaceFirst("<!--isteknuti_artikli-->",
				vratiIstekleArtikleKaoHtml());
		return isp;
	}// ugradiPodsjetnikUHTmlDokument

	private String vratiPregledeZaObavitiKaoHtml() {
		SearchCriteria sc = new SearchCriteria();
		ArrayList<Object> arl = new ArrayList<Object>();
		arl.add(sad);
		sc.setPodaci(arl);
		List<PregledVO> l;
		try {
			l = DAOFactory.getInstance().getPregledi().findAll(sc);
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod citanja pregleda koji se danas trebaju obaviti",
					e);
			return "Problem nastao pri pokušaju èitanja podataka. Kontaktirajte administratora!";
		}

		StringBuilder t = new StringBuilder( "<table>" );
		t.append( "<thead><tr style='font-weight:bold' align='bottom'><td>Klijent<td>Mjesto<td>Telefon<td>GSM<td>Vrijeme<td></tr></thead>");

		for (int i = 0; i < l.size(); i++) {
			PregledVO pvo = l.get(i);
			int sat, min;
			KlijentVO kvo = pvo.getKlijent();
			sat = pvo.getDatVrijeme().get(Calendar.HOUR_OF_DAY);
			min = pvo.getDatVrijeme().get(Calendar.MINUTE);
			String vrijeme = (sat < 10 ? "0" + sat : "" + sat) + ":"
					+ (min < 10 ? "0" + min : "" + min);

			t.append( "<tr><td>" + kvo.getIme() + " " + kvo.getPrezime() + "<td>"
					+ kvo.getMjesto().getNaziv() + "<td>" + kvo.getTel()
					+ "<td>" + kvo.getGsm() + "<td>" + vrijeme + "</tr>" );
		}// for i

		t.append( "</table>" );
		return t.toString();
	}// vratiPregledeZaObavitiKaoHtml

	private String vratiPregledeZaZakazatiKaoHtml() {
		String t = "<table>";
		List<KlijentVO> p = trebaZakazatiModel.getData();
		t += "<thead><tr style='font-weight:bold' align='bottom'><td>Klijent<td>Mjesto<td>Telefon<td>GSM<td>pregled zakazati<td></tr></thead>";

		for (int i = 0; i < p.size(); i++) {
			KlijentVO kvo = (KlijentVO) p.get(i);
			t += "<tr><td>" + kvo.getIme() + " " + kvo.getPrezime() + "<td>"
					+ kvo.getMjesto().getNaziv() + "<td>" + kvo.getTel()
					+ "<td>" + kvo.getGsm() + "<td>"
					+ Util.convertCalendarToString(kvo.getSlijedeciPregled())
					+ "</tr>";
		}// for i

		t += "</table>";
		return t;
	}// vratiPregledeZaObavitiKaoHtml

	private String vratiIstekleArtikleKaoHtml() {
		StringBuilder b = new StringBuilder(4096);
		b.append("<table>");
		ArrayList<ValueObject> p = (ArrayList<ValueObject>) istekliArtikliModel
				.getData();
		b.append("<thead><tr style='font-weight:bold' align='bottom'><td>Klijent<td>mjesto<td>telefon<td>roðen");
		b.append("<td>dat.kupnje<td align='center'>kupio<td>rok mj.</tr></thead>");

		int pSize = p.size();
		for (int i = 0; i < pSize; i++) {
			ValueObject kvo = (ValueObject) p.get(i);
			b.append("<tr><td>" + kvo.getValue("ime") + " ");
			b.append(kvo.getValue("prezime") + "<td>");
			b.append(kvo.getValue("mjesto") + "<td>");
			String telefon = (String) kvo.getValue("telefon");
			b.append(telefon == null ? "(nema)" : telefon);
			String rodj = (String) kvo.getValue("roð.");
			b.append("<td>" + (rodj == null ? "" : rodj) + "<td>");
			b.append(kvo.getValue("dat. izd."));
			b.append("<td>" + kvo.getValue("kupljeni artikl"));
			b.append("<td>" + kvo.getValue("mj."));
			b.append("</tr>");
		}// for i

		b.append("</table>");
		String res = b.toString();
		b.setLength(0);
		b = null;
		return res;
	}// vratiIstekleArtikleKaoHtml

}// DobroDosliPanel

class DobroDosliPanel_jbSinkronizacija_actionAdapter implements ActionListener {
	private DobroDosliPanel adaptee;

	DobroDosliPanel_jbSinkronizacija_actionAdapter(DobroDosliPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jbSinkronizacija_actionPerformed(e);
	}
}
