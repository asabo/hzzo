/*
 * Project opticari
 *
 */
package biz.sunce.util;

import java.awt.Color;
import java.awt.GraphicsConfiguration;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * datum:2005.05.01
 * 
 * @author asabo
 *
 */
public final class GUI {

	public static void centrirajFrame(JFrame frame) {
		int sir = (int) frame.getSize().getWidth();
		int duz = (int) frame.getSize().getHeight();
		double cx = 0d;
		double cy = 0d;

		GraphicsConfiguration gc = frame.getGraphicsConfiguration();

		if (gc == null)
			return;
		else {
			cx = gc.getBounds().getCenterX();
			cy = gc.getBounds().getCenterY();

			cx -= sir / 2;
			cy -= duz / 2;

			frame.setBounds((int) cx, (int) cy, sir, duz);
		}
		return;
	}

	public static void centrirajDialog(JDialog frame) {
		int sir = (int) frame.getSize().getWidth();
		int duz = (int) frame.getSize().getHeight();
		double cx = 0d;
		double cy = 0d;

		GraphicsConfiguration gc = frame.getGraphicsConfiguration();

		if (gc == null)
			return;
		else {
			cx = gc.getBounds().getCenterX();
			cy = gc.getBounds().getCenterY();

			cx -= sir / 2;
			cy -= duz / 2;

			frame.setBounds((int) cx, (int) cy, sir, duz);
		}
		return;
	}

	public static final void odradiUpozorenjeNaElementu(final JComponent komponenta,
			final String poruka) {
	odradiUpozorenjeNaElementu(komponenta, poruka, Color.red);
	}
	public static final void odradiUpozorenjeNaElementu(final JComponent komponenta,
			final String poruka, final Color boja) {
		new Thread() {
			public void run() 
			{
				String staraPoruka=null;
				
				if(poruka!=null)
				{
					staraPoruka=komponenta.getToolTipText();
					komponenta.setToolTipText(poruka);
					komponenta.getToolTipText(null);
				}
				
					komponenta.setBackground(boja);
				try {
					sleep(500);
				} catch (InterruptedException inte) {
					return;
				}

				komponenta.setBackground(Color.white);
				try {
					sleep(500);
				} catch (InterruptedException inte) {
					return;
				}

				komponenta.setBackground(boja);
				try {
					sleep(500);
				} catch (InterruptedException inte) {
					return;
				}

				komponenta.setBackground(Color.white);
				try {
					sleep(500);
				} catch (InterruptedException inte) {
					return;
				}
				
				if (poruka!=null)
				{
					komponenta.setToolTipText(staraPoruka);
				}
			}
		}.start();

	}
}
