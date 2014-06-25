/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.toedter.calendar.JCalendar;

/**
 * datum:2005.07.04
 * @author asabo
 * zaduzen u sebi nositi JCalendar object
 */
public class JCalendarFrame extends JFrame {

	private javax.swing.JPanel jContentPane = null;
	private Vector slusaci=new Vector(1,1);
	JCalendar datum=new JCalendar();   
 
		
	private javax.swing.JButton jbOk = null;
	private javax.swing.JButton jbOdustani = null;
	public interface SlusacIzmjeneDatuma{
	public void datumIzmjenjen(JCalendarFrame pozivatelj);
	}
	/**
	 * This is the default constructor
	 */
	public JCalendarFrame() {
		super();
		initialize();
	}
	
	public void dodajSlusaca(SlusacIzmjeneDatuma slusac)
	{
		this.slusaci.add(slusac);
	}
	 
	  
	 private void dispatchDatumIzmjenjenEvent()
	 {
	 	SlusacIzmjeneDatuma sl;
	 	for (int i=0; i<this.slusaci.size(); i++)
	 	{
	 		sl=(SlusacIzmjeneDatuma)this.slusaci.get(i);
	 		sl.datumIzmjenjen(this);
	 	}
	 }
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(277, 240);
		this.setContentPane(getJContentPane());
		this.setTitle("Odabir datuma");
		this.setName("OdabirDatumaFrame");
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/toedter/calendar/images/JCalendarColor16.gif")));
		 datum.setDecorationBackgroundVisible(false);
		  
		this.addWindowListener(new java.awt.event.WindowAdapter() { 
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {    
				 
					}
		});
	this.initializeLookAndFeels();
	}
	
	public final void initializeLookAndFeels() {
		 // if in classpath thry to load JGoodies Plastic Look & Feel
			 try {
					 UIManager.installLookAndFeel("JGoodies Plastic 3D",
							 "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
					 UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
			 } catch (Throwable t) {
				 try {
			 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 }  catch (Exception e) {
			 e.printStackTrace();
		 }
			 }
	 }//initializeLookAndFeel
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			consGridBagConstraints11.gridy = 1;
			consGridBagConstraints11.gridx = 0;
			consGridBagConstraints11.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints2.gridy = 1;
			consGridBagConstraints2.gridx = 1;
			consGridBagConstraints1.insets = new java.awt.Insets(5,30,17,30);
			consGridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints1.weighty = 1.0;
			consGridBagConstraints1.weightx = 1.0;
			consGridBagConstraints1.gridy = 0;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints1.gridwidth = 2;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getDatum(), consGridBagConstraints1);
			jContentPane.add(getJbOk(), consGridBagConstraints2);
			jContentPane.add(getJbOdustani(), consGridBagConstraints11);
			jContentPane.setToolTipText("odaberite željeni datum ");
			jContentPane.setPreferredSize(new java.awt.Dimension(300,300));
		}
		return jContentPane;
	}
	public JCalendar getDatum() {
		return datum;
	}

	public void setDatum(JCalendar calendar) {
		datum = calendar;
	}

	/**
	 * This method initializes jbOk
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbOk() {
		if(jbOk == null) {
			jbOk = new javax.swing.JButton();
			jbOk.setText("OK");
			jbOk.addMouseListener(new java.awt.event.MouseAdapter() { 
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{    
				 dispatchDatumIzmjenjenEvent();
				 hide();	 
				}
			});
			jbOk.addKeyListener(new java.awt.event.KeyAdapter() {   
				@Override
				public void keyReleased(java.awt.event.KeyEvent e) 
				{    
				if (e!=null && (e.getKeyChar()==KeyEvent.VK_ENTER || e.getKeyChar()==KeyEvent.VK_SPACE))
				{dispatchDatumIzmjenjenEvent(); hide();	} 
				
				} 
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) 
				{
					    
				}
			});
			}//if
			return jbOk;
			}//getJbOk
	/**
	 * This method initializes jbOdustani
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbOdustani() {
		if(jbOdustani == null) {
			jbOdustani = new javax.swing.JButton();
			jbOdustani.setText("Odustani");
			jbOdustani.addMouseListener(new java.awt.event.MouseAdapter() { 
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {    
				hide();	 
				}
			});
			jbOdustani.addKeyListener(new java.awt.event.KeyAdapter() {   
				@Override
				public void keyReleased(java.awt.event.KeyEvent e) {    
					if (e!=null && (e.getKeyChar()==KeyEvent.VK_ENTER || e.getKeyChar()==KeyEvent.VK_SPACE))
					hide();				
					} 
			});
			}
		return jbOdustani;			
	}//getJbOdustani
}   
