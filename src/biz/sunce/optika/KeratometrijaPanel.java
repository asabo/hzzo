
package biz.sunce.optika;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import biz.sunce.dao.DAO;
import biz.sunce.opticar.vo.KeratometrijaOko;
import biz.sunce.opticar.vo.KeratometrijaVO;

/*
 * Project opticari
 *
 */

/**
 * datum:2005.05.23
 * @author asabo
 *
 */
public class KeratometrijaPanel extends JPanel {

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JTextField jtBaza1D = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtBaza2D = null;
	private javax.swing.JTextField jtAXD = null;
	private javax.swing.JTextField jtVisusD = null;
	private javax.swing.JTextField jtBaza1L = null;
	private javax.swing.JTextField jtBaza2L = null;
	private javax.swing.JTextField jtAXL = null;
	private javax.swing.JTextField jtVisusL = null;
	private KeratometrijaVO ulazni;

//24.05.05. -asabo-  za postavljanje svih elemenata iz objekta
	public void napuniFormu(KeratometrijaVO ulazni)
	{
	 this.ulazni=ulazni;
	 if (ulazni==null) return;

	 KeratometrijaOko tmp=null;
	 tmp=ulazni.getLijevo();
	 if (tmp!=null)
	 {
	 jtBaza1L.setText(tmp.getBaza1());
	 jtBaza2L.setText(tmp.getBaza2());
	 jtAXL.setText(tmp.getAx());
	 jtVisusL.setText(tmp.getVisus());
	 }

	 tmp=ulazni.getDesno();
	 if (tmp!=null)
	 {
	 jtBaza1D.setText(tmp.getBaza1());
	 jtBaza2D.setText(tmp.getBaza2());
	 jtAXD.setText(tmp.getAx());
	 jtVisusD.setText(tmp.getVisus());
	 }
         this.omoguci();
	}//napuniFormu

	// uvijek vrati KeratometrijaVO objekt, bez obzira bio prazan ili ne
	public KeratometrijaVO vratiPodatke()
	{
		if (this.ulazni==null)
		{
			this.ulazni=new KeratometrijaVO();
			this.ulazni.setLijevo(new KeratometrijaOko());
			this.ulazni.setDesno(new KeratometrijaOko());
			// 31.05.05. -asabo- po tome znamo jeli da ga trebamo insertirati, a gore iznad ce mu se promjeniti sifra
			// pregleda ovisno kojem pregledu bude pridruzen...
			this.ulazni.setSifPregleda(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));

		}

		KeratometrijaOko d,l;
		d=this.ulazni.getDesno();
		l=this.ulazni.getLijevo();

		d.setBaza1(this.jtBaza1D.getText());
		d.setBaza2(this.jtBaza2D.getText());
		d.setAx(this.jtAXD.getText());
		d.setVisus(this.jtVisusD.getText());

		l.setBaza1(this.jtBaza1L.getText());
		l.setBaza2(this.jtBaza2L.getText());
		l.setAx(this.jtAXL.getText());
		l.setVisus(this.jtVisusL.getText());

		return this.ulazni;
	}

	public void pobrisiFormu()
	{
		final String p="";
		this.jtAXD.setText(p);
		this.jtAXL.setText(p);
		this.jtBaza1D.setText(p);
		this.jtBaza1L.setText(p);
		this.jtBaza2D.setText(p);
		this.jtBaza2L.setText(p);
		this.jtVisusD.setText(p);
		this.jtVisusL.setText(p);
                this.onemoguci();
	}

        public void omoguci(){this.postaviStatuseElemenata(true);}
        public void onemoguci(){this.postaviStatuseElemenata(false);}
        public void postaviStatuseElemenata(boolean s)
        {
            this.getJtAXD().setEnabled(s);
            this.getJtAXL().setEnabled(s);
            this.getJtBaza1D().setEnabled(s);
            this.getJtBaza1L().setEnabled(s);
            this.getJtBaza2D().setEnabled(s);
            this.getJtBaza2L().setEnabled(s);
            this.getJtVisusD().setEnabled(s);
            this.getJtVisusL().setEnabled(s);
        }//postaviStatuseElemenata

	public KeratometrijaPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints17 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints18 = new java.awt.GridBagConstraints();
		consGridBagConstraints13.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints13.weightx = 1.0;
		consGridBagConstraints13.gridy = 1;
		consGridBagConstraints13.gridx = 3;
		consGridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints6.weightx = 1.0;
		consGridBagConstraints6.gridy = 1;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints7.gridy = 1;
		consGridBagConstraints7.gridx = 0;
		consGridBagConstraints11.gridy = 0;
		consGridBagConstraints11.gridx = 4;
		consGridBagConstraints5.gridy = 0;
		consGridBagConstraints5.gridx = 1;
		consGridBagConstraints18.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints18.weightx = 1.0;
		consGridBagConstraints18.gridy = 2;
		consGridBagConstraints18.gridx = 4;
		consGridBagConstraints10.gridy = 0;
		consGridBagConstraints10.gridx = 3;
		consGridBagConstraints8.gridy = 2;
		consGridBagConstraints8.gridx = 0;
		consGridBagConstraints9.gridy = 0;
		consGridBagConstraints9.gridx = 2;
		consGridBagConstraints17.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints17.weightx = 1.0;
		consGridBagConstraints17.gridy = 2;
		consGridBagConstraints17.gridx = 3;
		consGridBagConstraints12.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints12.weightx = 1.0;
		consGridBagConstraints12.gridy = 1;
		consGridBagConstraints12.gridx = 2;
		consGridBagConstraints15.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints15.weightx = 1.0;
		consGridBagConstraints15.gridy = 2;
		consGridBagConstraints15.gridx = 1;
		consGridBagConstraints14.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints14.weightx = 1.0;
		consGridBagConstraints14.gridy = 1;
		consGridBagConstraints14.gridx = 4;
		consGridBagConstraints16.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints16.weightx = 1.0;
		consGridBagConstraints16.gridy = 2;
		consGridBagConstraints16.gridx = 2;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints5);
		this.add(getJtBaza1D(), consGridBagConstraints6);
		this.add(getJLabel1(), consGridBagConstraints7);
		this.add(getJLabel2(), consGridBagConstraints8);
		this.add(getJLabel3(), consGridBagConstraints9);
		this.add(getJLabel4(), consGridBagConstraints10);
		this.add(getJLabel5(), consGridBagConstraints11);
		this.add(getJtBaza2D(), consGridBagConstraints12);
		this.add(getJtAXD(), consGridBagConstraints13);
		this.add(getJtVisusD(), consGridBagConstraints14);
		this.add(getJtBaza1L(), consGridBagConstraints15);
		this.add(getJtBaza2L(), consGridBagConstraints16);
		this.add(getJtAXL(), consGridBagConstraints17);
		this.add(getJtVisusL(), consGridBagConstraints18);
		this.setSize(319, 67);
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(BevelBorder.RAISED), "Keratometrija", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
	}
	/**
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Baza 1");
		}
		return jLabel;
	}
	/**
	 * This method initializes jtBaza1D
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBaza1D() {
		if(jtBaza1D == null) {
			jtBaza1D = new javax.swing.JTextField();
			jtBaza1D.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtBaza1D;
	}
	/**
	 * This method initializes jLabel1
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText(" D ");
			jLabel1.setFont(new java.awt.Font("DialogInput", java.awt.Font.BOLD, 12));
		}
		return jLabel1;
	}
	/**
	 * This method initializes jLabel2
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText(" L ");
			jLabel2.setFont(new java.awt.Font("DialogInput", java.awt.Font.BOLD, 12));
		}
		return jLabel2;
	}
	/**
	 * This method initializes jLabel3
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Baza 2");
		}
		return jLabel3;
	}
	/**
	 * This method initializes jLabel4
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("AX ");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jLabel5
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Visus");
		}
		return jLabel5;
	}
	/**
	 * This method initializes jtBaza2D
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBaza2D() {
		if(jtBaza2D == null) {
			jtBaza2D = new javax.swing.JTextField();
			jtBaza2D.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtBaza2D;
	}
	/**
	 * This method initializes jtAXD
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAXD() {
		if(jtAXD == null) {
			jtAXD = new javax.swing.JTextField();
			jtAXD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtAXD;
	}
	/**
	 * This method initializes jtVisusD
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtVisusD() {
		if(jtVisusD == null) {
			jtVisusD = new javax.swing.JTextField();
			jtVisusD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtVisusD;
	}
	/**
	 * This method initializes jtBaza1L
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBaza1L() {
		if(jtBaza1L == null) {
			jtBaza1L = new javax.swing.JTextField();
			jtBaza1L.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtBaza1L;
	}
	/**
	 * This method initializes jtBaza2L
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtBaza2L() {
		if(jtBaza2L == null) {
			jtBaza2L = new javax.swing.JTextField();
			jtBaza2L.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtBaza2L;
	}
	/**
	 * This method initializes jtAXL
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtAXL() {
		if(jtAXL == null) {
			jtAXL = new javax.swing.JTextField();
			jtAXL.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtAXL;
	}
	/**
	 * This method initializes jtVisusL
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtVisusL() {
		if(jtVisusL == null) {
			jtVisusL = new javax.swing.JTextField();
			jtVisusL.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtVisusL;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
