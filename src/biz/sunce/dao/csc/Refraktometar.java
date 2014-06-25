/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.RefraktometarDAO;
import biz.sunce.opticar.vo.RefraktometarOko;
import biz.sunce.opticar.vo.RefraktometarVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * datum:2005.05.23
 * @author asabo
 *
 */
public final class Refraktometar implements RefraktometarDAO 
{

	public void insert(Object objekt) throws SQLException {
		RefraktometarVO ul=(RefraktometarVO)objekt;

			if (ul==null) 
				throw new SQLException("Insert refraktometar, ulazna vrijednost je null!");

			String upit =
								" INSERT INTO REFRAKTOMETAR "
							+	"		(dl,"								//1
							+	"		dsph,"							//2
							+	"		dcyl,"							//3
							+	"		ax,"								//4
							+	"		sifpregleda)"				//5
							+	" VALUES"
							+	" (?,?,?,?,?)";	//5 komada
				
			Connection conn 			= null;
			PreparedStatement ps 	= null;

		try 
		{
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);
			
			RefraktometarOko ulaz=ul.getLijevo();
				ps.setString(1,LIJEVO);
				ps.setString(2,ulaz.getDsph());
				ps.setString(3,ulaz.getDcyl());
				ps.setString(4,ulaz.getAx());
				ps.setInt(5,ul.getSifPregleda().intValue());


			ps.executeUpdate();//Unosimo lijevo
	
			ulaz=ul.getDesno();
				ps.setString(1,DESNO);
				ps.setString(2,ulaz.getDsph());
				ps.setString(3,ulaz.getDcyl());
				ps.setString(4,ulaz.getAx());
				ps.setInt(5,ul.getSifPregleda().intValue());
				
			ps.executeUpdate();//Unosimo desno

		  } 
		  catch (SQLException e) {
			Logger.fatal("Greska kod inserta refaktometar",e);
		  }finally{
			try {if (ps!=null) ps.close();} catch (SQLException e1){}	
			DAOFactory.freeConnection(conn);
		}
	}//insert

	public boolean update(Object objekt) throws SQLException {
		RefraktometarVO ul=(RefraktometarVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Insert refraktometar, ulazna vrijednost je null!");
			
			String upit =
								" update REFRAKTOMETAR set "
							//+	"		dl=?,"	14.06.05 -asabo- ne treba vise, jer tvori primary key pri updateu							
							+	"		dsph=?,"							//1
							+	"		dcyl=?,"							//2
							+	"		ax=?"								//3
							//+	"		sifpregleda=?"				//primary key, ne treba...
							+	" where sifPregleda=? and dl=?"; // primary key je takav...
						
			Connection conn 			= null;
			PreparedStatement ps 	= null;
   		
			try 
			{
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				
				RefraktometarOko ulaz=ul.getLijevo();

					ps.setString(1,ulaz.getDsph());
					ps.setString(2,ulaz.getDcyl());
					ps.setString(3,ulaz.getAx());
					   ps.setInt(4,ul.getSifPregleda().intValue());
					ps.setString(5,LIJEVO);
							
				  int kom=ps.executeUpdate();
			
			  	ulaz=ul.getDesno();
					ps.setString(1,ulaz.getDsph());
					ps.setString(2,ulaz.getDcyl());
					ps.setString(3,ulaz.getAx());
						 ps.setInt(4,ul.getSifPregleda().intValue());
					ps.setString(5,DESNO);
					
				  kom+=ps.executeUpdate();
				  
				 return kom==2;
				} 
				catch (SQLException e) {
				Logger.fatal("Greska kod update-a refraktometra",e);
				}finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}	
				DAOFactory.freeConnection(conn);
				return false;
			}//finally
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public ValueObject read(Object kljuc) throws SQLException {
	
		Integer sifPregleda = null;
		if (kljuc instanceof Integer){
			sifPregleda=(Integer)kljuc;
		}

		String upit=
						"SELECT " 
					+ "		dl,"
					+ "		sifpregleda,"
					+ "		dsph,"
					+ "		dcyl," // 14.06.05. -asabo- OPET zarez fali joooj ... 
					+ "		ax"
					+ " FROM " 
					+ "			refraktometar ";

		if (sifPregleda!=null) upit += " where sifpregleda =  "+sifPregleda.intValue();
			
		upit+=" order by sifPregleda";
		
		ResultSet rs					=	null;
		rs=DAOFactory.performQuery(upit);

		RefraktometarVO rvo=null;
		try
		{
			Integer sfp=null;
			while (rs.next()) {						
				if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda"))){
					rvo=constructRefraktometar(rs);
					sfp=rvo.getSifPregleda();
				} 
				 if (rs.getInt("sifPregleda")==sfp.intValue()){
					 constructRefraktometarOko(rs,rvo);
				 }												 
			}
		} catch (SQLException e) {
		Logger.fatal("SQL iznimka kod refraktometar.read",e);
		}
		finally{
			try{if (rs!=null) rs.close();}catch(SQLException sqle){}
		}
		return rvo;
	}//read

	public List findAll(Object kljuc) throws SQLException {
		
		System.err.println("SABO nije implementirano");
		return null;
	}

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
	
	private RefraktometarVO constructRefraktometar(ResultSet rs) throws SQLException {
		RefraktometarVO rvo=new RefraktometarVO();
		
		rvo.setSifPregleda(Integer.valueOf(rs.getInt("SIFPREGLEDA")));
		return rvo;
	}

	private RefraktometarOko constructRefraktometarOko (ResultSet rs,RefraktometarVO rvo)throws SQLException{	
		RefraktometarOko r=new RefraktometarOko();

		r.setDl(rs.getString("DL"));
		r.setDsph(rs.getString("DSPH"));
		r.setDcyl(rs.getString("DCYL"));
		r.setAx(rs.getString("AX"));
		if (r.getDl().equals(LIJEVO))
		rvo.setLijevo(r);
		else
		rvo.setDesno(r);
		return r;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}
}
