/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.PorukaDAO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.PorukaVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.util.GUI;
import biz.sunce.util.tablice.sort.JSortTable;

/**
 * datum:2006.11.09
 * @author asabo
 *
 */
public class PoslanePorukePanel extends JPanel implements SlusacModelaTablice 
{

	private javax.swing.JLabel jLabel = null;
	private JXTable podaci = null;
	private javax.swing.JScrollPane jScrollPane = null;
	PorukaDAO pdao=null;
	TableModel model=null;
	/**
	 * This is the default constructor
	 */
	public PoslanePorukePanel() {
		super();
		initialize();
		napuniPodatke();
	}
	
	private final void napuniPodatke()
		{
			final PoslanePorukePanel ja=this;
			new Thread("PunjacPoruka"){
				@Override
				public void run()
				{			
				//try{this.sleep(500);}catch(Exception e){}
				model=new TableModel(pdao,podaci);
				podaci.setModel(model);
				model.dodajSlusaca(ja);
				model.setFilter(null);
			 
				}//run
			}.start();
		}//napuniPodatke
	
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
		this.add(getJScrollPane(), consGridBagConstraints2);
		int faktor = GlavniFrame.getFaktor();
		this.setSize(790*faktor, 580*faktor);
		this.setPreferredSize(new java.awt.Dimension(790*faktor,580*faktor));
		this.setMinimumSize(new java.awt.Dimension(790*faktor,580*faktor));
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Popis poslanih poruka");
		}
		return jLabel;
	}
	/**
	 * This method initializes podaci
	 * 
	 * @return javax.swing.JTable
	 */
	private JXTable getPodaci() {
		if(podaci == null) {
			podaci = new JXTable();
			pdao=DAOFactory.getInstance().getPoruke();
		}
		return podaci;
	}
	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getPodaci());
			jScrollPane.setToolTipText("Tablica sa popisom kreiranih poruka");
		}
		return jScrollPane;
	}

	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj) 
	{
		PorukaVO pvo=null;
		
			if (posiljatelj!=null && posiljatelj.getData()!=null && posiljatelj.getData().size()>redak && redak>=0)
					{
					pvo=(PorukaVO)posiljatelj.getData().get(redak);
				
					if (pvo.getTipPoruke()==PorukaVO.TIP_PORUKE_SMS)
					{
						SMSFrame sms=new SMSFrame();
						
						KlijentVO kvo=null;
						
						if (pvo!=null)
						try 
						{
							kvo=(KlijentVO) DAOFactory.getInstance().getKlijenti().read(Integer.valueOf(pvo.getSifKlijenta()));
						} catch (SQLException e) {
							Logger.log("Problem pri trazenju klijenta "+pvo.getSifKlijenta()+ " za prikazivanje poruke: "+pvo.getSifra().intValue());
							kvo=null;
						}
				
							if (pvo==null) return;
							
						if (kvo!=null) sms.setKlijent(kvo);
						sms.napuniPodatke(pvo);
						sms.disableForme();							
						sms.pack();
						sms.show();
						GUI.centrirajFrame(sms);								
					}//if poruka je SMS
					else
					if (pvo.getTipPoruke()==PorukaVO.TIP_PORUKE_MAIL)
					{
						MailFrame mf=new MailFrame();
						
						KlijentVO kvo=null;
						
						if (pvo!=null)
						try 
						{
							kvo=(KlijentVO) DAOFactory.getInstance().getKlijenti().read(Integer.valueOf(pvo.getSifKlijenta()));
						} catch (SQLException e) {
							Logger.log("Problem pri trazenju klijenta "+pvo.getSifKlijenta()+ " za prikazivanje mail poruke: "+pvo.getSifra().intValue());
							kvo=null;
						}
				
							if (pvo==null) return;
							
						if (kvo!=null) mf.setKlijent(kvo);
						mf.napuniFormu(pvo);
						mf.disableForme();							
						mf.pack();
						mf.show();
						GUI.centrirajFrame(mf);								
					}//if poruka je Mail

				
					}//if
	}//redakOznacen

	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj, TableModel posiljatelj) {
	}
}
