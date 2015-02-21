// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IspisRacunaZZR.java

package biz.sunce.optika.hzzo.ispis;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.MjestoVO;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.RacunVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class IspisRacunaZZR
    implements Printable
{

    public IspisRacunaZZR(RacunVO racun)
    {
        visinaPodnozja = 0;
        p = new PostavkeBean();
        boja = new Color(134, 176, 133);
        this.zaglX = 0;
        this.zaglY = 0;
        this.racX = 0;
        this.racY = 0;
        napX = 0;
        napY = 0;
        glavni = new Font("Arial", 0, 12);
        mono = new Font("Courier New", 1, 16);
        mali = new Font("Times New Roman", 0, 9);
        nazivPomagalaMaliFont = new Font("Arial", 0, 7);
        centuryGothicVeliki = new Font("Century Gothic", 0, 12);
        centuryGothicVelikiBold = new Font("Century Gothic", 1, 12);
        centuryGothicSrednji = new Font("Century Gothic", 0, 9);
        centuryGothicSrednjiBold = new Font("Century Gothic", 1, 9);
        centuryGothicMali = new Font("Century Gothic", 0, 6);
        centuryGothicExtraMali = new Font("Century Gothic", 0, 4);
        centuryGothicMaliItalic = new Font("Century Gothic", 2, 6);
        crtice = new BasicStroke(0.7F, 0, 0, 1.0F, dash, 0.0F);
        pocetakStraniceOdskokX = 0;
        pocetakStraniceOdskokY = 0;
        omjerSir = 1.0F;
        omjerDuz = 1.0F;
        ddY = 0;
        this.racun = null;
        this.racun = racun;
        pFormat = new PageFormat();
        pJob = PrinterJob.getPrinterJob();
        Paper papir = new Paper();
        int marginaT = 30;
        int marginaL = 28;
        papir.setSize(A4_SIRINA_PX, A4_VISINA_PX);
        papir.setImageableArea(marginaL, marginaT, papir.getWidth() - (double)(marginaL * 2), papir.getHeight() - (double)(marginaT * 2));
        pFormat.setPaper(papir);
        String osX = PostavkeBean.getPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_X, "0");
        String osY = PostavkeBean.getPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_ODSKOK_Y, "0");
        String omSir = PostavkeBean.getPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_SIRINA, "1.0");
        String omDuz = PostavkeBean.getPostavkaDB(PostavkeBean.TVRTKA_HZZO_RACUN_OMJER_DUZINA, "1.0");
        try
        {
            pocetakStraniceOdskokX = Integer.parseInt(osX);
            pocetakStraniceOdskokY = Integer.parseInt(osY);
        }
        catch(NumberFormatException nfe)
        {
            Logger.log("ZZR Neispravne vrijednosti za parametre x i y kod ispisa x:" + osX + " y:" + osY, null);
        }
        try
        {
            omjerSir = Float.parseFloat(omSir);
            omjerDuz = Float.parseFloat(omDuz);
        }
        catch(NumberFormatException nfe)
        {
            Logger.log("ZZR Neispravne vrijednosti za parametre sir i duz kod ispisa sir:" + omSir + " duz:" + omDuz, null);
            omjerSir = 1.0F;
            omjerDuz = 1.0F;
        }
        int zaglX = 0;
        int zaglY = 0;
        int racX = (int)((float)zaglX * omjerSir + 0.5F);
        int racY = (int)((float)zaglY * omjerDuz + 210F * omjerDuz + 0.5F);
        napX = (int)((float)zaglX * omjerSir + 0.5F);
        napY = (int)((float)zaglY + 500F * omjerDuz + 0.5F);
    }

    public void printDialog()
    {
        printaj(true);
    }

    public void printaj(boolean prikaziDialog)
    {
        pJob.setCopies(brojKopija);
        boolean printanjesaDialogom = prikaziDialog && pJob.printDialog();
        if(printanjesaDialogom || !prikaziDialog)
        {
            PageFormat pf = pFormat;
            zaglX = (int)((pf.getImageableX() + 10D) * (double)omjerSir);
            zaglY = (int)(pf.getImageableY() * (double)omjerDuz);
            zaglX += pocetakStraniceOdskokX;
            zaglY += pocetakStraniceOdskokY;
            racX = zaglX;
            racY = (int)((float)zaglY + 210F * omjerSir + 0.5F);
            napX = zaglX;
            napY = (int)((float)zaglY + 675F * omjerDuz + 0.5F);
            pJob.setPrintable(this, pf);
            String naslov = "rn." + (racun == null ? "?!?" : racun.getBrojOsobnogRacunaOsnovno()) + " Hzzo";
            pJob.setJobName(naslov);
            try
            {
                pJob.print();
                brojKopija = pJob.getCopies();
            }
            catch(PrinterException printerException)
            {
                Logger.fatal("Iznimka prilikom ispisa hzzo racuna: ", printerException);
            }
            catch(Error greska)
            {
                Logger.fatal("Greska sustava prilikom ispisa hzzo racuna: ", greska);
            }
        }
    }

    public void finalize()
    {
        pJob = null;
        mali = null;
        mono = null;
        pFormat = null;
        racun = null;
        p = null;
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
        throws PrinterException
    {
        if(pageIndex > 0)
            return 1;
        int desnaKolona = (racX + 350) - 23;
        iscrtajObrazac(g);
        g.setFont(glavni);
        g.setColor(Color.black);
        MjestoVO podrUred = racun.getPodrucniUred();
        if(podrUred == null)
            try
            {
                podrUred = (MjestoVO)DAOFactory.getInstance().getMjesta().read(racun.getSifPodrucnogUreda());
            }
            catch(SQLException e)
            {
                Logger.log("ZZR Iznimka kod trazenja mjesta podrucnog ureda za racun (ispisOsn) osn: " + racun.getBrojOsobnogRacunaOsnovno(), e);
            }
        String pnaziv = podrUred == null ? "" : podrUred.getNaziv();
        g.drawString(pnaziv, (int)((float)zaglX + 75F * omjerSir), (int)((float)zaglY + 64F * omjerDuz));
        g.drawString(pnaziv, (int)((float)zaglX + 50F * omjerSir), (int)((float)zaglY + 83F * omjerDuz));
        String ulica = "";
        if(podrUred != null)
            ulica = PostavkeBean.getPostavkaDB("hzzozzr_adr_" + podrUred.getSifra().intValue(), "");
        g.drawString(ulica, (int)((float)zaglX + 62F * omjerSir), (int)((float)zaglY + 115F * omjerDuz));
        String brojOsobnogRacuna = racun.getBrojOsobnogRacunaOsnovno() == null ? "" : racun.getBrojOsobnogRacunaOsnovno();
        g.drawString(brojOsobnogRacuna, (int)((float)racX + 380F * omjerSir), (int)((float)ddY + 136F * omjerDuz));
        g.setFont(mono);
        String sifIsporucitelja = PostavkeBean.getHzzoSifraIsporucitelja();
        for(int i = 0; i < sifIsporucitelja.length(); i++)
            g.drawString(sifIsporucitelja.charAt(i) + "", (int)((double)((float)racX + 99F * omjerSir) + (double)i * 13.5D * (double)omjerSir), (int)((float)ddY + 37F * omjerDuz));

        String brojPotvrde = racun.getBrojPotvrde1();
        String brojRacuna = racun.getBrojPotvrde2();
        if(brojPotvrde.length() == 2)
            brojPotvrde = " " + brojPotvrde;
        else
        if(brojPotvrde.length() == 1)
            brojPotvrde = "  " + brojPotvrde;
        for(int i = 0; i < brojPotvrde.length(); i++)
            g.drawString(brojPotvrde.charAt(i) + "", (int)((double)((float)racX + 340F * omjerSir) + (double)i * 11.9D * (double)omjerSir + 0.5D), (int)((float)ddY + 40F * omjerDuz + 0.5F));

        for(int i = 0; i < brojRacuna.length(); i++)
            g.drawString(brojRacuna.charAt(i) + "", (int)((double)((float)desnaKolona + 57F * omjerSir) + (double)i * 12D * (double)omjerSir + 0.5D), (int)((float)ddY + 40F * omjerDuz + 0.5F));

        String mbIsporucitelja = p.getTvrtkaOIB();
        if(mbIsporucitelja != null && mbIsporucitelja.length() <= 8)
        {
            for(int i = 0; i < mbIsporucitelja.length(); i++)
                g.drawString(mbIsporucitelja.charAt(i) + "", (int)((double)((float)racX + 367F * omjerSir) + (double)i * 13.5D * (double)omjerSir + 0.5D), (int)((float)ddY + 177F * omjerDuz + 0.5F));

        } else
        if(mbIsporucitelja != null && mbIsporucitelja.length() > 8)
        {
            g.setFont(glavni);
            g.drawString(mbIsporucitelja, (int)((float)racX + 367F * omjerSir + 0.5F), (int)((float)ddY + 177F * omjerDuz + 0.5F));
        }
        g.setFont(glavni);
        String nazivOptike = p.getTvrtkaNaziv();
        g.drawString(nazivOptike, (int)((float)racX + 45F * omjerSir + 0.5F), (int)((float)ddY + 61F * omjerDuz + 0.5F));
        String adresaOptike = p.getMjestoRada() + ", " + p.getTvrtkaAdresa();
        g.drawString(adresaOptike, (int)((float)racX + 117F * omjerSir + 0.5F), (int)((float)ddY + 100F * omjerDuz + 0.5F));
        g.drawString(p.getTvrtkaRacun(), (int)((float)racX + 75F * omjerSir + 0.5F), (int)((float)ddY + 136F * omjerDuz + 0.5F));
        String poziv1 = racun.getPozivNaBroj1();
        String poziv2 = racun.getPozivNaBroj2();
        if(poziv1 == null)
            poziv1 = "";
        if(poziv2 == null)
            poziv2 = "";
        g.drawString(poziv1, (int)((float)zaglX + 66F * omjerSir + 0.5F), (int)((float)ddY + 176F * omjerDuz + 0.5F));
        g.drawString(poziv2, (int)((float)zaglX + 83F * omjerSir + 0.5F), (int)((float)ddY + 176F * omjerDuz + 0.5F));
        java.util.List stavke = racun.getStavkeRacuna();
        if(stavke == null)
            try
            {
                stavke = (ArrayList)DAOFactory.getInstance().getStavkeRacuna().findAll(racun);
            }
            catch(SQLException ex)
            {
                Logger.log("ZZRSQL iznimka kod trazenja stavki racuna za racun br.oso.osn.:" + (racun == null ? "?!?" : racun.getBrojOsobnogRacunaOsnovno()), ex);
            }
        int startcy = (int)((float)ddY + 226.5F * omjerSir + 0.5F);
        double odskokRetka = 21.100000000000001D * (double)omjerDuz;
        int suma = 0;
        int sumaPorezneOsnove = 0;
        int sumaPoreza = 0;
        PoreznaStopaVO stopa = null;
        int sirinaPoljaNazivPomagala = (int)(96F * omjerSir + 0.5F);
        for(int sf = 0; sf < stavke.size(); sf++)
        {
            StavkaRacunaVO st = (StavkaRacunaVO)stavke.get(sf);
            PomagaloVO pom = nadjiPomagalo(st.getSifArtikla());
            stopa = nadjiPoreznuSkupinu(st.getPoreznaStopa());
            String sfp = st.getSifProizvodjaca() == null ? "" : "" + st.getSifProizvodjaca().intValue();
            g.setFont(mono);
            String sif = st.getSifArtikla();
            int cy = (int)((double)startcy + (double)sf * odskokRetka);
            for(int i = 0; i < sif.length(); i++)
                g.drawString(sif.charAt(i) + "", (int)((double)((float)racX + 135F * omjerSir) + (double)i * 13.5D * (double)omjerSir + 0.5D), cy);

            if(sfp != null && sfp.trim().length() > 0)
            {
                sfp = sfp.trim();
                int razl = 7 - sfp.length();
                for(int i = 0; i < razl; i++)
                    sfp = " " + sfp;

                for(int i = 0; i < sfp.length(); i++)
                    g.drawString(sfp.charAt(i) + "", (int)((double)((float)racX + 237F * omjerSir) + (double)i * 13.5D * (double)omjerSir + 0.5D), cy);

            }
            g.setFont(nazivPomagalaMaliFont);
            String naziv = pom == null ? "?!?" : pom.getNaziv();
            FontMetrics fm = g.getFontMetrics(nazivPomagalaMaliFont);
            int sir = fm.stringWidth(naziv);
            int lijeviRubNazivPomagala = (int)((float)racX + 36F * omjerSir + 0.5F);
            if(sir <= sirinaPoljaNazivPomagala)
            {
                g.drawString(naziv, lijeviRubNazivPomagala, cy);
            } else
            {
                int mj = 0;
                int dozSir = 0;
                do
                {
                    if(mj >= naziv.length())
                        break;
                    dozSir += fm.charWidth(naziv.charAt(mj));
                    mj++;
                } while(dozSir < sirinaPoljaNazivPomagala);
                g.drawString(naziv.substring(0, mj), lijeviRubNazivPomagala, cy - nazivPomagalaMaliFont.getSize());
                int prvoMjesto = mj;
                dozSir = 0;
                do
                {
                    if(mj >= naziv.length())
                        break;
                    dozSir += fm.charWidth(naziv.charAt(mj));
                    mj++;
                } while(dozSir < sirinaPoljaNazivPomagala);
                g.drawString(naziv.substring(prvoMjesto, mj), lijeviRubNazivPomagala, cy);
            }
            int cijenaBezPDVa = (int)((float)st.getPoCijeni().intValue() / (1.0F + (float)stopa.getStopa().intValue() / 100F) + 0.5F);
            int totalBezPDVa = st.getKolicina().intValue() * cijenaBezPDVa;
            int total = (int)((float)totalBezPDVa * (1.0F + (float)stopa.getStopa().intValue() / 100F) + 0.5F);
            int poreznaOsnova = totalBezPDVa;
            int porez = total - totalBezPDVa;
            ispisiKolicinu(g, st.getKolicina().intValue(), (int)((float)racX + 360F * omjerSir + 0.5F), cy, glavni);
            ispisiNovac(g, st.getPoCijeni().intValue(), (int)((float)racX + 417F * omjerSir + 0.5F), cy, glavni);
            ispisiNovac(g, total, (int)((float)racX + 498F * omjerSir + 0.5F), cy, glavni);
            suma += total;
            sumaPorezneOsnove += poreznaOsnova;
            sumaPoreza += porez;
        }

        ispisiNovac(g, suma, (int)((float)racX + 498F * omjerSir + 0.5F), (int)((double)startcy + 7D * odskokRetka), glavni);
        int desniRub = (int)(480F * omjerSir + 0.5F);
        int iznosSudjelovanja = racun.getIznosSudjelovanja() == null ? 0 : racun.getIznosSudjelovanja().intValue();
        boolean nemaPoreza = sumaPoreza == 0;
        float omjerZdr = (float)(suma - iznosSudjelovanja) / (float)suma;
        sumaPoreza = (int)((float)sumaPoreza * omjerZdr + 0.5F);
        if(!racun.getOsnovnoOsiguranje().booleanValue())
            iznosSudjelovanja = 0;
        startcy = (int)(((double)startcy + 9D * odskokRetka) - 3D);
        odskokRetka = 17D;
        ispisiNovac(g, suma, racX + desniRub, (int)((double)startcy + 0.0D * odskokRetka), glavni);
        ispisiNovac(g, 0, racX + desniRub, (int)((double)startcy + 1.0D * odskokRetka), glavni);
        ispisiNovac(g, racun.getIznosOsnovnogOsiguranja().intValue(), racX + desniRub, (int)((double)startcy + 2D * odskokRetka), glavni);
        ispisiNovac(g, sumaPoreza, racX + desniRub, (int)((double)startcy + 3D * odskokRetka), glavni);
        g.setFont(mali);
        if(nemaPoreza)
            g.drawString("Oslobo\u0111eno pla\u0107anja PDV-a po \u010Dl. 10A Zakona o PDV-u.", desniRub - 160, (int)((double)startcy + 7D * odskokRetka));
        g.setFont(glavni);
        Calendar c = racun.getDatumNarudzbe();
        String datum = "" + c.get(5) + ". " + Util.mjeseci[c.get(2)];
        int god = c.get(1) - 2000;
        g.setFont(mali);
        g.drawString(datum, (int)((float)racX + 70F * omjerSir + 0.5F), (int)((double)(1.0F * omjerDuz + (float)startcy) + 4D * odskokRetka + 8.5D));
        g.drawString("" + (god >= 10 ? "" + god : "0" + god), racX + 135, (int)((double)(1.0F * omjerDuz + (float)startcy) + 4D * odskokRetka + 8.5D));
        Calendar c2 = racun.getDatumIzdavanja();
        datum = "" + c2.get(5) + ". " + Util.mjeseci[c2.get(2)];
        god = c2.get(1) - 2000;
        g.drawString(p.getMjestoRada(), (int)((float)racX + 55F * omjerSir + 0.5F), (int)((double)(5F * omjerDuz + (float)startcy) + 6D * odskokRetka + 9.5D));
        g.drawString(datum, (int)((float)racX + 130F * omjerSir + 0.5F), (int)((double)(5F * omjerDuz + (float)startcy) + 6D * odskokRetka + 9.5D));
        g.drawString("" + (god >= 10 ? "" + god : "0" + god), (int)((float)racX + 190F * omjerSir + 0.5F), (int)((double)(5F * omjerDuz + (float)startcy) + 6D * odskokRetka + 9.5D));
        DjelatnikVO dvo = GlavniFrame.getDjelatnik();
        String odgOsoba = dvo == null ? "?!?" : dvo.getIme() + " " + dvo.getPrezime();
        g.drawString(odgOsoba, (int)(435F * omjerSir + 0.5F), (int)((double)(5F * omjerDuz + (float)startcy) + 8D * odskokRetka + 11.5D));
        return 0;
    }

    private PomagaloVO nadjiPomagalo(String sifra)
    {
        PomagaloVO pom = null;
        try
        {
            if(sifra != null && !sifra.equals(""))
                pom = (PomagaloVO)DAOFactory.getInstance().getPomagala().read(sifra);
            else
                pom = null;
        }
        catch(SQLException e)
        {
            Logger.fatal("SQL iznimka kod Ispisa ra\u010Duna osn. osiguranje pri citanju odredjenog pomagala.. ", e);
        }
        return pom;
    }

    private PoreznaStopaVO nadjiPoreznuSkupinu(Integer sifra)
    {
        PoreznaStopaVO ps = null;
        try
        {
            if(sifra != null)
                ps = (PoreznaStopaVO)DAOFactory.getInstance().getPorezneStope().read(sifra);
            else
                ps = null;
        }
        catch(SQLException e)
        {
            Logger.fatal("SQL iznimka kod Ispisa ra\u010Duna osn. osiguranje pri citanju porezne stope.. ", e);
        }
        return ps;
    }

    private void ispisiKolicinu(Graphics g, int kolicina, int x, int y, Font f)
    {
        int broj = kolicina;
        String str = "" + kolicina;
        double sir = g.getFontMetrics(f).stringWidth(str);
        g.setFont(f);
        g.drawString(str, (int)((double)x - sir), y);
    }

    private void ispisiNovac(Graphics g, int iznos, int x, int y, Font f)
    {
        int broj = iznos / 100;
        int ostatak = iznos % 100;
        String str = "" + broj + "," + (ostatak >= 10 ? "" + ostatak : "0" + ostatak);
        double sir = g.getFontMetrics(f).stringWidth(str);
        g.setFont(f);
        g.drawString(str, (int)((double)x - sir), y);
    }

    private void iscrtajObrazac(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        int x = zaglX;
        int y = zaglY;
        int lijeviRub = x + 23;
        int sirina = 510;
        g2.setColor(boja);
        g2.setStroke(crtice);
        g2.drawRect(x, y, sirina, 146);
        g2.setFont(centuryGothicSrednjiBold);
        g2.drawString("HRVATSKI ZAVOD ZA ZDRAVSTVENO OSIGURANJE ZA\u0160TITE ZDRAVLJA NA RADU", lijeviRub, y + 27);
        g2.setFont(centuryGothicMali);
        g2.drawString("Podru\u010Dni ured: . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", lijeviRub, y + 67);
        g2.drawString("Mjesto:  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", lijeviRub, y + 86);
        g2.drawString("Ulica i broj:  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", lijeviRub, y + 118);
        g2.setFont(centuryGothicVelikiBold);
        g2.drawString("RA\u010CUN ZA ZDRAVSTVENO OSIGURANJE", x + 279, y + 56);
        g2.drawString("ZA\u0160TITE ZDRAVLJA NA RADU", x + 339, y + 71);
        g2.drawRect(x, y + 148, sirina, 595);
        ddY = y + 148;
        g2.setFont(centuryGothicSrednjiBold);
        g2.drawString("I. IZDAVANJE POMAGALA, DIJELA POMAGALA", x + 8, ddY + 10);
        g2.setFont(centuryGothicMali);
        g2.drawString("(ispunjava ugovorni isporu\u010Ditelj)", x + 210, ddY + 10);
        g2.drawString("Mati\u010Dni broj HZZOZZR:   0 2 2 7 5 0 2 3 ", x + 380, ddY + 10);
        int linijaNaziv = ddY + 64;
        int desnaKolona = (racX + 350) - 23;
        g2.setFont(centuryGothicMali);
        g2.drawString("Broj potvrde", x + 415, ddY + 25);
        nacrtajKockice(g2, desnaKolona + 13, ddY + 28, 3, 12F);
        nacrtajKockice(g2, desnaKolona + 57, ddY + 28, 9, 12F);
        g2.setFont(centuryGothicMaliItalic);
        g2.drawString("Ugovorni isporu\u010Ditelj: ", lijeviRub, ddY + 28);
        g2.drawString("(\u0161ifra) ", x + 60, ddY + 35);
        nacrtajKockice(g2, racX + 97, ddY + 26, 9);
        g2.drawString("Naziv: ", lijeviRub, linijaNaziv);
        g2.drawLine(x + 45, linijaNaziv, x + 250, linijaNaziv);
        String tipRacuna = PostavkeBean.getTipRacuna();
        g2.setFont(centuryGothicVelikiBold);
        g2.drawString(tipRacuna, desnaKolona, linijaNaziv);
        g2.setFont(centuryGothicMaliItalic);
        g2.drawString("Adresa (mjesto, ulica i broj) ", lijeviRub, linijaNaziv + 37);
        g2.drawLine(x + 110, linijaNaziv + 37, x + 320, linijaNaziv + 37);
        g2.drawString("Broj \u017Eiro-ra\u010Duna: ", lijeviRub, linijaNaziv + 73);
        g2.drawLine(x + 74, linijaNaziv + 73, x + 230, linijaNaziv + 73);
        g2.drawString("Broj ra\u010Duna", desnaKolona, linijaNaziv + 65);
        g2.drawString("isporu\u010Ditelja", desnaKolona, linijaNaziv + 73);
        g2.setColor(boja);
        g2.drawLine(racX + 380, linijaNaziv + 73, desnaKolona + 150, linijaNaziv + 73);
        g2.setColor(boja);
        g2.drawString("Poziv na broj: ", lijeviRub, linijaNaziv + 113);
        nacrtajOkvir(g2, lijeviRub + 43, linijaNaziv + 101, 14);
        nacrtajOkvir(g2, lijeviRub + 60, linijaNaziv + 101, 115);
        g2.drawString("Mati\u010Dni broj", desnaKolona, linijaNaziv + 105);
        g2.drawString("isporu\u010Ditelja:", desnaKolona, linijaNaziv + 112);
        String mbIsporucitelja = p.getTvrtkaOIB();
        if(mbIsporucitelja != null && mbIsporucitelja.length() <= 8)
            nacrtajKockice(g2, desnaKolona + 40, linijaNaziv + 101, 8);
        else
        if(mbIsporucitelja != null && mbIsporucitelja.length() > 8)
            nacrtajOkvir(g2, desnaKolona + 40, linijaNaziv + 101, 115);
        int visinaRetka = 21;
        int redaka = 8;
        int visina1 = visinaRetka * redaka;
        int visina2 = visina1 + visinaRetka;
        int pocetakY = ddY + 190;
        lijeviRub -= 17;
        g2.drawLine(lijeviRub, pocetakY, lijeviRub, pocetakY + visina1);
        g2.drawLine(lijeviRub + 28, pocetakY, lijeviRub + 28, pocetakY + visina1);
        g2.drawLine(lijeviRub + 124, pocetakY, lijeviRub + 124, pocetakY + visina1);
        g2.drawLine(lijeviRub + 226, pocetakY, lijeviRub + 226, pocetakY + visina1);
        g2.drawLine(lijeviRub + 325, pocetakY, lijeviRub + 325, pocetakY + visina2);
        g2.drawLine(lijeviRub + 363, pocetakY, lijeviRub + 363, pocetakY + visina2);
        g2.drawLine(lijeviRub + 415, pocetakY, lijeviRub + 415, pocetakY + visina2);
        g2.drawLine(lijeviRub + 497, pocetakY, lijeviRub + 497, pocetakY + visina2);
        g2.setFont(centuryGothicMali);
        for(int i = 0; i < redaka + 1; i++)
        {
            g2.drawLine(lijeviRub, pocetakY + visinaRetka * i, lijeviRub + 497, pocetakY + visinaRetka * i);
            if(i > 0 && i < 8)
            {
                nacrtajKockice(g2, lijeviRub + 127, pocetakY + visinaRetka * i + 4, 7);
                nacrtajKockice(g2, lijeviRub + 229, pocetakY + visinaRetka * i + 4, 7);
                g2.drawString("" + i + ".", lijeviRub + 7, pocetakY + visinaRetka * i + 12);
                continue;
            }
            if(i == 8)
                g2.drawLine(lijeviRub + 325, pocetakY + visinaRetka * (i + 1), lijeviRub + 497, pocetakY + visinaRetka * (i + 1));
        }

        g2.setFont(centuryGothicMaliItalic);
        g2.drawString("Redni", lijeviRub + 2, pocetakY + 10);
        g2.drawString("broj", lijeviRub + 4, pocetakY + 15);
        g2.drawString("Naziv pomagala", lijeviRub + 45, pocetakY + 13);
        g2.drawString("\u0160ifra pomagala", lijeviRub + 145, pocetakY + 13);
        g2.drawString("\u0160ifra proizvo\u0111a\u010Da", lijeviRub + 250, pocetakY + 13);
        g2.drawString("koli\u010Dina", lijeviRub + 334, pocetakY + 13);
        g2.drawString("Jedini\u010Dna cijena", lijeviRub + 364, pocetakY + 10);
        g2.drawString("u kn (s PDV-om)", lijeviRub + 366, pocetakY + 15);
        g2.drawString("Iznos u kn", lijeviRub + 437, pocetakY + 10);
        g2.drawString("(s PDV-om)", lijeviRub + 436, pocetakY + 15);
        g2.drawString("Ukupno", lijeviRub + 331, ((pocetakY + visinaRetka * 9) - visinaRetka / 2) + 2);
        int marginaObracun = lijeviRub + 234;
        int crtaStart = marginaObracun + 156;
        int pocetakObrY = (int)((((float)pocetakY + (float)(visinaRetka * 11)) - 6F) * 1.0F);
        visinaRetka = 17;
        g2.setFont(centuryGothicMaliItalic);
        g2.drawString("1. Iznos za pomagala ", marginaObracun, pocetakObrY + visinaRetka * 0);
        g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 0, crtaStart + 95, pocetakObrY + visinaRetka * 0);
        g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 0);
        g2.drawString("2. Iznos za postupke (u ljekarni)", marginaObracun, pocetakObrY + visinaRetka * 1);
        g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 1, crtaStart + 95, pocetakObrY + visinaRetka * 1);
        g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 1);
        g2.drawString("3. Ukupan iznos na teret HZZOZZR-a", marginaObracun, pocetakObrY + visinaRetka * 2);
        g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 2, crtaStart + 95, pocetakObrY + visinaRetka * 2);
        g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 2);
        g2.drawString("4. Iznos obra\u010Dunatog PDV-a u to\u010Dki 3.", marginaObracun, pocetakObrY + visinaRetka * 3);
        g2.drawLine(crtaStart, pocetakObrY + visinaRetka * 3, crtaStart + 95, pocetakObrY + visinaRetka * 3);
        g2.drawString("kn,", crtaStart + 97, pocetakObrY + visinaRetka * 3);
        g2.drawString("Datum narud\u017Ebe ", lijeviRub + 11, pocetakObrY + visinaRetka * 4 + 9);
        g2.drawLine(lijeviRub + 54 + 11, pocetakObrY + visinaRetka * 4 + 9, lijeviRub + 108 + 11, pocetakObrY + visinaRetka * 4 + 9);
        g2.drawString("/20", lijeviRub + 109 + 11, pocetakObrY + visinaRetka * 4 + 9);
        g2.drawLine(lijeviRub + 121 + 11, pocetakObrY + visinaRetka * 4 + 9, lijeviRub + 131 + 11, pocetakObrY + visinaRetka * 4 + 9);
        g2.drawString("g.", lijeviRub + 133 + 11, pocetakObrY + visinaRetka * 4 + 9);
        int razinaMjestoDatum = pocetakObrY + visinaRetka * 6 + 15;
        g2.drawString("Mjesto i datum ", lijeviRub, razinaMjestoDatum);
        g2.drawLine(lijeviRub + 44, razinaMjestoDatum, lijeviRub + 141, razinaMjestoDatum);
        g2.drawString(", ", lijeviRub + 144, razinaMjestoDatum);
        g2.drawLine(lijeviRub + 147, razinaMjestoDatum, lijeviRub + 175, razinaMjestoDatum);
        g2.drawString("20", lijeviRub + 176, razinaMjestoDatum);
        g2.drawLine(lijeviRub + 184, razinaMjestoDatum, lijeviRub + 194, razinaMjestoDatum);
        g2.drawString("g.", lijeviRub + 195, razinaMjestoDatum);
        int donjaRazina = pocetakObrY + visinaRetka * 9 + 1;
        g2.setFont(centuryGothicMali);
        g2.drawLine(lijeviRub, donjaRazina, lijeviRub + 176, donjaRazina);
        g2.drawString("Potpis osigurane osobe:", lijeviRub + 55, donjaRazina + 7);
        g2.drawString("M.P.", marginaObracun + 25, donjaRazina);
        g2.drawLine(crtaStart - 37, donjaRazina, crtaStart + 99, donjaRazina);
        g2.drawString("Ime, prezime i potpis odgovorne osobe", crtaStart - 28, donjaRazina + 7);
        g2.setFont(centuryGothicMali);
        g2.drawString("HZZO-Direkcija, Zagreb", x + 17, y + 205 + 455 + 3 + 6 + 83);
        g2.drawString("RacOsZO V1, 19/12/06", x + 17, y + 205 + 455 + 3 + 12 + 83);
        g2.drawString("* - zaokru\u017Eiti odgovaraju\u0107e", x + 105, y + 205 + 455 + 3 + 6 + 83);
        g2.setFont(centuryGothicExtraMali);
        g2.drawString("sunce-mikrosustavi.hr", x + 470, y + 205 + 455 + 4 + 83);
    }

    private void nacrtajKockice(Graphics2D g, int x, int y, int brojKockica)
    {
        nacrtajKockice(g, x, y, brojKockica, 13.5F);
    }

    private void nacrtajKockice(Graphics2D g, int x, int y, int brojKockica, float sirKoc)
    {
        float visKoc = 13.5F;
        g.setStroke(crtice);
        int sirina = (int)((float)brojKockica * sirKoc);
        g.drawRect(x, y, sirina, (int)visKoc);
        for(int i = 0; i < brojKockica; i++)
            g.drawLine((int)((float)x + (float)i * sirKoc), y, (int)((float)x + (float)i * sirKoc), (int)((float)y + visKoc));

    }

    private void nacrtajOkvir(Graphics2D g, int x, int y, int sirina)
    {
        float visKoc = 13.5F;
        g.setStroke(crtice);
        g.drawRect(x, y, sirina, (int)visKoc);
    }

    protected PageFormat pFormat;
    protected PrinterJob pJob;
    private Font fontPodnozje;
    private int visinaPodnozja;
    public static double A4_SIRINA_PX = 595.27560000000005D;
    public static double A4_VISINA_PX = 841.88980000000004D;
    PostavkeBean p;
    Color boja;
    int zaglX;
    int zaglY;
    int racX;
    int racY;
    int napX;
    int napY;
    Font glavni;
    Font mono;
    Font mali;
    Font nazivPomagalaMaliFont;
    Font centuryGothicVeliki;
    Font centuryGothicVelikiBold;
    Font centuryGothicSrednji;
    Font centuryGothicSrednjiBold;
    Font centuryGothicMali;
    Font centuryGothicExtraMali;
    Font centuryGothicMaliItalic;
    float dash[] = {
        2.0F, 1.0F
    };
    BasicStroke crtice;
    int pocetakStraniceOdskokX;
    int pocetakStraniceOdskokY;
    float omjerSir;
    float omjerDuz;
    int ddY;
    RacunVO racun;
    static int brojKopija = 1;

}
