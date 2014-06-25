

package biz.sunce.opticar.vo;

import java.util.Calendar;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public class HzzoObracunVO extends ValueObject
{

    public HzzoObracunVO()
    {
    }

    public Calendar getDatum()
    {
        return datum;
    }

    public void setDatum(Calendar calendar)
    {
        datum = calendar;
    }

    public Integer getSifPodruznice()
    {
        return sifPodruznice;
    }

    public Integer getSifOsiguranja()
    {
        return sifOsiguranja;
    }

    public void setSifPodruznice(Integer integer)
    {
        sifPodruznice = integer;
    }

    public void setSifOsiguranja(Integer sifOsiguranja)
    {
        this.sifOsiguranja = sifOsiguranja;
    }

    private Calendar datum;
    private Integer sifPodruznice;
    private Integer sifOsiguranja;
}