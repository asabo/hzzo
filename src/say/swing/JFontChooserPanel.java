package say.swing;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import biz.sunce.util.GUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public final class JFontChooserPanel extends JPanel implements JFontChooserDialog.SlusacFontChooserDialoga
{
	private static final long serialVersionUID = 3605869139220356726L;
	 
	JLabel jlNazivFonta = null;
	JFontChooserDialog dialog = new JFontChooserDialog();
	
	public java.awt.Font getSelectedFont() {
		return dialog==null?null:dialog.getSelectedFont();
	}

	public void setSelectedFont(java.awt.Font font) {
		this.dialog.setSelectedFont(font);
		postaviNazivFonta();
	}

	public JFontChooserPanel() {
		setToolTipText("forma za odabir fonta");
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		
		jlNazivFonta = new JLabel("Naziv fonta");
		jlNazivFonta.setToolTipText("naziv fonta koji ste odabrali\r\n");
		jlNazivFonta.setHorizontalAlignment(SwingConstants.LEFT);
		add(jlNazivFonta);
		
		JButton jbOdaberi = new JButton("...");
		jbOdaberi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			dialog.setVisible(true);
			dialog.pack();
			GUI.centrirajDialog(dialog);
			}
		});
		jbOdaberi.setToolTipText("kliknite kako biste odabrali font");
		add(jbOdaberi);
		postaviNazivFonta();
		dialog.dodajSlusaca(this);
	}
	
	private void postaviNazivFonta()
	{
		Font font = this.getSelectedFont();
		if (font==null)
		{
		this.jlNazivFonta.setText("(nije odabran)");
		return;
		}
		
		String naziv = font.getFontName();
		int vel = font.getSize();
		boolean bold = font.isBold();
		boolean italic = font.isItalic();
		
		String txt = naziv+" "+vel+" "+(bold?"zadebljano":"")+" "+(italic?"ukoso":"");
		
		this.jlNazivFonta.setText(txt);
	}

	public void fontOdabran(JFontChooserDialog pozivatelj, Font odabraniFont) {
		postaviNazivFonta();
	}

}
