// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.03.2009 22:40:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PregledVO.java

package biz.sunce.opticar.vo;

import biz.sunce.dao.*;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject, LijecnikVO, KlijentVO, KeratometrijaVO, 
//            SkiaskopijaVO, NaocaleVO, LeceVO, RefraktometarVO

public final class PregledVO extends ValueObject
{

    public PregledVO()
    {
        lijecnik = null;
        keratometrija = null;
        skiaskopija = null;
        rekfraktometar = null;
        klijent = null;
        ordNaocaleBlizu = null;
        ordNaocaleDaleko = null;
        prijeOrdNaocaleBlizu = null;
        prijeOrdNaocaleDaleko = null;
        prijeOrdLeceVO = null;
        ordLeceVO = null;
        setSifra(Integer.valueOf(-1));
    }

    public LijecnikVO getLijecnik()
    {
        if(lijecnik == null)
        {
            try
            {
                lijecnik = (LijecnikVO)DAOFactory.getInstance().getLijecnici().read(getSifLijecnika());
                return lijecnik;
            }
            catch(SQLException sqle)
            {
                Logger.fatal("SQL iznimka kod PregledVO.getLijecnik", sqle);
            }
            return null;
        } else
        {
            return lijecnik;
        }
    }

    public KlijentVO getKlijent()
    {
        if(klijent == null)
        {
            try
            {
                klijent = (KlijentVO)DAOFactory.getInstance().getKlijenti().read(getSifKlijenta());
                return klijent;
            }
            catch(SQLException sqle)
            {
                Logger.fatal("SQL iznimka kod PregledVO.getKlijent", sqle);
            }
            return null;
        } else
        {
            return klijent;
        }
    }

    public void setLijecnik(LijecnikVO lijecnik)
    {
        this.lijecnik = lijecnik;
        sifLijecnika = lijecnik.getSifra();
    }

    public KeratometrijaVO getKeratometrija()
    {
        if(keratometrija == null)
        {
            try
            {
                keratometrija = (KeratometrijaVO)DAOFactory.getInstance().getKeratometrija().read(getSifra());
                return keratometrija;
            }
            catch(SQLException sqle)
            {
                Logger.fatal("SQL iznimka kod PregledVO.getKeratometrija", sqle);
            }
            return null;
        } else
        {
            return keratometrija;
        }
    }

    public SkiaskopijaVO getSkiaskopija()
    {
        if(skiaskopija == null)
        {
            try
            {
                skiaskopija = (SkiaskopijaVO)DAOFactory.getInstance().getSkiaskopija().read(getSifra());
                return skiaskopija;
            }
            catch(SQLException sqle)
            {
                Logger.fatal("SQL iznimka kod PregledVO.getSkiaskopija", sqle);
            }
            return null;
        } else
        {
            return skiaskopija;
        }
    }

    public LeceVO getOrdLeceVO()
    {
        Integer ob = getOrdLece();
        if(ob != null)
            return getLece(ob);
        else
            return null;
    }

    public LeceVO getPrijeOrdLeceVO()
    {
        Integer ob = getPrijeOrdLece();
        if(ob != null)
            return getLece(ob);
        else
            return null;
    }

    public NaocaleVO getOrdNaocaleBlizuVO()
    {
        Integer ob = getOrdBlizu();
        if(ob != null)
            return getNaocale(ob);
        else
            return null;
    }

    public NaocaleVO getOrdNaocaleDalekoVO()
    {
        Integer ob = getOrdDaleko();
        if(ob != null)
            return getNaocale(ob);
        else
            return null;
    }

    public NaocaleVO getPrijeOrdNaocaleBlizuVO()
    {
        Integer ob = getPrijeOrdBlizu();
        if(ob != null)
            return getNaocale(ob);
        else
            return null;
    }

    public NaocaleVO getPrijeOrdNaocaleDalekoVO()
    {
        Integer ob = getPrijeOrdDaleko();
        if(ob != null)
            return getNaocale(ob);
        else
            return null;
    }

    private NaocaleVO getNaocale(Integer sifNaocala)
    {
        try
        {
            return (NaocaleVO)DAOFactory.getInstance().getNaocale().read(sifNaocala);
        }
        catch(SQLException sqle)
        {
            Logger.fatal("SQL iznimka kod PregledVO.getNaocale. Id:" + (sifNaocala == null ? -1 : sifNaocala.intValue()), sqle);
        }
        return null;
    }

    private LeceVO getLece(Integer sifLece)
    {
        try
        {
            return (LeceVO)DAOFactory.getInstance().getLece().read(sifLece);
        }
        catch(SQLException sqle)
        {
            Logger.fatal("SQL iznimka kod PregledVO.getLece. Id:" + (sifLece == null ? -1 : sifLece.intValue()), sqle);
        }
        return null;
    }

    public RefraktometarVO getRefraktometar()
    {
        if(rekfraktometar == null)
        {
            try
            {
                rekfraktometar = (RefraktometarVO)DAOFactory.getInstance().getRefraktometar().read(getSifra());
                return rekfraktometar;
            }
            catch(SQLException sqle)
            {
                Logger.fatal("SQL iznimka kod PregledVO.getRefraktometar", sqle);
            }
            return null;
        } else
        {
            return rekfraktometar;
        }
    }

    public String getAnamneza()
    {
        return anamneza;
    }

    public Calendar getDatVrijeme()
    {
        return datVrijeme;
    }

    public Integer getKontrolaZaMjeseci()
    {
        return kontrolaZaMjeseci;
    }

    public String getNapomena()
    {
        return napomena;
    }

    public Timestamp getObavljen()
    {
        return obavljen;
    }

    public Integer getOrdBlizu()
    {
        return ordBlizu;
    }

    public Integer getOrdDaleko()
    {
        return ordDaleko;
    }

    public Integer getOrdLece()
    {
        return ordLece;
    }

    public String getOrdNapomena()
    {
        return ordNapomena;
    }

    public String getOrdRazZjD()
    {
        return ordRazZjD;
    }

    public String getOrdRazZjL()
    {
        return ordRazZjL;
    }

    public String getOrdVOD()
    {
        return ordVOD;
    }

    public String getOrdVOS()
    {
        return ordVOS;
    }

    public Integer getPrijeOrdBlizu()
    {
        return prijeOrdBlizu;
    }

    public Integer getPrijeOrdDaleko()
    {
        return prijeOrdDaleko;
    }

    public Integer getPrijeOrdLece()
    {
        return prijeOrdLece;
    }

    public Integer getSifLijecnika()
    {
        return sifLijecnika;
    }

    public String getVisusL()
    {
        return visusL;
    }

    public String getVisusScL()
    {
        return visusScL;
    }

    public void setAnamneza(String anamneza)
    {
        this.anamneza = anamneza;
    }

    public void setDatVrijeme(Calendar datVrijeme)
    {
        this.datVrijeme = datVrijeme;
    }

    public void setKontrolaZaMjeseci(Integer kontrolaZaMjeseci)
    {
        this.kontrolaZaMjeseci = kontrolaZaMjeseci;
    }

    public void setNapomena(String napomena)
    {
        this.napomena = napomena;
    }

    public void setObavljen(Timestamp obavljen)
    {
        this.obavljen = obavljen;
    }

    public void setOrdBlizu(Integer ordBlizu)
    {
        this.ordBlizu = ordBlizu;
    }

    public void setOrdDaleko(Integer ordDaleko)
    {
        this.ordDaleko = ordDaleko;
    }

    public void setOrdLece(Integer ordLece)
    {
        this.ordLece = ordLece;
    }

    public void setOrdNapomena(String ordNapomena)
    {
        this.ordNapomena = ordNapomena;
    }

    public void setOrdRazZjD(String ordRazZjD)
    {
        this.ordRazZjD = ordRazZjD;
    }

    public void setOrdRazZjL(String ordRazZjL)
    {
        this.ordRazZjL = ordRazZjL;
    }

    public void setOrdVOD(String ordVOD)
    {
        this.ordVOD = ordVOD;
    }

    public void setOrdVOS(String ordVOS)
    {
        this.ordVOS = ordVOS;
    }

    public void setPrijeOrdBlizu(Integer prijeOrdBlizu)
    {
        this.prijeOrdBlizu = prijeOrdBlizu;
    }

    public void setPrijeOrdDaleko(Integer prijeOrdDaleko)
    {
        this.prijeOrdDaleko = prijeOrdDaleko;
    }

    public void setPrijeOrdLece(Integer prijeOrdLece)
    {
        this.prijeOrdLece = prijeOrdLece;
    }

    public void setSifLijecnika(Integer sifLijecnika)
    {
        this.sifLijecnika = sifLijecnika;
    }

    public void setVisusL(String visus)
    {
        visusL = visus;
    }

    public void setVisusScL(String visusSc)
    {
        visusScL = visusSc;
    }

    public Integer getSifKlijenta()
    {
        return sifKlijenta;
    }

    public void setSifKlijenta(Integer sifKlijenta)
    {
        this.sifKlijenta = sifKlijenta;
    }

    public String getVisusD()
    {
        return visusD;
    }

    public String getVisusScD()
    {
        return visusScD;
    }

    public void setVisusD(String string)
    {
        visusD = string;
    }

    public void setVisusScD(String string)
    {
        visusScD = string;
    }

    public void setKeratometrija(KeratometrijaVO keratometrijaVO)
    {
        keratometrija = keratometrijaVO;
    }

    public void setRekfraktometar(RefraktometarVO refraktometarVO)
    {
        rekfraktometar = refraktometarVO;
    }

    public void setSkiaskopija(SkiaskopijaVO skiaskopijaVO)
    {
        skiaskopija = skiaskopijaVO;
    }

    @Override
	public String toString()
    {
        return getKlijent().toString() + "\n" + Util.convertCalendarToString(getDatVrijeme());
    }

    public NaocaleVO getSuncaneNaocale()
    {
        return getNaocale(suncaneNaocale);
    }

    public Integer getSifSuncanihNaocala()
    {
        return suncaneNaocale;
    }

    public void setSifSuncanihNaocala(Integer sifra)
    {
        suncaneNaocale = sifra;
    }

    private Calendar datVrijeme;
    private String napomena;
    private Timestamp obavljen;
    private Integer kontrolaZaMjeseci;
    private String anamneza;
    private String ordVOD;
    private String ordVOS;
    private String ordRazZjL;
    private String ordRazZjD;
    private String ordNapomena;
    private String visusScL;
    private String visusL;
    private String visusScD;
    private String visusD;
    private Integer sifLijecnika;
    private Integer prijeOrdBlizu;
    private Integer prijeOrdDaleko;
    private Integer prijeOrdLece;
    private Integer ordBlizu;
    private Integer ordDaleko;
    private Integer ordLece;
    private Integer sifKlijenta;
    private Integer suncaneNaocale;
    private LijecnikVO lijecnik;
    private KeratometrijaVO keratometrija;
    private SkiaskopijaVO skiaskopija;
    private RefraktometarVO rekfraktometar;
    private KlijentVO klijent;
    private NaocaleVO ordNaocaleBlizu;
    private NaocaleVO ordNaocaleDaleko;
    private NaocaleVO prijeOrdNaocaleBlizu;
    private NaocaleVO prijeOrdNaocaleDaleko;
    private LeceVO prijeOrdLeceVO;
    private LeceVO ordLeceVO;
}