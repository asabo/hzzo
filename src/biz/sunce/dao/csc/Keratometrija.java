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
import biz.sunce.dao.KeratometrijaDAO;
import biz.sunce.opticar.vo.KeratometrijaOko;
import biz.sunce.opticar.vo.KeratometrijaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;

/**
 * datum:2005.05.23
 * @author asabo
 *
 */
public final class Keratometrija implements KeratometrijaDAO
{

	public void insert(Object objekt) throws SQLException
  {
		KeratometrijaVO ul=(KeratometrijaVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Insert keratometrija, ulazna vrijednost je null!");
			
			final String upit =
								" INSERT INTO KERATOMETRIJA "
							+	"		(ax,"					//1
							+	"		baza1,"				//2
							+	"		baza2,"				//3
							+	"		dl,"					//4
							+	"		sifpregleda,"	//5
							+	"		visus)"				//6
							+	" VALUES"
							+	" (?,?,?,?,?,?)";	//6 komada
						
			Connection conn 		= null;
			PreparedStatement ps 	= null;
   		
   		try 
   		{
			conn=DAOFactory.getConnection();
			
			if (conn==null)
			throw new SQLException ("Connection prema bazi podataka je null!");

			ps=conn.prepareStatement(upit);
			KeratometrijaOko ulaz=ul.getLijevo();

			ps.setString(1,ulaz.getAx());
			ps.setString(2,ulaz.getBaza1());
			ps.setString(3,ulaz.getBaza2());
			ps.setString(4,LIJEVO);
			ps.setInt(5,ul.getSifPregleda().intValue());//Ovo valjda ne smije biti null
			ps.setString(6,ulaz.getVisus());
	
			ps.executeUpdate();
			
			ulaz=ul.getDesno();

						ps.setString(1,ulaz.getAx());
						ps.setString(2,ulaz.getBaza1());
						ps.setString(3,ulaz.getBaza2());
						ps.setString(4,DESNO);
						ps.setInt(5,ul.getSifPregleda().intValue());//Ovo valjda ne smije biti null
						ps.setString(6,ulaz.getVisus());
						
				ps.executeUpdate();
			  } 
			  catch (SQLException e) {
				Logger.fatal("Greska kod inserta keratometrije",e);
			  }finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}	
				DAOFactory.freeConnection(conn);
			}
		}//insert

	public boolean update(Object objekt) throws SQLException {
		KeratometrijaVO ul=(KeratometrijaVO)objekt;
		
			if (ul==null) 
				throw new SQLException("Update keratometrija, ulazna vrijednost je null!");
			
			final String upit =
								" update KERATOMETRIJA set "
							+	"		ax=?,"					//1
							+	"		baza1=?,"				//2
							+	"		baza2=?,"				//3
							+	"		visus=?"				//4
							+	" where sifPregleda=? and dl=?"; // primary key je takav...
						
			Connection conn 		= null;
			PreparedStatement ps 	= null;
   		
			try 
			{
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				KeratometrijaOko ulaz=ul.getLijevo();
				boolean prolaz=false;

				if (ulaz!=null)
				{				
				ps.setString(1,ulaz.getAx());
				ps.setString(2,ulaz.getBaza1());
				ps.setString(3,ulaz.getBaza2());
		
				ps.setString(4,ulaz.getVisus());

				ps.setInt(5,ul.getSifPregleda().intValue());//Ovo valjda ne smije biti null
				ps.setString(6,LIJEVO);
			
				//moze biti da postoje podaci samo za jedno oko, pa ako prodje sve ok, prolaz je vec sada true
				prolaz=ps.executeUpdate()==1;
				}

				ulaz=ul.getDesno();
				
				if (ulaz!=null)
				{
				
				ps.setString(1,ulaz.getAx());
				ps.setString(2,ulaz.getBaza1());
				ps.setString(3,ulaz.getBaza2());
		
				ps.setString(4,ulaz.getVisus());

				ps.setInt(5,ul.getSifPregleda().intValue());//Ovo valjda ne smije biti null
				ps.setString(6,DESNO); //03.06.05. -asabo- bilo je LIJEVO
						
				 prolaz=ps.executeUpdate()==1;
				}
				return prolaz;
				} 
				catch (SQLException e) {
				Logger.fatal("Greska kod update-a keratometrije",e);
				return false;
				}
			    finally
			    {
				try {if (ps!=null) ps.close();} catch (SQLException e1){}	
				DAOFactory.freeConnection(conn);				
			    }//finally
			
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public KeratometrijaVO read(Object kljuc) throws SQLException {
		Integer sifPregleda = null;
			if (kljuc instanceof Integer){
				sifPregleda=(Integer)kljuc;
			}
			String upit=
								"SELECT " 
							+ "		sifpregleda,"
							+ "		dl,"
							+ "		baza1,"
							+ "		baza2,"
							+ "		ax,"
							+ "		visus"
							+ " FROM " 
							+ "			keratometrija ";

				if (sifPregleda!=null) upit += " where sifpregleda =  "+sifPregleda.intValue();
				
				upit+=" order by sifPregleda";
			
				ResultSet rs					=	null;
				 rs=DAOFactory.performQuery(upit);
				 
			KeratometrijaVO kvo=null;
								
				try
				{
					
					Integer sfp=null;
					while (rs.next())
						 {						
 						
							// ako nema sifre ili je sifra razlicita sifri trenutno ucitanog pregleda				
							// treba kreirati novi VO		
							if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda")))
							{
							kvo=constructKeratometrija(rs);
							sfp=kvo.getSifPregleda();
							} 
							
							 // svaki kvo ce se dva puta nadograditi sa constructom
							 // prvi puta ovaj if uvijek vrati true, drugi puta samo ako im je sifPRegleda isti
							 if (rs.getInt("sifPregleda")==sfp.intValue())
								 constructKeratometrijaOko(rs,kvo);												 
					}
				} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod Keratometrija.read",e);
				}
				finally{
					try{if (rs!=null) rs.close();}catch(SQLException sqle){}
				}
				return kvo;
	}//read

	public List<KeratometrijaVO> findAll(Object kljuc) throws SQLException {
		Integer sifPregleda = null;
		if (kljuc instanceof Integer){
			sifPregleda=(Integer)kljuc;
		}
		String upit=
							"SELECT " 
						+ "		sifpregleda,"
						+ "		dl,"
						+ "		baza1,"
						+ "		baza2,"
						+ "		ax,"
						+ "		visus"
						+ " FROM " 
						+ "			keratometrija ";

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
			
			List<KeratometrijaVO> lista= new ArrayList<KeratometrijaVO>();
								
			try{
				rs = ps.executeQuery();
				Integer sfp=null;
				KeratometrijaVO kvo=null;
				while (rs.next())
				   {						
				   	//TODO 
						//prvo provjeriti dali prethodno kreirani kvo ima istu sifru pregleda, ako nema, treba ga zapisati 
						// prvi if je prepisani doljnji if, a drugi je osigurac da se ne bi islo bezvezarije dodavati
						if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda"))) 						
 	   					if (kvo!=null)
 		   				{lista.add(kvo); sfp=null; kvo=null;}
 						
 						// ako nema sifre ili je sifra razlicita sifri trenutno ucitanog pregleda				
 						// treba kreirati novi VO		
					  if(sfp==null || (sfp.intValue()!=rs.getInt("sifPregleda")))
					  {
					  kvo=constructKeratometrija(rs);
					  sfp=kvo.getSifPregleda();
					  } 
					 	 // svaki kvo ce se dva puta nadograditi sa constructom
					 	 // prvi puta ovaj if uvijek vrati true, drugi puta samo ako im je sifPRegleda isti
					 	 if (rs.getInt("sifPregleda")==sfp.intValue())
  					 	 constructKeratometrijaOko(rs,kvo);
												 
				}
			} catch (SQLException e) {
		  Logger.fatal("SQL iznimka kod Keratometrija.findAll",e);
			}
			finally{
				try{if (rs!=null) rs.getStatement().close();}catch(SQLException e){}
				try{if (rs!=null) rs.close();}catch(SQLException sqle){}
			}
			return lista;
	}//findAll

	public Class<KeratometrijaVO> getVOClass() throws ClassNotFoundException {
		return KeratometrijaVO.class;
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
	public Object getValueAt(KeratometrijaVO vo, int kolonas) {
		return null;
	}
	public boolean setValueAt(KeratometrijaVO vo, Object vrijednost, int kolona) {
		return false;
	}
	public boolean isCellEditable(KeratometrijaVO vo, int kolona) {
		return false;
	}
	public int getRowCount() {
		return 0;
	}
	
	private KeratometrijaVO constructKeratometrija(ResultSet rs) throws SQLException 
	{
		KeratometrijaVO kvo=new KeratometrijaVO();
		
		kvo.setSifPregleda(Integer.valueOf(rs.getInt("SIFPREGLEDA")));
		return kvo;
	}
	
	
	private KeratometrijaOko constructKeratometrijaOko (ResultSet rs,KeratometrijaVO kvo)throws SQLException 
	{	
		KeratometrijaOko k=new KeratometrijaOko();

		k.setAx(rs.getString("AX"));
		k.setBaza1(rs.getString("BAZA1"));
		k.setBaza2(rs.getString("BAZA2"));
		k.setDl(rs.getString("DL"));

		k.setVisus(rs.getString("VISUS"));
		
		if (k.getDl().equals(LIJEVO))
		kvo.setLijevo(k);
		else
		kvo.setDesno(k);
		
		return k;	
	}

	public GUIEditor<KeratometrijaVO> getGUIEditor() {
		return null;
	}
}
