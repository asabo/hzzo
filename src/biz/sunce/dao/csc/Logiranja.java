/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

 
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.DjelatnikDAO;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.LogiranjeDAO;
import biz.sunce.opticar.vo.DjelatnikVO;
import biz.sunce.opticar.vo.LogiranjeVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;

/**
 * datum:2006.02.23
 * @author asabo
 *
 */
public final class Logiranja implements LogiranjeDAO
{
	//da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	 private final static String tablica="logiranje";
	 private String[] kolone={"djelatnik","logiran","odlogiran"};

	 String select="select sifDjelatnika,login,logout"+
								" from "+tablica;

	private DjelatnikDAO djelatnici=null;
	
	
	private DjelatnikDAO getDjelatnici()
	{
		if (this.djelatnici==null)
		this.djelatnici=DAOFactory.getInstance().getDjelatnici();
		return this.djelatnici;
	}

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) 
	{
		 
		return null;
	}//narusavaLiObjektKonzistentnost
	

	public void insert(Object objekt) throws SQLException 
	{
	String upit;
	LogiranjeVO ul=(LogiranjeVO)objekt;
 
		
	if (ul==null) 
	throw new SQLException("Insert "+tablica+", ulazna vrijednost je null!");
  
  
	upit="INSERT INTO "+tablica+" "+																																					// INSERT da se ne zabunujemo vise
    "(sif_djelatnika,login,logout)"+
    " values (?,?,?)";
    
		Connection 			conn	= null;
		PreparedStatement ps 	= null;
 
 
		try 
		 {
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);
			Timestamp lgin=null,lgout=null;

			lgin=new Timestamp(ul.getLogin().getTimeInMillis());
			
			if (ul.getLogout()!=null)
				lgout=new Timestamp(ul.getLogout().getTimeInMillis());
	
			ps.setInt(1,ul.getSifDjelatnika().intValue());
			ps.setTimestamp(2,lgin);
			
			if (lgout!=null) ps.setTimestamp(3,lgout); else ps.setNull(3,Types.TIMESTAMP);		
	 
			int kom=ps.executeUpdate();
			//status updated ce se samo postaviti po defaultu... 
				
			if (kom==1)
			{				
			}//if kom==1
			else
			{
				Logger.fatal("neuspio insert zapisa u tablicu "+tablica,null);
				return;
			}
				
			try{if(ps!=null) ps.close(); ps=null;}catch(SQLException e){}
		  
			} 
			// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
			finally{
			try {if (ps!=null) ps.close();} catch (SQLException e1){} ps=null;
			 
			if (conn!=null) DAOFactory.freeConnection(conn);  conn=null;
			}//finally
		
	}//insert


	//23.02.06. -asabo- kreirano ali mislim da se nece koristiti ...
	public boolean update(Object objekt) throws SQLException 
	{
		String upit;
		LogiranjeVO ul=(LogiranjeVO)objekt;
 
		if (ul==null) 
		throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");
 
  
		upit="Update "+tablica+" set "+
		"logout=current_timestamp where login=? and sif_djelatnika=?";

			Connection 			conn	= null;
			PreparedStatement ps 	= null;
 
			try 
			 {
				conn=DAOFactory.getConnection();
				
				Timestamp t=new Timestamp(ul.getLogin().getTimeInMillis());
				 
				ps=conn.prepareStatement(upit);
 				ps.setTimestamp(1,t);
 				ps.setInt(2,ul.getSifDjelatnika().intValue());
				 
				int kom=ps.executeUpdate();
		    return kom==1;
				} 
				// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
				finally
				{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
			 			
				if (conn!=null) DAOFactory.freeConnection(conn);
				}//finally
	}//update
	 
	public void delete(Object kljuc) throws SQLException 
	{
		 //tako nesto ne postoji
	}//delete

	//23.02.06. -asabo- kreirano
	public ValueObject read(Object kljuc) throws SQLException 
	{
		return null;
		// ne treba trenutno.. 
	}//read

	//08.01.06. -asabo- kreirano
	public final List findAll(Object kljuc) throws SQLException 
	{
		ArrayList list=new ArrayList(30);
		 
			String upit=select;
	 
				upit+=" order by login desc";
			  			
			 ResultSet rs=null;
	   
			 rs=DAOFactory.performQuery(upit);
	   
			 try
			 {	   
			 if (rs!=null)
			 while(rs.next())
			 {
			 	list.add(constructLogiranje(rs));
			 }//while
			 
			 return list;
			 }
				finally
				{
					try{if (rs!=null && rs.getStatement()!=null) rs.getStatement().close();}catch(SQLException sqle){}
					try{if (rs!=null ) rs.close();}catch(SQLException sqle){}		  	
				}
  
	}//findAll

	public final Class getVOClass() throws ClassNotFoundException 
	{
		return Class.forName("biz.sunce.opticar.vo.RacunVO");
	}

	public GUIEditor getGUIEditor() {
		try {
				//return (GUIEditor)Class.forName(DAO.GUI_DAO_ROOT+".Racun").newInstance();
				return null;
			} 
			/* catch(InstantiationException ie)
			{
				Logger.log("InstantiationException kod povlacenja GUIEditora za podatke "+tablica,ie);
				return null;
			}		
			catch(IllegalAccessException iae)
			{
				Logger.log("IllegalAccessException kod povlacenja GUIEditora za podatke "+tablica,iae);
				return null;
			}
			catch (ClassNotFoundException e) {
				Logger.log("Nema gui klase Racun?!?",e);
				return null;
			} */
			finally{}
	}//getGUIEditor

	public String getColumnName(int rb) 
	{
		if (rb>=0 && rb<kolone.length)
		return kolone[rb];
		else return null;
	}

	public int getColumnCount() {
		return kolone.length;
	}

	public final Class getColumnClass(int columnIndex) 
	{

		switch(columnIndex){
		default:	return STRING_CLASS;
		}
	}//getColumnClass

	public final Object getValueAt(ValueObject vo, int kolonas) {
		if (vo==null) return null;
		LogiranjeVO  l=(LogiranjeVO)vo;
		//private String[] kolone={"sifra","datum","klijent","datum narudžbe","kreiran","kreirao","izmjenjen","izmjenio"};

	switch(kolonas){
		case 0: 
		DjelatnikVO dvo=null;
				try {
					dvo=(DjelatnikVO) this.getDjelatnici().read(l.getSifDjelatnika());
				} catch (SQLException e) {
					Logger.fatal("Iznimka kod CSC Logiranje DAO getValueAt() - èitanje djelatnika",e);
					return "(problem)";
				}
				return dvo!=null?dvo.getIme()+" "+dvo.getPrezime():"?!?";
		case 1: return l.getLogin()==null?"nema logina":Util.convertCalendarToString(l.getLogin(),true);
		case 2: return l.getLogout()==null?"nema logouta":Util.convertCalendarToString(l.getLogout(),true);
		
		default:	return null;
	}
	}//getValueAt

	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}

	public int getRowCount() 
	{
		int komada=0;
		try 
		{
		komada=this.findAll(null).size();
		}	
		catch (SQLException e)
		{
		komada=0;	
		}
	 return komada;
	}

	//08.01.06. -asabo- kreirano
	private final LogiranjeVO constructLogiranje(ResultSet rs) throws SQLException 
	{
		LogiranjeVO lvo=new LogiranjeVO();
		Timestamp lin,lout;
		Calendar clin=Calendar.getInstance(), clout=null;
		
		lin=rs.getTimestamp("login");
		lout=rs.getTimestamp("logout");
		
		clin.setTimeInMillis(lin.getTime());
		
		lvo.setSifDjelatnika(Integer.valueOf(rs.getInt("sif_djelatnika")));
		lvo.setLogin(clin);
		
		if (lout==null) lvo.setLogout(null);
		else 
		{
			clout=Calendar.getInstance();
			clout.setTimeInMillis(lout.getTime());
			lvo.setLogout(clout);
		}
 							
		return lvo;
	}//constructLogiranje

}//TipoviZahtjeva
