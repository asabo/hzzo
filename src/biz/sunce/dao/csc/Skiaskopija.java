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

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.SkiaskopijaDAO;
import biz.sunce.opticar.vo.SkiaskopijaOko;
import biz.sunce.opticar.vo.SkiaskopijaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * datum:2005.05.23
 * @author dstanic
 *
 */
public final class Skiaskopija implements SkiaskopijaDAO 
{

	public void insert(SkiaskopijaVO objekt) throws SQLException {
		SkiaskopijaVO ul=(SkiaskopijaVO)objekt;

			if (ul==null) 
				throw new SQLException("Insert skiaskopija, ulazna vrijednost je null!");

			String upit =
								" INSERT INTO SKIASKOPIJA "
							+	"		(dl,"								//1
							+	"		sifpregleda,"				//2
							+	"		visus_sc,"					//3
							+	"		visus,"							//4
							+	"		dsph,"							//5
							+	"		dcyl,"							//6
							+	"		ax)"								//7
							+	" VALUES"
							+	" (?,?,?,?,?,?,?)";	//7 komada
				
			Connection conn 			= null;
			PreparedStatement ps 	= null;

		try 
		{
			conn=DAOFactory.getConnection();
			
			if (conn==null)
			throw new SQLException ("Connection prema bazi podataka je null!!");

			ps=conn.prepareStatement(upit);
			
			SkiaskopijaOko ulaz=ul.getLijevo();
			
				//03.06.05 -asabo- moramo provjeriti dali je null, tj. dali ima podataka za to oko
			  if (ulaz!=null)
			  {			  
				ps.setString(1,LIJEVO);// Desno Lijevo oko
				ps.setInt(2,ul.getSifPregleda().intValue());
				ps.setString(3,ulaz.getVisus_sc());
				ps.setString(4,ulaz.getVisus());
				ps.setString(5,ulaz.getDsph());
				ps.setString(6,ulaz.getDcyl());
				ps.setString(7,ulaz.getAx());
						  
				ps.executeUpdate();//Unosimo lijevo
			  }//if
	
  			ulaz=ul.getDesno();
			//03.06.05 -asabo- moramo provjeriti dali je null, tj. dali ima podataka za to oko
			if (ulaz!=null)
			{			    			
				ps.setString(1,DESNO);// Desno Lijevo oko
				ps.setInt(2,ul.getSifPregleda().intValue());
				ps.setString(3,ulaz.getVisus_sc());
				ps.setString(4,ulaz.getVisus());
				ps.setString(5,ulaz.getDsph());
				ps.setString(6,ulaz.getDcyl());
				ps.setString(7,ulaz.getAx());
				
			ps.executeUpdate();//Unosimo desno
			}//if

		  } 
		  catch (SQLException e) {
			Logger.fatal("Greska kod inserta skiaskopije",e);
		  }finally{
			try {if (ps!=null) ps.close();} catch (SQLException e1){}	
			DAOFactory.freeConnection(conn);
		}
	}//insert

	public boolean update(SkiaskopijaVO objekt) throws SQLException {
		SkiaskopijaVO ul=(SkiaskopijaVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Update skiaskopija, ulazna vrijednost je null!");
			
			String upit =
								" update SKIASKOPIJA set "
							//+	"		dl=?,"								//1
							//+	"		sifpregleda=?,"				//2
							+	"		visus_sc=?,"					//1
							+	"		visus=?,"							//2
							+	"		dsph=?,"							//3
							+	"		dcyl=?,"							//4
							+	"		ax=?"									//5
							+	" where sifPregleda=? and dl=?"; // primary key je takav...
						
			Connection conn 			= null;
			PreparedStatement ps 	= null;
   		
			try 
			{
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				SkiaskopijaOko ulaz=ul.getLijevo();
				boolean prolaz=false;
				
				  if (ulaz!=null) // moze biti i da nema podataka pa treba paziti
				  {				
					ps.setString(1,ulaz.getVisus_sc());
					ps.setString(2,ulaz.getVisus());
					ps.setString(3,ulaz.getDsph());
					ps.setString(4,ulaz.getDcyl());
					ps.setString(5,ulaz.getAx());

					ps.setInt(6,ul.getSifPregleda().intValue());
					ps.setString(7,LIJEVO);// Desno Lijevo oko
								
				  prolaz=ps.executeUpdate()==1;
				  }//if
				
				 ulaz=ul.getDesno();
				
				 if (ulaz!=null)
				 {				 
					ps.setString(1,ulaz.getVisus_sc());
					ps.setString(2,ulaz.getVisus());
					ps.setString(3,ulaz.getDsph());
					ps.setString(4,ulaz.getDcyl());
					ps.setString(5,ulaz.getAx());

					ps.setInt		(6,ul.getSifPregleda().intValue());
					ps.setString(7,DESNO);// Desno Lijevo oko

					prolaz=ps.executeUpdate()==1;
				 }
				 		
				 return prolaz;
				} 
				catch (SQLException e) {
				Logger.fatal("Greska kod update-a skiaskopije",e);
				}finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}	
				DAOFactory.freeConnection(conn);
				return false;
			}//finally
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public SkiaskopijaVO read(Object kljuc) throws SQLException {
		Integer sifPregleda = null;
			if (kljuc instanceof Integer){
				sifPregleda=(Integer)kljuc;
			}

			String upit=
							"SELECT " 
						+ "		dl,"
						+ "		sifpregleda,"
						+ "		visus_sc,"
						+ "		visus,"
						+ "		dsph,"
						+ "		dcyl," // 03.06.05 -asabo- dodao zarez
						+ "		ax"
						+ " FROM " 
						+ "			skiaskopija ";

			if (sifPregleda!=null) upit += " where sifpregleda =  "+sifPregleda.intValue();
			
			upit+=" order by sifPregleda";
		
			ResultSet rs					=	null;
			rs=DAOFactory.performQuery(upit);

			SkiaskopijaVO svo=null;
			try
			{
				Integer sfp=null;
				while (rs.next()) 
				{						
					if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda"))){
						svo=constructSkiaskopija(rs);
						sfp=svo.getSifPregleda();
					} 
					 if (rs.getInt("sifPregleda")==sfp.intValue()){
						 constructSkiaskopijaOko(rs,svo);
					 }												 
				}
			} catch (SQLException e) {
			Logger.fatal("SQL iznimka kod Skiaskopija.read",e);
			}
			finally{
				try{if (rs!=null) rs.close();}catch(SQLException sqle){}
			}
			return svo;
	}//read

	public List findAll(Object kljuc) throws SQLException {
		Integer sifPregleda = null;
		if (kljuc instanceof Integer){
			sifPregleda=(Integer)kljuc;
		}
		String upit=
							"SELECT " 
						+ "		dl,"
						+ "		sifpregleda,"
						+ "		visus_sc,"
						+ "		visus,"
						+ "		dsph,"
						+ "		dcyl"
						+ "		ax"
						+ " FROM " 
						+ "			skiaskopija ";

			if (sifPregleda!=null) upit += " AND sifpregleda = ?";
			
			//24.05.05. -asabo- addon radi findAll(!null)
			upit+=" order by sifpregleda,dl";

			Connection conn 			= null;
			PreparedStatement ps 	= null;
			ResultSet rs					=	null;
			conn=DAOFactory.getConnection();
			ps=conn.prepareStatement(upit);
			
			if (sifPregleda!=null){
				ps.setInt(1,sifPregleda.intValue());
			}
			
			List lista= new ArrayList();
								
			try{
				rs = ps.executeQuery();
				Integer sfp=null;
				SkiaskopijaVO svo=null;
				while (rs.next())
				   {						

						if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda"))) 						
						if (svo!=null)
						{lista.add(svo); sfp=null; svo=null;}

					  if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda")))
					  {
						svo=constructSkiaskopija(rs);
					  sfp=svo.getSifPregleda();
					  } 
						 if (rs.getInt("sifPregleda")==sfp.intValue())
						 constructSkiaskopijaOko(rs,svo);
												 
				}
			} catch (SQLException e) {
		  Logger.fatal("SQL iznimka kod Skiaskopija.findAll",e);
			}
			finally{
				try{if (rs!=null) rs.getStatement().close();}catch(SQLException e){}
				try{if (rs!=null) rs.close();}catch(SQLException sqle){}
			}
			return lista;
	}//findAll

	public Class getVOClass() throws ClassNotFoundException {
		return null;
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

	public Object getValueAt(SkiaskopijaVO vo, int kolonas) {
		return null;
	}

	public boolean setValueAt(SkiaskopijaVO vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(SkiaskopijaVO vo, int kolona) {
		return false;
	}

	public int getRowCount() {
		return 0;
	}
	private SkiaskopijaVO constructSkiaskopija(ResultSet rs) throws SQLException {
		SkiaskopijaVO svo=new SkiaskopijaVO();
		
		svo.setSifPregleda(Integer.valueOf(rs.getInt("SIFPREGLEDA")));
		return svo;
	}
	
	private SkiaskopijaOko constructSkiaskopijaOko (ResultSet rs,SkiaskopijaVO svo)throws SQLException
	{	
		SkiaskopijaOko s=new SkiaskopijaOko();
		
		s.setAx(rs.getString("AX"));
		s.setVisus(rs.getString("VISUS"));
		s.setVisus_sc(rs.getString("VISUS_SC"));//26.07.2005 Sabi i stanic skuzili gresku :)
		s.setDsph(rs.getString("DSPH"));
		s.setDcyl(rs.getString("DCYL"));
		s.setDl(rs.getString("DL"));
		
		if (s.getDl().equals(LIJEVO))
		svo.setLijevo(s);
		else
		svo.setDesno(s);

		return s;
	}

	public GUIEditor<SkiaskopijaVO> getGUIEditor() {
		return null;
	}//constructSkiaskopijaOko
}
