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

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.PorukaDAO;
import biz.sunce.opticar.vo.PorukaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;

/**
 * datum:2006.01.10
 * @author asabo
 *
 */
public final class Poruke implements PorukaDAO
{
//da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	 private final static String tablica="PORUKE";
	 private String[] kolone={"datum","tip poruke","primatelj","vrsta poruke","kreirano","poslano"};
	 private KlijentDAO klijenti;

	public void insert(Object objekt) throws SQLException
	{
		String upit;
		PorukaVO ul=(PorukaVO)objekt;

		if (ul==null)
		throw new SQLException("Insert "+tablica+", ulazna vrijednost je null!");

		int sifra=DAO.NEPOSTOJECA_SIFRA; // sifra unesenog retka

		upit="INSERT INTO "+tablica+" "+
		"(SIFRA,SIF_KLIJENTA,TIP_PORUKE,PORUKA,CREATED_BY,UPDATED_BY,CREATED,UPDATED,VRSTA_PORUKE,ADRESA)"+ //11.01.06. -asabo- dodano 23.01.06. -asabo- dodana adresa
		" "+
		" VALUES ("+
		(sifra=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica))+
		",?,?,?,?,null,current_timestamp,null,?,?)"; //31.01.06. -asabo- dodana kolona

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

				   ps.setInt(1,ul.getSifKlijenta());
				   ps.setInt(2,ul.getTipPoruke());
				ps.setString(3,ul.getPoruka());
				   ps.setInt(4,GlavniFrame.getSifDjelatnika());
				   ps.setInt(5,ul.getVrstaPoruke());
				ps.setString(6,ul.getAdresa());

				int kom=ps.executeUpdate();

				if (kom==1)
				{//zaista mini vjerojatno da insert uspije i da sifra bude -1...
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


	public boolean update(Object objekt) throws SQLException
	{
		PorukaVO ul=(PorukaVO)objekt;

						if (ul==null)
							throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");

						String upit =
											" update "+tablica+" set "
										+	"sif_klijenta=?,"			//1
										+	"	 tip_poruke=?,"			//2
										+	"			 poruka=?,"			//3
										+ "        sent=?,"     //4
										+ "vrsta_poruke=?,"		  //5 -asabo- 11.01.06. dodano
										+ "      adresa=?"      //6 -asabo- 23.01.06. dodano
										+ " updated=current_timestamp,"
										+ " updated_by="+GlavniFrame.getSifDjelatnika()
										+	" where sifra=?"; //7 primary key ...

						Connection conn 			= null;
						PreparedStatement ps 	= null;

						try
						{
							conn=DAOFactory.getConnection();

							ps=conn.prepareStatement(upit);

							ps.setInt(1,ul.getSifKlijenta());
							ps.setInt(2,ul.getTipPoruke());
							ps.setString(3,ul.getPoruka());

							if (ul.getPoslano()==0L)
							 ps.setNull(4,Types.TIMESTAMP);
							  else
							  {
							   Timestamp t=new Timestamp(ul.getPoslano());
							   ps.setTimestamp(4,t);
							  }

							ps.setInt(5,ul.getVrstaPoruke()); //11.01.06. -asabo- dodano

							ps.setString(6,ul.getAdresa()); // 23.01.06. -asabo- dodano

							ps.setInt(7,ul.getSifra().intValue());

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

	public void delete(Object kljuc) throws SQLException {
	}

	public ValueObject read(Object kljuc) throws SQLException
	{
		Integer sifra = null;
					if (kljuc instanceof Integer){
						sifra=(Integer)kljuc;
					}


					String upit=
										"SELECT "
									+ "   sifra,"
									+ "		sif_klijenta,"
									+ "		tip_poruke,"
									+ "		poruka,"
									+ "   created,"
									+ "		updated,"
									+ "		created_by,"
									+ "   updated_by,"
									+ "   sent,"
									+ "   vrsta_poruke,"
									+ "   adresa"
									+ " FROM "
									+ "	 "+tablica;

						if (sifra!=null) upit += " where sifra =  "+sifra.intValue();

						upit+=" order by sifra";

						ResultSet rs					=	null;

						rs=DAOFactory.performQuery(upit);

						PorukaVO pvo=null;

							try
							{
								if (rs.next())
								 {
									pvo=constructPoruka(rs);
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


	public List findAll(Object kljuc) throws SQLException
	{
		ArrayList list=new ArrayList(100);

		Integer sifra = null;
					if (kljuc instanceof Integer){
						sifra=(Integer)kljuc;
					}


					String upit=
										" SELECT "
									+ "   sifra,"
									+ "		sif_klijenta,"
									+ "		tip_poruke,"
									+ "		poruka,"
									+ "   created,"
									+ "		updated,"
									+ "		created_by,"
									+ "   updated_by,"
									+ "   sent,"
									+ "   vrsta_poruke,"
									+ "   adresa"
									+ " FROM "
									+ "	 "+tablica;

						if (sifra!=null) upit += " where sif_klijenta =  "+sifra.intValue();

						upit+=" order by created desc";

						ResultSet rs					=	null;

						rs=DAOFactory.performQuery(upit);

						PorukaVO pvo=null;

							try
							{
								while (rs.next())
								 {
									pvo=constructPoruka(rs);
									list.add(pvo);
								 }//while

							}
							// -asabo- nema CATCH-anja ...
							finally
							{
							try{if (rs!=null) rs.close();}catch(SQLException sqle){}
							}

		return list;
	 }//findAll

	private PorukaVO constructPoruka(ResultSet rs) throws SQLException
	{
		PorukaVO pvo=new PorukaVO();

		pvo.setSifra(Integer.valueOf(rs.getInt("sifra")));
		pvo.setSifKlijenta(rs.getInt("sif_klijenta"));
		pvo.setTipPoruke(rs.getInt("tip_poruke"));
		pvo.setPoruka(rs.getString("poruka"));

		pvo.setCreated(rs.getTimestamp("created").getTime());
		pvo.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));
		Timestamp t=rs.getTimestamp("updated");
		pvo.setLastUpdated(t!=null?t.getTime():0L);

		if (rs.wasNull()) pvo.setLastUpdated(0L);
		else
		pvo.setLastUpdatedBy(Integer.valueOf(rs.getInt("updated_by")));

		t=rs.getTimestamp("sent");
		pvo.setPoslano(t!=null?t.getTime():0L);

		//pozivnica za pregled, rodjendanska cestitka i sl...
		pvo.setVrstaPoruke(rs.getInt("vrsta_poruke"));

		pvo.setAdresa(rs.getString("adresa")); //23.01.06. -asabo- dodano

		return pvo;
	}//constructPoruka

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.PorukaVO");
	}

	public GUIEditor getGUIEditor() {
		return null;
	}

	public String getColumnName(int rb) {
		if (rb>=0 && rb<kolone.length)
		return kolone[rb];
		else return null;
	}

	public int getColumnCount() {
		return kolone.length;
	}

	public Class getColumnClass(int columnIndex) {
		try
			{

			switch(columnIndex){
			case 0:		return Class.forName("java.lang.String");
			case 1:		return Class.forName("java.lang.String");
			case 2:		return Class.forName("java.lang.String");
			case 3:		return Class.forName("java.lang.String");
			case 4:		return Class.forName("java.lang.String");
                        case 5:		return Class.forName("java.lang.String");
			default:	return null;
			}
			}
			catch(ClassNotFoundException cnfe)
			{
				Logger.fatal(tablica+" CSC - Integer ili String kao klase ne postoje?!?",cnfe);
				return null;
			}
	}

	public Object getValueAt(ValueObject vo, int kolona)
	{

		PorukaVO pvo=null;

		if (vo!=null && vo instanceof PorukaVO)
		pvo=(PorukaVO)vo;

		switch(kolona)
		{
		case 0:
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(pvo.getCreated());
		return Util.convertCalendarToString(c);
		case 1:
		return PorukaVO.NAZIV_TIPA_PORUKE[pvo.getTipPoruke()];

		case 2:
						try
						{
					   return this.getKlijenti().read(Integer.valueOf(pvo.getSifKlijenta()));
						}
						catch(SQLException sqle)
						{
							Logger.fatal("SQL iznimka kod ocitavanja klijenta Poruke CSC",sqle);
							return "?!?";
						}
		case 3:		return PorukaVO.NAZIV_VRSTE_PORUKE[pvo.getVrstaPoruke()];

                case 4:		if (pvo.getCreated()==0L) return "?!?";
                                 else
                                {
                                Calendar ca=Calendar.getInstance();
                                ca.setTimeInMillis(pvo.getCreated());
                                return Util.convertCalendarToString(ca,true);
                                }

		case 5:		if (pvo.getPoslano()==0L) return "nije poslano";
							else
								{
									Calendar ca=Calendar.getInstance();
									ca.setTimeInMillis(pvo.getPoslano());
									return Util.convertCalendarToString(ca,true);
								}
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
		try
		{
		return this.findAll(null).size();
		}
		catch(SQLException sqle)
		{
			Logger.fatal("SQL iznimka kod ocitavanja broja redaka Poruke CSC",sqle);
			return 0;
		}
	}//getRowCount

  // kako bismo izbjegli cesto pozivanje DaoFactory-ja ovo je trebalo biti lijepse rjesenje
	private final KlijentDAO getKlijenti()
	{
  	if (klijenti==null)
	   klijenti=DAOFactory.getInstance().getKlijenti();

	   return klijenti;
	}//getKlijenti

}//klasa
