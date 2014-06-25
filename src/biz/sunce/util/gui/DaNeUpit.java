/*
 * Project opticari
 *
 */
package biz.sunce.util.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * datum:2006.02.06
 * 
 * @author asabo
 * 
 */
public class DaNeUpit {
	static Object[] daNeOdgovori = { "Da", "Ne" };

	public static boolean upit(String upit, String naslovUpita, JFrame frame) {
		return upit(upit, naslovUpita, frame, true);
	}

	public static boolean upit(String upit, String naslovUpita, JFrame frame,
			boolean daOznacen) {
		return JOptionPane.showOptionDialog(frame, upit, naslovUpita,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, daNeOdgovori, daOznacen ? daNeOdgovori[0]
						: daNeOdgovori[1]) == 0;
	}// upit
}// klasa