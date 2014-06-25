/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.TransakcijeDAO;
import biz.sunce.opticar.vo.TransakcijaVO;

/**
 * datum:2006.01.31
 * @author asabo
 *
 */
public class ZahtjevZaIntervencijomFrame extends JFrame {

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel jLabel = null;
	private javax.swing.JButton jbPosalji = null;
	private javax.swing.JButton jbOdustani = null;
	private javax.swing.JTextArea jtaOpisProblema = null;
	/**
	 * This is the default constructor
	 */
	public ZahtjevZaIntervencijomFrame() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Zahtjev za intervencijom u sustav");
	}
	
	private void spremiZahtjev()
	{
		TransakcijeDAO tdao=DAOFactory.getInstance().getTransakcije();
	
		TransakcijaVO tvo=new TransakcijaVO();
		
		tvo.setNapomena(this.jtaOpisProblema.getText());
		tvo.setOdlazna(true);
	  
		tvo.setTip(TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_INTERVENCIJOM);
	
		try
		{
		tdao.insert(tvo);
		info("Zahtjev za intervencijom zabilježen, obavite sinkronizaciju što prije\nkako bi zahtjev što prije stigao na odredište!");
		this.dispose();
		}
		catch(SQLException sqle)
		{
			Logger.fatal("SQL iznimka kod zapisivanja zahtjeva za intervencijom u sustav",sqle);
			alert("Zahtjev za intervencijom nije zabiljezen, molimo kontaktirajte administratora sustava!");
		}
	}//spremiZahtjev
	private void alert(String poruka)
	{JOptionPane.showMessageDialog(this,poruka,"Upozorenje",JOptionPane.WARNING_MESSAGE);}
	
	private void info(String poruka)
	{JOptionPane.showMessageDialog(this,poruka,"Obavijest",JOptionPane.INFORMATION_MESSAGE);}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			consGridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints5.weighty = 1.0;
			consGridBagConstraints5.weightx = 1.0;
			consGridBagConstraints5.gridy = 1;
			consGridBagConstraints5.gridx = 0;
			consGridBagConstraints5.gridwidth = 2;
			consGridBagConstraints3.gridy = 2;
			consGridBagConstraints3.gridx = 0;
			consGridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints4.gridy = 2;
			consGridBagConstraints4.gridx = 1;
			consGridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints1);
			jContentPane.add(getJbPosalji(), consGridBagConstraints3);
			jContentPane.add(getJbOdustani(), consGridBagConstraints4);
			jContentPane.add(getJtaOpisProblema(), consGridBagConstraints5);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Opis problema: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jbPosalji
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbPosalji() {
		if(jbPosalji == null) {
			jbPosalji = new javax.swing.JButton();
			jbPosalji.setText("Pošalji");
			jbPosalji.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{    
			 	spremiZahtjev();
			 	}
			});
		}
		return jbPosalji;
	}
	/**
	 * This method initializes jbOdustani
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbOdustani() {
		if(jbOdustani == null) {
			jbOdustani = new javax.swing.JButton();
			jbOdustani.setText("Odustani");
			jbOdustani.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{dispose();}
			});
		}
		return jbOdustani;
	}
	/**
	 * This method initializes jtaOpisProblema
	 * 
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getJtaOpisProblema() {
		if(jtaOpisProblema == null) {
			jtaOpisProblema = new javax.swing.JTextArea();
		}
		return jtaOpisProblema;
	}
}
