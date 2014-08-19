/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import biz.sunce.dao.DAO;
import biz.sunce.opticar.vo.SkiaskopijaOko;
import biz.sunce.opticar.vo.SkiaskopijaVO;

/**
 * datum:2005.05.22
 * @author asabo
 *
 */
public final class SkiaskopijaPanel extends JPanel {

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JTextField jtVisusSCD = null;
	private javax.swing.JTextField jtDsphD = null;
	private javax.swing.JTextField jtDcylD = null;
	private javax.swing.JTextField jtAXD = null;
	private javax.swing.JTextField jtVisusD = null;
	private javax.swing.JTextField jtVisusSCL = null;
	private javax.swing.JTextField jtDsphL = null;
	private javax.swing.JTextField jtDcylL = null;
	private javax.swing.JTextField jtAXL = null;
	private javax.swing.JTextField jtVisusL = null;

	SkiaskopijaVO ulazni=null;

	//24.05.05. -asabo-  za postavljanje svih elemenata iz objekta
	public void napuniFormu(SkiaskopijaVO ulazni)
	{
	 SkiaskopijaOko tmp=null;

	 this.ulazni=ulazni;

	 if (this.ulazni==null) return;

	 if (ulazni.getLijevo()!=null)
	 {
	 tmp=ulazni.getLijevo();
	 jtVisusSCL.setText(tmp.getVisus_sc());
	 jtDsphL.setText(tmp.getDsph());
	 jtDcylL.setText(tmp.getDcyl());
	 jtAXL.setText(tmp.getAx());
	 jtVisusL.setText(tmp.getVisus());
	 }
	 if (ulazni.getDesno()!=null)
	 {
	 tmp=ulazni.getDesno();
	 jtVisusSCD.setText(tmp.getVisus_sc());
	 jtDsphD.setText(tmp.getDsph());
	 jtDcylD.setText(tmp.getDcyl());
	 jtAXD.setText(tmp.getAx());
	 jtVisusD.setText(tmp.getVisus());
	 }
         this.omoguci();
	}//napuniFormu

	public SkiaskopijaVO vratiPodatke()
	{
		SkiaskopijaOko l=null,d=null;

		if (this.ulazni==null)
				{
					this.ulazni=new SkiaskopijaVO();
					l=new SkiaskopijaOko();
					d=new SkiaskopijaOko();
					this.ulazni.setLijevo(l);
					this.ulazni.setDesno(d);
					this.ulazni.setSifPregleda(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));
				}

				l=this.ulazni.getLijevo();
				d=this.ulazni.getDesno();

		    // lijevo oko
				l.setAx(this.jtAXL.getText());
				l.setDcyl(this.jtDcylL.getText());
				l.setDsph(this.jtDsphL.getText());
				l.setVisus(this.jtVisusL.getText());
				l.setVisus_sc(this.jtVisusSCL.getText());
				l.setDl(DAO.LIJEVO);

				//desno oko
			d.setAx(this.jtAXD.getText());
			d.setDcyl(this.jtDcylD.getText());
			d.setDsph(this.jtDsphD.getText());
			d.setVisus(this.jtVisusD.getText());
			d.setVisus_sc(this.jtVisusSCD.getText());
			d.setDl(DAO.DESNO);

				return this.ulazni;
		}//vratiPodatke

    public void omoguci()
              {this.postaviStatuseElemenata(true);	}

              public void onemoguci()
              {this.postaviStatuseElemenata(false);	}

              //14.11.05. -asabo- postavljamo status elemenata na formi
              public void postaviStatuseElemenata(boolean s)
              {
              this.getJtAXD().setEnabled(s);
              this.getJtAXL().setEnabled(s);
              this.getJtDcylD().setEnabled(s);
              this.getJtDcylL().setEnabled(s);
              this.getJtDsphD().setEnabled(s);
              this.getJtDsphL().setEnabled(s);
              this.getJtVisusD().setEnabled(s);
              this.getJtVisusL().setEnabled(s);
              this.getJtVisusSCD().setEnabled(s);
              this.getJtVisusSCL().setEnabled(s);
              }//postaviStatus


	public void pobrisiFormu()
	{
		final String p="";
		this.jtAXD.setText(p);
	  this.jtAXL.setText(p);
	  this.jtDcylD.setText(p);
	  this.jtDcylL.setText(p);
	  this.jtDsphD.setText(p);
	  this.jtDsphL.setText(p);
	  this.jtVisusD.setText(p);
	  this.jtVisusL.setText(p);
	  this.jtVisusSCD.setText(p);
	  this.jtVisusSCL.setText(p);
          this.onemoguci();
	}//pobrisiFormu

	public SkiaskopijaPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints61 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints81 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints71 = new java.awt.GridBagConstraints();
		consGridBagConstraints51.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints51.weightx = 1.0;
		consGridBagConstraints51.gridy = 2;
		consGridBagConstraints51.gridx = 3;
		consGridBagConstraints71.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints71.weightx = 1.0;
		consGridBagConstraints71.gridy = 2;
		consGridBagConstraints71.gridx = 5;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 1;
		consGridBagConstraints21.gridx = 5;
		consGridBagConstraints11.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints11.weightx = 1.0;
		consGridBagConstraints11.gridy = 1;
		consGridBagConstraints11.gridx = 4;
		consGridBagConstraints61.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints61.weightx = 1.0;
		consGridBagConstraints61.gridy = 2;
		consGridBagConstraints61.gridx = 4;
		consGridBagConstraints41.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints41.weightx = 1.0;
		consGridBagConstraints41.gridy = 2;
		consGridBagConstraints41.gridx = 2;
		consGridBagConstraints31.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints31.weightx = 1.0;
		consGridBagConstraints31.gridy = 1;
		consGridBagConstraints31.gridx = 6;
		consGridBagConstraints81.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints81.weightx = 1.0;
		consGridBagConstraints81.gridy = 2;
		consGridBagConstraints81.gridx = 6;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 2;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints3.gridx = 4;
		consGridBagConstraints5.gridy = 0;
		consGridBagConstraints5.gridx = 6;
		consGridBagConstraints6.gridy = 1;
		consGridBagConstraints6.gridx = 1;
		consGridBagConstraints7.gridy = 2;
		consGridBagConstraints7.gridx = 1;
		consGridBagConstraints9.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints9.weightx = 1.0;
		consGridBagConstraints9.gridy = 1;
		consGridBagConstraints9.gridx = 3;
		consGridBagConstraints4.gridy = 0;
		consGridBagConstraints4.gridx = 5;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 3;
		consGridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints8.weightx = 1.0;
		consGridBagConstraints8.gridy = 1;
		consGridBagConstraints8.gridx = 2;
		consGridBagConstraints8.anchor = java.awt.GridBagConstraints.CENTER;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJLabel1(), consGridBagConstraints2);
		this.add(getJLabel2(), consGridBagConstraints3);
		this.add(getJLabel3(), consGridBagConstraints4);
		this.add(getJLabel4(), consGridBagConstraints5);
		this.add(getJLabel5(), consGridBagConstraints6);
		this.add(getJLabel6(), consGridBagConstraints7);
		this.add(getJtVisusSCD(), consGridBagConstraints8);
		this.add(getJtDsphD(), consGridBagConstraints9);
		this.add(getJtDcylD(), consGridBagConstraints11);
		this.add(getJtAXD(), consGridBagConstraints21);
		this.add(getJtVisusD(), consGridBagConstraints31);
		this.add(getJtVisusSCL(), consGridBagConstraints41);
		this.add(getJtDsphL(), consGridBagConstraints51);
		this.add(getJtDcylL(), consGridBagConstraints61);
		this.add(getJtAXL(), consGridBagConstraints71);
		this.add(getJtVisusL(), consGridBagConstraints81);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(346*faktor, 63*faktor);
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(BevelBorder.RAISED), "skiaskopija", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
	}
	/**
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Visus SC");
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Dsph");
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
			jLabel2.setText("Dcyl");
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
			jLabel3.setText("AX");
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
			jLabel4.setText("Visus");
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
			jLabel5.setText(" D ");
			jLabel5.setFont(new java.awt.Font("DialogInput", java.awt.Font.BOLD, 12));
		}
		return jLabel5;
	}
	/**
	 * This method initializes jLabel6
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText(" L ");
			jLabel6.setFont(new java.awt.Font("DialogInput", java.awt.Font.BOLD, 12));
		}
		return jLabel6;
	}
	/**
	 * This method initializes jtVisusSCD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtVisusSCD() {
		if(jtVisusSCD == null) {
			jtVisusSCD = new javax.swing.JTextField();
			jtVisusSCD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtVisusSCD;
	}
	/**
	 * This method initializes jtDsphD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDsphD() {
		if(jtDsphD == null) {
			jtDsphD = new javax.swing.JTextField();
			jtDsphD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtDsphD;
	}
	/**
	 * This method initializes jtDcylD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDcylD() {
		if(jtDcylD == null) {
			jtDcylD = new javax.swing.JTextField();
			jtDcylD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtDcylD;
	}
	/**
	 * This method initializes jtAXD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtAXD() {
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
	public javax.swing.JTextField getJtVisusD() {
		if(jtVisusD == null) {
			jtVisusD = new javax.swing.JTextField();
			jtVisusD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtVisusD;
	}
	/**
	 * This method initializes jtVisusSCL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtVisusSCL() {
		if(jtVisusSCL == null) {
			jtVisusSCL = new javax.swing.JTextField();
			jtVisusSCL.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtVisusSCL;
	}
	/**
	 * This method initializes jtDsphL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDsphL() {
		if(jtDsphL == null) {
			jtDsphL = new javax.swing.JTextField();
			jtDsphL.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtDsphL;
	}
	/**
	 * This method initializes jtDcylL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDcylL() {
		if(jtDcylL == null) {
			jtDcylL = new javax.swing.JTextField();
			jtDcylL.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtDcylL;
	}
	/**
	 * This method initializes jtAXL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtAXL() {
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
	public javax.swing.JTextField getJtVisusL() {
		if(jtVisusL == null) {
			jtVisusL = new javax.swing.JTextField();
			jtVisusL.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtVisusL;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
