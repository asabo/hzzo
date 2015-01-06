
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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.LeceDAO;
import biz.sunce.opticar.vo.LeceVO;
import biz.sunce.opticar.vo.OsobineLeceVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;

/**
 * datum:2005.06.21
 * @author asabo
 *
 */
public final class Lece implements  LeceDAO
{
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica="IZDANE_LECE";

	public void insert(LeceVO objekt) throws SQLException
	{
		LeceVO ul=(LeceVO)objekt;

			if (ul==null)
				throw new SQLException("Insert lece, ulazna vrijednost je null!");

				int sifLece=-1;

                                String upit =           " INSERT INTO "+tablica
							+ "		(id,"
							+	"		 datum,"				//1
							+	"		 vrstaLece,"		//2
							+	"		 podvrstaLece,"	//3
							+	"		 napomena,"			//4
							+	"		 model,"				//5
							+ "    sif_proizvodjaca," // 6 -asabo- dodano 20.02.06.
							+ "    created,"		  //
							+ "    updated,"			//
							+ "    created_by,"	  //
							+ "    updated_by"   	//
							+ " ) VALUES (";
							upit+=(sifLece=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica,"id"))+",";

							upit+=	"?,?,?,?,?,?,current_timestamp,null,?,null)";	//9 komada

			Connection 			conn	= null;
			PreparedStatement ps 	= null;
			ResultSet 		 rstemp = null;
			int sifra=DAO.NEPOSTOJECA_SIFRA; // sifra unesene lece

			try
			 {
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				OsobineLeceVO ulaz=ul.getLijeva();

				if (ul.getDatum()!=null)
				ps.setTimestamp(1,new Timestamp(ul.getDatum().getTimeInMillis()));
				else
				ps.setNull(1,Types.TIMESTAMP);

				if (ul.getSifVrste()!=null && ul.getSifVrste().intValue()!=DAO.NEPOSTOJECA_SIFRA)
				ps.setInt(2,ul.getSifVrste().intValue()); // vrsta lece
				else
				ps.setNull(2,Types.INTEGER);

				if (ul.getSifPodvrste()!=null && ul.getSifPodvrste().intValue()!=DAO.NEPOSTOJECA_SIFRA)
				ps.setInt(3,ul.getSifPodvrste().intValue()); // podvrsta lece
				else
				ps.setNull(3,Types.INTEGER);

				ps.setString(4,ul.getNapomena()); // napomena


				if (ul.getSifProizvodjaca()==null)
				{
				ps.setString(5,ul.getModel()); // model u tom slucaju moze biti nesto natrljano bez veze..
				ps.setNull(6,Types.INTEGER);
				}
				else
				{
					ps.setNull(5,Types.VARCHAR); // model u ovom slucaju se NE MOZE napisati
					ps.setInt(6,ul.getSifProizvodjaca().intValue());
				}

				ps.setInt(7,ul.getCreatedBy().intValue());

				int kom=ps.executeUpdate();

				if (kom==1)
				{
					sifra=sifLece;

				if (sifra==DAO.NEPOSTOJECA_SIFRA)
				{
					Logger.fatal("ne mozemo ocitati zadnju sifru insertirane lece!",null);
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

				ulaz=ul.getDesna();


				String tablica2="DIOPTRIJA_LECA";



				upit =
							" INSERT INTO "+tablica2
							+ "(dl,"					//1
							+"bazKriv,"		//2
							+"dijametar,"  //3
							+ "    dsph,"				//4
							+ "    ax,"		  		//5
							+ "    specIzrada,"	//6
							+ "    boja,"	  		//7
							+ "    dcyl,"       //8 -asabo- dodano 14.02.06.
							+ "      id," 				//9
							+ "   created," // 13.12.06. -asabo- dodano
							+ "   created_by"
							+ " ) values(?,?,?,?,?,?,?,?,?,current_timestamp,"+GlavniFrame.getSifDjelatnika()+")";	//9 komada

				ps=conn.prepareStatement(upit);

				// unos desnog stakla
				ps.setString(1,DAO.DESNO);
				ps.setString(2,ulaz.getBaznaKrivina());
				ps.setString(3,ulaz.getDijametar());
				ps.setString(4,ulaz.getDsph());
				ps.setString(5,ulaz.getAx());
				ps.setString(6,ulaz.getSpecIzrada());
				ps.setString(7,ulaz.getBoja());
				ps.setString(8,ulaz.getDcyl());
				ps.setInt(9,sifra);

				kom=ps.executeUpdate();

				if (kom!=1)
				Logger.fatal("Dioptrija leca nije uspjesno insertirana (desna)! ",null);
				else
				{   // unos lijevog stakla
					ulaz=ul.getLijeva();

					ps.setString(1,DAO.LIJEVO);
					ps.setString(2,ulaz.getBaznaKrivina());
					ps.setString(3,ulaz.getDijametar());
					ps.setString(4,ulaz.getDsph());
					ps.setString(5,ulaz.getAx());
					ps.setString(6,ulaz.getSpecIzrada());
					ps.setString(7,ulaz.getBoja());
					ps.setString(8,ulaz.getDcyl());
					ps.setInt(9,sifra);

					kom=ps.executeUpdate();

					if (kom!=1)
					Logger.fatal("Dioptrija leca nije uspjesno insertirana (lijeva)!",null);
				}//else
				}
					// nema catch-anja SQL exceptiona... neka se pozivatelj iznad jebe ...
				finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				try{if(rstemp!=null) rstemp.close();}catch(SQLException sqle){}
				DAOFactory.freeConnection(conn);
			}
		}//insert

	public boolean update(LeceVO objekt) throws SQLException
	{
		LeceVO ul=(LeceVO)objekt;

			if (ul==null)
				throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");


			String upit =
								" update "+tablica+" set "
							+	"     		  datum=?,"			//1
							+	" 		  vrstaLece=?,"			//2
							+	" 	 podvrstaLece=?,"			//3
							+ "        napomena=?,"			//4
							+ "           model=?,"			//5
							+ "sif_proizvodjaca=?,"      //6 -asabo- dodano 20.02.06.
							+ "         updated=current_timestamp,"
							+ "         updated_by="+GlavniFrame.getSifDjelatnika()
							+	" where id=?"; // primary key ...

			Connection conn 			= null;
			PreparedStatement ps 	= null;

			try
			{
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);

				ps.setTimestamp(1,new Timestamp(ul.getDatum().getTimeInMillis()));

				if (ul.getSifVrste()!=null && ul.getSifVrste().intValue()!=DAO.NEPOSTOJECA_SIFRA)
        ps.setInt(2,ul.getSifVrste().intValue());
        else
        ps.setNull(2,Types.INTEGER);

				if (ul.getSifPodvrste()!=null && ul.getSifPodvrste().intValue()!=DAO.NEPOSTOJECA_SIFRA)
				ps.setInt(3,ul.getSifPodvrste().intValue());
				else
				ps.setNull(3,Types.INTEGER);

				ps.setString(4,ul.getNapomena());

				if (ul.getSifProizvodjaca()!=null)
				{
					ps.setNull(5,Types.VARCHAR);
					ps.setInt(6,ul.getSifProizvodjaca().intValue());
				}
				else
				{
					ps.setString(5,ul.getModel()); // u ovom slucaju moze nesto pisati pod model...
					ps.setNull(6,Types.INTEGER);
				}

				ps.setInt(7,ul.getSifra().intValue());

				int kom=ps.executeUpdate();

				if (kom==0)
				 Logger.log("Lece.update nije uspio promjeniti podatke u retku?!?",null);

				String tablica2="DIOPTRIJA_LECA";
				OsobineLeceVO leca=ul.getLijeva();

				// NAPOMENA: tablica DIOPTRIJA_LECA, posto je ovisna o tablici izdane_lece, nema svoje updated, updated_by i sl. kolone

					upit =
								" update "+tablica2
								+" set "
                                                                +"    bazKriv=?,"    //1
								+"    dijametar=?,"  //2
								+ "    dsph=?,"	     //3
								+ "    ax=?,"	     //4
								+ "    specIzrada=?,"//5
								+ "          boja=?,"//6
								+ "          dcyl=?,"//7 -asabo- dodano 13.02.06.
								+ " updated=current_timestamp,"
								+ " updated_by="+GlavniFrame.getSifDjelatnika()
								+ "  where id=? and dl=?";	//primary key..
  				try {if (ps!=null) ps.close();} catch (SQLException e1){}

                                       ps=conn.prepareStatement(upit);
                                       PreparedStatement psLIns=null;

					//			unos desnog stakla
					ps.setString(1,leca.getBaznaKrivina());
					ps.setString(2,leca.getDijametar());
					ps.setString(3,leca.getDsph());
					ps.setString(4,leca.getAx());
					ps.setString(5,leca.getSpecIzrada());
					ps.setString(6,leca.getBoja());
					ps.setString(7,leca.getDcyl());

					ps.setInt(8,ul.getSifra().intValue());
					ps.setString(9,DAO.LIJEVO);

					kom=ps.executeUpdate();

					if (kom==0)
                                        {
                                            //krpanje buga, radi se o ovisno tablici, a insert nije uspijevao do sada
                                            // treba probati insertirati zapis, pa tek ako ni to ne ide zapisati u log gresku
                                           String upitIns =
                                                    " INSERT INTO "+tablica2
                                                    + "(dl,"					//1
                                                    +"bazKriv,"		//2
                                                    +"dijametar,"  //3
                                                    + "    dsph,"				//4
                                                    + "    ax,"		  		//5
                                                    + "    specIzrada,"	//6
                                                    + "    boja,"	  		//7
                                                    + "    dcyl,"       //8 -asabo- dodano 14.02.06.
                                                    + "      id," 				//9
                                                    + "   created," // 13.12.06. -asabo- dodano
                                                    + "   created_by"
                                                    + " ) values('"+DAO.LIJEVO+"',?,?,?,?,?,?,?,?,current_timestamp,"+GlavniFrame.getSifDjelatnika()+")";	//9 komada
                                                    psLIns=conn.prepareStatement(upitIns);
                                                    psLIns.setString(1,leca.getBaznaKrivina());
                                                    psLIns.setString(2,leca.getDijametar());
                                                    psLIns.setString(3,leca.getDsph());
                                                    psLIns.setString(4,leca.getAx());
                                                    psLIns.setString(5,leca.getSpecIzrada());
                                                    psLIns.setString(6,leca.getBoja());
                                                    psLIns.setString(7,leca.getDcyl());
                                                    psLIns.setInt(8,ul.getSifra().intValue());
                                                    try{
                                                   kom=psLIns.executeUpdate();
                                                   }
                                                   finally{
                                                   try{if(psLIns!=null) psLIns.close();}catch(SQLException sqle){}
                                                   }
                                        }//if kom==0
                                        if (kom==0)
					Logger.log("nije prosao update dioptrije leca (lijeva leca) sifra:"+ul.getSifra().intValue());

								// desno staklo
								leca=ul.getDesna();

							ps.setString(1,leca.getBaznaKrivina());
							ps.setString(2,leca.getDijametar());
							ps.setString(3,leca.getDsph());
							ps.setString(4,leca.getAx());
							ps.setString(5,leca.getSpecIzrada());
							ps.setString(6,leca.getBoja());
							ps.setString(7,leca.getDcyl());

							ps.setInt(8,ul.getSifra().intValue());
							ps.setString(9,DAO.DESNO);

							int kom2=ps.executeUpdate();
                                                        kom+=kom2;

                                                        if (kom2==0)
                                                        {
                                                            String upitIns =
                                                               " INSERT INTO "+tablica2
                                                               + "(dl,"					//1
                                                               +"bazKriv,"		//2
                                                               +"dijametar,"  //3
                                                               + "    dsph,"				//4
                                                               + "    ax,"		  		//5
                                                               + "    specIzrada,"	//6
                                                               + "    boja,"	  		//7
                                                               + "    dcyl,"       //8 -asabo- dodano 14.02.06.
                                                               + "      id," 				//9
                                                               + "   created," // 13.12.06. -asabo- dodano
                                                               + "   created_by"
                                                               + " ) values('"+DAO.DESNO+"',?,?,?,?,?,?,?,?,current_timestamp,"+GlavniFrame.getSifDjelatnika()+")";	//9 komada
                                                               psLIns=conn.prepareStatement(upitIns);
                                                               psLIns.setString(1,leca.getBaznaKrivina());
                                                               psLIns.setString(2,leca.getDijametar());
                                                               psLIns.setString(3,leca.getDsph());
                                                               psLIns.setString(4,leca.getAx());
                                                               psLIns.setString(5,leca.getSpecIzrada());
                                                               psLIns.setString(6,leca.getBoja());
                                                               psLIns.setString(7,leca.getDcyl());
                                                               psLIns.setInt(8,ul.getSifra().intValue());
                                                               try{
                                                                  kom2=psLIns.executeUpdate();
                                                                  kom+=kom2;
                                                                  }
                                                                  finally{
                                                                   try{if(psLIns!=null) psLIns.close();}catch(SQLException sqle){}
                                                                   }
                                                          }//if kom2==0

							if (kom2==0)
								Logger.log("nije prosao update dioptrije leca (desna leca) sifra:"+ul.getSifra().intValue());

				return kom==2;
				}
				//28.06.05. -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
				finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				DAOFactory.freeConnection(conn);
			}//finally
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public LeceVO read(Object kljuc) throws SQLException {
		Integer sifra = null;
			if (kljuc instanceof Integer){
				sifra=(Integer)kljuc;
			}


			String upit=
								"SELECT "
							+ "   id,"
							+ "		datum,"
							+ "		vrstaLece,"
							+ "		podvrstaLece,"
							+ "		napomena,"
							+ "      model,"
							+ "sif_proizvodjaca," //20.02.06. -asabo- dodano
							+ "    created,"
							+ "		updated,"
							+ "		created_by,"
							+ "   updated_by"
							+ " FROM "
							+ "	 "+tablica;

				if (sifra!=null) upit += " where id =  "+sifra.intValue();

				upit+=" order by id";

				ResultSet rs					=	null;
				ResultSet rs2					=	null;
				PreparedStatement ps  = null;
				Connection 			 con  = null;


				String tablica2="dioptrija_leca";
				String upit2="select dl,bazKriv,dijametar,dsph,ax,specIzrada,boja,dcyl from "+tablica2+" where id=? and dl=?";

				rs=DAOFactory.performQuery(upit);

			  LeceVO  lvo=null;

					try
					{
						con=DAOFactory.getConnection();

						if (con==null)
						throw new SQLException("Nema veze na bazu podataka Lece dao csc read");

						ps=con.prepareStatement(upit2);

						Integer sf=null;

						if (rs.next())
						 {
							lvo=constructLeca(rs);
							OsobineLeceVO tmp=null;

							//LIJEVA LECA
							ps.setInt(1,lvo.getSifra().intValue());
							ps.setString(2,DAO.LIJEVO);

							rs2=ps.executeQuery();

							if (rs2!=null && rs2.next())
							{
							tmp=constructOsobineLece(rs2,lvo);
							lvo.setLijeva(tmp);
							}

							try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}

							// DESNA LECA
							ps.setInt(1,lvo.getSifra().intValue());
							ps.setString(2,DAO.DESNO);

							rs2=ps.executeQuery();

							if (rs2!=null && rs2.next())
							{
							tmp=constructOsobineLece(rs2,lvo);
							lvo.setDesna(tmp);
							}

							try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}
							}//if
					}
					//28.06.05. -asabo- nema CATCH-anja ...
					finally
					{
					try{if (rs!=null) rs.close();}catch(SQLException sqle){}
					try{if(rs2!=null) rs2.close();}	catch(SQLException sqle){}
					try{if(ps!=null) ps.close(); ps=null;}	catch(SQLException sqle){}
					if (con!=null) DAOFactory.freeConnection(con);
				}
				return lvo;
	}//read

	public List<LeceVO> findAll(Object kljuc) throws SQLException {
		Integer sifra = null;

		List<LeceVO> lista=new ArrayList<LeceVO>();

		// zasada se nije pojavila neka potreba traziti izdane naocale po nekom kriteriju
		if (kljuc instanceof Integer){
			sifra=(Integer)kljuc;
		}

		String upit=
							"SELECT "
						+ "   id,"
						+ "		datum,"
						+ "		vrstaLece,"
						+ "		podvrstaLece,"
						+ "		napomena,"
						+ "      model,"
						+ "sif_proizvodjaca," //20.02.06. -asabo- dodano
						+ "    created,"
						+ "		updated,"
						+ "		created_by,"
						+ "   updated_by"
						+ " FROM "
						+ "	 "+tablica;

		if (sifra!=null) upit += " where id =  "+sifra.intValue();

		upit+=" order by id";

		ResultSet rs					=	null;
		ResultSet rs2					=	null;
		PreparedStatement ps  = null;
		Connection 			 con  = null;


			String tablica2="dioptrija_leca";
			String upit2="select dl,bazKriv,dijametar,dsph,ax,specIzrada,boja,dcyl from "+tablica2+" where id=? and dl=?";

			rs=DAOFactory.performQuery(upit);

			LeceVO  lvo=null;

			try
			{
				con=DAOFactory.getConnection();

				if (con==null)
				throw new SQLException("Nema veze na bazu podataka Lece dao csc findAll");

				ps=con.prepareStatement(upit2);

				
				while (rs.next())
				 {
					lvo=constructLeca(rs);
					OsobineLeceVO tmp=null;

					//LIJEVA LECA
					ps.setInt(1,lvo.getSifra().intValue());
					ps.setString(2,DAO.LIJEVO);

					rs2=ps.executeQuery();

					if (rs2!=null && rs2.next())
					{
					tmp=constructOsobineLece(rs2,lvo);
					lvo.setLijeva(tmp);
					}

					try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}

					// DESNA LECA
					ps.setInt(1,lvo.getSifra().intValue());
					ps.setString(2,DAO.DESNO);

					rs2=ps.executeQuery();

					if (rs2!=null && rs2.next())
					{
					tmp=constructOsobineLece(rs2,lvo);
					lvo.setDesna(tmp);
					}

					try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}
					lista.add(lvo);
					}//while
			}
			//30.06.05. -asabo- nema CATCH-anja ...
			finally
			{
			try{if (rs!=null) rs.close();}catch(SQLException sqle){} rs=null;
			try{if(rs2!=null) rs2.close();}	catch(SQLException sqle){} rs2=null;
			try{if(ps!=null) ps.close(); ps=null;}	catch(SQLException sqle){}  ps=null;
			if (con!=null) DAOFactory.freeConnection(con); con=null;
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
	public Object getValueAt(LeceVO vo, int kolonas) {
		return null;
	}
	public boolean setValueAt(LeceVO vo, Object vrijednost, int kolona) {
		return false;
	}
	public boolean isCellEditable(LeceVO vo, int kolona) {
		return false;
	}
	public int getRowCount() {
		return 0;
	}

	private LeceVO constructLeca(ResultSet rs) throws SQLException
	{
		LeceVO lvo=new LeceVO();
		Calendar c=Calendar.getInstance();

		lvo.setSifra(Integer.valueOf(rs.getInt("id")));

		c.setTimeInMillis(rs.getTimestamp("datum").getTime() );

		lvo.setDatum(c);

		lvo.setSifVrste(Integer.valueOf(rs.getInt("vrstaLece")));
		if (rs.wasNull()) lvo.setSifVrste(null);

		lvo.setSifPodvrste(Integer.valueOf(rs.getInt("podvrstaLece")));
		if (rs.wasNull()) lvo.setSifPodvrste(null);

		lvo.setNapomena(rs.getString("napomena"));

		int sfp=rs.getInt("sif_proizvodjaca");
		if (rs.wasNull())
		{
		lvo.setSifProizvodjaca(null);
		lvo.setModel(rs.getString("model"));
		}
		else
		{
			lvo.setSifProizvodjaca(Integer.valueOf(sfp));
			lvo.setModel(null); // u ovom slucaju model kao da ne postoji...
		}

		Timestamp tmp=rs.getTimestamp("updated");

		lvo.setCreated(rs.getTimestamp("created").getTime());
		lvo.setLastUpdated(tmp!=null?tmp.getTime():0L);

		lvo.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));
		lvo.setLastUpdatedBy(Integer.valueOf(rs.getInt("updated_by")));
		if (rs.wasNull()) lvo.setLastUpdatedBy(null);

		return lvo;
	}//constructLeca

	//lvo u ovoj situaciji nije potreban, ali je ostavljen za neke buduce potrebe..
	private OsobineLeceVO constructOsobineLece(ResultSet rs,LeceVO lvo)throws SQLException
	{
		if (rs==null) return null;

		OsobineLeceVO l=new OsobineLeceVO();

		l.setDl(rs.getString("dl"));
		l.setBaznaKrivina(rs.getString("bazKriv"));
		l.setDijametar(rs.getString("dijametar"));
		l.setDsph(rs.getString("dsph"));
		l.setAx(rs.getString("ax"));
		l.setSpecIzrada(rs.getString("specIzrada"));
		l.setBoja(rs.getString("boja"));
		l.setDcyl(rs.getString("dcyl"));

		return l;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}//constructOsobinaLece
}//klasa

