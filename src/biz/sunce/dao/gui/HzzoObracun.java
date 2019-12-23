 
package biz.sunce.dao.gui;

import biz.sunce.dao.*;
import biz.sunce.opticar.vo.*;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.toedter.calendar.JDateChooser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.SlusacDateChoosera;

public final class HzzoObracun extends JPanel
    implements GUIEditor<HzzoObracunVO>, SlusacDateChoosera, ItemListener
{ 
	private static final long serialVersionUID = 1L;
	private JDateChooser datum;
    private JLabel jLabel;
    private HzzoObracunVO oznaceni;
    private ArrayList<MjestoVO> podruznice;
    private HzzoObracunDAO obracunDAO;
    private RacunDAO racunDAO;
    @SuppressWarnings("rawtypes")
	private JComboBox jcPodruznica;
    private JLabel jLabel1;
    private JLabel jLabel2;
    @SuppressWarnings("rawtypes")
	private JComboBox jcOsiguranje;
    public static final int OSIGURANJE_HZZO = 1;
    public static final int OSIGURANJE_HZZOPZZR = 2;

    public HzzoObracun()
    {
        datum = null;
        jLabel = null;
        oznaceni = null;
        podruznice = null;
        obracunDAO = null;
        racunDAO = null;
        jcPodruznica = null;
        jLabel1 = null;
        jLabel2 = null;
        jcOsiguranje = null;
        initialize();
    }

    private HzzoObracunDAO getObracunDAO()
    {
        if(obracunDAO == null)
            obracunDAO = DAOFactory.getInstance().getHzzoObracuni();
        return obracunDAO;
    }

    private RacunDAO getRacunDAO()
    {
        if(racunDAO == null)
            racunDAO = DAOFactory.getInstance().getRacuni();
        return racunDAO;
    }

    private void initialize()
    {
        GridBagConstraints consGridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints consGridBagConstraints11 = new GridBagConstraints();
        GridBagConstraints consGridBagConstraints21 = new GridBagConstraints();
        GridBagConstraints consGridBagConstraints12 = new GridBagConstraints();
        GridBagConstraints consGridBagConstraints22 = new GridBagConstraints();
        GridBagConstraints consGridBagConstraints1 = new GridBagConstraints();
        consGridBagConstraints12.gridy = 2;
        consGridBagConstraints12.gridx = 0;
        consGridBagConstraints12.anchor = 13;
        consGridBagConstraints22.fill = 0;
        consGridBagConstraints22.weightx = 1.0D;
        consGridBagConstraints22.gridy = 2;
        consGridBagConstraints22.gridx = 1;
        consGridBagConstraints11.fill = 0;
        consGridBagConstraints11.weightx = 1.0D;
        consGridBagConstraints11.gridy = 1;
        consGridBagConstraints11.gridx = 1;
        consGridBagConstraints11.anchor = 17;
        consGridBagConstraints11.insets = new Insets(0, 5, 0, 0);
        consGridBagConstraints22.insets = new Insets(0, 5, 0, 0);
        consGridBagConstraints22.anchor = 17;
        consGridBagConstraints21.gridy = 1;
        consGridBagConstraints21.gridx = 0;
        consGridBagConstraints21.anchor = 13;
        consGridBagConstraints2.gridy = 0;
        consGridBagConstraints2.gridx = 0;
        consGridBagConstraints1.gridy = 0;
        consGridBagConstraints1.gridx = 1;
        consGridBagConstraints1.insets = new Insets(0, 5, 0, 0);
        consGridBagConstraints1.anchor = 17;
        setLayout(new GridBagLayout());
        add(getDatum(), consGridBagConstraints1);
        add(getJLabel(), consGridBagConstraints2);
        add(getJcPodruznica(), consGridBagConstraints11);
        add(getJLabel1(), consGridBagConstraints21);
        add(getJLabel2(), consGridBagConstraints12);
        add(getJcOsiguranje(), consGridBagConstraints22);
        setSize(327, 88);
    }

    public void napuniPodatke(HzzoObracunVO ulaz)
    {
        HzzoObracunVO hvo = (HzzoObracunVO)ulaz;
        this.oznaceni = hvo;
        if( this.oznaceni == null )
        {
            pobrisiFormu();
            return;
        }
        String hzzo = "HZZO";
        if(oznaceni.getSifOsiguranja() != null && oznaceni.getSifOsiguranja().intValue() == 2)
            hzzo = "HZZO ZZR";
        jcOsiguranje.setSelectedItem(hzzo);
       
        if(oznaceni != null && oznaceni.getDatum() != null)
            datum.setDatum(oznaceni == null ? null : oznaceni.getDatum());
        
        boolean skiniNeizvedive=this.omogucen;
        
        ucitajPopisPodruznica(skiniNeizvedive);
        
        Integer sfp = oznaceni.getSifPodruznice();
        if(sfp != null)
        {
            int i = 0;
            do
            {
                if(i >= jcPodruznica.getItemCount())
                    break;
                MjestoVO mvo = (MjestoVO)jcPodruznica.getItemAt(i);
                if(mvo != null && mvo.getSifra().intValue() == sfp.intValue())
                {
                    jcPodruznica.setSelectedIndex(i);
                    break;
                }
                i++;
            } while(true);
        }
        int sfo = oznaceni.getSifOsiguranja() == null ? 1 : oznaceni.getSifOsiguranja().intValue();
        if(sfo == 1)
            jcOsiguranje.setSelectedItem("HZZO");
        else
        if(sfo == 2)
            jcOsiguranje.setSelectedItem("HZZO ZZR");
        else
            Logger.log("Neprirodno stanje za sifra osiguranja parametar: " + sfo);
         
    }

    public HzzoObracunVO vratiPodatke()
    {
        if( this.oznaceni == null ){
            Logger.log("VratiPodatke - oznaceni je null?!?");
        	return null;
        }
        
        oznaceni.setModified(jeliIzmjenjen());
        oznaceni.setDatum(getDatum().getDatum()); 
        
        Integer sifP = null;
        MjestoVO mvo = (MjestoVO)jcPodruznica.getSelectedItem();
        if(mvo == null)
        {
            GlavniFrame.alert("Ne postoji odabrana podru\u017Enica osiguranja!");
            return null;
        } 
        else
        {
            sifP = mvo.getSifra();
            oznaceni.setSifPodruznice(sifP);
            int sfo = jcOsiguranje.getSelectedItem().equals("HZZO") ? OSIGURANJE_HZZO : OSIGURANJE_HZZOPZZR;
            oznaceni.setSifOsiguranja(Integer.valueOf(sfo));
            return oznaceni;
        }
    }//vratiPodatke

    private ArrayList<MjestoVO> getPodruznice()
    {
        if(podruznice == null)
        {
            SearchCriteria<String> krit = new SearchCriteria<String>();
            ArrayList<String> l = new ArrayList<String>(1);
            l.add("");
            krit.setPodaci(l);
            try
            {
                podruznice = (ArrayList<MjestoVO>)DAOFactory.getInstance().getMjesta().findAll(krit);
            }
            catch(SQLException e)
            {
                Logger.fatal("SQL iznimka kod tra\u017Eenja popisa svih hzzo podru\u017Enica", e);
                JOptionPane.showMessageDialog(GlavniFrame.getInstanca(), "Nastao je problem pri tra\u017Eenju hzzo podru\u017Enica. Kontaktirajte administratora sustava", "Upozorenje!", 2);
            }
        }
        
        return podruznice;
    }

    public void pobrisiFormu()
    {
        oznaceni = null;
        onemoguci();
    }

    public boolean isFormaIspravna()
    {
        return false;
    }

    private boolean omogucen = true;
    
    public void omoguci()
    {
    	this.omogucen=true;
        postaviStatuseElemenata(true);
    }

    public void onemoguci()
    {
    	this.omogucen=false;
        postaviStatuseElemenata(false);
    }

    private void postaviStatuseElemenata(boolean s)
    {
        getDatum().setEnabled(s);
        getJcPodruznica().setEnabled(s);
        getJcOsiguranje().setEnabled(s);
    }

    public boolean jeliIzmjenjen()
    {
        if(oznaceni == null)
            return false;
        if(oznaceni.getDatum() == null)
            return true;
        if(oznaceni.getSifPodruznice() == null)
            return true;
        if(jcPodruznica == null)
            Logger.log("jCPodruznica je null kod HzzoObracn.jeliObjektIzmjenjen ?!?");
        MjestoVO mvo = (MjestoVO)jcPodruznica.getSelectedItem();
        if(mvo == null)
            Logger.log("mvo je null kod HzzoObracun.jeliObjektIzmjenjen ?!?");
        if(oznaceni.getSifPodruznice().intValue() != mvo.getSifra().intValue())
            return true;
        else
            return !oznaceni.getDatum().equals(getDatum().getDatum());
    }

    private JDateChooser getDatum()
    {
        if(datum == null)
        {
            datum = new JDateChooser();
            datum.setDatum(Calendar.getInstance());
            datum.setToolTipText("datum kada je obra\u010Dun napravljen");
            datum.setPreferredSize(new Dimension(120, 20));
            datum.setMinimumSize(new Dimension(120, 20));
            datum.dodajSlusaca(this);
        }
        return datum;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void ucitajPopisPodruznica(boolean skiniNeizvedive)
    {    	
        ArrayList<MjestoVO> pod = getPodruznice();
        jcPodruznica.removeAllItems();
        
        //neizvedive podruznice su one za koje nema racuna u zadanom datumskom rasponu, 
    	//pa nema ni smisla ponuditi ih korisniku, jer bi obracun bio sa nula racuna    	
        if (!skiniNeizvedive)
        {        		
        	for (MjestoVO mvo:pod)
        	jcPodruznica.addItem(mvo);
        	
        	return;        	
        }
        
        if(pod == null)
            Logger.log("POD je null kod HzzoObracun.ucitajPopisPodruznica");
		else {
			int podSize = pod==null ? 0 : pod.size();
			
			if (pod!=null && podSize==0)
        		Logger.log("POD je prazan kod HzzoObracun.ucitajPopisPodruznica");
        	else
            if(pod != null)
             {
              for(int i = 0; i < podSize; i++)
              {
                MjestoVO mvo = (MjestoVO)pod.get(i);
                SearchCriteria krit = new SearchCriteria();
                krit.setKriterij("krit_svi_obracuni_za_podruznicu_hzzo");
                ArrayList<Object> l = new ArrayList<Object>(4);
                l.add(datum.getDatum());
                l.add(mvo.getSifra());
                l.add(null);
                int osiguranje = getJcOsiguranje().getSelectedItem().equals("HZZO") ? 1 : 2;
                l.add(Integer.valueOf(osiguranje));
                krit.setPodaci(l);
                try
                {
                    Calendar datObrPrethodni = null;
                    List<HzzoObracunVO> obracuni = getObracunDAO().findAll(krit);
                    
                    if(obracuni != null && obracuni.size() > 0)
                    {
                        HzzoObracunVO hvo = obracuni.get(0);
                        datObrPrethodni = hvo.getDatum();
                    }
                    
                    krit = new SearchCriteria();
                    krit.setKriterij("krit_svi_racuni_za_obracun");
                    l = new ArrayList<Object>(3);
                    //mozda nema prethodnog obracuna za tu poslovnicu, tada ce datObrPrethodni biti null..
                    l.add(datObrPrethodni);
                    l.add(datum.getDatum());
                    l.add(mvo.getSifra());
                    l.add(Integer.valueOf(osiguranje));
                    krit.setPodaci(l);
//                    if (mvo.getNaziv().equals("Pazin"))
//                    	System.out.println("Tu sam"+mvo.getSifDrzave()+":"+mvo.getSifra());
                    //da vidimo ima li za ovo mjesto racuna u periodu nekakvih racuna
                    l = (ArrayList)getRacunDAO().findAll(krit);
                    if(l != null && l.size() > 0)
                        jcPodruznica.addItem(mvo);
                    continue;
                }
                catch(SQLException e)
                {
                    Logger.fatal("SQL iznimka kod tra\u017Eenja popisa hzzo podru\u017Enica za zadani datum", e);
                    JOptionPane.showMessageDialog(getParent(), "Nastao je problem pri tra\u017Eenju podru\u017Enica. Provjerite poruke sustava", "Upozorenje", 2);
                    break;
                }
                catch(Exception e)
                {
                    Logger.fatal("Op\u0107enita iznimka kod tra\u017Eenja popisa hzzo podru\u017Enica za zadani datum", e);
                }
                
                JOptionPane.showMessageDialog(getParent(), "Nastala je iznimka pri tra\u017Eenju podru\u017Enica. Provjerite poruke sustava", "Upozorenje", 2);
                break;
            }//for i 

         }
		}
    }

    private JLabel getJLabel()
    {
        if(jLabel == null)
        {
            jLabel = new JLabel();
            jLabel.setText("Datum obra\u010Duna: ");
        }
        return jLabel;
    }

    public void dodajSlusacaSpremnostiPodataka(biz.sunce.dao.GUIEditor.SlusacSpremnostiPodataka slusacspremnostipodataka)
    {
    }

    @SuppressWarnings("rawtypes")
	private JComboBox getJcPodruznica()
    {
        if(this.jcPodruznica == null)
        {
            this.jcPodruznica = new JComboBox();
            this.jcPodruznica.setPreferredSize(new Dimension(120, 25));
            this.jcPodruznica.setMinimumSize(new Dimension(120, 25));
            this.ucitajPopisPodruznica(false);
        }
        return jcPodruznica;
    }

    private JLabel getJLabel1()
    {
        if(jLabel1 == null)
        {
            jLabel1 = new JLabel();
            jLabel1.setText("Podru\u017Enica: ");
        }
        return jLabel1;
    }
 
    private JLabel getJLabel2()
    {
        if(jLabel2 == null)
        {
            jLabel2 = new JLabel();
            jLabel2.setText("Osiguranje: ");
        }
        return jLabel2;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getJcOsiguranje()
    {
        if(jcOsiguranje == null)
        {
            jcOsiguranje = new JComboBox();
            jcOsiguranje.setPreferredSize(new Dimension(120, 25));
            jcOsiguranje.addItem("HZZO");
            jcOsiguranje.addItem("HZZO ZZR");
            jcOsiguranje.setSelectedItem("HZZO");
            jcOsiguranje.addItemListener(this);
        }
        return jcOsiguranje;
    }

    public void itemStateChanged(ItemEvent e)
    {
        if(e.getStateChange() == 1)
            ucitajPopisPodruznica(this.omogucen);
    }

	public void datumIzmjenjen(DatumskoPolje pozivatelj) {
		 ucitajPopisPodruznica(this.omogucen);
	}
}