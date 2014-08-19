/*
 * Project opticari
 *
 */
package biz.sunce.optika.tablice;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.PregledVO;

/**
 * datum:2005.07.17
 * @author asabo
 *
 */
public class KalendarObvezaCellRenderer extends JPanel implements TableCellRenderer
{
 
	private static final long serialVersionUID = -2836138787367356536L;

	KlijentVO klijent;

	private javax.swing.JLabel jlNaziv = null;
	private javax.swing.JLabel jlAdresa = null;  //  @jve:visual-info  decl-index=0 visual-constraint="8,134"
	/**
	 * This method initializes 
	 * 
	 */
	public KalendarObvezaCellRenderer() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        java.awt.GridLayout layGridLayout1 = new java.awt.GridLayout();
        layGridLayout1.setRows(2);
        layGridLayout1.setColumns(1);
        this.setLayout(layGridLayout1);
        this.add(getJlNaziv(), null);
        this.add(getJlAdresa(), null);
        //this.setSize(94, 52);
        this.setBackground(java.awt.Color.white);
			
	}
	/**
	 * This method initializes jlNaziv
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlNaziv() {
		if(jlNaziv == null) {
			jlNaziv = new javax.swing.JLabel();
			jlNaziv.setText("Pero Periæ");
			jlNaziv.setFont(new java.awt.Font("Batang", java.awt.Font.BOLD, 12));
			jlNaziv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jlNaziv.setBackground(java.awt.Color.white);
			jlNaziv.setForeground(java.awt.SystemColor.textHighlight);
		}
		return jlNaziv;
	}
	/**
	 * This method initializes jlAdresa
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJlAdresa() {
		if(jlAdresa == null) {
			jlAdresa = new javax.swing.JLabel();
			jlAdresa.setText("Periæeva 22, Zagreb");
			jlAdresa.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 10));
			jlAdresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jlAdresa.setBackground(java.awt.Color.white);
			jlAdresa.setForeground(java.awt.Color.gray);
		}
		return jlAdresa;
	}
	public KlijentVO getKlijent() {
		return klijent;
	}
	
	//public   void invalidate(){}
	//public   void validate(){}

	@Override
	public boolean isOpaque() { 
	Color back = getBackground();
	Component p = getParent(); 
	if (p != null) { 
		p = p.getParent(); 
	}
	// p should now be the JTable. 
	boolean colorMatch = (back != null) && (p != null) && 
		back.equals(p.getBackground()) && 
		p.isOpaque();
	return !colorMatch && super.isOpaque(); 
	}

	@Override
	public   void repaint(long tm, int x, int y, int width, int height) {}
	@Override
	public   void repaint(){}
	@Override
	public   void repaint(Rectangle r){}
	@Override
	public   void revalidate(){}
	
	@Override
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {	
		
		if (propertyName == null)
			return;
		
		if (propertyName.equals("text")) {
				super.firePropertyChange(propertyName, oldValue, newValue);
		    }
		}

   @Override
public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { }

			

	@Override
	public void updateUI() 
	{
		super.updateUI(); 
		setForeground(null);
		setBackground(null);
	}
    


	
	public void setValue(Object v)
	{
		if (v==null)
		{
			this.jlNaziv.setText("");
			this.jlAdresa.setText("");
		}
		else
		if (v instanceof PregledVO)		
		{
		PregledVO pvo=(PregledVO)v;
		this.setKlijent(pvo.getKlijent());
		}
	}

	public final void setKlijent(KlijentVO klijent ) 
	{
 
		if (klijent!=null)
		{
		this.jlNaziv.setText(klijent.getIme()+" "+klijent.getPrezime());
		this.jlAdresa.setText(klijent.getAdresa()+", "+klijent.getMjesto().getNaziv());
		}
		else
		{
		this.jlAdresa.setText("");
		this.jlNaziv.setText("");
		}
	}
	public Component getTableCellRendererComponent(JTable tablica, Object vrijednost, boolean selektiran, boolean imaFokus, int redak, int kolona) 
	{
		
		if (vrijednost!=null && (vrijednost instanceof PregledVO))
		{
			KlijentVO kvo=((PregledVO)vrijednost).getKlijent();
			this.setKlijent(kvo);
		}
		else this.setKlijent(null);
		
		return this;
	}//getTableCellRendererComponent

}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
