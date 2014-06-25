/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * datum:2006.02.25
 * @author asabo
 *
 */
public final class SlikaPanel extends JPanel {
 
private static final long serialVersionUID = -5178295342773690424L;
private BufferedImage pozadina;

	/**
	 * This is the default constructor
	 */
	public SlikaPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
 
	}

	public void setPozadina(BufferedImage pozadina)
	{
		this.pozadina=pozadina;
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g)
	{
		if (this.pozadina!=null){
		    int w = this.getWidth();
		    int h=this.getHeight();
		    double faktorSirina=1.0d,faktorVisina=1.0d;
		    if (this.pozadina.getWidth()>w)
		    	faktorSirina=(double)((double)w/this.pozadina.getWidth());
		    if (this.pozadina.getHeight()>h)
		    	faktorVisina=(double)((double)h/this.pozadina.getHeight());
		    
		    if (faktorVisina<faktorSirina){
				Image thumbnail = pozadina.getScaledInstance(-1, h, Image.SCALE_SMOOTH);
				g.drawImage(thumbnail, 0, 0, this);
		    }
		    else 
		    if (faktorSirina<faktorVisina){
				Image thumbnail = pozadina.getScaledInstance(w, -1, Image.SCALE_SMOOTH);
				g.drawImage(thumbnail, 0, 0, this);		    	
		    }
		    else
		     g.drawImage(this.pozadina,0,0,this);
		}
	}
}
