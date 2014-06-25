package biz.sunce.opticar.vo;

import java.util.Hashtable;

public class ValueObject
{

    public ValueObject()
    {
        sifra = null;
        modified = false;
        id = null;
    }

    public ValueObject(String kolone[], int tipoviKolona[])
    {
        sifra = null;
        modified = false;
        id = null;
        this.kolone = kolone;
        this.tipoviKolona = tipoviKolona;
    }

    public final void setValue(String kljuc, Object vrijednost)
        throws Exception
    {
        if(podaci == null)
            podaci = new Hashtable(kolone == null ? 0 : kolone.length);
        if(kljuc == null)
            throw new Exception("Kljuc je null vrijednost?!?");
        if(vrijednost != null)
            podaci.put(kljuc, vrijednost);
        else
            podaci.remove(kljuc);
    }

    public final void setValue(int redniBroj, Object vrijednost)
        throws Exception
    {
        if(kolone != null && kolone.length > redniBroj)
            setValue(kolone[redniBroj], vrijednost);
    }

    public final Object getValue(String kljuc)
    {
        if(podaci != null && kljuc != null)
            return podaci.get(kljuc);
        else
            return null;
    }

    public final Object getValue(int redniBroj)
    {
        if(kolone != null && kolone.length > redniBroj)
            return getValue(kolone[redniBroj]);
        else
            return null;
    }

    public final long getCreated()
    {
        return created;
    }

    public final Integer getCreatedBy()
    {
        return createdBy;
    }

    public final long getLastUpdated()
    {
        return lastUpdated;
    }

    public final Integer getLastUpdatedBy()
    {
        return lastUpdatedBy;
    }

    public char getStatus()
    {
        return status;
    }

    public void setCreated(long calendar)
    {
        created = calendar;
    }

    public void setCreatedBy(Integer integer)
    {
        createdBy = integer;
    }

    public void setLastUpdated(long calendar)
    {
        lastUpdated = calendar;
    }

    public void setLastUpdatedBy(Integer integer)
    {
        lastUpdatedBy = integer;
    }

    public void setStatus(char c)
    {
        status = c;
    }

    @Override
	public String toString()
    {
        String s = "";
        if(kolone != null)
        {
            int v = kolone.length;
            for(int i = 0; i < v; i++)
            {
                String ki = kolone[i];
                if(!ki.equalsIgnoreCase("CREATED") && !ki.equalsIgnoreCase("CREATED_BY") && !ki.equalsIgnoreCase("UPDATED") && !ki.equalsIgnoreCase("UPDATED_BY") && !ki.equalsIgnoreCase("STATUS"))
                {
                    Object p = getValue(kolone[i]);
                    s = s + p + " ";
                }
            }

        }
        return s;
    }

    public boolean isModified()
    {
        return modified;
    }

    public void setModified(boolean b)
    {
        modified = b;
    }

    public Integer getSifra()
    {
        return sifra;
    }

    public Object getId()
    {
        return id;
    }

    public void setSifra(Integer integer)
    {
        sifra = integer;
        id = sifra;
    }

    public void setId(Object id)
    {
        this.id = id;
    }

    public String[] getKolone()
    {
        return kolone;
    }

    public int[] getTipoviKolona()
    {
        return tipoviKolona;
    }

    public void setKolone(String strings[])
    {
        kolone = strings;
    }

    public void setTipoviKolona(int is[])
    {
        tipoviKolona = is;
    }

    private char status;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    private long created;
    private long lastUpdated;
    private Integer sifra;
    public static String TRUE_STRING = "D";
    public static String FALSE_STRING = "N";
    private String kolone[];
    private int tipoviKolona[];
    private Hashtable podaci;
    private boolean modified;
    private Object id;

}