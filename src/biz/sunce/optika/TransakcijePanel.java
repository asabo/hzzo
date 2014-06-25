/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.TransakcijeDAO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.opticar.vo.TransakcijaVO;
import biz.sunce.util.GUI;
import biz.sunce.util.tablice.sort.JSortTable;

/**
 * datum:2006.11.06
 * @author asabo
 *
 */
public class TransakcijePanel extends JPanel implements SlusacModelaTablice

{

	private javax.swing.JLabel jLabel = null;
	private JXTable transakcije = null;
	TableModel transModel=null;
	private javax.swing.JScrollPane jspTransakcije = null;
  TransakcijeDAO tdao=null;
	/**
	 * This is the default constructor
	 */
	public TransakcijePanel() {
		super();
		initialize();
		napuniPodatke();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints2.weighty = 1.0;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 1;
		consGridBagConstraints2.gridx = 0;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJLabel(), consGridBagConstraints1);
		this.add(getJspTransakcije(), consGridBagConstraints2);
		this.setSize(790, 580);
		this.setPreferredSize(new java.awt.Dimension(790,580));
		this.setMinimumSize(new java.awt.Dimension(790,580));
		this.setToolTipText("popis transakcija");
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Popis transakcija");
		}
		return jLabel;
	}
	
	private final void napuniPodatke()
	{
		final TransakcijePanel ja=this;
		new Thread("PunjacTranskacija"){
			@Override
			public void run()
			{			
			//try{this.sleep(500);}catch(Exception e){}
			transModel=new TableModel(tdao,transakcije);
			transakcije.setModel(transModel);
			transModel.dodajSlusaca(ja);
			transModel.setFilter(null);
			 
			}//run
		}.start();
	}//napuniPodatke
	
	/**
	 * This method initializes transakcije
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getTransakcije() {
		if(transakcije == null) {
			transakcije = new JXTable();
			transakcije.setToolTipText("tablica sa popisom obavljenih transakcija");
			DAOFactory inst=DAOFactory.getInstance();
			 tdao=inst.getTransakcije();
	
		}//if transakcije null
			return transakcije;
	}//getTransakcije
	/**
	 * This method initializes jspTransakcije
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspTransakcije() {
		if(jspTransakcije == null) {
			jspTransakcije = new javax.swing.JScrollPane();
			jspTransakcije.setViewportView(getTransakcije());
			jspTransakcije.setToolTipText("Popis transakcija...");
		}
		return jspTransakcije;
	}
	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) 
	{
		TransakcijaVO tvo=null;
		
		if (posiljatelj!=null && posiljatelj.getData()!=null && posiljatelj.getData().size()>redak && redak>=0)
				{
				tvo=(TransakcijaVO)posiljatelj.getData().get(redak);
				
				if (tvo.getTip()==TransakcijaVO.TIP_TRANSAKCIJE_ZAHTJEV_ZA_REZERVNIM_DIJELOVIMA)
				{
					ZahtjevZaRezervnimDijelomFrame z=new ZahtjevZaRezervnimDijelomFrame();
					
					z.napuniPodatke(tvo);
					z.disableForm();
					z.pack();
					z.show();
					GUI.centrirajFrame(z);								
				}
				
				}
	}
	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj, TableModel posiljatelj) {
	}
}
