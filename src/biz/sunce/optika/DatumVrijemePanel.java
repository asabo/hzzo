/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import biz.sunce.util.Util;
import com.toedter.calendar.JCalendar;

/**
 * datum:2005.05.30
 * @author asabo
 *
 */
public class DatumVrijemePanel extends JPanel implements JCalendarFrame.SlusacIzmjeneDatuma
{

	private JTextField jtDatum = null;
	private JCalendar jcDatum = null;
	private JCalendarFrame jcFrame=new JCalendarFrame();
	private javax.swing.JLabel jLabel = null;
	TitledBorder rub=null;
	private javax.swing.JButton jbDatum = null;
	private javax.swing.JButton jbSad = null;
	private VrijemePanel jpVrijeme = null;
	/**
	 * This is the default constructor
	 */
	public DatumVrijemePanel() {
		super();
		// u jcFrame ce se prikazivati jcDatum komponenta
			jcDatum=jcFrame.getDatum();
			jcFrame.dodajSlusaca(this); // prijeavljujemo se za slusanje izmjene datuma

			//jcDatum.setEnabled(false); // po defaultu je iskljucen
  		initialize();
	}
	public void setNaslov(String naslov)
	{
		this.rub.setTitle(naslov);
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		Dimension d=new Dimension(233, 55);
		this.add(getJtDatum(), null);
		//this.add(getJbDatum(), null);
		this.add(getJLabel(), null);
		this.add(getJpVrijeme(), null);
		this.add(getJbSad(), null);
		this.setSize(d);
		this.setPreferredSize(d);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
		this.rub=javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(BevelBorder.RAISED), "Naslov", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null);
		this.setBorder(rub);
		jcDatum.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {

			}
		});
	}
	/**
	 * This method initializes jtDatum
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getJtDatum() {
		if(jtDatum == null) {
			jtDatum = new JTextField();
			jtDatum.setPreferredSize(new java.awt.Dimension(70,20));
		}
		return jtDatum;
	}
	/**
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText(" u ");
		}
		return jLabel;
	}
	public java.util.Calendar getDatum()
	{
		java.util.Calendar tmp=null;

		tmp=Util.provjeriIspravnostDatuma(this.getJtDatum().getText());

		if (tmp==null) return null;

		int sat,min;
		sat=this.getSat();
		min=this.getMinuta();

		if (sat==-1) return null;
		if (min==-1) return null;


		// postavimo vrijeme...
		tmp.set(java.util.Calendar.HOUR_OF_DAY,sat);
		tmp.set(java.util.Calendar.MINUTE,min);
		tmp.set(Calendar.SECOND,0);
		tmp.set(Calendar.MILLISECOND,0);

		return tmp;
	}//getDatum

	public void pobrisiFormu()
	{
		final String p="";
		this.getJtDatum().setText(p);
	  this.getJpVrijeme().pobrisiFormu();
    this.onemoguci();
	}
 
	// zaduzena SAMO za postavljanje datumskog dijela (ne i vrijeme)
	private void setDatum(Calendar datum)
	{
		this.getJtDatum().setText(Util.convertCalendarToStringForForm(datum));
	}

	public void setDatumVrijeme(Calendar datum)
	{
		if(datum==null) {this.pobrisiFormu(); return;}
	   else this.omoguci();

		this.setDatum(datum); // postavlja SAMO datum u formu

		this.getJpVrijeme().setVrijeme(datum);

	}//setDatum

	public int getSat()
	{
	 return this.getJpVrijeme().getSat();
	}//getSat

	public int getMinuta()
	{
   return this.getJpVrijeme().getMinuta();
 	}//getMinuta


	private void alert(String poruka)
	{JOptionPane.showMessageDialog(this,poruka);}

 	/**
	 * This method initializes jbSad
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbDatum() {
		if(jbDatum == null) {
			Dimension d=new Dimension(16,16);
			URL iconURL = this.jcDatum.getClass().getResource("images/" + this.jcDatum.getName() +
							"Color16.gif");
			ImageIcon icon = new ImageIcon(iconURL);

			jbDatum = new javax.swing.JButton(icon);
			jbDatum.setSize(d);
			jbDatum.setMaximumSize(d);
			jbDatum.setMinimumSize(d);
			jbDatum.setPreferredSize(d);
			jbDatum.setMargin(new Insets(0,0,0,0));
			jbDatum.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
					// ako si 'zamrznut' nemas sta registrirati dogadjaje...
					if (!jbDatum.isEnabled()) return;
					
					java.util.Calendar tmp=null;
					tmp=Util.provjeriIspravnostDatuma(getJtDatum().getText());

					if (tmp!=null) jcDatum.setCalendar(tmp);
					//jcDatum.setEnabled(true);

					jcFrame.pack();
                                        jcFrame.show();
					biz.sunce.util.GUI.centrirajFrame(jcFrame);
				}
			});
		}
		return jbDatum;
	}
	public void datumIzmjenjen(JCalendarFrame pozivatelj)
	{
		// setira SAMO datum (ne i vrijeme) koje je u frameu korisnik promjenio
	this.setDatum(this.jcDatum.getCalendar());
	}
	/**
	 * This method initializes jbSad
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbSad() {
		if(jbSad == null) {
			jbSad = new javax.swing.JButton();
			jbSad.setText("");
			jbSad.setPreferredSize(new java.awt.Dimension(17,13));
			jbSad.setToolTipText("ako želite postaviti trenutno vrijeme, pritisnite ovaj gumb!");
			jbSad.setMargin(new Insets(0,0,0,0));
			jbSad.setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.PLAIN, 8));
			jbSad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/biz/sunce/icons/arrow_left.gif")));
			jbSad.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			jbSad.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
				if (!jbSad.isEnabled()) return;
	       setDatumVrijeme(Calendar.getInstance());
	 			}
			});
		}
		return jbSad;
	}
	
	public void omoguci(){this.postaviStatuseElemenata(true);}
				 public void onemoguci(){this.postaviStatuseElemenata(false);}
				 public void postaviStatuseElemenata(boolean s)
				 {
						  //this.getJbDatum().setEnabled(s);
						  this.getJbSad().setEnabled(s);
						  this.getJLabel().setEnabled(s);
						  this.getJpVrijeme().postaviStatuseElemenata(s);
						  this.getJtDatum().setEnabled(s);
				 }//postaviStatuseElemenata
	
	/**
	 * This method initializes jpVrijeme
	 *
	 * @return javax.swing.JPanel
	 */
	private VrijemePanel getJpVrijeme() {
		if(jpVrijeme == null) {
			jpVrijeme = new VrijemePanel();
		}
		return jpVrijeme;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
