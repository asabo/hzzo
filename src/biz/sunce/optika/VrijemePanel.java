/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.JPanel;

/**
 * datum:2005.07.11
 * @author asabo
 *
 */
public class VrijemePanel extends JPanel {

	private javax.swing.JTextField jtVrijeme = null;
	/**
	 * This is the default constructor
	 */
	public VrijemePanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		java.awt.FlowLayout layFlowLayout4 = new java.awt.FlowLayout();
		layFlowLayout4.setHgap(0);
		layFlowLayout4.setVgap(0);
		layFlowLayout4.setAlignment(java.awt.FlowLayout.LEFT);
		this.setLayout(layFlowLayout4);
		this.add(getJtVrijeme(), null);
		
		Dimension s=new Dimension(50,20);
		this.setSize(s);
		this.setMaximumSize(s);
		
	}
	/**
	 * This method initializes jtVrijeme
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtVrijeme() {
		if(jtVrijeme == null) {
			jtVrijeme = new javax.swing.JTextField();
			jtVrijeme.setPreferredSize(new java.awt.Dimension(49,20));
			jtVrijeme.setColumns(0);
			jtVrijeme.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			jtVrijeme.addKeyListener(new java.awt.event.KeyAdapter() { 
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) 
				{    
				if (!e.isActionKey() && e.getKeyChar()!=java.awt.event.KeyEvent.VK_BACK_SPACE)
				{
					// krecemo u akciju tek kad se pritisak tipke 'proknjizi' na formi
					new Thread()
					{
						@Override
						public void run()
						{
							try{Thread.sleep(5);}catch(Exception e){}
							provjeriStanjeUPolju();		
						}
					}.start();
				}
				else
				if (e.getKeyChar()==java.awt.event.KeyEvent.VK_BACK_SPACE)
				{
					String t=jtVrijeme.getText();
					int ti=t.indexOf(":");
					if (ti!=-1 && jtVrijeme.getCaretPosition()==ti+1)
					{
						String t2="";
						String[] tmp=t.split(":");
						for (int i=0; i<tmp.length; i++) t2+=tmp[i];
						jtVrijeme.setText(t2); 
					}//if
				}//if
				}
			});
			jtVrijeme.addFocusListener(new java.awt.event.FocusAdapter() { 
				@Override
				public void focusLost(java.awt.event.FocusEvent e) 
				{    
	 				int s,m;
	 				s=getSat();
	 				m=getMinuta();

	 				jtVrijeme.setText(pretvoriVrijemeUString(s,m));
	 			}
			});
		}
		return jtVrijeme;
	}
	
  private String pretvoriVrijemeUString(int s, int m)
  {
	if (s<0) s=0;
	if (m<0) m=0;
	if (s>24) s=s%24;
	if (m>60) m=m%60;
	String ss,sm;
	ss=s<10?"0"+s:""+s;
	sm=m<10?"0"+m:""+m;
	
	return ss+":"+sm;		
  }
  
  public void setVrijeme(java.util.Calendar datum)
  {
		int sat,min;
		sat=datum.get(Calendar.HOUR_OF_DAY);
		min=datum.get(Calendar.MINUTE);
   this.setVrijeme(sat,min);
  }

	public void setVrijeme(int sat, int minuta)
	{
		jtVrijeme.setText(pretvoriVrijemeUString(sat,minuta));
	}
	
	public void pobrisiFormu()
	{
		this.jtVrijeme.setText("");
	}
	
	//zaduzena pogledati sta je upisano u polju i dodati dvotocku di treba
	private void provjeriStanjeUPolju()
	{
	 String tekst=this.getJtVrijeme().getText();
	 int tl=tekst.length();
	 int pozicija=this.getJtVrijeme().getCaretPosition();
	 
	 
	 if (tl>0)
	 if (tekst.indexOf(":")==-1)
	  if (tl<2) tekst+=":";
	  else
	  tekst=tekst.substring(0,2)+":"+(tekst.length()>2?tekst.substring(2):"");
	  
	  // sad ga jos splitamo po dvotocki i podijelimo na jednake dijelove 
	  String[] dj=tekst.split(":");
	  
	  String h,m;
	  
	  
	  if (dj[0].length()<2){ h=dj[0]; m="";}
	  else
	  {
	  	h=dj[0].substring(0,2);
	  	m=dj[0].substring(2)+(dj.length==2?dj[1]:"");
	  	pozicija++;//pretakanje s lijeve na desnu stranu vodi i pomicanju kursora..
	  }
	  
	  tekst=h+":"+m;
	  
	  this.getJtVrijeme().setText(tekst);
  	try{this.getJtVrijeme().setCaretPosition(pozicija);}
  	catch(IllegalArgumentException ilea){}		
	}//provjeriStanjeUPolju
	
	public int getSat()
		{
			int sat;
			String s=this.getJtVrijeme().getText();
			try{
				if (s.length()>2) s=s.substring(0,2); 
				
				sat=Integer.parseInt(s);
			}
			catch(NumberFormatException nfe)
			{
				sat=-1;
			}
 		
			return sat;		
		}//getSat

		public int getMinuta()
		{
			int minuta;
			String s=this.getJtVrijeme().getText();
			try{
				if (s.length()<4) return -1;
				s=s.substring(3);
				  				
				minuta=Integer.parseInt(s);
			}
			catch(NumberFormatException nfe)
			{
				minuta=-1;
			}
		
		 
			return minuta;		
		}//getMinuta
		
	public void omoguci(){this.postaviStatuseElemenata(true);}
				 public void onemoguci(){this.postaviStatuseElemenata(false);}
				 public void postaviStatuseElemenata(boolean s)
				 {
					 this.getJtVrijeme().setEnabled(s);
				 }//postaviStatuseElemenata
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
