/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXTable;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.LeceDAO;
import biz.sunce.dao.NaocaleDAO;
import biz.sunce.opticar.vo.KlijentVO;
import biz.sunce.opticar.vo.LeceVO;
import biz.sunce.opticar.vo.LijecnikVO;
import biz.sunce.opticar.vo.NaocaleVO;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.opticar.vo.SlusacModelaTablice;
import biz.sunce.opticar.vo.TableModel;
import biz.sunce.util.HtmlPrintParser;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;
import biz.sunce.util.gui.DaNeUpit;
import biz.sunce.util.tablice.sort.SortTableModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * datum:2005.05.17
 * @author asabo
 *
 */
public final class PreglediPanel extends JPanel implements SlusacModelaTablice,javax.swing.event.PopupMenuListener, java.awt.event.ActionListener, MouseListener
{
	private int KONTROLA_ZA_MJESECI;

	private javax.swing.JPanel jpDatumskiDio = null;
	private javax.swing.JTabbedPane jtpPregled = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jlImePrezime = null;
	private javax.swing.JLabel jlAdresa = null;
	private javax.swing.JLabel jlMjesto = null;
	private javax.swing.JScrollPane podaci = null;
	private JXTable jtbPregledi = null;
	private javax.swing.JButton jbNoviPregled = null;

	private javax.swing.JPanel jpAnamneza = null;
	private javax.swing.JPanel jpPrijeOrdinirano = null;
	private javax.swing.JPanel jpPregled = null;
	private javax.swing.JPanel jpOrdinirano = null;
	private OsobineNaocalaPanel jpPrijeOrdNaocaleBlizu = null;
	private OsobineNaocalaPanel jpPrijeOrdNaocaleDaleko = null;
	private OsobineLecaPanel jpPrijeOrdLece = null;
	private OsobineNaocalaPanel jpOrdiniraneNaocaleDaleko = null;
	private javax.swing.JTextField jtOrdVOD = null;
	private javax.swing.JPanel jpOrdiniraneNaocaleOpciPodaci = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JTextField jtOrdVOS = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JTextField jtOrdRazZjenicaL = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtOrdRazZjenicaD = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtOrdNapomena = null;
	private OsobineLecaPanel jpOrdiniraneLece = null;
	private TableModel model=null;

	private javax.swing.JTextArea jtAnamneza = null;
	private javax.swing.JScrollPane jspAnamneza = null;
	private OsobineNaocalaPanel  jpOrdiniraneNaocaleBlizu = null;
	public SortTableModel getModel(){return this.model;}
	KlijentFrame glavni;

	// tu ce sjediti podaci o trenutno oznacenom pregledu
	PregledVO oznaceniPregled=null;

	private javax.swing.JComboBox jcLijecnik = null;
	private SkiaskopijaPanel jpSkiaskopija = null;
	private javax.swing.JLabel jLabel6 = null;
	private KeratometrijaPanel jpKeratometrija = null;
	private RefraktometarPanel jpRekraktometar = null;
	private javax.swing.JPanel jpVisus = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JTextField jtVisusSCL = null;
	private javax.swing.JTextField jtVisusSCD = null;
	private javax.swing.JLabel jLabel10 = null;
	private javax.swing.JTextField jtVisusL = null;
	private javax.swing.JTextField jtVisusD = null;
	private DatumVrijemePanel jpDatumPregleda = null;
	private DatumVrijemePanel jpPregledObavljen = null;
	private javax.swing.JButton jbIspisi = null;
	private javax.swing.JLabel jLabel11 = null;
	private javax.swing.JLabel jLabel12 = null;
	private javax.swing.JButton jbIsipisOrdinirano = null;
	private javax.swing.JButton jbSpremi2 = null;
        private javax.swing.JPopupMenu jPopUpTablicaPregleda=null;

        JMenuItem jmeitIzbrisi=null;

	public PreglediPanel(KlijentFrame glavni)
	{
		super();
		this.glavni=glavni;
		KONTROLA_ZA_MJESECI=PostavkeBean.getIntPostavkaSustava("KONTROLA_ZA_MJESECI",12);
		initialize();
		// moramo postaviti combo box sa lijecnicima
		this.napuniLijecnike();
		this.model.dodajSlusaca(this);
		this.onemoguci();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints3 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		consGridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints3.weighty = 1.0;
		consGridBagConstraints3.weightx = 1.0;
		consGridBagConstraints3.gridy = 0;
		consGridBagConstraints3.gridx = 1;
		consGridBagConstraints2.gridy = 0;
		consGridBagConstraints2.gridx = 0;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTH;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.VERTICAL;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJpDatumskiDio(), consGridBagConstraints2);
		this.add(getJtpPregled(), consGridBagConstraints3);
		 
		//this.setSize(963, 604);
		this.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e)
                        {
                        // ako kontrolu preuzme drugi panel na formi..
                        spremiPregledUBazu();
			}
			public void focusGained(java.awt.event.FocusEvent e)
			{

			}
		});
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentHidden(java.awt.event.ComponentEvent e) {
			 spremiPregledUBazu();
			}
			public void componentShown(java.awt.event.ComponentEvent e)
			{
				napuniFormuSaPodacima();
			}
		});
	}

	private void napuniLijecnike()
	{
		boolean uPogonu = GlavniFrame.getInstanca().running;
		
		if (!uPogonu)
			return;

		List<LijecnikVO> l=null;
		try
		{
			l=DAOFactory.getInstance().getLijecnici().findAll(null);
			
			if (l!=null)
			{
		    JComboBox jc=this.getJcLijecnik();

		    if (jc==null){ Logger.fatal("Combo BOX sa lijecnicima je prazan kod PreglediPanel.napuniLijecnike!!!",null); return;}

				jc.removeAllItems();

				int size = l.size();
				for (int i=0; i<size; i++)
				jc.addItem(l.get(i));

			}//if
		}
		catch(SQLException sqle)
		{
			Logger.fatal("SQL iznimka kod PreglediPanel.napuniLijecnike",sqle);
		}
	}//napuniLijecnike

	// metoda zaduzena za punjenje podataka o odredjenom pregledu sa podacima
	public void napuniFormuSaPodacima()
	{
	 KlijentVO kl=this.glavni.getOznaceni();
		// prvo postavljanje osnovih podataka o klijentu
		if (kl.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
		{
		this.jlImePrezime.setText(kl.getIme()+" "+kl.getPrezime());
		this.jlAdresa.setText(kl.getAdresa());
		this.jlMjesto.setText(kl.getNazivMjesta());

		this.updateajTablicuSaPregledima();

        this.napuniFormuSaPodacimaOPregledu();

		}//if
		else
		this.pobrisiFormu();
	}

        public void omoguci(){this.postaviStatuseElemenata(true);}
        public void onemoguci(){this.postaviStatuseElemenata(false);}

        private javax.swing.JPopupMenu getJpopUpTablicaPregleda()
        {
        if(jPopUpTablicaPregleda == null) {
                jPopUpTablicaPregleda = new javax.swing.JPopupMenu();
                jPopUpTablicaPregleda.addPopupMenuListener(this);

                jmeitIzbrisi=new JMenuItem("Izbriši pregled");
                jmeitIzbrisi.addActionListener(this);
                jPopUpTablicaPregleda.add(jmeitIzbrisi);

                jPopUpTablicaPregleda.setLabel("Menu sa opcijama vezanim uz preglede");
        }//ako je jpSaljiSMS null
        return jPopUpTablicaPregleda;
       }//getJpopUpTablicaPRegleda


        public void postaviStatuseElemenata(boolean s)
        {
            this.getJcLijecnik().setEnabled(s);
            this.getJbIspisi().setEnabled(s);
            this.getJpAnamneza().setEnabled(s);
            this.getJpAnamneza().setBackground(s?Color.white:Color.gray);
            this.getJpDatumPregleda().postaviStatuseElemenata(s);
            this.getJpPregledObavljen().postaviStatuseElemenata(s);
            this.getJpKeratometrija().postaviStatuseElemenata(s);
            this.getJpOrdiniraneLece().postaviStatuseElemenata(s);
            this.getJpOrdiniraneNaocaleBlizu().postaviStatusOmogucenostiElemenata(s);
            this.getJpOrdiniraneNaocaleDaleko().postaviStatusOmogucenostiElemenata(s);
            this.getJpPrijeOrdLece().postaviStatuseElemenata(s);
            this.getJpPrijeOrdNaocaleBlizu().postaviStatusOmogucenostiElemenata(s);
            this.getJpPrijeOrdNaocaleDaleko().postaviStatusOmogucenostiElemenata(s);
            this.getJpRekraktometar().postaviStatuseElemenata(s);
            this.getJpSkiaskopija().postaviStatuseElemenata(s);
            this.getJtaAnamneza().setEnabled(s);
            this.getJtOrdNapomena().setEnabled(s);
            this.getJtOrdRazZjenicaD().setEnabled(s);
            this.getJtOrdRazZjenicaL().setEnabled(s);
            this.getJtOrdVOD().setEnabled(s);
            this.getJtOrdVOS().setEnabled(s);
            this.getJtVisusD().setEnabled(s);
            this.getJtVisusL().setEnabled(s);
            this.getJtVisusSCD().setEnabled(s);

            this.getJtVisusSCL().setEnabled(s);
            this.jbSpremi2.setEnabled(s);
            this.jbIsipisOrdinirano.setEnabled(s);
        }//postaviStatuse

	// odvojena je metoda jer ce u nekim situacijama trebati samo puniti dio sa pregledima
	public void napuniFormuSaPodacimaOPregledu()
	{
		boolean uPogonu = GlavniFrame.getInstanca().running;
		
		if (uPogonu && this.oznaceniPregled!=null)
		{
			this.jpDatumPregleda.setDatumVrijeme(this.oznaceniPregled.getDatVrijeme());

			LijecnikVO lvo=this.oznaceniPregled.getLijecnik();

			podesiOdabranogLijecnika(lvo);

			this.getJpSkiaskopija().napuniFormu(this.oznaceniPregled.getSkiaskopija());

	    this.getJpKeratometrija().napuniFormu(this.oznaceniPregled.getKeratometrija());

	    this.getJpRekraktometar().napuniFormu(this.oznaceniPregled.getRefraktometar());

	    // ordinirane naocale / lece
	    this.getJpOrdiniraneNaocaleBlizu().napuniFormu(this.oznaceniPregled.getOrdNaocaleBlizuVO());
	    //29.06.05.
			this.getJpOrdiniraneNaocaleDaleko().napuniFormu(this.oznaceniPregled.getOrdNaocaleDalekoVO());

			//25.11.05. -asabo- treba omoguciti forme koje bi eventualno mogle biti zablokirane zbog praznih objekata
			//----
			this.getJpOrdiniraneLece().omoguci();
			this.getJpPrijeOrdLece().omoguci();

			this.getJpOrdiniraneNaocaleBlizu().omoguci();
			this.getJpOrdiniraneNaocaleDaleko().omoguci();
			this.getJpPrijeOrdNaocaleBlizu().omoguci();
			this.getJpPrijeOrdNaocaleDaleko().omoguci();
		  //----

	    // prije ordinirane naocale / lece
			this.getJpPrijeOrdNaocaleBlizu().napuniFormu(this.oznaceniPregled.getPrijeOrdNaocaleBlizuVO());
			this.getJpPrijeOrdNaocaleDaleko().napuniFormu(this.oznaceniPregled.getPrijeOrdNaocaleDalekoVO());

			//30.06.05. -asabo-
		 	this.getJpPrijeOrdLece().napuniFormu(this.oznaceniPregled.getPrijeOrdLeceVO());
		 	this.getJpOrdiniraneLece().napuniFormu(this.oznaceniPregled.getOrdLeceVO());

	    this.jtVisusD.setText(this.oznaceniPregled.getVisusD());
	    this.jtVisusL.setText(this.oznaceniPregled.getVisusL());
	    this.jtVisusSCD.setText(this.oznaceniPregled.getVisusScD());
	    this.jtVisusSCL.setText(this.oznaceniPregled.getVisusScL());

	    this.jtAnamneza.setText(this.oznaceniPregled.getAnamneza());

	    this.jtOrdNapomena.setText(this.oznaceniPregled.getOrdNapomena());

	    this.jtOrdRazZjenicaD.setText(this.oznaceniPregled.getOrdRazZjD());
	    this.jtOrdRazZjenicaL.setText(this.oznaceniPregled.getOrdRazZjL());
	    this.jtOrdVOD.setText(this.oznaceniPregled.getOrdVOD());
	    this.jtOrdVOS.setText(this.oznaceniPregled.getOrdVOS());

	    Timestamp t=this.oznaceniPregled.getObavljen();

            if (t != null) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(t.getTime());
                this.jpPregledObavljen.setDatumVrijeme(c);
            }
			this.omoguci();
		}//ako pregled nije null
		else
		this.pobrisiFormuSaPodacimaOPregledu();
		this.repaint();


	}//napuniFormuSaPodacimaOPregledu

	//trazi po itemsima koji ima istu sifru kao doticni pa tog oznacuje... ne ide drugacije, nazalost
	private void podesiOdabranogLijecnika(LijecnikVO lvo)
	{
	  int kom=this.jcLijecnik.getItemCount();
	  for (int i=0; i<kom; i++)
	  {
	  LijecnikVO lv=(LijecnikVO)this.jcLijecnik.getItemAt(i);
	  if (lv.getSifra().intValue()==lvo.getSifra().intValue())
		{this.jcLijecnik.setSelectedItem(lv); return;}
	  }//for i
	  Logger.log("Opasna situacija, combo box nema lijecnika "+lvo.toString()+" sa sifrom: "+lvo.getSifra().intValue());
	}
	// brise samo podatke na formi koji su vezani uz pregled
	public void pobrisiFormuSaPodacimaOPregledu()
	{
		String p="";
		this.jtAnamneza.setText(p);
		this.jtOrdNapomena.setText(p);
		this.jtOrdRazZjenicaD.setText(p);
		this.jtOrdRazZjenicaL.setText(p);
		this.jtOrdVOD.setText(p);
		this.jtOrdVOS.setText(p);

		this.jpPrijeOrdNaocaleBlizu.pobrisiFormu();
		this.jpPrijeOrdNaocaleDaleko.pobrisiFormu();
		this.jpPrijeOrdLece.pobrisiFormu();

		this.jpOrdiniraneLece.pobrisiFormu();
		this.jpOrdiniraneNaocaleBlizu.pobrisiFormu();
		this.jpOrdiniraneNaocaleDaleko.pobrisiFormu();

		this.jtVisusD.setText(p);
		this.jtVisusL.setText(p);
		this.jtVisusSCD.setText(p);
		this.jtVisusSCL.setText(p);

		this.jpSkiaskopija.pobrisiFormu();
		this.jpKeratometrija.pobrisiFormu();
		this.jpRekraktometar.pobrisiFormu();

		this.jpDatumPregleda.pobrisiFormu();
		this.jpPregledObavljen.pobrisiFormu();
		this.oznaceniPregled=null; // nema ga vise na formi...
    this.onemoguci(); // onemogucuje samo dio sa pregledima...
	}//pobrisiFormuSaPodacimaOPregledu

	// ucitava iz baze podataka popis svih pregleda za datog klijenta i puni tablicu sa podacima
	public void updateajTablicuSaPregledima()
	{
		KlijentVO kl=this.glavni!=null?this.glavni.getOznaceni():null;
		if (kl!=null && kl.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
		try{
		this.model.setData(DAOFactory.getInstance().getPregledi().findAll(kl.getSifra()));

		// jos treba
		}
		catch(SQLException sqle)
		{
			Logger.fatal("SQL iznimka kod PreglediPanel.updateajTablicuSaPregledima",sqle);
		}
	}//updateajTablicuSaPregledima

	// sve elemente na formi postavlja da budu prazni
	public void pobrisiFormu()
	{
		this.pobrisiFormuSaPodacimaOPregledu();

		final String p="";
		this.jlImePrezime.setText(p);
		this.jlAdresa.setText(p);
		this.jlMjesto.setText(p);
		this.model.setData(new ArrayList());
	}
	/**
	 * This method initializes jpDatumskiDio
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpDatumskiDio() {
		if(jpDatumskiDio == null) {
			jpDatumskiDio = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
			consGridBagConstraints31.gridy = 5;
			consGridBagConstraints31.gridx = 0;
			consGridBagConstraints21.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints21.weighty = 1.0;
			consGridBagConstraints21.weightx = 1.0;
			consGridBagConstraints21.gridy = 4;
			consGridBagConstraints21.gridx = 0;
			consGridBagConstraints21.anchor = java.awt.GridBagConstraints.SOUTH;
			consGridBagConstraints21.insets = new java.awt.Insets(1,0,0,0);
			consGridBagConstraints21.ipadx = 1;
			consGridBagConstraints1.gridy = 3;
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints1.insets = new java.awt.Insets(0,0,2,0);
			consGridBagConstraints6.gridy = 2;
			consGridBagConstraints6.gridx = 0;
			consGridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints5.gridy = 1;
			consGridBagConstraints5.gridx = 0;
			consGridBagConstraints5.anchor = java.awt.GridBagConstraints.CENTER;
			consGridBagConstraints4.gridx = 0;
			consGridBagConstraints4.gridy = 0;
			consGridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints5.insets = new java.awt.Insets(0,0,2,0);
			jpDatumskiDio.setLayout(new java.awt.GridBagLayout());
			jpDatumskiDio.add(getJLabel(), consGridBagConstraints4);
			jpDatumskiDio.add(getJlImePrezime(), consGridBagConstraints5);
			jpDatumskiDio.add(getJlAdresa(), consGridBagConstraints6);
			jpDatumskiDio.add(getJlMjesto(), consGridBagConstraints1);
			jpDatumskiDio.add(getPodaci(), consGridBagConstraints21);
			jpDatumskiDio.add(getJbNoviPregled(), consGridBagConstraints31);
			jpDatumskiDio.setPreferredSize(new java.awt.Dimension(150,600));
			jpDatumskiDio.setToolTipText("datumi kada su pregledi održavani");
			jpDatumskiDio.setBackground(new java.awt.Color(106,198,196));
		}
		return jpDatumskiDio;
	}
	/**
	 * This method initializes jtpPregled
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private javax.swing.JTabbedPane getJtpPregled() {
		if(jtpPregled == null) {
			jtpPregled = new javax.swing.JTabbedPane();
			jtpPregled.addTab("pregled", null, getJpPregled(), null);
			jtpPregled.addTab("anamneza", null, getJpAnamneza(), null);
			jtpPregled.addTab("prije ordinirano", null, getJpPrijeOrdinirano(), null);
			jtpPregled.addTab("ordinirano", null, getJpOrdinirano(), "naoèale i leæe koje je pacijent kupio nakon pregleda");
			jtpPregled.setPreferredSize(new java.awt.Dimension(650,600));
			jtpPregled.setToolTipText("podaci o pregledu ");
		}
		return jtpPregled;
	}//getJtpPregled
	/**
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Pregledi");
			jLabel.setPreferredSize(new java.awt.Dimension(150,16));
			jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
		}
		return jLabel;
	}
	/**
	 * This method initializes jlImePrezime
	 *
	 * @return javax.swing.JLabel
	 */
	public javax.swing.JLabel getJlImePrezime() {
		if(jlImePrezime == null) {
			jlImePrezime = new javax.swing.JLabel();
			jlImePrezime.setText("_");
			jlImePrezime.setPreferredSize(new java.awt.Dimension(150,16));
			jlImePrezime.setMinimumSize(new java.awt.Dimension(150,16));
			jlImePrezime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jlImePrezime.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
		}
		return jlImePrezime;
	}
	/**
	 * This method initializes jlAdresa
	 *
	 * @return javax.swing.JLabel
	 */
	public javax.swing.JLabel getJlAdresa() {
		if(jlAdresa == null) {
			jlAdresa = new javax.swing.JLabel();
			jlAdresa.setText("Adresa ");
		}
		return jlAdresa;
	}
	/**
	 * This method initializes jlMjesto
	 *
	 * @return javax.swing.JLabel
	 */
	public javax.swing.JLabel getJlMjesto() {
		if(jlMjesto == null) {
			jlMjesto = new javax.swing.JLabel();
			jlMjesto.setText("Mjesto");
			jlMjesto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		}
		return jlMjesto;
	}
	/**
	 * This method initializes jtbPregledi
	 *
	 * @return javax.swing.JTable
	 */
	public JXTable getJtbPregledi() {
		if(jtbPregledi == null) {
			jtbPregledi = new JXTable();
			this.model=new TableModel(DAOFactory.getInstance().getPregledi(),jtbPregledi);
			jtbPregledi.setModel(this.model);
			jtbPregledi.setMaximumSize(jtbPregledi.getSize());
			jtbPregledi.setMinimumSize(jtbPregledi.getSize());
                        jtbPregledi.addMouseListener(this);

		}
		return jtbPregledi;
	}
	/**
	 * This method initializes podaci
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getPodaci() {
		if(podaci == null) {
			podaci = new javax.swing.JScrollPane();
			podaci.setViewportView(getJtbPregledi());

			podaci.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			podaci.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			podaci.setToolTipText("popis zakazanih pregleda za klijenta");
			podaci.setPreferredSize(new java.awt.Dimension(452,402));
			podaci.setMaximumSize(new java.awt.Dimension(452,402));
		}
		return podaci;
	}
	/**
	 * This method initializes jbNoviPregled
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbNoviPregled() {
		if(jbNoviPregled == null) {
			jbNoviPregled = new javax.swing.JButton();
			jbNoviPregled.setText("Novi pregled");
			jbNoviPregled.setPreferredSize(new java.awt.Dimension(150,26));
			jbNoviPregled.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e)
				{
				spremiPregledUBazu(); // 1. ako postoji pregleda kakav stari, treba ga spremiti..
				pobrisiFormu(); //2. cista forma
				kreirajNoviPregled(); // e sad kreiraj novi pregled
				spremiPregledUBazu(); // zatim ga spremi prvi puta ...

				// punjenje tablice sa pregledima ide posebno
				// necemo updateati cijelu tablicu, vec samo na kraj tablice dodati ovaj novi pregled
				//model.getData().add(oznaceniPregled);
				//model.fireTableDataChanged();

				updateajTablicuSaPregledima();
				jtbPregledi.clearSelection();
				jtbPregledi.changeSelection(0,-1,false,false);

				napuniFormuSaPodacimaOPregledu();
				}
			});
			jbNoviPregled.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
						}
			});
		}
		return jbNoviPregled;
	}//getJbNoviPregled

	private void kreirajNoviPregled()
	{
		this.oznaceniPregled=new PregledVO();
		this.oznaceniPregled.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));

		Calendar dv;
		dv=Calendar.getInstance();
		dv.set(Calendar.SECOND,0);
		dv.set(Calendar.MILLISECOND,0);
		//vrijeme se postavlja na jedinicu mjere (12:18 postaje 12:15)
		dv.set(Calendar.MINUTE,dv.get(Calendar.MINUTE)-(dv.get(Calendar.MINUTE)%DAO.PREGLEDI_JEDINICA_MINUTA));
		this.oznaceniPregled.setDatVrijeme(dv);

		this.oznaceniPregled.setLijecnik((LijecnikVO)jcLijecnik.getSelectedItem());
		this.oznaceniPregled.setCreated(Calendar.getInstance().getTimeInMillis());
		this.oznaceniPregled.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
		this.oznaceniPregled.setSifKlijenta(glavni.getOznaceni().getSifra());
		this.oznaceniPregled.setKontrolaZaMjeseci(Integer.valueOf(KONTROLA_ZA_MJESECI));
	}//kreirajNoviPregled

	private void napuniPregledPodacimaIzForme()
	{
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (this.oznaceniPregled==null) return;

	 Calendar dv=this.jpDatumPregleda.getDatum();

	 if (dv==null)  // ne smije biti null
	 {
   dv=Calendar.getInstance();
   dv.setLenient(true); // da ga mozemo povecavati za po X minuta i da ne brinemo o 'prelijevanju'
   dv.set(Calendar.SECOND,0);
   dv.set(Calendar.MILLISECOND,0);
	 }
	 this.oznaceniPregled.setDatVrijeme(dv);


	 if (this.jpPregledObavljen.getDatum()!=null)
	 this.oznaceniPregled.setObavljen(new Timestamp(this.jpPregledObavljen.getDatum().getTimeInMillis()));
	 this.oznaceniPregled.setLijecnik((LijecnikVO)this.jcLijecnik.getSelectedItem());
	 this.oznaceniPregled.setKeratometrija(this.jpKeratometrija.vratiPodatke());
	 this.oznaceniPregled.setSkiaskopija(this.jpSkiaskopija.vratiPodatke());
	 this.oznaceniPregled.setRekfraktometar(this.jpRekraktometar.vratiPodatke()); //14.06.05. -asabo-

	 // 14.06.05. -asabo- visusi dodani
	 this.oznaceniPregled.setVisusD(this.jtVisusD.getText());
	 this.oznaceniPregled.setVisusL(this.jtVisusL.getText());
	 this.oznaceniPregled.setVisusScD(this.jtVisusSCD.getText());
	 this.oznaceniPregled.setVisusScL(this.jtVisusSCL.getText());

	 this.oznaceniPregled.setAnamneza(this.jtAnamneza.getText());

	 this.oznaceniPregled.setOrdVOD(this.jtOrdVOD.getText());
	 this.oznaceniPregled.setOrdVOS(this.jtOrdVOS.getText());
	 this.oznaceniPregled.setOrdNapomena(this.jtOrdNapomena.getText());
	 this.oznaceniPregled.setOrdRazZjD(this.jtOrdRazZjenicaD.getText());
	 this.oznaceniPregled.setOrdRazZjL(this.jtOrdRazZjenicaL.getText());

	 NaocaleVO temp;
	 LeceVO ltemp;

	 //ordinirane naocale /lece
	 temp=this.getJpOrdiniraneNaocaleBlizu().vratiPodatke();
	 this.updateNaocala(temp);
	 this.oznaceniPregled.setOrdBlizu(temp!=null?temp.getSifra():null);

    // iako mozda korisnik nije upisao nista o ordiniranom artiklu, upisujemo ipak taj podatak
    // lose rijeseno,ali negativa ce biti samo malo vise podataka u bazi :)
	 temp=this.getJpOrdiniraneNaocaleDaleko().vratiPodatke();
	 this.updateNaocala(temp);
	 this.oznaceniPregled.setOrdDaleko(temp.getSifra());

	 ltemp=this.getJpOrdiniraneLece().vratiPodatke();
	 this.updateLeca(ltemp);
	 this.oznaceniPregled.setOrdLece(ltemp.getSifra());
	 ltemp=null;

	 // prije ordinirane naocale / lece
	 temp=this.getJpPrijeOrdNaocaleBlizu().vratiPodatke();
	 this.updateNaocala(temp);
	 this.oznaceniPregled.setPrijeOrdBlizu(temp.getSifra());

	 temp=this.getJpPrijeOrdNaocaleDaleko().vratiPodatke();
	 this.updateNaocala(temp);
	 this.oznaceniPregled.setPrijeOrdDaleko(temp.getSifra());

	 ltemp=this.getJpPrijeOrdLece().vratiPodatke();
	 this.updateLeca(ltemp);
	 this.oznaceniPregled.setPrijeOrdLece(ltemp.getSifra());
	 this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}//napuniPregledPodacimaIzForme

	//29.06.05. metoda ima za zadatak primljeni NaocaleVO objekt poslati u bazu podataka
	// pod uvjetom da je isModified()==true. Takodjer metoda odlucuje dali treba objekt
	// insertirati ili updateirati. Vraca nazad true ako je uspjesno poslala objekt u bazu
	private boolean updateNaocala(NaocaleVO naocale)
	{
		// naocaleVO sam su za sebe 'jaki' objekt pa ih treba posebno pogledati i ako
		// je korisnik mjenjao nesto po njima, trebaju se sami updateati (bez pomoci 'tate')
		if (naocale!=null && naocale.isModified())
		{
		 NaocaleDAO ndao=DAOFactory.getInstance().getNaocale();
		 if (ndao!=null)
		 try
		 {
		 if (naocale.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
			ndao.update(naocale);
			 else ndao.insert(naocale);
		 }
		 catch (SQLException sqle)
		 {
			 Logger.fatal("SQL Iznimka kod pokusaja ubacivanja podataka o ordiniranim naocalama",sqle);
			 return false;
		 }
		 else
			{Logger.log("ndao je null ?!?"); return false;}
		}
		else
		{
			return false;
		}
   return true;
	}//updateNaocala

  //30.06.05. metoda ima za zadatak primljeni LeceVO objekt poslati u bazu podataka
	// pod uvjetom da je isModified()==true. Takodjer metoda odlucuje dali treba objekt
	// insertirati ili updateirati. Vraca nazad true ako je uspjesno poslala objekt u bazu
	private boolean updateLeca(LeceVO lece)
	{
		// naocaleVO sam su za sebe 'jaki' objekt pa ih treba posebno pogledati i ako
		// je korisnik mjenjao nesto po njima, trebaju se sami updateati (bez pomoci 'tate')
		if (lece!=null && lece.isModified())
		{
		 LeceDAO ldao=DAOFactory.getInstance().getLece();

		 if (ldao!=null)
		 try
		 {
		 if (lece.getSifra().intValue()!=DAO.NEPOSTOJECA_SIFRA)
			ldao.update(lece);
			 else ldao.insert(lece);
		 }
		 catch (SQLException sqle)
		 {
			 Logger.fatal("SQL Iznimka kod pokusaja ubacivanja podataka o ordiniranim lecama ",sqle);
			 return false;
		 }
		 else
			{Logger.log("ldao je null ?!?"); return false;}
		}
		else
		{
			return false;
		}
	 return true;
	}//updateLeca

	private void spremiPregledUBazu()
	{

		if (this.oznaceniPregled!=null)
		{
			this.napuniPregledPodacimaIzForme();

			// provjera jesu li minute namjestene na 'jedinicu mjere', dakle ne moze se zakazati saastanak u 12:18, vec u 12:15
			Calendar dv=this.oznaceniPregled.getDatVrijeme();

			if (dv!=null)
			dv.set(Calendar.MINUTE,dv.get(Calendar.MINUTE)-(dv.get(Calendar.MINUTE)%DAO.PREGLEDI_JEDINICA_MINUTA));
			else
			Logger.log("datVrijeme pregleda je null?!?",null);

			try
			{
			if (this.oznaceniPregled.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA)
			{
			this.oznaceniPregled.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
			if (this.glavni.getOznaceni().getSifra().intValue()!=-1)
			DAOFactory.getInstance().getPregledi().insert(this.oznaceniPregled);
			else
			{
			this.pobrisiFormuSaPodacimaOPregledu();
			 if (biz.sunce.util.gui.DaNeUpit.upit("Da biste mogli otvoriti novi pregled\nprvo trebate pohraniti klijenta. Može?","Pohraniti klijenta?",this.glavni))
                         {
                             boolean tmp=this.glavni.prikazatiUpozorenja;
                             this.glavni.prikazatiUpozorenja=true;
                             this.glavni.spremiKlijenta();
                             this.glavni.prikazatiUpozorenja=tmp;
                         }
			}
			}
			else
			{
			this.oznaceniPregled.setLastUpdatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
			boolean rez=DAOFactory.getInstance().getPregledi().update(this.oznaceniPregled);
			if (rez)
			this.pobrisiFormuSaPodacimaOPregledu();
			else
			JOptionPane.showMessageDialog(this,"Nastao je problem pri ažuriranju podataka. Kontaktirajte administratora","Upozorenje",JOptionPane.WARNING_MESSAGE);
			}
			}
			catch(SQLException sqle)
			{
				Logger.fatal("SQL iznimka kod spremanja pregleda u bazu podataka",sqle);
				JOptionPane.showMessageDialog(this,"Nastala je iznimka pri ažuriranju podataka. Kontaktirajte administratora","Upozorenje",JOptionPane.WARNING_MESSAGE);
			}
		}
	}//spremiPregledUBazu

	/**
	 * This method initializes jpAnamneza
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpAnamneza() {
		if(jpAnamneza == null) {
			jpAnamneza = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints59 = new java.awt.GridBagConstraints();
			consGridBagConstraints59.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints59.weighty = 1.0;
			consGridBagConstraints59.weightx = 1.0;
			consGridBagConstraints59.gridx = 0;
			consGridBagConstraints59.gridy = 0;
			jpAnamneza.setLayout(new java.awt.GridBagLayout());
			jpAnamneza.add(getJspAnamneza(), consGridBagConstraints59);
		}
		return jpAnamneza;
	}
	/**
	 * This method initializes jpPrijeOrdinirano
	 *
	 * @return javax.swing.JPanel
	 */
	public javax.swing.JPanel getJpPrijeOrdinirano() {
		if(jpPrijeOrdinirano == null) {
			jpPrijeOrdinirano = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints26 = new java.awt.GridBagConstraints();
			consGridBagConstraints26.gridy = 2;
			consGridBagConstraints26.gridx = 0;
			consGridBagConstraints10.gridy = 1;
			consGridBagConstraints10.gridx = 0;
			consGridBagConstraints9.gridy = 0;
			consGridBagConstraints9.gridx = 0;
			jpPrijeOrdinirano.setLayout(new java.awt.GridBagLayout());
			jpPrijeOrdinirano.add(getJpPrijeOrdNaocaleBlizu(), consGridBagConstraints9);
			jpPrijeOrdinirano.add(getJpPrijeOrdNaocaleDaleko(), consGridBagConstraints10);
			jpPrijeOrdinirano.add(getJpPrijeOrdLece(), consGridBagConstraints26);
		}
		return jpPrijeOrdinirano;
	}
	/**
	 * This method initializes jpPregled
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpPregled() {
		if(jpPregled == null) {
			jpPregled = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints23 = new java.awt.GridBagConstraints();
			consGridBagConstraints23.insets = new Insets(0, 0, 5, 5);
			java.awt.GridBagConstraints consGridBagConstraints32 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints19 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints45 = new java.awt.GridBagConstraints();
			consGridBagConstraints45.insets = new Insets(0, 0, 5, 5);
			java.awt.GridBagConstraints consGridBagConstraints321 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints331 = new java.awt.GridBagConstraints();
			consGridBagConstraints331.fill = GridBagConstraints.HORIZONTAL;
			java.awt.GridBagConstraints consGridBagConstraints310 = new java.awt.GridBagConstraints();
			consGridBagConstraints310.insets = new Insets(0, 0, 5, 5);
			java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
			consGridBagConstraints11.insets = new Insets(0, 0, 5, 5);
			java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
			consGridBagConstraints13.insets = new Insets(0, 0, 5, 5);
			java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
			consGridBagConstraints24.insets = new Insets(0, 0, 0, 5);
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			consGridBagConstraints12.insets = new Insets(0, 0, 5, 5);
			java.awt.GridBagConstraints consGridBagConstraints33 = new java.awt.GridBagConstraints();
			consGridBagConstraints33.insets = new Insets(0, 0, 5, 0);
			consGridBagConstraints33.gridy = 6;
			consGridBagConstraints33.gridx = 0;
			consGridBagConstraints33.anchor = java.awt.GridBagConstraints.CENTER;
			consGridBagConstraints33.gridwidth = 5;

			consGridBagConstraints13.gridy = 8;
			consGridBagConstraints13.gridx = 1;
			consGridBagConstraints13.anchor = java.awt.GridBagConstraints.SOUTH;
			consGridBagConstraints13.gridwidth = 3;
			consGridBagConstraints24.gridy = 9;
			consGridBagConstraints24.gridx = 1;
			consGridBagConstraints24.anchor = java.awt.GridBagConstraints.SOUTH;
			consGridBagConstraints24.gridwidth = 3;
			consGridBagConstraints12.gridy = 7;
			consGridBagConstraints12.gridx = 3;
			consGridBagConstraints11.gridy = 0;
			consGridBagConstraints11.gridx = 2;
			consGridBagConstraints310.gridy = 0;
			consGridBagConstraints310.gridx = 0;
			consGridBagConstraints331.gridy = 4;
			consGridBagConstraints331.gridx = 2;
			consGridBagConstraints331.gridwidth = 2;
			consGridBagConstraints321.gridy = 4;
			consGridBagConstraints321.gridx = 0;
			consGridBagConstraints321.gridwidth = 2;
			consGridBagConstraints19.gridy = 2;
			consGridBagConstraints19.gridx = 2;
			consGridBagConstraints45.gridy = 1;
			consGridBagConstraints45.gridx = 2;
			consGridBagConstraints45.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints19.gridwidth = 2;
			consGridBagConstraints23.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints23.weightx = 0.0D;
			consGridBagConstraints23.gridx = 3;
			consGridBagConstraints23.gridy = 1;
			consGridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints32.gridy = 2;
			consGridBagConstraints32.gridx = 0;
			consGridBagConstraints32.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints32.gridwidth = 2;
			consGridBagConstraints11.gridwidth = 2;
			consGridBagConstraints310.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints310.gridwidth = 2;
			GridBagLayout gbl_jpPregled = new GridBagLayout();
			gbl_jpPregled.columnWidths = new int[]{0, 212, 0, 108, 0};
			jpPregled.setLayout(gbl_jpPregled);
			jpPregled.add(getJcLijecnik(), consGridBagConstraints23);
			jpPregled.add(getJpSkiaskopija(), consGridBagConstraints32);
			jpPregled.add(getJLabel6(), consGridBagConstraints45);
			jpPregled.add(getJpKeratometrija(), consGridBagConstraints19);
			jpPregled.add(getJpRekraktometar(), consGridBagConstraints321);
			jpPregled.add(getJpVisus(), consGridBagConstraints331);
			jpPregled.add(getJpDatumPregleda(), consGridBagConstraints310);
			jpPregled.add(getJpPregledObavljen(), consGridBagConstraints11);
			jpPregled.add(getJbIspisi(), consGridBagConstraints12);
			jpPregled.add(getJLabel11(), consGridBagConstraints13);
			jpPregled.add(getJLabel12(), consGridBagConstraints24);
			jpPregled.add(getJpOrdiniraneNaocaleOpciPodaci(), consGridBagConstraints33);
			Dimension minSize = new java.awt.Dimension(443,803);
			jpPregled.setMinimumSize(minSize);
			jpPregled.setPreferredSize(minSize);
		}
		return jpPregled;
	}
	/**
	 * This method initializes jpOrdinirano
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpOrdinirano() {
		if(jpOrdinirano == null) {
			jpOrdinirano = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints28 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints44 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints63 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			consGridBagConstraints8.gridx = 0;
			consGridBagConstraints8.gridy = 3;
			consGridBagConstraints63.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints63.gridwidth = 1;
			consGridBagConstraints63.gridy = 0;
			consGridBagConstraints63.gridx = 0;
			consGridBagConstraints14.gridy = 3;
			consGridBagConstraints14.gridx = 1;
			consGridBagConstraints22.gridy = 2;
			consGridBagConstraints22.gridx = 0;
			consGridBagConstraints44.gridy = 0;
			consGridBagConstraints44.gridx = 0;
			consGridBagConstraints44.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints44.gridwidth = 2;
			consGridBagConstraints22.gridwidth = 2;
			consGridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints28.gridy = 1;
			consGridBagConstraints28.gridx = 0;
			consGridBagConstraints28.anchor = java.awt.GridBagConstraints.NORTH;
			consGridBagConstraints28.gridwidth = 2;
			jpOrdinirano.setLayout(new java.awt.GridBagLayout());
			jpOrdinirano.add(getJpOrdiniraneNaocaleDaleko(), consGridBagConstraints28);
			jpOrdinirano.add(getJpOrdiniraneLece(), consGridBagConstraints44);
			jpOrdinirano.add(getJpOrdiniraneNaocaleBlizu(), consGridBagConstraints22);
			jpOrdinirano.add(getJbIsipisOrdinirano(), consGridBagConstraints14);
			jpOrdinirano.add(getJbSpremi2(), consGridBagConstraints8);
			jpOrdinirano.setToolTipText("naoèale i leæe koje je pacijent kupio nakon pregleda");
		}
		return jpOrdinirano;
	}
	/**
	 * This method initializes jpPrijeOrdNaocaleBlizu
	 *
	 * @return javax.swing.JPanel
	 */
	public OsobineNaocalaPanel getJpPrijeOrdNaocaleBlizu() {
		if(jpPrijeOrdNaocaleBlizu == null) {
			jpPrijeOrdNaocaleBlizu = new OsobineNaocalaPanel(this.glavni);
			jpPrijeOrdNaocaleBlizu.setNaziv(" Naoèale blizu");
			jpPrijeOrdNaocaleBlizu.setPreferredSize(new java.awt.Dimension(690,171));
			jpPrijeOrdNaocaleBlizu.setMinimumSize(new java.awt.Dimension(610,168));
		}
		return jpPrijeOrdNaocaleBlizu;
	}
	/**
	 * This method initializes jpPrijeOrdNaocaleDaleko
	 *
	 * @return javax.swing.JPanel
	 */
	public OsobineNaocalaPanel getJpPrijeOrdNaocaleDaleko() {
		if(jpPrijeOrdNaocaleDaleko == null) {
			jpPrijeOrdNaocaleDaleko = new OsobineNaocalaPanel(this.glavni);
			jpPrijeOrdNaocaleDaleko.setNaziv("Naoèale daleko");
			jpPrijeOrdNaocaleDaleko.setPreferredSize(new java.awt.Dimension(690,171));
			jpPrijeOrdNaocaleDaleko.setMinimumSize(new java.awt.Dimension(610,171));
		}
		return jpPrijeOrdNaocaleDaleko;
	}
	/**
	 * This method initializes jpPrijeOrdLece
	 *
	 * @return javax.swing.JPanel
	 */
	public OsobineLecaPanel getJpPrijeOrdLece()
	{
		if(jpPrijeOrdLece == null)
		{
			jpPrijeOrdLece = new OsobineLecaPanel(this.glavni);
		}
		return jpPrijeOrdLece;
	}
	/**
	 * This method initializes jpOrdiniraneNaocaleDaleko
	 *
	 * @return javax.swing.JPanel
	 */
	private OsobineNaocalaPanel getJpOrdiniraneNaocaleDaleko() {
		if(jpOrdiniraneNaocaleDaleko == null) {
			jpOrdiniraneNaocaleDaleko = new OsobineNaocalaPanel(this.glavni);
			jpOrdiniraneNaocaleDaleko.setNaziv("Naoèale daleko");
			jpOrdiniraneNaocaleDaleko.setPreferredSize(new java.awt.Dimension(690,170));
			jpOrdiniraneNaocaleDaleko.setMinimumSize(new java.awt.Dimension(610,168));
		}
		return jpOrdiniraneNaocaleDaleko;
	}
	/**
	 * This method initializes jtOrdVOD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtOrdVOD() {
		if(jtOrdVOD == null) {
			jtOrdVOD = new javax.swing.JTextField();
			jtOrdVOD.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtOrdVOD;
	}
	/**
	 * This method initializes jpOrdiniraneNaocaleOpciPodaci
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpOrdiniraneNaocaleOpciPodaci() {
		if(jpOrdiniraneNaocaleOpciPodaci == null) {
			jpOrdiniraneNaocaleOpciPodaci = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints34 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints35 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints36 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints38 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints39 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints37 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints42 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints43 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints40 = new java.awt.GridBagConstraints();
			consGridBagConstraints43.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints43.weightx = 1.0;
			consGridBagConstraints43.gridy = 4;
			consGridBagConstraints43.gridx = 2;
			consGridBagConstraints43.gridwidth = 3;
			consGridBagConstraints42.gridy = 4;
			consGridBagConstraints42.gridx = 1;
			consGridBagConstraints42.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints38.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints38.gridy = 3;
			consGridBagConstraints38.gridx = 1;
			consGridBagConstraints36.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints36.gridy = 2;
			consGridBagConstraints36.gridx = 3;
			consGridBagConstraints39.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints39.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints39.weightx = 1.0;
			consGridBagConstraints39.gridy = 3;
			consGridBagConstraints39.gridx = 2;
			consGridBagConstraints34.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints34.gridy = 2;
			consGridBagConstraints34.gridx = 1;
			consGridBagConstraints37.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints37.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints37.weightx = 1.0;
			consGridBagConstraints37.gridy = 2;
			consGridBagConstraints37.gridx = 4;
			consGridBagConstraints35.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints35.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints35.weightx = 1.0;
			consGridBagConstraints35.gridy = 2;
			consGridBagConstraints35.gridx = 2;
			consGridBagConstraints34.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints41.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints41.weightx = 1.0;
			consGridBagConstraints41.gridy = 3;
			consGridBagConstraints41.gridx = 4;
			consGridBagConstraints40.insets = new java.awt.Insets(0,0,0,0);
			consGridBagConstraints40.gridy = 3;
			consGridBagConstraints40.gridx = 3;
			consGridBagConstraints36.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints38.anchor = java.awt.GridBagConstraints.EAST;
			jpOrdiniraneNaocaleOpciPodaci.setLayout(new java.awt.GridBagLayout());
			jpOrdiniraneNaocaleOpciPodaci.add(getJLabel1(), consGridBagConstraints34);
			jpOrdiniraneNaocaleOpciPodaci.add(getJtOrdVOD(), consGridBagConstraints35);
			jpOrdiniraneNaocaleOpciPodaci.add(getJLabel2(), consGridBagConstraints36);
			jpOrdiniraneNaocaleOpciPodaci.add(getJtOrdVOS(), consGridBagConstraints37);
			jpOrdiniraneNaocaleOpciPodaci.add(getJLabel3(), consGridBagConstraints38);
			jpOrdiniraneNaocaleOpciPodaci.add(getJtOrdRazZjenicaL(), consGridBagConstraints39);
			jpOrdiniraneNaocaleOpciPodaci.add(getJLabel4(), consGridBagConstraints40);
			jpOrdiniraneNaocaleOpciPodaci.add(getJtOrdRazZjenicaD(), consGridBagConstraints41);
			jpOrdiniraneNaocaleOpciPodaci.add(getJLabel5(), consGridBagConstraints42);
			jpOrdiniraneNaocaleOpciPodaci.add(getJtOrdNapomena(), consGridBagConstraints43);
			jpOrdiniraneNaocaleOpciPodaci.setMinimumSize(new Dimension(250,80));
			jpOrdiniraneNaocaleOpciPodaci.setPreferredSize(new java.awt.Dimension(230,70));
		}
		return jpOrdiniraneNaocaleOpciPodaci;
	}
	/**
	 * This method initializes jLabel1
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("VOD: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jLabel2
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("VOS: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jtOrdVOS
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtOrdVOS() {
		if(jtOrdVOS == null) {
			jtOrdVOS = new javax.swing.JTextField();
			jtOrdVOS.setPreferredSize(new java.awt.Dimension(50,20));
		}
		return jtOrdVOS;
	}
	/**
	 * This method initializes jLabel3
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Razmak zjenica: ");
		}
		return jLabel3;
	}
	/**
	 * This method initializes jtOrdRazZjenicaL
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtOrdRazZjenicaL() {
		if(jtOrdRazZjenicaL == null) {
			jtOrdRazZjenicaL = new javax.swing.JTextField();
			jtOrdRazZjenicaL.setPreferredSize(new java.awt.Dimension(30,20));
		}
		return jtOrdRazZjenicaL;
	}
	/**
	 * This method initializes jLabel4
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText(" / ");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jtOrdRazZjenicaD
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtOrdRazZjenicaD() {
		if(jtOrdRazZjenicaD == null) {
			jtOrdRazZjenicaD = new javax.swing.JTextField();
			jtOrdRazZjenicaD.setPreferredSize(new java.awt.Dimension(30,20));
		}
		return jtOrdRazZjenicaD;
	}
	/**
	 * This method initializes jLabel5
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Napomena: ");
		}
		return jLabel5;
	}
	/**
	 * This method initializes jtOrdNapomena
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtOrdNapomena() {
		if(jtOrdNapomena == null) {
			jtOrdNapomena = new javax.swing.JTextField();
		}
		return jtOrdNapomena;
	}
	/**
	 * This method initializes jpOrdiniraneLece
	 *
	 * @return javax.swing.JPanel
	 */
	private OsobineLecaPanel getJpOrdiniraneLece() {
		if(jpOrdiniraneLece == null) {
			jpOrdiniraneLece = new OsobineLecaPanel(this.glavni);
		}
		return jpOrdiniraneLece;
	}
	/**
	 * This method initializes jtaAnamneza
	 *
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getJtaAnamneza() {
		if(jtAnamneza == null) {
			jtAnamneza = new javax.swing.JTextArea();
			jtAnamneza.setRows(50);
			jtAnamneza.setColumns(40);
		}
		return jtAnamneza;
	}
	/**
	 * This method initializes jspAnamneza
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJspAnamneza() {
		if(jspAnamneza == null) {
			jspAnamneza = new javax.swing.JScrollPane();
			jspAnamneza.setViewportView(getJtaAnamneza());
		}
		return jspAnamneza;
	}
	/**
	 * This method initializes jpOrdiniraneNaocaleBlizu
	 *
	 * @return javax.swing.JPanel
	 */
	private OsobineNaocalaPanel getJpOrdiniraneNaocaleBlizu() {
		if(jpOrdiniraneNaocaleBlizu == null) {
			jpOrdiniraneNaocaleBlizu = new OsobineNaocalaPanel(this.glavni);
			jpOrdiniraneNaocaleBlizu.setNaziv("Naoèale blizu");
			jpOrdiniraneNaocaleBlizu.setPreferredSize(new java.awt.Dimension(690,170));
			jpOrdiniraneNaocaleBlizu.setMinimumSize(new java.awt.Dimension(610,168));
		}
		return jpOrdiniraneNaocaleBlizu;
	}
	public void redakOznacen(int redak, MouseEvent event, TableModel posiljatelj)
	{
		if (redak<0) return; // osiguranje...

		try{

		//eventualne izmjene na prethodnom pregledu spremiti u bazu podataka
	        // ne treba puniti podatke iz forme u pregled, jer to automatski obavlja metoda spremiPregledUBazu:
		this.spremiPregledUBazu(); // pregled u db
		this.pobrisiFormuSaPodacimaOPregledu(); // formu ocistiti
		this.oznaceniPregled=(PregledVO)posiljatelj.getData().get(redak); // drugi pregled oznacen...
		this.napuniFormuSaPodacimaOPregledu(); //napunimo formu...
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	public void redakIzmjenjen(int redak, TableModelEvent dogadjaj, TableModel posiljatelj) {
	}
	/**
	 * This method initializes jcLijecnik
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getJcLijecnik() {
		if(jcLijecnik == null) {
			jcLijecnik = new javax.swing.JComboBox();
			jcLijecnik.setPreferredSize(new java.awt.Dimension(120,25));
		}
		return jcLijecnik;
	}
	/**
	 * This method initializes jpSkiaskopija
	 *
	 * @return javax.swing.JPanel
	 */
	private SkiaskopijaPanel getJpSkiaskopija() {
		if(jpSkiaskopija == null) {
			jpSkiaskopija = new SkiaskopijaPanel();
		}
		return jpSkiaskopija;
	}
	/**
	 * This method initializes jLabel6
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Pregled obavio: ");
		}
		return jLabel6;
	}
	/**
	 * This method initializes jpKeratometrija
	 *
	 * @return javax.swing.JPanel
	 */
	private KeratometrijaPanel getJpKeratometrija() {
		if(jpKeratometrija == null) {
			jpKeratometrija = new KeratometrijaPanel();
		}
		return jpKeratometrija;
	}
	/**
	 * This method initializes jpRekraktometar
	 *
	 * @return javax.swing.JPanel
	 */
	private RefraktometarPanel getJpRekraktometar() {
		if(jpRekraktometar == null) {
			jpRekraktometar = new RefraktometarPanel();
		}
		return jpRekraktometar;
	}
	/**
	 * This method initializes jpVisus
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJpVisus() {
		if(jpVisus == null) {
			jpVisus = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints351 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints341 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints361 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints371 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints391 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints401 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints381 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints411 = new java.awt.GridBagConstraints();
			consGridBagConstraints351.gridy = 0;
			consGridBagConstraints351.gridx = 1;
			consGridBagConstraints341.gridy = 1;
			consGridBagConstraints341.gridx = 0;
			consGridBagConstraints361.gridy = 0;
			consGridBagConstraints361.gridx = 2;
			consGridBagConstraints401.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints401.weightx = 1.0;
			consGridBagConstraints401.gridy = 2;
			consGridBagConstraints401.gridx = 1;
			consGridBagConstraints371.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints371.weightx = 1.0;
			consGridBagConstraints371.gridy = 1;
			consGridBagConstraints371.gridx = 1;
			consGridBagConstraints381.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints381.weightx = 1.0;
			consGridBagConstraints381.gridy = 1;
			consGridBagConstraints381.gridx = 2;
			consGridBagConstraints341.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints411.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints411.weightx = 1.0;
			consGridBagConstraints411.gridy = 2;
			consGridBagConstraints411.gridx = 2;
			consGridBagConstraints391.gridy = 2;
			consGridBagConstraints391.gridx = 0;
			consGridBagConstraints391.anchor = java.awt.GridBagConstraints.EAST;
			jpVisus.setLayout(new java.awt.GridBagLayout());
			jpVisus.add(getJLabel7(), consGridBagConstraints341);
			jpVisus.add(getJLabel8(), consGridBagConstraints351);
			jpVisus.add(getJLabel9(), consGridBagConstraints361);
			jpVisus.add(getJtVisusSCL(), consGridBagConstraints371);
			jpVisus.add(getJtVisusSCD(), consGridBagConstraints381);
			jpVisus.add(getJLabel10(), consGridBagConstraints391);
			jpVisus.add(getJtVisusL(), consGridBagConstraints401);
			jpVisus.add(getJtVisusD(), consGridBagConstraints411);
			jpVisus.setPreferredSize(new java.awt.Dimension(200,80));
			jpVisus.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.RAISED), "Naoèale visus", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}
		return jpVisus;
	}
	/**
	 * This method initializes jLabel7
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Visus SC");
		}
		return jLabel7;
	}
	/**
	 * This method initializes jLabel8
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if(jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText(" L ");
			jLabel8.setFont(new java.awt.Font("DialogInput", java.awt.Font.BOLD, 12));
		}
		return jLabel8;
	}
	/**
	 * This method initializes jLabel9
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if(jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText(" D ");
			jLabel9.setFont(new java.awt.Font("DialogInput", java.awt.Font.BOLD, 12));
		}
		return jLabel9;
	}
	/**
	 * This method initializes jtVisusSCL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtVisusSCL() {
		if(jtVisusSCL == null) {
			jtVisusSCL = new javax.swing.JTextField();
			jtVisusSCL.setPreferredSize(new java.awt.Dimension(55,20));
			jtVisusSCL.setSize(new java.awt.Dimension(55,20));
			jtVisusSCL.setMinimumSize(new java.awt.Dimension(55,20));
		}
		return jtVisusSCL;
	}
	/**
	 * This method initializes jtVisusSCD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtVisusSCD() {
		if(jtVisusSCD == null) {
			jtVisusSCD = new javax.swing.JTextField();
			jtVisusSCD.setPreferredSize(new java.awt.Dimension(55,20));
			jtVisusSCD.setSize(new java.awt.Dimension(55,20));
			jtVisusSCD.setMinimumSize(new java.awt.Dimension(55,20));
		}
		return jtVisusSCD;
	}
	/**
	 * This method initializes jLabel10
	 *
	 * @return javax.swing.JLabel
	 */
	public javax.swing.JLabel getJLabel10() {
		if(jLabel10 == null) {
			jLabel10 = new javax.swing.JLabel();
			jLabel10.setText("Visus");
		}
		return jLabel10;
	}
	/**
	 * This method initializes jtVisusL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtVisusL() {
		if(jtVisusL == null) {
			jtVisusL = new javax.swing.JTextField();
			jtVisusL.setPreferredSize(new java.awt.Dimension(55,20));
			jtVisusL.setSize(new java.awt.Dimension(55,20));
			jtVisusL.setMinimumSize(new java.awt.Dimension(55,20));
		}
		return jtVisusL;
	}
	/**
	 * This method initializes jtVisusD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtVisusD() {
		if(jtVisusD == null) {
			jtVisusD = new javax.swing.JTextField();
			jtVisusD.setPreferredSize(new java.awt.Dimension(55,20));
			jtVisusD.setSize(new java.awt.Dimension(55,20));
			jtVisusD.setMinimumSize(new java.awt.Dimension(55,20));
		}
		return jtVisusD;
	}
	/**
	 * This method initializes jpDatumPregleda
	 *
	 * @return javax.swing.JPanel
	 */
	private DatumVrijemePanel getJpDatumPregleda() {
		if(jpDatumPregleda == null) {
			jpDatumPregleda = new DatumVrijemePanel();
			jpDatumPregleda.setNaslov("Datum pregleda");
		}
		return jpDatumPregleda;
	}
	/**
	 * This method initializes jpPregledObavljen
	 *
	 * @return javax.swing.JPanel
	 */
	private DatumVrijemePanel getJpPregledObavljen() {
		if(jpPregledObavljen == null) {
			jpPregledObavljen = new DatumVrijemePanel();
			jpPregledObavljen.setNaslov("Pregled obavljen");
		}
		return jpPregledObavljen;
	}

	private void printajPregled()
	{
    		 PregledVO pvo=this.oznaceniPregled;
	       //	just in case
					spremiPregledUBazu();
					PregledVO stari=this.oznaceniPregled; // bit ce null, ali evo, sve po pravilima..
					HtmlPrintParser parser=new HtmlPrintParser();
					String ispis=parser.ucitajHtmlPredlozak(Konstante.PREDLOZAK_PREGLED);
					this.oznaceniPregled=pvo;
					ispis=ugradiPregledUHTmlDokument(ispis);
					this.oznaceniPregled=stari; // vratimo sve kako je bilo...

					HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis,"pregled_"+pvo.getId());
	}//printajPregled
	/**
	 * This method initializes jbIspisi
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIspisi() {
		if(jbIspisi == null) {
			jbIspisi = new javax.swing.JButton();
			jbIspisi.setText("Ispiši");
			jbIspisi.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
				printajPregled();
				}

			});
			jbIspisi.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e)
				{

				}
			});
		}
		return jbIspisi;
	}

		//ispisuje ordinirane naocala i lece za ovaj pregled
		private void ispisiOrdinirano()
		{
			PregledVO pvo=this.oznaceniPregled;
			//	just in case
			spremiPregledUBazu();
			getJtbPregledi().clearSelection();
			PregledVO stari=this.oznaceniPregled; // bit ce null, ali evo, sve po pravilima..
			HtmlPrintParser parser=new HtmlPrintParser();
			String ispis=parser.ucitajHtmlPredlozak(Konstante.PREDLOZAK_ORDINIRANE_NAOCALE);
			this.oznaceniPregled=pvo;
			ispis=ugradiOrdiniraneNaocalePregledaUHTmlDokument(ispis);
			this.oznaceniPregled=stari; // vratimo sve kako je bilo...
                        HtmlPrintParser.ispisHTMLDokumentaNaStampac(ispis,"ordinirano_"+pvo.getId());
		}

	// ubacuje podatke u html dokument prije printanja
	private String ugradiPregledUHTmlDokument(String html)
	{

		html=html.replaceFirst("<!--klijent_podaci-->",this.oznaceniPregled.getKlijent().toHtml());

		html=html.replaceFirst("<!--datum_pregleda-->",Util.convertCalendarToString(this.oznaceniPregled.getDatVrijeme()));
		html=html.replaceFirst("<!--pregled_obavio-->",this.oznaceniPregled.getLijecnik().toString());

		html=html.replaceFirst("<!--naocale_prijeord_blizu-->",this.oznaceniPregled.getPrijeOrdNaocaleBlizuVO()!=null?this.oznaceniPregled.getPrijeOrdNaocaleBlizuVO().toHtml():"");
		html=html.replaceFirst("<!--naocale_prijeord_daleko-->",this.oznaceniPregled.getPrijeOrdNaocaleDalekoVO()!=null?this.oznaceniPregled.getPrijeOrdNaocaleDalekoVO().toHtml():"");
		html=html.replaceFirst("<!--lece_prijeord-->",this.oznaceniPregled.getPrijeOrdLeceVO()!=null?this.oznaceniPregled.getPrijeOrdLeceVO().toHtml():"");

		html=html.replaceFirst("<!--naocale_ord_daleko-->",this.oznaceniPregled.getOrdNaocaleDalekoVO()!=null?this.oznaceniPregled.getOrdNaocaleDalekoVO().toHtml():"");
		html=html.replaceFirst("<!--naocale_ord_blizu-->",this.oznaceniPregled.getOrdNaocaleBlizuVO()!=null?this.oznaceniPregled.getOrdNaocaleBlizuVO().toHtml():"");

		html=html.replaceFirst("<!--skiaskopija-->",this.oznaceniPregled.getSkiaskopija()!=null?this.oznaceniPregled.getSkiaskopija().toHtml():"");
		html=html.replaceFirst("<!--keratometrija-->",this.oznaceniPregled.getKeratometrija()!=null?this.oznaceniPregled.getKeratometrija().toHtml():"");
		html=html.replaceFirst("<!--refraktometar-->",this.oznaceniPregled.getRefraktometar()!=null?this.oznaceniPregled.getRefraktometar().toHtml():"");

		html=html.replaceFirst("<!--prijeord_lece-->",this.oznaceniPregled.getPrijeOrdLeceVO()!=null?this.oznaceniPregled.getPrijeOrdLeceVO().toHtml():"");

		html=html.replaceFirst("<!--ord_lece-->",this.oznaceniPregled.getOrdLeceVO()!=null?this.oznaceniPregled.getOrdLeceVO().toHtml():"");

		html=html.replaceFirst("<!--vod-->",this.oznaceniPregled.getOrdVOD());
		html=html.replaceFirst("<!--vos-->",this.oznaceniPregled.getOrdVOS());

		html=html.replaceFirst("<!--lrazzj-->",this.oznaceniPregled.getOrdRazZjL());
		html=html.replaceFirst("<!--drazzj-->",this.oznaceniPregled.getOrdRazZjD());

		if (this.oznaceniPregled.getNapomena()!=null)
		html=html.replaceFirst("<!--napomena_pregled-->",this.oznaceniPregled.getOrdNapomena().replaceAll("\\n","<br>"));

		if (this.oznaceniPregled.getAnamneza()!=null)
			html=html.replaceFirst("<!--anamneza-->",this.oznaceniPregled.getAnamneza().replaceAll("\\n","<br>"));

	String naoVisus="<table cellpadding='0'><tr><td><b>Naoèale Visus</b><td><b>L</b><td width='10'><td><b>D</b></tr>";
	      naoVisus+="<tr><td>Visus SC<td>"+this.oznaceniPregled.getVisusScL()+"<td><td>"+this.oznaceniPregled.getVisusScD()+"</tr>";
				naoVisus+="<tr><td>Visus   <td>"+this.oznaceniPregled.getVisusL()+"<td><td>"+this.oznaceniPregled.getVisusD()+"</tr>";
				naoVisus+="</table>";

		html=html.replaceFirst("<!--naocale_visus-->",naoVisus);
		//	html=html.replaceFirst("<!---->",);
		return html;
	}//ugradiPregledUHTmlDokument

//ubacuje podatke u html dokument prije printanja
 private String ugradiOrdiniraneNaocalePregledaUHTmlDokument(String html)
 {
   if (this.oznaceniPregled==null) return "?!?";

	 html=html.replaceFirst("<!--klijent_podaci-->",this.oznaceniPregled.getKlijent().toHtmlForEnvelope());
     String jmbg = this.oznaceniPregled.getKlijent()==null?"":this.oznaceniPregled.getKlijent().getJmbg();
	html=html.replaceFirst("<!--jmbg-->",jmbg);

	 html=html.replaceFirst("<!--datum_pregleda-->",Util.convertCalendarToString(this.oznaceniPregled.getDatVrijeme()));
	 html=html.replaceFirst("<!--pregled_obavio-->",this.oznaceniPregled.getLijecnik().toString());


	 html=html.replaceFirst("<!--naocale_ord_daleko-->",this.oznaceniPregled.getOrdNaocaleDalekoVO()!=null?this.oznaceniPregled.getOrdNaocaleDalekoVO().toHtml():"");
	 html=html.replaceFirst("<!--naocale_ord_blizu-->",this.oznaceniPregled.getOrdNaocaleBlizuVO()!=null?this.oznaceniPregled.getOrdNaocaleBlizuVO().toHtml():"");

	 html=html.replaceFirst("<!--ord_lece-->",this.oznaceniPregled.getOrdLeceVO()!=null?this.oznaceniPregled.getOrdLeceVO().toHtml():"");

	 return html;
 }//ugradiOrdiniraneNaocalePregledaUHTmlDokument

	/**
	 * This method initializes jLabel11
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel11() {
		if(jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setText("Napomena: ako promjenite neki podatak na ovoj formi, te zatim odaberete drugi pregled sa popisa, pohranit æete izmjene!");
		}
		return jLabel11;
	}
	/**
	 * This method initializes jLabel12
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel12() {
		if(jLabel12 == null) {
			jLabel12 = new javax.swing.JLabel();
			jLabel12.setText("ako ste greškom nešto krivo unijeli, zatvorite prozor i ništa neæe biti pohranjeno");
		}
		return jLabel12;
	}
	/**
	 * This method initializes jbIsipisOrdinirano
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbIsipisOrdinirano() {
		if(jbIsipisOrdinirano == null) {
			jbIsipisOrdinirano = new javax.swing.JButton();
			jbIsipisOrdinirano.setText("Ispiši ordinirano");
			jbIsipisOrdinirano.setMnemonic(java.awt.event.KeyEvent.VK_I);
			jbIsipisOrdinirano.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
				ispisiOrdinirano();
				}
			});
		}
		return jbIsipisOrdinirano;
	}
	/**
	 * This method initializes jbSpremi2
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJbSpremi2() {
		if(jbSpremi2 == null) {
			jbSpremi2 = new javax.swing.JButton();
			jbSpremi2.setText("Spremi");
			jbSpremi2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
				spremiPregledUBazu();
				getJtbPregledi().clearSelection();
				updateajTablicuSaPregledima();
				}
			});
		}
		return jbSpremi2;
	}

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    int selektiraniRedak=-1;

    public void actionPerformed(ActionEvent e)
    {
        // ako klijent odluci pobrisati zeljeni model
        if (e.getSource()==this.jmeitIzbrisi)
        {
         PregledVO pvo=null;

         if (this.selektiraniRedak>=0 && this.selektiraniRedak<this.model.getData().size())
         pvo=(PregledVO)this.model.getData().get(selektiraniRedak);

         if (pvo!=null && DaNeUpit.upit("Jeste sigurni da želite pobrisati pregled?","Brisati pregled?",this.glavni))
         {
            try
            {
             DAOFactory.getInstance().getPregledi().delete(pvo);
             this.pobrisiFormuSaPodacimaOPregledu();
             this.updateajTablicuSaPregledima();
            }
            catch (SQLException ex)
            {
            Logger.log("SQL iznimka kod pokusaja brisanja pregleda "+(pvo.getSifra()!=null?pvo.getSifra().intValue():-1),ex);
            GlavniFrame.alert("Nastao je problem pri pokušaju brisanja pregleda. Provjerite poruke sustava!");
            }
         }//if pvo!=null
        }//if source je jmeitIzbrisi
    }//actionPerformed

    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource()==this.jtbPregledi && e.getButton()==MouseEvent.BUTTON3)
        {
            int redak=jtbPregledi.rowAtPoint(e.getPoint());
            this.jtbPregledi.setRowSelectionInterval(redak,redak);
            this.getJpopUpTablicaPregleda().show(this.jtbPregledi,e.getX(),e.getY());
            this.selektiraniRedak=redak;

        }
    }//mouseClicked

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
