/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.LogiranjeVO;
import biz.sunce.util.beans.PostavkeBean;

/**
 * datum:2006.02.27
 * 
 * @author asabo
 * 
 */
public final class LogiranjeFrame extends JFrame 
{

	private static final long serialVersionUID = 2057412397723566508L;

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtKorisnickoIme = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JPasswordField jtLozinka = null;
	private javax.swing.JButton jbUlaz = null;
	private javax.swing.JButton jButton1 = null;
	GlavniFrame glavni = null;
	final LogiranjeFrame ja = this;
	PostavkeBean postavke = null;

	/**
	 * This is the default constructor
	 */
	public LogiranjeFrame() {
		super();
		setContentPane(getJContentPane());

		Thread t = new Thread() {
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				yield();
				initialize();
				yield();
				update(getGraphics());
			}
		};

		t.start();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */

	private void initialize() {

		setSize(300, 150);
		setResizable(false);

		setTitle("Prijava u sustav");
		setName("prijavaUSustav");

		setFocusTraversalKeysEnabled(true);

		JTextField jtKorisnickoIme = getJtKorisnickoIme();
		String korIme = null;
		
		try{
		korIme=PostavkeBean.getPostavkaDB(
				biz.sunce.optika.Konstante.POSTAVKE_KORISNICKO_IME, "korisnik");
		}
		catch(Exception e)
		{
			System.err.println("Problem: "+e.getMessage());
		}
		
		if (korIme!=null)
		jtKorisnickoIme.setText(korIme);

		if (korIme.equals(""))
			jtKorisnickoIme.requestFocusInWindow();
		else {
			getJtLozinka().requestFocusInWindow();
		}

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(-1);
			}
		});
		
		ImageIcon myAppImage = loadIcon("sunce-hzzo.png");

		if (myAppImage != null)
			setIconImage(myAppImage.getImage());

		pack();
	}

	private ImageIcon loadIcon(String strPath) {
		URL imgURL = LogiranjeFrame.class.getResource(strPath);

		if (imgURL != null)
			return new ImageIcon(imgURL);
		else
			return null;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			consGridBagConstraints6.gridy = 2;
			consGridBagConstraints6.gridx = 1;
			consGridBagConstraints6.ipadx = 3;
			consGridBagConstraints7.gridy = 2;
			consGridBagConstraints7.gridx = 2;
			consGridBagConstraints3.gridy = 1;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints5.weightx = 1.0;
			consGridBagConstraints5.gridy = 1;
			consGridBagConstraints5.gridx = 1;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints2.weightx = 1.0;
			consGridBagConstraints2.gridy = 0;
			consGridBagConstraints2.gridx = 1;
			consGridBagConstraints2.gridwidth = 2;
			consGridBagConstraints5.gridwidth = 2;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJtKorisnickoIme(), consGridBagConstraints2);
			jContentPane.add(getJLabel1(), consGridBagConstraints3);
			jContentPane.add(getJtLozinka(), consGridBagConstraints5);
			jContentPane.add(getJbUlaz(), consGridBagConstraints6);
			jContentPane.add(getJButton1(), consGridBagConstraints7);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Korisnièko ime: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes jtKorisnickoIme
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtKorisnickoIme() {
		if (jtKorisnickoIme == null) {
			jtKorisnickoIme = new javax.swing.JTextField();
			jtKorisnickoIme
					.setToolTipText("ovdje unosite svoje korisnièko ime ");
			jtKorisnickoIme.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER)
						logirajKorisnika();
				}
			});

		}
		return jtKorisnickoIme;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Lozinka: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtLozinka
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private javax.swing.JPasswordField getJtLozinka() {
		if (jtLozinka == null) {
			jtLozinka = new javax.swing.JPasswordField();
			jtLozinka.setToolTipText("Vaša lozinka za prijavu u sustav");
			jtLozinka.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER)
						logirajKorisnika();
				}
			});
			jtLozinka.requestFocus();
		}
		return jtLozinka;
	}

	/**
	 * This method initializes jbUlaz
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbUlaz() {
		if (jbUlaz == null) {
			jbUlaz = new javax.swing.JButton();
			jbUlaz.setText("Ulaz");
			jbUlaz.setSelected(false);
			jbUlaz.setMnemonic(java.awt.event.KeyEvent.VK_U);
			jbUlaz.setToolTipText("kada unesete korisnièko ime i lozinku kliknite keko biste se ulogirali");
			jbUlaz.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					logirajKorisnika();
				}
			});
		}
		return jbUlaz;
	}

	private void logirajKorisnika() {
		glavni.setEnabled(false);
		SearchCriteria krit = new SearchCriteria();
		ArrayList<String> arl = new ArrayList<String>(2);
		arl.add(jtKorisnickoIme.getText().trim());
		arl.add(new String(jtLozinka.getPassword()));
		krit.setPodaci(arl);

		DjelatnikVO dvo = null;

		try {
			dvo = (DjelatnikVO) DAOFactory.getInstance().getDjelatnici()
					.read(krit);
		} catch (SQLException e1) {
			Logger.fatal("SQL iznimka kod trazenja podataka o djelatniku..", e1);
		}

		if (dvo != null) {
			GlavniFrame.setSifDjelatnika(dvo.getSifra().intValue());
			LogiranjeVO lvo = new LogiranjeVO();
			lvo.setSifDjelatnika(dvo.getSifra());
			lvo.setLogin(Calendar.getInstance());
			lvo.setLogout(null);

			try {
				DAOFactory.getInstance().getLogiranja().insert(lvo);
				GlavniFrame.setLogiranjeVO(lvo);
			} catch (SQLException e2) {
				Logger.fatal(
						"Problem pri zapisivanju èinjenice logiranja korisnika "
								+ dvo.getIme() + " " + dvo.getPrezime(), e2);
			}

			synchronized (glavni) {
				glavni.notify();
			}
			
			glavni.setVisible(true);
			glavni.setEnabled(true);
			
			PostavkeBean.setPostavkaDB(
					biz.sunce.optika.Konstante.POSTAVKE_KORISNICKO_IME,
					jtKorisnickoIme.getText().trim());
			dispose();
		} else
			JOptionPane.showMessageDialog(ja,
					"Korisnièko ime ili lozinka nisu ispravni", "Upozorenje",
					JOptionPane.WARNING_MESSAGE);

	}// logirajKorisnika

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new javax.swing.JButton();
			jButton1.setText("Odustani");
			jButton1.setMnemonic(java.awt.event.KeyEvent.VK_O);
			jButton1.setToolTipText("izlazak iz programa");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(-1);
				}

			});
		}
		return jButton1;
	}

	public void setGlavni(GlavniFrame frame) {
		glavni = frame;
		glavni.setVisible(false);
	}

	public PostavkeBean getPostavke() {
		if (postavke == null) {
			postavke = new PostavkeBean();
		}
		return postavke;
	}

}
