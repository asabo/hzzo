package biz.sunce.optika;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingx.JXTable;

import com.ansa.util.ZipUtil;
import com.ansa.util.SimpleEncryptUtils;
import com.ansa.util.beans.ActivationBean;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.install.Installer;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LogiranjeVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.hzzo.HzzoIzvjescePanel;
import biz.sunce.optika.hzzo.HzzoKreiranjeObracuna;
import biz.sunce.optika.hzzo.HzzoPostojeciRacuniPanel;
import biz.sunce.optika.hzzo.HzzoRacunPanel;
import biz.sunce.optika.hzzo.HzzoStatistikaArtikliPanel;
import biz.sunce.optika.hzzo.PostojeciHzzoObracuniPanel;
import biz.sunce.optika.net.SynchEngine;
import biz.sunce.optika.zakrpe.IspraviCijenuZaRevin;
import biz.sunce.optika.zakrpe.Zakrpe;
import biz.sunce.util.GUI;
import biz.sunce.util.HtmlPrintParser;
 
import biz.sunce.util.beans.PostavkeBean;

 


public final class GlavniFrame extends JFrame implements SlusacModelaTablice {
	private static final String DIREKTORIJ_ZA_KREIRANJE_DISKETE = "direktorij_za_kreiranje_diskete";
	private static final String WORKING_ROOT = ".opticar";
	public static final String ODABRANI_PRINTER = "odabrani_printer";
	
	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private static final long serialVersionUID = 2048481288977710850L;
	public static final byte[] SOFTWARE_VERSION = { 1, 0, 0, 4 };
	public static final byte[] PROTOCOL_VERSION = { 1, 0, 0, 0 };
	private static String DAO_DB_ADR = "dao_db_adr";

	static String workingHomeLocation = null;

	private javax.swing.JPanel jcGlavniContentPane = null;

	private javax.swing.JMenuBar jmMenu = null;
	private javax.swing.JMenu jMenu = null;
	private javax.swing.JMenu jmUredi = null;
	private javax.swing.JMenuItem jmPostavke = null;
	private javax.swing.JMenuItem jmIzlaz = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private JXTable jtPodaci = null;
	private javax.swing.JMenu jmKlijenti = null;
	private javax.swing.JMenuItem jmKarticaKlijenta = null;
	TableModel model = null;
	final JFrame ja = this;
	// 12.05.05.
	private static int sifDjelatnika = biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA;

	private javax.swing.JMenu jmObveze = null;
	private static GlavniFrame instanca = null;
	private static LogiranjeVO logiranjeVO = null;

	private javax.swing.JMenuItem jmNaruceniPregledi = null;

	public static final int getSifDjelatnika() 
	{
		if (sifDjelatnika == biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA) 
		{
			LogiranjeFrame lf = new LogiranjeFrame();
			lf.setGlavni(instanca);
			lf.setVisible(true);
			
			GUI.centrirajFrame(lf);	
			 
			try {
				synchronized (instanca) {					
					instanca.wait();
				}
			} catch (InterruptedException e) {
				sifDjelatnika = biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA;
			}
		}// if
		
		return sifDjelatnika;
	}// getSifDjelatnika

	public static final DjelatnikVO getDjelatnik() {
		int sfd = getSifDjelatnika();
		DjelatnikVO dvo = null;

		if (sfd != biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA) {
			try {
				dvo = (DjelatnikVO) DAOFactory.getInstance().getDjelatnici()
						.read(Integer.valueOf(sfd));
			} catch (SQLException ex) {
				Logger.fatal("SQL iznimka kod GlavniFrame.getDjelatnik()", ex);
				dvo = null;
			}
		}

		return dvo;
	}// getDjelatnik

	public static final String getCharEncoding() {
		return System.getProperty("file.encoding");
	}

	public static final String getJavaVersion() {
		return System.getProperty("java.vm.version");
	}

	public static final String getOS() {
		return System.getProperty("os.name") + ":"
				+ System.getProperty("os.version");
	}

	public static final String getUserLanguage() {
		return System.getProperty("user.name") + ":"
				+ System.getProperty("user.language");
	}

	// provjerava jeli verzija softvera jednaka ulaznoj
	private static boolean provjeriVerzijuSoftvera(byte[] ulaz) {

		if (ulaz == null || ulaz.length != SOFTWARE_VERSION.length)
			return false;

		for (int i = 0; i < SOFTWARE_VERSION.length; i++)
			if (SOFTWARE_VERSION[i] != ulaz[i])
				return false;

		return true;
	}// provjeriVerzijuSoftvera

	public static void setSifDjelatnika(int sifra) {
		sifDjelatnika = sifra;
	}

	private javax.swing.JMenuItem jmBackup = null;
	private javax.swing.JMenuItem jmPovratPodataka = null;
	private javax.swing.JMenuItem jmLijecnici = null;
	private javax.swing.JMenuItem jmOperateri = null;
	private javax.swing.JMenuItem jmPredlosci = null;
	private javax.swing.JMenuItem jmAktivacija = null;

	public static GlavniFrame getInstanca() {
		return instanca;
	}

	DAOFactory dfo = null;

	private javax.swing.JMenuItem jmTrenutneObveze = null;
	private javax.swing.JMenuItem jmAzuriranjePodataka = null;
	private javax.swing.JMenuItem jmPorukeSustava = null;
	private javax.swing.JMenu jmHzzo = null;
	private javax.swing.JMenuItem jmiKreiranjeHzzoRacuna = null;
	private javax.swing.JMenuItem jmPostojeciRacuni = null;
	private javax.swing.JMenuItem jmKreiranjeDiskete = null;
	private javax.swing.JMenuItem jmKreiraniObracuni = null;

	private javax.swing.JMenuItem jmPomagala = null;
	private javax.swing.JMenuItem jmPopisObracuna = null;

	private javax.swing.JMenuItem jmStatistika = null;
	private javax.swing.JMenuItem jmProizvodjaci = null;
	private javax.swing.JMenuItem jmHzzoIzvjesce = null;
	private javax.swing.JMenuItem jmTransakcije = null;
	private javax.swing.JMenuItem jmPoslanePoruke = null;
	private javax.swing.JMenuItem jmBackupPodatakaUDatoteku = null;
	private javax.swing.JMenu jmSinkronizacija = null;
	private javax.swing.JMenuItem jmPovratIzDatoteke = null;
	static String[] parametri;

	public static void main(String[] args) {
		boolean problemi = false;
		java.sql.Connection con = null;

		parametri = args;
		
		Font font = new Font("Arial", Font.PLAIN, 12);
		Font stari=(Font) UIManager.get("Label.font");
		UIManager.put("Label.font", font==null?stari:font);

		String adr;
		if ((adr = adresaHomeFoldera()) != null) {
			File f = new File(adr);
			if (!f.exists())
				try {
					f.mkdir();
				} catch (Exception e) {
					System.err.println("Ne možemo kreirati ishodišni folder: "
							+ f);
					Logger.fatal("Ne možemo kreirati ishodišni folder: " + f, e);
					alert("Ne može se kreirati ishodišna mapa: " + f);
				}

			setWorkingHomeLocation(adr);
		}

		// prvo provjeri dali postoji baza uopæe, pa instaliraj ako je nema
		Installer.instalirajBazu(getWorkingHomeLocation());

		try {
			DAOFactory.getInstance();
			con = DAOFactory.getConnection();

			if (parametri.length > 0 && parametri[0] != null
					&& parametri[0].trim().equals("sva_pomagala"))
				setKoristiSvaPomagala(true);

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err
					.println("Greška pri pokušaju spajanja na podatkovni podsustav: "
							+ e);
			problemi = true;
		} finally {
			if (con != null)
				try {
					DAOFactory.freeConnection(con);
				} 
			    catch (SQLException sqle) {
				}
		}

		instanca = new GlavniFrame();
		
		//getSifDjelatnika();
		
		if (instanca != null)
			instanca.setVisible(true);
		else
			System.exit(-1);

		if (problemi) {
			JOptionPane
					.showMessageDialog(
							instanca,
							"Program ne može pristupiti podacima! Molimo provjerite jeli još jedan program pokrenut.\n Ako nije - kontaktirajte administratora sustava",
							"Kritièno!", JOptionPane.WARNING_MESSAGE);
			System.exit(-1);
		} else {

			Zakrpe zakrpa = new Zakrpe();
			zakrpa.zakrpaj();
			
			getPublicKey();
			
			instanca.srediMenuStavkeSObziromNaPravaKorisnika();
			
		}// else

		registrirajKontrolneTipke();
      
		Thread.currentThread().setName("Sunce*HZZO - ");
		
		Thread t = new Thread()
		{
			public void run()
			{
				setPriority(Thread.MIN_PRIORITY);
				yield();
				getSifDjelatnika();
			}
		};
		
		t.start();
		
	}// main

	@SuppressWarnings("serial")
	private static void registrirajKontrolneTipke() 
	{	
		JRootPane panel = instanca.getRootPane();
		panel.getActionMap().put("akcije", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dogadjajPokrenut(e);
			}
		});

		InputMap inputMap = panel.getInputMap();
		KeyStroke controlA = KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK);

		inputMap.put(controlA, "akcije");

	}

	private static void dogadjajPokrenut(ActionEvent e) {
		IspraviCijenuZaRevin isp = new IspraviCijenuZaRevin();
		int rez = isp.zakrpaj();
		alert("Ukupno ispravljeno cijena za Revin: " + (rez - 1));
	}

	/*
	 * gleda u ulaznim parametrima jeli možda neki poèinje sa -w i vraæa ga kao
	 * adresu home foldera, inaèe sa null oznaèava da nema posebno definirane
	 * adrese home foldera
	 */
	private static String adresaHomeFoldera() {
		if (parametri != null)
			for (String par : parametri) {
				if (par.startsWith("-w") && par.length() > 2) {
					return par.substring(2);
				}
			}
		return null;
	}

	public static final String getDirektorijZaPohranuObracuna() {
		String direktorij = PostavkeBean.getKorisnickaPostavka(
				DIREKTORIJ_ZA_KREIRANJE_DISKETE, "A:");
		return direktorij;
	}

	public static final void setDirektorijZaPohranuObracuna(String dir) {
		PostavkeBean.setPostavka(DIREKTORIJ_ZA_KREIRANJE_DISKETE, dir, true);
	}

	/**
	 * This is the default constructor
	 */
	private GlavniFrame() {
		super();
		initialize();
	}

	public static final void alert(String poruka) {
		JOptionPane.showMessageDialog(instanca, poruka, "Upozorenje!",
				JOptionPane.WARNING_MESSAGE);
	}

	public static final void info(String poruka) {
		JOptionPane.showMessageDialog(instanca, poruka, "Obavijest",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setJMenuBar(getJmMenu());
		
		this.setResizable(false);
		this.setVisible(true);
		this.setName("glavni");
		// this.setIconImage(
		// (Image)PictureUtil.vratiKaoBufferedImage(getClass().getClassLoader().getResourceAsStream("biz/sunce/obrasci/OptoClub.jpg")));

		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				izlaz();
			}
		});
		// this.menu=new JMenuBar();
		
		
		Thread t = new Thread ()
		{
		 public void run()
		 {
			 PostavkeBean postavke = new PostavkeBean();
			 setTitle(postavke.getTvrtkaNaziv() + " "
						+ postavke.getMjestoRada());
		
			setContentPane(new DobroDosliPanel());
			ImageIcon ikona = getImageIcon();
			 if (ikona != null)
			  setIconImage(ikona.getImage());
		 }
			
		};
		
		SwingUtilities.invokeLater( t );
	
		this.centriraj();
	}

	private static ImageIcon icon = null;

	public static final ImageIcon getImageIcon() {
		if (icon == null) {
			icon = loadIcon("sunce-hzzo.png");
		}
		return icon;
	}

	private static ImageIcon loadIcon(String strPath) {
		URL imgURL = GlavniFrame.class.getResource(strPath);

		if (imgURL != null)
			return new ImageIcon(imgURL);
		else
			return null;
	}

	private void centriraj() {
		biz.sunce.util.GUI.centrirajFrame(this);
	}// centriraj

	/*
	 * private static void ucitajCijene(){ String rbr=null;
	 * 
	 * try{ BufferedReader r = new java.io.BufferedReader(new
	 * java.io.InputStreamReader( new FileInputStream(new
	 * File("d:/podaci.cvs")))); String line;
	 * 
	 * Hashtable<String, PomagaloVO> pomagala=new Hashtable(1500);
	 * 
	 * PomagaloVO pvo=null;
	 * 
	 * while ((line = r.readLine()) != null) { String[] dta=line.split(";"); if
	 * (dta.length!=25) continue;
	 * 
	 * rbr=dta[0]; String sifra = dta[1]; String strCijena = dta[23]; String
	 * pdvStr = dta[22];
	 * System.out.println(sifra+": pdv:"+pdvStr+" cijena: "+strCijena
	 * +" bp:"+dta[24]+" len:"+dta.length); pvo=new PomagaloVO();
	 * 
	 * strCijena=strCijena.replaceFirst("\\.", "").replaceFirst(",", ".");
	 * Double cijena=null;
	 * 
	 * try{ cijena=Double.valueOf(strCijena); } catch(NumberFormatException
	 * nfe){ continue; } int pdvSkupina=0; pdvStr=pdvStr.replaceFirst("\\%",
	 * ""); int pdv=0; try{ pdv=Integer.valueOf(pdvStr); }
	 * catch(NumberFormatException nfe){ continue; }
	 * 
	 * switch(pdv){ case 5: pdvSkupina=12;break; case 25: pdvSkupina=1; break;
	 * default: pdvSkupina=0; }
	 * 
	 * if (pdvSkupina==0) System.out.println("nema pdv skupine: "+pdvStr);
	 * 
	 * pvo.setSifraArtikla(sifra); pvo.setCijenaSPDVom( Integer.valueOf((int)
	 * (cijena.doubleValue()*100.0d)));
	 * pvo.setPoreznaSkupina(Integer.valueOf(pdvSkupina));
	 * 
	 * pomagala.put(sifra, pvo); }
	 * 
	 * FileOutputStream fos=new FileOutputStream(new File("d:/cijene.dta"));
	 * 
	 * ObjectOutputStream oos=new ObjectOutputStream(fos);
	 * oos.writeObject(pomagala); oos.close();
	 * 
	 * } catch(Exception e){ e.printStackTrace();
	 * System.out.println("RBR: "+rbr+"E: "+e); } }
	 */

	/**
	 * This method initializes jcGlavniContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJcGlavniContentPane() {
		if (jcGlavniContentPane == null) {
			jcGlavniContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			consGridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints1.weighty = 1.0;
			consGridBagConstraints1.weightx = 1.0;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			jcGlavniContentPane.setLayout(new java.awt.GridBagLayout());
			jcGlavniContentPane.add(getJScrollPane(), consGridBagConstraints1);
		}
		return jcGlavniContentPane;
	}

	/**
	 * This method initializes jmMenu
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private javax.swing.JMenuBar getJmMenu() {
		if (jmMenu == null) {
			jmMenu = new javax.swing.JMenuBar();
			jmMenu.add(getJmDatoteka());
			jmMenu.add(getJmUredi());
			jmMenu.add(getJmKlijenti());
			jmMenu.add(getJmObveze());
			jmMenu.add(getJmHzzo());
		}
		return jmMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJmDatoteka() {
		if (jMenu == null) {
			jMenu = new javax.swing.JMenu();
			jMenu.add(getJmPostavke());
			jMenu.add(getJmBackup());
			jMenu.add(getJmPovratPodataka());
			jMenu.add(getJmAktivacija());
			jMenu.add(getJmPorukeSustava());
			jMenu.add(getJmTransakcije());
			jMenu.add(getJmPoslanePoruke());
			jMenu.add(getJmIzlaz());
			jMenu.add(getJmSinkronizacija());
			jMenu.setText("Datoteka");
		}
		return jMenu;
	}

	/**
	 * This method initializes jmUredi
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJmUredi() {
		if (jmUredi == null) {
			jmUredi = new javax.swing.JMenu();
			jmUredi.add(getJmLijecnici());
			jmUredi.add(getJmOperateri());
			jmUredi.add(getJmPredlosci());
			jmUredi.add(getJmPomagala());
			jmUredi.add(getJmProizvodjaci());
			jmUredi.setText("Uredi");
		}
		return jmUredi;
	}

	/**
	 * This method initializes jmPostavke
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPostavke() {
		if (jmPostavke == null) {
			jmPostavke = new javax.swing.JMenuItem();
			jmPostavke.setText("postavke");
			jmPostavke
					.setToolTipText("ovdje postavljate temeljne postavke Vašeg radnog okruženja");
			jmPostavke.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PostavkeFrame pf = new PostavkeFrame();
					pf.pack();
					pf.show();
				}
			});
		}
		return jmPostavke;
	}

	/**
	 * This method initializes jmIzlaz
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmIzlaz() {
		if (jmIzlaz == null) {
			jmIzlaz = new javax.swing.JMenuItem();
			jmIzlaz.setText("Izlaz");
			jmIzlaz.setToolTipText("Izlaz iz aplikacije");
			jmIzlaz.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					GlavniFrame.izlaz();
				}
			});
		}
		return jmIzlaz;
	}

	public static void izlaz() {
		// prije izlaska zapisati cinjenicu da se korisnik odlogirao
		LogiranjeVO logiranjeVO2 = getLogiranjeVO();
		if (logiranjeVO2 != null) {
			try {
				DAOFactory.getInstance().getLogiranja()
						.update(logiranjeVO2);
			} catch (SQLException e) {
				Logger.fatal(
						"Problem pri zapisivanju cinjenice odlogiravanja korisnika id: "+logiranjeVO2.getSifDjelatnika(),
						e);
			}
		}// if
			// 30.04.05 - asabo - obavezno DAOFactory-ju reci da pozatvara veze
			// prema bazi podataka
		DAOFactory.getInstance().destroy();
		System.exit(-1);
	}

	/**
	 * This method initializes jtPodaci
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getJtPodaci() {
		if (jtPodaci == null) {
			jtPodaci = new JXTable();
			// TODO ovdje mjenjas tablicu sa podacima koje hoces vidjeti vani na
			// ekranu
			this.model = new TableModel(
					DAOFactory.getInstance().getLijecnici(), jtPodaci);
			this.model.dodajSlusaca(this);
			this.jtPodaci.setModel(this.model);
		}
		return jtPodaci;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getJtPodaci());
			jScrollPane.setToolTipText("Vaše današnje obaveze");
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jmKlijenti
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJmKlijenti() {
		if (jmKlijenti == null) {
			jmKlijenti = new javax.swing.JMenu();
			jmKlijenti.add(getJmKarticaKlijenta());
			jmKlijenti.setText("Klijenti");
			jmKlijenti.setActionCommand("");
		}
		return jmKlijenti;
	}

	/**
	 * This method initializes jmKarticaKlijenta
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmKarticaKlijenta() {
		if (jmKarticaKlijenta == null) {
			jmKarticaKlijenta = new javax.swing.JMenuItem();
			jmKarticaKlijenta.setText("Kartica klijenata");
			jmKarticaKlijenta.setToolTipText("popis Vaših klijenata");
			jmKarticaKlijenta
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();

							KlijentiPanel kp = new KlijentiPanel();
							setContentPane(kp);
							pack();
						}
					});
		}
		return jmKarticaKlijenta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.opticar.vo.SlusacModelaTablice#redakOznacen(int,
	 * java.awt.event.MouseEvent, biz.sunce.opticar.vo.TableModel)
	 */
	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) {
		// ovako znamo koji imamo tableModel i koji ValueObject-i sjede unutra
		if (posiljatelj == this.model && event.getClickCount() == 2
				&& (this.model.getData().get(redak) instanceof KlijentVO)) {
			KlijentVO kvo = (KlijentVO) this.model.getData().get(redak);
			KlijentFrame kf = new KlijentFrame();
			kf.setOznaceni(kvo);
			kf.setVisible(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.sunce.opticar.vo.SlusacModelaTablice#redakIzmjenjen(int,
	 * javax.swing.event.TableModelEvent, biz.sunce.opticar.vo.TableModel)
	 */
	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel posiljatelj) {

	}

	/**
	 * This method initializes jmObveze
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJmObveze() {
		if (jmObveze == null) {
			jmObveze = new javax.swing.JMenu();
			jmObveze.add(getJmNaruceniPregledi());

			jmObveze.add(getJmTrenutneObveze());
			jmObveze.setText("Obveze");
			jmObveze.setToolTipText("pregledi, sastanci ... ");
		}
		return jmObveze;
	}

	/**
	 * This method initializes jmNaruceniPregledi
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmNaruceniPregledi() {
		if (jmNaruceniPregledi == null) {
			jmNaruceniPregledi = new javax.swing.JMenuItem();
			jmNaruceniPregledi.setText("Naruèeni pregledi");
			jmNaruceniPregledi.setToolTipText("kalendar trenutnih obaveza");
			jmNaruceniPregledi
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							ObvezePanel obv = new ObvezePanel(ja);

							setContentPane(obv);
							pack();
						}
					});
		}
		return jmNaruceniPregledi;
	}

	/**
	 * This method initializes jmBackup
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmBackup() {
		if (jmBackup == null) {
			jmBackup = new javax.swing.JMenuItem();
			jmBackup.setText("Backup podataka");
			jmBackup.setToolTipText("osigurajte svoje podatke sigurnosnom kopijom...");
			jmBackup.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setFileFilter(new FileFilter() {

						@Override
						public String getDescription() {
							return "ZIP datoteka";
						}

						@Override
						public boolean accept(File arg0) {
							String fname = arg0.getName().toLowerCase();
							return arg0.isDirectory() || fname.endsWith(".zip");
						}
					});
					jfc.setToolTipText("Molimo, odaberite datoteku u koju želite arhivirati svoje podatke");

					jfc.setSelectedFile(new File("backup.zip"));

					int rez = jfc.showDialog(GlavniFrame.getInstanca(),
							"Pohrani");

					if (rez == JFileChooser.APPROVE_OPTION) {

						String dir = getWorkingHomeLocation();

						try {
							DAOFactory.killFactory();

							try {
								Thread.sleep(200);
							} catch (InterruptedException inte) {
								return;
							}
							java.io.File rootDir = new java.io.File(dir);
							java.io.File dbLock = new java.io.File(rootDir,
									"opticardb/optika/db.lck");

							dbLock.delete();
							ZipUtil.zip(rootDir, jfc.getSelectedFile());
						} catch (IOException ioe) {
							alert("Iznimka pri radu sa datoteènim sustavom, provjerite poruke sustava!");
							Logger.fatal("Iznimka kod backupa podataka", ioe);
						} finally {
							DAOFactory.getInstance();
						}
					}// if

				}// actionPerformed backup podataka
			});
		}
		return jmBackup;
	}

	private javax.swing.JMenuItem getJmPovratPodataka() {
		if (jmPovratPodataka == null) {
			jmPovratPodataka = new javax.swing.JMenuItem();
			jmPovratPodataka.setText("Povrat backupiranih podataka");
			jmPovratPodataka
					.setToolTipText("povratite svoje podatke iz sigurnosne kopije...");
			jmPovratPodataka
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JFileChooser jfc = new JFileChooser();
							jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
							jfc.setFileFilter(new FileFilter() {

								@Override
								public String getDescription() {
									return "ZIP datoteka";
								}

								@Override
								public boolean accept(File arg0) {
									String fname = arg0.getName().toLowerCase();
									return fname.endsWith(".zip");
								}
							});

							jfc.setToolTipText("Molimo, odaberite datoteku iz koje želite povratiti svoje podatke");

							int rez = jfc.showDialog(GlavniFrame.getInstanca(),
									"Uèitaj");

							if (rez == JFileChooser.APPROVE_OPTION) {

								String dir = getWorkingHomeLocation();

								try {
									DAOFactory.killFactory();

									try {
										Thread.sleep(200);
									} catch (InterruptedException inte) {
										return;
									}
									java.io.File rootDir = new java.io.File(dir);

									ZipUtil.unzip(jfc.getSelectedFile(),
											rootDir);

									alert("Povrat podataka uspješno obavljen, morate ponovno pokrenuti aplikaciju!");
									System.exit(0);
								} catch (IOException ioe) {
									alert("Iznimka pri radu sa datoteènim sustavom, provjerite poruke sustava!");
									Logger.fatal(
											"Iznimka kod backupa podataka", ioe);
								} finally {

								}
							}// if

						}// actionPerformed backup podataka
					});
		}
		return jmPovratPodataka;
	}

	public final static String getWorkingHomeLocation() {

		if (workingHomeLocation == null) {
			String adr = PostavkeBean.getPostavkaSustava(DAO_DB_ADR, "");

			if (!adr.equals("")) {
				workingHomeLocation = adr;
				return workingHomeLocation;
			}

			Properties p = System.getProperties();

			String sep = p.getProperty("file.separator");
			String uHome = p.getProperty("user.home");

			workingHomeLocation = uHome + sep + WORKING_ROOT;
		}

		return workingHomeLocation;
	}// getWorkingHomeLocation

	public final static void setWorkingHomeLocation(String loc) {
		workingHomeLocation = loc;
	}// setWorkingHomeLocation

	/**
	 * This method initializes jmLijecnici
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmLijecnici() {
		if (jmLijecnici == null) {
			jmLijecnici = new javax.swing.JMenuItem();
			jmLijecnici.setText("Lijeènici");
			jmLijecnici
					.setToolTipText("ovdje uvedite popis svih lijeènika koji obavljaju preglede");
			jmLijecnici.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getContentPane().removeAll();
					DAOObjektPanel daop = new DAOObjektPanel();
					daop.setDAOObjekt(DAOFactory.getInstance().getLijecnici());
					daop.setSviElementiSeMoguBrisati(false);
					setContentPane(daop);
					pack();
				}
			});
		}
		return jmLijecnici;
	}

	/**
	 * This method initializes jmOperateri
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmOperateri() {
		if (jmOperateri == null) {
			jmOperateri = new javax.swing.JMenuItem();
			jmOperateri.setText("Djelatnici");
			jmOperateri
					.setToolTipText("ovdje uvedite popis svih djelatnika koji æe raditi u programu");
			jmOperateri.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (GlavniFrame.getDjelatnik() == null
							|| !GlavniFrame.getDjelatnik().getAdministrator()
									.booleanValue()) {
						JOptionPane.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Nema ovlasti izvršiti dotiènu akciju",
								"Obavijest", JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					getContentPane().removeAll();
					DAOObjektPanel daop = new DAOObjektPanel();
					daop.setDAOObjekt(DAOFactory.getInstance().getDjelatnici());
					daop.setSviElementiSeMoguBrisati(false);
					setContentPane(daop);
					pack();
				}
			});
		}
		return jmOperateri;
	}

	// vraca BufferedImage sliku sa izgledom zaglavlja svakog dokumenta koji ce
	// se ispisivati
	// procedura kreiranja slike kasnije ce se napraviti na ovaj ili onaj nacin
	public static final BufferedImage getZaglavljeDokumenata() {
		int sw = 550, sh = 55;
		BufferedImage slika = null;

		slika = HtmlPrintParser.ucitajSliku(getLokacijaLogoSlike());

		if (slika != null)
			return slika;
		else
			return HtmlPrintParser
					.ucitajSliku("biz/sunce/obrasci/OptoClub.jpg");

		/*
		 * BufferedImage slika=new
		 * BufferedImage(sw,sh,BufferedImage.TYPE_INT_RGB); Graphics2D
		 * g=(Graphics2D)slika.getGraphics();
		 * g.setRenderingHint(RenderingHints.KEY_ANTIALIASING
		 * ,RenderingHints.VALUE_ANTIALIAS_OFF);
		 * g.setRenderingHint(RenderingHints
		 * .KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		 * g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.
		 * VALUE_INTERPOLATION_BICUBIC); g.setBackground(Color.WHITE);
		 * 
		 * g.fillRect(0,0,sw,sh);
		 * 
		 * g.setColor(Color.black); g.setFont(new
		 * java.awt.Font("Arial",java.awt.Font.BOLD,25));
		 * g.drawString("OptoClub",10,27);
		 * 
		 * g.dispose();
		 * 
		 * return slika;
		 */
	}// getZaglavljeDokumenata

	private final static String fileSeparator = System
			.getProperty("file.separator");

	private static final String getLokacijaLogoSlike() {
		String sep = fileSeparator;
		return vratiKonfiguracijskiDirektorijKorisnika() + sep + "logo.jpg";
	}

	public static final void pohraniLogo(BufferedImage logo) {

		BufferedImage bufferedThumbnail = null;
		int visina = 45;
		if (logo.getHeight() > visina) {
			Image thumbnail = logo.getScaledInstance(-1, visina,
					Image.SCALE_SMOOTH);
			bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
					thumbnail.getHeight(null), BufferedImage.TYPE_INT_RGB);
			bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
		}
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(getLokacijaLogoSlike()));
			ImageIO.write(bufferedThumbnail == null ? logo : bufferedThumbnail,
					"png", fos);
		} catch (Exception e) {
			Logger.log(
					"Iznimka kod pohranjivanja logo slike u datoteèni sustav na lokaciji: "
							+ getLokacijaLogoSlike(), e);
		}
	}

	/**
	 * This method initializes jmPredlosci
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPredlosci() {
		if (jmPredlosci == null) {
			jmPredlosci = new javax.swing.JMenuItem();
			jmPredlosci.setText("Predlošci");
			jmPredlosci
					.setToolTipText("predlošci raznih poruka koje namjeravate slati klijentima, èestitke, pozivi i sl.");
			jmPredlosci.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getContentPane().removeAll();
					DAOObjektPanel daop = new DAOObjektPanel();
					daop.setDAOObjekt(DAOFactory.getInstance().getPredlosci());
					daop.setSviElementiSeMoguBrisati(false);
					setContentPane(daop);
					GlavniFrame.getInstanca().pack();
				}
			});
		}
		return jmPredlosci;
	}

	/**
	 * This method initializes jmAktivacija
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmAktivacija() {
		if (jmAktivacija == null) {
			jmAktivacija = new javax.swing.JMenuItem();
			jmAktivacija.setText("Aktivacija");
			jmAktivacija
					.setToolTipText("aktivirajte svoj proizvod kako biste mogli u potpunosti koristiti sustav");
			jmAktivacija.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					aktivirajSoftver();
				}
			});
		}
		return jmAktivacija;
	}

	// gdje ce lezati raznorazne konfiguracijske datoteke
	public final static String vratiKonfiguracijskiDirektorijKorisnika() {
 
		return getWorkingHomeLocation();
	}// vratiKonfiguracijskiDirektorijKorisnika

	static byte[] pubKey = null;

	// metoda vraca javni kljuc zapisan u konfiguracijskoj datoteci potreban za
	// asimetricno kriptiranje podataka namjenjenih transportu preko weba
	// i usput postavlja hzzo sifru isporucitelja
	public final static byte[] getPublicKey() {

		if (pubKey != null)
			return pubKey;

		byte[] podaci = null;
		Date d = null; 

		// String dbDir = biz.sunce.dao.DAOFactory.getDAODBFileLocation();

		String dir = getWorkingHomeLocation();

		FileInputStream fin = null;
		ObjectInputStream oins = null;
		if (dir != null)
			try {

				fin = new FileInputStream(dir + "/" + ".key");

				int komada = fin.available();

				try {
					oins = new ObjectInputStream(fin);
				} catch (ObjectStreamException stream) {
					// stari mode
					podaci = new byte[komada];
					fin.read(podaci, 0, komada);
					return podaci;
				}
				if (komada < 0 || komada > 65535) {
					Logger.log("Nelogièna situacija, komada iznosi " + komada
							+ " kod pokušaja uèitavanja public key datoteke...");
					return null;
				}

				int keySize = oins == null ? 0 : oins.readInt();
				int hzzoSize = oins == null ? 0 : oins.readInt();
				byte[] hzzo = new byte[hzzoSize];
				String hzzoSifIsporucitelja = null;

				podaci = null;
				if (fin != null) {
					podaci = new byte[keySize];
					oins.read(podaci, 0, keySize);
					oins.read(hzzo, 0, hzzoSize);
					hzzoSifIsporucitelja = new String(hzzo);
					hzzoSifIsporucitelja = SimpleEncryptUtils
							.xorMessage(hzzoSifIsporucitelja);
					PostavkeBean
							.setHzzoSifraIsporucitelja(hzzoSifIsporucitelja);

					PostavkeBean.setDatumValjanosti(null);

					if (oins.available() > 0) {
						int datValjS = oins.readInt();
						if (datValjS < 1024) {

							byte[] dvb = new byte[datValjS];
							oins.read(dvb, 0, datValjS);
							String datValj = new String(dvb);
							datValj = SimpleEncryptUtils.xorMessage(datValj);

							try {
								
								if (datValj!=null && !datValj.startsWith("kreirano"))
								{
								 d = datValj==null?null:DATE_FORMATTER.parse(datValj);
								 PostavkeBean.setDatumValjanosti(d);
								}
							} 
							catch (Exception e) {
								 Logger.warn("Problem kod parsiranja dat.valj: "+datValj,
								 e);
							}
						}
					}

				} else
					Logger.log("Nelogièna situacija, Input Stream je null, a komada="
							+ komada
							+ " kod pokušaja uèitavanja public key datoteke...");

			} catch (FileNotFoundException fnfe) {
				//Logger.fatal("Nema public key datoteke", fnfe);
				return null;
			} catch (IOException ioe) {
				Logger.fatal("IO iznimka kod èitanja public key datoteke", ioe);
				return null;
			} finally {
				try {
					if (fin != null)
						fin.close();
				} catch (IOException ioe) {
				}
			}
		else
			Logger.log("user dir je null kod èitanja public key-a ?!?");

		pubKey = podaci;
		
		if (isDatumValjanostiIstekao())
		{
			alert("Licenca za korištenje proizvoda je istekla! Kontaktirajte Vašeg dobavljaèa! "+org.apache.http.impl.cookie.DateUtils.formatDate(d) );								
		}
		
		return podaci;
	}// getPublicKey

	public final static boolean isDatumValjanostiIstekao() {
		Date d = PostavkeBean.getDatumValjanosti();
		if (d == null)
			return false;
		Date danas = new Date();
		return (d.before(danas));
	}

	// metoda se pokrece jednom u zivotnom vijeku softvera, prepisuje potrebne
	// stavke u postavke
	private void aktivirajSoftver() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setToolTipText("Odaberite datoteku sa aktivacijskim podacima...");
		FileFilter ff = new FileFilter() {

			@Override
			public String getDescription() {
				return "HZZO aktivacijska datoteka";
			}

			@Override
			public boolean accept(File f) {
				return (f.getName().endsWith(".akt"));
			}
		};
		jfc.setFileFilter(ff);
		int rez = jfc.showDialog(GlavniFrame.getInstanca(), "Uèitaj");

		if (rez == JFileChooser.APPROVE_OPTION) {
			File dat = null;
			FileInputStream ins = null;
			ObjectInputStream oins = null;
			FileOutputStream fos = null;

			try {
				dat = jfc.getSelectedFile();
				ins = new FileInputStream(dat);
				oins = new ObjectInputStream(ins);

				ActivationBean act = null;

				act = (ActivationBean) oins.readObject();

				Calendar datKreiranja = act.getDatumKreiranja();
				String hzzoSifra = act.getHzzoSifraIsporucitelja();
				String datumValjanosti = act.getDatumValjanosti();

				PostavkeBean
						.setPostavkaDB(
								PostavkeBean.TVRTKA_HZZO_SIFRA_ISPORUCITELJA,
								hzzoSifra);
				hzzoSifra = SimpleEncryptUtils.xorMessage(hzzoSifra);

				if (datKreiranja != null) {
					boolean l = datKreiranja.isLenient();
					datKreiranja.setLenient(true);
					datKreiranja.set(Calendar.DATE,
							datKreiranja.get(Calendar.DATE) + 20);
					datKreiranja.setLenient(l);
					Calendar danas = Calendar.getInstance();
					if (datKreiranja.before(danas)) {
						JOptionPane
								.showMessageDialog(
										this,
										"Aktivacijska datoteka nije dobra. Molimo Vas zatražite novu!",
										"upozorenje",
										JOptionPane.WARNING_MESSAGE);
						return;
					}
				}// if datKreiranja nije null

				PostavkeBean post = new PostavkeBean();

				post.setMjestoRada(act.getMjesto());
				post.setTvrtkaAdresa(act.getAdresa());
				post.setTvrtkaBanka(act.getBanka());
				post.setTvrtkaEmail(act.getEmail());
				post.setTvrtkaFax(act.getFax());
				post.setTvrtkaOIB(act.getMaticniBroj());
				post.setTvrtkaNaziv(act.getNazivTvrtke());
				post.setTvrtkaRacun(act.getBrojRacuna());
				post.setTvrtkaTelefon(act.getTelefon());
				post.setSifraTvrtke(act.getSifraTvrtke());
				post.setSifraPoslovnice(act.getSifraPoslovnice());

				// opceniti podaci potrebni za spajanje na web servis
				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SIFRA_KLIJENTA,
						"" + act.getSifraKorisnika());

				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SIFRA_POSLOVNICE,
						"" + act.getSifraPoslovnice());
				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SIFRA_TVRTKE, ""
						+ act.getSifraTvrtke());
				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SYNCH_USERNAME,
						"" + act.getUsername());
				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SYNCH_PASSWORD,
						"" + act.getPassword());

				post.saveData();
				String datValjOrig = datumValjanosti;

				// spremiti datoteku sa javnim kljucem...
				String dir = vratiKonfiguracijskiDirektorijKorisnika();
				fos = new FileOutputStream(dir + "/" + ".key");

				if (datumValjanosti == null)
					datumValjanosti = SimpleEncryptUtils.xorMessage("kreirano "
							+ System.currentTimeMillis());
				else
					datumValjanosti = SimpleEncryptUtils
							.xorMessage(datumValjanosti);

				byte[] publicKey = act.getPublicKey();
				byte[] hzzoBytes = hzzoSifra.getBytes();
				byte[] datBytes = datumValjanosti.getBytes();

				ObjectOutputStream oout = new ObjectOutputStream(fos);
				oout.writeInt(publicKey.length);
				oout.writeInt(hzzoBytes.length);
				oout.write(publicKey);
				oout.write(hzzoBytes);
				oout.writeInt(datBytes.length);
				oout.write(datBytes);
				oout.flush();
				oout.close();

				try {
					fos.close();
				} catch (IOException ioe) {
				} // sve i da pukne bitnije je da kljuc naseli u db direktoriju

				String wDir = getWorkingHomeLocation();

				// 03.07.06. -asabo- dodano pohranjivanje kljuca jos i na
				// lokaciju db root-a (tamo ce biti 'sigurniji' od havarije)
				// i imat cemo kljuc na dvije lokacije kako se ne bi dogodilo da
				// se izgubi kljuc uslijed havarije...
				fos = new FileOutputStream(wDir + "/" + ".key");

				oout = new ObjectOutputStream(fos);
				oout.writeInt(publicKey.length);
				oout.writeInt(hzzoBytes.length);
				oout.write(publicKey);
				oout.write(hzzoBytes);
				oout.writeInt(datBytes.length);
				oout.write(datBytes);
				oout.flush();
				oout.close();

				try {
					fos.close();
					fos = null;
				} catch (IOException ioe) {
				}

				// PostavkeBean.setPostavka("proizvod_aktiviran", "true",
				// false);
				PostavkeBean.setPostavkaDB("proizvod_aktiviran", "true");

				JOptionPane.showMessageDialog(this,
						"Program uspješno aktiviran! "+(datValjOrig!=null?" Datum valjanosti aktivacije: "+datValjOrig:""), "Obavijest",
						JOptionPane.INFORMATION_MESSAGE);

				// iskopcati stavku menija da se vise ne vidi...
				srediMenuStavkeSObziromNaPravaKorisnika();
			} catch (Exception e) {
				JOptionPane
						.showMessageDialog(
								this,
								"Aktivacija nije uspjela, kontaktirajte administratora sustava!",
								"Upozorenje", JOptionPane.WARNING_MESSAGE);
				Logger.fatal("Iznimka kod aktivacije proizvoda, datoteka: "
						+ dat, e);
			} finally {
				try {
					if (oins != null)
						oins.close();
					oins = null;
				} catch (Exception e) {
				}
				try {
					if (ins != null)
						ins.close();
					ins = null;
				} catch (Exception e) {
				}
				try {
					if (fos != null)
						fos.close();
					fos = null;
				} catch (Exception e) {
				}
				jfc = null;
				dat = null;
			}
		}// if
	}// aktivirajSoftver

	private void kreirajHzzoRacun() {
		kreirajHzzoRacun(null);
	}

	public void kreirajHzzoRacun(final RacunVO rvo) {
		// just in case
		if (!isImaPravoNaHzzo())
			return;

		Thread t = new Thread() {
			public void run() {
				busy();
				HzzoRacunPanel racun = new HzzoRacunPanel(rvo);
				repaint();
				yield();
				setContentPane(racun);
				GlavniFrame.getInstanca().pack();
				idle();
			}
		};
		
		SwingUtilities.invokeLater(t);

	}// kreirajHzzoRacun

	final static Cursor curDefault = Cursor.getDefaultCursor();
	final static Cursor curBusy = Cursor
			.getPredefinedCursor(Cursor.WAIT_CURSOR);

	public final void busy() {
		this.setCursor(curBusy);
		this.setEnabled(false);
	}

	public final void idle() {
		this.setCursor(curDefault);
		this.setEnabled(true);
	}

	public static final void glavniFrameBusy() {
		if (instanca != null) {
			instanca.setCursor(curBusy);
			instanca.setEnabled(false);
		}
	}

	public static final void glavniFrameIdle() {
		if (instanca != null) {
			instanca.setCursor(curDefault);
			instanca.setEnabled(true);
		}
	}

	private void kreirajPregledRacuna() {

		Thread t = new Thread() {
			public void run() {
				
				// just in case
				if (!isImaPravoNaHzzo())
					return;
				
				busy();

				HzzoPostojeciRacuniPanel racuni = new HzzoPostojeciRacuniPanel();

				yield();
				setContentPane(racuni);

				GlavniFrame.getInstanca().pack();
				idle();
			}
		};

		SwingUtilities.invokeLater(t);

	}// kreirajHzzoRacun

	public static final boolean isImaPravoNaHzzo() {
		return true;
		/*
		 * PostavkeBean pb = new PostavkeBean(); String postavka =
		 * PostavkeBean.getPostavkaDB(Konstante.HZZO_POSTAVKA, null); String p =
		 * "h" + pb.getSifraTvrtke() + "z"; if (postavka != null &&
		 * p.equals(postavka)) return true; return true;
		 */
	}

	static Boolean koristiSvaPomagala = null;

	public static final boolean isKoristiSvaPomagala() {
		if (koristiSvaPomagala == null) {
			String postavka = PostavkeBean.getPostavkaDB(
					Konstante.HZZO_POSTAVKA_SVA_POMAGALA, null);
			final String p = "koristi";
			if (postavka != null && p.equals(postavka))
				koristiSvaPomagala = Boolean.TRUE;
			else
				koristiSvaPomagala = Boolean.FALSE;
		}

		return koristiSvaPomagala;
	}

	public static final void setKoristiSvaPomagala(boolean koristi) {

		PostavkeBean.setPostavkaDB(Konstante.HZZO_POSTAVKA_SVA_POMAGALA,
				koristi ? "koristi" : "ne");
		koristiSvaPomagala = Boolean.valueOf(koristi);
	}

	public static final boolean sinkronizacijaPodataka() {
		return sinkronizacijaPodataka(null);
	}

	public static final boolean sinkronizacijaPodataka(
			PrikazivacSinkronizacijskihPoruka prikazivac) {
		boolean rez = true;
		String username = GlavniFrame.getUsernameZaSynch();
		String pass = GlavniFrame.getPasswordZaSynch();
		if (prikazivac != null)
			SynchEngine.dodajPrikazivacaPoruka(prikazivac);
		rez = SynchEngine.performSynchronization(null, null, username, pass);
		username = "odishf";
		pass = "asdf";
		return rez;
	}// sinkronizacijaPodataka

	public static final int getSifraKorisnikaZaSynch() {
		int post = PostavkeBean.getIntPostavkaDb(
				Konstante.POSTAVKE_SIFRA_KLIJENTA, -2);

		if (post == -2)
			post = PostavkeBean.getIntPostavkaSustava(
					Konstante.POSTAVKE_SIFRA_KLIJENTA, -1);
		if (post != -1)
			PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SIFRA_KLIJENTA, ""
					+ post);
		return post;
	}

	public static final int getSifraPoslovniceZaSynch() {
		int post = PostavkeBean.getIntPostavkaDb(
				Konstante.POSTAVKE_SIFRA_POSLOVNICE, -2);

		if (post == -2) {
			post = PostavkeBean.getIntPostavkaSustava(
					Konstante.POSTAVKE_SIFRA_POSLOVNICE, -1);
			if (post != -1)
				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SIFRA_POSLOVNICE,
						"" + post);
		}

		return post;
	}

	public static final int getSifraTvrtkeZaSynch() {
		int post = PostavkeBean.getIntPostavkaDb(
				Konstante.POSTAVKE_SIFRA_TVRTKE, -2);

		if (post == -2) {
			post = PostavkeBean.getIntPostavkaSustava(
					Konstante.POSTAVKE_SIFRA_TVRTKE, -1);
			if (post != -1)
				PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SIFRA_TVRTKE, ""
						+ post);
		}

		return post;
	}

	public static final String getUsernameZaSynch() {
		// idemo na to da sve postavke budu zapisane u DB, samo nuzno u
		// registrima
		String p = PostavkeBean.getPostavkaDB(
				Konstante.POSTAVKE_SYNCH_USERNAME, "");

		if (p.equals("")) {
			p = PostavkeBean.getPostavkaSustava(
					Konstante.POSTAVKE_SYNCH_USERNAME, "");
			PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SYNCH_USERNAME, p);
		}

		return p;
	}

	public static final String getPasswordZaSynch() {

		// idemo na to da sve postavke budu zapisane u DB, samo nuzno u
		// registrima
		String p = PostavkeBean.getPostavkaDB(
				Konstante.POSTAVKE_SYNCH_PASSWORD, "");

		if (p.equals("")) {
			p = PostavkeBean.getPostavkaSustava(
					Konstante.POSTAVKE_SYNCH_PASSWORD, "");
			PostavkeBean.setPostavkaDB(Konstante.POSTAVKE_SYNCH_PASSWORD, p);
		}

		return p;
	}

	public static final boolean isProizvodAktiviran() {

		if (parametri != null)
			for (String param : parametri) {
				if (param.equalsIgnoreCase("aktivacija"))
					return false;
			}

		if (isDatumValjanostiIstekao())
			return false;
		
		if (getPublicKey() == null)
			return false;

		// idemo na to da sve postavke budu zapisane u DB, samo nuzno u
		// registrima
		final String proiz_akt = "proizvod_aktiviran";
		String p = PostavkeBean.getPostavkaDB(proiz_akt, "");

		if (p.equals("")) {
			p = PostavkeBean.getPostavkaSustava(proiz_akt, "false");
			PostavkeBean.setPostavkaDB(proiz_akt, p);
		}

		return p != null && p.equals("true");
	}

	private void srediMenuStavkeSObziromNaPravaKorisnika() {
		boolean aktiviran = isProizvodAktiviran();

		// ako je softver aktiviran, nema potrebe dalje biti vidljiv
		if (aktiviran)
			this.getJmAktivacija().setVisible(false);

	}

	/**
	 * This method initializes jmTrenutneObveze
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmTrenutneObveze() {
		if (jmTrenutneObveze == null) {
			jmTrenutneObveze = new javax.swing.JMenuItem();
			jmTrenutneObveze.setText("Podsjetnik");
			jmTrenutneObveze
					.setToolTipText("podsjetnik, obveze, roðendani, novosti...");
			jmTrenutneObveze
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();
							DobroDosliPanel dd = new DobroDosliPanel();
							setContentPane(dd);
							GlavniFrame.getInstanca().pack();
							repaint();
						}
					});
		}
		return jmTrenutneObveze;
	}

	/**
	 * This method initializes jmAzuriranjePodataka
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmAzuriranjePodataka() {
		if (jmAzuriranjePodataka == null) {
			jmAzuriranjePodataka = new javax.swing.JMenuItem();
			jmAzuriranjePodataka.setText("Ažuriranje podataka");
			jmAzuriranjePodataka
					.setToolTipText("razmjena podataka sa sustavom, SMS ili mail poruke koje ste poslali, zahtjevi za èlanskim iskaznicama ili rezervnim dijelovima");
			jmAzuriranjePodataka
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							GlavniFrame.sinkronizacijaPodataka();
						}
					});
		}
		return jmAzuriranjePodataka;
	}

	/**
	 * This method initializes jmPorukeSustava
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPorukeSustava() {
		if (jmPorukeSustava == null) {
			jmPorukeSustava = new javax.swing.JMenuItem();
			jmPorukeSustava.setText("Poruke sustava");
			jmPorukeSustava
					.setToolTipText("ako je bilo kakvih grešaka u radu sustava, ovdje to možete provjeriti ");
			jmPorukeSustava
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();
							PorukeSustavaPanel por = new PorukeSustavaPanel();

							setContentPane(por);
							GlavniFrame.getInstanca().pack();
						}
					});
		}
		return jmPorukeSustava;
	}

	/**
	 * This method initializes jmHzzo
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJmHzzo() {
		if (jmHzzo == null) {
			jmHzzo = new javax.swing.JMenu();
			jmHzzo.add(getJmiKreiranjeHzzoRacuna());
			jmHzzo.add(getJmPostojeciRacuni());
			jmHzzo.add(getJmKreiranjeDiskete());
			jmHzzo.add(getJmKreiraniObracuni());
			jmHzzo.add(getJmPopisObracuna());
			jmHzzo.add(getJmStatistika());
			jmHzzo.add(getJmHzzoIzvjesce());
			jmHzzo.setText("HZZO");
			jmHzzo.setVisible(GlavniFrame.isImaPravoNaHzzo());
		}
		return jmHzzo;
	}

	/**
	 * This method initializes jmiKreiranjeHzzoRacuna
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmiKreiranjeHzzoRacuna() {
		if (jmiKreiranjeHzzoRacuna == null) {
			jmiKreiranjeHzzoRacuna = new javax.swing.JMenuItem();
			jmiKreiranjeHzzoRacuna.setText("Kreiranje raèuna");
			jmiKreiranjeHzzoRacuna
					.setToolTipText("kada trebate izraditi raèun za HZZO za svog klijenta");

			jmiKreiranjeHzzoRacuna
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							kreirajHzzoRacun();
						}
					});
		}
		return jmiKreiranjeHzzoRacuna;
	}

	/**
	 * This method initializes jmPostojeciRacuni
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPostojeciRacuni() {
		if (jmPostojeciRacuni == null) {
			jmPostojeciRacuni = new javax.swing.JMenuItem();
			jmPostojeciRacuni.setText("Postojeæi raèuni");
			jmPostojeciRacuni.setToolTipText("popis svih izdanih raèuna");
			jmPostojeciRacuni
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							kreirajPregledRacuna();
						}
					});
		}
		return jmPostojeciRacuni;
	}

	/**
	 * This method initializes jmKreiranjeDiskete
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmKreiranjeDiskete() {
		if (jmKreiranjeDiskete == null) {
			jmKreiranjeDiskete = new javax.swing.JMenuItem();
			jmKreiranjeDiskete.setText("Kreiraj raèun HZZO-u");
			jmKreiranjeDiskete
					.setToolTipText("pohranjuje datoteku raèuna na disketu");
			jmKreiranjeDiskete
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							kreirajDisketu();
						}
					});
		}
		return jmKreiranjeDiskete;
	}

	private void kreirajDisketu() {
		Thread t = new Thread() {
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				busy();
				try {
					sleep(25);
				} catch (InterruptedException inte) {
				}
				yield();
				HzzoKreiranjeObracuna obr = new HzzoKreiranjeObracuna();
				yield();
				obr.pack();
				obr.setVisible(true);
				GUI.centrirajFrame(obr);
				idle();
			}
		};

		SwingUtilities.invokeLater(t);

	}

	public static LogiranjeVO getLogiranjeVO() {
		return logiranjeVO;
	}

	public static void setLogiranjeVO(LogiranjeVO logiranjeVO) {
		GlavniFrame.logiranjeVO = logiranjeVO;
	}

	/**
	 * This method initializes jmKreiraniObracuni
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmKreiraniObracuni() {
		if (jmKreiraniObracuni == null) {
			jmKreiraniObracuni = new javax.swing.JMenuItem();
			jmKreiraniObracuni.setText("Administracija obraèuna");
			jmKreiraniObracuni
					.setToolTipText("izmjena ili brisanje krivo izraðenih obraèuna");
			jmKreiraniObracuni
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Thread t = new Thread() {
								public void run() {
									setPriority(Thread.MIN_PRIORITY);
									busy();
									try {
										sleep(25);
									} catch (InterruptedException inte) {
									}
									yield();
									if (GlavniFrame.getDjelatnik() == null
											|| !GlavniFrame.getDjelatnik()
													.getAdministrator()
													.booleanValue()) {
										JOptionPane.showMessageDialog(
												GlavniFrame.getInstanca(),
												"Nemate ovlasti izvršiti dotiènu akciju",
												"Obavijest",
												JOptionPane.INFORMATION_MESSAGE);
										return;
									}
									getContentPane().removeAll();
									DAOObjektPanel daop = new DAOObjektPanel();
									daop.setDAOObjekt(DAOFactory.getInstance()
											.getHzzoObracuni());
									daop.setSviElementiSeMoguBrisati(true);
									setContentPane(daop);
									pack();
									idle();
								}
							};
							SwingUtilities.invokeLater(t);
						}// kreiraniobracuni actionPerformed
					});
		}
		return jmKreiraniObracuni;
	}

	/**
	 * This method initializes jmPomagala
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPomagala() {
		if (jmPomagala == null) {
			jmPomagala = new javax.swing.JMenuItem();
			jmPomagala.setText("Pomagala");
			jmPomagala.setToolTipText("popis hzzo artikala...");
			if (!GlavniFrame.isImaPravoNaHzzo())
				jmPomagala.setVisible(false);
			jmPomagala.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Thread t = new Thread() {
						public void run() {
							busy();
							getContentPane().removeAll();
							DAOObjektPanel daop = new DAOObjektPanel();
							daop.setDAOObjekt(DAOFactory.getInstance()
									.getPomagala());
							daop.setSviElementiSeMoguBrisati(true);
							setContentPane(daop);
							daop.dodajSlusaca(new SlusacDaoObjektPanela() {

								public void objektSpremljen(ValueObject objekt) {

									DAOFactory.getInstance().getPomagala()
											.clearFromCache(objekt);
								}
							});

							GlavniFrame.getInstanca().pack();
							idle();
						}
					};
					SwingUtilities.invokeLater(t);
				}
			});
		}
		return jmPomagala;
	}

	/**
	 * This method initializes jmPopisObracuna
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPopisObracuna() {
		if (jmPopisObracuna == null) {
			jmPopisObracuna = new javax.swing.JMenuItem();
			jmPopisObracuna.setText("Kreirani obraèuni");
			jmPopisObracuna
					.setToolTipText("ako želite ponovno kreirati ošteæenu disketu ili ispisati dopis");
			jmPopisObracuna
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();
							PostojeciHzzoObracuniPanel pan = new PostojeciHzzoObracuniPanel();

							setContentPane(pan);
							GlavniFrame.getInstanca().pack();
						}
					});
		}
		return jmPopisObracuna;
	}

	/**
	 * This method initializes jmStatistika
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmStatistika() {
		if (jmStatistika == null) {
			jmStatistika = new javax.swing.JMenuItem();
			jmStatistika.setText("Statistika");
			jmStatistika.setToolTipText("Rezultati prodaje u nekom razdoblju");
			jmStatistika.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (GlavniFrame.getDjelatnik() == null
							|| !GlavniFrame.getDjelatnik().getAdministrator()
									.booleanValue()) {
						JOptionPane.showMessageDialog(
								GlavniFrame.getInstanca(),
								"Nema ovlasti izvršiti dotiènu akciju",
								"Obavijest", JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					HzzoStatistikaArtikliPanel stat = new HzzoStatistikaArtikliPanel();
					getContentPane().removeAll();
					setContentPane(stat);
					GlavniFrame.getInstanca().pack();
				}
			});
		}
		return jmStatistika;
	}

	/**
	 * This method initializes jmProizvodjaci
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmProizvodjaci() {
		if (jmProizvodjaci == null) {
			jmProizvodjaci = new javax.swing.JMenuItem();
			jmProizvodjaci.setText("Proizvoðaèi");
			jmProizvodjaci
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();
							DAOObjektPanel daop = new DAOObjektPanel();
							daop.setDAOObjekt(DAOFactory.getInstance()
									.getProizvodjaci());
							daop.setSviElementiSeMoguBrisati(false);
							daop.setNemaBrisanja(true);
							setContentPane(daop);
							GlavniFrame.getInstanca().pack();
						}
					});
		}
		return jmProizvodjaci;
	}

	/**
	 * This method initializes jmHzzoIzvjesce
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmHzzoIzvjesce() {
		if (jmHzzoIzvjesce == null) {
			jmHzzoIzvjesce = new javax.swing.JMenuItem();
			jmHzzoIzvjesce.setText("Hzzo izvješæe");
			jmHzzoIzvjesce.setToolTipText("tromjeseèno izvješæe za HZZO");
			jmHzzoIzvjesce
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (GlavniFrame.getDjelatnik() == null
									|| !GlavniFrame.getDjelatnik()
											.getAdministrator().booleanValue()) {
								JOptionPane.showMessageDialog(
										GlavniFrame.getInstanca(),
										"Nema ovlasti izvršiti dotiènu akciju",
										"Obavijest",
										JOptionPane.INFORMATION_MESSAGE);
								return;
							}

							getContentPane().removeAll();
							HzzoIzvjescePanel ip = new HzzoIzvjescePanel();
							setContentPane(ip);
							pack();
						}
					});
		}
		return jmHzzoIzvjesce;
	}

	/**
	 * This method initializes jmTransakcije
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmTransakcije() {
		if (jmTransakcije == null) {
			jmTransakcije = new javax.swing.JMenuItem();
			jmTransakcije.setText("Transakcije");
			jmTransakcije
					.setToolTipText("Popis svih Vaših zahtjeva i njihovi statusi");
			jmTransakcije
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();
							TransakcijePanel tp = new TransakcijePanel();
							setContentPane(tp);
							GlavniFrame.getInstanca().pack();
							repaint();
						}
					});
		}
		return jmTransakcije;
	}

	/**
	 * This method initializes jmPoslanePoruke
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPoslanePoruke() {
		if (jmPoslanePoruke == null) {
			jmPoslanePoruke = new javax.swing.JMenuItem();
			jmPoslanePoruke.setText("Poslane poruke");
			jmPoslanePoruke
					.setToolTipText("statistika poslanih SMS i mail poruka koje ste poslali");
			jmPoslanePoruke
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getContentPane().removeAll();
							PoslanePorukePanel pp = new PoslanePorukePanel();
							setContentPane(pp);
							GlavniFrame.getInstanca().pack();
							repaint();
						}
					});
		}
		return jmPoslanePoruke;
	}

	/**
	 * This method initializes jmBackupPodatakaUDatoteku
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmBackupPodatakaUDatoteku() {
		if (jmBackupPodatakaUDatoteku == null) {
			jmBackupPodatakaUDatoteku = new javax.swing.JMenuItem();
			jmBackupPodatakaUDatoteku.setText("Pohranite u datoteku");
			jmBackupPodatakaUDatoteku
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JFileChooser jfc = new JFileChooser();
							jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
							jfc.setToolTipText("Molimo, odaberite datoteku u koju æete arhivirati podatke");
							PostavkeBean p = new PostavkeBean();
							Calendar c = Calendar.getInstance();
							String dan = c.get(Calendar.DAY_OF_MONTH) + ". "
									+ (c.get(Calendar.MONTH) + 1) + ". "
									+ c.get(Calendar.YEAR);

							java.io.File f = new java.io.File(p
									.getTvrtkaNaziv()
									+ " "
									+ p.getMjestoRada()
									+ " podaci " + dan + ".dta");
							jfc.setSelectedFile(f);
							int rez = jfc.showDialog(GlavniFrame.getInstanca(),
									"Pohrani");

							if (rez == JFileChooser.APPROVE_OPTION) {
								String username = GlavniFrame
										.getUsernameZaSynch();
								String pass = GlavniFrame.getPasswordZaSynch();

								try {
									SynchEngine.performSynchronization(
											null,
											null,
											username,
											pass,
											new FileOutputStream(jfc
													.getSelectedFile()));
								} catch (Exception e1) {
									System.out
											.println("Iznimka kod SyncEngine: "
													+ e1);
								}
								username = "odishf";
								pass = "asdf";

							}// if
						}// actionPerformed
					});
		}
		return jmBackupPodatakaUDatoteku;
	}

	/**
	 * This method initializes jmSinkronizacija
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJmSinkronizacija() {
		if (jmSinkronizacija == null) {
			jmSinkronizacija = new javax.swing.JMenu();
			jmSinkronizacija.add(getJmBackupPodatakaUDatoteku());
			jmSinkronizacija.add(getJmAzuriranjePodataka());
			jmSinkronizacija.add(getJmPovratIzDatoteke());
			jmSinkronizacija.setText("Sinkronizacija");
			jmSinkronizacija
					.setToolTipText("Sinkronizacija podataka izmeðu udaljenih sustava");
		}
		return jmSinkronizacija;
	}

	/**
	 * This method initializes jmPovratIzDatoteke
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJmPovratIzDatoteke() {
		if (jmPovratIzDatoteke == null) {
			jmPovratIzDatoteke = new javax.swing.JMenuItem();
			jmPovratIzDatoteke.setText("Uèitajte iz datoteke");
			jmPovratIzDatoteke
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JFileChooser jfc = new JFileChooser();
							jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
							jfc.setToolTipText("Molimo, odaberite datoteku iz koju æete uèitati podatke");
							//PostavkeBean p = new PostavkeBean();
							Calendar c = Calendar.getInstance();
							String dan = c.get(Calendar.DAY_OF_MONTH) + ". "
									+ (c.get(Calendar.MONTH) + 1) + ". "
									+ c.get(Calendar.YEAR);

							int rez = jfc.showDialog(GlavniFrame.getInstanca(),
									"Uèitaj");

							if (rez == JFileChooser.APPROVE_OPTION) {
								String username = GlavniFrame
										.getUsernameZaSynch();
								String pass = GlavniFrame.getPasswordZaSynch();

								try {

									SynchEngine.performSynchFromFile(jfc
											.getSelectedFile());
								} catch (Exception e1) {
									System.out
											.println("Iznimka kod SyncEngine: "
													+ e1 + "uname: " + username);
								}
								username = "odishf";
								pass = "asdf";

							}// if
						}// actionPerformed

					});
		}
		return jmPovratIzDatoteke;
	}

	/**
	 * @see #pitanje(String)
	 */
	public static final int ODGOVOR_DA = JOptionPane.YES_OPTION;
	/**
	 * @see #pitanje(String)
	 */
	public static final int ODGOVOR_NE = JOptionPane.NO_OPTION;
	/**
	 * @see #pitanje(String)
	 */
	public static final int ODGOVOR_ODUSTANI = JOptionPane.CANCEL_OPTION;

	/**
	 * Pozvati kada treba korisniku izbaciti dialog sa pitanjem na koje mora
	 * odgovoriti sa DA, NE, ili ODUSTANI. Nazad ce se vratiti int sa
	 * vrijednošæu: za oznaceni odgovor bit ce postavljeno na NE
	 * <ul>
	 * <li>{@link NPanel#ODGOVOR_DA} - DA</li>
	 * <li>{@link NPanel#ODGOVOR_NE} - NE</li>
	 * <li>{@link NPanel#ODGOVOR_ODUSTANI} - Odustao (Cancel)</li>
	 * </ul>
	 */

	public static final int pitanje(String pitanje) {
		return pitanje(pitanje, 1);
	}// pitanje

	/**
	 * Pozvati kada treba korisniku izbaciti dialog sa pitanjem na koje mora
	 * odgovoriti sa DA, NE, ili ODUSTANI. Nazad ce se vratiti int sa
	 * vrijednošæu: za oznaceni odgovor mozete staviti 0: DA, 1: NE, 2: ODUSTANI
	 * <ul>
	 * <li>{@link NPanel#ODGOVOR_DA} - DA</li>
	 * <li>{@link NPanel#ODGOVOR_NE} - NE</li>
	 * <li>{@link NPanel#ODGOVOR_ODUSTANI} - Odustao (Cancel)</li>
	 * </ul>
	 */
	public static final int pitanje(String pitanje, int oznaceniOdgovor) {
		// Custom button text
		Object[] options = { "Da", "Ne", "Odustani" };

		if (oznaceniOdgovor < 0 || oznaceniOdgovor > 2)
			oznaceniOdgovor = 0;

		int n = JOptionPane.showOptionDialog(GlavniFrame.getInstanca(),
				pitanje, "Pitanje", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[oznaceniOdgovor]);
		return n;
	}// pitanje

	/**
	 * Pozvati kada treba korisniku izbaciti dialog sa pitanjem na koje mora
	 * odgovoriti sa DA ili NE. Nazad ce se vratiti int sa vrijednošæu:
	 * <ul>
	 * <li>{@link NPanel#ODGOVOR_DA} - DA</li>
	 * <li>{@link NPanel#ODGOVOR_NE} - NE</li>
	 * </ul>
	 */
	public static final int pitanjeDaNe(String pitanje) {
		// Custom button text
		Object[] options = { "Da", "Ne" };
		int n = JOptionPane.showOptionDialog(GlavniFrame.getInstanca(),
				pitanje, "Pitanje", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		return n;
	}// pitanjeDaNe

	/**
	 * Otvara prozorèiæ sa tekst field-om u koji se može unijeti tekst
	 * 
	 * @param opis
	 *            informacija korisniku - na što se odnosi unos
	 * @return String koji je korisnik unio ili null, ako je kliknuo na Cancel
	 */
	public static String input(String opis) {
		return JOptionPane.showInputDialog(GlavniFrame.getInstanca(), opis,
				"Unos", JOptionPane.QUESTION_MESSAGE);
	}// input

} // @jve:visual-info decl-index=0 visual-constraint="4,7"
