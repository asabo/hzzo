/*$
 * Project opticari
 *
 */
package biz.sunce.dao.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DAOObjekt;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.PomagaloDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.SearchCriteriaObject;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;
import biz.sunce.util.StringUtils;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

 
/**
 * datum:2006.03.03
 * 
 * @author asabo
 * 
 */
public final class StavkaRacuna extends JPanel implements GUIEditor<StavkaRacunaVO>,
		SlusacOznaceneLabelePretrazivanja {

	private static final String SIF_PROIZVODJACA = "SIF_PROIZVODJACA";
	private static final String XXXXXXXXXXXX = "XXXXXXXXXXXX";
	private static final String COURIER_NEW = "Courier New";
	private static final long serialVersionUID = 8480359436583618375L;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jlSifraStavke = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtNazivStavke = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtKolicina = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtPoCijeni = null;
	StavkaRacunaVO oznaceni = null;
	PomagaloVO oznaceniPomagalo = null;
	ProizvodjacVO oznaceniProizvodjac = null;
	SearchCriteriaObject so = null;
	private Set<SlusacSpremnostiPodataka> slusaci = new HashSet<SlusacSpremnostiPodataka>();

	PretrazivanjeProzor pretrazivanjePomagala = null;
	PretrazivanjeProzor pretrazivanjeProizvodjaca = null,
			pretrazivanjeProizvodaProizvodjaca = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtSifProizvodjaca = null;
	private javax.swing.JTextField jtSifraProizvoda = null;
	private javax.swing.JLabel jLabel5 = null;
	private JLabel lblNadoplatio;
	private JTextField jtNadoplatio;

	/**
	 * This is the default constructor
	 */
	public StavkaRacuna() {
		super();
		initialize();
		Thread t = new Thread() {
			public void run() {
				this.setPriority(Thread.MIN_PRIORITY);
				this.setName("Validator stavki racuna");
				this.setPriority(Thread.MIN_PRIORITY);
				yield();
				validate();
				yield();
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
		consGridBagConstraints1.insets = new Insets(0, 0, 5, 5);
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		consGridBagConstraints4.insets = new Insets(0, 0, 5, 0);
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		consGridBagConstraints5.insets = new Insets(0, 0, 5, 5);
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		consGridBagConstraints6.insets = new Insets(0, 0, 5, 5);
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		consGridBagConstraints7.insets = new Insets(0, 0, 5, 5);
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		consGridBagConstraints8.insets = new Insets(0, 0, 5, 0);
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		consGridBagConstraints12.insets = new Insets(0, 0, 1, 0);
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
		consGridBagConstraints22.insets = new Insets(0, 0, 1, 5);
		consGridBagConstraints22.gridy = 3;
		consGridBagConstraints22.gridx = 2;
		consGridBagConstraints22.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints12.weightx = 1.0;
		consGridBagConstraints12.gridy = 3;
		consGridBagConstraints12.gridx = 3;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 0;
		consGridBagConstraints21.gridx = 3;
		consGridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints11.gridy = 0;
		consGridBagConstraints11.gridx = 2;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints5.gridy = 2;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints7.gridy = 2;
		consGridBagConstraints7.gridx = 2;
		consGridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints6.weightx = 1.0;
		consGridBagConstraints6.gridy = 2;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 1;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints4.gridwidth = 3;
		consGridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints8.weightx = 1.0;
		consGridBagConstraints8.gridy = 2;
		consGridBagConstraints8.gridx = 3;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints8.gridwidth = 1;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};
		this.setLayout(gridBagLayout);
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJlSifraStavke(), new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 1, 5, 5), 0, 0));
		this.add(getJLabel1(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 5), 0, 0));
		this.add(getJtNazivStavke(), consGridBagConstraints4);
		this.add(getJLabel2(), consGridBagConstraints5);
		this.add(getJtKolicina(), consGridBagConstraints6);
		this.add(getJLabel3(), consGridBagConstraints7);
		this.add(getJtPoCijeni(), consGridBagConstraints8);
		this.add(getJLabel4(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		this.add(getJtSifProizvodjaca(), new GridBagConstraints(3, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 5, 0), 0, 0));
		GridBagConstraints gbc_lblNadoplatio = new GridBagConstraints();
		gbc_lblNadoplatio.anchor = GridBagConstraints.EAST;
		gbc_lblNadoplatio.insets = new Insets(0, 0, 0, 5);
		gbc_lblNadoplatio.gridx = 0;
		gbc_lblNadoplatio.gridy = 3;
		add(getLblNadoplatio(), gbc_lblNadoplatio);
		GridBagConstraints gbc_jtNadoplatio = new GridBagConstraints();
		gbc_jtNadoplatio.anchor = GridBagConstraints.WEST;
		gbc_jtNadoplatio.insets = new Insets(0, 0, 1, 5);
		gbc_jtNadoplatio.gridx = 1;
		gbc_jtNadoplatio.gridy = 3;
		add(getJtNadoplatio(), gbc_jtNadoplatio);
		this.add(getJtSifraProizvoda(), consGridBagConstraints12);
		this.add(getJLabel5(), consGridBagConstraints22);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(405*faktor, 82*faktor);
		this.setMinimumSize(new Dimension(393*faktor, 82*faktor));
		this.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(550, 100));
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setToolTipText("\u0161ifra stavke");
			jLabel.setText("\u0161ifra stavke:");
		}
		return jLabel;
	}

	/**
	 * This method initializes jlSifraStavke
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlSifraStavke() {
		if (jlSifraStavke == null) {
			jlSifraStavke = new javax.swing.JLabel();
			jlSifraStavke.setText(XXXXXXXXXXXX);
			jlSifraStavke.setToolTipText("HZZO šifra artikla ili usluge");
			jlSifraStavke.setFont(new java.awt.Font(COURIER_NEW,
					java.awt.Font.BOLD, 14));
		}
		return jlSifraStavke;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setToolTipText("naziv stavke");
			jLabel1.setText("Naziv stavke:");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtNazivStavke
	 * 
	 * @return javax.swing.JTextField
	 */
	@SuppressWarnings("deprecation")
	private javax.swing.JTextField getJtNazivStavke() {
		if (jtNazivStavke == null) {
			jtNazivStavke = new javax.swing.JTextField();
			jtNazivStavke
					.setToolTipText("ovdje upisujete šifru ili naziv stavke");
			jtNazivStavke.setPreferredSize(new Dimension(250, 24));
			jtNazivStavke.setMinimumSize(new java.awt.Dimension(250, 20));
			jtNazivStavke
					.setNextFocusableComponent(this.getJtSifProizvodjaca());
			this.pretrazivanjePomagala = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getPomagala(), -40, 16, 400, 120, jtNazivStavke);
			this.pretrazivanjePomagala.setMaksimumZaPretrazivanje(10);
			this.pretrazivanjePomagala.setMinimumZnakovaZaPretrazivanje(2);

			if (GlavniFrame.isKoristiSvaPomagala()) {
				SearchCriteria scr = new SearchCriteria();
				scr.setKriterij(PomagaloDAO.KRITERIJ_KORISTIMO_SVA_POMAGALA);
				this.pretrazivanjePomagala.setKriterij(scr);
			}

			this.pretrazivanjePomagala.dodajSlusaca(this);
			jtNazivStavke.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtNazivStavke.selectAll();
				}
			});
		}
		return jtNazivStavke;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setToolTipText("koli\u010Dina");
			jLabel2.setText("Koli\u010Dina:");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jtKolicina
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtKolicina() {
		if (jtKolicina == null) {
			jtKolicina = new javax.swing.JTextField();
			jtKolicina.setPreferredSize(new Dimension(120, 24));
			jtKolicina.setMinimumSize(new Dimension(100, 23));
			jtKolicina
					.setToolTipText("kolièina robe ili usluge, mora biti CIJELI pozitivan broj");
			jtKolicina.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtKolicina.selectAll(); // da olaksamo osobi unos druge
											// kolicine
				}
			});
			jtKolicina.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e != null && e.getKeyChar() == KeyEvent.VK_ENTER
							&& !jtPoCijeni.getText().trim().equals("")) {
						posaljiObavijestOSpremnostiPodataka();
					} else if (jtPoCijeni.getText().trim().equals(""))
						jtPoCijeni.requestFocusInWindow();
				}
			});
		}
		return jtKolicina;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("po cijeni:");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jtPoCijeni
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPoCijeni() {
		if (jtPoCijeni == null) {
			jtPoCijeni = new javax.swing.JTextField();
			jtPoCijeni.setPreferredSize(new Dimension(120, 24));
			jtPoCijeni.setMinimumSize(new Dimension(90, 23));
			jtPoCijeni.setToolTipText("cijena izražena od strane hzzo-a");
			jtPoCijeni.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtPoCijeni.selectAll();// da osoba moze lakse pobrisati
											// cijenu ako nije ispravna
				}
			});
			jtPoCijeni.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e != null && e.getKeyChar() == KeyEvent.VK_ENTER && jtPoCijeni.getText().length()>0) {
						posaljiObavijestOSpremnostiPodataka();
					}
				}
			});
		}
		return jtPoCijeni;
	}

	// kad su podaci spremni za slanje objekt salje obavijest svim registriranim
	// slusacima
	private void posaljiObavijestOSpremnostiPodataka() {
		if (slusaci != null) {
			
			Iterator<SlusacSpremnostiPodataka> it = slusaci.iterator();
			
			while (it.hasNext()){
				SlusacSpremnostiPodataka sl = it.next();

				if (sl != null)
				 sl.podaciSpremni(this);
				  else  
					Logger.log("Slusac stavke racuna null?!?");
			}
		}
	}// posaljiObavijestOSpremnostiPodataka

	public void napuniPodatke(StavkaRacunaVO ulaz) {
		this.oznaceni =  ulaz;
		if (this.oznaceni == null) {
			this.pobrisiFormu();
			return;
		}
		this.jlSifraStavke.setText(this.oznaceni.getSifArtikla());
		if (this.oznaceni.getSifArtikla() != null)
			this.nadjiPomagalo(this.oznaceni.getSifArtikla());

		String sifraArtikla = this.oznaceni.getSifArtikla();

		boolean iso9999 = sifraArtikla != null && sifraArtikla.length() == 12
				&& StringUtils.imaSamoBrojeve(sifraArtikla);

		if (this.oznaceni.getSifArtikla() != null)
			this.jtNazivStavke
					.setText(this.oznaceniPomagalo != null ? this.oznaceniPomagalo
							.getNaziv() : "?!?");

		Integer k = this.oznaceni.getKolicina();
		Integer c = this.oznaceni.getPoCijeni();
		Integer dp = this.oznaceni.getDoplataKlijenta();
		
		this.jtKolicina.setText("" + (k != null ? "" + k.intValue() : ""));
		this.jtPoCijeni.setText(""
				+ (c != null ? "" + (c.intValue() / 100.0f) : ""));
		this.jtNadoplatio.setText("" + (dp != null ? "" + (dp.intValue() / 100.0f) : ""));
		this.jtSifProizvodjaca
				.setText(this.oznaceni.getSifProizvodjaca() != null ? ""
						+ this.oznaceni.getSifProizvodjaca().intValue() : "");
		this.jtSifraProizvoda
				.setText(this.oznaceni.getEkstraSifProizvoda() != null ? this.oznaceni
						.getEkstraSifProizvoda() : "");
		this.omoguci();
		this.jtNazivStavke.requestFocusInWindow();
		if (iso9999)
			this.iskljuciSifProizvodjaca();
		else
			ukljuciSifProizvodjaca();

	}// napuniPodatke

	private PomagaloVO nadjiPomagalo(String sifra) {
		try {
			if (sifra != null && !sifra.equals("") && GlavniFrame.running) {
				this.oznaceniPomagalo = (PomagaloVO) DAOFactory.getInstance()
						.getPomagala().read(sifra);
				if (this.pretrazivanjeProizvodjaca != null)
					this.pretrazivanjeProizvodaProizvodjaca.setEnabled(false);
				if (this.pretrazivanjeProizvodaProizvodjaca != null)
					this.pretrazivanjeProizvodaProizvodjaca.setEnabled(true);
			} else {
				this.oznaceniPomagalo = null;
				if (this.pretrazivanjeProizvodjaca != null)
					this.pretrazivanjeProizvodaProizvodjaca.setEnabled(true);
				if (this.pretrazivanjeProizvodaProizvodjaca != null)
					this.pretrazivanjeProizvodaProizvodjaca.setEnabled(false);
			}
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod stavka racuna GUI komponenta pri citanju odredjenog pomagala.. ",
					e);
		}

		return this.oznaceniPomagalo;
	}// nadjiPomagalo

	public StavkaRacunaVO vratiPodatke() {
		if (this.oznaceni == null) {
			this.oznaceni = new StavkaRacunaVO();
			// this.oznaceni.setSifra()
		}

		this.oznaceni
				.setSifArtikla(this.oznaceniPomagalo != null ? this.oznaceniPomagalo
						.getSifraArtikla() : null);

		// kada je kolicina 0, korisnik zeli pobrisati stavku iz racuna
		int kolicina = -1, poCijeni = 0, sifProizv = -1, nadoplatio = -1;
		String kolicinaStr = this.jtKolicina.getText().trim();
		String cijenaStr = this.jtPoCijeni.getText().trim();
		String sifProizvStr = this.jtSifProizvodjaca.getText().trim();
		String sifProizvodaStr = this.jtSifraProizvoda.getText().trim();
		String nadoplatioStr = this.jtNadoplatio.getText().trim();

		try {

			if (!kolicinaStr.equals(""))
				kolicina = Integer.parseInt(kolicinaStr);

			if (!cijenaStr.equals("")) {
				String str = cijenaStr;
				str = str.replaceAll("\\,", ".");
				String[] spl = str.split("\\.");
				if (spl != null && spl.length == 1)
					str = str + "00";
				else if (spl != null && spl.length > 1) {
					if (spl[1].length() == 0)
						str = spl[0] + "00";
					else if (spl[1].length() == 1)
						str = spl[0] + spl[1] + "0";
					else if (spl[1].length() == 2)
						str = spl[0] + spl[1];
					else if (spl[1].length() > 2)
						str = spl[0] + spl[1].substring(0, 2);

				}
				str = str.replaceAll("\\.", "");
				// maknemo zarez i tocku i broj ocitamo kao cijeli... i onda
				// nema gubitka lipe..
				poCijeni = (Integer.parseInt(str));
			}// if poCijeniStr
			
			if (!nadoplatioStr.equals("")) {
				String str = nadoplatioStr;
				str = str.replaceAll("\\,", ".");
				String[] spl = str.split("\\.");
				if (spl != null && spl.length == 1)
					str = str + "00";
				else if (spl != null && spl.length > 1) {
					if (spl[1].length() == 0)
						str = spl[0] + "00";
					else if (spl[1].length() == 1)
						str = spl[0] + spl[1] + "0";
					else if (spl[1].length() == 2)
						str = spl[0] + spl[1];
					else if (spl[1].length() > 2)
						str = spl[0] + spl[1].substring(0, 2);

				}
				str = str.replaceAll("\\.", "");
				// maknemo zarez i tocku i broj ocitamo kao cijeli... i onda
				// nema gubitka lipe..
				 nadoplatio = (Integer.parseInt(str));
			}// if nadoplatioStr

			if (!sifProizvStr.equals(""))
				sifProizv = Integer.parseInt(sifProizvStr);

		} catch (NumberFormatException nfe) {
			String por = "Pokušaj zapisivanja neispravnih vrijednosti za stavku raèuna. Kolièina:"
					+ kolicinaStr
					+ " po cijeni: "
					+ cijenaStr
					+ " šifra proizvoðaèa: " + sifProizvStr
					+ " nadoplatio: " + nadoplatioStr;
			GlavniFrame.alert(por);
			Logger.log(por, nfe);
		}
		this.oznaceni.setKolicina(Integer.valueOf(kolicina));
		this.oznaceni.setPoCijeni(Integer.valueOf(poCijeni));
		this.oznaceni.setDoplataKlijenta(nadoplatio==-1 ? null : Integer.valueOf(nadoplatio));
		if (sifProizv > 0)
			this.oznaceni.setSifProizvodjaca(Integer.valueOf(sifProizv));
		else
			this.oznaceni.setSifProizvodjaca(null);

		if (sifProizvodaStr.equals(""))
			this.oznaceni.setEkstraSifProizvoda(null);
		else
			this.oznaceni.setEkstraSifProizvoda(sifProizvodaStr);

		return this.oznaceni;
	}// vratiPodatke

	public void pobrisiFormu() {
		this.oznaceni = null;
		this.oznaceniPomagalo = null;
		final String p = "";
		this.jlSifraStavke.setText("XXXXXXXXX");
		this.jtKolicina.setText(p);
		this.jtNazivStavke.setText(p);
		this.jtPoCijeni.setText(p);
		this.jtSifProizvodjaca.setText(p);
		this.jtSifraProizvoda.setText(p);
		if (this.pretrazivanjeProizvodjaca != null)
			this.pretrazivanjeProizvodjaca.setEnabled(true);
		
		this.pretrazivanjePomagala.setOznacena(null);
		
		if (this.pretrazivanjeProizvodaProizvodjaca != null)
			this.pretrazivanjeProizvodaProizvodjaca.setEnabled(false);

		this.onemoguci();
	}

	public boolean isFormaIspravna() {
		return false;
	}

	public void omoguci() {
		this.postaviStatuseElemenata(true);
	}

	public void onemoguci() {
		this.postaviStatuseElemenata(false);
	}

	private void postaviStatuseElemenata(boolean s) {
		this.jlSifraStavke.setEnabled(s);
		this.jtKolicina.setEnabled(s);
		this.jtNazivStavke.setEnabled(s);
		this.jtPoCijeni.setEnabled(s);
		this.jtSifProizvodjaca.setEnabled(s);
		this.jtSifraProizvoda.setEnabled(s);
		this.jtNadoplatio.setEnabled(s);
	}

	public boolean jeliIzmjenjen() {
		return false;
	}

	public void labelaOznacena(Labela labela) {
		Object izvornik = labela != null ? labela.getIzvornik() : null;

		if (labela == null || izvornik == null)
			return;

		if (izvornik instanceof PomagaloVO) {
			this.oznaceniPomagalo = (PomagaloVO) izvornik;
			this.jtNazivStavke.setText(this.oznaceniPomagalo.getNaziv());
			String sifraArtikla = this.oznaceniPomagalo.getSifraArtikla();
			this.jlSifraStavke.setText(sifraArtikla);
			this.jtKolicina.setText("1");
			// staviti pretrazivacu do znanja za koji proizvod traziti
			// proizvodjace
			this.so.setValue(sifraArtikla);
			// iskljuciti jednog pretrazivaca i ukljuciti drugog..
			this.pretrazivanjeProizvodjaca.setEnabled(false);
			this.pretrazivanjeProizvodaProizvodjaca.setEnabled(true);

			boolean iso9999 = sifraArtikla != null
					&& sifraArtikla.length() == 12
					&& StringUtils.imaSamoBrojeve(sifraArtikla);

			if (!iso9999 && this.jtSifProizvodjaca.getText().trim().equals(""))
				try {
					ArrayList<?> l = (ArrayList<?>) DAOFactory.getInstance()
							.getProizvodjaciProizvoda().findAll(this.so);
					if (l != null && l.size() > 0 && l.get(0) != null) {
						ValueObject vob = (ValueObject) l.get(0);
						Object val = vob.getValue(SIF_PROIZVODJACA);

						if (val != null)
							this.jtSifProizvodjaca.setText(val.toString());
					}
				} catch (SQLException e) {
					Logger.fatal(
							"SQL iznimka kod traženja proizvoðaèa proizvoda", e);
				}

			// 20.03.06. ako cijena postoji, ubacit cemo je unutra da olaksamo
			// osobi unos
			if (this.oznaceniPomagalo.getCijenaSPDVom() != null
					&& this.oznaceniPomagalo.getCijenaSPDVom().intValue() > 0) {
				float cijena = this.oznaceniPomagalo.getCijenaSPDVom()
						.intValue() / 100.0f;
				this.jtPoCijeni.setText("" + cijena);
			}

			if (!iso9999 && this.jtSifProizvodjaca.getText().trim().equals("")) {
				this.jtSifProizvodjaca.requestFocusInWindow();
			} else {

				this.jtKolicina.requestFocusInWindow();
			}

			if (!iso9999)
				ukljuciSifProizvodjaca();
			else
				iskljuciSifProizvodjaca();

		}// if izvornik PomagaloVO
		else if (izvornik instanceof ProizvodjacVO) {
			this.oznaceniProizvodjac = (ProizvodjacVO) izvornik;
			Integer hzzo = this.oznaceniProizvodjac != null ? this.oznaceniProizvodjac
					.getHzzoSifra() : null;
			this.jtSifProizvodjaca.setText(hzzo != null ? "" + hzzo.intValue()
					: "");
			this.jtNazivStavke.requestFocusInWindow();
		} else if (izvornik instanceof ValueObject) {
			ValueObject vo = (ValueObject) izvornik;

			this.jtSifProizvodjaca
					.setText("" + vo.getValue(SIF_PROIZVODJACA));
			this.jtKolicina.requestFocusInWindow();
		}

	}// labelaOznacena

	private void iskljuciSifProizvodjaca() {
		this.jtSifProizvodjaca.setText("");
		this.jtSifProizvodjaca.setEnabled(false);
	}

	private void ukljuciSifProizvodjaca() {
		this.jtSifProizvodjaca.setEnabled(true);
	}

	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
		if (slusac != null)
			this.slusaci.add(slusac);
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("HZZO \u0161if. proizv:");
			jLabel4.setToolTipText("HZZO šifra proizvodjaèa, neobavezan podatak, ako ste naveli u zaglavlju raèuna šifru, ne trebate unositi podatak");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jtSifProizvodjaca
	 * 
	 * @return javax.swing.JTextField
	 */
	@SuppressWarnings("deprecation")
	private javax.swing.JTextField getJtSifProizvodjaca() {
		if (jtSifProizvodjaca == null) {
			jtSifProizvodjaca = new javax.swing.JTextField();
			jtSifProizvodjaca.setPreferredSize(new Dimension(120, 20));
			jtSifProizvodjaca.setMinimumSize(new Dimension(90, 20));
			jtSifProizvodjaca
					.setToolTipText("šifra proizvodjaèa po HZZO šifarniku, obavezan podatak");
			jtSifProizvodjaca.setNextFocusableComponent(this.getJtKolicina());

			jtSifProizvodjaca
					.addFocusListener(new java.awt.event.FocusAdapter() {
						@Override
						public void focusLost(java.awt.event.FocusEvent e) {
							if (oznaceniPomagalo == null)
								getJtNazivStavke().requestFocusInWindow();
						}

						@Override
						public void focusGained(java.awt.event.FocusEvent e) {
							jtSifProizvodjaca.selectAll();
						}
					});
			// izmjenom sifre artikla mjenjat ce se so objekt
			// a ovisno o tome jeli so postavljen ili nije pozivat ce se jedan
			// ili drugi pretrazivac prozor
			so = SearchCriteriaObject.getInstance("SIF_ARTIKLA", null,
					java.sql.Types.VARCHAR,
					SearchCriteriaObject.CRITERIA_WORD_STRICT);
			jtSifProizvodjaca.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e != null && e.getKeyChar() == KeyEvent.VK_ENTER) {
						// zasada nista slati jer sta ako je korisnik stisnuo
						// enter na pretrazivackom prozoru
						// posaljiObavijestOSpremnostiPodataka();
					}
				}
			});
			// ako proizvod ne bude na formi, sve cemo proizvodjace ponuditi na
			// odabir
			this.pretrazivanjeProizvodjaca = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getProizvodjaci(), 10, 12, 120, 100,
					jtSifProizvodjaca);
			this.pretrazivanjeProizvodjaca.setMaksimumZaPretrazivanje(6);
			this.pretrazivanjeProizvodjaca.setMinimumZnakovaZaPretrazivanje(2);
			this.pretrazivanjeProizvodjaca.dodajSlusaca(this);

			// inace nudimo samo one koji se nalaze registrirani za taj proizvod
			DAOObjekt proizvodjaciProizvoda = GlavniFrame.running?DAOFactory.getInstance()
					.getProizvodjaciProizvoda():null;
			this.pretrazivanjeProizvodaProizvodjaca = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), proizvodjaciProizvoda, 10, 12, 120, 100,
					jtSifProizvodjaca);
			this.pretrazivanjeProizvodaProizvodjaca
					.setMaksimumZaPretrazivanje(2);
			this.pretrazivanjeProizvodaProizvodjaca
					.setMinimumZnakovaZaPretrazivanje(1);
			this.pretrazivanjeProizvodaProizvodjaca.dodajSlusaca(this);
			this.pretrazivanjeProizvodaProizvodjaca.setEnabled(false); // ovaj
																		// je u
																		// sjeni
																		// dok
																		// se ne
																		// odabere
																		// proizvod
			this.pretrazivanjeProizvodaProizvodjaca.setSoKriterij(this.so);
			this.pretrazivanjeProizvodaProizvodjaca
					.setDescriptorColumn(SIF_PROIZVODJACA);

			if (proizvodjaciProizvoda!=null)
			{
			proizvodjaciProizvoda
					.addOrderByKolona("UPDATED", false);
			proizvodjaciProizvoda
					.addOrderByKolona("CREATED", false);
			}
			// DAOFactory.getInstance().getProizvodjaciProizvoda().setOrderByKolona("BROJAC",false);
		}
		return jtSifProizvodjaca;
	}

	/**
	 * This method initializes jtSifraProizvoda
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtSifraProizvoda() {
		if (jtSifraProizvoda == null) {
			jtSifraProizvoda = new javax.swing.JTextField();
			jtSifraProizvoda
					.setToolTipText("ako stranka nije uzela standardni proizvod, tu trebate unijeti SVOJU šifru tog proizvoda");
			jtSifraProizvoda.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e != null && e.getKeyChar() == KeyEvent.VK_ENTER) {
						posaljiObavijestOSpremnostiPodataka();
					}
				}
			});
		}
		return jtSifraProizvoda;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("\u0160if. nest. proizvoda:");
			jLabel5.setToolTipText("ako stranka nije uzela standardni proizvod, tu trebate unijeti SVOJU šifru tog proizvoda");
		}
		return jLabel5;
	}
	private JLabel getLblNadoplatio() {
		if (lblNadoplatio == null) {
			lblNadoplatio = new JLabel("Nadoplatio:");
			lblNadoplatio.setToolTipText("brutto iznos koji je stranka nadoplatila za X komada tog artikla");
		}
		return lblNadoplatio;
	}
	private JTextField getJtNadoplatio() {
		if (jtNadoplatio == null) {
			jtNadoplatio = new JTextField();
			jtNadoplatio.setToolTipText("brutto iznos koji je stranka nadoplatila za X komada tog artikla");
			jtNadoplatio.setPreferredSize(new Dimension(120, 24));
			jtNadoplatio.setMinimumSize(new Dimension(100, 23));
			jtNadoplatio.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtNadoplatio.selectAll(); // da olaksamo osobi unos druge
											// kolicine
				}
			});
			jtNadoplatio.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e != null && e.getKeyChar() == KeyEvent.VK_ENTER
							&& !jtNadoplatio.getText().trim().equals("")) {
						posaljiObavijestOSpremnostiPodataka();
					}
				}
			});
		}
		return jtNadoplatio;
	}
}
