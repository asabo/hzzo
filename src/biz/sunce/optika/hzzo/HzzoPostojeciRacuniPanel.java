/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.print.PrintService;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.PomagaloDAO;
import biz.sunce.dao.RacunDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.StavkaRacunaDAO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.optika.hzzo.ispis.IspisRacunaDopunskoOsiguranje;
import biz.sunce.optika.hzzo.ispis.IspisRacunaOsnovnoOsiguranje;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;
import biz.sunce.util.beans.PostavkeBean;
import biz.sunce.util.gui.DaNeUpit;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.SlusacDateChoosera;

/**
 * datum:2006.03.11
 * 
 * @author asabo
 * 
 */
public final class HzzoPostojeciRacuniPanel extends JPanel implements
		SlusacModelaTablice, SlusacOznaceneLabelePretrazivanja,
		SlusacDateChoosera {
	private static final long serialVersionUID = 1L;
	private static final char STATUS_STORNIRAN = DAO.STATUS_STORNIRAN_PS.charAt(0);

	private javax.swing.JLabel jLabel = null;
	private JDateChooser datumOd = null;
	private javax.swing.JLabel jLabel1 = null;
	private JDateChooser datumDo = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtKlijent = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtPodrucniUred = null;
	private javax.swing.JScrollPane jspPodaci = null;
	private JXTable podaci = null;
	private TableModel<RacunVO> racuniModel = null;
	private SearchCriteria kriterij = null;
	private static KlijentVO oznaceniKlijent = null;
	private PretrazivanjeProzor pretrazivanjeKlijenti = null;
	private PretrazivanjeProzor pretrazivanjePodruznice = null;
	private static PomagaloVO oznacenoPomagalo = null;
	private static LijecnikVO oznaceniLijecnik = null;
	private PretrazivanjeProzor pretrazivanjeLijecnici = null;
	private PretrazivanjeProzor pretrazivanjePomagala = null;

	private static MjestoVO oznacenoMjesto = null;
	private static String brojOsobnogRacuna = null; // 27.9.06. -asabo- dodano
	private static String strPomagalo = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtLijecnik = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtArtikl = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JTextField jtBrojOsobnogRacuna = null;
	private static Calendar oznaceniDatumOd = null, oznaceniDatumDo = null;

	private javax.swing.JPopupMenu popup = null;

	ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			popupOdabran(event);
		}
	};

	public void popupOdabran(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("Ispiši raèun(e)")) {
			this.ispisiOznaceneRacune();
		} else if (event.getActionCommand().equalsIgnoreCase(
				"Storniraj raèun(e)")) {
			this.stornirajOznaceneRacune();
		}

	}// popupOdabran

	private void stornirajOznaceneRacune() {
		int[] retci = this.podaci.getSelectedRows();

		int rlen = retci.length;
		if (rlen == 0) {
			GlavniFrame.alert("Niste oznaèili niti jedan raèun!");
			return;
		}

		boolean odg = DaNeUpit.upit("Jeste li sigurni da želite stornirati "
				+ (rlen == 1 ? "raèun" : "raèune") + "?", "Stornirati?",
				GlavniFrame.getInstanca());

		if (!odg)
			return;

		boolean rez = true;
		int storniranih = 0;
		List<RacunVO> racunData = this.racuniModel.getData();

		for (int i = 0; i < rlen; i++) {
			
			RacunVO rvo = (RacunVO) racunData.get(retci[i]);
			boolean vecStorniran = rvo.getStatus() == STATUS_STORNIRAN;
			if (rlen == 1 && vecStorniran) {
				GlavniFrame.alert("Raèun je veè storniran otprije!");
				return;
			}

			if (vecStorniran)
				continue;

			rez = this.stornirajRacun(rvo);
			if (!rez)
				break;
			else
				storniranih++;
		}// for

		if (rez)
			GlavniFrame.info(rlen == 1 ? "Raèun uspješno storniran!" : ""
					+ storniranih + "/" + rlen + " raèuna stornirano!");

		this.osvjeziTablicuSaPodacima();
	}// stornirajOznaceneRacune

	private boolean stornirajRacun(RacunVO rvo) {
		RacunDAO rdao = this.getRacuniDAO();
		StavkaRacunaDAO stdao = this.getStavke();

		rvo.setStatus(DAO.STATUS_STORNIRAN_PS.charAt(0));
		rvo.setIznosSudjelovanja(0);

		List<StavkaRacunaVO> stavkeRacuna = null;

		try {
			stavkeRacuna = stdao.findAll(rvo);

			rdao.update(rvo);

			for (StavkaRacunaVO stavka : stavkeRacuna) {
				stavka.setKolicina(0);
				// stavka.setStatus(DAO.STATUS_STORNIRAN_PS.charAt(0));
				stdao.update(stavka);
			}

			return true;
		} catch (Exception e) {
			Logger.fatal(
					"Iznimka kod pokušaja storniranja raèuna:" + rvo.getId(), e);
			GlavniFrame.alert("Raèun br. " + rvo.getSifra()
					+ " NIJE uspješno storniran!");
			return false;
		}
	}

	RacunDAO racuni = null;

	private RacunDAO getRacuniDAO() {
		if (this.racuni == null)
			this.racuni = DAOFactory.getInstance().getRacuni();
		return this.racuni;
	}// getRacuni

	StavkaRacunaDAO stavke = null;

	private final StavkaRacunaDAO getStavke() {
		if (this.stavke == null)
			this.stavke = DAOFactory.getInstance().getStavkeRacuna();
		return this.stavke;
	}// getStavke

	private final boolean ispisUDefaultPrinter = PostavkeBean
			.isIspisUGlavniPrinter();

	private void ispisiOznaceneRacune() {
		int[] retci = this.podaci.getSelectedRows();

		int rlen = retci.length;
		if (rlen == 0) {
			GlavniFrame.alert("Niste oznaèili niti jedan raèun!");
			return;
		}

		PrinterJob pJob = PrinterJob.getPrinterJob();
		boolean printerOdabran = false;

		String printer = PostavkeBean.getPostavkaDB(GlavniFrame.ODABRANI_PRINTER, "");
		
		PrintService servis = null;
		
		if (!"".equals(printer) && printer!=null)
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
					Logger.warn("Nismo uspjeli postaviti printer za ispis (pr): "+printer, e);
				}
				break;
			 }
			}
		}
		
		
		if (!ispisUDefaultPrinter) {
			printerOdabran = pJob.printDialog();
		} else
			printerOdabran = true;

		if (printerOdabran)
			for (int i = 0; i < rlen; i++) {
				RacunVO rvo = (RacunVO) this.racuniModel.getData()
						.get(retci[i]);
				this.ispisiRacun(rvo, pJob);
			}

	}// ispisiOznaceneRacune

	private void ispisiRacun(RacunVO rvo, PrinterJob pJob) {
		if (rvo == null || pJob == null)
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
			ispisDop = null;
		}
	}// ispisiRacun

	private class SlusacMisa extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			// ako je desni klik, prikazi menu
			if (e.getButton() == MouseEvent.BUTTON3) {
				// Point p = e.getPoint();
				int redak = podaci.getSelectedRow();
				if (redak == -1) {
					redak = podaci.rowAtPoint(e.getPoint());
					podaci.setRowSelectionInterval(redak, redak);
				}

				popup.show(podaci, e.getX(), e.getY());
			}
		}
	}// klasa

	/**
	 * This is the default constructor
	 */
	public HzzoPostojeciRacuniPanel() {
		super();
		initialize();
		osvjeziTablicuSaPodacima();
		GlavniFrame.getInstanca().pack();

		popup = new JPopupMenu("opcije");
		JMenuItem jmIspisiRacune = new JMenuItem("Ispiši raèun(e)");
		JMenuItem jmStornirajRacune = new JMenuItem("Storniraj raèun(e)");

		jmIspisiRacune.addActionListener(al);
		jmStornirajRacune.addActionListener(al);

		popup.add(jmIspisiRacune);
		popup.add(jmStornirajRacune);

		this.podaci.addMouseListener(new SlusacMisa());
		
		Thread t = new Thread()
		{
			public void run()
			{
				podaci.packAll();
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
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		consGridBagConstraints15.gridy = 1;
		consGridBagConstraints15.gridx = 1;
		consGridBagConstraints15.gridwidth = 2;
		consGridBagConstraints15.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 1;
		consGridBagConstraints21.gridx = 3;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 8;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 7;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 6;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 4;
		consGridBagConstraints13.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints13.weightx = 1.0;
		consGridBagConstraints13.gridy = 0;
		consGridBagConstraints13.gridx = 8;
		consGridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints12.gridy = 0;
		consGridBagConstraints12.gridx = 7;
		consGridBagConstraints14.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints14.weighty = 1.0;
		consGridBagConstraints14.weightx = 1.0;
		consGridBagConstraints14.gridy = 2;
		consGridBagConstraints14.gridx = 0;
		consGridBagConstraints11.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints11.weightx = 1.0;
		consGridBagConstraints11.gridy = 0;
		consGridBagConstraints11.gridx = 6;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints10.gridy = 0;
		consGridBagConstraints10.gridx = 4;
		consGridBagConstraints8.gridy = 0;
		consGridBagConstraints8.gridx = 2;
		consGridBagConstraints9.gridy = 0;
		consGridBagConstraints9.gridx = 3;
		consGridBagConstraints9.ipady = 0;
		consGridBagConstraints9.insets = new java.awt.Insets(5, 0, 5, 0);
		consGridBagConstraints6.gridy = 0;
		consGridBagConstraints6.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints14.gridwidth = 9;
		consGridBagConstraints7.gridy = 0;
		consGridBagConstraints7.gridx = 1;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints6);
		this.add(getDatumOd(), consGridBagConstraints7);
		this.add(getJLabel1(), consGridBagConstraints8);
		this.add(getDatumDo(), consGridBagConstraints9);
		this.add(getJLabel2(), consGridBagConstraints10);
		this.add(getJtKlijent(), consGridBagConstraints11);
		this.add(getJLabel3(), consGridBagConstraints12);
		this.add(getJtPodrucniUred(), consGridBagConstraints13);
		this.add(getJspPodaci(), consGridBagConstraints14);
		this.add(getJLabel4(), consGridBagConstraints1);
		this.add(getJtLijecnik(), consGridBagConstraints2);
		this.add(getJLabel5(), consGridBagConstraints3);
		this.add(getJtArtikl(), consGridBagConstraints4);
		this.add(getJLabel6(), consGridBagConstraints15);
		this.add(getJtBrojOsobnogRacuna(), consGridBagConstraints21);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(790*faktor, 560*faktor);
		this.setLocation(3, 22);
		this.setPreferredSize(new java.awt.Dimension(790*faktor, 560*faktor));
		this.setMinimumSize(new java.awt.Dimension(790*faktor, 560*faktor));
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Datum od: ");
			jLabel.setPreferredSize(new java.awt.Dimension(60, 20));
			jLabel.setMinimumSize(new java.awt.Dimension(60, 20));
		}
		return jLabel;
	}

	/**
	 * This method initializes datumOd
	 * 
	 * @return javax.swing.JPanel
	 */
	private JDateChooser getDatumOd() {
		if (datumOd == null) {
			datumOd = new JDateChooser();
			datumOd.setDatum(Calendar.getInstance());
			Calendar c = datumOd.getDatum();

			if (c == null)
				c = Calendar.getInstance();
			datumOd.setDatum(c);

			boolean l = c.isLenient();
			c.setLenient(true);
			c.set(java.util.Calendar.MONTH, c.get(Calendar.MONTH) - 6);
			c.setLenient(l);
			datumOd.setDatum(c);
			datumOd.setPreferredSize(new java.awt.Dimension(135, 20));
			datumOd.setMinimumSize(new java.awt.Dimension(135, 20));
			datumOd.setToolTipText("datum od kada želite raèune na popisu");

			if (oznaceniDatumOd != null)
				datumOd.setDatum(oznaceniDatumOd);

			datumOd.dodajSlusaca(this);
		}
		return datumOd;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Datum do: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes datumDo
	 * 
	 * @return javax.swing.JPanel
	 */
	private JDateChooser getDatumDo() {
		if (datumDo == null) {
			datumDo = new JDateChooser();

			datumDo.setPreferredSize(new java.awt.Dimension(135, 20));
			datumDo.setMinimumSize(new java.awt.Dimension(135, 20));
			datumDo.setToolTipText("datum do kojeg želite vidjeti raèune na popisu");

			Calendar c = Calendar.getInstance();
			datumDo.setDatum(c);

			if (oznaceniDatumDo != null)
				datumDo.setDatum(oznaceniDatumDo);
			datumDo.dodajSlusaca(this);
		}
		return datumDo;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Korisnik: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jtKlijent
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtKlijent() {
		if (jtKlijent == null) {
			jtKlijent = new javax.swing.JTextField();
			jtKlijent.setPreferredSize(new java.awt.Dimension(120, 20));
			jtKlijent.setMinimumSize(new java.awt.Dimension(120, 20));
			jtKlijent
					.setToolTipText("ako želite pronaæi raèun za nekog korisnika, upišite tu njegovo ime i odaberite korisnika sa popisa koji æe se pojaviti");

			if (oznaceniKlijent != null)
				jtKlijent.setText(oznaceniKlijent.getIme() + " "
						+ oznaceniKlijent.getPrezime());

			jtKlijent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jtKlijent.getText().trim().equals("")) {
						oznaceniKlijent = null;
						osvjeziTablicuSaPodacima();
					}
				}
			});
			jtKlijent.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtKlijent.getText().trim().equals("")) {
						oznaceniKlijent = null;
						osvjeziTablicuSaPodacima();
					}
				}

				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtKlijent.selectAll();
				}
			});
			this.pretrazivanjeKlijenti = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getKlijenti(), 10, 20, 140, 120, jtKlijent);
			SearchCriteria sce = new SearchCriteria();
			sce.setKriterij(DAO.KRITERIJ_KLIJENT_LIMIT_1000);
			this.pretrazivanjeKlijenti.setKriterij(sce);
			this.pretrazivanjeKlijenti.dodajSlusaca(this);
			this.pretrazivanjeKlijenti.setMinimumZnakovaZaPretrazivanje(2);
			this.pretrazivanjeKlijenti.setMaksimumZaPretrazivanje(11);
		}

		return jtKlijent;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Podruèni ured: ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jtPodrucniUred
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPodrucniUred() {
		if (jtPodrucniUred == null) {
			jtPodrucniUred = new javax.swing.JTextField();
			jtPodrucniUred.setPreferredSize(new java.awt.Dimension(100, 20));
			jtPodrucniUred.setMinimumSize(new java.awt.Dimension(100, 20));
			jtPodrucniUred
					.setToolTipText("popis raèuna za odreðenu poslovnicu hzzo-a");

			if (oznacenoMjesto != null)
				jtPodrucniUred.setText(oznacenoMjesto.getNaziv());

			jtPodrucniUred
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (jtPodrucniUred.getText().trim().equals("")) {
								oznacenoMjesto = null;
								osvjeziTablicuSaPodacima();
							}
						}
					});
			jtPodrucniUred.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtPodrucniUred.getText().trim().equals("")) {
						oznacenoMjesto = null;
						osvjeziTablicuSaPodacima();
					}
				}

				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtPodrucniUred.selectAll();
				}
			});

			this.pretrazivanjePodruznice = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getMjesta(), 10, 20, 120, 60, jtPodrucniUred);
			SearchCriteria kr = new SearchCriteria();
			kr.setKriterij(MjestoVO.KRITERIJ_PRETRAZIVANJA_PODRUDRUZNICE);
			// filter ce ugradjivati upit u kriterij prije zvanja findAll
			// metode...
			this.pretrazivanjePodruznice.setKriterij(kr);
			this.pretrazivanjePodruznice.dodajSlusaca(this);
		}
		return jtPodrucniUred;
	}

	/**
	 * This method initializes podaci
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getPodaci() {
		if (podaci == null) {
			podaci = new JXTable();
			this.racuniModel = new TableModel<RacunVO>(DAOFactory.getInstance()
					.getRacuni(), podaci);
			// koristit ce se kasnije pri osvjezavanju tablice
			kriterij = new SearchCriteria();
			kriterij.setKriterij(RacunDAO.KRITERIJ_RACUNI_PO_VISE_KRITERIJA);
			this.racuniModel.dodajSlusaca(this);

			this.podaci.setModel(this.racuniModel);
		}
		return podaci;
	}

	/**
	 * This method initializes jspPodaci
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspPodaci() {
		if (jspPodaci == null) {
			jspPodaci = new javax.swing.JScrollPane();
			jspPodaci.setViewportView(getPodaci());
			jspPodaci.setPreferredSize(new java.awt.Dimension(790, 560));
		}
		return jspPodaci;
	}

	private void osvjeziTablicuSaPodacima() {
		Thread t = new Thread() {

			public void run() {
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					// provjera
					if (jtPodrucniUred.getText().trim().equals(""))
						oznacenoMjesto = null;
					if (jtLijecnik.getText().trim().equals(""))
						oznaceniLijecnik = null;
					if (jtKlijent.getText().trim().equals(""))
						oznaceniKlijent = null;

					ArrayList<Object> l = new ArrayList<Object>(4);
					l.add(datumOd.getDatum());
					l.add(datumDo.getDatum());
					l.add(oznaceniKlijent);
					l.add(oznacenoMjesto);
					// 22.04.06. -asabo- dodano
					l.add(oznaceniLijecnik);

					if (oznacenoPomagalo != null)
						l.add(oznacenoPomagalo);
					else
						l.add(jtArtikl.getText().trim().toUpperCase());

					l.add(brojOsobnogRacuna);

					if (racuniModel.getData() != null)
						racuniModel.getData().clear();

					kriterij.setPodaci(l);
					racuniModel.setFilter(kriterij);
					podaci.packAll();

					strPomagalo = jtArtikl.getText().trim();
				} finally {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		};
		SwingUtilities.invokeLater(t);
	}// osvjeziTablicuSaPodacima

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		if (redak >= 0 && event.getButton() != MouseEvent.BUTTON3
				&& event.getClickCount() == 2) {
			Object o = posiljatelj.getData().get(redak);

			if (this.racuniModel.getData() != null)
				this.racuniModel.clearData(); // prvo brisanje starih podataka

			GlavniFrame gf = (GlavniFrame) GlavniFrame.getInstanca();
			gf.kreirajHzzoRacun((RacunVO) o);
			System.gc();
		}
		// dvostruki desni klik brise klijenta, naravno pod uvjetom da klijent
		// kaze ok to hocu
		else if (redak >= 0
				&& event.getButton() == MouseEvent.BUTTON3
				&& event.getClickCount() == 2
				&& DaNeUpit.upit("Jeste sigurni da želite pobrisati raèun?",
						"Brisati raèun?", GlavniFrame.getInstanca())) {
			RacunVO rvo = (RacunVO) this.racuniModel.getData().get(redak);
			pobrisiRacun(rvo);
		}// else

	}// redakOznacen

	// ne birse ga fizicki, samo mu postavlja status na 'D'
	private void pobrisiRacun(RacunVO racun) {
		if (racun == null)
			return;

		try {
			// racun.setStatus('X'); // ides van fizicki..
			DAOFactory.getInstance().getRacuni().delete(racun);
			osvjeziTablicuSaPodacima();
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod brisanja raèuna u HzzoPostojeciRacuni", e);
			JOptionPane
					.showMessageDialog(
							GlavniFrame.getInstanca(),
							"Ne možemo pobrisati raèun?!? Kontaktirajte administratora sustava!",
							"Upozorenje!", JOptionPane.WARNING_MESSAGE);
		}
	}// brisiRacun

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {
	}

	public void labelaOznacena(Labela labela) {
		Object izv = labela.getIzvornik();
		if (izv == null)
			return;
		if (izv instanceof KlijentVO) {
			oznaceniKlijent = (KlijentVO) izv;
			this.jtKlijent.setText(oznaceniKlijent.getIme() + " "
					+ oznaceniKlijent.getPrezime());
		} else if (izv instanceof MjestoVO) {
			oznacenoMjesto = (MjestoVO) izv;
			this.jtPodrucniUred.setText(oznacenoMjesto.getNaziv());
		} else if (izv instanceof LijecnikVO) {
			oznaceniLijecnik = (LijecnikVO) izv;
			jtLijecnik.setText(oznaceniLijecnik.toString());
		} else if (izv instanceof PomagaloVO) {
			oznacenoPomagalo = (PomagaloVO) izv;
			this.jtArtikl.setText(oznacenoPomagalo.getNaziv());
		}

		osvjeziTablicuSaPodacima();
	}// labelaOznacena

	 
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Lijeènik: ");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jtLijecnik
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtLijecnik() {
		if (jtLijecnik == null) {
			jtLijecnik = new javax.swing.JTextField();
			jtLijecnik.setPreferredSize(new java.awt.Dimension(120, 20));
			jtLijecnik.setMinimumSize(new java.awt.Dimension(120, 20));
			jtLijecnik
					.setToolTipText("svi raèuni izdani od strane odreðenog lijeènika");

			jtLijecnik.setText(oznaceniLijecnik != null ? oznaceniLijecnik
					.getIme() + " " + oznaceniLijecnik.getPrezime() : "");

			this.pretrazivanjeLijecnici = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getLijecnici(), 10, 20, 120, 80, jtLijecnik);

			this.pretrazivanjeLijecnici.dodajSlusaca(this);

			jtLijecnik.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtLijecnik.getText().trim().equals(""))
						oznaceniLijecnik = null;
				}

				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtLijecnik.selectAll();
				}
			});
		}
		return jtLijecnik;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Artikl:");
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
			jtArtikl.setToolTipText("svi raèuni koji u sebi sadržavaju odreðeni artikl");

			if (oznacenoPomagalo != null)
				jtArtikl.setText(oznacenoPomagalo.getNaziv());
			else if (strPomagalo != null)
				jtArtikl.setText(strPomagalo.toUpperCase());

			this.pretrazivanjePomagala = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), DAOFactory.getInstance()
							.getPomagala(), 10, 20, 150, 100, jtArtikl);

			if (GlavniFrame.isKoristiSvaPomagala()) {
				SearchCriteria scr = new SearchCriteria();
				scr.setKriterij(PomagaloDAO.KRITERIJ_KORISTIMO_SVA_POMAGALA);
				this.pretrazivanjePomagala.setKriterij(scr);
			}

			this.pretrazivanjePomagala.dodajSlusaca(this);
			this.pretrazivanjePomagala.setMinimumZnakovaZaPretrazivanje(2);
			this.pretrazivanjePomagala.setMaksimumZaPretrazivanje(11);

			jtArtikl.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtArtikl.getText().trim().equals(""))
						oznacenoPomagalo = null;
					osvjeziTablicuSaPodacima();
				}

				@Override
				public void focusGained(java.awt.event.FocusEvent e) {
					jtArtikl.selectAll();
				}
			});
		}
		return jtArtikl;
	}

	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Broj osobnog raèuna: ");
		}
		return jLabel6;
	}

	/**
	 * This method initializes jtBrojOsobnogRacuna
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojOsobnogRacuna() {
		if (jtBrojOsobnogRacuna == null) {
			jtBrojOsobnogRacuna = new javax.swing.JTextField();

			if (brojOsobnogRacuna != null)
				jtBrojOsobnogRacuna.setText(brojOsobnogRacuna);

			jtBrojOsobnogRacuna
					.addFocusListener(new java.awt.event.FocusAdapter() {
						@Override
						public void focusLost(java.awt.event.FocusEvent e) {

							brojOsobnogRacuna = jtBrojOsobnogRacuna.getText()
									.trim();
							osvjeziTablicuSaPodacima();
						}

						@Override
						public void focusGained(java.awt.event.FocusEvent e) {
							jtBrojOsobnogRacuna.selectAll();
						}
					});
		}
		return jtBrojOsobnogRacuna;
	}

	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		oznaceniDatumOd = this.datumOd.getDatum();
		oznaceniDatumDo = this.datumDo.getDatum();
		osvjeziTablicuSaPodacima();
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
