/*
 * Project opticari
 *
 */
package biz.sunce.optika;

import java.sql.SQLException;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.PorukeSustavaDAO;
import biz.sunce.opticar.vo.PorukaSustavaVO;

/**
 * datum:2005.04.30
 * @author dstanic
 *
 */
public final class Logger {
  private static  PorukeSustavaDAO dao=null;


	public static boolean log(String poruka)
	{return log(poruka,null);}

	public static boolean debug(String poruka)
	{return debug(poruka,null);}

	public static boolean log(String poruka,Throwable iznimka){
		return log(poruka,iznimka, PorukeSustavaDAO.INFO);
	}
	public static boolean warn(String poruka,Throwable iznimka){
		return log(poruka,iznimka, PorukeSustavaDAO.WARNING);
	}
	public static boolean debug(String poruka,Throwable iznimka){
		return log(poruka,iznimka, PorukeSustavaDAO.DEBUG);
	}
	public static boolean fatal(String poruka,Throwable iznimka){
		return log(poruka,iznimka, PorukeSustavaDAO.FATAL);
	}
	private static final PorukeSustavaDAO getDAO()
	{
		if (dao==null)
		{
			dao=DAOFactory.getInstance().getPorukeSustava();
		}
		return dao;
	}//getDAO

	// 30.04.05 - asabo - kostur metode
	public static final boolean log(String poruka,Throwable iznimka, int level)
	{
		if (iznimka!=null)
		iznimka.printStackTrace();	// TODO nek se ispisuje Privremeno cemo ostavit ovako
		boolean rez=true;

		System.out.println("LOG: "+(poruka!=null?poruka:"-")+":"+(iznimka!=null?iznimka.toString():""));

		String originalnaPoruka=null;

		try {

			originalnaPoruka=poruka;
			//dodano skracenje poruke kako bi se izbjegao eventualni exception u jdbc driverima (csc ima problem sa time)
			if (poruka!=null && poruka.length()>255) poruka=poruka.substring(0,255);

  			PorukaSustavaVO porukaVO=new PorukaSustavaVO();

                          porukaVO.setRazina(level);
                          porukaVO.setPoruka(poruka);
                          porukaVO.setIznimka(iznimka!=null?iznimka.toString():null);

                          getDAO().insert(porukaVO);
                          porukaVO=null;
                      }
                      catch (SQLException e)
                      {
			System.err.println("Iznimka kod logiranja greske. Poruka: "+originalnaPoruka);
			e.printStackTrace();
                      }
                      catch(Exception e)
                      {
                          System.err.println("Opæa iznimka kod Logger.log()"+e);
                          e.printStackTrace(); //sustav ne smije pasti pri logiranju, mora nastaviti dalje..
                      }
                      finally
                      {
                      }
                      return rez;
	}//log
}//klasa
