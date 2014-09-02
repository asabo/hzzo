/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.tablice.KalendarObvezaCellRenderer;
import biz.sunce.optika.tablice.KalendarObvezaModel;
import biz.sunce.toedter.calendar.JDateChooser;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.SlusacDateChoosera;

/**
 * datum:2005.07.11
 * 
 * @author asabo
 * 
 */
public final class ObvezePanel extends JPanel implements
		SlusacDateChoosera, SlusacKlijentiFramea,
		SlusacModelaTablice {
	private int vremInterval = -1;
	private int pocetakSat = -1, pocetakMinuta = -1;
	private int krajSat = -1, krajMinuta = -1;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtVremInterval = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel3 = null;
	private VrijemePanel jpPocetakRada = null;
	private VrijemePanel jpKrajRada = null;
	private javax.swing.JLabel jLabel4 = null;
	private JDateChooser jpPocetniDatum = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JComboBox jcOrdinarius = null;
	private javax.swing.JScrollPane jspKalendar = null;
	private JXTable jtbKalendar = null;
	private javax.swing.JScrollPane jspTrebaZakazati = null;
	private JXTable jtbTrebaZakazati = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel6 = null;

	private KalendarObvezaModel obvezeModel = null;
	private TableModel trebaZakazatiModel = null;
	private JFrame glavni = null;

	final ObvezePanel ob = this;

	private javax.swing.JButton jButton = null;
	private javax.swing.JButton jButton1 = null;

	/**
	 * This is the default constructor
	 */
	public ObvezePanel(JFrame glavni) {
		super();
		this.glavni = glavni;
		initialize();
		this.napuniLijecnike();
		this.datumIzmjenjen(this.getJpPocetniDatum());
		this.trebaZakazatiModel.dodajSlusaca(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints17 = new java.awt.GridBagConstraints();
		consGridBagConstraints17.gridy = 5;
		consGridBagConstraints17.gridx = 1;
		consGridBagConstraints14.gridy = 5;
		consGridBagConstraints14.gridx = 0;
		consGridBagConstraints5.gridy = 0;
		consGridBagConstraints5.gridx = 6;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints3.gridx = 1;
		consGridBagConstraints3.gridwidth = 4;
		consGridBagConstraints3.ipadx = 0;
		consGridBagConstraints3.ipady = 15;
		consGridBagConstraints3.weighty = 0.0D;
		consGridBagConstraints3.insets = new java.awt.Insets(15, 0, 0, 0);
		consGridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints16.weighty = 1.0;
		consGridBagConstraints16.weightx = 1.0;
		consGridBagConstraints16.gridy = 1;
		consGridBagConstraints16.gridx = 6;
		consGridBagConstraints16.gridheight = 4;
		consGridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints15.weighty = 100.0D;
		consGridBagConstraints15.weightx = 2.0D;
		consGridBagConstraints15.gridy = 6;
		consGridBagConstraints15.gridx = 0;
		consGridBagConstraints15.gridwidth = 7;
		consGridBagConstraints13.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints13.weightx = 1.0;
		consGridBagConstraints13.gridy = 3;
		consGridBagConstraints13.gridx = 4;
		consGridBagConstraints12.gridy = 3;
		consGridBagConstraints12.gridx = 3;
		consGridBagConstraints10.gridy = 1;
		consGridBagConstraints10.gridx = 3;
		consGridBagConstraints16.insets = new java.awt.Insets(0, 0, 0, 4);
		consGridBagConstraints11.gridy = 1;
		consGridBagConstraints11.gridx = 4;
		consGridBagConstraints9.gridy = 4;
		consGridBagConstraints9.gridx = 1;
		consGridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints4.gridy = 3;
		consGridBagConstraints4.gridx = 0;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints8.gridy = 3;
		consGridBagConstraints8.gridx = 1;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints6.gridy = 4;
		consGridBagConstraints6.gridx = 0;
		consGridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHEAST;
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints15.insets = new java.awt.Insets(2, 2, 7, 7);
		consGridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints16.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints15.ipadx = 1;
		consGridBagConstraints15.ipady = 1;
		consGridBagConstraints15.anchor = java.awt.GridBagConstraints.NORTH;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJtVremInterval(), consGridBagConstraints2);
		this.add(getJLabel2(), consGridBagConstraints4);
		this.add(getJLabel3(), consGridBagConstraints6);
		this.add(getJpPocetakRada(), consGridBagConstraints8);
		this.add(getJpKrajRada(), consGridBagConstraints9);
		this.add(getJLabel4(), consGridBagConstraints10);
		this.add(getJpPocetniDatum(), consGridBagConstraints11);
		this.add(getJLabel5(), consGridBagConstraints12);
		this.add(getJcOrdinarius(), consGridBagConstraints13);
		this.add(getJspKalendar(), consGridBagConstraints15);
		this.add(getJspTrebaZakazati(), consGridBagConstraints16);
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getJLabel6(), consGridBagConstraints5);
		this.add(getJButton(), consGridBagConstraints14);
		this.add(getJButton1(), consGridBagConstraints17);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(790*faktor, 580*faktor);
		this.setPreferredSize(new java.awt.Dimension(790*faktor, 580*faktor));
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Vrem. interval: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes jtVremInterval
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtVremInterval() {
		if (jtVremInterval == null) {
			jtVremInterval = new javax.swing.JTextField();
			jtVremInterval.setPreferredSize(new java.awt.Dimension(80, 20));
			jtVremInterval.setMinimumSize(new java.awt.Dimension(80, 20));
			int inter = this.getVremInterval();
			jtVremInterval.setText("" + inter);

			jtVremInterval.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					int interval = -1;
					String txt = jtVremInterval.getText();
					if (txt != null && txt.length() > 0)
						try {
							interval = Integer.parseInt(txt);
						} catch (NumberFormatException nfe) {
							interval = -1;
						}
					if (interval != -1)
						setVremInterval(interval);
				}// focusLost

			});
		}
		return jtVremInterval;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Poèetak rada: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("kraj rada: ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jpPocetakRada
	 * 
	 * @return javax.swing.JPanel
	 */
	private VrijemePanel getJpPocetakRada() {
		if (jpPocetakRada == null) {
			jpPocetakRada = new VrijemePanel();
			jpPocetakRada.setPreferredSize(new java.awt.Dimension(50, 20));
			jpPocetakRada.setMinimumSize(new java.awt.Dimension(50, 20));

			int sat = this.getPocetakSat();
			int min = this.getPocetakMinuta();

			jpPocetakRada.setVrijeme(sat, min);

			jpPocetakRada.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					int min = jpPocetakRada.getMinuta();
					int sat = jpPocetakRada.getSat();
					setPocetak(sat, min);

				}// focusLost
			});

		}
		return jpPocetakRada;
	}

	/**
	 * This method initializes jpKrajRada
	 * 
	 * @return javax.swing.JPanel
	 */
	private VrijemePanel getJpKrajRada() {
		if (jpKrajRada == null) {
			jpKrajRada = new VrijemePanel();
			jpKrajRada.setPreferredSize(new java.awt.Dimension(50, 20));
			jpKrajRada.setMinimumSize(new java.awt.Dimension(50, 20));

			int sat = this.getKrajSat();
			int min = this.getKrajMinuta();

			jpKrajRada.setVrijeme(sat, min);

			jpKrajRada.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					int h, m;
					h = jpKrajRada.getSat();
					m = jpKrajRada.getMinuta();
					setKraj(h, m);
				}
			});
		}
		return jpKrajRada;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("datum: ");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jpPocetniDatum
	 * 
	 * @return javax.swing.JPanel
	 */
	private JDateChooser getJpPocetniDatum() {
		if (jpPocetniDatum == null) {
			jpPocetniDatum = new JDateChooser();
			this.jpPocetniDatum.setDatum(Calendar.getInstance());
			jpPocetniDatum.dodajSlusaca(this);
		}
		return jpPocetniDatum;
	}

	private void napuniLijecnike() {
		List l = null;
		try {
			l = DAOFactory.getInstance().getLijecnici().findAll(null);
			if (l != null) {
				JComboBox jc = this.getJcOrdinarius();

				if (jc == null) {
					Logger.fatal(
							"Combo BOX sa lijecnicima je prazan kod ObvezePanel.napuniLijecnike!!!",
							null);
					return;
				}

				jc.removeAllItems();

				int ls = l.size();
				for (int i = 0; i < ls; i++)
					jc.addItem(l.get(i));

			}// if
		} catch (SQLException sqle) {
			Logger.fatal("SQL iznimka kod ObvezePanel.napuniLijecnike", sqle);
		}
	}// napuniLijecnike

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("ordinarius: ");
		}
		return jLabel5;
	}

	/**
	 * This method initializes jcOrdinarius
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getJcOrdinarius() {
		if (jcOrdinarius == null) {
			jcOrdinarius = new javax.swing.JComboBox();
			jcOrdinarius.setPreferredSize(new java.awt.Dimension(120, 25));
			jcOrdinarius.setToolTipText("ijeènik koji æe obaviti pregled");
		}
		return jcOrdinarius;
	}

	/**
	 * This method initializes jtbKalendar
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getJtbKalendar() {
		if (jtbKalendar == null) {
			jtbKalendar = new JXTable();

			// Set a pink background and tooltip for the Color column renderer.
			DefaultTableCellRenderer iscrtavacVrijemeKolone = new DefaultTableCellRenderer();
			iscrtavacVrijemeKolone.setBackground(Color.LIGHT_GRAY);
			iscrtavacVrijemeKolone.setToolTipText("vrijeme...");

			this.obvezeModel = new KalendarObvezaModel(DAOFactory.getInstance()
					.getPregledi(), jtbKalendar, this.getJcOrdinarius(),
					iscrtavacVrijemeKolone);
			jtbKalendar.setModel(this.obvezeModel);
			jtbKalendar.setRowHeight(25);
			jtbKalendar.setRowSelectionAllowed(false);
			jtbKalendar.setColumnSelectionAllowed(false);

			// this.obvezeModel.setPocetak(this.jpPocetakRada.getSat(),this.jpPocetakRada.getMinuta());
			// this.obvezeModel.setKraj(this.jpKrajRada.getSat(),this.jpKrajRada.getMinuta());
 
			KalendarObvezaCellRenderer kcel = new KalendarObvezaCellRenderer();

			PretrazivanjeProzor pret = new PretrazivanjeProzor(this.glavni,
					DAOFactory.getInstance().getKlijenti(), 0, 0, 150, 100,
					jtbKalendar);
			// TextFieldEditorPolje tfed=new TextFieldEditorPolje(null);
			// jtbKalendar.setDefaultRenderer(String.class,pokusni);
			jtbKalendar.setDefaultEditor(String.class, pret);
			jtbKalendar.setDefaultRenderer(String.class, kcel);
			TableColumn vrijemeKolona = jtbKalendar.getColumn("vrijeme");

			vrijemeKolona.setCellRenderer(iscrtavacVrijemeKolone);
			vrijemeKolona.setMaxWidth(60);

		}
		return jtbKalendar;
	}

	/**
	 * This method initializes jspKalendar
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspKalendar() {
		if (jspKalendar == null) {
			jspKalendar = new javax.swing.JScrollPane();
			jspKalendar.setViewportView(getJtbKalendar());
			jspKalendar.setPreferredSize(new java.awt.Dimension(0, 0));

		}
		return jspKalendar;
	}

	/**
	 * This method initializes jtbTrebaZakazati
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getJtbTrebaZakazati() {
		if (jtbTrebaZakazati == null) {
			jtbTrebaZakazati = new JXTable();
			jtbTrebaZakazati.setRowHeight(15);
			this.trebaZakazatiModel = new TableModel(DAOFactory.getInstance()
					.getKlijenti(), jtbTrebaZakazati);
			this.jtbTrebaZakazati.setModel(this.trebaZakazatiModel);
		}
		return jtbTrebaZakazati;
	}

	/**
	 * This method initializes jspTrebaZakazati
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspTrebaZakazati() {
		if (jspTrebaZakazati == null) {
			jspTrebaZakazati = new javax.swing.JScrollPane();
			jspTrebaZakazati.setViewportView(getJtbTrebaZakazati());
			int faktor = GlavniFrame.getFaktor();
			jspTrebaZakazati.setPreferredSize(new java.awt.Dimension(300*faktor, 150*faktor));
			jspTrebaZakazati.setMaximumSize(new java.awt.Dimension(300*faktor, 50*faktor));
		}
		return jspTrebaZakazati;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Naruèeni pregledi");
			jLabel1.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
		}
		return jLabel1;
	}

	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Trebalo bi zakazati: ");
		}
		return jLabel6;
	}

	public int getVremInterval() {
		if (vremInterval == -1)
			vremInterval = PostavkeBean.getIntPostavkaSustava(
					"VREMENSKI_INTERVAL", 15);
		return vremInterval;
	}

	public void setVremInterval(int i) {
		vremInterval = i;
		this.obvezeModel.setInterval(vremInterval);
		PostavkeBean.setPostavkaDB("VREMENSKI_INTERVAL", ""+i);
	}

	public void setKraj(int sat, int minuta) {
		krajSat = sat;
		krajMinuta = minuta;
		this.obvezeModel.setKraj(sat, minuta);
	}

	public void setPocetak(int sat, int minuta) {
		pocetakSat = sat;
		pocetakMinuta = minuta; // zasada nije potrebno, ali neka ide..
		this.obvezeModel.setPocetak(sat, minuta);
	}
 

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton() {
		if (jButton == null) {
			jButton = new javax.swing.JButton();
			jButton.setText("Novi klijent");
			jButton.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					KlijentFrame kf = new KlijentFrame();
					kf.dodajSlusaca(ob);
					kf.show();
				}
			});
		}
		return jButton;
	}

	public int getPocetakMinuta() {
		if (pocetakMinuta == -1)
			pocetakMinuta = PostavkeBean.getIntPostavkaDb(
					"POCETAK_RADA_MINUTA", 0);
		return pocetakMinuta;
	}

	public void setPocetakMinuta(int i) {
		pocetakMinuta = i;
		PostavkeBean.setPostavkaDB("POCETAK_RADA_MINUTA", ""+i);
	}

	public int getKrajSat() {
		if (krajSat == -1)
			krajSat = PostavkeBean.getIntPostavkaDb("KRAJ_RADA_SAT", 20);
		return krajSat;
	}

	public void setKrajSat(int i) {
		krajSat = i;
		PostavkeBean.setPostavkaDB("KRAJ_RADA_SAT", ""+i);
	}

	public int getKrajMinuta() {
		if (krajMinuta == -1)
			krajMinuta = PostavkeBean.getIntPostavkaDb("KRAJ_RADA_MINUTA",
					0);
		return krajMinuta;
	}

	public void setKrajMinuta(int i) {
		krajMinuta = i;
		PostavkeBean.setPostavkaDB("KRAJ_RADA_MINUTA", ""+i);
	}

	public int getPocetakSat() {
		if (pocetakSat == -1)
			pocetakSat = PostavkeBean.getIntPostavkaDb("POCETAK_RADA_SAT",
					8);
		return pocetakSat;
	}

	public void setPocetakSat(int i) {
		pocetakSat = i;
		PostavkeBean.setPostavkaDB("POCETAK_RADA_SAT", ""+i);
	}

	public void klijentIzmjenjen(KlijentFrame pozivac) {
		this.trebaZakazatiModel.updatePodataka();
	}

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		if (posiljatelj == this.trebaZakazatiModel) {
			KlijentVO kvo = (KlijentVO) this.trebaZakazatiModel.getData().get(
					redak);
			KlijentFrame kf = new KlijentFrame(kvo);
			// kf.setOznaceni(kvo);
			kf.show();
		}
	}

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new javax.swing.JButton();
			jButton1.setText("Ispiši");
			jButton1.setToolTipText("Ispisujete kalendar obveza na štampaèu");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ispisObveza();
				}
			});
		}
		return jButton1;
	}

	private void ispisObveza() {
		HtmlPrintParser parser = new HtmlPrintParser();
		String sIspis = parser
				.ucitajHtmlPredlozak(Konstante.PREDLOZAK_TJEDNE_OBVEZE);
		sIspis = ugradiUHtmlDokument(sIspis);

		HtmlPrintParser.ispisHTMLDokumentaNaStampac(sIspis, true, "ispis_obveza");

	}

	private String ugradiUHtmlDokument(String ispis) {
		String isp = ispis;
		isp = isp.replaceFirst("<!--pregled_obaveza-->",
				generirajTablicuObvezaHtml());
		isp = isp.replaceFirst("<!--datum-->",
				Util.convertCalendarToString(getJpPocetniDatum().getDatum()));
		isp = isp.replaceFirst("<!--lijecnik-->", this.jcOrdinarius
				.getSelectedItem().toString());
		return isp;
	}

	private String generirajTablicuObvezaHtml() {
		String t = "<table>";
		int kolona = this.obvezeModel.getColumnCount();
		int redaka = this.obvezeModel.getRowCount();

		t += "<thead><tr style='font-weight:bold' align='center'>";
		for (int i = 0; i < kolona; i++)
			t += "<td>" + this.obvezeModel.getColumnName(i);
		t += "<td></tr></thead>";

		for (int i = 0; i < redaka; i++) {
			t += "<tr>";
			for (int j = 0; j < kolona; j++) {
				KlijentVO kvo = null;
				PregledVO pvo = null;

				if (this.obvezeModel.getValueAt(i, j) != null) {
					if (this.obvezeModel.getValueAt(i, j) instanceof String) {
						t += "<td><b>" + this.obvezeModel.getValueAt(i, j)
								+ "</b>";
					} else if (this.obvezeModel.getValueAt(i, j) instanceof PregledVO) {
						pvo = (PregledVO) this.obvezeModel.getValueAt(i, j);
						kvo = pvo.getKlijent();
					}

					if (pvo != null)
						t += "<td><i>" + kvo.getIme() + " " + kvo.getPrezime()
								+ "</i><br><i>" + kvo.getMjesto().getNaziv()
								+ "</i>";
				} else
					t += "<td>";
			}// for j
			t += "</tr>";
		}// for i

		t += "</table>";
		return t;
	}// generirajTablicuObvezaHtml

	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		if (pozivatelj == this.jpPocetniDatum)
			this.obvezeModel.setStartniDatum(this.jpPocetniDatum.getDatum());
		this.trebaZakazatiModel.setFilter(this.jpPocetniDatum.getDatum());
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
