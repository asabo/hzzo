// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:53
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TransakcijaVO.java

package biz.sunce.opticar.vo;

import java.io.*;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class TransakcijaVO extends ValueObject
{

    public TransakcijaVO()
    {
    }

    public String getArtikl()
    {
        return artikl;
    }

    public long getDatVrijeme()
    {
        return datVrijeme;
    }

    public int getID()
    {
        return ID;
    }

    public String getNapomena()
    {
        return napomena;
    }

    public boolean isOdlazna()
    {
        return odlazna;
    }

    public byte[] getSadrzaj()
    {
        return sadrzaj;
    }

    public int getSifBoje()
    {
        return sifBoje;
    }

    public int getSifKlijenta()
    {
        return sifKlijenta;
    }

    public short getTip()
    {
        return tip;
    }

    public String getTipZahtjeva()
    {
        return ZAHTJEVI[tip];
    }

    public void setArtikl(String string)
    {
        artikl = string;
    }

    public void setDatVrijeme(long l)
    {
        datVrijeme = l;
    }

    public void setID(int i)
    {
        ID = i;
    }

    public void setNapomena(String string)
    {
        napomena = string;
    }

    public void setOdlazna(boolean b)
    {
        odlazna = b;
    }

    public void setSadrzaj(byte bs[])
    {
        sadrzaj = bs;
    }

    public void setSadrzaj(Object obj)
        throws Exception
    {
        ByteArrayOutputStream baos=null;
        ObjectOutputStream oout=null;
        try{
        baos = new ByteArrayOutputStream();
        oout = null;
        oout = new ObjectOutputStream(baos);
        oout.writeObject(obj);
        oout.flush();
        baos.flush();
        baos.close();
        setSadrzaj(baos.toByteArray());
        baos = null;
        }
        finally{
        try{if(baos != null)baos.close();}catch(IOException ioe) { }
        try{if(oout != null)oout.close();}catch(IOException ioe) { }
        }
    }

    public void setSifBoje(int i)
    {
        sifBoje = i;
    }

    public void setSifKlijenta(int i)
    {
        sifKlijenta = i;
    }

    public void setTip(short s)
    {
        tip = s;
    }

    public long getOdobrena()
    {
        return odobrena;
    }

    public void setOdobrena(long l)
    {
        odobrena = l;
    }

    public String getBrojKartice()
    {
        return brojKartice;
    }

    public void setBrojKartice(String string)
    {
        brojKartice = string;
    }

    public short getVrsta()
    {
        return vrsta;
    }

    public void setVrsta(short s)
    {
        vrsta = s;
    }

    public static final short TIP_TRANSAKCIJE_ZAHTJEV_ZA_REZERVNIM_DIJELOVIMA = 1;
    public static final short TIP_TRANSAKCIJE_ZAHTJEV_ZA_KARTICOM = 2;
    public static final short TIP_TRANSAKCIJE_ZAHTJEV_ZA_INTERVENCIJOM = 3;
    public static final String ZAHTJEVI[] = {
        "", "zahtjev za izdavanjem rezervnog dijela", "zahtjev za izdavanjem \u010Dlanske iskaznice", "zahtjev za intervencijom u sustavu"
    };
    int ID;
    short tip;
    int sifKlijenta;
    long datVrijeme;
    int sifBoje;
    String artikl;
    boolean odlazna;
    byte sadrzaj[];
    String napomena;
    long odobrena;
    String brojKartice;
    short vrsta;

}