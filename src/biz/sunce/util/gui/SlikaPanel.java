/*
 * Project opticari
 *
 */
package biz.sunce.util.gui;

import javax.swing.*;

import biz.sunce.optika.Logger;
import biz.sunce.util.PictureUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.IOException;

public class SlikaPanel extends JPanel implements MouseListener{
 Image slika;
 java.awt.image.BufferedImage bufSlika;
 FontMetrics fm;
 final String nemaSlike="(nema slike)";
 int fmduz,fmvis;
 
 boolean updatable;

 public SlikaPanel()
 {
 	super();
 	this.updatable=true;
 	this.bufSlika=null;
 	this.addMouseListener(this);
 }

	public SlikaPanel(boolean updatable) {
		super();
		this.slika=null;
		this.updatable=updatable;
		this.bufSlika=null;
		this.addMouseListener(this);
	}



@Override
public void paint(Graphics g)
{
		super.paint(g);
		if (slika!=null)
		g.drawImage( slika, this.getWidth()/2-slika.getWidth(null)/2, this.getHeight()/2-slika.getHeight(null)/2, this);
		else
		{
			if (fm==null) {fm=g.getFontMetrics(g.getFont()); fmduz=fm.stringWidth(nemaSlike)/2; fmvis=g.getFont().getSize()/2;}
				
			g.setColor(this.getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g.setColor(Color.BLACK);
			g.drawString(nemaSlike,this.getWidth()/2-fmduz,this.getHeight()/2-fmvis);		
		}
}//paint

public void setSlika(BufferedImage slika)
{
// sliku cemo smanjiti po siroj koordinati tako da uvijek cijela upadne unutra
if (slika==null) {this.bufSlika=null; this.slika=null; return;}

int sir,vis;
sir=slika.getWidth();
vis=slika.getHeight();

if (sir>this.getWidth() && (sir>=vis))
slika=PictureUtil.preformatirajSliku(slika,this.getWidth());
else
if (vis>this.getHeight() && (vis>=sir))
slika=PictureUtil.preformatirajSlikuPoDuzini(slika,this.getHeight());

this.slika=slika;
this.bufSlika=slika;
this.repaint();
}

public void setSlika(Image slika)
{
this.slika=slika;
this.bufSlika=null;
}

public BufferedImage getSlika()
{
	return this.bufSlika;
}



public void mouseClicked(MouseEvent ev) 
{
  	java.awt.image.BufferedImage bi=null;
	  java.awt.image.BufferedImage bufSlika=null;

	 if (ev.getModifiers()==InputEvent.BUTTON3_MASK)
	 {

		 Clipboard podaci;
		 podaci=this.getToolkit().getSystemClipboard();

		 boolean dozvoljeno=false;

		try {

		dozvoljeno=podaci.getContents("System").isDataFlavorSupported(DataFlavor.imageFlavor);
		if (dozvoljeno)
		{
		bi=(BufferedImage)podaci.getContents("System").getTransferData(DataFlavor.imageFlavor);

		bufSlika=new BufferedImage(bi.getWidth(), bi.getHeight(),BufferedImage.TYPE_INT_RGB);

				bufSlika.setData(bi.getData());

		 this.setSlika(bufSlika);
		} // if dozvoljeno
		
		// otkaci se odmah sa sistemskog clipboarda, da ne bi bilo problema...
		bi=null;
		bufSlika=null;
		podaci=null;
		} // try
		catch (IOException ex) {
		Logger.fatal("IO Iznimka kod prebacivanja slike sa clipboarda ",ex);
	}catch (UnsupportedFlavorException ex) {
		Logger.fatal("UnsupportedFlavor Iznimka kod prebacivanja slike sa clipboarda ",ex); 
	}
	 } // if desni klik
}//mouseClicked

public void mouseEntered(MouseEvent arg0) {}

public void mouseExited(MouseEvent arg0) {}

public void mousePressed(MouseEvent arg0) {}

public void mouseReleased(MouseEvent arg0) {}

} //klasa
