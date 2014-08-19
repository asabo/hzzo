/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Dimension;

import javax.swing.JPanel;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.util.gui.DaNeUpit;
import biz.sunce.util.tablice.sort.JSortTable;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.opticar.vo.PorukaSustavaVO;

/**
 * datum:2006.02.23
 * @author asabo
 *
 */
public class PorukeSustavaPanel extends JPanel implements SlusacModelaTablice
{

	private JXTable jtbPorukeSustava = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private TableModel porukeModel=null;
	private javax.swing.JButton jbIzbrisiLog = null;
	/**
	 * This is the default constructor
	 */
	public PorukeSustavaPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		consGridBagConstraints11.gridy = 0;
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints1.weighty = 1.0;
		consGridBagConstraints1.weightx = 1.0;
		consGridBagConstraints1.gridy = 1;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.insets = new java.awt.Insets(25,0,0,0);
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJScrollPane(), consGridBagConstraints1);
		this.add(getJbIzbrisiLog(), consGridBagConstraints11);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(800*faktor, 600*faktor);
		this.setMinimumSize(new Dimension(500*faktor, 200*faktor));
		this.setToolTipText("poruke sustava...");
	}
	/**
	 * This method initializes jtbPorukeSustava
	 *
	 * @return javax.swing.JTable
	 */
	private JXTable getJtbPorukeSustava() {
		if(jtbPorukeSustava == null) {
			jtbPorukeSustava = new JXTable();
			porukeModel=new TableModel(DAOFactory.getInstance().getPorukeSustava(),jtbPorukeSustava);
			jtbPorukeSustava.setModel(porukeModel);
      porukeModel.dodajSlusaca(this);
			porukeModel.setFilter(null);
			jtbPorukeSustava.setToolTipText("poruke sustava");
		}
		return jtbPorukeSustava;
	}
	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getJtbPorukeSustava());
			jScrollPane.setPreferredSize(new java.awt.Dimension(800,600));

			jScrollPane.setToolTipText("poruke sustava");
		}
		return jScrollPane;
	}

    public void redakOznacen(int redak, MouseEvent event,
                             TableModel posiljatelj)
    {
        if (event.getClickCount()==2)
        {
            PorukaSustavaVO psvo=(PorukaSustavaVO)posiljatelj.getData().get(redak);

             PorukaSustavaFrame psf=new PorukaSustavaFrame();
             psf.setPoruka(psvo);
             psf.show();
             biz.sunce.util.GUI.centrirajFrame(psf);
        }
    }

    public void redakIzmjenjen(int redak, TableModelEvent dogadjaj,
                               TableModel posiljatelj) {
    }
	/**
	 * This method initializes jbIzbrisiLog
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIzbrisiLog() {
		if(jbIzbrisiLog == null) {
			jbIzbrisiLog = new javax.swing.JButton();
			jbIzbrisiLog.setText("Izbriši log");
			jbIzbrisiLog.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{    
				
				if (DaNeUpit.upit("Želite izbrisati sve poruke sustava?","brisanje loga",GlavniFrame.getInstanca()))
				{
					try {
						DAOFactory.getInstance().getPorukeSustava().delete("sve");
						porukeModel.setFilter(null);
					} catch (SQLException e1) {
					 Logger.fatal("SQL iznimka kod brisanja poruka iz system_loga",e1);
					}						
				}
				}
			});
		}
		return jbIzbrisiLog;
	}
}
