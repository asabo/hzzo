/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.RacunDAO;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.dao.gui.Racun;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.GUI;
import biz.sunce.util.KontrolneZnamenkeUtils;
import biz.sunce.util.RacuniUtil;
import biz.sunce.util.StringUtils;
import biz.sunce.util.beans.PostavkeBean;

import java.awt.Dimension;
 
 
/**
 * datum:2006.03.04
 * 
 * @author asabo
 * 
 */
public final class HzzoRacunPanel extends JPanel implements
		SlusacModelaTablice, GUIEditor.SlusacSpremnostiPodataka {

	//IDovi poreznih stopa
	private static final int POREZNA_STOPA_0 = 2;
	private static final int POREZNA_STOPA_5 = 12;

	private static final long serialVersionUID = -5137132339677509200L;

	private static final Calendar prviPrvi2013 = Calendar.getInstance();
 	
	static{
		prviPrvi2013.set(Calendar.YEAR, 2013);
		prviPrvi2013.set(Calendar.MONTH, 0);
		prviPrvi2013.set(Calendar.DAY_OF_MONTH,1);
	}

	private javax.swing.JPanel jpRacunPanel = null;
	private javax.swing.JPanel jpStavkaRacuna = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private JXTable jtbStavkeRacuna = null;
	private RacunDAO racuni = null;
	private StavkaRacunaDAO stavke = null;
	
	//za slucaj da program azurira sudjelovanje, tu ce sjediti zadnja vrijednost
	//koju cemo usporedjivati sa onom na formi i po tome znati jeli korisnik sam mjenjao ili nije
	private String azuriranoSudjelovanje=null;

	private RacunVO oznaceni = null;
	private TableModel<StavkaRacunaVO> stavkeRacunaModel = null;
	private ArrayList<StavkaRacunaVO> stavkeRacuna = null;
	private javax.swing.JButton jbPohrani = null;  

	private javax.swing.JButton jbDodaj = null;
	private javax.swing.JPanel jpStavkaSaDodajGumbom = null;
	private javax.swing.JLabel jlUkupno = null;

	/**
	 * This is the default constructor
	 */
	public HzzoRacunPanel(RacunVO rvo) {
		super();

		initialize();
		this.revalidate();
		this.repaint();
		//GlavniFrame.getInstanca().pack();
		if (rvo != null)
			ucitajRacun(rvo);

		Thread t = new Thread() {
			@Override
			public void run() {
				this.setPriority(Thread.MIN_PRIORITY);
				try {
					yield();
					Thread.sleep(200);
					yield();
				} catch (InterruptedException inte) {
					return;
				}
				Racun rcp = (Racun) getJpRacunPanel();
				yield();
				rcp.getJtPodruznica().requestFocusInWindow();
			}
		};

		SwingUtilities.invokeLater(t);
	}

	// daje stavkeDAO, ne i same stavke racuna
	private final StavkaRacunaDAO getStavke() {
		if (this.stavke == null)
			this.stavke = DAOFactory.getInstance().getStavkeRacuna();
		return this.stavke;
	}// getStavke

	private final RacunDAO getRacuni() {
		if (this.racuni == null)
			this.racuni = DAOFactory.getInstance().getRacuni();
		return this.racuni;
	}// getRacuni

	// tjera seditora da mu vrati najsvjezije podatke prije vracanja objekta
	// ne pristupati this.oznaceni zato direktno!
	private RacunVO getOznaceni() {
		GUIEditor<RacunVO> ed = (GUIEditor<RacunVO>) this.getJpRacunPanel();
		this.oznaceni =  ed.vratiPodatke();
		return this.oznaceni;
	}// getOznaceni

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		consGridBagConstraints12.gridwidth = 2;
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		consGridBagConstraints12.gridy = 4;
		consGridBagConstraints12.gridx = 2;
		consGridBagConstraints31.gridy = 2;
		consGridBagConstraints31.gridx = 3;
		consGridBagConstraints21.gridy = 1;
		consGridBagConstraints21.gridx = 0;
		consGridBagConstraints11.gridx = 3;
		consGridBagConstraints11.gridy = 4;
		consGridBagConstraints3.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints3.weighty = 1.0;
		consGridBagConstraints3.weightx = 1.0;
		consGridBagConstraints3.gridy = 3;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints1.weighty = 1.0;
		consGridBagConstraints1.weightx = 1.0;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.CENTER;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.SOUTHEAST;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints3.gridwidth = 5;
		consGridBagConstraints1.gridwidth = 4;
		consGridBagConstraints11.insets = new java.awt.Insets(0, 0, 2, 0);
		consGridBagConstraints3.insets = new java.awt.Insets(2, 0, 0, 0);
		consGridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints21.ipadx = 2;
		consGridBagConstraints21.ipady = 2;
		consGridBagConstraints21.gridwidth = 5;
		consGridBagConstraints31.anchor = java.awt.GridBagConstraints.CENTER;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJpRacunPanel(), consGridBagConstraints1);
		this.add(getJScrollPane(), new GridBagConstraints(3, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJbPohrani(), consGridBagConstraints11);
		this.add(getJpStavkaSaDodajGumbom(), new GridBagConstraints(3, 1, 1, 2, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 1, 0), 2, 2));
		this.add(getJlUkupno(), consGridBagConstraints12);
		int faktor = GlavniFrame.getFaktor();
		//this.setSize(790*faktor, 580*faktor);
		this.setToolTipText("forma za unos hzzo raèuna");
		this.setPreferredSize(new Dimension(800, 596));
		this.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
		this.setLocation(2, 18);
		this.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				// za slucaj nestajanja panela, da vidimo jeli mozda prazan
				// racun na formi
				if (stavkeRacuna != null && stavkeRacuna.size() == 0) {
					brisiRacun(); // just in case, ne mozemo imati racun sa nula
									// stavki
					// kada stranka doda novu stavku, racun ce se opet 'sam'
					// kreirati
				}
			}
		});

		this.revalidate();
	}

	/**
	 * This method initializes jpRacunPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpRacunPanel() {
		if (jpRacunPanel == null) {
			jpRacunPanel = new JPanel();
			jpRacunPanel.setToolTipText("glava ra\u010Duna");
			jpRacunPanel.setLayout(null);
			if (true)
				this.jpRacunPanel = (JPanel) (getRacuni() != null ? getRacuni()
						.getGUIEditor() : null);

			jpRacunPanel.setToolTipText("zaglavlje raèuna, opæi podaci... ");
			
			// jpRacunPanel.revalidate();
		}
		return jpRacunPanel;
	}

	/**
	 * This method initializes jpStavkaRacuna
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpStavkaRacuna() {
		if (jpStavkaRacuna == null) {
			jpStavkaRacuna = new JPanel();
			GUIEditor<StavkaRacunaVO> stavka = this.getStavke() != null ? this.getStavke()
					.getGUIEditor() : null;
			if (true)
				this.jpStavkaRacuna = (JPanel) (stavka);
			int faktor = GlavniFrame.getFaktor();
			jpStavkaRacuna.setPreferredSize(new java.awt.Dimension(435*faktor, 114*faktor));
			jpStavkaRacuna.setMaximumSize(new java.awt.Dimension(435*faktor, 114*faktor));
			jpStavkaRacuna.setMinimumSize(new java.awt.Dimension(435*faktor, 114*faktor));
			jpStavkaRacuna.setToolTipText("stavka raèuna");
			stavka.dodajSlusacaSpremnostiPodataka(this);

		}// if
		return jpStavkaRacuna;
	}

	/**
	 * This method initializes jtbStavkeRacuna
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getJtbStavkeRacuna() {
		if (jtbStavkeRacuna == null) {
			jtbStavkeRacuna = new JXTable();
			this.stavkeRacunaModel = new TableModel(this.getStavke(),
					jtbStavkeRacuna);
			this.stavkeRacunaModel.dodajSlusaca(this);
			jtbStavkeRacuna.setModel(this.stavkeRacunaModel);
		
			DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
			rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			jtbStavkeRacuna.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
			
		}
		return jtbStavkeRacuna;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getJtbStavkeRacuna());
			jScrollPane.setPreferredSize(new Dimension(600, 222));
			jScrollPane.setMinimumSize(new java.awt.Dimension(545, 205));

			jScrollPane.setToolTipText("stavke raèuna");
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jbPohrani
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPohrani() {
		if (jbPohrani == null) {
			jbPohrani = new javax.swing.JButton();
			jbPohrani.setText("Zakljuèi");
			jbPohrani
					.setToolTipText("po završetku unosa raèuna kliknite ovdje");
			jbPohrani.setMnemonic(java.awt.event.KeyEvent.VK_Z);
			jbPohrani.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					zakljuciRacun();
				}
			});
		}
		return jbPohrani;
	}

	// ponovno iz baze podataka povlaci stavke za doticni racun, pohranjuje ih u
	// this.stavkeRacuna za kasniju upotrebu
	// te osvjezava tablicu sa stavkama
	private void osvjeziStavke() {
		if (this.getOznaceni() == null || this.stavke == null)
			return;

		try {
			this.stavkeRacuna = (ArrayList<StavkaRacunaVO>) this.getStavke()
					.findAll(this.getOznaceni());
			this.stavkeRacunaModel.setData(this.stavkeRacuna);
			int stavkeSize = this.stavkeRacuna != null ? this.stavkeRacuna
					.size() : -1;
			if (this.stavkeRacuna != null && stavkeSize > 0) {
				int ukupno = 0;

				ukupno = RacuniUtil.izracunajTotalRacuna(this.stavkeRacuna);

				int kn, lp;
				String sUk;
				kn = ukupno / 100;
				lp = ukupno % 100;
				sUk =  kn + "." + (lp < 10 ? "0" + lp : "" + lp);
				this.jlUkupno.setText("Ukupno: " + sUk);
				
				azurirajSudjelovanje(ukupno);
				
				this.jtbStavkeRacuna.packAll();
			} else
				this.jlUkupno.setText("");

		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod povlaèenja stavki raèuna: "
							+ (this.getOznaceni() != null
									&& this.getOznaceni().getSifra() != null ? this
									.getOznaceni().getSifra().intValue()
									: -1), e);
		}
	}// osvjeziStavke

	private void zakljuciRacun() {
		// pohranjivanje zaglavlja racuna prvo
		try
		{
		this.jbPohrani.setEnabled(false);
		if (pohraniRacun()) {
			RacunVO rvo = this.getOznaceni();
			ArrayList<StavkaRacunaVO> stavkeR = null;
			try {
				stavkeR = (ArrayList<StavkaRacunaVO>) getStavke().findAll(rvo);
				rvo.setStavkeRacuna(stavkeR);
			} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod punjenja stavki racuna u racun",
						e);
				JOptionPane
						.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Nastao je problem kod èitanja stavki raèuna. Kontaktirajte administratora!",
								"Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int iznosRacuna = 0;
			int stavkeSize = stavkeR==null?-1:stavkeR.size();
			
			if (stavkeR == null) {
				Logger.log(
						"prazna lista stavki racuna kod provjeravanja zbroja stavki",
						null);
				return; // nemoguca situacija
			} else if (stavkeSize == 0) {
				GlavniFrame.alert("Vaš raèun nema niti jednu stavku!");
				return;
			}

			iznosRacuna = RacuniUtil.izracunajTotalRacuna(stavkeR);

			// iznos sudjelovanja isti je broj, a odnosi se na placanje stranke
			// ili placanje dopunskog osiguranja
			// ovisno o tipu racuna
			int iznSudjelovanja = 0, iznOsnovnogOsiguranja = 0;

			iznSudjelovanja = rvo.getIznosSudjelovanja()==null? 0 : rvo.getIznosSudjelovanja().intValue();

			iznOsnovnogOsiguranja = iznosRacuna - iznSudjelovanja;

			if (iznOsnovnogOsiguranja < 0) {
				JOptionPane
						.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Iznos sudjelovanja na teret stranke ili dop. osiguranja\n nije ispravan! (prevelik)",
								"Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}

			rvo.setIznosOsnovnogOsiguranja(Integer.valueOf(iznOsnovnogOsiguranja));

			// updateati iznos osnovnog osiguranja,
			// ne raditi to preko pohraniRacun jer on nanovo ucitava sa forme
			// cijeli objekt, da se ne bi sta ponistilo..
			try {
				getRacuni().update(rvo);
			} catch (SQLException e1) {
				Logger.fatal(
						"SQL iznimka kod updatea zaglavlja raèuna (iznos osnovnog osiguranja)",
						e1);
			} catch (Exception e1) {
				Logger.fatal("Opæenita iznimka kod updatea zaglavlja raèuna",
						e1);
			}

			// u principu nemamo sta provjeravati, ali trebat cu ovaj kod za
			// kasnije
			if (iznSudjelovanja + iznOsnovnogOsiguranja != iznosRacuna) {
				JOptionPane
						.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Raèun nije ispravan, iznos sudjelovanja i tereta osnovnog zdr. osiguranja\n se ne poklapaju za ukupnim iznosom: sudjelovanje="
										+ iznSudjelovanja
										+ " osn.osiguranje="
										+ iznOsnovnogOsiguranja
										+ " raèun: "
										+ iznosRacuna, "Upozorenje",
								JOptionPane.WARNING_MESSAGE);
				return;
			}

			// otvaramo mu novi Frame pa nek unutra bira koje ce sve obrasce
			// printati ...
			HzzoIzborIspisaRacuna izborIspisa = new HzzoIzborIspisaRacuna();
			izborIspisa.setRacun(rvo);

			izborIspisa.pack();
			izborIspisa.setVisible(true);
			GUI.centrirajFrame(izborIspisa);

			// ciscenje elemenata sa forme
			brisiFormu();
		}// if racun uspjesno pohranjen
		}
		finally
		{
			jbPohrani.setEnabled(true);
		}
	}// zakljuciRacun

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		GUIEditor ed = (GUIEditor) this.jpStavkaRacuna;
		List<ValueObject> data = posiljatelj.getData();
		if (data!=null && redak>=0 && data.size()>redak)
		 ed.napuniPodatke((ValueObject) data.get(redak));
	}

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	/**
	 * This method initializes jbDodaj
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbDodaj() {
		if (jbDodaj == null) {
			jbDodaj = new javax.swing.JButton();
			jbDodaj.setText("Dodaj");
			jbDodaj.setMnemonic(java.awt.event.KeyEvent.VK_A);
			jbDodaj.setToolTipText("ALT-A za dodati stavku u raèun");
			jbDodaj.setFont(new Font("Arial", Font.PLAIN, 11));
			jbDodaj.setPreferredSize(new java.awt.Dimension(63, 21));
			jbDodaj.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dodajStavkuURacun();
				}
			});
		}
		return jbDodaj;
	}

	/**
	 * This method initializes jpStavkaSaDodajGumbom
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpStavkaSaDodajGumbom() {
		if (jpStavkaSaDodajGumbom == null) {
			jpStavkaSaDodajGumbom = new javax.swing.JPanel();
			FlowLayout jpStavkaSaDodajGumbomLayout = new FlowLayout();
			jpStavkaSaDodajGumbomLayout.setVgap(1);
			jpStavkaSaDodajGumbom.setLayout(jpStavkaSaDodajGumbomLayout);

			jpStavkaSaDodajGumbom.add(getJpStavkaRacuna(), null);
			jpStavkaSaDodajGumbom.add(getJbDodaj());
			int faktor = GlavniFrame.getFaktor();
			jpStavkaSaDodajGumbom.setPreferredSize(new Dimension(524, 112));
			jpStavkaSaDodajGumbom.setMinimumSize(new java.awt.Dimension(515*faktor,
					112*faktor));
		}
		return jpStavkaSaDodajGumbom;
	}

	// ciscenje elemenata sa forme
	@SuppressWarnings("unchecked")
	private void brisiFormu() {
		GUIEditor<RacunVO> ed = (GUIEditor<RacunVO>) this.jpRacunPanel;
		ed.pobrisiFormu();
		getOznaceni();

		GUIEditor<StavkaRacunaVO> sed = (GUIEditor<StavkaRacunaVO>) this.jpStavkaRacuna;
		sed.pobrisiFormu();

		// nova stavka unutra za biti spremno
		StavkaRacunaVO svo = new StavkaRacunaVO();
		svo.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
		sed.napuniPodatke(svo);

		this.oznaceni = null;
		this.stavkeRacuna = null;
		this.azuriranoSudjelovanje = null;

		this.stavkeRacunaModel.setData(null);
		this.jlUkupno.setText("");
	}

	private boolean pohraniRacun() {
		boolean rez = true;
		RacunVO rvo = this.getOznaceni();

		if (rvo == null) {
			JOptionPane
					.showMessageDialog(
							this.getParent(),
							"Nastao je ozbiljan problem pri pohranjivanju raèuna. Molimo kontaktirajte administratora sustava",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		// provjerava jeli objekt ispravan prije spremanja u bazu podataka
		String poruka = getRacuni().narusavaLiObjektKonzistentnost(rvo);
		if (poruka != null) {
			boolean upozorenje = false;

			if (poruka.startsWith("@")) {
				poruka = poruka.substring(1);
				upozorenje = true;
			}

			if (!upozorenje) // necemo prikazivati upozorenja, zasada..
				GlavniFrame.alert(poruka);
			else 
				if (poruka!=null)
					GlavniFrame.info(poruka+" - raèun ce ipak biti proknji\u017Een kao ispravan!");
			
			if (!upozorenje)
				return false;
		}// if poruka nije prazna

		if (rvo.getSifra().intValue() == DAO.NEPOSTOJECA_SIFRA) {
			try {
				getRacuni().insert(rvo);

				boolean trebaUpdate = false;

				// 09.04.06. -asabo- dodano
				if (rvo.getBrojOsobnogRacunaOsnovno() == null
						|| rvo.getBrojOsobnogRacunaOsnovno().trim().equals("")) {
					rvo.setBrojOsobnogRacunaOsnovno(rvo.getSifra() != null ? ""
							+ rvo.getSifra().intValue() : "?!?");
					trebaUpdate = true;
				}

				// dodatak, ako se racun prvi puta unosi u db, te ako mu je
				// poziv na broj prazan, treba
				// ugraditi poziv na broj, a stranka ce ga pobrisati ako joj ne
				// pase
				// ugradjujemo samo ako vec nije nesto gore postavljeno
				if (PostavkeBean.getPostavkaDB(
						PostavkeBean.TVRTKA_HZZO_RACUN_AUTOM_POZIV_BROJ, "D")
						.equals("D")
						&& rvo.getPozivNaBroj2() == null) {
					int sfp = GlavniFrame.getSifraPoslovniceZaSynch();
					String poziv2 = rvo.getDatumIzdavanja().get(
							java.util.Calendar.YEAR)
							+ (sfp > -1 ? "-" + sfp : "")
							+ "-"
							+ rvo.getSifra().intValue();
					rvo.setPozivNaBroj1("01");
					String kontrolni = KontrolneZnamenkeUtils
							.kontrolnaZnamenkaZaModel("01", poziv2);
					rvo.setPozivNaBroj2(poziv2 + kontrolni);

					trebaUpdate = true;
				}// if treba dodati poziv na broj

				// jos jednom updateati ga
				if (trebaUpdate) {
					getRacuni().update(rvo);
					GUIEditor ed = (GUIEditor) this.getJpRacunPanel();
					ed.napuniPodatke(rvo);
				}// if treba update
			} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod insertiranja raèuna", e);
				JOptionPane.showMessageDialog(this.getParent(),
						"Nastao je problem pri spremanju raèuna",
						"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				return false;
			} catch (Exception e) {
				Logger.fatal("Opæenita iznimka kod insertiranja raèuna", e);
				JOptionPane.showMessageDialog(this.getParent(),
						"Nastao je problem pri spremanju raèuna",
						"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}// if
		else {
			try {
				getRacuni().update(rvo);
			} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod updateanja raèuna", e);
				JOptionPane.showMessageDialog(this.getParent(),
						"Nastao je problem pri spremanju izmjenjenog raèuna",
						"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				return false;
			} catch (Exception e) {
				Logger.fatal("Opæenita iznimka kod updateanja raèuna", e);
				JOptionPane.showMessageDialog(this.getParent(),
						"Nastao je problem pri spremanju raèuna",
						"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}// else

		return rez;
	}// pohraniRacun

	boolean azurirajIznosSudjelovanja = PostavkeBean.isAutomatskoRacunanjeSudjelovanja();
	
	private void azurirajSudjelovanje(int ukupno)
	{
		if (!azurirajIznosSudjelovanja)
			return;

		Racun rcp = (Racun) getJpRacunPanel();

		String iznSudjForma = rcp.getJtIznosSudjelovanja().getText().trim();

		if  (
				(azuriranoSudjelovanje == null && iznSudjForma.equals(""))
				|| (azuriranoSudjelovanje != null && iznSudjForma.equals(azuriranoSudjelovanje))
		    ) 
		   {
			float iznos;

			iznos = izracunajSudjelovanje(ukupno);

			final String str = "" + iznos;

			rcp.getJtIznosSudjelovanja().setText(str);
			GUI.odradiUpozorenjeNaElementu(rcp.getJtIznosSudjelovanja(), "Automatski smo izmjenili iznos sudjelovanja!", Color.yellow);
			azuriranoSudjelovanje = str;
		}// if
	}

	private float izracunajSudjelovanje(int ukupno) {
		float iznos;
		float sudj = (((float) ukupno) * 0.2f);

		if (sudj < 5000.0)
			sudj = 5000.0f;

		int sudjInt = (int) (sudj + 0.5f);

		iznos = ((float) sudjInt) / 100.0f;
		
		return iznos;
	}
	
	// provjerava jeli racun posjeduje sifru, ako ne
	// poziva metodu za pohranjivanje racuna, povratkom koje ako je nazad doslo
	// true
	// pokusava ponovno unijeti stavku racuna
	private boolean dodajStavkuURacun() {
		try {

			boolean rez = true;

			@SuppressWarnings("unchecked")
			GUIEditor<StavkaRacunaVO> ed = (GUIEditor<StavkaRacunaVO>) this.jpStavkaRacuna;
			StavkaRacunaVO srvo = null;

			srvo = (StavkaRacunaVO) ed.vratiPodatke();

			if (srvo == null) {
				JOptionPane
						.showMessageDialog(
								this.getParent(),
								"Nastao je problem pri pohranjivanju stavke raèuna. Molimo kontaktirajte administratora sustava",
								"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				return false;
			}

			// treba brisati stavku iz racuna
			if (srvo.getKolicina().intValue() == 0
					&& postojiLiStavkaURacunu(srvo)) {
				brisiStavkuIzRacuna(srvo);
				ed.pobrisiFormu(); // prazna forma
				osvjeziStavke(); // napuni tablicu sa stavkama, postavi novi
									// objekt sa stavkama...
				// nova stavka u nju, makar je uništio klik na tablicu... uvijek
				// mora postojati prazna stavka u formi
				StavkaRacunaVO svo = new StavkaRacunaVO();
				svo.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
				ed.napuniPodatke(svo);

				if (this.stavkeRacuna != null && this.stavkeRacuna.size() == 0) {
					brisiRacun(); // just in case, ne mozemo imati racun sa nula
									// stavki
					// kada stranka doda novu stavku, racun ce se opet 'sam'
					// kreirati
				}

				return true;
			}// if
			else if (srvo.getKolicina().intValue() == 0)
				return true; // netko se zajebaje, prvu stavku stavlja 0
								// komada...

			// >=7 je ispravno, jer ako je ==7, vec ima 7 komada unutra, a ovdje
			// smo dosli do tocke da nema update-a
			if (stavkeRacuna != null && stavkeRacuna.size() >= 7
					&& !postojiLiStavkaURacunu(srvo)) {
				JOptionPane.showMessageDialog(GlavniFrame.getInstanca(),
						"Možete unijeti maksimalno 7 stavki u raèun",
						"upozorenje", JOptionPane.WARNING_MESSAGE);
				return false;
			}

			// ako racun nije pohranjen, treba ga pohraniti
			if (getOznaceni().getSifra().intValue() == DAO.NEPOSTOJECA_SIFRA) {
				boolean tmp = pohraniRacun();
				if (!tmp)
					return false;
			}// if

			// moramo sad odmah postaviti da se provjera konzistentnosti ne
			// javi..
			srvo.setSifRacuna(getOznaceni().getSifra());

			String por = this.getStavke().narusavaLiObjektKonzistentnost(srvo);

			if (por != null) {
				boolean upozorenje = false;
				if (por.startsWith("@")) {
					por = por.substring(1);
					upozorenje = true;
				}

				if (!upozorenje) 
					JOptionPane.showMessageDialog(this.getParent(), por,
							upozorenje ? "Napomena.." : "Upozorenje!",
							JOptionPane.WARNING_MESSAGE);
				if (!upozorenje)
					return false;
			}

			// treba nam cijelo pomagalo da nadjemo koja je porezna skupina
			PomagaloVO pvo = null;

			try {
				pvo = (PomagaloVO) DAOFactory.getInstance().getPomagala()
						.read(srvo.getSifArtikla());

				// 20.03.06. -asabo- dodatak; ako je cijena >0, spremit cemo je
				// u pomagalo i 'zapamtiti'
				if (srvo.getPoCijeni() != null
						&& srvo.getPoCijeni().intValue() > 0) {
					pvo.setCijenaSPDVom(srvo.getPoCijeni());

					DAOFactory.getInstance().getPomagala().update(pvo); // zapamti
																		// ...
				}
			} catch (SQLException e1) {
				Logger.fatal("SQL iznimka kod traženja pomagala za stavku "
						+ srvo.getSifArtikla(), e1);
			}

			// podesavanje porezne skupine za stavku koja se unosi
			if (pvo != null){
			
				srvo.setPoreznaStopa(pvo.getPoreznaSkupina());
				
				// pripaziti ako se radi o racunu prije 1.1.2013 da se ne postavi porezna stopa od 5%
				if (srvo.getPoreznaStopa().intValue()==POREZNA_STOPA_5){
					Calendar datIzd = getOznaceni().getDatumIzdavanja();
					if (datIzd.before(prviPrvi2013)){
						srvo.setPoreznaStopa(POREZNA_STOPA_0);
					}
				}
			}
			
			// ako je stavka prethodno uspjesno insertirana sustav ce upisati
			// pod njenom sifrom sifru racuna za koju je navedena. Inace ju nece
			// imati sigurno
			if (!postojiLiStavkaURacunu(srvo)) {

				// zatim obavezno ide insert...
				try {
					// ovdje ako bude trebalo mozemo postaviti
					// srvo.setSifRacuna(DAO.NEPOSTOJECA_SIFRA) pa provjeriti
					// jeli insert uspio sa getSifRacuna();
					//nova stavka ako se unosi i ako nema postavljen iznos nadoplate, onda je taj iznos 0
					if (srvo.getDoplataKlijenta()==null) srvo.setDoplataKlijenta(0);
					
					this.getStavke().insert(srvo);
				} catch (SQLException e) {
					Logger.fatal("SQL iznimka kod insertiranja stavke raèuna",
							e);
					JOptionPane
							.showMessageDialog(
									this.getParent(),
									"Nastao je problem pri pohranjivanju stavke raèuna. Molimo kontaktirajte administratora sustava",
									"Upozorenje!", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}// if trebaInsert
			else {
				// ide update
				try {
					this.getStavke().update(srvo);
				} catch (SQLException e) {
					Logger.fatal("SQL iznimka kod updateanja stavke raèuna", e);
					JOptionPane
							.showMessageDialog(
									this.getParent(),
									"Nastao je problem pri pohranjivanju stavke raèuna. Molimo kontaktirajte administratora sustava",
									"Upozorenje!", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}// else
			ed.pobrisiFormu(); // prazna forma
			osvjeziStavke(); // napuni tablicu sa stavkama, postavi novi objekt
								// sa stavkama...
			// nova stavka u nju, makar je uništio klik na tablicu... uvijek
			// mora postojati prazna stavka u formi
			StavkaRacunaVO svo = new StavkaRacunaVO();
			svo.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
			ed.napuniPodatke(svo);
			return rez;
		} catch (Throwable t) {
			Logger.fatal("Iznimka kod dodavanja stavke u racun", t);
			JOptionPane
					.showMessageDialog(
							this.getParent(),
							"Nastao je ozbiljan problem pri pohranjivanju stavke raèuna. Molimo kontaktirajte administratora sustava",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}// dodajStavkuURacun

	private void brisiStavkuIzRacuna(StavkaRacunaVO svo) {
		try {
			this.getStavke().delete(svo);
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod brisanja stavke raèuna", e);
			JOptionPane
					.showMessageDialog(
							this.getParent(),
							"Nastao je problem pri pohranjivanju stavke raèuna. Molimo kontaktirajte administratora sustava",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			return;
		} catch (Exception e) {
			Logger.fatal("Opæenita iznimka kod brisanja stavke raèuna", e);
			JOptionPane
					.showMessageDialog(
							this.getParent(),
							"Nastao je problem pri pohranjivanju stavke raèuna. Molimo kontaktirajte administratora sustava",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}// brisiStavkuIzRacuna

	private void ucitajRacun(RacunVO rvo) {
		@SuppressWarnings("unchecked")
		GUIEditor<RacunVO> ed = (GUIEditor<RacunVO>) getJpRacunPanel();
		ed.napuniPodatke(rvo);
		osvjeziStavke();
		
		podesiAzuriranjeSudjelovanja();
		
	}

	private void podesiAzuriranjeSudjelovanja() {

		Racun rcp = (Racun) getJpRacunPanel();

		String iznSudjForma = rcp.getJtIznosSudjelovanja().getText().trim();

		
		int izracunatiIznos = RacuniUtil.izracunajTotalRacuna(this.stavkeRacuna);
		float izrIznos = izracunajSudjelovanje(izracunatiIznos);
		float iznSudjFormaFloat = StringUtils.isEmpty(iznSudjForma)? -1.0f: Float.valueOf(iznSudjForma);
		
		//ako iznosi nisu isti, to znaci da je korisnik rucno unio iznos sudjelovanja, dakle tako treba i ostati
		//

		if (izrIznos != iznSudjFormaFloat)
			azuriranoSudjelovanje = "" + izrIznos;
		else
			azuriranoSudjelovanje = iznSudjForma;
	}

	// fizicko brisanje racuna u kojem nema stavki...
	private void brisiRacun() {
		if (this.getOznaceni() == null)
			return;

		try {
			this.getOznaceni().setStatus('X'); // ides van fizicki..
			this.getRacuni().delete(this.getOznaceni());
			this.oznaceni = null;
		} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod brisanja raèuna ", e);
		}
	}// brisiRacun

	private boolean postojiLiStavkaURacunu(StavkaRacunaVO st) {
		if (this.stavkeRacuna == null)
			return false;
		int stavkeRSize = this.stavkeRacuna.size();
		for (int i = 0; i < stavkeRSize; i++) {
			StavkaRacunaVO svo = (StavkaRacunaVO) this.stavkeRacuna.get(i);
			if (svo.getSifArtikla().equals(st.getSifArtikla()))
				return true;
		}
		
		return false;
	}// postojiLiStavkaURacunu

	public void podaciSpremni(@SuppressWarnings("rawtypes") GUIEditor pozivatelj) {
		if (pozivatelj == this.getJpStavkaRacuna()) {
			this.dodajStavkuURacun();
		}
	}

	/**
	 * This method initializes jlUkupno
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlUkupno() 
	{
		if (jlUkupno == null) {
			jlUkupno = new javax.swing.JLabel();
			jlUkupno.setText("");
			jlUkupno.setToolTipText("ukupni iznos raèuna ");
			jlUkupno.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
		}
		
	 return jlUkupno;
	}
}// klasa
