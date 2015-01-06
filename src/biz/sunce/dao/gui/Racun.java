package biz.sunce.dao.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DrzavaDAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.LijecnikDAO;
import biz.sunce.dao.MjestoDAO;
import biz.sunce.dao.ProizvodjaciDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DrzavaVO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.VrstaPomagalaVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.optika.azurirac.AzuriracPomagala;
import biz.sunce.optika.net.HZZOFetchUtil;
import biz.sunce.optika.net.VrstaUpita;
import biz.sunce.toedter.calendar.JDateChooser;
import biz.sunce.util.GUI;
import biz.sunce.util.KontrolneZnamenkeUtils;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;
import biz.sunce.util.StringUtils;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

import com.modp.checkdigits.CheckDigit;
import com.modp.checkdigits.CheckISO7064Mod11_10;
import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.SlusacDateChoosera;

/**
 * datum:2006.03.01
 * 
 * @author asabo
 * 
 */
@SuppressWarnings("serial")
public final class Racun extends JPanel implements GUIEditor<RacunVO>,
		SlusacOznaceneLabelePretrazivanja, SlusacDateChoosera 
		{


	private javax.swing.JLabel jLabel = null;
	private javax.swing.JToggleButton jtbOsnovno = null;
	private javax.swing.JToggleButton jtbDopunsko = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtKlijent = null;
	private javax.swing.JLabel jLabel2 = null;
	private JDateChooser datumNarudzbe = null;
	private javax.swing.JLabel jLabel3 = null;
	private JDateChooser datumIsporuke = null;
	private javax.swing.JPanel jpnTipOsiguranja = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtSifraProizvodjaca = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtBrojPotvrde1 = null;
	private javax.swing.JTextField jtBrojPotvrde2 = null;
	private javax.swing.JLabel jlBrojDopunskogOsig = null;
	private javax.swing.JTextField jtBrojPoliceDopunsko = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JTextField jtIznosSudjelovanja = null;
	
	Font arial = new Font("Arial", Font.PLAIN, 11);
	Font arial12 = new Font("Arial", Font.PLAIN, 12);
	
	RacunVO oznaceni = null;
	KlijentVO oznaceniKlijent = null;
	MjestoVO oznacenaPodruznica = null;
	DrzavaVO oznacenaDrzava = null;
	LijecnikVO oznaceniLijecnik = null;
	ProizvodjacVO oznaceniProizvodjac = null;

	PretrazivanjeProzor pretrazivanjeKlijenti = null,
			pretrazivanjePodruznica = null, pretrazivanjeDrzave = null,
			pretrazivanjeLijecnici = null;
	private JTextField jtSifraAktivnostiDop;
	private JLabel jlSifraDopunskeAkt;
	private JTextField jtBrojPotvrdeLijecnik;
	private JLabel jlBrojPotvrde;
	private JTextField jtSifraAktivnosti;
	private JLabel jlSifraAktivnost;
	PretrazivanjeProzor pretrazivanjeProizvodjaci = null;

	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField jtPodruznica = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JPanel jpPozivNaBroj = null;
	private javax.swing.JTextField jtPozivNaBroj1 = null;
	private javax.swing.JTextField jtPozivNaBroj2 = null;
	private javax.swing.JLabel jLabel10 = null;
	private javax.swing.JTextField jtNapomena = null;
	private javax.swing.JLabel jLabel11 = null;
	private javax.swing.JTextField jtAdresaPodruznice = null;
	private javax.swing.JLabel jLabel12 = null;
	private javax.swing.JTextField jtBrojIskaznice1 = null;
	private javax.swing.JTextField jtBrojIskaznice2 = null;
	private javax.swing.JCheckBox jcIno = null;
	private javax.swing.JLabel jlBrojBolesnickogLista = null;
	private javax.swing.JTextField jtBrojInoLista1 = null;
	private javax.swing.JTextField jtBrojInoLista2 = null;
	private javax.swing.JLabel jlDrzavaKlijenta = null;
	private javax.swing.JTextField jtDrzavaInoOsobe = null;
	private javax.swing.JLabel jLabel15 = null;
	private javax.swing.JTextField jtLijecnik = null;
	private javax.swing.JLabel jlBrojOsoRacZaOsnOsig = null;
	private javax.swing.JTextField jtBrojOsobnogRacunaOsnovno = null;
	private javax.swing.JLabel jlBrojOsobRacZaDopOsig = null;
	private javax.swing.JTextField jtBrojOsobnogRacunaDopunsko = null;
	private javax.swing.JCheckBox jcStrankaUzelaSkupljiModel = null;
	private javax.swing.JLabel jLabel18 = null;
	private javax.swing.JComboBox jcVrstaPomagala = null;
	private javax.swing.JCheckBox jcRobaIsporucena = null;

	private JPanel jpBrojBolesnickogL;
	private JPanel jpBrojPotvrdeHzzo;
	private JPanel jpBrojOsiguranjaKlijenta;

	/**
	 * This is the default constructor
	 */
	public Racun() {
		super();
		initialize();
		this.getJtBrojPoliceDopunsko().setEnabled(
				this.getJtbDopunsko().isSelected());
		this.postaviOsnovnoOsiguranje(true);
		iskljuciIno();

		this.getJtSifraProizvodjaca().setEnabled(false);
		this.jtIznosSudjelovanja
				.setNextFocusableComponent(this.jcRobaIsporucena);
		this.getJtIznosSudjelovanja().setNextFocusableComponent(
				this.getJtBrojOsobnogRacunaOsnovno());
		this.getJtBrojOsobnogRacunaOsnovno().setNextFocusableComponent(
				this.getJcRobaIsporucena());

		Thread t = new Thread() {
			public void run() {
				this.setPriority(Thread.MIN_PRIORITY);
				yield();
				try {					
					sleep(500);		
					System.gc(); // prilikom kreiranja nove forme racuna dobro bi bilo
					// probati otpustiti resurse, jer to je moment kad korisnik unosi podatke na formu, dakle 
					//procesorski i memorijski nezahtjevna faza gdje se ne moze primjetiti eventualni GC
				} catch (InterruptedException inte) {
					return;
				}
				yield();
				jtBrojOsobnogRacunaOsnovno.requestFocusInWindow();
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
		consGridBagConstraints1.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		consGridBagConstraints4.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		consGridBagConstraints5.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		consGridBagConstraints6.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		consGridBagConstraints14.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		consGridBagConstraints13.gridwidth = 2;
		java.awt.GridBagConstraints consGridBagConstraints18 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints19 = new java.awt.GridBagConstraints();
		consGridBagConstraints19.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints20 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints gbc_jlBrojDopunskogOsig = new java.awt.GridBagConstraints();
		gbc_jlBrojDopunskogOsig.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		consGridBagConstraints11.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
		consGridBagConstraints41.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
		consGridBagConstraints51.anchor = GridBagConstraints.WEST;
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		consGridBagConstraints2.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints110 = new java.awt.GridBagConstraints();
		consGridBagConstraints110.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints111 = new java.awt.GridBagConstraints();
		consGridBagConstraints111.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
		consGridBagConstraints22.insets = new Insets(0, 0, 1, 0);
		java.awt.GridBagConstraints consGridBagConstraints112 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints gbc_jlBrojBolesnickogLista = new java.awt.GridBagConstraints();
		gbc_jlBrojBolesnickogLista.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints42 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints gbc_jlDrzavaKlijenta = new java.awt.GridBagConstraints();
		gbc_jlDrzavaKlijenta.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints114 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints121 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints131 = new java.awt.GridBagConstraints();
		consGridBagConstraints131.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints43 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints141 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints gbc_jlBrojOsoRacZaOsnOsig = new java.awt.GridBagConstraints();
		gbc_jlBrojOsoRacZaOsnOsig.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints52 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints116 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints gbc_jlBrojOsobRacZaDopOsig = new java.awt.GridBagConstraints();
		gbc_jlBrojOsobRacZaDopOsig.insets = new Insets(0, 0, 2, 0);
		java.awt.GridBagConstraints consGridBagConstraints117 = new java.awt.GridBagConstraints();
		consGridBagConstraints117.gridy = 12;
		consGridBagConstraints117.gridx = 5;
		consGridBagConstraints117.gridwidth = 2;
		consGridBagConstraints116.gridy = 11;
		consGridBagConstraints116.gridx = 0;
		consGridBagConstraints116.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints24.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints24.weightx = 1.0;
		consGridBagConstraints24.gridy = 11;
		consGridBagConstraints24.gridx = 1;
		consGridBagConstraints24.gridwidth = 2;
		consGridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints24.insets = new Insets(0, 2, 1, 0);
		consGridBagConstraints52.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints52.weightx = 1.0;
		consGridBagConstraints52.gridy = 10;
		consGridBagConstraints52.gridx = 5;
		consGridBagConstraints52.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints52.insets = new Insets(0, 2, 2, 0);
		gbc_jlBrojOsobRacZaDopOsig.gridy = 10;
		gbc_jlBrojOsobRacZaDopOsig.gridx = 3;
		gbc_jlBrojOsobRacZaDopOsig.gridwidth = 2;
		gbc_jlBrojOsobRacZaDopOsig.anchor = java.awt.GridBagConstraints.EAST;
		gbc_jlBrojOsoRacZaOsnOsig.gridy = 10;
		gbc_jlBrojOsoRacZaOsnOsig.gridx = 0;
		consGridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints31.weightx = 1.0;
		consGridBagConstraints31.gridy = 10;
		consGridBagConstraints31.gridx = 1;
		consGridBagConstraints31.gridwidth = 2;
		consGridBagConstraints31.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints141.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints141.weightx = 1.0;
		consGridBagConstraints141.gridy = 5;
		consGridBagConstraints141.gridx = 1;
		consGridBagConstraints141.gridwidth = 2;
		consGridBagConstraints131.gridy = 5;
		consGridBagConstraints131.gridx = 0;
		consGridBagConstraints131.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints114.insets = new java.awt.Insets(0, 5, 0, 0);
		consGridBagConstraints114.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints114.weightx = 1.0;
		consGridBagConstraints114.gridy = 4;
		consGridBagConstraints114.gridx = 1;
		consGridBagConstraints114.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints121.insets = new java.awt.Insets(0, 0, 0, 0);
		consGridBagConstraints121.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints121.weightx = 1.0;
		consGridBagConstraints121.gridy = 4;
		consGridBagConstraints121.gridx = 2;
		consGridBagConstraints121.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints43.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints43.weightx = 1.0;
		consGridBagConstraints43.gridy = 5;
		consGridBagConstraints43.gridx = 5;
		gbc_jlDrzavaKlijenta.gridy = 5;
		gbc_jlDrzavaKlijenta.gridx = 4;
		gbc_jlDrzavaKlijenta.anchor = java.awt.GridBagConstraints.EAST;
		gbc_jlBrojBolesnickogLista.gridy = 4;
		gbc_jlBrojBolesnickogLista.gridx = 4;
		consGridBagConstraints42.gridy = 4;
		consGridBagConstraints42.gridx = 3;
		consGridBagConstraints112.gridy = 4;
		consGridBagConstraints112.gridx = 0;
		consGridBagConstraints112.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints111.gridy = 2;
		consGridBagConstraints111.gridx = 4;
		consGridBagConstraints111.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints22.weightx = 1.0;
		consGridBagConstraints22.gridy = 2;
		consGridBagConstraints22.gridx = 5;
		consGridBagConstraints110.gridy = 9;
		consGridBagConstraints110.gridx = 0;
		consGridBagConstraints110.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 9;
		consGridBagConstraints21.gridx = 1;
		consGridBagConstraints21.gridwidth = 5;
		consGridBagConstraints41.gridy = 3;
		consGridBagConstraints41.gridx = 4;
		consGridBagConstraints41.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints51.gridy = 3;
		consGridBagConstraints51.gridx = 5;
		consGridBagConstraints51.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints11.gridy = 2;
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 2;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints19.gridy = 8;
		consGridBagConstraints19.gridx = 4;
		consGridBagConstraints20.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints20.weightx = 1.0;
		consGridBagConstraints20.gridy = 8;
		consGridBagConstraints20.gridx = 5;
		consGridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints18.weightx = 1.0;
		consGridBagConstraints18.gridy = 8;
		consGridBagConstraints18.gridx = 1;
		gbc_jlBrojDopunskogOsig.gridy = 8;
		gbc_jlBrojDopunskogOsig.gridx = 0;
		gbc_jlBrojDopunskogOsig.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints14.gridy = 7;
		consGridBagConstraints14.gridx = 4;
		consGridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints13.weightx = 1.0;
		consGridBagConstraints13.gridy = 7;
		consGridBagConstraints13.gridx = 1;
		consGridBagConstraints12.gridy = 7;
		consGridBagConstraints12.gridx = 0;
		consGridBagConstraints10.gridy = 1;
		consGridBagConstraints10.gridx = 1;
		consGridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints10.gridwidth = 3;
		consGridBagConstraints6.gridy = 6;
		consGridBagConstraints6.gridx = 0;
		consGridBagConstraints7.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints7.weightx = 1.0;
		consGridBagConstraints7.gridy = 6;
		consGridBagConstraints7.gridx = 1;
		consGridBagConstraints9.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints9.weightx = 1.0;
		consGridBagConstraints9.gridy = 6;
		consGridBagConstraints9.gridx = 5;
		consGridBagConstraints8.gridy = 6;
		consGridBagConstraints8.gridx = 4;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
		consGridBagConstraints5.weightx = 1.0;
		consGridBagConstraints5.gridy = 3;
		consGridBagConstraints5.gridx = 1;
		consGridBagConstraints5.gridwidth = 3;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints4.gridy = 3;
		consGridBagConstraints4.gridx = 0;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints8.insets = new Insets(0, 1, 2, 0);
		consGridBagConstraints18.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints7.ipadx = 0;
		consGridBagConstraints7.ipady = 0;
		consGridBagConstraints7.insets = new Insets(0, 2, 2, 0);

		consGridBagConstraints13.insets = new Insets(0, 2, 0, 0);
		consGridBagConstraints6.anchor = java.awt.GridBagConstraints.SOUTHEAST;
		consGridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints51.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints7.anchor = java.awt.GridBagConstraints.SOUTHWEST;
		consGridBagConstraints21.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints19.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints9.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints20.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints43.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints43.insets = new Insets(1, 2, 2, 0);
		consGridBagConstraints43.ipady = -5;
		gbc_jlBrojBolesnickogLista.anchor = java.awt.GridBagConstraints.EAST;
		gbc_jlBrojBolesnickogLista.ipady = 0;
		consGridBagConstraints112.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints18.gridwidth = 2;
		consGridBagConstraints42.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints42.insets = new java.awt.Insets(0, 0, 0, 0);
		consGridBagConstraints2.gridwidth = 2;
		gbc_jlDrzavaKlijenta.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints141.insets = new Insets(0, 2, 2, 0);
		consGridBagConstraints141.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
		gbc_jlBrojBolesnickogLista.fill = java.awt.GridBagConstraints.HORIZONTAL;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 8};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0};
		this.setLayout(gridBagLayout);
		java.awt.GridBagConstraints consGridBagConstraints115 = new java.awt.GridBagConstraints();
		consGridBagConstraints115.insets = new Insets(0, 0, 2, 0);
		consGridBagConstraints115.gridy = 0;
		consGridBagConstraints115.gridx = 0;
		consGridBagConstraints115.gridwidth = 3;
		consGridBagConstraints115.anchor = GridBagConstraints.WEST;
		this.add(getJcStrankaUzelaSkupljiModel(), consGridBagConstraints115);
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJLabel1(), consGridBagConstraints4);
		this.add(getJtKlijent(), consGridBagConstraints5);
		GridBagConstraints gbc_jpBrojOsiguranjaKlijenta = new GridBagConstraints();
		gbc_jpBrojOsiguranjaKlijenta.insets = new Insets(0, 0, 2, 0);
		gbc_jpBrojOsiguranjaKlijenta.gridwidth = 2;
		gbc_jpBrojOsiguranjaKlijenta.fill = GridBagConstraints.HORIZONTAL;
		gbc_jpBrojOsiguranjaKlijenta.gridx = 1;
		gbc_jpBrojOsiguranjaKlijenta.gridy = 4;
		add(getJpBrojOsiguranjaKlijenta(), gbc_jpBrojOsiguranjaKlijenta);
		GridBagConstraints gbc_jpBrojBolesnickogL = new GridBagConstraints();
		gbc_jpBrojBolesnickogL.insets = new Insets(0, 2, 2, 0);
		gbc_jpBrojBolesnickogL.anchor = GridBagConstraints.WEST;
		gbc_jpBrojBolesnickogL.gridx = 5;
		gbc_jpBrojBolesnickogL.gridy = 4;
		add(getJpBrojBolesnickogL(), gbc_jpBrojBolesnickogL);
		this.add(getJLabel2(), consGridBagConstraints6);
		this.add(getDatumNarudzbe(), consGridBagConstraints7);
		this.add(getJLabel3(), consGridBagConstraints8);
		this.add(getDatumIsporuke(), consGridBagConstraints9);
		this.add(getJpnTipOsiguranja(), new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJLabel4(), consGridBagConstraints12);
		this.add(getJtSifraProizvodjaca(), consGridBagConstraints13);
		this.add(getJLabel5(), consGridBagConstraints14);
		GridBagConstraints gbc_jpBrojPotvrdeHzzo = new GridBagConstraints();
		gbc_jpBrojPotvrdeHzzo.insets = new Insets(0, 0, 2, 0);
		gbc_jpBrojPotvrdeHzzo.fill = GridBagConstraints.HORIZONTAL;
		gbc_jpBrojPotvrdeHzzo.gridx = 5;
		gbc_jpBrojPotvrdeHzzo.gridy = 7;
		add(getJpBrojPotvrdeHzzo(), gbc_jpBrojPotvrdeHzzo);
		this.add(getJlBrojDopunskogOsig(), gbc_jlBrojDopunskogOsig);
		this.add(getJtBrojPoliceDopunsko(), consGridBagConstraints18);
		this.add(getJLabel7(), consGridBagConstraints19);
		this.add(getJtIznosSudjelovanja(), consGridBagConstraints20);
		this.add(getJLabel8(), consGridBagConstraints11);
		this.add(getJtPodruznica(), consGridBagConstraints2);
		this.add(getJLabel9(), consGridBagConstraints41);
		this.add(getJpPozivNaBroj(), consGridBagConstraints51);
		this.add(getJLabel10(), consGridBagConstraints110);
		this.add(getJtNapomena(), consGridBagConstraints21);
		this.add(getJLabel11(), consGridBagConstraints111);
		this.add(getJtAdresaPodruznice(), consGridBagConstraints22);
		this.add(getJLabel12(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 0), 0, 0));
		this.add(getJcIno(), new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(getJlBrojBolesnickogLista(), gbc_jlBrojBolesnickogLista);
		this.add(getJlDrzavaKlijenta(), gbc_jlDrzavaKlijenta);
		this.add(getJtDrzavaInoOsobe(), consGridBagConstraints43);
		this.add(getJLabel15(), consGridBagConstraints131);
		this.add(getJtLijecnik(), consGridBagConstraints141);
		this.add(getJlBrojOsoRacZaOsnOsig(), gbc_jlBrojOsoRacZaOsnOsig);
		this.add(getJtBrojOsobnogRacunaOsnovno(), consGridBagConstraints31);
		this.add(getJlBrojOsobRacZaDopOsig(), gbc_jlBrojOsobRacZaDopOsig);
		this.add(getJtBrojOsobnogRacunaDopunsko(), consGridBagConstraints52);
		this.add(getJLabel18(), consGridBagConstraints116);
		this.add(getJcVrstaPomagala(), consGridBagConstraints24);
		this.add(getJcRobaIsporucena(), new GridBagConstraints(3, 11, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 1, 0), 0, 0));
		this.add(getJlSifraAktivnost(), new GridBagConstraints(4, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 2, 0), 0, 0));
		this.add(getJtSifraAktivnosti(), new GridBagConstraints(5, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJlBrojPotvrde(), new GridBagConstraints(4, 11, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJtBrojPotvrdeLijecnik(), new GridBagConstraints(5, 11, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
		this.add(getJlSifraDopunskeAkt(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(getJtSifraAktivnostiDop(), new GridBagConstraints(5, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
 
		this.setPreferredSize(new Dimension(795, 400));
		this.setMinimumSize(new Dimension(755, 408));
		//this.setMaximumSize(new java.awt.Dimension(sir, duz));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(
				new javax.swing.border.SoftBevelBorder(
						javax.swing.border.SoftBevelBorder.RAISED),
				"podaci o raèunu",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.setToolTipText("podaci o raèunu");
		this.setEnabled(false);
		
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("tip osiguranja: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes jtbOsnovno
	 * 
	 * @return javax.swing.JToggleButton
	 */
	private javax.swing.JToggleButton getJtbOsnovno() {
		if (jtbOsnovno == null) {
			jtbOsnovno = new javax.swing.JToggleButton();
			jtbOsnovno.setText("Osnovno");
			jtbOsnovno.setSelected(true);
			jtbOsnovno.setMnemonic(java.awt.event.KeyEvent.VK_O);
			jtbOsnovno.setFont(arial12);
			jtbOsnovno
					.setToolTipText("kliknite ili pritisnite ALT-O ako radite raèun sa osnovnim osiguranjem");
			jtbOsnovno.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jtbOsnovno.isSelected()) {
						jtbDopunsko.setSelected(false);
						postaviOsnovnoOsiguranje(true);
					} else {
						jtbDopunsko.setSelected(true);
						postaviOsnovnoOsiguranje(false);
					}
				}
			});
		}
		return jtbOsnovno;
	}

	/**
	 * This method initializes jtbDopunsko
	 * 
	 * @return javax.swing.JToggleButton
	 */
	private javax.swing.JToggleButton getJtbDopunsko() {
		if (jtbDopunsko == null) {
			jtbDopunsko = new javax.swing.JToggleButton();
			jtbDopunsko.setText("Dopunsko");
			jtbDopunsko.setMnemonic(java.awt.event.KeyEvent.VK_D);
			jtbDopunsko.setFont(arial12);
			jtbDopunsko
					.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			jtbDopunsko
					.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jtbDopunsko.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
			jtbDopunsko
					.setToolTipText("kliknite ili pritisnite ALT-D ako radite raèun sa dopunskim osiguranjem");
			jtbDopunsko.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jtbDopunsko.isSelected()) {
						jtbOsnovno.setSelected(false);
						postaviOsnovnoOsiguranje(false);
					} else {
						jtbOsnovno.setSelected(true);
						postaviOsnovnoOsiguranje(true);
					}
				}// actionPerformed
			});
		}
		return jtbDopunsko;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("klijent: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtKlijent
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtKlijent() {
		if (jtKlijent == null) {
			jtKlijent = new javax.swing.JTextField();
			jtKlijent.setHorizontalAlignment(SwingConstants.LEFT);
			jtKlijent.setPreferredSize(new Dimension(160, 25));
			jtKlijent.setMinimumSize(new Dimension(160, 23));
			jtKlijent
					.setToolTipText("obavezan podatak, ime Vašeg klijenta za kojeg izraðujete raèun, ako Vam se ne pokaže na popisu, vodite raèuna da ste prvo unijeli ime, pa prezime");
			jtKlijent.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtBrojIskaznice2.getText().trim().equals(""))
						jtBrojIskaznice1.requestFocusInWindow();
				}

				public void focusGained(java.awt.event.FocusEvent e) {
					jtKlijent.selectAll();
				}
			});
			jtKlijent.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (jtKlijent.getText().trim().equals(""))
						oznaceniKlijent = null;

					if (oznaceniKlijent != null
							&& !jtKlijent.getText().equals(
									oznaceniKlijent.getIme() + " "
											+ oznaceniKlijent.getPrezime()))
						oznaceniKlijent = null;
				}
			});
			jtKlijent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jtKlijent.getText().trim().equals(""))
						oznaceniKlijent = null;

				}
			});
			KlijentDAO klijenti = GlavniFrame.running? DAOFactory.getInstance()
					.getKlijenti():null;
			this.pretrazivanjeKlijenti = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), klijenti, 10, 20, 170, 100,
					(Component) jtKlijent);
			this.pretrazivanjeKlijenti.dodajSlusaca(this);
			this.pretrazivanjeKlijenti.setMaksimumZaPretrazivanje(11);
			this.pretrazivanjeKlijenti.setMinimumZnakovaZaPretrazivanje(3);
			SearchCriteria krit = new SearchCriteria();
			krit.setKriterij(biz.sunce.dao.DAO.KRITERIJ_KLIJENT_LIMIT_1000);
			this.pretrazivanjeKlijenti.setKriterij(krit);
		}
		
		return jtKlijent;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Datum narudžbe: ");
		}
		
		return jLabel2;
	}

	/**
	 * This method initializes datumNarudzbe
	 * 
	 * @return javax.swing.JTextField
	 */
	private JDateChooser getDatumNarudzbe() {
		if (datumNarudzbe == null) {
			datumNarudzbe = new JDateChooser();
			datumNarudzbe.setDateFormatString("dd.MM.yyyy");
			datumNarudzbe.setDatum(Calendar.getInstance());
			datumNarudzbe.setPreferredSize(new java.awt.Dimension(125, 20));
			datumNarudzbe.setMinimumSize(new java.awt.Dimension(125, 20));
			datumNarudzbe
					.setToolTipText("datum naruèivanja pomagala, mora biti manji ili jednak datumu isporuke");

		}
		return datumNarudzbe;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("datum isporuke: ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes datumIsporuke
	 * 
	 * @return javax.swing.JTextField
	 */
	private JDateChooser getDatumIsporuke() {
		if (datumIsporuke == null) {
			datumIsporuke = new JDateChooser();
			datumIsporuke.setDateFormatString("dd.MM.yyyy");
			datumIsporuke.setPreferredSize(new java.awt.Dimension(125, 20));
			datumIsporuke.setMinimumSize(new java.awt.Dimension(125, 20));
			datumIsporuke.setDatum(Calendar.getInstance());
			datumIsporuke
					.setToolTipText("datum isporuke robe, mora biti isti ili veæi od datuma narudžbe");
			datumIsporuke.dodajSlusaca(this);
		}
		return datumIsporuke;
	}

	public void napuniPodatke(RacunVO ulaz) {
		this.omoguci();
		this.getJtSifraProizvodjaca().setEnabled(false);

		if (ulaz == null) {
			this.pobrisiFormu();
			return;
		}
		this.oznaceni = (RacunVO) ulaz;
		postaviOsnovnoOsiguranje(this.oznaceni.getOsnovnoOsiguranje()
				.booleanValue());

		try {
			if (oznaceni.getSifKlijenta() != null)
				oznaceniKlijent = (KlijentVO) DAOFactory.getInstance()
						.getKlijenti().read(oznaceni.getSifKlijenta());
			else
				oznaceniKlijent = null; // just in case
		} catch (SQLException e) {
			Logger.fatal(
					"Greska kod Racun DAO napuniPodatke - klijent se ne moze ucitati ",
					e);
			JOptionPane.showMessageDialog(this.getParent(),
					"Nastao je problem pri punjenju podataka o racunu!",
					"Upozorenje", JOptionPane.WARNING_MESSAGE);
		}
		if (oznaceniKlijent != null)
			this.jtKlijent.setText(this.oznaceniKlijent.getIme() + " "
					+ this.oznaceniKlijent.getPrezime());

		try {
			oznacenaPodruznica = (MjestoVO) DAOFactory.getInstance()
					.getMjesta().read(oznaceni.getSifPodrucnogUreda());
			if (oznacenaPodruznica != null) {
				this.jtPodruznica.setText(oznacenaPodruznica.getNaziv());

				String hzzoKljuc = this.oznaceni.getAktivnostZZR().trim()
						.toUpperCase().startsWith("A6") ? "hzzo_adr_"
						: "hzzozzr_adr_";
				// 20.03.06. -asabo- dodano
				// pogledati u bazu podataka postoji li zapis o adresi
				// podruznice
				this.jtAdresaPodruznice.setText(PostavkeBean
						.getPostavkaDB(
								hzzoKljuc
										+ this.oznacenaPodruznica.getSifra()
												.intValue(), ""));
			}// if podruznica ispravna
		} catch (SQLException e) {
			Logger.fatal(
					"Greška kod Racun DAO napuniPodatke - podružnica se ne može uèitati ",
					e);
			JOptionPane.showMessageDialog(this.getParent(),
					"Nastao je problem pri punjenju podataka o racunu!",
					"Upozorenje", JOptionPane.WARNING_MESSAGE);
		}

		this.datumNarudzbe.setDatum(this.oznaceni.getDatumNarudzbe());
		this.datumIsporuke.setDatum(this.oznaceni.getDatumIzdavanja());
		this.jtSifraProizvodjaca
				.setText(this.oznaceni.getSifProizvodjaca() != null ? this.oznaceni
						.getSifProizvodjaca() : "");
		this.jtBrojPotvrde1
				.setText(this.oznaceni.getBrojPotvrde1() != null ? this.oznaceni
						.getBrojPotvrde1() : "");
		this.jtBrojPotvrde2
				.setText(this.oznaceni.getBrojPotvrde2() != null ? this.oznaceni
						.getBrojPotvrde2() : "");

		// -as- 2009-05-13
		this.jtSifraAktivnostiDop.setText(this.oznaceni.getAktivnostDop());

		// bez obzira bilo dopunsko ili osnovno osiguranje, postavit cemo
		// vrijednost elementa
		this.jtBrojPoliceDopunsko
				.setText(this.oznaceni.getBrojPoliceDopunsko());
		this.postaviOsnovnoOsiguranje(this.oznaceni.getOsnovnoOsiguranje()
				.booleanValue());

		String sudj = this.oznaceni.getIznosSudjelovanja()==null ? "": 
			""+ ((float) this.oznaceni.getIznosSudjelovanja().intValue() / 100.0f);
		this.jtIznosSudjelovanja.setText("" + sudj);
		

		if (this.oznaceni.getPozivNaBroj1() != null)
			this.jtPozivNaBroj1.setText(this.oznaceni.getPozivNaBroj1());
		else
			this.jtPozivNaBroj1.setText("");

		if (this.oznaceni.getPozivNaBroj2() != null)
			this.jtPozivNaBroj2.setText(this.oznaceni.getPozivNaBroj2());
		else
			this.jtPozivNaBroj1.setText("");

		if (this.oznaceni.getNapomena() != null)
			this.jtNapomena.setText(this.oznaceni.getNapomena());
		else
			this.jtNapomena.setText("");

		// 31.03.06. -asabo- dodano
		// sif_drzave,broj_iskaznice1,broj_iskaznice2,ino_broj_lista1,ino_broj_lista2
		if (this.oznaceni.getBrojIskaznice1() != null)
			this.jtBrojIskaznice1.setText(this.oznaceni.getBrojIskaznice1());

		if (this.oznaceni.getBrojIskaznice2() != null)
			this.jtBrojIskaznice2.setText(this.oznaceni.getBrojIskaznice2());

		// 2009-03-17
		this.jtBrojPotvrdeLijecnik.setText(this.oznaceni
				.getBrojPotvrdePomagala());
		this.jtSifraAktivnosti.setText(this.oznaceni.getAktivnostZZR());

		if (this.oznaceni.getSifDrzave() != null) {
			ukljuciIno();
			this.jtBrojInoLista1.setText(this.oznaceni
					.getBrojInoBolesnickogLista1());
			this.jtBrojInoLista2.setText(this.oznaceni
					.getBrojInoBolesnickogLista2());

			try {
				this.oznacenaDrzava = (DrzavaVO) DAOFactory.getInstance()
						.getDrzava().read(oznaceni.getSifDrzave());
				this.jtDrzavaInoOsobe
						.setText(this.oznacenaDrzava != null ? this.oznacenaDrzava
								.getNaziv() : "?!?");
			} catch (SQLException e1) {
				Logger.fatal("SQL iznimka kod trazenja države po šifri", e1);
				JOptionPane
						.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Nastao je problem pri pretraživanju podataka. Provjerite poruke sustava!",
								"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			}

		} else
			iskljuciIno();

		// 03.04.06. -asabo- dodano punjenje forme podacima o lijecniku
		if (oznaceni.getSifLijecnika() != null)
			try {
				this.oznaceniLijecnik = (LijecnikVO) DAOFactory.getInstance()
						.getLijecnici().read(oznaceni.getSifLijecnika());
				this.jtLijecnik
						.setText(this.oznaceniLijecnik != null ? this.oznaceniLijecnik
								.toString() : "?!?");
			} catch (SQLException e1) {
				Logger.fatal("SQL iznimka kod trazenja lijeènika po šifri", e1);
				JOptionPane
						.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Nastao je problem pri pretraživanju podataka. Provjerite poruke sustava!",
								"Upozorenje!", JOptionPane.WARNING_MESSAGE);
			}
		else {
			this.oznaceniLijecnik = null;
			this.jtLijecnik.setText("");
		}

		// 09.04.06. -asabo- dodano
		this.jtBrojOsobnogRacunaOsnovno.setText(this.oznaceni
				.getBrojOsobnogRacunaOsnovno());
		this.jtBrojOsobnogRacunaDopunsko.setText(this.oznaceni
				.getBrojOsobnogRacunaDopunsko());

		// 07.05.06. -asabo- dodano
		this.jcStrankaUzelaSkupljiModel.setSelected(this.oznaceni
				.getKupljenSkupljiArtikl() != null ? this.oznaceni
				.getKupljenSkupljiArtikl().booleanValue() : false);

		// 02.07.06. -asabo- dodano
		this.jcRobaIsporucena
				.setSelected(this.oznaceni.getRobaIsporucena() != null ? this.oznaceni
						.getRobaIsporucena().booleanValue() : false);

		// 14.05.06. -asabo- dodano, treba namjestiti combo obx sa vrstama
		// pomagala na ispravnu vrijednost
		JComboBox jcb = getJcVrstaPomagala();
		int kom = jcb.getItemCount();

		for (int i = 0; i < kom; i++) {
			VrstaPomagalaVO vpvo = (VrstaPomagalaVO) jcb.getItemAt(i);
			if (vpvo.getSifra().intValue() == this.oznaceni.getVrstaPomagala()
					.intValue()) {
				jcb.setSelectedIndex(i);
				break;
			}
		}// for i

	}// napuniPodatke

	private void ukljuciIno() {
		this.jtBrojInoLista1.setEnabled(true);
		this.jtBrojInoLista2.setEnabled(true);
		this.jtDrzavaInoOsobe.setEnabled(true);
		this.jlBrojBolesnickogLista.setEnabled(true);
		this.jlDrzavaKlijenta.setEnabled(true);
		this.jcIno.setSelected(true);
	}

	private void iskljuciIno() {
		this.jtBrojInoLista1.setEnabled(false);
		this.jtBrojInoLista2.setEnabled(false);
		this.jtDrzavaInoOsobe.setEnabled(false);
		this.jlBrojBolesnickogLista.setEnabled(false);
		this.jlDrzavaKlijenta.setEnabled(false);
		this.jcIno.setSelected(false);
	}

	// ukljucuje ili iskljucuje potrebne elemente ne form
	private void postaviOsnovnoOsiguranje(boolean osnovno) {
		if (osnovno) {
			this.jtbOsnovno.setSelected(true);
			this.jtbDopunsko.setSelected(false);
			this.jtBrojPoliceDopunsko.setEnabled(false);
			this.jtBrojOsobnogRacunaDopunsko.setEnabled(false);
			this.jtSifraAktivnostiDop.setEnabled(false);
			this.jlBrojOsobRacZaDopOsig.setEnabled(false);
			this.jlBrojDopunskogOsig.setEnabled(false);
			this.jlSifraDopunskeAkt.setEnabled(false);
		} else {
			this.jtbOsnovno.setSelected(false);
			this.jtbDopunsko.setSelected(true);
			this.jtBrojPoliceDopunsko.setEnabled(true);
			this.jtBrojOsobnogRacunaDopunsko.setEnabled(true);			
			this.jtSifraAktivnostiDop.setEnabled(true);
			this.jlBrojOsobRacZaDopOsig.setEnabled(true);
			this.jlBrojDopunskogOsig.setEnabled(true);
			this.jlSifraDopunskeAkt.setEnabled(true);
		}
	}// postaviOsnovnoOsiguranje

	public RacunVO vratiPodatke() {
		// osnovna filozofija, ako je forma prazna, oznaceni==null, kreiramo
		// novi objekt i vracamo ga a onaj iznad gleda po sifri jeli novi ili
		// nije
		// ako je forma disejblana, enejblamo ju ...
		String iznSudjelovanjaStr = this.jtIznosSudjelovanja.getText();
		if (this.oznaceni == null) {
			this.oznaceni = new RacunVO();
			this.oznaceni.setSifra(Integer
					.valueOf(biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA));
			this.omoguci();

			boolean osn = this.jtbOsnovno.isSelected();
			this.oznaceni.setOsnovnoOsiguranje(Boolean.valueOf(osn));
			this.oznaceni.setDopunskoOsiguranje(Boolean.valueOf(!osn));

			this.postaviOsnovnoOsiguranje(this.jtbOsnovno.isSelected());

			if (this.oznaceni.getSifDrzave() == null)
				this.iskljuciIno();
			else
				ukljuciIno();

		}// if oznaceni prazan

		float iznosSudjelovanja = -1.0f;
		try {
			if (iznSudjelovanjaStr != null
					&& !iznSudjelovanjaStr.trim().equals(""))
				iznosSudjelovanja = Float.parseFloat(iznSudjelovanjaStr
						.replaceAll("\\,", ".").trim());
		} catch (NumberFormatException nfe) {
			Logger.log("Problem sa parsiranjem iznosa sudjelovanja! "+iznSudjelovanjaStr, nfe);
			iznosSudjelovanja = -2.0f;
		}

		if (iznosSudjelovanja >= 0.0f)
			this.oznaceni.setIznosSudjelovanja(Integer
					.valueOf((int) (iznosSudjelovanja * 100.0f + 0.005f)));
		else
			this.oznaceni.setIznosSudjelovanja( null );

		boolean osnovno = this.jtbOsnovno.isSelected();

		// sudjelovanje placa ili sam korisnik ili dopunsko osiguranje (to je
		// iznos koji osnovno ne pokriva)
		// trebalo bi sve ici preko iznosa osiguranja, ali nesto mi govori da ce
		// kasnije raditi oba broja pa zasad jednog nuliramo
		if (osnovno) {
			this.oznaceni.setOsnovnoOsiguranje(Boolean.valueOf(true));
			this.oznaceni.setDopunskoOsiguranje(Boolean.valueOf(false));
			this.oznaceni.setBrojPoliceDopunsko(null);
		} else {
			this.oznaceni.setOsnovnoOsiguranje(Boolean.valueOf(false));
			this.oznaceni.setDopunskoOsiguranje(Boolean.valueOf(true));
			this.oznaceni.setBrojPoliceDopunsko(this.jtBrojPoliceDopunsko
					.getText().trim());
		}
		// 02.07.06. -asabo- dodano
		this.oznaceni.setRobaIsporucena(Boolean.valueOf(this.jcRobaIsporucena
				.isSelected()));

		if (this.oznaceniKlijent != null)
			this.oznaceni.setSifKlijenta(this.oznaceniKlijent.getSifra());
		else {
			// ako nema oznacenog klijenta a nekakav tekst u klijent polju
			// postoji, jos k tome moze se splitati na barem dva pojma
			// znaci da je operater unio klijentovo ime i prezime pa cemo na
			// 'umjetan' nacin unijeti novog klijenta u db

			String[] imePrezime = this.jtKlijent.getText().trim().split(" ");
			int imPrLen = imePrezime.length;
			if (imPrLen > 1) {
				try {
					// ne mozemo drugacije nego pretpostaviti je prvo ime, a
					// zatim prezime
					KlijentVO kvo = new KlijentVO();
					kvo.setIme(imePrezime[0].trim());

					String prezime = "";

					for (int o = 1; o < imPrLen; o++)
						prezime += imePrezime[o] + " ";

					kvo.setPrezime(prezime.trim());
					kvo.setAdresa("(nema)");
					// kvo.setDatRodjenja(java.util.Calendar.getInstance());
					kvo.setMjesto(this.oznacenaPodruznica);

					// zapisati i njegov hzzo broj ako postoji
					if (!jtBrojIskaznice1.getText().trim().equals("")
							&& !jtBrojIskaznice2.getText().trim().equals("")) {
						kvo.setHzzo(jtBrojIskaznice1.getText().trim() + "/"
								+ jtBrojIskaznice2.getText().trim());
					}

					kvo.setSpol(new Character(
							imePrezime[0] != null
									&& (imePrezime[0].charAt(imPrLen - 1) == 'A' || imePrezime[0]
											.charAt(imPrLen - 1) == 'a') ? KlijentVO.SPOL_ZENSKO
									: KlijentVO.SPOL_MUSKO));
					Calendar c = Calendar.getInstance();
					kvo.setDatUpisa(c);
					kvo.setSlijedeciPregled(Calendar.getInstance()); // just in
																		// case
					kvo.setUmro(Boolean.valueOf(false));
					kvo.setPrimaInfo(false);
					kvo.setModified(true);
					kvo.setCreated(System.currentTimeMillis());
					Integer sifDj = GlavniFrame.running?Integer.valueOf(GlavniFrame
							.getSifDjelatnika()):null;
					kvo.setCreatedBy(sifDj);

					if (GlavniFrame.running)
					 DAOFactory.getInstance().getKlijenti().insert(kvo);

					if (kvo.getSifra() == null
							|| kvo.getSifra().intValue() == biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA)
						Logger.log("Insertiran novi klijent sa hzzo forme, greške nema, ali šifra mu je: "
								+ kvo != null
								&& kvo.getSifra() != null ? ""
								+ kvo.getSifra().intValue() : "null");

					this.oznaceniKlijent = kvo;
					this.jtKlijent.setText("" + kvo.getIme() + " "
							+ kvo.getPrezime());
					this.oznaceni.setSifKlijenta(this.oznaceniKlijent
							.getSifra());
				} catch (SQLException e) {
					Logger.fatal(
							"SQL iznimka kod pokušaja ubacivanja novog klijenta iz Hzzo forme",
							e);
					JOptionPane
							.showMessageDialog(
									GlavniFrame.getInstanca(),
									"Nastao je problem pri pohranjivanju klijenta. Kontaktirajte administratora",
									"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				} catch (Exception e) {
					Logger.fatal(
							"Opæa iznimka kod pokušaja ubacivanja novog klijenta iz Hzzo forme",
							e);
					JOptionPane
							.showMessageDialog(
									GlavniFrame.getInstanca(),
									"Nastao je problem pri pohranjivanju klijenta. Kontaktirajte administratora",
									"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				}
			}// ako ima barem dva podatka
		}// else

		this.oznaceni.setDatumNarudzbe(this.datumNarudzbe.getDatum());
		this.oznaceni.setDatumIzdavanja(this.datumIsporuke.getDatum());
		this.oznaceni.setSifProizvodjaca(this.jtSifraProizvodjaca.getText());
		this.oznaceni.setBrojPotvrde1(this.jtBrojPotvrde1.getText());
		this.oznaceni.setBrojPotvrde2(this.jtBrojPotvrde2.getText());
		this.oznaceni
				.setSifPodrucnogUreda(this.oznacenaPodruznica != null ? this.oznacenaPodruznica
						.getSifra() : null);
		// da se ne mora pokretati DAOFactory i traziti koje je to mjesto...
		this.oznaceni.setPodrucniUred(this.oznacenaPodruznica);
		this.oznaceni.setBrojPotvrdePomagala(this.jtBrojPotvrdeLijecnik
				.getText());
		this.oznaceni.setAktivnostZZR(this.jtSifraAktivnosti.getText());

		if (!this.jtPozivNaBroj1.getText().trim().equals(""))
			this.oznaceni.setPozivNaBroj1(this.jtPozivNaBroj1.getText().trim());
		else
			this.oznaceni.setPozivNaBroj1(null);

		if (!this.jtPozivNaBroj2.getText().trim().equals(""))
			this.oznaceni.setPozivNaBroj2(this.jtPozivNaBroj2.getText().trim());
		else
			this.oznaceni.setPozivNaBroj2(null);

		if (!this.jtNapomena.getText().trim().equals(""))
			this.oznaceni.setNapomena(this.jtNapomena.getText().trim());
		else
			this.oznaceni.setNapomena(null);

		// 01.04.06. -asabo- dodano
		if (jcIno.isSelected()) {
			this.oznaceni.setBrojInoBolesnickogLista1(this.jtBrojInoLista1
					.getText().trim());
			this.oznaceni.setBrojInoBolesnickogLista2(this.jtBrojInoLista2
					.getText().trim());
			this.oznaceni
					.setSifDrzave(this.oznacenaDrzava != null ? this.oznacenaDrzava
							.getSifra() : null);
		} else {
			this.oznaceni.setSifDrzave(null);// sa ovim se osiguravamo da vise
												// ino nije ukljucen, a ostali
												// podaci mogu ostati za
												// predomisljaj...
		}

		this.oznaceni.setBrojIskaznice1(this.jtBrojIskaznice1.getText().trim());
		this.oznaceni.setBrojIskaznice2(this.jtBrojIskaznice2.getText().trim());

		// 03.04.06. -asabo- dodano
		if (this.oznaceniLijecnik != null)
			this.oznaceni.setSifLijecnika(this.oznaceniLijecnik.getSifra());
		else {
			// 08.04.06. -asabo- dodano
			// ako nema oznacenog lijecnika a nekakav tekst u klijent polju
			// postoji, jos k tome moze se splitati na barem dva pojma
			// znaci da je operater unio klijentovo ime i prezime pa cemo na
			// 'umjetan' nacin unijeti novog klijenta u db
			this.oznaceni.setSifLijecnika(null);// just in case
			String dr = this.jtLijecnik.getText().trim();

			dr = dr.replaceFirst("dr. med.", "");
			dr = dr.replaceFirst("dr.med.", "");
			dr = dr.replaceFirst("dr.", "");

			dr = dr.replaceFirst("Dr. med.", "");
			dr = dr.replaceFirst("Dr.med.", "");
			dr = dr.replaceFirst("Dr.", "");

			String[] imePrezime = dr.split(" ");
			if (imePrezime.length > 1) {
				try {
					// ne mozemo drugacije nego pretpostaviti je prvo ime, a
					// zatim prezime
					LijecnikVO lvo = new LijecnikVO();
					lvo.setIme(imePrezime[0]);

					String prezime = "";

					for (int o = 1; o < imePrezime.length; o++)
						prezime += imePrezime[o] + " ";

					lvo.setPrezime(prezime.trim());
					lvo.setTitula("dr.");

					// prvo treba pokusati pronaci postoji li ipak lijecnik sa
					// takvim imenom i prezimenom u bazi
					SearchCriteria krit = new SearchCriteria();
					krit.setKriterij(LijecnikDAO.KRITERIJ_IME_PREZIME);
					ArrayList<String> l = new ArrayList<String>(2);
					l.add(lvo.getIme());
					l.add(lvo.getPrezime());
					krit.setPodaci(l);
					// prvo treba provjeriti da slucajno lijecnik vec ne postoji
					// u bazi, kod njih na to treba paziti
					LijecnikVO lvo2 = (LijecnikVO) DAOFactory.getInstance()
							.getLijecnici().read(krit);

					if (lvo2 != null) {
						this.oznaceniLijecnik = lvo2;
						this.jtLijecnik.setText("" + lvo2.getIme() + " "
								+ lvo2.getPrezime());
						this.oznaceni.setSifLijecnika(this.oznaceniLijecnik
								.getSifra());
					} else {
						// insertiramo lijecnika u bazu podataka
						DAOFactory.getInstance().getLijecnici().insert(lvo);

						if (lvo.getSifra() == null
								|| lvo.getSifra().intValue() == biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA)
							Logger.log("Insertiran novi lijeènik sa hzzo forme, greške nema, ali šifra mu je: "
									+ lvo != null
									&& lvo.getSifra() != null ? ""
									+ lvo.getSifra().intValue() : "null");

						this.oznaceniLijecnik = lvo;
						this.jtLijecnik.setText("" + lvo.getIme() + " "
								+ lvo.getPrezime());
						this.oznaceni.setSifLijecnika(this.oznaceniLijecnik
								.getSifra());
					}// else nije nasao lijecnika sa tim imenom i prezimenom
				} catch (SQLException e) {
					Logger.fatal(
							"SQL iznimka kod pokušaja ubacivanja novog lijecnika iz Hzzo forme",
							e);
					JOptionPane
							.showMessageDialog(
									GlavniFrame.getInstanca(),
									"Nastao je problem pri pohranjivanju lijeènika. Kontaktirajte administratora",
									"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				} catch (Exception e) {
					Logger.fatal(
							"Opæa iznimka kod pokušaja ubacivanja novog lijecnika iz Hzzo forme",
							e);
					JOptionPane
							.showMessageDialog(
									GlavniFrame.getInstanca(),
									"Nastao je problem pri pohranjivanju lijeènika. Kontaktirajte administratora",
									"Upozorenje!", JOptionPane.WARNING_MESSAGE);
				}
			}// ako ima barem dva podatka
		}// else lijecnik nije odabran a postoji ime i prezime na formi

		// 09.04.06. -asabo- dodano
		this.oznaceni
				.setBrojOsobnogRacunaOsnovno(this.jtBrojOsobnogRacunaOsnovno
						.getText().trim());
		this.oznaceni
				.setBrojOsobnogRacunaDopunsko(this.jtBrojOsobnogRacunaDopunsko
						.getText().trim());
		this.oznaceni.setKupljenSkupljiArtikl(Boolean
				.valueOf(this.jcStrankaUzelaSkupljiModel.isSelected()));

		// 14.05.06. -asabo- dodano
		VrstaPomagalaVO vpvo = (VrstaPomagalaVO) this.jcVrstaPomagala
				.getSelectedItem();
		this.oznaceni.setVrstaPomagala(vpvo.getSifra());

		// -as- 2009-03-21
		String bp = this.jtBrojPotvrdeLijecnik.getText().trim();
		this.oznaceni.setBrojPotvrdePomagala(bp.equals("") ? null : bp);

		// -as- 2009-05-13
		this.oznaceni.setAktivnostDop(osnovno ? null
				: this.jtSifraAktivnostiDop.getText().trim());

		return this.oznaceni;
	}// vratiPodatke

	private void alert(String poruka) {
		JOptionPane.showMessageDialog(GlavniFrame.getInstanca(), poruka,
				"Upozorenje!", JOptionPane.WARNING_MESSAGE);
	}

	public void pobrisiFormu() {
		this.oznaceni = null;
		this.oznaceniKlijent = null;
		this.oznacenaPodruznica = null;
		this.oznacenaDrzava = null;

		final String p = "";
		this.jtKlijent.setText(p);
		this.jtSifraProizvodjaca.setText(p);
		this.jtBrojPotvrde1.setText(p);
		this.jtBrojPotvrde2.setText(p);
		this.jtBrojPoliceDopunsko.setText(p);
		this.jtIznosSudjelovanja.setText(p);
		this.jtPozivNaBroj1.setText(p);
		this.jtPozivNaBroj2.setText(p);
		this.jtPodruznica.setText(p);
		this.jtNapomena.setText(p);
		this.jcIno.setSelected(false);
		this.jtBrojInoLista1.setText(p);
		this.jtBrojInoLista2.setText(p);
		this.jtBrojIskaznice1.setText(p);
		this.jtBrojIskaznice2.setText(p);
		this.jtDrzavaInoOsobe.setText(p);

		this.jtAdresaPodruznice.setText(p);

		this.jtLijecnik.setText(p);

		// 09.04.06. -asabo- dodano
		this.jtBrojOsobnogRacunaOsnovno.setText(p);
		this.jtBrojOsobnogRacunaDopunsko.setText(p);
		this.jcStrankaUzelaSkupljiModel.setSelected(false);
		this.jcRobaIsporucena.setSelected(true); // uvijek ce biti ukljucen..

		this.jtBrojPotvrdeLijecnik.setText(p);
		this.jtSifraAktivnostiDop.setText("A6900737"); // defaultna vrijednost
														// za vecinu korisnika

		postaviVrstePomagalaNaPocetnuPoziciju();
		postaviOsnovnoOsiguranje(true);

		onemoguci();
	}// pobrisiFormu
		// ne koristi se...

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
		this.jtbOsnovno.setEnabled(s);
		this.jtbDopunsko.setEnabled(s);
		this.jtKlijent.setEnabled(s);
		this.datumIsporuke.setEnabled(s);
		this.datumNarudzbe.setEnabled(s);
		// this.jtSifraProizvodjaca.setEnabled(s);
		this.jtBrojPotvrde1.setEnabled(s);
		this.jtBrojPotvrde2.setEnabled(s);
		this.jtBrojPoliceDopunsko.setEnabled(s);
		this.jtIznosSudjelovanja.setEnabled(s);
		this.jtPodruznica.setEnabled(s);
		this.jtPozivNaBroj1.setEnabled(s);
		this.jtPozivNaBroj2.setEnabled(s);
		this.datumIsporuke.setEnabled(s);
		this.datumNarudzbe.setEnabled(s);
		this.jtBrojIskaznice1.setEnabled(s);
		this.jtBrojIskaznice2.setEnabled(s);
		this.jcIno.setEnabled(s);
		this.jtBrojInoLista1.setEnabled(s);
		this.jtBrojInoLista2.setEnabled(s);
		this.jtDrzavaInoOsobe.setEnabled(s);
		this.jtLijecnik.setEnabled(s);
		this.jtBrojOsobnogRacunaOsnovno.setEnabled(s);
		this.jtBrojOsobnogRacunaDopunsko.setEnabled(s);
		this.jcStrankaUzelaSkupljiModel.setEnabled(s);
		this.jcVrstaPomagala.setEnabled(s);
		this.jcRobaIsporucena.setEnabled(s);
	}// postaviStatuseElemenata

	public boolean jeliIzmjenjen() {
		return false;
	}

	/**
	 * This method initializes jpnTipOsiguranja
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpnTipOsiguranja() {
		if (jpnTipOsiguranja == null) {
			jpnTipOsiguranja = new javax.swing.JPanel();
			java.awt.FlowLayout tipOsiguranjaFlowLayout = new java.awt.FlowLayout();
			tipOsiguranjaFlowLayout.setAlignment(FlowLayout.LEFT);
			tipOsiguranjaFlowLayout.setHgap(2);
			tipOsiguranjaFlowLayout.setVgap(0);
			jpnTipOsiguranja.setLayout(tipOsiguranjaFlowLayout);
			jpnTipOsiguranja.add(getJtbOsnovno(), null);
			jpnTipOsiguranja.add(getJtbDopunsko(), null);
			jpnTipOsiguranja.setPreferredSize(new Dimension(155, 27));
			jpnTipOsiguranja
					.setToolTipText("osiguranje može biti osnovno ili dopunsko");
		}
		return jpnTipOsiguranja;
	}

	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setEnabled(false);
			jLabel4.setToolTipText("ne koristi se vise!");
			jLabel4.setText("Šifra proizvoðaèa: ");
		}
		return jLabel4;
	}

	/**
	 * This method initializes jtSifraProizvodjaca
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtSifraProizvodjaca() {
		if (jtSifraProizvodjaca == null) {
			jtSifraProizvodjaca = new javax.swing.JTextField();
			jtSifraProizvodjaca.setHorizontalAlignment(SwingConstants.LEFT);
			jtSifraProizvodjaca
					.setPreferredSize(new java.awt.Dimension(120, 20));
			jtSifraProizvodjaca.setMinimumSize(new java.awt.Dimension(120, 20));

			jtSifraProizvodjaca
					.setToolTipText("ne koristi se vise!");

			jtSifraProizvodjaca
					.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusGained(java.awt.event.FocusEvent e) {
							jtSifraProizvodjaca.selectAll();
						}
					});

			ProizvodjaciDAO proizvodjaci = GlavniFrame.running?DAOFactory.getInstance()
					.getProizvodjaci():null;
			this.pretrazivanjeProizvodjaci = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), proizvodjaci, 10, 20, 170, 100,
					(Component) jtSifraProizvodjaca);
			this.pretrazivanjeProizvodjaci.dodajSlusaca(this);
			this.pretrazivanjeProizvodjaci.setMaksimumZaPretrazivanje(8);

		}
		return jtSifraProizvodjaca;
	}

	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Broj potvrde HZZO-a: ");
		}
		return jLabel5;
	}

	/**
	 * This method initializes jtBrojPotvrde1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojPotvrde1() {
		if (jtBrojPotvrde1 == null) {
			jtBrojPotvrde1 = new javax.swing.JTextField();
			jtBrojPotvrde1.setPreferredSize(new Dimension(45, 24));
			jtBrojPotvrde1.setMinimumSize(new Dimension(45, 23));
			jtBrojPotvrde1
					.setToolTipText("šifra podruènog ureda, automatski æe se postaviti");
		}
		return jtBrojPotvrde1;
	}

	/**
	 * This method initializes jtBrojPotvrde2
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojPotvrde2() {
		if (jtBrojPotvrde2 == null) {
			jtBrojPotvrde2 = new javax.swing.JTextField();
			jtBrojPotvrde2.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent arg0) {
					provjeriIspravnostHzzoBrojaPotvrde();
				}

			});
			jtBrojPotvrde2.setPreferredSize(new Dimension(100, 24));
			jtBrojPotvrde2.setMinimumSize(new Dimension(91, 23));
			jtBrojPotvrde2.setToolTipText("broj potvrde HZZO-a");
		}
		return jtBrojPotvrde2;
	}
	
	private Thread ispitivacHzzoBrojaPotvrde=null;
	private final static CheckDigit cd = new CheckISO7064Mod11_10();
	/*
	 * spina poseban thread koji provjerava jeli broj ispravan, i ako nije odradjuje animaciju kojom 
	 * upozorava korisnika na neispravnost broja. Nazalost, jos uvijek su u opticaju brojevi koji nisu ispravni
	 * po ISO standardu, ali kao takvi su propisani i odobreni od HZZO-a, pa moraju proci.. 
	 */
	private void provjeriIspravnostHzzoBrojaPotvrde() {

		if (ispitivacHzzoBrojaPotvrde == null
				|| !ispitivacHzzoBrojaPotvrde.isAlive()) {
			ispitivacHzzoBrojaPotvrde = new Thread() {
				@Override
				public void run() {
					final String broj = jtBrojPotvrde2.getText().trim();
					
					if (broj.length() > 8 && !cd.verify(broj)) 
					{
						GUI.odradiUpozorenjeNaElementu(jtBrojPotvrde2, "Neispavan broj potvrde!!!");						
					}
				}
			};

			ispitivacHzzoBrojaPotvrde.start();
		}	
	}


	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlBrojDopunskogOsig() {
		if (jlBrojDopunskogOsig == null) {
			jlBrojDopunskogOsig = new javax.swing.JLabel();
			jlBrojDopunskogOsig.setText("Broj dopunskog osig.: ");
		}
		return jlBrojDopunskogOsig;
	}

	/**
	 * This method initializes jtBrojPoliceDopunsko
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojPoliceDopunsko() {
		if (jtBrojPoliceDopunsko == null) {
			jtBrojPoliceDopunsko = new javax.swing.JTextField();
			jtBrojPoliceDopunsko.setHorizontalAlignment(SwingConstants.LEFT);
			jtBrojPoliceDopunsko
					.setToolTipText("broj kartice dopunskog osiguranja, 8 znakova, obavezan podatak ako radite raèun za dopunsko osiguranje");
			jtBrojPoliceDopunsko.setPreferredSize(new java.awt.Dimension(120,
					20));
			jtBrojPoliceDopunsko
					.setMinimumSize(new java.awt.Dimension(120, 20));
		}
		return jtBrojPoliceDopunsko;
	}

	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if (jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Iznos sudjelovanja: ");
		}
		return jLabel7;
	}

	/**
	 * This method initializes jtIznosSudjelovanja
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtIznosSudjelovanja() {
		if (jtIznosSudjelovanja == null) {
			jtIznosSudjelovanja = new javax.swing.JTextField();
			jtIznosSudjelovanja
					.setToolTipText("iznos sudjelovanja na raèun klijenta ili dopunskog osiguranja ovisno o kojoj vrsti raèuna se radi, sudjelovanje klijenta ili iznos na teret dopunskog osiguranja");
			jtIznosSudjelovanja
					.setPreferredSize(new Dimension(120, 23));
			jtIznosSudjelovanja.setMinimumSize(new java.awt.Dimension(120, 20));
			jtIznosSudjelovanja.setText("");
			jtIznosSudjelovanja
					.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusGained(java.awt.event.FocusEvent e) {
							jtIznosSudjelovanja.selectAll();
						}
					});
		}
		return jtIznosSudjelovanja;
	}

	public void labelaOznacena(Labela labela) {
		Object izvornik = labela.getIzvornik();
		if (izvornik == null)
			return;

		if (izvornik instanceof KlijentVO) {
			this.oznaceniKlijent = (KlijentVO) labela.getIzvornik();
			this.jtKlijent
					.setText(this.oznaceniKlijent != null ? this.oznaceniKlijent
							.getIme() + " " + this.oznaceniKlijent.getPrezime()
							: "?!?");

			String hzzoBroj = this.oznaceniKlijent.getHzzo();
			if (hzzoBroj != null && hzzoBroj.indexOf('/') != -1) {
				String[] br = hzzoBroj.split("/");
				if (br != null && br.length == 2) {
					this.jtBrojIskaznice1.setText(br[0]);
					this.jtBrojIskaznice2.setText(br[1]);
					getJtLijecnik().requestFocusInWindow();
					this.brojIskaznice2FocusLost(null, false);

				}// if
			}// if hzzoBroj postoji
		}// if izvornik KlijentVO
		else if (izvornik instanceof MjestoVO) {
			this.oznacenaPodruznica = (MjestoVO) izvornik;
			this.jtPodruznica.setText(this.oznacenaPodruznica.getNaziv());

			String hzzoKljuc = jtSifraAktivnosti.getText().trim().toUpperCase()
					.startsWith("A6") ? "hzzo_adr_" : "hzzozzr_adr_";
			jtAdresaPodruznice.setText(PostavkeBean.getPostavkaDB(hzzoKljuc
					+ oznacenaPodruznica.getSifra().intValue(), ""));

			Integer bp = this.oznacenaPodruznica.getSifPodruzniceHzzo();
			if (bp != null) {
				this.jtBrojPotvrde1.setText("" + bp.intValue());
				// samo ako unutra nema NISTA
				if (this.jtBrojIskaznice1.getText().trim().equals(""))
					this.jtBrojIskaznice1.setText("" + bp.intValue());
			}// if

			this.jtKlijent.requestFocusInWindow();

		}// if izvornik
		else if (izvornik instanceof DrzavaVO) {
			this.oznacenaDrzava = (DrzavaVO) labela.getIzvornik();
			this.jtDrzavaInoOsobe
					.setText(this.oznacenaDrzava != null ? this.oznacenaDrzava
							.getNaziv() : "?!?");
			this.jtIznosSudjelovanja.requestFocusInWindow();
		} else if (izvornik instanceof LijecnikVO) {
			this.oznaceniLijecnik = (LijecnikVO) labela.getIzvornik();
			this.jtLijecnik
					.setText(this.oznaceniLijecnik != null ? this.oznaceniLijecnik
							.toString() : "?!?");
			this.jtBrojPotvrde2.requestFocusInWindow();
		} else if (izvornik instanceof ProizvodjacVO) {
			this.oznaceniProizvodjac = (ProizvodjacVO) izvornik;
			Integer hz = this.oznaceniProizvodjac.getHzzoSifra();
			this.jtSifraProizvodjaca.setText(hz != null ? "" + hz.intValue()
					: "");
		}
	}

	/**
	 * This method initializes jLabel8
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if (jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("HZZO Podružnica: ");
		}
		return jLabel8;
	}

	/**
	 * This method initializes jtPodruznica
	 * 
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtPodruznica() {
		if (jtPodruznica == null) {
			jtPodruznica = new javax.swing.JTextField();
			jtPodruznica.setToolTipText("naziv HZZO podru\u017Enice");
			jtPodruznica.setHorizontalAlignment(SwingConstants.LEFT);
			jtPodruznica.setPreferredSize(new Dimension(160, 23));
			jtPodruznica.setMinimumSize(new java.awt.Dimension(160, 22));
			jtPodruznica.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(java.awt.event.FocusEvent e) {
					jtPodruznica.selectAll();
				}
			});

			MjestoDAO mjesta = GlavniFrame.running?DAOFactory.getInstance()
					.getMjesta():null;
			this.pretrazivanjePodruznica = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), mjesta, 10, 20, 160, 60,
					(Component) jtPodruznica);
			SearchCriteria kr = new SearchCriteria();
			kr.setKriterij(MjestoVO.KRITERIJ_PRETRAZIVANJA_PODRUZNICE);

			// filter ce ugradjivati upit u kriterij prije zvanja findAll
			// metode...
			this.pretrazivanjePodruznica.setKriterij(kr);

			this.pretrazivanjePodruznica.setMaksimumZaPretrazivanje(8);
			this.pretrazivanjePodruznica.dodajSlusaca(this);

		}// if jtPodruznica==null
		return jtPodruznica;
	}

	/**
	 * This method initializes jLabel9
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if (jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText("Poziv na br.: ");
		}
		return jLabel9;
	}

	/**
	 * This method initializes jpPozivNaBroj
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpPozivNaBroj() {
		if (jpPozivNaBroj == null) {
			jpPozivNaBroj = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout4 = new java.awt.FlowLayout();
			layFlowLayout4.setAlignment(FlowLayout.LEFT);
			layFlowLayout4.setHgap(0);
			layFlowLayout4.setVgap(0);
			jpPozivNaBroj.setLayout(layFlowLayout4);
			jpPozivNaBroj.add(getJtPozivNaBroj1(), null);
			jpPozivNaBroj.add(getJtPozivNaBroj2(), null);
			jpPozivNaBroj.setPreferredSize(new Dimension(200, 25));
			jpPozivNaBroj.setMinimumSize(new Dimension(166, 25));
			jpPozivNaBroj
					.setToolTipText("poziv na broj, sam æe se postaviti ako ste tako htjeli");
		}
		return jpPozivNaBroj;
	}

	/**
	 * This method initializes jtPozivNaBroj1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPozivNaBroj1() {
		if (jtPozivNaBroj1 == null) {
			jtPozivNaBroj1 = new javax.swing.JTextField();
			jtPozivNaBroj1.setPreferredSize(new Dimension(35, 24));
			jtPozivNaBroj1.setMinimumSize(new Dimension(30, 22));
			jtPozivNaBroj1.setToolTipText("prvi dio poziva na broj");
			jtPozivNaBroj1
					.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return jtPozivNaBroj1;
	}

	/**
	 * This method initializes jtPozivNaBroj2
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtPozivNaBroj2() {
		if (jtPozivNaBroj2 == null) {
			jtPozivNaBroj2 = new javax.swing.JTextField();
			jtPozivNaBroj2.setPreferredSize(new Dimension(150, 24));
			jtPozivNaBroj2.setToolTipText("drugi dio poziva na broj");
			jtPozivNaBroj2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
			jtPozivNaBroj2.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					// ako je broj osobnog racuna prazan postaviti u njega
					// vrijednost upisanog
					// poziva na broj
					if (jtBrojOsobnogRacunaOsnovno.getText().trim().equals(""))
						jtBrojOsobnogRacunaOsnovno.setText(jtPozivNaBroj2
								.getText());
				}

				public void focusGained(java.awt.event.FocusEvent e) {
					jtBrojPotvrde2.selectAll();
				}
			});
		}
		return jtPozivNaBroj2;
	}

	/**
	 * This method initializes jLabel10
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel10() {
		if (jLabel10 == null) {
			jLabel10 = new javax.swing.JLabel();
			jLabel10.setText("Napomena: ");
			jLabel10.setToolTipText("Napomena: ");
		}
		return jLabel10;
	}

	/**
	 * This method initializes jtNapomena
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtNapomena() {
		if (jtNapomena == null) {
			jtNapomena = new javax.swing.JTextField();
			jtNapomena
					.setToolTipText("napomena uz izdani raèun, bit æe ispisana na formularu");
		}
		return jtNapomena;
	}

	/**
	 * This method initializes jLabel11
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel11() {
		if (jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setText("Adresa podru\u017Enice: ");
		}
		return jLabel11;
	}

	/**
	 * This method initializes jtAdresaPodruznice
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAdresaPodruznice() {
		if (jtAdresaPodruznice == null) {
			jtAdresaPodruznice = new javax.swing.JTextField();
			jtAdresaPodruznice.setHorizontalAlignment(SwingConstants.LEFT);
			jtAdresaPodruznice.setPreferredSize(new Dimension(162, 22));
			jtAdresaPodruznice
					.setToolTipText("adresa HZZO podružnice za koju radite raèun");
			jtAdresaPodruznice
					.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusLost(java.awt.event.FocusEvent e) {
							String hzzoKljuc = jtSifraAktivnosti.getText()
									.trim().toUpperCase().startsWith("A6") ? "hzzo_adr_"
									: "hzzozzr_adr_";
							if (oznacenaPodruznica != null
									&& !jtAdresaPodruznice.getText().trim()
											.equals(""))
								PostavkeBean.setPostavkaDB(hzzoKljuc
										+ oznacenaPodruznica.getSifra()
												.intValue(), jtAdresaPodruznice
										.getText().trim());
						}

						public void focusGained(java.awt.event.FocusEvent e) {
							jtAdresaPodruznice.selectAll();
						}
					});
		}
		return jtAdresaPodruznice;
	}

	/**
	 * This method initializes jLabel12
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel12() {
		if (jLabel12 == null) {
			jLabel12 = new javax.swing.JLabel();
			jLabel12.setText("Broj osiguranja klijenta:");
			jLabel12.setPreferredSize(new java.awt.Dimension(133, 20));
			jLabel12.setMinimumSize(new java.awt.Dimension(133, 20));
			jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel12;
	}

	/**
	 * This method initializes jtBrojIskaznice1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojIskaznice1() {
		if (jtBrojIskaznice1 == null) {
			jtBrojIskaznice1 = new javax.swing.JTextField();
			jtBrojIskaznice1.setPreferredSize(new Dimension(40, 24));
			jtBrojIskaznice1.setMinimumSize(new Dimension(40, 24));
			jtBrojIskaznice1.setToolTipText("prvi dio, 3 broja");
		}
		return jtBrojIskaznice1;
	}

	/**
	 * This method initializes jtBrojIskaznice2
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojIskaznice2() {
		if (jtBrojIskaznice2 == null) {
			jtBrojIskaznice2 = new javax.swing.JTextField();
			jtBrojIskaznice2.setPreferredSize(new Dimension(111, 24));
			jtBrojIskaznice2.setMinimumSize(new Dimension(90, 24));
			jtBrojIskaznice2.setToolTipText("drugi dio, 8 brojeva");
			jtBrojIskaznice2.setNextFocusableComponent(getJtLijecnik());
			jtBrojIskaznice2.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent arg0) {
					brojIskaznice2FocusLost(arg0, false);
				}
			});
			jtBrojIskaznice2.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent ke) {
					brojIskaznice2KeyTyped(ke);
				}
			});
		}
		return jtBrojIskaznice2;
	}

	/**
	 * This method initializes jcIno
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcIno() {
		if (jcIno == null) {
			jcIno = new javax.swing.JCheckBox();
			jcIno.setText("INO");
			jcIno.setToolTipText("ako je osoba stranac ukljuèite ovu opciju");
			jcIno.setMnemonic(java.awt.event.KeyEvent.VK_I);
			jcIno.setPreferredSize(new java.awt.Dimension(54, 24));
			jcIno.setMinimumSize(new Dimension(54, 24));
			jcIno.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jcIno.setFont(arial);
			jcIno.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jtBrojInoLista1.requestFocusInWindow();
				}

			});
			jcIno.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (jcIno.isSelected())
						ukljuciIno();
					else
						iskljuciIno();
				}
			});
		}
		return jcIno;
	}

	/**
	 * This method initializes jLabel13
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlBrojBolesnickogLista() {
		if (jlBrojBolesnickogLista == null) {
			jlBrojBolesnickogLista = new javax.swing.JLabel();
			jlBrojBolesnickogLista.setText("Br. bolesni\u010Dkog lista: ");
			jlBrojBolesnickogLista.setPreferredSize(new Dimension(136, 20));
			jlBrojBolesnickogLista.setMinimumSize(new Dimension(132, 20));
			jlBrojBolesnickogLista.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jlBrojBolesnickogLista.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
			jlBrojBolesnickogLista.setVisible(true);
		}
		return jlBrojBolesnickogLista;
	}

	/**
	 * This method initializes jtBrojInoLista1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojInoLista1() {
		if (jtBrojInoLista1 == null) {
			jtBrojInoLista1 = new javax.swing.JTextField();
			jtBrojInoLista1.setPreferredSize(new Dimension(40, 24));
			jtBrojInoLista1.setMinimumSize(new Dimension(40, 24));
			jtBrojInoLista1.setMaximumSize(new Dimension(40, 24));
			jtBrojInoLista1
					.setToolTipText("prvi dio broja bolesnièkog lista stranca - 3 znamenke - obavezan podatak ");
		}
		return jtBrojInoLista1;
	}

	/**
	 * This method initializes jtBrojInoLista2
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojInoLista2() {
		if (jtBrojInoLista2 == null) {
			jtBrojInoLista2 = new javax.swing.JTextField();
			jtBrojInoLista2.setPreferredSize(new Dimension(120, 24));
			jtBrojInoLista2.setMinimumSize(new Dimension(90, 24));
			jtBrojInoLista2
					.setToolTipText("drugi dio bolesnièkog lista stranca - 8 znamenki - obavezan podatak ");
		}
		return jtBrojInoLista2;
	}

	/**
	 * This method initializes jLabel14
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlDrzavaKlijenta() {
		if (jlDrzavaKlijenta == null) {
			jlDrzavaKlijenta = new javax.swing.JLabel();
			jlDrzavaKlijenta.setText("Država klijenta: ");
			jlDrzavaKlijenta.setPreferredSize(new java.awt.Dimension(89, 20));
			jlDrzavaKlijenta.setMinimumSize(new java.awt.Dimension(89, 20));
			jlDrzavaKlijenta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jlDrzavaKlijenta;
	}

	/**
	 * This method initializes jtDrzavaInoOsobe
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtDrzavaInoOsobe() {
		if (jtDrzavaInoOsobe == null) {
			jtDrzavaInoOsobe = new javax.swing.JTextField();
			jtDrzavaInoOsobe.setPreferredSize(new Dimension(147, 25));
			jtDrzavaInoOsobe.setMinimumSize(new java.awt.Dimension(120, 25));

			jtDrzavaInoOsobe
					.setToolTipText("država klijenta (ako je stranac) - obavezan podatak");
			jtDrzavaInoOsobe
					.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusGained(java.awt.event.FocusEvent e) {
							jtDrzavaInoOsobe.selectAll();
						}
					});
			jtDrzavaInoOsobe
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (jtDrzavaInoOsobe.getText().trim().equals(""))
								oznacenaDrzava = null;
						}
					});

			DrzavaDAO drzava = GlavniFrame.running?DAOFactory.getInstance()
					.getDrzava():null;
			this.pretrazivanjeDrzave = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), drzava, 10, 20, 170, 100,
					(Component) jtDrzavaInoOsobe);
			this.pretrazivanjeDrzave.dodajSlusaca(this);
			this.pretrazivanjeDrzave.setMaksimumZaPretrazivanje(7);
		}
		return jtDrzavaInoOsobe;
	}

	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
	}

	/**
	 * This method initializes jLabel15
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel15() {
		if (jLabel15 == null) {
			jLabel15 = new javax.swing.JLabel();
			jLabel15.setText("Lijeènik: ");
		}
		return jLabel15;
	}

	/**
	 * This method initializes jtLijecnik
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtLijecnik() {
		if (jtLijecnik == null) {
			jtLijecnik = new javax.swing.JTextField();
			jtLijecnik.setPreferredSize(new Dimension(160, 23));
			jtLijecnik.setMinimumSize(new java.awt.Dimension(160, 20));
			jtLijecnik
					.setToolTipText("lijeènik koji je izdao recept, ako ne dobijete ponudu na popisu, pazite da obavezno unesete prvo ime pa prezime lijeènika");
			jtLijecnik.setNextFocusableComponent(getJtBrojPotvrde2());
			jtLijecnik.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jtLijecnik.getText().trim().equals(""))
						oznaceniLijecnik = null; // brisanje lijecnika..

				}
			});
			jtLijecnik.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					if (jtLijecnik.getText().trim().equals(""))
						oznaceniLijecnik = null; // brisanje lijecnika..
					if (oznaceniLijecnik != null
							&& !oznaceniLijecnik.toString().equals(
									jtLijecnik.getText().trim()))
						oznaceniLijecnik = null;
				}

				public void focusGained(java.awt.event.FocusEvent e) {
					jtLijecnik.selectAll();
				}
			});

			LijecnikDAO lijecnici = GlavniFrame.running?DAOFactory.getInstance()
					.getLijecnici():null;
			this.pretrazivanjeLijecnici = new PretrazivanjeProzor(
					GlavniFrame.getInstanca(), lijecnici, 10, 20, 170, 100,
					(Component) jtLijecnik);
			this.pretrazivanjeLijecnici.dodajSlusaca(this);
			this.pretrazivanjeLijecnici.setMinimumZnakovaZaPretrazivanje(3);
			this.pretrazivanjeLijecnici.setMaksimumZaPretrazivanje(10);
		}
		return jtLijecnik;
	}

	/**
	 * This method initializes jLabel16
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlBrojOsoRacZaOsnOsig() {
		if (jlBrojOsoRacZaOsnOsig == null) {
			jlBrojOsoRacZaOsnOsig = new javax.swing.JLabel();
			jlBrojOsoRacZaOsnOsig.setText("Broj oso. raè. za osn. osig: ");
			jlBrojOsoRacZaOsnOsig.setToolTipText("Broj osobnog raèuna za osnovno osiguranje");
		}
		return jlBrojOsoRacZaOsnOsig;
	}

	/**
	 * This method initializes jtBrojOsobnogRacunaOsnovno
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojOsobnogRacunaOsnovno() 
	{
		if (jtBrojOsobnogRacunaOsnovno == null) 
		{
			jtBrojOsobnogRacunaOsnovno = new javax.swing.JTextField();
			jtBrojOsobnogRacunaOsnovno.setPreferredSize(new Dimension(120, 23));
			jtBrojOsobnogRacunaOsnovno.setMinimumSize(new java.awt.Dimension(
					120, 22));
			jtBrojOsobnogRacunaOsnovno
					.setToolTipText("broj osobnog raèuna za osnovno osiguranje, obavezan podatak, ako ne stavite ništa, upisat æe se broj raèuna");
			jtBrojOsobnogRacunaOsnovno
					.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusLost(java.awt.event.FocusEvent e) {
							// 11.05.06. -asabo- dodano kopiranje broja osobnog
							// racuna..
							if (jtbDopunsko.isSelected()
									&& jtBrojOsobnogRacunaDopunsko.getText()
											.trim().equals("")
									&& !jtBrojOsobnogRacunaOsnovno.getText()
											.trim().equals(""))
								jtBrojOsobnogRacunaDopunsko
										.setText(jtBrojOsobnogRacunaOsnovno
												.getText().trim() + "");
						}

						public void focusGained(java.awt.event.FocusEvent e) {
							jtBrojOsobnogRacunaOsnovno.selectAll();
						}
					}
					);
		}
		
		return jtBrojOsobnogRacunaOsnovno;
	}

	/**
	 * This method initializes jLabel17
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlBrojOsobRacZaDopOsig() 
	{
		if (jlBrojOsobRacZaDopOsig == null) 
		{
			jlBrojOsobRacZaDopOsig = new javax.swing.JLabel();
			jlBrojOsobRacZaDopOsig.setText("Broj osob. raè. za dop. osig:");
		}
		
		return jlBrojOsobRacZaDopOsig;
	}

	/**
	 * This method initializes jtBrojOsobnogRacunaDopunsko
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBrojOsobnogRacunaDopunsko() 
	{
		if (jtBrojOsobnogRacunaDopunsko == null) {
			jtBrojOsobnogRacunaDopunsko = new javax.swing.JTextField();
			jtBrojOsobnogRacunaDopunsko
					.setPreferredSize(new Dimension(161, 23));
			jtBrojOsobnogRacunaDopunsko.setMinimumSize(new java.awt.Dimension(
					120, 22));
			jtBrojOsobnogRacunaDopunsko
					.setToolTipText("broj osobnog raèuna za dopunsko osiguranje, nije obavezan podatak");
			jtBrojOsobnogRacunaDopunsko
					.addFocusListener(new java.awt.event.FocusAdapter() {
						public void focusGained(java.awt.event.FocusEvent e) {
							jtBrojOsobnogRacunaDopunsko.selectAll();
						}
					});
		}
	
		return jtBrojOsobnogRacunaDopunsko;
	}

	/**
	 * This method initializes jcStrankaUzelaSkupljiModel
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcStrankaUzelaSkupljiModel() 
	{
		if (jcStrankaUzelaSkupljiModel == null) 
		{
			jcStrankaUzelaSkupljiModel = new javax.swing.JCheckBox();
			jcStrankaUzelaSkupljiModel.setText("stranka uzela skuplji model");
			jcStrankaUzelaSkupljiModel
					.setToolTipText("ako je stranka odabrala skuplji artikl od onoga koji je propisao HZZO");
			jcStrankaUzelaSkupljiModel
					.setMnemonic(java.awt.event.KeyEvent.VK_S);
			jcStrankaUzelaSkupljiModel.setVisible(false);
		}
	
		return jcStrankaUzelaSkupljiModel;
	}

	/**
	 * This method initializes jLabel18
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel18() {
		if (jLabel18 == null) {
			jLabel18 = new javax.swing.JLabel();
			jLabel18.setText("Vrsta pomagala: ");
		}
		return jLabel18;
	}

	private void postaviVrstePomagalaNaPocetnuPoziciju() 
	{
		// ako korisnik koristi sva hzzo pomagala onda ne izdaje samo ocna
		// pomagala
		boolean svaPomagala = GlavniFrame.isKoristiSvaPomagala();
		
		int kom = jcVrstaPomagala.getItemCount();
		VrstaPomagalaVO vpvo = null;
		
		for (int i = 0; i < kom; i++) 
		{
			vpvo = (VrstaPomagalaVO) jcVrstaPomagala.getItemAt(i);
			if (svaPomagala && vpvo.getSifra().intValue() == 1)
				jcVrstaPomagala.setSelectedIndex(i);
			else if (!svaPomagala && vpvo.getSifra().intValue() == 4)
				jcVrstaPomagala.setSelectedIndex(i);
		}// for i
		
		vpvo = null;
	
	}// postaviVrstePomagalaNaPocetnuPoziciju

	/**
	 * This method initializes jcVrstaPomagala
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getJcVrstaPomagala() 
	{
		if (jcVrstaPomagala == null) 
		{
			jcVrstaPomagala = new javax.swing.JComboBox();
			jcVrstaPomagala.setPreferredSize(new java.awt.Dimension(121, 25));
			jcVrstaPomagala.setMinimumSize(new java.awt.Dimension(121, 25));
			jcVrstaPomagala.setFont(arial12);
			jcVrstaPomagala
					.setToolTipText("ovisno o vrsti pomagala za koje izdajete raèun, odaberite opciju, oèna, ortopedska ili popravak");
			try {
				ArrayList<VrstaPomagalaVO> lista = (ArrayList<VrstaPomagalaVO>) DAOFactory.getInstance()
						.getVrstePomagala().findAll(null);

				if (lista != null) 
				{
					int ls = lista.size();
					for (int i = 0; i < ls; i++)
						jcVrstaPomagala.addItem(lista.get(i));
				}

				postaviVrstePomagalaNaPocetnuPoziciju();
				lista = null;
			} 
			catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod povlacenja popisa vrsta pomagala", e);
				alert("problem pri kreiranju forme za unos racuna. Kontaktirajte administratora");
			}
		}
		
		return jcVrstaPomagala;
	}

	/**
	 * This method initializes jcRobaIsporucena
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcRobaIsporucena() {
		if (jcRobaIsporucena == null) {
			jcRobaIsporucena = new javax.swing.JCheckBox();
			jcRobaIsporucena.setText("isporu\u010deno");
			jcRobaIsporucena
					.setToolTipText("ako stranka još nije primila robu, iskljuèite ovu opciju");
			jcRobaIsporucena.setFont(arial);
			jcRobaIsporucena.setSelected(true);
		}
		return jcRobaIsporucena;
	}

	public void datumIzmjenjen(JDateChooser pozivatelj) {
		if (pozivatelj == this.datumIsporuke) {
			// ako je datum isporuke u buducnosti znaci da roba nije isporucena
			// i iskljuciti cemo automatski jcRobaIsporucena
			Calendar c = Calendar.getInstance();
			if (c.before(this.datumIsporuke.getDatum()))
				this.jcRobaIsporucena.setSelected(false);
		}

	}

	private JLabel getJlSifraAktivnost() {
		if (jlSifraAktivnost == null) {
			jlSifraAktivnost = new JLabel();
			jlSifraAktivnost.setText("\u0160ifra aktivnosti ZZR: ");
		}
		return jlSifraAktivnost;
	}

	private JTextField getJtSifraAktivnosti() {
		if (jtSifraAktivnosti == null) {
			jtSifraAktivnosti = new JTextField();
			jtSifraAktivnosti.setPreferredSize(new Dimension(100, 21));
			jtSifraAktivnosti.setText("A6900059");
			jtSifraAktivnosti.setMinimumSize(new Dimension(100, 21));
			jtSifraAktivnosti
					.setToolTipText("\u0161ifra aktivnosti za hzzo/hzzo zzr");
		}
		return jtSifraAktivnosti;
	}

	private JLabel getJlBrojPotvrde() 
	{
		if (jlBrojPotvrde == null) {
			jlBrojPotvrde = new JLabel();
			jlBrojPotvrde.setText("Broj potvrde: ");
		}
		return jlBrojPotvrde;
	}

	private JTextField getJtBrojPotvrdeLijecnik() 
	{
		if (jtBrojPotvrdeLijecnik == null) 
		{
			jtBrojPotvrdeLijecnik = new JTextField();
			jtBrojPotvrdeLijecnik.setPreferredSize(new Dimension(33, 24));
			jtBrojPotvrdeLijecnik
					.setToolTipText("broj potvrde lijeènika, moze biti 13 ili 14 znamenki, 14.zn. je kontrolna");
			jtBrojPotvrdeLijecnik.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent evt) {
					jtBrojPotvrdeLijecnikFocusLost(evt);
				}
			});
		}
		return jtBrojPotvrdeLijecnik;
	}

	private void jtBrojPotvrdeLijecnikFocusLost(FocusEvent evt) {
		jtBrojIskaznice2.setBackground(Color.white);
		// System.out.println("jtBrojPotvrdeLijecnik.focusLost, event="+evt);
		// TODO provjeriti kontrolni broj od potvrde, ne treba, spremanje racuna
		// ce ju blokirati
		String brpotv = jtBrojPotvrdeLijecnik.getText().trim();

		// samo ako ima 13 znakova, racunamo 14. kontrolni
		if (brpotv.length() == 13) {
			int kontrolni = Util.izracunajKontrolniBrojPotvrdeLijecnika(brpotv);
			jtBrojPotvrdeLijecnik.setText(brpotv + kontrolni);
		}
		else
			if (brpotv.length()==14)
			{
				if (!Util.brojPotvrdeLijecnikaIspravna(brpotv))
				{
					GUI.odradiUpozorenjeNaElementu(jtBrojPotvrdeLijecnik, "Broj potvrde lijecnika nije ispravan!");
				}
			}
	}// jtBrojPotvrdeLijecnikFocusLost

	protected void brojIskaznice2KeyTyped(KeyEvent ke) {
		Thread t = new Thread() {
			public void run() {
				try {
					sleep(20);
				} catch (InterruptedException inte) {
					return;
				}
				if (brojIskaznice2FocusLost(null, true)) {
					jtLijecnik.requestFocusInWindow();
				}
			}
		};
		
		SwingUtilities.invokeLater(t);
	}

	/**
	 * vraca true ako je pokrenio pretrazivanje
	 * 
	 * @param arg0
	 * @return
	 */
	protected boolean brojIskaznice2FocusLost(
			FocusEvent arg0,
			boolean izKeyTyped) 
	{
		String broj = this.jtBrojIskaznice2.getText().trim();
		String flid = this.jtBrojIskaznice1.getText().trim();
		boolean ispravanFlid = flid.length() == 2 || flid.length() == 3;

		VrstaUpita vrsta = null;

		if ((broj.length() == 11 && KontrolneZnamenkeUtils.ispravanOIB(broj)))
			vrsta = VrstaUpita.OIB;
		else if (!izKeyTyped && broj.length() == 11)
			alert("Ako pokušavate upisati OIB - nije ispravan!");
		else if (broj.length() == 9 && KontrolneZnamenkeUtils.ispravanMBO(broj))
			vrsta = VrstaUpita.MBO;
		else if (!izKeyTyped && broj.length() == 9)
			alert("Ako pokušavate upisati MBO - nije ispravan!");
		else if (!izKeyTyped
				&& (broj.length() == 8 || broj.length() == 7 || broj.length() == 6)
				&& ispravanFlid)
			vrsta = VrstaUpita.FLIDID;

		// ako je korisnik unio OIB na drugom mjestu, treba otiæi na HZZO-ove
		// stranice i probati vidjeti
		// može li se dobiti nazad
		if (vrsta != null) {

			if (vrsta == VrstaUpita.FLIDID)
				broj = flid + "/" + broj;

			final VrstaUpita vr = vrsta;
			final String br = broj;
			Thread t = new Thread() {
				public void run() {
					setPriority(Thread.MIN_PRIORITY);
					setName("Skupljac FLID-ID od HZZO-a");
					String staro = null;
					Boolean proslo = null;
					String stariTT = null;
					try {
						jtBrojIskaznice2.setBackground(Color.yellow);
						jtBrojIskaznice1.setBackground(Color.yellow);
						staro = jtBrojIskaznice1.getText();
						stariTT = jtBrojIskaznice2.getToolTipText();
						jtBrojIskaznice1.setText(">>>");
						proslo = false;
						String[] rez = HZZOFetchUtil.nadjiFlidIdDzo( br, vr, jtBrojIskaznice1, jtBrojIskaznice2 );
						if (rez == null || rez.length != 3)
							return;

						String flidId = rez[0];
						String dzo = rez[1];
						String vrijediDo = rez[2];

						if (flidId != null) {
							String[] flid = flidId.split("/");
							if (flid == null || flid.length != 2)
								return;
							try {
								Long.parseLong(flid[1]);
							} catch (NumberFormatException nfe) {
								if (vr == VrstaUpita.FLIDID) {
									alert("Broj kartice nije ispravan!");
									jtBrojPoliceDopunsko.setText("");
									postaviOsnovnoOsiguranje(true);
								}

								return;
							}
							// if ()
							jtBrojIskaznice1.setText(flid[0]);
							String kartica = flid[1] != null ? flid[1].trim()
									: null;

							if (kartica == null)
								return;

							if (kartica.length() == 7)
								kartica = "0" + kartica;
							else if (kartica.length() == 6)
								kartica = "00" + kartica;
							else if (kartica.length() == 5)
								kartica = "000" + kartica;
							else if (kartica.length() == 4)
								kartica = "0000" + kartica;
							else if (kartica.length() == 3)
								kartica = "00000" + kartica;

							jtBrojIskaznice2.setText(kartica);

							proslo = true;
							HZZOFetchUtil.povuceniBroj = kartica;
						}// if
						if (dzo != null) {

							dzo = dzo.trim();
							if (dzo.length() == 7)
								dzo = "0" + dzo;
							else if (dzo.length() == 6)
								dzo = "00" + dzo;
							else if (dzo.length() == 5)
								dzo = "000" + dzo;
							else if (dzo.length() == 4)
								dzo = "0000" + dzo;

							postaviOsnovnoOsiguranje(dzo.length() != 8);

							jtBrojPoliceDopunsko.setText(dzo);
							
							if ( !"".equals(dzo) )
							 GUI.odradiUpozorenjeNaElementu(jtBrojPoliceDopunsko, "Automatski smo izmjenili broj police dopunskog osiguranja!", Color.yellow);

							if (vrijediDo != null && vrijediDo.length() > 9)
								jtBrojPoliceDopunsko
										.setToolTipText("Vrijedi do: "
												+ vrijediDo);
							else
								jtBrojPoliceDopunsko.setToolTipText("");
							// proslo=true;
						}// if
						else {
							jtBrojPoliceDopunsko.setText("");
							postaviOsnovnoOsiguranje(true);
						}
					} finally {
						if (proslo != null && !proslo && staro != null)
							jtBrojIskaznice1.setText(staro);
						jtBrojIskaznice2.setToolTipText(stariTT);
						jtBrojIskaznice2.setBackground(Color.WHITE);
						jtBrojIskaznice1.setBackground(Color.WHITE);
					}
				}// run
			};

			// SwingUtilities.invokeLater(t);
			t.start();
			return true;
		} //
		return false;
	} // brojIskaznice2FocusLost



	
	

	private JLabel getJlSifraDopunskeAkt() {
		if (jlSifraDopunskeAkt == null) {
			jlSifraDopunskeAkt = new JLabel();
			jlSifraDopunskeAkt.setText("\u0160ifra dop. aktivnosti: ");
		}
		return jlSifraDopunskeAkt;
	}

	private JTextField getJtSifraAktivnostiDop() {
		if (jtSifraAktivnostiDop == null) {
			jtSifraAktivnostiDop = new JTextField();
			jtSifraAktivnostiDop.setHorizontalAlignment(SwingConstants.LEFT);
			jtSifraAktivnostiDop.setText("A6900737");
			jtSifraAktivnostiDop.setMinimumSize(new Dimension(100, 21));
			jtSifraAktivnostiDop.setPreferredSize(new Dimension(100, 21));
			jtSifraAktivnostiDop
					.setToolTipText("Ako je raèun za dopunsko osiguranje, tu navodite šifru aktivnosti za dop. osiguranje");
		}
		return jtSifraAktivnostiDop;
	}

	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		if (pozivatelj == this.datumIsporuke) {
			// ako je datum isporuke u buducnosti znaci da roba nije isporucena
			// i iskljuciti cemo automatski jcRobaIsporucena
			Calendar c = Calendar.getInstance();
			if (c.before(this.datumIsporuke.getDatum()))
				this.jcRobaIsporucena.setSelected(false);
		}
	}
	private JPanel getJpBrojBolesnickogL() {
		if (jpBrojBolesnickogL == null) {
			jpBrojBolesnickogL = new JPanel();
			 
			jpBrojBolesnickogL.setToolTipText("broj bolesni\u010Dkog lista");
			jpBrojBolesnickogL.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			jpBrojBolesnickogL.add(getJtBrojInoLista1());
			jpBrojBolesnickogL.add(getJtBrojInoLista2());
			jpBrojBolesnickogL.setMinimumSize(new Dimension(172, 25));
			jpBrojBolesnickogL.setPreferredSize(new Dimension(200, 25));
		}
		return jpBrojBolesnickogL;
	}
	private JPanel getJpBrojPotvrdeHzzo() {
		if (jpBrojPotvrdeHzzo == null) {
			jpBrojPotvrdeHzzo = new JPanel();
			jpBrojPotvrdeHzzo.setToolTipText("broj potvrde HZZO-a");
			FlowLayout flowLayout = (FlowLayout) jpBrojPotvrdeHzzo.getLayout();
			flowLayout.setVgap(1);
			flowLayout.setHgap(2);
			flowLayout.setAlignment(FlowLayout.LEFT);
			jpBrojPotvrdeHzzo.add(getJtBrojPotvrde1());
			jpBrojPotvrdeHzzo.add(getJtBrojPotvrde2());
		}
		return jpBrojPotvrdeHzzo;
	}
	private JPanel getJpBrojOsiguranjaKlijenta() {
		if (jpBrojOsiguranjaKlijenta == null) {
			jpBrojOsiguranjaKlijenta = new JPanel();
			jpBrojOsiguranjaKlijenta.setToolTipText("broj osiguranja klijenta");
			FlowLayout flowLayout = (FlowLayout) jpBrojOsiguranjaKlijenta.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			flowLayout.setVgap(0);
			flowLayout.setHgap(2);
			jpBrojOsiguranjaKlijenta.add(getJtBrojIskaznice1());
			jpBrojOsiguranjaKlijenta.add(getJtBrojIskaznice2());
		}
		return jpBrojOsiguranjaKlijenta;
	}
} 
