/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import biz.sunce.util.Util;
import com.toedter.calendar.JCalendar;


/**
 * datum:2005.05.24
 * @author asabo
 *
 */
public class DatumPanel extends JPanel implements JCalendarFrame.SlusacIzmjeneDatuma
{
	public interface SlusacDatumPanela
	{
		public void datumIzmjenjen(DatumPanel pozivatelj);
	}
	
	private javax.swing.JTextField jtDatum = null;
	private javax.swing.JButton jbDatum = null;
	private JCalendar jcDatum = null;
	private Vector slusaci=null;
	private JCalendarFrame jcFrame=new JCalendarFrame();  //  @jve:visual-info  decl-index=0 visual-constraint="238,14"
	/**
	 * This is the default constructor
	 */
	public DatumPanel() 
	{
		super();
		jcDatum=jcFrame.getDatum(); // mora ici prije initialize()
		initialize();
		this.jcFrame.dodajSlusaca(this);
	}
	public void setTekst(String tekst)
	{
		//this.datum.setText(tekst);
	}
 
	public void setDatum(Calendar datum)
	{	
		this.getJtDatum().setText(Util.convertCalendarToStringForForm(datum));
	}
	
	private void obavijestiSlusaceOPromjeniDatuma()
	{
		if (this.slusaci!=null)
		for (int i=0; i<this.slusaci.size(); i++)
		{
			SlusacDatumPanela sl=(SlusacDatumPanela)this.slusaci.get(i);
			sl.datumIzmjenjen(this);
		}
	}//obavijestiSlusaceOPromjeniDatuma
	
	 public java.util.Calendar getDatum()
		{
			java.util.Calendar tmp=null;
 		
			tmp=Util.provjeriIspravnostDatuma(this.getJtDatum().getText());

			if (tmp==null) return null;

			// postavimo vrijeme na nulu... 
			tmp.set(java.util.Calendar.HOUR_OF_DAY,0);
			tmp.set(java.util.Calendar.MINUTE,0);
			tmp.set(Calendar.SECOND,0);
			tmp.set(Calendar.MILLISECOND,0);
		
			return tmp;
		}//getDatum

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		consGridBagConstraints2.ipady = 0;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 1;
		consGridBagConstraints1.ipadx = -84;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints1.weightx = 1.0;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.gridx = 0;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJtDatum(), consGridBagConstraints1);
		this.add(getJbDatum(), consGridBagConstraints2);
		Dimension d=new java.awt.Dimension(105,20);
		this.setSize(d);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
	}
	/**
	 * This method initializes jtDatum
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtDatum() {
		if(jtDatum == null) {
			jtDatum = new javax.swing.JTextField();
			Dimension d=new java.awt.Dimension(100,20);
			jtDatum.setPreferredSize(new java.awt.Dimension(90,20));
			jtDatum.setMaximumSize(d);
			jtDatum.setMinimumSize(d);
			jtDatum.setToolTipText("pronaðite željeni datum");
			jtDatum.addFocusListener(new java.awt.event.FocusAdapter() { 
				@Override
				public void focusLost(java.awt.event.FocusEvent e) 
				{
				if (getDatum()!=null)    
	 			obavijestiSlusaceOPromjeniDatuma();	
	 			}
			});
		}
		return jtDatum;
	}
	/**
	 * This method initializes jbDatum
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbDatum() 
	{
		
		if(jbDatum == null) 
		{
			Dimension d=new Dimension(20,20);
	  	URL iconURL = this.jcDatum.getClass().getResource("images/" + this.jcDatum.getName() +
						"Color16.gif");
		ImageIcon icon = null;
		
		try{		
		if (iconURL!=null)
		icon=new ImageIcon(iconURL);
		}	
		catch(Exception cnfe)
		{}
		
		jbDatum = new javax.swing.JButton();
		if (icon!=null)
		jbDatum.setIcon(icon);
		

		jbDatum.setMaximumSize(d);
		jbDatum.setMinimumSize(d);
		jbDatum.setPreferredSize(d);
		jbDatum.setSize(d);
		
		jbDatum.setMargin(new Insets(0,0,0,0));
		jbDatum.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		jbDatum.setMnemonic('d');
		jbDatum.addMouseListener(new java.awt.event.MouseAdapter() { 
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) 
			{    
				java.util.Calendar tmp=null; 		
				tmp=Util.provjeriIspravnostDatuma(getJtDatum().getText());
					
				if (tmp!=null) jcDatum.setCalendar(tmp);
				//jcDatum.setEnabled(true);
					
				jcFrame.pack();
				jcFrame.show();		
				biz.sunce.util.GUI.centrirajFrame(jcFrame);				
			}
		});
		}//if jbDatum==null
		return jbDatum;
	}
	public void datumIzmjenjen(JCalendarFrame pozivatelj) 
	{
		this.jtDatum.setText(Util.convertCalendarToStringForForm(pozivatelj.getDatum().getCalendar()));
		
		if (getDatum()!=null)
		obavijestiSlusaceOPromjeniDatuma();
	}
	public Vector getSlusaci() {
		if (slusaci==null) slusaci=new Vector(2,1);
		return slusaci;
	}

	public void dodajSlusaca(SlusacDatumPanela slusac) 
	{	
		getSlusaci().add(slusac);
	}

}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
