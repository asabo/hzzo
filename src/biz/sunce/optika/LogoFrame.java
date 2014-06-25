package biz.sunce.optika;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import javax.swing.JFileChooser;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class LogoFrame extends javax.swing.JFrame {
	private SlikaPanel jcSlika;
	private JButton jbOdaberiDatoteku;
	private JButton jbSpremi;
	private JButton jbOdustani;
	private boolean slikaPostavljena;

	/**
	* Auto-generated main method to display this JFrame
	*/
		
	public LogoFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			pack();
			GridBagLayout thisLayout = new GridBagLayout();
			this.setTitle("Logo tvrtke");
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				jcSlika = new SlikaPanel();
				getContentPane().add(jcSlika, new GridBagConstraints(0, 0, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jcSlika.setPreferredSize(new java.awt.Dimension(592, 174));
				jcSlika.setBackground(Color.white);
			}
			{
				jbOdaberiDatoteku = new JButton();
				getContentPane().add(jbOdaberiDatoteku, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jbOdaberiDatoteku.setText("Odaberi datoteku");
				jbOdaberiDatoteku.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						 JFileChooser jfc=new JFileChooser();
						 jfc.setToolTipText("Odaberite datoteku sa logom tvrtke");
						 jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
							jfc.setFileFilter(new FileFilter() {

								@Override
								public String getDescription() {
									return "slike";
								}

								@Override
								public boolean accept(File arg0) {
									String fname = arg0.getName().toLowerCase();
									return arg0.isDirectory()||fname.endsWith(".jpg")||fname.endsWith(".png")||fname.endsWith(".bmp")||fname.endsWith(".gif")||fname.endsWith(".jpeg");
								}
							});
							jfc.setToolTipText("Molimo, odaberite datoteku sa logo slikom svoje tvrtke");

							 
							int rez = jfc.showDialog(GlavniFrame.getInstanca(),
									"Uèitaj");

							if (rez == JFileChooser.APPROVE_OPTION) {
								ucitajSliku(jfc.getSelectedFile());
							}
					}
 
				});
			}
			{
				jbSpremi = new JButton();
				getContentPane().add(jbSpremi, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jbSpremi.setText("Spremi");
				jbSpremi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						spremiSliku();
					}
				});
			}
			{
				jbOdustani = new JButton();
				getContentPane().add(jbOdustani, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jbOdustani.setText("Odustani");
				jbOdustani.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dispose();
					}
				});
			}
			setSize(600, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
		this.slika=GlavniFrame.getZaglavljeDokumenata();
		this.jcSlika.setPozadina(this.slika);
		slikaPostavljena = false;
	}
	
	public SlikaPanel getJcSlika() {
		return jcSlika;
	}
	private void spremiSliku() {
		// TODO Auto-generated method stub
		if (slika!=null && this.slikaPostavljena)
		GlavniFrame.pohraniLogo(slika);
		
		this.dispose();
	}
	
	BufferedImage slika=null;
	
	private void ucitajSliku(File selectedFile) {
		// TODO Auto-generated method stub
		System.out.println("Uèitavamo:"+selectedFile.getAbsolutePath());
		try {
			this.slika=ImageIO.read(selectedFile);
			this.jcSlika.setPozadina(slika);
			this.slikaPostavljena=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.log("Problem sa konvertiranjem datoteke "+selectedFile+" u sliku");
			GlavniFrame.alert("Problem kod uèitavanja slike!");		
			this.slikaPostavljena=false;
		}
	}
}
