/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.PorukeSustavaDAO;
import biz.sunce.opticar.vo.PorukaSustavaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;
import biz.sunce.optika.GlavniFrame;

/**
 * datum:2006.02.23
 * @author asabo
 *
 */
public final class PorukeSustava implements PorukeSustavaDAO
{
	//da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	 private final static String tablica="SYSTEM_LOG";
	 private String[] kolone={"vrijeme","vrsta","poruka","iznimka"};

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) {
		return null;
	}

	public void insert(Object objekt) throws SQLException
	{
	String upit;
	PorukaSustavaVO ul=(PorukaSustavaVO)objekt;

	if (ul==null)
	throw new SQLException("Insert "+tablica+", ulazna vrijednost je null!");

   //TODO 23.02.06. -asabo- inace ova metoda nije namjenjena koristenju trenutno, Logger koristi svoj vlastiti
   // mehanizam, sto nije dobro, za buducu potrebu treba ga preraditi da koristi DAO...

	upit="INSERT INTO "+tablica+" "+
	"(razina,iznimka,poruka,created,created_by) "+
	" VALUES ("+
	"?,?,?,current_timestamp,"+GlavniFrame.getSifDjelatnika()+")"; //dodano 17.06.06. created_by

		Connection 	conn	= null;
		PreparedStatement ps 	= null;
		ResultSet 	 rstemp = null;

		try
		 {
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);

			ps.setInt(1,ul.getRazina());
			ps.setString(2,ul.getIznimka());
			ps.setString(3,ul.getPoruka());

			int kom=ps.executeUpdate();

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
			try {if (ps!=null) ps.close();} catch (SQLException e1){}
			try{if(rstemp!=null) rstemp.close();}catch(SQLException sqle){}
			if (conn!=null) DAOFactory.freeConnection(conn);
			}//finally

	}//insert


	//23.02.06. -asabo- kreirano ali mislim da se nece koristiti ...
	public boolean update(Object objekt) throws SQLException
	{
		 // ne moze se updateati...
		  return false;
	}//update

	//23.02.06. -asabo- a ne moze se ni pobrisati jednom upisan redak
	public void delete(Object kljuc) throws SQLException
	{
		if (kljuc!=null && kljuc instanceof String)
		{
			String klj=(String)kljuc;
			if (klj.equalsIgnoreCase("sve"))
			{
				String upit="delete from system_log";
				DAOFactory.performUpdate(upit);
			}
		}
	}//delete

	//23.02.06. -asabo- kreirano
	public ValueObject read(Object kljuc) throws SQLException
	{
		 // konkretan jedan objekt ne moze se dobiti
		 return null;
	}//read

	//08.01.06. -asabo- kreirano
	public final List findAll(Object kljuc) throws SQLException
	{
		ArrayList list=new ArrayList(10);

			String sKljuc = null;
			Integer razinaKljuc=null;

					  if (kljuc instanceof Integer){
					  razinaKljuc=(Integer)kljuc;
						}
						else
						if (kljuc instanceof String){
							sKljuc=(String)kljuc;
						}


						String upit=
											"SELECT "
										+ "   razina,"
										+ "		iznimka,"
										+ "   poruka,"
										+ "   created"
										+ " FROM "
										+ "	 "+tablica;

							if (sKljuc!=null) upit += " where poruka like '%"+sKljuc.replaceAll("\\'","")+"%'";
							else
							if(razinaKljuc!=null) upit+=" where razina="+razinaKljuc.intValue();

							upit+=" order by created desc";

							ResultSet rs	=	null;

							rs=DAOFactory.performQuery(upit);

							PorukaSustavaVO  poruka=null;

								try
								{
									if (rs!=null)
									while (rs.next())
									 {
										poruka=constructPoruka(rs);
										list.add(poruka);
									 }//while
								}
								// -asabo- nema CATCH-anja ...
								finally
								{
								try{if(rs!=null) rs.close();}catch(SQLException sqle){}
								}

		return list;
	}//findAll

	public final Class getVOClass() throws ClassNotFoundException
	{
		return Class.forName("biz.sunce.opticar.vo.PorukaSustavaVO");
	}

	public GUIEditor getGUIEditor() {
		try {
				//return (GUIEditor)Class.forName(DAO.GUI_DAO_ROOT+".TipTransakcije").newInstance();
				return null;
			}
			/*catch(InstantiationException ie)
			{
				Logger.log("InstantiationException kod povlacenja GUIEditora klase TipTransakcije",ie);
				return null;
			}
			catch(IllegalAccessException iae)
			{
				Logger.log("IllegalAccessException kod povlacenja GUIEditora klase TipTransakcije",iae);
				return null;
			}
			catch (ClassNotFoundException e) {
				Logger.log("Nema klase Predlozak?!?",e);
				return null;
			}*/
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
		try
		{
		switch(columnIndex){
		default:	return Class.forName("java.lang.String");
		}
		}
		catch(ClassNotFoundException cnfe)
		{
			Logger.fatal(tablica+" CSC -   String kao klasa ne postoji?!?",cnfe);
			return null;
		}
	}//getColumnClass

	public final Object getValueAt(ValueObject vo, int kolonas) {
		if (vo==null) return null;
		PorukaSustavaVO  poruka=(PorukaSustavaVO)vo;
	switch(kolonas){
		case 0:		Calendar c=Calendar.getInstance();
							c.setTimeInMillis(poruka.getCreated());
							return Util.convertCalendarToString(c,true);    	//vrijeme
		case 1:		return RAZINE[poruka.getRazina()]; 								//vrsta
		case 2:   return poruka.getPoruka(); 												//poruka
		case 3:   return poruka.getIznimka(); 											//iznimka
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
	private final PorukaSustavaVO constructPoruka(ResultSet rs) throws SQLException
	{
		PorukaSustavaVO ttvo=new PorukaSustavaVO();

		ttvo.setRazina(rs.getInt("razina"));
		ttvo.setPoruka(rs.getString("poruka"));
		ttvo.setIznimka(rs.getString("iznimka"));
		ttvo.setCreated(rs.getTimestamp("created").getTime());

		return ttvo;
	}//constructPoruka

}//TipoviZahtjeva
