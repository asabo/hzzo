package biz.sunce.util;

/**
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: ANSA
 * </p>
 * 
 * @author Ante Sabo
 * @version 1.0
 */

public final class SlikovniContainer extends javax.swing.JPanel {

	private static final long serialVersionUID = -5411830258295809384L;
	java.awt.image.BufferedImage slika;

	public SlikovniContainer(java.awt.image.BufferedImage slika) {
		super();
		this.slika = slika;
		this.setLayout(null);
	}

	@Override
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		if (slika != null)
			g.drawImage(this.slika, 0, 0, this.getWidth(), this.getHeight(),
					null);
	}

	public void setSlika(java.awt.image.BufferedImage slika) {
		this.slika = slika;
	}

	public java.awt.image.BufferedImage getSlika() {
		return this.slika;
	}
}// klasa
