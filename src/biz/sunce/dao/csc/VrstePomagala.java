 
package biz.sunce.dao.csc;

/**
 * datum:2005.06.29
 * @author asabo
 *
 */
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.VrstePomagalaDAO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.opticar.vo.VrstaPomagalaVO;
import biz.sunce.optika.Logger;
 
/**
 * datum:2005.06.21
 * @author asabo
 *
 */
public final class VrstePomagala implements VrstePomagalaDAO
{
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica="hzzo_vrste_pomagala";

	public void insert(Object objekt) throws SQLException
	{
		VrstaPomagalaVO ul=(VrstaPomagalaVO)objekt;
		int sifra=DAO.NEPOSTOJECA_SIFRA;
		
			if (ul==null) 
				throw new SQLException("Insert "+tablica+" - ulazna vrijednost je null!");
				
			
			String upit =
								" INSERT INTO "+tablica
							+ "		(sifra,"						
							+	"		 naziv"			//1
		 					+ " ) VALUES (";
							upit += (sifra=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica,"sifra"))+",";																									
							upit+=	"?)";	
						
			Connection conn 			= null;
			PreparedStatement ps 	= null;
			ResultSet 		 rstemp = null;
	   		
			try 
			 {
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				
				ps.setString(1,ul.getNaziv());
				 
 
				int kom=ps.executeUpdate();
				
				if (kom==1)
				ul.setSifra(Integer.valueOf(sifra)); // po tome i pozivac zna da je insert uspio...
				else
				{
					Logger.fatal("neuspio insert zapisa u tablicu "+tablica,null);
					return;
				}
				
				try{if(ps!=null) ps.close();}catch(SQLException e){}
				
				} 
					// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
				finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				try{if(rstemp!=null) rstemp.close();}catch(SQLException sqle){}					
				DAOFactory.freeConnection(conn);
			}//finally
		}//insert

	public boolean update(Object objekt) throws SQLException {
		VrstaPomagalaVO ul=(VrstaPomagalaVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");

		//iako se u principu ne bi trebalo moci nikako dogoditi, just in case...
		if (ul.getSifra()==null || ul.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA) 
			throw new SQLException("Update "+tablica+", ulazna vrijednost sifre nije ispravna! sifra: "+ul.getSifra());
			
			String upit =
								" update "+tablica+" set "
							+	"		     naziv=?"		//1
							+	"  where sifra=?"; 		// primary key ...
						
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
				 Logger.log("VrstePomagala.update nije uspio promjeniti podatke u retku?!?",null);
						
				return kom==1; // jeli ispravnim updateanjem se smatra promjena samo jednog retka u tablici
				} 
				//28.06.05. -asabo- NEMA CATCH-anja! - sve ide pozivatelju...  
				finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}	
				if (conn!=null) DAOFactory.freeConnection(conn);
				return false;
			}//finally
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public VrstaPomagalaVO read(Object kljuc) throws SQLException {
		Integer sifra = null;
			if (kljuc instanceof Integer){
				sifra=(Integer)kljuc;
			}
						
			String upit=
								"SELECT " 
							+ " sifra,"
							+	" naziv"		
							+ " FROM " 
							+ "	 "+tablica;

				if (sifra!=null) upit += " where sifra =  "+sifra.intValue();
				
				upit+=" order by sifra";
			
				ResultSet 				rs	=	null;
				Connection 			 con  = null;
						
				rs=DAOFactory.performQuery(upit);
				 
			  VrstaPomagalaVO vpvo=null;
								
					try
					{										
						Integer sf=null;
					  
						if (rs.next())
						 {						 	 
							vpvo=constructVrstaPomagala(rs);
						  
						}//if rs.next					   
					}
					//28.06.05. -asabo- nema CATCH-anja ...
					finally
					{
					try{if (rs!=null) rs.close();}catch(SQLException sqle){}
					if (con!=null) DAOFactory.freeConnection(con);	  						  												 					
				}
				return vpvo;
	}//read

	public List findAll(Object kljuc) throws SQLException 
	{
		Integer sifra = null;
		
		List lista=new ArrayList(3);
		
		if (kljuc instanceof Integer)
		{
			sifra=(Integer)kljuc;
		}
		else if (kljuc instanceof VrstaPomagalaVO)
		{
			VrstaPomagalaVO vpvo=(VrstaPomagalaVO)kljuc;
			sifra=vpvo.getSifra();
		}
		// sve je ostalo pripremljeno za situaciju kad ce u buducnosti trebati moa pretrazivati po nekom kriteriju
		
		String upit=
							"SELECT " 
						+ "     sifra,"
						+	"     naziv"		
						+ " FROM " 
						+ "	 "+tablica;

			if (sifra!=null) upit += " where sifra =  "+sifra.intValue();
					
			upit+=" order by sifra";
			
  		ResultSet rs =	null;
										
			rs=DAOFactory.performQuery(upit);
				 
			VrstaPomagalaVO vpvo=null;
								
			try
			{			
					  
				while (rs.next())
				 {						 	 
					vpvo=constructVrstaPomagala(rs);
					  
					lista.add(vpvo);			  						  												 
					}//while  
			}//try
			//30.06.05. -asabo- nema CATCH-anja ...
			finally
			{
			try{if (rs!=null) rs.close();}catch(SQLException sqle){}
			}
	 
			return lista;
	}//findAll

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.VrstaPomagalaVO");
	}
	public String getColumnName(int rb) {
		return null;
	}
	public int getColumnCount() {
		return 0;
	}
	public Class getColumnClass(int columnIndex) {
		return null;
	}
	public Object getValueAt(VrstaPomagalaVO vo, int kolonas) {
		return null;
	}
	public boolean setValueAt(VrstaPomagalaVO vo, Object vrijednost, int kolona) {
		return false;
	}
	public boolean isCellEditable(VrstaPomagalaVO vo, int kolona) {
		return false;
	}
	public int getRowCount() {
		return 0;
	}
	
	private VrstaPomagalaVO constructVrstaPomagala(ResultSet rs) throws SQLException 
	{
		VrstaPomagalaVO lvo=new VrstaPomagalaVO();
		
		lvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		
		lvo.setNaziv(rs.getString("naziv"));				 
 
		return lvo;
	}//constructVrstaPomagala

	public GUIEditor getGUIEditor() {
		return null;
	}//constructLeca

	public String narusavaLiObjektKonzistentnost(VrstaPomagalaVO objekt) {
		 
		return null;
	}
	
}//klasa

