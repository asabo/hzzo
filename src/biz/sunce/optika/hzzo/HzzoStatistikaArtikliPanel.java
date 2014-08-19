/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.RacunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;
import biz.sunce.util.Util;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.SlusacDateChoosera;

/**
 * datum:2006.05.15
 * 
 * @author asabo
 * 
 */
public final class HzzoStatistikaArtikliPanel extends JPanel implements
		SlusacDateChoosera, SlusacOznaceneLabelePretrazivanja {

	 
	private static final long serialVersionUID = 5778488582058501536L;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel2 = null;
	private JDateChooser dDo = null;
	private JDateChooser dOd = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtLijecnik = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtHzzoPodruznica = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtArtikl = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private JXTable tStavke = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JLabel jlUkupnoKomada = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JLabel jlUkupanIznos = null;
	PretrazivanjeProzor lijecniciPretrazivanje = null;
	PretrazivanjeProzor podruznicePretrazivanje = null;
	TableModel model = null;

	LijecnikVO oznaceniLijecnik = null;
	MjestoVO oznacenoMjesto = null;
	SearchCriteria kriterij = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JLabel jlTeretHzzo = null;

	/**
	 * This is the default constructor
	 */
	public HzzoStatistikaArtikliPanel() {
		super();
		initialize();
		new Thread() {
			@Override
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
				}
				yield();
				GlavniFrame.getInstanca().busy();
				osvjeziListu();

				tStavke.packAll();
				GlavniFrame.getInstanca().idle();
			}
		}.start();
	}

	private SearchCriteria getKriterij() {
		if (this.kriterij == null) {
			this.kriterij = new SearchCriteria();
			this.kriterij
					.setKriterij(StavkaRacunaDAO.KRITERIJ_UKUPNO_PO_STAVKAMA);
		}
		return this.kriterij;
	}

	// na osnovu parametara ispisuje podatke u tablici...
	private void osvjeziListu() {
		ArrayList l = new ArrayList(3);
		l.add(dOd.getDatum());
		l.add(dDo.getDatum());
		l.add(this.oznaceniLijecnik); // bio null, ne bio..
		l.add(this.oznacenoMjesto);
		l.add(this.jtArtikl.getText().trim().toUpperCase());
		SearchCriteria k = getKriterij();
		k.setPodaci(l);
		this.model.setFilter(k);
		l = (ArrayList) this.model.getData();
		int kol = 0, sum = 0, sudjelovanje = 0;
		RacunDAO rdao = DAOFactory.getInstance().getRacuni();
		RacunVO rvo = null;
		// Hashtable racuni=new Hashtable();
		for (int i = 0; i < l.size(); i++) {
			StavkaRacunaVO srvo = (StavkaRacunaVO) l.get(i);
			kol += srvo.getKolicina().intValue();
			sum += srvo.getPoCijeni().intValue();

			// za svaku stavku skidamo po jedno sudjelovanje
			/*
			 * if (!racuni.containsKey(srvo.getSifRacuna())) { try {
			 * rvo=(RacunVO) rdao.read(srvo.getSifRacuna());
			 * 
			 * if (rvo!=null) {
			 * sudjelovanje+=rvo.getIznosSudjelovanja().intValue();
			 * racuni.put(srvo.getSifRacuna(),rvo); } } catch (SQLException e) {
			 * Logger
			 * .log("Problem pri citanju podataka o racunu kod kreiranja statistike"
			 * ,e); } }//if
			 */
		}// for i
			// racuni.clear(); racuni=null;

		jlUkupnoKomada.setText("" + kol);
		jlUkupanIznos.setText(Util.pretvoriLipeUIznosKaoString(sum));
		// jlTeretHzzo.setText(Util.pretvoriLipeUIznosKaoString(sum-sudjelovanje));
	}// osvjeziListu

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints17 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints18 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		consGridBagConstraints3.gridy = 4;
		consGridBagConstraints3.gridx = 11;
		consGridBagConstraints14.gridy = 4;
		consGridBagConstraints14.gridx = 10;
		consGridBagConstraints17.gridy = 4;
		consGridBagConstraints17.gridx = 3;
		consGridBagConstraints17.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints18.gridy = 4;
		consGridBagConstraints18.gridx = 5;
		consGridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints15.gridy = 4;
		consGridBagConstraints15.gridx = 1;
		consGridBagConstraints15.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints16.gridy = 4;
		consGridBagConstraints16.gridx = 2;
		consGridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints7.gridy = 2;
		consGridBagConstraints7.gridx = 0;
		consGridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints12.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints12.weightx = 1.0;
		consGridBagConstraints12.gridy = 2;
		consGridBagConstraints12.gridx = 5;
		consGridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints11.gridy = 1;
		consGridBagConstraints11.gridx = 5;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 2;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 0;
		consGridBagConstraints5.gridy = 1;
		consGridBagConstraints5.gridx = 3;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints6.gridy = 1;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints9.gridy = 2;
		consGridBagConstraints9.gridx = 2;
		consGridBagConstraints9.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints10.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints10.weightx = 1.0;
		consGridBagConstraints10.gridy = 2;
		consGridBagConstraints10.gridx = 3;
		consGridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints8.weightx = 1.0;
		consGridBagConstraints8.gridy = 2;
		consGridBagConstraints8.gridx = 1;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints2.insets = new java.awt.Insets(0, 3, 0, 0);
		consGridBagConstraints4.insets = new java.awt.Insets(0, 3, 0, 0);
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints13.weighty = 1.0;
		consGridBagConstraints13.weightx = 1.0;
		consGridBagConstraints13.gridy = 3;
		consGridBagConstraints13.gridx = 0;
		consGridBagConstraints13.gridwidth = 12;
		consGridBagConstraints13.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints1.gridwidth = 10;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJLabel1(), consGridBagConstraints2);
		this.add(getJLabel2(), consGridBagConstraints4);
		this.add(getDDo(), consGridBagConstraints5);
		this.add(getDOd(), consGridBagConstraints6);
		this.add(getJLabel3(), consGridBagConstraints7);
		this.add(getJtLijecnik(), consGridBagConstraints8);
		this.add(getJLabel4(), consGridBagConstraints9);
		this.add(getJtHzzoPodruznica(), consGridBagConstraints10);
		this.add(getJLabel5(), consGridBagConstraints11);
		this.add(getJtArtikl(), consGridBagConstraints12);
		this.add(getJScrollPane(), consGridBagConstraints13);
		this.add(getJLabel6(), consGridBagConstraints15);
		this.add(getJlUkupnoKomada(), consGridBagConstraints16);
		this.add(getJLabel7(), consGridBagConstraints17);
		this.add(getJlUkupanIznos(), consGridBagConstraints18);
		this.add(getJLabel8(), consGridBagConstraints14);
		this.add(getJlTeretHzzo(), consGridBagConstraints3);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(790*faktor, 580*faktor);
		this.setToolTipText("Statistike o izlazu robe po više kriterija");
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Izlaz robe");
			jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
		}
		return jLabel;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Datum od:  ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Datum do: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes dDo
	 * 
	 * @return javax.swing.JPanel
	 */
	private JDateChooser getDDo() {
		if (dDo == null) {
			dDo = new JDateChooser();
			dDo.setDatum(Calendar.getInstance());
			dDo.setPreferredSize(new java.awt.Dimension(130, 20));
			dDo.setMinimumSize(new java.awt.Dimension(130, 20));
			dDo.setToolTipText("datum do kojeg želite vidjeti iznose na popisu");
			dDo.dodajSlusaca(this);
		}
		return dDo;
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
			Calendar c = dOd.getDatum();
			boolean l = c.isLenient();
			c.setLenient(true);
			c.set(java.util.Calendar.MONTH, c.get(Calendar.MONTH) - 3); // zadnja
																		// tri
																		// mjeseca
																		// su
																		// zanimljiva...
			c.setLenient(l);
			dOd.setDatum(c);
			dOd.setPreferredSize(new java.awt.Dimension(130, 20));
			dOd.setMinimumSize(new java.awt.Dimension(130, 20));
			dOd.setToolTipText("datum od kada želite iznose na popisu");
			dOd.dodajSlusaca(this);
		}
		return dOd;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Lijeènik:  ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jtLijecnik
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtLijecnik() {
		if (jtLijecnik == null) {
			jtLijecnik = new javax.swing.JTextField();
			jtLijecnik.setPreferredSize(new java.awt.Dimension(150, 20));
			jtLijecnik.setMinimumSize(new java.awt.Dimension(150, 20));
			jtLijecnik.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtLijecnik.getText().trim().equals("")) {
						oznaceniLijecnik = null;
						osvjeziListu();
					}
				}

				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtLijecnik.selectAll();
				}
			});
			this.lijecniciPretrazivanje = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getLijecnici(), 10, 10, 130, 70, jtLijecnik);

			this.lijecniciPretrazivanje.dodajSlusaca(this);
		}
		return jtLijecnik;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Hzzo podružnica: ");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jtHzzoPodruznica
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtHzzoPodruznica() {
		if (jtHzzoPodruznica == null) {
			jtHzzoPodruznica = new javax.swing.JTextField();
			jtHzzoPodruznica.setPreferredSize(new java.awt.Dimension(140, 20));
			jtHzzoPodruznica.setMinimumSize(new java.awt.Dimension(140, 20));
			jtHzzoPodruznica
					.addFocusListener(new java.awt.event.FocusAdapter() {
						@Override
						public void focusLost(java.awt.event.FocusEvent e) {
							if (jtHzzoPodruznica.getText().trim().equals("")) {
								oznacenoMjesto = null;
								osvjeziListu();
							}

						}// focusLost

						@Override
						public void focusGained(java.awt.event.FocusEvent e) {
							jtHzzoPodruznica.selectAll();
						}
					});
			this.podruznicePretrazivanje = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getMjesta(), 10, 10, 130, 70, jtHzzoPodruznica);
			SearchCriteria kr = new SearchCriteria();
			kr.setKriterij(MjestoVO.KRITERIJ_PRETRAZIVANJA_PODRUDRUZNICE);

			// filter ce ugradjivati upit u kriterij prije zvanja findAll
			// metode...
			this.podruznicePretrazivanje.setKriterij(kr);

			this.podruznicePretrazivanje.setMaksimumZaPretrazivanje(8);
			this.podruznicePretrazivanje.dodajSlusaca(this);
		}
		return jtHzzoPodruznica;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Artikl: ");
		}
		return jLabel5;
	}

	/**
	 * This method initializes jtArtikl
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtArtikl() {
		if (jtArtikl == null) {
			jtArtikl = new javax.swing.JTextField();
			jtArtikl.setPreferredSize(new java.awt.Dimension(120, 20));
			jtArtikl.setMinimumSize(new java.awt.Dimension(120, 20));
			jtArtikl.setToolTipText("šifra HZZO skupine artikala, što više znakova upišete, to manju skupinu artikla æete dobiti ");
			jtArtikl.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					osvjeziListu();
				}
			});
			jtArtikl.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					new Thread() {
						@Override
						public void run() {
							osvjeziListu();
						}
					}.start();
				}

			});
		}
		return jtArtikl;
	}

	/**
	 * This method initializes tStavke
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getTStavke() {
		if (tStavke == null) {
			tStavke = new JXTable();
			this.model = new TableModel(DAOFactory.getInstance()
					.getStavkeRacuna(), tStavke);
			this.tStavke.setModel(this.model);

		}
		return tStavke;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getTStavke());
			jScrollPane.setPreferredSize(new java.awt.Dimension(790, 400));
			jScrollPane.setMinimumSize(new java.awt.Dimension(790, 400));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Ukupno komada: ");
		}
		return jLabel6;
	}

	/**
	 * This method initializes jlUkupnoKomada
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlUkupnoKomada() {
		if (jlUkupnoKomada == null) {
			jlUkupnoKomada = new javax.swing.JLabel();
			jlUkupnoKomada.setText("0");
			jlUkupnoKomada.setFont(new java.awt.Font("Georgia",
					java.awt.Font.BOLD, 14));
		}
		return jlUkupnoKomada;
	}

	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if (jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Ukupan iznos: ");
		}
		return jLabel7;
	}

	/**
	 * This method initializes jlUkupanIznos
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlUkupanIznos() {
		if (jlUkupanIznos == null) {
			jlUkupanIznos = new javax.swing.JLabel();
			jlUkupanIznos.setText("000,00");
			jlUkupanIznos.setFont(new java.awt.Font("Georgia",
					java.awt.Font.BOLD, 14));
		}
		return jlUkupanIznos;
	}

	public void labelaOznacena(Labela labela) {
		if (labela.getIzvornik() instanceof LijecnikVO) {
			this.oznaceniLijecnik = (LijecnikVO) labela.getIzvornik();
			jtLijecnik.setText(this.oznaceniLijecnik.toString());
		} else if (labela.getIzvornik() instanceof MjestoVO) {
			this.oznacenoMjesto = (MjestoVO) labela.getIzvornik();
			jtHzzoPodruznica.setText(this.oznacenoMjesto.toString());
		}
		osvjeziListu();
	}

	/**
	 * This method initializes jLabel8
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if (jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("Na teret HZZO: ");
		}
		return jLabel8;
	}

	/**
	 * This method initializes jlTeretHzzo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlTeretHzzo() {
		if (jlTeretHzzo == null) {
			jlTeretHzzo = new javax.swing.JLabel();
			jlTeretHzzo.setText("_____");
			jlTeretHzzo.setFont(new java.awt.Font("Georgia",
					java.awt.Font.BOLD, 14));
		}
		return jlTeretHzzo;
	}

	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		this.osvjeziListu();
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
