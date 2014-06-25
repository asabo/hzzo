package biz.sunce.optika;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import biz.sunce.dao.DAO;
import biz.sunce.opticar.vo.RefraktometarOko;
import biz.sunce.opticar.vo.RefraktometarVO;

/*
 * Project opticari
 *
 */

/**
 * datum:2005.05.23
 * @author asabo
 *
 */
public class RefraktometarPanel extends JPanel {

	private javax.swing.JTextField jtDsphD = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtDsphL = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtDcylD = null;
	private javax.swing.JTextField jtDcylL = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtAXD = null;
	private javax.swing.JTextField jtAXL = null;
	RefraktometarVO ulazni=null;

//24.05.05. -asabo- ena za postavljanje svih elemenata iz objekta
public void napuniFormu(RefraktometarVO ulazni)
{
	 	this.ulazni=ulazni;
	 	if (this.ulazni==null) return;

	  RefraktometarOko tmp=null;
	  tmp=ulazni.getLijevo();

	 if (tmp!=null)
	 {
	 jtDsphL.setText(tmp.getDsph());
	 jtDcylL.setText(tmp.getDcyl());
	 jtAXL.setText(tmp.getAx());
	 }

	 tmp=ulazni.getDesno();

	 if (tmp!=null)
	 {
	 jtDsphD.setText(tmp.getDsph());
	 jtDcylD.setText(tmp.getDcyl());
	 jtAXD.setText(tmp.getAx());
	 }
         this.omoguci();
	}//napuniFormu

	public RefraktometarVO vratiPodatke()
	{
		RefraktometarOko l=null,d=null;

		if (this.ulazni==null)
					{
						this.ulazni=new RefraktometarVO();
						l=new RefraktometarOko();
						d=new RefraktometarOko();
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
					l.setDl(DAO.LIJEVO);

					//desno oko
					d.setAx(this.jtAXD.getText());
					d.setDcyl(this.jtDcylD.getText());
					d.setDsph(this.jtDsphD.getText());
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
                this.onemoguci();
	}//pobrisiFormu


	public RefraktometarPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints23 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints25 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints27 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints26 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints29 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints30 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints28 = new java.awt.GridBagConstraints();
		consGridBagConstraints22.gridy = 0;
		consGridBagConstraints22.gridx = 1;
		consGridBagConstraints25.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints25.weightx = 1.0;
		consGridBagConstraints25.gridy = 2;
		consGridBagConstraints25.gridx = 1;
		consGridBagConstraints24.gridy = 2;
		consGridBagConstraints24.gridx = 0;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 1;
		consGridBagConstraints21.gridx = 1;
		consGridBagConstraints23.gridy = 1;
		consGridBagConstraints23.gridx = 0;
		consGridBagConstraints27.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints27.weightx = 1.0;
		consGridBagConstraints27.gridy = 1;
		consGridBagConstraints27.gridx = 2;
		consGridBagConstraints30.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints30.weightx = 1.0;
		consGridBagConstraints30.gridy = 1;
		consGridBagConstraints30.gridx = 3;
		consGridBagConstraints26.gridy = 0;
		consGridBagConstraints26.gridx = 2;
		consGridBagConstraints31.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints31.weightx = 1.0;
		consGridBagConstraints31.gridy = 2;
		consGridBagConstraints31.gridx = 3;
		consGridBagConstraints29.gridy = 0;
		consGridBagConstraints29.gridx = 3;
		consGridBagConstraints28.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints28.weightx = 1.0;
		consGridBagConstraints28.gridy = 2;
		consGridBagConstraints28.gridx = 2;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJtDsphD(), consGridBagConstraints21);
		this.add(getJLabel(), consGridBagConstraints22);
		this.add(getJLabel1(), consGridBagConstraints23);
		this.add(getJLabel2(), consGridBagConstraints24);
		this.add(getJtDsphL(), consGridBagConstraints25);
		this.add(getJLabel3(), consGridBagConstraints26);
		this.add(getJtDcylD(), consGridBagConstraints27);
		this.add(getJtDcylL(), consGridBagConstraints28);
		this.add(getJLabel4(), consGridBagConstraints29);
		this.add(getJtAXD(), consGridBagConstraints30);
		this.add(getJtAXL(), consGridBagConstraints31);
		this.setSize(226, 61);
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(BevelBorder.RAISED), "Refraktometar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

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
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Dsph");
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
	 * This method initializes jLabel3
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("DCyl");
		}
		return jLabel3;
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
	 * This method initializes jLabel4
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("AX");
		}
		return jLabel4;
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
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
