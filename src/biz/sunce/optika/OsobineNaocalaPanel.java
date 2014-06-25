/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.ProizvodjaciDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.SlikeDAO;
import biz.sunce.opticar.vo.ArtiklVO;
import biz.sunce.opticar.vo.NaocaleVO;
import biz.sunce.opticar.vo.NaocalnaLecaVO;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.opticar.vo.SlikaVO;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlikaFrame;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;

/**
 * datum:2005.05.18
 * 
 * @author asabo panel koji ce se opcenito upotrebljavati za upisivanje podataka
 *         o naocalama imat ce sve potrebno za upis podataka za izradu naocala
 *         ili podatke o vec izradjenim naocalama
 */
public final class OsobineNaocalaPanel extends JPanel implements
		SlusacOznaceneLabelePretrazivanja {
	private javax.swing.JLabel jlNazivSvojstva = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtDsphD = null;
	private javax.swing.JTextField jtDcylD = null;
	private javax.swing.JTextField jtAxD = null;
	private javax.swing.JTextField jtDsphL = null;
	private javax.swing.JTextField jtDcylL = null;
	private javax.swing.JTextField jtAxL = null;
	private javax.swing.JTextField jtNapomenaD = null;
	private javax.swing.JTextField jtNapomenaL = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JTextField jtModelNaocala = null;
	private javax.swing.JButton jbSlika = null;
	private NaocaleVO ulazni;
	private PretrazivanjeProzor pretrazivanje = null;
	private JFrame nosac = null;
	// 26.07.05. -asabo- ovdje ce lezati zapis o eventualnom modelu naocala
	ArtiklVO artikl = null;
	Dimension min = new Dimension(430, 137);

	// 09.10.05. -asabo- korisnik ce zapisati marku proizvodjaca ako hoce
	ProizvodjacVO proizvodjacVO = null;
	PretrazivanjeProzor proizvodjaciPretrazivanje = null;

	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JTextField jtNapomenaCvike = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField jtLoyaltyKartica = null;
	private javax.swing.JCheckBox jcZaSunce = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JTextField jtProizvodjac = null;

	// 24.11.05. -asabo- moramo znati jeli forma omogucena ili nije...
	private boolean omogucen = false;

	private javax.swing.JLabel jLabel10 = null;
	private javax.swing.JTextField jtKvalitetaLeca = null;
	private javax.swing.JLabel jLabel11 = null;
	private javax.swing.JTextField jtFi2 = null;
	private javax.swing.JTextField jtFi1 = null;
	private javax.swing.JLabel jLabel12 = null;
	private javax.swing.JLabel jLabel13 = null;
	private javax.swing.JTextField jtAdd = null;
	private javax.swing.JLabel jLabel14 = null;
	private javax.swing.JTextField jtSloj = null;
	private javax.swing.JLabel jLabel15 = null;
	private javax.swing.JTextField jtPdD = null;
	private javax.swing.JTextField jtPdL = null;
	private javax.swing.JLabel jLabel16 = null;
	private javax.swing.JLabel jLabel17 = null;
	private javax.swing.JTextField jtPrisD = null;
	private javax.swing.JTextField jtBPrisD = null;
	private javax.swing.JTextField jtPrisL = null;
	private javax.swing.JTextField jtBPrisL = null;
	private javax.swing.JPanel jpFI = null;
	private javax.swing.JLabel jLabel18 = null;
	private javax.swing.JTextField jtAddD = null;
	private javax.swing.JTextField jtAddL = null;

	public OsobineNaocalaPanel(JFrame nosac) {
		super();
		this.nosac = nosac;
		this.initialize();
		this.setMinimumSize(min);
	}

	/**
	 * This is the default constructor
	 */
	public OsobineNaocalaPanel() {
		super();
		initialize();
		this.setMinimumSize(min);
		this.onemoguci();
	}

	public void omoguci() {
		this.postaviStatusOmogucenostiElemenata(true);
	}

	public void onemoguci() {
		this.postaviStatusOmogucenostiElemenata(false);
	}

	// 24.11.05. -asabo- jeli forma omogucena ili nije
	public boolean isOmogucen() {
		return this.omogucen;
	}

	// 14.11.05. -asabo- postavljamo status elemenata na formi
	public void postaviStatusOmogucenostiElemenata(boolean status) {
		// ne mozemo samo napraviti this.setEnabled(status) jer ne utjece na
		// djecu
		this.jtDsphD.setEnabled(status);
		this.jtDcylD.setEnabled(status);
		this.jtAxD.setEnabled(status);
		this.jtNapomenaD.setEnabled(status);

		this.jtDsphL.setEnabled(status);
		this.jtDcylL.setEnabled(status);
		this.jtAxL.setEnabled(status);
		this.jtNapomenaL.setEnabled(status);

		this.jtNapomenaCvike.setEnabled(status);
		this.jtModelNaocala.setEnabled(status);
		this.jcZaSunce.setEnabled(status);
		this.jtLoyaltyKartica.setEnabled(status);
		this.jtProizvodjac.setEnabled(status);

		this.jbSlika.setEnabled(status); // 25.11.05. -asabo-

		this.jtFi1.setEnabled(status);
		this.jtFi2.setEnabled(status);
		this.jtAdd.setEnabled(status);
		this.jtKvalitetaLeca.setEnabled(status);
		this.jtSloj.setEnabled(status);

		this.jtPdD.setEnabled(status);
		this.jtPdL.setEnabled(status);

		this.jtPrisD.setEnabled(status);
		this.jtPrisL.setEnabled(status);
		this.jtBPrisD.setEnabled(status);
		this.jtBPrisL.setEnabled(status);
		this.jtAddD.setEnabled(status);
		this.jtAddL.setEnabled(status);

		this.omogucen = status;
	}// omoguci

	public void napuniFormu(NaocaleVO ulazni) {
		try {
			// 25.11.05. -asabo- za svaki slucaj treba biti siguran da je mjesto
			// gdje sjedi oznacena cvika prazno
			this.setArtikl(null);

			this.ulazni = ulazni;
			if (ulazni == null) {
				return;
			}
			this.omoguci();

			NaocalnaLecaVO tmp = null;

			this.jtModelNaocala.setText(getNazivModela(ulazni));
			// 05.10.05. -asabo- dodano
			this.jtNapomenaCvike.setText(ulazni.getNapomena());
			this.jcZaSunce.setSelected(ulazni.isZaSunce());
			if (ulazni.getBrojKartice() > 0L)
				this.jtLoyaltyKartica.setText("" + ulazni.getBrojKartice());
			else
				this.jtLoyaltyKartica.setText("");

			ProizvodjacVO pvo = getProizvodjac(ulazni);
			this.proizvodjacVO = pvo;
			this.jtProizvodjac.setText(pvo != null ? pvo.getNaziv() : "");

			// 13.03.2006. -asabo- dodano
			this.jtFi1.setText(ulazni.getFi1() != null ? ulazni.getFi1() : "");
			this.jtFi2.setText(ulazni.getFi2() != null ? ulazni.getFi2() : "");
			this.jtAdd.setText(ulazni.getAdd() != null ? ulazni.getAdd() : "");
			this.jtKvalitetaLeca
					.setText(ulazni.getKvalitetaLeca() != null ? ulazni
							.getKvalitetaLeca() : "");
			this.jtSloj.setText(ulazni.getSloj() != null ? ulazni.getSloj()
					: "");

			tmp = ulazni.getDesna();

			if (tmp != null) {
				this.jtDsphD.setText(tmp.getDsph());
				this.jtDcylD.setText(tmp.getDcyl());
				this.jtAxD.setText(tmp.getAx());
				this.jtNapomenaD.setText(tmp.getNapomena());
				this.jtPdD.setText(tmp.getPd() != null ? tmp.getPd() : ""); // 20.11.06.
																			// -asabo-
																			// dodano
				this.jtPrisD
						.setText(tmp.getPris() != null ? tmp.getPris() : ""); // 13.12.06.
																				// -asabo-
																				// dodano
				this.jtBPrisD.setText(tmp.getBpris() != null ? tmp.getBpris()
						: "");
				this.jtAddD.setText(tmp.getAdicija() != null ? tmp.getAdicija()
						: "");
			}// if tmp!=null

			tmp = ulazni.getLijeva();

			if (tmp != null) {
				this.jtDsphL.setText(tmp.getDsph());
				this.jtDcylL.setText(tmp.getDcyl());
				this.jtAxL.setText(tmp.getAx());
				this.jtNapomenaL.setText(tmp.getNapomena());
				this.jtPdL.setText(tmp.getPd() != null ? tmp.getPd() : ""); // 20.11.06.
																			// -asabo-
																			// dodano
				this.jtPrisL
						.setText(tmp.getPris() != null ? tmp.getPris() : ""); // 13.12.06.
																				// -asabo-
																				// dodano
				this.jtBPrisL.setText(tmp.getBpris() != null ? tmp.getBpris()
						: "");
				this.jtAddL.setText(tmp.getAdicija() != null ? tmp.getAdicija()
						: "");
			}
			if (this.ulazni != null)
				this.jtModelNaocala.setText(getNazivModela(this.ulazni));
			else
				this.jtModelNaocala.setText("?!?");

		} catch (Error err) {
			Logger.fatal(
					"Greska susstava kod OsobineNaocalaPanel.napuniFormu() ",
					err);
			System.out
					.println("Greska sustava kod osobineNaocalaPanel.napuniFormu: "
							+ err);
			err.printStackTrace();
			throw err;
		}
	}// napuniFormu

	// 15.11.05. -asabo- vraca nazad naziv proizvodjaca za naocale ili null ako
	// nema proizvodjaca
	public final static ProizvodjacVO getProizvodjac(NaocaleVO ulaz) {
		ProizvodjacVO pvo = null;
		// 09.10.05. -asabo- sifra proizvodjaca stakala
		Integer itmp = ulaz.getSifProizvodjacaStakla();
		if (itmp != null) {
			try {
				pvo = (ProizvodjacVO) DAOFactory.getInstance()
						.getProizvodjaci().read(itmp);
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod citanja proizvodjaca stakala u OsobineNaocalaPanel.getProizvodjac() ",
						e);
			}
		}// if

		return pvo;
	}// getProizvodjac

	// 15.11.05. -asabo- gleda postoji li u modelu sifra artikla, ako da, vraca
	// nazad naziv artikla
	// ako ne, vraca ulaz.getModel()
	public static String getNazivModela(NaocaleVO ulaz) {
		if (ulaz == null)
			return null;

		String tmp = ulaz.getModel();
		Integer sf = ulaz.getSifBoje();

		ArtiklVO avo = null;
		Object o = null;

		if (sf != null)
			try {
				o = DAOFactory.getInstance().getArtikli().read(sf);
				if (o != null) {
					avo = (ArtiklVO) o;
					tmp = avo.toString();
				}// if o ispravan
			} catch (SQLException ex) {
				Logger.fatal(
						"SQL iznimka kod OsobineNaocalaPanel.getNazivModela - citanje ArtiklVO na osnovu sifre:"
								+ (sf != null ? sf.intValue() : -1), ex);
				o = null; // obavezno u ovoj situaciji
			}

		return tmp;
	}// getNazivModela

	public void pobrisiFormu() {
		this.onemoguci();
		this.ulazni = null; // 29.06.05 -asabo- treba i ponistiti ulazni
							// parametar da ne bi kasnije presao u neki drugi
							// pregled
		final String p = "";
		this.getJtDsphD().setText(p);
		this.getJtDsphL().setText(p);

		this.getJtDcylD().setText(p);
		this.getJtDcylL().setText(p);

		this.getJtAxD().setText(p);
		this.getJtAxL().setText(p);

		this.getJtNapomenaD().setText(p);
		this.getJtNapomenaL().setText(p);

		this.getJtModelNaocala().setText(p);

		// 05.10.05. -asabo- dodano
		this.getJtNapomenaCvike().setText(p);
		this.getJcZaSunce().setSelected(false);
		this.getJtLoyaltyKartica().setText(p);

		this.jtFi1.setText(p);
		this.jtFi2.setText(p);
		this.jtAdd.setText(p);
		this.jtKvalitetaLeca.setText(p);
		this.jtSloj.setText(p);

		// 20.11.06. -asabo- dodano
		this.jtPdD.setText(p);
		this.jtPdL.setText(p);

		this.jtPrisL.setText(p);
		this.jtPrisD.setText(p);
		this.jtBPrisD.setText(p);
		this.jtBPrisL.setText(p);
		this.jtAddD.setText(p);
		this.jtAddL.setText(p);

		this.proizvodjacVO = null;
		this.jtProizvodjac.setText(p);
		this.setArtikl(null); // 14.11.05. -asabo- dodano
		this.proizvodjacVO = null; // i njega pobrisati...
		this.artikl = null; // 25.11.05. -asabo- takodjer
	}// pobrisiFormu

	// uvijek vrati NaocaleVO objekt, bez obzira bio prazan ili ne
	public NaocaleVO vratiPodatke() {

		NaocaleVO ulazni = this.getUlazni();

		NaocalnaLecaVO d, l;
		d = ulazni.getDesna();
		l = ulazni.getLijeva();

		//
		ulazni.setModified(this.jeliObjektIzmjenjen());

		if (d != null) {
			d.setDsph(this.jtDsphD.getText().trim());
			d.setDcyl(this.jtDcylD.getText().trim());
			d.setAx(this.jtAxD.getText().trim());
			d.setNapomena(this.jtNapomenaD.getText().trim());
			d.setPd(this.jtPdD.getText().trim());
			d.setPris(this.jtPrisD.getText().trim()); // 13.12.06. -asabo-
														// dodano
			d.setBpris(this.jtBPrisD.getText().trim());
			d.setAdicija(this.jtAddD.getText().trim());
		}
		if (l != null) {
			l.setDsph(this.jtDsphL.getText().trim());
			l.setDcyl(this.jtDcylL.getText().trim());
			l.setAx(this.jtAxL.getText().trim());
			l.setNapomena(this.jtNapomenaL.getText().trim());
			l.setPd(this.jtPdL.getText().trim());
			l.setPris(this.jtPrisL.getText().trim()); // 13.12.06. -asabo-
														// dodano
			l.setBpris(this.jtBPrisL.getText().trim());
			l.setAdicija(this.jtAddL.getText().trim());
		}

		if (this.getArtikl() != null) {
			Integer sif = this.getArtikl().getSifra();
			ulazni.setSifBoje(sif);
			ulazni.setModel(null); // ili ima sifru artikla, ili ima tekstualni
									// naziv artikla..
		} else {
			ulazni.setSifBoje(null);
			ulazni.setModel(this.jtModelNaocala.getText());
		}

		// 05.10.05. -asabo- dodano
		ulazni.setNapomena(this.getJtNapomenaCvike().getText());
		long broj = 0;
		String tmp = this.getJtLoyaltyKartica().getText();
		if (tmp != null && !tmp.trim().equals(""))
			try {
				tmp = tmp.trim();
				broj = Long.parseLong(tmp);
			} catch (NumberFormatException nfe) {
				broj = 0L;
			}

		ulazni.setBrojKartice(broj); // ako nema broja, bit ce nula (ne -1)
		ulazni.setZaSunce(this.getJcZaSunce().isSelected());

		if (this.proizvodjacVO == null)
			ulazni.setSifProizvodjacaStakla(null);
		else
			ulazni.setSifProizvodjacaStakla(this.proizvodjacVO.getSifra());

		ulazni.setFi1(this.jtFi1.getText().equals("") ? null : this.jtFi1
				.getText());
		ulazni.setFi2(this.jtFi2.getText().equals("") ? null : this.jtFi2
				.getText());
		ulazni.setAdd(this.jtAdd.getText().equals("") ? null : this.jtAdd
				.getText());
		ulazni.setKvalitetaLeca(this.jtKvalitetaLeca.getText().equals("") ? null
				: this.jtKvalitetaLeca.getText());
		ulazni.setSloj(this.jtSloj.getText().equals("") ? null : this.jtSloj
				.getText());

		return ulazni;
	}// vratiPodatke

	private boolean jeliObjektIzmjenjen() {
		if (this.ulazni == null)
			return false;
		boolean m = false;
		final String p = "";

		NaocalnaLecaVO d, l;
		d = this.ulazni.getDesna();
		l = this.ulazni.getLijeva();

		if (d != null) {
			// ako je objekt prazan, a neka vrijednost postoji na formi, tada je
			// objekt novi i 'izmjenjen' je...
			if (d.getDsph() == null && !this.jtDsphD.getText().equals(p))
				return true;
			if (d.getDcyl() == null && !this.jtDcylD.getText().equals(p))
				return true;
			if (d.getAx() == null && !this.jtAxD.getText().equals(p))
				return true;
			if (d.getNapomena() == null
					&& !this.jtNapomenaD.getText().equals(p))
				return true;
			if (d.getPd() == null && !this.jtPdD.getText().equals(p))
				return true;
		}

		if (l != null) {
			if (l.getDsph() == null && !this.jtDsphL.getText().equals(p))
				return true;
			if (l.getDcyl() == null && !this.jtDcylL.getText().equals(p))
				return true;
			if (l.getAx() == null && !this.jtAxL.getText().equals(p))
				return true;
			if (l.getNapomena() == null
					&& !this.jtNapomenaL.getText().equals(p))
				return true;
			if (l.getPd() == null && !this.jtPdL.getText().equals(p))
				return true;
		}

		// 05.10.05. -asabo- dodana provjera nova tri elementa
		if (this.ulazni.getNapomena() == null
				&& !this.jtNapomenaCvike.getText().equals(p))
			return true;
		if (this.ulazni.getBrojKartice() == 0L
				&& !this.jtLoyaltyKartica.getText().equals(p))
			return true;
		if (this.ulazni.isZaSunce() != this.jcZaSunce.isSelected())
			return true;

		// 13.03.06. -asabo- dodana provjera novih 5 elemenata
		if (this.ulazni.getFi1() == null && !this.jtFi1.getText().equals(p))
			return true;
		if (this.ulazni.getFi2() == null && !this.jtFi2.getText().equals(p))
			return true;
		if (this.ulazni.getAdd() == null && !this.jtAdd.getText().equals(p))
			return true;
		if (this.ulazni.getKvalitetaLeca() == null
				&& !this.jtKvalitetaLeca.getText().equals(p))
			return true;
		if (this.ulazni.getSloj() == null && !this.jtSloj.getText().equals(p))
			return true;

		// 09.10.05. -asabo- provjera za proizvodjaca
		if (this.ulazni.getSifProizvodjacaStakla() == null
				&& this.proizvodjacVO != null)
			return true;

		// 30.08.05. -asabo- dodana provjera i za artikl polje, dosad ga nije
		// bilo, a sad ga ima..
		if (this.ulazni.getSifBoje() == null && this.getArtikl() != null)
			return true;

		// GUI elementi su stavljeni prvi u usporedbu jer ne mogu biti null
		if (d.getDsph() != null
				&& (!this.jtDsphD.getText().equals(d.getDsph())))
			return m = true;
		if (d.getDcyl() != null
				&& (!this.jtDcylD.getText().equals(d.getDcyl())))
			return m = true;
		if (d.getAx() != null && (!this.jtAxD.getText().equals(d.getAx())))
			return m = true;
		if (d.getNapomena() != null
				&& (!this.jtNapomenaD.getText().equals(d.getNapomena())))
			return m = true;
		if (d.getPd() != null && (!this.jtPdD.getText().equals(d.getPd())))
			return m = true;

		if (l.getDsph() != null
				&& (!this.jtDsphL.getText().equals(l.getDsph())))
			return m = true;
		if (l.getDcyl() != null
				&& (!this.jtDcylL.getText().equals(l.getDcyl())))
			return m = true;
		if (l.getAx() != null && (!this.jtAxL.getText().equals(l.getAx())))
			return m = true;
		if (l.getNapomena() != null
				&& (!this.jtNapomenaL.getText().equals(l.getNapomena())))
			return m = true;
		if (l.getPd() != null && (!this.jtPdL.getText().equals(l.getPd())))
			return m = true;

		// 05.10.05. -asabo- dodana provjera nova tri elementa
		if (this.ulazni.getNapomena() != null
				&& (!this.jtNapomenaCvike.getText().equals(
						this.ulazni.getNapomena())))
			return true;

		// 13.03.06. -asabo- dodana provjera novih 5 elemenata
		if (this.ulazni.getFi1() != null
				&& (!this.jtFi1.getText().equals(this.ulazni.getFi1())))
			return true;
		if (this.ulazni.getFi2() != null
				&& (!this.jtFi2.getText().equals(this.ulazni.getFi2())))
			return true;
		if (this.ulazni.getAdd() != null
				&& (!this.jtAdd.getText().equals(this.ulazni.getAdd())))
			return true;
		if (this.ulazni.getKvalitetaLeca() != null
				&& (!this.jtKvalitetaLeca.getText().equals(
						this.ulazni.getKvalitetaLeca())))
			return true;
		if (this.ulazni.getSloj() != null
				&& (!this.jtSloj.getText().equals(this.ulazni.getSloj())))
			return true;

		if (this.ulazni.getBrojKartice() != 0L) {
			long stari = this.ulazni.getBrojKartice();
			long novi = 0L;
			String tmp = this.jtLoyaltyKartica.getText().trim();
			try {
				novi = Long.parseLong(tmp);
			} catch (NumberFormatException nfe) {
				novi = 0L;
			}
			if (novi != stari)
				return true; // cak i ako je novi nula, to znaci da je korisnik
								// pobrisao podatak - znaci izmjenio ga je
								// namjerno
		}// if ulazni nije nula

		// 09.10.05. -asabo- provjera za proizvodjaca
		if (this.ulazni.getSifProizvodjacaStakla() != null
				&& this.proizvodjacVO.getSifra() != null
				&& this.proizvodjacVO.getSifra().intValue() != this.ulazni
						.getSifProizvodjacaStakla().intValue())
			return true;

		// jeli sunce kliknuto ili ne ovdje ne treba provjeravati

		// 30.08.05. -asabo- provjera jeli izmjenjen model naocala, moze biti da
		// ga je dosad bilo
		// a sada ga nema vise na formi
		if ((this.ulazni.getSifBoje() != null
				&& this.ulazni.getSifBoje().intValue() != DAO.NEPOSTOJECA_SIFRA && this
					.getArtikl() == null))
			return true;

		// 30.08.05. -asabo- ili da ga ima i sada i prije ga je bilo, samo su
		// razlicite sifre...
		ArtiklVO avo = this.getArtikl();
		if (this.ulazni != null
				&& this.ulazni.getSifBoje() != null
				&& this.getArtikl() != null
				&& this.ulazni.getSifBoje().intValue() != avo.getSifra()
						.intValue())
			return true;

		return m;
	}// jeliObjektIzmjenjen

	public void setNaziv(String naziv) {
		this.getJlNazivSvojstva().setText(naziv);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints61 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints71 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints81 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints52 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints42 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints53 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints43 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints32 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints23 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints82 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints91 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints101 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints111 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints34 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints25 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints35 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints45 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints54 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints63 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints73 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints33 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints26 = new java.awt.GridBagConstraints();
		consGridBagConstraints33.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints33.weightx = 1.0;
		consGridBagConstraints33.gridy = 2;
		consGridBagConstraints33.gridx = 8;
		consGridBagConstraints16.gridy = 0;
		consGridBagConstraints16.gridx = 8;
		consGridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints26.weightx = 1.0;
		consGridBagConstraints26.gridy = 1;
		consGridBagConstraints26.gridx = 8;
		consGridBagConstraints73.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints73.weighty = 1.0;
		consGridBagConstraints73.weightx = 1.0;
		consGridBagConstraints73.gridy = 3;
		consGridBagConstraints73.gridx = 7;
		consGridBagConstraints73.anchor = java.awt.GridBagConstraints.CENTER;
		consGridBagConstraints73.gridwidth = 3;
		consGridBagConstraints63.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints63.weightx = 1.0;
		consGridBagConstraints63.gridy = 2;
		consGridBagConstraints63.gridx = 7;
		consGridBagConstraints54.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints54.weightx = 1.0;
		consGridBagConstraints54.gridy = 2;
		consGridBagConstraints54.gridx = 6;
		consGridBagConstraints45.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints45.weightx = 1.0;
		consGridBagConstraints45.gridy = 1;
		consGridBagConstraints45.gridx = 7;
		consGridBagConstraints15.gridy = 0;
		consGridBagConstraints15.gridx = 6;
		consGridBagConstraints25.gridy = 0;
		consGridBagConstraints25.gridx = 7;
		consGridBagConstraints35.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints35.weightx = 1.0;
		consGridBagConstraints35.gridy = 1;
		consGridBagConstraints35.gridx = 6;
		consGridBagConstraints34.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints34.weightx = 1.0;
		consGridBagConstraints34.gridy = 2;
		consGridBagConstraints34.gridx = 5;
		consGridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints24.weightx = 1.0;
		consGridBagConstraints24.gridy = 1;
		consGridBagConstraints24.gridx = 5;
		consGridBagConstraints14.gridy = 0;
		consGridBagConstraints14.gridx = 5;
		consGridBagConstraints101.gridy = 5;
		consGridBagConstraints101.gridx = 7;
		consGridBagConstraints101.gridheight = 1;
		consGridBagConstraints101.gridwidth = 2;
		consGridBagConstraints111.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints111.weightx = 1.0;
		consGridBagConstraints111.gridy = 5;
		consGridBagConstraints111.gridx = 9;
		consGridBagConstraints111.gridwidth = 7;
		consGridBagConstraints91.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints91.weightx = 1.0;
		consGridBagConstraints91.gridy = 3;
		consGridBagConstraints91.gridx = 15;
		consGridBagConstraints82.gridy = 3;
		consGridBagConstraints82.gridx = 14;
		consGridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints23.weightx = 1.0;
		consGridBagConstraints23.gridy = 4;
		consGridBagConstraints23.gridx = 2;
		consGridBagConstraints23.gridwidth = 10;
		consGridBagConstraints13.gridy = 4;
		consGridBagConstraints13.gridx = 0;
		consGridBagConstraints13.gridwidth = 2;
		consGridBagConstraints13.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints43.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints43.weightx = 1.0;
		consGridBagConstraints43.gridy = 6;
		consGridBagConstraints43.gridx = 15;
		consGridBagConstraints32.gridy = 6;
		consGridBagConstraints32.gridx = 7;
		consGridBagConstraints32.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints53.gridy = 5;
		consGridBagConstraints53.gridx = 6;
		consGridBagConstraints42.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints42.weightx = 1.0;
		consGridBagConstraints42.gridy = 6;
		consGridBagConstraints42.gridx = 2;
		consGridBagConstraints31.gridy = 6;
		consGridBagConstraints31.gridx = 0;
		consGridBagConstraints31.gridwidth = 2;
		consGridBagConstraints31.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints22.weightx = 1.0;
		consGridBagConstraints22.gridy = 3;
		consGridBagConstraints22.gridx = 2;
		consGridBagConstraints12.gridy = 3;
		consGridBagConstraints12.gridx = 0;
		consGridBagConstraints12.gridheight = 1;
		consGridBagConstraints12.gridwidth = 2;
		consGridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints52.gridy = 5;
		consGridBagConstraints52.gridx = 4;
		consGridBagConstraints52.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 5;
		consGridBagConstraints21.gridx = 2;
		consGridBagConstraints21.gridwidth = 2;
		consGridBagConstraints11.gridy = 5;
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints11.gridwidth = 2;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 3;
		consGridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints3.weightx = 1.0;
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 4;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints1.weightx = 1.0;
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 2;
		consGridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints61.weightx = 1.0;
		consGridBagConstraints61.gridy = 2;
		consGridBagConstraints61.gridx = 4;
		consGridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints71.weightx = 1.0;
		consGridBagConstraints71.gridy = 1;
		consGridBagConstraints71.gridx = 9;
		consGridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints41.weightx = 1.0;
		consGridBagConstraints41.gridy = 2;
		consGridBagConstraints41.gridx = 2;
		consGridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints51.weightx = 1.0;
		consGridBagConstraints51.gridy = 2;
		consGridBagConstraints51.gridx = 3;
		consGridBagConstraints81.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints81.weightx = 1.0;
		consGridBagConstraints81.gridy = 2;
		consGridBagConstraints81.gridx = 9;
		consGridBagConstraints8.gridy = 0;
		consGridBagConstraints8.gridx = 3;
		consGridBagConstraints8.ipadx = 5;
		consGridBagConstraints6.gridy = 2;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints10.gridy = 0;
		consGridBagConstraints10.gridx = 9;
		consGridBagConstraints10.ipadx = 5;
		consGridBagConstraints5.gridy = 1;
		consGridBagConstraints5.gridx = 1;
		consGridBagConstraints7.gridy = 0;
		consGridBagConstraints7.gridx = 2;
		consGridBagConstraints7.ipadx = 5;
		consGridBagConstraints9.gridy = 0;
		consGridBagConstraints9.gridx = 4;
		consGridBagConstraints9.ipadx = 5;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 0;
		consGridBagConstraints4.gridheight = 2;
		consGridBagConstraints4.ipadx = 5;
		consGridBagConstraints1.ipadx = 1;
		consGridBagConstraints22.gridwidth = 4;
		consGridBagConstraints53.gridwidth = 2;
		consGridBagConstraints43.gridwidth = 1;
		consGridBagConstraints42.gridwidth = 2;
		consGridBagConstraints81.gridwidth = 7;
		consGridBagConstraints71.gridwidth = 7;
		consGridBagConstraints82.insets = new java.awt.Insets(0, 0, 0, 2);
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints111.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints32.gridwidth = 3;
		consGridBagConstraints43.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints101.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints53.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints52.gridwidth = 2;
		consGridBagConstraints91.anchor = java.awt.GridBagConstraints.WEST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJlNazivSvojstva(), consGridBagConstraints4);
		this.add(getJLabel(), consGridBagConstraints5);
		this.add(getJLabel1(), consGridBagConstraints6);
		this.add(getJLabel2(), consGridBagConstraints7);
		this.add(getJLabel3(), consGridBagConstraints8);
		this.add(getJLabel4(), consGridBagConstraints9);
		this.add(getJLabel5(), consGridBagConstraints10);
		this.add(getJtDsphD(), consGridBagConstraints1);
		this.add(getJtDcylD(), consGridBagConstraints2);
		this.add(getJtAxD(), consGridBagConstraints3);
		this.add(getJtDsphL(), consGridBagConstraints41);
		this.add(getJtDcylL(), consGridBagConstraints51);
		this.add(getJtAxL(), consGridBagConstraints61);
		this.add(getJtNapomenaD(), consGridBagConstraints71);
		this.add(getJtNapomenaL(), consGridBagConstraints81);
		this.add(getJLabel6(), consGridBagConstraints11);
		this.add(getJtModelNaocala(), consGridBagConstraints21);
		this.add(getJbSlika(), consGridBagConstraints52);
		this.add(getJLabel7(), consGridBagConstraints12);
		this.add(getJtNapomenaCvike(), consGridBagConstraints22);
		this.add(getJLabel8(), consGridBagConstraints31);
		this.add(getJtLoyaltyKartica(), consGridBagConstraints42);
		this.add(getJcZaSunce(), consGridBagConstraints53);
		this.add(getJLabel9(), consGridBagConstraints32);
		this.add(getJtProizvodjac(), consGridBagConstraints43);
		this.add(getJLabel10(), consGridBagConstraints13);
		this.add(getJtKvalitetaLeca(), consGridBagConstraints23);
		this.add(getJLabel13(), consGridBagConstraints82);
		this.add(getJtAdd(), consGridBagConstraints91);
		this.add(getJLabel14(), consGridBagConstraints101);
		this.add(getJtSloj(), consGridBagConstraints111);
		this.add(getJLabel15(), consGridBagConstraints14);
		this.add(getJtPdD(), consGridBagConstraints24);
		this.add(getJtPdL(), consGridBagConstraints34);
		this.add(getJLabel16(), consGridBagConstraints15);
		this.add(getJLabel17(), consGridBagConstraints25);
		this.add(getJtPrisD(), consGridBagConstraints35);
		this.add(getJtBPrisD(), consGridBagConstraints45);
		this.add(getJtPrisL(), consGridBagConstraints54);
		this.add(getJtBPrisL(), consGridBagConstraints63);
		this.add(getJpFI(), consGridBagConstraints73);
		this.add(getJLabel18(), consGridBagConstraints16);
		this.add(getJtAddD(), consGridBagConstraints26);
		this.add(getJtAddL(), consGridBagConstraints33);
		this.setSize(690, 170);
		this.setBorder(new javax.swing.border.SoftBevelBorder(
				BevelBorder.RAISED));
		this.setPreferredSize(new java.awt.Dimension(690, 170));
		this.setMinimumSize(new java.awt.Dimension(590, 170));
		this.setToolTipText("FI stakala...");
	}

	/**
	 * This method initializes jlNazivSvojstva
	 * 
	 * @return javax.swing.JLabel
	 */
	public javax.swing.JLabel getJlNazivSvojstva() {
		if (jlNazivSvojstva == null) {
			jlNazivSvojstva = new javax.swing.JLabel();
			jlNazivSvojstva.setText("Naziv svojstva");
			jlNazivSvojstva.setFont(new java.awt.Font("Dialog",
					java.awt.Font.BOLD, 11));
		}
		return jlNazivSvojstva;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("  D");
			jLabel.setPreferredSize(new java.awt.Dimension(20, 16));
			jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
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
			jLabel1.setText("  L");
			jLabel1.setPreferredSize(new java.awt.Dimension(20, 16));
			jLabel1.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
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
			jLabel2.setText("Dsph");
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
			jLabel3.setText("Dcyl");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("AX");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Napomena");
		}
		return jLabel5;
	}

	/**
	 * This method initializes jtDsphD
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDsphD() {
		if (jtDsphD == null) {
			jtDsphD = new javax.swing.JTextField();
			jtDsphD.setPreferredSize(new java.awt.Dimension(55, 20));
			jtDsphD.setMinimumSize(new java.awt.Dimension(35, 20));
		}
		return jtDsphD;

	}

	/**
	 * This method initializes jtDcylD
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDcylD() {
		if (jtDcylD == null) {
			jtDcylD = new javax.swing.JTextField();
			jtDcylD.setPreferredSize(new java.awt.Dimension(90, 20));
			jtDcylD.setMinimumSize(new java.awt.Dimension(70, 20));

		}
		return jtDcylD;
	}

	/**
	 * This method initializes jtAxD
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtAxD() {
		if (jtAxD == null) {
			jtAxD = new javax.swing.JTextField();
			jtAxD.setPreferredSize(new java.awt.Dimension(90, 20));
			jtAxD.setMinimumSize(new java.awt.Dimension(25, 20));

		}
		return jtAxD;
	}

	/**
	 * This method initializes jtDsphL
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDsphL() {
		if (jtDsphL == null) {
			jtDsphL = new javax.swing.JTextField();
			jtDsphL.setPreferredSize(new java.awt.Dimension(50, 20));
		}
		return jtDsphL;
	}

	/**
	 * This method initializes jtDcylL
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDcylL() {
		if (jtDcylL == null) {
			jtDcylL = new javax.swing.JTextField();
			jtDcylL.setPreferredSize(new java.awt.Dimension(90, 20));
		}
		return jtDcylL;
	}

	/**
	 * This method initializes jtAxL
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtAxL() {
		if (jtAxL == null) {
			jtAxL = new javax.swing.JTextField();
			jtAxL.setPreferredSize(new java.awt.Dimension(90, 20));
		}
		return jtAxL;
	}

	/**
	 * This method initializes jtNapomenaD
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtNapomenaD() {
		if (jtNapomenaD == null) {
			jtNapomenaD = new javax.swing.JTextField();
			jtNapomenaD.setPreferredSize(new java.awt.Dimension(80, 20));
		}
		return jtNapomenaD;
	}

	/**
	 * This method initializes jtNapomenaL
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtNapomenaL() {
		if (jtNapomenaL == null) {
			jtNapomenaL = new javax.swing.JTextField();
		}
		return jtNapomenaL;
	}

	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Model naoèala: ");
		}
		return jLabel6;
	}

	/**
	 * This method initializes jtModelNaocala
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtModelNaocala() {
		if (jtModelNaocala == null) {
			jtModelNaocala = new javax.swing.JTextField();
			jtModelNaocala.setToolTipText("model okvira naoèala");
			jtModelNaocala.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(java.awt.event.KeyEvent e) {
					// pretrazivanje.setMjesto(jtModelNaocala.getLocationOnScreen(),true);
				}

				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
							}
							if (jtModelNaocala.getText().equals(""))
								setArtikl(null);
							else
							// 05.10.05. -asabo- dodana 'fora' za
							// ukljuciti/iskljuciti 'za sunce' cbox
							if (jtModelNaocala.getText().startsWith("S"))
								jcZaSunce.setSelected(true);
							else if (jtModelNaocala.getText().startsWith("V"))
								jcZaSunce.setSelected(false);
						}
					}.start();
				}// keyTypedza
			});
			this.pretrazivanje = new PretrazivanjeProzor(this.nosac, DAOFactory
					.getInstance().getArtikli(), 20, 15, 120, 150,
					this.jtModelNaocala);

			this.pretrazivanje.setMinimumZnakovaZaPretrazivanje(3);
			this.pretrazivanje.dodajSlusaca(this);
		}// if jtModeloNaocala==null
		return jtModelNaocala;
	}

	/**
	 * This method initializes jbSlika
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbSlika() {
		if (jbSlika == null) {
			jbSlika = new javax.swing.JButton();
			jbSlika.setText("slika");
			jbSlika.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			jbSlika.setPreferredSize(new java.awt.Dimension(60, 17));
			jbSlika.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (getArtikl() == null)
						return;

					ArtiklVO avo = getArtikl();
					BufferedImage slika = null;
					SlikeDAO sdao = DAOFactory.getInstance().getSlike();
					SearchCriteria krit = new SearchCriteria();
					krit.setKriterij(DAO.KRITERIJ_SLIKA_NAOCALE);
					java.util.List l = new ArrayList(1);
					l.add(avo);
					krit.setPodaci(l);
					try {
						SlikaVO o = sdao.read(krit);
						if (o != null)
							slika = o.getSlika();
						else
							slika = null;
					} catch (SQLException ex) {
						Logger.fatal(
								"SQL Iznimka kod trazenja fotografije za artikl. OsobineNaocalaPanel",
								ex);
						return;
					}
					if (slika == null)
						return;

					SlikaFrame sf = new SlikaFrame(slika);
					// sf.setSize(slika.getWidth()+2,slika.getHeight()+15);
					sf.setTitle("fotografija artikla - " + avo.getNazivMarke()
							+ " " + avo.toString());

					sf.show();
					sf.prilagodiFrameVeliciniSlike();
				}// mouseClicked gumba 'slika'
			});
		}
		return jbSlika;
	}// getJbSlika

	public NaocaleVO getUlazni() {
		if (this.ulazni == null) {
			this.ulazni = new NaocaleVO();
			this.ulazni.setLijeva(new NaocalnaLecaVO());
			this.ulazni.setDesna(new NaocalnaLecaVO());
			// ne treba postavljati modified na true, radi se o novom objektu

			this.ulazni.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
			this.ulazni.setSifBoje(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
			this.ulazni.setCreatedBy(Integer.valueOf(GlavniFrame
					.getSifDjelatnika()));
			this.ulazni.setDatum(java.util.Calendar.getInstance()); // zasada
																	// samo
																	// tako..
		}// if
		return this.ulazni;
	}// getUlazni

	public ArtiklVO getArtikl() {
		return artikl;
	}

	public void labelaOznacena(Labela labela) {
		if (labela == null)
			return;
		else if (labela.getIzvornik() != null
				&& labela.getIzvornik() instanceof ArtiklVO) {
			ArtiklVO cvika = (ArtiklVO) labela.getIzvornik();
			this.setArtikl(cvika);
			this.jtModelNaocala.setText(cvika.toString());
		}// if
		else if (labela.getIzvornik() != null
				&& labela.getIzvornik() instanceof ProizvodjacVO) {
			ProizvodjacVO proizvodjac = (ProizvodjacVO) labela.getIzvornik();
			this.proizvodjacVO = proizvodjac;
			this.jtProizvodjac.setText(proizvodjac.toString());
		}// if

	}// labelaOznacena

	public void setArtikl(ArtiklVO artikl) {
		this.artikl = artikl;
		if (this.artikl == null && this.jcZaSunce != null)
			this.jcZaSunce.setSelected(false); // 15.11.05. -asabo- za svaki
												// slicaj
	}

	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if (jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Napomena: ");
		}
		return jLabel7;
	}

	/**
	 * This method initializes jtNapomenaCvike
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtNapomenaCvike() {
		if (jtNapomenaCvike == null) {
			jtNapomenaCvike = new javax.swing.JTextField();
		}
		return jtNapomenaCvike;
	}

	/**
	 * This method initializes jLabel8
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if (jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("Loyalty kartica: ");
		}
		return jLabel8;
	}

	/**
	 * This method initializes jtLoyaltyKartica
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtLoyaltyKartica() {
		if (jtLoyaltyKartica == null) {
			jtLoyaltyKartica = new javax.swing.JTextField();
		}
		return jtLoyaltyKartica;
	}

	/**
	 * This method initializes jcZaSunce
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcZaSunce() {
		if (jcZaSunce == null) {
			jcZaSunce = new javax.swing.JCheckBox();
			jcZaSunce.setName("za sunce");
			jcZaSunce.setText("za sunce");
		}
		return jcZaSunce;
	}

	/**
	 * This method initializes jLabel9
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if (jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText("Proizvoðaè leæa: ");
		}
		return jLabel9;
	}

	/**
	 * This method initializes jtProizvodjac
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtProizvodjac() {
		if (jtProizvodjac == null) {
			jtProizvodjac = new javax.swing.JTextField();
			jtProizvodjac.setToolTipText("marka leæa...");
			jtProizvodjac.setPreferredSize(new java.awt.Dimension(120, 20));
			jtProizvodjac.setMinimumSize(new java.awt.Dimension(100, 20));
			jtProizvodjac.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(java.awt.event.KeyEvent e) {
					// brisemo eventualnu vrijednost trenutnog proizvodjaca
					// postoji ...
					if (jtProizvodjac.getText().equals(""))
						proizvodjacVO = null;
				}
			});
			this.pretrazivanje = new PretrazivanjeProzor(this.nosac, DAOFactory
					.getInstance().getProizvodjaci(), 10, 10, 150, 100,
					jtProizvodjac);
			SearchCriteria kr = new SearchCriteria();
			// pretrazivanjeProzor trebao bi gledati samo unutra proizvodjaca
			// koji
			// rade samo stakla
			kr.setKriterij(ProizvodjaciDAO.KRITERIJ_PROIZVODJACI_SAMO_STAKLA);
			this.pretrazivanje.setKriterij(kr);
			this.pretrazivanje.dodajSlusaca(this);
		}
		return jtProizvodjac;
	}

	/**
	 * This method initializes jLabel10
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel10() {
		if (jLabel10 == null) {
			jLabel10 = new javax.swing.JLabel();
			jLabel10.setText("Kvaliteta leæa: ");
		}
		return jLabel10;
	}

	/**
	 * This method initializes jtKvalitetaLeca
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtKvalitetaLeca() {
		if (jtKvalitetaLeca == null) {
			jtKvalitetaLeca = new javax.swing.JTextField();
			jtKvalitetaLeca.setToolTipText("opisne osobine ugraðenih leæa");
		}
		return jtKvalitetaLeca;
	}

	/**
	 * This method initializes jLabel11
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel11() {
		if (jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setText("FI: ");
			jLabel11.setPreferredSize(new java.awt.Dimension(16, 16));
		}
		return jLabel11;
	}

	/**
	 * This method initializes jtFi2
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtFi2() {
		if (jtFi2 == null) {
			jtFi2 = new javax.swing.JTextField();
			jtFi2.setPreferredSize(new java.awt.Dimension(30, 20));
			jtFi2.setMinimumSize(new java.awt.Dimension(30, 20));
		}
		return jtFi2;
	}

	/**
	 * This method initializes jtFi1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtFi1() {
		if (jtFi1 == null) {
			jtFi1 = new javax.swing.JTextField();
			jtFi1.setPreferredSize(new java.awt.Dimension(30, 20));
			jtFi1.setMinimumSize(new java.awt.Dimension(30, 20));

		}
		return jtFi1;
	}

	/**
	 * This method initializes jLabel12
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel12() {
		if (jLabel12 == null) {
			jLabel12 = new javax.swing.JLabel();
			jLabel12.setText(" / ");
			jLabel12.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
		}
		return jLabel12;
	}

	/**
	 * This method initializes jLabel13
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel13() {
		if (jLabel13 == null) {
			jLabel13 = new javax.swing.JLabel();
			jLabel13.setText("Add:");
		}
		return jLabel13;
	}

	/**
	 * This method initializes jtAdd
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAdd() {
		if (jtAdd == null) {
			jtAdd = new javax.swing.JTextField();
			jtAdd.setPreferredSize(new java.awt.Dimension(60, 20));
			jtAdd.setMinimumSize(new java.awt.Dimension(60, 20));

			jtAdd.setToolTipText("opæenita adicija ako vrijedi za oba stakla");
		}
		return jtAdd;
	}

	/**
	 * This method initializes jLabel14
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel14() {
		if (jLabel14 == null) {
			jLabel14 = new javax.swing.JLabel();
			jLabel14.setText("Sloj: ");
		}
		return jLabel14;
	}

	/**
	 * This method initializes jtSloj
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtSloj() {
		if (jtSloj == null) {
			jtSloj = new javax.swing.JTextField();
			jtSloj.setToolTipText("sloj na leæama... ");
		}
		return jtSloj;
	}

	/**
	 * This method initializes jLabel15
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel15() {
		if (jLabel15 == null) {
			jLabel15 = new javax.swing.JLabel();
			jLabel15.setText("PD");
		}
		return jLabel15;
	}

	/**
	 * This method initializes jtPdD
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPdD() {
		if (jtPdD == null) {
			jtPdD = new javax.swing.JTextField();
			jtPdD.setPreferredSize(new java.awt.Dimension(70, 20));
			jtPdD.setMinimumSize(new java.awt.Dimension(30, 20));
			jtPdD.setToolTipText("Razmak zjenica za desno oko");
		}
		return jtPdD;
	}

	/**
	 * This method initializes jtPdL
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPdL() {
		if (jtPdL == null) {
			jtPdL = new javax.swing.JTextField();
			jtPdL.setPreferredSize(new java.awt.Dimension(70, 20));
			jtPdL.setToolTipText("Razmak zjenica za lijevo oko");
		}
		return jtPdL;
	}

	/**
	 * This method initializes jLabel16
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel16() {
		if (jLabel16 == null) {
			jLabel16 = new javax.swing.JLabel();
			jLabel16.setText("Pris");
		}
		return jLabel16;
	}

	/**
	 * This method initializes jLabel17
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel17() {
		if (jLabel17 == null) {
			jLabel17 = new javax.swing.JLabel();
			jLabel17.setText("B.Pris");
		}
		return jLabel17;
	}

	/**
	 * This method initializes jtPrisD
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPrisD() {
		if (jtPrisD == null) {
			jtPrisD = new javax.swing.JTextField();
			jtPrisD.setPreferredSize(new java.awt.Dimension(70, 20));
			jtPrisD.setMinimumSize(new java.awt.Dimension(30, 20));
		}
		return jtPrisD;
	}

	/**
	 * This method initializes jtBPrisD
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBPrisD() {
		if (jtBPrisD == null) {
			jtBPrisD = new javax.swing.JTextField();
			jtBPrisD.setPreferredSize(new java.awt.Dimension(70, 20));
			jtBPrisD.setMinimumSize(new java.awt.Dimension(30, 20));
		}
		return jtBPrisD;
	}

	/**
	 * This method initializes jtPrisL
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPrisL() {
		if (jtPrisL == null) {
			jtPrisL = new javax.swing.JTextField();
		}
		return jtPrisL;
	}

	/**
	 * This method initializes jtBPrisL
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBPrisL() {
		if (jtBPrisL == null) {
			jtBPrisL = new javax.swing.JTextField();
		}
		return jtBPrisL;
	}

	/**
	 * This method initializes jpFI
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpFI() {
		if (jpFI == null) {
			jpFI = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout8 = new java.awt.FlowLayout();
			layFlowLayout8.setHgap(0);
			layFlowLayout8.setVgap(0);
			layFlowLayout8.setAlignment(java.awt.FlowLayout.CENTER);
			jpFI.setLayout(layFlowLayout8);
			jpFI.add(getJLabel11(), null);
			jpFI.add(getJtFi1(), null);
			jpFI.add(getJLabel12(), null);
			jpFI.add(getJtFi2(), null);
			jpFI.setPreferredSize(new java.awt.Dimension(126, 20));
			jpFI.setMinimumSize(new java.awt.Dimension(100, 21));
			jpFI.setVisible(true);
		}
		return jpFI;
	}

	/**
	 * This method initializes jLabel18
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel18() {
		if (jLabel18 == null) {
			jLabel18 = new javax.swing.JLabel();
			jLabel18.setText("Add");
		}
		return jLabel18;
	}

	/**
	 * This method initializes jtAddD
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAddD() {
		if (jtAddD == null) {
			jtAddD = new javax.swing.JTextField();
			jtAddD.setPreferredSize(new java.awt.Dimension(70, 20));
			jtAddD.setPreferredSize(new java.awt.Dimension(35, 20));
		}
		return jtAddD;
	}

	/**
	 * This method initializes jtAddL
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAddL() {
		if (jtAddL == null) {
			jtAddL = new javax.swing.JTextField();
		}
		return jtAddL;
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
