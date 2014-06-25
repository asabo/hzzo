package biz.sunce.dao.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * datum:2006.04.03
 * 
 * @author asabo
 */
public final class Pomagalo extends JPanel implements GUIEditor {

	private static final long serialVersionUID = -3302442603460739747L;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtSifra = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JTextField jtNaziv = null;
	private javax.swing.JLabel jLabel2 = null;
	@SuppressWarnings("rawtypes")
	private javax.swing.JComboBox jcPoreznaSkupina = null;

	PomagaloVO oznaceni = null;

	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JCheckBox jcOptickoPomagalo = null;

	/**
	 * This is the default constructor
	 */
	public Pomagalo() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		consGridBagConstraints11.gridy = 3;
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints21.gridy = 3;
		consGridBagConstraints21.gridx = 1;
		consGridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints5.gridy = 2;
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints6.weightx = 1.0;
		consGridBagConstraints6.gridy = 2;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 1;
		consGridBagConstraints4.gridx = 1;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints3.gridy = 1;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJtSifra(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJLabel1(), consGridBagConstraints3);
		this.add(getJtNaziv(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJLabel2(), consGridBagConstraints5);
		this.add(getJcPoreznaSkupina(), new GridBagConstraints(1, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJLabel3(), consGridBagConstraints11);
		this.add(getJcOptickoPomagalo(), consGridBagConstraints21);
		this.setSize(367, 94);
		this.setPreferredSize(new java.awt.Dimension(508, 96));
	}

	public void napuniPodatke(ValueObject ulaz) {

		if (ulaz == null) {
			pobrisiFormu();
			return;
		}
		this.oznaceni = (PomagaloVO) ulaz;
		this.jtSifra.setText(this.oznaceni.getSifraArtikla());
		this.jtNaziv.setText(this.oznaceni.getNaziv());
		Integer skupina = this.oznaceni.getPoreznaSkupina();
		postaviPoreznuSkupinu(skupina);
		this.jcOptickoPomagalo
				.setSelected(this.oznaceni.getOptickoPomagalo() != null ? this.oznaceni
						.getOptickoPomagalo().booleanValue() : false);

	}// napuniPodatke

	private void postaviPoreznuSkupinu(Integer ulaz) {
		if (ulaz == null)
			return; // zasada ne znam sta ce ici

		int itemCount = this.jcPoreznaSkupina.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			PoreznaStopaVO pvo = (PoreznaStopaVO) this.jcPoreznaSkupina
					.getItemAt(i);
			if (pvo != null && pvo.getSifra().intValue() == ulaz.intValue()) {
				this.jcPoreznaSkupina.setSelectedIndex(i);
				return;
			}
		}// for i
	}

	public ValueObject vratiPodatke() {
		if (this.oznaceni == null) {
			return null;
		}
		this.oznaceni.setSifraArtikla(this.jtSifra.getText().trim()
				.toUpperCase());
		this.oznaceni.setNaziv(this.jtNaziv.getText().trim());
		this.oznaceni.setOptickoPomagalo(Boolean.valueOf(this.jcOptickoPomagalo
				.isSelected()));

		PoreznaStopaVO pvo = (PoreznaStopaVO) this.jcPoreznaSkupina
				.getSelectedItem();

		if (pvo != null)
			this.oznaceni.setPoreznaSkupina(pvo.getSifra());
		
		return this.oznaceni;
	}// vratiPodatke

	public void pobrisiFormu() {
		final String p = "";
		this.jtSifra.setText(p);
		this.jtNaziv.setText(p);
		this.jcOptickoPomagalo.setSelected(false);
		// this.jcPoreznaSkupina.setSelectedIndex(0);
		this.oznaceni = null;
	}

	public boolean isFormaIspravna() {
		return false;
	}

	public void omoguci() {
		postaviStatuse(true);
	}

	public void onemoguci() {
		postaviStatuse(false);
	}

	private void postaviStatuse(boolean s) {
		this.jtSifra.setEnabled(s);
		this.jtSifra.setEditable(s);
		this.jtNaziv.setEnabled(s);
		this.jtNaziv.setEditable(s);
		this.jcPoreznaSkupina.setEnabled(s);
		this.jcPoreznaSkupina.setEditable(s);
		this.jcOptickoPomagalo.setEnabled(s);
	}

	public boolean jeliIzmjenjen() {
		return true;
	}

	public void dodajSlusacaSpremnostiPodataka(SlusacSpremnostiPodataka slusac) {
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("šifra: ");
		}
		return jLabel;
	}

	/**
	 * This method initializes jtSifra
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtSifra() {
		if (jtSifra == null) {
			jtSifra = new javax.swing.JTextField();
			jtSifra.setToolTipText("hzzo šifra artikla - maksimalno 9 znakova");
			jtSifra.setPreferredSize(new java.awt.Dimension(160, 20));
			jtSifra.setMinimumSize(new java.awt.Dimension(160, 20));
		}
		return jtSifra;
	}

	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("naziv: ");
		}
		return jLabel1;
	}

	/**
	 * This method initializes jtNaziv
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtNaziv() {
		if (jtNaziv == null) {
			jtNaziv = new javax.swing.JTextField();
			jtNaziv.setToolTipText("naziv artikla ");
			jtNaziv.setPreferredSize(new java.awt.Dimension(160, 20));
			jtNaziv.setMinimumSize(new java.awt.Dimension(160, 20));
		}
		return jtNaziv;
	}

	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Porezna skupina: ");
		}
		return jLabel2;
	}

	/**
	 * This method initializes jcPoreznaSkupina
	 * 
	 * @return javax.swing.JComboBox
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private javax.swing.JComboBox getJcPoreznaSkupina() {
		if (jcPoreznaSkupina == null) {
			jcPoreznaSkupina = new javax.swing.JComboBox();
			jcPoreznaSkupina
					.setToolTipText("porezna skupina u koju artikl spada");
			ArrayList l = null;
			try {
				l = (ArrayList) DAOFactory.getInstance().getPorezneStope()
						.findAll(null);
				int ls = l.size();
				for (int i = 0; i < ls; i++)
					jcPoreznaSkupina.addItem(l.get(i));
			} catch (SQLException e) {
				Logger.fatal(
						"SQL iznimka kod povlacenja popisa poreznih grupa", e);
			}
		}
		return jcPoreznaSkupina;
	}

	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Oèno pomagalo: ");
		}
		return jLabel3;
	}

	/**
	 * This method initializes jcOptickoPomagalo
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getJcOptickoPomagalo() {
		if (jcOptickoPomagalo == null) {
			jcOptickoPomagalo = new javax.swing.JCheckBox();
		}
		return jcOptickoPomagalo;
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
