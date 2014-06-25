/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.ProizvodjaciDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.VrsteLecaDAO;
import biz.sunce.opticar.vo.LeceVO;
import biz.sunce.opticar.vo.OsobineLeceVO;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.opticar.vo.TipLeceVO;
import biz.sunce.util.Labela;
import biz.sunce.util.PretrazivanjeProzor;
import biz.sunce.util.SlusacOznaceneLabelePretrazivanja;

/**
 * datum:2005.05.18
 * @author asabo
 *
 */
public class OsobineLecaPanel extends JPanel implements SlusacOznaceneLabelePretrazivanja
 {

	private javax.swing.JComboBox jcVrstaLeca = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JComboBox jcPodvrstaLeca = null;
	private javax.swing.JLabel jLabel2 = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JTextField jtBaznaKrivinaD = null;
	private javax.swing.JTextField jtBaznaKrivinaL = null;
	private javax.swing.JTextField jtDijametarD = null;
	private javax.swing.JTextField jtDijametarL = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JTextField jtDsphD = null;
	private javax.swing.JTextField jtDsphL = null;
	private javax.swing.JLabel jLabel6 = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JTextField jtDcylD = null;
	private javax.swing.JTextField jtDcylL = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JTextField jtAxD = null;
	private javax.swing.JTextField jtAxL = null;
	private javax.swing.JLabel jLabel9 = null;
	private javax.swing.JTextField jtPosebnaIzradaD = null;
	private javax.swing.JTextField jtPosebnaIzradaL = null;
	private javax.swing.JLabel jLabel10 = null;
	private javax.swing.JTextField jtBojaLeceD = null;
	private javax.swing.JTextField jtBojaLeceL = null;
	private javax.swing.JLabel jLabel11 = null;
	private javax.swing.JTextField jtModelLeca = null;

	LeceVO ulazni=null;
	VrsteLecaDAO vrsteLeca=null;
	PretrazivanjeProzor proizvodjaciLeca=null;
	ProizvodjacVO oznaceni=null;
	private JFrame nosac=null;

	private javax.swing.JLabel jLabel12 = null;
	private javax.swing.JTextField jtNapomena = null;
	/**
	 * This is the default constructor
	 */
	public OsobineLecaPanel(JFrame nosac)
	{
		super();
		this.nosac=nosac;
		initialize();
	  this.napuniComboBox(jcVrstaLeca,null);
	  //this.napuniComboBox(jcPodvrstaLeca,jcVrstaLeca.getSelectedItem());
	}//konstruktor

	//06.10.05. -asabo- puni vrstu ili podvrstu lece
	private void napuniComboBox(JComboBox box,Object kljuc)
	{
		vrsteLeca = DAOFactory.getInstance().getVrsteLeca();
				if (vrsteLeca!=null)
				{
				List lece=null;

				try{
				lece=vrsteLeca.findAll(kljuc);
				}
				catch(SQLException sqle)
				{
				Logger.fatal("SQL iznimka kod OsobineLecaPanel.napuniComboBox ",sqle);
				lece=null;
				}
				 if (lece!=null)
				 {
					box.removeAllItems();
					for (int i=0; i<lece.size(); i++)
					{
					box.addItem(lece.get(i));
					}//for i
				 }//if lece ispravne
				}//if vrsteLeca ispravan
	}//napuniComboBox

	private void postaviComboBox(JComboBox box,TipLeceVO tip)
	{
		if (box==null || tip==null) return; // da se ne zajebajemo
		TipLeceVO tvo=null;
		int kom=box.getItemCount();
		for (int i=0; i<kom; i++)
		{
	  	tvo=(TipLeceVO)box.getItemAt(i);
	  	if (tvo.getSifra().intValue()==tip.getSifra().intValue())
  		{box.setSelectedIndex(i); break;}
		}	//for i
	}//postaviComboBox

	public void napuniFormu(LeceVO ulazni)
	{
			this.ulazni=ulazni;
			if (ulazni==null){return;} //14.11.05. -asabo- this.pobrisiFormu() je izbacen odavde (nemamo sta brisati na svoju ruku)
			this.omoguci();

				//06.10.05. -asabo- vrste i podvrste leca...
	 			if (ulazni.getVrsta()!=null)
	 			{this.postaviComboBox(this.jcVrstaLeca,ulazni.getVrsta());}

				if (ulazni.getPodvrsta()!=null)
				{this.postaviComboBox(this.jcPodvrstaLeca,ulazni.getPodvrsta());}

			OsobineLeceVO tmp=null;

			if (this.ulazni.getNapomena()!=null)
			this.jtNapomena.setText(this.ulazni.getNapomena());

			tmp=ulazni.getDesna();

			if (tmp!=null)
			{
			 this.jtBaznaKrivinaD.setText(tmp.getBaznaKrivina());
			 this.jtDijametarD.setText(tmp.getDijametar());
			 this.jtDsphD.setText(tmp.getDsph());
			 this.jtDcylD.setText(tmp.getDcyl());
			 this.jtAxD.setText(tmp.getAx());
			 this.jtPosebnaIzradaD.setText(tmp.getSpecIzrada());
			 this.jtBojaLeceD.setText(tmp.getBoja());

			}
			else
			System.out.println("Ulazni parametar LecaVo je null?!?");

			tmp=ulazni.getLijeva();

				if (tmp!=null)
				{
				 this.jtBaznaKrivinaL.setText(tmp.getBaznaKrivina());
				 this.jtDijametarL.setText(tmp.getDijametar());
				 this.jtDsphL.setText(tmp.getDsph());
				 this.jtDcylL.setText(tmp.getDcyl());
				 this.jtAxL.setText(tmp.getAx());
				 this.jtPosebnaIzradaL.setText(tmp.getSpecIzrada());
				 this.jtBojaLeceL.setText(tmp.getBoja());
				}

				Integer sfp=ulazni.getSifProizvodjaca();
				if(sfp==null)
				this.jtModelLeca.setText(ulazni.getModel());
				else
				{
					try {
						this.oznaceni=(ProizvodjacVO) DAOFactory.getInstance().getProizvodjaci().read(sfp);
					} catch (SQLException e) {
						Logger.fatal("OsobineLecaPanel - SQL iznimka kod ucitavanja proizvodjaca sa zadanom sifrom "+sfp.intValue(),e);
					}

					if (this.oznaceni!=null)
					this.jtModelLeca.setText(this.oznaceni.getNaziv());
					else
					Logger.log("OsobineLecaPanel - neprirodno stanje, za ucitavanje proizvodjaca za sifru "+sfp.intValue()+" vratilo se nazad null?!?");

				}//if sfp!=null

				//this.jcVrstaLeca.setSelectedItem()
		}//napuniPodatke

	// uvijek vrati NaocaleVO objekt, bez obzira bio prazan ili ne
	public LeceVO vratiPodatke()
	{
		LeceVO ulazni=this.getUlazni();

		OsobineLeceVO d,l;
		d= ulazni.getDesna();
		l= ulazni.getLijeva();

		//ako je jtModel leca prazno polje, znaci da oznaceni treba biti null
		//jer ako je oznacenom neka vrijednost postavljena, postavljena je i jtModel
		if (jtModelLeca.getText().equals(""))
		oznaceni=null; // mehanizam brisanja...

		ulazni.setModified(this.jeliObjektIzmjenjen());

		ulazni.setVrsta((TipLeceVO)jcVrstaLeca.getSelectedItem());
		ulazni.setPodvrsta((TipLeceVO)jcPodvrstaLeca.getSelectedItem());
		String n=jtNapomena.getText();
		if (n!=null && n.equals("")) n=null;
		ulazni.setNapomena(n); // 09.10.05. -asabo- treba napomenu dodati

		d.setBaznaKrivina(this.jtBaznaKrivinaD.getText());
		d.setDijametar(this.jtDijametarD.getText());
		d.setDsph(this.jtDsphD.getText());
		d.setDcyl(this.jtDcylD.getText());
		d.setAx(this.jtAxD.getText());
		d.setSpecIzrada(this.jtPosebnaIzradaD.getText());
		d.setBoja(this.jtBojaLeceD.getText());

		l.setBaznaKrivina(this.jtBaznaKrivinaL.getText());
		l.setDijametar(this.jtDijametarL.getText());
		l.setDsph(this.jtDsphL.getText());
		l.setDcyl(this.jtDcylL.getText());
		l.setAx(this.jtAxL.getText());
		l.setSpecIzrada(this.jtPosebnaIzradaL.getText());
		l.setBoja(this.jtBojaLeceL.getText());

           // osim sto oznaceni mora biti ne-null, mora i tekst unutar jtModela odgovarati oznacenom
	   if (oznaceni!=null && oznaceni.getNaziv().equals(jtModelLeca.getText().trim()))
	   {
	   	ulazni.setSifProizvodjaca(this.oznaceni.getSifra());
	   	ulazni.setModel(null);
	   }
	   else
	   {
		 ulazni.setModel(this.jtModelLeca.getText());
		 ulazni.setSifProizvodjaca(null); //just in case...
	   }

		return  ulazni;
	}//vratiPodatke

	// daje odogovor jeli sta korisnik na formi promjenio, ako je, onda idemo zapisivati, inace ne diramo...
	private boolean jeliObjektIzmjenjen()
	{
		LeceVO ulazni=this.getUlazni();

		if (ulazni==null) return false;

		TipLeceVO tmp=null,ozn=null;

		//06.10.05. -asabo- provjera jeli korisnik mjenjao vrstu lece
		tmp=ulazni.getVrsta();
		ozn=(TipLeceVO)jcVrstaLeca.getSelectedItem();
	  if (tmp!=null && ozn!=null && tmp.getSifra().intValue()!=ozn.getSifra().intValue() ) return true;

		//06.10.05. -asabo- provjera jeli korisnik mjenjao podvrstu lece
		tmp=ulazni.getPodvrsta();
		ozn=(TipLeceVO)jcPodvrstaLeca.getSelectedItem();
		if (tmp!=null && ozn!=null && (tmp.getSifra().intValue()!=ozn.getSifra().intValue()) ) return true;

		//ako su razliciti po pitanju da je jedan null a drugi nije znaci da se mjenjalo po objektu...
		if ((oznaceni==null)^(ulazni.getSifProizvodjaca()==null)) return true;

		//ako oba nisu null idemo provjeriti jeli su im iste vrijednosti koje nose...
		if (oznaceni!=null && ulazni.getSifProizvodjaca()!=null &&
		(oznaceni.getSifra().intValue()!=ulazni.getSifProizvodjaca().intValue()  || !jtModelLeca.getText().trim().equals(oznaceni.getNaziv()) ) ) return true;

		//ako sifre ne postoje provjeriti samu vrijednost model varijable, dali je razlicita
		if (oznaceni==null && ulazni.getSifProizvodjaca()==null && !this.jtModelLeca.getText().equals(ulazni.getModel()))
		return true;


		final String p="";

		OsobineLeceVO d,l;
		d= ulazni.getDesna();
		l= ulazni.getLijeva();

		if (d==null)
                    Logger.debug("desna leca je null kod OsobineLeca.jeliObjektIzmjenjen");

		if (l==null)
		Logger.debug("lijeva leca je null kod OsobineLeca.jeliObjektIzmjenjen");

		//09.10.05. -asabo- napomena dodana
		if (ulazni.getNapomena()==null &&!this.jtNapomena.getText().equals(p)) 	return true;

		//ako je objekt prazan, a neka vrijednost postoji na formi, tada je objekt novi i 'izmjenjen' je...
		if ((d==null || d.getBaznaKrivina()==null) &&!this.jtBaznaKrivinaD.getText().equals(p)) 	return true;
		if ((d==null || d.getDijametar()==null) 		&&!this.jtDijametarD.getText().equals(p)) 		return true;
		if ((d==null || d.getDsph()==null)			&&!this.jtDsphD.getText().equals(p)) 					return true;
		if ((d==null || d.getDcyl()==null)			&&!this.jtDcylD.getText().equals(p)) 					return true;
		if ((d==null || d.getAx()==null)			&&!this.jtAxD.getText().equals(p)) 						return true;
                if ((d==null || d.getSpecIzrada()==null) 	&&!this.jtPosebnaIzradaD.getText().equals(p)) return true;
		if ((d==null || d.getBoja()==null)              &&!this.jtBojaLeceD.getText().equals(p)) 			return true;

		if ( (l==null || l.getBaznaKrivina()==null) &&!this.jtBaznaKrivinaL.getText().equals(p)) 	return true;
		if ( (l==null || l.getDijametar()==null) 		&&!this.jtDijametarL.getText().equals(p)) 		return true;
		if ( (l==null || l.getDsph()==null) 				&&!this.jtDsphL.getText().equals(p)) 					return true;
		if ( (l==null || l.getDcyl()==null) 				&&!this.jtDcylL.getText().equals(p)) 					return true;
		if ( (l==null || l.getAx()==null) 					&&!this.jtAxL.getText().equals(p)) 						return true;
		if ( (l==null || l.getSpecIzrada()==null)		&&!this.jtPosebnaIzradaL.getText().equals(p)) return true;
		if ( (l==null || l.getBoja()==null) 				&&!this.jtBojaLeceL.getText().equals(p)) 			return true;

		//09.10.05. -asabo- napomena dodana
		if (ulazni.getNapomena()!=null &&!this.jtNapomena.getText().equals(ulazni.getNapomena())) 	return true;


		// GUI elementi su stavljeni prvi u usporedbu jer ne mogu biti null
		if (d.getBaznaKrivina()!=null &&!this.jtBaznaKrivinaD.getText().equals(d.getBaznaKrivina())) 	return true;
		if (d.getDijametar()!=null 		&&!this.jtDijametarD.getText().equals(d.getDijametar())) 				return true;
		if (d.getDsph()!=null 				&&!this.jtDsphD.getText().equals(d.getDsph())) 									return  true;
		if (d.getDcyl()!=null 				&&!this.jtDcylD.getText().equals(d.getDcyl())) 									return  true;
		if (d.getAx()!=null 					&&!this.jtAxD.getText().equals(d.getAx())) 											return  true;
		if (d.getSpecIzrada()!=null 	&&!this.jtPosebnaIzradaD.getText().equals(d.getSpecIzrada())) 	return true;
		if (d.getBoja()!=null 				&&!this.jtBojaLeceD.getText().equals(d.getBoja())) 							return true;

		if (l.getBaznaKrivina()!=null &&!this.jtBaznaKrivinaL.getText().equals(l.getBaznaKrivina())) 	return true;
		if (l.getDijametar()!=null 		&&!this.jtDijametarL.getText().equals(l.getDijametar())) 				return true;
		if (l.getDsph()!=null 				&&!this.jtDsphL.getText().equals(l.getDsph())) 									return  true;
		if (l.getDcyl()!=null 				&&!this.jtDcylL.getText().equals(l.getDcyl())) 									return  true;
		if (l.getAx()!=null 					&&!this.jtAxL.getText().equals(l.getAx())) 											return  true;
		if (l.getSpecIzrada()!=null		&&!this.jtPosebnaIzradaL.getText().equals(l.getSpecIzrada())) 	return true;
		if (l.getBoja()!=null 				&&!this.jtBojaLeceL.getText().equals(l.getBoja())) 							return true;
 		// jos treba provjeriti model naocala, ali to necemo zasada
	 return false;
	}//jeliObjektIzmjenjen


	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints13 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints14 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints15 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints17 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints16 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints19 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints18 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints20 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints21 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints23 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints24 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints45 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints25 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints46 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints47 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints48 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints49 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints50 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints52 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints53 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints54 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints55 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints56 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints110 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints26 = new java.awt.GridBagConstraints();
		consGridBagConstraints110.gridy = 4;
		consGridBagConstraints110.gridx = 4;
		consGridBagConstraints110.gridwidth = 1;
		consGridBagConstraints110.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints26.weightx = 1.0;
		consGridBagConstraints26.gridy = 4;
		consGridBagConstraints26.gridx = 5;
		consGridBagConstraints26.gridwidth = 3;
		consGridBagConstraints1.gridy = 4;
		consGridBagConstraints1.gridx = 1;
		consGridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints2.weightx = 1.0;
		consGridBagConstraints2.gridy = 4;
		consGridBagConstraints2.gridx = 2;
		consGridBagConstraints2.gridwidth = 2;
		consGridBagConstraints56.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints56.weightx = 1.0;
		consGridBagConstraints56.gridy = 3;
		consGridBagConstraints56.gridx = 7;
		consGridBagConstraints54.gridy = 1;
		consGridBagConstraints54.gridx = 7;
		consGridBagConstraints55.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints55.weightx = 1.0;
		consGridBagConstraints55.gridy = 2;
		consGridBagConstraints55.gridx = 7;
		consGridBagConstraints52.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints52.weightx = 1.0;
		consGridBagConstraints52.gridy = 2;
		consGridBagConstraints52.gridx = 6;
		consGridBagConstraints53.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints53.weightx = 1.0;
		consGridBagConstraints53.gridy = 3;
		consGridBagConstraints53.gridx = 6;
		consGridBagConstraints51.gridy = 1;
		consGridBagConstraints51.gridx = 6;
		consGridBagConstraints50.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints50.weightx = 1.0;
		consGridBagConstraints50.gridy = 3;
		consGridBagConstraints50.gridx = 5;
		consGridBagConstraints48.gridy = 1;
		consGridBagConstraints48.gridx = 5;
		consGridBagConstraints49.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints49.weightx = 1.0;
		consGridBagConstraints49.gridy = 2;
		consGridBagConstraints49.gridx = 5;
		consGridBagConstraints46.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints46.weightx = 1.0;
		consGridBagConstraints46.gridy = 2;
		consGridBagConstraints46.gridx = 4;
		consGridBagConstraints47.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints47.weightx = 1.0;
		consGridBagConstraints47.gridy = 3;
		consGridBagConstraints47.gridx = 4;
		consGridBagConstraints45.gridy = 1;
		consGridBagConstraints45.gridx = 4;
		consGridBagConstraints15.gridy = 2;
		consGridBagConstraints15.gridx = 0;
		consGridBagConstraints12.gridy = 0;
		consGridBagConstraints12.gridx = 1;
		consGridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints17.gridy = 1;
		consGridBagConstraints17.gridx = 1;
		consGridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints14.weightx = 1.0;
		consGridBagConstraints14.gridy = 0;
		consGridBagConstraints14.gridx = 6;
		consGridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints16.gridy = 3;
		consGridBagConstraints16.gridx = 0;
		consGridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints23.weightx = 1.0;
		consGridBagConstraints23.gridy = 2;
		consGridBagConstraints23.gridx = 3;
		consGridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints20.weightx = 1.0;
		consGridBagConstraints20.gridy = 2;
		consGridBagConstraints20.gridx = 2;
		consGridBagConstraints13.gridy = 0;
		consGridBagConstraints13.gridx = 4;
		consGridBagConstraints13.anchor = java.awt.GridBagConstraints.EAST;
		consGridBagConstraints22.gridy = 1;
		consGridBagConstraints22.gridx = 2;
		consGridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints24.weightx = 1.0;
		consGridBagConstraints24.gridy = 3;
		consGridBagConstraints24.gridx = 3;
		consGridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints21.weightx = 1.0;
		consGridBagConstraints21.gridy = 3;
		consGridBagConstraints21.gridx = 2;
		consGridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints19.weightx = 1.0;
		consGridBagConstraints19.gridy = 3;
		consGridBagConstraints19.gridx = 1;
		consGridBagConstraints25.gridy = 1;
		consGridBagConstraints25.gridx = 3;
		consGridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints18.weightx = 1.0;
		consGridBagConstraints18.gridy = 2;
		consGridBagConstraints18.gridx = 1;
		consGridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
		consGridBagConstraints11.weightx = 1.0;
		consGridBagConstraints11.gridy = 0;
		consGridBagConstraints11.gridx = 2;
		consGridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints11.gridwidth = 2;
		consGridBagConstraints13.gridwidth = 2;
		consGridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		consGridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		this.setLayout(new java.awt.GridBagLayout());
		this.add(getJcVrstaLeca(), consGridBagConstraints11);
		this.add(getJLabel(), consGridBagConstraints12);
		this.add(getJLabel1(), consGridBagConstraints13);
		this.add(getJcPodvrstaLeca(), consGridBagConstraints14);
		this.add(getJLabel2(), consGridBagConstraints15);
		this.add(getJLabel3(), consGridBagConstraints16);
		this.add(getJLabel4(), consGridBagConstraints17);
		this.add(getJtBaznaKrivinaD(), consGridBagConstraints18);
		this.add(getJtBaznaKrivinaL(), consGridBagConstraints19);
		this.add(getJtDijametarD(), consGridBagConstraints20);
		this.add(getJtDijametarL(), consGridBagConstraints21);
		this.add(getJLabel5(), consGridBagConstraints22);
		this.add(getJtDsphD(), consGridBagConstraints23);
		this.add(getJtDsphL(), consGridBagConstraints24);
		this.add(getJLabel6(), consGridBagConstraints25);
		this.add(getJLabel7(), consGridBagConstraints45);
		this.add(getJtDcylD(), consGridBagConstraints46);
		this.add(getJtDcylL(), consGridBagConstraints47);
		this.add(getJLabel8(), consGridBagConstraints48);
		this.add(getJtAxD(), consGridBagConstraints49);
		this.add(getJtAxL(), consGridBagConstraints50);
		this.add(getJLabel9(), consGridBagConstraints51);
		this.add(getJtPosebnaIzradaD(), consGridBagConstraints52);
		this.add(getJtPosebnaIzradaL(), consGridBagConstraints53);
		this.add(getJLabel10(), consGridBagConstraints54);
		this.add(getJtBojaLeceD(), consGridBagConstraints55);
		this.add(getJtBojaLeceL(), consGridBagConstraints56);
		this.add(getJLabel11(), consGridBagConstraints1);
		this.add(getJtModelLeca(), consGridBagConstraints2);
		this.add(getJLabel12(), consGridBagConstraints110);
		this.add(getJtNapomena(), consGridBagConstraints26);
		this.setSize(515, 115);
		this.setMinimumSize(new java.awt.Dimension(515, 115));

		this.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(BevelBorder.RAISED), "leæe", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.setPreferredSize(new java.awt.Dimension(515,115));
	}

                 public void omoguci()
		{this.postaviStatuseElemenata(true);	}

		public void onemoguci()
		{this.postaviStatuseElemenata(false);	}

		//14.11.05. -asabo- postavljamo status elemenata na formi
		public void postaviStatuseElemenata(boolean status)
		{
			this.jcVrstaLeca.setEnabled(status);
			this.jcPodvrstaLeca.setEnabled(status);

			this.jtBaznaKrivinaD.setEnabled(status);
			this.jtDijametarD.setEnabled(status);
			this.jtDcylD.setEnabled(status);
			this.jtDsphD.setEnabled(status);
			this.jtAxD.setEnabled(status);
			this.jtPosebnaIzradaD.setEnabled(status);
			this.jtBojaLeceD.setEnabled(status);

			this.jtBaznaKrivinaL.setEnabled(status);
			this.jtDijametarL.setEnabled(status);
			this.jtDcylL.setEnabled(status);
			this.jtDsphL.setEnabled(status);
			this.jtAxL.setEnabled(status);
			this.jtPosebnaIzradaL.setEnabled(status);
			this.jtBojaLeceL.setEnabled(status);

			this.jtModelLeca.setEnabled(status);
			this.jtNapomena.setEnabled(status);

		}//postaviStatus


	 public void pobrisiFormu()
	 {
	 	this.ulazni=null; // mozda i ne bi trebalo to raditi... ?
	 	this.onemoguci();
	 	final String p="";
	 	this.jtBaznaKrivinaD.setText(p);
	 	this.jtBaznaKrivinaL.setText(p);
	 	this.jtDijametarD.setText(p);
	 	this.jtDijametarL.setText(p);
	 	this.jtDsphD.setText(p);
	 	this.jtDsphL.setText(p);
	 	this.jtDcylD.setText(p);
	 	this.jtDcylL.setText(p);
	 	this.jtAxD.setText(p);
	 	this.jtAxL.setText(p);
	 	this.jtPosebnaIzradaD.setText(p);
	 	this.jtPosebnaIzradaL.setText(p);
	 	this.jtBojaLeceD.setText(p);
	 	this.jtBojaLeceL.setText(p);
	 	this.jtModelLeca.setText(p);
	 	this.jtNapomena.setText(p);
	 	this.oznaceni=null;
	 }//pobrisiFormu

	public javax.swing.JComboBox getJcVrstaLeca() {
		if(jcVrstaLeca == null) {
			jcVrstaLeca = new javax.swing.JComboBox();
			jcVrstaLeca.setPreferredSize(new java.awt.Dimension(100,25));
			jcVrstaLeca.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e)
				{
				napuniComboBox(jcPodvrstaLeca,jcVrstaLeca.getSelectedItem());
				}
			});
		}
		return jcVrstaLeca;
	}
	/**
	 * This method initializes jLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Vrsta leæa: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Podvrsta leæa: ");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jcPodvrstaLeca
	 *
	 * @return javax.swing.JComboBox
	 */
	public javax.swing.JComboBox getJcPodvrstaLeca() {
		if(jcPodvrstaLeca == null) {
			jcPodvrstaLeca = new javax.swing.JComboBox();
			jcPodvrstaLeca.setPreferredSize(new java.awt.Dimension(100,25));
		}
		return jcPodvrstaLeca;
	}
	/**
	 * This method initializes jLabel2
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText(" D ");
			jLabel2.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
		}
		return jLabel2;
	}
	/**
	 * This method initializes jLabel3
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText(" L ");
			jLabel3.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
		}
		return jLabel3;
	}
	/**
	 * This method initializes jLabel4
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Bazna krivina");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jtBaznaKrivinaD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtBaznaKrivinaD() {
		if(jtBaznaKrivinaD == null) {
			jtBaznaKrivinaD = new javax.swing.JTextField();
		}
		return jtBaznaKrivinaD;
	}
	/**
	 * This method initializes jtBaznaKrivinaL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtBaznaKrivinaL() {
		if(jtBaznaKrivinaL == null) {
			jtBaznaKrivinaL = new javax.swing.JTextField();
		}
		return jtBaznaKrivinaL;
	}
	/**
	 * This method initializes jtDijametarD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDijametarD() {
		if(jtDijametarD == null) {
			jtDijametarD = new javax.swing.JTextField();
		}
		return jtDijametarD;
	}
	/**
	 * This method initializes jtDijametarL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDijametarL() {
		if(jtDijametarL == null) {
			jtDijametarL = new javax.swing.JTextField();
		}
		return jtDijametarL;
	}
	/**
	 * This method initializes jLabel5
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Dijametar");
		}
		return jLabel5;
	}
	/**
	 * This method initializes jtDsphD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDsphD() {
		if(jtDsphD == null) {
			jtDsphD = new javax.swing.JTextField();
		}
		return jtDsphD;
	}
	/**
	 * This method initializes jtDsphL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDsphL() {
		if(jtDsphL == null) {
			jtDsphL = new javax.swing.JTextField();
		}
		return jtDsphL;
	}
	/**
	 * This method initializes jLabel6
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Dsph");
		}
		return jLabel6;
	}
	/**
	 * This method initializes jLabel7
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("Dcyl");
		}
		return jLabel7;
	}
	/**
	 * This method initializes jtDcylD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDcylD() {
		if(jtDcylD == null) {
			jtDcylD = new javax.swing.JTextField();
		}
		return jtDcylD;
	}
	/**
	 * This method initializes jtDcylL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtDcylL() {
		if(jtDcylL == null) {
			jtDcylL = new javax.swing.JTextField();
		}
		return jtDcylL;
	}
	/**
	 * This method initializes jLabel8
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if(jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText("AX");
		}
		return jLabel8;
	}
	/**
	 * This method initializes jtAxD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtAxD() {
		if(jtAxD == null) {
			jtAxD = new javax.swing.JTextField();
		}
		return jtAxD;
	}
	/**
	 * This method initializes jtAxL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtAxL() {
		if(jtAxL == null) {
			jtAxL = new javax.swing.JTextField();
		}
		return jtAxL;
	}
	/**
	 * This method initializes jLabel9
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel9() {
		if(jLabel9 == null) {
			jLabel9 = new javax.swing.JLabel();
			jLabel9.setText("Posebna izrada");
		}
		return jLabel9;
	}
	/**
	 * This method initializes jtPosebnaIzradaD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtPosebnaIzradaD() {
		if(jtPosebnaIzradaD == null) {
			jtPosebnaIzradaD = new javax.swing.JTextField();
		}
		return jtPosebnaIzradaD;
	}
	/**
	 * This method initializes jtPosebnaIzradaL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtPosebnaIzradaL() {
		if(jtPosebnaIzradaL == null) {
			jtPosebnaIzradaL = new javax.swing.JTextField();
		}
		return jtPosebnaIzradaL;
	}
	/**
	 * This method initializes jLabel10
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel10() {
		if(jLabel10 == null) {
			jLabel10 = new javax.swing.JLabel();
			jLabel10.setText("Boja");
		}
		return jLabel10;
	}
	/**
	 * This method initializes jtBojaLeceD
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtBojaLeceD() {
		if(jtBojaLeceD == null) {
			jtBojaLeceD = new javax.swing.JTextField();
		}
		return jtBojaLeceD;
	}
	/**
	 * This method initializes jtBojaLeceL
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtBojaLeceL() {
		if(jtBojaLeceL == null) {
			jtBojaLeceL = new javax.swing.JTextField();
		}
		return jtBojaLeceL;
	}
	/**
	 * This method initializes jLabel11
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel11() {
		if(jLabel11 == null) {
			jLabel11 = new javax.swing.JLabel();
			jLabel11.setText("model: ");
		}
		return jLabel11;
	}
	/**
	 * This method initializes jtModelLeca
	 *
	 * @return javax.swing.JTextField
	 */
	public javax.swing.JTextField getJtModelLeca() {
		if(jtModelLeca == null) {
			jtModelLeca = new javax.swing.JTextField();
			proizvodjaciLeca=new PretrazivanjeProzor(this.nosac,DAOFactory.getInstance().getProizvodjaci(),10,20,140,60,this.jtModelLeca);
			SearchCriteria kriterij=new SearchCriteria();
			kriterij.setKriterij(ProizvodjaciDAO.KRITERIJ_PROIZVODJACI_SAMO_LECE);
			proizvodjaciLeca.setKriterij(kriterij);
			jtModelLeca.addFocusListener(new java.awt.event.FocusAdapter() {
				@Override
				public void focusLost(java.awt.event.FocusEvent e) {
					proizvodjaciLeca.setFilter("");
				}
			});
			jtModelLeca.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					new Thread(){
					@Override
					public void run(){
						try{Thread.sleep(25);}catch(Exception e){}
						if (jtModelLeca.getText().equals("")) oznaceni=null; // mehanizam brisanja...
					}
					}.start();
				}
			});

			proizvodjaciLeca.dodajSlusaca(this);
		}
		return jtModelLeca;
	}
	public LeceVO getUlazni()
	{
		if (this.ulazni==null)
				{
					this.ulazni=new LeceVO();
					this.ulazni.setLijeva(new OsobineLeceVO());
					this.ulazni.setDesna(new OsobineLeceVO());
					// ne treba postavljati modified na true, radi se o novom objektu

					this.ulazni.setSifra(Integer.valueOf(DAO.NEPOSTOJECA_SIFRA));

					this.ulazni.setCreatedBy(Integer.valueOf(GlavniFrame.getSifDjelatnika()));
					this.ulazni.setDatum(java.util.Calendar.getInstance()); //zasada samo tako..
				}//if
		return this.ulazni;
	}

	/**
	 * This method initializes jLabel12
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel12() {
		if(jLabel12 == null) {
			jLabel12 = new javax.swing.JLabel();
			jLabel12.setText("Napomena: ");
		}
		return jLabel12;
	}
	/**
	 * This method initializes jtNapomena
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJtNapomena() {
		if(jtNapomena == null) {
			jtNapomena = new javax.swing.JTextField();
		}
		return jtNapomena;
	}

	public void labelaOznacena(Labela labela)
	{
		ProizvodjacVO pvo=null;
		pvo=(ProizvodjacVO)labela.getIzvornik();

		this.oznaceni=pvo;
		if (pvo!=null)
		this.jtModelLeca.setText(pvo.getNaziv());
		else this.jtModelLeca.setText("?!?");

	}//labelaOznacena
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
