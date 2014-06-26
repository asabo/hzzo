package biz.sunce.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import biz.sunce.optika.Logger;

/**
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: ANSA
 * </p>
 * 
 * @author Ante Sabo
 * @version 1.0
 */

@SuppressWarnings("serial")
public final class SlikaFrame extends JFrame {
	BorderLayout borderLayout1 = new BorderLayout();
	SlikovniContainer slk;
	BufferedImage slika = null;
	JPanel jpSlika = new JPanel();
	JButton jbPrintaj = new JButton();
	JButton jbKopiraj = new JButton();
	JPanel donjiPanel = new JPanel();
	JButton jbSpremi = new JButton();
	JButton jbZatvori = new JButton();
	boolean prikazivanjePosebnogPanela = false;
	JPanel jpDatumi = new JPanel();
	JLabel jLabel1 = new JLabel();
	JTextField jtDatumOd = new JTextField();
	JLabel jlDo = new JLabel();
	JTextField jtDatumDo = new JTextField();
	java.util.Calendar datumOd, datumDo;

	// samo ako polje dobije fokus, moze pri gubitku fokusa aktivirati
	// generiranje grafikona
	boolean dozvolaUcitavanjaPodataka = false;

	public SlikaFrame(BufferedImage slika) {
		try {
			this.postaviSliku(slika);
			jbInit();
			this.centriraj();

			this.repaint();
			this.prikazivanjePosebnogPanela = false;
			this.napuniFormu();
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
	}// SlikaFrame

	private void postaviSliku(BufferedImage slika) {
		if (this.slk == null)
			this.slk = new SlikovniContainer(slika);
		else
			this.slk.setSlika(slika);

		this.slika = slika;
		if (slika != null) {
			int vis = this.donjiPanel.getHeight();
			this.setSize(slika.getWidth(null), slika.getHeight(null) + vis);
		}
	}

	public void prilagodiFrameVeliciniSlike() {
		if (slika != null) {
			int vis = this.donjiPanel.getHeight();
			this.setSize(slika.getWidth(null), slika.getHeight(null) + vis);
			this.validate(); // ponovno rasporedi elemente na formi...
		}
	}// prilagodiFrameVeliciniSlike

	private void napuniFormu() {
		if (this.prikazivanjePosebnogPanela)
			this.jpDatumi.setVisible(true);
		else
			this.jpDatumi.setVisible(false);
		if (this.prikazivanjePosebnogPanela) {
			int d, m, g;
			String sd, sm, sg;

			int god, gdo;
			god = datumOd.get(Calendar.YEAR);
			gdo = datumDo.get(Calendar.YEAR);
			if (god > 2000)
				god -= 2000;
			if (gdo > 2000)
				gdo -= 2000;

			d = datumOd.get(Calendar.DAY_OF_MONTH);
			sd = d < 10 ? "0" + d : "" + d;

			m = (datumOd.get(Calendar.MONTH) + 1);
			sm = m < 10 ? "0" + m : "" + m;

			g = god;
			sg = g < 10 ? "0" + g : "" + g;

			String tmp = sd + "" + sm + "" + sg;

			this.jtDatumOd.setText(tmp);

			d = datumDo.get(Calendar.DAY_OF_MONTH);
			sd = d < 10 ? "0" + d : "" + d;

			m = (datumDo.get(Calendar.MONTH) + 1);
			sm = m < 10 ? "0" + m : "" + m;

			g = gdo;
			sg = g < 10 ? "0" + g : "" + g;

			tmp = sd + "" + sm + "" + sg;

			this.jtDatumDo.setText(tmp);
		}
	}// napuniFormu

	void jbInit() throws Exception {
		this.getContentPane().setLayout(borderLayout1);
		jbPrintaj.setText("Printaj");
		jbPrintaj
				.addActionListener(new SlikaFrame_jbPrintaj_actionAdapter(this));
		jbKopiraj.setText("Kopiraj");
		jbKopiraj
				.addActionListener(new SlikaFrame_jbKopiraj_actionAdapter(this));
		jpSlika.setBackground(Color.white);
		if (this.slika != null)
			this.slk.setMinimumSize(new Dimension(this.slika.getWidth(null),
					this.slika.getHeight(null)));

		jbSpremi.setText("Spremi sliku");
		jbSpremi.addActionListener(new SlikaFrame_jbSpremi_actionAdapter(this));
		jbZatvori.setText("Zatvori");
		jbZatvori
				.addActionListener(new SlikaFrame_jbZatvori_actionAdapter(this));
		jpDatumi.setMinimumSize(new Dimension(150, 23));
		jpDatumi.setPreferredSize(new Dimension(300, 23));
		jpDatumi.setToolTipText("");
		jLabel1.setText("Datum od: ");
		jtDatumOd.setMinimumSize(new Dimension(50, 20));
		jtDatumOd.setPreferredSize(new Dimension(60, 20));
		jtDatumOd.setText(" ");
		jtDatumOd.addFocusListener(new SlikaFrame_jtDatumOd_focusAdapter(this));
		jtDatumOd
				.addActionListener(new SlikaFrame_jtDatumOd_actionAdapter(this));
		jlDo.setText(" do: ");
		jtDatumDo.setPreferredSize(new Dimension(60, 20));
		jtDatumDo.setText(" ");
		jtDatumDo.addFocusListener(new SlikaFrame_jtDatumDo_focusAdapter(this));
		donjiPanel.setToolTipText("");
		this.getContentPane().add(this.slk, BorderLayout.CENTER);
		this.getContentPane().add(donjiPanel, BorderLayout.SOUTH);
		donjiPanel.add(jpDatumi);
		jpDatumi.add(jLabel1);
		jpDatumi.add(jtDatumOd);
		jpDatumi.add(jlDo);
		jpDatumi.add(jtDatumDo);
		donjiPanel.add(jbPrintaj, null);
		donjiPanel.add(jbKopiraj, null);
		donjiPanel.add(jbSpremi, null);
		donjiPanel.add(jbZatvori, null);
	}// jbInit

	void jbPrintaj_actionPerformed(ActionEvent e) {
		PrintUtilities.printComponentLandscape(this.slk);
	}

	void jbKopiraj_actionPerformed(ActionEvent e) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

		this.slk.getTransferHandler().exportToClipboard(this.slk, cb,
				TransferHandler.COPY);
	}

	private void centriraj() {
		int sir = this.getWidth();
		int duz = this.getHeight();
		double cx = 0d;
		double cy = 0d;

		GraphicsConfiguration gc = this.getGraphicsConfiguration();

		if (gc == null)
			return;
		else {
			cx = gc.getBounds().getCenterX();
			cy = gc.getBounds().getCenterY();

			cx -= sir / 2;
			cy -= duz / 2;

			this.setBounds((int) cx, (int) cy, sir, duz);
		}
		return;
	}// centriraj

	void jbSpremi_actionPerformed(ActionEvent e) {

		javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(
				this.getTitle() + ".jpg");
		jfc.setDialogTitle("Odabir lager liste");
		jfc.setToolTipText("Ovdje odabirete koju æete datoteku uèitati ");
		jfc.setMultiSelectionEnabled(false);
		jfc.setDialogType(javax.swing.JFileChooser.FILES_ONLY);

		jfc.setName(this.getTitle() + ".jpg");
		jfc.setSelectedFile(new java.io.File(this.getTitle() + ".jpg"));

		jfc.showDialog(this, "Spremi");

		java.io.File dat = jfc.getSelectedFile();

		jfc.setVisible(false);
		jfc = null;

		if (dat != null) {
			byte[] podaci = new PictureResizer().preformatirajSlikuKaoJPEG(
					this.slk.getSlika(), 80.0f);

			java.io.FileOutputStream faos = null;

			try {
				faos = new java.io.FileOutputStream(dat);
				faos.write(podaci);
			} catch (Exception ex) {
				Logger.log("Iznimka kod zapisivanja slike u datoteku", ex);
				System.err.println("Iznimka kod zapisivanja slike u datoteku: "
						+ ex);
			} finally {
				try {
					if (faos != null)
						faos.close();
					faos = null;
				} catch (Exception e2) {
				}
				faos = null;
				podaci = null;
			}

		}// if

	}

	void jbZatvori_actionPerformed(ActionEvent e) {
		this.dispose();
	}// /fja jbSpremi...

	public Calendar getDatumDo() {
		return datumDo;
	}

	public Calendar getDatumOd() {
		return datumOd;
	}

	public void setDatumDo(Calendar datumDo) {
		this.datumDo = datumDo;
	}

	public void setDatumOd(Calendar datumOd) {
		this.datumOd = datumOd;
	}

	public void jtDatumOd_actionPerformed(ActionEvent e) {

	}

	public void jtDatumOd_focusLost(FocusEvent e) {
		datumiIzmjenjeni();
	}

	public void jtDatumDo_focusLost(FocusEvent e) {
		datumiIzmjenjeni();
	}

	private void datumiIzmjenjeni() {
		if (!this.dozvolaUcitavanjaPodataka)
			return;

		if ((this.datumOd = Util.provjeriIspravnostDatuma(jtDatumOd.getText())) == null) {
			alert("Polje DATUM OD nije ispravno unešeno!");
			return;
		}
		if ((this.datumDo = Util.provjeriIspravnostDatuma(jtDatumDo.getText())) == null) {
			alert("Polje DATUM DO nije ispravno unešeno!");
			return;
		}

		if (this.datumDo.before(this.datumOd)) {
			alert("Datum OD mlaði od datuma DO");
			return;
		}

		// this.slika=this.generator.generirajGrafikon(this.datumOd,this.datumDo);
		this.postaviSliku(this.slika);
		this.repaint();
		this.dozvolaUcitavanjaPodataka = false;
	}// datumiIzmjenjeni

	void alert(String poruka) {
		JOptionPane.showMessageDialog(this, poruka, "Upozorenje!",
				JOptionPane.WARNING_MESSAGE);
	}

	public void jtDatumOd_focusGained(FocusEvent e) {
		this.dozvolaUcitavanjaPodataka = true;
	}

	public void jtDatumDo_focusGained(FocusEvent e) {
		this.dozvolaUcitavanjaPodataka = true;
	}

}

class SlikaFrame_jtDatumDo_focusAdapter extends FocusAdapter {
	private SlikaFrame adaptee;

	SlikaFrame_jtDatumDo_focusAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	@Override
	public void focusGained(FocusEvent e) {
		adaptee.jtDatumDo_focusGained(e);
	}

	@Override
	public void focusLost(FocusEvent e) {
		adaptee.jtDatumDo_focusLost(e);
	}
}

class SlikaFrame_jtDatumOd_focusAdapter extends FocusAdapter {
	private SlikaFrame adaptee;

	SlikaFrame_jtDatumOd_focusAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	@Override
	public void focusGained(FocusEvent e) {
		adaptee.jtDatumOd_focusGained(e);
	}

	@Override
	public void focusLost(FocusEvent e) {
		adaptee.jtDatumOd_focusLost(e);
	}
}

class SlikaFrame_jtDatumOd_actionAdapter implements ActionListener {
	private SlikaFrame adaptee;

	SlikaFrame_jtDatumOd_actionAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jtDatumOd_actionPerformed(e);
	}
}

class SlikaFrame_jbPrintaj_actionAdapter implements
		java.awt.event.ActionListener {
	SlikaFrame adaptee;

	SlikaFrame_jbPrintaj_actionAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jbPrintaj_actionPerformed(e);
	}
}

class SlikaFrame_jbKopiraj_actionAdapter implements
		java.awt.event.ActionListener {
	SlikaFrame adaptee;

	SlikaFrame_jbKopiraj_actionAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jbKopiraj_actionPerformed(e);
	}
}

class SlikaFrame_jbSpremi_actionAdapter implements
		java.awt.event.ActionListener {
	SlikaFrame adaptee;

	SlikaFrame_jbSpremi_actionAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jbSpremi_actionPerformed(e);
	}
}

class SlikaFrame_jbZatvori_actionAdapter implements
		java.awt.event.ActionListener {
	SlikaFrame adaptee;

	SlikaFrame_jbZatvori_actionAdapter(SlikaFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jbZatvori_actionPerformed(e);
	}
}
