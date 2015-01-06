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
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.PredlosciDAO;
import biz.sunce.opticar.vo.PredlozakVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;

/**
 * datum:2005.12.27
 * @author asabo
 *
 */
public final class Predlosci implements PredlosciDAO 
{
	//da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	 private final static String tablica="PREDLOSCI";
	 private String[] kolone={"sifra","tip podataka","naziv"};

	public String narusavaLiObjektKonzistentnost(PredlozakVO objekt) {
		return null;
	}

	public void insert(PredlozakVO objekt) throws SQLException 
	{
	String upit;
	PredlozakVO ul=(PredlozakVO)objekt;
		
	if (ul==null) 
	throw new SQLException("Insert "+tablica+", ulazna vrijednost je null!");

	int sifra=DAO.NEPOSTOJECA_SIFRA; // sifra unesenog retka
  
	upit="INSERT INTO "+tablica+" "+
	"(SIFRA,TEKST,CREATED_BY,UPDATED_BY,CREATED,UPDATED,"+
	" NAZIV,TIP_PODATAKA) "+
	" VALUES ("+
	(sifra=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica))+
	",?,?,null,current_timestamp,null,?,?)";

		Connection 			conn	= null;
		PreparedStatement ps 	= null;
		ResultSet 		 rstemp = null;
		
		if (sifra==DAO.NEPOSTOJECA_SIFRA)
		{
		Logger.fatal("Neprirodna situacija; pri insertu tablice "+tablica+" sifra koja se treba insertirati je "+sifra,null);
		return;
		}  		
		
		try 
		 {
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);
			
			ps.setString(1,ul.getTekst());
			ps.setInt(2,ul.getCreatedBy().intValue());
			ps.setString(3,ul.getNaziv());
			ps.setString(4,ul.getTipPodataka());
				
			int kom=ps.executeUpdate();
				
			if (kom==1)
			{				
	 		
	 		if (sifra==DAO.NEPOSTOJECA_SIFRA)
			{
				Logger.fatal("ne mozemo ocitati zadnju sifru insertiranja zapisa u tablicu "+tablica,null);
				return;
			}
			else
			ul.setSifra(Integer.valueOf(sifra)); // po tome i pozivac zna da je insert uspio...
				
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
			DAOFactory.freeConnection(conn);
		  }//finally
		
	}//insert


	//07.01.06. -asabo- kreirano
	public boolean update(PredlozakVO objekt) throws SQLException 
	{
		PredlozakVO ul=(PredlozakVO)objekt;
		
					if (ul==null) 
						throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");
	 
					String upit =
										" update "+tablica+" set "
									+	"		  tekst=?,"				//1
									+	"		  naziv=?,"				//2
									+	"		tip_podataka=?,"	//3
									+ " updated=current_timestamp,"
									+ " updated_by="+GlavniFrame.getSifDjelatnika()
									+	" where sifra=?"; // primary key ...
						
					Connection conn 			= null;
					PreparedStatement ps 	= null;
   		
					try 
					{
						conn=DAOFactory.getConnection();

						ps=conn.prepareStatement(upit);
				
					  ps.setString(1,ul.getTekst());
					  ps.setString(2,ul.getNaziv());
					  ps.setString(3,ul.getTipPodataka());
					  ps.setInt(4,ul.getSifra().intValue());
				 
						int kom=ps.executeUpdate();
					
						if (kom==0)
						 Logger.log(tablica+" update nije uspio promjeniti podatke u retku?!?",null);
				
					  return kom==1;
						} 
						//  -asabo- NEMA CATCH-anja! - sve ide pozivatelju...  
						finally{
						try {if (ps!=null) ps.close();} catch (SQLException e1){}	
						DAOFactory.freeConnection(conn);
						
					}//finally
	}//update

	public void delete(Object kljuc) throws SQLException 
	{
		PredlozakVO ul=(PredlozakVO)kljuc;
		
							if (ul==null) 
								throw new SQLException("delete "+tablica+", ulazna vrijednost je null!");
								
								int sifra=ul.getSifra()!=null?ul.getSifra().intValue():DAO.NEPOSTOJECA_SIFRA;
	 
	 						// jedna od rijetkih situacija, gdje fizicki brisemo zapis iz db
							String upit =
							" delete from "+tablica+" where sifra="+sifra;
												   		
	  						try 
   							{
								int kom=0;
								
								if (sifra!=DAO.NEPOSTOJECA_SIFRA)
								kom=DAOFactory.performUpdate(upit);
						
								if (kom==0)
								 Logger.log(tablica+" delete nije uspio izbrisati redak?!? sifra:"+sifra,null);
	 					 
								} 
								//  -asabo- NEMA CATCH-anja! - sve ide pozivatelju...  
								finally{						
								}//finally		
	}//delete

	//08.01.06. -asabo- kreirano
	public PredlozakVO read(Object kljuc) throws SQLException 
	{
		Integer sifra = null;
				if (kljuc instanceof Integer){
					sifra=(Integer)kljuc;
				}
			
			
				String upit=
									"SELECT " 
								+ "   sifra,"
								+ "		naziv,"
								+ "		tekst,"
								+ "		tip_podataka,"
								+ "   created,"
								+ "		updated,"
								+ "		created_by,"
								+ "   updated_by,"
								+ "   status"
								+ " FROM " 
								+ "	 "+tablica;

					if (sifra!=null) upit += " where sifra =  "+sifra.intValue();
				
					upit+=" order by sifra";
			
					ResultSet rs					=	null;
 	 		 
		  		rs=DAOFactory.performQuery(upit);
				 
					PredlozakVO  pvo=null;
								
						try
						{			
							if (rs.next())
							 {						 	 
								pvo=constructPredlozak(rs);
							 }//if	
							 else pvo=null;				   
						}
						// -asabo- nema CATCH-anja ...
						finally
						{
						try{if (rs!=null) rs.close();}catch(SQLException sqle){}
					  }
					return pvo;
	}//read

	//08.01.06. -asabo- kreirano
	public List findAll(Object kljuc) throws SQLException 
	{
		ArrayList list=new ArrayList(10);
		
		  String sKljuc = null;
		
						if (kljuc instanceof String){
							sKljuc=(String)kljuc;
						}
			
			
						String upit=
											"SELECT " 
										+ "   sifra,"
										+ "		naziv,"
										+ "		tekst,"
										+ "		tip_podataka,"
										+ "   created,"
										+ "		updated,"
										+ "		created_by,"
										+ "   updated_by,"
										+ "   status"
										+ " FROM " 
										+ "	 "+tablica;

							if (sKljuc!=null) upit += " where naziv like '%"+sKljuc+"%'"+
																				"    or naziv like '"+sKljuc+"%'"+
																				"	   or naziv like '%"+sKljuc+"'";
				
							upit+=" order by sifra";
			
							ResultSet rs	=	null;
 	 		 
							rs=DAOFactory.performQuery(upit);
				 
							PredlozakVO pvo=null;
								
								try
								{			
									if (rs!=null)
									while (rs.next())
									 {						 	 
										pvo=constructPredlozak(rs);
										list.add(pvo);
									 }//while									  		   
								}
								// -asabo- nema CATCH-anja ...
								finally
								{
								try{if(rs!=null) rs.close();}catch(SQLException sqle){}
								}
 
		return list;
	}//findAll

	public Class getVOClass() throws ClassNotFoundException 
	{
		return Class.forName("biz.sunce.opticar.vo.PredlozakVO");
	}

	public GUIEditor getGUIEditor() {
		try {
				return (GUIEditor)Class.forName(DAO.GUI_DAO_ROOT+".Predlozak").newInstance();
			} 
			catch(InstantiationException ie)
			{
				Logger.log("InstantiationException kod povlacenja GUIEditora klase Predlozak",ie);
				return null;
			}		
			catch(IllegalAccessException iae)
			{
				Logger.log("IllegalAccessException kod povlacenja GUIEditora klase Predlozak",iae);
				return null;
			}
			catch (ClassNotFoundException e) {
				Logger.log("Nema klase Predlozak?!?",e);
				return null;
			}
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

	public Class getColumnClass(int columnIndex) 
	{
		try
		{
		switch(columnIndex){
		case 0:		return INTEGER_CLASS;
		case 1:		return STRING_CLASS;
		case 2:		return STRING_CLASS;
		default:	return null;		
	  }
		}
		catch(Exception cnfe)
		{
			Logger.fatal(tablica+" CSC - Integer ili String kao klase ne postoje?!?",cnfe);
			return null;
		}
	}//getColumnClass

	public Object getValueAt(PredlozakVO vo, int kolonas) {
		if (vo==null) return null;
	PredlozakVO  pvo=(PredlozakVO)vo;
	switch(kolonas){
		case 0:		return pvo.getSifra();  
		case 1:		return pvo.getTipPodataka();
		case 2:		return pvo.getNaziv();
		default:	return null;
	}
	}//getValueAt

	public boolean setValueAt(PredlozakVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(PredlozakVO vo, int kolona) {
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
	private PredlozakVO constructPredlozak(ResultSet rs) throws SQLException 
	{
		PredlozakVO pvo=new PredlozakVO();
		
 
		pvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		pvo.setNaziv(rs.getString("naziv"));
		pvo.setTekst(rs.getString("tekst"));
		pvo.setTipPodataka(rs.getString("tip_podataka"));

		pvo.setCreated(rs.getTimestamp("created").getTime());
		Timestamp t=rs.getTimestamp("updated");
		pvo.setLastUpdated(t!=null?t.getTime():0L);

		pvo.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));
		pvo.setLastUpdatedBy(Integer.valueOf(rs.getInt("updated_by")));
		if (rs.wasNull()) pvo.setLastUpdatedBy(null);		
		
		pvo.setStatus(rs.getString("status").charAt(0));	
		
		return pvo;
	}//constructPredlozak

}//Predlosci
