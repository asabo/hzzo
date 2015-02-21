package say.swing;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;

public final class JFontChooserDialog extends JDialog {
	
	JFontChooser chooser = null;
	JFontChooserDialog instanca = this;
	
	public interface SlusacFontChooserDialoga{
		public void fontOdabran(JFontChooserDialog pozivatelj,Font odabraniFont);
	}
	
	ArrayList<SlusacFontChooserDialoga> slusaci = new ArrayList<JFontChooserDialog.SlusacFontChooserDialoga>(1);
	
	public void dodajSlusaca(SlusacFontChooserDialoga slusac)
	{
		this.slusaci.add(slusac);
	}
	
	public JFontChooserDialog() {
		setTitle("Odabir fonta");
		 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {400, 0, 89};
		gridBagLayout.rowHeights = new int[]{284, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		chooser  = new JFontChooser();
		 
		GridBagConstraints gbc_chooser = new GridBagConstraints();
		gbc_chooser.gridwidth = 2;
		gbc_chooser.fill = GridBagConstraints.BOTH;
		gbc_chooser.anchor = GridBagConstraints.NORTHWEST;
		gbc_chooser.gridx = 0;
		gbc_chooser.gridy = 0;
		getContentPane().add(chooser, gbc_chooser);
		
		JButton btnNewButton = new JButton("Spremi");
		btnNewButton.setToolTipText("ako \u017Eelite pohraniti odabrani font, kliknite na Spremi");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (SlusacFontChooserDialoga slusac:slusaci)
				{
					slusac.fontOdabran(instanca,chooser.getSelectedFont());
				}
				setVisible(false);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			setVisible(false);
			}
		});
		btnOdustani.setToolTipText("ako \u017Eelite odustati od odabiranja fonta");
		GridBagConstraints gbc_btnOdustani = new GridBagConstraints();
		gbc_btnOdustani.insets = new Insets(0, 0, 0, 5);
		gbc_btnOdustani.gridx = 1;
		gbc_btnOdustani.gridy = 1;
		getContentPane().add(btnOdustani, gbc_btnOdustani);
	}
 
	private static final long serialVersionUID = -4111701368451773125L;
	
	public void setSelectedFont(Font font)
	{
		instanca.chooser.setSelectedFont(font);
	}
	
	public Font getSelectedFont()
	{
		return instanca.chooser.getSelectedFont();
	}

}
