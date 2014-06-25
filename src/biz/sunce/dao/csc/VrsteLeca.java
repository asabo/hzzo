 
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.VrsteLecaDAO;
import biz.sunce.opticar.vo.OsobineLeceVO;
import biz.sunce.opticar.vo.TipLeceVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
 
/**
 * datum:2005.06.21
 * @author asabo
 *
 */
public final class VrsteLeca implements VrsteLecaDAO
{
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica="TIPOVI_LECA";

	public void insert(Object objekt) throws SQLException
	{
		TipLeceVO ul=(TipLeceVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Insert vrste leca, ulazna vrijednost je null!");
				
			
			String upit =
								" INSERT INTO "+tablica
							+ "		(sifra,"						
							+	"		 naziv,"			//1
							+	"		 napomena,"		//2
							+	"		 podvrstaOd"	//3
							+ " ) VALUES (";
							upit += DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica,"sifra")+",";																									
							upit+=	"?,?,?)";	//3 komada (4 sa sifrom)
						
			Connection conn 			= null;
			PreparedStatement ps 	= null;
			ResultSet 		 rstemp = null;
			int sifra=DAO.NEPOSTOJECA_SIFRA; // sifra unesene naocale
   		
			try 
			 {
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				
				ps.setString(1,ul.getNaziv());
				ps.setString(2,ul.getNapomena());
				
				if (ul.getPodvrstaOd()==null)
				ps.setNull(3,Types.INTEGER);
				else
				ps.setInt(3,ul.getPodvrstaOd().getSifra().intValue());
 
				int kom=ps.executeUpdate();
				
				if (kom==1)
				{				
				String upit_sifra="select max(sifra) from "+tablica;
				
				rstemp=DAOFactory.performQuery(upit_sifra);
								
				if (rstemp!=null && rstemp.next())
				sifra=rstemp.getInt(1);
				else sifra=DAO.NEPOSTOJECA_SIFRA;
				
				try{if(rstemp!=null) rstemp.close(); rstemp=null;}catch(SQLException sqle){}
								
				if (sifra==DAO.NEPOSTOJECA_SIFRA)
				{
					Logger.fatal("ne mozemo ocitati zadnju sifru insertirane vrste leca!",null);
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
		TipLeceVO ul=(TipLeceVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");

		//iako se u principu ne bi trebalo moci nikako dogoditi, just in case...
		if (ul.getSifra()==null || ul.getSifra().intValue()==DAO.NEPOSTOJECA_SIFRA) 
			throw new SQLException("Update "+tablica+", ulazna vrijednost sifre nije ispravna! sifra: "+ul.getSifra());
			
			String upit =
								" update "+tablica+" set "
							+	"		     naziv=?,"		//1
							+	"		  napomena=?,"		//2
							+	"		podvrstaOd=?,"		//3
							+	"  where sifra=?"; 		// primary key ...
						
			Connection conn 			= null;
			PreparedStatement ps 	= null;
   		
			try 
			{
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				
				ps.setString(1,ul.getNaziv());
				ps.setString(2,ul.getNapomena());
				
				if (ul.getPodvrstaOd()==null)
				ps.setNull(3,Types.INTEGER);
				else
				ps.setInt(3,ul.getSifra().intValue());
				
				ps.setInt(4,ul.getSifra().intValue());
					 
				int kom=ps.executeUpdate();
					
				if (kom==0)
				 Logger.log("VrsteLeca.update nije uspio promjeniti podatke u retku?!?",null);
						
				return kom==1; // jeli ispravnim updateanjem se smatra promjena samo jednog retka u tablici
				} 
				//28.06.05. -asabo- NEMA CATCH-anja! - sve ide pozivatelju...  
				finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}	
				DAOFactory.freeConnection(conn);
				return false;
			}//finally
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public ValueObject read(Object kljuc) throws SQLException {
		Integer sifra = null;
			if (kljuc instanceof Integer){
				sifra=(Integer)kljuc;
			}
						
			String upit=
								"SELECT " 
							+ " sifra,"
							+	" naziv,"		
							+	"napomena,"	
							+	"podvrstaOd"		
 							+ " FROM " 
							+ "	 "+tablica;

				if (sifra!=null) upit += " where sifra =  "+sifra.intValue();
				
				upit+=" order by sifra";
			
				ResultSet 				rs	=	null;
				Connection 			 con  = null;
		
						
				rs=DAOFactory.performQuery(upit);
				 
			  TipLeceVO lvo=null;
								
					try
					{										
						Integer sf=null;
					  
						if (rs.next())
						 {						 	 
							lvo=constructLeca(rs);
							OsobineLeceVO tmp=null;
						  
						}//if rs.next					   
					}
					//28.06.05. -asabo- nema CATCH-anja ...
					finally
					{
					try{if (rs!=null) rs.close();}catch(SQLException sqle){}
					if (con!=null) DAOFactory.freeConnection(con);	  						  												 					
				}
				return lvo;
	}//read

	public List findAll(Object kljuc) throws SQLException 
	{
		Integer sifra = null;
		
		List lista=new ArrayList();
		
		if (kljuc instanceof Integer)
		{
			sifra=(Integer)kljuc;
		}
		else if (kljuc instanceof TipLeceVO)
		{
			TipLeceVO tvo=(TipLeceVO)kljuc;
			sifra=tvo.getSifra();
		}
		// sve je ostalo pripremljeno za situaciju kad ce u buducnosti trebati moa pretrazivati po nekom kriteriju
		
		String upit=
							"SELECT " 
						+ "     sifra,"
						+	"     naziv,"		
						+	"  napomena,"	
						+	"podvrstaOd"		
						+ " FROM " 
						+ "	 "+tablica;

			if (sifra!=null) upit += " where podvrstaOd =  "+sifra.intValue();
			else 
			if (sifra==null) upit += " where podvrstaOd is null";
				
			upit+=" order by sifra";
			
  		ResultSet rs =	null;
										
			rs=DAOFactory.performQuery(upit);
				 
			TipLeceVO lvo=null;
								
			try
			{			
					  
				while (rs.next())
				 {						 	 
					lvo=constructLeca(rs);
					  
					lista.add(lvo);			  						  												 
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
		return Class.forName("biz.sunce.opticar.vo.TipLeceVO");
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
	public Object getValueAt(ValueObject vo, int kolonas) {
		return null;
	}
	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}
	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}
	public int getRowCount() {
		return 0;
	}
	
	private TipLeceVO constructLeca(ResultSet rs) throws SQLException 
	{
		TipLeceVO lvo=new TipLeceVO();
		
		lvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		
		lvo.setNaziv(rs.getString("naziv"));				 
		lvo.setNapomena(rs.getString("napomena"));
		
		int nadVrsta=rs.getInt("podvrstaOd");
		if (rs.wasNull()) nadVrsta=-1;
		if (nadVrsta==-1)
		lvo.setPodvrstaOd(null);
      else
		   lvo.setPodvrstaOd((TipLeceVO)this.read(Integer.valueOf(nadVrsta))); // presedan . . . rekurzivno se metoda poziva...
				
		return lvo;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}//constructLeca
	
}//klasa

