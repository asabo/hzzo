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
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.VrsteRezervnihDijelovaDAO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.opticar.vo.VrstaRezervnogDijelaVO;
import biz.sunce.optika.Logger;

/**
 * datum:2006.01.31
 * @author asabo
 *
 */
 
public final class VrsteRezervnihDijelova implements VrsteRezervnihDijelovaDAO  
{
	//da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	 private final static String tablica="VRSTE_REZERVNIH_DIJELOVA";
	 private String[] kolone={"sifra","naziv"};

	public String narusavaLiObjektKonzistentnost(ValueObject objekt) {
		return null;
	}

	public void insert(Object objekt) throws SQLException 
	{
	String upit;
	VrstaRezervnogDijelaVO  ul=(VrstaRezervnogDijelaVO )objekt;
		
	if (ul==null) 
	throw new SQLException("Insert "+tablica+", ulazna vrijednost je null!");

	int sifra=DAO.NEPOSTOJECA_SIFRA; // sifra unesenog retka
  
	upit="INSERT INTO "+tablica+" "+
	"(SIFRA,naziv ) "+
	" VALUES ("+
	(sifra=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica,"sifra"))+
	",?)";

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
			
			ps.setString(1,ul.getNaziv());
			 
				
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
	public boolean update(Object objekt) throws SQLException 
	{
		VrstaRezervnogDijelaVO  ul=(VrstaRezervnogDijelaVO )objekt;
		
					if (ul==null) 
						throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");
	 
					String upit =
										" update "+tablica+" set "
									+	"		  naziv=?,"				//1
									+	" where sifra=?"; // primary key ...
						
					Connection conn 			= null;
					PreparedStatement ps 	= null;
   		
					try 
					{
						conn=DAOFactory.getConnection();

						ps=conn.prepareStatement(upit);
				
						ps.setString(1,ul.getNaziv());
						ps.setInt(2,ul.getSifra().intValue());
				 
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
		VrstaRezervnogDijelaVO  ul=(VrstaRezervnogDijelaVO )kljuc;
		
		     if (true ) return; //ostaje kod, ali se ne smije pokretati, situacija nemoguca...
		
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
	public ValueObject read(Object kljuc) throws SQLException 
	{
		Integer sifra = null;
				if (kljuc instanceof Integer){
					sifra=(Integer)kljuc;
				}
			
			
				String upit=
									"SELECT " 
								+ "   sifra,"
								+ "		naziv"
								+ " FROM " 
								+ "	 "+tablica;

					if (sifra!=null) upit += " where sifra =  "+sifra.intValue();
				
					upit+=" order by sifra";
			
					ResultSet rs					=	null;
 	 		 
					rs=DAOFactory.performQuery(upit);
				 
					VrstaRezervnogDijelaVO   rezDio=null;
								
						try
						{			
							if (rs.next())
							 {						 	 
								rezDio=constructRezervniDio(rs);
							 }//if	
							 else rezDio=null;				   
						}
						// -asabo- nema CATCH-anja ...
						finally
						{
						try{if (rs!=null) rs.close();}catch(SQLException sqle){}
						}
					return rezDio;
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
										+ "		naziv"
										+ " FROM " 
										+ "	 "+tablica;

							if (sKljuc!=null) upit += " where naziv like '%"+sKljuc+"%'"+
																				"    or naziv like '"+sKljuc+"%'"+
																				"	   or naziv like '%"+sKljuc+"'";
				
							upit+=" order by sifra";
			
							ResultSet rs	=	null;
 	 		 
							rs=DAOFactory.performQuery(upit);
				 
							VrstaRezervnogDijelaVO  rezDio=null;
								
								try
								{			
									if (rs!=null)
									while (rs.next())
									 {						 	 
										rezDio=constructRezervniDio(rs);
										list.add(rezDio);
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
		return Class.forName("biz.sunce.opticar.vo.VrstaRezervnogDijelaVO");
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

	public Class getColumnClass(int columnIndex) 
	{
		try
		{
		switch(columnIndex){
		case 0:		return Class.forName("java.lang.Integer");  
		case 1:		return Class.forName("java.lang.String");
		default:	return null;		
		}
		}
		catch(ClassNotFoundException cnfe)
		{
			Logger.fatal(tablica+" CSC - Integer ili String kao klase ne postoje?!?",cnfe);
			return null;
		}
	}//getColumnClass

	public Object getValueAt(ValueObject vo, int kolonas) {
		if (vo==null) return null;
		VrstaRezervnogDijelaVO   rezDio=(VrstaRezervnogDijelaVO )vo;
		switch(kolonas){
		case 0:		return rezDio.getSifra();  
		case 1:		return rezDio.getNaziv();
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
	private VrstaRezervnogDijelaVO  constructRezervniDio(ResultSet rs) throws SQLException 
	{
		VrstaRezervnogDijelaVO  rezDio=new VrstaRezervnogDijelaVO ();
 
		rezDio.setSifra(Integer.valueOf(rs.getInt("sifra")));
		rezDio.setNaziv(rs.getString("naziv"));
 
		return rezDio;
	}//constructPredlozak

}//VrsteRezervnihDijelova
