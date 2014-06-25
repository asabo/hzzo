/*
 * Project opticari
 *
 */
package biz.sunce.util;

/**
 * datum:2005.05.04
 * @author asabo
 *
 */

import java.awt.event.MouseEvent;

import biz.sunce.opticar.vo.ValueObject;

@SuppressWarnings("serial")
public final class Labela extends javax.swing.JLabel implements
		java.awt.event.MouseListener {
	PretrazivanjeProzor prozor;
	Object izvornik;
	boolean oznacena;
	public static final java.awt.Color NORMALNA_BOJA = java.awt.Color.black;
	public static final java.awt.Color OZNACENA_BOJA = java.awt.Color.red;

	private int originalY;

	public Labela(PretrazivanjeProzor prozor) {
		this.prozor = prozor;
		this.addMouseListener(this);
	}

	/**
	 * mouseClicked
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseClicked(MouseEvent e) {
		this.prozor.misKliknut(this);
	}

	/**
	 * mouseEntered
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * mouseExited
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * mousePressed
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * mouseReleased
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseReleased(MouseEvent e) {
	}

	public Object getIzvornik() {
		return izvornik;
	}

	public final void setIzvornik(Object izvornik) {
		setIzvornik(izvornik, null);
	}

	public final void setIzvornik(Object izvornik, String descriptor) {
		this.izvornik = izvornik;
		if (descriptor != null && izvornik != null
				&& izvornik instanceof ValueObject) {
			ValueObject vo = (ValueObject) izvornik;
			Object value = vo.getValue(descriptor);
			String s = "";
			if (value != null) {
				if (value instanceof Number)
					s = "" + ((Number) value).longValue();
				else
					s = value.toString();
			}
			this.setText(s);
		}// if postoji descriptor
		else
			this.setText(izvornik != null ? izvornik.toString() : "?!?");

	}// setIzvornik

	public boolean isOznacena() {
		return oznacena;
	}

	public void setOznacena(boolean oznacena) {
		// mjenjanje boja...
		if (oznacena)
			this.setForeground(Labela.OZNACENA_BOJA);
		else
			this.setForeground(Labela.NORMALNA_BOJA);

		this.oznacena = oznacena;
	}

	public int getOriginalY() {
		return originalY;
	}

	public void setOriginalY(int originalY) {
		this.originalY = originalY;
	}//setOznacena

}// Labela klasa
