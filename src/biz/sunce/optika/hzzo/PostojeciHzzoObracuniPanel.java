/*
 * Project opticari
 *
 */
package biz.sunce.optika.hzzo;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.HzzoObracunVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.util.GUI;

/**
 * datum:2006.04.09
 * 
 * @author asabo
 * 
 */
public final class PostojeciHzzoObracuniPanel extends JPanel implements
		SlusacModelaTablice<HzzoObracunVO> {
	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel jpPretrazivanjeKriteriji = null;
	private javax.swing.JScrollPane jspObracuni = null;
	private JXTable obracuni = null;
	private TableModel<HzzoObracunVO> obracuniModel = null;

	private javax.swing.JLabel jLabel = null; // @jve:visual-info decl-index=0
												// visual-constraint="371,571"

	/**
	 * This is the default constructor
	 */
	public PostojeciHzzoObracuniPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
		consGridBagConstraints5.gridx = 0;
		consGridBagConstraints5.gridy = 1;
		consGridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints4.weighty = 1.0;
		consGridBagConstraints4.weightx = 1.0;
		consGridBagConstraints4.gridy = 2;
		consGridBagConstraints4.gridx = 0;
		consGridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints3.fill = java.awt.GridBagConstraints.NONE;
		consGridBagConstraints3.weighty = 1.0;
		consGridBagConstraints3.weightx = 1.0;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints3.gridx = 0;
		consGridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTH;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJpPretrazivanjeKriteriji(), consGridBagConstraints3);
		this.add(getJspObracuni(), consGridBagConstraints4);
		this.add(getJLabel(), consGridBagConstraints5);
		int faktor = GlavniFrame.getFaktor();
		//this.setSize(790*faktor, 560*faktor);
		this.setPreferredSize(new java.awt.Dimension(790, 560));

		Thread t = new Thread() {
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				GlavniFrame.getInstanca().busy();
				getObracuni().packAll();
				obracuni.setModel(obracuniModel);
				yield();
				obracuniModel.reload();
				GlavniFrame.getInstanca().idle();				
			}
		};

		SwingUtilities.invokeLater(t);
	}

	/**
	 * This method initializes jpPretrazivanjeKriteriji
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpPretrazivanjeKriteriji() {
		if (jpPretrazivanjeKriteriji == null) {
			int faktor = GlavniFrame.getFaktor();
			jpPretrazivanjeKriteriji = new javax.swing.JPanel();
			jpPretrazivanjeKriteriji.setLayout(new java.awt.GridBagLayout());
			jpPretrazivanjeKriteriji.setPreferredSize(new java.awt.Dimension(
					500*faktor, 200*faktor));
			jpPretrazivanjeKriteriji.setMinimumSize(new java.awt.Dimension(500*faktor,
					100*faktor));

		}
		return jpPretrazivanjeKriteriji;
	}

	/**
	 * This method initializes obracuni
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getObracuni() {
		if (obracuni == null) {
			obracuni = new JXTable();
			obracuni.setToolTipText("dvostrukim klikom otvarate podatke o obraèunu");

			this.obracuniModel = new TableModel<HzzoObracunVO>(DAOFactory.getInstance()
					.getHzzoObracuni(), this.obracuni);
			this.obracuniModel.dodajSlusaca(this);
			
		}
		return obracuni;
	}

	/**
	 * This method initializes jspObracuni
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspObracuni() {
		if (jspObracuni == null) {
			jspObracuni = new javax.swing.JScrollPane();
			jspObracuni.setViewportView(getObracuni());
			jspObracuni
					.setToolTipText("dvostrukim klikom otvarate obraèun, ako želite ponovno kreirati disketu ili ispisati dopis");
			jspObracuni.setPreferredSize(new java.awt.Dimension(580, 400));
			jspObracuni.setMinimumSize(new java.awt.Dimension(580, 350));
		}
		return jspObracuni;
	}

	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Popis kreiranih obraèuna: ");
		}
		return jLabel;
	}

	public void redakOznacen(final int redak, MouseEvent event,
			TableModel<HzzoObracunVO> posiljatelj) {
		if (event.getClickCount() == 2) {
			Thread t = new Thread() {

				public void run() {
					this.setPriority(Thread.MIN_PRIORITY);
					GlavniFrame.glavniFrameBusy();
					HzzoObracunVO hvo = (HzzoObracunVO) obracuniModel.getData()
							.get(redak);
					HzzoKreiranjeObracuna obr = new HzzoKreiranjeObracuna(hvo, false);
					obr.enableIspravak(); // da se moze ev. kliknuti na ispravak
											// obracuna

					obr.setVisible(true);
					GUI.centrirajFrame(obr);
					GlavniFrame.glavniFrameIdle();
				}
			};

			SwingUtilities.invokeLater(t);
		}// if
	}

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
			TableModel<HzzoObracunVO> posiljatelj) {
	}
} // @jve:visual-info decl-index=0 visual-constraint="10,10"
